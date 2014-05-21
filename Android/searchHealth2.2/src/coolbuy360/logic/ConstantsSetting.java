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
 * ���ò���������
 * @author yangxc
 */
public class ConstantsSetting {
	
	/**
	 * Ĭ�ϵ�ҳ��������
	 */
	public final static int QLDefaultPageSize = 15;
	public final static int PromotionPreviewDays = 15;
	public final static String EfficiencyTestTag = "EfficiencyTest";
	
	//========= QL FormatString For DrugType ========
	/**
	 * ��ѯҩƷһ�����༯�ϣ�����DrugTypeID,DrugTypeName,ParentID,OrderNum�ĸ���ֵ��
	 */
	public static String QLRootDrugTypes = "SELECT DrugTypeID,DrugTypeName,ParentID,OrderNum FROM DRG_Type " +
		"where IsUse=1 and ParentID is null order by OrderNum desc,DrugTypeName asc"; 
	
	/**
	 * ���ݸ�������ID��ѯҩƷ�����Ӽ��ϣ�����DrugTypeID,DrugTypeName,ParentID,OrderNum�ĸ���ֵ��
	 * %1$s�������Ϊ��������ID
	 */
	public static String QLChildDrugTypesByParent = "SELECT DrugTypeID,DrugTypeName,ParentID,OrderNum FROM DRG_Type " +
			"where ParentID=%1$s and IsUse=1 order by OrderNum desc,DrugTypeName asc"; 
	
	/**
	 * ��ѯ����ҩƷ���༯�ϣ�����DrugTypeID,DrugTypeName,ParentID,OrderNum�ĸ���ֵ��
	 */
	public static String QLAllDrugTypes = "SELECT DrugTypeID,DrugTypeName,ParentID,OrderNum FROM DRG_Type " +
			"where IsUse=1 order by ParentID asc,OrderNum desc,DrugTypeName asc"; 
	
	
	//========= QL FormatString For Drug ========
	//����ҩƷ���Ϸ��ص��ֶ�
	private static String _drugListFields = "DRG_Info.DrugID as drugid" +  //ҩƷID
			",DRG_Info.DrugName as drugname" +   //ҩƷ����
			",DRG_Info.ApprovalNum as approvalnum" +  //��׼�ֺ�
			",dbo.drgApprovalTypeConverter(DRG_Info.ApprovalNum) as approvaltype" +  //��׼���ͣ�h-��ҩ��z-��ҩ��b-����Ʒ��s-������ҩ��j-����ҩƷ
			",DRG_Info.IsHCDrug as ishcdrug" +  //�Ƿ�ҽ��ҩ���0����ʾ���񡱣���1����ʾ���ǡ�
			",DRG_Info.PrescriptionType as prescriptiontype" +  //�������ͣ�1-����ҩ��2-����Ǵ���ҩ��3-����Ǵ���ҩ
			",DRG_Info.DrugTypeID as drugtypeid" +  //ҩƷ����ID
			",DRG_Info.DrugImg as drugimg" +  //ҩƷͼƬ
			",DRG_ProductionEnterprise.ProductionEnterpriseName as enterprisename ";  //������ҵ����
	
	public static String QLDrugsByDrugType = "SELECT " + _drugListFields +
			"FROM DRG_Info LEFT JOIN DRG_ProductionEnterprise " +
			"ON DRG_Info.ProductionEnterpriseID=DRG_ProductionEnterprise.ProductionEnterpriseID " +
			"where DrugTypeID=%1$s and DRG_Info.IsUse=1 order by drugname asc"; 
	
	public static String QLDrugsByKeywords = "SELECT " + _drugListFields +
			"FROM DRG_Info LEFT JOIN DRG_ProductionEnterprise " +
			"ON DRG_Info.ProductionEnterpriseID=DRG_ProductionEnterprise.ProductionEnterpriseID " +
			"where DRG_Info.IsUse=1 and " +
			"(DrugName^'%$keyword$%' " +   //��ҩƷ����
			"or GoodsName^'%$keyword$%' " +   //����Ʒ����
			"or GoodsNameEng^'%$keyword$%' " +   //��Ӣ������
			"or GoodsNamePY^'%$keyword$%' " +   //��ƴ�����
			"or Indication^'%$keyword$%') order by drugname asc";   //������֢״��
	
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
	
