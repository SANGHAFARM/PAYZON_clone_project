package erp.payroll.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.payroll.dao.FourInsuranceDao;
import erp.payroll.dto.FourInsurancePage.FourInsuranceDeduction;
import erp.payroll.dto.FourInsurancePage;
import erp.payroll.dto.FourInsurancePage.FourInsuranceTotals;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 4대보험 공제내역과 전체 합계를 구성한다.
// 4대보험보험 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 四大保険保険の業務ルールとデータ変更トランザクションを処理する。
public class FourInsuranceService {

	private FourInsuranceDao insuranceDao = new FourInsuranceDao();

	// 요청 조건에 맞는 4대보험보험 화면 데이터를 구성하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// リクエスト条件に合う四大保険保険の画面データを構成して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public FourInsurancePage getPage(String year, String month, String sequence) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			FourInsurancePage page = insuranceDao.selectPage(conn, year, month, sequence);
			page.setTotals(calculateTotals(page));
			return page;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 조회된 금액과 업무 규칙을 이용해 합계정보 계산 결과를 생성한다.
	// 입력금액과 기간·요율·절사단위를 적용하고 계산 중 발생할 수 있는 NULL이나 음수 결과를 안전하게 보정한다.
	// 照会した金額と業務ルールを使用して合計情報の計算結果を生成する。
	// 入力金額と期間・料率・端数処理単位を適用し、計算中に発生し得るNULLや負数結果を安全に補正する。
	private FourInsuranceTotals calculateTotals(FourInsurancePage page) {
		FourInsuranceTotals totals = new FourInsuranceTotals();
		for (FourInsuranceDeduction deduction : page.getDeductions()) {
			totals.setPensionEmployer(totals.getPensionEmployer() + deduction.getPensionEmployer());
			totals.setPensionEmployee(totals.getPensionEmployee() + deduction.getPensionEmployee());
			totals.setHealthEmployer(totals.getHealthEmployer() + deduction.getHealthEmployer());
			totals.setHealthEmployee(totals.getHealthEmployee() + deduction.getHealthEmployee());
			totals.setCareEmployer(totals.getCareEmployer() + deduction.getCareEmployer());
			totals.setCareEmployee(totals.getCareEmployee() + deduction.getCareEmployee());
			totals.setEmploymentEmployer(totals.getEmploymentEmployer() + deduction.getEmploymentEmployer());
			totals.setEmploymentEmployee(totals.getEmploymentEmployee() + deduction.getEmploymentEmployee());
		}
		return totals;
	}
}
