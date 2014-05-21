/**
 * 
 */
package coolbuy360.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import coolbuy360.service.CommandResult;

import android.content.Context;

/**
 * 药品查询
 * @author yangxc
 */
public class Drug {
		
	/**
	 * 根据分类返回药品集合，不分页。
	 * @param drugTypeid 药品分类ID 
	 * @return
	 */
	public static List<Map<String,String>> getListByDrugType(String drugTypeid)
	{
		return getListByDrugType(drugTypeid,0,0);	
	}	
	
	/**
	 * 根据分类返回药品集合，包含drugid、drugname、approvalnum、approvaltype, ishcdrug、prescriptiontype、
	 * drugtypeid、drugimg、enterprisename键值。
	 * @param drugTypeid 药品分类ID
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> getListByDrugType(String drugTypeid, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLDrugsByDrugType;
		strQL = String.format(strQL, drugTypeid);		
		return ConstantsSetting.qLGetList(pageSize, pageIndex,strQL, null);		
	}
	
	/**
	 * 根据关键字返回药品集合，不分页
	 * @param keyword 关键字
	 * @return
	 */
	public static List<Map<String,String>> getListByKeyWord(String keyword)
	{
		return getListByKeyWord(keyword,0,0);
	}
	
	/**
	 * 根据关键字返回药品集合，包含drugid、drugname、approvalnum、approvaltype, ishcdrug、prescriptiontype、
	 * drugtypeid、drugimg、enterprisename键值。
	 * @param keyword 关键字
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> getListByKeyWord(String keyword, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLDrugsByKeywords;
		//定义占位符
		Map<String, String> postValue = new HashMap<String,String>();
		postValue.put("keyword", keyword);		
		return ConstantsSetting.qLGetList(pageSize, pageIndex,strQL, postValue);		
	}
	
	/**
	 * 根据疾病返回药品集合，不分页
	 * @param diseaseid 疾病ID
	 * @return
	 */
	public static List<Map<String,String>> getListByDisease(String diseaseid)
	{
		return getListByDisease(diseaseid,0,0);
	}
	
	/**
	 * 根据疾病返回药品集合，包含drugid、drugname、approvalnum、approvaltype, ishcdrug、prescriptiontype、
	 * drugtypeid、drugimg、enterprisename键值。
	 * @param diseaseid 疾病ID
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> getListByDisease(String diseaseid, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLDrugsByDisease;
		strQL = String.format(strQL, diseaseid);		
		return ConstantsSetting.qLGetList(pageSize, pageIndex,strQL, null);		
	}
	
	/**
	 * 根据条形码查药品，不分页
	 * @param barCode 条形码
	 * @return
	 */
	public static List<Map<String,String>> getListByBarCode(String barCode)
	{
		return getListByBarCode(barCode,0,0);
	}

	/**
	 * 根据条形码查药品，包含drugid、drugname、approvalnum、approvaltype, ishcdrug、prescriptiontype、
	 * drugtypeid、drugimg、enterprisename键值。
	 * @param barCode 条形码
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> getListByBarCode(String barCode, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLDrugsByBarCode;
		strQL = String.format(strQL, barCode);
		return ConstantsSetting.qLGetList(pageSize, pageIndex,strQL, null);		
	}
	
	/**
	 * 查找附近能够买到对应药品的药店，不分页
	 * @param drugID 药品ID
	 * @param distance 搜索距离范围，单位“公里”
	 * @param currentLat 当前位置纬坐标
	 * @param currentLong 当前位置经坐标
	 * @param isHC 是否医保指定药房筛选条件，-1表示不限，0表示否，1表示是
	 * @return
	 */
	public static List<Map<String,String>> whereToBuy(String drugID, double distance, double currentLat, double currentLong, 
			int isHC)
	{
		return whereToBuy(drugID, distance, currentLat, currentLong,0,0,isHC);
	}
	
