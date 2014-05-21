/**
 * 
 */
package coolbuy360.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import coolbuy360.service.CommandResult;

/**
 * 药店收藏
 * @author yangxc
 *
 */
public class DrugStoreFavorite {

	/**
	 * 更改药店收藏状态
	 * @param context
	 * @param drugStoreID 药店ID
	 * @param operationType 操作类型
	 * @return
	 */
	public static CommandResult SetDrugStoreFav(Context context, String drugStoreID,
			FavOperationType operationType) {
		String strQL = ConstantsSetting.QLSetDrugStoreFav;
		Map<String, String> postValue = new HashMap<String, String>();
		String appUserID = User.IsLogged ? User.getMemberAppUserID() : User
				.getDevAppUserID(context);
		postValue.put("appUserID", appUserID);
		postValue.put("drugStoreID", drugStoreID);
		postValue.put("operationType", operationType.name());
		List<Map<String, String>> result = ConstantsSetting.qLGetList(0, 0,
				strQL, postValue);
		if (result != null && result.size() > 0) {
			Map<String, String> values = result.get(0);
			CommandResult commandResult = new CommandResult(
					values.get("result"), values.get("message"));
			commandResult.setOriginalResult(values);
			String addscore = values.get("addscore");
			if (addscore != null && !addscore.equals("")
					&& !addscore.equals("0")) {
				User.updateScore(context, addscore);
			}
			return commandResult;
		}
		CommandResult commandResult = new CommandResult("false", "未知错误");
		return commandResult;
	}
	
	/**
	 * 查询药店收藏记录，分页查找，包含键值：appuserid、drugfavid、favtime、DrugStoreID，DrugStoreName，Tel，
	 * Mobile，IsTel，IsDoor，IsCOD，IsHC，Is24Hour，IsMember，LongValue，LatValue，Address，distance。所有键值小写。
	 * @param context
	 * @param currentLat 当前位置纬坐标
	 * @param currentLong 当前位置经坐标
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> getList(Context context, 
			double currentLat, double currentLong, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLDrugStoreFavList;
		String appUserID = User.IsLogged ? User.getMemberAppUserID() : User
				.getDevAppUserID(context);		
		strQL = String.format(strQL, currentLat, currentLong, appUserID);
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);	
	}
	
	/**
	 * 查询药店收藏记录，不计算距离，分页查找，包含键值：appuserid、drugfavid、favtime、DrugStoreID，DrugStoreName，
	 * Tel，Mobile，IsTel，IsDoor，IsCOD，IsHC，Is24Hour，IsMember，LongValue，LatValue，Address。所有键值小写。
	 * @param context
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> getListNoLocation(Context context, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLDrugStoreFavListNoLoc;
		String appUserID = User.IsLogged ? User.getMemberAppUserID() : User
				.getDevAppUserID(context);		
		strQL = String.format(strQL, appUserID);
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);	
	}	
}
