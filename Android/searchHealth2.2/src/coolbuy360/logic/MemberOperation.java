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
 * 会员手术记录
 * @author yangxc
 *
 */
public class MemberOperation {

	/**
	 * 根据会员ID返回手术记录列表，包含ID、OperationName、ImplementTime、 Remarks、CreateTime键值。
	 * @param memberid 会员ID
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> getListByMemberID(String memberid, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLMemberOperationsByMemberID;
		strQL = String.format(strQL, memberid);
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);
	}
	
	/**
	 * 删除一条手术记录。
	 * @param id 手术记录ID
	 * @return
	 */
	public static CommandResult Delete(String id)
	{
	    CommandResult result = new CommandResult(false, "删除失败");
	    
		String strQL = ConstantsSetting.qLDeleteMemberOperation;
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
	 * 新增一条手术记录
	 * @param obj 手术记录对象实例
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
	 * 更新一条手术记录
	 * @param obj 手术记录对象实例
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
		
		String operationname = obj.get("operationname");
		if(operationname==null || operationname.equals("")){
			result.setMessage("请填写手术名称。");
			return result;			
		} else if(operationname.length()>100) {
			result.setMessage("手术名称字数不能超过100个字。");
			return result;
		}
		
		String implementtime = obj.get("implementtime");
		if(implementtime==null || implementtime.equals("")){
			result.setMessage("请选择手术实施时间。");
			return result;			
		} else {
			try {
				String otString = Util.getDateFormat(implementtime, "yyyy-MM-dd", "yyyy-MM-dd");
				if(otString.equals("")){
					result.setMessage("手术实施时间格式不正确。");
					return result;
				}
			} catch (Exception e) {
				result.setMessage("手术实施时间格式不正确。");
				return result;
			}
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