	//����ҩƷ���鷵�ص��ֶ�
	private static String _drugInfoFields = "DRG_Info.DrugID as drugid" +  //ҩƷID
			",DRG_Info.DrugName as drugname" +   //ҩƷ����
			",DRG_Info.BarCode as BarCode" +   //����
			",DRG_Info.ApprovalNum as approvalnum" +  //��׼�ֺ�
			",dbo.drgApprovalTypeConverter(DRG_Info.ApprovalNum) as approvaltype" +  //��׼���ͣ�h-��ҩ��z-��ҩ��b-����Ʒ��s-������ҩ��j-����ҩƷ
			",DRG_Info.GoodsName as GoodsName" +  //��Ʒ��
			",DRG_Info.GoodsNameEng as GoodsNameEng" +  //Ӣ����
			",DRG_Info.GoodsNamePY as GoodsNamePY" +  //����ƴ��
			",DRG_Info.Specification as Specification" +  //���
			",DRG_Info.Formulation as Formulation" +  //����
			",DRG_Info.Properties as Properties" +  //��״
			",DRG_Info.Packing as Packing" +  //��װ
			",DRG_Info.Composition as Composition" +  //�ɷ�
			",DRG_Info.Indication as Indication" +  //��Ӧ֢
			",DRG_Info.UsageDosage as UsageDosage" +  //�÷�����
			",DRG_Info.AdverseReactions as AdverseReactions" +  //������Ӧ
			",DRG_Info.Tabu as Tabu" +  //����
			",DRG_Info.Attention as Attention" +  //ע������
			",DRG_Info.Storage as Storage" +  //����
			",DRG_Info.IsHCDrug as ishcdrug" +  //�Ƿ�ҽ��ҩ���0����ʾ���񡱣���1����ʾ���ǡ�
			",DRG_Info.PrescriptionType as prescriptiontype" +  //�������ͣ�1-����ҩ��2-����Ǵ���ҩ��3-����Ǵ���ҩ
			",DRG_Info.ProductionAddress as ProductionAddress" +  //������ַ
			",DRG_Info.DrugImg as drugimg" +  //ҩƷͼƬ
			",DRG_ProductionEnterprise.ProductionEnterpriseName as enterprisename ";  //������ҵ����
	
	public static String QLDrugInfoByID = "SELECT " + _drugInfoFields +
			"FROM DRG_Info LEFT JOIN DRG_ProductionEnterprise " +
			"ON DRG_Info.ProductionEnterpriseID=DRG_ProductionEnterprise.ProductionEnterpriseID " +
			"where DRG_Info.DrugID='%1$s' order by drugname asc";
	
		
	//========= QL FormatString For �ع��� ========
	//����ڰ��ع���Ϣ���Ϸ��ص��ֶ�
	private static String _blackDrugListFields = "DRG_BlackList.BlackListID as BlackListID" +  //�ڰ�ID
			",DRG_BlackList.DrugID as DrugID" +   //ҩƷID
			",DRG_BlackList.PubTime as PubTime" +  //����ʱ��
			",DRG_BlackList.DangerLever as DangerLever" +  //Σ���̶�
			",DRG_BlackList.Danger as Danger " +  //Σ��
			",DRG_Info.DrugName as drugname " + //ҩƷ����
			",DRG_Info.DrugImg as DrugImg "; //ҩƷͼƬ
		
	public static String QLBlackDrugs = "SELECT " + _blackDrugListFields +
			"FROM DRG_BlackList LEFT JOIN DRG_Info " +
			"ON DRG_BlackList.DrugID = DRG_Info.DrugID " +
			"WHERE DRG_BlackList.IsUse=1 order by PubTime desc"; 
	
