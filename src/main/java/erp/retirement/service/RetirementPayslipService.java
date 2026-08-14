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
public class RetirementPayslipService {

	private final RetirementCalculationDao calculationDao = RetirementCalculationDao.getInstance();

	public List<Integer> getPaymentYears() {
		int currentYear = LocalDate.now().getYear();
		List<Integer> years = new ArrayList<>();
		for (int year = currentYear + 1; year >= currentYear - 5; year--) {
			years.add(year);
		}
		return years;
	}

	public PayslipData getData(int year, String keyword, Integer calculationId) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			PayslipData data = new PayslipData();
			data.items = calculationDao.selectBenefitList(conn, year);
			filterByEmployeeName(data.items, keyword);
			data.company = CompanyDao.getInstance().selectById(conn, 1);

			Integer selectedId = calculationId;
			if (!containsCalculation(data.items, selectedId)) {
				selectedId = data.items.isEmpty() ? null : data.items.get(0).getCalculationId();
			}
			if (selectedId != null) {
				data.selected = makePayslip(conn, selectedId);
			}
			return data;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void filterByEmployeeName(List<RetirementBenefitListItem> items, String keyword) {
		if (keyword == null || keyword.trim().isEmpty()) {
			return;
		}
		String searchWord = keyword.trim();
		items.removeIf(item -> item.getEmployeeName() == null
				|| !item.getEmployeeName().contains(searchWord));
	}

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

	private void addOtherIncome(RetirementPayslip payslip, RetirementIncomeEntry entry) {
		RetirementPayslip.OtherIncome income = new RetirementPayslip.OtherIncome();
		income.setItemName(entry.getItemName());
		income.setAnnualAmount(entry.getAmount());
		income.setThreeMonthAmount(entry.getThreeMonthAmount());
		payslip.getOtherIncomes().add(income);
	}

	private String findDepartmentName(Connection conn, Integer departmentId) throws SQLException {
		if (departmentId == null) {
			return "";
		}
		Department department = DepartmentDao.getInstance().selectById(conn, departmentId);
		return department == null ? "" : department.getDepartmentName();
	}

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

		public List<RetirementBenefitListItem> getItems() {
			return items;
		}

		public RetirementPayslip getSelected() {
			return selected;
		}

		public Company getCompany() {
			return company;
		}
	}
}
