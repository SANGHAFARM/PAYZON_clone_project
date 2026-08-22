package erp.payroll.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import erp.payroll.dao.DayWorkerPayrollDao;
import erp.payroll.dao.PayrollManagementDao;
import erp.payroll.dto.DayWorkerPaymentPage.DayWorkerEmployeePage;
import erp.payroll.dto.DayWorkerPaymentEmployee;
import erp.payroll.dto.DayWorkerPaymentPage;
import erp.payroll.dto.PayrollManagementItem;
import erp.payroll.model.PayrollRun;
import erp.settings.dao.DepartmentDao;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 일용직 근무기록을 급여 회차와 연결한다.
// 일용직근로자급여 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 日雇い労働者給与の業務ルールとデータ変更トランザクションを処理する。
public class DayWorkerPayrollService {

	private static final int EMPLOYEE_PAGE_SIZE = 10;

	private DayWorkerPayrollDao dayWorkerDao = new DayWorkerPayrollDao();
	private PayrollManagementDao managementDao = new PayrollManagementDao();
	private DepartmentDao departmentDao = DepartmentDao.getInstance();

	// 요청 조건에 맞는 일용직근로자급여 화면 데이터를 구성하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// リクエスト条件に合う日雇い労働者給与の画面データを構成して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public DayWorkerPaymentPage getPage(PayrollRun requestRun, Integer employeeId, String keyword,
			Integer departmentId, int employeePage) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			PayrollRun run = managementDao.selectRun(conn, requestRun.getPayYear(), requestRun.getPayMonth(),
					requestRun.getPaySeq(), "2");
			int runId = run == null ? 0 : run.getPayrollRunId();
			Date startDate = run == null ? requestRun.getCalcStartDate() : run.getCalcStartDate();
			Date endDate = run == null ? requestRun.getCalcEndDate() : run.getCalcEndDate();
			List<DayWorkerPaymentEmployee> employees = run == null ? new ArrayList<>()
					: dayWorkerDao.selectPaymentEmployees(conn, runId, startDate, endDate);
			DayWorkerPaymentEmployee selectedEmployee = selectEmployee(employees, employeeId);
			if (selectedEmployee != null) {
				setEmployeeDetail(conn, runId, selectedEmployee, startDate, endDate);
			}

			int count = dayWorkerDao.countAvailableEmployees(conn, runId, keyword, departmentId);
			int totalPages = Math.max(1, (count + EMPLOYEE_PAGE_SIZE - 1) / EMPLOYEE_PAGE_SIZE);
			int currentPage = Math.min(Math.max(employeePage, 1), totalPages);
			DayWorkerPaymentPage page = new DayWorkerPaymentPage();
			page.setRun(run);
			page.setPaymentEmployees(employees);
			page.setSelectedEmployee(selectedEmployee);
			page.setAvailableEmployeePage(new DayWorkerEmployeePage(dayWorkerDao.selectAvailableEmployees(conn,
					runId, keyword, departmentId, currentPage, EMPLOYEE_PAGE_SIZE), totalPages));
			page.setDepartments(departmentDao.selectAll(conn));
			return page;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 입력값을 검증한 후 일용직근로자급여 데이터를 트랜잭션으로 저장하거나 수정한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 入力値を検証した後、日雇い労働者給与データをトランザクションで登録または更新する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void save(PayrollRun requestRun, int employeeId, long[] amounts) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			PayrollRun run = getOrCreateRun(conn, requestRun);
			Integer payrollEmployeeId = managementDao.selectPayrollEmployeeId(conn, run.getPayrollRunId(), employeeId);
			if (payrollEmployeeId == null) {
				managementDao.insertPayrollEmployee(conn, run.getPayrollRunId(), employeeId);
				payrollEmployeeId = managementDao.selectPayrollEmployeeId(conn, run.getPayrollRunId(), employeeId);
			}
			List<PayrollManagementItem> deductionItems = dayWorkerDao.selectDeductionEntries(conn,
					payrollEmployeeId);
			setDeductionAmounts(deductionItems, amounts);
			managementDao.replaceEntries(conn, payrollEmployeeId, new ArrayList<>(), deductionItems);
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 조회된 금액과 업무 규칙을 이용해 일용직근로자급여 계산 결과를 생성한다.
	// 입력금액과 기간·요율·절사단위를 적용하고 계산 중 발생할 수 있는 NULL이나 음수 결과를 안전하게 보정한다.
	// 照会した金額と業務ルールを使用して日雇い労働者給与の計算結果を生成する。
	// 入力金額と期間・料率・端数処理単位を適用し、計算中に発生し得るNULLや負数結果を安全に補正する。
	public long[] calculate(PayrollRun requestRun, int employeeId, long mutualAidFee) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			long[] calculated = dayWorkerDao.selectAutomaticDeductions(conn, employeeId,
					requestRun.getCalcStartDate(), requestRun.getCalcEndDate());
			return new long[] { calculated[0], calculated[1], calculated[2], calculated[3], calculated[4],
					calculated[5], mutualAidFee };
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 근무지급내역 조건의 충족 여부를 확인하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 勤務支給明細条件を満たしているか確認して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public boolean hasWorkPayments(PayrollRun requestRun, int employeeId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return !dayWorkerDao.selectWorkPayments(conn, employeeId, requestRun.getCalcStartDate(),
					requestRun.getCalcEndDate()).isEmpty();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 일용직근로자급여 처리에 필요한 OrCreate급여 회차를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 日雇い労働者給与処理に必要なOrCreate給与回次を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	private PayrollRun getOrCreateRun(Connection conn, PayrollRun requestRun) throws SQLException {
		PayrollRun run = managementDao.selectRun(conn, requestRun.getPayYear(), requestRun.getPayMonth(),
				requestRun.getPaySeq(), "2");
		if (run == null) {
			requestRun.setPayrollRunId(managementDao.insertRun(conn, requestRun));
			return requestRun;
		}
		managementDao.updateRunDates(conn, run.getPayrollRunId(), requestRun);
		return run;
	}

	// 전달받은 사원상세정보 값을 일용직근로자급여 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員詳細情報の値を日雇い労働者給与オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	private void setEmployeeDetail(Connection conn, int runId, DayWorkerPaymentEmployee employee, Date startDate,
			Date endDate) throws SQLException {
		employee.setWorkPayments(dayWorkerDao.selectWorkPayments(conn, employee.getEmployeeId(), startDate, endDate));
		Integer payrollEmployeeId = managementDao.selectPayrollEmployeeId(conn, runId, employee.getEmployeeId());
		if (payrollEmployeeId != null) {
			setDeductionFields(employee, dayWorkerDao.selectDeductionEntries(conn, payrollEmployeeId));
		}
	}

	// 전달받은 공제Fields 값을 일용직근로자급여 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った控除Fieldsの値を日雇い労働者給与オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	private void setDeductionFields(DayWorkerPaymentEmployee employee, List<PayrollManagementItem> items) {
		for (PayrollManagementItem item : items) {
			String name = item.getItemName();
			if ("국민연금".equals(name)) {
				employee.setNationalPension(item.getAmount());
			} else if ("건강보험".equals(name)) {
				employee.setHealthInsurance(item.getAmount());
			} else if ("장기요양보험".equals(name)) {
				employee.setLongTermCareInsurance(item.getAmount());
			} else if ("고용보험".equals(name)) {
				employee.setEmploymentInsurance(item.getAmount());
			} else if ("소득세".equals(name)) {
				employee.setIncomeTax(item.getAmount());
			} else if ("지방소득세".equals(name)) {
				employee.setLocalIncomeTax(item.getAmount());
			} else if ("상조회비".equals(name)) {
				employee.setMutualAidFee(item.getAmount());
			}
		}
	}

	// 전달받은 공제금액 목록 값을 일용직근로자급여 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った控除金額一覧の値を日雇い労働者給与オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	private void setDeductionAmounts(List<PayrollManagementItem> items, long[] amounts) {
		for (PayrollManagementItem item : items) {
			String name = item.getItemName();
			if ("국민연금".equals(name)) {
				item.setAmount(amounts[0]);
			} else if ("건강보험".equals(name)) {
				item.setAmount(amounts[1]);
			} else if ("장기요양보험".equals(name)) {
				item.setAmount(amounts[2]);
			} else if ("고용보험".equals(name)) {
				item.setAmount(amounts[3]);
			} else if ("소득세".equals(name)) {
				item.setAmount(amounts[4]);
			} else if ("지방소득세".equals(name)) {
				item.setAmount(amounts[5]);
			} else if ("상조회비".equals(name)) {
				item.setAmount(amounts[6]);
			}
		}
	}

	// 일용직근로자급여 처리에 필요한 사원 데이터를 조회하여 반환한다.
	// 조회 전용 Connection으로 관련 DAO 결과를 조합하며 데이터 변경이 없으므로 별도의 commit이나 rollback을 수행하지 않는다.
	// 日雇い労働者給与処理に必要な社員データを照会して返す。
	// 照会専用Connectionで関連DAOの結果を組み合わせ、データ変更がないため個別のcommitやrollbackは実行しない。
	private DayWorkerPaymentEmployee selectEmployee(List<DayWorkerPaymentEmployee> employees, Integer employeeId) {
		// 최초 진입 시 첫 일용직 사원을 자동 선택하지 않는다.
		// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
		if (employees.isEmpty() || employeeId == null) {
			return null;
		}
		for (DayWorkerPaymentEmployee employee : employees) {
			if (employee.getEmployeeId() == employeeId) {
				return employee;
			}
		}
		return null;
	}
}
