package erp.payroll.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

// 항목별 대장에 표시할 결재란 설정
public class PayrollApprovalSetting implements Serializable {

	private static final long serialVersionUID = 1L;

	private int approvalCount = 3;
	private Map<Integer, String> approvalUses = new HashMap<>();
	private Map<Integer, String> approverNames = new HashMap<>();

	public PayrollApprovalSetting() {
		for (int number = 1; number <= 5; number++) {
			approvalUses.put(number, number <= approvalCount ? "Y" : "N");
			approverNames.put(number, "");
		}
	}

	public int getApprovalCount() { return approvalCount; }
	public void setApprovalCount(int value) { approvalCount = value; }
	public Map<Integer, String> getApprovalUses() { return approvalUses; }
	public Map<Integer, String> getApproverNames() { return approverNames; }
}
