package erp.payroll.dto;

import java.sql.Date;
import java.util.List;

// 4대보험 공제내역 화면의 조회 결과를 전달한다.
// 4대보험보험화면 데이터 처리에 필요한 값을 계층 간에 전달한다.
// 四大保険保険画面データ処理に必要な値を各階層間で受け渡す。
public class FourInsurancePage {

	private Date calculationStart;
	private Date calculationEnd;
	private Date paymentDate;
	private List<FourInsuranceDeduction> deductions;
	private FourInsuranceTotals totals;

	// 4대보험보험화면 데이터 객체에 저장된 계산Start 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 四大保険保険画面データオブジェクトに保存された計算Startの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getCalculationStart() { return calculationStart; }
	public void setCalculationStart(Date value) { calculationStart = value; }
	// 4대보험보험화면 데이터 객체에 저장된 계산End 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 四大保険保険画面データオブジェクトに保存された計算Endの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getCalculationEnd() { return calculationEnd; }
	public void setCalculationEnd(Date value) { calculationEnd = value; }
	// 4대보험보험화면 데이터 객체에 저장된 지급일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 四大保険保険画面データオブジェクトに保存された支給日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getPaymentDate() { return paymentDate; }
	public void setPaymentDate(Date value) { paymentDate = value; }
	// 4대보험보험화면 데이터 객체에 저장된 공제 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 四大保険保険画面データオブジェクトに保存された控除一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<FourInsuranceDeduction> getDeductions() { return deductions; }
	public void setDeductions(List<FourInsuranceDeduction> value) { deductions = value; }
	// 4대보험보험화면 데이터 객체에 저장된 합계정보 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 四大保険保険画面データオブジェクトに保存された合計情報の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public FourInsuranceTotals getTotals() { return totals; }
	public void setTotals(FourInsuranceTotals value) { totals = value; }

	// 사원 한 명의 4대보험 근로자 및 사업주 부담액
	// 保険・税区分と適用基準を確認し、対象金額に合う控除または非課税処理を適用する。
	public static class FourInsuranceDeduction {
		private String employmentTypeName;
		private String employeeName;
		private String departmentName;
		private String positionName;
		private long pensionEmployer;
		private long pensionEmployee;
		private long healthEmployer;
		private long healthEmployee;
		private long careEmployer;
		private long careEmployee;
		private long employmentEmployer;
		private long employmentEmployee;

		// 4대보험보험화면 데이터 객체에 저장된 고용구분명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 四大保険保険画面データオブジェクトに保存された雇用区分名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getEmploymentTypeName() { return employmentTypeName; }
		public void setEmploymentTypeName(String value) { employmentTypeName = value; }
		// 4대보험보험화면 데이터 객체에 저장된 사원명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 四大保険保険画面データオブジェクトに保存された社員名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getEmployeeName() { return employeeName; }
		public void setEmployeeName(String value) { employeeName = value; }
		// 4대보험보험화면 데이터 객체에 저장된 부서명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 四大保険保険画面データオブジェクトに保存された部署名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getDepartmentName() { return departmentName; }
		public void setDepartmentName(String value) { departmentName = value; }
		// 4대보험보험화면 데이터 객체에 저장된 직위명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 四大保険保険画面データオブジェクトに保存された役職名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getPositionName() { return positionName; }
		public void setPositionName(String value) { positionName = value; }
		// 4대보험보험화면 데이터 객체에 저장된 연금사업주 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 四大保険保険画面データオブジェクトに保存された年金事業主の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getPensionEmployer() { return pensionEmployer; }
		public void setPensionEmployer(long value) { pensionEmployer = value; }
		// 4대보험보험화면 데이터 객체에 저장된 연금사원 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 四大保険保険画面データオブジェクトに保存された年金社員の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getPensionEmployee() { return pensionEmployee; }
		public void setPensionEmployee(long value) { pensionEmployee = value; }
		// 4대보험보험화면 데이터 객체에 저장된 연금합계 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 四大保険保険画面データオブジェクトに保存された年金合計の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getPensionTotal() { return pensionEmployer + pensionEmployee; }
		// 4대보험보험화면 데이터 객체에 저장된 건강사업주 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 四大保険保険画面データオブジェクトに保存された健康事業主の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getHealthEmployer() { return healthEmployer; }
		public void setHealthEmployer(long value) { healthEmployer = value; }
		// 4대보험보험화면 데이터 객체에 저장된 건강사원 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 四大保険保険画面データオブジェクトに保存された健康社員の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getHealthEmployee() { return healthEmployee; }
		public void setHealthEmployee(long value) { healthEmployee = value; }
		// 4대보험보험화면 데이터 객체에 저장된 건강합계 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 四大保険保険画面データオブジェクトに保存された健康合計の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getHealthTotal() { return healthEmployer + healthEmployee; }
		// 4대보험보험화면 데이터 객체에 저장된 장기요양사업주 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 四大保険保険画面データオブジェクトに保存された介護事業主の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getCareEmployer() { return careEmployer; }
		public void setCareEmployer(long value) { careEmployer = value; }
		// 4대보험보험화면 데이터 객체에 저장된 장기요양사원 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 四大保険保険画面データオブジェクトに保存された介護社員の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getCareEmployee() { return careEmployee; }
		public void setCareEmployee(long value) { careEmployee = value; }
		// 4대보험보험화면 데이터 객체에 저장된 장기요양합계 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 四大保険保険画面データオブジェクトに保存された介護合計の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getCareTotal() { return careEmployer + careEmployee; }
		// 4대보험보험화면 데이터 객체에 저장된 고용사업주 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 四大保険保険画面データオブジェクトに保存された雇用事業主の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getEmploymentEmployer() { return employmentEmployer; }
		public void setEmploymentEmployer(long value) { employmentEmployer = value; }
		// 4대보험보험화면 데이터 객체에 저장된 고용사원 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 四大保険保険画面データオブジェクトに保存された雇用社員の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getEmploymentEmployee() { return employmentEmployee; }
		public void setEmploymentEmployee(long value) { employmentEmployee = value; }
		// 4대보험보험화면 데이터 객체에 저장된 고용합계 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 四大保険保険画面データオブジェクトに保存された雇用合計の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getEmploymentTotal() { return employmentEmployer + employmentEmployee; }
		// 4대보험보험화면 데이터 객체에 저장된 합계사업주 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 四大保険保険画面データオブジェクトに保存された合計事業主の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getTotalEmployer() { return pensionEmployer + healthEmployer + careEmployer + employmentEmployer; }
		// 4대보험보험화면 데이터 객체에 저장된 합계사원 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 四大保険保険画面データオブジェクトに保存された合計社員の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getTotalEmployee() { return pensionEmployee + healthEmployee + careEmployee + employmentEmployee; }
		// 4대보험보험화면 데이터 객체에 저장된 Grand합계 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 四大保険保険画面データオブジェクトに保存されたGrand合計の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getGrandTotal() { return getTotalEmployer() + getTotalEmployee(); }
	}

	public static class FourInsuranceTotals extends FourInsuranceDeduction { }
}
