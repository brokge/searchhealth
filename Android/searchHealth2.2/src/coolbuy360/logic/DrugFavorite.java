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
 * ҩƷ�ղ�
 * @author yangxc
 * 
 */
public class DrugFavorite {

	/**
	 * ����ҩƷ�ղ�״̬
	 * @param context
	 * @param drugID ҩƷID
	 * @param operationType ��������
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
		CommandResult commandResult = new CommandResult("false", "δ֪����");
		return commandResult;
	}
	
	/**
	 * ��ѯҩƷ�ղؼ�¼������appuserid��drugfavid��favtime��drugid��drugname��approvalnum��approvaltype, 
	 * ishcdrug��prescriptiontype��drugtypeid��drugimg��enterprisename��ֵ��
	 * @param context
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
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
