/**
 * 
 */
package coolbuy360.logic;

import java.util.List;
import java.util.Map;

/**
 * 应用版本
 * 
 * @author yangxc
 */
public class AppVersion {

	/**
	 * 查询最新版本信息，包含verno,vername,updatedescr,FileSize,IsNecessary,UpdateTime,
	 * updateurl键值。
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
