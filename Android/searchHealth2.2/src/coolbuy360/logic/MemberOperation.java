/**
 * 
 */
package coolbuy360.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import coolbuy360.service.CommandResult;
import coolbuy360.service.Util;

/**
 * ��Ա������¼
 * @author yangxc
 *
 */
public class MemberOperation {

	/**
	 * ���ݻ�ԱID����������¼�б�����ID��OperationName��ImplementTime�� Remarks��CreateTime��ֵ��
	 * @param memberid ��ԱID
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
	 * @return
	 */
	public static List<Map<String,String>> getListByMemberID(String memberid, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLMemberOperationsByMemberID;
		strQL = String.format(strQL, memberid);
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);
	}
	
	/**
	 * ɾ��һ��������¼��
	 * @param id ������¼ID
	 * @return
	 */
	public static CommandResult Delete(String id)
	{
	    CommandResult result = new CommandResult(false, "ɾ��ʧ��");
	    
		String strQL = ConstantsSetting.qLDeleteMemberOperation;
		strQL = String.format(strQL, id);
		
		try {			
			Boolean insertResult = ConstantsSetting.qlDelete(strQL, null);
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
	
	/**
	 * ����һ��������¼
	 * @param obj ������¼����ʵ��
	 * @return
	 */
	public static CommandResult insert(Map<String, String> obj)
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
	    
		String strQL = ConstantsSetting.QLInsertMemberOperation;

		Map<String,String> postValue = new HashMap<String, String>();
		postValue.put("memberid", memberid);
		postValue.put("operationname", obj.get("operationname"));
		postValue.put("implementtime", obj.get("implementtime"));
		postValue.put("remarks", obj.get("remarks"));
		postValue.put("createtime", Util.getNowDate("yyyy-MM-dd HH:mm:ss"));
		
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
	 * ����һ��������¼
	 * @param obj ������¼����ʵ��
	 * @return
	 */
	public static CommandResult update(Map<String, String> obj)
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
	    updateFields.add(new QLUpdateField("OperationName", obj.get("operationname")));
	    updateFields.add(new QLUpdateField("ImplementTime", obj.get("implementtime"), "datetime", true));
	    updateFields.add(new QLUpdateField("Remarks", obj.get("remarks")));
	    
	    String whereString = "where ID=" + obj.get("id");
		
		try {			
			CommandResult updateResult = ConstantsSetting.QLUpdate("MBR_Operation", updateFields, whereString, null);
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
	public static CommandResult validate(Map<String, String> obj) 
	{
		CommandResult result = new CommandResult(false, "���ݸ�ʽ��֤����");
		
		String operationname = obj.get("operationname");
		if(operationname==null || operationname.equals("")){
			result.setMessage("����д�������ơ�");
			return result;			
		} else if(operationname.length()>100) {
			result.setMessage("���������������ܳ���100���֡�");
			return result;
		}
		
		String implementtime = obj.get("implementtime");
		if(implementtime==null || implementtime.equals("")){
			result.setMessage("��ѡ������ʵʩʱ�䡣");
			return result;			
		} else {
			try {
				String otString = Util.getDateFormat(implementtime, "yyyy-MM-dd", "yyyy-MM-dd");
				if(otString.equals("")){
					result.setMessage("����ʵʩʱ���ʽ����ȷ��");
					return result;
				}
			} catch (Exception e) {
				result.setMessage("����ʵʩʱ���ʽ����ȷ��");
				return result;
			}
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
