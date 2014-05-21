/**
 * 
 */
package coolbuy360.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import android.content.Context;
import coolbuy360.service.CommandResult;
import coolbuy360.service.Service;
import coolbuy360.service.Service.CHttpConnectionCallback;
import coolbuy360.service.TimestampException;

/**
 * 常用参数的设置
 * @author yangxc
 */
public class ConstantsSetting {
	
	/**
	 * 默认单页数据条数
	 */
	public final static int QLDefaultPageSize = 15;
	public final static int PromotionPreviewDays = 15;
	public final static String EfficiencyTestTag = "EfficiencyTest";
	
	//========= QL FormatString For DrugType ========
	/**
	 * 查询药品一级分类集合，包含DrugTypeID,DrugTypeName,ParentID,OrderNum四个键值。
	 */
	public static String QLRootDrugTypes = "SELECT DrugTypeID,DrugTypeName,ParentID,OrderNum FROM DRG_Type " +
		"where IsUse=1 and ParentID is null order by OrderNum desc,DrugTypeName asc"; 
	
	/**
	 * 根据父级分类ID查询药品分类子集合，包含DrugTypeID,DrugTypeName,ParentID,OrderNum四个键值。
	 * %1$s传入参数为父级分类ID
	 */
	public static String QLChildDrugTypesByParent = "SELECT DrugTypeID,DrugTypeName,ParentID,OrderNum FROM DRG_Type " +
			"where ParentID=%1$s and IsUse=1 order by OrderNum desc,DrugTypeName asc"; 
	
	/**
	 * 查询所有药品分类集合，包含DrugTypeID,DrugTypeName,ParentID,OrderNum四个键值。
	 */
	public static String QLAllDrugTypes = "SELECT DrugTypeID,DrugTypeName,ParentID,OrderNum FROM DRG_Type " +
			"where IsUse=1 order by ParentID asc,OrderNum desc,DrugTypeName asc"; 
	
	
	//========= QL FormatString For Drug ========
	//定义药品集合返回的字段
	private static String _drugListFields = "DRG_Info.DrugID as drugid" +  //药品ID
			",DRG_Info.DrugName as drugname" +   //药品名称
			",DRG_Info.ApprovalNum as approvalnum" +  //批准字号
			",dbo.drgApprovalTypeConverter(DRG_Info.ApprovalNum) as approvaltype" +  //批准类型，h-西药，z-中药，b-保健品，s-生物制药，j-进口药品
			",DRG_Info.IsHCDrug as ishcdrug" +  //是否医保药物，“0”表示“否”，“1”表示“是”
			",DRG_Info.PrescriptionType as prescriptiontype" +  //处方类型，1-处方药，2-甲类非处方药，3-乙类非处方药
			",DRG_Info.DrugTypeID as drugtypeid" +  //药品分类ID
			",DRG_Info.DrugImg as drugimg" +  //药品图片
			",DRG_ProductionEnterprise.ProductionEnterpriseName as enterprisename ";  //生产企业名称
	
	public static String QLDrugsByDrugType = "SELECT " + _drugListFields +
			"FROM DRG_Info LEFT JOIN DRG_ProductionEnterprise " +
			"ON DRG_Info.ProductionEnterpriseID=DRG_ProductionEnterprise.ProductionEnterpriseID " +
			"where DrugTypeID=%1$s and DRG_Info.IsUse=1 order by drugname asc"; 
	
	public static String QLDrugsByKeywords = "SELECT " + _drugListFields +
			"FROM DRG_Info LEFT JOIN DRG_ProductionEnterprise " +
			"ON DRG_Info.ProductionEnterpriseID=DRG_ProductionEnterprise.ProductionEnterpriseID " +
			"where DRG_Info.IsUse=1 and " +
			"(DrugName^'%$keyword$%' " +   //从药品名查
			"or GoodsName^'%$keyword$%' " +   //从商品名查
			"or GoodsNameEng^'%$keyword$%' " +   //从英文名查
			"or GoodsNamePY^'%$keyword$%' " +   //从拼音码查
			"or Indication^'%$keyword$%') order by drugname asc";   //从主治症状查
	
	public static String QLDrugsByBarCode = "SELECT " + _drugListFields +
			"FROM DRG_Info LEFT JOIN DRG_ProductionEnterprise " +
			"ON DRG_Info.ProductionEnterpriseID=DRG_ProductionEnterprise.ProductionEnterpriseID " +
			"where BarCode='%1$s' and DRG_Info.IsUse=1 order by drugname asc";
		
	public static String QLDrugsByDisease = "SELECT " + _drugListFields +
			"FROM DIS_Drug INNER JOIN DRG_Info " +
			"ON DIS_Drug.DrugID = DRG_Info.DrugID " +
			"LEFT OUTER JOIN DRG_ProductionEnterprise " +
			"ON DRG_Info.ProductionEnterpriseID = DRG_ProductionEnterprise.ProductionEnterpriseID " +
			"WHERE DIS_Drug.DiseaseID = %1$s AND DRG_Info.IsUse=1 order by drugname asc "; 
	
