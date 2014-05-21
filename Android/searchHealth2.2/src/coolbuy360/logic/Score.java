/**
 * 
 */
package coolbuy360.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员健康值
 * @author yangxc
 *
 */
public class Score {
	
	/**
	 * 返回会员健康值日志列表，包含[Score],[EventType],[CreateTime]键值。
	 * @param memberid 会员ID
	 * @param pagesize 每页条数，0表示按系统默认值
	 * @param starttime 翻页起始时间戳，若为空字符，则取第一页数据
	 * @return
	 */
	public static List<Map<String,String>> getLogs(String memberid, int pagesize, String starttime)
	{
		String strQL = "plugin DrugCenter.Logic.V1 Member GetScoreLogs($memberid$,$pagesize$,$starttime$)";
		Map<String,String> postValue = new HashMap<String, String>();
		postValue.put("memberid", memberid);
		if (starttime == null) {
			starttime = "";
		}
		postValue.put("starttime", starttime);
		return ConstantsSetting.qLGetListByProcedure(pagesize, 0, strQL, postValue);
	}
}
