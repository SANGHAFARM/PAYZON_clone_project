package erp.payroll.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.payroll.dao.PayrollPayslipDao;
import erp.payroll.dto.PayrollPayslipPage.PayrollPayslipCompany;
import erp.payroll.dto.PayrollPayslipPage.PayrollPayslipEmployee;
import erp.payroll.dto.PayrollPayslipPage;
import erp.payroll.dto.PayrollRegisterColumn;
import erp.payroll.model.PayrollRun;
import erp.settings.dao.CompanyDao;
import erp.settings.model.Company;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 급여 회차와 사원별 급여명세서를 구성한다.
// 급여명세서 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 給与明細書の業務ルールとデータ変更トランザクションを処理する。
public class PayrollPayslipService {

	private PayrollPayslipDao payslipDao = new PayrollPayslipDao();
	private CompanyDao companyDao = CompanyDao.getInstance();

	// 요청 조건에 맞는 급여명세서 화면 데이터를 구성하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// リクエスト条件に合う給与明細書の画面データを構成して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public PayrollPayslipPage getPage(String year, String month, String sequence, Integer employeeId,
			String keyword) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			List<PayrollRun> runs = payslipDao.selectRuns(conn, year, month, sequence);
			List<PayrollPayslipEmployee> employees = payslipDao.selectEmployees(conn, year, month, sequence, keyword);
			payslipDao.fillAmounts(conn, year, month, sequence, employees);
			payslipDao.fillDailyPayments(conn, year, month, sequence, employees);
			PayrollPayslipEmployee selectedEmployee = selectEmployee(employees, employeeId);
			List<PayrollRegisterColumn> paymentItems = new ArrayList<>();
			List<PayrollRegisterColumn> deductionItems = new ArrayList<>();
			if (selectedEmployee != null) {
				paymentItems = payslipDao.selectPaymentColumns(conn, year, month, sequence,
						selectedEmployee.getEmployeeId());
				if (selectedEmployee.getPaymentAmounts().containsKey(-1)) {
					paymentItems.add(0, new PayrollRegisterColumn(-1, "일용직 급여"));
				}
				deductionItems = payslipDao.selectDeductionColumns(conn, year, month, sequence,
						selectedEmployee.getEmployeeId());
				fillEmptyAmounts(selectedEmployee, paymentItems, deductionItems);
			}

			PayrollPayslipPage page = new PayrollPayslipPage();
			if (!runs.isEmpty()) {
				PayrollRun run = runs.get(0);
				page.setCalculationStart(run.getCalcStartDate());
				page.setCalculationEnd(run.getCalcEndDate());
				page.setPaymentDate(run.getPayDate());
			}
			page.setEmployees(employees);
			page.setSelectedEmployee(selectedEmployee);
			page.setPaymentItems(paymentItems);
			page.setDeductionItems(deductionItems);
			page.setCompany(makeCompany(companyDao.selectById(conn, 1)));
			return page;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 급여명세서 처리에 필요한 사원 데이터를 조회하여 반환한다.
	// 조회 전용 Connection으로 관련 DAO 결과를 조합하며 데이터 변경이 없으므로 별도의 commit이나 rollback을 수행하지 않는다.
	// 給与明細書処理に必要な社員データを照会して返す。
	// 照会専用Connectionで関連DAOの結果を組み合わせ、データ変更がないため個別のcommitやrollbackは実行しない。
	private PayrollPayslipEmployee selectEmployee(List<PayrollPayslipEmployee> employees, Integer employeeId) {
		// 최초 진입 시 첫 사원의 급여명세서를 자동 표시하지 않는다.
		// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
		if (employees.isEmpty() || employeeId == null) {
			return null;
		}
		for (PayrollPayslipEmployee employee : employees) {
			if (employee.getEmployeeId() == employeeId) {
				return employee;
			}
		}
		return null;
	}

	// 누락된 빈 값금액 목록 값을 기본값으로 채워 화면 계산과 합계 처리를 안정화한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 不足している空値金額一覧の値を初期値で補い、画面計算と合計処理を安定させる。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	private void fillEmptyAmounts(PayrollPayslipEmployee employee, List<PayrollRegisterColumn> paymentItems,
			List<PayrollRegisterColumn> deductionItems) {
		for (PayrollRegisterColumn item : paymentItems) {
			employee.getPaymentAmounts().putIfAbsent(item.getItemId(), 0L);
			employee.getPaymentCalculations().putIfAbsent(item.getItemId(), "-");
		}
		for (PayrollRegisterColumn item : deductionItems) {
			employee.getDeductionAmounts().putIfAbsent(item.getItemId(), 0L);
			employee.getDeductionCalculations().putIfAbsent(item.getItemId(), "-");
		}
	}

	// 조회값과 입력값을 조합하여 사업장 처리 데이터를 구성한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 照会値と入力値を組み合わせて事業所の処理データを構成する。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	private PayrollPayslipCompany makeCompany(Company company) {
		PayrollPayslipCompany result = new PayrollPayslipCompany();
		if (company != null) {
			result.setCompanyName(company.getCmpnName());
			result.setRepresentativeName(company.getCeoName());
			result.setLogoUrl(company.getLogoImgPath());
			result.setStampUrl(company.getStampImgPath());
		}
		return result;
	}
}
