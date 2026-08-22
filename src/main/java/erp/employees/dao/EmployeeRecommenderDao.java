package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.employees.model.EmployeeRecommender;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 추천인 내역 데이터베이스 접근(DAO) 클래스
// 사원추천인 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 社員推薦人データをデータベースから照会し、登録・更新・削除する。
public class EmployeeRecommenderDao {

	// 싱글톤 인스턴스 생성
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static EmployeeRecommenderDao employeeRecommenderDao = new EmployeeRecommenderDao();

	// 싱글톤 접근 메서드
	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static EmployeeRecommenderDao getInstance() {
		return employeeRecommenderDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	// 전달받은 값으로 사원추천인 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で社員推薦人オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private EmployeeRecommenderDao() {
	}

	// 추천인 내역 등록
	// 전달받은 사원추천인 데이터를 데이터베이스에 등록하고 처리 건수를 반환한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 受け取った社員推薦人データをデータベースへ登録し、処理件数を返す。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void insert(Connection conn, EmployeeRecommender recommender) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO EMPLOYEE_RECOMMENDER "
					+ "(EMPLOYEE_RECOMMENDER_ID, EMPLOYEE_ID, RECOMMENDER_NAME, RELATION, COMPANY_NAME, POSITION_NAME, TEL_NO) "
					+ "VALUES (EMPLOYEE_RECOMMENDER_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, recommender.getEmployeeId());
			pstmt.setString(2, recommender.getRecommenderName());
			pstmt.setString(3, recommender.getRelation());
			pstmt.setString(4, recommender.getCompanyName());
			pstmt.setString(5, recommender.getPositionName());
			pstmt.setString(6, recommender.getTelNo());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 추천인 내역 단건 조회
	// 조회 조건에 맞는 By식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うBy識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public EmployeeRecommender selectById(Connection conn, int recId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_RECOMMENDER_ID, EMPLOYEE_ID, RECOMMENDER_NAME, RELATION, COMPANY_NAME, POSITION_NAME, TEL_NO "
					+ "FROM EMPLOYEE_RECOMMENDER WHERE EMPLOYEE_RECOMMENDER_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, recId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeRecommenderFromResultSet(rs);
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 특정 사원의 추천인 내역 목록 조회
	// 조회 조건에 맞는 ByEmp식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うByEmp識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<EmployeeRecommender> selectByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_RECOMMENDER_ID, EMPLOYEE_ID, RECOMMENDER_NAME, RELATION, COMPANY_NAME, POSITION_NAME, TEL_NO "
					+ "FROM EMPLOYEE_RECOMMENDER WHERE EMPLOYEE_ID = ? ORDER BY EMPLOYEE_RECOMMENDER_ID ASC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			rs = pstmt.executeQuery();

			List<EmployeeRecommender> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeRecommenderFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 추천인 내역 수정
	// 식별조건에 해당하는 사원추천인 데이터를 전달받은 값으로 수정한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 識別条件に該当する社員推薦人データを受け取った値で更新する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int update(Connection conn, EmployeeRecommender recommender) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE EMPLOYEE_RECOMMENDER SET "
					+ "EMPLOYEE_ID = ?, RECOMMENDER_NAME = ?, RELATION = ?, COMPANY_NAME = ?, POSITION_NAME = ?, TEL_NO = ? "
					+ "WHERE EMPLOYEE_RECOMMENDER_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, recommender.getEmployeeId());
			pstmt.setString(2, recommender.getRecommenderName());
			pstmt.setString(3, recommender.getRelation());
			pstmt.setString(4, recommender.getCompanyName());
			pstmt.setString(5, recommender.getPositionName());
			pstmt.setString(6, recommender.getTelNo());
			pstmt.setInt(7, recommender.getEmployeeRecommenderId());

			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 추천인 내역 삭제
	// 선택되거나 식별된 사원추천인 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別された社員推薦人データを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int delete(Connection conn, int recId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_RECOMMENDER WHERE EMPLOYEE_RECOMMENDER_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, recId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 사원번호(EMPLOYEE_ID)를 기준으로 해당 사원의 추천인 전체 삭제
	// 선택되거나 식별된 ByEmp식별번호 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別されたByEmp識別番号データを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void deleteByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_RECOMMENDER WHERE EMPLOYEE_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 EmployeeRecommender 객체로 변환
	// 조회값과 입력값을 조합하여 추천인From처리결과Set 처리 데이터를 구성한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 照会値と入力値を組み合わせて推薦人From処理結果Setの処理データを構成する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private EmployeeRecommender makeRecommenderFromResultSet(ResultSet rs) throws SQLException {
		EmployeeRecommender rec = new EmployeeRecommender();

		rec.setEmployeeRecommenderId(rs.getInt("EMPLOYEE_RECOMMENDER_ID"));
		rec.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		rec.setRecommenderName(rs.getString("RECOMMENDER_NAME"));
		rec.setRelation(rs.getString("RELATION"));
		rec.setCompanyName(rs.getString("COMPANY_NAME"));
		rec.setPositionName(rs.getString("POSITION_NAME"));
		rec.setTelNo(rs.getString("TEL_NO"));

		return rec;
	}
}
