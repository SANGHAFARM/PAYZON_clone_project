package erp.hr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.hr.model.EmpGuarantor;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 보증인 내역 데이터베이스 접근(DAO) 클래스
public class EmpGuarantorDao {

    // 싱글톤 인스턴스 생성
    private static EmpGuarantorDao empGuarantorDao = new EmpGuarantorDao();

    // 싱글톤 접근 메서드
    public static EmpGuarantorDao getInstance() {
        return empGuarantorDao;
    }

    // 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
    private EmpGuarantorDao() {}

    // 보증인 내역 등록
    // 시퀀스를 사용하여 기본키 발급 및 보증인 데이터 저장
    public void insert(Connection conn, EmpGuarantor guarantor) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO EMP_GUARANTOR "
                       + "(GUARANTOR_ID, EMP_ID, GUARANTOR_NAME, RELATION, JUMIN_NO, GUARANTEE_AMT, GUARANTEE_DATE, EXPIRE_DATE, TEL_NO) "
                       + "VALUES (SEQ_EMP_GUARANTOR_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, guarantor.getEmpId());
            pstmt.setString(2, guarantor.getGuarantorName());
            pstmt.setString(3, guarantor.getRelation());
            pstmt.setString(4, guarantor.getJuminNo());
            pstmt.setLong(5, guarantor.getGuaranteeAmt());
            
            // 날짜 데이터가 null일 경우를 대비한 데이터베이스 null 방어 로직 적용
            if (guarantor.getGuaranteeDate() == null) {
                pstmt.setNull(6, Types.DATE);
            } else {
                pstmt.setTimestamp(6, new Timestamp(guarantor.getGuaranteeDate().getTime()));
            }
            
            if (guarantor.getExpireDate() == null) {
                pstmt.setNull(7, Types.DATE);
            } else {
                pstmt.setTimestamp(7, new Timestamp(guarantor.getExpireDate().getTime()));
            }
            
            pstmt.setString(8, guarantor.getTelNo());
            
            pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 보증인 내역 단건 조회
    // 기본키(GUARANTOR_ID)를 기준으로 1건의 데이터 반환
    public EmpGuarantor selectById(Connection conn, int guarantorId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT GUARANTOR_ID, EMP_ID, GUARANTOR_NAME, RELATION, JUMIN_NO, GUARANTEE_AMT, GUARANTEE_DATE, EXPIRE_DATE, TEL_NO "
                       + "FROM EMP_GUARANTOR WHERE GUARANTOR_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, guarantorId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return makeEmpGuarantorFromResultSet(rs);
            }
            return null; // 조회된 데이터가 없을 경우 null 반환
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 특정 사원의 보증인 내역 목록 조회
    // 사원번호(EMP_ID)를 기준으로 연관된 보증인 내역 전체 반환 (등록순 정렬)
    public List<EmpGuarantor> selectByEmpId(Connection conn, int empId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT GUARANTOR_ID, EMP_ID, GUARANTOR_NAME, RELATION, JUMIN_NO, GUARANTEE_AMT, GUARANTEE_DATE, EXPIRE_DATE, TEL_NO "
                       + "FROM EMP_GUARANTOR WHERE EMP_ID = ? ORDER BY GUARANTOR_ID ASC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            rs = pstmt.executeQuery();
            
            List<EmpGuarantor> result = new ArrayList<>();
            while (rs.next()) {
                result.add(makeEmpGuarantorFromResultSet(rs));
            }
            return result;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 보증인 내역 수정
    // 기본키를 기준으로 보증인 인적사항 및 보증 금액, 날짜 데이터 수정
    public int update(Connection conn, EmpGuarantor guarantor) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE EMP_GUARANTOR SET "
                       + "EMP_ID = ?, GUARANTOR_NAME = ?, RELATION = ?, JUMIN_NO = ?, GUARANTEE_AMT = ?, GUARANTEE_DATE = ?, EXPIRE_DATE = ?, TEL_NO = ? "
                       + "WHERE GUARANTOR_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, guarantor.getEmpId());
            pstmt.setString(2, guarantor.getGuarantorName());
            pstmt.setString(3, guarantor.getRelation());
            pstmt.setString(4, guarantor.getJuminNo());
            pstmt.setLong(5, guarantor.getGuaranteeAmt());
            
            // 날짜 null 방어 로직 적용
            if (guarantor.getGuaranteeDate() == null) {
                pstmt.setNull(6, Types.DATE);
            } else {
                pstmt.setTimestamp(6, new Timestamp(guarantor.getGuaranteeDate().getTime()));
            }
            
            if (guarantor.getExpireDate() == null) {
                pstmt.setNull(7, Types.DATE);
            } else {
                pstmt.setTimestamp(7, new Timestamp(guarantor.getExpireDate().getTime()));
            }
            
            pstmt.setString(8, guarantor.getTelNo());
            pstmt.setInt(9, guarantor.getGuarantorId());
            
            return pstmt.executeUpdate(); // 수정된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 보증인 내역 삭제
    // 기본키를 기준으로 해당 데이터 삭제
    public int delete(Connection conn, int guarantorId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM EMP_GUARANTOR WHERE GUARANTOR_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, guarantorId);
            
            return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 EmpGuarantor 객체로 변환
    // 코드 중복 방지를 위한 공통 매핑 처리
    private EmpGuarantor makeEmpGuarantorFromResultSet(ResultSet rs) throws SQLException {
        EmpGuarantor guarantor = new EmpGuarantor();
        guarantor.setGuarantorId(rs.getInt("GUARANTOR_ID"));
        guarantor.setEmpId(rs.getInt("EMP_ID"));
        guarantor.setGuarantorName(rs.getString("GUARANTOR_NAME"));
        guarantor.setRelation(rs.getString("RELATION"));
        guarantor.setJuminNo(rs.getString("JUMIN_NO"));
        guarantor.setGuaranteeAmt(rs.getLong("GUARANTEE_AMT"));
        
        // 데이터베이스의 Date 값이 null일 경우를 대비한 안전한 변환 처리
        Timestamp guaranteeTs = rs.getTimestamp("GUARANTEE_DATE");
        if (guaranteeTs != null) {
            guarantor.setGuaranteeDate(new java.util.Date(guaranteeTs.getTime()));
        }
        
        Timestamp expireTs = rs.getTimestamp("EXPIRE_DATE");
        if (expireTs != null) {
            guarantor.setExpireDate(new java.util.Date(expireTs.getTime()));
        }
        
        guarantor.setTelNo(rs.getString("TEL_NO"));
        
        return guarantor;
    }
}