package erp.retirement.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import erp.employees.dao.EmployeeDao;
import erp.employees.dto.EmployeeListItem;
import erp.employees.model.Employee;
import erp.employees.service.EmployeeSearchCondition;
import erp.retirement.dao.RetirementBenefitQueryDao;
import erp.retirement.dao.RetirementCalculationDao;
import erp.retirement.dao.RetirementIncomeEntryDao;
import erp.retirement.dao.RetirementTaxDeferralDao;
import erp.retirement.dto.RetirementBenefitForm;
import erp.retirement.dto.RetirementBenefitListItem;
import erp.retirement.model.RetirementCalculation;
import erp.retirement.model.RetirementIncomeEntry;
import erp.retirement.model.RetirementTaxDeferral;
import erp.settings.dao.DepartmentDao;
import erp.settings.model.Department;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 퇴직급여 조회, 계산, 저장과 삭제를 처리한다.
public class RetirementBenefitService {

	private final RetirementCalculationDao calculationDao = RetirementCalculationDao.getInstance();
	private final RetirementIncomeEntryDao incomeEntryDao = RetirementIncomeEntryDao.getInstance();
	private final RetirementTaxDeferralDao taxDeferralDao = RetirementTaxDeferralDao.getInstance();
	private final EmployeeDao employeeDao = EmployeeDao.getInstance();
	private final DepartmentDao departmentDao = DepartmentDao.getInstance();
	private final RetirementBenefitQueryDao benefitQueryDao = new RetirementBenefitQueryDao();

