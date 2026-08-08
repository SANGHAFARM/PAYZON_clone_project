package erp.hr.model;

// EMP_RECOMMENDER: 추천인
public class EmpRecommender {
	private int recommenderId;
	private int empId;
	private String recommenderName;
	private String relation;
	private String companyName;
	private String positionName;
	private String telNo;

	// 기본 생성자
	public EmpRecommender() {
	}

	public int getRecommenderId() {
		return recommenderId;
	}

	public void setRecommenderId(int recommenderId) {
		this.recommenderId = recommenderId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
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