package erp.attendance.service.page;

import java.util.List;

import erp.attendance.dto.LeaveInquiryDto;

public class LeaveInquiryPage {

	private int total;
	private int currentPage;
	private List<LeaveInquiryDto> content;
	private int totalPages;
	private int startPage;
	private int endPage;
	
	public LeaveInquiryPage(int total, int currentPage, int size, List<LeaveInquiryDto> content) {
		this.total = total;
		this.currentPage = currentPage;
		this.content = content;
		if (total==0) {
			totalPages = 0;
			startPage = 0;
			endPage = 0;
		} else {
			totalPages = total/size;
			if (total%size > 0) {
				totalPages++;
			}
			int modVal = currentPage%10;
			startPage = currentPage/10*10+1;
			if (modVal==0) {startPage-=10;
			}
			endPage = startPage+9;
			if (endPage>totalPages) {
				endPage = totalPages;
			}
			
		}
		
	}
	public LeaveInquiryPage() {
		super();
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public List<LeaveInquiryDto> getContent() {
		return content;
	}
	public void setContent(List<LeaveInquiryDto> content) {
		this.content = content;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	public boolean hasNoDailyWorks() {
		return total ==0;
	}
	public boolean hasDailyWorks() {
		return total>0;
	}
	
}
