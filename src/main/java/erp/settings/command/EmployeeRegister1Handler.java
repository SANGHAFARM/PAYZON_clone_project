package erp.settings.command;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.employees.model.Employee;
import erp.employees.model.EmployeeCareer;
import erp.employees.model.EmployeeDependent;
import erp.employees.model.EmployeeEducation;
import erp.employees.model.EmployeeInsuranceHistory;
import erp.employees.service.EmployeeHistoryService;
import erp.employees.service.EmployeePhotoService;
import erp.employees.service.EmployeeRegisterService;
import erp.settings.model.Department;
import erp.settings.model.JobPosition;
import erp.settings.service.DepartmentPositionService;
import mvc.command.CommandHandler;

public class EmployeeRegister1Handler implements CommandHandler {

	// 3가지 핵심 비즈니스 서비스를 싱글톤으로 불러옵니다.
	private EmployeeRegisterService registerService = EmployeeRegisterService.getInstance();
	private EmployeeHistoryService historyService = EmployeeHistoryService.getInstance();
	private EmployeePhotoService photoService = EmployeePhotoService.getInstance();
	private DepartmentPositionService deptPosService = DepartmentPositionService.getInstance();

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

	// [GET] 화면 렌더링 (사원 정보 조회)
	private String processForm(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String empIdStr = req.getParameter("empId");
		
		List<Department> departmentList = deptPosService.getDepartmentOptions();
		List<JobPosition> positionList = deptPosService.getJobPositionOptions();
		req.setAttribute("departmentList", departmentList);
		req.setAttribute("positionList", positionList);

		int empId = parseEmployeeId(empIdStr);
		if (empId > 0) {

			Employee employee = registerService.getEmployeeBasicProfile(empId);
			if (employee == null) {
				req.getSession().setAttribute("message", "존재하지 않는 사원입니다.");
				res.sendRedirect(req.getContextPath() + "/settings/register1.do");
				return null;
			}
			List<EmployeeDependent> dependents = historyService.getDependents(empId);
			List<EmployeeEducation> educations = historyService.getEducations(empId);
			List<EmployeeCareer> careers = historyService.getCareers(empId);
			List<EmployeeInsuranceHistory> insuranceRows = historyService.getInsuranceHistories(empId);
			
			// 서버에서 경력 기간(년/월) 직접 계산 로직
			if (careers != null) {
				for (EmployeeCareer c : careers) {
					if (c.getJoinDate() != null && c.getQuitDate() != null) {
						// 기존 Date 타입을 계산하기 쉬운 LocalDate로 변환
						LocalDate start = c.getJoinDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						LocalDate end = c.getQuitDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

						// 퇴사일이 입사일보다 이후인 정상적인 경우에만 계산
						if (!end.isBefore(start)) {
							Period period = Period.between(start, end);
							int totalMonths = (period.getYears() * 12) + period.getMonths();

							// 요구사항 반영: 1개월 미만일 경우 최소 1개월로 처리
							if (totalMonths < 1) {
								totalMonths = 1;
							}

							// 계산된 총 개월 수를 다시 년과 월로 분리하여 객체에 세팅
							c.setYears(totalMonths / 12);
							c.setMonths(totalMonths % 12);
						}
					}
				}
			}

			// JSP(뷰)로 전달하기 위해 request 영역에 저장
			req.setAttribute("employee", employee);
			req.setAttribute("dependents", dependents);
			req.setAttribute("educations", educations);
			req.setAttribute("careers", careers);
			req.setAttribute("insuranceRows", insuranceRows);
		} else {
			Integer reservedId = (Integer) req.getSession().getAttribute("reservedEmployeeId");
			if (reservedId == null || reservedId <= 0) {
				reservedId = registerService.reserveEmployeeId();
				req.getSession().setAttribute("reservedEmployeeId", reservedId);
			}
			req.setAttribute("anticipatedEmpNo", registerService.getEmployeeNumberPreview(reservedId));
		}
		req.setAttribute("draftPhotoPreset", req.getSession().getAttribute("draftPhotoPreset"));

		bindRowCounts(req);

		return "/WEB-INF/view/settings/employee-register-1.jsp";
	}

