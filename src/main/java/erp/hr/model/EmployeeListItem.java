package erp.hr.model;


/*
 * 근태기록관리에서 사원 정보를 보여주기 위한 클래스
*/
public class EmployeeListItem {

	private int empId;
	private String empType;
	private String empNo;
	private String empNameKr;
	private String deptName;
	private String posName;
	public EmployeeListItem() {
	}
	public EmployeeListItem(int empId, String empType, String empNo, String empNameKr, String deptName,
			String posName) {
		super();
		this.empId = empId;
		this.empType = empType;
		this.empNo = empNo;
		this.empNameKr = empNameKr;
		this.deptName = deptName;
		this.posName = posName;
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
	
}
