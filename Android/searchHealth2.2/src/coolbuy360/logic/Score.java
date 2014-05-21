/**
 * 
 */
package coolbuy360.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ��Ա����ֵ
 * @author yangxc
 *
 */
public class Score {
	
	/**
	 * ���ػ�Ա����ֵ��־�б�����[Score],[EventType],[CreateTime]��ֵ��
	 * @param memberid ��ԱID
	 * @param pagesize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param starttime ��ҳ��ʼʱ�������Ϊ���ַ�����ȡ��һҳ����
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