	/**
	 * 查找附近能够买到对应药品的药店，分页查找，包含键值：DrugStoreID，DrugStoreName，Tel，Mobile，IsTel，IsDoor，IsCOD，
	 * IsHC，Is24Hour，IsMember，LongValue，LatValue，Address，distance，OldPrice，NowPrice。所有键值小写。
	 * @param drugID 药品ID
	 * @param distance 搜索距离范围，单位“公里”
	 * @param currentLat 当前位置纬坐标
	 * @param currentLong 当前位置经坐标
	 * @param isHC 是否医保指定药房筛选条件，-1表示不限，0表示否，1表示是
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> whereToBuy(String drugID, double distance, 
			double currentLat, double currentLong, int isHC, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLWhereToBuyDrug;
		String tisHC = " and DST_Info.IsHC=";
		if(isHC==-1)
			strQL = String.format(strQL, currentLat, currentLong, distance, "", drugID);
		else
			strQL = String.format(strQL, currentLat, currentLong, distance, tisHC + isHC + "", drugID);
		return ConstantsSetting.qLGetList(pageSize, pageIndex,strQL, null);	
	}
	
	/**
	 * 地图模式查找附近能够买到对应药品的药店，包含键值：DrugStoreID，DrugStoreName，LongValue，LatValue，distance。所有键值小写。
	 * @param drugID 药品ID
	 * @param distance 搜索距离范围，单位“公里”
	 * @param currentLat 当前位置纬坐标
	 * @param currentLong 当前位置经坐标
	 * @param isHC 是否医保指定药房筛选条件，-1表示不限，0表示否，1表示是
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> whereToBuyMapMode(String drugID, double distance, 
			double currentLat, double currentLong, int isHC, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLWhereToBuyDrugMapMode;
		String tisHC = " and DST_Info.IsHC=";
		if(isHC==-1)
			strQL = String.format(strQL, currentLat, currentLong, distance, "", drugID);
		else
			strQL = String.format(strQL, currentLat, currentLong, distance, tisHC + isHC + "", drugID);
		return ConstantsSetting.qLGetList(pageSize, pageIndex,strQL, null);	
	}
	
	/**
	 * 查找全城能够买到对应药品的药店，分页查找，包含键值：DrugStoreID，DrugStoreName，Tel，Mobile，IsTel，IsDoor，IsCOD，
	 * IsHC，Is24Hour，IsMember，LongValue，LatValue，Address，distance，OldPrice，NowPrice。所有键值小写。
	 * @param drugID 药品ID
	 * @param cityName 城市名称
	 * @param currentLat 当前位置纬坐标
	 * @param currentLong 当前位置经坐标
	 * @param isHC 是否医保指定药房筛选条件，-1表示不限，0表示否，1表示是
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> whereToBuy(String drugID, String cityName, 
			double currentLat, double currentLong, int isHC, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLWhereToBuyInCity;
		String tisHC = " and DST_Info.IsHC=";
		if(isHC==-1)
			strQL = String.format(strQL, currentLat, currentLong, cityName, "", drugID);
		else
			strQL = String.format(strQL, currentLat, currentLong, cityName, tisHC + isHC + "", drugID);
		return ConstantsSetting.qLGetList(pageSize, pageIndex,strQL, null);	
	}
	
	/**
	 * 地图模式查找全城能够买到对应药品的药店，包含键值：DrugStoreID，DrugStoreName，LongValue，LatValue，distance。所有键值小写。
	 * @param drugID 药品ID
	 * @param cityName 城市名称
	 * @param currentLat 当前位置纬坐标
	 * @param currentLong 当前位置经坐标
	 * @param isHC 是否医保指定药房筛选条件，-1表示不限，0表示否，1表示是
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> whereToBuyMapMode(String drugID, String cityName, 
			double currentLat, double currentLong, int isHC, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLWhereToBuyInCityMapMode;
		String tisHC = " and DST_Info.IsHC=";
		if(isHC==-1)
			strQL = String.format(strQL, currentLat, currentLong, cityName, "", drugID);
		else
			strQL = String.format(strQL, currentLat, currentLong, cityName, tisHC + isHC + "", drugID);
		return ConstantsSetting.qLGetList(pageSize, pageIndex,strQL, null);	
	}
	
	/**
	 * 根据药品ID查询药品详细信息，返回一条记录
	 * @param drugID 药品ID
	 * @return
	 */
	public static List<Map<String,String>> getInfo(String drugID)
	{
		String strQL = ConstantsSetting.QLDrugInfoByID;
		strQL = String.format(strQL, drugID);
		return ConstantsSetting.qLGetList(0, 0,strQL, null);
	}
	
	/**
	 * 药品属性Key及属性名称
	 */
	public static Map<String,String> DrugProperties = new HashMap<String, String>()
	{
		{
			put("drugname","药品名称");
			put("barcode","条码");
			put("approvalnum","批准字号");
			put("goodsname","商品名");
			put("goodsnameeng","英文名");
			put("goodsnamepy","拼音");
			put("specification","规格");
			put("formulation","剂型");
			put("properties","性状");
			put("packing","包装");
			put("composition","成分");
			put("indication","适应症");
			put("usagedosage","用法用量");
			put("adversereactions","不良反应");
			put("tabu","禁忌");
			put("attention","注意事项");
			put("storage","贮藏");
			put("enterprisename","生产企业");
			put("productionaddress","生产地址");
		}			
	};		
		
	/**
	 * 查药品黑榜集合，包含drugid、drugname、键值。
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> getBlackList(int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLBlackDrugs;
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);		
	}
		
	/**
	 * 查药品黑榜详情，包含drugid、drugname、键值。
	 * @param blackID 黑榜ID
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> getBlackInfo(String blackID, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLBlackDrugInfoByID;
		strQL = String.format(strQL, blackID);
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);		
	}	
		
	/**
	 * 查药品红榜集合，包含drugid、drugname、键值。
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> getRedList(int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLRedDrugs;
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);		
	}
		
	/**
	 * 查药品红榜详情，包含drugid、drugname、键值。
	 * @param redID 红榜ID
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> getRedInfo(String redID, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLRedDrugInfoByID;
		strQL = String.format(strQL, redID);
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);		
	}
	
	/**
	 * 判断药品是否已经被收藏
	 * @param context
	 * @param drugID 
	 * @return
	 */
	public static boolean isCollected(Context context, String drugID)
	{
		String strQL = ConstantsSetting.QLDrugIsCollected;
		String appUserID = User.IsLogged ? User.getMemberAppUserID() : User
				.getDevAppUserID(context);
		strQL = String.format(strQL, appUserID, drugID);
		List<Map<String, String>> result = ConstantsSetting.qLGetList(0, 0,
				strQL, null);
		if (result != null && result.size() > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 收藏药品
	 * @param context
	 * @param drugID 
	 * @return
	 */
	public static CommandResult doCollect(Context context,String drugID)
	{
		return DrugFavorite.SetDrugFav(context, drugID, FavOperationType.Add);
	}
	
	/**
	 * 取消药品收藏
	 * @param context
	 * @param drugID
	 * @return
	 */
	public static CommandResult unCollect(Context context,String drugID)
	{
		return DrugFavorite.SetDrugFav(context, drugID, FavOperationType.Remove);
	}
	
	
}
