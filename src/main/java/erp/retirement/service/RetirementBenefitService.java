package erp.retirement.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
// 퇴직급여정산 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 退職給与精算の業務ルールとデータ変更トランザクションを処理する。
public class RetirementBenefitService {

	private final RetirementCalculationDao calculationDao = RetirementCalculationDao.getInstance();
	private final RetirementIncomeEntryDao incomeEntryDao = RetirementIncomeEntryDao.getInstance();
	private final RetirementTaxDeferralDao taxDeferralDao = RetirementTaxDeferralDao.getInstance();
	private final EmployeeDao employeeDao = EmployeeDao.getInstance();
	private final DepartmentDao departmentDao = DepartmentDao.getInstance();
	private final RetirementBenefitQueryDao benefitQueryDao = new RetirementBenefitQueryDao();

	// 정산 종료일 이전 최근 3개월의 급여를 불러온다.
	// 퇴직급여정산 처리에 필요한 최근급여상세내역 목록 데이터를 조회하여 반환한다.
	// 조회 전용 Connection으로 관련 DAO 결과를 조합하며 데이터 변경이 없으므로 별도의 commit이나 rollback을 수행하지 않는다.
	// 退職給与精算処理に必要な直近給与明細一覧データを照会して返す。
	// 照会専用Connectionで関連DAOの結果を組み合わせ、データ変更がないため個別のcommitやrollbackは実行しない。
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

