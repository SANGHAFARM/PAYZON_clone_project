package erp.employees.dto;

// 사원 목록의 현재 페이지와 페이지 이동 범위를 계산한다.
// 사원화면 데이터정보 정보를 보관하고 관련 기능에서 사용할 수 있도록 제공한다.
// 社員画面データ情報情報を保持し、関連機能から利用できるように提供する。
public class EmployeePageInfo {
	private final int currentPage;
	private final int totalPages;
	private final int startPage;
	private final int endPage;

	// 전달받은 값으로 사원화면 데이터정보 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で社員画面データ情報オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public EmployeePageInfo(int totalCount, int currentPage, int pageSize) {
		// 조회 결과가 없을 때도 JSP의 페이지 반복 범위가 유효하도록 1페이지를 유지한다.
		// 照会結果を列ごとに読み取り、画面またはサービスで使用するオブジェクトへ変換する。
		this.totalPages = (int) Math.ceil((double) totalCount / pageSize);
		this.currentPage = totalPages == 0 ? 1 : Math.min(currentPage, totalPages);
		this.startPage = totalPages == 0 ? 1 : ((this.currentPage - 1) / 5) * 5 + 1;
		this.endPage = totalPages == 0 ? 1 : Math.min(startPage + 4, totalPages);
	}

	// 사원화면 데이터정보 객체에 저장된 Current화면 데이터 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員画面データ情報オブジェクトに保存されたCurrent画面データの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getCurrentPage() { return currentPage; }
	// 사원화면 데이터정보 객체에 저장된 합계Pages 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員画面データ情報オブジェクトに保存された合計Pagesの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getTotalPages() { return totalPages; }
	// 사원화면 데이터정보 객체에 저장된 Start화면 데이터 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員画面データ情報オブジェクトに保存されたStart画面データの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getStartPage() { return startPage; }
	// 사원화면 데이터정보 객체에 저장된 End화면 데이터 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員画面データ情報オブジェクトに保存されたEnd画面データの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getEndPage() { return endPage; }
	// Has이전 회차 조건의 충족 여부를 확인하여 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// Has前回条件を満たしているか確認して返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public boolean isHasPrevious() { return startPage > 1; }
	// Has다음 조건의 충족 여부를 확인하여 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// Has次条件を満たしているか確認して返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public boolean isHasNext() { return endPage < totalPages; }
	// 사원화면 데이터정보 객체에 저장된 이전 회차화면 데이터 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員画面データ情報オブジェクトに保存された前回画面データの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getPreviousPage() { return Math.max(1, startPage - 1); }
	// 사원화면 데이터정보 객체에 저장된 다음화면 데이터 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員画面データ情報オブジェクトに保存された次画面データの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getNextPage() { return Math.min(totalPages, endPage + 1); }
}
