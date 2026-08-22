package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.attendance.dao.ProjectDao;
import erp.attendance.model.Project;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 현장·프로젝트Insert 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 現場・プロジェクトInsertの業務ルールとデータ変更トランザクションを処理する。
public class ProjectInsertService {
	private ProjectDao projectDao = ProjectDao.getInstance();
	
	// 현장·프로젝트Insert 처리에 사용할 현장·프로젝트Insert 데이터나 객체를 생성한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 現場・プロジェクトInsert処理で使用する現場・プロジェクトInsertデータまたはオブジェクトを生成する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void insert(String projectName) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			Project project = projectDao.selectByName(conn, projectName);
			//만약 해당 이름의 프로젝트가 존재하면 중복 오류 발생
			// 重複値とデータベース制約違反を確認し、保存可能なデータだけを処理する。
			if (project!=null) {
				throw new RuntimeException("Project name already exists : " + projectName );
			}
			project = new Project();
			project.setProjectName(projectName);
			projectDao.insert(conn, project);
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			JdbcUtil.rollback(conn);
			throw new RuntimeException();
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}
}
