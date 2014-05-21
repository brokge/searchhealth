/**
 * 
 */
package coolbuy360.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yangxc
 *
 */
public class MemberHealthReport {
	
	/**
	 * ���ػ�Ա�Ľ������棬����[RealID],[ReportType],[Content],[EventTime],[Remarks],[CreateTime],[Others],[ReportID]��ֵ��
	 * @param memberid ��ԱID
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ��
	 * @return
	 */
	public static List<Map<String,String>> getList(String memberid, int pageSize, int pageIndex)
	{
		String strQL = "plugin DrugCenter.Logic.V1 Member GetHealthReports($memberid$,$pagesize$,$pageindex$)";
		Map<String,String> postValue = new HashMap<String, String>();
		postValue.put("memberid", memberid);
		return ConstantsSetting.qLGetListByProcedure(pageSize, pageIndex, strQL, postValue);
	}
}
