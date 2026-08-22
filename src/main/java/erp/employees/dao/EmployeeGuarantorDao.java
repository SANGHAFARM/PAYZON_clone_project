package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.employees.model.EmployeeGuarantor;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 신원보증인 내역 데이터베이스 접근(DAO) 클래스
// 사원보증인 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 社員保証人データをデータベースから照会し、登録・更新・削除する。
public class EmployeeGuarantorDao {

	// 싱글톤 인스턴스 생성
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static EmployeeGuarantorDao employeeGuarantorDao = new EmployeeGuarantorDao();

	// 싱글톤 접근 메서드
	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static EmployeeGuarantorDao getInstance() {
		return employeeGuarantorDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	// 전달받은 값으로 사원보증인 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で社員保証人オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private EmployeeGuarantorDao() {
	}

	// 보증인 내역 등록
	// 전달받은 사원보증인 데이터를 데이터베이스에 등록하고 처리 건수를 반환한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 受け取った社員保証人データをデータベースへ登録し、処理件数を返す。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void insert(Connection conn, EmployeeGuarantor guarantor) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO EMPLOYEE_GUARANTOR "
					+ "(EMPLOYEE_GUARANTOR_ID, EMPLOYEE_ID, GUARANTOR_NAME, RELATION, JUMIN_NO, GUARANTEE_AMT, GUARANTEE_DATE, EXPIRE_DATE, TEL_NO) "
					+ "VALUES (EMPLOYEE_GUARANTOR_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, guarantor.getEmployeeId());
			pstmt.setString(2, guarantor.getGuarantorName());
			pstmt.setString(3, guarantor.getRelation());
			pstmt.setString(4, guarantor.getJuminNo());
			pstmt.setObject(5, guarantor.getGuaranteeAmt(), Types.NUMERIC);

			if (guarantor.getGuaranteeDate() == null) {
				pstmt.setNull(6, Types.DATE);
			} else {
				pstmt.setTimestamp(6, new Timestamp(guarantor.getGuaranteeDate().getTime()));
			}

			if (guarantor.getExpireDate() == null) {
				pstmt.setNull(7, Types.DATE);
			} else {
				pstmt.setTimestamp(7, new Timestamp(guarantor.getExpireDate().getTime()));
			}

			pstmt.setString(8, guarantor.getTelNo());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 보증인 내역 단건 조회
	// 조회 조건에 맞는 By식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うBy識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public EmployeeGuarantor selectById(Connection conn, int guarantorId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_GUARANTOR_ID, EMPLOYEE_ID, GUARANTOR_NAME, RELATION, JUMIN_NO, GUARANTEE_AMT, GUARANTEE_DATE, EXPIRE_DATE, TEL_NO "
					+ "FROM EMPLOYEE_GUARANTOR WHERE EMPLOYEE_GUARANTOR_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, guarantorId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeGuarantorFromResultSet(rs);
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 특정 사원의 보증인 내역 목록 조회
	// 조회 조건에 맞는 ByEmp식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うByEmp識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<EmployeeGuarantor> selectByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_GUARANTOR_ID, EMPLOYEE_ID, GUARANTOR_NAME, RELATION, JUMIN_NO, GUARANTEE_AMT, GUARANTEE_DATE, EXPIRE_DATE, TEL_NO "
					+ "FROM EMPLOYEE_GUARANTOR WHERE EMPLOYEE_ID = ? ORDER BY GUARANTEE_DATE DESC NULLS LAST, EMPLOYEE_GUARANTOR_ID DESC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			rs = pstmt.executeQuery();

			List<EmployeeGuarantor> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeGuarantorFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 보증인 내역 수정
	// 식별조건에 해당하는 사원보증인 데이터를 전달받은 값으로 수정한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 識別条件に該当する社員保証人データを受け取った値で更新する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int update(Connection conn, EmployeeGuarantor guarantor) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE EMPLOYEE_GUARANTOR SET "
					+ "EMPLOYEE_ID = ?, GUARANTOR_NAME = ?, RELATION = ?, JUMIN_NO = ?, GUARANTEE_AMT = ?, GUARANTEE_DATE = ?, EXPIRE_DATE = ?, TEL_NO = ? "
					+ "WHERE EMPLOYEE_GUARANTOR_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, guarantor.getEmployeeId());
			pstmt.setString(2, guarantor.getGuarantorName());
			pstmt.setString(3, guarantor.getRelation());
			pstmt.setString(4, guarantor.getJuminNo());
			pstmt.setObject(5, guarantor.getGuaranteeAmt(), Types.NUMERIC);

			if (guarantor.getGuaranteeDate() == null) {
				pstmt.setNull(6, Types.DATE);
			} else {
				pstmt.setTimestamp(6, new Timestamp(guarantor.getGuaranteeDate().getTime()));
			}

			if (guarantor.getExpireDate() == null) {
				pstmt.setNull(7, Types.DATE);
			} else {
				pstmt.setTimestamp(7, new Timestamp(guarantor.getExpireDate().getTime()));
			}

			pstmt.setString(8, guarantor.getTelNo());
			pstmt.setInt(9, guarantor.getEmployeeGuarantorId());

			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 보증인 내역 삭제
	// 선택되거나 식별된 사원보증인 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別された社員保証人データを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int delete(Connection conn, int guarantorId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_GUARANTOR WHERE EMPLOYEE_GUARANTOR_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, guarantorId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 사원번호(EMPLOYEE_ID)를 기준으로 해당 사원의 신원보증인 전체 삭제
	// 선택되거나 식별된 ByEmp식별번호 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別されたByEmp識別番号データを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void deleteByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_GUARANTOR WHERE EMPLOYEE_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 EmployeeGuarantor 객체로 변환
	// 조회값과 입력값을 조합하여 보증인From처리결과Set 처리 데이터를 구성한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 照会値と入力値を組み合わせて保証人From処理結果Setの処理データを構成する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private EmployeeGuarantor makeGuarantorFromResultSet(ResultSet rs) throws SQLException {
		EmployeeGuarantor guarantor = new EmployeeGuarantor();

		guarantor.setEmployeeGuarantorId(rs.getInt("EMPLOYEE_GUARANTOR_ID"));
		guarantor.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		guarantor.setGuarantorName(rs.getString("GUARANTOR_NAME"));
		guarantor.setRelation(rs.getString("RELATION"));
		guarantor.setJuminNo(rs.getString("JUMIN_NO"));

		long amt = rs.getLong("GUARANTEE_AMT");
		guarantor.setGuaranteeAmt(rs.wasNull() ? null : amt);

		Timestamp guaTs = rs.getTimestamp("GUARANTEE_DATE");
		if (guaTs != null) {
			guarantor.setGuaranteeDate(new java.util.Date(guaTs.getTime()));
		}

		Timestamp expireTs = rs.getTimestamp("EXPIRE_DATE");
		if (expireTs != null) {
			guarantor.setExpireDate(new java.util.Date(expireTs.getTime()));
		}

		guarantor.setTelNo(rs.getString("TEL_NO"));

		return guarantor;
	}
}
