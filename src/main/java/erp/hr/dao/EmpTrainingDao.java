package erp.hr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.hr.model.EmpTraining;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 교육/훈련 내역 데이터베이스 접근(DAO) 클래스
public class EmpTrainingDao {

    // 싱글톤 인스턴스 생성
    private static EmpTrainingDao empTrainingDao = new EmpTrainingDao();

    // 싱글톤 접근 메서드
    public static EmpTrainingDao getInstance() {
        return empTrainingDao;
    }

    // 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
    private EmpTrainingDao() {}

    // 교육/훈련 내역 등록 (INSERT)
    // 시퀀스를 사용하여 기본키 발급 및 데이터 저장
    public void insert(Connection conn, EmpTraining training) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO EMP_TRAINING "
                       + "(TRAIN_ID, EMP_ID, TRAIN_TYPE, TRAIN_NAME, START_DATE, END_DATE, TRAIN_INSTITUTE, TRAIN_COST, REFUND_COST) "
                       + "VALUES (SEQ_EMP_TRAIN_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, training.getEmpId());
            pstmt.setString(2, training.getTrainType());
            pstmt.setString(3, training.getTrainName());
            
            // 교육 시작일과 종료일이 null일 경우를 대비한 데이터베이스 null 방어 로직 적용
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
            pstmt.setLong(7, training.getTrainCost());
            pstmt.setLong(8, training.getRefundCost());
            
            pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 교육/훈련 내역 단건 조회 (SELECT BY ID)
    // 기본키(TRAIN_ID)를 기준으로 1건의 데이터 조회
    public EmpTraining selectById(Connection conn, int trainId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT TRAIN_ID, EMP_ID, TRAIN_TYPE, TRAIN_NAME, START_DATE, END_DATE, TRAIN_INSTITUTE, TRAIN_COST, REFUND_COST "
                       + "FROM EMP_TRAINING WHERE TRAIN_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, trainId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return makeEmpTrainingFromResultSet(rs);
            }
            return null; // 조회된 데이터가 없을 경우 null 반환
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 특정 사원의 교육/훈련 내역 목록 조회
    // 사원번호(EMP_ID)를 기준으로 연관된 교육/훈련 내역 전체 반환 (최신 시작일 기준 내림차순 정렬)
    public List<EmpTraining> selectByEmpId(Connection conn, int empId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT TRAIN_ID, EMP_ID, TRAIN_TYPE, TRAIN_NAME, START_DATE, END_DATE, TRAIN_INSTITUTE, TRAIN_COST, REFUND_COST "
                       + "FROM EMP_TRAINING WHERE EMP_ID = ? ORDER BY START_DATE DESC, TRAIN_ID DESC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            rs = pstmt.executeQuery();
            
            List<EmpTraining> result = new ArrayList<>();
            while (rs.next()) {
                result.add(makeEmpTrainingFromResultSet(rs));
            }
            return result;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 교육/훈련 내역 수정 (UPDATE)
    // 기본키를 기준으로 교육 내용 및 비용 데이터 수정
    public int update(Connection conn, EmpTraining training) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE EMP_TRAINING SET "
                       + "EMP_ID = ?, TRAIN_TYPE = ?, TRAIN_NAME = ?, START_DATE = ?, END_DATE = ?, TRAIN_INSTITUTE = ?, TRAIN_COST = ?, REFUND_COST = ? "
                       + "WHERE TRAIN_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, training.getEmpId());
            pstmt.setString(2, training.getTrainType());
            pstmt.setString(3, training.getTrainName());
            
            // 날짜 null 방어 로직 적용
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
            pstmt.setLong(7, training.getTrainCost());
            pstmt.setLong(8, training.getRefundCost());
            pstmt.setInt(9, training.getTrainId());
            
            return pstmt.executeUpdate(); // 수정된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 교육/훈련 내역 삭제 (DELETE)
    // 기본키를 기준으로 해당 데이터 삭제
    public int delete(Connection conn, int trainId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM EMP_TRAINING WHERE TRAIN_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, trainId);
            
            return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 EmpTraining 객체로 변환
    // 코드 중복 방지를 위한 공통 매핑 처리
    private EmpTraining makeEmpTrainingFromResultSet(ResultSet rs) throws SQLException {
        EmpTraining training = new EmpTraining();
        training.setTrainId(rs.getInt("TRAIN_ID"));
        training.setEmpId(rs.getInt("EMP_ID"));
        training.setTrainType(rs.getString("TRAIN_TYPE"));
        training.setTrainName(rs.getString("TRAIN_NAME"));
        
        // 데이터베이스의 Date 값이 null일 경우를 대비한 안전한 변환 처리
        Timestamp startTs = rs.getTimestamp("START_DATE");
        if (startTs != null) {
            training.setStartDate(new java.util.Date(startTs.getTime()));
        }
        
        Timestamp endTs = rs.getTimestamp("END_DATE");
        if (endTs != null) {
            training.setEndDate(new java.util.Date(endTs.getTime()));
        }
        
        training.setTrainInstitute(rs.getString("TRAIN_INSTITUTE"));
        training.setTrainCost(rs.getLong("TRAIN_COST"));
        training.setRefundCost(rs.getLong("REFUND_COST"));
        
        return training;
    }
}