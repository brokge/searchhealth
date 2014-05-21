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
 * 会员疾病史
 * @author yangxc
 *
 */
public class MemberDisease {

	/**
	 * 根据会员ID返回疾病记录列表，包含ID、DiseaseName、DiagnoseTime、IsContagious, IsHereditary, Remarks、CreateTime键值。
	 * @param memberid 会员ID
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> getListByMemberID(String memberid, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLMemberDiseasesByMemberID;
		strQL = String.format(strQL, memberid);
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);
	}
	
	/**
	 * 根据会员ID返回传染病记录列表，包含ID、DiseaseName、DiagnoseTime、IsContagious, IsHereditary, Remarks、CreateTime键值。
	 * @param memberid 会员ID
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> getContagiousListByMemberID(String memberid, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLMemberContagioussByMemberID;
		strQL = String.format(strQL, memberid);
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);
	}
	
	/**
	 * 根据会员ID返回遗传病记录列表，包含ID、DiseaseName、DiagnoseTime、IsContagious, IsHereditary, Remarks、CreateTime键值。
	 * @param memberid 会员ID
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> getHereditaryListByMemberID(String memberid, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLMemberHereditarysByMemberID;
		strQL = String.format(strQL, memberid);
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);
	}
	
	/**
	 * 删除一条疾病记录。
	 * @param id 疾病记录ID
	 * @return
	 */
	public static CommandResult Delete(String id)
	{
	    CommandResult result = new CommandResult(false, "删除失败");
	    
		String strQL = ConstantsSetting.qLDeleteMemberDisease;
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
	 * 新增一条疾病记录
	 * @param obj 疾病记录对象实例
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
		
		String diseasename = obj.get("diseasename");
		if(diseasename==null || diseasename.equals("")){
			result.setMessage("请选择或填写疾病名称。");
			return result;			
		} else if(diseasename.length()>50) {
			result.setMessage("疾病名称字数不能超过50个字。");
			return result;
		}
		
		String diagnosetime = obj.get("diagnosetime");
		if(diagnosetime==null || diagnosetime.equals("")){
			result.setMessage("请选择确诊时间。");
			return result;			
		} else {
			try {
				String otString = Util.getDateFormat(diagnosetime, "yyyy-MM-dd", "yyyy-MM-dd");
				if(otString.equals("")){
					result.setMessage("确诊时间格式不正确。");
					return result;
				}
			} catch (Exception e) {
				result.setMessage("确诊时间格式不正确。");
				return result;
			}
		}

		String iscontagious = obj.get("iscontagious");
		if(iscontagious==null || iscontagious.equals("")){
			result.setMessage("请选择是否传染病。");
			return result;			
		} 

		String ishereditary = obj.get("ishereditary");
		if(ishereditary==null || ishereditary.equals("")){
			result.setMessage("请选择是否遗传病。");
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
