package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.employees.model.EmployeeSuretyInsurance;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 신원보증보험 내역 데이터베이스 접근(DAO) 클래스
// 사원신원보증보험 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 社員身元保証保険データをデータベースから照会し、登録・更新・削除する。
public class EmployeeSuretyInsuranceDao {

	// 싱글톤 인스턴스 생성
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static EmployeeSuretyInsuranceDao employeeSuretyInsuranceDao = new EmployeeSuretyInsuranceDao();

	// 싱글톤 접근 메서드
	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static EmployeeSuretyInsuranceDao getInstance() {
		return employeeSuretyInsuranceDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	// 전달받은 값으로 사원신원보증보험 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で社員身元保証保険オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private EmployeeSuretyInsuranceDao() {
	}

	// 보증보험 내역 등록
	// 전달받은 사원신원보증보험 데이터를 데이터베이스에 등록하고 처리 건수를 반환한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 受け取った社員身元保証保険データをデータベースへ登録し、処理件数を返す。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void insert(Connection conn, EmployeeSuretyInsurance insurance) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO EMPLOYEE_SURETY_INSURANCE "
					+ "(EMPLOYEE_SURETY_INSURANCE_ID, EMPLOYEE_ID, PROVIDER_NAME, INSURANCE_NO, INSURANCE_AMT, SIGNUP_DATE, EXPIRE_DATE, NOTE) "
					+ "VALUES (EMPLOYEE_SURETY_INSURANCE_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, insurance.getEmployeeId());
			pstmt.setString(2, insurance.getProviderName());
			pstmt.setString(3, insurance.getInsuranceNo());
			pstmt.setObject(4, insurance.getInsuranceAmt(), Types.NUMERIC);

			if (insurance.getSignupDate() == null) {
				pstmt.setNull(5, Types.DATE);
			} else {
				pstmt.setTimestamp(5, new Timestamp(insurance.getSignupDate().getTime()));
			}

			if (insurance.getExpireDate() == null) {
				pstmt.setNull(6, Types.DATE);
			} else {
				pstmt.setTimestamp(6, new Timestamp(insurance.getExpireDate().getTime()));
			}

			pstmt.setString(7, insurance.getNote());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 보증보험 내역 단건 조회
	// 조회 조건에 맞는 By식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うBy識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public EmployeeSuretyInsurance selectById(Connection conn, int insuranceId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_SURETY_INSURANCE_ID, EMPLOYEE_ID, PROVIDER_NAME, INSURANCE_NO, INSURANCE_AMT, SIGNUP_DATE, EXPIRE_DATE, NOTE "
					+ "FROM EMPLOYEE_SURETY_INSURANCE WHERE EMPLOYEE_SURETY_INSURANCE_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, insuranceId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeInsuranceFromResultSet(rs);
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 특정 사원의 보증보험 내역 목록 조회
	// 조회 조건에 맞는 ByEmp식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うByEmp識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<EmployeeSuretyInsurance> selectByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_SURETY_INSURANCE_ID, EMPLOYEE_ID, PROVIDER_NAME, INSURANCE_NO, INSURANCE_AMT, SIGNUP_DATE, EXPIRE_DATE, NOTE "
					+ "FROM EMPLOYEE_SURETY_INSURANCE WHERE EMPLOYEE_ID = ? ORDER BY SIGNUP_DATE DESC NULLS LAST, EMPLOYEE_SURETY_INSURANCE_ID DESC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			rs = pstmt.executeQuery();

			List<EmployeeSuretyInsurance> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeInsuranceFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 보증보험 내역 수정
	// 식별조건에 해당하는 사원신원보증보험 데이터를 전달받은 값으로 수정한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 識別条件に該当する社員身元保証保険データを受け取った値で更新する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int update(Connection conn, EmployeeSuretyInsurance insurance) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE EMPLOYEE_SURETY_INSURANCE SET "
					+ "EMPLOYEE_ID = ?, PROVIDER_NAME = ?, INSURANCE_NO = ?, INSURANCE_AMT = ?, SIGNUP_DATE = ?, EXPIRE_DATE = ?, NOTE = ? "
					+ "WHERE EMPLOYEE_SURETY_INSURANCE_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, insurance.getEmployeeId());
			pstmt.setString(2, insurance.getProviderName());
			pstmt.setString(3, insurance.getInsuranceNo());
			pstmt.setObject(4, insurance.getInsuranceAmt(), Types.NUMERIC);

			if (insurance.getSignupDate() == null) {
				pstmt.setNull(5, Types.DATE);
			} else {
				pstmt.setTimestamp(5, new Timestamp(insurance.getSignupDate().getTime()));
			}

			if (insurance.getExpireDate() == null) {
				pstmt.setNull(6, Types.DATE);
			} else {
				pstmt.setTimestamp(6, new Timestamp(insurance.getExpireDate().getTime()));
			}

			pstmt.setString(7, insurance.getNote());
			pstmt.setInt(8, insurance.getEmployeeSuretyInsuranceId());

			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 보증보험 내역 삭제
	// 선택되거나 식별된 사원신원보증보험 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別された社員身元保証保険データを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int delete(Connection conn, int insuranceId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_SURETY_INSURANCE WHERE EMPLOYEE_SURETY_INSURANCE_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, insuranceId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}
	
	// 사원번호(EMPLOYEE_ID)를 기준으로 해당 사원의 보증보험 전체 삭제
	// 선택되거나 식별된 ByEmp식별번호 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別されたByEmp識別番号データを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void deleteByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_SURETY_INSURANCE WHERE EMPLOYEE_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 EmployeeSuretyInsurance 객체로 변환
	// 조회값과 입력값을 조합하여 보험From처리결과Set 처리 데이터를 구성한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 照会値と入力値を組み合わせて保険From処理結果Setの処理データを構成する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private EmployeeSuretyInsurance makeInsuranceFromResultSet(ResultSet rs) throws SQLException {
		EmployeeSuretyInsurance insurance = new EmployeeSuretyInsurance();

		insurance.setEmployeeSuretyInsuranceId(rs.getInt("EMPLOYEE_SURETY_INSURANCE_ID"));
		insurance.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		insurance.setProviderName(rs.getString("PROVIDER_NAME"));
		insurance.setInsuranceNo(rs.getString("INSURANCE_NO"));

		long amt = rs.getLong("INSURANCE_AMT");
		insurance.setInsuranceAmt(rs.wasNull() ? null : amt);

		Timestamp signTs = rs.getTimestamp("SIGNUP_DATE");
		if (signTs != null) {
			insurance.setSignupDate(new java.util.Date(signTs.getTime()));
		}

		Timestamp expireTs = rs.getTimestamp("EXPIRE_DATE");
		if (expireTs != null) {
			insurance.setExpireDate(new java.util.Date(expireTs.getTime()));
		}

		insurance.setNote(rs.getString("NOTE"));

		return insurance;
	}
}