	// [POST] 폼 전송 시 'action' 파라미터에 따른 동적 로직 분기
	private String processAction(HttpServletRequest req, HttpServletResponse res) throws Exception {
		req.setCharacterEncoding("UTF-8");

		// JSP의 <button name="action" value="..."> 에서 넘어온 값
		String action = req.getParameter("action");
		String empIdStr = req.getParameter("empId");

		// 신규 등록은 0으로 구분하고 실제 PK는 서비스에서 시퀀스로 발급합니다.
		int empId = parseEmployeeId(empIdStr);

		try {
			if ("clearPhotoPreview".equals(action)) {
				req.getSession().removeAttribute("draftPhotoPreset");
				res.sendRedirect(req.getContextPath() + "/settings/register1.do"
						+ (empId > 0 ? "?empId=" + empId : ""));
				return null;
			}

			if ("previewPhoto".equals(action)) {
				String photoCandidate = req.getParameter("photoCandidate");
				getPresetPhotoPath(photoCandidate);
				req.getSession().setAttribute("draftPhotoPreset", photoCandidate);
				res.sendRedirect(req.getContextPath() + "/settings/register1.do"
						+ (empId > 0 ? "?empId=" + empId : ""));
				return null;
			}

			if (action != null && action.startsWith("add")) {
				increaseRowCount(req, action);
				res.sendRedirect(req.getContextPath() + "/settings/register1.do"
						+ (empId > 0 ? "?empId=" + empId : ""));
				return null;
			}

			if ("save".equals(action)) {
				// 1. 통합 저장 로직
				validateRequiredFields(req);

				// 1-1. 텍스트 폼에서 메인 사원(Employee) 객체 추출 및 저장
				Employee employee = parseEmployeeBasicInfo(req, empId);
				if (empId == 0) {
					Integer reservedId = (Integer) req.getSession().getAttribute("reservedEmployeeId");
					if (reservedId != null && reservedId > 0) {
						employee.setEmployeeId(reservedId);
					}
				}
				if (empId > 0) {
					// 사원정보 1 화면에 없는 퇴직 항목은 기존 값을 그대로 유지한다.
					Employee existingEmployee = registerService.getEmployeeBasicProfile(empId);
					if (existingEmployee != null) {
						employee.setStatus(existingEmployee.getStatus());
						employee.setRetireType(existingEmployee.getRetireType());
						employee.setRetireDate(existingEmployee.getRetireDate());
						employee.setRetireReason(existingEmployee.getRetireReason());
						employee.setAfterRetireContact(existingEmployee.getAfterRetireContact());
					}
				}
				empId = registerService.saveEmployeeBasicInfo(employee);

				// 1-2. 1:N 이력 리스트 추출 및 일괄 갱신 (모든 함수에 empId 전달)
				List<EmployeeDependent> deps = parseDependents(req, empId);
				List<EmployeeEducation> edus = parseEducations(req, empId);
				List<EmployeeCareer> cars = parseCareers(req, empId);
				List<EmployeeInsuranceHistory> insurances = parseInsuranceHistories(req, empId);

				// 서비스 호출 시 4대보험 이력(insurances)도 함께 넘겨줍니다.
				historyService.saveAllHistories(empId, deps, edus, cars, insurances);
				String photoPreset = req.getParameter("photoPreset");
				if (isBlank(photoPreset)) {
					photoPreset = (String) req.getSession().getAttribute("draftPhotoPreset");
				}
				if (!isBlank(photoPreset)) {
					photoService.uploadPhoto(empId, getPresetPhotoPath(photoPreset));
				}
				req.getSession().removeAttribute("reservedEmployeeId");
				req.getSession().removeAttribute("draftPhotoPreset");

				req.getSession().setAttribute("message", "사원 정보가 성공적으로 저장되었습니다.");

			} else if ("savePhoto".equals(action)) {
				requireSavedEmployee(empId);
				String savedPath = getPresetPhotoPath(req.getParameter("photoPreset"));
				photoService.uploadPhoto(empId, savedPath);
				req.getSession().setAttribute("message", "사진이 등록되었습니다.");

			} else if ("deletePhoto".equals(action)) {
				requireSavedEmployee(empId);
				photoService.deletePhoto(empId);
				req.getSession().removeAttribute("draftPhotoPreset");
				req.getSession().setAttribute("message", "사진이 삭제되었습니다.");

			} else if ("deleteDependents".equals(action)) {
				// 4. 하위 이력 선택 삭제 로직 (예: 부양가족)
				String[] deleteIds = req.getParameterValues("dependentDeleteIds");
				if (deleteIds != null) {
					List<Integer> idList = new ArrayList<>();
					for (String id : deleteIds) {
						if (!isBlank(id)) idList.add(Integer.parseInt(id));
					}
					if (!idList.isEmpty()) historyService.deleteSelectedItems("dependent", idList);
					decreaseRowCount(req, "dependentRowCount", 4, deleteIds.length);
					req.getSession().setAttribute("message", "선택한 부양가족이 삭제되었습니다.");
				}

				// 5. 학력 선택 삭제 로직
			} else if ("deleteEducations".equals(action)) {
				String[] deleteIds = req.getParameterValues("educationDeleteIds");
				if (deleteIds != null) {
					List<Integer> idList = new ArrayList<>();
					for (String id : deleteIds) {
						if (!isBlank(id)) idList.add(Integer.parseInt(id));
					}
					if (!idList.isEmpty()) historyService.deleteSelectedItems("education", idList);
					decreaseRowCount(req, "educationRowCount", 3, deleteIds.length);
					req.getSession().setAttribute("message", "선택한 학력이 삭제되었습니다.");
				}

				// 6. 경력 선택 삭제 로직
			} else if ("deleteCareers".equals(action)) {
				String[] deleteIds = req.getParameterValues("careerDeleteIds");
				if (deleteIds != null) {
					List<Integer> idList = new ArrayList<>();
					for (String id : deleteIds) {
						if (!isBlank(id)) idList.add(Integer.parseInt(id));
					}
					if (!idList.isEmpty()) historyService.deleteSelectedItems("career", idList);
					decreaseRowCount(req, "careerRowCount", 3, deleteIds.length);
					req.getSession().setAttribute("message", "선택한 경력이 삭제되었습니다.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("message", "처리 중 오류가 발생했습니다: " + e.getMessage());
		}

		// 작업 완료 후 데이터 중복 전송(F5)을 막기 위해 현재 사원 번호를 달고 GET 화면으로 리다이렉트
		res.sendRedirect(req.getContextPath() + "/settings/register1.do?empId=" + empId);
		return null; // 포워딩 방지
	}

	private int parseEmployeeId(String value) {
		try {
			int employeeId = Integer.parseInt(value);
			return employeeId > 0 ? employeeId : 0;
		} catch (Exception e) {
			return 0;
		}
	}

	private void requireSavedEmployee(int employeeId) {
		if (employeeId <= 0 || registerService.getEmployeeBasicProfile(employeeId) == null) {
			throw new IllegalArgumentException("사원정보 1을 먼저 저장해주세요.");
		}
	}

	private String getPresetPhotoPath(String preset) {
		if (preset != null && preset.matches("0[1-5]")) {
			return "/images/settings/employee-presets/employee-" + preset + ".png";
		}
		throw new IllegalArgumentException("사용할 기본 사원사진을 선택해주세요.");
	}

	private void validateRequiredFields(HttpServletRequest req) {
		if (isBlank(req.getParameter("empType")) || isBlank(req.getParameter("empNameKr"))
				|| isBlank(req.getParameter("joinDate")) || isBlank(req.getParameter("foreignYn"))
				|| isBlank(req.getParameter("basicPay")) || isBlank(req.getParameter("incomeType"))) {
			throw new IllegalArgumentException("필수 입력사항을 모두 입력해주세요.");
		}
		boolean separate = req.getParameter("durunuriSeparateYn") != null;
		if ((!separate && isBlank(req.getParameter("durunuriRate")))
				|| (separate && (isBlank(req.getParameter("durunuriNpRate"))
						|| isBlank(req.getParameter("durunuriEiRate"))))) {
			throw new IllegalArgumentException("두루누리 사회보험 지원 설정을 선택해주세요.");
		}
	}

	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}

	private void bindRowCounts(HttpServletRequest req) {
		req.setAttribute("dependentRowCount", getRowCount(req, "dependentRowCount", 4));
		req.setAttribute("educationRowCount", getRowCount(req, "educationRowCount", 3));
		req.setAttribute("careerRowCount", getRowCount(req, "careerRowCount", 3));
	}

	private int getRowCount(HttpServletRequest req, String key, int defaultCount) {
		Integer count = (Integer) req.getSession().getAttribute(key);
		return count == null ? defaultCount : Math.max(defaultCount, Math.min(count, 20));
	}

	private void increaseRowCount(HttpServletRequest req, String action) {
		String key;
		int defaultCount;
		if ("addDependent".equals(action)) {
			key = "dependentRowCount";
			defaultCount = 4;
		} else if ("addEducation".equals(action)) {
			key = "educationRowCount";
			defaultCount = 3;
		} else if ("addCareer".equals(action)) {
			key = "careerRowCount";
			defaultCount = 3;
		} else {
			return;
		}
		req.getSession().setAttribute(key, Math.min(getRowCount(req, key, defaultCount) + 1, 20));
	}

	private void decreaseRowCount(HttpServletRequest req, String key, int defaultCount, int amount) {
		int count = getRowCount(req, key, defaultCount);
		req.getSession().setAttribute(key, Math.max(defaultCount, count - Math.max(amount, 0)));
	}

	/**
	 * [Helper] 폼에서 넘어온 사원 메인 정보를 v5 스키마(EMPLOYEE)에 맞게 파싱
	 */
	private Employee parseEmployeeBasicInfo(HttpServletRequest req, int empId) throws Exception {
		Employee emp = new Employee();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// [1. 사원 기본정보]
		emp.setEmployeeId(empId);
		emp.setEmpNo(req.getParameter("empNo"));
		emp.setEmpType(req.getParameter("empType"));
		emp.setEmpNameKr(req.getParameter("empNameKr"));
		emp.setEmpNameEn(req.getParameter("empNameEn"));
		emp.setForeignYn(req.getParameter("foreignYn"));
		emp.setJuminNo(req.getParameter("juminNo")); // v5 JUMIN_NO 매핑

		String joinDate = req.getParameter("joinDate");
		if (joinDate != null && !joinDate.trim().isEmpty()) {
			emp.setJoinDate(sdf.parse(joinDate));
		}

		// 부서 및 직위 (v5 스키마 기준 NUMBER 타입)
		String deptIdStr = req.getParameter("deptId");
		if (deptIdStr != null && !deptIdStr.trim().isEmpty()) {
			emp.setDepartmentId(deptPosService.requireDepartmentId(deptIdStr));
		}
		String posIdStr = req.getParameter("posId");
		if (posIdStr != null && !posIdStr.trim().isEmpty()) {
			emp.setJobPositionId(deptPosService.requireJobPositionId(posIdStr));
		}

		// [2. 연락처 및 주소]
		emp.setZipCode(req.getParameter("zipCode"));
		emp.setAddress(req.getParameter("address"));
		emp.setTelNo(req.getParameter("telNo"));
		emp.setMobileNo(req.getParameter("mobileNo"));
		emp.setEmail(req.getParameter("email"));
		emp.setSnsAddress(req.getParameter("snsAddress"));
		emp.setMemo(req.getParameter("memo"));

		// [3. 급여 및 소득세 설정]
		String basicPayStr = req.getParameter("basicPay");
		emp.setBasicPay(basicPayStr != null && !basicPayStr.trim().isEmpty() ? Long.parseLong(basicPayStr) : 0);

		String incomeType = req.getParameter("incomeType");
		emp.setIncomeType(incomeType != null ? incomeType : "근로소득자"); // 기본값 '근로소득자'

		String incomeTaxRateStr = req.getParameter("incomeTaxRate");
		emp.setIncomeTaxRate(
				incomeTaxRateStr != null && !incomeTaxRateStr.trim().isEmpty() ? Integer.parseInt(incomeTaxRateStr)
						: 100);

		// 체크박스는 체크 해제 시 null이 넘어오므로 삼항연산자 처리
		emp.setYouthTaxReduceYn(req.getParameter("youthTaxReduceYn") != null ? "Y" : "N");

		String youthTaxRateStr = req.getParameter("youthTaxRate");
		emp.setYouthTaxRate(
				youthTaxRateStr != null && !youthTaxRateStr.trim().isEmpty() ? Integer.parseInt(youthTaxRateStr) : 0);

		// [4. 4대보험 설정]
		emp.setNpYn(req.getParameter("npYn") != null ? "Y" : "N");
		emp.setHiYn(req.getParameter("hiYn") != null ? "Y" : "N");
		emp.setLtciYn(req.getParameter("ltciYn") != null ? "Y" : "N");
		emp.setEiYn(req.getParameter("eiYn") != null ? "Y" : "N");

		String hiReduceRateStr = req.getParameter("hiReduceRate");
		emp.setHiReduceRate(
				hiReduceRateStr != null && !hiReduceRateStr.trim().isEmpty() ? Integer.parseInt(hiReduceRateStr) : 0);

		String ltciReduceRateStr = req.getParameter("ltciReduceRate");
		emp.setLtciReduceRate(
				ltciReduceRateStr != null && !ltciReduceRateStr.trim().isEmpty() ? Integer.parseInt(ltciReduceRateStr)
						: 0);

		// [5. 두루누리 사회보험 지원]
		// 통합 설정은 같은 지원율을 적용하고, 분리 설정은 국민연금과 고용보험을 각각 저장합니다.
		boolean separate = req.getParameter("durunuriSeparateYn") != null;
		emp.setDurunuriSeparateYn(separate ? "Y" : "N");
		if (separate) {
			emp.setDurunuriNpRate(parseRate(req.getParameter("durunuriNpRate")));
			emp.setDurunuriEiRate(parseRate(req.getParameter("durunuriEiRate")));
		} else {
			int rate = parseRate(req.getParameter("durunuriRate"));
			emp.setDurunuriNpRate(rate);
			emp.setDurunuriEiRate(rate);
		}

		// [6. 보험료 계산 기준 금액]
		String npBase = req.getParameter("npMonthlyBase");
		emp.setNpMonthlyBase(npBase != null && !npBase.trim().isEmpty() ? Long.parseLong(npBase) : 0);

		String hiBase = req.getParameter("hiMonthlyBase");
		emp.setHiMonthlyBase(hiBase != null && !hiBase.trim().isEmpty() ? Long.parseLong(hiBase) : 0);

		String eiBase = req.getParameter("eiMonthlyBase");
		emp.setEiMonthlyBase(eiBase != null && !eiBase.trim().isEmpty() ? Long.parseLong(eiBase) : 0);

		// [7. 급여계좌]
		emp.setBankName(req.getParameter("bankName"));
		emp.setAccountNo(req.getParameter("accountNo"));

		// [8. 병역정보]
		emp.setDischargeType(req.getParameter("dischargeType"));
		emp.setMilBranch(req.getParameter("milBranch"));

		String milStart = req.getParameter("milServiceStart");
		if (milStart != null && !milStart.trim().isEmpty()) {
			emp.setMilServiceStart(sdf.parse(milStart));
		}

		String milEnd = req.getParameter("milServiceEnd");
		if (milEnd != null && !milEnd.trim().isEmpty()) {
			emp.setMilServiceEnd(sdf.parse(milEnd));
		}

		emp.setMilRank(req.getParameter("milRank"));
		emp.setMilSpecialty(req.getParameter("milSpecialty"));
		emp.setMilUnfinishedReason(req.getParameter("milUnfinishedReason"));

		// [9. 퇴직상태 기본값 세팅]
		// 사원등록 1페이지에서는 기본적으로 '재직' 상태입니다. (실제 퇴직처리는 2페이지 또는 퇴직관리 메뉴에서 수행)
		emp.setStatus("재직");

		return emp;
	}

	private int parseRate(String value) {
		if ("80".equals(value) || "90".equals(value)) {
			return Integer.parseInt(value);
		}
		return 0;
	}

	// [Helper] 폼에서 배열 형태로 넘어온 부양가족 리스트를 DTO에 맞게 파싱하는 메서드
	private List<EmployeeDependent> parseDependents(HttpServletRequest req, int empId) {
		List<EmployeeDependent> list = new ArrayList<>();

		for (int i = 0; i < parseRowCount(req, "dependentRowCount", 4); i++) {
			String relation = req.getParameter("dependents[" + i + "].relation");
			String depName = req.getParameter("dependents[" + i + "].depName");

			if (relation != null && !relation.trim().isEmpty() && depName != null && !depName.trim().isEmpty()) {
				EmployeeDependent dep = new EmployeeDependent();
				dep.setEmployeeId(empId); // 외래키 세팅 추가

				dep.setRelation(relation);
				dep.setDepName(depName);

				String nationality = req.getParameter("dependents[" + i + "].nationality");
				dep.setNationalType(nationality != null && !nationality.isEmpty() ? nationality : "내국인");

				dep.setJuminNo(req.getParameter("dependents[" + i + "].juminNo"));

				dep.setDisabledYn(req.getParameter("dependents[" + i + "].disabledYn") != null ? "Y" : "N");
				dep.setBasicDeductYn(req.getParameter("dependents[" + i + "].deductionYn") != null ? "Y" : "N");
				dep.setHealthInsYn(req.getParameter("dependents[" + i + "].healthYn") != null ? "Y" : "N");
				dep.setCohabitYn(req.getParameter("dependents[" + i + "].cohabitYn") != null ? "Y" : "N");
				dep.setIncomeTaxYn(req.getParameter("dependents[" + i + "].incomeTaxYn") != null ? "Y" : "N");
				dep.setChildUnder20Yn(req.getParameter("dependents[" + i + "].childYn") != null ? "Y" : "N");

				list.add(dep);
			}
		}
		return list;
	}

	// [Helper] 폼에서 배열 형태로 넘어온 4대보험 취득/상실 이력 파싱
	private List<EmployeeInsuranceHistory> parseInsuranceHistories(HttpServletRequest req, int empId) throws Exception {
		List<EmployeeInsuranceHistory> list = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// JSP 화면상 국민연금, 건강보험, 고용보험, 산재보험 순서대로 input 태그가 존재함
		String[] types = { "국민연금", "건강보험", "고용보험", "산재보험" };

		// name="insuranceNo" 등의 속성으로 여러 개가 넘어오므로 getParameterValues 사용
		String[] nos = req.getParameterValues("insuranceNo");
		String[] startDates = req.getParameterValues("insuranceStartDate");
		String[] endDates = req.getParameterValues("insuranceEndDate");

		if (nos != null) {
			for (int i = 0; i < types.length; i++) {
				// 기호번호나 날짜 중 하나라도 입력된 데이터가 있다면 저장
				boolean hasData = (nos.length > i && nos[i] != null && !nos[i].trim().isEmpty())
						|| (startDates.length > i && startDates[i] != null && !startDates[i].trim().isEmpty())
						|| (endDates.length > i && endDates[i] != null && !endDates[i].trim().isEmpty());

				if (hasData) {
					EmployeeInsuranceHistory ins = new EmployeeInsuranceHistory();
					ins.setEmployeeId(empId); // 외래키 세팅
					ins.setInsuranceType(types[i]); // 구분 (v5 스키마 INSURANCE_TYPE)

					if (nos.length > i) {
						ins.setSymbolNo(nos[i]); // 기호번호 (v5 스키마 SYMBOL_NO)
					}

					if (startDates.length > i && startDates[i] != null && !startDates[i].trim().isEmpty()) {
						ins.setAcquireDate(sdf.parse(startDates[i])); // 취득일 (v5 스키마 ACQUIRE_DATE)
					}

					if (endDates.length > i && endDates[i] != null && !endDates[i].trim().isEmpty()) {
						ins.setLossDate(sdf.parse(endDates[i])); // 상실일 (v5 스키마 LOSS_DATE)
					}

					list.add(ins);
				}
			}
		}
		return list;
	}

	// 학력 리스트 파싱
	private List<EmployeeEducation> parseEducations(HttpServletRequest req, int empId) {
		List<EmployeeEducation> list = new ArrayList<>();

		for (int i = 0; i < parseRowCount(req, "educationRowCount", 3); i++) {
			String schoolName = req.getParameter("educations[" + i + "].schoolName");

			// 학교명이 비어있지 않은 실제 데이터만 리스트에 추가
			if (schoolName != null && !schoolName.trim().isEmpty()) {
				EmployeeEducation edu = new EmployeeEducation();
				edu.setEmployeeId(empId);

				// schoolType -> EDU_TYPE
				edu.setEduType(req.getParameter("educations[" + i + "].schoolType"));

				// JSP의 "YYYY-MM" 형태를 DB 규격인 "YYYYMM" (VARCHAR2(6)) 형태로 변환
				String adminYm = req.getParameter("educations[" + i + "].admissionYm");
				if (adminYm != null && !adminYm.isEmpty()) {
					edu.setAdmissionYm(adminYm.replace("-", ""));
				}

				String gradYm = req.getParameter("educations[" + i + "].graduationYm");
				if (gradYm != null && !gradYm.isEmpty()) {
					edu.setGradYm(gradYm.replace("-", ""));
				}

				edu.setSchoolName(schoolName);

				// major -> MAJOR_NAME
				edu.setMajorName(req.getParameter("educations[" + i + "].major"));

				// completionStatus -> COMPLETE_TYPE
				edu.setCompleteType(req.getParameter("educations[" + i + "].completionStatus"));

				list.add(edu);
			}
		}
		return list;
	}

	// 경력 리스트 파싱
	private List<EmployeeCareer> parseCareers(HttpServletRequest req, int empId) throws Exception {
		List<EmployeeCareer> list = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		for (int i = 0; i < parseRowCount(req, "careerRowCount", 3); i++) {
			String companyName = req.getParameter("careers[" + i + "].companyName");

			// 회사명이 비어있지 않은 실제 데이터만 리스트에 추가
			if (companyName != null && !companyName.trim().isEmpty()) {
				EmployeeCareer career = new EmployeeCareer();
				career.setEmployeeId(empId);
				career.setCompanyName(companyName);

				// startDate -> JOIN_DATE
				String startDate = req.getParameter("careers[" + i + "].startDate");
				if (startDate != null && !startDate.trim().isEmpty()) {
					career.setJoinDate(sdf.parse(startDate));
				}

				// endDate -> QUIT_DATE
				String endDate = req.getParameter("careers[" + i + "].endDate");
				if (endDate != null && !endDate.trim().isEmpty()) {
					career.setQuitDate(sdf.parse(endDate));
				}

				// lastPosition -> FINAL_POSITION
				career.setFinalPosition(req.getParameter("careers[" + i + "].lastPosition"));

				// duty -> DUTY
				career.setDuty(req.getParameter("careers[" + i + "].duty"));

				// retireReason -> QUIT_REASON
				career.setQuitReason(req.getParameter("careers[" + i + "].retireReason"));

				list.add(career);
			}
		}
		return list;
	}

	private int parseRowCount(HttpServletRequest req, String name, int defaultCount) {
		try {
			return Math.max(defaultCount, Math.min(Integer.parseInt(req.getParameter(name)), 20));
		} catch (Exception e) {
			return defaultCount;
		}
	}
}
