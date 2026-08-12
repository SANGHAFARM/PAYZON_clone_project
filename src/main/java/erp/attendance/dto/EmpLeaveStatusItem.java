package erp.attendance.dto;
/*
 * 근태관리>휴가조회에서 검색결과를 담을 클래스 
*/
public class EmpLeaveStatusItem {
    private int empId;
    private String empType;
    private String empNo;
    private String empNameKr;
    private String deptName;
    private String posName;
    private String itemName;
    private double totalDays;
    private double usedDays;
    private double remainDays;
	public EmpLeaveStatusItem() {
	}
	public EmpLeaveStatusItem(int empId, String empType, String empNo, String empNameKr, String deptName,
			String posName, String itemName, double totalDays, double usedDays, double remainDays) {
		super();
		this.empId = empId;
		this.empType = empType;
		this.empNo = empNo;
		this.empNameKr = empNameKr;
		this.deptName = deptName;
		this.posName = posName;
		this.itemName = itemName;
		this.totalDays = totalDays;
		this.usedDays = usedDays;
		this.remainDays = remainDays;
	}
	public int getEmpId() {
		return empId;
	}
	public void setEmpId(int empId) {
		this.empId = empId;
	}
	public String getEmpType() {
		return empType;
	}
	public void setEmpType(String empType) {
		this.empType = empType;
	}
	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	public String getEmpNameKr() {
		return empNameKr;
	}
	public void setEmpNameKr(String empNameKr) {
		this.empNameKr = empNameKr;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getPosName() {
		return posName;
	}
	public void setPosName(String posName) {
		this.posName = posName;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public double getTotalDays() {
		return totalDays;
	}
	public void setTotalDays(double totalDays) {
		this.totalDays = totalDays;
	}
	public double getUsedDays() {
		return usedDays;
	}
	public void setUsedDays(double usedDays) {
		this.usedDays = usedDays;
	}
	public double getRemainDays() {
		return remainDays;
	}
	public void setRemainDays(double remainDays) {
		this.remainDays = remainDays;
	}
	
	
}
