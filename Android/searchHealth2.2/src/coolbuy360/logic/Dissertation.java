/**
 * 
 */
package coolbuy360.logic;

import java.util.List;
import java.util.Map;

/**
 * ר��
 * @author yangxc
 *
 */
public class Dissertation {
	
	/**
	 * ����ר���б�����[DissertationID],[Title],[Image],[Resume]��ֵ��
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ��
	 * @return
	 */
	public static List<Map<String,String>> getList(int pageSize, int pageIndex)
	{
		String strQL = "plugin DrugCenter.Logic.V1 News GetDissertations($pagesize$,$pageindex$)";
		return ConstantsSetting.qLGetListByProcedure(pageSize, pageIndex, strQL, null);
	}
}
