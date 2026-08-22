package erp.settings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import erp.settings.model.Company;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 회사 기본 및 담당자 정보 설정 데이터베이스 접근(DAO) 클래스
// 사업장 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 事業所データをデータベースから照会し、登録・更新・削除する。
public class CompanyDao {

	// 싱글톤 인스턴스 생성
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static CompanyDao companyDao = new CompanyDao();

	// 싱글톤 접근 메서드
	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static CompanyDao getInstance() {
		return companyDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	// 전달받은 값으로 사업장 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で事業所オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private CompanyDao() {
	}

	// 회사 정보 등록
	// 전달받은 사업장 데이터를 데이터베이스에 등록하고 처리 건수를 반환한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 受け取った事業所データをデータベースへ登録し、処理件数を返す。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void insert(Connection conn, Company company) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO COMPANY ("
					+ "COMPANY_ID, CMPN_NAME, CEO_TITLE, CEO_NAME, BIZ_REG_NO, CORP_REG_NO, FOUNDATION_DATE, HOMEPAGE_URL, "
					+ "ZIP_CODE, ADDRESS, TEL_NO, FAX_NO, BIZ_TYPE, BIZ_ITEM, MANAGER_NAME, MANAGER_DEPT_NAME, MANAGER_POS_NAME, "
					+ "MANAGER_TEL_NO, MANAGER_MOBILE_NO, MANAGER_EMAIL, PAY_CALC_START_SCOPE, PAY_CALC_START_DAY, PAY_CALC_END_SCOPE, "
					+ "PAY_CALC_END_DAY, PAY_DATE_SCOPE, PAY_DATE_DAY, PAY_BANK_NAME, PAY_ACCOUNT_NO, PAY_ACCOUNT_HOLDER, LOGO_IMG_PATH, STAMP_IMG_PATH) "
					+ "VALUES (COMPANY_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			// [회사 기본정보][会社基本情報]
			pstmt.setString(1, company.getCmpnName());// 상호[商号]
			pstmt.setString(2, company.getCeoTitle());// 대표자직급[代表者職級]
			pstmt.setString(3, company.getCeoName());// 대표자성명[代表者名]
			pstmt.setString(4, company.getBizRegNo());//사업자번호[事業者番号]
			pstmt.setString(5, company.getCorpRegNo());// 법인등록번호[法人登録番号]

			// 설립일[設立日]
			// 現在値と業務条件を比較し、条件を満たす場合にだけ後続処理を実行する。
			if (company.getFoundationDate() == null) {
				pstmt.setNull(6, Types.DATE);
			} else {
				pstmt.setTimestamp(6, new Timestamp(company.getFoundationDate().getTime()));
			}

			pstmt.setString(7, company.getHomepageUrl());// 홈페이지[ホームページ]
			pstmt.setString(8, company.getZipCode());// [사업장 주소 및 연락처]
			pstmt.setString(9, company.getAddress());//우편번호[郵便番号]
			pstmt.setString(10, company.getTelNo());// 전화번호[電話番号]
			pstmt.setString(11, company.getFaxNo());// 팩스번호[ファックス番号]
			
			// [사업자등록 정보]
			pstmt.setString(12, company.getBizType());// 업태[業態]
			pstmt.setString(13, company.getBizItem());// 종목[種目]
			
			// [급여·인사 담당자][給与・人事担当者]
			pstmt.setString(14, company.getManagerName());// 담당자성명[担当者名]
			pstmt.setString(15, company.getManagerDeptName());// 담당자부서[担当者部署]
			pstmt.setString(16, company.getManagerPosName());// 담당자직위[担当者職位]
			pstmt.setString(17, company.getManagerTelNo());// 담당자 전화번호[担当者電話番号]
			pstmt.setString(18, company.getManagerMobileNo()); // 담당자 휴대폰번호[担当者携帯番号]
			pstmt.setString(19, company.getManagerEmail());// 담당자 이메일[担当者E-MAIL]
			
			// [급여계산 기간 및 지급일 설정][給与計算期間及び支給日設定]
			pstmt.setString(20, company.getPayCalcStartScope());// 급여 산정기간 시작월[給与算定期間開始月]
			pstmt.setString(21, company.getPayCalcStartDay());// 급여 산정기간 시작일[給与算定期間開始日]
			pstmt.setString(22, company.getPayCalcEndScope());// 급여 산정기간 종료월[給与算定期間終了月]
			pstmt.setString(23, company.getPayCalcEndDay());// 급여 산정기간 종료일[給与算定期間終了日]
			pstmt.setString(24, company.getPayDateScope());// 급여 지급월[給与支給月]
			pstmt.setString(25, company.getPayDateDay());// 급여 지급일[給与支給日]
			