	//定义药品详情返回的字段
	private static String _drugInfoFields = "DRG_Info.DrugID as drugid" +  //药品ID
			",DRG_Info.DrugName as drugname" +   //药品名称
			",DRG_Info.BarCode as BarCode" +   //条码
			",DRG_Info.ApprovalNum as approvalnum" +  //批准字号
			",dbo.drgApprovalTypeConverter(DRG_Info.ApprovalNum) as approvaltype" +  //批准类型，h-西药，z-中药，b-保健品，s-生物制药，j-进口药品
			",DRG_Info.GoodsName as GoodsName" +  //商品名
			",DRG_Info.GoodsNameEng as GoodsNameEng" +  //英文名
			",DRG_Info.GoodsNamePY as GoodsNamePY" +  //汉语拼音
			",DRG_Info.Specification as Specification" +  //规格
			",DRG_Info.Formulation as Formulation" +  //剂型
			",DRG_Info.Properties as Properties" +  //性状
			",DRG_Info.Packing as Packing" +  //包装
			",DRG_Info.Composition as Composition" +  //成分
			",DRG_Info.Indication as Indication" +  //适应症
			",DRG_Info.UsageDosage as UsageDosage" +  //用法用量
			",DRG_Info.AdverseReactions as AdverseReactions" +  //不良反应
			",DRG_Info.Tabu as Tabu" +  //禁忌
			",DRG_Info.Attention as Attention" +  //注意事项
			",DRG_Info.Storage as Storage" +  //贮藏
			",DRG_Info.IsHCDrug as ishcdrug" +  //是否医保药物，“0”表示“否”，“1”表示“是”
			",DRG_Info.PrescriptionType as prescriptiontype" +  //处方类型，1-处方药，2-甲类非处方药，3-乙类非处方药
			",DRG_Info.ProductionAddress as ProductionAddress" +  //生产地址
			",DRG_Info.DrugImg as drugimg" +  //药品图片
			",DRG_ProductionEnterprise.ProductionEnterpriseName as enterprisename ";  //生产企业名称
	
	public static String QLDrugInfoByID = "SELECT " + _drugInfoFields +
			"FROM DRG_Info LEFT JOIN DRG_ProductionEnterprise " +
			"ON DRG_Info.ProductionEnterpriseID=DRG_ProductionEnterprise.ProductionEnterpriseID " +
			"where DRG_Info.DrugID='%1$s' order by drugname asc";
	
		
	//========= QL FormatString For 曝光栏 ========
	//定义黑榜曝光信息集合返回的字段
	private static String _blackDrugListFields = "DRG_BlackList.BlackListID as BlackListID" +  //黑榜ID
			",DRG_BlackList.DrugID as DrugID" +   //药品ID
			",DRG_BlackList.PubTime as PubTime" +  //发布时间
			",DRG_BlackList.DangerLever as DangerLever" +  //危害程度
			",DRG_BlackList.Danger as Danger " +  //危害
			",DRG_Info.DrugName as drugname " + //药品名称
			",DRG_Info.DrugImg as DrugImg "; //药品图片
		
	public static String QLBlackDrugs = "SELECT " + _blackDrugListFields +
			"FROM DRG_BlackList LEFT JOIN DRG_Info " +
			"ON DRG_BlackList.DrugID = DRG_Info.DrugID " +
			"WHERE DRG_BlackList.IsUse=1 order by PubTime desc"; 
	
	//定义黑榜曝光信息详情返回的字段
	private static String _blackDrugInfoFields = "DRG_BlackList.BlackListID as BlackListID" +  //黑榜ID
			",DRG_BlackList.DrugID as DrugID" +   //药品ID
			",DRG_BlackList.PubTime as PubTime" +  //发布时间
			",DRG_BlackList.ExpTime as ExpTime " +  //曝光时间
			",DRG_BlackList.DangerLever as DangerLever" +  //危害程度
			",DRG_BlackList.ProductionDate as ProductionDate " +  //生产日期
			",DRG_BlackList.BatchNum as BatchNum " +  //批号
			",DRG_BlackList.SamplingResults as SamplingResults " +  //抽检结果
			",DRG_BlackList.Danger as Danger " +  //危害
			",DRG_BlackList.SrcURL as SrcURL " +  //来源网页网址
			",DRG_BlackList.SrcTitle as SrcTitle " +  //来源网页标题
			",DRG_BlackList.ExpDescr as ExpDescr " +  //曝光描述
			",DRG_Info.DrugName as drugname " +  //药品名称
			",DRG_Info.DrugImg as DrugImg "; //药品图片
		
	public static String QLBlackDrugInfoByID = "SELECT " + _blackDrugInfoFields +
			"FROM DRG_BlackList LEFT JOIN DRG_Info " +
			"ON DRG_BlackList.DrugID = DRG_Info.DrugID " +
			"WHERE DRG_BlackList.IsUse=1 And DRG_BlackList.BlackListID=%1$s " +
			"order by PubTime desc"; 

	//定义红榜信息集合返回的字段
	private static String _redDrugListFields = "DRG_RedList.RedListID as RedListID" +  //红榜ID
			",DRG_RedList.DrugID as DrugID" +   //药品ID
			",DRG_RedList.PubTime as PubTime" +  //发布时间
			",DRG_RedList.UpTime as UpTime" +  //上榜时间
			",DRG_RedList.SamplingResults as SamplingResults " +  //抽检结果
			",DRG_Info.DrugName as drugname " +  //药品名称
			",DRG_Info.DrugImg as DrugImg "; //药品图片
		
	public static String QLRedDrugs = "SELECT " + _redDrugListFields +
			"FROM DRG_RedList LEFT JOIN DRG_Info " +
			"ON DRG_RedList.DrugID = DRG_Info.DrugID " +
			"WHERE DRG_RedList.IsUse=1 order by PubTime desc"; 

	//定义红榜信息详情返回的字段
	private static String _redDrugInfoFields = "DRG_RedList.RedListID as RedListID" +  //红榜ID
			",DRG_RedList.DrugID as DrugID" +   //药品ID
			",DRG_RedList.PubTime as PubTime" +  //发布时间
			",DRG_RedList.UpTime as UpTime" +  //上榜时间
			",DRG_RedList.ProductionDate as ProductionDate " +  //生产日期
			",DRG_RedList.BatchNum as BatchNum " +  //批号
			",DRG_RedList.SamplingResults as SamplingResults " +  //抽检结果
			",DRG_RedList.SrcURL as SrcURL " +  //来源网页网址
			",DRG_RedList.SrcTitle as SrcTitle " +  //来源网页标题
			",DRG_RedList.UpDescr as UpDescr " +  //上榜概述
			",DRG_Info.DrugName as drugname " +  //药品名称
			",DRG_Info.DrugImg as DrugImg "; //药品图片
		
