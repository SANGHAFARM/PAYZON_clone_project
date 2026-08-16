package erp.employees.dto;

// 사원 목록의 현재 페이지와 페이지 이동 범위를 계산한다.
public class EmployeePageInfo {
	private final int currentPage;
	private final int totalPages;
	private final int startPage;
	private final int endPage;

	public EmployeePageInfo(int totalCount, int currentPage, int pageSize) {
		// 조회 결과가 없을 때도 JSP의 페이지 반복 범위가 유효하도록 1페이지를 유지한다.
		this.totalPages = (int) Math.ceil((double) totalCount / pageSize);
		this.currentPage = totalPages == 0 ? 1 : Math.min(currentPage, totalPages);
		this.startPage = totalPages == 0 ? 1 : ((this.currentPage - 1) / 5) * 5 + 1;
		this.endPage = totalPages == 0 ? 1 : Math.min(startPage + 4, totalPages);
	}

	public int getCurrentPage() { return currentPage; }
	public int getTotalPages() { return totalPages; }
	public int getStartPage() { return startPage; }
	public int getEndPage() { return endPage; }
	public boolean isHasPrevious() { return startPage > 1; }
	public boolean isHasNext() { return endPage < totalPages; }
	public int getPreviousPage() { return Math.max(1, startPage - 1); }
	public int getNextPage() { return Math.min(totalPages, endPage + 1); }
}
