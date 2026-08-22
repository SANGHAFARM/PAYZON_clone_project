package erp.employees.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import erp.employees.dao.CertificateIssuanceDao;
import erp.employees.dao.EmployeeDao;
import erp.employees.dto.CertificateCareerItem;
import erp.employees.dto.EmployeeListItem;
import erp.employees.model.CertificateIssuance;
import erp.employees.model.Employee;
import erp.settings.dao.CompanyDao;
import erp.settings.dao.DepartmentDao;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 제증명서 화면 조회와 발급내역 저장을 처리한다.
// 제증명서발급 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 証明書発行の業務ルールとデータ変更トランザクションを処理する。
public class CertificateIssueService {
	private final EmployeeDao employeeDao = EmployeeDao.getInstance();
	private final CertificateIssuanceDao certificateDao = CertificateIssuanceDao.getInstance();

	// 제증명서발급 처리에 필요한 발급데이터를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 証明書発行処理に必要な発行データを照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public CertificateIssueData getIssueData(Integer employeeId, String keyword) {
		EmployeeSearchCondition condition = new EmployeeSearchCondition();
		condition.setSearchTarget("ALL");
		condition.setKeyword(keyword == null ? "" : keyword.trim());
		condition.setEmploymentType("");
		condition.setStatus("");
		condition.setPage(1);
		condition.setPageSize(100);

		try (Connection conn = ConnectionProvider.getConnection()) {
			CertificateIssueData data = new CertificateIssueData();
			data.employees = employeeDao.selectListByCondition(conn, condition);
			data.departments = DepartmentDao.getInstance().selectAll(conn);
			data.company = CompanyDao.getInstance().selectById(conn, 1);
			data.issueDate = new Date();

			// 증명서는 사원을 직접 선택한 후에만 발급 대상 정보를 조회한다.
			// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
			Integer selectedId = employeeId;
			for (EmployeeListItem employee : data.employees) {
				if (selectedId != null && employee.getEmployeeId() == selectedId) {
					data.selectedEmployee = employee;
					data.careers = createCareerRows(employee);
					break;
				}
			}
			return data;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// sue 조건의 충족 여부를 확인하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// sue条件を満たしているか確認して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public void issue(CertificateIssuance certificate) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			certificate.setCertDocNo("CERT-" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
			certificateDao.insert(conn, certificate);
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 재직상태에 맞는 증명서만 발급할 수 있는지 확인한다.
	// 발급 입력값과 업무 처리 가능 여부를 검증한다.
	// 필수값과 상태 조합을 확인해 잘못된 데이터가 DAO 또는 후속 계산으로 전달되지 않도록 차단한다.
	// 発行の入力値と業務処理の可否を検証する。
	// 必須値と状態の組み合わせを確認し、不正データがDAOまたは後続計算へ渡らないよう遮断する。
	public String validateIssue(int employeeId, String certificateType) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			Employee employee = employeeDao.selectById(conn, employeeId);
			if (employee == null) {
				return "선택한 사원정보가 없습니다.";
			}
			if ("WORKING".equals(certificateType) && "퇴직".equals(employee.getStatus())) {
				return "퇴직 사원은 재직증명서를 작성할 수 없습니다.";
			}
			if ("RETIREMENT".equals(certificateType) && !"퇴직".equals(employee.getStatus())) {
				return "재직 사원은 퇴직증명서를 작성할 수 없습니다.";
			}
			return null;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// 제증명서발급 처리에 사용할 경력행 목록 데이터나 객체를 생성한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 証明書発行処理で使用する経歴行一覧データまたはオブジェクトを生成する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	private List<CertificateCareerItem> createCareerRows(EmployeeListItem employee) {
		List<CertificateCareerItem> careers = new ArrayList<>();
		CertificateCareerItem career = new CertificateCareerItem();
		career.setJoinDate(employee.getJoinDate());
		career.setRetirementDate(employee.getRetirementDate());
		career.setDepartmentName(employee.getDepartmentName());
		career.setPositionName(employee.getPositionName());
		career.setDuty("");
		careers.add(career);
		employee.setCareerPeriod(calculateCareerPeriod(employee.getJoinDate(), employee.getRetirementDate()));
		return careers;
	}

	// 조회된 금액과 업무 규칙을 이용해 경력기간 계산 결과를 생성한다.
	// 입력금액과 기간·요율·절사단위를 적용하고 계산 중 발생할 수 있는 NULL이나 음수 결과를 안전하게 보정한다.
	// 照会した金額と業務ルールを使用して経歴期間の計算結果を生成する。
	// 入力金額と期間・料率・端数処理単位を適用し、計算中に発生し得るNULLや負数結果を安全に補正する。
	private String calculateCareerPeriod(String start, String end) {
		try {
			LocalDate startDate = LocalDate.parse(start);
			LocalDate endDate = end == null || end.isEmpty() ? LocalDate.now() : LocalDate.parse(end);
			Period period = Period.between(startDate, endDate);
			return period.getYears() + "년 " + period.getMonths() + "개월 " + period.getDays() + "일";
		} catch (Exception e) {
			return "";
		}
	}

	public static class CertificateIssueData {
		private List<EmployeeListItem> employees;
		private EmployeeListItem selectedEmployee;
		private List<CertificateCareerItem> careers;
		private Object departments;
		private Object company;
		private Date issueDate;
		// 제증명서발급 처리에 필요한 사원를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 証明書発行処理に必要な社員を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public List<EmployeeListItem> getEmployees() { return employees; }
		// 제증명서발급 처리에 필요한 Selected사원를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 証明書発行処理に必要なSelected社員を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public EmployeeListItem getSelectedEmployee() { return selectedEmployee; }
		// 제증명서발급 처리에 필요한 Careers를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 証明書発行処理に必要なCareersを照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public List<CertificateCareerItem> getCareers() { return careers; }
		// 제증명서발급 처리에 필요한 부서 목록를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 証明書発行処理に必要な部署一覧を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public Object getDepartments() { return departments; }
		// 제증명서발급 처리에 필요한 사업장를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 証明書発行処理に必要な事業所を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public Object getCompany() { return company; }
		// 제증명서발급 처리에 필요한 발급일자를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 証明書発行処理に必要な発行日付を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public Date getIssueDate() { return issueDate; }
	}
}
