/**
 * 
 */
package coolbuy360.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取药品类型数据
 * @author yangxc
 */
public class DrugType {

	/**
	 * 返回药品一级分类集合，包含drugtypeid、drugtypename、parentid、ordernum四个键值。
	 */
	public static List<Map<String, String>> getList()
	{
		//简单查询
		String strQL = ConstantsSetting.QLRootDrugTypes;
		//多表查询
		//String strQL = "SELECT DRG_Type.DrugTypeName, DRG_Info.DrugName FROM DRG_Type INNER JOIN DRG_Info ON DRG_Type.DrugTypeID = DRG_Info.DrugTypeID where DRG_Type.drugtypename^'%药%' order by DRG_Type.drugtypename desc,page 5|2";
		//添加记录
		//String strQL = "insert into SYS_Ver(Os) values(ddd)";
		//修改记录
		//String strQL = "update SYS_Ver set verno=333 where verid=3";
		//删除记录
		//String strQL = "delete SYS_Ver where verid=3 or verid=4";
		
		//Map<String, String> PostValue = new HashMap<String,String>();
		//PostValue.put(("name", "");
		
		//callback.get_JSON();
		//callback.get_PageIndex();
		//callback.get_PageSize();
		//callback.get_RecordCount();
		//callback.is_IsSuccess();
		
		//callback.getLists();
				
		//返回Lists
		return ConstantsSetting.qLGetList(0, 0,strQL, null);	
	}
	
	/**
	 * 返回药品分类子集合，包含drugtypeid、drugtypename、parentid、ordernum四个键值。
	 * @param parentid 父级分类ID
	 * @return
	 */
	public static List<Map<String,String>> getChildren(String parentid)
	{
		String strQL = ConstantsSetting.QLChildDrugTypesByParent; 
		strQL = String.format(strQL, parentid);
		return ConstantsSetting.qLGetList(0, 0,strQL, null);		
	}
	
	/**
	 * 返回所有药品分类
	 */
	public static DrugTypes getAllDrugTypes()
	{
		String strQL = ConstantsSetting.QLAllDrugTypes; 		
		List<Map<String, String>> drugTypes = ConstantsSetting.qLGetList(0, 0, strQL, null);
		List<Map<String,String>> rootTypes = new ArrayList<Map<String,String>>();
		List<List<Map<String,String>>> childTypes = new ArrayList<List<Map<String,String>>>();
		List<String> rootIDs = new ArrayList<String>();
		for(Map<String, String> drugTypeItem:drugTypes)
		{
			if(drugTypeItem.get("parentid").equals(""))
			{
				rootTypes.add(drugTypeItem);
				rootIDs.add(drugTypeItem.get("drugtypeid").toString());				
				childTypes.add(new ArrayList<Map<String,String>>());
			}
			else
			{
				int parentIndex = rootIDs.indexOf(drugTypeItem.get("parentid"));
				childTypes.get(parentIndex).add(drugTypeItem);
			}
		}
		DrugTypes allDrugTypes = new DrugTypes();
		allDrugTypes.RootTypes=rootTypes;
		allDrugTypes.ChildTypes=childTypes;
		return allDrugTypes;
	}
	
	/**
	 * @author yangxc
	 * 用于绑定的药品分类，包含一级分类和二级分类两个集合
	 */
	public static class DrugTypes
	{
		public List<Map<String,String>> RootTypes;   //一级药品分类
		public List<List<Map<String,String>>> ChildTypes;	//二级药品分类
			
		public DrugTypes()
		{
			
		}
	}
}
