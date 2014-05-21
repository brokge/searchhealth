/**
 * 
 */
package coolbuy360.logic;

import java.util.List;
import java.util.Map;

import android.content.Context;
import coolbuy360.service.CommandResult;

/**
 * ҩ���ѯ
 * @author yangxc
 */
public class DrugStore {

	/**
	 * ���Ҹ�����ҩ�꣬��ҳ���ң�������ֵ��DrugStoreID��DrugStoreName��Tel��Mobile��IsTel��IsDoor��IsCOD��
	 * IsHC��Is24Hour��IsMember��LongValue��LatValue��Address��distance�����м�ֵСд��
	 * @param distance �������뷶Χ����λ�����
	 * @param currentLat ��ǰλ��γ����
	 * @param currentLong ��ǰλ�þ�����
	 * @param isHC �Ƿ�ҽ��ָ��ҩ��ɸѡ������-1��ʾ���ޣ�0��ʾ��1��ʾ��
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
	 * @return
	 */
	public static List<Map<String,String>> getList(double distance, 
			double currentLat, double currentLong, int isHC, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLDrugStores;
		String tisHC = " and DST_Info.IsHC=";
		if(isHC==-1)
			strQL = String.format(strQL, currentLat, currentLong, distance, "");
		else
			strQL = String.format(strQL, currentLat, currentLong, distance, tisHC + isHC + "");
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);	
	}

	/**
	 * ��ͼģʽ���Ҹ�����ҩ�꣬������ֵ��DrugStoreID��DrugStoreName��LongValue��LatValue��distance�����м�ֵСд��
	 * @param distance �������뷶Χ����λ�����
	 * @param currentLat ��ǰλ��γ����
	 * @param currentLong ��ǰλ�þ�����
	 * @param isHC �Ƿ�ҽ��ָ��ҩ��ɸѡ������-1��ʾ���ޣ�0��ʾ��1��ʾ��
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
	 * @return
	 */
	public static List<Map<String,String>> getListMapMode(double distance, 
			double currentLat, double currentLong, int isHC, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLDrugStoresMapMode;
		String tisHC = " and DST_Info.IsHC=";
		if(isHC==-1)
			strQL = String.format(strQL, currentLat, currentLong, distance, "");
		else
			strQL = String.format(strQL, currentLat, currentLong, distance, tisHC + isHC + "");
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);	
	}
	
	/**
	 * ����ȫ�ǵ�ҩ�꣬��ҳ���ң�������ֵ��DrugStoreID��DrugStoreName��Tel��Mobile��IsTel��IsDoor��IsCOD��
	 * IsHC��Is24Hour��IsMember��LongValue��LatValue��Address��distance�����м�ֵСд��
	 * @param cityName ��������
	 * @param currentLat ��ǰλ��γ����
	 * @param currentLong ��ǰλ�þ�����
	 * @param isHC �Ƿ�ҽ��ָ��ҩ��ɸѡ������-1��ʾ���ޣ�0��ʾ��1��ʾ��
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
	 * @return
	 */
	public static List<Map<String,String>> getList(String cityName, 
			double currentLat, double currentLong, int isHC, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLDrugStoresInCity;
		String tisHC = " and DST_Info.IsHC=";
		if(isHC==-1)
			strQL = String.format(strQL, currentLat, currentLong, cityName, "");
		else
			strQL = String.format(strQL, currentLat, currentLong, cityName, tisHC + isHC + "");
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);	
	}
	
	/**
	 * ��ͼģʽ����ȫ�ǵ�ҩ�꣬������ֵ��DrugStoreID��DrugStoreName��LongValue��LatValue��distance�����м�ֵСд��
	 * @param cityName ��������
	 * @param currentLat ��ǰλ��γ����
	 * @param currentLong ��ǰλ�þ�����
	 * @param isHC �Ƿ�ҽ��ָ��ҩ��ɸѡ������-1��ʾ���ޣ�0��ʾ��1��ʾ��
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
	 * @return
	 */
	public static List<Map<String,String>> getListMapMode(String cityName, 
			double currentLat, double currentLong, int isHC, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLDrugStoresInCityMapMode;
		String tisHC = " and DST_Info.IsHC=";
		if(isHC==-1)
			strQL = String.format(strQL, currentLat, currentLong, cityName, "");
		else
			strQL = String.format(strQL, currentLat, currentLong, cityName, tisHC + isHC + "");
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);	
	}
	
	/**
	 * ����ҩ��ID��ѯҩ����ϸ��Ϣ������һ����¼��������ֵ��DrugStoreID��DrugStoreName��Tel��Mobile��IsTel��IsDoor��IsCOD��
	 * IsHC��Is24Hour��IsMember��LongValue��LatValue��Address��distance��OldPrice��NowPrice��Intro��Logo��BusinessTime��
	 * DoorContent��OtherService��FullAddress�����м�ֵСд��
	 * @param currentLat ��ǰλ��γ����
	 * @param currentLong ��ǰλ�þ�����
	 * @param drugStoreID ҩ��ID
	 * @return
	 */
	public static List<Map<String,String>> getInfo(double currentLat, double currentLong, String drugStoreID)
	{
		String strQL = ConstantsSetting.QLDrugStoreInfoByID;
		strQL = String.format(strQL, currentLat, currentLong, drugStoreID);
		return ConstantsSetting.qLGetList(0, 0, strQL, null);
	}
	
	/**
	 * �ж�ҩ���Ƿ��Ѿ����ղ�
	 * @param context
	 * @param drugStoreID 
	 * @return
	 */
	public static boolean isCollected(Context context, String drugStoreID)
	{
		String strQL = ConstantsSetting.QLDrugStoreIsCollected;
		String appUserID = User.IsLogged ? User.getMemberAppUserID() : User
				.getDevAppUserID(context);
		strQL = String.format(strQL, appUserID, drugStoreID);
		List<Map<String, String>> result = ConstantsSetting.qLGetList(0, 0,
				strQL, null);
		if (result != null && result.size() > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * �ղ�ҩ��
	 * @param context
	 * @param drugStoreID 
	 * @return
	 */
	public static CommandResult doCollect(Context context,String drugStoreID)
	{
		return DrugStoreFavorite.SetDrugStoreFav(context, drugStoreID, FavOperationType.Add);
	}
	
	/**
	 * ȡ��ҩ���ղ�
	 * @param context
	 * @param drugStoreID
	 * @return
	 */
	public static CommandResult unCollect(Context context,String drugStoreID)
	{
		return DrugStoreFavorite.SetDrugStoreFav(context, drugStoreID, FavOperationType.Remove);
	}
}