	public static String QLRedDrugInfoByID = "SELECT " + _redDrugInfoFields +
			"FROM DRG_RedList LEFT JOIN DRG_Info " +
			"ON DRG_RedList.DrugID = DRG_Info.DrugID " +
			"WHERE DRG_RedList.IsUse=1 And DRG_RedList.RedListID=%1$s " +
			"order by PubTime desc"; 
	
	
	//========= QL FormatString For DrugStore ========
	//定义药店集合返回的字段
	private static String _drugStoreListFields = "DST_Info.DrugStoreID as DrugStoreID" +  //药店ID
			",DST_Info.DrugStoreName as DrugStoreName" +   //药店名称
			",DST_Info.Tel as Tel" +  //电话
			",DST_Info.Mobile as Mobile" +  //手机
			",DST_Info.IsTel as IsTel" +  //是否电话预订，0为否，1为是
			",DST_Info.IsDoor as IsDoor" +  //是否送货上门，0为否，1为是
			",DST_Info.IsCOD as IsCOD" +  //是否货到付款，0为否，1为是
			",DST_Info.IsHC as IsHC" +  //是否支持医保，0为否，1为是
			",DST_Info.Is24Hour as Is24Hour" +  //是否24小时营业，0为否，1为是
			",DST_Info.IsMember as IsMember" +  //是否有会员优惠，0为否，1为是
			",DST_Info.LongValue as LongValue" +  //经坐标
			",DST_Info.LatValue as LatValue" +  //纬坐标
			",DST_Info.Address as Address" +  //药店地址
			",dbo.dstGetDistance(DST_Info.LatValue,DST_Info.LongValue,%1$s,%2$s) as distance" +  //与当前位置的距离，单位公里
			",dbo.dstPromotionOn(DST_Info.DrugStoreID, " + PromotionPreviewDays + ") as ispromotionon ";  //近期是否有优惠促销活动

	private static String _drugStoreListFieldsMapMode = "DST_Info.DrugStoreID as DrugStoreID" +  //药店ID
			",DST_Info.DrugStoreName as DrugStoreName" +   //药店名称
			",DST_Info.LongValue as LongValue" +  //经坐标
			",DST_Info.LatValue as LatValue" +  //纬坐标
			",dbo.dstGetDistance(DST_Info.LatValue,DST_Info.LongValue,%1$s,%2$s) as distance ";  //与当前位置的距离，单位公里

	private static String _drugStoreListFieldsNoLoc = "DST_Info.DrugStoreID as DrugStoreID" +  //药店ID
			",DST_Info.DrugStoreName as DrugStoreName" +   //药店名称
			",DST_Info.Tel as Tel" +  //电话
			",DST_Info.Mobile as Mobile" +  //手机
			",DST_Info.IsTel as IsTel" +  //是否电话预订，0为否，1为是
			",DST_Info.IsDoor as IsDoor" +  //是否送货上门，0为否，1为是
			",DST_Info.IsCOD as IsCOD" +  //是否货到付款，0为否，1为是
			",DST_Info.IsHC as IsHC" +  //是否支持医保，0为否，1为是
			",DST_Info.Is24Hour as Is24Hour" +  //是否24小时营业，0为否，1为是
			",DST_Info.IsMember as IsMember" +  //是否有会员优惠，0为否，1为是
			",DST_Info.Address as Address" +  //药店地址
			",dbo.dstPromotionOn(DST_Info.DrugStoreID, " + PromotionPreviewDays + ") as ispromotionon " ;  //近期是否有优惠促销活动
	
	private static String _drugStoreInfoFields = "DST_Info.DrugStoreID as DrugStoreID" +  //药店ID
			",DST_Info.DrugStoreName as DrugStoreName" +   //药店名称
			",DST_Info.Address as Address" +  //药店地址
			",DST_Info.LongValue as LongValue" +  //经坐标
			",DST_Info.LatValue as LatValue" +  //纬坐标
			",DST_Info.Tel as Tel" +  //电话
			",DST_Info.Mobile as Mobile" +  //手机
			",DST_Info.IsTel as IsTel" +  //是否电话预订，0为否，1为是
			",DST_Info.IsDoor as IsDoor" +  //是否送货上门，0为否，1为是
			",DST_Info.IsCOD as IsCOD" +  //是否货到付款，0为否，1为是
			",DST_Info.IsHC as IsHC" +  //是否支持医保，0为否，1为是
			",DST_Info.Intro as Intro" +  //药店简介
			",DST_Info.Logo as Logo" +  //药店Logo
			",DST_Info.BusinessTime as BusinessTime" +  //营业时间
			",DST_Info.Is24Hour as Is24Hour" +  //是否24小时营业，0为否，1为是
			",DST_Info.IsMember as IsMember" +  //是否有会员优惠，0为否，1为是
			",DST_Info.DoorContent as DoorContent" +  //送货上门的范围
			",DST_Info.OtherService as OtherService" +  //其它服务
			",dbo.dstGetDistance(DST_Info.LatValue,DST_Info.LongValue,%1$s,%2$s) as distance" +  //与当前位置的距离，单位公里
			",dbo.dstGetFullAddress(DST_Info.District,DST_Info.Address) as FullAddress ";  //完整地址	
		
