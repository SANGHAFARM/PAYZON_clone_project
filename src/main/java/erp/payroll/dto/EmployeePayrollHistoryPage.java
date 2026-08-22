package erp.payroll.dto;

import java.util.List;
import erp.settings.model.Department;

// 사원별 급여내역 화면의 조회 결과를 전달한다.
// 사원급여이력화면 데이터 처리에 필요한 값을 계층 간에 전달한다.
// 社員給与履歴画面データ処理に必要な値を各階層間で受け渡す。
public class EmployeePayrollHistoryPage {

	private EmployeePayrollHistoryEmployee selectedEmployee;
	private List<EmployeePayrollHistoryEmployee> employees;
	private List<Department> departments;
	private List<EmployeePayrollHistoryItem> histories;
	private EmployeePayrollHistoryTotal total;
	private EmployeePayrollHistoryPageInfo pageInfo;

	// 사원급여이력화면 데이터 객체에 저장된 Selected사원 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員給与履歴画面データオブジェクトに保存されたSelected社員の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public EmployeePayrollHistoryEmployee getSelectedEmployee() { return selectedEmployee; }
	public void setSelectedEmployee(EmployeePayrollHistoryEmployee value) { selectedEmployee = value; }
	// 사원급여이력화면 데이터 객체에 저장된 사원 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員給与履歴画面データオブジェクトに保存された社員の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<EmployeePayrollHistoryEmployee> getEmployees() { return employees; }
	public void setEmployees(List<EmployeePayrollHistoryEmployee> value) { employees = value; }
	// 사원급여이력화면 데이터 객체에 저장된 부서 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員給与履歴画面データオブジェクトに保存された部署一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<Department> getDepartments() { return departments; }
	public void setDepartments(List<Department> value) { departments = value; }
	// 사원급여이력화면 데이터 객체에 저장된 이력 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員給与履歴画面データオブジェクトに保存された履歴一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<EmployeePayrollHistoryItem> getHistories() { return histories; }
	public void setHistories(List<EmployeePayrollHistoryItem> value) { histories = value; }
	// 사원급여이력화면 데이터 객체에 저장된 합계 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員給与履歴画面データオブジェクトに保存された合計の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public EmployeePayrollHistoryTotal getTotal() { return total; }
	public void setTotal(EmployeePayrollHistoryTotal value) { total = value; }
	// 사원급여이력화면 데이터 객체에 저장된 화면 데이터정보 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員給与履歴画面データオブジェクトに保存された画面データ情報の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public EmployeePayrollHistoryPageInfo getPageInfo() { return pageInfo; }
	public void setPageInfo(EmployeePayrollHistoryPageInfo value) { pageInfo = value; }

	// 사원별 급여내역에서 사용하는 사원 정보
	// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
	public static class EmployeePayrollHistoryEmployee {
		private int employeeId;
		private String employmentTypeName;
		private String employeeNumber;
		private String employeeName;
		private String departmentName;
		private String positionName;
		private String status;

		// 사원급여이력화면 데이터 객체에 저장된 사원식별번호 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存された社員識別番号の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public int getEmployeeId() { return employeeId; }
		public void setEmployeeId(int value) { employeeId = value; }
		// 사원급여이력화면 데이터 객체에 저장된 고용구분명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存された雇用区分名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getEmploymentTypeName() { return employmentTypeName; }
		public void setEmploymentTypeName(String value) { employmentTypeName = value; }
		// 사원급여이력화면 데이터 객체에 저장된 사원번호 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存された社員番号の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getEmployeeNumber() { return employeeNumber; }
		public void setEmployeeNumber(String value) { employeeNumber = value; }
		// 사원급여이력화면 데이터 객체에 저장된 사원명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存された社員名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getEmployeeName() { return employeeName; }
		public void setEmployeeName(String value) { employeeName = value; }
		// 사원급여이력화면 데이터 객체에 저장된 부서명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存された部署名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getDepartmentName() { return departmentName; }
		public void setDepartmentName(String value) { departmentName = value; }
		// 사원급여이력화면 데이터 객체에 저장된 직위명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存された役職名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getPositionName() { return positionName; }
		public void setPositionName(String value) { positionName = value; }
		// 사원급여이력화면 데이터 객체에 저장된 상태 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存された状態の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getStatus() { return status; }
		public void setStatus(String value) { status = value; }
	}

	// 월별 급여 및 법정 공제 내역
	// 基準日と期間の境界を確認し、照会・計算に使用できる日付形式へ変換する。
	public static class EmployeePayrollHistoryItem {
		private String paymentMonth;
		private String paymentRound;
		private long standardMonthlyIncome;
		private long totalPayment;
		private long totalDeduction;
		private long nationalPension;
		private long healthInsurance;
		private long longTermCareInsurance;
		private long employmentInsurance;
		private long incomeTax;
		private long localIncomeTax;

