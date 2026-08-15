package erp.employees.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import erp.employees.dao.CertificateIssuanceDao;
import erp.employees.dao.EmployeeDao;
import erp.employees.dto.CertificateCareerItem;
import erp.employees.dto.EmployeeListItem;
import erp.employees.model.CertificateIssuance;
import erp.employees.model.Employee;
import erp.settings.dao.CompanyDao;
import erp.settings.dao.DepartmentDao;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 제증명서 화면 조회와 발급내역 저장을 처리하는 Service
public class CertificateIssueService {
	private final EmployeeDao employeeDao = EmployeeDao.getInstance();
	private final CertificateIssuanceDao certificateDao = CertificateIssuanceDao.getInstance();

	public CertificateIssueData getIssueData(Integer employeeId, String keyword) {
		EmployeeSearchCondition condition = new EmployeeSearchCondition();
		condition.setSearchTarget("ALL");
		condition.setKeyword(keyword == null ? "" : keyword.trim());
		condition.setEmploymentType("");
		condition.setStatus("");
		condition.setPage(1);
		condition.setPageSize(100);

		try (Connection conn = ConnectionProvider.getConnection()) {
			CertificateIssueData data = new CertificateIssueData();
			data.employees = employeeDao.selectListByCondition(conn, condition);
			data.departments = DepartmentDao.getInstance().selectAll(conn);
			data.company = CompanyDao.getInstance().selectById(conn, 1);
			data.issueDate = new Date();

			// 증명서는 사원을 직접 선택한 후에만 발급 대상 정보를 조회한다.
			Integer selectedId = employeeId;
			for (EmployeeListItem employee : data.employees) {
				if (selectedId != null && employee.getEmployeeId() == selectedId) {
					data.selectedEmployee = employee;
					data.careers = createCareerRows(employee);
					break;
				}
			}
			return data;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void issue(CertificateIssuance certificate) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			certificate.setCertDocNo("CERT-" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
			certificateDao.insert(conn, certificate);
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 재직상태에 맞는 증명서만 발급할 수 있는지 확인한다.
	public String validateIssue(int employeeId, String certificateType) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			Employee employee = employeeDao.selectById(conn, employeeId);
			if (employee == null) {
				return "선택한 사원정보가 없습니다.";
			}
			if ("WORKING".equals(certificateType) && "퇴직".equals(employee.getStatus())) {
				return "퇴직 사원은 재직증명서를 작성할 수 없습니다.";
			}
			if ("RETIREMENT".equals(certificateType) && !"퇴직".equals(employee.getStatus())) {
				return "재직 사원은 퇴직증명서를 작성할 수 없습니다.";
			}
			return null;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private List<CertificateCareerItem> createCareerRows(EmployeeListItem employee) {
		List<CertificateCareerItem> careers = new ArrayList<>();
		CertificateCareerItem career = new CertificateCareerItem();
		career.setJoinDate(employee.getJoinDate());
		career.setRetirementDate(employee.getRetirementDate());
		career.setDepartmentName(employee.getDepartmentName());
		career.setPositionName(employee.getPositionName());
		career.setDuty("");
		careers.add(career);
		employee.setCareerPeriod(calculateCareerPeriod(employee.getJoinDate(), employee.getRetirementDate()));
		return careers;
	}

	private String calculateCareerPeriod(String start, String end) {
		try {
			LocalDate startDate = LocalDate.parse(start);
			LocalDate endDate = end == null || end.isEmpty() ? LocalDate.now() : LocalDate.parse(end);
			Period period = Period.between(startDate, endDate);
			return period.getYears() + "년 " + period.getMonths() + "개월 " + period.getDays() + "일";
		} catch (Exception e) {
			return "";
		}
	}

	public static class CertificateIssueData {
		private List<EmployeeListItem> employees;
		private EmployeeListItem selectedEmployee;
		private List<CertificateCareerItem> careers;
		private Object departments;
		private Object company;
		private Date issueDate;
		public List<EmployeeListItem> getEmployees() { return employees; }
		public EmployeeListItem getSelectedEmployee() { return selectedEmployee; }
		public List<CertificateCareerItem> getCareers() { return careers; }
		public Object getDepartments() { return departments; }
		public Object getCompany() { return company; }
		public Date getIssueDate() { return issueDate; }
	}
}
