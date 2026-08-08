package erp.hr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import erp.hr.model.Employee;

public class EmployeeDAO {
	private static EmployeeDAO employeeDAO = new EmployeeDAO();

	public static EmployeeDAO getInstance() {
		return employeeDAO;
	}

	private EmployeeDAO() {
	}
	
	public int insert(Connection conn, Employee emp) throws SQLException{
		String sql = "INSERT INTO EMPLOYEE VALUES (SEQ_EMP_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?)";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, emp.getEmpNo());
			pstmt.setString(2, emp.getEmpType());
			pstmt.setString(3, emp.getEmpNameKr());
			pstmt.setString(4, emp.getEmpNameEn());
			pstmt.setString(5, emp.getForeignYn());
			pstmt.setDate(6, dateToSQLDate(emp.getJoinDate()));
			pstmt.setInt(7, emp.getDeptId());
			pstmt.setInt(8, emp.getPosId());
			pstmt.setString(9, emp.getJuminNo());
			
			pstmt.setString(10, emp.getZipCode());
			pstmt.setString(11, emp.getAddress());
			pstmt.setString(12, emp.getTelNo());
			pstmt.setString(13, emp.getMobileNo());
			pstmt.setString(14, emp.getEmail());
			pstmt.setString(15, emp.getSnsAddress());
			pstmt.setString(16, emp.getMemo());
			pstmt.setString(17, emp.getPhotoPath());
			
			pstmt.setLong(18, emp.getBasicPay());
			pstmt.setString(19, emp.getIncomeType());
			pstmt.setInt(20, emp.getIncomeTaxRate());
			pstmt.setString(21, emp.getYouthTaxReduceYn());
			pstmt.setInt(22, emp.getYouthTaxRate());
			
			pstmt.setString(23, emp.getNpYn());
			pstmt.setString(24, emp.getHiYn());
			pstmt.setString(25, emp.getLtciYn());
			pstmt.setString(26, emp.getEiYn());
			pstmt.setInt(27, emp.getHiReduceRate());
			pstmt.setInt(28, emp.getLtciReduceRate());
			
			pstmt.setString(29, emp.getDurunuriSeparateYn());
			pstmt.setInt(30, emp.getDurunuriNpRate());
			pstmt.setInt(31, emp.getDurunuriEiRate());
			
			pstmt.setLong(32, emp.getNpMonthlyBase());
			pstmt.setLong(33, emp.getHiMonthlyBase());
			pstmt.setLong(34, emp.getEiMonthlyBase());
			
			pstmt.setString(35, emp.getBankName());
			pstmt.setString(36, emp.getAccountNo());
			
			pstmt.setString(37, emp.getDischargeType());
			pstmt.setString(38, emp.getMilBranch());
			pstmt.setDate(39, dateToSQLDate(emp.getMilServiceStart()));
			pstmt.setDate(40, dateToSQLDate(emp.getMilServiceEnd()));
			pstmt.setString(41, emp.getMilRank());
			pstmt.setString(42, emp.getMilSpecialty());
			pstmt.setString(43, emp.getMilUnfinishedReason());
			
			pstmt.setString(44, emp.getStatus());
			pstmt.setString(45, emp.getRetireType());
			pstmt.setDate(46, dateToSQLDate(emp.getRetireDate()));
			pstmt.setString(47, emp.getRetireReason());
			pstmt.setString(48, emp.getAfterRetireContact());
			return pstmt.executeUpdate();
		}
		
		
	}
	
	/*
	 * java.util.Date 타입을 java.sql.Date타입으로 변환하는 메서드
	 * java.util.Dateタイプをjava.sql.Dateタイプに変換するメソッド
	 */
	private java.sql.Date dateToSQLDate(java.util.Date date) {
		return (date != null) ? new java.sql.Date(date.getTime()) : null;
	}

	
	
}
