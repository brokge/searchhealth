/**
 * 
 */
package coolbuy360.logic;

import java.util.List;
import java.util.Map;

/**
 * �ڰ���Ϣ
 * @author yangxc
 *
 */
public class BlackDrug {

	/**
	 * ��ѯ�ڰ񼯺ϣ�������ֵ��BlackListID��DrugID��PubTime��DangerLever��Danger��drugname�����м�ֵСд��
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
	 * @return
	 */
	public static List<Map<String,String>> getList(int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLBlackDrugs;
		strQL = String.format(strQL);
		return ConstantsSetting.qLGetList(pageSize, pageIndex,strQL, null);		
	}
	
	/**
	 * ���ݺڰ�ID��ѯ�ڰ���ϸ��Ϣ������һ����¼
	 * @param blackListID �ڰ�ID
	 * @return
	 */
	public static List<Map<String,String>> getInfo(String blackListID)
	{
		String strQL = ConstantsSetting.QLBlackDrugInfoByID;
		strQL = String.format(strQL, blackListID);
		return ConstantsSetting.qLGetList(0, 0,strQL, null);
	}
}
