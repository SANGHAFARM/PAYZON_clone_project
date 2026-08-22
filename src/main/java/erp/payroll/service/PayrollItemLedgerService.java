package erp.payroll.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import erp.payroll.dao.PayrollItemLedgerDao;
import erp.payroll.dto.PayrollItemLedgerPage;
import erp.payroll.dto.PayrollItemLedgerPage.PayrollItemLedgerRow;
import erp.payroll.dto.PayrollItemLedgerPage.PayrollItemLedgerTotals;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 항목별 대장 조회 결과와 합계를 구성한다.
// 급여항목Ledger 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 給与項目Ledgerの業務ルールとデータ変更トランザクションを処理する。
public class PayrollItemLedgerService {

	private PayrollItemLedgerDao ledgerDao = new PayrollItemLedgerDao();

	// 요청 조건에 맞는 급여항목Ledger 화면 데이터를 구성하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// リクエスト条件に合う給与項目Ledgerの画面データを構成して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public PayrollItemLedgerPage getPage(String startMonth, String endMonth, String itemCode) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			List<String> months = makeMonths(startMonth, endMonth);
			List<PayrollItemLedgerRow> rows = new ArrayList<>();
			if (isItemCode(itemCode)) {
				rows = ledgerDao.selectRows(conn, startMonth, endMonth, itemCode, months);
			}
			PayrollItemLedgerPage page = new PayrollItemLedgerPage();
			page.setItems(ledgerDao.selectItems(conn));
			page.setMonths(months);
			page.setRows(rows);
			page.setTotals(calculateTotals(rows, months.size()));
			return page;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 조회값과 입력값을 조합하여 월 목록 처리 데이터를 구성한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 照会値と入力値を組み合わせて月一覧の処理データを構成する。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	private List<String> makeMonths(String startMonth, String endMonth) {
		List<String> months = new ArrayList<>();
		YearMonth month = YearMonth.parse(startMonth);
		YearMonth lastMonth = YearMonth.parse(endMonth);
		while (!month.isAfter(lastMonth)) {
			months.add(month.toString());
			month = month.plusMonths(1);
		}
		return months;
	}

	// 조회된 금액과 업무 규칙을 이용해 합계정보 계산 결과를 생성한다.
	// 입력금액과 기간·요율·절사단위를 적용하고 계산 중 발생할 수 있는 NULL이나 음수 결과를 안전하게 보정한다.
	// 照会した金額と業務ルールを使用して合計情報の計算結果を生成する。
	// 入力金額と期間・料率・端数処理単位を適用し、計算中に発生し得るNULLや負数結果を安全に補正する。
	private PayrollItemLedgerTotals calculateTotals(List<PayrollItemLedgerRow> rows, int monthCount) {
		List<Long> amounts = new ArrayList<>();
		for (int index = 0; index < monthCount; index++) {
			long total = 0;
			for (PayrollItemLedgerRow row : rows) {
				total += row.getMonthlyAmounts().get(index);
			}
			amounts.add(total);
		}
		PayrollItemLedgerTotals totals = new PayrollItemLedgerTotals();
		totals.setMonthlyAmounts(amounts);
		return totals;
	}

	// 항목코드 조건의 충족 여부를 확인하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 項目コード条件を満たしているか確認して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	private boolean isItemCode(String itemCode) {
		return itemCode != null && itemCode.matches("[PD][0-9]+");
	}
}
