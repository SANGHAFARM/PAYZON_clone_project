package erp.attendance.service.page;

import java.util.List;

import erp.attendance.dto.AttendanceDetailDto;
import erp.attendance.dto.DailyWorkDetailDto;

//근태조회 -> 상세 조회에서 처리할 근태내역 페이지
//勤怠照会　→　詳細照会で処理する勤怠履歴ページ
public class AttendanceDetailPage {
	private int total;
	private int currentPage;
	private List<AttendanceDetailDto> content;
	private int totalPages;
	private int startPage;
	private int endPage;

	public AttendanceDetailPage(int total, int currentPage, int size, List<AttendanceDetailDto> content) {
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

	public AttendanceDetailPage() {
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

	public List<AttendanceDetailDto> getContent() {
		return content;
	}

	public void setContent(List<AttendanceDetailDto> content) {
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
		return total == 0;
	}

	public boolean hasDailyWorks() {
		return total > 0;
	}

}
