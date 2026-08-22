package erp.retirement.dto;

// 퇴직급여 입력/관리 목록의 조회 결과 한 행을 전달한다.
// 퇴직급여정산목록항목 처리에 필요한 값을 계층 간에 전달한다.
// 退職給与精算一覧項目処理に必要な値を各階層間で受け渡す。
public class RetirementBenefitListItem {
	private int calculationId, employeeId, serviceDays;
	private String paymentDate, settlementType, employeeName, positionName, departmentName,
			calculationStartDate, calculationEndDate, paymentMethod;
	private long netPayment;
	// 퇴직급여정산목록항목 객체에 저장된 계산식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算一覧項目オブジェクトに保存された計算識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getCalculationId() { return calculationId; }
	public void setCalculationId(int value) { calculationId = value; }
	// 퇴직급여정산목록항목 객체에 저장된 사원식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算一覧項目オブジェクトに保存された社員識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getEmployeeId() { return employeeId; }
	public void setEmployeeId(int value) { employeeId = value; }
	// 퇴직급여정산목록항목 객체에 저장된 근속일수 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算一覧項目オブジェクトに保存された勤続日数の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getServiceDays() { return serviceDays; }
	public void setServiceDays(int value) { serviceDays = value; }
	// 퇴직급여정산목록항목 객체에 저장된 지급일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算一覧項目オブジェクトに保存された支給日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPaymentDate() { return paymentDate; }
	public void setPaymentDate(String value) { paymentDate = value; }
	// 퇴직급여정산목록항목 객체에 저장된 정산구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算一覧項目オブジェクトに保存された精算区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getSettlementType() { return settlementType; }
	public void setSettlementType(String value) { settlementType = value; }
	// 퇴직급여정산목록항목 객체에 저장된 사원명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算一覧項目オブジェクトに保存された社員名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmployeeName() { return employeeName; }
	public void setEmployeeName(String value) { employeeName = value; }
	// 퇴직급여정산목록항목 객체에 저장된 직위명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算一覧項目オブジェクトに保存された役職名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPositionName() { return positionName; }
	public void setPositionName(String value) { positionName = value; }
	// 퇴직급여정산목록항목 객체에 저장된 부서명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算一覧項目オブジェクトに保存された部署名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getDepartmentName() { return departmentName; }
	public void setDepartmentName(String value) { departmentName = value; }
	// 퇴직급여정산목록항목 객체에 저장된 계산Start일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算一覧項目オブジェクトに保存された計算Start日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getCalculationStartDate() { return calculationStartDate; }
	public void setCalculationStartDate(String value) { calculationStartDate = value; }
	// 퇴직급여정산목록항목 객체에 저장된 계산End일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算一覧項目オブジェクトに保存された計算End日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getCalculationEndDate() { return calculationEndDate; }
	public void setCalculationEndDate(String value) { calculationEndDate = value; }
	// 퇴직급여정산목록항목 객체에 저장된 지급방법 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算一覧項目オブジェクトに保存された支給方法の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPaymentMethod() { return paymentMethod; }
	public void setPaymentMethod(String value) { paymentMethod = value; }
	// 퇴직급여정산목록항목 객체에 저장된 Net지급 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算一覧項目オブジェクトに保存されたNet支給の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getNetPayment() { return netPayment; }
	public void setNetPayment(long value) { netPayment = value; }
}
