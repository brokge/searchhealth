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
	 * ����һ��push�û���¼
	 * @param obj ��������ʵ��
	 *      obj���������baiduuserid,memberid,tags,devicenum,devicetype,isuse��
	 * @return
	 */
	public static CommandResult insert(Map<String, String> obj)
	{
	    CommandResult result = new CommandResult(false, "����ʧ��"); 	    
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
				result.setMessage("�����ɹ���");
				return result;
			} else {
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
	    	result.setMessage("δ֪��������ʧ�ܡ�");
			return result;
		}
	}	
	
	/**
	 * ����һ��push_user��¼
	 * @param obj push_user����ʵ��
	 *      ʵ�������� ������memberid,createtime,tags,isuse,pushuserid��
	 * @return        
	 */
	public static CommandResult update(Map<String, String> obj)
	{
	    CommandResult result = new CommandResult(false, "���³ɹ�");		    
	    List<QLUpdateField> updateFields = new ArrayList<QLUpdateField>();
	    updateFields.add(new QLUpdateField("MemberId", obj.get("memberid")));
	    updateFields.add(new QLUpdateField("CreateTime", obj.get("createtime"), "datetime", true));//���һ�θ��µļ�¼
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
	    	result.setMessage("δ֪���󣬸���ʧ�ܡ�");
			return result;
		}		
		return result;
	}
	/**
	 * pushuser�Ĺ������������ݴ洢���̽��и��»�������
	 * @param obj
	 *    ��Ҫ����ȥ��pushvalue
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
	 * ����������ϢID��ѯ������ϸ��Ϣ������һ����¼
	 * @param promotionID ���ͻID
	 * @return
	 */
	public static List<Map<String,String>> getMessageInfo(String messageID)
	{
		String strQL = ConstantsSetting.QLGetPushMessage;
		strQL = String.format(strQL, messageID);
		return ConstantsSetting.qLGetList(0, 0,strQL, null);
	}
	
}
