package erp.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.payroll.model.DeductItem;
import jdbc.JdbcUtil; // 교재에서 사용하는 자원 반환용 유틸리티 클래스

// 공제항목 데이터베이스 접근(DAO) 클래스
public class DeductItemDao {

    // 공제항목 등록 (INSERT)
    // SEQ_DEDUCT_ITEM_ID 시퀀스를 사용하여 PK를 자동 발급
    public void insert(Connection conn, DeductItem item) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO DEDUCT_ITEM "
                       + "(DEDUCT_ITEM_ID, DEDUCT_NAME, CALC_METHOD, ROUND_UNIT, NOTE, USE_YN) "
                       + "VALUES (SEQ_DEDUCT_ITEM_ID.NEXTVAL, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, item.getDeductName());
            pstmt.setString(2, item.getCalcMethod());
            pstmt.setInt(3, item.getRoundUnit());
            pstmt.setString(4, item.getNote());
            pstmt.setString(5, item.getUseYn());
            
            pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 공제항목 단건 조회 (SELECT BY ID)
    // 기본키인 DEDUCT_ITEM_ID를 기준으로 공제항목 1건을 조회
    public DeductItem selectById(Connection conn, int deductItemId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT DEDUCT_ITEM_ID, DEDUCT_NAME, CALC_METHOD, ROUND_UNIT, NOTE, USE_YN "
                       + "FROM DEDUCT_ITEM WHERE DEDUCT_ITEM_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, deductItemId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return makeDeductItemFromResultSet(rs);
            }
            return null; // 해당하는 데이터가 없을 경우 null 반환
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 공제항목 전체 목록 조회 (SELECT ALL)
    // 최근 등록된 항목이 먼저 보이도록 내림차순(DESC)으로 전체 목록을 조회
    public List<DeductItem> selectAll(Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT DEDUCT_ITEM_ID, DEDUCT_NAME, CALC_METHOD, ROUND_UNIT, NOTE, USE_YN "
                       + "FROM DEDUCT_ITEM ORDER BY DEDUCT_ITEM_ID DESC";
            
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            List<DeductItem> result = new ArrayList<>();
            while (rs.next()) {
                result.add(makeDeductItemFromResultSet(rs));
            }
            return result;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 공제항목 수정 (UPDATE)
    // DEDUCT_ITEM_ID를 기준으로 나머지 설정값들을 수정
    public int update(Connection conn, DeductItem item) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE DEDUCT_ITEM SET "
                       + "DEDUCT_NAME = ?, CALC_METHOD = ?, ROUND_UNIT = ?, NOTE = ?, USE_YN = ? "
                       + "WHERE DEDUCT_ITEM_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, item.getDeductName());
            pstmt.setString(2, item.getCalcMethod());
            pstmt.setInt(3, item.getRoundUnit());
            pstmt.setString(4, item.getNote());
            pstmt.setString(5, item.getUseYn());
            pstmt.setInt(6, item.getDeductItemId());
            
            return pstmt.executeUpdate(); // 수정된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 공제항목 삭제 (DELETE)
    public int delete(Connection conn, int deductItemId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM DEDUCT_ITEM WHERE DEDUCT_ITEM_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, deductItemId);
            
            return pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // [공통 메서드] ResultSet 데이터를 DeductItem 객체로 변환
    // 코드의 중복을 방지하기 위해 별도의 private 메서드로 분리
    private DeductItem makeDeductItemFromResultSet(ResultSet rs) throws SQLException {
        DeductItem item = new DeductItem();
        item.setDeductItemId(rs.getInt("DEDUCT_ITEM_ID"));
        item.setDeductName(rs.getString("DEDUCT_NAME"));
        item.setCalcMethod(rs.getString("CALC_METHOD"));
        item.setRoundUnit(rs.getInt("ROUND_UNIT"));
        item.setNote(rs.getString("NOTE"));
        item.setUseYn(rs.getString("USE_YN"));
        return item;
    }
}