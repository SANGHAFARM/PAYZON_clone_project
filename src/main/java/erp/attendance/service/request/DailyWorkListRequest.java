package erp.attendance.service.request;


//일용직 근무 월별 조회에서 사용
public class DailyWorkListRequest {
	private int year;
	private int month;
	private Integer deptId;
	private Integer posId;

	public DailyWorkListRequest() {
	}

	public DailyWorkListRequest(int year, int month, Integer deptId, Integer posId) {
		super();
		this.year = year;
		this.month = month;
		this.deptId = deptId;
		this.posId = posId;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getPosId() {
		return posId;
	}

	public void setPosId(Integer posId) {
		this.posId = posId;
	}
	

}
