package erp.attendance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.attendance.dto.EmployeeLeaveRow;
import erp.attendance.dto.LeaveInquiryDto;
import erp.attendance.model.EmployeeLeaveBalance;
import erp.attendance.service.request.LeaveInquiryRequest;
import jdbc.JdbcUtil;
import oracle.net.aso.p;

/*import erp.attend.model.EmpLeave;*/
/*import erp.attend.model.EmpLeaveStatusItem;*/
/*import erp.attend.model.LeaveStatusCondition;*/

// 사원휴가Balance 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 社員休暇Balanceデータをデータベースから照会し、登録・更新・削除する。
public class EmployeeLeaveBalanceDao {

	private static EmployeeLeaveBalanceDao employeeLeaveBalanceDao = new EmployeeLeaveBalanceDao();

	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static EmployeeLeaveBalanceDao getInstance() {
		return employeeLeaveBalanceDao;
	}

	// 전달받은 값으로 사원휴가Balance 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で社員休暇Balanceオブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private EmployeeLeaveBalanceDao() {

	}

	// [삽입] 새로운 사원별 휴가 부여 내역 추가 처리
	// 전달받은 사원휴가Balance 데이터를 데이터베이스에 등록하고 처리 건수를 반환한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 受け取った社員休暇Balanceデータをデータベースへ登録し、処理件数を返す。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void insert(Connection conn, EmployeeLeaveBalance balance) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			// v5 스키마 규격에 맞춘 INSERT 쿼리 작성 (시퀀스 적용)
			// 業務条件に合うSQLを準備し、入力値をバインドしてデータベースで実行する。
			String sql = "INSERT INTO EMPLOYEE_LEAVE_BALANCE "
					+ "(EMPLOYEE_LEAVE_BALANCE_ID, EMPLOYEE_ID, LEAVE_ITEM_ID, TOTAL_DAYS) "
					+ "VALUES (EMPLOYEE_LEAVE_BALANCE_SEQ.NEXTVAL, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, balance.getEmployeeId());
			pstmt.setInt(2, balance.getLeaveItemId());
			pstmt.setDouble(3, balance.getTotalDays());

			pstmt.executeUpdate(); // 쿼리 실행 수행

		} finally {
			// 자원 누수 방지를 위한 객체 반환 처리
			// 使用済みのJDBCリソースを安全に閉じ、接続漏れやリソース漏れを防止する。
			JdbcUtil.close(pstmt);
		}
	}

	/*
	 * // 사원 1명의 휴가정보를 조회하는 메서드 public EmployeeLeaveBalance
	 * 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。 selectByEmpIdAndLeaveItemId(Connection
	 * conn, int employeeId, int leaveItemId) throws SQLException { String sql =
	 * "SELECT * FROM EMPLOYEE_LEAVE_BALANCE WHERE EMPLOYEE_ID = ? AND LEAVE_ITEM_ID = ?"
	 * ; try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	 * pstmt.setInt(1, employeeId); pstmt.setInt(2, leaveItemId); try (ResultSet rs
	 * = pstmt.executeQuery()) { if (rs.next()) { return convertEmpLeave(rs); } } }
	 * return null; }
	 */

	public int selectCountLeaveBalance(Connection conn, LeaveInquiryRequest req) throws SQLException {
		String sql = "SELECT COUNT(*) FROM EMPLOYEE E "
				+ "LEFT JOIN EMPLOYEE_LEAVE_BALANCE ELB ON ELB.EMPLOYEE_ID = E.EMPLOYEE_ID AND ELB.LEAVE_ITEM_ID = ? "
				+ "LEFT JOIN DEPARTMENT D ON D.DEPARTMENT_ID = E.DEPARTMENT_ID "
				+ "LEFT JOIN JOB_POSITION J ON J.JOB_POSITION_ID = E.JOB_POSITION_ID "
				+ "LEFT JOIN LEAVE_ITEM LI ON LI.LEAVE_ITEM_ID = ? "
				+ "WHERE (? IS NULL OR E.EMP_TYPE LIKE '%' || ? || '%' " + "OR E.EMP_NO LIKE '%' || ? || '%'"
				+ "OR E.EMP_NAME_KR LIKE '%' || ? || '%' " + "OR D.DEPARTMENT_NAME LIKE '%' || ? || '%' "
				+ "OR J.JOB_POSITION_NAME LIKE '%' || ? || '%' " + "OR LI.ITEM_NAME LIKE '%' || ? || '%') "
				+ "AND (? IS NULL OR E.STATUS = ?) " + "AND (? IS NULL OR E.EMP_TYPE = ?) "
				+ "AND (? IS NULL OR D.DEPARTMENT_ID = ?) " + "AND (? IS NULL OR J.JOB_POSITION_ID = ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, req.getLeaveItemId());
			pstmt.setInt(2, req.getLeaveItemId());
			pstmt.setString(3, req.getKeyword());
			pstmt.setString(4, req.getKeyword());
			pstmt.setString(5, req.getKeyword());
			pstmt.setString(6, req.getKeyword());
			pstmt.setString(7, req.getKeyword());
			pstmt.setString(8, req.getKeyword());
			pstmt.setString(9, req.getKeyword());
			pstmt.setString(10, req.getStatus());
			pstmt.setString(11, req.getStatus());
			pstmt.setString(12, req.getEmpType());
			pstmt.setString(13, req.getEmpType());
			setIntOrNull(pstmt, 14, 15, req.getDepartmentId());
			setIntOrNull(pstmt, 16, 17, req.getJobPositionId());
			int result = 0;
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					result = rs.getInt("COUNT(*)");
				}
			}
			return result;
		}
	}

	
	//휴가조회(페이징 처리)
	//休暇照会（ページング処理）
	public List<LeaveInquiryDto> selectLeaveBalance(Connection conn, LeaveInquiryRequest req, int firstRow, int endRow)
			throws SQLException {
		String sql = "select * from (select rownum as rnum, a.* from (SELECT E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NO, E.EMP_NAME_KR, D.DEPARTMENT_NAME, J.JOB_POSITION_NAME, "
				+ "LI.ITEM_NAME, NVL(ELB.TOTAL_DAYS, 0) AS TOTAL_DAYS " + "FROM EMPLOYEE E "
				+ "LEFT JOIN EMPLOYEE_LEAVE_BALANCE ELB ON ELB.EMPLOYEE_ID = E.EMPLOYEE_ID AND ELB.LEAVE_ITEM_ID = ? "
				+ "LEFT JOIN DEPARTMENT D ON D.DEPARTMENT_ID = E.DEPARTMENT_ID "
				+ "LEFT JOIN JOB_POSITION J ON J.JOB_POSITION_ID = E.JOB_POSITION_ID "
				+ "LEFT JOIN LEAVE_ITEM LI ON LI.LEAVE_ITEM_ID = ? " + "WHERE (? IS NULL OR "
				+ "      E.EMP_TYPE LIKE '%' || ? || '%' OR " + "      E.EMP_NO LIKE '%' || ? || '%' OR "
				+ "      E.EMP_NAME_KR LIKE '%' || ? || '%' OR " + "      D.DEPARTMENT_NAME LIKE '%' || ? || '%' OR "
				+ "      J.JOB_POSITION_NAME LIKE '%' || ? || '%' OR " + "      LI.ITEM_NAME LIKE '%' || ? || '%') "
				+ "AND (? IS NULL OR E.STATUS = ?) " + "AND (? IS NULL OR E.EMP_TYPE = ?) "
				+ "AND (? IS NULL OR D.DEPARTMENT_ID = ?) " + "AND (? IS NULL OR J.JOB_POSITION_ID = ?)) a where rownum <=? ) where rnum > ?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, req.getLeaveItemId());
			pstmt.setInt(2, req.getLeaveItemId());
			pstmt.setString(3, req.getKeyword());
			pstmt.setString(4, req.getKeyword());
			pstmt.setString(5, req.getKeyword());
			pstmt.setString(6, req.getKeyword());
			pstmt.setString(7, req.getKeyword());
			pstmt.setString(8, req.getKeyword());
			pstmt.setString(9, req.getKeyword());
			pstmt.setString(10, req.getStatus());
			pstmt.setString(11, req.getStatus());
			pstmt.setString(12, req.getEmpType());
			pstmt.setString(13, req.getEmpType());
			setIntOrNull(pstmt, 14, 15, req.getDepartmentId());
			setIntOrNull(pstmt, 16, 17, req.getJobPositionId());
			pstmt.setInt(18, endRow);
			pstmt.setInt(19, firstRow);
			List<LeaveInquiryDto> list = new ArrayList<>();
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					list.add(convertLeaveInquiryDto(conn, rs, req.getLeaveItemId()));
				}
			}
			return list;
		}
	}

	// 휴가조회(전체목록) 화면용 - 필터에 맞는 여러 사원의, 특정 휴가항목 전체/사용/잔여 조회
	// 조회 조건에 맞는 휴가Balance 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로
	// 변환한다.
	// 検索条件に合う休暇Balanceデータをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<LeaveInquiryDto> selectLeaveBalance(Connection conn, int employeeId, int leaveItemId) throws SQLException {
		String sql = "SELECT E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NO, E.EMP_NAME_KR, D.DEPARTMENT_NAME, J.JOB_POSITION_NAME, "
				+ "LI.ITEM_NAME, NVL(ELB.TOTAL_DAYS, 0) AS TOTAL_DAYS "
				+ "FROM EMPLOYEE E "
				+ "LEFT JOIN EMPLOYEE_LEAVE_BALANCE ELB ON ELB.EMPLOYEE_ID = E.EMPLOYEE_ID AND ELB.LEAVE_ITEM_ID = ? "
				+ "LEFT JOIN DEPARTMENT D ON D.DEPARTMENT_ID = E.DEPARTMENT_ID "
				+ "LEFT JOIN JOB_POSITION J ON J.JOB_POSITION_ID = E.JOB_POSITION_ID "
				+ "LEFT JOIN LEAVE_ITEM LI ON LI.LEAVE_ITEM_ID = ? "
				+ "WHERE E.EMPLOYEE_ID = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, leaveItemId);
			pstmt.setInt(2, leaveItemId);
			pstmt.setInt(3, employeeId);

			List<LeaveInquiryDto> list = new ArrayList<>();
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					list.add(convertLeaveInquiryDto(conn, rs, leaveItemId));
				}
			}
			return list;
		}
	}

	// 입력 데이터를 휴가조회전달 데이터 처리에 필요한 형식으로 변환한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 入力データを休暇照会転送データ処理に必要な形式へ変換する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private LeaveInquiryDto convertLeaveInquiryDto(Connection conn, ResultSet rs, int leaveItemId) throws SQLException {
		LeaveInquiryDto dto = new LeaveInquiryDto();
		int employeeId = rs.getInt("EMPLOYEE_ID");
		dto.setEmployeeId(employeeId);
		dto.setEmpType(rs.getString("EMP_TYPE"));
		dto.setEmpNo(rs.getString("EMP_NO"));
		dto.setEmpNameKr(rs.getString("EMP_NAME_KR"));
		dto.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
		dto.setJobPositionName(rs.getString("JOB_POSITION_NAME"));
		dto.setItemName(rs.getString("ITEM_NAME"));
		long totalDays = rs.getLong("TOTAL_DAYS");
		dto.setTotalDays(totalDays);

		double usedDays = 0;
		String sql = "SELECT SUM(ATTEND_VALUE) AS USED_DAYS FROM EMPLOYEE_ATTENDANCE WHERE EMPLOYEE_ID = ? AND LEAVE_ITEM_ID = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, employeeId);
			pstmt.setInt(2, leaveItemId);
			try (ResultSet rsSub = pstmt.executeQuery()) {
				if (rsSub.next()) {
					usedDays = rsSub.getDouble("USED_DAYS");
				}
			}
		}
		dto.setUsedDays(usedDays);
		dto.setRemainingDays(totalDays - usedDays);
		return dto;

	}

	// [조회] 특정 휴가항목에 대한 전체 사원의 휴가 부여 현황 조회
	// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
	// EMPLOYEE 테이블을 기준으로 LEFT JOIN을 수행하여 휴가 데이터가 없는 사원도 조회함
	// 조회 조건에 맞는 사원휴가행 목록 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로
	// 변환한다.
	// 検索条件に合う社員休暇行一覧データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<EmployeeLeaveRow> selectEmployeeLeaveRows(Connection conn, int leaveItemId, String keyword,
			String status) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<EmployeeLeaveRow> list = new ArrayList<>();

		try {
			// 동적 쿼리 구성을 위한 StringBuilder 사용 처리
			// 業務条件に合うSQLを準備し、入力値をバインドしてデータベースで実行する。
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT elb.EMPLOYEE_LEAVE_BALANCE_ID, e.EMPLOYEE_ID, e.EMP_TYPE, e.EMP_NO, e.EMP_NAME_KR, ");
			sql.append("d.DEPARTMENT_NAME, p.JOB_POSITION_NAME, e.JOIN_DATE, NVL(elb.TOTAL_DAYS, 0) AS TOTAL_DAYS ");
			sql.append("FROM EMPLOYEE e ");
			// 해당 휴가항목(LEAVE_ITEM_ID)에 대해서만 아우터 조인 수행
			// 関連テーブルを識別キーで結合し、画面表示に必要な名称と詳細情報をまとめて取得する。
			sql.append(
					"LEFT JOIN EMPLOYEE_LEAVE_BALANCE elb ON e.EMPLOYEE_ID = elb.EMPLOYEE_ID AND elb.LEAVE_ITEM_ID = ? ");
			sql.append("LEFT JOIN DEPARTMENT d ON e.DEPARTMENT_ID = d.DEPARTMENT_ID ");
			sql.append("LEFT JOIN JOB_POSITION p ON e.JOB_POSITION_ID = p.JOB_POSITION_ID ");
			sql.append("WHERE 1=1 ");

			// 재직/퇴직 상태 검색 조건 추가
			// 検索語と選択条件を組み合わせ、利用者が指定した対象だけを照会する。
			if (status != null && !status.isEmpty()) {
				sql.append("AND e.STATUS = ? ");
			}

			// 사원번호 또는 이름 검색 조건 추가
			// 検索語と選択条件を組み合わせ、利用者が指定した対象だけを照会する。
			if (keyword != null && !keyword.isEmpty()) {
				sql.append("AND (e.EMP_NAME_KR LIKE ? OR e.EMP_NO LIKE ?) ");
			}

			sql.append("ORDER BY e.EMP_NO ASC");

			pstmt = conn.prepareStatement(sql.toString());

			// 파라미터 인덱스 동적 할당 처리
			// リクエストから入力値を取得し、空白除去と形式変換を行って業務処理へ渡す。
			int index = 1;
			pstmt.setInt(index++, leaveItemId);

			if (status != null && !status.isEmpty()) {
				pstmt.setString(index++, status);
			}

			if (keyword != null && !keyword.isEmpty()) {
				pstmt.setString(index++, "%" + keyword + "%");
				pstmt.setString(index++, "%" + keyword + "%");
			}

			rs = pstmt.executeQuery();

			// 조회된 결과 매핑 처리
			// 対象データを順番に繰り返し処理し、各要素へ同じ業務基準を適用する。
			while (rs.next()) {
				EmployeeLeaveRow row = new EmployeeLeaveRow();
				row.setEmpLeaveId(rs.getInt("EMPLOYEE_LEAVE_BALANCE_ID")); // 값이 없으면 0 반환됨
				row.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
				row.setEmpType(rs.getString("EMP_TYPE"));
				row.setEmpNo(rs.getString("EMP_NO"));
				row.setEmpName(rs.getString("EMP_NAME_KR"));
				row.setDeptName(rs.getString("DEPARTMENT_NAME"));
				row.setPosName(rs.getString("JOB_POSITION_NAME"));
				row.setJoinDate(rs.getDate("JOIN_DATE"));
				row.setLeaveDays(rs.getDouble("TOTAL_DAYS"));

				list.add(row);
			}
			return list;

		} finally {
			// 자원 안전 반환 처리
			// 使用済みのJDBCリソースを安全に閉じ、接続漏れやリソース漏れを防止する。
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// [수정] 기존 사원의 부여된 휴가일수 갱신 처리
	// 식별조건에 해당하는 사원휴가Balance 데이터를 전달받은 값으로 수정한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 識別条件に該当する社員休暇Balanceデータを受け取った値で更新する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void update(Connection conn, EmployeeLeaveBalance balance) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			// 휴가일수 갱신 쿼리 작성
			// 業務条件に合うSQLを準備し、入力値をバインドしてデータベースで実行する。
			String sql = "UPDATE EMPLOYEE_LEAVE_BALANCE " + "SET TOTAL_DAYS = ? "
					+ "WHERE EMPLOYEE_LEAVE_BALANCE_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setDouble(1, balance.getTotalDays());
			pstmt.setInt(2, balance.getEmployeeLeaveBalanceId()); // 식별 가능한 기본키 매핑

			pstmt.executeUpdate(); // 쿼리 실행 수행

		} finally {
			// 자원 누수 방지를 위한 객체 반환 처리
			// 使用済みのJDBCリソースを安全に閉じ、接続漏れやリソース漏れを防止する。
			JdbcUtil.close(pstmt);
		}
	}

	// [삭제] 사원에게 부여된 특정 휴가 내역 완전 삭제 처리
	// 선택되거나 식별된 사원휴가Balance 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別された社員休暇Balanceデータを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void delete(Connection conn, int empLeaveId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			// 기본키 기반 레코드 삭제 쿼리 작성
			// 業務条件に合うSQLを準備し、入力値をバインドしてデータベースで実行する。
			String sql = "DELETE FROM EMPLOYEE_LEAVE_BALANCE WHERE EMPLOYEE_LEAVE_BALANCE_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empLeaveId);

			pstmt.executeUpdate(); // 쿼리 실행 수행

		} finally {
			// 자원 누수 방지를 위한 객체 반환 처리
			// 使用済みのJDBCリソースを安全に閉じ、接続漏れやリソース漏れを防止する。
			JdbcUtil.close(pstmt);
		}
	}

	// 전달받은 정수 또는 빈 값 값을 사원휴가Balance 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った整数または空値の値を社員休暇Balanceオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	private void setIntOrNull(PreparedStatement pstmt, int idx1, int idx2, Integer value) throws SQLException {
		if (value != null) {
			pstmt.setInt(idx1, value);
			pstmt.setInt(idx2, value);
		} else {
			pstmt.setNull(idx1, java.sql.Types.NUMERIC);
			pstmt.setNull(idx2, java.sql.Types.NUMERIC);
		}
	}

}
