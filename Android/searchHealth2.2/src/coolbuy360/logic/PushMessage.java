/**
 * 
 */
package coolbuy360.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

/**
 * @author yangxc
 *
 */
public class PushMessage {
	
	/**
	 * 返回专题下的文章列表，包含[MessageID],[Title],[Content],[SendTime],[AndroidParameters]键值。
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码
	 * @return
	 */
	public static List<Map<String,String>> getList(Context context, int pageSize, int pageIndex)
	{
		String strQL = "plugin DrugCenter.Logic.V1 PushCommon GetPushMessages($tags$,$baiduuserid$,$pagesize$,$pageindex$)";
		Map<String,String> postValue = new HashMap<String, String>();
		/*PushConfig pushConf = new PushConfig(context);
		String baiduuerid = pushConf.getBaiduUserID(context);
		String tags = pushConf.getTags(context);*/
		String baiduuerid = PushConfig.getValue(context, PushConfig.BaiduUserID);
		String tags = PushConfig.getValue(context, PushConfig.Tags);
		if (baiduuerid == null || baiduuerid.equals(""))
			baiduuerid = "0";
		if (tags == null || tags.equals(""))
			tags = "0";
		postValue.put("tags", tags);
		postValue.put("baiduuserid", baiduuerid);
		return ConstantsSetting.qLGetListByProcedure(pageSize, pageIndex, strQL, postValue);
	}

}
