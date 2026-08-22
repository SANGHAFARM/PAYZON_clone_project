package erp.payroll.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import erp.payroll.dao.PayrollManagementDao;
import erp.payroll.dto.PayrollManagementPage.PayrollEmployeePage;
import erp.payroll.dto.PayrollManagementPage.PayrollManagementEmployee;
import erp.payroll.dto.PayrollManagementItem;
import erp.payroll.dto.PayrollManagementPage;
import erp.payroll.dto.PayrollManagementPage.PayrollPositionOption;
import erp.payroll.dto.PayrollManagementPage.PayrollTotals;
import erp.payroll.model.PayrollRun;
import erp.settings.dao.DepartmentDao;
import erp.settings.dao.AttendanceItemDao;
import erp.settings.dao.JobPositionDao;
import erp.settings.dao.TaxFreeItemDao;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 급여 회차와 사원별 지급·공제 내역을 관리한다.
// 급여입력·관리 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 給与入力・管理の業務ルールとデータ変更トランザクションを処理する。
public class PayrollManagementService {

	private static final int EMPLOYEE_PAGE_SIZE = 10;

	private PayrollManagementDao managementDao = new PayrollManagementDao();
	private DepartmentDao departmentDao = DepartmentDao.getInstance();
	private JobPositionDao positionDao = JobPositionDao.getInstance();
	private TaxFreeItemDao taxFreeItemDao = TaxFreeItemDao.getInstance();
	private AttendanceItemDao attendanceItemDao = AttendanceItemDao.getInstance();

