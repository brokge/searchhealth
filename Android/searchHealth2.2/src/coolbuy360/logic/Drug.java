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
 * ҩƷ��ѯ
 * @author yangxc
 */
public class Drug {
		
	/**
	 * ���ݷ��෵��ҩƷ���ϣ�����ҳ��
	 * @param drugTypeid ҩƷ����ID 
	 * @return
	 */
	public static List<Map<String,String>> getListByDrugType(String drugTypeid)
	{
		return getListByDrugType(drugTypeid,0,0);	
	}	
	
	/**
	 * ���ݷ��෵��ҩƷ���ϣ�����drugid��drugname��approvalnum��approvaltype, ishcdrug��prescriptiontype��
	 * drugtypeid��drugimg��enterprisename��ֵ��
	 * @param drugTypeid ҩƷ����ID
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
	 * @return
	 */
	public static List<Map<String,String>> getListByDrugType(String drugTypeid, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLDrugsByDrugType;
		strQL = String.format(strQL, drugTypeid);		
		return ConstantsSetting.qLGetList(pageSize, pageIndex,strQL, null);		
	}
	
	/**
	 * ���ݹؼ��ַ���ҩƷ���ϣ�����ҳ
	 * @param keyword �ؼ���
	 * @return
	 */
	public static List<Map<String,String>> getListByKeyWord(String keyword)
	{
		return getListByKeyWord(keyword,0,0);
	}
	
	/**
	 * ���ݹؼ��ַ���ҩƷ���ϣ�����drugid��drugname��approvalnum��approvaltype, ishcdrug��prescriptiontype��
	 * drugtypeid��drugimg��enterprisename��ֵ��
	 * @param keyword �ؼ���
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
	 * @return
	 */
	public static List<Map<String,String>> getListByKeyWord(String keyword, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLDrugsByKeywords;
		//����ռλ��
		Map<String, String> postValue = new HashMap<String,String>();
		postValue.put("keyword", keyword);		
		return ConstantsSetting.qLGetList(pageSize, pageIndex,strQL, postValue);		
	}
	
	/**
	 * ���ݼ�������ҩƷ���ϣ�����ҳ
	 * @param diseaseid ����ID
	 * @return
	 */
	public static List<Map<String,String>> getListByDisease(String diseaseid)
	{
		return getListByDisease(diseaseid,0,0);
	}
	
	/**
	 * ���ݼ�������ҩƷ���ϣ�����drugid��drugname��approvalnum��approvaltype, ishcdrug��prescriptiontype��
	 * drugtypeid��drugimg��enterprisename��ֵ��
	 * @param diseaseid ����ID
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
	 * @return
	 */
	public static List<Map<String,String>> getListByDisease(String diseaseid, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLDrugsByDisease;
		strQL = String.format(strQL, diseaseid);		
		return ConstantsSetting.qLGetList(pageSize, pageIndex,strQL, null);		
	}
	
	/**
	 * �����������ҩƷ������ҳ
	 * @param barCode ������
	 * @return
	 */
	public static List<Map<String,String>> getListByBarCode(String barCode)
	{
		return getListByBarCode(barCode,0,0);
	}

	/**
	 * �����������ҩƷ������drugid��drugname��approvalnum��approvaltype, ishcdrug��prescriptiontype��
	 * drugtypeid��drugimg��enterprisename��ֵ��
	 * @param barCode ������
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
	 * @return
	 */
	public static List<Map<String,String>> getListByBarCode(String barCode, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLDrugsByBarCode;
		strQL = String.format(strQL, barCode);
		return ConstantsSetting.qLGetList(pageSize, pageIndex,strQL, null);		
	}
	
	/**
	 * ���Ҹ����ܹ��򵽶�ӦҩƷ��ҩ�꣬����ҳ
	 * @param drugID ҩƷID
	 * @param distance �������뷶Χ����λ�����
	 * @param currentLat ��ǰλ��γ����
	 * @param currentLong ��ǰλ�þ�����
	 * @param isHC �Ƿ�ҽ��ָ��ҩ��ɸѡ������-1��ʾ���ޣ�0��ʾ��1��ʾ��
	 * @return
	 */
	public static List<Map<String,String>> whereToBuy(String drugID, double distance, double currentLat, double currentLong, 
			int isHC)
	{
		return whereToBuy(drugID, distance, currentLat, currentLong,0,0,isHC);
	}
	
