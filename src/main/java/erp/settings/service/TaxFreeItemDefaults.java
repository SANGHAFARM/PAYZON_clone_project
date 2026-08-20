package erp.settings.service;

import java.util.ArrayList;
import java.util.List;

import erp.settings.model.TaxFreeItem;

/** 원본 PAYZON 급여항목 화면을 기준으로 제공하는 비과세·감면 소득 기본 코드 목록 */
class TaxFreeItemDefaults {

	private TaxFreeItemDefaults() {
	}

	static List<TaxFreeItem> create() {
		List<TaxFreeItem> items = new ArrayList<>();
		items.add(item("A01", "소법§12 4 가", "", "복무중인 병이 받는 급여", 0L, "N", "비과세"));
		items.add(item("B01", "소법§12 4 나", "", "법률에 의한 동원직장에서 받는 급여", 0L, "N", "비과세"));
		items.add(item("C01", "소법§12 4 다", "", "「산업재해보상보험법」에 의해 지급받는 요양급여 등", 0L, "N", "비과세"));
		items.add(item("D01", "소법§ 12 3 라", "", "「근로기준법」등에 따라 지급받는 요양보상금 등", 0L, "N", "비과세"));
		items.add(item("E01", "소법§ 12 3 마", "", "「고용보험법」에 따라 받는 육아휴직급여 등", 0L, "N", "비과세"));
		items.add(item("E02", "소법§ 12 3 마", "", "「국가공무원법」 등에 따라 받는 육아휴직수당 등", 0L, "N", "비과세"));
		items.add(item("E10", "소법§ 12 3 바", "", "「국민연금법」에 따라 받는 반환일시금(사망으로 받는 것에 한함) 및 사망일시금", 0L, "N", "비과세"));
		items.add(item("F01", "소법§ 12 3 사", "", "「공무원연금법」 등에 따라 받는 요양비 등", 0L, "N", "비과세"));
		items.add(item("G01", "소법§12 4 아", "", "비과세 학자금(소령§ 11)", 0L, "Y", "비과세"));
		items.add(item("H01", "소법§ 12 3 자", "", "소령§12 1(법령ㆍ조례에 의한 보수를 받지 아니하는 위원 등이 받는 수당)", 0L, "Y", "비과세"));
		items.add(item("H02", "소법§ 12 3 자", "", "소령§12 2～3(일직료ㆍ숙직료 등)", 0L, "N", "비과세"));
		items.add(item("H03", "소법§ 12 3 자", "", "소령§12 3(자가운전보조금)", 200000L, "N", "비과세"));
		items.add(item("H04", "소법§ 12 3 자", "", "소령§12 4～8(법령에 의해 착용하는 제복 등)", 0L, "N", "비과세"));
		items.add(item("H05", "소법§ 12 3 자", "", "소령§12 9～11(경호수당, 승선수당 등)", 0L, "Y", "비과세"));
		items.add(item("H06", "소법§ 12 3 자", "", "소령§12 12 가(연구보조비)-「유아교육법」, 「초중등교육법」", 200000L, "Y", "비과세"));
		items.add(item("H07", "소법§ 12 3 자", "", "소령§12 12 가(연구보조비)-「고등교육법」", 200000L, "Y", "비과세"));
		items.add(item("H08", "소법§ 12 3 자", "", "소령§12 12 가(연구보조비)-「특별법에 의한 교육기관」", 200000L, "Y", "비과세"));
		items.add(item("H09", "소법§ 12 3 자", "", "소령§12 12 나(연구보조비)", 0L, "Y", "비과세"));
		items.add(item("H10", "소법§ 12 3 자", "", "소령§12 12 다(연구보조비)", 0L, "Y", "비과세"));
		items.add(item("H14", "소법§ 12 3 자", "", "소령§12 13 가(보육교사 근무환경개선비)-「영유아보육법 시행령」", 0L, "Y", "비과세"));
		items.add(item("H15", "소법§ 12 3 자", "", "소령§12 13 나(사립유치원 수석교사ㆍ교사의 인건비)-「유아교육법 시행령」", 0L, "Y", "비과세"));
		items.add(item("H11", "소법§ 12 3 자", "", "소령§12 14 (취재수당)", 200000L, "Y", "비과세"));
		items.add(item("H12", "소법§ 12 3 자", "", "소령§12 15 (벽지수당)", 200000L, "Y", "비과세"));
		items.add(item("H13", "소법§ 12 3 자", "", "소령§12 16 (천재ㆍ지변 등 재해로 받는 급여)", 0L, "Y", "비과세"));
		items.add(item("H16", "소법§ 12 3 자", "", "소령§12 17 (정부 · 공공기관 중 지방이전기관 종사자 이전지원금)", 0L, "Y", "비과세"));
		items.add(item("I01", "소법§ 12 3 차", "", "외국정부 또는 국제기관에 근무하는 사람에 대한 비과세", 0L, "Y", "비과세"));
		items.add(item("J01", "소법§ 12 3 카", "", "「국가유공자 등 예우 및 지원에 관한 법률」에 따라 받는 보훈급여금 및 학습보조비", 0L, "N", "비과세"));
		items.add(item("J10", "소법§ 12 3 타", "", "「전직대통령 예우에 관한 법률」에 따라 받는 연금", 0L, "N", "비과세"));
		items.add(item("K01", "소법§ 12 3 파", "", "작전임무 수행을 위해 외국에 주둔하는 군인 등이 받는 급여", 0L, "Y", "비과세"));
		items.add(item("L01", "소법§ 12 3 하", "", "종군한 군인 등이 전사한 경우 해당 과세기간 비과세", 0L, "N", "비과세"));
		items.add(item("M01", "소법§ 12 3 거", "", "소령§16①1(국외근로) 100만원", 1000000L, "Y", "비과세"));
		items.add(item("M02", "소법§ 12 3 거", "", "소령§16①1(국외근로) 300만원", 3000000L, "Y", "비과세"));
		items.add(item("M03", "소법§ 12 3 거", "", "소령§16①2(국외근로)", 0L, "Y", "비과세"));
		items.add(item("N01", "소법§ 12 3 너", "", "「국민건강보험법」등에 따라 사용자가 부담하는 부담금 등", 0L, "N", "비과세"));
		items.add(item("O01", "소법§ 12 3 더", "", "생산직 등에 종사하는 근로자의 야간수당 등", 0L, "Y", "비과세"));
		items.add(item("P01", "소법§ 12 3 러", "", "비과세 식사대(월 20만원 이하)", 200000L, "N", "비과세"));
		items.add(item("P02", "소법§ 12 3 러", "", "현물 급식", 0L, "N", "비과세"));
		items.add(item("Q01", "소법§ 12 3 머", "", "출산ㆍ6세 이하의 자녀의 보육 관련 비과세", 0L, "Y", "비과세"));
		items.add(item("R01", "소법§ 12 3 버", "", "국군포로가 지급받는 보수 등", 0L, "N", "비과세"));
		items.add(item("R10", "소법§ 12 3 서", "", "「교육기본법」 제28조제1항에 따라 받는 장학금", 0L, "Y", "비과세"));
		items.add(item("S01", "구 조특법§ 15", "", "주식매수선택권 비과세", 0L, "Y", "비과세"));
		items.add(item("Y02", "조특법§ 88의4⑥", "", "우리사주조합 인출금 비과세(50%)", 0L, "Y", "비과세"));
		items.add(item("Y03", "조특법§ 88의4⑥", "", "우리사주조합 인출금 비과세(75%)", 0L, "Y", "비과세"));
		items.add(item("Y21", "조특법§ 30", "", "장기 미취업자 중소기업 취업 비과세(폐지)", 0L, "Y", "비과세"));
		items.add(item("Y22", "소법§ 12 3 자", "", "소령§12 13 다(전공의 수련보조수당)", 0L, "Y", "비과세"));
		items.add(item("T01", "조특법§ 18", "", "외국인 기술자 소득세 면제", 0L, "Y", "감면 소득"));
		items.add(item("T10", "조특법§ 30", "", "중소기업에 취업하는 청년에 대한 소득세 감면", 0L, "Y", "감면 소득"));
		items.add(item("T20", "조세조약", "", "조세조약 상 교직자 조항의 소득세 감면", 0L, "Y", "감면 소득"));
		return items;
	}

	private static TaxFreeItem item(String code, String legalClause, String reportField,
			String name, long limit, String statementYn, String category) {
		TaxFreeItem item = new TaxFreeItem();
		item.setTaxFreeCode(code);
		item.setLegalClause(legalClause);
		item.setReportField(reportField);
		item.setTaxFreeName(name);
		item.setDefaultLimit(limit);
		item.setPayStatementYn(statementYn);
		item.setIncomeCategory(category);
		return item;
	}
}

