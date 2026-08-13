package erp.retirement.command;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.retirement.service.RetirementPayslipService;
import erp.retirement.service.RetirementPayslipService.PayslipData;
import mvc.command.CommandHandler;

// 지급년도와 사원을 기준으로 퇴직급여명세서를 조회하는 읽기 전용 Handler
public class RetirementPayslipHandler implements CommandHandler {
	private static final String VIEW="/WEB-INF/view/retirement/retirement-payslip.jsp";
	private final RetirementPayslipService service=new RetirementPayslipService();
	@Override public String process(HttpServletRequest req,HttpServletResponse res){int year=intValue(req.getParameter("paymentYear"),LocalDate.now().getYear());PayslipData d=service.getData(year,req.getParameter("keyword"),parseInt(req.getParameter("calculationId")));req.setAttribute("paymentYears",service.getPaymentYears());req.setAttribute("selectedYear",year);req.setAttribute("retirementPayslips",d.getItems());req.setAttribute("selectedPayslip",d.getSelected());req.setAttribute("company",d.getCompany());Calendar c=Calendar.getInstance();req.setAttribute("issueYear",c.get(Calendar.YEAR));req.setAttribute("issueMonth",c.get(Calendar.MONTH)+1);req.setAttribute("issueDay",c.get(Calendar.DAY_OF_MONTH));return VIEW;}
	private Integer parseInt(String v){try{return Integer.valueOf(v);}catch(Exception e){return null;}}private int intValue(String v,int d){Integer n=parseInt(v);return n==null?d:n;}
}
