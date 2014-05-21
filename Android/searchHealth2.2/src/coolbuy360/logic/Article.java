/**
 * 
 */
package coolbuy360.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yangxc
 *
 */
public class Article {
	
	/**
	 * ����ר���µ������б�����[ArticleID],[Title],[Image],[BigImage],[Resume],[OrderNo],[CreateTime]��ֵ��
	 * @param dissertationid ר��ID
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param pageIndex ҳ��
	 * @return
	 */
	public static List<Map<String,String>> getListOfDissertation(String dissertationid, int pageSize, int pageIndex)
	{
		String strQL = "plugin DrugCenter.Logic.V1 News GetArticlesOfDissertation($dissertationid$,$pagesize$,$pageindex$)";
		Map<String,String> postValue = new HashMap<String, String>();
		postValue.put("dissertationid", dissertationid);
		return ConstantsSetting.qLGetListByProcedure(pageSize, pageIndex, strQL, postValue);
	}
	
	/**
	 * ���ؽ�����Ѷ��Ŀ�µ������б�����[ArticleID],[Title],[Image],[BigImage],[Resume],[OrderNo],[UpdateTime]��ֵ��
	 * @param columnid ��ĿID
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param starttime ��ҳ��ʼʱ�������Ϊ���ַ�����ȡ��һҳ����
	 * @return
	 */
	public static List<Map<String,String>> getListOfColumn(String columnid, int pageSize, String starttime)
	{
		String strQL = "plugin DrugCenter.Logic.V1 News GetArticlesOfColumn($columnid$,$pagesize$,$starttime$)";
		Map<String,String> postValue = new HashMap<String, String>();
		postValue.put("columnid", columnid);
		if (starttime == null) {
			starttime = "";
		}
		postValue.put("starttime", starttime);
		return ConstantsSetting.qLGetListByProcedure(pageSize, 0, strQL, postValue);
	}
	
	/**
	 * �����������飬����[ArticleID],[Author],[Detail]��ֵ��
	 * @param articleid ����ID
	 * @return
	 */
	public static List<Map<String,String>> getInfo(String articleid)
	{
		String strQL = "plugin DrugCenter.Logic.V1 News GetArticleByID($articleid$)";
		Map<String,String> postValue = new HashMap<String, String>();
		postValue.put("articleid", articleid);
		return ConstantsSetting.qLGetListByProcedure(0, 1, strQL, postValue);
	}
	
	/**
	 * ��������
	 * @author yangxc
	 *
	 */
	public enum ArticleType{
		/**
		 * ��Ѷ��Ŀ
		 */
		column,
		/**
		 * ר��
		 */
		dissertation
	}

}
