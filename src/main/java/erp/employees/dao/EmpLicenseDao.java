package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.employees.model.EmployeeLicense;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 자격/면허 내역 데이터베이스 접근(DAO) 클래스
public class EmpLicenseDao {

    // 싱글톤 인스턴스 생성
    private static EmpLicenseDao empLicenseDao = new EmpLicenseDao();

    // 싱글톤 접근 메서드
    public static EmpLicenseDao getInstance() {
        return empLicenseDao;
    }

    // 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
    private EmpLicenseDao() {}

    // 자격/면허 내역 등록
    // 시퀀스를 사용하여 기본키 발급 및 자격증 데이터 저장
    public void insert(Connection conn, EmployeeLicense license) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO EMP_LICENSE "
                       + "(LIC_ID, EMP_ID, LIC_NAME, ACQ_DATE, ISSUER, LICENSE_NO, NOTE) "
                       + "VALUES (SEQ_EMP_LIC_ID.NEXTVAL, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, license.getEmpId());
            pstmt.setString(2, license.getLicName());
            
            // 취득일자가 null일 경우를 대비한 데이터베이스 null 방어 로직 적용
            if (license.getAcqDate() == null) {
                pstmt.setNull(3, Types.DATE);
            } else {
                pstmt.setTimestamp(3, new Timestamp(license.getAcqDate().getTime()));
            }
            
            pstmt.setString(4, license.getIssuer());
            pstmt.setString(5, license.getLicenseNo());
            pstmt.setString(6, license.getNote());
            
            pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 자격/면허 내역 단건 조회
    // 기본키(LIC_ID)를 기준으로 1건의 데이터 반환
    public EmployeeLicense selectById(Connection conn, int licId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT LIC_ID, EMP_ID, LIC_NAME, ACQ_DATE, ISSUER, LICENSE_NO, NOTE "
                       + "FROM EMP_LICENSE WHERE LIC_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, licId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return makeEmpLicenseFromResultSet(rs);
            }
            return null; // 조회된 데이터가 없을 경우 null 반환
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 특정 사원의 자격/면허 내역 목록 조회
    // 사원번호(EMP_ID)를 기준으로 연관된 자격증 내역 전체 반환 (최근 취득일 기준 내림차순 정렬)
    public List<EmployeeLicense> selectByEmpId(Connection conn, int empId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT LIC_ID, EMP_ID, LIC_NAME, ACQ_DATE, ISSUER, LICENSE_NO, NOTE "
                       + "FROM EMP_LICENSE WHERE EMP_ID = ? ORDER BY ACQ_DATE DESC NULLS LAST, LIC_ID DESC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            rs = pstmt.executeQuery();
            
            List<EmployeeLicense> result = new ArrayList<>();
            while (rs.next()) {
                result.add(makeEmpLicenseFromResultSet(rs));
            }
            return result;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 자격/면허 내역 수정
    // 기본키를 기준으로 자격증명, 발급기관, 취득일자 등 상세 데이터 수정
    public int update(Connection conn, EmployeeLicense license) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE EMP_LICENSE SET "
                       + "EMP_ID = ?, LIC_NAME = ?, ACQ_DATE = ?, ISSUER = ?, LICENSE_NO = ?, NOTE = ? "
                       + "WHERE LIC_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, license.getEmpId());
            pstmt.setString(2, license.getLicName());
            
            // 날짜 null 방어 로직 적용
            if (license.getAcqDate() == null) {
                pstmt.setNull(3, Types.DATE);
            } else {
                pstmt.setTimestamp(3, new Timestamp(license.getAcqDate().getTime()));
            }
            
            pstmt.setString(4, license.getIssuer());
            pstmt.setString(5, license.getLicenseNo());
            pstmt.setString(6, license.getNote());
            pstmt.setInt(7, license.getLicId());
            
            return pstmt.executeUpdate(); // 수정된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 자격/면허 내역 삭제
    // 기본키를 기준으로 해당 데이터 삭제
    public int delete(Connection conn, int licId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM EMP_LICENSE WHERE LIC_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, licId);
            
            return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 EmployeeLicense 객체로 변환
    // 코드 중복 방지를 위한 공통 매핑 처리
    private EmployeeLicense makeEmpLicenseFromResultSet(ResultSet rs) throws SQLException {
        EmployeeLicense license = new EmployeeLicense();
        license.setLicId(rs.getInt("LIC_ID"));
        license.setEmpId(rs.getInt("EMP_ID"));
        license.setLicName(rs.getString("LIC_NAME"));
        
        // 데이터베이스의 Date 값이 null일 경우를 대비한 안전한 변환 처리
        Timestamp acqTs = rs.getTimestamp("ACQ_DATE");
        if (acqTs != null) {
            license.setAcqDate(new java.util.Date(acqTs.getTime()));
        }
        
        license.setIssuer(rs.getString("ISSUER"));
        license.setLicenseNo(rs.getString("LICENSE_NO"));
        license.setNote(rs.getString("NOTE"));
        
        return license;
    }
}