/**
 * 
 */
package coolbuy360.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ������ѯ
 * @author yangxc
 */
public class Disease {
	
	/**
	 * ���ݹؼ��ַ��ؼ������ϣ�����DiseaseID��DiseaseName��Part��DiseaseAlias��ֵ�����м�ֵСд��
	 * @param keyword �ؼ���
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ�룬0��ʾ����ҳ
	 * @return
	 */
	public static List<Map<String,String>> getListByKeyWord(String keyword, int pageSize, int pageIndex)
	{
		String strQL = ConstantsSetting.QLDiseasesByKeywords;
		//����ռλ��
		Map<String, String> postValue = new HashMap<String,String>();
		postValue.put("keyword", keyword);		
		return ConstantsSetting.qLGetList(pageSize, pageIndex, strQL, postValue);		
	}
	
	/**
	 * ���ݼ���ID��ѯ������ϸ��Ϣ������һ����¼��������ֵ��DiseaseID��DiseaseName��Part��DiseaseAlias�����м�ֵСд��
	 * @param diseaseID ����ID
	 * @return
	 */
	public static List<Map<String,String>> getInfo(String diseaseID)
	{
		String strQL = ConstantsSetting.QLDiseaseInfoByID;
		strQL = String.format(strQL, diseaseID);
		return ConstantsSetting.qLGetList(0, 0, strQL, null);
	}
	
	/**
	 * ��ѯ6�����������ؼ��ʣ�������ֵ��CommonDiseaseName��OrderNum��DiseaseID�����м�ֵСд��
	 * @return
	 */
	public static List<Map<String,String>> getCommonDiseases()
	{
		String strQL = ConstantsSetting.QLCommonDiseases;
		return ConstantsSetting.qLGetList(6, 1, strQL, null);
	}
}