	public static String QLWhereToBuyDrug = "SELECT " + _drugStoreListFields +
			",DST_DrugPrice.OldPrice as OldPrice " +  //药品原价
			",DST_DrugPrice.NowPrice as NowPrice " +  //药品当前价
			"from DST_DrugPrice Inner Join DST_Info " +
			"On DST_DrugPrice.DrugStoreID=DST_Info.DrugStoreID " +
			"where DST_DrugPrice.DrugID=%5$s and DST_Info.IsUse=1 and DST_DrugPrice.IsOnSell=1 " +
			"and dbo.dstGetDistance(DST_Info.LatValue,DST_Info.LongValue,%1$s,%2$s) < %3$s %4$s " +
			"order by distance asc "; 	
		
	public static String QLWhereToBuyDrugMapMode = "SELECT " + _drugStoreListFieldsMapMode +
			"from DST_DrugPrice Inner Join DST_Info " +
			"On DST_DrugPrice.DrugStoreID=DST_Info.DrugStoreID " +
			"where DST_DrugPrice.DrugID=%5$s and DST_Info.IsUse=1 and DST_DrugPrice.IsOnSell=1 " +
			"and dbo.dstGetDistance(DST_Info.LatValue,DST_Info.LongValue,%1$s,%2$s) < %3$s %4$s " +
			"order by distance asc "; 
	
	public static String QLWhereToBuyInCity = "SELECT " + _drugStoreListFields +
			",DST_DrugPrice.OldPrice as OldPrice " +  //药品原价
			",DST_DrugPrice.NowPrice as NowPrice " +  //药品当前价
			"from DST_DrugPrice Inner Join DST_Info " +
			"On DST_DrugPrice.DrugStoreID=DST_Info.DrugStoreID " +
			"where DST_DrugPrice.DrugID=%5$s and DST_Info.IsUse=1 and DST_DrugPrice.IsOnSell=1 " +
			"and DST_Info.City=dbo.sysGetAreaID('%3$s') %4$s " +
			"order by distance asc "; 
	
	public static String QLWhereToBuyInCityMapMode = "SELECT " + _drugStoreListFieldsMapMode +
			"from DST_DrugPrice Inner Join DST_Info " +
			"On DST_DrugPrice.DrugStoreID=DST_Info.DrugStoreID " +
			"where DST_DrugPrice.DrugID=%5$s and DST_Info.IsUse=1 and DST_DrugPrice.IsOnSell=1 " +
			"and DST_Info.City=dbo.sysGetAreaID('%3$s') %4$s " +
			"order by distance asc "; 
	
	public static String QLDrugStores = "SELECT " + _drugStoreListFields +
			"from DST_Info Left Join APP_Null on DST_Info.Country = APP_Null.id " +
			"where DST_Info.IsUse=1 " +
			"and dbo.dstGetDistance(DST_Info.LatValue,DST_Info.LongValue,%1$s,%2$s) < %3$s %4$s " +
			"order by distance asc "; 
	
	public static String QLDrugStoresMapMode = "SELECT " + _drugStoreListFieldsMapMode +
			"from DST_Info Left Join APP_Null on DST_Info.Country = APP_Null.id " +
			"where DST_Info.IsUse=1 " +
			"and dbo.dstGetDistance(DST_Info.LatValue,DST_Info.LongValue,%1$s,%2$s) < %3$s %4$s " +
			"order by distance asc "; 
	
	public static String QLDrugStoresInCity = "SELECT " + _drugStoreListFields +
			"from DST_Info Left Join APP_Null on DST_Info.Country = APP_Null.id " +
			"where DST_Info.IsUse=1 " +
			"and DST_Info.City=dbo.sysGetAreaID('%3$s') %4$s " +
			"order by distance asc "; 
	
	public static String QLDrugStoresInCityMapMode = "SELECT " + _drugStoreListFieldsMapMode +
			"from DST_Info Left Join APP_Null on DST_Info.Country = APP_Null.id " +
			"where DST_Info.IsUse=1 " +
			"and DST_Info.City=dbo.sysGetAreaID('%3$s') %4$s " +
			"order by distance asc "; 
	
	public static String QLDrugStoreInfoByID = "SELECT " + _drugStoreInfoFields +
			"FROM DST_Info Left Join APP_Null on DST_Info.Country = APP_Null.id " +
			"where DST_Info.DrugStoreID='%3$s' order by DrugStoreName asc";
	
	//========= QL FormatString For Promotion ========
	//定义药店促销活动返回的字段
	private static String _promotionListFields = "PromotionID" +  //促销活动ID
			",Title" +   //标题
			",Image" +   //图片
			",BeginTime" +   //开始时间
			",CreateTime" +   //创建时间
			",OrderNo ";  //排序
	
	/**
	 * 参数1：药店ID，参数2：今天，参数3：昨天
	 */
	/*public static String QLPromotionsByDrugStoreID = "SELECT " + _promotionListFields +
			"from DST_Promotion " +
			"where [State]=1 " +
			"and DrugStoreID=%1$s " +
			"and (BeginTime <= CONVERT(DATETIME, '%2$s', 102)) " +
			"and (EndTime >= CONVERT(DATETIME, '%3$s', 102)) " +
			"order by OrderNo DESC,BeginTime DESC,CreateTime DESC "; */
	public static String QLPromotionsByDrugStoreID = "plugin DrugCenter.Logic.V1 DrugStore " +
			"GetStorePromotionList($storeid$,$pagesize$,$pageindex$)";
	
	private static String _promotionInfoFields = "PromotionID" +  //促销活动ID
			",Title" +   //标题
			",Image" +   //图片
			",BeginTime" +   //开始时间
			",EndTime" +   //结束时间
			",Detail ";   //详情
	
