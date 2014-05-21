/**
 * 
 */
package coolbuy360.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ��ȡҩƷ��������
 * @author yangxc
 */
public class DrugType {

	/**
	 * ����ҩƷһ�����༯�ϣ�����drugtypeid��drugtypename��parentid��ordernum�ĸ���ֵ��
	 */
	public static List<Map<String, String>> getList()
	{
		//�򵥲�ѯ
		String strQL = ConstantsSetting.QLRootDrugTypes;
		//����ѯ
		//String strQL = "SELECT DRG_Type.DrugTypeName, DRG_Info.DrugName FROM DRG_Type INNER JOIN DRG_Info ON DRG_Type.DrugTypeID = DRG_Info.DrugTypeID where DRG_Type.drugtypename^'%ҩ%' order by DRG_Type.drugtypename desc,page 5|2";
		//��Ӽ�¼
		//String strQL = "insert into SYS_Ver(Os) values(ddd)";
		//�޸ļ�¼
		//String strQL = "update SYS_Ver set verno=333 where verid=3";
		//ɾ����¼
		//String strQL = "delete SYS_Ver where verid=3 or verid=4";
		
		//Map<String, String> PostValue = new HashMap<String,String>();
		//PostValue.put(("name", "");
		
		//callback.get_JSON();
		//callback.get_PageIndex();
		//callback.get_PageSize();
		//callback.get_RecordCount();
		//callback.is_IsSuccess();
		
		//callback.getLists();
				
		//����Lists
		return ConstantsSetting.qLGetList(0, 0,strQL, null);	
	}
	
	/**
	 * ����ҩƷ�����Ӽ��ϣ�����drugtypeid��drugtypename��parentid��ordernum�ĸ���ֵ��
	 * @param parentid ��������ID
	 * @return
	 */
	public static List<Map<String,String>> getChildren(String parentid)
	{
		String strQL = ConstantsSetting.QLChildDrugTypesByParent; 
		strQL = String.format(strQL, parentid);
		return ConstantsSetting.qLGetList(0, 0,strQL, null);		
	}
	
	/**
	 * ��������ҩƷ����
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
	 * ���ڰ󶨵�ҩƷ���࣬����һ������Ͷ���������������
	 */
	public static class DrugTypes
	{
		public List<Map<String,String>> RootTypes;   //һ��ҩƷ����
		public List<List<Map<String,String>>> ChildTypes;	//����ҩƷ����
			
		public DrugTypes()
		{
			
		}
	}
}
