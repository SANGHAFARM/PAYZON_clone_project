package erp.settings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import erp.settings.model.Company;
import jdbc.JdbcUtil;

public class CompanyDao {

	private static CompanyDao companyDao = new CompanyDao();

	public static CompanyDao getInstance() {
		return companyDao;
	}

	private CompanyDao() {
	}

	/*
	 * Company 테이블에 회사 정보를 입력하는 메서드 Companyテーブルに会社情報を入力するメソッド
	 */
	public int insert(Connection conn, Company co) throws SQLException {
		String sql = "INSERT INTO COMPANY VALUES (seq_company_id.nextval, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			// [회사 기본정보][会社基本情報]
			pstmt.setString(1, co.getCmpnName()); // 상호[商号]
			pstmt.setString(2, co.getCeoTitle()); // 대표자직급[代表者職級]
			pstmt.setString(3, co.getCeoName()); // 대표자성명[代表者名]
			pstmt.setString(4, co.getBizRegNo()); // 사업자번호[事業者番号]
			pstmt.setString(5, co.getCorpRegNo()); // 법인등록번호[法人登録番号]
			pstmt.setDate(6, dateToSQLDate(co.getFoundationDate())); // 설립일[設立日]
			pstmt.setString(7, co.getHomepageUrl()); // 홈페이지[ホームページ]

			// [사업장 주소 및 연락처][事業所住所及び連絡先]
			pstmt.setString(8, co.getZipCode()); // 우편번호[郵便番号]
			pstmt.setString(9, co.getAddress()); // 사업장 주소[事業所住所]
			pstmt.setString(10, co.getTelNo()); // 전화번호[電話番号]
			pstmt.setString(11, co.getFaxNo()); // 팩스번호[ファックス番号]

			// [사업자등록 정보][事業者登録情報]
			pstmt.setString(12, co.getBizType()); // 업태[業態]
			pstmt.setString(13, co.getBizItem()); // 종목[種目]

			// [급여·인사 담당자][給与・人事担当者]
			pstmt.setString(14, co.getManagerName()); // 담당자 성명[担当者名]
			pstmt.setString(15, co.getManagerDeptName()); // 담당자부서[担当者部署]
			pstmt.setString(16, co.getManagerPosName()); // 담당자직위[担当者職位]
			pstmt.setString(17, co.getManagerTelNo()); // 담당자 전화번호[担当者電話番号]
			pstmt.setString(18, co.getManagerMobileNo()); // 담당자 휴대폰번호[担当者携帯番号]
			pstmt.setString(19, co.getManagerEmail()); // 담당자 이메일[担当者E-MAIL]

			// [급여계산 기간 및 지급일 설정][給与計算期間及び支給日設定]
			pstmt.setString(20, co.getPayCalcStartScope()); // 급여 산정기간 시작월[給与算定期間開始月]
			pstmt.setString(21, co.getPayCalcStartDay()); // 급여 산정기간 시작일[給与算定期間開始日]
			pstmt.setString(22, co.getPayCalcEndScope()); // 급여 산정기간 종료월[給与算定期間終了月]
			pstmt.setString(23, co.getPayCalcEndDay()); // 급여 산정기간 종료일[給与算定期間終了日]
			pstmt.setString(24, co.getPayDateScope()); // 급여 지급월[給与支給月]
			pstmt.setString(25, co.getPayDateDay()); // 급여 지급일[給与支給日]

			// [회사 급여지급 계좌][会社給与支給口座]
			pstmt.setString(26, co.getPayBankName()); // 금율기관[金融機関]
			pstmt.setString(27, co.getPayAccountNo()); // 계좌번호[口座番号]
			pstmt.setString(28, co.getPayAccountHolder()); // 예금주[預金者]

			// [증명서·명세서 이미지][証明書・明細書イメージ]
			pstmt.setString(29, co.getLogoImgPath()); // 로고[ロゴ]
			pstmt.setString(30, co.getStampImgPath()); // 도장[判子]
			return pstmt.executeUpdate();
		}
	}

	/*
	 * 회사ID로 회사 정보를 조회하는 메서드 会社IDで会社情報を照会するメソッド
	 */
	public Company selectById(Connection conn, int no) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM COMPANY WHERE COMPANY_ID = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();
			Company co = null;
			if (rs.next()) {
				co = convertCompany(rs);
			}
			return co;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	/*
	 * Company 테이블에 있는 회사 정보를 수정하는 메서드 Companyテーブルにある会社情報を修正するメソッド
	 */
	public int update(Connection conn, Company co) throws SQLException {
		String sql = "UPDATE COMPANY SET CMPN_NAME=?, CEO_TITLE=?, CEO_NAME=?, BIZ_REG_NO=?, "
				+ "CORP_REG_NO=?, FOUNDATION_DATE=?, HOMEPAGE_URL=?, ZIP_CODE=?, ADDRESS=?, TEL_NO=?, FAX_NO=?, "
				+ "BIZ_TYPE=?, BIZ_ITEM=?, MANAGER_NAME=?, MANAGER_DEPT_NAME=?, MANAGER_POS_NAME=?, MANAGER_TEL_NO=?, MANAGER_MOBILE_NO=?, MANAGER_EMAIL=?,"
				+ "PAY_CALC_START_SCOPE=?, PAY_CALC_START_DAY=?, PAY_CALC_END_SCOPE=?, PAY_CALC_END_DAY=?, PAY_DATE_SCOPE=?, PAY_DATE_DAY=?,"
				+ " PAY_BANK_NAME=?, PAY_ACCOUNT_NO=?, PAY_ACCOUNT_HOLDER=?, LOGO_IMG_PATH=?, STAMP_IMG_PATH=? WHERE COMPANY_ID=?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			// [회사 기본정보][会社基本情報]
			pstmt.setString(1, co.getCmpnName()); // 상호[商号]
			pstmt.setString(2, co.getCeoTitle()); // 대표자직급[代表者職級]
			pstmt.setString(3, co.getCeoName()); // 대표자성명[代表者名]
			pstmt.setString(4, co.getBizRegNo()); // 사업자번호[事業者番号]
			pstmt.setString(5, co.getCorpRegNo()); // 법인등록번호[法人登録番号]
			pstmt.setDate(6, dateToSQLDate(co.getFoundationDate())); // 설립일[設立日]
			pstmt.setString(7, co.getHomepageUrl()); // 홈페이지[ホームページ]

			// [사업장 주소 및 연락처][事業所住所及び連絡先]
			pstmt.setString(8, co.getZipCode()); // 우편번호[郵便番号]
			pstmt.setString(9, co.getAddress()); // 사업장 주소[事業所住所]
			pstmt.setString(10, co.getTelNo()); // 전화번호[電話番号]
			pstmt.setString(11, co.getFaxNo()); // 팩스번호[ファックス番号]

			// [사업자등록 정보][事業者登録情報]
			pstmt.setString(12, co.getBizType()); // 업태[業態]
			pstmt.setString(13, co.getBizItem()); // 종목[種目]

			// [급여·인사 담당자][給与・人事担当者]
			pstmt.setString(14, co.getManagerName()); // 담당자 성명[担当者名]
			pstmt.setString(15, co.getManagerDeptName()); // 담당자부서[担当者部署]
			pstmt.setString(16, co.getManagerPosName()); // 담당자직위[担当者職位]
			pstmt.setString(17, co.getManagerTelNo()); // 담당자 전화번호[担当者電話番号]
			pstmt.setString(18, co.getManagerMobileNo()); // 담당자 휴대폰번호[担当者携帯番号]
			pstmt.setString(19, co.getManagerEmail()); // 담당자 이메일[担当者E-MAIL]

			// [급여계산 기간 및 지급일 설정][給与計算期間及び支給日設定]
			pstmt.setString(20, co.getPayCalcStartScope()); // 급여 산정기간 시작월[給与算定期間開始月]
			pstmt.setString(21, co.getPayCalcStartDay()); // 급여 산정기간 시작일[給与算定期間開始日]
			pstmt.setString(22, co.getPayCalcEndScope()); // 급여 산정기간 종료월[給与算定期間終了月]
			pstmt.setString(23, co.getPayCalcEndDay()); // 급여 산정기간 종료일[給与算定期間終了日]
			pstmt.setString(24, co.getPayDateScope()); // 급여 지급월[給与支給月]
			pstmt.setString(25, co.getPayDateDay()); // 급여 지급일[給与支給日]

			// [회사 급여지급 계좌][会社給与支給口座]
			pstmt.setString(26, co.getPayBankName()); // 금율기관[金融機関]
			pstmt.setString(27, co.getPayAccountNo()); // 계좌번호[口座番号]
			pstmt.setString(28, co.getPayAccountHolder()); // 예금주[預金者]

			// [증명서·명세서 이미지][証明書・明細書イメージ]
			pstmt.setString(29, co.getLogoImgPath()); // 로고[ロゴ]
			pstmt.setString(30, co.getStampImgPath()); // 도장[判子]
			pstmt.setInt(31, co.getCompanyId()); // 회사ID[会社ID]
			return pstmt.executeUpdate();
		}

	}

	/*
	 * java.util.Date 타입을 java.sql.Date타입으로 변환하는 메서드
	 * java.util.Dateタイプをjava.sql.Dateタイプに変換するメソッド
	 */
	private java.sql.Date dateToSQLDate(java.util.Date date) {
		return (date != null) ? new java.sql.Date(date.getTime()) : null;
	}

	/*
	 * // ResultSet으로 Company객체를 만들어 반환하는 메서드 ResultSetでCompanyオブジェクトを作って返すメソッド
	 */
	private Company convertCompany(ResultSet rs) throws SQLException {
		Company co = new Company();

		// [회사 기본정보][会社基本情報]
		co.setCompanyId(rs.getInt("COMPANY_ID")); // 회사ID[会社ID]
		co.setCmpnName(rs.getString("CMPN_NAME")); // 상호[商号]
		co.setCeoTitle(rs.getString("CEO_TITLE")); // 대표자직급[代表者職級]
		co.setCeoName(rs.getString("CEO_NAME")); // 대표자성명[代表者名]
		co.setBizRegNo(rs.getString("BIZ_REG_NO")); // 사업자번호[事業者番号]
		co.setCorpRegNo(rs.getString("CORP_REG_NO")); // 법인등록번호[法人登録番号]
		co.setFoundationDate(rs.getDate("FOUNDATION_DATE")); // 설립일[設立日]
		co.setHomepageUrl(rs.getString("HOMEPAGE_URL")); // 홈페이지[ホームページ]

		// [사업장 주소 및 연락처][事業所住所及び連絡先]
		co.setZipCode(rs.getString("ZIP_CODE")); // 우편번호[郵便番号]
		co.setAddress(rs.getString("ADDRESS")); // 사업장 주소[事業所住所]
		co.setTelNo(rs.getString("TEL_NO")); // 전화번호[電話番号]
		co.setFaxNo(rs.getString("FAX_NO")); // 팩스번호[ファックス番号]

		// [사업자등록 정보][事業者登録情報]
		co.setBizType(rs.getString("BIZ_TYPE")); // 업태[業態]
		co.setBizItem(rs.getString("BIZ_ITEM")); // 종목[種目]

		// [급여·인사 담당자][給与・人事担当者]
		co.setManagerName(rs.getString("MANAGER_NAME")); // 담당자 성명[担当者名]
		co.setManagerDeptName(rs.getString("MANAGER_DEPT_NAME")); // 담당자부서[担当者部署]
		co.setManagerPosName(rs.getString("MANAGER_POS_NAME")); // 담당자직위[担当者職位]
		co.setManagerTelNo(rs.getString("MANAGER_TEL_NO")); // 담당자 전화번호[担当者電話番号]
		co.setManagerMobileNo(rs.getString("MANAGER_MOBILE_NO")); // 담당자 휴대폰번호[担当者携帯番号]
		co.setManagerEmail(rs.getString("MANAGER_EMAIL")); // 담당자 이메일[担当者E-MAIL]

		// [급여계산 기간 및 지급일 설정][給与計算期間及び支給日設定]
		co.setPayCalcStartScope(rs.getString("PAY_CALC_START_SCOPE")); // 급여 산정기간 시작월[給与算定期間開始月]
		co.setPayCalcStartDay(rs.getString("PAY_CALC_START_DAY")); // 급여 산정기간 시작일[給与算定期間開始日]
		co.setPayCalcEndScope(rs.getString("PAY_CALC_END_SCOPE")); // 급여 산정기간 종료월[給与算定期間終了月]
		co.setPayCalcEndDay(rs.getString("PAY_CALC_END_DAY")); // 급여 산정기간 종료일[給与算定期間終了日]
		co.setPayDateScope(rs.getString("PAY_DATE_SCOPE")); // 급여 지급월[給与支給月]
		co.setPayDateDay(rs.getString("PAY_DATE_DAY")); // 급여 지급일[給与支給日]

		// [회사 급여지급 계좌][会社給与支給口座]
		co.setPayBankName(rs.getString("PAY_BANK_NAME")); // 금율기관[金融機関]
		co.setPayAccountNo(rs.getString("PAY_ACCOUNT_NO")); // 계좌번호[口座番号]
		co.setPayAccountHolder(rs.getString("PAY_ACCOUNT_HOLDER")); // 예금주[預金者]

		// [증명서·명세서 이미지][証明書・明細書イメージ]
		co.setLogoImgPath(rs.getString("LOGO_IMG_PATH")); // 로고[ロゴ]
		co.setStampImgPath(rs.getString("STAMP_IMG_PATH")); // 도장[判子]

		return co;
	}

}