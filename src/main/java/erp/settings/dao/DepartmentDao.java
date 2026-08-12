/*
 * package erp.settings.dao;
 * 
 * import java.sql.Connection; import java.sql.PreparedStatement; import
 * java.sql.ResultSet; import java.sql.SQLException; import java.util.ArrayList;
 * import java.util.List;
 * 
 * import erp.settings.model.Department; import jdbc.JdbcUtil;
 * 
 * public class DepartmentDao { private static DepartmentDao departmentDao = new
 * DepartmentDao();
 * 
 * public static DepartmentDao getInstance() { return departmentDao; }
 * 
 * private DepartmentDao() { }
 * 
 * 
 * Department테이블에 부서 정보를 입력하는 메서드 Departmentテーブルに部署情報を入力するメソッド
 * 
 * public int insert(Connection conn, Department dept) throws SQLException{
 * String sql = "INSERT INTO DEPARTMENT VALUES (SEQ_DEPT_ID.NEXTVAL, ?)";
 * try(PreparedStatement pstmt = conn.prepareStatement(sql)){ pstmt.setString(1,
 * dept.getDeptName()); return pstmt.executeUpdate(); } }
 * 
 * 
 * Department테이블의 모든 데이터를 조회하는 메서드 Departmentテーブルの全てのデータを照会するメソッド
 * 
 * public List<Department> select(Connection conn) throws SQLException {
 * PreparedStatement pstmt = null; ResultSet rs = null; try { pstmt =
 * conn.prepareStatement("SELECT * FROM DEPARTMENT"); rs = pstmt.executeQuery();
 * List<Department> result = new ArrayList<>(); while (rs.next()) {
 * result.add(convertDepartment(rs)); } return result; } finally {
 * JdbcUtil.close(rs); JdbcUtil.close(pstmt); } }
 * 
 * 
 * Department테이블에 있는 부서 정보를 수정하는 메서드 Departmentテーブルにある部署情報を修正するメソッド
 * 
 * public int update(Connection conn, Department dept) throws SQLException{
 * String sql = "UPDATE DEPARTMENT SET DEPT_NAME=? WHERE DEPT_ID=?";
 * try(PreparedStatement pstmt = conn.prepareStatement(sql)){ pstmt.setString(1,
 * dept.getDeptName()); pstmt.setInt(2, dept.getDeptId()); return
 * pstmt.executeUpdate(); }
 * 
 * }
 * 
 * 
 * Department테이블에 있는 부서를 삭제하는 메서드 Departmentテーブルにある部署を削除するメソッド
 * 
 * public int delete(Connection conn, int no) throws SQLException { String sql =
 * "DELETE FROM DEPARTMENT WHERE DEPT_ID=?"; try(PreparedStatement pstmt =
 * conn.prepareStatement(sql)){ pstmt.setInt(1, no); return
 * pstmt.executeUpdate(); } }
 * 
 * 
 * ResultSet으로 Department객체를 만들어 반환하는 메서드 ResultSetでDepartmentオブジェクトを作って返すメソッド
 * 
 * private Department convertDepartment(ResultSet rs) throws SQLException{
 * Department dept = new Department(); dept.setDeptId(rs.getInt("DEPT_ID"));
 * dept.setDeptName("DEPT_NAME"); return dept; } }
 */

package erp.settings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.settings.model.Department;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 부서 설정 데이터베이스 접근(DAO) 클래스
public class DepartmentDao {

	// 싱글톤 인스턴스 생성
	private static DepartmentDao departmentDao = new DepartmentDao();

	// 싱글톤 접근 메서드
	public static DepartmentDao getInstance() {
		return departmentDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private DepartmentDao() {
	}

	// 부서 정보 등록
	public void insert(Connection conn, Department dept) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO DEPARTMENT (DEPARTMENT_ID, DEPARTMENT_NAME) VALUES (DEPARTMENT_SEQ.NEXTVAL, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dept.getDepartmentName());
			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 부서 정보 단건 조회
	public Department selectById(Connection conn, int deptId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM DEPARTMENT WHERE DEPARTMENT_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, deptId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				Department dept = new Department();
				dept.setDepartmentId(rs.getInt("DEPARTMENT_ID"));
				dept.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
				return dept;
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 부서 목록 전체 조회
	public List<Department> selectAll(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM DEPARTMENT ORDER BY DEPARTMENT_ID ASC";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			List<Department> result = new ArrayList<>();
			while (rs.next()) {
				Department dept = new Department();
				dept.setDepartmentId(rs.getInt("DEPARTMENT_ID"));
				dept.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
				result.add(dept);
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 부서 정보 수정
	public int update(Connection conn, Department dept) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE DEPARTMENT SET DEPARTMENT_NAME = ? WHERE DEPARTMENT_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dept.getDepartmentName());
			pstmt.setInt(2, dept.getDepartmentId());
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 부서 정보 삭제
	public int delete(Connection conn, int deptId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM DEPARTMENT WHERE DEPARTMENT_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, deptId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}
}