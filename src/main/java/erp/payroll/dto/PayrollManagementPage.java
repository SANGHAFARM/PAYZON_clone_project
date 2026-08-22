package erp.payroll.dto;

import java.util.List;

import erp.payroll.model.PayrollRun;
import erp.settings.model.Department;
import erp.settings.model.AttendanceItem;
import erp.settings.model.TaxFreeItem;

// 급여입력 화면에 필요한 조회 결과를 한 번에 전달한다.
// 급여입력·관리화면 데이터 처리에 필요한 값을 계층 간에 전달한다.
// 給与入力・管理画面データ処理に必要な値を各階層間で受け渡す。
public class PayrollManagementPage {

	private PayrollRun run;
	private List<PayrollManagementEmployee> paymentEmployees;
	private PayrollManagementEmployee selectedEmployee;
	private List<PayrollManagementItem> paymentGiveItems;
	private List<PayrollManagementItem> paymentDeductionItems;
	private PayrollTotals paymentTotals;
	private PayrollEmployeePage availableEmployeePage;
	private List<Department> departments;
	private List<PayrollPositionOption> positions;
	private List<PayrollPeriodOption> previousPaymentPeriods;
	private List<TaxFreeItem> taxFreeItems;
	private List<AttendanceItem> attendanceItems;

