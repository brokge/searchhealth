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
	 * 返回专题下的文章列表，包含[ArticleID],[Title],[Image],[BigImage],[Resume],[OrderNo],[CreateTime]键值。
	 * @param dissertationid 专题ID
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param pageIndex 页码
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
	 * 返回健康资讯栏目下的文章列表，包含[ArticleID],[Title],[Image],[BigImage],[Resume],[OrderNo],[UpdateTime]键值。
	 * @param columnid 栏目ID
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param starttime 翻页起始时间戳，若为空字符，则取第一页数据
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
	 * 返回文章详情，包含[ArticleID],[Author],[Detail]键值。
	 * @param articleid 文章ID
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
	 * 文章类型
	 * @author yangxc
	 *
	 */
	public enum ArticleType{
		/**
		 * 资讯栏目
		 */
		column,
		/**
		 * 专题
		 */
		dissertation
	}

}