		// 사원급여이력화면 데이터 객체에 저장된 지급월 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存された支給月の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getPaymentMonth() { return paymentMonth; }
		public void setPaymentMonth(String value) { paymentMonth = value; }
		// 사원급여이력화면 데이터 객체에 저장된 지급절사 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存された支給端数処理の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getPaymentRound() { return paymentRound; }
		public void setPaymentRound(String value) { paymentRound = value; }
		// 사원급여이력화면 데이터 객체에 저장된 Standard월간소득 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存されたStandard月間所得の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getStandardMonthlyIncome() { return standardMonthlyIncome; }
		public void setStandardMonthlyIncome(long value) { standardMonthlyIncome = value; }
		// 사원급여이력화면 데이터 객체에 저장된 합계지급 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存された合計支給の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getTotalPayment() { return totalPayment; }
		public void setTotalPayment(long value) { totalPayment = value; }
		// 사원급여이력화면 데이터 객체에 저장된 합계공제 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存された合計控除の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getTotalDeduction() { return totalDeduction; }
		public void setTotalDeduction(long value) { totalDeduction = value; }
		// 사원급여이력화면 데이터 객체에 저장된 Net지급 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存されたNet支給の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getNetPayment() { return totalPayment - totalDeduction; }
		// 사원급여이력화면 데이터 객체에 저장된 국민연금 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存された国民年金の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getNationalPension() { return nationalPension; }
		public void setNationalPension(long value) { nationalPension = value; }
		// 사원급여이력화면 데이터 객체에 저장된 건강보험 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存された健康保険の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getHealthInsurance() { return healthInsurance; }
		public void setHealthInsurance(long value) { healthInsurance = value; }
		// 사원급여이력화면 데이터 객체에 저장된 정수Term장기요양보험 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存された整数Term介護保険の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getLongTermCareInsurance() { return longTermCareInsurance; }
		public void setLongTermCareInsurance(long value) { longTermCareInsurance = value; }
		// 사원급여이력화면 데이터 객체에 저장된 고용보험 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存された雇用保険の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getEmploymentInsurance() { return employmentInsurance; }
		public void setEmploymentInsurance(long value) { employmentInsurance = value; }
		// 사원급여이력화면 데이터 객체에 저장된 소득세금 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存された所得税金の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getIncomeTax() { return incomeTax; }
		public void setIncomeTax(long value) { incomeTax = value; }
		// 사원급여이력화면 데이터 객체에 저장된 Local소득세금 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存されたLocal所得税金の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getLocalIncomeTax() { return localIncomeTax; }
		public void setLocalIncomeTax(long value) { localIncomeTax = value; }
	}

	public static class EmployeePayrollHistoryTotal extends EmployeePayrollHistoryItem { }

	// 사원별 급여내역 페이징 정보
	// 全件数と表示件数からページ範囲を計算し、現在ページに該当するデータだけを取得する。
	public static class EmployeePayrollHistoryPageInfo {
		private int currentPage;
		private int totalPages;
		public EmployeePayrollHistoryPageInfo(int currentPage, int totalPages) { this.currentPage = currentPage; this.totalPages = totalPages; }
		// 사원급여이력화면 데이터 객체에 저장된 Current화면 데이터 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存されたCurrent画面データの値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public int getCurrentPage() { return currentPage; }
		public int getStartPage() { return totalPages == 0 ? 1 : ((currentPage - 1) / 5) * 5 + 1; }
		// 사원급여이력화면 데이터 객체에 저장된 End화면 데이터 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存されたEnd画面データの値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public int getEndPage() { return Math.min(getStartPage() + 4, totalPages); }
		// Has이전 회차 조건의 충족 여부를 확인하여 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// Has前回条件を満たしているか確認して返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public boolean isHasPrevious() { return getStartPage() > 1; }
		// 사원급여이력화면 데이터 객체에 저장된 이전 회차화면 데이터 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存された前回画面データの値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public int getPreviousPage() { return Math.max(1, getStartPage() - 1); }
		// Has다음 조건의 충족 여부를 확인하여 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// Has次条件を満たしているか確認して返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public boolean isHasNext() { return getEndPage() < totalPages; }
		// 사원급여이력화면 데이터 객체에 저장된 다음화면 데이터 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 社員給与履歴画面データオブジェクトに保存された次画面データの値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public int getNextPage() { return Math.min(totalPages, getEndPage() + 1); }
	}
}
