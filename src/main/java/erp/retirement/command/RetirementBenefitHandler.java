package erp.retirement.command;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.retirement.dto.RetirementBenefitForm;
import erp.retirement.model.RetirementIncomeEntry;
import erp.retirement.model.RetirementTaxDeferral;
import erp.retirement.service.RetirementBenefitService;
import erp.retirement.service.RetirementBenefitService.BenefitPageData;
import mvc.command.CommandHandler;

// 퇴직급여 목록, 사원선택, 계산, 저장 및 삭제 URL을 처리하는 Handler
public class RetirementBenefitHandler implements CommandHandler {
	private static final String VIEW="/WEB-INF/view/retirement/retirement-benefit.jsp";
	private final RetirementBenefitService service=new RetirementBenefitService();
	@Override public String process(HttpServletRequest req,HttpServletResponse res)throws Exception{
		String uri=req.getRequestURI();
		if(uri.endsWith("/benefit.do")&&req.getMethod().equalsIgnoreCase("GET"))return list(req,null);
		if(uri.endsWith("/employee-search.do"))return list(req,null);
		if(uri.endsWith("/new.do"))return list(req,service.prepareNew(requiredInt(req,"employeeId")));
		if(uri.endsWith("/calculate.do"))return list(req,readForm(req,true));
		if(uri.endsWith("/save.do")){RetirementBenefitForm f=readForm(req,true);int id=service.save(f);redirect(req,res,"saved",id);return null;}
		if(uri.endsWith("/delete-all.do")){service.delete(null,true);redirect(req,res,"deleted",null);return null;}
		if(uri.endsWith("/delete.do")){service.delete(parseInt(req.getParameter("calculationId")),false);redirect(req,res,"deleted",null);return null;}
		res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);return null;
	}
	private String list(HttpServletRequest req,RetirementBenefitForm override){int year=parseInt(req.getParameter("paymentYear"))==null?java.time.LocalDate.now().getYear():parseInt(req.getParameter("paymentYear"));Integer calcId=parseInt(req.getParameter("calculationId"));BenefitPageData d=service.getPage(year,calcId,req.getParameter("employeeKeyword"),parseInt(req.getParameter("departmentId")));RetirementBenefitForm form=override!=null?override:d.getForm();if(form!=null&&req.getRequestURI().endsWith("/calculate.do"))service.calculate(form);req.setAttribute("paymentYears",service.getPaymentYears());req.setAttribute("selectedYear",year);req.setAttribute("retirementBenefits",d.getBenefits());req.setAttribute("selectableEmployees",d.getEmployees());req.setAttribute("departments",d.getDepartments());req.setAttribute("retirementBenefit",form);if("saved".equals(req.getParameter("result")))req.setAttribute("message","퇴직급여 내역을 저장했습니다.");if("deleted".equals(req.getParameter("result")))req.setAttribute("message","퇴직급여 내역을 삭제했습니다.");return VIEW;}
	private RetirementBenefitForm readForm(HttpServletRequest req,boolean rows){RetirementBenefitForm f=new RetirementBenefitForm();f.setEmployeeId(requiredInt(req,"employeeId"));f.setSettlementType(req.getParameter("settlementType"));f.setStartDate(req.getParameter("startDate"));f.setEndDate(req.getParameter("endDate"));f.setExcludedDays(intValue(req.getParameter("excludedDays")));f.setCompensation(longValue(req.getParameter("compensation")));f.setDismissalAllowance(longValue(req.getParameter("dismissalAllowance")));f.setTaxFreeRetirement(longValue(req.getParameter("taxFreeRetirement")));f.setPrepaidTax(longValue(req.getParameter("prepaidTax")));f.setTaxCredit(longValue(req.getParameter("taxCredit")));f.setDailyOrdinary(longValue(req.getParameter("dailyOrdinary")));f.setRetirementIncome(longValue(req.getParameter("retirementIncome")));f.setIncomeTax(longValue(req.getParameter("incomeTax")));f.setLocalIncomeTax(longValue(req.getParameter("localIncomeTax")));f.setRuralTax(longValue(req.getParameter("ruralTax")));f.setOtherDeduction(longValue(req.getParameter("otherDeduction")));f.setPaymentMethod(req.getParameter("paymentMethod"));f.setPaymentDate(req.getParameter("paymentDate"));if(rows){readSalaryRows(req,f);readOtherRows(req,f);readDeferrals(req,f);}return f;}
	private void readSalaryRows(HttpServletRequest req,RetirementBenefitForm f){String[] starts=req.getParameterValues("salaryStartDate"),ends=req.getParameterValues("salaryEndDate"),amounts=req.getParameterValues("salaryTotal");if(starts==null)return;for(int i=0;i<starts.length;i++){if(blank(starts[i])||blank(ends[i]))continue;RetirementIncomeEntry e=new RetirementIncomeEntry();e.setDataType("SALARY");e.setPeriodStartDate(date(starts[i]));e.setPeriodEndDate(date(ends[i]));e.setCalcDays((double)(java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDate.parse(starts[i]),java.time.LocalDate.parse(ends[i]))+1));e.setAmount(longAt(amounts,i));e.setThreeMonthAmount(0);f.getIncomeEntries().add(e);}}
	private void readOtherRows(HttpServletRequest req,RetirementBenefitForm f){String[] months=req.getParameterValues("otherIncomeMonth"),names=req.getParameterValues("otherIncomeItem"),amounts=req.getParameterValues("otherIncomeAmount"),three=req.getParameterValues("threeMonthAmount");if(months==null)return;for(int i=0;i<months.length;i++){if(blank(months[i])||blank(at(names,i)))continue;RetirementIncomeEntry e=new RetirementIncomeEntry();e.setDataType("ETC_INCOME");e.setCalcDays(0d);e.setPayYm(months[i].replace("-",""));e.setItemName(at(names,i));e.setAmount(longAt(amounts,i));e.setThreeMonthAmount(longAt(three,i));f.getIncomeEntries().add(e);}}
	private void readDeferrals(HttpServletRequest req,RetirementBenefitForm f){String[] names=req.getParameterValues("pensionProvider"),biz=req.getParameterValues("pensionBusinessNo"),accounts=req.getParameterValues("pensionAccount"),dates=req.getParameterValues("pensionDate"),amounts=req.getParameterValues("pensionAmount");if(names==null)return;for(int i=0;i<names.length;i++){if(blank(names[i])||blank(at(accounts,i)))continue;RetirementTaxDeferral d=new RetirementTaxDeferral();d.setBizName(names[i]);d.setBizRegNo(at(biz,i));d.setAccountNo(at(accounts,i));d.setDepositDate(blank(at(dates,i))?null:date(at(dates,i)));d.setDepositAmt(longAt(amounts,i));f.getTaxDeferrals().add(d);}}
	private void redirect(HttpServletRequest req,HttpServletResponse res,String result,Integer id)throws IOException{res.sendRedirect(req.getContextPath()+"/retirement/benefit.do?result="+result+(id==null?"":"&calculationId="+id));}
	private int requiredInt(HttpServletRequest req,String name){Integer v=parseInt(req.getParameter(name));if(v==null)throw new IllegalArgumentException("사원을 선택하세요.");return v;} private Integer parseInt(String v){try{return Integer.valueOf(v);}catch(Exception e){return null;}} private int intValue(String v){Integer n=parseInt(v);return n==null?0:n;} private long longValue(String v){try{return Long.parseLong(v==null?"0":v.replace(",","").trim());}catch(Exception e){return 0;}} private Date date(String v){try{return new SimpleDateFormat("yyyy-MM-dd").parse(v);}catch(Exception e){return null;}} private boolean blank(String v){return v==null||v.trim().isEmpty();} private String at(String[] a,int i){return a!=null&&i<a.length?a[i]:"";} private long longAt(String[] a,int i){return longValue(at(a,i));}
}
