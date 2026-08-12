package erp.employees.model;

/**
 * [인사/사원관리] 사원 입사 추천인 내역 Model DB 테이블: EMPLOYEE_RECOMMENDER
 */
public class EmployeeRecommender {

	// [DB 관리 항목]
	private int employeeRecommenderId; // 추천인 식별 번호 (PK)
	private int employeeId; // 사원 식별 번호 (FK)

	// [추천인 입력 항목]
	private String recommenderName; // 추천인 성명
	private String relation; // 관계
	private String companyName; // 소속 회사명
	private String positionName; // 직위명
	private String telNo; // 전화번호

	public EmployeeRecommender() {
	}

	// Getter & Setter
	public int getEmployeeRecommenderId() {
		return employeeRecommenderId;
	}

	public void setEmployeeRecommenderId(int employeeRecommenderId) {
		this.employeeRecommenderId = employeeRecommenderId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getRecommenderName() {
		return recommenderName;
	}

	public void setRecommenderName(String recommenderName) {
		this.recommenderName = recommenderName;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}
}