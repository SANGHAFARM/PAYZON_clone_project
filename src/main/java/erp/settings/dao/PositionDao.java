package erp.settings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.settings.model.JobPosition;
import jdbc.JdbcUtil;

public class PositionDao {
	private static PositionDao positionDao = new PositionDao();
	public static PositionDao getInstance() {
		return positionDao;
	}
	private PositionDao() {
	}
	
	
	/*
	 * Position테이블에 직위 정보를 입력하는 메서드
	 * Positionテーブルに職位情報を入力するメソッド
	 */
	public int insert(Connection conn, JobPosition pos) throws SQLException{
		String sql = "INSERT INTO POSITION VALUES (SEQ_POS_ID.NEXTVAL, ?)";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, pos.getPosName());
			return pstmt.executeUpdate();
		}
	}
	
	/*
	 * Position테이블의 모든 데이터를 조회하는 메서드
	 * Positionテーブルの全てのデータを照会するメソッド
	 */	
	public List<JobPosition> select(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("SELECT * FROM POSITION");
			rs = pstmt.executeQuery();
			List<JobPosition> result = new ArrayList<>();
			while (rs.next()) {
				result.add(convertPosition(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}
	
	/*
	 * Position테이블에 있는 직위 정보를 수정하는 메서드
	 * Positionテーブルにある職位情報を修正するメソッド
	 */
	public int update(Connection conn, JobPosition pos) throws SQLException{
		String sql = "UPDATE POSITION SET POS_NAME=? WHERE POS_ID=?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, pos.getPosName());
			pstmt.setInt(2, pos.getPosId());
			return pstmt.executeUpdate();
		}
	}
	
	/*
	 * Position테이블에 있는 부서를 삭제하는 메서드
	 * Positionテーブルにある職位を削除するメソッド
	 */	
	public int delete(Connection conn, int no) throws SQLException {
		String sql = "DELETE FROM POSITION WHERE POS_ID=?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, no);
			return pstmt.executeUpdate();
		}
	}
	
	
	/*
	 * ResultSet으로 Position객체를 만들어 반환하는 메서드
	 * ResultSetでPositionオブジェクトを作って返すメソッド
	 */
	private JobPosition convertPosition(ResultSet rs) throws SQLException{
		JobPosition pos = new JobPosition();
		pos.setPosId(rs.getInt("POS_ID"));
		pos.setPosName("POS_NAME");
		return pos;
	}
}
