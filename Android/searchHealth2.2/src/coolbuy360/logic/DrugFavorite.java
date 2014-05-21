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
 * 药品收藏
 * @author yangxc
 * 
 */
public class DrugFavorite {

	/**
	 * 更改药品收藏状态
	 * @param context
	 * @param drugID 药品ID
	 * @param operationType 操作类型
	 * @return
	 */
	public static CommandResult SetDrugFav(Context context, String drugID,
			FavOperationType operationType) {
		String strQL = ConstantsSetting.QLSetDrugFav;
		Map<String, String> postValue = new HashMap<String, String>();
		String appUserID = User.IsLogged ? User.getMemberAppUserID() : User
				.getDevAppUserID(context);
		postValue.put("appUserID", appUserID);
		postValue.put("drugID", drugID);
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
	 * 查询药品收藏记录，包含appuserid、drugfavid、favtime、drugid、drugname、approvalnum、approvaltype, 
	 * ishcdrug、prescriptiontype、drugtypeid、drugimg、enterprisename键值。
	 * @param context
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> getList(Context context, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLDrugFavList;
		String appUserID = User.IsLogged ? User.getMemberAppUserID() : User
				.getDevAppUserID(context);
		strQL = String.format(strQL, appUserID);		
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);		
	}
}
