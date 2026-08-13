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

// 여러 사원 상세 테이블을 모아 인사기록카드 한 화면의 데이터를 만드는 Service
public class EmployeeRecordCardService {
	private final EmployeeDao employeeDao = EmployeeDao.getInstance();

	public EmployeeRecordCardData getRecordCard(Integer employeeId, EmployeeSearchCondition condition) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			// 사원 선택값이 없으면 검색 결과의 첫 번째 사원을 기본 표시한다.
			List<EmployeeListItem> employees = employeeDao.selectListByCondition(conn, condition);
			Integer selectedId = employeeId;

			// 사원 선택에 공통으로 필요한 부서, 직위, 회사 정보를 먼저 저장한다.
			EmployeeRecordCardData data = new EmployeeRecordCardData();
			data.employees = employees;
			data.departments = DepartmentDao.getInstance().selectAll(conn);
			data.positions = JobPositionDao.getInstance().selectAll(conn);
			data.company = CompanyDao.getInstance().selectById(conn, 1);
			if (selectedId == null) return data;

			// EMPLOYEE를 기준으로 가족, 보험, 학력 등 각 상세 DAO의 목록을 조회한다.
			data.employee = employeeDao.selectById(conn, selectedId);
			if (data.employee == null) return data;
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
		// Handler에 기록카드용 조회 결과들을 묶어서 반환하는 화면 데이터 객체
		private Employee employee;
		private List<EmployeeListItem> employees;
		private Object departments, positions, company, families, insurances, educations, careers, licenses, languages,
				trainings, awards, appointments, retirementCalculations;
		public Employee getEmployee() { return employee; }
		public List<EmployeeListItem> getEmployees() { return employees; }
		public Object getDepartments() { return departments; }
		public Object getPositions() { return positions; }
		public Object getCompany() { return company; }
		public Object getFamilies() { return families; }
		public Object getInsurances() { return insurances; }
		public Object getEducations() { return educations; }
		public Object getCareers() { return careers; }
		public Object getLicenses() { return licenses; }
		public Object getLanguages() { return languages; }
		public Object getTrainings() { return trainings; }
		public Object getAwards() { return awards; }
		public Object getAppointments() { return appointments; }
		public Object getRetirementCalculations() { return retirementCalculations; }
	}
}
