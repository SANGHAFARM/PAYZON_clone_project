package erp.attendance.service;

import java.util.Map;

//일용직 근무 월별 조회에서 사용
public class DailyWorkListRequest {
	private Integer year;
	private Integer month;
	private Integer deptId;
	private Integer posId;

	public DailyWorkListRequest() {
	}

	public DailyWorkListRequest(Integer year, Integer month, Integer deptId, Integer posId) {
		super();
		this.year = year;
		this.month = month;
		this.deptId = deptId;
		this.posId = posId;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
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
	public void validate(Map<String, Boolean> errors) {
		if (year == null) {
			errors.put("year", Boolean.TRUE);
		}
		if (month == null) {
			errors.put("month", Boolean.TRUE);
		}
	}

}