	public static String QLPromotionInfo = "SELECT " + _promotionInfoFields +
			"from DST_Promotion " +
			"where State=1 " +
			"and PromotionID=%1$s "; 	
		
	//========= QL FormatString For Disease ========
	//定义疾病集合返回的字段
	private static String _diseaseListFields = "DIS_Info.DiseaseID as DiseaseID" +  //疾病ID
			",DIS_Info.DiseaseName as DiseaseName" +   //疾病名称
			",DIS_Info.Part as Part" +  //部位
			",DIS_Info.DiseaseAlias as DiseaseAlias ";  //疾病别名

	//定义疾病详情返回的字段
	private static String _diseaseInfoFields = "DIS_Info.DiseaseID as DiseaseID" +  //疾病ID
			",DIS_Info.DiseaseName as DiseaseName" +   //疾病名称
			",DIS_Info.Part as Part" +  //部位
			",DIS_Info.Pathogeny as Pathogeny" +  //病因
			",DIS_Info.Symptoms as Symptoms" +  //症状表现
			",DIS_Info.Descr as Descr" +  //概述
			",DIS_Info.PreventiveCare as PreventiveCare" +  //预防保健
			",DIS_Info.DiseaseAlias as DiseaseAlias" +  //疾病别名
			",DIS_Info.DiffDiag as DiffDiag" +  //诊断鉴别
			",DIS_Info.Treat as Treat ";  //治疗

	public static String QLDiseasesByKeywords = "SELECT " + _diseaseListFields +
			"FROM DIS_Info Left Join APP_Null on DIS_Info.DiseaseName = APP_Null.id " +
			"where DIS_Info.IsUse=1 and " +
			"(DIS_Info.DiseaseName^'%$keyword$%' " +   //从疾病名称查
			"or DIS_Info.Part^'%$keyword$%' " +   //从部位查
			"or DIS_Info.Symptoms^'%$keyword$%' " +   //从症状表现查
			"or DIS_Info.Descr^'%$keyword$%' " +   //从概述查
			"or DIS_Info.DiseaseAlias^'%$keyword$%') order by LEN(DiseaseName) asc,DiseaseID asc";   //从疾病别名查

	public static String QLDiseaseInfoByID = "SELECT " + _diseaseInfoFields +
			"FROM DIS_Info Left Join APP_Null on DIS_Info.DiseaseName = APP_Null.id " +
			"where DIS_Info.DiseaseID=%1$s order by DiseaseName asc";
	
	//定义常见疾病集合返回字段
	private static String _commonDiseaseListFields = "CommonDiseaseName" +  //名称
			",OrderNum" +   //排序
			",DiseaseID ";  //疾病ID

	public static String QLCommonDiseases = "SELECT " + _commonDiseaseListFields +
			"FROM DIS_Common " +
			"where IsUse=1 " +
			"order by OrderNum desc"; 

	//========= QL FormatString For Version ========
	//定义版本信息返回的字段
	private static String _versionListFields = "VerNo" +  //版本号
			",VerName" +   //版本名称
			",UpdateDescr" +  //更新说明
			",FileSize" +  //文件大小
			",IsNecessary" +  //是否必须更新
			",UpdateTime" +  //更新时间
			",UpdateURL ";  //更新地址
	
	public static String QLLastVersionForAndroid = "SELECT " + _versionListFields +
			"FROM SYS_Ver " +
			"where Os='android' order by UpdateTime desc,verid desc ";
	
	//========= QL FormatString For Feedback ========
	public static String QLInsertOneFeedback = "insert into SYS_Feedback" +
			"(AppUserID,FeedbackContent,FeedbackTime) " +
			"VALUES($appuserid$,$content$,$time$)";
	
	public static String QLInsertOneFeedbackByMember = "insert into SYS_Feedback" +
			"(AppUserID,MemberID,FeedbackContent,FeedbackTime) " +
			"VALUES($appuserid$,$memberid$,$content$,$time$)";

	//===================== QL FormatString For User==========================
	public static String QLUserLogin = "plugin DrugCenter.Logic.V1 Member Login($id$,$pwd$,$deviceAppUID$)";
	
	public static String QLGetDevAppUserID = "plugin DrugCenter.Logic.V1 Member GetDevAppUserID($deviceID$)";	
	
	public static String QLChangePassword = "plugin DrugCenter.Logic.V1 Member ChangePwd($id$,$oldPwd$,$newPwd$)";	
	
	public static String QLUpdateFirstLocation = "UPDATE USR_APPUser SET Address=$localaddress$ where AppUserID=%1$s";
	
	public static String QLCheckMemberIdCardNum = "Select MemberID From USR_Member where IdCardNum='%1$s' order by MemberID";	
	public static String QLCheckMemberMobile = "Select MemberID From USR_Member where Mobile='%1$s' order by MemberID";		
	
	//定义用户信息的返回字段
//	SELECT [MemberID]
//	        ,[UserName]     
//	        ,[MemberName]     
//	        ,[Email]
//	        ,[Mobile]     
//	        ,[Sex]      
//	        ,[Job]
//	        ,[HC]
//	        ,[Income]      
//	        ,[BuyWay]
//	        ,[FamilyAddress]     
//	        ,[IdCardNum]     
//	        ,[Birthday]
//	        ,[Telephone]
//	        ,[EducationLevel]
//	        ,[CommonHospital]
//
//	    FROM [DrugCenter].[dbo].[USR_Member] 
//	     where [UserName] ='31000101w00001'
	     