	//����ڰ��ع���Ϣ���鷵�ص��ֶ�
	private static String _blackDrugInfoFields = "DRG_BlackList.BlackListID as BlackListID" +  //�ڰ�ID
			",DRG_BlackList.DrugID as DrugID" +   //ҩƷID
			",DRG_BlackList.PubTime as PubTime" +  //����ʱ��
			",DRG_BlackList.ExpTime as ExpTime " +  //�ع�ʱ��
			",DRG_BlackList.DangerLever as DangerLever" +  //Σ���̶�
			",DRG_BlackList.ProductionDate as ProductionDate " +  //��������
			",DRG_BlackList.BatchNum as BatchNum " +  //����
			",DRG_BlackList.SamplingResults as SamplingResults " +  //�����
			",DRG_BlackList.Danger as Danger " +  //Σ��
			",DRG_BlackList.SrcURL as SrcURL " +  //��Դ��ҳ��ַ
			",DRG_BlackList.SrcTitle as SrcTitle " +  //��Դ��ҳ����
			",DRG_BlackList.ExpDescr as ExpDescr " +  //�ع�����
			",DRG_Info.DrugName as drugname " +  //ҩƷ����
			",DRG_Info.DrugImg as DrugImg "; //ҩƷͼƬ
		
	public static String QLBlackDrugInfoByID = "SELECT " + _blackDrugInfoFields +
			"FROM DRG_BlackList LEFT JOIN DRG_Info " +
			"ON DRG_BlackList.DrugID = DRG_Info.DrugID " +
			"WHERE DRG_BlackList.IsUse=1 And DRG_BlackList.BlackListID=%1$s " +
			"order by PubTime desc"; 

	//��������Ϣ���Ϸ��ص��ֶ�
	private static String _redDrugListFields = "DRG_RedList.RedListID as RedListID" +  //���ID
			",DRG_RedList.DrugID as DrugID" +   //ҩƷID
			",DRG_RedList.PubTime as PubTime" +  //����ʱ��
			",DRG_RedList.UpTime as UpTime" +  //�ϰ�ʱ��
			",DRG_RedList.SamplingResults as SamplingResults " +  //�����
			",DRG_Info.DrugName as drugname " +  //ҩƷ����
			",DRG_Info.DrugImg as DrugImg "; //ҩƷͼƬ
		
	public static String QLRedDrugs = "SELECT " + _redDrugListFields +
			"FROM DRG_RedList LEFT JOIN DRG_Info " +
			"ON DRG_RedList.DrugID = DRG_Info.DrugID " +
			"WHERE DRG_RedList.IsUse=1 order by PubTime desc"; 

	//��������Ϣ���鷵�ص��ֶ�
	private static String _redDrugInfoFields = "DRG_RedList.RedListID as RedListID" +  //���ID
			",DRG_RedList.DrugID as DrugID" +   //ҩƷID
			",DRG_RedList.PubTime as PubTime" +  //����ʱ��
			",DRG_RedList.UpTime as UpTime" +  //�ϰ�ʱ��
			",DRG_RedList.ProductionDate as ProductionDate " +  //��������
			",DRG_RedList.BatchNum as BatchNum " +  //����
			",DRG_RedList.SamplingResults as SamplingResults " +  //�����
			",DRG_RedList.SrcURL as SrcURL " +  //��Դ��ҳ��ַ
			",DRG_RedList.SrcTitle as SrcTitle " +  //��Դ��ҳ����
			",DRG_RedList.UpDescr as UpDescr " +  //�ϰ����
			",DRG_Info.DrugName as drugname " +  //ҩƷ����
			",DRG_Info.DrugImg as DrugImg "; //ҩƷͼƬ
		
