package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.attendance.dao.ProjectDao;
import erp.attendance.model.Project;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 현장·프로젝트Modify 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 現場・プロジェクトModifyの業務ルールとデータ変更トランザクションを処理する。
public class ProjectModifyService {
	private ProjectDao projectDao = ProjectDao.getInstance();

	// 입력값을 검증한 후 현장·프로젝트Modify 데이터를 트랜잭션으로 저장하거나 수정한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 入力値を検証した後、現場・プロジェクトModifyデータをトランザクションで登録または更新する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public Integer modify(Project projectReq) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			Project project = projectDao.selectByName(conn, projectReq.getProjectName());
			// 만약 해당 이름의 프로젝트가 존재하면 중복 오류 발생
			// 重複値とデータベース制約違反を確認し、保存可能なデータだけを処理する。
			if (project != null) {
				throw new RuntimeException("同じ名前のプロジェクトがあります : " + project.getProjectName());
			}
			int result = projectDao.update(conn, projectReq);
			conn.commit();
			return result;
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
