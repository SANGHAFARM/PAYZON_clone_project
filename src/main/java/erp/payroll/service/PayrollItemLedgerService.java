package erp.payroll.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import erp.payroll.dao.PayrollItemLedgerDao;
import erp.payroll.dto.PayrollItemLedgerPage;
import erp.payroll.dto.PayrollItemLedgerRow;
import erp.payroll.dto.PayrollItemLedgerTotals;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 항목별 대장 조회 결과와 합계를 구성하는 서비스
public class PayrollItemLedgerService {

	private PayrollItemLedgerDao ledgerDao = new PayrollItemLedgerDao();

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

	private boolean isItemCode(String itemCode) {
		return itemCode != null && itemCode.matches("[PD][0-9]+");
	}
}
