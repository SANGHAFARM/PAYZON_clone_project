package erp.payroll.dto;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import erp.settings.model.Department;

// 급여대장 목록과 상세 화면의 조회 결과를 묶어서 전달한다.
// 급여등록화면 데이터 처리에 필요한 값을 계층 간에 전달한다.
// 給与登録画面データ処理に必要な値を各階層間で受け渡す。
public class PayrollRegisterPage {

	public static class PayrollRegisterListPage {
		private List<PayrollRegisterItem> registers;
		private PayrollRegisterTotals totals;
		private PayrollRegisterPageInfo pageInfo;
		// 급여등록화면 데이터 객체에 저장된 급여등록목록화면 데이터 값을 반환한다.
		// Controller·Service·DAO·JSP 사이에서 동일한 데이터를 일관된 구조로 전달하기 위해 사용한다.
		// 給与登録画面データオブジェクトに保存された給与登録一覧画面データの値を返す。
		// Controller・Service・DAO・JSP間で同じデータを一貫した構造として受け渡すために使用する。
		public PayrollRegisterListPage(List<PayrollRegisterItem> registers, PayrollRegisterTotals totals, PayrollRegisterPageInfo pageInfo) {
			this.registers = registers; this.totals = totals; this.pageInfo = pageInfo;
		}
		// 급여등록화면 데이터 객체에 저장된 발급대장 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された発行台帳の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public List<PayrollRegisterItem> getRegisters() { return registers; }
		// 급여등록화면 데이터 객체에 저장된 합계정보 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された合計情報の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public PayrollRegisterTotals getTotals() { return totals; }
		// 급여등록화면 데이터 객체에 저장된 화면 데이터정보 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された画面データ情報の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public PayrollRegisterPageInfo getPageInfo() { return pageInfo; }
	}

	public static class PayrollRegisterDetailPage {
		private PayrollRegisterItem register;
		private List<PayrollRegisterColumn> paymentItems;
		private List<PayrollRegisterColumn> deductionItems;
		private List<PayrollRegisterEmployee> employees;
		private PayrollRegisterTotals totals;
		private List<Department> departments;
		// 급여등록화면 데이터 객체에 저장된 등록 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された登録の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public PayrollRegisterItem getRegister() { return register; }
		public void setRegister(PayrollRegisterItem value) { register = value; }
		// 급여등록화면 데이터 객체에 저장된 지급항목 목록 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された支給項目一覧の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public List<PayrollRegisterColumn> getPaymentItems() { return paymentItems; }
		public void setPaymentItems(List<PayrollRegisterColumn> value) { paymentItems = value; }
		// 급여등록화면 데이터 객체에 저장된 공제항목 목록 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された控除項目一覧の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public List<PayrollRegisterColumn> getDeductionItems() { return deductionItems; }
		public void setDeductionItems(List<PayrollRegisterColumn> value) { deductionItems = value; }
		// 급여등록화면 데이터 객체에 저장된 사원 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された社員の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public List<PayrollRegisterEmployee> getEmployees() { return employees; }
		public void setEmployees(List<PayrollRegisterEmployee> value) { employees = value; }
		// 급여등록화면 데이터 객체에 저장된 합계정보 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された合計情報の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public PayrollRegisterTotals getTotals() { return totals; }
		public void setTotals(PayrollRegisterTotals value) { totals = value; }
		// 급여등록화면 데이터 객체에 저장된 부서 목록 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された部署一覧の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public List<Department> getDepartments() { return departments; }
		public void setDepartments(List<Department> value) { departments = value; }
	}

	public static class PayrollRegisterItem {
		private int registerId;
		private String paymentYear;
		private String paymentYearMonth;
		private String paymentRoundName;
		private String incomeType;
		private Date calculationStart;
		private Date calculationEnd;
		private Date paymentDate;
		private int employeeCount;
		private long totalPayment;
		private long totalDeduction;
		// 급여등록화면 데이터 객체에 저장된 등록식별번호 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された登録識別番号の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public int getRegisterId() { return registerId; }
		public void setRegisterId(int value) { registerId = value; }
		// 급여등록화면 데이터 객체에 저장된 지급연도 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された支給年度の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getPaymentYear() { return paymentYear; }
		public void setPaymentYear(String value) { paymentYear = value; }
		// 급여등록화면 데이터 객체에 저장된 지급연도월 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された支給年度月の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getPaymentYearMonth() { return paymentYearMonth; }
		public void setPaymentYearMonth(String value) { paymentYearMonth = value; }
		// 급여등록화면 데이터 객체에 저장된 지급절사명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された支給端数処理名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getPaymentRoundName() { return paymentRoundName; }
		public void setPaymentRoundName(String value) { paymentRoundName = value; }
		// 급여등록화면 데이터 객체에 저장된 소득구분 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された所得区分の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getIncomeType() { return incomeType; }
		public void setIncomeType(String value) { incomeType = value; }
		// 급여등록화면 데이터 객체에 저장된 계산Start 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された計算Startの値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public Date getCalculationStart() { return calculationStart; }
		public void setCalculationStart(Date value) { calculationStart = value; }
		// 급여등록화면 데이터 객체에 저장된 계산End 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された計算Endの値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public Date getCalculationEnd() { return calculationEnd; }
		public void setCalculationEnd(Date value) { calculationEnd = value; }
		// 급여등록화면 데이터 객체에 저장된 지급일자 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された支給日付の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public Date getPaymentDate() { return paymentDate; }
		public void setPaymentDate(Date value) { paymentDate = value; }
		// 급여등록화면 데이터 객체에 저장된 사원건수 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された社員件数の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public int getEmployeeCount() { return employeeCount; }
		public void setEmployeeCount(int value) { employeeCount = value; }
		// 급여등록화면 데이터 객체에 저장된 합계지급 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された合計支給の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getTotalPayment() { return totalPayment; }
		public void setTotalPayment(long value) { totalPayment = value; }
		// 급여등록화면 데이터 객체에 저장된 합계공제 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された合計控除の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getTotalDeduction() { return totalDeduction; }
		public void setTotalDeduction(long value) { totalDeduction = value; }
		// 급여등록화면 데이터 객체에 저장된 Net지급 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存されたNet支給の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getNetPayment() { return totalPayment - totalDeduction; }
	}