	public static String QLRedDrugInfoByID = "SELECT " + _redDrugInfoFields +
			"FROM DRG_RedList LEFT JOIN DRG_Info " +
			"ON DRG_RedList.DrugID = DRG_Info.DrugID " +
			"WHERE DRG_RedList.IsUse=1 And DRG_RedList.RedListID=%1$s " +
			"order by PubTime desc"; 
	
	
	//========= QL FormatString For DrugStore ========
	//����ҩ�꼯�Ϸ��ص��ֶ�
	private static String _drugStoreListFields = "DST_Info.DrugStoreID as DrugStoreID" +  //ҩ��ID
			",DST_Info.DrugStoreName as DrugStoreName" +   //ҩ������
			",DST_Info.Tel as Tel" +  //�绰
			",DST_Info.Mobile as Mobile" +  //�ֻ�
			",DST_Info.IsTel as IsTel" +  //�Ƿ�绰Ԥ����0Ϊ��1Ϊ��
			",DST_Info.IsDoor as IsDoor" +  //�Ƿ��ͻ����ţ�0Ϊ��1Ϊ��
			",DST_Info.IsCOD as IsCOD" +  //�Ƿ�������0Ϊ��1Ϊ��
			",DST_Info.IsHC as IsHC" +  //�Ƿ�֧��ҽ����0Ϊ��1Ϊ��
			",DST_Info.Is24Hour as Is24Hour" +  //�Ƿ�24СʱӪҵ��0Ϊ��1Ϊ��
			",DST_Info.IsMember as IsMember" +  //�Ƿ��л�Ա�Żݣ�0Ϊ��1Ϊ��
			",DST_Info.LongValue as LongValue" +  //������
			",DST_Info.LatValue as LatValue" +  //γ����
			",DST_Info.Address as Address" +  //ҩ���ַ
			",dbo.dstGetDistance(DST_Info.LatValue,DST_Info.LongValue,%1$s,%2$s) as distance" +  //�뵱ǰλ�õľ��룬��λ����
			",dbo.dstPromotionOn(DST_Info.DrugStoreID, " + PromotionPreviewDays + ") as ispromotionon ";  //�����Ƿ����Żݴ����

	private static String _drugStoreListFieldsMapMode = "DST_Info.DrugStoreID as DrugStoreID" +  //ҩ��ID
			",DST_Info.DrugStoreName as DrugStoreName" +   //ҩ������
			",DST_Info.LongValue as LongValue" +  //������
			",DST_Info.LatValue as LatValue" +  //γ����
			",dbo.dstGetDistance(DST_Info.LatValue,DST_Info.LongValue,%1$s,%2$s) as distance ";  //�뵱ǰλ�õľ��룬��λ����

	private static String _drugStoreListFieldsNoLoc = "DST_Info.DrugStoreID as DrugStoreID" +  //ҩ��ID
			",DST_Info.DrugStoreName as DrugStoreName" +   //ҩ������
			",DST_Info.Tel as Tel" +  //�绰
			",DST_Info.Mobile as Mobile" +  //�ֻ�
			",DST_Info.IsTel as IsTel" +  //�Ƿ�绰Ԥ����0Ϊ��1Ϊ��
			",DST_Info.IsDoor as IsDoor" +  //�Ƿ��ͻ����ţ�0Ϊ��1Ϊ��
			",DST_Info.IsCOD as IsCOD" +  //�Ƿ�������0Ϊ��1Ϊ��
			",DST_Info.IsHC as IsHC" +  //�Ƿ�֧��ҽ����0Ϊ��1Ϊ��
			",DST_Info.Is24Hour as Is24Hour" +  //�Ƿ�24СʱӪҵ��0Ϊ��1Ϊ��
			",DST_Info.IsMember as IsMember" +  //�Ƿ��л�Ա�Żݣ�0Ϊ��1Ϊ��
			",DST_Info.Address as Address" +  //ҩ���ַ
			",dbo.dstPromotionOn(DST_Info.DrugStoreID, " + PromotionPreviewDays + ") as ispromotionon " ;  //�����Ƿ����Żݴ����
	
	private static String _drugStoreInfoFields = "DST_Info.DrugStoreID as DrugStoreID" +  //ҩ��ID
			",DST_Info.DrugStoreName as DrugStoreName" +   //ҩ������
			",DST_Info.Address as Address" +  //ҩ���ַ
			",DST_Info.LongValue as LongValue" +  //������
			",DST_Info.LatValue as LatValue" +  //γ����
			",DST_Info.Tel as Tel" +  //�绰
			",DST_Info.Mobile as Mobile" +  //�ֻ�
			",DST_Info.IsTel as IsTel" +  //�Ƿ�绰Ԥ����0Ϊ��1Ϊ��
			",DST_Info.IsDoor as IsDoor" +  //�Ƿ��ͻ����ţ�0Ϊ��1Ϊ��
			",DST_Info.IsCOD as IsCOD" +  //�Ƿ�������0Ϊ��1Ϊ��
			",DST_Info.IsHC as IsHC" +  //�Ƿ�֧��ҽ����0Ϊ��1Ϊ��
			",DST_Info.Intro as Intro" +  //ҩ����
			",DST_Info.Logo as Logo" +  //ҩ��Logo
			",DST_Info.BusinessTime as BusinessTime" +  //Ӫҵʱ��
			",DST_Info.Is24Hour as Is24Hour" +  //�Ƿ�24СʱӪҵ��0Ϊ��1Ϊ��
			",DST_Info.IsMember as IsMember" +  //�Ƿ��л�Ա�Żݣ�0Ϊ��1Ϊ��
			",DST_Info.DoorContent as DoorContent" +  //�ͻ����ŵķ�Χ
			",DST_Info.OtherService as OtherService" +  //��������
			",dbo.dstGetDistance(DST_Info.LatValue,DST_Info.LongValue,%1$s,%2$s) as distance" +  //�뵱ǰλ�õľ��룬��λ����
			",dbo.dstGetFullAddress(DST_Info.District,DST_Info.Address) as FullAddress ";  //������ַ	
		