	private static String _userInfoFields=" MemberID"+//会员id
				",UserName"+//用户姓名
				",MemberName "+//会员姓名
				",Email  "+//会员email
				",Mobile  "+//会员手机号
				",Sex"+//会员性别
				",Job  "+//会员职业
				",HC  "+//医保情况:正常，无，冻结
				",Income  "+//医疗支出
				",BuyWay "+//购买途径
				",FamilyAddress  "+//家庭住址
				",IdCardNum "+//身份证号
				",Birthday "+//出生日期
				",Telephone "+//电话
				",EducationLevel "+//教育水平
				",CommonHospital  "//首选医院
						;
	public static String QLGetUserInfo="SELECT"+_userInfoFields+
				"FROM USR_Member "+
				"where MemberID=%1$s";
	
//	update Table set a=1,b=2,c=$c$ where id=1 and id=2 or name=$name$ and name^标题 模糊查询 
	public static String QLSetUserInfo=" UPDATE USR_Member SET "
				+"MemberName=$membername$ "
				+",Email=$email$"
				+",Mobile=$mobile$"
				+",Sex=$sex$"
				+",Job=$job$"
				+",HC=$hc$"
				+",Income=$income$"
				+",BuyWay=$buyway$"
				+",FamilyAddress=$familyaddress$"
				+",IdCardNum=$idcardnum$"
				+",Birthday=$birthday$"
				+",Telephone=$telephone$"
				+",EducationLevel=$educationlevel$"
				+",CommonHospital=$commonhospital$" 
				+"  where MemberID=$memberid$  " ;
			
	//========= QL FormatString For HealthCalendar ========
	// ,[MemberID]      ,[EventTime]      ,[Summarize]      ,[Remarks]      ,[CreateTime]
	private static String _healthCalendarInfoFields = " ID,MemberID" +//会员id
			",EventTime" +	//事件时间
		    ",Summarize" +	//概述
		    ",Remarks" +	//说明
		    ",CreateTime "; //创建时间
		
	public static String QLGetHCalendarByMemberID = "SELECT" + _healthCalendarInfoFields +
			"FROM MBR_HealthCalendar "+
			"where MemberID=%1$s "+
			" ORDER BY EventTime DESC ";    
	
	public static String QLGetHCalendarInfoByID = "SELECT" + _healthCalendarInfoFields +
			"FROM MBR_HealthCalendar "+
			"where MemberID=%1$s and ID=%2$s";
	
	// ,[EventTime]       ,[Summarize]       ,[Remarks]       ,[CreateTime])
    public static String QLInsertOneHCalendarInfoByMemberID = "insert into MBR_HealthCalendar " +
			"(MemberID,EventTime,Summarize,Remarks,CreateTime) " +
			"VALUES($memberid$,$eventtime$,$summarize$,$remarks$,$createtime$)";
    
    //========= QL FormatString For Member Disease ========
  	//定义会员疾病史返回的字段
  	private static String _memberDiseaseListFields = "ID" +  //过敏史ID
  			",DiseaseName" +   //疾病名称
  			",DiagnoseTime" +  //确诊时间
  			",IsContagious" +  //是否传染
  			",IsHereditary" +  //是否遗传
  			",Remarks" +  //说明
  			",CreateTime ";  //创建时间
  	
  	public static String QLMemberDiseasesByMemberID = "SELECT " + _memberDiseaseListFields +
  			"FROM MBR_Disease " +
  			"where MemberID=%1$s " +
  			"order by DiagnoseTime desc,CreateTime desc ";
  	
  	public static String QLMemberContagioussByMemberID = "SELECT " + _memberDiseaseListFields +
  			"FROM MBR_Disease " +
  			"where MemberID=%1$s and IsContagious=1 " +
  			"order by DiagnoseTime desc,CreateTime desc ";
  	
  	public static String QLMemberHereditarysByMemberID = "SELECT " + _memberDiseaseListFields +
  			"FROM MBR_Disease " +
  			"where MemberID=%1$s and IsHereditary=1 " +
  			"order by DiagnoseTime desc,CreateTime desc ";
  	
  	public static String QLInsertMemberDisease = "insert into MBR_Disease" +
  			"(MemberID,DiseaseName,DiagnoseTime,IsContagious,IsHereditary,Remarks,CreateTime) " +
  			"VALUES($memberid$,$diseasename$,$diagnosetime$,$iscontagious$,$ishereditary$,$remarks$,$createtime$)";
	
	public static String qLDeleteMemberDisease = "delete MBR_Disease WHERE ID=%1$s";
    
  	//========= QL FormatString For Allergic ========
	//定义会员过敏史返回的字段
	private static String _memberAllergicListFields = "ID" +  //过敏史ID
			",Allergen" +   //过敏源
			",OccurrenceTime" +  //发生时间
			",Symptom" +  //症状
			",Remarks" +  //说明
			",CreateTime ";  //创建时间
	
	public static String QLMemberAllergicsByMemberID = "SELECT " + _memberAllergicListFields +
			"FROM MBR_Allergic " +
			"where MemberID=%1$s " +
			"order by OccurrenceTime desc,CreateTime desc ";
	
	public static String QLInsertMemberAllergic = "insert into MBR_Allergic" +
			"(MemberID,Allergen,OccurrenceTime,Symptom,Remarks,CreateTime) " +
			"VALUES($memberid$,$allergen$,$occurrencetime$,$symptom$,$remarks$,$createtime$)";
	
	public static String qLDeleteMemberAllergic = "delete MBR_Allergic WHERE ID=%1$s";
	
	//========= QL FormatString For Operation ========
	//定义会员手术记录返回的字段
	private static String _memberOperationListFields = "ID" +  //过敏史ID
			",OperationName" +   //过敏源
			",ImplementTime" +  //发生时间
			",Remarks" +  //说明
			",CreateTime ";  //创建时间
	