	public static class PayrollRegisterEmployee {
		private int employeeId;
		private String employmentTypeName;
		private String employeeName;
		private String departmentName;
		private String positionName;
		private Map<Integer, Long> paymentAmounts = new LinkedHashMap<>();
		private Map<Integer, Long> deductionAmounts = new LinkedHashMap<>();
		// 급여등록화면 데이터 객체에 저장된 사원식별번호 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された社員識別番号の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public int getEmployeeId() { return employeeId; }
		public void setEmployeeId(int value) { employeeId = value; }
		// 급여등록화면 데이터 객체에 저장된 고용구분명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された雇用区分名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getEmploymentTypeName() { return employmentTypeName; }
		public void setEmploymentTypeName(String value) { employmentTypeName = value; }
		// 급여등록화면 데이터 객체에 저장된 사원명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された社員名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getEmployeeName() { return employeeName; }
		public void setEmployeeName(String value) { employeeName = value; }
		// 급여등록화면 데이터 객체에 저장된 부서명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された部署名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getDepartmentName() { return departmentName; }
		public void setDepartmentName(String value) { departmentName = value; }
		// 급여등록화면 데이터 객체에 저장된 직위명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された役職名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getPositionName() { return positionName; }
		public void setPositionName(String value) { positionName = value; }
		// 급여등록화면 데이터 객체에 저장된 지급금액 목록 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された支給金額一覧の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public Map<Integer, Long> getPaymentAmounts() { return paymentAmounts; }
		// 급여등록화면 데이터 객체에 저장된 공제금액 목록 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された控除金額一覧の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public Map<Integer, Long> getDeductionAmounts() { return deductionAmounts; }
		// 급여등록화면 데이터 객체에 저장된 합계지급 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された合計支給の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getTotalPayment() { return sum(paymentAmounts); }
		// 급여등록화면 데이터 객체에 저장된 합계공제 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された合計控除の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getTotalDeduction() { return sum(deductionAmounts); }
		// 급여등록화면 데이터 객체에 저장된 Net지급 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存されたNet支給の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getNetPayment() { return getTotalPayment() - getTotalDeduction(); }
		private long sum(Map<Integer, Long> values) { long total = 0; for (Long value : values.values()) total += value == null ? 0 : value; return total; }
	}

	public static class PayrollRegisterTotals {
		private long totalPayment;
		private long totalDeduction;
		private Map<Integer, Long> paymentAmounts = new LinkedHashMap<>();
		private Map<Integer, Long> deductionAmounts = new LinkedHashMap<>();
		// 급여등록화면 데이터 객체에 저장된 합계지급 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された合計支給の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getTotalPayment() { return totalPayment; }
		public void setTotalPayment(long value) { totalPayment = value; }
		// 급여등록화면 데이터 객체에 저장된 합계공제 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された合計控除の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getTotalDeduction() { return totalDeduction; }
		public void setTotalDeduction(long value) { totalDeduction = value; }
		// 급여등록화면 데이터 객체에 저장된 Net지급 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存されたNet支給の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getNetPayment() { return totalPayment - totalDeduction; }
		// 급여등록화면 데이터 객체에 저장된 지급금액 목록 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された支給金額一覧の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public Map<Integer, Long> getPaymentAmounts() { return paymentAmounts; }
		// 급여등록화면 데이터 객체에 저장된 공제금액 목록 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された控除金額一覧の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public Map<Integer, Long> getDeductionAmounts() { return deductionAmounts; }
	}

	public static class PayrollRegisterPageInfo {
		private int number;
		private int totalPages;
		public PayrollRegisterPageInfo(int number, int totalPages) { this.number = number; this.totalPages = totalPages; }
		// 급여등록화면 데이터 객체에 저장된 번호 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された番号の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public int getNumber() { return number; }
		// 급여등록화면 데이터 객체에 저장된 합계Pages 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された合計Pagesの値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public int getTotalPages() { return totalPages; }
	}

	public static class PayrollEmploymentTypeOption {
		private String code;
		private String name;
		public PayrollEmploymentTypeOption(String code, String name) { this.code = code; this.name = name; }
		// 급여등록화면 데이터 객체에 저장된 코드 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存されたコードの値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getCode() { return code; }
		// 급여등록화면 데이터 객체에 저장된 명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与登録画面データオブジェクトに保存された名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getName() { return name; }
	}
}
