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
	 * 返回会员的健康报告，包含[RealID],[ReportType],[Content],[EventTime],[Remarks],[CreateTime],[Others],[ReportID]键值。
	 * @param memberid 会员ID
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码
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
