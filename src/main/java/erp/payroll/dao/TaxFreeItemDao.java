package erp.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.payroll.model.TaxFreeItem;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 비과세 목록 데이터베이스 접근(DAO) 클래스
public class TaxFreeItemDao {
	
	// 싱글톤 인스턴스 생성
	private static TaxFreeItemDao taxFreeItemDao = new TaxFreeItemDao();
	
	// 싱글톤 접근 메서드
	public static TaxFreeItemDao getInstance() {
		return taxFreeItemDao;
	}
	
	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private TaxFreeItemDao() {}

    // 비과세 목록 등록
    // 기본키(TAX_FREE_CODE)를 포함하여 데이터 저장
    public void insert(Connection conn, TaxFreeItem item) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO TAX_FREE_ITEM "
                       + "(TAX_FREE_CODE, LEGAL_CLAUSE, REPORT_FIELD, TAX_FREE_NAME, "
                       + "DEFAULT_LIMIT, PAY_STATEMENT_YN, INCOME_CATEGORY) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, item.getTaxFreeCode());
            pstmt.setString(2, item.getLegalClause());
            pstmt.setString(3, item.getReportField());
            pstmt.setString(4, item.getTaxFreeName());
            pstmt.setLong(5, item.getDefaultLimit());
            pstmt.setString(6, item.getPayStatementYn());
            pstmt.setString(7, item.getIncomeCategory());
            
            pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 비과세 목록 단건 조회
    // 기본키(TAX_FREE_CODE)를 기준으로 1건의 데이터 조회
    public TaxFreeItem selectById(Connection conn, String taxFreeCode) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT TAX_FREE_CODE, LEGAL_CLAUSE, REPORT_FIELD, TAX_FREE_NAME, "
                       + "DEFAULT_LIMIT, PAY_STATEMENT_YN, INCOME_CATEGORY "
                       + "FROM TAX_FREE_ITEM WHERE TAX_FREE_CODE = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, taxFreeCode);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return makeTaxFreeItemFromResultSet(rs);
            }
            return null;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 비과세 목록 전체 조회
    // 전체 목록을 코드 기준 오름차순으로 정렬하여 반환
    public List<TaxFreeItem> selectAll(Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT TAX_FREE_CODE, LEGAL_CLAUSE, REPORT_FIELD, TAX_FREE_NAME, "
                       + "DEFAULT_LIMIT, PAY_STATEMENT_YN, INCOME_CATEGORY "
                       + "FROM TAX_FREE_ITEM ORDER BY TAX_FREE_CODE ASC";
            
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            List<TaxFreeItem> result = new ArrayList<>();
            while (rs.next()) {
                result.add(makeTaxFreeItemFromResultSet(rs));
            }
            return result;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 비과세 목록 수정
    // 기본키를 기준으로 항목의 세부 설정값 수정
    public int update(Connection conn, TaxFreeItem item) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE TAX_FREE_ITEM SET "
                       + "LEGAL_CLAUSE = ?, REPORT_FIELD = ?, TAX_FREE_NAME = ?, "
                       + "DEFAULT_LIMIT = ?, PAY_STATEMENT_YN = ?, INCOME_CATEGORY = ? "
                       + "WHERE TAX_FREE_CODE = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, item.getLegalClause());
            pstmt.setString(2, item.getReportField());
            pstmt.setString(3, item.getTaxFreeName());
            pstmt.setLong(4, item.getDefaultLimit());
            pstmt.setString(5, item.getPayStatementYn());
            pstmt.setString(6, item.getIncomeCategory());
            pstmt.setString(7, item.getTaxFreeCode());
            
            return pstmt.executeUpdate(); // 수정된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 비과세 목록 삭제
    // 기본키를 기준으로 데이터 삭제
    public int delete(Connection conn, String taxFreeCode) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM TAX_FREE_ITEM WHERE TAX_FREE_CODE = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, taxFreeCode);
            
            return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 TaxFreeItem 객체로 변환
    // 코드 중복 방지를 위한 공통 매핑 처리
    private TaxFreeItem makeTaxFreeItemFromResultSet(ResultSet rs) throws SQLException {
        TaxFreeItem item = new TaxFreeItem();
        item.setTaxFreeCode(rs.getString("TAX_FREE_CODE"));
        item.setLegalClause(rs.getString("LEGAL_CLAUSE"));
        item.setReportField(rs.getString("REPORT_FIELD"));
        item.setTaxFreeName(rs.getString("TAX_FREE_NAME"));
        item.setDefaultLimit(rs.getLong("DEFAULT_LIMIT"));
        item.setPayStatementYn(rs.getString("PAY_STATEMENT_YN"));
        item.setIncomeCategory(rs.getString("INCOME_CATEGORY"));
        return item;
    }
}