/**
 * 
 */
package coolbuy360.logic;

import java.util.List;
import java.util.Map;

/**
 * @author yangxc
 *	健康资讯栏目
 */
public class NewsColumn {
	
	/**
	 * 返回专题列表，包含[ColumnID],[ColumnName]键值。
	 * @param pageSize 健康资讯栏目数
	 * @return
	 */
	public static List<Map<String,String>> getList(int pageSize)
	{
		String strQL = "plugin DrugCenter.Logic.V1 News GetNewsColumns($pagesize$)";
		if (pageSize == 0) {
			pageSize = 4;
		}
		return ConstantsSetting.qLGetListByProcedure(pageSize, 0, strQL, null);
	}
}
