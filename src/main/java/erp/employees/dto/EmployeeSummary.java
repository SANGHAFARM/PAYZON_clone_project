package erp.employees.dto;

// 사원현황 화면의 상태별·고용형태별 인원수를 전달한다.
// 사원요약정보 정보를 보관하고 관련 기능에서 사용할 수 있도록 제공한다.
// 社員集計情報情報を保持し、関連機能から利用できるように提供する。
public class EmployeeSummary {
	private int workingCount;
	private int regularCount;
	private int contractCount;
	private int temporaryCount;
	private int dispatchedCount;
	private int commissionedCount;
	private int dailyCount;
	private int retiredCount;
	private int totalCount;

	// 사원요약정보 객체에 저장된 Working건수 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員集計情報オブジェクトに保存されたWorking件数の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getWorkingCount() { return workingCount; }
	public void setWorkingCount(int workingCount) { this.workingCount = workingCount; }
	// 사원요약정보 객체에 저장된 Regular건수 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員集計情報オブジェクトに保存されたRegular件数の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getRegularCount() { return regularCount; }
	public void setRegularCount(int regularCount) { this.regularCount = regularCount; }
	// 사원요약정보 객체에 저장된 Contract건수 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員集計情報オブジェクトに保存されたContract件数の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getContractCount() { return contractCount; }
	public void setContractCount(int contractCount) { this.contractCount = contractCount; }
	// 사원요약정보 객체에 저장된 Temporary건수 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員集計情報オブジェクトに保存されたTemporary件数の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getTemporaryCount() { return temporaryCount; }
	public void setTemporaryCount(int temporaryCount) { this.temporaryCount = temporaryCount; }
	// 사원요약정보 객체에 저장된 Dispatched건수 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員集計情報オブジェクトに保存されたDispatched件数の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getDispatchedCount() { return dispatchedCount; }
	public void setDispatchedCount(int dispatchedCount) { this.dispatchedCount = dispatchedCount; }
	// 사원요약정보 객체에 저장된 Commissioned건수 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員集計情報オブジェクトに保存されたCommissioned件数の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getCommissionedCount() { return commissionedCount; }
	public void setCommissionedCount(int commissionedCount) { this.commissionedCount = commissionedCount; }
	// 사원요약정보 객체에 저장된 일용직건수 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員集計情報オブジェクトに保存された日雇い件数の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getDailyCount() { return dailyCount; }
	public void setDailyCount(int dailyCount) { this.dailyCount = dailyCount; }
	// 사원요약정보 객체에 저장된 퇴직자건수 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員集計情報オブジェクトに保存された退職者件数の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getRetiredCount() { return retiredCount; }
	public void setRetiredCount(int retiredCount) { this.retiredCount = retiredCount; }
	// 사원요약정보 객체에 저장된 합계건수 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員集計情報オブジェクトに保存された合計件数の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getTotalCount() { return totalCount; }
	public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
}
