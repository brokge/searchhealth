/**
 * 
 */
package coolbuy360.logic;

import java.util.List;
import java.util.Map;

/**
 * �����Ϣ
 * @author Administrator
 *
 */
public class RedDrug {

	/**
	 * ��ѯ��񼯺ϣ�������ֵ��RedListID��DrugID��PubTime��UpTime��SamplingResults��drugname�����м�ֵСд��
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
	 * @return
	 */
	public static List<Map<String,String>> getList(int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLRedDrugs;
		strQL = String.format(strQL);
		return ConstantsSetting.qLGetList(pageSize, pageIndex,strQL, null);		
	}
	
	/**
	 * ���ݺ��ID��ѯ�����ϸ��Ϣ������һ����¼
	 * @param redListID ���ID
	 * @return
	 */
	public static List<Map<String,String>> getInfo(String redListID)
	{
		String strQL = ConstantsSetting.QLRedDrugInfoByID;
		strQL = String.format(strQL, redListID);
		return ConstantsSetting.qLGetList(0, 0,strQL, null);
	}
}
