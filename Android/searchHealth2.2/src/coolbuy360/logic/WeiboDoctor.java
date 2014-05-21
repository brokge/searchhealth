/**
 * 
 */
package coolbuy360.logic;

import java.util.List;
import java.util.Map;

/**
 * 微博医生
 * @author yangxc
 *
 */
public class WeiboDoctor {

	
	/**
	 * 查询微博医生，分页查找，包含键值：WBDoctorID，DoctorName，WBUserName，GoodDomain，WBUrl。所有键值小写。
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> getList(int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLWeiboDocs;
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);	
	}
}
