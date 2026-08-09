package erp.hr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.hr.model.Employee;
import erp.hr.model.EmployeeListItem;

public class EmployeeDao {
	private static EmployeeDao employeeDao = new EmployeeDao();

	public static EmployeeDao getInstance() {
		return employeeDao;
	}

	private EmployeeDao() {
	}

	/*
	 * 사원 등록하는 메서드
	 */
	public int insert(Connection conn, Employee emp) throws SQLException {
		String sql = "INSERT INTO EMPLOYEE VALUES (SEQ_EMP_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
	 * 근태관리에서 조회로 사용할 사원 조회 메서드
	 * 재직상태, 고용형태, 부서, 직위, 키워드에 따라 조회내용이 달라짐
	 */
	public List<EmployeeListItem> selectList(Connection conn, String status, String empType, Integer deptId,
			Integer posId, String keyword) throws SQLException {
		String sql = "SELECT e.EMP_ID, e.EMP_TYPE, e.EMP_NO, e.EMP_NAME_KR, d.DEPT_NAME, p.POS_NAME "
				+ "FROM EMPLOYEE e " + "LEFT JOIN DEPARTMENT d ON e.DEPT_ID = d.DEPT_ID "
				+ "LEFT JOIN POSITION p ON e.POS_ID = p.POS_ID " + "WHERE (? IS NULL OR e.STATUS = ?) "
				+ "AND (? IS NULL OR e.EMP_TYPE = ?) " + "AND (? IS NULL OR e.DEPT_ID = ?) "
				+ "AND (? IS NULL OR e.POS_ID = ?) " + "AND (e.EMP_NAME_KR LIKE ? OR e.EMP_NO LIKE ?) "
				+ "ORDER BY e.EMP_ID";
		List<EmployeeListItem> list = new ArrayList<>();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, status);
			pstmt.setString(2, status);
			pstmt.setString(3, empType);
			pstmt.setString(4, empType);
			if (deptId != null) {
				pstmt.setInt(5, deptId);
				pstmt.setInt(6, deptId);
			} else {
				pstmt.setNull(5, java.sql.Types.NUMERIC);
				pstmt.setNull(6, java.sql.Types.NUMERIC);
			}
			if (posId != null) {
				pstmt.setInt(7, posId);
				pstmt.setInt(8, posId);
			} else {
				pstmt.setNull(7, java.sql.Types.NUMERIC);
				pstmt.setNull(8, java.sql.Types.NUMERIC);
			}
			pstmt.setString(9, "%" + keyword + "%");
			pstmt.setString(10, "%" + keyword + "%");
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					EmployeeListItem item = new EmployeeListItem();
					item.setEmpId(rs.getInt("EMP_ID"));
					item.setEmpType(rs.getString("EMP_TYPE"));
					item.setEmpNo(rs.getString("EMP_NO"));
					item.setEmpNameKr(rs.getString("EMP_NAME_KR"));
					item.setDeptName(rs.getString("DEPT_NAME"));
					item.setPosName(rs.getString("POS_NAME"));
					list.add(item);
				}
			}
		}
		return list;
	}

	/*
	 * 사원 삭제하는 메서드
	 */
	public int delete(Connection conn, int empId) throws SQLException {
		String sql = "DELETE FROM EMPLOYEE WHERE EMP_ID = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, empId);
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
