/**
 * 
 */
package coolbuy360.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 疾病查询
 * @author yangxc
 */
public class Disease {
	
	/**
	 * 根据关键字返回疾病集合，包含DiseaseID、DiseaseName、Part、DiseaseAlias键值，所有键值小写。
	 * @param keyword 关键字
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码，0表示不分页
	 * @return
	 */
	public static List<Map<String,String>> getListByKeyWord(String keyword, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLDiseasesByKeywords;
		//定义占位符
		Map<String, String> postValue = new HashMap<String,String>();
		postValue.put("keyword", keyword);		
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, postValue);		
	}
	
	/**
	 * 根据疾病ID查询疾病详细信息，返回一条记录，包含键值：DiseaseID、DiseaseName、Part、DiseaseAlias。所有键值小写。
	 * @param diseaseID 疾病ID
	 * @return
	 */
	public static List<Map<String,String>> getInfo(String diseaseID)
	{
		String strQL = ConstantsSetting.QLDiseaseInfoByID;
		strQL = String.format(strQL, diseaseID);
		return ConstantsSetting.qLGetList(0, 0, strQL, null);
	}
	
	/**
	 * 查询6条常见疾病关键词，包含键值：CommonDiseaseName、OrderNum、DiseaseID。所有键值小写。
	 * @return
	 */
	public static List<Map<String,String>> getCommonDiseases()
	{
		String strQL = ConstantsSetting.QLCommonDiseases;
		return ConstantsSetting.qLGetList(6, 1, strQL, null);
	}
}
