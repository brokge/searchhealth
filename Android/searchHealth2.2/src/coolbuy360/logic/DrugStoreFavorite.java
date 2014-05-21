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
 * ҩ���ղ�
 * @author yangxc
 *
 */
public class DrugStoreFavorite {

	/**
	 * ����ҩ���ղ�״̬
	 * @param context
	 * @param drugStoreID ҩ��ID
	 * @param operationType ��������
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
		CommandResult commandResult = new CommandResult("false", "δ֪����");
		return commandResult;
	}
	
	/**
	 * ��ѯҩ���ղؼ�¼����ҳ���ң�������ֵ��appuserid��drugfavid��favtime��DrugStoreID��DrugStoreName��Tel��
	 * Mobile��IsTel��IsDoor��IsCOD��IsHC��Is24Hour��IsMember��LongValue��LatValue��Address��distance�����м�ֵСд��
	 * @param context
	 * @param currentLat ��ǰλ��γ����
	 * @param currentLong ��ǰλ�þ�����
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
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
	 * ��ѯҩ���ղؼ�¼����������룬��ҳ���ң�������ֵ��appuserid��drugfavid��favtime��DrugStoreID��DrugStoreName��
	 * Tel��Mobile��IsTel��IsDoor��IsCOD��IsHC��Is24Hour��IsMember��LongValue��LatValue��Address�����м�ֵСд��
	 * @param context
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
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
