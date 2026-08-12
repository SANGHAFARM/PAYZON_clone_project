package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.employees.model.EmployeeSuretyInsurance;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 보증보험 내역 데이터베이스 접근(DAO) 클래스
public class EmployeeSuretyInsuranceDao {

    // 싱글톤 인스턴스 생성
    private static EmployeeSuretyInsuranceDao employeeSuretyInsuranceDao = new EmployeeSuretyInsuranceDao();

    // 싱글톤 접근 메서드
    public static EmployeeSuretyInsuranceDao getInstance() {
        return employeeSuretyInsuranceDao;
    }

    // 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
    private EmployeeSuretyInsuranceDao() {}

    // 보증보험 내역 등록 (INSERT)
    // 시퀀스를 사용하여 기본키 발급 및 데이터 저장
    public void insert(Connection conn, EmployeeSuretyInsurance ins) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO EMPLOYEE_SURETY_INSURANCE "
                       + "(EMPLOYEE_SURETY_INSURANCE_ID, EMPLOYEE_ID, PROVIDER_NAME, INSURANCE_NO, INSURANCE_AMT, SIGNUP_DATE, EXPIRE_DATE, NOTE) "
                       + "VALUES (SEQ_EMP_SURETY_INS_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, ins.getEmployeeId());
            pstmt.setString(2, ins.getProviderName());
            pstmt.setString(3, ins.getInsuranceNo());
            pstmt.setLong(4, ins.getInsuranceAmt());
            
            // 날짜 데이터가 null일 경우를 대비한 데이터베이스 null 방어 로직 적용
            if (ins.getSignupDate() == null) {
                pstmt.setNull(5, Types.DATE);
            } else {
                pstmt.setTimestamp(5, new Timestamp(ins.getSignupDate().getTime()));
            }
            
            if (ins.getExpireDate() == null) {
                pstmt.setNull(6, Types.DATE);
            } else {
                pstmt.setTimestamp(6, new Timestamp(ins.getExpireDate().getTime()));
            }
            
            pstmt.setString(7, ins.getNote());
            
            pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 보증보험 내역 단건 조회 (SELECT BY ID)
    // 기본키(EMPLOYEE_SURETY_INSURANCE_ID)를 기준으로 1건의 데이터 조회
    public EmployeeSuretyInsurance selectById(Connection conn, int suretyInsId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT EMPLOYEE_SURETY_INSURANCE_ID, EMPLOYEE_ID, PROVIDER_NAME, INSURANCE_NO, INSURANCE_AMT, SIGNUP_DATE, EXPIRE_DATE, NOTE "
                       + "FROM EMPLOYEE_SURETY_INSURANCE WHERE EMPLOYEE_SURETY_INSURANCE_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, suretyInsId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return makeEmpSuretyInsuranceFromResultSet(rs);
            }
            return null; // 조회된 데이터가 없을 경우 null 반환
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 특정 사원의 보증보험 내역 목록 조회
    // 사원번호(EMPLOYEE_ID)를 기준으로 연관된 보증보험 내역 전체 반환 (등록순 정렬)
    public List<EmployeeSuretyInsurance> selectByEmpId(Connection conn, int empId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT EMPLOYEE_SURETY_INSURANCE_ID, EMPLOYEE_ID, PROVIDER_NAME, INSURANCE_NO, INSURANCE_AMT, SIGNUP_DATE, EXPIRE_DATE, NOTE "
                       + "FROM EMPLOYEE_SURETY_INSURANCE WHERE EMPLOYEE_ID = ? ORDER BY EMPLOYEE_SURETY_INSURANCE_ID ASC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            rs = pstmt.executeQuery();
            
            List<EmployeeSuretyInsurance> result = new ArrayList<>();
            while (rs.next()) {
                result.add(makeEmpSuretyInsuranceFromResultSet(rs));
            }
            return result;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 보증보험 내역 수정 (UPDATE)
    // 기본키를 기준으로 기관 정보 및 날짜 데이터 수정
    public int update(Connection conn, EmployeeSuretyInsurance ins) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE EMPLOYEE_SURETY_INSURANCE SET "
                       + "EMPLOYEE_ID = ?, PROVIDER_NAME = ?, INSURANCE_NO = ?, INSURANCE_AMT = ?, SIGNUP_DATE = ?, EXPIRE_DATE = ?, NOTE = ? "
                       + "WHERE EMPLOYEE_SURETY_INSURANCE_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, ins.getEmployeeId());
            pstmt.setString(2, ins.getProviderName());
            pstmt.setString(3, ins.getInsuranceNo());
            pstmt.setLong(4, ins.getInsuranceAmt());
            
            // 날짜 null 방어 로직 적용
            if (ins.getSignupDate() == null) {
                pstmt.setNull(5, Types.DATE);
            } else {
                pstmt.setTimestamp(5, new Timestamp(ins.getSignupDate().getTime()));
            }
            
            if (ins.getExpireDate() == null) {
                pstmt.setNull(6, Types.DATE);
            } else {
                pstmt.setTimestamp(6, new Timestamp(ins.getExpireDate().getTime()));
            }
            
            pstmt.setString(7, ins.getNote());
            pstmt.setLong(8, ins.getEmployeeSuretyInsuranceId());
            
            return pstmt.executeUpdate(); // 수정된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 보증보험 내역 삭제 (DELETE)
    // 기본키를 기준으로 해당 데이터 삭제
    public int delete(Connection conn, int suretyInsId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM EMPLOYEE_SURETY_INSURANCE WHERE EMPLOYEE_SURETY_INSURANCE_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, suretyInsId);
            
            return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 EmployeeSuretyInsurance 객체로 변환
    // 코드 중복 방지를 위한 공통 매핑 처리
    private EmployeeSuretyInsurance makeEmpSuretyInsuranceFromResultSet(ResultSet rs) throws SQLException {
        EmployeeSuretyInsurance ins = new EmployeeSuretyInsurance();
        ins.setEmployeeSuretyInsuranceId(rs.getLong("EMPLOYEE_SURETY_INSURANCE_ID"));
        ins.setEmployeeId(rs.getLong("EMPLOYEE_ID"));
        ins.setProviderName(rs.getString("PROVIDER_NAME"));
        ins.setInsuranceNo(rs.getString("INSURANCE_NO"));
        ins.setInsuranceAmt(rs.getLong("INSURANCE_AMT"));
        
        // 데이터베이스의 Date 값이 null일 경우를 대비한 안전한 변환 처리
        Timestamp signupTs = rs.getTimestamp("SIGNUP_DATE");
        if (signupTs != null) {
            ins.setSignupDate(new java.util.Date(signupTs.getTime()));
        }
        
        Timestamp expireTs = rs.getTimestamp("EXPIRE_DATE");
        if (expireTs != null) {
            ins.setExpireDate(new java.util.Date(expireTs.getTime()));
        }
        
        ins.setNote(rs.getString("NOTE"));
        
        return ins;
    }
}
