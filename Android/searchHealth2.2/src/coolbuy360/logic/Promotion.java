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
 * ҩ������
 * @author yangxc
 *
 */
public class Promotion {
	
	/**
	 * ����ҩ�귵�ش�������ϡ�
	 * @param drugStoreID ҩ��ID
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
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
		if(result.contains("�����timestampʱ��")){
			throw new TimestampException("�ͻ���ʱ���������ʱ�䲻һ��");
		}
		return callback.getLists();
	}
	
	/**
	 * ���ݴ����ID��ѯ������ϸ��Ϣ������һ����¼
	 * @param promotionID �����ID
	 * @return
	 */
	public static List<Map<String,String>> getInfo(String promotionID)
	{
		String strQL = ConstantsSetting.QLPromotionInfo;
		strQL = String.format(strQL, promotionID);
		return ConstantsSetting.qLGetList(0, 0,strQL, null);
	}
}
