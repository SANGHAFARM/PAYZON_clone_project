package erp.payroll.dto;

import java.util.List;

// 급여 대상에 추가할 일용직 사원 목록과 페이지 정보
public class DayWorkerEmployeePage {

	private List<DayWorkerPaymentEmployee> content;
	private int totalPages;

	public DayWorkerEmployeePage(List<DayWorkerPaymentEmployee> content, int totalPages) {
		this.content = content;
		this.totalPages = totalPages;
	}

	public List<DayWorkerPaymentEmployee> getContent() {
		return content;
	}

	public int getTotalPages() {
		return totalPages;
	}
}
