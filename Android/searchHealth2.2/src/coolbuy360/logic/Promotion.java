/**
 * 
 */
package coolbuy360.logic;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import coolbuy360.service.Service;
import coolbuy360.service.TimestampException;
import coolbuy360.service.Util;
import coolbuy360.service.Service.CHttpConnectionCallback;

/**
 * 药店促销活动
 * @author yangxc
 *
 */
public class Promotion {
	
	/**
	 * 根据药店返回促销活动集合。
	 * @param drugStoreID 药店ID
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> getListByDrugStore(String drugStoreID, int pageSize, int pageIndex)
	{
		/*String strQL = ConstantsSetting.QLPromotionsByDrugStoreID;
		GregorianCalendar gc = new GregorianCalendar(); 
		gc.setTime(new Date()); 
		gc.add(Calendar.DATE, -1);
		String today = Util.getNowDate("yyyy-MM-dd HH:mm:ss");
		String yesterday = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(gc.getTime());		
		strQL = String.format(strQL, drugStoreID, today, yesterday);		
		return ConstantsSetting.qLGetList(pageSize, pageIndex,strQL, null);	*/
		
		String strQL = ConstantsSetting.QLPromotionsByDrugStoreID;
		Map<String,String> postValue = new HashMap<String, String>();
		postValue.put("storeid", drugStoreID);
		postValue.put("pagesize", pageSize + "");
		postValue.put("pageindex", pageIndex + "");
		Service service = new Service();	
		CHttpConnectionCallback callback = service.Do(strQL, postValue);
		String result=callback.get_JSON();
		if(result.contains("请求的timestamp时间")){
			throw new TimestampException("客户端时间与服务器时间不一致");
		}
		return callback.getLists();
	}
	
	/**
	 * 根据促销活动ID查询促销详细信息，返回一条记录
	 * @param promotionID 促销活动ID
	 * @return
	 */
	public static List<Map<String,String>> getInfo(String promotionID)
	{
		String strQL = ConstantsSetting.QLPromotionInfo;
		strQL = String.format(strQL, promotionID);
		return ConstantsSetting.qLGetList(0, 0,strQL, null);
	}
}
