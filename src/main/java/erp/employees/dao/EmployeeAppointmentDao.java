package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.employees.model.EmployeeAppointment;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 인사발령 내역 데이터베이스 접근(DAO) 클래스
// 사원발령 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 社員発令データをデータベースから照会し、登録・更新・削除する。
public class EmployeeAppointmentDao {

	// 싱글톤 인스턴스 생성
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static EmployeeAppointmentDao employeeAppointmentDao = new EmployeeAppointmentDao();

	// 싱글톤 접근 메서드
	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static EmployeeAppointmentDao getInstance() {
		return employeeAppointmentDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	// 전달받은 값으로 사원발령 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で社員発令オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private EmployeeAppointmentDao() {
	}

	// 인사발령 내역 등록
	// 入力された内容を新しい業務データとして構成し、関連データとともに登録する。
	// 시퀀스를 사용하여 기본키 발급 및 데이터 저장
	// 전달받은 사원발령 데이터를 데이터베이스에 등록하고 처리 건수를 반환한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 受け取った社員発令データをデータベースへ登録し、処理件数を返す。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void insert(Connection conn, EmployeeAppointment app) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO EMPLOYEE_APPOINTMENT "
					+ "(EMPLOYEE_APPOINTMENT_ID, EMPLOYEE_ID, APP_TYPE, APP_DATE, DEPARTMENT_NAME, JOB_POSITION_NAME, JOB_TITLE_DUTY, NOTE) "
					+ "VALUES (EMPLOYEE_APPOINTMENT_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, app.getEmployeeId());
			pstmt.setString(2, app.getAppType());

			if (app.getAppDate() == null) {
				pstmt.setNull(3, Types.DATE);
			} else {
				pstmt.setTimestamp(3, new Timestamp(app.getAppDate().getTime()));
			}

			pstmt.setString(4, app.getDepartmentName());
			pstmt.setString(5, app.getJobPositionName());
			pstmt.setString(6, app.getJobTitleDuty());
			pstmt.setString(7, app.getNote());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 인사발령 내역 단건 조회
	// 識別番号に該当する一件の詳細データを照会し、編集・詳細表示に使用する。
	// 기본키(EMPLOYEE_APPOINTMENT_ID)를 기준으로 1건의 데이터 조회
	// 조회 조건에 맞는 By식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うBy識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public EmployeeAppointment selectById(Connection conn, int appId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_APPOINTMENT_ID, EMPLOYEE_ID, APP_TYPE, APP_DATE, DEPARTMENT_NAME, JOB_POSITION_NAME, JOB_TITLE_DUTY, NOTE "
					+ "FROM EMPLOYEE_APPOINTMENT WHERE EMPLOYEE_APPOINTMENT_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, appId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeEmpAppointmentFromResultSet(rs);
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 특정 사원의 인사발령 내역 목록 조회
	// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
	// 사원번호(EMPLOYEE_ID)를 기준으로 연관된 발령 내역 전체 반환 (발령일 기준 내림차순 정렬)
	// 조회 조건에 맞는 ByEmp식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うByEmp識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<EmployeeAppointment> selectByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_APPOINTMENT_ID, EMPLOYEE_ID, APP_TYPE, APP_DATE, DEPARTMENT_NAME, JOB_POSITION_NAME, JOB_TITLE_DUTY, NOTE "
					+ "FROM EMPLOYEE_APPOINTMENT WHERE EMPLOYEE_ID = ? ORDER BY APP_DATE DESC, EMPLOYEE_APPOINTMENT_ID DESC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			rs = pstmt.executeQuery();

			List<EmployeeAppointment> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeEmpAppointmentFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 인사발령 내역 수정
	// 識別された既存データへ変更値を反映し、未変更項目は従来の値を維持する。
	// 기본키를 기준으로 부서, 직위 및 기타 발령 상세 데이터 수정
	// 식별조건에 해당하는 사원발령 데이터를 전달받은 값으로 수정한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 識別条件に該当する社員発令データを受け取った値で更新する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int update(Connection conn, EmployeeAppointment app) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE EMPLOYEE_APPOINTMENT SET "
					+ "EMPLOYEE_ID = ?, APP_TYPE = ?, APP_DATE = ?, DEPARTMENT_NAME = ?, JOB_POSITION_NAME = ?, JOB_TITLE_DUTY = ?, NOTE = ? "
					+ "WHERE EMPLOYEE_APPOINTMENT_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, app.getEmployeeId());
			pstmt.setString(2, app.getAppType());

			if (app.getAppDate() == null) {
				pstmt.setNull(3, Types.DATE);
			} else {
				pstmt.setTimestamp(3, new Timestamp(app.getAppDate().getTime()));
			}

			pstmt.setString(4, app.getDepartmentName());
			pstmt.setString(5, app.getJobPositionName());
			pstmt.setString(6, app.getJobTitleDuty());
			pstmt.setString(7, app.getNote());
			pstmt.setInt(8, app.getEmployeeAppointmentId());

			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 인사발령 내역 삭제
	// 選択された対象と関連範囲を確認して削除し、残りの一覧状態を再構成する。
	// 기본키를 기준으로 해당 데이터 삭제
	// 선택되거나 식별된 사원발령 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別された社員発令データを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int delete(Connection conn, int appId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_APPOINTMENT WHERE EMPLOYEE_APPOINTMENT_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, appId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 사원번호(EMPLOYEE_ID)를 기준으로 해당 사원의 인사발령 내역 전체 삭제
	// 선택되거나 식별된 ByEmp식별번호 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別されたByEmp識別番号データを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void deleteByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_APPOINTMENT WHERE EMPLOYEE_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 EmployeeAppointment 객체로 변환
	// 조회값과 입력값을 조합하여 Emp발령From처리결과Set 처리 데이터를 구성한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 照会値と入力値を組み合わせてEmp発令From処理結果Setの処理データを構成する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private EmployeeAppointment makeEmpAppointmentFromResultSet(ResultSet rs) throws SQLException {
		EmployeeAppointment app = new EmployeeAppointment();

		app.setEmployeeAppointmentId(rs.getInt("EMPLOYEE_APPOINTMENT_ID"));
		app.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		app.setAppType(rs.getString("APP_TYPE"));

		Timestamp appTs = rs.getTimestamp("APP_DATE");
		if (appTs != null) {
			app.setAppDate(new java.util.Date(appTs.getTime()));
		}

		app.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
		app.setJobPositionName(rs.getString("JOB_POSITION_NAME"));
		app.setJobTitleDuty(rs.getString("JOB_TITLE_DUTY"));
		app.setNote(rs.getString("NOTE"));

		return app;
	}
}
