package erp.settings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.settings.model.PayItem;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 급여 지급항목 설정 데이터베이스 접근(DAO) 클래스
public class PayItemDao {

	// 싱글톤 인스턴스 생성
	private static PayItemDao payItemDao = new PayItemDao();

	// 싱글톤 접근 메서드
	public static PayItemDao getInstance() {
		return payItemDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private PayItemDao() {
	}

	// 지급항목 등록
	// 시퀀스를 사용하여 기본키 발급 및 항목 데이터 저장
	public void insert(Connection conn, PayItem item) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO PAY_ITEM "
					+ "(PAY_ITEM_ID, PAY_NAME, TAX_TYPE, TAX_FREE_CODE, TAX_FREE_LIMIT, CALC_METHOD, ROUND_UNIT, PAY_METHOD, LINK_ATTEND_ID, BULK_PAY_AMOUNT, USE_YN) "
					+ "VALUES (PAY_ITEM_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, item.getPayName());
			pstmt.setString(2, item.getTaxType());
			pstmt.setString(3, item.getTaxFreeCode());
			pstmt.setObject(4, item.getTaxFreeLimit(), Types.NUMERIC);
			pstmt.setString(5, item.getCalcMethod());
			pstmt.setObject(6, item.getRoundUnit(), Types.NUMERIC);
			pstmt.setString(7, item.getPayMethod());
			pstmt.setObject(8, item.getLinkAttendId(), Types.NUMERIC);
			pstmt.setObject(9, item.getBulkPayAmount(), Types.NUMERIC);
			pstmt.setString(10, item.getUseYn());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 지급항목 전체 목록 조회
	public List<PayItem> selectAll(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM PAY_ITEM ORDER BY PAY_ITEM_ID ASC";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			List<PayItem> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeItemFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 PayItem 객체로 변환
	private PayItem makeItemFromResultSet(ResultSet rs) throws SQLException {
		PayItem item = new PayItem();

		item.setPayItemId(rs.getInt("PAY_ITEM_ID"));
		item.setPayName(rs.getString("PAY_NAME"));
		item.setTaxType(rs.getString("TAX_TYPE"));
		item.setTaxFreeCode(rs.getString("TAX_FREE_CODE"));

		long tLimit = rs.getLong("TAX_FREE_LIMIT");
		item.setTaxFreeLimit(rs.wasNull() ? null : tLimit);

		item.setCalcMethod(rs.getString("CALC_METHOD"));

		int rUnit = rs.getInt("ROUND_UNIT");
		item.setRoundUnit(rs.wasNull() ? null : rUnit);

		item.setPayMethod(rs.getString("PAY_METHOD"));

		int lId = rs.getInt("LINK_ATTEND_ID");
		item.setLinkAttendId(rs.wasNull() ? null : lId);

		long bAmt = rs.getLong("BULK_PAY_AMOUNT");
		item.setBulkPayAmount(rs.wasNull() ? null : bAmt);

		item.setUseYn(rs.getString("USE_YN"));

		return item;
	}
}