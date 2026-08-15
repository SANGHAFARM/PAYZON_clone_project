package erp.settings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.settings.model.AttendanceItem;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 근태항목 설정 데이터베이스 접근(DAO) 클래스
public class AttendanceItemDao {

	// 싱글톤 인스턴스 생성
	private static AttendanceItemDao attendanceItemDao = new AttendanceItemDao();

	// 싱글톤 접근 메서드
	public static AttendanceItemDao getInstance() {
		return attendanceItemDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private AttendanceItemDao() {
	}

	// 근태항목 등록
	// 시퀀스를 사용하여 기본키 발급 및 항목 데이터 저장
	public void insert(Connection conn, AttendanceItem item) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO ATTENDANCE_ITEM (ATTENDANCE_ITEM_ID, ATTENDANCE_GROUP_ID, ATTEND_NAME, UNIT_TYPE, DEDUCT_LEAVE_ID, WORK_HOUR_TYPE, USE_YN) "
					+ "VALUES (ATTENDANCE_ITEM_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, item.getAttendanceGroupId());
			pstmt.setString(2, item.getAttendName());
			pstmt.setString(3, item.getUnitType());
			pstmt.setObject(4, item.getDeductLeaveId(), Types.NUMERIC);
			pstmt.setString(5, item.getWorkHourType());
			pstmt.setString(6, item.getUseYn());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 근태항목 전체 목록 조회
	public List<AttendanceItem> selectAll(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM ATTENDANCE_ITEM ORDER BY ATTENDANCE_GROUP_ID ASC, ATTENDANCE_ITEM_ID ASC";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			List<AttendanceItem> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeItemFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	public AttendanceItem selectById(Connection conn, int id) throws SQLException {
		String sql = "SELECT * FROM ATTENDANCE_ITEM WHERE ATTENDANCE_ITEM_ID = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return makeItemFromResultSet(rs);
				}
			}
		}
		return null;
	}

	// ResultSet 데이터를 AttendanceItem 객체로 변환
	private AttendanceItem makeItemFromResultSet(ResultSet rs) throws SQLException {
		AttendanceItem item = new AttendanceItem();

		item.setAttendanceItemId(rs.getInt("ATTENDANCE_ITEM_ID"));
		item.setAttendanceGroupId(rs.getInt("ATTENDANCE_GROUP_ID"));
		item.setAttendName(rs.getString("ATTEND_NAME"));
		item.setUnitType(rs.getString("UNIT_TYPE"));

		int deductId = rs.getInt("DEDUCT_LEAVE_ID");
		item.setDeductLeaveId(rs.wasNull() ? null : deductId);

		item.setWorkHourType(rs.getString("WORK_HOUR_TYPE"));
		item.setUseYn(rs.getString("USE_YN"));

		return item;
	}
	
	// 기존에 등록된 근태항목 정보 수정 처리
	public void update(Connection conn, AttendanceItem item) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			// 근태항목 정보 갱신을 위한 UPDATE 쿼리문 작성
			String sql = "UPDATE ATTENDANCE_ITEM "
					+ "SET ATTEND_NAME = ?, UNIT_TYPE = ?, ATTENDANCE_GROUP_ID = ?, "
					+ "DEDUCT_LEAVE_ID = ?, WORK_HOUR_TYPE = ?, USE_YN = ? "
					+ "WHERE ATTENDANCE_ITEM_ID = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, item.getAttendName());
			pstmt.setString(2, item.getUnitType());
			pstmt.setInt(3, item.getAttendanceGroupId());
			
			// 외래키인 휴가공제 식별 번호가 0보다 크면 정수 할당, 아니면 DB에 NULL 세팅
			if (item.getDeductLeaveId() > 0) {
				pstmt.setInt(4, item.getDeductLeaveId());
			} else {
				pstmt.setNull(4, java.sql.Types.NUMERIC);
			}
			
			// 근로시간연계 속성이 존재하면 문자열 할당, 아니면 DB에 NULL 세팅
			if (item.getWorkHourType() != null && !item.getWorkHourType().trim().isEmpty()) {
				pstmt.setString(5, item.getWorkHourType());
			} else {
				pstmt.setNull(5, java.sql.Types.VARCHAR);
			}
			
			pstmt.setString(6, item.getUseYn());
			pstmt.setInt(7, item.getAttendanceItemId()); // 식별 가능한 기본키 매핑
			
			// 쿼리 실행 수행
			pstmt.executeUpdate();
			
		} finally {
			// 자원 누수 방지를 위한 객체 반환 처리
			JdbcUtil.close(pstmt);
		}
	}

	// 기본키를 기준으로 특정 근태항목 데이터 완전 삭제 처리
	public void delete(Connection conn, int attendItemId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			// 기본키 기반 레코드 삭제 쿼리문 작성
			String sql = "DELETE FROM ATTENDANCE_ITEM WHERE ATTENDANCE_ITEM_ID = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, attendItemId);
			
			// 쿼리 실행 수행
			pstmt.executeUpdate();
			
		} finally {
			// 자원 누수 방지를 위한 객체 반환 처리
			JdbcUtil.close(pstmt);
		}
	}
}