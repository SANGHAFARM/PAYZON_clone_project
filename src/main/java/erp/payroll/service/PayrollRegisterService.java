package erp.payroll.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import erp.payroll.dao.PayrollRegisterDao;
import erp.payroll.dto.PayrollRegisterColumn;
import erp.payroll.dto.PayrollRegisterPage.PayrollEmploymentTypeOption;
import erp.payroll.dto.PayrollRegisterPage.PayrollRegisterDetailPage;
import erp.payroll.dto.PayrollRegisterPage.PayrollRegisterEmployee;
import erp.payroll.dto.PayrollRegisterPage.PayrollRegisterItem;
import erp.payroll.dto.PayrollRegisterPage.PayrollRegisterListPage;
import erp.payroll.dto.PayrollRegisterPage.PayrollRegisterPageInfo;
import erp.payroll.dto.PayrollRegisterPage.PayrollRegisterTotals;
import erp.settings.dao.DepartmentDao;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 급여대장 목록과 사원별 상세 집계를 구성한다.
// 급여등록 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 給与登録の業務ルールとデータ変更トランザクションを処理する。
public class PayrollRegisterService {

	private static final int PAGE_SIZE = 10;

	private PayrollRegisterDao registerDao = new PayrollRegisterDao();
	private DepartmentDao departmentDao = DepartmentDao.getInstance();