	// 퇴직급여정산 처리에 필요한 지급연도 목록를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 退職給与精算処理に必要な支給年度一覧を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<Integer> getPaymentYears() {
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		List<Integer> years = new ArrayList<>();
		for (int year = currentYear + 1; year >= currentYear - 5; year--) {
			years.add(year);
		}
		return years;
	}

	// 요청 조건에 맞는 퇴직급여정산 화면 데이터를 구성하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// リクエスト条件に合う退職給与精算の画面データを構成して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
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

	// 퇴직급여정산 처리에 사용할 사원검색조건 데이터나 객체를 생성한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 退職給与精算処理で使用する社員検索条件データまたはオブジェクトを生成する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
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

	// 저장된 정산이 있으면 최근 내역을 불러오고, 없으면 신규 정산 화면을 준비한다.
	// 조회값과 입력값을 조합하여 입력·관리 처리 데이터를 구성한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 照会値と入力値を組み合わせて入力・管理の処理データを構成する。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	public RetirementBenefitForm prepareForManagement(int employeeId) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			List<RetirementCalculation> calculations = calculationDao.selectByEmpId(conn, employeeId);
			if (calculations.isEmpty()) {
				return prepareNew(employeeId);
			}

			RetirementCalculation calculation = calculations.get(0);
			RetirementBenefitForm form = fromModel(calculation);
			int calculationId = calculation.getRetirementCalculationId();
			form.getIncomeEntries().addAll(incomeEntryDao.selectByCalcId(conn, calculationId));
			form.getTaxDeferrals().addAll(taxDeferralDao.selectByCalcId(conn, calculationId));
			return form;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// 신규 정산 목록에 이미 추가한 사원을 검색 조건과 관계없이 다시 조회한다.
	// 퇴직급여정산 처리에 필요한 사원를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 退職給与精算処理に必要な社員を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<EmployeeListItem> getEmployees(List<Integer> employeeIds) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			EmployeeSearchCondition condition = createEmployeeCondition("", null);
			condition.setPageSize(10000);
			List<EmployeeListItem> employees = employeeDao.selectListByCondition(conn, condition);
			employees.removeIf(employee -> !employeeIds.contains(employee.getEmployeeId()));
			return employees;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// 임시 목록에 담긴 사원별 최근 정산을 상단 목록 표시용으로 조회한다.
	// 퇴직급여정산 처리에 필요한 최근퇴직급여 목록를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 退職給与精算処理に必要な最新退職給与一覧を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public Map<Integer, RetirementBenefitForm> getLatestBenefits(List<Integer> employeeIds) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			Map<Integer, RetirementBenefitForm> benefits = new HashMap<>();
			for (Integer employeeId : employeeIds) {
				List<RetirementCalculation> calculations = calculationDao.selectByEmpId(conn, employeeId);
				if (!calculations.isEmpty()) {
					benefits.put(employeeId, fromModel(calculations.get(0)));
				}
			}
			return benefits;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// 사원정보 화면에서 표시할 가장 최근 퇴직급여 정산 결과를 조회한다.
	// 퇴직급여정산 처리에 필요한 최근정산를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 退職給与精算処理に必要な最新精算を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public RetirementBenefitForm getLatestBenefit(int employeeId) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			List<RetirementCalculation> calculations = calculationDao.selectByEmpId(conn, employeeId);
			return calculations.isEmpty() ? null : fromModel(calculations.get(0));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// 조회값과 입력값을 조합하여 신규 처리 데이터를 구성한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 照会値と入力値を組み合わせて新規の処理データを構成する。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	public RetirementBenefitForm prepareNew(int employeeId) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			Employee employee = employeeDao.selectById(conn, employeeId);
			if (employee == null) {
				throw new IllegalArgumentException("사원을 찾을 수 없습니다.");
			}

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			RetirementBenefitForm form = new RetirementBenefitForm();
			form.setEmployeeId(employeeId);
			// 재직 사원은 중간정산, 퇴직 사원은 퇴직정산으로 고정한다.
			// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
			form.setSettlementType("퇴직".equals(employee.getStatus()) ? "RETIREMENT" : "INTERIM");
			if (employee.getJoinDate() != null) {
				form.setStartDate(dateFormat.format(employee.getJoinDate()));
			}
			if (employee.getRetireDate() != null) {
				form.setEndDate(dateFormat.format(employee.getRetireDate()));
			}
			// 선택한 사원의 재직기간을 신규 정산 화면에 바로 표시한다.
			// 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
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

	// 조회된 금액과 업무 규칙을 이용해 퇴직급여정산 계산 결과를 생성한다.
	// 입력금액과 기간·요율·절사단위를 적용하고 계산 중 발생할 수 있는 NULL이나 음수 결과를 안전하게 보정한다.
	// 照会した金額と業務ルールを使用して退職給与精算の計算結果を生成する。
	// 入力金額と期間・料率・端数処理単位を適用し、計算中に発生し得るNULLや負数結果を安全に補正する。
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

	// 계산 입력값과 업무 처리 가능 여부를 검증한다.
	// 필수값과 상태 조합을 확인해 잘못된 데이터가 DAO 또는 후속 계산으로 전달되지 않도록 차단한다.
	// 計算の入力値と業務処理の可否を検証する。
	// 必須値と状態の組み合わせを確認し、不正データがDAOまたは後続計算へ渡らないよう遮断する。
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

	// 조회된 금액과 업무 규칙을 이용해 임금 계산 결과를 생성한다.
	// 입력금액과 기간·요율·절사단위를 적용하고 계산 중 발생할 수 있는 NULL이나 음수 결과를 안전하게 보정한다.
	// 照会した金額と業務ルールを使用して賃金の計算結果を生成する。
	// 入力金額と期間・料率・端数処理単位を適用し、計算中に発生し得るNULLや負数結果を安全に補正する。
	private void calculateWage(RetirementBenefitForm form) {
		long salaryTotal = 0;
		long salaryDays = 0;
		long otherIncomeTotal = 0;

		for (RetirementIncomeEntry entry : form.getIncomeEntries()) {
			if (entry.isSalaryData()) {
				salaryTotal += entry.getAmount();
				salaryDays += entry.getCalcDays() == null ? 0 : Math.round(entry.getCalcDays());
			} else {
				// 최근 1년 기타소득 중 평균임금 산정에 반영할 3개월분을 환산한다.
				// 基準日と期間の境界を確認し、照会・計算に使用できる日付形式へ変換する。
				long threeMonthAmount = Math.max(0, entry.getAmount()) * 3 / 12;
				entry.setThreeMonthAmount(threeMonthAmount);
				otherIncomeTotal += threeMonthAmount;
			}
		}

		form.setSalaryTotal(salaryTotal);
		form.setSalaryDaysTotal(salaryDays);
		form.setThreeMonthTotal(salaryTotal + otherIncomeTotal);
		form.setDailyAverage(salaryDays == 0 ? 0 : form.getThreeMonthTotal() / salaryDays);
		// 별도 통상임금 자료가 없으므로 프로젝트에서는 산정된 평균임금을 기준임금으로 사용한다.
		// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
		form.setDailyOrdinary(form.getDailyAverage());
	}

	// 조회된 금액과 업무 규칙을 이용해 지급 계산 결과를 생성한다.
	// 입력금액과 기간·요율·절사단위를 적용하고 계산 중 발생할 수 있는 NULL이나 음수 결과를 안전하게 보정한다.
	// 照会した金額と業務ルールを使用して支給の計算結果を生成する。
	// 入力金額と期間・料率・端数処理単位を適用し、計算中に発生し得るNULLや負数結果を安全に補正する。
	private void calculatePayment(RetirementBenefitForm form, int serviceDays) {
		long dailyBase = Math.max(form.getDailyAverage(), form.getDailyOrdinary());
		long retirementIncome = Math.round(dailyBase * 30.0 * serviceDays / 365.0)
				+ Math.max(0, form.getCompensation()) + Math.max(0, form.getDismissalAllowance());
		long taxablePayment = Math.max(0, retirementIncome - Math.max(0, form.getTaxFreeRetirement()));
		int serviceYears = Math.max(1, (int) Math.ceil(serviceDays / 365.0));

		// 퇴직소득세는 근속연수공제와 환산급여공제를 차례로 적용해 계산한다.
		// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
		long serviceDeduction = calculateServiceDeduction(serviceYears);
		long convertedPay = Math.max(0, taxablePayment - serviceDeduction) * 12 / serviceYears;
		long taxBase = Math.max(0, convertedPay - calculateConvertedPayDeduction(convertedPay));
		long calculatedTax = floorToTen(calculateProgressiveTax(taxBase) * serviceYears / 12);
		long incomeTax = floorToTen(Math.max(0,
				calculatedTax - Math.max(0, form.getPrepaidTax()) - Math.max(0, form.getTaxCredit())));
		long localIncomeTax = floorToTen(incomeTax / 10);

		long deferralAmount = getDeferralAmount(form);
		double deferralRate = retirementIncome == 0 ? 0
				: Math.min(1.0, (double) deferralAmount / retirementIncome);
		long deferredIncomeTax = floorToTen(Math.round(incomeTax * deferralRate));
		long deferredLocalTax = floorToTen(Math.round(localIncomeTax * deferralRate));
		long withholdingTax = Math.max(0, incomeTax + localIncomeTax
				- deferredIncomeTax - deferredLocalTax);

		form.setRetirementIncome(retirementIncome);
		form.setTaxablePayment(taxablePayment);
		form.setCalculatedTax(calculatedTax);
		form.setIncomeTax(incomeTax);
		form.setLocalIncomeTax(localIncomeTax);
		form.setDeferredIncomeTax(deferredIncomeTax);
		form.setDeferredLocalTax(deferredLocalTax);
		form.setRuralTax(0);
		form.setOtherDeduction(0);
		form.setWithholdingTax(withholdingTax);
		// 연금계좌 이체액은 현금 수령액에서 제외하되 퇴직급여 자체에는 포함한다.
		// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
		form.setNetPayment(Math.max(0, retirementIncome - Math.min(retirementIncome, deferralAmount)
				- withholdingTax));
	}

	// 조회된 금액과 업무 규칙을 이용해 근속공제 계산 결과를 생성한다.
	// 입력금액과 기간·요율·절사단위를 적용하고 계산 중 발생할 수 있는 NULL이나 음수 결과를 안전하게 보정한다.
	// 照会した金額と業務ルールを使用して勤続控除の計算結果を生成する。
	// 入力金額と期間・料率・端数処理単位を適用し、計算中に発生し得るNULLや負数結果を安全に補正する。
	private long calculateServiceDeduction(int serviceYears) {
		if (serviceYears <= 5) {
			return serviceYears * 1000000L;
		}
		if (serviceYears <= 10) {
			return 5000000L + (serviceYears - 5) * 2000000L;
		}
		if (serviceYears <= 20) {
			return 15000000L + (serviceYears - 10) * 2500000L;
		}
		return 40000000L + (serviceYears - 20) * 3000000L;
	}

	// 조회된 금액과 업무 규칙을 이용해 환산지급공제 계산 결과를 생성한다.
	// 입력금액과 기간·요율·절사단위를 적용하고 계산 중 발생할 수 있는 NULL이나 음수 결과를 안전하게 보정한다.
	// 照会した金額と業務ルールを使用して換算支給控除の計算結果を生成する。
	// 入力金額と期間・料率・端数処理単位を適用し、計算中に発生し得るNULLや負数結果を安全に補正する。
	private long calculateConvertedPayDeduction(long convertedPay) {
		if (convertedPay <= 8000000L) {
			return convertedPay;
		}
		if (convertedPay <= 70000000L) {
			return 8000000L + Math.round((convertedPay - 8000000L) * 0.60);
		}
		if (convertedPay <= 100000000L) {
			return 45200000L + Math.round((convertedPay - 70000000L) * 0.55);
		}
		if (convertedPay <= 300000000L) {
			return 61700000L + Math.round((convertedPay - 100000000L) * 0.45);
		}
		return 151700000L + Math.round((convertedPay - 300000000L) * 0.35);
	}

	// 조회된 금액과 업무 규칙을 이용해 누진세금 계산 결과를 생성한다.
	// 입력금액과 기간·요율·절사단위를 적용하고 계산 중 발생할 수 있는 NULL이나 음수 결과를 안전하게 보정한다.
	// 照会した金額と業務ルールを使用して累進税金の計算結果を生成する。
	// 入力金額と期間・料率・端数処理単位を適用し、計算中に発生し得るNULLや負数結果を安全に補正する。
	private long calculateProgressiveTax(long taxBase) {
		if (taxBase <= 14000000L) return Math.round(taxBase * 0.06);
		if (taxBase <= 50000000L) return Math.round(taxBase * 0.15 - 1260000L);
		if (taxBase <= 88000000L) return Math.round(taxBase * 0.24 - 5760000L);
		if (taxBase <= 150000000L) return Math.round(taxBase * 0.35 - 15440000L);
		if (taxBase <= 300000000L) return Math.round(taxBase * 0.38 - 19940000L);
		if (taxBase <= 500000000L) return Math.round(taxBase * 0.40 - 25940000L);
		if (taxBase <= 1000000000L) return Math.round(taxBase * 0.42 - 35940000L);
		return Math.round(taxBase * 0.45 - 65940000L);
	}

	// 퇴직급여정산 처리에 필요한 과세이연금액를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 退職給与精算処理に必要な課税繰延金額を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	private long getDeferralAmount(RetirementBenefitForm form) {
		long total = 0;
		for (RetirementTaxDeferral deferral : form.getTaxDeferrals()) {
			total += Math.max(0, deferral.getDepositAmt());
		}
		return total;
	}

	// 보험료와 세액 계산 결과를 지정된 절사단위에 맞춰 내림 처리한다.
	// 입력금액과 기간·요율·절사단위를 적용하고 계산 중 발생할 수 있는 NULL이나 음수 결과를 안전하게 보정한다.
	// 保険料と税額の計算結果を指定された端数処理単位に合わせて切り捨てる。
	// 入力金額と期間・料率・端数処理単位を適用し、計算中に発生し得るNULLや負数結果を安全に補正する。
	private long floorToTen(long amount) {
		return Math.max(0, amount / 10 * 10);
	}

	// 입력값을 검증한 후 퇴직급여정산 데이터를 트랜잭션으로 저장하거나 수정한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 入力値を検証した後、退職給与精算データをトランザクションで登録または更新する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
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

	// 지급 입력값과 업무 처리 가능 여부를 검증한다.
	// 필수값과 상태 조합을 확인해 잘못된 데이터가 DAO 또는 후속 계산으로 전달되지 않도록 차단한다.
	// 支給の入力値と業務処理の可否を検証する。
	// 必須値と状態の組み合わせを確認し、不正データがDAOまたは後続計算へ渡らないよう遮断する。
	private void validatePayment(RetirementBenefitForm form) {
		if (form.getPaymentMethod() == null || form.getPaymentMethod().trim().isEmpty()
				|| form.getPaymentDate() == null || form.getPaymentDate().trim().isEmpty()) {
			throw new IllegalArgumentException("지급방법과 지급일을 입력하세요.");
		}
	}

	// 퇴직급여정산 처리에 사용할 소득상세내역 목록 데이터나 객체를 생성한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 退職給与精算処理で使用する所得明細一覧データまたはオブジェクトを生成する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	private void insertIncomeEntries(Connection conn, int calculationId,
			List<RetirementIncomeEntry> entries) throws SQLException {
		for (RetirementIncomeEntry entry : entries) {
			entry.setRetirementCalculationId(calculationId);
			incomeEntryDao.insert(conn, entry);
		}
	}

	// 퇴직급여정산 처리에 사용할 세금과세이연 목록 데이터나 객체를 생성한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 退職給与精算処理で使用する税金課税繰延一覧データまたはオブジェクトを生成する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	private void insertTaxDeferrals(Connection conn, int calculationId,
			List<RetirementTaxDeferral> deferrals) throws SQLException {
		for (RetirementTaxDeferral deferral : deferrals) {
			deferral.setRetirementCalculationId(calculationId);
			taxDeferralDao.insert(conn, deferral);
		}
	}

	// 선택되거나 식별된 퇴직급여정산 데이터를 삭제하고 관련 상태를 정리한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 選択または識別された退職給与精算データを削除し、関連状態を整理する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
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

	// 입력 데이터를 도메인 데이터 처리에 필요한 형식으로 변환한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 入力データをドメインデータ処理に必要な形式へ変換する。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
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

	// 입력 데이터를 도메인 데이터 처리에 필요한 형식으로 변환한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 入力データをドメインデータ処理に必要な形式へ変換する。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
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

		// 퇴직급여정산 처리에 필요한 퇴직급여 목록를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 退職給与精算処理に必要な退職給与一覧を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public List<RetirementBenefitListItem> getBenefits() {
			return benefits;
		}

		// 퇴직급여정산 처리에 필요한 사원를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 退職給与精算処理に必要な社員を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public List<EmployeeListItem> getEmployees() {
			return employees;
		}

		// 퇴직급여정산 처리에 필요한 부서 목록를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 退職給与精算処理に必要な部署一覧を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public List<Department> getDepartments() {
			return departments;
		}

		// 요청 조건에 맞는 퇴직급여정산 입력화면를 구성하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// リクエスト条件に合う退職給与精算の入力画面を構成して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public RetirementBenefitForm getForm() {
			return form;
		}
	}
}