	public static String QLMemberOperationsByMemberID = "SELECT " + _memberOperationListFields +
			"FROM MBR_Operation " +
			"where MemberID=%1$s " +
			"order by ImplementTime desc,CreateTime desc ";
	
	public static String QLInsertMemberOperation = "insert into MBR_Operation" +
			"(MemberID,OperationName,ImplementTime,Remarks,CreateTime) " +
			"VALUES($memberid$,$operationname$,$implementtime$,$remarks$,$createtime$)";
	
	public static String qLDeleteMemberOperation = "delete MBR_Operation WHERE ID=%1$s";
	
	//========= QL FormatString For Favorite ========
	//定义药品收藏集合返回的字段
	private static String _drugFavListFields = "USR_APPUserDrugFav.AppUserID as appuserid" +  //用户应用ID
			",USR_APPUserDrugFav.DrugFavID as drugfavid" +   //药品收藏记录ID
			",USR_APPUserDrugFav.FavTime as favtime" ;  //药品收藏时间
	
	//定义药店收藏集合返回的字段
	private static String _drugStoreFavListFields = "USR_APPUserDrugStoreFav.AppUserID as appuserid" +  //用户应用ID
			",USR_APPUserDrugStoreFav.DrugStoreFavID as drugstorefavid" +   //药店收藏记录ID
			",USR_APPUserDrugStoreFav.FavTime as favtime" ;  //药店收藏时间
	
	public static String QLSetDrugFav = "plugin DrugCenter.Logic.V1 Member SetDrugFav($appUserID$,$drugID$,$operationType$)";
	
	public static String QLDrugIsCollected="select DrugID from USR_APPUserDrugFav where AppUserID=%1$s and DrugID=%2$s";
	
	public static String QLSetDrugStoreFav = "plugin DrugCenter.Logic.V1 Member SetDrugStoreFav($appUserID$,$drugStoreID$,$operationType$)";
	
	public static String QLDrugStoreIsCollected="select DrugStoreID from USR_APPUserDrugStoreFav where AppUserID=%1$s and DrugStoreID=%2$s";
	
	public static String QLDrugFavList = "SELECT " + _drugFavListFields + "," + _drugListFields +
			"FROM USR_APPUserDrugFav INNER JOIN DRG_Info ON USR_APPUserDrugFav.DrugID = DRG_Info.DrugID " +
			"LEFT OUTER JOIN DRG_ProductionEnterprise " +
			"ON DRG_Info.ProductionEnterpriseID = DRG_ProductionEnterprise.ProductionEnterpriseID " + 
			"where USR_APPUserDrugFav.AppUserID=%1$s and DRG_Info.IsUse=1 " +
			"order by favtime desc,drugname asc ";
	
	public static String QLDrugStoreFavList = "SELECT " + _drugStoreFavListFields + "," + _drugStoreListFields +
			"FROM USR_APPUserDrugStoreFav INNER JOIN DST_Info " +
			"ON USR_APPUserDrugStoreFav.DrugStoreID = DST_Info.DrugStoreID " +
			"where USR_APPUserDrugStoreFav.AppUserID=%3$s and DST_Info.IsUse=1 " +
			"order by favtime desc,DrugStoreName asc ";
	
	public static String QLDrugStoreFavListNoLoc = "SELECT " + _drugStoreFavListFields + "," + _drugStoreListFieldsNoLoc +
			"FROM USR_APPUserDrugStoreFav INNER JOIN DST_Info " +
			"ON USR_APPUserDrugStoreFav.DrugStoreID = DST_Info.DrugStoreID " +
			"where USR_APPUserDrugStoreFav.AppUserID=%1$s and DST_Info.IsUse=1 " +
			"order by favtime desc,DrugStoreName asc ";
	
	//========= QL FormatString For 微博医生 ========
	//定义微博医生集合返回的字段
	private static String _WeiboDocListFields = "WBD_Info.WBDoctorID as WBDoctorID" +  //微博医生ID
			",WBD_Info.DoctorName as DoctorName" +   //医生姓名
			",WBD_Info.WBUserName as WBUserName" +   //微博账号
			",WBD_Info.GoodDomain as GoodDomain" +   //擅长领域
			",WBD_Info.WBUrl as WBUrl" ;  //微博地址
	
	public static String QLWeiboDocs = "SELECT " + _WeiboDocListFields +
			" from WBD_Info Left Join APP_Null on WBD_Info.DoctorName = APP_Null.id " +
			"where WBD_Info.IsUse=1 " +
			"order by DoctorName asc "; 	
	
	
	//========= QL FormatString For 消息推送[PUSH_User] ========
	//插入pushuser数据
	public static String QLInsertPUSH_User = "insert into PUSH_User" +
	"(BaiduUserID ,MemberId,Tags,DeviceNum,DeviceType,CreateTime,IsUse) " +
	"VALUES($baiduuserid$,$memberid$,$tags$,$devicenum$,$devicetype$,$createtime$,$isuse$)";
	public static String QLSetPUSH_User=" UPDATE PUSH_User SET "
		+",BaiduUserID=$baiduuserid$"
		+",MemberId=$memberid$"
		+",Tags=$tags$"
		+",DeviceNum=$devicenum$"
		+",DeviceType=$devicetype$"
		//+",CreateTime=$createtime$"
		+",IsUse=$isuse$"	
		+"  where PushUserID=$pushuserid$  " ;
	public static String QLPush_User = "plugin DrugCenter.Logic.V1 PushCommon PushUser($baiduuserid$,$memberid$,$tags$,$devicenum$,$devicetype$,$isuse$)";
/*	SELECT  [MessageID]     
	         ,[MessageType]
	         ,[MessageType2]
	         ,[MessagesTitle]
	         ,[MessagesContent]    
	     FROM [DrugCenter].[dbo].[PUSH_Message]*/
	public static String QLPushMessageField="MessageType"		
		+" ,MessageType2 "
		+" ,MessagesTitle "
		+" ,SendTime "
		+" ,MessagesContent "
		;	
	public static String QLGetPushMessage = "SELECT " 
		+  QLPushMessageField
		+" from PUSH_Message " 
		+"where MessageID=%1$s order by SendTime asc "  ;
	
	
	
	
	
