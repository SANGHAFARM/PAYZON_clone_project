package erp.payroll.dto;

import java.util.Date;
import java.util.List;

import erp.payroll.model.PayrollRun;
import erp.settings.model.Department;

// 일용직 급여입력 화면에 필요한 조회 결과를 전달한다.
// 일용직근로자지급화면 데이터 처리에 필요한 값을 계층 간에 전달한다.
// 日雇い労働者支給画面データ処理に必要な値を各階層間で受け渡す。
public class DayWorkerPaymentPage {

	private PayrollRun run;
	private List<DayWorkerPaymentEmployee> paymentEmployees;
	private DayWorkerPaymentEmployee selectedEmployee;
	private DayWorkerEmployeePage availableEmployeePage;
	private List<Department> departments;

	// 일용직근로자지급화면 데이터 객체에 저장된 급여 회차 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給画面データオブジェクトに保存された給与回次の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public PayrollRun getRun() {
		return run;
	}

	// 전달받은 급여 회차 값을 일용직근로자지급화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った給与回次の値を日雇い労働者支給画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setRun(PayrollRun run) {
		this.run = run;
	}

	// 일용직근로자지급화면 데이터 객체에 저장된 지급사원 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給画面データオブジェクトに保存された支給社員の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<DayWorkerPaymentEmployee> getPaymentEmployees() {
		return paymentEmployees;
	}

	// 전달받은 지급사원 값을 일용직근로자지급화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給社員の値を日雇い労働者支給画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPaymentEmployees(List<DayWorkerPaymentEmployee> paymentEmployees) {
		this.paymentEmployees = paymentEmployees;
	}

	// 일용직근로자지급화면 데이터 객체에 저장된 Selected사원 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給画面データオブジェクトに保存されたSelected社員の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public DayWorkerPaymentEmployee getSelectedEmployee() {
		return selectedEmployee;
	}

	// 전달받은 Selected사원 값을 일용직근로자지급화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったSelected社員の値を日雇い労働者支給画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setSelectedEmployee(DayWorkerPaymentEmployee selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}

	// 일용직근로자지급화면 데이터 객체에 저장된 사용가능사원화면 데이터 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給画面データオブジェクトに保存された利用可能社員画面データの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public DayWorkerEmployeePage getAvailableEmployeePage() {
		return availableEmployeePage;
	}

	// 전달받은 사용가능사원화면 데이터 값을 일용직근로자지급화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った利用可能社員画面データの値を日雇い労働者支給画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAvailableEmployeePage(DayWorkerEmployeePage availableEmployeePage) {
		this.availableEmployeePage = availableEmployeePage;
	}

	// 일용직근로자지급화면 데이터 객체에 저장된 부서 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給画面データオブジェクトに保存された部署一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<Department> getDepartments() {
		return departments;
	}

	// 전달받은 부서 목록 값을 일용직근로자지급화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った部署一覧の値を日雇い労働者支給画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	// 급여 대상에 추가할 일용직 사원 목록과 페이지 정보
	// 全件数と表示件数からページ範囲を計算し、現在ページに該当するデータだけを取得する。
	public static class DayWorkerEmployeePage {
		private List<DayWorkerPaymentEmployee> content;
		private int totalPages;

		// 일용직근로자지급화면 데이터 객체에 저장된 일용직근로자사원화면 데이터 값을 반환한다.
		// Controller·Service·DAO·JSP 사이에서 동일한 데이터를 일관된 구조로 전달하기 위해 사용한다.
		// 日雇い労働者支給画面データオブジェクトに保存された日雇い労働者社員画面データの値を返す。
		// Controller・Service・DAO・JSP間で同じデータを一貫した構造として受け渡すために使用する。
		public DayWorkerEmployeePage(List<DayWorkerPaymentEmployee> content, int totalPages) {
			this.content = content;
			this.totalPages = totalPages;
		}

		// 일용직근로자지급화면 데이터 객체에 저장된 내용 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 日雇い労働者支給画面データオブジェクトに保存された内容の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public List<DayWorkerPaymentEmployee> getContent() { return content; }
		// 일용직근로자지급화면 데이터 객체에 저장된 합계Pages 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 日雇い労働者支給画面データオブジェクトに保存された合計Pagesの値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public int getTotalPages() { return totalPages; }
	}

	// 일용직 사원의 근무일별 지급 내역
	// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
	public static class DayWorkerPaymentWork {
		private Date workDate;
		private int paymentRate;
		private long paymentAmount;
		private long incomeTax;
		private long localIncomeTax;

		// 일용직근로자지급화면 데이터 객체에 저장된 근무일자 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 日雇い労働者支給画面データオブジェクトに保存された勤務日付の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public Date getWorkDate() { return workDate; }
		public void setWorkDate(Date workDate) { this.workDate = workDate; }
		// 일용직근로자지급화면 데이터 객체에 저장된 지급비율 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 日雇い労働者支給画面データオブジェクトに保存された支給率の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public int getPaymentRate() { return paymentRate; }
		public void setPaymentRate(int paymentRate) { this.paymentRate = paymentRate; }
		// 일용직근로자지급화면 데이터 객체에 저장된 지급금액 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 日雇い労働者支給画面データオブジェクトに保存された支給金額の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getPaymentAmount() { return paymentAmount; }
		public void setPaymentAmount(long paymentAmount) { this.paymentAmount = paymentAmount; }
		// 일용직근로자지급화면 데이터 객체에 저장된 소득세금 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 日雇い労働者支給画面データオブジェクトに保存された所得税金の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getIncomeTax() { return incomeTax; }
		public void setIncomeTax(long incomeTax) { this.incomeTax = incomeTax; }
		// 일용직근로자지급화면 데이터 객체에 저장된 Local소득세금 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 日雇い労働者支給画面データオブジェクトに保存されたLocal所得税金の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getLocalIncomeTax() { return localIncomeTax; }
		public void setLocalIncomeTax(long localIncomeTax) { this.localIncomeTax = localIncomeTax; }
	}
}
