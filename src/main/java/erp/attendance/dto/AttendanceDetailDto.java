package erp.attendance.dto;
import java.util.Date;

/*
 *근태 조회탭의 상세 조회 결과를 담을 모델
*/
public class AttendanceDetailDto {
    private Date inputDate;
    private String empType;
    private String empNameKr;
    private String departmentName;
    private String jobPositionName;
    private String attendName;
    private Date startDate;
    private Date endDate;
    private double attendValue;
    private long payAmount;
    private String note;

	public AttendanceDetailDto(Date inputDate, String empType, String empNameKr, String departmentName,
			String jobPositionName, String attendName, Date startDate, Date endDate, double attendValue, long payAmount,
			String note) {
		super();
		this.inputDate = inputDate;
		this.empType = empType;
		this.empNameKr = empNameKr;
		this.departmentName = departmentName;
		this.jobPositionName = jobPositionName;
		this.attendName = attendName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.attendValue = attendValue;
		this.payAmount = payAmount;
		this.note = note;
	}
	public AttendanceDetailDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Date getInputDate() {
		return inputDate;
	}
	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}
	public String getEmpType() {
		return empType;
	}
	public void setEmpType(String empType) {
		this.empType = empType;
	}
	public String getEmpNameKr() {
		return empNameKr;
	}
	public void setEmpNameKr(String empNameKr) {
		this.empNameKr = empNameKr;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getJobPositionName() {
		return jobPositionName;
	}
	public void setJobPositionName(String jobPositionName) {
		this.jobPositionName = jobPositionName;
	}
	public String getAttendName() {
		return attendName;
	}
	public void setAttendName(String attendName) {
		this.attendName = attendName;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public double getAttendValue() {
		return attendValue;
	}
	public void setAttendValue(double attendValue) {
		this.attendValue = attendValue;
	}
	public long getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(long payAmount) {
		this.payAmount = payAmount;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
    
    
}
