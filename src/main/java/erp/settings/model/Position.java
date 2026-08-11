package erp.settings.model;

// POSITION: 직위 설정
public class Position {
	
	// 기본 생성자
	public Position() {
	}
	
	private int posId;
	private String posName;
	
	public int getPosId() {
		return posId;
	}
	
	public void setPosId(int posId) {
		this.posId = posId;
	}
	
	public String getPosName() {
		return posName;
	}
	
	public void setPosName(String posName) {
		this.posName = posName;
	}
}