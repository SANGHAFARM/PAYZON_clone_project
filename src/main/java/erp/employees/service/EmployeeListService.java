package erp.employees.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import erp.employees.dao.EmployeeDao;
import erp.employees.dto.EmployeeListItem;
import erp.employees.dto.EmployeePageInfo;
import erp.employees.dto.EmployeeSummary;
import jdbc.connection.ConnectionProvider;

// 사원 목록 조회와 상단 현황 집계를 함께 처리하는 Service
public class EmployeeListService {
	private final EmployeeDao employeeDao = EmployeeDao.getInstance();

	public EmployeeListResult getEmployeeList(EmployeeSearchCondition condition) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			// 조건에 맞는 전체 행 수를 먼저 구해 현재 페이지 범위를 계산한다.
			int totalCount = employeeDao.countByCondition(conn, condition);
			EmployeePageInfo pageInfo = new EmployeePageInfo(totalCount, condition.getPage(), condition.getPageSize());
			condition.setPage(pageInfo.getCurrentPage());
			// 계산된 범위의 목록과 화면 상단에 표시할 전체 사원 현황을 조회한다.
			List<EmployeeListItem> employees = employeeDao.selectListByCondition(conn, condition);
			EmployeeSummary summary = employeeDao.selectSummary(conn);
			return new EmployeeListResult(employees, summary, pageInfo);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> getEmploymentTypes() {
		return Arrays.asList("정규직", "계약직", "임시직", "파견직", "위촉직", "일용직");
	}

	public static class EmployeeListResult {
		// Handler에 여러 조회 결과를 한 번에 반환하기 위한 결과 객체
		private final List<EmployeeListItem> employees;
		private final EmployeeSummary summary;
		private final EmployeePageInfo pageInfo;

		public EmployeeListResult(List<EmployeeListItem> employees, EmployeeSummary summary, EmployeePageInfo pageInfo) {
			this.employees = employees;
			this.summary = summary;
			this.pageInfo = pageInfo;
		}
		public List<EmployeeListItem> getEmployees() { return employees; }
		public EmployeeSummary getSummary() { return summary; }
		public EmployeePageInfo getPageInfo() { return pageInfo; }
	}
}