	/**
	 * 更新表
	 * @param tableName 表名
	 * @param updateFields 更新的字段
	 * @param whereString 条件字串
	 * @param wherePostValues 条件字串中的PostValues
	 * @return
	 */
	public static CommandResult QLUpdate(String tableName,
			List<QLUpdateField> updateFields, String whereString,
			Map<String, String> wherePostValues) 
	{
		CommandResult result = new CommandResult("false", "");
		Map<String,String> postValue = new HashMap<String, String>();
		StringBuilder strQL = new StringBuilder();
		strQL.append("UPDATE ");
		strQL.append(tableName);
		strQL.append(" SET ");
		
		for (QLUpdateField qlUpdateField : updateFields) {
			if (qlUpdateField.getNeedUpdate()) {
				if (qlUpdateField.getType().equals("datetime")
						&& (qlUpdateField.getValue() == null || qlUpdateField
								.getValue().equals(""))) {
					continue;
				} else if (qlUpdateField.getType().equals("int")
						&& (qlUpdateField.getValue() == null || qlUpdateField
								.getValue().equals(""))) {
					continue;
				} else {
					String fieldName = qlUpdateField.getField();
					strQL.append(fieldName + "=$f" + fieldName.toLowerCase()
							+ "$,");
					postValue.put("f" + fieldName.toLowerCase(), qlUpdateField.getValue());
				}
			}
		}
		strQL.deleteCharAt(strQL.lastIndexOf(","));
		strQL.append(" " + whereString + " ");
		
		if (wherePostValues != null && wherePostValues.size() > 0) {
			postValue.putAll(wherePostValues);
		}
		
		Service service = new Service();	
		CHttpConnectionCallback callback = service.Do(strQL.toString(), postValue);
		String apiResult=callback.get_JSON();
		if(apiResult.contains("请求的timestamp时间")){
			throw new TimestampException("客户端时间与服务器时间不一致");
		}
		
		result.setResult(callback.is_IsSuccess());		
		return result;
	}
	
	/**
	 * 调用Service查询，得到结果集
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @param strQL 查询的Sql语句
	 * @param PostValue 占位符Post参数及值
	 * @return
	 */
	public static List<Map<String,String>> qLGetList(int pageSize,
			int pageIndex, String strQL, Map<String, String> postValue)
	{
		if(pageSize==0)
			pageSize=ConstantsSetting.QLDefaultPageSize;
		if(pageIndex!=0)
			strQL = strQL + " ,page " + pageSize + "|" + pageIndex;
		Service service = new Service();	
		CHttpConnectionCallback callback = service.Do(strQL, postValue);
		String result=callback.get_JSON();
		if(result.contains("请求的timestamp时间")){
			throw new TimestampException("客户端时间与服务器时间不一致");
		}
		return callback.getLists();
	}
	
	/**
	 * 调用Service根据存储过程查询，得到结果集
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码
	 * @param strQL 查询的Sql语句
	 * @param PostValue 占位符Post参数及值
	 * @return
	 */
	public static List<Map<String,String>> qLGetListByProcedure(int pageSize,
			int pageIndex, String strQL, Map<String, String> postValue)
	{
		if(postValue == null)
			postValue = new HashMap<String, String>();
		if(pageSize==0)
			pageSize=ConstantsSetting.QLDefaultPageSize;
		Service service = new Service();
		postValue.put("pagesize", String.valueOf(pageSize));
		postValue.put("pageindex", String.valueOf(pageIndex));
		CHttpConnectionCallback callback = service.Do(strQL, postValue);
		String result=callback.get_JSON();
		if(result.contains("请求的timestamp时间")){
			throw new TimestampException("客户端时间与服务器时间不一致");
		}
		return callback.getLists();
	}
	
	/**
	 * 数据插入操作并得到操作结果
	 * @param strQL 插入的Sql语句
	 * @param postValue 占位符Post参数及值
	 * @return
	 */
	public static Boolean qLInsert(String strQL, Map<String, String> postValue)
	{
		Service service = new Service();	
		CHttpConnectionCallback callback = service.Do(strQL, postValue);
		return callback.is_IsSuccess();
	}
	
	/**
	 * 数据更新的操作并返回结果
	 * @param strQL
	 * @param postValue
	 * @return
	 */
	public static Boolean qlUpdate(String strQL,Map<String,String> postValue)
	{
		Service service = new Service();	
		CHttpConnectionCallback callback = service.Do(strQL, postValue);
		return callback.is_IsSuccess();	
	}
	
	/**
	 * 数据删除的操作并返回结果
	 * @param strQL
	 * @param postValue
	 * @return
	 */
	public static Boolean qlDelete(String strQL,Map<String,String> postValue)
	{		
		Service service = new Service();	
		CHttpConnectionCallback callback = service.Do(strQL, postValue);
		return callback.is_IsSuccess();	
	}
	
	
	private static UUID _dUuid;
	
	/**
	 * 获取设备唯一码
	 * @param context 传getbaseContext()
	 * @return
	 */
	public static UUID getCUUID(Context context)
	{
		if(_dUuid.equals(null))
		{
			DeviceUuidFactory duf=new DeviceUuidFactory(context);
			_dUuid = duf.getDeviceUuid();
		}
		return _dUuid;
	}
	
}
