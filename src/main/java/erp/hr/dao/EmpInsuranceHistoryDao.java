package erp.hr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.hr.model.EmpInsuranceHistory;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 4대보험 자격정보 데이터베이스 접근(DAO) 클래스
public class EmpInsuranceHistoryDao {

    // 싱글톤 인스턴스 생성
    private static EmpInsuranceHistoryDao empInsuranceHistoryDao = new EmpInsuranceHistoryDao();

    // 싱글톤 접근 메서드
    public static EmpInsuranceHistoryDao getInstance() {
        return empInsuranceHistoryDao;
    }

    // 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
    private EmpInsuranceHistoryDao() {}

    // 4대보험 자격정보 등록
    // 시퀀스를 사용하여 기본키 발급 및 자격정보 데이터 저장
    public void insert(Connection conn, EmpInsuranceHistory history) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO EMP_INSURANCE_HISTORY "
                       + "(INS_HIST_ID, EMP_ID, INSURANCE_TYPE, SYMBOL_NO, ACQUIRE_DATE, LOSS_DATE) "
                       + "VALUES (SEQ_EMP_INS_HIST_ID.NEXTVAL, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, history.getEmpId());
            pstmt.setString(2, history.getInsuranceType());
            pstmt.setString(3, history.getSymbolNo());
            
            // 취득일자 및 상실일자가 null일 경우를 대비한 데이터베이스 null 방어 로직 적용
            if (history.getAcquireDate() == null) {
                pstmt.setNull(4, Types.DATE);
            } else {
                pstmt.setTimestamp(4, new Timestamp(history.getAcquireDate().getTime()));
            }
            
            if (history.getLossDate() == null) {
                pstmt.setNull(5, Types.DATE);
            } else {
                pstmt.setTimestamp(5, new Timestamp(history.getLossDate().getTime()));
            }
            
            pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 4대보험 자격정보 단건 조회
    // 기본키(INS_HIST_ID)를 기준으로 1건의 데이터 반환
    public EmpInsuranceHistory selectById(Connection conn, int insHistId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT INS_HIST_ID, EMP_ID, INSURANCE_TYPE, SYMBOL_NO, ACQUIRE_DATE, LOSS_DATE "
                       + "FROM EMP_INSURANCE_HISTORY WHERE INS_HIST_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, insHistId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return makeEmpInsuranceHistoryFromResultSet(rs);
            }
            return null; // 조회된 데이터가 없을 경우 null 반환
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 특정 사원의 4대보험 자격정보 목록 조회
    // 사원번호(EMP_ID)를 기준으로 연관된 자격정보 전체 반환 (최근 취득일 기준 내림차순 정렬)
    public List<EmpInsuranceHistory> selectByEmpId(Connection conn, int empId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT INS_HIST_ID, EMP_ID, INSURANCE_TYPE, SYMBOL_NO, ACQUIRE_DATE, LOSS_DATE "
                       + "FROM EMP_INSURANCE_HISTORY WHERE EMP_ID = ? ORDER BY ACQUIRE_DATE DESC NULLS LAST, INS_HIST_ID DESC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            rs = pstmt.executeQuery();
            
            List<EmpInsuranceHistory> result = new ArrayList<>();
            while (rs.next()) {
                result.add(makeEmpInsuranceHistoryFromResultSet(rs));
            }
            return result;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 4대보험 자격정보 수정
    // 기본키를 기준으로 보험 종류, 기호 번호 및 취득/상실 일자 데이터 수정
    public int update(Connection conn, EmpInsuranceHistory history) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE EMP_INSURANCE_HISTORY SET "
                       + "EMP_ID = ?, INSURANCE_TYPE = ?, SYMBOL_NO = ?, ACQUIRE_DATE = ?, LOSS_DATE = ? "
                       + "WHERE INS_HIST_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, history.getEmpId());
            pstmt.setString(2, history.getInsuranceType());
            pstmt.setString(3, history.getSymbolNo());
            
            // 날짜 null 방어 로직 적용
            if (history.getAcquireDate() == null) {
                pstmt.setNull(4, Types.DATE);
            } else {
                pstmt.setTimestamp(4, new Timestamp(history.getAcquireDate().getTime()));
            }
            
            if (history.getLossDate() == null) {
                pstmt.setNull(5, Types.DATE);
            } else {
                pstmt.setTimestamp(5, new Timestamp(history.getLossDate().getTime()));
            }
            
            pstmt.setInt(6, history.getInsHistId());
            
            return pstmt.executeUpdate(); // 수정된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 4대보험 자격정보 삭제
    // 기본키를 기준으로 해당 데이터 삭제
    public int delete(Connection conn, int insHistId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM EMP_INSURANCE_HISTORY WHERE INS_HIST_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, insHistId);
            
            return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 EmpInsuranceHistory 객체로 변환
    // 코드 중복 방지를 위한 공통 매핑 처리
    private EmpInsuranceHistory makeEmpInsuranceHistoryFromResultSet(ResultSet rs) throws SQLException {
        EmpInsuranceHistory history = new EmpInsuranceHistory();
        history.setInsHistId(rs.getInt("INS_HIST_ID"));
        history.setEmpId(rs.getInt("EMP_ID"));
        history.setInsuranceType(rs.getString("INSURANCE_TYPE"));
        history.setSymbolNo(rs.getString("SYMBOL_NO"));
        
        // 데이터베이스의 Date 값이 null일 경우를 대비한 안전한 변환 처리
        Timestamp acquireTs = rs.getTimestamp("ACQUIRE_DATE");
        if (acquireTs != null) {
            history.setAcquireDate(new java.util.Date(acquireTs.getTime()));
        }
        
        Timestamp lossTs = rs.getTimestamp("LOSS_DATE");
        if (lossTs != null) {
            history.setLossDate(new java.util.Date(lossTs.getTime()));
        }
        
        return history;
    }
}