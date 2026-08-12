package erp.payroll.model;

import java.util.Date;

/**
 * [급여관리] 월별 급여계산 회차 마스터 Model DB 테이블: PAYROLL_RUN
 */
public class PayrollRun {

	// [DB 관리 항목]
	private Long payrollRunId; // 급여계산 회차 식별 번호 (PK)

	// [귀속연월과 급여차수]
	private String payYear; // 귀속연월 (연도, YYYY)
	private String payMonth; // 귀속연월 (월, MM)
	private String paySeq; // 급여차수 (01~10)

	// [급여회차 유형]
	private String incomeType; // 자동 저장 항목: 급여 화면 구분 (0=일반소득, 1=사업/기타소득, 2=일용직)

	// [정산기간·급여지급일]
	private Date calcStartDate; // 정산기간 (시작일)
	private Date calcEndDate; // 정산기간 (종료일)
	private Date payDate; // 급여지급일

	public PayrollRun() {
	}

	// Getter & Setter
	public Long getPayrollRunId() {
		return payrollRunId;
	}

	public void setPayrollRunId(Long payrollRunId) {
		this.payrollRunId = payrollRunId;
	}

	public String getPayYear() {
		return payYear;
	}

	public void setPayYear(String payYear) {
		this.payYear = payYear;
	}

	public String getPayMonth() {
		return payMonth;
	}

	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}

	public String getPaySeq() {
		return paySeq;
	}

	public void setPaySeq(String paySeq) {
		this.paySeq = paySeq;
	}

	public String getIncomeType() {
		return incomeType;
	}

	public void setIncomeType(String incomeType) {
		this.incomeType = incomeType;
	}

	public Date getCalcStartDate() {
		return calcStartDate;
	}

	public void setCalcStartDate(Date calcStartDate) {
		this.calcStartDate = calcStartDate;
	}

	public Date getCalcEndDate() {
		return calcEndDate;
	}

	public void setCalcEndDate(Date calcEndDate) {
		this.calcEndDate = calcEndDate;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
}