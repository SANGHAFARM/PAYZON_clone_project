package erp.settings.command;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

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
import mvc.command.CommandHandler;

public class EmployeeRegister2Handler implements CommandHandler {

	// 4개의 핵심 비즈니스 서비스 객체 할당
	private EmployeeRegisterService registerService = EmployeeRegisterService.getInstance();
	private EmployeePhotoService photoService = EmployeePhotoService.getInstance();
	private EmployeeSkillRecordService skillService = EmployeeSkillRecordService.getInstance();
	private EmployeeGuaranteeService guaranteeService = EmployeeGuaranteeService.getInstance();

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
		String empIdStr = req.getParameter("empId");

		// 사원번호 파라미터가 아예 안 넘어왔을 경우 (신규 사원 등록 중 넘어오려 할 때)
		if (empIdStr == null || empIdStr.trim().isEmpty()) {
			req.getSession().setAttribute("message", "사원 기본 정보가 없습니다. 1단계를 먼저 완료해 주세요.");
			res.sendRedirect(req.getContextPath() + "/settings/register1.do");
			return null;
		}

		int empId = Integer.parseInt(empIdStr);

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
		req.setAttribute("licenses", licenses);
		req.setAttribute("languages", languages);
		req.setAttribute("trainings", trainings);
		req.setAttribute("rewardPunishes", rewardPunishes);
		req.setAttribute("appointments", appointments);
		req.setAttribute("recommender", recommender);
		req.setAttribute("suretyInsurance", suretyInsurance);
		req.setAttribute("guarantor", guarantor);

		return "/WEB-INF/view/settings/employee-register-2.jsp";
	}

	// [POST] 액션 파라미터에 따른 비즈니스 로직 분기 처리
	private String processAction(HttpServletRequest req, HttpServletResponse res) throws Exception {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		String empIdStr = req.getParameter("empId");

		// 신규 등록 방지를 위한 사원번호 임시 기본값 할당
		int empId = (empIdStr != null && !empIdStr.isEmpty()) ? Integer.parseInt(empIdStr) : 100;

		try {
			if ("save".equals(action)) {
				// [1] 사원 기본정보 중 퇴직 관련 속성 업데이트
				Employee employee = registerService.getEmployeeBasicProfile(empId);

				// DB에 해당 사원이 없으면 저장을 중단하고 돌려보냄
				if (employee == null) {
					req.getSession().setAttribute("message", "사원 기본 정보가 존재하지 않습니다. 1단계를 먼저 완료해 주세요.");
					res.sendRedirect(req.getContextPath() + "/settings/register1.do"); // 1단계 화면으로 강제 이동
					return null;
				}

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				employee.setRetireType(req.getParameter("retireType"));
				employee.setRetireReason(req.getParameter("retireReason"));
				employee.setAfterRetireContact(req.getParameter("afterRetireContact"));

				String retireDateStr = req.getParameter("retireDate");
				if (retireDateStr != null && !retireDateStr.trim().isEmpty()) {
					// 1. 퇴직일자가 입력되었으므로 날짜를 세팅하고,
					employee.setRetireDate(sdf.parse(retireDateStr));
					// 2. 사원의 상태도 '퇴직'으로 변경해 줍니다!
					employee.setStatus("퇴직");
				} else {
					// (선택 사항) 만약 실수로 날짜를 잘못 넣어서 다시 지웠을 경우를 대비해 '재직'으로 롤백
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
				// 프로필 사진 등록 로직
				Part photoPart = req.getPart("photoFile");
				photoService.uploadPhoto(empId, "/upload/emp/photo_" + empId + ".jpg");
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
					for (String id : deleteIds)
						idList.add(Integer.parseInt(id));
					skillService.deleteSelectedItems("license", idList);
					req.getSession().setAttribute("message", "선택한 자격/면허가 삭제되었습니다.");
				}

			} else if ("deleteLanguages".equals(action)) {
				// 어학능력 선택 삭제 로직
				String[] deleteIds = req.getParameterValues("languageDeleteIds");
				if (deleteIds != null) {
					List<Integer> idList = new ArrayList<>();
					for (String id : deleteIds)
						idList.add(Integer.parseInt(id));
					skillService.deleteSelectedItems("language", idList);
					req.getSession().setAttribute("message", "선택한 어학능력이 삭제되었습니다.");
				}

			} else if ("deleteTrainings".equals(action)) {
				// 교육 및 훈련 선택 삭제 로직
				String[] deleteIds = req.getParameterValues("trainingDeleteIds");
				if (deleteIds != null) {
					List<Integer> idList = new ArrayList<>();
					for (String id : deleteIds)
						idList.add(Integer.parseInt(id));
					skillService.deleteSelectedItems("training", idList);
					req.getSession().setAttribute("message", "선택한 교육/훈련이 삭제되었습니다.");
				}

			} else if ("deleteRewardPunishes".equals(action)) {
				// 상벌 내역 선택 삭제 로직
				String[] deleteIds = req.getParameterValues("rewardDeleteIds");
				if (deleteIds != null) {
					List<Integer> idList = new ArrayList<>();
					for (String id : deleteIds)
						idList.add(Integer.parseInt(id));
					skillService.deleteSelectedItems("reward", idList);
					req.getSession().setAttribute("message", "선택한 상벌 내역이 삭제되었습니다.");
				}

			} else if ("deleteAppointments".equals(action)) {
				// 인사발령 내역 선택 삭제 로직
				String[] deleteIds = req.getParameterValues("appointmentDeleteIds");
				if (deleteIds != null) {
					List<Integer> idList = new ArrayList<>();
					for (String id : deleteIds)
						idList.add(Integer.parseInt(id));
					skillService.deleteSelectedItems("appointment", idList);
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

	// [Helper] 자격 및 면허 폼 데이터 파싱
	private List<EmployeeLicense> parseLicenses(HttpServletRequest req, int empId) throws Exception {
		List<EmployeeLicense> list = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// 화면 0~2 루프 기준 반복 파싱
		for (int i = 0; i < 3; i++) {
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
		for (int i = 0; i < 1; i++) {
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
		for (int i = 0; i < 2; i++) {
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
		for (int i = 0; i < 2; i++) {
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
		for (int i = 0; i < 2; i++) {
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