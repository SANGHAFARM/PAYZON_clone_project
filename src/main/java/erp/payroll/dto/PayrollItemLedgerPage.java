package erp.payroll.dto;

import java.util.ArrayList;
import java.util.List;

// 항목별 대장 화면의 조회 결과를 전달한다.
// 급여항목Ledger화면 데이터 처리에 필요한 값을 계층 간에 전달한다.
// 給与項目Ledger画面データ処理に必要な値を各階層間で受け渡す。
public class PayrollItemLedgerPage {

	private List<PayrollItemLedgerOption> items;
	private List<String> months;
	private List<PayrollItemLedgerRow> rows;
	private PayrollItemLedgerTotals totals;

	// 급여항목Ledger화면 데이터 객체에 저장된 항목 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与項目Ledger画面データオブジェクトに保存された項目一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<PayrollItemLedgerOption> getItems() { return items; }
	public void setItems(List<PayrollItemLedgerOption> value) { items = value; }
	// 급여항목Ledger화면 데이터 객체에 저장된 월 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与項目Ledger画面データオブジェクトに保存された月一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<String> getMonths() { return months; }
	public void setMonths(List<String> value) { months = value; }
	// 급여항목Ledger화면 데이터 객체에 저장된 행 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与項目Ledger画面データオブジェクトに保存された行一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<PayrollItemLedgerRow> getRows() { return rows; }
	public void setRows(List<PayrollItemLedgerRow> value) { rows = value; }
	// 급여항목Ledger화면 데이터 객체에 저장된 합계정보 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与項目Ledger画面データオブジェクトに保存された合計情報の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public PayrollItemLedgerTotals getTotals() { return totals; }
	public void setTotals(PayrollItemLedgerTotals value) { totals = value; }

	public static class PayrollItemLedgerOption {
		private String itemCode;
		private String itemName;
		public PayrollItemLedgerOption(String itemCode, String itemName) { this.itemCode = itemCode; this.itemName = itemName; }
		// 급여항목Ledger화면 데이터 객체에 저장된 항목코드 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与項目Ledger画面データオブジェクトに保存された項目コードの値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getItemCode() { return itemCode; }
		// 급여항목Ledger화면 데이터 객체에 저장된 항목명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与項目Ledger画面データオブジェクトに保存された項目名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getItemName() { return itemName; }
	}

	// 사원 한 명의 월별 항목 금액
	// 基準日と期間の境界を確認し、照会・計算に使用できる日付形式へ変換する。
	public static class PayrollItemLedgerRow {
		private int employeeId;
		private String employmentTypeName;
		private String employeeName;
		private String departmentName;
		private String positionName;
		private List<Long> monthlyAmounts = new ArrayList<>();
		// 급여항목Ledger화면 데이터 객체에 저장된 사원식별번호 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与項目Ledger画面データオブジェクトに保存された社員識別番号の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public int getEmployeeId() { return employeeId; }
		public void setEmployeeId(int value) { employeeId = value; }
		// 급여항목Ledger화면 데이터 객체에 저장된 고용구분명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与項目Ledger画面データオブジェクトに保存された雇用区分名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getEmploymentTypeName() { return employmentTypeName; }
		public void setEmploymentTypeName(String value) { employmentTypeName = value; }
		// 급여항목Ledger화면 데이터 객체에 저장된 사원명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与項目Ledger画面データオブジェクトに保存された社員名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getEmployeeName() { return employeeName; }
		public void setEmployeeName(String value) { employeeName = value; }
		// 급여항목Ledger화면 데이터 객체에 저장된 부서명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与項目Ledger画面データオブジェクトに保存された部署名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getDepartmentName() { return departmentName; }
		public void setDepartmentName(String value) { departmentName = value; }
		// 급여항목Ledger화면 데이터 객체에 저장된 직위명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与項目Ledger画面データオブジェクトに保存された役職名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getPositionName() { return positionName; }
		public void setPositionName(String value) { positionName = value; }
		// 급여항목Ledger화면 데이터 객체에 저장된 월간금액 목록 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与項目Ledger画面データオブジェクトに保存された月間金額一覧の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public List<Long> getMonthlyAmounts() { return monthlyAmounts; }
		public void setMonthlyAmounts(List<Long> value) { monthlyAmounts = value; }
		public long getTotalAmount() { long total = 0; for (Long amount : monthlyAmounts) total += amount == null ? 0 : amount; return total; }
	}

	public static class PayrollItemLedgerTotals {
		private List<Long> monthlyAmounts = new ArrayList<>();
		// 급여항목Ledger화면 데이터 객체에 저장된 월간금액 목록 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与項目Ledger画面データオブジェクトに保存された月間金額一覧の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public List<Long> getMonthlyAmounts() { return monthlyAmounts; }
		public void setMonthlyAmounts(List<Long> value) { monthlyAmounts = value; }
		public long getTotalAmount() { long total = 0; for (Long amount : monthlyAmounts) total += amount == null ? 0 : amount; return total; }
	}
}
