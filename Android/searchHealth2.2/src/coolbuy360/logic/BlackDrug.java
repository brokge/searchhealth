/**
 * 
 */
package coolbuy360.logic;

import java.util.List;
import java.util.Map;

/**
 * 黑榜信息
 * @author yangxc
 *
 */
public class BlackDrug {

	/**
	 * 查询黑榜集合，包含键值：BlackListID、DrugID、PubTime、DangerLever、Danger、drugname。所有键值小写。
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> getList(int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLBlackDrugs;
		strQL = String.format(strQL);
		return ConstantsSetting.qLGetList(pageSize, pageIndex,strQL, null);		
	}
	
	/**
	 * 根据黑榜ID查询黑榜详细信息，返回一条记录
	 * @param blackListID 黑榜ID
	 * @return
	 */
	public static List<Map<String,String>> getInfo(String blackListID)
	{
		String strQL = ConstantsSetting.QLBlackDrugInfoByID;
		strQL = String.format(strQL, blackListID);
		return ConstantsSetting.qLGetList(0, 0,strQL, null);
	}
}
