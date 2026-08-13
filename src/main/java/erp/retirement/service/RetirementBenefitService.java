package erp.retirement.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import erp.employees.dao.EmployeeDao;
import erp.employees.dto.EmployeeListItem;
import erp.employees.service.EmployeeSearchCondition;
import erp.retirement.dao.RetirementCalculationDao;
import erp.retirement.dao.RetirementIncomeEntryDao;
import erp.retirement.dao.RetirementTaxDeferralDao;
import erp.retirement.dto.RetirementBenefitForm;
import erp.retirement.dto.RetirementBenefitListItem;
import erp.retirement.model.RetirementCalculation;
import erp.retirement.model.RetirementIncomeEntry;
import erp.retirement.model.RetirementTaxDeferral;
import erp.settings.dao.DepartmentDao;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 퇴직급여 화면 조회, 기본 계산, 마스터·상세 저장 및 삭제 Service
public class RetirementBenefitService {
	private final RetirementCalculationDao calculationDao=RetirementCalculationDao.getInstance();
	public List<Integer> getPaymentYears(){int year=Calendar.getInstance().get(Calendar.YEAR);List<Integer> years=new ArrayList<>();for(int i=year+1;i>=year-5;i--)years.add(i);return years;}
	public BenefitPageData getPage(int year,Integer calculationId,String keyword,Integer departmentId){
		try(Connection conn=ConnectionProvider.getConnection()){
			BenefitPageData data=new BenefitPageData();data.benefits=calculationDao.selectBenefitList(conn,year);data.departments=DepartmentDao.getInstance().selectAll(conn);
			EmployeeSearchCondition c=new EmployeeSearchCondition();c.setSearchTarget("ALL");c.setKeyword(keyword==null?"":keyword);c.setEmploymentType("");c.setStatus("");c.setPage(1);c.setPageSize(100);c.setDepartmentId(departmentId);
			data.employees=EmployeeDao.getInstance().selectListByCondition(conn,c);
			if(calculationId!=null){RetirementCalculation calc=calculationDao.selectById(conn,calculationId);if(calc!=null){data.form=fromModel(calc);data.form.getIncomeEntries().addAll(RetirementIncomeEntryDao.getInstance().selectByCalcId(conn,calculationId));data.form.getTaxDeferrals().addAll(RetirementTaxDeferralDao.getInstance().selectByCalcId(conn,calculationId));}}
			return data;
		}catch(SQLException e){throw new RuntimeException(e);}
	}
	public RetirementBenefitForm prepareNew(int employeeId){
		try(Connection conn=ConnectionProvider.getConnection()){
			erp.employees.model.Employee e=EmployeeDao.getInstance().selectById(conn,employeeId);if(e==null)throw new IllegalArgumentException("사원을 찾을 수 없습니다.");
			RetirementBenefitForm f=new RetirementBenefitForm();f.setEmployeeId(employeeId);if(e.getJoinDate()!=null)f.setStartDate(new java.text.SimpleDateFormat("yyyy-MM-dd").format(e.getJoinDate()));if(e.getRetireDate()!=null)f.setEndDate(new java.text.SimpleDateFormat("yyyy-MM-dd").format(e.getRetireDate()));return f;
		}catch(SQLException e){throw new RuntimeException(e);}
	}
	public void calculate(RetirementBenefitForm f){
		LocalDate start=LocalDate.parse(f.getStartDate()),end=LocalDate.parse(f.getEndDate());if(end.isBefore(start))throw new IllegalArgumentException("정산 종료일은 시작일보다 빠를 수 없습니다.");
		int days=(int)ChronoUnit.DAYS.between(start,end)+1-f.getExcludedDays();if(days<0)throw new IllegalArgumentException("제외일수를 확인하세요.");f.setServiceDays(days);f.setServiceYears(days/365);f.setTaxYear(end.getYear());
		long salary=0,salaryDays=0,etc=0;for(RetirementIncomeEntry e:f.getIncomeEntries()){if(e.isSalaryData()){salary+=e.getAmount();salaryDays+=e.getCalcDays()==null?0:Math.round(e.getCalcDays());}else etc+=e.getThreeMonthAmount();}
		f.setSalaryTotal(salary);f.setSalaryDaysTotal(salaryDays);f.setThreeMonthTotal(salary+etc);f.setDailyAverage(salaryDays==0?0:f.getThreeMonthTotal()/salaryDays);
		long base=Math.max(f.getDailyAverage(),f.getDailyOrdinary());long calculated=f.getRetirementIncome()>0?f.getRetirementIncome():Math.round(base*30.0*days/365.0)+f.getCompensation()+f.getDismissalAllowance();f.setRetirementIncome(calculated);
		f.setTaxablePayment(Math.max(0,calculated-f.getTaxFreeRetirement()));f.setWithholdingTax(Math.max(0,f.getIncomeTax()+f.getLocalIncomeTax()+f.getRuralTax()+f.getOtherDeduction()-f.getDeferredIncomeTax()-f.getDeferredLocalTax()));f.setNetPayment(Math.max(0,f.getTaxablePayment()-f.getWithholdingTax()));
	}
	public int save(RetirementBenefitForm f){calculate(f);if(f.getPaymentMethod()==null||f.getPaymentMethod().trim().isEmpty()||f.getPaymentDate()==null||f.getPaymentDate().isEmpty())throw new IllegalArgumentException("지급방법과 지급일을 입력하세요.");Connection conn=null;try{conn=ConnectionProvider.getConnection();conn.setAutoCommit(false);int id=f.getCalculationId()>0?f.getCalculationId():calculationDao.nextId(conn);if(f.getCalculationId()>0)calculationDao.delete(conn,id);RetirementCalculation c=toModel(f,id);calculationDao.insertWithId(conn,c);for(RetirementIncomeEntry e:f.getIncomeEntries()){e.setRetirementCalculationId(id);RetirementIncomeEntryDao.getInstance().insert(conn,e);}for(RetirementTaxDeferral d:f.getTaxDeferrals()){d.setRetirementCalculationId(id);RetirementTaxDeferralDao.getInstance().insert(conn,d);}conn.commit();return id;}catch(SQLException e){JdbcUtil.rollback(conn);throw new RuntimeException(e);}finally{JdbcUtil.close(conn);}}
	public void delete(Integer id,boolean all){Connection conn=null;try{conn=ConnectionProvider.getConnection();conn.setAutoCommit(false);if(all)calculationDao.deleteAll(conn);else if(id!=null)calculationDao.delete(conn,id);conn.commit();}catch(SQLException e){JdbcUtil.rollback(conn);throw new RuntimeException(e);}finally{JdbcUtil.close(conn);}}
	private RetirementCalculation toModel(RetirementBenefitForm f,int id)throws SQLException{RetirementCalculation c=new RetirementCalculation();c.setRetirementCalculationId(id);c.setEmployeeId(f.getEmployeeId());c.setCalcType("INTERIM".equals(f.getSettlementType())?"중간정산":"퇴직정산");try{java.text.SimpleDateFormat s=new java.text.SimpleDateFormat("yyyy-MM-dd");c.setCalcStartDate(s.parse(f.getStartDate()));c.setRetireDate(s.parse(f.getEndDate()));c.setPayDate(s.parse(f.getPaymentDate()));}catch(Exception e){throw new SQLException(e);}c.setServiceYears(f.getServiceYears());c.setServiceDays(f.getServiceDays());c.setExcludeDays(f.getExcludedDays());c.setCompensationAmt(f.getCompensation());c.setDismissalAmt(f.getDismissalAllowance());c.setTaxFreeRetireAmt(f.getTaxFreeRetirement());c.setPrepaidTaxAmt(f.getPrepaidTax());c.setTaxCreditAmt(f.getTaxCredit());c.setThreeMonthTotal(f.getThreeMonthTotal());c.setAvgMonthWage(f.getSalaryTotal()/3);c.setAvgDayWage(f.getDailyAverage());c.setOrdinaryDayWage(f.getDailyOrdinary());c.setRetireIncome(f.getRetirementIncome());c.setCalculatedTaxAmt(f.getCalculatedTax());c.setIncomeTax(f.getIncomeTax());c.setLocalIncomeTax(f.getLocalIncomeTax());c.setDeferredIncomeTax(f.getDeferredIncomeTax());c.setDeferredLocalTax(f.getDeferredLocalTax());c.setSpecialRuralTax(f.getRuralTax());c.setOtherDeductAmt(f.getOtherDeduction());c.setTaxableRetireAmt(f.getTaxablePayment());c.setWithholdingTaxAmt(f.getWithholdingTax());c.setActualPayAmt(f.getNetPayment());c.setPayMethod(f.getPaymentMethod());return c;}
	private RetirementBenefitForm fromModel(RetirementCalculation c){RetirementBenefitForm f=new RetirementBenefitForm();java.text.SimpleDateFormat s=new java.text.SimpleDateFormat("yyyy-MM-dd");f.setCalculationId(c.getRetirementCalculationId());f.setEmployeeId(c.getEmployeeId());f.setSettlementType("중간정산".equals(c.getCalcType())?"INTERIM":"RETIREMENT");f.setStartDate(s.format(c.getCalcStartDate()));f.setEndDate(s.format(c.getRetireDate()));f.setServiceYears(c.getServiceYears());f.setServiceDays(c.getServiceDays());f.setExcludedDays(c.getExcludeDays());f.setCompensation(c.getCompensationAmt());f.setDismissalAllowance(c.getDismissalAmt());f.setTaxFreeRetirement(c.getTaxFreeRetireAmt());f.setPrepaidTax(c.getPrepaidTaxAmt());f.setTaxCredit(c.getTaxCreditAmt());f.setThreeMonthTotal(c.getThreeMonthTotal());f.setDailyAverage(c.getAvgDayWage());f.setDailyOrdinary(c.getOrdinaryDayWage());f.setRetirementIncome(c.getRetireIncome());f.setCalculatedTax(c.getCalculatedTaxAmt());f.setIncomeTax(c.getIncomeTax());f.setLocalIncomeTax(c.getLocalIncomeTax());f.setDeferredIncomeTax(c.getDeferredIncomeTax());f.setDeferredLocalTax(c.getDeferredLocalTax());f.setRuralTax(c.getSpecialRuralTax());f.setOtherDeduction(c.getOtherDeductAmt());f.setTaxablePayment(c.getTaxableRetireAmt());f.setWithholdingTax(c.getWithholdingTaxAmt());f.setNetPayment(c.getActualPayAmt());f.setPaymentMethod(c.getPayMethod());f.setPaymentDate(s.format(c.getPayDate()));return f;}
	public static class BenefitPageData{private List<RetirementBenefitListItem> benefits;private List<EmployeeListItem> employees;private Object departments;private RetirementBenefitForm form;public List<RetirementBenefitListItem> getBenefits(){return benefits;}public List<EmployeeListItem> getEmployees(){return employees;}public Object getDepartments(){return departments;}public RetirementBenefitForm getForm(){return form;}}
}
