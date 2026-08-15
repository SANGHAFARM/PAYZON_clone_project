<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>일용직 근무기록/관리</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common/common.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/attendance/day-worker-management.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf"%>
	<main class="page-content day-worker-page">
		<header class="page-heading">
			<div>
				<p>근태관리</p>
				<h1>일용직 근무기록/관리</h1>
			</div>
		</header>

		<section class="worker-panel">
			<!-- =========================================================== -->
			<!--                  사원 조회(검색어, 재직상태) 영역                   -->
			<!-- =========================================================== -->
			<div class="worker-search">
				<form method="get"
					action="${pageContext.request.contextPath}/attendance/day-worker-management.do">
					<!-- 프로퍼티로 수정 필요!!!!!!!!!!!!!!!!!! -->
					<input type="search" name="keyword"
						value="<c:out value='${keyword}'/>" placeholder="검색어 입력"
						aria-label="검색어">

					<!-- 검색할 때 현재 선택된 'status'가 유지되도록 hidden으로 포함 -->
					<c:if test="${not empty status }">
						<input type="hidden" name="status" value="${status}">
					</c:if>

					<button type="submit" class="button button-primary">검색</button>
					<a class="button button-outline"
						href="${pageContext.request.contextPath}/attendance/day-worker-management.do">전체보기</a>
					<!-- 프로퍼티로 수정 필요!!!!!!!!!!!!!!!!!! -->
				</form>

				<form class="status-filter" method="get"
					action="${pageContext.request.contextPath}/attendance/day-worker-management.do">
					<!-- 프로퍼티로 수정 필요!!!!!!!!!!!!!!!!!! -->
					<select name="status" aria-label="상태별">
						<option value="">상태별</option>
						<option value="재직" ${ status eq '재직' ? 'selected' : ''}>재직</option>
						<option value="퇴직" ${status eq '퇴직' ? 'selected' : ''}>퇴직</option>
					</select>

					<!-- 상태를 조회할 때 현재 입력된 검색어가 유지되도록 hidden으로 포함 -->
					<c:if test="${not empty param.keyword }">
						<input type="hidden" name="keyword" value="${param.keyword }">
					</c:if>
					<button type="submit" class="button button-primary">조회</button>
				</form>
			</div>

			<!-- =========================================================== -->
			<!--                          사원 목록                              -->
			<!-- =========================================================== -->
			<div class="worker-layout">
				<div class="employee-list-wrap">
					<table class="data-table employee-table">
						<thead>
							<tr>
								<th class="check-cell"><input type="checkbox"
									aria-label="전체 선택"></th>
								<th>구분</th>
								<th>사원번호</th>
								<th>성명</th>
								<th>부서</th>
								<th>근무기록</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="dayworker" items="${dayWorkers}">
								<tr>
									<td class="check-cell"><input type="checkbox"
										name="employeeIds" value="${dayworker.employeeId}"
										aria-label="${dayworker.empNameKr} 선택"
										${not empty editId ? 'disabled' : '' }></td>
									<td><c:out value="${dayworker.empType}" /></td>
									<td><c:out value="${dayworker.empNo}" /></td>
									<td><c:out value="${dayworker.empNameKr}" /></td>
									<td><c:out value="${dayworker.departmentName}" /></td>
									<td><a class="button button-small"
										href="${pageContext.request.contextPath}/attendance/day-worker-management.do?employeeId=${dayworker.employeeId}#work-history-${dayworker.employeeId}">관리</a></td>
								</tr>
							</c:forEach>
							<c:if test="${empty dayWorkers}">
								<tr>
									<td colspan="6" class="empty-row">조회된 일용직 사원이 없습니다.</td>
								</tr>
							</c:if>
						</tbody>
					</table>
				</div>

				<!-- =========================================================== -->
				<!--                         일용직 근무 입력 폼                        -->
				<!-- =========================================================== -->
				<form id="recordForm" class="record-form" method="post"
					action="${pageContext.request.contextPath}/attendance/day-worker-management.do">

					<c:if test="${not empty editId}">
						<input type="hidden" name="editId" value="${editId}">
					</c:if>

					<div id="hiddenEmployeeInputs"></div>

					<h2>일용직 근무기록 입력</h2>
					<div class="form-fields">
						<label><span>근무일자</span> <input type="date"
							name="workDate" required
							value="${empty editId ? today : workDate}"></label> <label><span>현장/프로젝트</span><span
							class="project-control"><select name="projectId"><option
										value="">선택하세요.</option>
									<c:forEach var="project" items="${projects}">
										<option value="${project.projectId}"
											${projectId eq project.projectId ? 'selected' : ''}><c:out
												value="${project.projectName}" /></option>
									</c:forEach></select><a class="button button-project" href="#project-manager">목록관리</a></span>
						</label> <label> <span>일당</span> <span class="amount-control">
								<input type="number" id="dailyPay" name="dailyPay" min="0"
								value="${dailyPay}" placeholder="일당을 입력해주세요"> <em>원</em>
						</span></label> <label> <span>지급율</span> <input type="number"
							id="payRate" name="payRate" min="0" step="0.1"
							value="${empty editId ? 1.0 : payRate}"></label> <label
							class="calculated"> <span>소득세</span> <span
							class="amount-control"> <input type="text" id="incomeTax"
								name="incomeTax"
								value="${empty editId ? calculatedIncomeTax : incomeTax}"
								placeholder="자동 계산됩니다" readonly> <em>원</em></span></label> <label
							class="calculated"> <span>지방소득세</span> <span
							class="amount-control"> <input type="text"
								id="localIncomeTax" name="localIncomeTax"
								value="${empty editId ? calculatedLocalIncomeTax : localIncomeTax}"
								placeholder="자동 계산됩니다" readonly> <em>원</em></span></label> <label
							class="calculated"><span>실지급액</span><span
							class="amount-control"> <input type="text" id="actualPay"
								name="actualPay"
								value="${empty editId ? calculatedActualPay : actualPay}"
								placeholder="자동 계산됩니다" readonly><em>원</em></span> </label>
					</div>
					<div class="form-actions">

						<c:choose>
							<c:when test="${empty editId}">
								<button type="submit" id="saveBtn"
									class="button button-primary action-button">저장</button>
							</c:when>
							<c:otherwise>
								<button type="submit" class="button button-primary action-button">수정</button>
							</c:otherwise>
						</c:choose>


						<!-- 수정 모드일 시 수정취소 버튼 활성화 -->
						<c:choose>
							<c:when test="${empty editId}">
								<button type="reset"
									class="button button-muted action-button clear-button">내용지우기</button>
							</c:when>
							<c:otherwise>
								<a
									href="${pageContext.request.contextPath}/attendance/day-worker-management.do"
									class="button button-muted action-button">수정취소</a>
							</c:otherwise>
						</c:choose>
					</div>
				</form>
			</div>
		</section>
	</main>


	<!-- =========================================================== -->
	<!--                         사원별 근무 기록                          -->
	<!-- =========================================================== -->
	<c:forEach var="employee" items="${dayWorkers}">
		<div id="work-history-${employee.employeeId}" class="modal-overlay">
			<section class="modal work-history-modal" role="dialog"
				aria-modal="true"
				aria-labelledby="history-title-${employee.employeeId}">
				<header>
					<h2 id="history-title-${employee.employeeId}">사원별 근무기록</h2>
					<a href="#" aria-label="닫기">&times;</a>
				</header>
				<div class="modal-body">
					<div class="record-summary">
						<p>
							성명 : <strong><c:out value="${employee.empNameKr}" /></strong> (
							<c:out value="${employee.empNo}" />
							) 부서 :
							<c:out value="${employee.departmentName}" />
							<%-- 							직위 :
							<c:out value="${employee.positionName}" /> --%>
						</p>
						<form method="get">
							<input type="hidden" name="employeeId"
								value="${param.employeeId}"> <select name="year"
								aria-label="연도">
								<c:forEach var="y" begin="2015" end="2026">
									<option value="${y}"
										${y eq (empty param.year ? 2026 : param.year) ? 'selected' : ''}>${y}년</option>
								</c:forEach>
							</select> <select name="month" aria-label="월">
								<c:forEach var="monthNo" begin="1" end="12">
									<option value="${monthNo}"
										${monthNo eq (empty param.month ? 8 : param.month) ? 'selected' : ''}>${monthNo}월</option>
								</c:forEach>
							</select>

							<button type="submit">조회</button>
						</form>
					</div>
					<table class="data-table">
						<thead>
							<tr>
								<th>번호</th>
								<th>근무일자</th>
								<th>현장/프로젝트</th>
								<th>일당</th>
								<th>지급율</th>
								<th>지급액</th>
								<th>소득세</th>
								<th>지방소득세</th>
								<th>실지급액</th>
								<th>수정/삭제</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="record" items="${workRecords}" varStatus="status">
								<tr>
									<td><c:out value="${status.count }" /></td>
									<td><c:out value="${record.workDate}" /></td>
									<td><c:out value="${record.projectName}" /></td>
									<td><c:out value="${record.dailyPay}" /></td>
									<td><c:out value="${record.payRate}" /></td>
									<td><c:out value="${record.grossPay}" /></td>
									<td><c:out value="${record.incomeTax}" /></td>
									<td><c:out value="${record.localIncomeTax}" /></td>
									<td><c:out value="${record.actualPay}" /></td>
									<td><a class="mini-button"
										href="?editId=${record.dailyWorkRecordId}&employeeId=${param.employeeId}&workDate=${record.workDate}&projectId=${record.projectId }&dailyPay=${record.dailyPay }&payRate=${record.payRate }&incomeTax=${record.incomeTax }&localIncomeTax=${record.localIncomeTax }&actualPay=${record.actualPay }">수정</a>
										<%-- <a class="mini-button mini-delete"	href="?deleteId=${record.dailyWorkRecordId}&employeeId=${param.employeeId}&year=${param.year}&month=${param.month}#work-history-${param.employeeId}">삭제</a> --%>
										<form
											action="${pageContext.request.contextPath}/attendance/day-worker-management.do"
											method="post" style="display: inline;">
											<input type="hidden" name="deleteId"
												value="${record.dailyWorkRecordId}"> <input
												type="hidden" name="employeeId" value="${param.employeeId}">
											<input type="hidden" name="year" value="${param.year}">
											<input type="hidden" name="month" value="${param.month}">
											<button type="submit" class="mini-button mini-delete">삭제</button>
										</form></td>
								</tr>
							</c:forEach>
							<c:if test="${empty workRecords}">
								<tr>
									<td colspan="10" class="empty-row">등록된 근무기록이 없습니다.</td>
								</tr>
							</c:if>
						</tbody>
					</table>
				</div>
			</section>
		</div>
	</c:forEach>

	<div id="project-manager" class="modal-overlay">
		<section class="modal project-modal" role="dialog" aria-modal="true"
			aria-labelledby="project-title">
			<header>
				<h2 id="project-title">현장/프로젝트 목록관리</h2>
				<a href="#" aria-label="닫기">&times;</a>
			</header>
			<div class="modal-body">
				<ul class="project-list">
					<c:forEach var="project" items="${projects}">
						<li><span><c:out value="${project.projectName}" /></span><span><a
								href="?projectEdit=${project.projectId}">수정</a><a
								href="?projectDelete=${project.projectId}">삭제</a></span></li>
					</c:forEach>
					<c:if test="${empty workProjects}">
						<li><span>등록된 현장/프로젝트가 없습니다.</span></li>
					</c:if>
				</ul>

				<form class="project-add" method="post"
					action="${pageContext.request.contextPath}/attendance/day-worker-management-test/project/save">
					<input type="text" name="projectName" placeholder="새 현장/프로젝트명"
						required>
					<button type="submit" class="button button-primary">추가하기</button>
				</form>
			</div>
		</section>
	</div>

	<!--  소득세, 지방소득세, 실지급액을 자동계산하는 스크립트 -->
	<script>
