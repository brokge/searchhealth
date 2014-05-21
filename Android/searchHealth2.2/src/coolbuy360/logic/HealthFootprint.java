/**
 * copyRight habei
 */
package coolbuy360.logic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import coolbuy360.service.CommandResult;
import coolbuy360.service.Util;

import android.content.Context;
import android.util.Log;


/**
 * �����㼣��HealthCalendar��
 * @author Administrator
 *
 */
public class HealthFootprint {
	private static HealthFootprint healthFootprint;	
	/**
	 * ˽�й��캯������֤����ģʽ
	 * @param context
	 */
	private HealthFootprint()
	{		
	}
	/**
	 * ��ȡ��������
	 * @param context
	 * @return
	 */	
	public static HealthFootprint getHealthFootprintInstance()
	{
		if(healthFootprint==null)
		{			
			healthFootprint=new HealthFootprint();			
		}		
		return healthFootprint;		
	}	
	
	/**
	 * ����memberId��ѯ�����㼣�б����ض�����¼
	 * @param memberId��Աid
	 * @return
	 */
	public List<Map<String,String>> getHFootprintList(String memberId)
	{
		String strQL = ConstantsSetting.QLGetHCalendarByMemberID;
		strQL = String.format(strQL, memberId);
		return ConstantsSetting.qLGetList(0, 0,strQL, null);
	}
	/**
	 * ��ҳ��ѯ������־�����Ϣ
	 * @param memberId
	 *        ��Աid
	 * @param pageSize
	 *        ÿҳ��ʾ������
	 * @param pageIndex
	 *        ҳ������
	 * @return ���ز������ List<map<String,String>>
	 */
	public List<Map<String,String>> getHFootprintList(String memberId,int pageSize,int pageIndex)
	{
		String strQL = ConstantsSetting.QLGetHCalendarByMemberID;
		strQL = String.format(strQL, memberId);
		return ConstantsSetting.qLGetList(pageSize,pageIndex,strQL, null);
	}	
	/**
	 * ����memberId��id��ѯ�����㼣��ϸ��Ϣ������һ����¼
	 * @param memberId��Աid
	 * @param ID �����㼣����ϸ��Ϣid
	 * @return
	 */
	public List<Map<String,String>> getHFootprintInfo(int memberId,int ID)
	{
		String strQL = ConstantsSetting.QLGetHCalendarInfoByID;
		strQL = String.format(strQL, memberId,ID);
		return ConstantsSetting.qLGetList(0, 0,strQL, null);
	}	
	
	
/*	public Boolean delete(int ID)
	{	
		String strQL = "";	
		Map<String, String> postValue = new HashMap<String,String>();		
		strQL = HealthFootprint.QLDeleteHCalendarInfoByMemberID;		
		postValue.put("id", ID+"");			
		Log.i("chenlinwei", strQL);
		Log.i("chenlinwei", postValue+"");
		return ConstantsSetting.qLInsert(strQL, postValue);		
	}
	*/
	
	/**
	 * ɾ��һ��������¼��
	 * @param id ������¼ID
	 * @return
	 */
	public  CommandResult Delete(String ID)
	{
	    CommandResult result = new CommandResult(false, "ɾ��ʧ��");
	    
	    String strQL = "";	
		Map<String, String> postValue = new HashMap<String,String>();		
		strQL = HealthFootprint.QLDeleteHCalendarInfoByMemberID;		
		postValue.put("id", ID);	
		
		try {			
			Boolean insertResult = ConstantsSetting.qlDelete(strQL, postValue);
			if(insertResult){
				result.setResult(true);
				result.setMessage("ɾ���ɹ���");
				return result;
			} else {
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
	    	result.setMessage("δ֪����ɾ��ʧ�ܡ�");
			return result;
		}
	}	
	public static String QLDeleteHCalendarInfoByMemberID="delete MBR_HealthCalendar WHERE ID=$id$";
	
	/**
	 * �ύһ��������������õ��������
	 * @param context
	 * @param content ��������
	 * @return
	 */
	public  Boolean insert(String memberid,String eventTime,String summarize,String remarks)
	{
		String strQL = "";	
		Map<String, String> postValue = new HashMap<String,String>();		
		strQL = ConstantsSetting.QLInsertOneHCalendarInfoByMemberID;		
		postValue.put("memberid", memberid);
		postValue.put("eventtime", eventTime);
		postValue.put("summarize", summarize);
		postValue.put("remarks", remarks);
		Date date = new Date();
		SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		String times = from.format(date);
		postValue.put("createtime", times);
		Log.i("chenlinwei", strQL);
		Log.i("chenlinwei", postValue+"");
		return ConstantsSetting.qLInsert(strQL, postValue);
	}
	/**
	 * ����һ��������¼
	 * @param obj ������¼����ʵ��
	 * @return
	 */
	public  CommandResult update(Map<String, String> obj)
	{
	    CommandResult result = new CommandResult(false, "����ʧ��");
	    // ��֤������Ч��
	    CommandResult validateResult = validate(obj);
	    if(!validateResult.getResult()) {
	    	return validateResult;
	    } 
	    
	    String memberid = User.getMemberID();
	    if(memberid==null){
	    	result.setMessage("�����µ�¼��");
	    	return result;
	    }	    
	    List<QLUpdateField> updateFields = new ArrayList<QLUpdateField>();
	    updateFields.add(new QLUpdateField("EventTime", obj.get("eventtime"), "datetime", true));
	    updateFields.add(new QLUpdateField("Summarize", obj.get("summarize")));
	    updateFields.add(new QLUpdateField("Remarks", obj.get("remarks")));	    
	    String whereString = " where ID=" + obj.get("id");
		
		try {			
			CommandResult updateResult = ConstantsSetting.QLUpdate("MBR_HealthCalendar", updateFields, whereString, null);
			if(updateResult!=null) {
				return updateResult;
			} 
		} catch (Exception e) {
			// TODO: handle exception
	    	result.setMessage("δ֪���󣬱���ʧ�ܡ�");
			return result;
		}
		
		return result;
	}
	
	/**
	 * ��֤������Ч��
	 * @param obj
	 * @return
	 */
	private  CommandResult validate(Map<String, String> obj) 
	{
		CommandResult result = new CommandResult(false, "���ݸ�ʽ��֤����");
		
		String allergen = obj.get("eventtime");	
		
		String eventtime = obj.get("eventtime");
		if(eventtime==null || eventtime.equals("")){
			result.setMessage("��ѡ���������ʱ�䡣");
			return result;			
		} else {
			try {
				String otString = Util.getDateFormat(eventtime, "yyyy-MM-dd", "yyyy-MM-dd");
				if(otString.equals("")){
					result.setMessage("�¼�����ʱ���ʽ����ȷ��");
					return result;
				}
			} catch (Exception e) {
				result.setMessage("�¼�����ʱ���ʽ����ȷ��");
				return result;
			}
		}

		String summarize = obj.get("summarize");
		if(summarize!=null && summarize.length()>500) {
			result.setMessage("�����������ܳ���500���֡�");
			return result;
		}

		String remarks = obj.get("remarks");
		if(remarks!=null && remarks.length()>500) {
			result.setMessage("����˵���������ܳ���500���֡�");
			return result;
		}
		
		result.setResult(true);
		result.setMessage("");		
		return result;
	}
	
	

}
