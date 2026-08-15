package erp.settings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.settings.dto.PayItemRow;
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
	
	// 지급항목 전체 목록과 연결된 비과세명, 근태항목명을 조인(Join)하여 조회 처리
	public List<PayItemRow> selectPayItemRows(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<PayItemRow> list = new ArrayList<>();

		try {
			// PAY_ITEM을 기준으로 TAX_FREE_ITEM과 ATTENDANCE_ITEM을 LEFT JOIN 하는 쿼리 작성
			String sql = "SELECT p.*, t.TAX_FREE_NAME, a.ATTEND_NAME "
					+ "FROM PAY_ITEM p "
					+ "LEFT JOIN TAX_FREE_ITEM t ON p.TAX_FREE_CODE = t.TAX_FREE_CODE "
					+ "LEFT JOIN ATTENDANCE_ITEM a ON p.LINK_ATTEND_ID = a.ATTENDANCE_ITEM_ID "
					+ "ORDER BY p.PAY_ITEM_ID ASC";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				PayItemRow row = new PayItemRow();
				row.setPayItemId(rs.getInt("PAY_ITEM_ID"));
				row.setPayName(rs.getString("PAY_NAME"));
				row.setTaxType(rs.getString("TAX_TYPE"));
				row.setTaxFreeCode(rs.getString("TAX_FREE_CODE"));
				row.setTaxFreeLimit(rs.getLong("TAX_FREE_LIMIT"));
				row.setCalcMethod(rs.getString("CALC_METHOD"));
				row.setRoundUnit(rs.getInt("ROUND_UNIT"));
				row.setPayMethod(rs.getString("PAY_METHOD"));
				
				// 외래키 값이 0(또는 NULL)인 경우에 대한 안전한 매핑
				int linkId = rs.getInt("LINK_ATTEND_ID");
				row.setLinkAttendId(rs.wasNull() ? null : linkId);
				
				long bulkAmount = rs.getLong("BULK_PAY_AMOUNT");
				row.setBulkPayAmount(rs.wasNull() ? null : bulkAmount);
				
				row.setUseYn(rs.getString("USE_YN"));
				row.setTaxFreeName(rs.getString("TAX_FREE_NAME"));
				row.setAttendName(rs.getString("ATTEND_NAME"));

				list.add(row);
			}
			return list;
		} finally {
			// 자원 누수 방지를 위한 반환 처리
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 기본키(PAY_ITEM_ID)를 기준으로 특정 지급항목 데이터를 단건 조회 처리
	public PayItem selectById(Connection conn, int payItemId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT * FROM PAY_ITEM WHERE PAY_ITEM_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, payItemId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				PayItem item = new PayItem();
				item.setPayItemId(rs.getInt("PAY_ITEM_ID"));
				item.setPayName(rs.getString("PAY_NAME"));
				item.setTaxType(rs.getString("TAX_TYPE"));
				item.setTaxFreeCode(rs.getString("TAX_FREE_CODE"));
				item.setTaxFreeLimit(rs.getLong("TAX_FREE_LIMIT"));
				item.setCalcMethod(rs.getString("CALC_METHOD"));
				item.setRoundUnit(rs.getInt("ROUND_UNIT"));
				item.setPayMethod(rs.getString("PAY_METHOD"));
				
				int linkId = rs.getInt("LINK_ATTEND_ID");
				item.setLinkAttendId(rs.wasNull() ? null : linkId);
				
				long bulkAmount = rs.getLong("BULK_PAY_AMOUNT");
				item.setBulkPayAmount(rs.wasNull() ? null : bulkAmount);
				
				item.setUseYn(rs.getString("USE_YN"));
				return item;
			}
			return null;
		} finally {
			// 자원 반환 처리
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 기존에 등록된 지급항목의 설정 데이터 갱신 처리
	// NULL이 가능한 항목(비과세코드, 근태연결, 일괄지급액)에 대한 분기 처리 포함
	public void update(Connection conn, PayItem item) throws SQLException {
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

			// 비과세 코드 NULL 처리
			if (item.getTaxFreeCode() != null && !item.getTaxFreeCode().isEmpty()) {
				pstmt.setString(3, item.getTaxFreeCode());
			} else {
				pstmt.setNull(3, java.sql.Types.VARCHAR);
			}

			pstmt.setLong(4, item.getTaxFreeLimit() != null ? item.getTaxFreeLimit() : 0L);
			pstmt.setString(5, item.getCalcMethod());
			pstmt.setInt(6, item.getRoundUnit() != null ? item.getRoundUnit() : 0);

			// 지급 방법 NULL 처리
			if (item.getPayMethod() != null && !item.getPayMethod().isEmpty()) {
				pstmt.setString(7, item.getPayMethod());
			} else {
				pstmt.setNull(7, java.sql.Types.VARCHAR);
			}

			// 근태항목 외래키 NULL 처리
			if (item.getLinkAttendId() != null && item.getLinkAttendId() > 0) {
				pstmt.setInt(8, item.getLinkAttendId());
			} else {
				pstmt.setNull(8, java.sql.Types.NUMERIC);
			}

			// 일괄지급액 NULL 처리
			if (item.getBulkPayAmount() != null) {
				pstmt.setLong(9, item.getBulkPayAmount());
			} else {
				pstmt.setNull(9, java.sql.Types.NUMERIC);
			}

			pstmt.setString(10, item.getUseYn());
			pstmt.setInt(11, item.getPayItemId());

			pstmt.executeUpdate();
		} finally {
			// 자원 반환 처리
			JdbcUtil.close(pstmt);
		}
	}

	// 기본키를 기준으로 특정 지급항목 데이터 완전 삭제 처리
	public void delete(Connection conn, int payItemId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM PAY_ITEM WHERE PAY_ITEM_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, payItemId);
			pstmt.executeUpdate();
		} finally {
			// 자원 반환 처리
			JdbcUtil.close(pstmt);
		}
	}
}