	/**
	 * ���Ҹ����ܹ��򵽶�ӦҩƷ��ҩ�꣬��ҳ���ң�������ֵ��DrugStoreID��DrugStoreName��Tel��Mobile��IsTel��IsDoor��IsCOD��
	 * IsHC��Is24Hour��IsMember��LongValue��LatValue��Address��distance��OldPrice��NowPrice�����м�ֵСд��
	 * @param drugID ҩƷID
	 * @param distance �������뷶Χ����λ�����
	 * @param currentLat ��ǰλ��γ����
	 * @param currentLong ��ǰλ�þ�����
	 * @param isHC �Ƿ�ҽ��ָ��ҩ��ɸѡ������-1��ʾ���ޣ�0��ʾ��1��ʾ��
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
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
	 * ��ͼģʽ���Ҹ����ܹ��򵽶�ӦҩƷ��ҩ�꣬������ֵ��DrugStoreID��DrugStoreName��LongValue��LatValue��distance�����м�ֵСд��
	 * @param drugID ҩƷID
	 * @param distance �������뷶Χ����λ�����
	 * @param currentLat ��ǰλ��γ����
	 * @param currentLong ��ǰλ�þ�����
	 * @param isHC �Ƿ�ҽ��ָ��ҩ��ɸѡ������-1��ʾ���ޣ�0��ʾ��1��ʾ��
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
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
	 * ����ȫ���ܹ��򵽶�ӦҩƷ��ҩ�꣬��ҳ���ң�������ֵ��DrugStoreID��DrugStoreName��Tel��Mobile��IsTel��IsDoor��IsCOD��
	 * IsHC��Is24Hour��IsMember��LongValue��LatValue��Address��distance��OldPrice��NowPrice�����м�ֵСд��
	 * @param drugID ҩƷID
	 * @param cityName ��������
	 * @param currentLat ��ǰλ��γ����
	 * @param currentLong ��ǰλ�þ�����
	 * @param isHC �Ƿ�ҽ��ָ��ҩ��ɸѡ������-1��ʾ���ޣ�0��ʾ��1��ʾ��
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
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
	 * ��ͼģʽ����ȫ���ܹ��򵽶�ӦҩƷ��ҩ�꣬������ֵ��DrugStoreID��DrugStoreName��LongValue��LatValue��distance�����м�ֵСд��
	 * @param drugID ҩƷID
	 * @param cityName ��������
	 * @param currentLat ��ǰλ��γ����
	 * @param currentLong ��ǰλ�þ�����
	 * @param isHC �Ƿ�ҽ��ָ��ҩ��ɸѡ������-1��ʾ���ޣ�0��ʾ��1��ʾ��
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
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
	 * ����ҩƷID��ѯҩƷ��ϸ��Ϣ������һ����¼
	 * @param drugID ҩƷID
	 * @return
	 */
	public static List<Map<String,String>> getInfo(String drugID)
	{
		String strQL = ConstantsSetting.QLDrugInfoByID;
		strQL = String.format(strQL, drugID);
		return ConstantsSetting.qLGetList(0, 0,strQL, null);
	}
	
	/**
	 * ҩƷ����Key����������
	 */
	public static Map<String,String> DrugProperties = new HashMap<String, String>()
	{
		{
			put("drugname","ҩƷ����");
			put("barcode","����");
			put("approvalnum","��׼�ֺ�");
			put("goodsname","��Ʒ��");
			put("goodsnameeng","Ӣ����");
			put("goodsnamepy","ƴ��");
			put("specification","���");
			put("formulation","����");
			put("properties","��״");
			put("packing","��װ");
			put("composition","�ɷ�");
			put("indication","��Ӧ֢");
			put("usagedosage","�÷�����");
			put("adversereactions","������Ӧ");
			put("tabu","����");
			put("attention","ע������");
			put("storage","����");
			put("enterprisename","������ҵ");
			put("productionaddress","������ַ");
		}			
	};		
		
	/**
	 * ��ҩƷ�ڰ񼯺ϣ�����drugid��drugname����ֵ��
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
	 * @return
	 */
	public static List<Map<String,String>> getBlackList(int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLBlackDrugs;
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);		
	}
		
	/**
	 * ��ҩƷ�ڰ����飬����drugid��drugname����ֵ��
	 * @param blackID �ڰ�ID
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
	 * @return
	 */
	public static List<Map<String,String>> getBlackInfo(String blackID, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLBlackDrugInfoByID;
		strQL = String.format(strQL, blackID);
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);		
	}	
		
	/**
	 * ��ҩƷ��񼯺ϣ�����drugid��drugname����ֵ��
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
	 * @return
	 */
	public static List<Map<String,String>> getRedList(int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLRedDrugs;
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);		
	}
		
	/**
	 * ��ҩƷ������飬����drugid��drugname����ֵ��
	 * @param redID ���ID
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
	 * @return
	 */
	public static List<Map<String,String>> getRedInfo(String redID, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLRedDrugInfoByID;
		strQL = String.format(strQL, redID);
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, null);		
	}
	
	/**
	 * �ж�ҩƷ�Ƿ��Ѿ����ղ�
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
	 * �ղ�ҩƷ
	 * @param context
	 * @param drugID 
	 * @return
	 */
	public static CommandResult doCollect(Context context,String drugID)
	{
		return DrugFavorite.SetDrugFav(context, drugID, FavOperationType.Add);
	}
	
	/**
	 * ȡ��ҩƷ�ղ�
	 * @param context
	 * @param drugID
	 * @return
	 */
	public static CommandResult unCollect(Context context,String drugID)
	{
		return DrugFavorite.SetDrugFav(context, drugID, FavOperationType.Remove);
	}
	
	
}
