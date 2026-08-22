package erp.home.dto;

import java.util.ArrayList;
import java.util.List;

import erp.employees.dto.EmployeeSummary;
import erp.payroll.dto.PayrollRegisterPage.PayrollRegisterItem;
import erp.settings.model.Company;

// 홈 화면에 필요한 회사, 사원 및 최근 급여 현황을 전달한다.
// 홈대시보드 정보를 보관하고 관련 기능에서 사용할 수 있도록 제공한다.
// ホームダッシュボード情報を保持し、関連機能から利用できるように提供する。
public class HomeDashboard {

	private Company company;
	private EmployeeSummary employeeSummary;
	private List<PayrollRegisterItem> recentPayrolls = new ArrayList<>();

	// 홈대시보드 객체에 저장된 사업장 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// ホームダッシュボードオブジェクトに保存された事業所の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Company getCompany() {
		return company;
	}

	// 전달받은 사업장 값을 홈대시보드 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った事業所の値をホームダッシュボードオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCompany(Company company) {
		this.company = company;
	}

	// 홈대시보드 객체에 저장된 사원요약정보 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// ホームダッシュボードオブジェクトに保存された社員集計情報の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public EmployeeSummary getEmployeeSummary() {
		return employeeSummary;
	}

	// 전달받은 사원요약정보 값을 홈대시보드 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員集計情報の値をホームダッシュボードオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeSummary(EmployeeSummary employeeSummary) {
		this.employeeSummary = employeeSummary;
	}

	// 홈대시보드 객체에 저장된 최근Payrolls 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// ホームダッシュボードオブジェクトに保存された直近Payrollsの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<PayrollRegisterItem> getRecentPayrolls() {
		return recentPayrolls;
	}

	// 전달받은 최근Payrolls 값을 홈대시보드 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った直近Payrollsの値をホームダッシュボードオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setRecentPayrolls(List<PayrollRegisterItem> recentPayrolls) {
		this.recentPayrolls = recentPayrolls;
	}

	// 홈대시보드 객체에 저장된 최근급여 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// ホームダッシュボードオブジェクトに保存された最新給与の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public PayrollRegisterItem getLatestPayroll() {
		return recentPayrolls.isEmpty() ? null : recentPayrolls.get(0);
	}
}
