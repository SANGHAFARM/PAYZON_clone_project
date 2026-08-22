package erp.attendance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.attendance.model.Project;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 현장/프로젝트 목록 데이터베이스 접근(DAO) 클래스
// 현장·프로젝트 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 現場・プロジェクトデータをデータベースから照会し、登録・更新・削除する。
public class ProjectDao {

	// 싱글톤 인스턴스 생성
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static ProjectDao projectDao = new ProjectDao();

	// 싱글톤 접근 메서드
	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static ProjectDao getInstance() {
		return projectDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	// 전달받은 값으로 현장·프로젝트 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で現場・プロジェクトオブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private ProjectDao() {
	}

	// 프로젝트 정보 등록
	// 전달받은 현장·프로젝트 데이터를 데이터베이스에 등록하고 처리 건수를 반환한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 受け取った現場・プロジェクトデータをデータベースへ登録し、処理件数を返す。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void insert(Connection conn, Project project) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO PROJECT (PROJECT_ID, PROJECT_NAME) VALUES (PROJECT_SEQ.NEXTVAL, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, project.getProjectName());
			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 프로젝트 정보 단건 조회
	// 조회 조건에 맞는 By식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うBy識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public Project selectById(Connection conn, int projectId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM PROJECT WHERE PROJECT_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, projectId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				Project project = new Project();
				project.setProjectId(rs.getInt("PROJECT_ID"));
				project.setProjectName(rs.getString("PROJECT_NAME"));
				return project;
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}
	
	//프로젝트 이름으로 프로젝트를 조회하는 메서드
	// 조회 조건에 맞는 By명칭 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うBy名称データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public Project selectByName(Connection conn, String projectName) throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM PROJECT WHERE PROJECT_NAME = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, projectName);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				Project project = new Project();
				project.setProjectId(rs.getInt("PROJECT_ID"));
				project.setProjectName(rs.getString("PROJECT_NAME"));
				return project;
			}
			return null;
		}finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 프로젝트 목록 전체 조회
	// 조회 조건에 맞는 전체 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う全体データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<Project> selectAll(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM PROJECT ORDER BY PROJECT_ID ASC";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			List<Project> result = new ArrayList<>();
			while (rs.next()) {
				Project project = new Project();
				project.setProjectId(rs.getInt("PROJECT_ID"));
				project.setProjectName(rs.getString("PROJECT_NAME"));
				result.add(project);
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 프로젝트 정보 수정
	// 식별조건에 해당하는 현장·프로젝트 데이터를 전달받은 값으로 수정한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 識別条件に該当する現場・プロジェクトデータを受け取った値で更新する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int update(Connection conn, Project project) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE PROJECT SET PROJECT_NAME = ? WHERE PROJECT_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, project.getProjectName());
			pstmt.setInt(2, project.getProjectId());
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 프로젝트 정보 삭제
	// 선택되거나 식별된 현장·프로젝트 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別された現場・プロジェクトデータを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int delete(Connection conn, int projectId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM PROJECT WHERE PROJECT_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, projectId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}
}
