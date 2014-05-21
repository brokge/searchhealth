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
 * 健康足迹（HealthCalendar）
 * @author Administrator
 *
 */
public class HealthFootprint {
	private static HealthFootprint healthFootprint;	
	/**
	 * 私有构造函数，保证单例模式
	 * @param context
	 */
	private HealthFootprint()
	{		
	}
	/**
	 * 获取单例对象
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
	 * 根据memberId查询健康足迹列表，返回多条记录
	 * @param memberId会员id
	 * @return
	 */
	public List<Map<String,String>> getHFootprintList(String memberId)
	{
		String strQL = ConstantsSetting.QLGetHCalendarByMemberID;
		strQL = String.format(strQL, memberId);
		return ConstantsSetting.qLGetList(0, 0,strQL, null);
	}
	/**
	 * 分页查询健康日志相关信息
	 * @param memberId
	 *        会员id
	 * @param pageSize
	 *        每页显示的条数
	 * @param pageIndex
	 *        页的索引
	 * @return 返回操作结果 List<map<String,String>>
	 */
	public List<Map<String,String>> getHFootprintList(String memberId,int pageSize,int pageIndex)
	{
		String strQL = ConstantsSetting.QLGetHCalendarByMemberID;
		strQL = String.format(strQL, memberId);
		return ConstantsSetting.qLGetList(pageSize,pageIndex,strQL, null);
	}	
	/**
	 * 根据memberId和id查询健康足迹详细信息，返回一条记录
	 * @param memberId会员id
	 * @param ID 健康足迹的详细信息id
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
	 * 删除一条过敏记录。
	 * @param id 过敏记录ID
	 * @return
	 */
	public  CommandResult Delete(String ID)
	{
	    CommandResult result = new CommandResult(false, "删除失败");
	    
	    String strQL = "";	
		Map<String, String> postValue = new HashMap<String,String>();		
		strQL = HealthFootprint.QLDeleteHCalendarInfoByMemberID;		
		postValue.put("id", ID);	
		
		try {			
			Boolean insertResult = ConstantsSetting.qlDelete(strQL, postValue);
			if(insertResult){
				result.setResult(true);
				result.setMessage("删除成功。");
				return result;
			} else {
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
	    	result.setMessage("未知错误，删除失败。");
			return result;
		}
	}	
	public static String QLDeleteHCalendarInfoByMemberID="delete MBR_HealthCalendar WHERE ID=$id$";
	
	/**
	 * 提交一条反馈意见，并得到操作结果
	 * @param context
	 * @param content 反馈内容
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
	 * 更新一条过敏记录
	 * @param obj 过敏记录对象实例
	 * @return
	 */
	public  CommandResult update(Map<String, String> obj)
	{
	    CommandResult result = new CommandResult(false, "保存失败");
	    // 验证数据有效性
	    CommandResult validateResult = validate(obj);
	    if(!validateResult.getResult()) {
	    	return validateResult;
	    } 
	    
	    String memberid = User.getMemberID();
	    if(memberid==null){
	    	result.setMessage("请重新登录。");
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
	    	result.setMessage("未知错误，保存失败。");
			return result;
		}
		
		return result;
	}
	
	/**
	 * 验证数据有效性
	 * @param obj
	 * @return
	 */
	private  CommandResult validate(Map<String, String> obj) 
	{
		CommandResult result = new CommandResult(false, "数据格式验证错误。");
		
		String allergen = obj.get("eventtime");	
		
		String eventtime = obj.get("eventtime");
		if(eventtime==null || eventtime.equals("")){
			result.setMessage("请选择过敏发生时间。");
			return result;			
		} else {
			try {
				String otString = Util.getDateFormat(eventtime, "yyyy-MM-dd", "yyyy-MM-dd");
				if(otString.equals("")){
					result.setMessage("事件发生时间格式不正确。");
					return result;
				}
			} catch (Exception e) {
				result.setMessage("事件发生时间格式不正确。");
				return result;
			}
		}

		String summarize = obj.get("summarize");
		if(summarize!=null && summarize.length()>500) {
			result.setMessage("概述字数不能超过500个字。");
			return result;
		}

		String remarks = obj.get("remarks");
		if(remarks!=null && remarks.length()>500) {
			result.setMessage("附加说明字数不能超过500个字。");
			return result;
		}
		
		result.setResult(true);
		result.setMessage("");		
		return result;
	}
	
	

}
