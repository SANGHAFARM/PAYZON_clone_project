package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.employees.model.EmployeeTraining;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 교육/훈련 이력 데이터베이스 접근(DAO) 클래스
public class EmployeeTrainingDao {

	// 싱글톤 인스턴스 생성
	private static EmployeeTrainingDao employeeTrainingDao = new EmployeeTrainingDao();

	// 싱글톤 접근 메서드
	public static EmployeeTrainingDao getInstance() {
		return employeeTrainingDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private EmployeeTrainingDao() {
	}

	// 교육/훈련 내역 등록
	public void insert(Connection conn, EmployeeTraining training) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO EMPLOYEE_TRAINING "
					+ "(EMPLOYEE_TRAINING_ID, EMPLOYEE_ID, TRAIN_TYPE, TRAIN_NAME, START_DATE, END_DATE, TRAIN_INSTITUTE, TRAIN_COST, REFUND_COST) "
					+ "VALUES (EMPLOYEE_TRAINING_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, training.getEmployeeId());
			pstmt.setString(2, training.getTrainType());
			pstmt.setString(3, training.getTrainName());

			if (training.getStartDate() == null) {
				pstmt.setNull(4, Types.DATE);
			} else {
				pstmt.setTimestamp(4, new Timestamp(training.getStartDate().getTime()));
			}

			if (training.getEndDate() == null) {
				pstmt.setNull(5, Types.DATE);
			} else {
				pstmt.setTimestamp(5, new Timestamp(training.getEndDate().getTime()));
			}

			pstmt.setString(6, training.getTrainInstitute());
			pstmt.setObject(7, training.getTrainCost(), Types.NUMERIC);
			pstmt.setObject(8, training.getRefundCost(), Types.NUMERIC);

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 교육/훈련 내역 단건 조회
	public EmployeeTraining selectById(Connection conn, int trainingId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_TRAINING_ID, EMPLOYEE_ID, TRAIN_TYPE, TRAIN_NAME, START_DATE, END_DATE, TRAIN_INSTITUTE, TRAIN_COST, REFUND_COST "
					+ "FROM EMPLOYEE_TRAINING WHERE EMPLOYEE_TRAINING_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, trainingId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeTrainingFromResultSet(rs);
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 특정 사원의 교육/훈련 내역 목록 조회
	public List<EmployeeTraining> selectByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_TRAINING_ID, EMPLOYEE_ID, TRAIN_TYPE, TRAIN_NAME, START_DATE, END_DATE, TRAIN_INSTITUTE, TRAIN_COST, REFUND_COST "
					+ "FROM EMPLOYEE_TRAINING WHERE EMPLOYEE_ID = ? ORDER BY START_DATE DESC NULLS LAST, EMPLOYEE_TRAINING_ID DESC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			rs = pstmt.executeQuery();

			List<EmployeeTraining> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeTrainingFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 교육/훈련 내역 수정
	public int update(Connection conn, EmployeeTraining training) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE EMPLOYEE_TRAINING SET "
					+ "EMPLOYEE_ID = ?, TRAIN_TYPE = ?, TRAIN_NAME = ?, START_DATE = ?, END_DATE = ?, TRAIN_INSTITUTE = ?, TRAIN_COST = ?, REFUND_COST = ? "
					+ "WHERE EMPLOYEE_TRAINING_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, training.getEmployeeId());
			pstmt.setString(2, training.getTrainType());
			pstmt.setString(3, training.getTrainName());

			if (training.getStartDate() == null) {
				pstmt.setNull(4, Types.DATE);
			} else {
				pstmt.setTimestamp(4, new Timestamp(training.getStartDate().getTime()));
			}

			if (training.getEndDate() == null) {
				pstmt.setNull(5, Types.DATE);
			} else {
				pstmt.setTimestamp(5, new Timestamp(training.getEndDate().getTime()));
			}

			pstmt.setString(6, training.getTrainInstitute());
			pstmt.setObject(7, training.getTrainCost(), Types.NUMERIC);
			pstmt.setObject(8, training.getRefundCost(), Types.NUMERIC);
			pstmt.setInt(9, training.getEmployeeTrainingId());

			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 교육/훈련 내역 삭제
	public int delete(Connection conn, int trainingId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_TRAINING WHERE EMPLOYEE_TRAINING_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, trainingId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 EmployeeTraining 객체로 변환
	private EmployeeTraining makeTrainingFromResultSet(ResultSet rs) throws SQLException {
		EmployeeTraining training = new EmployeeTraining();

		training.setEmployeeTrainingId(rs.getInt("EMPLOYEE_TRAINING_ID"));
		training.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		training.setTrainType(rs.getString("TRAIN_TYPE"));
		training.setTrainName(rs.getString("TRAIN_NAME"));

		Timestamp startTs = rs.getTimestamp("START_DATE");
		if (startTs != null) {
			training.setStartDate(new java.util.Date(startTs.getTime()));
		}

		Timestamp endTs = rs.getTimestamp("END_DATE");
		if (endTs != null) {
			training.setEndDate(new java.util.Date(endTs.getTime()));
		}

		training.setTrainInstitute(rs.getString("TRAIN_INSTITUTE"));

		long tCost = rs.getLong("TRAIN_COST");
		training.setTrainCost(rs.wasNull() ? null : tCost);

		long rCost = rs.getLong("REFUND_COST");
		training.setRefundCost(rs.wasNull() ? null : rCost);

		return training;
	}
}