	// 요청 조건에 맞는 급여등록 목록를 구성하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// リクエスト条件に合う給与登録の一覧を構成して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public PayrollRegisterListPage getList(String year, int pageNumber) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			int count = registerDao.countRuns(conn, year);
			int totalPages = count == 0 ? 0 : (count + PAGE_SIZE - 1) / PAGE_SIZE;
			int currentPage = totalPages == 0 ? 0 : Math.min(Math.max(pageNumber, 0), totalPages - 1);
			List<PayrollRegisterItem> registers = registerDao.selectRuns(conn, year, currentPage * PAGE_SIZE,
					PAGE_SIZE);
			return new PayrollRegisterListPage(registers, calculateListTotals(registers),
					new PayrollRegisterPageInfo(currentPage, totalPages));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 요청 조건에 맞는 급여등록 상세정보를 구성하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// リクエスト条件に合う給与登録の詳細情報を構成して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public PayrollRegisterDetailPage getDetail(int runId, String employmentType, Integer departmentId,
			String incomeType) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			PayrollRegisterItem register = registerDao.selectRunById(conn, runId);
			if (register == null) {
				throw new IllegalArgumentException("조회할 급여대장이 없습니다.");
			}
			List<PayrollRegisterColumn> paymentItems;
			if ("2".equals(register.getIncomeType())) {
				paymentItems = new ArrayList<>();
				paymentItems.add(new PayrollRegisterColumn(-1, "일용직 급여"));
			} else {
				paymentItems = registerDao.selectPayColumns(conn, runId);
			}
			List<PayrollRegisterColumn> deductionItems = registerDao.selectDeductColumns(conn, runId);
			List<PayrollRegisterEmployee> employees = new ArrayList<>();
			if (matchesIncomeType(register.getIncomeType(), incomeType)) {
				employees = registerDao.selectEmployees(conn, runId, emptyToNull(employmentType), departmentId);
			}
			registerDao.fillEntryAmounts(conn, runId, employees);
			if ("2".equals(register.getIncomeType())) {
				registerDao.fillDailyPayments(conn, runId, employees);
			}
			fillEmptyAmounts(employees, paymentItems, deductionItems);

			PayrollRegisterDetailPage page = new PayrollRegisterDetailPage();
			page.setRegister(register);
			page.setPaymentItems(paymentItems);
			page.setDeductionItems(deductionItems);
			page.setEmployees(employees);
			page.setTotals(calculateDetailTotals(employees));
			page.setDepartments(departmentDao.selectAll(conn));
			return page;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 선택되거나 식별된 급여등록 데이터를 삭제하고 관련 상태를 정리한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 選択または識別された給与登録データを削除し、関連状態を整理する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void delete(int runId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			registerDao.deleteRun(conn, runId);
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 급여등록 처리에 필요한 고용Types를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 給与登録処理に必要な雇用Typesを照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<PayrollEmploymentTypeOption> getEmploymentTypes() {
		List<PayrollEmploymentTypeOption> types = new ArrayList<>();
		types.add(new PayrollEmploymentTypeOption("정규직", "정규직"));
		types.add(new PayrollEmploymentTypeOption("계약직", "계약직"));
		types.add(new PayrollEmploymentTypeOption("임시직", "임시직"));
		types.add(new PayrollEmploymentTypeOption("파견직", "파견직"));
		types.add(new PayrollEmploymentTypeOption("위촉직", "위촉직"));
		types.add(new PayrollEmploymentTypeOption("일용직", "일용직"));
		return types;
	}

	// 조회된 금액과 업무 규칙을 이용해 목록합계정보 계산 결과를 생성한다.
	// 입력금액과 기간·요율·절사단위를 적용하고 계산 중 발생할 수 있는 NULL이나 음수 결과를 안전하게 보정한다.
	// 照会した金額と業務ルールを使用して一覧合計情報の計算結果を生成する。
	// 入力金額と期間・料率・端数処理単位を適用し、計算中に発生し得るNULLや負数結果を安全に補正する。
	private PayrollRegisterTotals calculateListTotals(List<PayrollRegisterItem> registers) {
		PayrollRegisterTotals totals = new PayrollRegisterTotals();
		long payment = 0;
		long deduction = 0;
		for (PayrollRegisterItem register : registers) {
			payment += register.getTotalPayment();
			deduction += register.getTotalDeduction();
		}
		totals.setTotalPayment(payment);
		totals.setTotalDeduction(deduction);
		return totals;
	}

	// 조회된 금액과 업무 규칙을 이용해 상세정보합계정보 계산 결과를 생성한다.
	// 입력금액과 기간·요율·절사단위를 적용하고 계산 중 발생할 수 있는 NULL이나 음수 결과를 안전하게 보정한다.
	// 照会した金額と業務ルールを使用して詳細情報合計情報の計算結果を生成する。
	// 入力金額と期間・料率・端数処理単位を適用し、計算中に発生し得るNULLや負数結果を安全に補正する。
	private PayrollRegisterTotals calculateDetailTotals(List<PayrollRegisterEmployee> employees) {
		PayrollRegisterTotals totals = new PayrollRegisterTotals();
		long payment = 0;
		long deduction = 0;
		for (PayrollRegisterEmployee employee : employees) {
			addAmounts(totals.getPaymentAmounts(), employee.getPaymentAmounts());
			addAmounts(totals.getDeductionAmounts(), employee.getDeductionAmounts());
			payment += employee.getTotalPayment();
			deduction += employee.getTotalDeduction();
		}
		totals.setTotalPayment(payment);
		totals.setTotalDeduction(deduction);
		return totals;
	}

	// 급여등록 처리에 사용할 금액 목록 데이터나 객체를 생성한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 給与登録処理で使用する金額一覧データまたはオブジェクトを生成する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	private void addAmounts(Map<Integer, Long> totals, Map<Integer, Long> amounts) {
		for (Map.Entry<Integer, Long> entry : amounts.entrySet()) {
			long current = totals.containsKey(entry.getKey()) ? totals.get(entry.getKey()) : 0;
			totals.put(entry.getKey(), current + entry.getValue());
		}
	}

	// 누락된 빈 값금액 목록 값을 기본값으로 채워 화면 계산과 합계 처리를 안정화한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 不足している空値金額一覧の値を初期値で補い、画面計算と合計処理を安定させる。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	private void fillEmptyAmounts(List<PayrollRegisterEmployee> employees,
			List<PayrollRegisterColumn> paymentItems, List<PayrollRegisterColumn> deductionItems) {
		for (PayrollRegisterEmployee employee : employees) {
			for (PayrollRegisterColumn item : paymentItems) {
				employee.getPaymentAmounts().putIfAbsent(item.getItemId(), 0L);
			}
			for (PayrollRegisterColumn item : deductionItems) {
				employee.getDeductionAmounts().putIfAbsent(item.getItemId(), 0L);
			}
		}
	}

	// 조회된 데이터가 matches소득구분 검색조건을 충족하는지 확인한다.
	// 필수값과 상태 조합을 확인해 잘못된 데이터가 DAO 또는 후속 계산으로 전달되지 않도록 차단한다.
	// 照会したデータがmatches所得区分の検索条件を満たすか確認する。
	// 必須値と状態の組み合わせを確認し、不正データがDAOまたは後続計算へ渡らないよう遮断する。
	private boolean matchesIncomeType(String runIncomeType, String selectedIncomeType) {
		String selected = emptyToNull(selectedIncomeType);
		if (selected == null) {
			return true;
		} else if ("0".equals(runIncomeType)) {
			return "WORK".equals(selected);
		} else if ("1".equals(runIncomeType)) {
			return "BUSINESS".equals(selected);
		}
		return "DAILY".equals(selected);
	}

	// 요청 문자열을 정리하고 빈 값To빈 값 처리에 필요한 안전한 값으로 변환한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// リクエスト文字列を整え、空値To空値処理に必要な安全な値へ変換する。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	private String emptyToNull(String value) {
		return value == null || value.trim().isEmpty() ? null : value.trim();
	}
}
