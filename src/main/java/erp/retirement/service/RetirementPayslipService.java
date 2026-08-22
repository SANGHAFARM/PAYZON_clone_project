package erp.retirement.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import erp.employees.dao.EmployeeDao;
import erp.employees.model.Employee;
import erp.retirement.dao.RetirementCalculationDao;
import erp.retirement.dao.RetirementIncomeEntryDao;
import erp.retirement.dto.RetirementBenefitListItem;
import erp.retirement.dto.RetirementPayslip;
import erp.retirement.model.RetirementCalculation;
import erp.retirement.model.RetirementIncomeEntry;
import erp.settings.dao.CompanyDao;
import erp.settings.dao.DepartmentDao;
import erp.settings.dao.JobPositionDao;
import erp.settings.model.Company;
import erp.settings.model.Department;
import erp.settings.model.JobPosition;
import jdbc.connection.ConnectionProvider;

// 저장된 퇴직급여와 산정자료를 명세서 한 장으로 조합한다.
// 퇴직급여명세서 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 退職給与明細書の業務ルールとデータ変更トランザクションを処理する。
public class RetirementPayslipService {

	private final RetirementCalculationDao calculationDao = RetirementCalculationDao.getInstance();

	// 퇴직급여명세서 처리에 필요한 지급연도 목록를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 退職給与明細書処理に必要な支給年度一覧を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<Integer> getPaymentYears() {
		int currentYear = LocalDate.now().getYear();
		List<Integer> years = new ArrayList<>();
		for (int year = currentYear + 1; year >= currentYear - 5; year--) {
			years.add(year);
		}
		return years;
	}

	// 퇴직급여명세서 처리에 필요한 데이터를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 退職給与明細書処理に必要なデータを照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public PayslipData getData(int year, String keyword, Integer calculationId) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			PayslipData data = new PayslipData();
			data.items = calculationDao.selectBenefitList(conn, year);
			filterByEmployeeName(data.items, keyword);
			data.company = CompanyDao.getInstance().selectById(conn, 1);

			// 최초 진입 시 첫 명세서를 자동 선택하지 않는다.
			// 利用者が選択した値を確定した場合のみ元の画面へ反映し、取消時は既存状態を維持する。
			Integer selectedId = containsCalculation(data.items, calculationId) ? calculationId : null;
			if (selectedId != null) {
				data.selected = makePayslip(conn, selectedId);
			}
			return data;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// 조회된 데이터가 요청 필터By사원명칭 검색조건을 충족하는지 확인한다.
	// 필수값과 상태 조합을 확인해 잘못된 데이터가 DAO 또는 후속 계산으로 전달되지 않도록 차단한다.
	// 照会したデータがリクエストフィルターBy社員名称の検索条件を満たすか確認する。
	// 必須値と状態の組み合わせを確認し、不正データがDAOまたは後続計算へ渡らないよう遮断する。
	private void filterByEmployeeName(List<RetirementBenefitListItem> items, String keyword) {
		if (keyword == null || keyword.trim().isEmpty()) {
			return;
		}
		String searchWord = keyword.trim();
		items.removeIf(item -> item.getEmployeeName() == null
				|| !item.getEmployeeName().contains(searchWord));
	}

	// 조회된 데이터가 contains계산 검색조건을 충족하는지 확인한다.
	// 필수값과 상태 조합을 확인해 잘못된 데이터가 DAO 또는 후속 계산으로 전달되지 않도록 차단한다.
	// 照会したデータがcontains計算の検索条件を満たすか確認する。
	// 必須値と状態の組み合わせを確認し、不正データがDAOまたは後続計算へ渡らないよう遮断する。
	private boolean containsCalculation(List<RetirementBenefitListItem> items, Integer calculationId) {
		if (calculationId == null) {
			return false;
		}
		for (RetirementBenefitListItem item : items) {
			if (item.getCalculationId() == calculationId) {
				return true;
			}
		}
		return false;
	}

	// 조회값과 입력값을 조합하여 명세서 처리 데이터를 구성한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 照会値と入力値を組み合わせて明細書の処理データを構成する。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	private RetirementPayslip makePayslip(Connection conn, int calculationId) throws SQLException {
		RetirementCalculation calculation = calculationDao.selectById(conn, calculationId);
		if (calculation == null) {
			return null;
		}

		Employee employee = EmployeeDao.getInstance().selectById(conn, calculation.getEmployeeId());
		if (employee == null) {
			return null;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		RetirementPayslip payslip = new RetirementPayslip();
		payslip.setCalculationId(calculationId);
		payslip.setEmployeeId(employee.getEmployeeId());
		payslip.setSettlementType(calculation.getCalcType());
		payslip.setEmployeeName(employee.getEmpNameKr());
		payslip.setJoinDate(dateFormat.format(calculation.getCalcStartDate()));
		payslip.setRetirementDate(dateFormat.format(calculation.getRetireDate()));
		payslip.setDepartmentName(findDepartmentName(conn, employee.getDepartmentId()));
		payslip.setPositionName(findPositionName(conn, employee.getJobPositionId()));
		payslip.setServiceDays(calculation.getServiceDays());
		payslip.setCompensation(calculation.getCompensationAmt());
		payslip.setDismissalAllowance(calculation.getDismissalAmt());
		payslip.setThreeMonthTotal(calculation.getThreeMonthTotal());
		payslip.setDailyAverage(calculation.getAvgDayWage());
		payslip.setDailyOrdinary(calculation.getOrdinaryDayWage());
		payslip.setRetirementIncome(calculation.getRetireIncome());
		payslip.setIncomeTax(calculation.getIncomeTax());
		payslip.setLocalIncomeTax(calculation.getLocalIncomeTax());
		payslip.setOtherDeduction(calculation.getSpecialRuralTax()
				+ calculation.getOtherDeductAmt());
		payslip.setDeductionTotal(calculation.getIncomeTax()
				+ calculation.getLocalIncomeTax()
				+ calculation.getSpecialRuralTax()
				+ calculation.getOtherDeductAmt());
		payslip.setNetPayment(calculation.getActualPayAmt());

		List<RetirementIncomeEntry> entries = RetirementIncomeEntryDao.getInstance()
				.selectByCalcId(conn, calculationId);
		for (RetirementIncomeEntry entry : entries) {
			if (entry.isSalaryData()) {
				addSalaryDetail(payslip, entry, dateFormat);
			} else if (entry.isEtcIncomeData()) {
				addOtherIncome(payslip, entry);
			}
		}
		while (payslip.getSalaryDetails().size() < 4) {
			payslip.getSalaryDetails().add(new RetirementPayslip.SalaryDetail());
		}
		return payslip;
	}

	// 퇴직급여명세서 처리에 사용할 급여상세정보 데이터나 객체를 생성한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 退職給与明細書処理で使用する給与詳細情報データまたはオブジェクトを生成する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	private void addSalaryDetail(RetirementPayslip payslip, RetirementIncomeEntry entry,
			SimpleDateFormat dateFormat) {
		RetirementPayslip.SalaryDetail detail = new RetirementPayslip.SalaryDetail();
		if (entry.getPeriodStartDate() != null) {
			detail.setStartDate(dateFormat.format(entry.getPeriodStartDate()));
		}
		if (entry.getPeriodEndDate() != null) {
			detail.setEndDate(dateFormat.format(entry.getPeriodEndDate()));
		}
		detail.setDays(entry.getCalcDays() == null ? 0 : entry.getCalcDays());
		detail.setAmount(entry.getAmount());
		payslip.getSalaryDetails().add(detail);
		payslip.setSalaryDaysTotal(payslip.getSalaryDaysTotal() + Math.round(detail.getDays()));
		payslip.setSalaryTotal(payslip.getSalaryTotal() + detail.getAmount());
	}

	// 퇴직급여명세서 처리에 사용할 기타소득 데이터나 객체를 생성한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 退職給与明細書処理で使用するその他所得データまたはオブジェクトを生成する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	private void addOtherIncome(RetirementPayslip payslip, RetirementIncomeEntry entry) {
		RetirementPayslip.OtherIncome income = new RetirementPayslip.OtherIncome();
		income.setItemName(entry.getItemName());
		income.setAnnualAmount(entry.getAmount());
		income.setThreeMonthAmount(entry.getThreeMonthAmount());
		payslip.getOtherIncomes().add(income);
	}

	// 퇴직급여명세서 처리에 필요한 부서명칭 데이터를 조회하여 반환한다.
	// 조회 전용 Connection으로 관련 DAO 결과를 조합하며 데이터 변경이 없으므로 별도의 commit이나 rollback을 수행하지 않는다.
	// 退職給与明細書処理に必要な部署名称データを照会して返す。
	// 照会専用Connectionで関連DAOの結果を組み合わせ、データ変更がないため個別のcommitやrollbackは実行しない。
	private String findDepartmentName(Connection conn, Integer departmentId) throws SQLException {
		if (departmentId == null) {
			return "";
		}
		Department department = DepartmentDao.getInstance().selectById(conn, departmentId);
		return department == null ? "" : department.getDepartmentName();
	}

	// 퇴직급여명세서 처리에 필요한 직위명칭 데이터를 조회하여 반환한다.
	// 조회 전용 Connection으로 관련 DAO 결과를 조합하며 데이터 변경이 없으므로 별도의 commit이나 rollback을 수행하지 않는다.
	// 退職給与明細書処理に必要な役職名称データを照会して返す。
	// 照会専用Connectionで関連DAOの結果を組み合わせ、データ変更がないため個別のcommitやrollbackは実行しない。
	private String findPositionName(Connection conn, Integer positionId) throws SQLException {
		if (positionId == null) {
			return "";
		}
		JobPosition position = JobPositionDao.getInstance().selectById(conn, positionId);
		return position == null ? "" : position.getJobPositionName();
	}

	public static class PayslipData {
		private List<RetirementBenefitListItem> items;
		private RetirementPayslip selected;
		private Company company;

		// 퇴직급여명세서 처리에 필요한 항목 목록를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 退職給与明細書処理に必要な項目一覧を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public List<RetirementBenefitListItem> getItems() {
			return items;
		}

		// 퇴직급여명세서 처리에 필요한 Selected를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 退職給与明細書処理に必要なSelectedを照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public RetirementPayslip getSelected() {
			return selected;
		}

		// 퇴직급여명세서 처리에 필요한 사업장를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 退職給与明細書処理に必要な事業所を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public Company getCompany() {
			return company;
		}
	}
}
