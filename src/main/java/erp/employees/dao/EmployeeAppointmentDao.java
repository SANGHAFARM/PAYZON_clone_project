package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.employees.model.EmployeeAppointment;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 인사발령 내역 데이터베이스 접근(DAO) 클래스
public class EmployeeAppointmentDao {

    // 싱글톤 인스턴스 생성
    private static EmployeeAppointmentDao employeeAppointmentDao = new EmployeeAppointmentDao();

    // 싱글톤 접근 메서드
    public static EmployeeAppointmentDao getInstance() {
        return employeeAppointmentDao;
    }

    // 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
    private EmployeeAppointmentDao() {}

    // 인사발령 내역 등록
    // 시퀀스를 사용하여 기본키 발급 및 데이터 저장
    public void insert(Connection conn, EmployeeAppointment app) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO EMPLOYEE_APPOINTMENT "
                       + "(EMPLOYEE_APPOINTMENT_ID, EMPLOYEE_ID, APP_TYPE, APP_DATE, DEPARTMENT_NAME, JOB_POSITION_NAME, JOB_TITLE_DUTY, NOTE) "
                       + "VALUES (SEQ_EMP_APP_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, app.getEmployeeId());
            pstmt.setString(2, app.getAppType());
            
            // 발령일자가 null일 경우를 대비한 데이터베이스 null 방어 로직 적용
            if (app.getAppDate() == null) {
                pstmt.setNull(3, Types.DATE);
            } else {
                pstmt.setTimestamp(3, new Timestamp(app.getAppDate().getTime()));
            }
            
            pstmt.setString(4, app.getDepartmentName());
            pstmt.setString(5, app.getJobPositionName());
            pstmt.setString(6, app.getJobTitleDuty());
            pstmt.setString(7, app.getNote());
            
            pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 인사발령 내역 단건 조회
    // 기본키(EMPLOYEE_APPOINTMENT_ID)를 기준으로 1건의 데이터 조회
    public EmployeeAppointment selectById(Connection conn, int appId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT EMPLOYEE_APPOINTMENT_ID, EMPLOYEE_ID, APP_TYPE, APP_DATE, DEPARTMENT_NAME, JOB_POSITION_NAME, JOB_TITLE_DUTY, NOTE "
                       + "FROM EMPLOYEE_APPOINTMENT WHERE EMPLOYEE_APPOINTMENT_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, appId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return makeEmpAppointmentFromResultSet(rs);
            }
            return null; // 조회된 데이터가 없을 경우 null 반환
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 특정 사원의 인사발령 내역 목록 조회
    // 사원번호(EMPLOYEE_ID)를 기준으로 연관된 발령 내역 전체 반환 (발령일 기준 내림차순 정렬)
    public List<EmployeeAppointment> selectByEmpId(Connection conn, int empId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT EMPLOYEE_APPOINTMENT_ID, EMPLOYEE_ID, APP_TYPE, APP_DATE, DEPARTMENT_NAME, JOB_POSITION_NAME, JOB_TITLE_DUTY, NOTE "
                       + "FROM EMPLOYEE_APPOINTMENT WHERE EMPLOYEE_ID = ? ORDER BY APP_DATE DESC, EMPLOYEE_APPOINTMENT_ID DESC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            rs = pstmt.executeQuery();
            
            List<EmployeeAppointment> result = new ArrayList<>();
            while (rs.next()) {
                result.add(makeEmpAppointmentFromResultSet(rs));
            }
            return result;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 인사발령 내역 수정
    // 기본키를 기준으로 부서, 직위 및 기타 발령 상세 데이터 수정
    public int update(Connection conn, EmployeeAppointment app) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE EMPLOYEE_APPOINTMENT SET "
                       + "EMPLOYEE_ID = ?, APP_TYPE = ?, APP_DATE = ?, DEPARTMENT_NAME = ?, JOB_POSITION_NAME = ?, JOB_TITLE_DUTY = ?, NOTE = ? "
                       + "WHERE EMPLOYEE_APPOINTMENT_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, app.getEmployeeId());
            pstmt.setString(2, app.getAppType());
            
            // 날짜 null 방어 로직 적용
            if (app.getAppDate() == null) {
                pstmt.setNull(3, Types.DATE);
            } else {
                pstmt.setTimestamp(3, new Timestamp(app.getAppDate().getTime()));
            }
            
            pstmt.setString(4, app.getDepartmentName());
            pstmt.setString(5, app.getJobPositionName());
            pstmt.setString(6, app.getJobTitleDuty());
            pstmt.setString(7, app.getNote());
            pstmt.setLong(8, app.getEmployeeAppointmentId());
            
            return pstmt.executeUpdate(); // 수정된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 인사발령 내역 삭제
    // 기본키를 기준으로 해당 데이터 삭제
    public int delete(Connection conn, int appId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM EMPLOYEE_APPOINTMENT WHERE EMPLOYEE_APPOINTMENT_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, appId);
            
            return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 EmployeeAppointment 객체로 변환
    // 코드 중복 방지를 위한 공통 매핑 처리
    private EmployeeAppointment makeEmpAppointmentFromResultSet(ResultSet rs) throws SQLException {
        EmployeeAppointment app = new EmployeeAppointment();
        app.setEmployeeAppointmentId(rs.getLong("EMPLOYEE_APPOINTMENT_ID"));
        app.setEmployeeId(rs.getLong("EMPLOYEE_ID"));
        app.setAppType(rs.getString("APP_TYPE"));
        
        // 데이터베이스의 Date 값이 null일 경우를 대비한 안전한 변환 처리
        Timestamp appTs = rs.getTimestamp("APP_DATE");
        if (appTs != null) {
            app.setAppDate(new java.util.Date(appTs.getTime()));
        }
        
        app.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
        app.setJobPositionName(rs.getString("JOB_POSITION_NAME"));
        app.setJobTitleDuty(rs.getString("JOB_TITLE_DUTY"));
        app.setNote(rs.getString("NOTE"));
        
        return app;
    }
}
