package erp.settings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.settings.model.TaxFreeItem;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 비과세/감면 소득 설정 데이터베이스 접근(DAO) 클래스
public class TaxFreeItemDao {

	// 싱글톤 인스턴스 생성
	private static TaxFreeItemDao taxFreeItemDao = new TaxFreeItemDao();

	// 싱글톤 접근 메서드
	public static TaxFreeItemDao getInstance() {
		return taxFreeItemDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private TaxFreeItemDao() {
	}

	// 비과세 설정 항목 등록
	public void insert(Connection conn, TaxFreeItem item) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO TAX_FREE_ITEM (TAX_FREE_CODE, LEGAL_CLAUSE, REPORT_FIELD, TAX_FREE_NAME, DEFAULT_LIMIT, PAY_STATEMENT_YN, INCOME_CATEGORY) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, item.getTaxFreeCode());
			pstmt.setString(2, item.getLegalClause());
			pstmt.setString(3, item.getReportField());
			pstmt.setString(4, item.getTaxFreeName());
			pstmt.setObject(5, item.getDefaultLimit(), Types.NUMERIC);
			pstmt.setString(6, item.getPayStatementYn());
			pstmt.setString(7, item.getIncomeCategory());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 비과세 설정 항목 전체 조회
	public List<TaxFreeItem> selectAll(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM TAX_FREE_ITEM "
					+ "ORDER BY CASE WHEN INCOME_CATEGORY = '비과세' THEN 1 ELSE 2 END, TAX_FREE_CODE ASC";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			List<TaxFreeItem> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeItemFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 비과세 코드를 기준으로 법정 항목과 기본 한도액을 조회
	public TaxFreeItem selectByCode(Connection conn, String taxFreeCode) throws SQLException {
		String sql = "SELECT * FROM TAX_FREE_ITEM WHERE TAX_FREE_CODE = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, taxFreeCode);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next() ? makeItemFromResultSet(rs) : null;
			}
		}
	}

	// 기본 법정 코드가 없는 경우에만 마스터 목록에 추가한다.
	public void insertIfAbsent(Connection conn, TaxFreeItem item) throws SQLException {
		String sql = "MERGE INTO TAX_FREE_ITEM T USING (SELECT ? TAX_FREE_CODE FROM DUAL) S "
				+ "ON (T.TAX_FREE_CODE = S.TAX_FREE_CODE) WHEN NOT MATCHED THEN INSERT "
				+ "(TAX_FREE_CODE, LEGAL_CLAUSE, REPORT_FIELD, TAX_FREE_NAME, DEFAULT_LIMIT, PAY_STATEMENT_YN, INCOME_CATEGORY) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, item.getTaxFreeCode());
			pstmt.setString(2, item.getTaxFreeCode());
			pstmt.setString(3, item.getLegalClause());
			pstmt.setString(4, item.getReportField());
			pstmt.setString(5, item.getTaxFreeName());
			pstmt.setLong(6, item.getDefaultLimit());
			pstmt.setString(7, item.getPayStatementYn());
			pstmt.setString(8, item.getIncomeCategory());
			pstmt.executeUpdate();
		}
	}

	// 사용자 정의 코드의 다음 번호를 발급한다.
	public String selectNextUserCode(Connection conn) throws SQLException {
		String sql = "SELECT 'U' || LPAD(NVL(MAX(TO_NUMBER(SUBSTR(TAX_FREE_CODE, 2))), 0) + 1, 5, '0') "
				+ "FROM TAX_FREE_ITEM WHERE REGEXP_LIKE(TAX_FREE_CODE, '^U[0-9]{5}$')";
		try (PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			rs.next();
			return rs.getString(1);
		}
	}

	// ResultSet 데이터를 TaxFreeItem 객체로 변환
	private TaxFreeItem makeItemFromResultSet(ResultSet rs) throws SQLException {
		TaxFreeItem item = new TaxFreeItem();

		item.setTaxFreeCode(rs.getString("TAX_FREE_CODE"));
		item.setLegalClause(rs.getString("LEGAL_CLAUSE"));
		item.setReportField(rs.getString("REPORT_FIELD"));
		item.setTaxFreeName(rs.getString("TAX_FREE_NAME"));

		long limit = rs.getLong("DEFAULT_LIMIT");
		item.setDefaultLimit(rs.wasNull() ? null : limit);

		item.setPayStatementYn(rs.getString("PAY_STATEMENT_YN"));
		item.setIncomeCategory(rs.getString("INCOME_CATEGORY"));

		return item;
	}
}
