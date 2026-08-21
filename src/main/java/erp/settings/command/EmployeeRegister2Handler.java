package erp.settings.command;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.employees.model.Employee;
import erp.employees.model.EmployeeAppointment;
import erp.employees.model.EmployeeGuarantor;
import erp.employees.model.EmployeeLanguage;
import erp.employees.model.EmployeeLicense;
import erp.employees.model.EmployeeRecommender;
import erp.employees.model.EmployeeRewardDiscipline;
import erp.employees.model.EmployeeSuretyInsurance;
import erp.employees.model.EmployeeTraining;
import erp.employees.service.EmployeeGuaranteeService;
import erp.employees.service.EmployeePhotoService;
import erp.employees.service.EmployeeRegisterService;
import erp.employees.service.EmployeeSkillRecordService;
import erp.settings.service.DepartmentPositionService;
import erp.retirement.service.RetirementBenefitService;
import mvc.command.CommandHandler;

public class EmployeeRegister2Handler implements CommandHandler {

	// 4개의 핵심 비즈니스 서비스 객체 할당
	private EmployeeRegisterService registerService = EmployeeRegisterService.getInstance();
	private EmployeePhotoService photoService = EmployeePhotoService.getInstance();
	private EmployeeSkillRecordService skillService = EmployeeSkillRecordService.getInstance();
	private EmployeeGuaranteeService guaranteeService = EmployeeGuaranteeService.getInstance();
	private DepartmentPositionService deptPosService = DepartmentPositionService.getInstance();
	private RetirementBenefitService retirementBenefitService = new RetirementBenefitService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// HTTP 요청 방식(GET/POST)에 따른 분기 처리
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processAction(req, res);
		}
		return null;
	}

	// [GET] 사원정보 2 화면 렌더링 및 데이터 조회
	private String processForm(HttpServletRequest req, HttpServletResponse res) throws Exception {
		int empId = parseEmployeeId(req.getParameter("empId"));

		// 1단계에서 저장된 사원만 사원정보 2 화면에 접근할 수 있다.
		if (empId == 0) {
			req.getSession().setAttribute("message", "사원 기본 정보가 없습니다. 1단계를 먼저 완료해 주세요.");
			res.sendRedirect(req.getContextPath() + "/settings/register1.do");
			return null;
		}

		// 1. 사원 공통 기본정보 조회
		Employee employee = registerService.getEmployeeBasicProfile(empId);

		// 파라미터는 넘어왔지만 DB에 해당 사원이 없는 경우 (잘못된 주소 조작)
		if (employee == null) {
			req.getSession().setAttribute("message", "존재하지 않는 사원입니다. 1단계를 먼저 완료해 주세요.");
			res.sendRedirect(req.getContextPath() + "/settings/register1.do");
			return null;
		}

		// 2. 역량 및 인사기록(1:N) 리스트 조회
		List<EmployeeLicense> licenses = skillService.getLicenses(empId);
		List<EmployeeLanguage> languages = skillService.getLanguages(empId);
		List<EmployeeTraining> trainings = skillService.getTrainings(empId);
		List<EmployeeRewardDiscipline> rewardPunishes = skillService.getRewardPunishes(empId);
		List<EmployeeAppointment> appointments = skillService.getAppointments(empId);

		// 3. 추천 및 신원보증 단건 조회
		EmployeeRecommender recommender = guaranteeService.getRecommender(empId);
		EmployeeSuretyInsurance suretyInsurance = guaranteeService.getSuretyInsurance(empId);
		EmployeeGuarantor guarantor = guaranteeService.getGuarantor(empId);

		// 4. 조회 데이터를 JSP 속성으로 바인딩
		req.setAttribute("employee", employee);
		// 사원정보 1에서 저장한 부서와 직위를 요약 카드와 기본정보에 표시한다.
		req.setAttribute("departmentList", deptPosService.getDepartmentOptions());
		req.setAttribute("positionList", deptPosService.getJobPositionOptions());
		req.setAttribute("licenses", licenses);
		req.setAttribute("languages", languages);
		req.setAttribute("trainings", trainings);
		req.setAttribute("rewardPunishes", rewardPunishes);
		req.setAttribute("appointments", appointments);
		req.setAttribute("recommender", recommender);
		req.setAttribute("suretyInsurance", suretyInsurance);
		req.setAttribute("guarantor", guarantor);
		req.setAttribute("latestRetirementBenefit", retirementBenefitService.getLatestBenefit(empId));
		bindRowCounts(req, licenses.size(), languages.size(), trainings.size(), rewardPunishes.size(), appointments.size());

		return "/WEB-INF/view/settings/employee-register-2.jsp";
	}

	// [POST] 액션 파라미터에 따른 비즈니스 로직 분기 처리
	private String processAction(HttpServletRequest req, HttpServletResponse res) throws Exception {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		int empId = parseEmployeeId(req.getParameter("empId"));

		// 임의의 ID로 저장하지 않고, 1단계에서 실제로 생성된 사원만 처리한다.
		if (empId == 0 || registerService.getEmployeeBasicProfile(empId) == null) {
			req.getSession().setAttribute("message", "사원정보 1을 먼저 저장해 주세요.");
			res.sendRedirect(req.getContextPath() + "/settings/register1.do");
			return null;
		}

		try {
			if (action != null && action.startsWith("add")) {
				increaseRowCount(req, action);
				res.sendRedirect(req.getContextPath() + "/settings/register2.do?empId=" + empId);
				return null;
			}

			if ("save".equals(action)) {
				// [1] 사원 기본정보 중 퇴직 관련 속성 업데이트
				Employee employee = registerService.getEmployeeBasicProfile(empId);

				if (employee == null) {
					req.getSession().setAttribute("message", "사원 기본 정보가 존재하지 않습니다. 1단계를 먼저 완료해 주세요.");
					res.sendRedirect(req.getContextPath() + "/settings/register1.do");
					return null;
				}

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				employee.setRetireType(req.getParameter("retireType"));
				employee.setRetireReason(req.getParameter("retireReason"));
				employee.setAfterRetireContact(req.getParameter("afterRetireContact"));

				String retireDateStr = req.getParameter("retireDate");
				if (retireDateStr != null && !retireDateStr.trim().isEmpty()) {
					java.util.Date parsedRetireDate = sdf.parse(retireDateStr);

					// 퇴사일이 입사일보다 빠른지 검사
					if (parsedRetireDate.before(employee.getJoinDate())) {
						// 에러 메시지를 세션에 담고
						req.getSession().setAttribute("message",
								"저장 실패: 퇴직일자는 입사일(" + sdf.format(employee.getJoinDate()) + ")보다 이전일 수 없습니다.");
						// 저장을 중단한 뒤 현재 페이지로 돌려보냄
						res.sendRedirect(req.getContextPath() + "/settings/register2.do?empId=" + empId);
						return null;
					}

					// 검사를 무사히 통과했다면 날짜와 '퇴직' 상태를 세팅
					employee.setRetireDate(parsedRetireDate);
					employee.setStatus("퇴직");
				} else {
					// 날짜가 없으면 다시 '재직'으로 복구
					employee.setRetireDate(null);
					employee.setStatus("재직");
				}
				// 갱신된 퇴직 정보 데이터베이스 저장
				registerService.saveEmployeeBasicInfo(employee);

				// [2] 역량 및 인사기록 폼 데이터 파싱
				List<EmployeeLicense> licenses = parseLicenses(req, empId);
				List<EmployeeLanguage> languages = parseLanguages(req, empId);
				List<EmployeeTraining> trainings = parseTrainings(req, empId);
				List<EmployeeRewardDiscipline> rewards = parseRewardPunishes(req, empId);
				List<EmployeeAppointment> appointments = parseAppointments(req, empId);

				// 파싱된 역량/인사기록 데이터 일괄 저장 (트랜잭션)
				skillService.saveAllSkillRecords(empId, licenses, languages, trainings, rewards, appointments);

				// [3] 추천 및 신원보증 폼 데이터 파싱
				EmployeeRecommender recommender = parseRecommender(req, empId);
				EmployeeSuretyInsurance surety = parseSurety(req, empId);
				EmployeeGuarantor guarantor = parseGuarantor(req, empId);

				// 파싱된 추천/보증 데이터 일괄 저장 (트랜잭션)
				guaranteeService.saveGuarantees(empId, recommender, surety, guarantor);

				req.getSession().setAttribute("message", "사원 정보(2)가 저장되었습니다.");

			} else if ("savePhoto".equals(action)) {
				// 서버 업로드 대신 프로젝트에 준비된 예시 사진 중 하나를 저장한다.
				photoService.uploadPhoto(empId, getPresetPhotoPath(req.getParameter("photoPreset")));
				req.getSession().setAttribute("message", "사진이 등록되었습니다.");

			} else if ("deletePhoto".equals(action)) {
				// 프로필 사진 삭제 로직
				photoService.deletePhoto(empId);
				req.getSession().setAttribute("message", "사진이 삭제되었습니다.");

			} else if ("deleteLicenses".equals(action)) {
				// 자격 및 면허 선택 삭제 로직
				String[] deleteIds = req.getParameterValues("licenseDeleteIds");
				if (deleteIds != null) {
					List<Integer> idList = new ArrayList<>();
					for (String id : deleteIds) if (!isBlank(id)) idList.add(Integer.parseInt(id));
					if (!idList.isEmpty()) skillService.deleteSelectedItems("license", idList);
					decreaseRowCount(req, "licenseRowCount", 3, deleteIds.length);
					req.getSession().setAttribute("message", "선택한 자격/면허가 삭제되었습니다.");
				}

			} else if ("deleteLanguages".equals(action)) {
				// 어학능력 선택 삭제 로직
				String[] deleteIds = req.getParameterValues("languageDeleteIds");
				if (deleteIds != null) {
					List<Integer> idList = new ArrayList<>();
					for (String id : deleteIds) if (!isBlank(id)) idList.add(Integer.parseInt(id));
					if (!idList.isEmpty()) skillService.deleteSelectedItems("language", idList);
					decreaseRowCount(req, "languageRowCount", 1, deleteIds.length);
					req.getSession().setAttribute("message", "선택한 어학능력이 삭제되었습니다.");
				}

			} else if ("deleteTrainings".equals(action)) {
				// 교육 및 훈련 선택 삭제 로직
				String[] deleteIds = req.getParameterValues("trainingDeleteIds");
				if (deleteIds != null) {
					List<Integer> idList = new ArrayList<>();
					for (String id : deleteIds) if (!isBlank(id)) idList.add(Integer.parseInt(id));
					if (!idList.isEmpty()) skillService.deleteSelectedItems("training", idList);
					decreaseRowCount(req, "trainingRowCount", 2, deleteIds.length);
					req.getSession().setAttribute("message", "선택한 교육/훈련이 삭제되었습니다.");
				}

			} else if ("deleteRewardPunishes".equals(action)) {
				// 상벌 내역 선택 삭제 로직
				String[] deleteIds = req.getParameterValues("rewardDeleteIds");
				if (deleteIds != null) {
					List<Integer> idList = new ArrayList<>();
					for (String id : deleteIds) if (!isBlank(id)) idList.add(Integer.parseInt(id));
					if (!idList.isEmpty()) skillService.deleteSelectedItems("reward", idList);
					decreaseRowCount(req, "rewardRowCount", 2, deleteIds.length);
					req.getSession().setAttribute("message", "선택한 상벌 내역이 삭제되었습니다.");
				}

			} else if ("deleteAppointments".equals(action)) {
				// 인사발령 내역 선택 삭제 로직
				String[] deleteIds = req.getParameterValues("appointmentDeleteIds");
				if (deleteIds != null) {
					List<Integer> idList = new ArrayList<>();
					for (String id : deleteIds) if (!isBlank(id)) idList.add(Integer.parseInt(id));
					if (!idList.isEmpty()) skillService.deleteSelectedItems("appointment", idList);
					decreaseRowCount(req, "appointmentRowCount", 2, deleteIds.length);
					req.getSession().setAttribute("message", "선택한 인사발령 내역이 삭제되었습니다.");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("message", "처리 중 오류 발생: " + e.getMessage());
		}

		// 데이터 중복 전송 방지를 위한 리다이렉트 처리
		res.sendRedirect(req.getContextPath() + "/settings/register2.do?empId=" + empId);
		return null;
	}

	private int parseEmployeeId(String value) {
		try {
			int employeeId = Integer.parseInt(value);
			return employeeId > 0 ? employeeId : 0;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private String getPresetPhotoPath(String presetPhoto) {
		if (presetPhoto == null || !presetPhoto.matches("0[1-5]")) {
			throw new IllegalArgumentException("사용할 사원 사진을 선택해 주세요.");
		}
		return "/images/settings/employee-presets/employee-" + presetPhoto + ".png";
	}

	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}

	private void bindRowCounts(HttpServletRequest req, int licenses, int languages, int trainings,
			int rewards, int appointments) {
		req.setAttribute("licenseRowCount", getRowCount(req, "licenseRowCount", Math.max(3, licenses)));
		req.setAttribute("languageRowCount", getRowCount(req, "languageRowCount", Math.max(1, languages)));
		req.setAttribute("trainingRowCount", getRowCount(req, "trainingRowCount", Math.max(2, trainings)));
		req.setAttribute("rewardRowCount", getRowCount(req, "rewardRowCount", Math.max(2, rewards)));
		req.setAttribute("appointmentRowCount", getRowCount(req, "appointmentRowCount", Math.max(2, appointments)));
	}

	private int getRowCount(HttpServletRequest req, String key, int defaultCount) {
		Integer count = (Integer) req.getSession().getAttribute(key);
		return count == null ? defaultCount : Math.max(defaultCount, Math.min(count, 20));
	}

	private void increaseRowCount(HttpServletRequest req, String action) {
		String key;
		int defaultCount;
		if ("addLicense".equals(action)) {
			key = "licenseRowCount"; defaultCount = 3;
		} else if ("addLanguage".equals(action)) {
			key = "languageRowCount"; defaultCount = 1;
		} else if ("addTraining".equals(action)) {
			key = "trainingRowCount"; defaultCount = 2;
		} else if ("addRewardPunish".equals(action)) {
			key = "rewardRowCount"; defaultCount = 2;
		} else if ("addAppointment".equals(action)) {
			key = "appointmentRowCount"; defaultCount = 2;
		} else {
			return;
		}
		req.getSession().setAttribute(key, Math.min(getRowCount(req, key, defaultCount) + 1, 20));
	}

	private void decreaseRowCount(HttpServletRequest req, String key, int defaultCount, int amount) {
		int count = getRowCount(req, key, defaultCount);
		req.getSession().setAttribute(key, Math.max(defaultCount, count - Math.max(amount, 0)));
	}

	private int parseRowCount(HttpServletRequest req, String name, int defaultCount) {
		try {
			return Math.max(defaultCount, Math.min(Integer.parseInt(req.getParameter(name)), 20));
		} catch (Exception e) {
			return defaultCount;
		}
	}

	// [Helper] 자격 및 면허 폼 데이터 파싱
	private List<EmployeeLicense> parseLicenses(HttpServletRequest req, int empId) throws Exception {
		List<EmployeeLicense> list = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// 화면 0~2 루프 기준 반복 파싱
		for (int i = 0; i < parseRowCount(req, "licenseRowCount", 3); i++) {
			String licName = req.getParameter("licenses[" + i + "].licenseName");

			if (licName != null && !licName.trim().isEmpty()) {
				EmployeeLicense lic = new EmployeeLicense();
				lic.setEmployeeId(empId);
				lic.setLicName(licName);
				lic.setIssuer(req.getParameter("licenses[" + i + "].issuer"));
				lic.setLicenseNo(req.getParameter("licenses[" + i + "].licenseNo"));
				lic.setNote(req.getParameter("licenses[" + i + "].note"));

				String acqDateStr = req.getParameter("licenses[" + i + "].acquireDate");
				if (acqDateStr != null && !acqDateStr.trim().isEmpty()) {
					lic.setAcqDate(sdf.parse(acqDateStr));
				}

				list.add(lic);
			}
		}
		return list;
	}

	// [Helper] 어학능력 폼 데이터 파싱
	private List<EmployeeLanguage> parseLanguages(HttpServletRequest req, int empId) throws Exception {
		List<EmployeeLanguage> list = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// 화면 1줄 기준 반복 파싱
		for (int i = 0; i < parseRowCount(req, "languageRowCount", 1); i++) {
			String langName = req.getParameter("languages[" + i + "].languageName");

			if (langName != null && !langName.trim().isEmpty()) {
				EmployeeLanguage lang = new EmployeeLanguage();
				lang.setEmployeeId(empId);
				lang.setLangName(langName);
				lang.setTestName(req.getParameter("languages[" + i + "].testName"));
				lang.setScore(req.getParameter("languages[" + i + "].score"));
				lang.setReadingLevel(req.getParameter("languages[" + i + "].reading"));
				lang.setWritingLevel(req.getParameter("languages[" + i + "].writing"));
				lang.setSpeakingLevel(req.getParameter("languages[" + i + "].speaking"));

				String acqDateStr = req.getParameter("languages[" + i + "].acquireDate");
				if (acqDateStr != null && !acqDateStr.trim().isEmpty()) {
					lang.setAcqDate(sdf.parse(acqDateStr));
				}

				list.add(lang);
			}
		}
		return list;
	}

	// [Helper] 교육 및 훈련 폼 데이터 파싱
	private List<EmployeeTraining> parseTrainings(HttpServletRequest req, int empId) throws Exception {
		List<EmployeeTraining> list = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// 화면 0~1 루프 기준 반복 파싱
		for (int i = 0; i < parseRowCount(req, "trainingRowCount", 2); i++) {
			String trainName = req.getParameter("trainings[" + i + "].trainingName");

			if (trainName != null && !trainName.trim().isEmpty()) {
				EmployeeTraining training = new EmployeeTraining();
				training.setEmployeeId(empId);
				training.setTrainName(trainName);
				training.setTrainType(req.getParameter("trainings[" + i + "].trainingType"));
				training.setTrainInstitute(req.getParameter("trainings[" + i + "].institution"));

				String costStr = req.getParameter("trainings[" + i + "].trainingCost");
				if (costStr != null && !costStr.trim().isEmpty()) {
					training.setTrainCost(Long.parseLong(costStr));
				}

				String refundStr = req.getParameter("trainings[" + i + "].refundCost");
				if (refundStr != null && !refundStr.trim().isEmpty()) {
					training.setRefundCost(Long.parseLong(refundStr));
				}

				// 리스트에 넣기 전에 금액을 비교
				if (training.getRefundCost() > training.getTrainCost()) {
					throw new IllegalArgumentException("환급교육비가 교육비보다 클 수 없습니다. (교육명: " + trainName + ")");
				}

				String startDateStr = req.getParameter("trainings[" + i + "].startDate");
				if (startDateStr != null && !startDateStr.trim().isEmpty()) {
					training.setStartDate(sdf.parse(startDateStr));
				}

				String endDateStr = req.getParameter("trainings[" + i + "].endDate");
				if (endDateStr != null && !endDateStr.trim().isEmpty()) {
					training.setEndDate(sdf.parse(endDateStr));
				}

				list.add(training);
			}
		}
		return list;
	}

	// [Helper] 상벌 내역 폼 데이터 파싱
	private List<EmployeeRewardDiscipline> parseRewardPunishes(HttpServletRequest req, int empId) throws Exception {
		List<EmployeeRewardDiscipline> list = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// 화면 0~1 루프 기준 반복 파싱
		for (int i = 0; i < parseRowCount(req, "rewardRowCount", 2); i++) {
			String rpName = req.getParameter("rewardPunishes[" + i + "].rpName");
			String rpType = req.getParameter("rewardPunishes[" + i + "].rpType");

			if (rpName != null && !rpName.trim().isEmpty() && rpType != null && !rpType.trim().isEmpty()) {
				EmployeeRewardDiscipline reward = new EmployeeRewardDiscipline();
				reward.setEmployeeId(empId);
				reward.setRpName(rpName);
				reward.setRpType(rpType);
				reward.setRpAuthority(req.getParameter("rewardPunishes[" + i + "].grantor"));
				reward.setRpContent(req.getParameter("rewardPunishes[" + i + "].content"));
				reward.setNote(req.getParameter("rewardPunishes[" + i + "].note"));

				String rpDateStr = req.getParameter("rewardPunishes[" + i + "].rpDate");
				if (rpDateStr != null && !rpDateStr.trim().isEmpty()) {
					reward.setRpDate(sdf.parse(rpDateStr));
				}

				list.add(reward);
			}
		}
		return list;
	}

	// [Helper] 인사발령 내역 폼 데이터 파싱
	private List<EmployeeAppointment> parseAppointments(HttpServletRequest req, int empId) throws Exception {
		List<EmployeeAppointment> list = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// 화면 0~1 루프 기준 반복 파싱
		for (int i = 0; i < parseRowCount(req, "appointmentRowCount", 2); i++) {
			String appType = req.getParameter("appointments[" + i + "].appointmentType");
			String appDateStr = req.getParameter("appointments[" + i + "].appointmentDate");

			if (appType != null && !appType.trim().isEmpty() && appDateStr != null && !appDateStr.trim().isEmpty()) {
				EmployeeAppointment app = new EmployeeAppointment();
				app.setEmployeeId(empId);
				app.setAppType(appType);
				app.setAppDate(sdf.parse(appDateStr));
				app.setDepartmentName(req.getParameter("appointments[" + i + "].deptName"));
				app.setJobPositionName(req.getParameter("appointments[" + i + "].posName"));
				app.setJobTitleDuty(req.getParameter("appointments[" + i + "].dutyName"));
				app.setNote(req.getParameter("appointments[" + i + "].note"));

				list.add(app);
			}
		}
		return list;
	}

	// [Helper] 보증보험 단건 데이터 파싱
	private EmployeeSuretyInsurance parseSurety(HttpServletRequest req, int empId) throws Exception {
		EmployeeSuretyInsurance surety = new EmployeeSuretyInsurance();
		surety.setEmployeeId(empId);

		surety.setProviderName(req.getParameter("suretyInsurance.institution"));
		surety.setInsuranceNo(req.getParameter("suretyInsurance.insuranceNo"));

		String amountStr = req.getParameter("suretyInsurance.amount");
		if (amountStr != null && !amountStr.trim().isEmpty()) {
			surety.setInsuranceAmt(Long.parseLong(amountStr));
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String startDateStr = req.getParameter("suretyInsurance.startDate");
		if (startDateStr != null && !startDateStr.trim().isEmpty()) {
			surety.setSignupDate(sdf.parse(startDateStr));
		}

		String endDateStr = req.getParameter("suretyInsurance.endDate");
		if (endDateStr != null && !endDateStr.trim().isEmpty()) {
			surety.setExpireDate(sdf.parse(endDateStr));
		}

		surety.setNote(req.getParameter("suretyInsurance.note"));

		return surety;
	}

	// [Helper] 신원보증인 단건 데이터 파싱
	private EmployeeGuarantor parseGuarantor(HttpServletRequest req, int empId) throws Exception {
		EmployeeGuarantor guarantor = new EmployeeGuarantor();
		guarantor.setEmployeeId(empId);

		guarantor.setGuarantorName(req.getParameter("guarantor.guarantorName"));
		guarantor.setRelation(req.getParameter("guarantor.relation"));
		guarantor.setJuminNo(req.getParameter("guarantor.juminNo"));

		String amountStr = req.getParameter("guarantor.amount");
		if (amountStr != null && !amountStr.trim().isEmpty()) {
			guarantor.setGuaranteeAmt(Long.parseLong(amountStr));
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String startDateStr = req.getParameter("guarantor.startDate");
		if (startDateStr != null && !startDateStr.trim().isEmpty()) {
			guarantor.setGuaranteeDate(sdf.parse(startDateStr));
		}

		String endDateStr = req.getParameter("guarantor.endDate");
		if (endDateStr != null && !endDateStr.trim().isEmpty()) {
			guarantor.setExpireDate(sdf.parse(endDateStr));
		}

		guarantor.setTelNo(req.getParameter("guarantor.telNo"));

		return guarantor;
	}

	// [Helper] 추천인 단건 데이터 파싱
	private EmployeeRecommender parseRecommender(HttpServletRequest req, int empId) {
		EmployeeRecommender rec = new EmployeeRecommender();
		rec.setEmployeeId(empId);

		rec.setRecommenderName(req.getParameter("recommender.recommenderName"));
		rec.setRelation(req.getParameter("recommender.relation"));
		rec.setCompanyName(req.getParameter("recommender.companyName"));
		rec.setPositionName(req.getParameter("recommender.positionName"));
		rec.setTelNo(req.getParameter("recommender.telNo"));

		return rec;
	}
}
