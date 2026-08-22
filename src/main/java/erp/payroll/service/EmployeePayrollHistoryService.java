package erp.payroll.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import erp.payroll.dao.EmployeePayrollHistoryDao;
import erp.payroll.dto.EmployeePayrollHistoryPage.EmployeePayrollHistoryEmployee;
import erp.payroll.dto.EmployeePayrollHistoryPage.EmployeePayrollHistoryItem;
import erp.payroll.dto.EmployeePayrollHistoryPage;
import erp.payroll.dto.EmployeePayrollHistoryPage.EmployeePayrollHistoryPageInfo;
import erp.payroll.dto.EmployeePayrollHistoryPage.EmployeePayrollHistoryTotal;
import erp.settings.dao.DepartmentDao;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 사원별 급여내역 화면에 필요한 조회 결과를 구성한다.
// 사원급여이력 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 社員給与履歴の業務ルールとデータ変更トランザクションを処理する。
public class EmployeePayrollHistoryService {

	private static final int PAGE_SIZE = 10;

	private EmployeePayrollHistoryDao historyDao = new EmployeePayrollHistoryDao();
	private DepartmentDao departmentDao = DepartmentDao.getInstance();

	// 요청 조건에 맞는 사원급여이력 화면 데이터를 구성하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// リクエスト条件に合う社員給与履歴の画面データを構成して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public EmployeePayrollHistoryPage getPage(Integer employeeId, String startMonth, String endMonth,
			int pageNumber, String keyword, Integer departmentId, String status, boolean loadHistories) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			List<EmployeePayrollHistoryEmployee> employees = historyDao.selectEmployees(conn, keyword,
					departmentId, status);
			EmployeePayrollHistoryEmployee selectedEmployee = findSelectedEmployee(conn, employeeId, employees);
			List<EmployeePayrollHistoryItem> allHistories = new ArrayList<>();
			if (loadHistories && selectedEmployee != null) {
				allHistories = combineHistories(historyDao.selectHistories(conn,
						selectedEmployee.getEmployeeId(), startMonth, endMonth));
			}

			int totalPages = allHistories.isEmpty() ? 0 : (allHistories.size() + PAGE_SIZE - 1) / PAGE_SIZE;
			int currentPage = totalPages == 0 ? 1 : Math.min(Math.max(pageNumber, 1), totalPages);
			int fromIndex = totalPages == 0 ? 0 : (currentPage - 1) * PAGE_SIZE;
			int toIndex = Math.min(fromIndex + PAGE_SIZE, allHistories.size());

			EmployeePayrollHistoryPage page = new EmployeePayrollHistoryPage();
			page.setSelectedEmployee(selectedEmployee);
			page.setEmployees(employees);
			page.setDepartments(departmentDao.selectAll(conn));
			page.setHistories(new ArrayList<>(allHistories.subList(fromIndex, toIndex)));
			page.setTotal(calculateTotal(allHistories));
			page.setPageInfo(new EmployeePayrollHistoryPageInfo(currentPage, totalPages));
			return page;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 사원급여이력 처리에 필요한 Selected사원 데이터를 조회하여 반환한다.
	// 조회 전용 Connection으로 관련 DAO 결과를 조합하며 데이터 변경이 없으므로 별도의 commit이나 rollback을 수행하지 않는다.
	// 社員給与履歴処理に必要なSelected社員データを照会して返す。
	// 照会専用Connectionで関連DAOの結果を組み合わせ、データ変更がないため個別のcommitやrollbackは実行しない。
	private EmployeePayrollHistoryEmployee findSelectedEmployee(Connection conn, Integer employeeId,
			List<EmployeePayrollHistoryEmployee> employees) throws SQLException {
		if (employeeId != null) {
			return historyDao.selectEmployee(conn, employeeId);
		}
		return null;
	}

	// 같은 지급월과 차수의 소득 유형별 결과를 한 줄로 합친다.
	// 여러 급여 회차에서 조회한 사원별 내역을 하나의 연속된 급여이력으로 합친다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 複数の給与回次から照会した社員別明細を一つの連続した給与履歴へ統合する。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	private List<EmployeePayrollHistoryItem> combineHistories(List<EmployeePayrollHistoryItem> source) {
		Map<String, EmployeePayrollHistoryItem> combined = new LinkedHashMap<>();
		for (EmployeePayrollHistoryItem item : source) {
			String key = item.getPaymentMonth() + "-" + item.getPaymentRound();
			EmployeePayrollHistoryItem target = combined.get(key);
			if (target == null) {
				target = new EmployeePayrollHistoryItem();
				target.setPaymentMonth(item.getPaymentMonth());
				target.setPaymentRound(item.getPaymentRound());
				combined.put(key, target);
			}
			add(target, item);
		}
		return new ArrayList<>(combined.values());
	}

	// 조회된 금액과 업무 규칙을 이용해 합계 계산 결과를 생성한다.
	// 입력금액과 기간·요율·절사단위를 적용하고 계산 중 발생할 수 있는 NULL이나 음수 결과를 안전하게 보정한다.
	// 照会した金額と業務ルールを使用して合計の計算結果を生成する。
	// 入力金額と期間・料率・端数処理単位を適用し、計算中に発生し得るNULLや負数結果を安全に補正する。
	private EmployeePayrollHistoryTotal calculateTotal(List<EmployeePayrollHistoryItem> histories) {
		EmployeePayrollHistoryTotal total = new EmployeePayrollHistoryTotal();
		for (EmployeePayrollHistoryItem history : histories) {
			total.setStandardMonthlyIncome(total.getStandardMonthlyIncome() + history.getStandardMonthlyIncome());
			total.setTotalPayment(total.getTotalPayment() + history.getTotalPayment());
			total.setTotalDeduction(total.getTotalDeduction() + history.getTotalDeduction());
			total.setNationalPension(total.getNationalPension() + history.getNationalPension());
			total.setHealthInsurance(total.getHealthInsurance() + history.getHealthInsurance());
			total.setLongTermCareInsurance(total.getLongTermCareInsurance() + history.getLongTermCareInsurance());
			total.setEmploymentInsurance(total.getEmploymentInsurance() + history.getEmploymentInsurance());
			total.setIncomeTax(total.getIncomeTax() + history.getIncomeTax());
			total.setLocalIncomeTax(total.getLocalIncomeTax() + history.getLocalIncomeTax());
		}
		return total;
	}

	// 사원급여이력 처리에 사용할 사원급여이력 데이터나 객체를 생성한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 社員給与履歴処理で使用する社員給与履歴データまたはオブジェクトを生成する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	private void add(EmployeePayrollHistoryItem target, EmployeePayrollHistoryItem value) {
		target.setStandardMonthlyIncome(Math.max(target.getStandardMonthlyIncome(), value.getStandardMonthlyIncome()));
		target.setTotalPayment(target.getTotalPayment() + value.getTotalPayment());
		target.setTotalDeduction(target.getTotalDeduction() + value.getTotalDeduction());
		target.setNationalPension(target.getNationalPension() + value.getNationalPension());
		target.setHealthInsurance(target.getHealthInsurance() + value.getHealthInsurance());
		target.setLongTermCareInsurance(target.getLongTermCareInsurance() + value.getLongTermCareInsurance());
		target.setEmploymentInsurance(target.getEmploymentInsurance() + value.getEmploymentInsurance());
		target.setIncomeTax(target.getIncomeTax() + value.getIncomeTax());
		target.setLocalIncomeTax(target.getLocalIncomeTax() + value.getLocalIncomeTax());
	}
}
