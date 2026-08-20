<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>기본환경설정 &gt; 사원등록 2</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/settings/employee-register.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf"%>
	<main class="page-content">
		<div class="employee-page">
			<c:set var="defaultDepartments"
				value="${fn:split('사장실,개발팀,콘텐츠팀,업무지원팀,디자인팀,관리팀,기획전략팀', ',')}" />
			<c:set var="defaultPositions"
				value="${fn:split('이사,차장,사장,부장,과장,대리,주임,사원,실장', ',')}" />
			<c:set var="employmentTypes"
				value="${fn:split('정규직,계약직,임시직,파견직,위촉직,일용직', ',')}" />
			<c:set var="trainingTypes"
				value="${fn:split('사내직무,사외직무,계층교육,어학교육,기타', ',')}" />
			<c:set var="rewardTypes"
				value="${fn:split('포상,표창,시상,면직,정직,감봉,견책,주의,경고,조치불가,해고', ',')}" />
			<c:set var="appointmentTypes"
				value="${fn:split('채용,전보,승진,승격,승호,파견', ',')}" />
			<c:set var="retireTypes"
				value="${fn:split('정년퇴직,정리해고,자발적 퇴직,임원퇴직,중간정산,기타', ',')}" />

			<header class="page-heading">
				<div>
					<p>기본환경설정</p>
					<h1>사원등록</h1>
				</div>
				<p class="page-heading__notice">
					<strong>*</strong> 표시는 필수입력사항입니다.
				</p>
			</header>
			<c:if test="${not empty message}">
				<p class="form-message">
					<c:out value="${message}" />
				</p>
				<c:remove var="message" scope="session" />
			</c:if>
			<form
				action="${pageContext.request.contextPath}/settings/register2.do"
				method="post" enctype="multipart/form-data">
				<input type="hidden" name="empId"
					value="<c:out value='${employee.employeeId}' />">
				<div class="employee-layout">
					<aside class="employee-summary">
						<div class="photo-box">
							<c:choose>
								<c:when test="${not empty employee.photoPath}">
									<img
										src="${pageContext.request.contextPath}<c:out value='${employee.photoPath}' />"
										alt="사원 사진">
								</c:when>
								<c:otherwise>
									<span>사진 대기</span>
								</c:otherwise>
							</c:choose>
						</div>
						<div class="summary-actions">
							<a href="#photo-upload-modal">등록</a>
							<button name="action" value="deletePhoto">삭제</button>
						</div>
						<dl>
							<div>
								<dt>사원번호</dt>
								<dd>
									<c:out value="${employee.empNo}" />
								</dd>
							</div>
							<div>
								<dt>성명</dt>
								<dd>
									<c:out value="${employee.empNameKr}" />
								</dd>
							</div>
							<div>
								<dt>부서</dt>
								<dd>
									<%-- 전체 부서 목록을 돌면서 현재 사원의 부서ID와 일치하는 부서명을 출력 --%>
									<c:forEach var="dept" items="${departmentList}">
										<c:if test="${dept.departmentId eq employee.departmentId}">
											<c:out value="${dept.departmentName}" />
										</c:if>
									</c:forEach>
								</dd>
							</div>
							<div>
								<dt>직위</dt>
								<dd>
									<%-- 전체 직위 목록을 돌면서 현재 사원의 직위ID와 일치하는 직위명을 출력 --%>
									<c:forEach var="pos" items="${positionList}">
										<c:if test="${pos.jobPositionId eq employee.jobPositionId}">
											<c:out value="${pos.jobPositionName}" />
										</c:if>
									</c:forEach>
								</dd>
							</div>
							<div>
								<dt>입사일</dt>
								<dd>
									<fmt:formatDate value="${employee.joinDate}" pattern="yyyy/MM/dd" />
								</dd>
							</div>
						</dl>
						<nav class="section-shortcuts" aria-label="사원정보 바로가기">
							<section>
								<p class="shortcut-title">
									<span>사원정보</span><em>01</em>
								</p>
								<div class="section-links">
									<a
										href="${pageContext.request.contextPath}/settings/register1.do?empId=${employee.employeeId}#salary-insurance">급여/4대보험</a><a
										href="${pageContext.request.contextPath}/settings/register1.do?empId=${employee.employeeId}#dependents">부양가족</a><a
										href="${pageContext.request.contextPath}/settings/register1.do?empId=${employee.employeeId}#education">학력</a><a
										href="${pageContext.request.contextPath}/settings/register1.do?empId=${employee.employeeId}#career">경력</a><a
										href="${pageContext.request.contextPath}/settings/register1.do?empId=${employee.employeeId}#military">병역</a>
								</div>
							</section>
							<section>
								<p class="shortcut-title">
									<span>사원정보</span><em>02</em>
								</p>
								<div class="section-links">
									<a href="#license">자격/면허</a><a href="#training">교육/훈련</a><a
										href="#reward-punish">상벌</a><a href="#appointment">발령</a><a
										href="#recommendation">추천/신원보증</a><a href="#retirement">퇴직</a>
								</div>
							</section>
						</nav>
					</aside>

					<div class="employee-form">
						<section class="form-card">
							<h2>기본정보</h2>
							<div class="form-grid">
								<label class="field"><span>사원번호</span><input
									name="empNo" value="<c:out value='${employee.empNo}' />"
									maxlength="50"></label><label class="field"><span><b>*</b>
										고용형태</span><select name="empType" required><option value="">선택해주세요.</option>
										<c:forEach var="type" items="${employmentTypes}">
											<option value="${type}"
												${type eq employee.empType ? 'selected' : ''}>${type}</option>
										</c:forEach></select></label> <label class="field"><span><b>*</b> 성명(한글)</span><input
									name="empNameKr"
									value="<c:out value='${employee.empNameKr}' />" required
									maxlength="50"></label><label class="field"><span>성명(영문)</span><input
									name="empNameEn"
									value="<c:out value='${employee.empNameEn}' />" maxlength="100"></label>
								<label class="field"><span><b>*</b> 입사일</span><input
									type="date" name="joinDate"
									value="<fmt:formatDate value='${employee.joinDate}' pattern='yyyy-MM-dd' />"
									required></label><label class="field"><span>퇴사일</span><input
									type="date" 
									value="<fmt:formatDate value='${employee.retireDate}' pattern='yyyy-MM-dd' />"
									readonly></label> <label class="field"><span>부서</span><select
									name="deptId"><option value="">선택해주세요.</option>
										<c:choose>
											<c:when test="${not empty departmentList}">
												<c:forEach var="dept" items="${departmentList}">
													<option value="${dept.departmentId}"
														${dept.departmentId eq employee.departmentId ? 'selected' : ''}>${dept.departmentName}</option>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<c:forEach var="dept" items="${defaultDepartments}">
													<option>${dept}</option>
												</c:forEach>
											</c:otherwise>
										</c:choose></select></label><label class="field"><span>직위</span><select
									name="posId"><option value="">선택해주세요.</option>
										<c:choose>
											<c:when test="${not empty positionList}">
												<c:forEach var="pos" items="${positionList}">
													<option value="${pos.jobPositionId}"
														${pos.jobPositionId eq employee.jobPositionId ? 'selected' : ''}>${pos.jobPositionName}</option>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<c:forEach var="pos" items="${defaultPositions}">
													<option>${pos}</option>
												</c:forEach>
											</c:otherwise>
										</c:choose></select></label> <label class="field"><span>내/외국인</span><select
									name="foreignYn"><option value="">선택해주세요.</option>
										<option value="N"
											${employee.foreignYn eq 'N' ? 'selected' : ''}>내국인</option>
										<option value="Y"
											${employee.foreignYn eq 'Y' ? 'selected' : ''}>외국인</option></select></label><label
									class="field"><span>주민번호</span><input name="juminNo"
									value="<c:out value='${employee.juminNo}' />"></label>
								<div class="field field--wide">
									<span>주소</span>
									<div class="address-row">
										<input name="zipCode" value="${employee.zipCode}"
											placeholder="우편번호"><input name="address"
											value="<c:out value='${employee.address}' />"
											placeholder="주소">
									</div>
								</div>
								<label class="field"><span>전화번호</span><input
									name="telNo" value="${employee.telNo}"></label><label
									class="field"><span>휴대폰</span><input name="mobileNo"
									value="${employee.mobileNo}"></label><label class="field"><span>이메일</span><input
									type="email" name="email" value="${employee.email}"></label><label
									class="field"><span>SNS</span><input name="snsAddress"
									value="${employee.snsAddress}"></label><label
									class="field field--wide"><span>기타사항</span> <textarea
										name="memo"><c:out value="${employee.memo}" /></textarea></label>
							</div>
						</section>

						<div class="part-divider">사원정보 2</div>
						<section class="form-card" id="license">
							<h2>자격·면허 &amp; 어학능력</h2>
							<div class="sub-card-title">
								<h3>자격 &amp; 면허</h3>
								<div>
									<button name="action" value="addLicense">추가하기</button>
									<button name="action" value="deleteLicenses">선택삭제</button>
								</div>
							</div>
							<div class="table-wrap">
								<table>
									<thead>
										<tr>
											<th>선택</th>
											<th>자격/면허명</th>
											<th>취득일</th>
											<th>발행기관</th>
											<th>증번호</th>
											<th>비고</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach begin="0" end="2" varStatus="row">
											<c:set var="item" value="${licenses[row.index]}" />
											<tr>
												<td><input type="checkbox" name="licenseDeleteIds"
													value="${item.employeeLicenseId}"></td>
												<td><input name="licenses[${row.index}].licenseName"
													value="<c:out value='${item.licName}' />"></td>
												<td><input type="date"
													name="licenses[${row.index}].acquireDate"
													value="<fmt:formatDate value='${item.acqDate}' pattern='yyyy-MM-dd' />"></td>
												<td><input name="licenses[${row.index}].issuer"
													value="${item.issuer}"></td>
												<td><input name="licenses[${row.index}].licenseNo"
													value="${item.licenseNo}"></td>
												<td><input name="licenses[${row.index}].note"
													value="${item.note}"></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
							<div class="sub-card-title">
								<h3>어학능력</h3>
								<div>
									<button name="action" value="addLanguage">추가하기</button>
									<button name="action" value="deleteLanguages">선택삭제</button>
								</div>
							</div>
							<div class="table-wrap">
								<table>
									<thead>
										<tr>
											<th>선택</th>
											<th>외국어명</th>
											<th>시험</th>
											<th>공인점수</th>
											<th>취득일</th>
											<th>독해</th>
											<th>작문</th>
											<th>회화</th>
										</tr>
									</thead>
									<tbody>
										<c:set var="lang" value="${languages[0]}" />
										<tr>
											<td><input type="checkbox" name="languageDeleteIds"
												value="${lang.employeeLanguageId}"></td>
											<td><input name="languages[0].languageName"
												value="${lang.langName}"></td>
											<td><input name="languages[0].testName"
												value="${lang.testName}"></td>
											<td><input type="number" name="languages[0].score"
												value="${lang.score}"></td>
											<td><input type="date" name="languages[0].acquireDate"
												value="<fmt:formatDate value='${lang.acqDate}' pattern='yyyy-MM-dd' />"></td>
											<c:forEach var="ability"
												items="${fn:split('reading,writing,speaking', ',')}">
												<td><select name="languages[0].${ability}"><option
															value="">선택</option>
														<option value="상">상</option>
														<option value="중">중</option>
														<option value="하">하</option></select></td>
											</c:forEach>
										</tr>
									</tbody>
								</table>
							</div>
						</section>

						<section class="form-card" id="training">
							<div class="card-title">
								<h2>교육/훈련</h2>
								<div>
									<button name="action" value="addTraining">추가하기</button>
									<button name="action" value="deleteTrainings">선택삭제</button>
								</div>
							</div>
							<div class="table-wrap">
								<table>
									<thead>
										<tr>
											<th>선택</th>
											<th>교육구분</th>
											<th>교육명</th>
											<th>교육기간(부터)</th>
											<th>교육기간(까지)</th>
											<th>교육기관</th>
											<th>교육비</th>
											<th>환급교육비</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach begin="0" end="1" varStatus="row">
											<c:set var="item" value="${trainings[row.index]}" />
											<tr>
												<td><input type="checkbox" name="trainingDeleteIds"
													value="${item.employeeTrainingId}"></td>
												<td><select name="trainings[${row.index}].trainingType"><option
															value="">선택</option>
														<c:forEach var="type" items="${trainingTypes}">
															<option ${type eq item.trainType ? 'selected' : ''}>${type}</option>
														</c:forEach></select></td>
												<td><input name="trainings[${row.index}].trainingName"
													value="${item.trainName}"></td>
												<td><input type="date"
													name="trainings[${row.index}].startDate"
													value="<fmt:formatDate value='${item.startDate}' pattern='yyyy-MM-dd' />"></td>
												<td><input type="date"
													name="trainings[${row.index}].endDate"
													value="<fmt:formatDate value='${item.endDate}' pattern='yyyy-MM-dd' />"></td>
												<td><input name="trainings[${row.index}].institution"
													value="${item.trainInstitute}"></td>
												<td><label class="money-cell"><input
														type="number" name="trainings[${row.index}].trainingCost"
														value="${item.trainCost}"><span>원</span></label></td>
												<td><label class="money-cell"><input
														type="number" name="trainings[${row.index}].refundCost"
														value="${item.refundCost}"><span>원</span></label></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</section>

						<section class="form-card" id="reward-punish">
							<div class="card-title">
								<h2>상벌</h2>
								<div>
									<button name="action" value="addRewardPunish">추가하기</button>
									<button name="action" value="deleteRewardPunishes">선택삭제</button>
								</div>
							</div>
							<div class="table-wrap">
								<table>
									<thead>
										<tr>
											<th>선택</th>
											<th>구분</th>
											<th>상벌명</th>
											<th>상벌권자</th>
											<th>상벌일자</th>
											<th>상벌내용</th>
											<th>비고</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach begin="0" end="1" varStatus="row">
											<c:set var="item" value="${rewardPunishes[row.index]}" />
											<tr>
												<td><input type="checkbox" name="rewardDeleteIds"
													value="${item.employeeRewardDisciplineId}"></td>
												<td><select name="rewardPunishes[${row.index}].rpType"><option
															value="">선택</option>
														<c:forEach var="type" items="${rewardTypes}">
															<option ${type eq item.rpType ? 'selected' : ''}>${type}</option>
														</c:forEach></select></td>
												<td><input name="rewardPunishes[${row.index}].rpName"
													value="${item.rpName}"></td>
												<td><input name="rewardPunishes[${row.index}].grantor"
													value="${item.rpAuthority}"></td>
												<td><input type="date"
													name="rewardPunishes[${row.index}].rpDate"
													value="<fmt:formatDate value='${item.rpDate}' pattern='yyyy-MM-dd' />"></td>
												<td><input name="rewardPunishes[${row.index}].content"
													value="${item.rpContent}"></td>
												<td><input name="rewardPunishes[${row.index}].note"
													value="${item.note}"></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</section>

						<section class="form-card" id="appointment">
							<div class="card-title">
								<h2>발령</h2>
								<div>
									<button name="action" value="addAppointment">추가하기</button>
									<button name="action" value="deleteAppointments">선택삭제</button>
								</div>
							</div>
							<div class="table-wrap">
								<table>
									<thead>
										<tr>
											<th>선택</th>
											<th>발령구분</th>
											<th>발령일자</th>
											<th>부서</th>
											<th>직위</th>
											<th>직책</th>
											<th>비고</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach begin="0" end="1" varStatus="row">
											<c:set var="item" value="${appointments[row.index]}" />
											<tr>
												<td><input type="checkbox" name="appointmentDeleteIds"
													value="${item.employeeAppointmentId}"></td>
												<td><select
													name="appointments[${row.index}].appointmentType"><option
															value="">선택</option>
														<c:forEach var="type" items="${appointmentTypes}">
															<option ${type eq item.appType ? 'selected' : ''}>${type}</option>
														</c:forEach></select></td>
												<td><input type="date"
													name="appointments[${row.index}].appointmentDate"
													value="<fmt:formatDate value='${item.appDate}' pattern='yyyy-MM-dd' />"></td>
												<td><input name="appointments[${row.index}].deptName"
													value="${item.departmentName}"></td>
												<td><input name="appointments[${row.index}].posName"
													value="${item.jobPositionName}"></td>
												<td><input name="appointments[${row.index}].dutyName"
													value="${item.jobTitleDuty}"></td>
												<td><input name="appointments[${row.index}].note"
													value="${item.note}"></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</section>

						<section class="form-card" id="recommendation">
							<h2>추천/신원보증</h2>
							<div class="sub-card-title">
								<h3>추천인</h3>
							</div>
							<div class="table-wrap">
								<table>
									<thead>
										<tr>
											<th>성명</th>
											<th>관계</th>
											<th>회사명</th>
											<th>직위</th>
											<th>전화번호</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td><input name="recommender.recommenderName"
												value="${recommender.recommenderName}"></td>
											<td><input name="recommender.relation"
												value="${recommender.relation}"></td>
											<td><input name="recommender.companyName"
												value="${recommender.companyName}"></td>
											<td><input name="recommender.positionName"
												value="${recommender.positionName}"></td>
											<td><input name="recommender.telNo"
												value="${recommender.telNo}"></td>
										</tr>
									</tbody>
								</table>
							</div>
							<div class="sub-card-title">
								<h3>보증보험</h3>
							</div>
							<div class="table-wrap">
								<table>
									<thead>
										<tr>
											<th>가입기관</th>
											<th>보험번호</th>
											<th>보험금액</th>
											<th>가입일자</th>
											<th>만료일자</th>
											<th>비고</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td><input name="suretyInsurance.institution"
												value="${suretyInsurance.providerName}"></td>
											<td><input name="suretyInsurance.insuranceNo"
												value="${suretyInsurance.insuranceNo}"></td>
											<td><label class="money-cell"><input
													type="number" name="suretyInsurance.amount"
													value="${suretyInsurance.insuranceAmt}"><span>원</span></label></td>
											<td><input type="date" name="suretyInsurance.startDate"
												value="<fmt:formatDate value='${suretyInsurance.signupDate}' pattern='yyyy-MM-dd' />"></td>
											<td><input type="date" name="suretyInsurance.endDate"
												value="<fmt:formatDate value='${suretyInsurance.expireDate}' pattern='yyyy-MM-dd' />"></td>
											<td><input name="suretyInsurance.note"
												value="${suretyInsurance.note}"></td>
										</tr>
									</tbody>
								</table>
							</div>
							<div class="sub-card-title">
								<h3>신원보증인</h3>
							</div>
							<div class="table-wrap">
								<table>
									<thead>
										<tr>
											<th>성명</th>
											<th>관계</th>
											<th>주민등록번호</th>
											<th>보증금액</th>
											<th>보증일자</th>
											<th>만료일자</th>
											<th>전화번호</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td><input name="guarantor.guarantorName"
												value="${guarantor.guarantorName}"></td>
											<td><input name="guarantor.relation"
												value="${guarantor.relation}"></td>
											<td><input name="guarantor.juminNo"
												value="${guarantor.juminNo}"></td>
											<td><label class="money-cell"><input
													type="number" name="guarantor.amount"
													value="${guarantor.guaranteeAmt}"><span>원</span></label></td>
											<td><input type="date" name="guarantor.startDate"
												value="<fmt:formatDate value='${guarantor.guaranteeDate}' pattern='yyyy-MM-dd' />"></td>
											<td><input type="date" name="guarantor.endDate"
												value="<fmt:formatDate value='${guarantor.expireDate}' pattern='yyyy-MM-dd' />"></td>
											<td><input name="guarantor.telNo"
												value="${guarantor.telNo}"></td>
										</tr>
									</tbody>
								</table>
							</div>
						</section>

						<section class="form-card" id="retirement">
							<h2>퇴직</h2>
							<div class="table-wrap">
								<table>
									<thead>
										<tr>
											<th>퇴직구분</th>
											<th>퇴직일자</th>
											<th>퇴직사유</th>
											<th>퇴직 후 연락처</th>
											<th>퇴직금</th>
											<th>퇴직금명세서</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td><select name="retireType"><option value="">선택</option>
													<c:forEach var="type" items="${retireTypes}">
														<option ${type eq employee.retireType ? 'selected' : ''}>${type}</option>
													</c:forEach></select></td>
											<td><input type="date" name="retireDate"
												value="<fmt:formatDate value='${employee.retireDate}' pattern='yyyy-MM-dd' />"></td>
											<td><input name="retireReason"
												value="${employee.retireReason}"></td>
											<td><input name="afterRetireContact"
												value="${employee.afterRetireContact}"></td>
											<td><label class="money-cell"><input
													value="${retireAmount}" readonly><span>원</span></label></td>
											<td><a class="table-button"
												href="${pageContext.request.contextPath}/retirement/payslip.do?empId=${employee.employeeId}">명세서
													다운로드</a></td>
										</tr>
									</tbody>
								</table>
							</div>
						</section>

						<div class="form-actions">
							<button class="button button--primary" name="action" value="save">저장하기</button>
							<a class="button"
								href="${pageContext.request.contextPath}/settings/register2.do?empId=${employee.employeeId}">취소하기</a><a
								class="button"
								href="${pageContext.request.contextPath}/employees/employees.do">리스트</a><a
								class="button"
								href="${pageContext.request.contextPath}/settings/register2.do">신규사원등록하기</a>
						</div>
					</div>
				</div>
				<div id="photo-upload-modal" class="upload-modal" role="dialog"
					aria-modal="true" aria-labelledby="photo-upload-title">
					<a class="upload-modal__backdrop" href="#" aria-label="닫기"></a>
					<div class="upload-modal__panel">
						<div class="upload-modal__title">
							<h2 id="photo-upload-title">이미지 등록하기</h2>
							<a href="#" aria-label="닫기">×</a>
						</div>
						<div class="upload-modal__body">
							<input type="file" name="photoFile" accept="image/png,image/jpeg">
							<p>
								* 파일 용량 : <strong>1MB 미만</strong>이어야 합니다.<br>* 파일명 : <strong>영문
									또는 숫자</strong>로 되어 있어야 합니다.
							</p>
						</div>
						<button class="upload-modal__confirm" name="action"
							value="savePhoto">확인</button>
					</div>
				</div>
			</form>
		</div>
	</main>
	<%@ include file="/WEB-INF/view/common/footer.jspf"%>
</body>
</html>
