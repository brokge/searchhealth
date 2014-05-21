/**
 * 
 */
package coolbuy360.logic;

import java.util.List;
import java.util.Map;

/**
 * 红榜信息
 * @author Administrator
 *
 */
public class RedDrug {

	/**
	 * 查询红榜集合，包含键值：RedListID、DrugID、PubTime、UpTime、SamplingResults、drugname。所有键值小写。
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> getList(int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLRedDrugs;
		strQL = String.format(strQL);
		return ConstantsSetting.qLGetList(pageSize, pageIndex,strQL, null);		
	}
	
	/**
	 * 根据红榜ID查询红榜详细信息，返回一条记录
	 * @param redListID 红榜ID
	 * @return
	 */
	public static List<Map<String,String>> getInfo(String redListID)
	{
		String strQL = ConstantsSetting.QLRedDrugInfoByID;
		strQL = String.format(strQL, redListID);
		return ConstantsSetting.qLGetList(0, 0,strQL, null);
	}
}
