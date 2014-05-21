package coolbuy360.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.string;

import coolbuy360.service.CommandResult;
import coolbuy360.service.MD5;
import coolbuy360.service.Util;



public class PushUser {

	/**
	 * 新增一条push用户记录
	 * @param obj 新增对象实例
	 *      obj对象包括（baiduuserid,memberid,tags,devicenum,devicetype,isuse）
	 * @return
	 */
	public static CommandResult insert(Map<String, String> obj)
	{
	    CommandResult result = new CommandResult(false, "新增失败"); 	    
		String strQL = ConstantsSetting.QLInsertPUSH_User;
		//$baiduuserid$,$memberid$,$tags$,$devicenum$,$devicetype$,$createtime$,$isuse$		
		Map<String,String> postValue = new HashMap<String, String>();
		postValue.put("baiduuserid",obj.get("baiduuserid"));
		postValue.put("memberid", obj.get("memberid"));
		postValue.put("tags", obj.get("tags"));
		postValue.put("devicenum", obj.get("devicenum"));
		postValue.put("devicetype", obj.get("devicetype"));
		postValue.put("createtime", Util.getNowDate("yyyy-MM-dd HH:mm:ss"));
		postValue.put("isuse", obj.get("isuse"));		
		try {			
			Boolean insertResult = ConstantsSetting.qLInsert(strQL, postValue);
			if(insertResult){
				result.setResult(true);
				result.setMessage("新增成功。");
				return result;
			} else {
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
	    	result.setMessage("未知错误，新增失败。");
			return result;
		}
	}	
	
	/**
	 * 更新一条push_user记录
	 * @param obj push_user对象实例
	 *      实例对象中 包括（memberid,createtime,tags,isuse,pushuserid）
	 * @return        
	 */
	public static CommandResult update(Map<String, String> obj)
	{
	    CommandResult result = new CommandResult(false, "更新成功");		    
	    List<QLUpdateField> updateFields = new ArrayList<QLUpdateField>();
	    updateFields.add(new QLUpdateField("MemberId", obj.get("memberid")));
	    updateFields.add(new QLUpdateField("CreateTime", obj.get("createtime"), "datetime", true));//最后一次更新的记录
	    updateFields.add(new QLUpdateField("Tags", obj.get("tags")));
	    updateFields.add(new QLUpdateField("IsUse", obj.get("isuse")));	    
	    String whereString = "where PushUserID=" + obj.get("pushuserid");		
		try {			
			CommandResult updateResult = ConstantsSetting.QLUpdate("PUSH_User", updateFields, whereString, null);
			if(updateResult!=null) {
				return updateResult;
			} 
		} catch (Exception e) {
			// TODO: handle exception
	    	result.setMessage("未知错误，更新失败。");
			return result;
		}		
		return result;
	}
	/**
	 * pushuser的公共操作（根据存储过程进行更新或新增）
	 * @param obj
	 *    需要传过去的pushvalue
	 * @return
	 */
	public static Boolean PushUserHandle(Map<String, String> obj) {	
		String strQL = ConstantsSetting.QLPush_User;		
		Map<String,String> postValue = new HashMap<String, String>();
		postValue.put("baiduuserid",obj.get("baiduuserid"));
		postValue.put("memberid", obj.get("memberid"));
		postValue.put("tags", obj.get("tags"));
		postValue.put("devicenum", obj.get("devicenum"));
		postValue.put("devicetype", obj.get("devicetype"));	
		postValue.put("isuse", obj.get("isuse"));		
		List<Map<String, String>> result = ConstantsSetting.qLGetList(0, 0,
				strQL, obj);		
		if (result != null && result.size() > 0) {
			Map<String, String> values = result.get(0);
			String countReuslt = values.get("result").toString();
			if (countReuslt.equals("true")) {				
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}			
	}	
	
	/**
	 * 根据推送消息ID查询推送详细信息，返回一条记录
	 * @param promotionID 推送活动ID
	 * @return
	 */
	public static List<Map<String,String>> getMessageInfo(String messageID)
	{
		String strQL = ConstantsSetting.QLGetPushMessage;
		strQL = String.format(strQL, messageID);
		return ConstantsSetting.qLGetList(0, 0,strQL, null);
	}
	
}
