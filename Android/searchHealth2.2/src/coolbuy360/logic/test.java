package coolbuy360.logic;

import java.util.List;
import java.util.Map;

import coolbuy360.service.Service;
import coolbuy360.service.Service.CHttpConnectionCallback;

public class test {
	/**
	 * �߼��϶�QL��PostVlaue�������ã�����Service��Do����������List
	 * @return
	 */
	public static List<Map<String, String>> DoGetLists()
	{
		//�򵥲�ѯ
		String strQL = "SELECT DrugTypeID,DrugTypeName,ParentID,IsUse,OrderNum  FROM DRG_Type"; 
		//����ѯ
		//String strQL = "SELECT DRG_Type.DrugTypeName, DRG_Info.DrugName FROM DRG_Type INNER JOIN DRG_Info ON DRG_Type.DrugTypeID = DRG_Info.DrugTypeID where DRG_Type.drugtypename^'%ҩ%' order by DRG_Type.drugtypename desc,page 5|2";
		//��Ӽ�¼
		//String strQL = "insert into SYS_Ver(Os) values(ddd)";
		//�޸ļ�¼
		//String strQL = "update SYS_Ver set verno=333 where verid=3";
		//ɾ����¼
		//String strQL = "delete SYS_Ver where verid=3 or verid=4";
		Service service = new Service();
		
		//Map<String, String> PostValue = new HashMap<String,String>();
		//PostValue.put(("name", "");
		
		CHttpConnectionCallback callback = service.Do(strQL,null);
		
		callback.get_JSON();
		callback.get_PageIndex();
		callback.get_PageSize();
		callback.get_RecordCount();
		callback.is_IsSuccess();
		
		//callback.getLists();
		
		
		
		//����Է��ص�Lists�����߼��ϵĴ���
		
		//����Lists
		return callback.getLists();
	}
		

}
