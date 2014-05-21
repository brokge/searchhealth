package coolbuy360.logic;

import java.util.List;
import java.util.Map;

import coolbuy360.service.Service;
import coolbuy360.service.Service.CHttpConnectionCallback;

public class test {
	/**
	 * 逻辑上对QL和PostVlaue进行设置，调用Service的Do方法，返回List
	 * @return
	 */
	public static List<Map<String, String>> DoGetLists()
	{
		//简单查询
		String strQL = "SELECT DrugTypeID,DrugTypeName,ParentID,IsUse,OrderNum  FROM DRG_Type"; 
		//多表查询
		//String strQL = "SELECT DRG_Type.DrugTypeName, DRG_Info.DrugName FROM DRG_Type INNER JOIN DRG_Info ON DRG_Type.DrugTypeID = DRG_Info.DrugTypeID where DRG_Type.drugtypename^'%药%' order by DRG_Type.drugtypename desc,page 5|2";
		//添加记录
		//String strQL = "insert into SYS_Ver(Os) values(ddd)";
		//修改记录
		//String strQL = "update SYS_Ver set verno=333 where verid=3";
		//删除记录
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
		
		
		
		//这里对返回的Lists进行逻辑上的处理
		
		//返回Lists
		return callback.getLists();
	}
		

}
