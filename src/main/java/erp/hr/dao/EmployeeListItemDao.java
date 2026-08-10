package erp.hr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.hr.model.EmployeeListItem;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 근태기록관리 등에서 사용할 사원 목록 정보 데이터베이스 접근(DAO) 클래스
public class EmployeeListItemDao {

	// 싱글톤 인스턴스 생성
	private static EmployeeListItemDao employeeListItemDao = new EmployeeListItemDao();

	// 싱글톤 접근 메서드
	public static EmployeeListItemDao getInstance() {
		return employeeListItemDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private EmployeeListItemDao() {
	}

	// 사원 목록 전체 조회
	// 사원 테이블과 부서, 직위 테이블을 외부 조인(LEFT JOIN)하여 전체 사원 목록 반환
	public List<EmployeeListItem> selectAll(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT E.EMP_ID, E.EMP_TYPE, E.EMP_NO, E.EMP_NAME_KR, D.DEPT_NAME, P.POS_NAME "
					+ "FROM EMPLOYEE E " + "LEFT JOIN DEPARTMENT D ON E.DEPT_ID = D.DEPT_ID "
					+ "LEFT JOIN POSITION P ON E.POS_ID = P.POS_ID " + "ORDER BY E.EMP_ID DESC";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			List<EmployeeListItem> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeEmployeeListItemFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 사원 목록 단건 조회
	// 기본키(EMP_ID)를 기준으로 특정 사원의 조인된 상세 정보 조회
	public EmployeeListItem selectById(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT E.EMP_ID, E.EMP_TYPE, E.EMP_NO, E.EMP_NAME_KR, D.DEPT_NAME, P.POS_NAME "
					+ "FROM EMPLOYEE E " + "LEFT JOIN DEPARTMENT D ON E.DEPT_ID = D.DEPT_ID "
					+ "LEFT JOIN POSITION P ON E.POS_ID = P.POS_ID " + "WHERE E.EMP_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeEmployeeListItemFromResultSet(rs);
			}
			return null; // 해당하는 사원 데이터가 없을 경우 null 반환
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}
	
	//재직상태, 구분, 부서, 직위, 검색어로 EmployeeListItem을 조회하는 메서드
	public List<EmployeeListItem> selectByCondition(Connection conn, String status, String empType, Integer deptId, Integer posId, String keyword) throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT E.EMP_ID, E.EMP_TYPE, E.EMP_NO, E.EMP_NAME_KR, D.DEPT_NAME, P.POS_NAME "
	                + "FROM EMPLOYEE E "
	                + "LEFT JOIN DEPARTMENT D ON E.DEPT_ID = D.DEPT_ID "
	                + "LEFT JOIN POSITION P ON E.POS_ID = P.POS_ID "
	                + "WHERE (? IS NULL OR E.STATUS = ?) "
	                + "AND (? IS NULL OR E.EMP_TYPE = ?) "
	                + "AND (? IS NULL OR E.DEPT_ID = ?) "
	                + "AND (? IS NULL OR E.POS_ID = ?) "
	                + "AND (E.EMP_NAME_KR LIKE ? OR E.EMP_NO LIKE ?) "
	                + "ORDER BY E.EMP_ID DESC";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, status);
	        pstmt.setString(2, status);
	        pstmt.setString(3, empType);
	        pstmt.setString(4, empType);
	        
	        if (deptId == null) {
	            pstmt.setNull(5, java.sql.Types.INTEGER);
	            pstmt.setNull(6, java.sql.Types.INTEGER);
	        } else {
	            pstmt.setInt(5, deptId);
	            pstmt.setInt(6, deptId);
	        }

	        if (posId == null) {
	            pstmt.setNull(7, java.sql.Types.INTEGER);
	            pstmt.setNull(8, java.sql.Types.INTEGER);
	        } else {
	            pstmt.setInt(7, posId);
	            pstmt.setInt(8, posId);
	        }
	        String kw = (keyword != null) ? keyword : "";
	        pstmt.setString(9, "%" + kw + "%");
	        pstmt.setString(10, "%" + kw + "%");
	        rs = pstmt.executeQuery();
	        List<EmployeeListItem> result = new ArrayList<>();
	        while (rs.next()) {
	            result.add(makeEmployeeListItemFromResultSet(rs));
	        }
	        return result;
		}finally {
	        JdbcUtil.close(rs);
	        JdbcUtil.close(pstmt);
	    }
	}

	// ResultSet 데이터를 EmployeeListItem 객체로 변환
	// 코드 중복 방지를 위한 공통 매핑 처리
	private EmployeeListItem makeEmployeeListItemFromResultSet(ResultSet rs) throws SQLException {
		EmployeeListItem item = new EmployeeListItem();
		item.setEmpId(rs.getInt("EMP_ID"));
		item.setEmpType(rs.getString("EMP_TYPE"));
		item.setEmpNo(rs.getString("EMP_NO"));
		item.setEmpNameKr(rs.getString("EMP_NAME_KR"));

		// 부서나 직위가 배정되지 않은 사원(null)의 경우 방어 로직을 적용하여 빈 문자열 처리
		String deptName = rs.getString("DEPT_NAME");
		item.setDeptName(deptName != null ? deptName : "");

		String posName = rs.getString("POS_NAME");
		item.setPosName(posName != null ? posName : "");

		return item;
	}
}