/*  웹페이지의 모든 HTML 구조가 완전히 로드된 안전한 시점에 스크립트가 실행되도록 감싸주는 역할 */
document.addEventListener('DOMContentLoaded', function() {
	
	//변수 저장
    const dailyPayInput = document.getElementById('dailyPay'); //일당
    const payRateInput = document.getElementById('payRate');//지급율
    const incomeTaxInput = document.getElementById('incomeTax');//소득세
    const localIncomeTaxInput = document.getElementById('localIncomeTax');//지방소득세
    const actualPayInput = document.getElementById('actualPay');//실지급액

    //세금 계산 메서드
    function calculateTaxes() {
    	//입력된 일당과 지급율을 숫자로 변환하고, 값이 비어있으면 각각 0과 1.0으로 저장
        const dailyPay = parseFloat(dailyPayInput.value) || 0;
        const payRate = parseFloat(payRateInput.value) || 1.0;

        // 총 지급액 (일당 * 지급율)
        const totalPay = dailyPay * payRate;

        // 비과세 15만원 공제 후 2.7% 적용
        let taxableAmount = totalPay - 150000;
        if (taxableAmount < 0) taxableAmount = 0;

        let incomeTax = 0;
        if (totalPay > 150000) {
            incomeTax = Math.floor(taxableAmount * 0.027 / 10) * 10; // 10원 미만 절사
        }

        let localIncomeTax = Math.floor(incomeTax * 0.1 / 10) * 10;
        let actualPay = Math.floor(totalPay - incomeTax - localIncomeTax);

        // 결과 입력창에 반영
        incomeTaxInput.value = incomeTax.toLocaleString();
        localIncomeTaxInput.value = localIncomeTax.toLocaleString();
        actualPayInput.value = actualPay.toLocaleString();
    }

    dailyPayInput.addEventListener('input', calculateTaxes);
    payRateInput.addEventListener('input', calculateTaxes);
    
    
 // --- 새로 추가하는 체크박스 연동 및 저장 버튼 제어 스크립트 ---
    const saveBtn = document.getElementById('saveBtn');
    
    saveBtn.addEventListener('click', function() {
        // 1. 테이블에서 체크된 사원들 가져오기
        const checkedBoxes = document.querySelectorAll('input[name="employeeIds"]:checked');

        // 3. 기존 hidden 인풋 초기화
        const container = document.getElementById('hiddenEmployeeInputs');
        container.innerHTML = '';
        
        // 4. 체크된 사원 ID들을 동적 hidden 인풋으로 폼에 주입
        checkedBoxes.forEach(function(checkbox) {
            const hiddenInput = document.createElement('input');
            hiddenInput.type = 'hidden';
            hiddenInput.name = 'employeeIds'; // 자바 컨트롤러에서 받을 배열 이름
            hiddenInput.value = checkbox.value;
            container.appendChild(hiddenInput);
        });
        
        // 5. 폼 제출
        document.getElementById('recordForm').submit();
    });
});
</script>
	<%@ include file="/WEB-INF/view/common/footer.jspf"%>
</body>
</html>
