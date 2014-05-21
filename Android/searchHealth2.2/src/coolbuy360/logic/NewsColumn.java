/**
 * 
 */
package coolbuy360.logic;

import java.util.List;
import java.util.Map;

/**
 * @author yangxc
 *	������Ѷ��Ŀ
 */
public class NewsColumn {
	
	/**
	 * ����ר���б�����[ColumnID],[ColumnName]��ֵ��
	 * @param pageSize ������Ѷ��Ŀ��
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