	public static String QLWhereToBuyDrug = "SELECT " + _drugStoreListFields +
			",DST_DrugPrice.OldPrice as OldPrice " +  //ҩƷԭ��
			",DST_DrugPrice.NowPrice as NowPrice " +  //ҩƷ��ǰ��
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
			",DST_DrugPrice.OldPrice as OldPrice " +  //ҩƷԭ��
			",DST_DrugPrice.NowPrice as NowPrice " +  //ҩƷ��ǰ��
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
	//����ҩ���������ص��ֶ�
	private static String _promotionListFields = "PromotionID" +  //�����ID
			",Title" +   //����
			",Image" +   //ͼƬ
			",BeginTime" +   //��ʼʱ��
			",CreateTime" +   //����ʱ��
			",OrderNo ";  //����
	
	/**
	 * ����1��ҩ��ID������2�����죬����3������
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
	
	private static String _promotionInfoFields = "PromotionID" +  //�����ID
			",Title" +   //����
			",Image" +   //ͼƬ
			",BeginTime" +   //��ʼʱ��
			",EndTime" +   //����ʱ��
			",Detail ";   //����
	
	public static String QLPromotionInfo = "SELECT " + _promotionInfoFields +
			"from DST_Promotion " +
			"where State=1 " +
			"and PromotionID=%1$s "; 	
		
	//========= QL FormatString For Disease ========
	//���弲�����Ϸ��ص��ֶ�
	private static String _diseaseListFields = "DIS_Info.DiseaseID as DiseaseID" +  //����ID
			",DIS_Info.DiseaseName as DiseaseName" +   //��������
			",DIS_Info.Part as Part" +  //��λ
			",DIS_Info.DiseaseAlias as DiseaseAlias ";  //��������

	//���弲�����鷵�ص��ֶ�
	private static String _diseaseInfoFields = "DIS_Info.DiseaseID as DiseaseID" +  //����ID
			",DIS_Info.DiseaseName as DiseaseName" +   //��������
			",DIS_Info.Part as Part" +  //��λ
			",DIS_Info.Pathogeny as Pathogeny" +  //����
			",DIS_Info.Symptoms as Symptoms" +  //֢״����
			",DIS_Info.Descr as Descr" +  //����
			",DIS_Info.PreventiveCare as PreventiveCare" +  //Ԥ������
			",DIS_Info.DiseaseAlias as DiseaseAlias" +  //��������
			",DIS_Info.DiffDiag as DiffDiag" +  //��ϼ���
			",DIS_Info.Treat as Treat ";  //����

	public static String QLDiseasesByKeywords = "SELECT " + _diseaseListFields +
			"FROM DIS_Info Left Join APP_Null on DIS_Info.DiseaseName = APP_Null.id " +
			"where DIS_Info.IsUse=1 and " +
			"(DIS_Info.DiseaseName^'%$keyword$%' " +   //�Ӽ������Ʋ�
			"or DIS_Info.Part^'%$keyword$%' " +   //�Ӳ�λ��
			"or DIS_Info.Symptoms^'%$keyword$%' " +   //��֢״���ֲ�
			"or DIS_Info.Descr^'%$keyword$%' " +   //�Ӹ�����
			"or DIS_Info.DiseaseAlias^'%$keyword$%') order by LEN(DiseaseName) asc,DiseaseID asc";   //�Ӽ���������

	public static String QLDiseaseInfoByID = "SELECT " + _diseaseInfoFields +
			"FROM DIS_Info Left Join APP_Null on DIS_Info.DiseaseName = APP_Null.id " +
			"where DIS_Info.DiseaseID=%1$s order by DiseaseName asc";
	
	//���峣���������Ϸ����ֶ�
	private static String _commonDiseaseListFields = "CommonDiseaseName" +  //����
			",OrderNum" +   //����
			",DiseaseID ";  //����ID

	public static String QLCommonDiseases = "SELECT " + _commonDiseaseListFields +
			"FROM DIS_Common " +
			"where IsUse=1 " +
			"order by OrderNum desc"; 

	//========= QL FormatString For Version ========
	//����汾��Ϣ���ص��ֶ�
	private static String _versionListFields = "VerNo" +  //�汾��
			",VerName" +   //�汾����
			",UpdateDescr" +  //����˵��
			",FileSize" +  //�ļ���С
			",IsNecessary" +  //�Ƿ�������
			",UpdateTime" +  //����ʱ��
			",UpdateURL ";  //���µ�ַ
	
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
	
	//�����û���Ϣ�ķ����ֶ�
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
	     
	private static String _userInfoFields=" MemberID"+//��Աid
				",UserName"+//�û�����
				",MemberName "+//��Ա����
				",Email  "+//��Աemail
				",Mobile  "+//��Ա�ֻ���
				",Sex"+//��Ա�Ա�
				",Job  "+//��Աְҵ
				",HC  "+//ҽ�����:�������ޣ�����
				",Income  "+//ҽ��֧��
				",BuyWay "+//����;��
				",FamilyAddress  "+//��ͥסַ
				",IdCardNum "+//���֤��
				",Birthday "+//��������
				",Telephone "+//�绰
				",EducationLevel "+//����ˮƽ
				",CommonHospital  "//��ѡҽԺ
						;
	public static String QLGetUserInfo="SELECT"+_userInfoFields+
				"FROM USR_Member "+
				"where MemberID=%1$s";
	
//	update Table set a=1,b=2,c=$c$ where id=1 and id=2 or name=$name$ and name^���� ģ����ѯ 
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
	private static String _healthCalendarInfoFields = " ID,MemberID" +//��Աid
			",EventTime" +	//�¼�ʱ��
		    ",Summarize" +	//����
		    ",Remarks" +	//˵��
		    ",CreateTime "; //����ʱ��
		
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
  	//�����Ա����ʷ���ص��ֶ�
  	private static String _memberDiseaseListFields = "ID" +  //����ʷID
  			",DiseaseName" +   //��������
  			",DiagnoseTime" +  //ȷ��ʱ��
  			",IsContagious" +  //�Ƿ�Ⱦ
  			",IsHereditary" +  //�Ƿ��Ŵ�
  			",Remarks" +  //˵��
  			",CreateTime ";  //����ʱ��
  	
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
	//�����Ա����ʷ���ص��ֶ�
	private static String _memberAllergicListFields = "ID" +  //����ʷID
			",Allergen" +   //����Դ
			",OccurrenceTime" +  //����ʱ��
			",Symptom" +  //֢״
			",Remarks" +  //˵��
			",CreateTime ";  //����ʱ��
	
	public static String QLMemberAllergicsByMemberID = "SELECT " + _memberAllergicListFields +
			"FROM MBR_Allergic " +
			"where MemberID=%1$s " +
			"order by OccurrenceTime desc,CreateTime desc ";
	
	public static String QLInsertMemberAllergic = "insert into MBR_Allergic" +
			"(MemberID,Allergen,OccurrenceTime,Symptom,Remarks,CreateTime) " +
			"VALUES($memberid$,$allergen$,$occurrencetime$,$symptom$,$remarks$,$createtime$)";
	
	public static String qLDeleteMemberAllergic = "delete MBR_Allergic WHERE ID=%1$s";
	
	//========= QL FormatString For Operation ========
	//�����Ա������¼���ص��ֶ�
	private static String _memberOperationListFields = "ID" +  //����ʷID
			",OperationName" +   //����Դ
			",ImplementTime" +  //����ʱ��
			",Remarks" +  //˵��
			",CreateTime ";  //����ʱ��
	
	public static String QLMemberOperationsByMemberID = "SELECT " + _memberOperationListFields +
			"FROM MBR_Operation " +
			"where MemberID=%1$s " +
			"order by ImplementTime desc,CreateTime desc ";
	
	public static String QLInsertMemberOperation = "insert into MBR_Operation" +
			"(MemberID,OperationName,ImplementTime,Remarks,CreateTime) " +
			"VALUES($memberid$,$operationname$,$implementtime$,$remarks$,$createtime$)";
	
	public static String qLDeleteMemberOperation = "delete MBR_Operation WHERE ID=%1$s";
	
	//========= QL FormatString For Favorite ========
	//����ҩƷ�ղؼ��Ϸ��ص��ֶ�
	private static String _drugFavListFields = "USR_APPUserDrugFav.AppUserID as appuserid" +  //�û�Ӧ��ID
			",USR_APPUserDrugFav.DrugFavID as drugfavid" +   //ҩƷ�ղؼ�¼ID
			",USR_APPUserDrugFav.FavTime as favtime" ;  //ҩƷ�ղ�ʱ��
	
	//����ҩ���ղؼ��Ϸ��ص��ֶ�
	private static String _drugStoreFavListFields = "USR_APPUserDrugStoreFav.AppUserID as appuserid" +  //�û�Ӧ��ID
			",USR_APPUserDrugStoreFav.DrugStoreFavID as drugstorefavid" +   //ҩ���ղؼ�¼ID
			",USR_APPUserDrugStoreFav.FavTime as favtime" ;  //ҩ���ղ�ʱ��
	
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
	
	//========= QL FormatString For ΢��ҽ�� ========
	//����΢��ҽ�����Ϸ��ص��ֶ�
	private static String _WeiboDocListFields = "WBD_Info.WBDoctorID as WBDoctorID" +  //΢��ҽ��ID
			",WBD_Info.DoctorName as DoctorName" +   //ҽ������
			",WBD_Info.WBUserName as WBUserName" +   //΢���˺�
			",WBD_Info.GoodDomain as GoodDomain" +   //�ó�����
			",WBD_Info.WBUrl as WBUrl" ;  //΢����ַ
	
	public static String QLWeiboDocs = "SELECT " + _WeiboDocListFields +
			" from WBD_Info Left Join APP_Null on WBD_Info.DoctorName = APP_Null.id " +
			"where WBD_Info.IsUse=1 " +
			"order by DoctorName asc "; 	
	
	
	//========= QL FormatString For ��Ϣ����[PUSH_User] ========
	//����pushuser����
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
	 * ���±�
	 * @param tableName ����
	 * @param updateFields ���µ��ֶ�
	 * @param whereString �����ִ�
	 * @param wherePostValues �����ִ��е�PostValues
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
		if(apiResult.contains("�����timestampʱ��")){
			throw new TimestampException("�ͻ���ʱ���������ʱ�䲻һ��");
		}
		
		result.setResult(callback.is_IsSuccess());		
		return result;
	}
	
	/**
	 * ����Service��ѯ���õ������
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
	 * @param strQL ��ѯ��Sql���
	 * @param PostValue ռλ��Post������ֵ
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
		if(result.contains("�����timestampʱ��")){
			throw new TimestampException("�ͻ���ʱ���������ʱ�䲻һ��");
		}
		return callback.getLists();
	}
	
	/**
	 * ����Service���ݴ洢���̲�ѯ���õ������
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ��
	 * @param strQL ��ѯ��Sql���
	 * @param PostValue ռλ��Post������ֵ
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
		if(result.contains("�����timestampʱ��")){
			throw new TimestampException("�ͻ���ʱ���������ʱ�䲻һ��");
		}
		return callback.getLists();
	}
	
	/**
	 * ���ݲ���������õ��������
	 * @param strQL �����Sql���
	 * @param postValue ռλ��Post������ֵ
	 * @return
	 */
	public static Boolean qLInsert(String strQL, Map<String, String> postValue)
	{
		Service service = new Service();	
		CHttpConnectionCallback callback = service.Do(strQL, postValue);
		return callback.is_IsSuccess();
	}
	
	/**
	 * ���ݸ��µĲ��������ؽ��
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
	 * ����ɾ���Ĳ��������ؽ��
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
	 * ��ȡ�豸Ψһ��
	 * @param context ��getbaseContext()
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
