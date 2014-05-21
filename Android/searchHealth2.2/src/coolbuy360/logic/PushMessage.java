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
	 * ����ר���µ������б�����[MessageID],[Title],[Content],[SendTime],[AndroidParameters]��ֵ��
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ��
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
