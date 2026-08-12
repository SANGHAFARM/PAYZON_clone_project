package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.employees.model.EmployeeDependent;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 부양가족 정보 데이터베이스 접근(DAO) 클래스
public class EmployeeDependentDao {

	// 싱글톤 인스턴스 생성
	private static EmployeeDependentDao employeeDependentDao = new EmployeeDependentDao();

	// 싱글톤 접근 메서드
	public static EmployeeDependentDao getInstance() {
		return employeeDependentDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private EmployeeDependentDao() {
	}

	// 부양가족 정보 등록
	// 시퀀스를 사용하여 기본키 발급 및 부양가족 데이터 저장
	public void insert(Connection conn, EmployeeDependent dependent) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO EMPLOYEE_DEPENDENT "
					+ "(EMPLOYEE_DEPENDENT_ID, EMPLOYEE_ID, RELATION, DEP_NAME, NATIONAL_TYPE, JUMIN_NO, "
					+ "DISABLED_YN, BASIC_DEDUCT_YN, HEALTH_INS_YN, COHABIT_YN, INCOME_TAX_YN, CHILD_UNDER20_YN) "
					+ "VALUES (EMPLOYEE_DEPENDENT_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dependent.getEmployeeId());
			pstmt.setString(2, dependent.getRelation());
			pstmt.setString(3, dependent.getDepName());
			pstmt.setString(4, dependent.getNationalType());
			pstmt.setString(5, dependent.getJuminNo());
			pstmt.setString(6, dependent.getDisabledYn());
			pstmt.setString(7, dependent.getBasicDeductYn());
			pstmt.setString(8, dependent.getHealthInsYn());
			pstmt.setString(9, dependent.getCohabitYn());
			pstmt.setString(10, dependent.getIncomeTaxYn());
			pstmt.setString(11, dependent.getChildUnder20Yn());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 부양가족 정보 단건 조회
	// 기본키(EMPLOYEE_DEPENDENT_ID)를 기준으로 단일 부양가족 데이터 반환
	public EmployeeDependent selectById(Connection conn, int depId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_DEPENDENT_ID, EMPLOYEE_ID, RELATION, DEP_NAME, NATIONAL_TYPE, JUMIN_NO, "
					+ "DISABLED_YN, BASIC_DEDUCT_YN, HEALTH_INS_YN, COHABIT_YN, INCOME_TAX_YN, CHILD_UNDER20_YN "
					+ "FROM EMPLOYEE_DEPENDENT WHERE EMPLOYEE_DEPENDENT_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, depId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeEmpDependentFromResultSet(rs);
			}
			return null; // 조회된 데이터가 없을 경우 null 반환
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 특정 사원의 부양가족 목록 조회
	// 사원번호(EMPLOYEE_ID)를 기준으로 연관된 부양가족 전체 목록 반환 (등록순 정렬)
	public List<EmployeeDependent> selectByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_DEPENDENT_ID, EMPLOYEE_ID, RELATION, DEP_NAME, NATIONAL_TYPE, JUMIN_NO, "
					+ "DISABLED_YN, BASIC_DEDUCT_YN, HEALTH_INS_YN, COHABIT_YN, INCOME_TAX_YN, CHILD_UNDER20_YN "
					+ "FROM EMPLOYEE_DEPENDENT WHERE EMPLOYEE_ID = ? ORDER BY EMPLOYEE_DEPENDENT_ID ASC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			rs = pstmt.executeQuery();

			List<EmployeeDependent> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeEmpDependentFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 부양가족 정보 수정
	// 기본키를 기준으로 부양가족 인적사항 및 공제 설정 데이터 수정
	public int update(Connection conn, EmployeeDependent dependent) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE EMPLOYEE_DEPENDENT SET "
					+ "EMPLOYEE_ID = ?, RELATION = ?, DEP_NAME = ?, NATIONAL_TYPE = ?, JUMIN_NO = ?, "
					+ "DISABLED_YN = ?, BASIC_DEDUCT_YN = ?, HEALTH_INS_YN = ?, COHABIT_YN = ?, "
					+ "INCOME_TAX_YN = ?, CHILD_UNDER20_YN = ? " + "WHERE EMPLOYEE_DEPENDENT_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dependent.getEmployeeId());
			pstmt.setString(2, dependent.getRelation());
			pstmt.setString(3, dependent.getDepName());
			pstmt.setString(4, dependent.getNationalType());
			pstmt.setString(5, dependent.getJuminNo());
			pstmt.setString(6, dependent.getDisabledYn());
			pstmt.setString(7, dependent.getBasicDeductYn());
			pstmt.setString(8, dependent.getHealthInsYn());
			pstmt.setString(9, dependent.getCohabitYn());
			pstmt.setString(10, dependent.getIncomeTaxYn());
			pstmt.setString(11, dependent.getChildUnder20Yn());
			pstmt.setInt(12, dependent.getEmployeeDependentId());

			return pstmt.executeUpdate(); // 수정된 행의 개수 반환
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 부양가족 정보 삭제
	// 기본키를 기준으로 해당 데이터 삭제
	public int delete(Connection conn, int depId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_DEPENDENT WHERE EMPLOYEE_DEPENDENT_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, depId);

			return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 EmployeeDependent 객체로 변환
	// 코드 중복 방지를 위한 공통 매핑 처리
	private EmployeeDependent makeEmpDependentFromResultSet(ResultSet rs) throws SQLException {
		EmployeeDependent dependent = new EmployeeDependent();

		dependent.setEmployeeDependentId(rs.getInt("EMPLOYEE_DEPENDENT_ID"));
		dependent.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		dependent.setRelation(rs.getString("RELATION"));
		dependent.setDepName(rs.getString("DEP_NAME"));
		dependent.setNationalType(rs.getString("NATIONAL_TYPE"));
		dependent.setJuminNo(rs.getString("JUMIN_NO"));
		dependent.setDisabledYn(rs.getString("DISABLED_YN"));
		dependent.setBasicDeductYn(rs.getString("BASIC_DEDUCT_YN"));
		dependent.setHealthInsYn(rs.getString("HEALTH_INS_YN"));
		dependent.setCohabitYn(rs.getString("COHABIT_YN"));
		dependent.setIncomeTaxYn(rs.getString("INCOME_TAX_YN"));
		dependent.setChildUnder20Yn(rs.getString("CHILD_UNDER20_YN"));

		return dependent;
	}
}