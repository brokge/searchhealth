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
 * ��Ա����ʷ
 * @author yangxc
 *
 */
public class MemberDisease {

	/**
	 * ���ݻ�ԱID���ؼ�����¼�б�����ID��DiseaseName��DiagnoseTime��IsContagious, IsHereditary, Remarks��CreateTime��ֵ��
	 * @param memberid ��ԱID
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
	 * @return
	 */
	public static List<Map<String,String>> getListByMemberID(String memberid, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLMemberDiseasesByMemberID;
		strQL = String.format(strQL, memberid);
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);
	}
	
	/**
	 * ���ݻ�ԱID���ش�Ⱦ����¼�б�����ID��DiseaseName��DiagnoseTime��IsContagious, IsHereditary, Remarks��CreateTime��ֵ��
	 * @param memberid ��ԱID
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
	 * @return
	 */
	public static List<Map<String,String>> getContagiousListByMemberID(String memberid, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLMemberContagioussByMemberID;
		strQL = String.format(strQL, memberid);
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);
	}
	
	/**
	 * ���ݻ�ԱID�����Ŵ�����¼�б�����ID��DiseaseName��DiagnoseTime��IsContagious, IsHereditary, Remarks��CreateTime��ֵ��
	 * @param memberid ��ԱID
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
	 * @return
	 */
	public static List<Map<String,String>> getHereditaryListByMemberID(String memberid, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLMemberHereditarysByMemberID;
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
	    
		String strQL = ConstantsSetting.qLDeleteMemberDisease;
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
	    
		String strQL = ConstantsSetting.QLInsertMemberDisease;

		Map<String,String> postValue = new HashMap<String, String>();
		postValue.put("memberid", memberid);
		postValue.put("diseasename", obj.get("diseasename"));
		postValue.put("diagnosetime", obj.get("diagnosetime"));
		postValue.put("iscontagious", obj.get("iscontagious"));
		postValue.put("ishereditary", obj.get("ishereditary"));
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
	    updateFields.add(new QLUpdateField("DiseaseName", obj.get("diseasename")));
	    updateFields.add(new QLUpdateField("DiagnoseTime", obj.get("diagnosetime"), "datetime", true));
	    updateFields.add(new QLUpdateField("IsContagious", obj.get("iscontagious")));
	    updateFields.add(new QLUpdateField("IsHereditary", obj.get("ishereditary")));
	    updateFields.add(new QLUpdateField("Remarks", obj.get("remarks")));
	    
	    String whereString = "where ID=" + obj.get("id");
		
		try {			
			CommandResult updateResult = ConstantsSetting.QLUpdate("MBR_Disease", updateFields, whereString, null);
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
		
		String diseasename = obj.get("diseasename");
		if(diseasename==null || diseasename.equals("")){
			result.setMessage("��ѡ�����д�������ơ�");
			return result;			
		} else if(diseasename.length()>50) {
			result.setMessage("���������������ܳ���50���֡�");
			return result;
		}
		
		String diagnosetime = obj.get("diagnosetime");
		if(diagnosetime==null || diagnosetime.equals("")){
			result.setMessage("��ѡ��ȷ��ʱ�䡣");
			return result;			
		} else {
			try {
				String otString = Util.getDateFormat(diagnosetime, "yyyy-MM-dd", "yyyy-MM-dd");
				if(otString.equals("")){
					result.setMessage("ȷ��ʱ���ʽ����ȷ��");
					return result;
				}
			} catch (Exception e) {
				result.setMessage("ȷ��ʱ���ʽ����ȷ��");
				return result;
			}
		}

		String iscontagious = obj.get("iscontagious");
		if(iscontagious==null || iscontagious.equals("")){
			result.setMessage("��ѡ���Ƿ�Ⱦ����");
			return result;			
		} 

		String ishereditary = obj.get("ishereditary");
		if(ishereditary==null || ishereditary.equals("")){
			result.setMessage("��ѡ���Ƿ��Ŵ�����");
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
