package erp.retirement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.retirement.model.RetirementTaxDeferral;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 퇴직연금계좌의 과세이연 내역을 저장하고 조회한다.
// 퇴직급여세금과세이연 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 退職給与税金課税繰延データをデータベースから照会し、登録・更新・削除する。
public class RetirementTaxDeferralDao {

	// 싱글톤 인스턴스 생성
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static RetirementTaxDeferralDao retirementTaxDeferralDao = new RetirementTaxDeferralDao();

	// 싱글톤 접근 메서드
	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static RetirementTaxDeferralDao getInstance() {
		return retirementTaxDeferralDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	// 전달받은 값으로 퇴직급여세금과세이연 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で退職給与税金課税繰延オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private RetirementTaxDeferralDao() {
	}

	// 과세이연 내역 등록
	// 保険・税区分と適用基準を確認し、対象金額に合う控除または非課税処理を適用する。
	// 시퀀스를 사용하여 기본키 발급 및 자료 저장
	// 전달받은 퇴직급여세금과세이연 데이터를 데이터베이스에 등록하고 처리 건수를 반환한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 受け取った退職給与税金課税繰延データをデータベースへ登録し、処理件数を返す。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void insert(Connection conn, RetirementTaxDeferral deferral) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO RETIREMENT_TAX_DEFERRAL "
					+ "(RETIREMENT_TAX_DEFERRAL_ID, RETIREMENT_CALCULATION_ID, BIZ_NAME, BIZ_REG_NO, ACCOUNT_NO, DEPOSIT_DATE, DEPOSIT_AMT) "
					+ "VALUES (RETIREMENT_TAX_DEFERRAL_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, deferral.getRetirementCalculationId());
			pstmt.setString(2, deferral.getBizName());
			pstmt.setString(3, deferral.getBizRegNo());
			pstmt.setString(4, deferral.getAccountNo());

			// 날짜 null 방어 로직 적용
			// 未入力値とNULLを区別して正規化し、制約違反や不要な値の保存を防止する。
			if (deferral.getDepositDate() == null) {
				pstmt.setNull(5, Types.DATE);
			} else {
				pstmt.setTimestamp(5, new Timestamp(deferral.getDepositDate().getTime()));
			}

			pstmt.setLong(6, deferral.getDepositAmt());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 특정 퇴직정산에 포함된 과세이연 내역을 조회한다.
	// 조회 조건에 맞는 By계산식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うBy計算識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<RetirementTaxDeferral> selectByCalcId(Connection conn, int calcId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM RETIREMENT_TAX_DEFERRAL WHERE RETIREMENT_CALCULATION_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, calcId);
			rs = pstmt.executeQuery();

			List<RetirementTaxDeferral> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeDeferralFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 기본키로 과세이연 내역을 삭제한다.
	// 선택되거나 식별된 퇴직급여세금과세이연 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別された退職給与税金課税繰延データを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int delete(Connection conn, int deferralId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM RETIREMENT_TAX_DEFERRAL WHERE RETIREMENT_TAX_DEFERRAL_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, deferralId);
			return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 조회 결과를 과세이연 객체로 변환한다.
	// 조회값과 입력값을 조합하여 과세이연From처리결과Set 처리 데이터를 구성한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 照会値と入力値を組み合わせて課税繰延From処理結果Setの処理データを構成する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private RetirementTaxDeferral makeDeferralFromResultSet(ResultSet rs) throws SQLException {
		RetirementTaxDeferral deferral = new RetirementTaxDeferral();

		deferral.setRetirementTaxDeferralId(rs.getInt("RETIREMENT_TAX_DEFERRAL_ID"));
		deferral.setRetirementCalculationId(rs.getInt("RETIREMENT_CALCULATION_ID"));
		deferral.setBizName(rs.getString("BIZ_NAME"));
		deferral.setBizRegNo(rs.getString("BIZ_REG_NO"));
		deferral.setAccountNo(rs.getString("ACCOUNT_NO"));

		Timestamp depTs = rs.getTimestamp("DEPOSIT_DATE");
		if (depTs != null) {
			deferral.setDepositDate(new java.util.Date(depTs.getTime()));
		}

		deferral.setDepositAmt(rs.getLong("DEPOSIT_AMT"));

		return deferral;
	}
}
