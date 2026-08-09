package erp.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.payroll.model.PayItem;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 지급항목 데이터베이스 접근(DAO) 클래스
public class PayItemDao {

    // 지급항목 등록 (INSERT)
    // SEQ_PAY_ITEM_ID 시퀀스를 사용하여 PK 발급 및 데이터 저장
    public void insert(Connection conn, PayItem item) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO PAY_ITEM "
                       + "(PAY_ITEM_ID, PAY_NAME, TAX_TYPE, TAX_FREE_CODE, TAX_FREE_LIMIT, "
                       + "CALC_METHOD, ROUND_UNIT, PAY_METHOD, LINK_ATTEND_ID, BULK_PAY_AMOUNT, USE_YN) "
                       + "VALUES (SEQ_PAY_ITEM_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, item.getPayName());
            pstmt.setString(2, item.getTaxType());
            pstmt.setString(3, item.getTaxFreeCode());
            pstmt.setLong(4, item.getTaxFreeLimit());
            pstmt.setString(5, item.getCalcMethod());
            pstmt.setInt(6, item.getRoundUnit());
            pstmt.setString(7, item.getPayMethod());
            
            // Integer 타입인 근태연결 ID의 null 값 처리
            if (item.getLinkAttendId() == null) {
                pstmt.setNull(8, Types.INTEGER);
            } else {
                pstmt.setInt(8, item.getLinkAttendId());
            }
            
            // Long 타입인 일괄지급액의 null 값 처리
            if (item.getBulkPayAmount() == null) {
                pstmt.setNull(9, Types.NUMERIC);
            } else {
                pstmt.setLong(9, item.getBulkPayAmount());
            }
            
            pstmt.setString(10, item.getUseYn());
            
            pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 지급항목 단건 조회 (SELECT BY ID)
    // 기본키(PAY_ITEM_ID)를 기준으로 1건의 데이터 조회
    public PayItem selectById(Connection conn, int payItemId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT PAY_ITEM_ID, PAY_NAME, TAX_TYPE, TAX_FREE_CODE, TAX_FREE_LIMIT, "
                       + "CALC_METHOD, ROUND_UNIT, PAY_METHOD, LINK_ATTEND_ID, BULK_PAY_AMOUNT, USE_YN "
                       + "FROM PAY_ITEM WHERE PAY_ITEM_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, payItemId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return makePayItemFromResultSet(rs);
            }
            return null; // 조회된 데이터가 없을 경우 null 반환
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 지급항목 전체 목록 조회 (SELECT ALL)
    // 전체 목록을 내림차순(최신순)으로 정렬하여 조회
    public List<PayItem> selectAll(Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT PAY_ITEM_ID, PAY_NAME, TAX_TYPE, TAX_FREE_CODE, TAX_FREE_LIMIT, "
                       + "CALC_METHOD, ROUND_UNIT, PAY_METHOD, LINK_ATTEND_ID, BULK_PAY_AMOUNT, USE_YN "
                       + "FROM PAY_ITEM ORDER BY PAY_ITEM_ID DESC";
            
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            List<PayItem> result = new ArrayList<>();
            while (rs.next()) {
                result.add(makePayItemFromResultSet(rs));
            }
            return result;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 지급항목 수정 (UPDATE)
    // 기본키를 기준으로 항목의 설정값 수정
    public int update(Connection conn, PayItem item) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE PAY_ITEM SET "
                       + "PAY_NAME = ?, TAX_TYPE = ?, TAX_FREE_CODE = ?, TAX_FREE_LIMIT = ?, "
                       + "CALC_METHOD = ?, ROUND_UNIT = ?, PAY_METHOD = ?, LINK_ATTEND_ID = ?, "
                       + "BULK_PAY_AMOUNT = ?, USE_YN = ? "
                       + "WHERE PAY_ITEM_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, item.getPayName());
            pstmt.setString(2, item.getTaxType());
            pstmt.setString(3, item.getTaxFreeCode());
            pstmt.setLong(4, item.getTaxFreeLimit());
            pstmt.setString(5, item.getCalcMethod());
            pstmt.setInt(6, item.getRoundUnit());
            pstmt.setString(7, item.getPayMethod());
            
            // Integer 타입의 null 값 방어 로직 적용
            if (item.getLinkAttendId() == null) {
                pstmt.setNull(8, Types.INTEGER);
            } else {
                pstmt.setInt(8, item.getLinkAttendId());
            }
            
            // Long 타입의 null 값 방어 로직 적용
            if (item.getBulkPayAmount() == null) {
                pstmt.setNull(9, Types.NUMERIC);
            } else {
                pstmt.setLong(9, item.getBulkPayAmount());
            }
            
            pstmt.setString(10, item.getUseYn());
            pstmt.setInt(11, item.getPayItemId());
            
            return pstmt.executeUpdate(); // 수정된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 지급항목 삭제 (DELETE)
    // 기본키를 기준으로 데이터 삭제
    public int delete(Connection conn, int payItemId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM PAY_ITEM WHERE PAY_ITEM_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, payItemId);
            
            return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 PayItem 객체로 변환
    // 코드 중복 방지를 위한 ResultSet 매핑 공통 메서드 분리
    private PayItem makePayItemFromResultSet(ResultSet rs) throws SQLException {
        PayItem item = new PayItem();
        item.setPayItemId(rs.getInt("PAY_ITEM_ID"));
        item.setPayName(rs.getString("PAY_NAME"));
        item.setTaxType(rs.getString("TAX_TYPE"));
        item.setTaxFreeCode(rs.getString("TAX_FREE_CODE"));
        item.setTaxFreeLimit(rs.getLong("TAX_FREE_LIMIT"));
        item.setCalcMethod(rs.getString("CALC_METHOD"));
        item.setRoundUnit(rs.getInt("ROUND_UNIT"));
        item.setPayMethod(rs.getString("PAY_METHOD"));
        
        // 데이터베이스의 숫자 컬럼이 null일 경우 자바의 0으로 자동 변환되는 현상 방지
        int linkAttendId = rs.getInt("LINK_ATTEND_ID");
        if (!rs.wasNull()) {
            item.setLinkAttendId(linkAttendId);
        }
        
        long bulkPayAmount = rs.getLong("BULK_PAY_AMOUNT");
        if (!rs.wasNull()) {
            item.setBulkPayAmount(bulkPayAmount);
        }
        
        item.setUseYn(rs.getString("USE_YN"));
        return item;
    }
}