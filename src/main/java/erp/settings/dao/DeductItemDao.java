package erp.settings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.settings.model.DeductItem;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 급여 공제항목 설정 데이터베이스 접근(DAO) 클래스
public class DeductItemDao {

	// 싱글톤 인스턴스 생성
	private static DeductItemDao deductItemDao = new DeductItemDao();

	// 싱글톤 접근 메서드
	public static DeductItemDao getInstance() {
		return deductItemDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private DeductItemDao() {
	}

	// 공제항목 등록
	// 시퀀스를 사용하여 기본키 발급 및 항목 데이터 저장
	public void insert(Connection conn, DeductItem item) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO DEDUCT_ITEM (DEDUCT_ITEM_ID, DEDUCT_NAME, CALC_METHOD, ROUND_UNIT, NOTE, USE_YN) "
					+ "VALUES (DEDUCT_ITEM_SEQ.NEXTVAL, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, item.getDeductName());
			pstmt.setString(2, item.getCalcMethod());
			pstmt.setObject(3, item.getRoundUnit(), Types.NUMERIC);
			pstmt.setString(4, item.getNote());
			pstmt.setString(5, item.getUseYn());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 공제항목 전체 목록 조회
	public List<DeductItem> selectAll(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM DEDUCT_ITEM ORDER BY DEDUCT_ITEM_ID ASC";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			List<DeductItem> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeItemFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 DeductItem 객체로 변환
	private DeductItem makeItemFromResultSet(ResultSet rs) throws SQLException {
		DeductItem item = new DeductItem();

		item.setDeductItemId(rs.getInt("DEDUCT_ITEM_ID"));
		item.setDeductName(rs.getString("DEDUCT_NAME"));
		item.setCalcMethod(rs.getString("CALC_METHOD"));

		int rUnit = rs.getInt("ROUND_UNIT");
		item.setRoundUnit(rs.wasNull() ? null : rUnit);

		item.setNote(rs.getString("NOTE"));
		item.setUseYn(rs.getString("USE_YN"));

		return item;
	}
	
	// 기본키(DEDUCT_ITEM_ID)를 기준으로 특정 공제항목 데이터를 단건 조회 처리
	public DeductItem selectById(Connection conn, int deductItemId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			// 기본키를 조건으로 공제항목을 조회하는 쿼리 작성
			String sql = "SELECT * FROM DEDUCT_ITEM WHERE DEDUCT_ITEM_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, deductItemId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				DeductItem item = new DeductItem();
				item.setDeductItemId(rs.getInt("DEDUCT_ITEM_ID"));
				item.setDeductName(rs.getString("DEDUCT_NAME"));
				item.setCalcMethod(rs.getString("CALC_METHOD"));
				item.setRoundUnit(rs.getInt("ROUND_UNIT"));
				item.setNote(rs.getString("NOTE"));
				item.setUseYn(rs.getString("USE_YN"));
				return item;
			}
			return null;
		} finally {
			// 사용 완료된 객체 반환 처리
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 기존에 등록된 공제항목의 명칭, 계산방법, 절사단위 등 설정 데이터 갱신 처리
	public void update(Connection conn, DeductItem item) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			// 공제항목 정보 갱신을 위한 UPDATE 쿼리문 작성
			String sql = "UPDATE DEDUCT_ITEM SET "
					+ "DEDUCT_NAME = ?, CALC_METHOD = ?, ROUND_UNIT = ?, NOTE = ?, USE_YN = ? "
					+ "WHERE DEDUCT_ITEM_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, item.getDeductName());
			pstmt.setString(2, item.getCalcMethod());
			pstmt.setInt(3, item.getRoundUnit() != null ? item.getRoundUnit() : 0);
			pstmt.setString(4, item.getNote());
			pstmt.setString(5, item.getUseYn());
			pstmt.setInt(6, item.getDeductItemId());

			// 쿼리 실행 수행
			pstmt.executeUpdate();
		} finally {
			// 자원 반환 처리
			JdbcUtil.close(pstmt);
		}
	}

	// 기본키를 기준으로 특정 공제항목 데이터 완전 삭제 처리
	public void delete(Connection conn, int deductItemId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			// 기본키 기반 레코드 삭제 쿼리문 작성
			String sql = "DELETE FROM DEDUCT_ITEM WHERE DEDUCT_ITEM_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, deductItemId);
			
			// 쿼리 실행 수행
			pstmt.executeUpdate();
		} finally {
			// 자원 반환 처리
			JdbcUtil.close(pstmt);
		}
	}
}