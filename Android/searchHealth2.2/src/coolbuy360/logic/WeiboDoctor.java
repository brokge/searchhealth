/**
 * 
 */
package coolbuy360.logic;

import java.util.List;
import java.util.Map;

/**
 * ΢��ҽ��
 * @author yangxc
 *
 */
public class WeiboDoctor {

	
	/**
	 * ��ѯ΢��ҽ������ҳ���ң�������ֵ��WBDoctorID��DoctorName��WBUserName��GoodDomain��WBUrl�����м�ֵСд��
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
	 * @return
	 */
	public static List<Map<String,String>> getList(int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLWeiboDocs;
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);	
	}
}
