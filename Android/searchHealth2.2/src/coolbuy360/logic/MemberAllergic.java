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
 * 会员过敏史
 * @author yangxc
 *
 */
public class MemberAllergic {
	
	/**
	 * 根据会员ID返回过敏记录列表，包含ID、Allergen、OccurrenceTime、Symptom, Remarks、CreateTime键值。
	 * @param memberid 会员ID
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> getListByMemberID(String memberid, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLMemberAllergicsByMemberID;
		strQL = String.format(strQL, memberid);
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);
	}
	
	/**
	 * 删除一条过敏记录。
	 * @param id 过敏记录ID
	 * @return
	 */
	public static CommandResult Delete(String id)
	{
	    CommandResult result = new CommandResult(false, "删除失败");
	    
		String strQL = ConstantsSetting.qLDeleteMemberAllergic;
		strQL = String.format(strQL, id);
		
		try {			
			Boolean insertResult = ConstantsSetting.qlDelete(strQL, null);
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
	
	/**
	 * 新增一条过敏记录
	 * @param obj 过敏记录对象实例
	 * @return
	 */
	public static CommandResult insert(Map<String, String> obj)
	{
	    CommandResult result = new CommandResult(false, "新增失败");
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
	    
		String strQL = ConstantsSetting.QLInsertMemberAllergic;

		Map<String,String> postValue = new HashMap<String, String>();
		postValue.put("memberid", memberid);
		postValue.put("allergen", obj.get("allergen"));
		postValue.put("occurrencetime", obj.get("occurrencetime"));
		postValue.put("symptom", obj.get("symptom"));
		postValue.put("remarks", obj.get("remarks"));
		postValue.put("createtime", Util.getNowDate("yyyy-MM-dd HH:mm:ss"));
		
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
	 * 更新一条过敏记录
	 * @param obj 过敏记录对象实例
	 * @return
	 */
	public static CommandResult update(Map<String, String> obj)
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
	    updateFields.add(new QLUpdateField("Allergen", obj.get("allergen")));
	    updateFields.add(new QLUpdateField("OccurrenceTime", obj.get("occurrencetime"), "datetime", true));
	    updateFields.add(new QLUpdateField("Symptom", obj.get("symptom")));
	    updateFields.add(new QLUpdateField("Remarks", obj.get("remarks")));
	    
	    String whereString = "where ID=" + obj.get("id");
		
		try {			
			CommandResult updateResult = ConstantsSetting.QLUpdate("MBR_Allergic", updateFields, whereString, null);
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
	public static CommandResult validate(Map<String, String> obj) 
	{
		CommandResult result = new CommandResult(false, "数据格式验证错误。");
		
		String allergen = obj.get("allergen");
		if(allergen==null || allergen.equals("")){
			result.setMessage("请选择或填写过敏源。");
			return result;			
		} else if(allergen.length()>50) {
			result.setMessage("过敏源字数不能超过50个字。");
			return result;
		}
		
		String occurrenceTime = obj.get("occurrencetime");
		if(occurrenceTime==null || occurrenceTime.equals("")){
			result.setMessage("请选择过敏发生时间。");
			return result;			
		} else {
			try {
				String otString = Util.getDateFormat(occurrenceTime, "yyyy-MM-dd", "yyyy-MM-dd");
				if(otString.equals("")){
					result.setMessage("过敏发生时间格式不正确。");
					return result;
				}
			} catch (Exception e) {
				result.setMessage("过敏发生时间格式不正确。");
				return result;
			}
		}

		String symptom = obj.get("symptom");
		if(symptom!=null && symptom.length()>500) {
			result.setMessage("过敏症状字数不能超过500个字。");
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
