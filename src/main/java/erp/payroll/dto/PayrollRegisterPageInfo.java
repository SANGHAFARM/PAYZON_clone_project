package erp.payroll.dto;

// 급여대장 목록의 페이지 정보
public class PayrollRegisterPageInfo {

	private int number;
	private int totalPages;

	public PayrollRegisterPageInfo(int number, int totalPages) {
		this.number = number;
		this.totalPages = totalPages;
	}

	public int getNumber() {
		return number;
	}

	public int getTotalPages() {
		return totalPages;
	}
}
