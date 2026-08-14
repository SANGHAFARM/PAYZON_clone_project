package erp.payroll.dto;

// 사원별 급여내역 페이징 정보
public class EmployeePayrollHistoryPageInfo {

	private int currentPage;
	private int totalPages;

	public EmployeePayrollHistoryPageInfo(int currentPage, int totalPages) {
		this.currentPage = currentPage;
		this.totalPages = totalPages;
	}

	public int getCurrentPage() { return currentPage; }
	public int getStartPage() { return totalPages == 0 ? 1 : ((currentPage - 1) / 5) * 5 + 1; }
	public int getEndPage() { return Math.min(getStartPage() + 4, totalPages); }
	public boolean isHasPrevious() { return getStartPage() > 1; }
	public int getPreviousPage() { return Math.max(1, getStartPage() - 1); }
	public boolean isHasNext() { return getEndPage() < totalPages; }
	public int getNextPage() { return Math.min(totalPages, getEndPage() + 1); }
}
