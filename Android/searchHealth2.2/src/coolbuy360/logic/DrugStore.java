/**
 * 
 */
package coolbuy360.logic;

import java.util.List;
import java.util.Map;

import android.content.Context;
import coolbuy360.service.CommandResult;

/**
 * 药店查询
 * @author yangxc
 */
public class DrugStore {

	/**
	 * 查找附近的药店，分页查找，包含键值：DrugStoreID，DrugStoreName，Tel，Mobile，IsTel，IsDoor，IsCOD，
	 * IsHC，Is24Hour，IsMember，LongValue，LatValue，Address，distance。所有键值小写。
	 * @param distance 搜索距离范围，单位“公里”
	 * @param currentLat 当前位置纬坐标
	 * @param currentLong 当前位置经坐标
	 * @param isHC 是否医保指定药房筛选条件，-1表示不限，0表示否，1表示是
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
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
	 * 地图模式查找附近的药店，包含键值：DrugStoreID，DrugStoreName，LongValue，LatValue，distance。所有键值小写。
	 * @param distance 搜索距离范围，单位“公里”
	 * @param currentLat 当前位置纬坐标
	 * @param currentLong 当前位置经坐标
	 * @param isHC 是否医保指定药房筛选条件，-1表示不限，0表示否，1表示是
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
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
	 * 查找全城的药店，分页查找，包含键值：DrugStoreID，DrugStoreName，Tel，Mobile，IsTel，IsDoor，IsCOD，
	 * IsHC，Is24Hour，IsMember，LongValue，LatValue，Address，distance。所有键值小写。
	 * @param cityName 城市名称
	 * @param currentLat 当前位置纬坐标
	 * @param currentLong 当前位置经坐标
	 * @param isHC 是否医保指定药房筛选条件，-1表示不限，0表示否，1表示是
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
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
	 * 地图模式查找全城的药店，包含键值：DrugStoreID，DrugStoreName，LongValue，LatValue，distance。所有键值小写。
	 * @param cityName 城市名称
	 * @param currentLat 当前位置纬坐标
	 * @param currentLong 当前位置经坐标
	 * @param isHC 是否医保指定药房筛选条件，-1表示不限，0表示否，1表示是
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
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
	 * 根据药店ID查询药店详细信息，返回一条记录，包含键值：DrugStoreID，DrugStoreName，Tel，Mobile，IsTel，IsDoor，IsCOD，
	 * IsHC，Is24Hour，IsMember，LongValue，LatValue，Address，distance，OldPrice，NowPrice，Intro，Logo，BusinessTime，
	 * DoorContent，OtherService，FullAddress。所有键值小写。
	 * @param currentLat 当前位置纬坐标
	 * @param currentLong 当前位置经坐标
	 * @param drugStoreID 药店ID
	 * @return
	 */
	public static List<Map<String,String>> getInfo(double currentLat, double currentLong, String drugStoreID)
	{
		String strQL = ConstantsSetting.QLDrugStoreInfoByID;
		strQL = String.format(strQL, currentLat, currentLong, drugStoreID);
		return ConstantsSetting.qLGetList(0, 0, strQL, null);
	}
	
	/**
	 * 判断药店是否已经被收藏
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
	 * 收藏药店
	 * @param context
	 * @param drugStoreID 
	 * @return
	 */
	public static CommandResult doCollect(Context context,String drugStoreID)
	{
		return DrugStoreFavorite.SetDrugStoreFav(context, drugStoreID, FavOperationType.Add);
	}
	
	/**
	 * 取消药店收藏
	 * @param context
	 * @param drugStoreID
	 * @return
	 */
	public static CommandResult unCollect(Context context,String drugStoreID)
	{
		return DrugStoreFavorite.SetDrugStoreFav(context, drugStoreID, FavOperationType.Remove);
	}
}
