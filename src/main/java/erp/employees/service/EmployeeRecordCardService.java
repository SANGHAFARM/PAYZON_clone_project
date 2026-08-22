package erp.employees.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.employees.dao.EmployeeAppointmentDao;
import erp.employees.dao.EmployeeCareerDao;
import erp.employees.dao.EmployeeDao;
import erp.employees.dao.EmployeeDependentDao;
import erp.employees.dao.EmployeeEducationDao;
import erp.employees.dao.EmployeeInsuranceHistoryDao;
import erp.employees.dao.EmployeeLanguageDao;
import erp.employees.dao.EmployeeLicenseDao;
import erp.employees.dao.EmployeeRewardDisciplineDao;
import erp.employees.dao.EmployeeTrainingDao;
import erp.employees.dto.EmployeeListItem;
import erp.employees.model.Employee;
import erp.retirement.dao.RetirementCalculationDao;
import erp.settings.dao.CompanyDao;
import erp.settings.dao.DepartmentDao;
import erp.settings.dao.JobPositionDao;
import jdbc.connection.ConnectionProvider;

// 사원 상세 이력을 모아 인사기록카드 화면 데이터를 구성한다.
// 사원기록카드 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 社員記録カードの業務ルールとデータ変更トランザクションを処理する。
public class EmployeeRecordCardService {
	private final EmployeeDao employeeDao = EmployeeDao.getInstance();

	// 사원기록카드 처리에 필요한 기록카드를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 社員記録カード処理に必要な記録カードを照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public EmployeeRecordCardData getRecordCard(Integer employeeId, EmployeeSearchCondition condition) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			// 사원 목록은 선택창에 표시하고, 기록카드는 사원을 직접 선택한 후에만 조회한다.
			// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
			List<EmployeeListItem> employees = employeeDao.selectListByCondition(conn, condition);
			Integer selectedId = employeeId;

			// 사원 선택에 공통으로 필요한 부서, 직위, 회사 정보를 먼저 저장한다.
			// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
			EmployeeRecordCardData data = new EmployeeRecordCardData();
			data.employees = employees;
			data.departments = DepartmentDao.getInstance().selectAll(conn);
			data.positions = JobPositionDao.getInstance().selectAll(conn);
			data.company = CompanyDao.getInstance().selectById(conn, 1);
			if (selectedId == null) {
				return data;
			}

			// EMPLOYEE를 기준으로 가족, 보험, 학력 등 각 상세 DAO의 목록을 조회한다.
			// 保険・税区分と適用基準を確認し、対象金額に合う控除または非課税処理を適用する。
			data.employee = employeeDao.selectById(conn, selectedId);
			if (data.employee == null) {
				return data;
			}
			data.families = EmployeeDependentDao.getInstance().selectByEmpId(conn, selectedId);
			data.insurances = EmployeeInsuranceHistoryDao.getInstance().selectByEmpId(conn, selectedId);
			data.educations = EmployeeEducationDao.getInstance().selectByEmpId(conn, selectedId);
			data.careers = EmployeeCareerDao.getInstance().selectByEmpId(conn, selectedId);
			data.licenses = EmployeeLicenseDao.getInstance().selectByEmpId(conn, selectedId);
			data.languages = EmployeeLanguageDao.getInstance().selectByEmpId(conn, selectedId);
			data.trainings = EmployeeTrainingDao.getInstance().selectByEmpId(conn, selectedId);
			data.awards = EmployeeRewardDisciplineDao.getInstance().selectByEmpId(conn, selectedId);
			data.appointments = EmployeeAppointmentDao.getInstance().selectByEmpId(conn, selectedId);
			data.retirementCalculations = RetirementCalculationDao.getInstance().selectByEmpId(conn, selectedId);
			return data;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static class EmployeeRecordCardData {
		// 기록카드 조회 결과를 한 번에 반환한다.
		// 照会結果を列ごとに読み取り、画面またはサービスで使用するオブジェクトへ変換する。
		private Employee employee;
		private List<EmployeeListItem> employees;
		private Object departments, positions, company, families, insurances, educations, careers, licenses, languages,
				trainings, awards, appointments, retirementCalculations;
		// 사원기록카드 처리에 필요한 사원를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 社員記録カード処理に必要な社員を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public Employee getEmployee() { return employee; }
		// 사원기록카드 처리에 필요한 사원를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 社員記録カード処理に必要な社員を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public List<EmployeeListItem> getEmployees() { return employees; }
		// 사원기록카드 처리에 필요한 부서 목록를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 社員記録カード処理に必要な部署一覧を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public Object getDepartments() { return departments; }
		// 사원기록카드 처리에 필요한 직위 목록를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 社員記録カード処理に必要な役職一覧を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public Object getPositions() { return positions; }
		// 사원기록카드 처리에 필요한 사업장를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 社員記録カード処理に必要な事業所を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public Object getCompany() { return company; }
		// 사원기록카드 처리에 필요한 Families를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 社員記録カード処理に必要なFamiliesを照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public Object getFamilies() { return families; }
		// 사원기록카드 처리에 필요한 Insurances를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 社員記録カード処理に必要なInsurancesを照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public Object getInsurances() { return insurances; }
		// 사원기록카드 처리에 필요한 학력 목록를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 社員記録カード処理に必要な学歴一覧を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public Object getEducations() { return educations; }
		// 사원기록카드 처리에 필요한 Careers를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 社員記録カード処理に必要なCareersを照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public Object getCareers() { return careers; }
		// 사원기록카드 처리에 필요한 자격증 목록를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 社員記録カード処理に必要な資格一覧を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public Object getLicenses() { return licenses; }
		// 사원기록카드 처리에 필요한 어학 목록를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 社員記録カード処理に必要な語学一覧を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public Object getLanguages() { return languages; }
		// 사원기록카드 처리에 필요한 교육훈련 목록를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 社員記録カード処理に必要な教育訓練一覧を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public Object getTrainings() { return trainings; }
		// 사원기록카드 처리에 필요한 Awards를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 社員記録カード処理に必要なAwardsを照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public Object getAwards() { return awards; }
		// 사원기록카드 처리에 필요한 발령 목록를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 社員記録カード処理に必要な発令一覧を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public Object getAppointments() { return appointments; }
		// 사원기록카드 처리에 필요한 퇴직급여Calculations를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 社員記録カード処理に必要な退職給与Calculationsを照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public Object getRetirementCalculations() { return retirementCalculations; }
	}
}