			// [회사 급여지급 계좌][会社給与支給口座]
			pstmt.setString(26, company.getPayBankName());//금융기관[金融機関]
			pstmt.setString(27, company.getPayAccountNo());// 계좌번호[口座番号]
			pstmt.setString(28, company.getPayAccountHolder());// 예금주[預金者]
			
			// [증명서·명세서 이미지][証明書・明細書イメージ] 
			pstmt.setString(29, company.getLogoImgPath());//로고[ロゴ]
			pstmt.setString(30, company.getStampImgPath());// 도장[判子]

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 회사 정보 단건 조회 (기본키 기준)
	// 조회 조건에 맞는 By식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うBy識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public Company selectById(Connection conn, int companyId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM COMPANY WHERE COMPANY_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, companyId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeCompanyFromResultSet(rs);
			}
			return null; // 조회된 데이터가 없을 경우 null 반환
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 회사 정보 수정
	// 식별조건에 해당하는 사업장 데이터를 전달받은 값으로 수정한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 識別条件に該当する事業所データを受け取った値で更新する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int update(Connection conn, Company company) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE COMPANY SET "
					+ "CMPN_NAME = ?, CEO_TITLE = ?, CEO_NAME = ?, BIZ_REG_NO = ?, CORP_REG_NO = ?, FOUNDATION_DATE = ?, HOMEPAGE_URL = ?, "
					+ "ZIP_CODE = ?, ADDRESS = ?, TEL_NO = ?, FAX_NO = ?, BIZ_TYPE = ?, BIZ_ITEM = ?, MANAGER_NAME = ?, MANAGER_DEPT_NAME = ?, MANAGER_POS_NAME = ?, "
					+ "MANAGER_TEL_NO = ?, MANAGER_MOBILE_NO = ?, MANAGER_EMAIL = ?, PAY_CALC_START_SCOPE = ?, PAY_CALC_START_DAY = ?, PAY_CALC_END_SCOPE = ?, "
					+ "PAY_CALC_END_DAY = ?, PAY_DATE_SCOPE = ?, PAY_DATE_DAY = ?, PAY_BANK_NAME = ?, PAY_ACCOUNT_NO = ?, PAY_ACCOUNT_HOLDER = ?, LOGO_IMG_PATH = ?, STAMP_IMG_PATH = ? "
					+ "WHERE COMPANY_ID = ?";

			pstmt = conn.prepareStatement(sql);
			// [회사기본정보][会社基本情報]
			pstmt.setString(1, company.getCmpnName());// 상호[商号]
			pstmt.setString(2, company.getCeoTitle());// 대표자직급[代表者職級]
			pstmt.setString(3, company.getCeoName());// 대표자성명[代表者名]
			pstmt.setString(4, company.getBizRegNo());// 사업자번호[事業者番号]
			pstmt.setString(5, company.getCorpRegNo());// 법인등록번호[法人登録番号]

			// 설립일[設立日]
			// 現在値と業務条件を比較し、条件を満たす場合にだけ後続処理を実行する。
			if (company.getFoundationDate() == null) {
				pstmt.setNull(6, Types.DATE);
			} else {
				pstmt.setTimestamp(6, new Timestamp(company.getFoundationDate().getTime()));
			}
			pstmt.setString(7, company.getHomepageUrl());// 홈페이지[ホームページ]
			pstmt.setString(8, company.getZipCode());// [사업장 주소 및 연락처]
			pstmt.setString(9, company.getAddress());//우편번호[郵便番号]
			pstmt.setString(10, company.getTelNo());// 전화번호[電話番号]
			pstmt.setString(11, company.getFaxNo());// 팩스번호[ファックス番号]
			
			// [사업자등록 정보]
			pstmt.setString(12, company.getBizType());// 업태[業態]
			pstmt.setString(13, company.getBizItem());// 종목[種目]
			
			// [급여·인사 담당자][給与・人事担当者]
			pstmt.setString(14, company.getManagerName());// 담당자성명[担当者名]
			pstmt.setString(15, company.getManagerDeptName());// 담당자부서[担当者部署]
			pstmt.setString(16, company.getManagerPosName());// 담당자직위[担当者職位]
			pstmt.setString(17, company.getManagerTelNo());// 담당자 전화번호[担当者電話番号]
			pstmt.setString(18, company.getManagerMobileNo());// 담당자 휴대폰번호[担当者携帯番号]
			pstmt.setString(19, company.getManagerEmail());// 담당자 이메일[担当者E-MAIL]
			
			// [급여계산 기간 및 지급일 설정][給与計算期間及び支給日設定]
			pstmt.setString(20, company.getPayCalcStartScope());// 급여 산정기간 시작월[給与算定期間開始月]
			pstmt.setString(21, company.getPayCalcStartDay());// 급여 산정기간 시작일[給与算定期間開始日]
			pstmt.setString(22, company.getPayCalcEndScope());// 급여 산정기간 종료월[給与算定期間終了月]
			pstmt.setString(23, company.getPayCalcEndDay());// 급여 산정기간 종료일[給与算定期間終了日]
			pstmt.setString(24, company.getPayDateScope());// 급여 지급월[給与支給月]
			pstmt.setString(25, company.getPayDateDay());// 급여 지급일[給与支給日]
			
			// [회사 급여지급 계좌][会社給与支給口座]
			pstmt.setString(26, company.getPayBankName());//금융기관[金融機関]
			pstmt.setString(27, company.getPayAccountNo());// 계좌번호[口座番号]
			pstmt.setString(28, company.getPayAccountHolder());// 예금주[預金者]
			
			// [증명서·명세서 이미지][証明書・明細書イメージ] 
			pstmt.setString(29, company.getLogoImgPath());//로고[ロゴ]
			pstmt.setString(30, company.getStampImgPath());// 도장[判子]
			pstmt.setInt(31, company.getCompanyId());// 회사ID[会社ID]

			return pstmt.executeUpdate(); // 수정된 행의 개수 반환
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 Company 객체로 변환
	// 조회값과 입력값을 조합하여 사업장From처리결과Set 처리 데이터를 구성한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 照会値と入力値を組み合わせて事業所From処理結果Setの処理データを構成する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private Company makeCompanyFromResultSet(ResultSet rs) throws SQLException {
		Company company = new Company();
		company.setCompanyId(rs.getInt("COMPANY_ID"));
		company.setCmpnName(rs.getString("CMPN_NAME"));
		company.setCeoTitle(rs.getString("CEO_TITLE"));
		company.setCeoName(rs.getString("CEO_NAME"));
		company.setBizRegNo(rs.getString("BIZ_REG_NO"));
		company.setCorpRegNo(rs.getString("CORP_REG_NO"));

		Timestamp fdTs = rs.getTimestamp("FOUNDATION_DATE");
		if (fdTs != null) {
			company.setFoundationDate(new java.util.Date(fdTs.getTime()));
		}

		company.setHomepageUrl(rs.getString("HOMEPAGE_URL"));
		company.setZipCode(rs.getString("ZIP_CODE"));
		company.setAddress(rs.getString("ADDRESS"));
		company.setTelNo(rs.getString("TEL_NO"));
		company.setFaxNo(rs.getString("FAX_NO"));
		company.setBizType(rs.getString("BIZ_TYPE"));
		company.setBizItem(rs.getString("BIZ_ITEM"));
		company.setManagerName(rs.getString("MANAGER_NAME"));
		company.setManagerDeptName(rs.getString("MANAGER_DEPT_NAME"));
		company.setManagerPosName(rs.getString("MANAGER_POS_NAME"));
		company.setManagerTelNo(rs.getString("MANAGER_TEL_NO"));
		company.setManagerMobileNo(rs.getString("MANAGER_MOBILE_NO"));
		company.setManagerEmail(rs.getString("MANAGER_EMAIL"));
		company.setPayCalcStartScope(rs.getString("PAY_CALC_START_SCOPE"));
		company.setPayCalcStartDay(rs.getString("PAY_CALC_START_DAY"));
		company.setPayCalcEndScope(rs.getString("PAY_CALC_END_SCOPE"));
		company.setPayCalcEndDay(rs.getString("PAY_CALC_END_DAY"));
		company.setPayDateScope(rs.getString("PAY_DATE_SCOPE"));
		company.setPayDateDay(rs.getString("PAY_DATE_DAY"));
		company.setPayBankName(rs.getString("PAY_BANK_NAME"));
		company.setPayAccountNo(rs.getString("PAY_ACCOUNT_NO"));
		company.setPayAccountHolder(rs.getString("PAY_ACCOUNT_HOLDER"));
		company.setLogoImgPath(rs.getString("LOGO_IMG_PATH"));
		company.setStampImgPath(rs.getString("STAMP_IMG_PATH"));

		return company;
	}
}