	// 급여입력·관리화면 데이터 객체에 저장된 급여 회차 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与入力・管理画面データオブジェクトに保存された給与回次の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public PayrollRun getRun() {
		return run;
	}

	// 전달받은 급여 회차 값을 급여입력·관리화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った給与回次の値を給与入力・管理画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setRun(PayrollRun run) {
		this.run = run;
	}

	// 급여입력·관리화면 데이터 객체에 저장된 지급사원 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与入力・管理画面データオブジェクトに保存された支給社員の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<PayrollManagementEmployee> getPaymentEmployees() {
		return paymentEmployees;
	}

	// 전달받은 지급사원 값을 급여입력·관리화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給社員の値を給与入力・管理画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPaymentEmployees(List<PayrollManagementEmployee> paymentEmployees) {
		this.paymentEmployees = paymentEmployees;
	}

	// 급여입력·관리화면 데이터 객체에 저장된 Selected사원 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与入力・管理画面データオブジェクトに保存されたSelected社員の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public PayrollManagementEmployee getSelectedEmployee() {
		return selectedEmployee;
	}

	// 전달받은 Selected사원 값을 급여입력·관리화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったSelected社員の値を給与入力・管理画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setSelectedEmployee(PayrollManagementEmployee selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}

	// 급여입력·관리화면 데이터 객체에 저장된 지급지급항목 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与入力・管理画面データオブジェクトに保存された支給支給項目一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<PayrollManagementItem> getPaymentGiveItems() {
		return paymentGiveItems;
	}

	// 전달받은 지급지급항목 목록 값을 급여입력·관리화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給支給項目一覧の値を給与入力・管理画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPaymentGiveItems(List<PayrollManagementItem> paymentGiveItems) {
		this.paymentGiveItems = paymentGiveItems;
	}

	// 급여입력·관리화면 데이터 객체에 저장된 지급공제항목 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与入力・管理画面データオブジェクトに保存された支給控除項目一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<PayrollManagementItem> getPaymentDeductionItems() {
		return paymentDeductionItems;
	}

	// 전달받은 지급공제항목 목록 값을 급여입력·관리화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給控除項目一覧の値を給与入力・管理画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPaymentDeductionItems(List<PayrollManagementItem> paymentDeductionItems) {
		this.paymentDeductionItems = paymentDeductionItems;
	}

	// 급여입력·관리화면 데이터 객체에 저장된 지급합계정보 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与入力・管理画面データオブジェクトに保存された支給合計情報の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public PayrollTotals getPaymentTotals() {
		return paymentTotals;
	}

	// 전달받은 지급합계정보 값을 급여입력·관리화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給合計情報の値を給与入力・管理画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPaymentTotals(PayrollTotals paymentTotals) {
		this.paymentTotals = paymentTotals;
	}

	// 급여입력·관리화면 데이터 객체에 저장된 사용가능사원화면 데이터 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与入力・管理画面データオブジェクトに保存された利用可能社員画面データの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public PayrollEmployeePage getAvailableEmployeePage() {
		return availableEmployeePage;
	}

	// 전달받은 사용가능사원화면 데이터 값을 급여입력·관리화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った利用可能社員画面データの値を給与入力・管理画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAvailableEmployeePage(PayrollEmployeePage availableEmployeePage) {
		this.availableEmployeePage = availableEmployeePage;
	}

	// 급여입력·관리화면 데이터 객체에 저장된 부서 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与入力・管理画面データオブジェクトに保存された部署一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<Department> getDepartments() {
		return departments;
	}

	// 전달받은 부서 목록 값을 급여입력·관리화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った部署一覧の値を給与入力・管理画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	// 급여입력·관리화면 데이터 객체에 저장된 직위 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与入力・管理画面データオブジェクトに保存された役職一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<PayrollPositionOption> getPositions() {
		return positions;
	}

	// 전달받은 직위 목록 값을 급여입력·관리화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った役職一覧の値を給与入力・管理画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPositions(List<PayrollPositionOption> positions) {
		this.positions = positions;
	}

	// 급여입력·관리화면 데이터 객체에 저장된 이전 회차지급Periods 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与入力・管理画面データオブジェクトに保存された前回支給Periodsの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<PayrollPeriodOption> getPreviousPaymentPeriods() {
		return previousPaymentPeriods;
	}

	// 전달받은 이전 회차지급Periods 값을 급여입력·관리화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った前回支給Periodsの値を給与入力・管理画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPreviousPaymentPeriods(List<PayrollPeriodOption> previousPaymentPeriods) {
		this.previousPaymentPeriods = previousPaymentPeriods;
	}

	// 급여입력·관리화면 데이터 객체에 저장된 세금비과세항목 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与入力・管理画面データオブジェクトに保存された税金非課税項目一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<TaxFreeItem> getTaxFreeItems() {
		return taxFreeItems;
	}

	// 전달받은 세금비과세항목 목록 값을 급여입력·관리화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った税金非課税項目一覧の値を給与入力・管理画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTaxFreeItems(List<TaxFreeItem> taxFreeItems) {
		this.taxFreeItems = taxFreeItems;
	}

	// 급여입력·관리화면 데이터 객체에 저장된 근태항목 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与入力・管理画面データオブジェクトに保存された勤怠項目一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<AttendanceItem> getAttendanceItems() {
		return attendanceItems;
	}

	// 전달받은 근태항목 목록 값을 급여입력·관리화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った勤怠項目一覧の値を給与入力・管理画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAttendanceItems(List<AttendanceItem> attendanceItems) {
		this.attendanceItems = attendanceItems;
	}

	// 급여입력 화면에 표시할 사원별 급여 정보
	// 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
	public static class PayrollManagementEmployee {
		private int employeeId;
		private String employmentType;
		private String employeeNo;
		private String name;
		private String departmentName;
		private String positionName;
		private String statusName;
		private long grossPayment;
		private long totalDeduction;
		// 급여입력·관리화면 데이터 객체에 저장된 사원식별번호 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与入力・管理画面データオブジェクトに保存された社員識別番号の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public int getEmployeeId() { return employeeId; }
		public void setEmployeeId(int value) { employeeId = value; }
		// 급여입력·관리화면 데이터 객체에 저장된 고용구분 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与入力・管理画面データオブジェクトに保存された雇用区分の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getEmploymentType() { return employmentType; }
		public void setEmploymentType(String value) { employmentType = value; }
		// 급여입력·관리화면 데이터 객체에 저장된 사원번호 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与入力・管理画面データオブジェクトに保存された社員番号の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getEmployeeNo() { return employeeNo; }
		public void setEmployeeNo(String value) { employeeNo = value; }
		// 급여입력·관리화면 데이터 객체에 저장된 명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与入力・管理画面データオブジェクトに保存された名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getName() { return name; }
		public void setName(String value) { name = value; }
		// 급여입력·관리화면 데이터 객체에 저장된 부서명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与入力・管理画面データオブジェクトに保存された部署名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getDepartmentName() { return departmentName; }
		public void setDepartmentName(String value) { departmentName = value; }
		// 급여입력·관리화면 데이터 객체에 저장된 직위명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与入力・管理画面データオブジェクトに保存された役職名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getPositionName() { return positionName; }
		public void setPositionName(String value) { positionName = value; }
		// 급여입력·관리화면 데이터 객체에 저장된 상태명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与入力・管理画面データオブジェクトに保存された状態名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getStatusName() { return statusName; }
		public void setStatusName(String value) { statusName = value; }
		// 급여입력·관리화면 데이터 객체에 저장된 Gross지급 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与入力・管理画面データオブジェクトに保存されたGross支給の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getGrossPayment() { return grossPayment; }
		public void setGrossPayment(long value) { grossPayment = value; }
		// 급여입력·관리화면 데이터 객체에 저장된 합계공제 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与入力・管理画面データオブジェクトに保存された合計控除の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getTotalDeduction() { return totalDeduction; }
		public void setTotalDeduction(long value) { totalDeduction = value; }
		// 급여입력·관리화면 데이터 객체에 저장된 Net지급 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与入力・管理画面データオブジェクトに保存されたNet支給の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getNetPayment() { return grossPayment - totalDeduction; }
	}

	public static class PayrollEmployeePage {
		private List<PayrollManagementEmployee> content;
		private int totalPages;
		public PayrollEmployeePage(List<PayrollManagementEmployee> content, int totalPages) { this.content = content; this.totalPages = totalPages; }
		// 급여입력·관리화면 데이터 객체에 저장된 내용 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与入力・管理画面データオブジェクトに保存された内容の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public List<PayrollManagementEmployee> getContent() { return content; }
		// 급여입력·관리화면 데이터 객체에 저장된 합계Pages 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与入力・管理画面データオブジェクトに保存された合計Pagesの値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public int getTotalPages() { return totalPages; }
	}

	public static class PayrollTotals {
		private long grossPayment;
		private long totalDeduction;
		// 급여입력·관리화면 데이터 객체에 저장된 Gross지급 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与入力・管理画面データオブジェクトに保存されたGross支給の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getGrossPayment() { return grossPayment; }
		public void setGrossPayment(long value) { grossPayment = value; }
		// 급여입력·관리화면 데이터 객체에 저장된 합계공제 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与入力・管理画面データオブジェクトに保存された合計控除の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getTotalDeduction() { return totalDeduction; }
		public void setTotalDeduction(long value) { totalDeduction = value; }
		// 급여입력·관리화면 데이터 객체에 저장된 Net지급 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与入力・管理画面データオブジェクトに保存されたNet支給の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getNetPayment() { return grossPayment - totalDeduction; }
	}

	public static class PayrollPositionOption {
		private int positionId;
		private String positionName;
		public PayrollPositionOption(int id, String name) { positionId = id; positionName = name; }
		// 급여입력·관리화면 데이터 객체에 저장된 직위식별번호 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与入力・管理画面データオブジェクトに保存された役職識別番号の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public int getPositionId() { return positionId; }
		// 급여입력·관리화면 데이터 객체에 저장된 직위명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与入力・管理画面データオブジェクトに保存された役職名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getPositionName() { return positionName; }
	}

	public static class PayrollPeriodOption {
		private int periodId;
		private String periodName;
		public PayrollPeriodOption(int id, String name) { periodId = id; periodName = name; }
		// 급여입력·관리화면 데이터 객체에 저장된 기간식별번호 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与入力・管理画面データオブジェクトに保存された期間識別番号の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public int getPeriodId() { return periodId; }
		// 급여입력·관리화면 데이터 객체에 저장된 기간명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与入力・管理画面データオブジェクトに保存された期間名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getPeriodName() { return periodName; }
	}
}
