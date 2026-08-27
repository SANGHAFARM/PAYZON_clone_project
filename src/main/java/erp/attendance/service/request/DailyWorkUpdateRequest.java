package erp.attendance.service.request;

import java.util.Date;
import java.util.Map;

//일용직 근무기록 수정 리퀘스트
//日雇い勤務記録修正に使うクラス
// 일용직근무Update요청정보 처리에 필요한 값을 계층 간에 전달한다.
// 日雇い勤務Updateリクエスト情報処理に必要な値を各階層間で受け渡す。
public class DailyWorkUpdateRequest {
	private int dailyWorkRecordId;    // 일용직 근무기록ID(日雇い勤務記録ID)
	private Date workDate;            // 근무일자(勤務日)
	private Integer projectId;        // 프로젝트ID(プロジェクトID)
	private long dailyPay;            // 일당(日当)
	private double payRate;           // 지급율(支給率)
	private long incomeTax;           // 소득세(所得税)
	private long localIncomeTax;      // 지방소득세(地方所得税)
	private long actualPay;           // 실지급액(実支給額)
	
	

	// 전달받은 값으로 일용직근무Update요청정보 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で日雇い勤務Updateリクエスト情報オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public DailyWorkUpdateRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	// 전달받은 값으로 일용직근무Update요청정보 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で日雇い勤務Updateリクエスト情報オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public DailyWorkUpdateRequest(int dailyWorkRecordId, Date workDate, Integer projectId, long dailyPay,
			double payRate, long incomeTax, long localIncomeTax, long actualPay) {
		super();
		this.dailyWorkRecordId = dailyWorkRecordId;
		this.workDate = workDate;
		this.projectId = projectId;
		this.dailyPay = dailyPay;
		this.payRate = payRate;
		this.incomeTax = incomeTax;
		this.localIncomeTax = localIncomeTax;
		this.actualPay = actualPay;
	}

	// 일용직근무Update요청정보 객체에 저장된 근무일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務Updateリクエスト情報オブジェクトに保存された勤務日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getWorkDate() {
		return workDate;
	}

	// 전달받은 근무일자 값을 일용직근무Update요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った勤務日付の値を日雇い勤務Updateリクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	// 일용직근무Update요청정보 객체에 저장된 현장·프로젝트식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務Updateリクエスト情報オブジェクトに保存された現場・プロジェクト識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getProjectId() {
		return projectId;
	}

	// 전달받은 현장·프로젝트식별번호 값을 일용직근무Update요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った現場・プロジェクト識別番号の値を日雇い勤務Updateリクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	// 일용직근무Update요청정보 객체에 저장된 일용직지급 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務Updateリクエスト情報オブジェクトに保存された日雇い支給の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getDailyPay() {
		return dailyPay;
	}

	// 전달받은 일용직지급 값을 일용직근무Update요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った日雇い支給の値を日雇い勤務Updateリクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDailyPay(long dailyPay) {
		this.dailyPay = dailyPay;
	}

	// 일용직근무Update요청정보 객체에 저장된 지급비율 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務Updateリクエスト情報オブジェクトに保存された支給率の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public double getPayRate() {
		return payRate;
	}

	// 전달받은 지급비율 값을 일용직근무Update요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給率の値を日雇い勤務Updateリクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPayRate(double payRate) {
		this.payRate = payRate;
	}

	// 일용직근무Update요청정보 객체에 저장된 소득세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務Updateリクエスト情報オブジェクトに保存された所得税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getIncomeTax() {
		return incomeTax;
	}

	// 전달받은 소득세금 값을 일용직근무Update요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った所得税金の値を日雇い勤務Updateリクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setIncomeTax(long incomeTax) {
		this.incomeTax = incomeTax;
	}

	// 일용직근무Update요청정보 객체에 저장된 Local소득세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務Updateリクエスト情報オブジェクトに保存されたLocal所得税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getLocalIncomeTax() {
		return localIncomeTax;
	}

	// 전달받은 Local소득세금 값을 일용직근무Update요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったLocal所得税金の値を日雇い勤務Updateリクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setLocalIncomeTax(long localIncomeTax) {
		this.localIncomeTax = localIncomeTax;
	}

	// 일용직근무Update요청정보 객체에 저장된 Actual지급 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務Updateリクエスト情報オブジェクトに保存されたActual支給の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getActualPay() {
		return actualPay;
	}

	// 전달받은 Actual지급 값을 일용직근무Update요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったActual支給の値を日雇い勤務Updateリクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setActualPay(long actualPay) {
		this.actualPay = actualPay;
	}

	// 일용직근무Update요청정보 입력값과 업무 처리 가능 여부를 검증한다.
	// Controller·Service·DAO·JSP 사이에서 동일한 데이터를 일관된 구조로 전달하기 위해 사용한다.
	// 日雇い勤務Updateリクエスト情報の入力値と業務処理の可否を検証する。
	// Controller・Service・DAO・JSP間で同じデータを一貫した構造として受け渡すために使用する。
	public void validate(Map<String, Boolean> errors) {
		if (dailyWorkRecordId==0) {
			errors.put("dailyWorkRecordId", Boolean.TRUE);
		}
		if (projectId == null || projectId == 0) {
			errors.put("projectId", Boolean.TRUE);
		}
		if (workDate == null) {
			errors.put("workDate", Boolean.TRUE);
		}

	}

	// 일용직근무Update요청정보 객체에 저장된 일용직근무기록식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務Updateリクエスト情報オブジェクトに保存された日雇い勤務記録識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getDailyWorkRecordId() {
		return dailyWorkRecordId;
	}

	// 전달받은 일용직근무기록식별번호 값을 일용직근무Update요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った日雇い勤務記録識別番号の値を日雇い勤務Updateリクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDailyWorkRecordId(int dailyWorkRecordId) {
		this.dailyWorkRecordId = dailyWorkRecordId;
	}
	

}
