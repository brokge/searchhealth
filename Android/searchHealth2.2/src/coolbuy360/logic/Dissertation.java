/**
 * 
 */
package coolbuy360.logic;

import java.util.List;
import java.util.Map;

/**
 * 专题
 * @author yangxc
 *
 */
public class Dissertation {
	
	/**
	 * 返回专题列表，包含[DissertationID],[Title],[Image],[Resume]键值。
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码
	 * @return
	 */
	public static List<Map<String,String>> getList(int pageSize, int pageIndex)
	{
		String strQL = "plugin DrugCenter.Logic.V1 News GetDissertations($pagesize$,$pageindex$)";
		return ConstantsSetting.qLGetListByProcedure(pageSize, pageIndex, strQL, null);
	}
}
