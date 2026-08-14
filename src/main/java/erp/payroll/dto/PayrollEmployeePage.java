package erp.payroll.dto;

import java.util.List;

// 급여 대상에 추가할 사원 목록과 페이지 정보
public class PayrollEmployeePage {

	private List<PayrollManagementEmployee> content;
	private int totalPages;

	public PayrollEmployeePage(List<PayrollManagementEmployee> content, int totalPages) {
		this.content = content;
		this.totalPages = totalPages;
	}

	public List<PayrollManagementEmployee> getContent() {
		return content;
	}

	public int getTotalPages() {
		return totalPages;
	}
}
