/**
 * 
 */
package coolbuy360.logic;

import java.util.List;
import java.util.Map;

/**
 * Ӧ�ð汾
 * 
 * @author yangxc
 */
public class AppVersion {

	/**
	 * ��ѯ���°汾��Ϣ������verno,vername,updatedescr,FileSize,IsNecessary,UpdateTime,
	 * updateurl��ֵ��
	 */
	public static Map<String, String> getLastVersion() {
		String strQL = ConstantsSetting.QLLastVersionForAndroid;
		List<Map<String, String>> result = ConstantsSetting.qLGetList(1, 1,
				strQL, null);
		if (result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	/*
	 * public static List<Map<String,String>> getLastVersion() { String strQL =
	 * ConstantsSetting.QLLastVersionForAndroid; return
	 * ConstantsSetting.qLGetList(1, 1, strQL, null); }
	 */
}