	// 정산 종료일 이전 최근 3개월의 급여를 불러온다.
	public void loadRecentSalaryEntries(RetirementBenefitForm form) {
		if (form.getEndDate() == null || form.getEndDate().trim().isEmpty()) {
			throw new IllegalArgumentException("정산 종료일을 입력하세요.");
		}

		try (Connection conn = ConnectionProvider.getConnection()) {
			form.getIncomeEntries().removeIf(RetirementIncomeEntry::isSalaryData);
			form.getIncomeEntries().addAll(benefitQueryDao.selectRecentSalaryEntries(conn,
					form.getEmployeeId(), form.getEndDate()));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Integer> getPaymentYears() {
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		List<Integer> years = new ArrayList<>();
		for (int year = currentYear + 1; year >= currentYear - 5; year--) {
			years.add(year);
		}
		return years;
	}

	public BenefitPageData getPage(int year, Integer calculationId, String keyword,
			Integer departmentId) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			BenefitPageData data = new BenefitPageData();
			data.benefits = calculationDao.selectBenefitList(conn, year);
			data.departments = departmentDao.selectAll(conn);
			data.employees = employeeDao.selectListByCondition(conn,
					createEmployeeCondition(keyword, departmentId));

			if (calculationId != null) {
				RetirementCalculation calculation = calculationDao.selectById(conn, calculationId);
				if (calculation != null) {
					data.form = fromModel(calculation);
					data.form.getIncomeEntries().addAll(incomeEntryDao.selectByCalcId(conn, calculationId));
					data.form.getTaxDeferrals().addAll(taxDeferralDao.selectByCalcId(conn, calculationId));
				}
			}
			return data;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private EmployeeSearchCondition createEmployeeCondition(String keyword, Integer departmentId) {
		EmployeeSearchCondition condition = new EmployeeSearchCondition();
		condition.setSearchTarget("ALL");
		condition.setKeyword(keyword == null ? "" : keyword);
		condition.setEmploymentType("");
		condition.setStatus("");
		condition.setPage(1);
		condition.setPageSize(100);
		condition.setDepartmentId(departmentId);
		return condition;
	}

	public RetirementBenefitForm prepareNew(int employeeId) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			Employee employee = employeeDao.selectById(conn, employeeId);
			if (employee == null) {
				throw new IllegalArgumentException("사원을 찾을 수 없습니다.");
			}

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			RetirementBenefitForm form = new RetirementBenefitForm();
			form.setEmployeeId(employeeId);
			if (employee.getJoinDate() != null) {
				form.setStartDate(dateFormat.format(employee.getJoinDate()));
			}
			if (employee.getRetireDate() != null) {
				form.setEndDate(dateFormat.format(employee.getRetireDate()));
			}
			// 선택한 사원의 재직기간을 신규 정산 화면에 바로 표시한다.
			if (form.getStartDate() != null && form.getEndDate() != null) {
				LocalDate startDate = LocalDate.parse(form.getStartDate());
				LocalDate endDate = LocalDate.parse(form.getEndDate());
				int serviceDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
				form.setServiceDays(Math.max(0, serviceDays));
				form.setServiceYears(Math.max(0, serviceDays) / 365);
			}
			form.setExcludedDays(0);
			return form;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void calculate(RetirementBenefitForm form) {
		validateCalculation(form);

		LocalDate startDate = LocalDate.parse(form.getStartDate());
		LocalDate endDate = LocalDate.parse(form.getEndDate());
		int totalDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
		int serviceDays = totalDays - form.getExcludedDays();

		form.setServiceDays(serviceDays);
		form.setServiceYears(serviceDays / 365);
		form.setTaxYear(endDate.getYear());
		calculateWage(form);
		calculatePayment(form, serviceDays);
	}

	private void validateCalculation(RetirementBenefitForm form) {
		if (!"RETIREMENT".equals(form.getSettlementType())
				&& !"INTERIM".equals(form.getSettlementType())) {
			throw new IllegalArgumentException("정산구분을 선택하세요.");
		}

		LocalDate startDate;
		LocalDate endDate;
		try {
			startDate = LocalDate.parse(form.getStartDate());
			endDate = LocalDate.parse(form.getEndDate());
		} catch (Exception e) {
			throw new IllegalArgumentException("정산 시작일과 종료일을 입력하세요.");
		}

		if (endDate.isBefore(startDate)) {
			throw new IllegalArgumentException("정산 종료일은 시작일보다 빠를 수 없습니다.");
		}
		int totalDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
		if (form.getExcludedDays() < 0 || form.getExcludedDays() >= totalDays) {
			throw new IllegalArgumentException("제외일수를 확인하세요.");
		}
		if (totalDays - form.getExcludedDays() < 365) {
			throw new IllegalArgumentException("근속일수 1년 미만인 사원은 퇴직급여 정산 대상이 아닙니다.");
		}
	}

	private void calculateWage(RetirementBenefitForm form) {
		long salaryTotal = 0;
		long salaryDays = 0;
		long otherIncomeTotal = 0;

		for (RetirementIncomeEntry entry : form.getIncomeEntries()) {
			if (entry.isSalaryData()) {
				salaryTotal += entry.getAmount();
				salaryDays += entry.getCalcDays() == null ? 0 : Math.round(entry.getCalcDays());
			} else {
				otherIncomeTotal += entry.getThreeMonthAmount();
			}
		}

		form.setSalaryTotal(salaryTotal);
		form.setSalaryDaysTotal(salaryDays);
		form.setThreeMonthTotal(salaryTotal + otherIncomeTotal);
		form.setDailyAverage(salaryDays == 0 ? 0 : form.getThreeMonthTotal() / salaryDays);
	}

	private void calculatePayment(RetirementBenefitForm form, int serviceDays) {
		long dailyBase = Math.max(form.getDailyAverage(), form.getDailyOrdinary());
		long retirementIncome = form.getRetirementIncome();
		if (retirementIncome <= 0) {
			retirementIncome = Math.round(dailyBase * 30.0 * serviceDays / 365.0)
					+ form.getCompensation() + form.getDismissalAllowance();
		}

		long taxablePayment = Math.max(0, retirementIncome - form.getTaxFreeRetirement());
		long withholdingTax = form.getIncomeTax() + form.getLocalIncomeTax()
				+ form.getRuralTax() + form.getOtherDeduction()
				- form.getDeferredIncomeTax() - form.getDeferredLocalTax();

		form.setRetirementIncome(retirementIncome);
		form.setTaxablePayment(taxablePayment);
		form.setWithholdingTax(Math.max(0, withholdingTax));
		form.setNetPayment(Math.max(0, taxablePayment - form.getWithholdingTax()));
	}

	public int save(RetirementBenefitForm form) {
		calculate(form);
		validatePayment(form);

		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);

			int calculationId = form.getCalculationId() > 0
					? form.getCalculationId() : calculationDao.nextId(conn);
			if (form.getCalculationId() > 0) {
				calculationDao.delete(conn, calculationId);
			}

			calculationDao.insertWithId(conn, toModel(form, calculationId));
			insertIncomeEntries(conn, calculationId, form.getIncomeEntries());
			insertTaxDeferrals(conn, calculationId, form.getTaxDeferrals());
			conn.commit();
			return calculationId;
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	private void validatePayment(RetirementBenefitForm form) {
		if (form.getPaymentMethod() == null || form.getPaymentMethod().trim().isEmpty()
				|| form.getPaymentDate() == null || form.getPaymentDate().trim().isEmpty()) {
			throw new IllegalArgumentException("지급방법과 지급일을 입력하세요.");
		}
	}

	private void insertIncomeEntries(Connection conn, int calculationId,
			List<RetirementIncomeEntry> entries) throws SQLException {
		for (RetirementIncomeEntry entry : entries) {
			entry.setRetirementCalculationId(calculationId);
			incomeEntryDao.insert(conn, entry);
		}
	}

	private void insertTaxDeferrals(Connection conn, int calculationId,
			List<RetirementTaxDeferral> deferrals) throws SQLException {
		for (RetirementTaxDeferral deferral : deferrals) {
			deferral.setRetirementCalculationId(calculationId);
			taxDeferralDao.insert(conn, deferral);
		}
	}

	public void delete(Integer calculationId, boolean deleteAll, Integer paymentYear) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			if (deleteAll) {
				if (paymentYear == null) {
					throw new IllegalArgumentException("삭제할 지급년도를 선택해주세요");
				}
				calculationDao.deleteAllByPaymentYear(conn, paymentYear);
			} else if (calculationId != null) {
				calculationDao.delete(conn, calculationId);
			}
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	private RetirementCalculation toModel(RetirementBenefitForm form, int calculationId)
			throws SQLException {
		RetirementCalculation calculation = new RetirementCalculation();
		calculation.setRetirementCalculationId(calculationId);
		calculation.setEmployeeId(form.getEmployeeId());
		calculation.setCalcType("INTERIM".equals(form.getSettlementType()) ? "중간정산" : "퇴직정산");

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			calculation.setCalcStartDate(dateFormat.parse(form.getStartDate()));
			calculation.setRetireDate(dateFormat.parse(form.getEndDate()));
			calculation.setPayDate(dateFormat.parse(form.getPaymentDate()));
		} catch (ParseException e) {
			throw new SQLException(e);
		}

		calculation.setServiceYears(form.getServiceYears());
		calculation.setServiceDays(form.getServiceDays());
		calculation.setExcludeDays(form.getExcludedDays());
		calculation.setCompensationAmt(form.getCompensation());
		calculation.setDismissalAmt(form.getDismissalAllowance());
		calculation.setTaxFreeRetireAmt(form.getTaxFreeRetirement());
		calculation.setPrepaidTaxAmt(form.getPrepaidTax());
		calculation.setTaxCreditAmt(form.getTaxCredit());
		calculation.setThreeMonthTotal(form.getThreeMonthTotal());
		calculation.setAvgMonthWage(form.getSalaryTotal() / 3);
		calculation.setAvgDayWage(form.getDailyAverage());
		calculation.setOrdinaryDayWage(form.getDailyOrdinary());
		calculation.setRetireIncome(form.getRetirementIncome());
		calculation.setCalculatedTaxAmt(form.getCalculatedTax());
		calculation.setIncomeTax(form.getIncomeTax());
		calculation.setLocalIncomeTax(form.getLocalIncomeTax());
		calculation.setDeferredIncomeTax(form.getDeferredIncomeTax());
		calculation.setDeferredLocalTax(form.getDeferredLocalTax());
		calculation.setSpecialRuralTax(form.getRuralTax());
		calculation.setOtherDeductAmt(form.getOtherDeduction());
		calculation.setTaxableRetireAmt(form.getTaxablePayment());
		calculation.setWithholdingTaxAmt(form.getWithholdingTax());
		calculation.setActualPayAmt(form.getNetPayment());
		calculation.setPayMethod(form.getPaymentMethod());
		return calculation;
	}

	private RetirementBenefitForm fromModel(RetirementCalculation calculation) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		RetirementBenefitForm form = new RetirementBenefitForm();
		form.setCalculationId(calculation.getRetirementCalculationId());
		form.setEmployeeId(calculation.getEmployeeId());
		form.setSettlementType("중간정산".equals(calculation.getCalcType()) ? "INTERIM" : "RETIREMENT");
		form.setStartDate(dateFormat.format(calculation.getCalcStartDate()));
		form.setEndDate(dateFormat.format(calculation.getRetireDate()));
		form.setServiceYears(calculation.getServiceYears());
		form.setServiceDays(calculation.getServiceDays());
		form.setExcludedDays(calculation.getExcludeDays());
		form.setCompensation(calculation.getCompensationAmt());
		form.setDismissalAllowance(calculation.getDismissalAmt());
		form.setTaxFreeRetirement(calculation.getTaxFreeRetireAmt());
		form.setPrepaidTax(calculation.getPrepaidTaxAmt());
		form.setTaxCredit(calculation.getTaxCreditAmt());
		form.setThreeMonthTotal(calculation.getThreeMonthTotal());
		form.setDailyAverage(calculation.getAvgDayWage());
		form.setDailyOrdinary(calculation.getOrdinaryDayWage());
		form.setRetirementIncome(calculation.getRetireIncome());
		form.setCalculatedTax(calculation.getCalculatedTaxAmt());
		form.setIncomeTax(calculation.getIncomeTax());
		form.setLocalIncomeTax(calculation.getLocalIncomeTax());
		form.setDeferredIncomeTax(calculation.getDeferredIncomeTax());
		form.setDeferredLocalTax(calculation.getDeferredLocalTax());
		form.setRuralTax(calculation.getSpecialRuralTax());
		form.setOtherDeduction(calculation.getOtherDeductAmt());
		form.setTaxablePayment(calculation.getTaxableRetireAmt());
		form.setWithholdingTax(calculation.getWithholdingTaxAmt());
		form.setNetPayment(calculation.getActualPayAmt());
		form.setPaymentMethod(calculation.getPayMethod());
		form.setPaymentDate(dateFormat.format(calculation.getPayDate()));
		return form;
	}

	public static class BenefitPageData {
		private List<RetirementBenefitListItem> benefits;
		private List<EmployeeListItem> employees;
		private List<Department> departments;
		private RetirementBenefitForm form;

		public List<RetirementBenefitListItem> getBenefits() {
			return benefits;
		}

		public List<EmployeeListItem> getEmployees() {
			return employees;
		}

		public List<Department> getDepartments() {
			return departments;
		}

		public RetirementBenefitForm getForm() {
			return form;
		}
	}
}