	// 요청 조건에 맞는 급여입력·관리 화면 데이터를 구성하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// リクエスト条件に合う給与入力・管理の画面データを構成して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public PayrollManagementPage getPage(String year, String month, String sequence, String incomeType,
			Integer employeeId, String keyword, int employeePage) {
		return getPage(year, month, sequence, incomeType, employeeId, keyword, null, null, null, employeePage);
	}

	// 요청 조건에 맞는 급여입력·관리 화면 데이터를 구성하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// リクエスト条件に合う給与入力・管理の画面データを構成して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public PayrollManagementPage getPage(String year, String month, String sequence, String incomeType,
			Integer employeeId, String keyword, Integer departmentId, Integer positionId, String status,
			int employeePage) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			PayrollRun run = managementDao.selectRun(conn, year, month, sequence, incomeType);
			List<PayrollManagementEmployee> employees = new ArrayList<>();
			Integer payrollEmployeeId = null;
			PayrollManagementEmployee selectedEmployee = null;

			if (run != null) {
				employees = managementDao.selectPayrollEmployees(conn, run.getPayrollRunId());
				selectedEmployee = selectEmployee(employees, employeeId);
				if (selectedEmployee != null) {
					payrollEmployeeId = managementDao.selectPayrollEmployeeId(conn, run.getPayrollRunId(),
							selectedEmployee.getEmployeeId());
				}
			}

			List<PayrollManagementItem> payItems = managementDao.selectPayItems(conn, payrollEmployeeId);
			List<PayrollManagementItem> deductItems = managementDao.selectDeductItems(conn, payrollEmployeeId);
			PayrollManagementPage page = new PayrollManagementPage();
			page.setRun(run);
			page.setPaymentEmployees(employees);
			page.setSelectedEmployee(selectedEmployee);
			page.setPaymentGiveItems(payItems);
			page.setPaymentDeductionItems(deductItems);
			page.setPaymentTotals(calculateTotals(payItems, deductItems));
			page.setDepartments(departmentDao.selectAll(conn));
			List<PayrollPositionOption> positions = new ArrayList<>();
			positionDao.selectAll(conn).forEach(position -> positions.add(new PayrollPositionOption(
					position.getJobPositionId(), position.getJobPositionName())));
			page.setPositions(positions);
			page.setPreviousPaymentPeriods(managementDao.selectPreviousRuns(conn, incomeType));
			// 기본환경설정에 등록된 비과세/감면 코드를 지급항목 팝업에 제공한다.
			// 保険・税区分と適用基準を確認し、対象金額に合う控除または非課税処理を適用する。
			page.setTaxFreeItems(taxFreeItemDao.selectAll(conn));
			page.setAttendanceItems(attendanceItemDao.selectAll(conn));

			if (run == null) {
				page.setAvailableEmployeePage(new PayrollEmployeePage(new ArrayList<>(), 1));
			} else {
				int count = managementDao.countAvailableEmployees(conn, run.getPayrollRunId(), keyword,
						departmentId, positionId, status);
				int totalPages = Math.max(1, (count + EMPLOYEE_PAGE_SIZE - 1) / EMPLOYEE_PAGE_SIZE);
				int currentPage = Math.min(Math.max(employeePage, 1), totalPages);
				page.setAvailableEmployeePage(new PayrollEmployeePage(
						managementDao.selectAvailableEmployees(conn, run.getPayrollRunId(), keyword, departmentId,
								positionId, status, currentPage, EMPLOYEE_PAGE_SIZE),
						totalPages));
			}
			return page;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 급여입력·관리 처리에 필요한 이전 회차 데이터를 조회하여 반환한다.
	// 조회 전용 Connection으로 관련 DAO 결과를 조합하며 데이터 변경이 없으므로 별도의 commit이나 rollback을 수행하지 않는다.
	// 給与入力・管理処理に必要な前回データを照会して返す。
	// 照会専用Connectionで関連DAOの結果を組み合わせ、データ変更がないため個別のcommitやrollbackは実行しない。
	public void loadPrevious(PayrollRun requestRun, int previousRunId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			PayrollRun run = getOrCreateRun(conn, requestRun);
			managementDao.copyPreviousRun(conn, previousRunId, run.getPayrollRunId());
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 지급항목의 추가·수정·삭제 구분에 따라 데이터를 처리하고 결과를 반환한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 支給項目の追加・更新・削除区分に従ってデータを処理し、結果を返す。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void managePayItem(String action, Integer itemId, String itemName, String taxType, String taxFreeCode,
			long taxFreeLimit, String calculationMethod, int roundUnit, String payMethod, Integer attendanceItemId,
			Long bulkAmount) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			String calculation = defaultPayCalculation(calculationMethod, payMethod);
			Long roundedBulkAmount = bulkAmount == null ? null : roundDown(bulkAmount, roundUnit);
			managementDao.managePayItem(conn, action, itemId, itemName, taxType, taxFreeCode, taxFreeLimit,
					calculation, roundUnit, payMethod, attendanceItemId, roundedBulkAmount);
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 공제항목의 추가·수정·삭제 구분에 따라 데이터를 처리하고 결과를 반환한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 控除項目の追加・更新・削除区分に従ってデータを処理し、結果を返す。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void manageDeductItem(String action, Integer itemId, String itemName, String calculationMethod,
			int roundUnit, String note) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			managementDao.manageDeductItem(conn, action, itemId, itemName,
					defaultDeductionCalculation(itemName, calculationMethod), roundUnit, note);
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 산식을 입력하지 않으면 지급방식에 맞는 기본 계산 설명을 저장한다.
	// 사원정보와 항목 설정을 기준으로 지급계산의 초기 계산금액을 산출한다.
	// 입력금액과 기간·요율·절사단위를 적용하고 계산 중 발생할 수 있는 NULL이나 음수 결과를 안전하게 보정한다.
	// 社員情報と項目設定を基準に支給計算の初期計算金額を算出する。
	// 入力金額と期間・料率・端数処理単位を適用し、計算中に発生し得るNULLや負数結果を安全に補正する。
	private String defaultPayCalculation(String calculationMethod, String payMethod) {
		if (calculationMethod != null && !calculationMethod.trim().isEmpty()) return calculationMethod.trim();
		if ("일괄지급".equals(payMethod)) return "일괄지급액";
		if ("근태연계".equals(payMethod)) return "근태수량 × 지급단가";
		return "직접입력";
	}

	// 대표적인 법정 공제는 실무에서 사용하는 기본 산식을 안내값으로 저장한다.
	// 사원정보와 항목 설정을 기준으로 공제계산의 초기 계산금액을 산출한다.
	// 입력금액과 기간·요율·절사단위를 적용하고 계산 중 발생할 수 있는 NULL이나 음수 결과를 안전하게 보정한다.
	// 社員情報と項目設定を基準に控除計算の初期計算金額を算出する。
	// 入力金額と期間・料率・端数処理単位を適用し、計算中に発生し得るNULLや負数結果を安全に補正する。
	private String defaultDeductionCalculation(String itemName, String calculationMethod) {
		if (calculationMethod != null && !calculationMethod.trim().isEmpty()) return calculationMethod.trim();
		String name = itemName == null ? "" : itemName;
		if (name.contains("국민연금")) return "기준소득월액 × 4.5%";
		if (name.contains("건강보험")) return "보수월액 × 3.545%";
		if (name.contains("장기요양")) return "건강보험료 × 장기요양보험요율";
		if (name.contains("고용보험")) return "보수월액 × 0.9%";
		if (name.contains("지방소득세")) return "소득세 × 10%";
		if (name.contains("소득세")) return "근로소득 간이세액표";
		return "직접입력";
	}

	// 절사단위 미만 금액은 버림 처리하여 실제 지급 단위와 맞춘다.
	// 보험료와 세액 계산 결과를 지정된 절사단위에 맞춰 내림 처리한다.
	// 입력금액과 기간·요율·절사단위를 적용하고 계산 중 발생할 수 있는 NULL이나 음수 결과를 안전하게 보정한다.
	// 保険料と税額の計算結果を指定された端数処理単位に合わせて切り捨てる。
	// 入力金額と期間・料率・端数処理単位を適用し、計算中に発生し得るNULLや負数結果を安全に補正する。
	private long roundDown(long amount, int roundUnit) {
		return roundUnit > 1 ? amount / roundUnit * roundUnit : amount;
	}

	// 급여입력·관리 처리에 사용할 사원 데이터나 객체를 생성한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 給与入力・管理処理で使用する社員データまたはオブジェクトを生成する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void addEmployees(PayrollRun requestRun, int[] employeeIds) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			PayrollRun run = getOrCreateRun(conn, requestRun);
			for (int employeeId : employeeIds) {
				if (managementDao.selectPayrollEmployeeId(conn, run.getPayrollRunId(), employeeId) == null) {
					managementDao.insertPayrollEmployee(conn, run.getPayrollRunId(), employeeId);
				}
			}
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 선택되거나 식별된 사원 데이터를 삭제하고 관련 상태를 정리한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 選択または識別された社員データを削除し、関連状態を整理する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void deleteEmployees(PayrollRun requestRun, int[] employeeIds, boolean deleteAll) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			PayrollRun run = managementDao.selectRun(conn, requestRun.getPayYear(), requestRun.getPayMonth(),
					requestRun.getPaySeq(), requestRun.getIncomeType());
			if (run != null) {
				managementDao.deletePayrollEmployees(conn, run.getPayrollRunId(), employeeIds, deleteAll);
			}
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 입력값을 검증한 후 급여입력·관리 데이터를 트랜잭션으로 저장하거나 수정한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 入力値を検証した後、給与入力・管理データをトランザクションで登録または更新する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void save(PayrollRun requestRun, int employeeId, List<PayrollManagementItem> payItems,
			List<PayrollManagementItem> deductItems) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			PayrollRun run = getOrCreateRun(conn, requestRun);
			managementDao.updateRunDates(conn, run.getPayrollRunId(), requestRun);
			Integer payrollEmployeeId = managementDao.selectPayrollEmployeeId(conn, run.getPayrollRunId(), employeeId);
			if (payrollEmployeeId == null) {
				managementDao.insertPayrollEmployee(conn, run.getPayrollRunId(), employeeId);
				payrollEmployeeId = managementDao.selectPayrollEmployeeId(conn, run.getPayrollRunId(), employeeId);
			}
			copyItemNames(deductItems, managementDao.selectDeductItems(conn, payrollEmployeeId));
			applyDefaultDeductions(payItems, deductItems);
			managementDao.replaceEntries(conn, payrollEmployeeId, payItems, deductItems);
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 이전 급여내역의 지급·공제 항목명을 현재 화면 항목에 복사한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 前回給与明細の支給・控除項目名を現在の画面項目へコピーする。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	private void copyItemNames(List<PayrollManagementItem> requested, List<PayrollManagementItem> configured) {
		for (PayrollManagementItem item : requested) {
			for (PayrollManagementItem source : configured) {
				if (item.getItemCode() == source.getItemCode()) {
					item.setItemName(source.getItemName());
					break;
				}
			}
		}
	}

	// 사용자가 0원으로 둔 법정 공제만 간이 산식으로 계산한다. 직접 입력한 금액은 그대로 보존한다.
	// 조회값과 입력값을 조합하여 기본공제 목록 처리 데이터를 구성한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 照会値と入力値を組み合わせて初期控除一覧の処理データを構成する。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	private void applyDefaultDeductions(List<PayrollManagementItem> payItems,
			List<PayrollManagementItem> deductItems) {
		long grossPay = 0;
		for (PayrollManagementItem item : payItems) grossPay += item.getAmount();
		long healthInsurance = Math.round(grossPay * 0.03545);
		long incomeTax = Math.round(Math.max(0, grossPay - 1500000) * 0.06);
		for (PayrollManagementItem item : deductItems) {
			String name = item.getItemName() == null ? "" : item.getItemName();
			if (name.contains("건강보험") && item.getAmount() != 0) healthInsurance = item.getAmount();
			if (name.equals("소득세") && item.getAmount() != 0) incomeTax = item.getAmount();
		}
		for (PayrollManagementItem item : deductItems) {
			if (item.getAmount() != 0) continue;
			String name = item.getItemName() == null ? "" : item.getItemName();
			long amount = 0;
			if (name.contains("국민연금")) amount = Math.round(grossPay * 0.045);
			else if (name.contains("건강보험")) amount = Math.round(grossPay * 0.03545);
			else if (name.contains("장기요양")) amount = Math.round(healthInsurance * 0.1295);
			else if (name.contains("고용보험")) amount = Math.round(grossPay * 0.009);
			else if (name.equals("소득세")) amount = incomeTax;
			else if (name.contains("지방소득세")) amount = Math.round(incomeTax * 0.1);
			item.setAmount(roundDown(amount, 10));
		}
	}

	// 급여입력·관리 처리에 필요한 OrCreate급여 회차를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 給与入力・管理処理に必要なOrCreate給与回次を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	private PayrollRun getOrCreateRun(Connection conn, PayrollRun requestRun) throws SQLException {
		PayrollRun run = managementDao.selectRun(conn, requestRun.getPayYear(), requestRun.getPayMonth(),
				requestRun.getPaySeq(), requestRun.getIncomeType());
		if (run == null) {
			requestRun.setPayrollRunId(managementDao.insertRun(conn, requestRun));
			return requestRun;
		}
		return run;
	}

	// 급여입력·관리 처리에 필요한 사원 데이터를 조회하여 반환한다.
	// 조회 전용 Connection으로 관련 DAO 결과를 조합하며 데이터 변경이 없으므로 별도의 commit이나 rollback을 수행하지 않는다.
	// 給与入力・管理処理に必要な社員データを照会して返す。
	// 照会専用Connectionで関連DAOの結果を組み合わせ、データ変更がないため個別のcommitやrollbackは実行しない。
	private PayrollManagementEmployee selectEmployee(List<PayrollManagementEmployee> employees, Integer employeeId) {
		// 최초 진입 시 첫 사원을 자동 선택하지 않는다.
		// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
		if (employees.isEmpty() || employeeId == null) {
			return null;
		}
		for (PayrollManagementEmployee employee : employees) {
			if (employee.getEmployeeId() == employeeId) {
				return employee;
			}
		}
		return null;
	}

	// 조회된 금액과 업무 규칙을 이용해 합계정보 계산 결과를 생성한다.
	// 입력금액과 기간·요율·절사단위를 적용하고 계산 중 발생할 수 있는 NULL이나 음수 결과를 안전하게 보정한다.
	// 照会した金額と業務ルールを使用して合計情報の計算結果を生成する。
	// 入力金額と期間・料率・端数処理単位を適用し、計算中に発生し得るNULLや負数結果を安全に補正する。
	private PayrollTotals calculateTotals(List<PayrollManagementItem> payItems,
			List<PayrollManagementItem> deductItems) {
		PayrollTotals totals = new PayrollTotals();
		long payTotal = 0;
		long deductTotal = 0;
		for (PayrollManagementItem item : payItems) {
			payTotal += item.getAmount();
		}
		for (PayrollManagementItem item : deductItems) {
			deductTotal += item.getAmount();
		}
		totals.setGrossPayment(payTotal);
		totals.setTotalDeduction(deductTotal);
		return totals;
	}

	// 조회값과 입력값을 조합하여 급여 회차 처리 데이터를 구성한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 照会値と入力値を組み合わせて給与回次の処理データを構成する。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	public PayrollRun makeRun(String year, String month, String sequence, String incomeType, String startDate,
			String endDate, String payDate) {
		int numericYear = Integer.parseInt(year);
		int numericMonth = Integer.parseInt(month);
		YearMonth yearMonth = YearMonth.of(numericYear, numericMonth);
		PayrollRun run = new PayrollRun();
		run.setPayYear(String.format("%04d", numericYear));
		run.setPayMonth(String.format("%02d", numericMonth));
		run.setPaySeq(String.format("%02d", Integer.parseInt(sequence)));
		if ("daily".equals(incomeType)) {
			run.setIncomeType("2");
		} else if ("business".equals(incomeType)) {
			run.setIncomeType("1");
		} else {
			run.setIncomeType("0");
		}
		run.setCalcStartDate(toDate(startDate, yearMonth.atDay(1)));
		run.setCalcEndDate(toDate(endDate, yearMonth.atEndOfMonth()));
		run.setPayDate(toDate(payDate, yearMonth.atEndOfMonth()));
		return run;
	}

	// 입력 데이터를 일자 처리에 필요한 형식으로 변환한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 入力データを日付処理に必要な形式へ変換する。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	private Date toDate(String value, LocalDate defaultDate) {
		LocalDate date = value == null || value.trim().isEmpty() ? defaultDate : LocalDate.parse(value);
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
}
