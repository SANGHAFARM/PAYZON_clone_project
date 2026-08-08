package erp.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.common.model.Department;
import jdbc.JdbcUtil;

public class DepartmentDAO {
	private static DepartmentDAO departmentDAO = new DepartmentDAO();
	
	public static DepartmentDAO getInstance() {
		return departmentDAO;
	}
	
	private DepartmentDAO() {
	}
	
	/*
	 * Department테이블에 부서 정보를 입력하는 메서드
	 * Departmentテーブルに部署情報を入力するメソッド
	 */
	public int insert(Connection conn, Department dept) throws SQLException{
		String sql = "INSERT INTO DEPARTMENT VALUES (SEQ_DEPT_ID.NEXTVAL, ?)";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, dept.getDeptName());
			return pstmt.executeUpdate();
		}
	}
	
	/*
	 * Department테이블의 모든 데이터를 조회하는 메서드
	 * Departmentテーブルの全てのデータを照会するメソッド
	 */	
	public List<Department> select(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("SELECT * FROM DEPARTMENT");
			rs = pstmt.executeQuery();
			List<Department> result = new ArrayList<>();
			while (rs.next()) {
				result.add(convertDepartment(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}
	
	/*
	 * Department테이블에 있는 부서 정보를 수정하는 메서드
	 * Departmentテーブルにある部署情報を修正するメソッド
	 */
	public int update(Connection conn, Department dept) throws SQLException{
		String sql = "UPDATE DEPARTMENT SET DEPT_NAME=? WHERE DEPT_ID=?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, dept.getDeptName());
			pstmt.setInt(2, dept.getDeptId());
			return pstmt.executeUpdate();
		}
		
	}
	
	/*
	 * Department테이블에 있는 부서를 삭제하는 메서드
	 * Departmentテーブルにある部署を削除するメソッド
	 */	
	public int delete(Connection conn, int no) throws SQLException {
		String sql = "DELETE FROM DEPARTMENT WHERE DEPT_ID=?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, no);
			return pstmt.executeUpdate();
		}
	}
	
	/*
	 * ResultSet으로 Department객체를 만들어 반환하는 메서드
	 * ResultSetでDepartmentオブジェクトを作って返すメソッド
	 */
	private Department convertDepartment(ResultSet rs) throws SQLException{
		Department dept = new Department();
		dept.setDeptId(rs.getInt("DEPT_ID"));
		dept.setDeptName("DEPT_NAME");
		return dept;
	}
	

}
