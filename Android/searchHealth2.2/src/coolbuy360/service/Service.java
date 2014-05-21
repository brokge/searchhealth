package coolbuy360.service;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import coolbuy360.logic.ConstantsSetting;
import coolbuy360.service.HttpConnection.HttpConnectionCallback;
import coolbuy360.service.HttpConnection.HttpMethod;

public class Service {
	private static String _Ver = "1";
	private static String _AppKey = "12361";
	private static String _AppSecret = "2be271bf4d8f48c3a2965d149de6a73a";
	private static String _Server = "http://api.wcjk100.com/";
	//private static String _Server = "http://192.168.2.90/";

	/**
	 * 提供QL请求
	 * @param QL 查询字符串
	 * @return
	 */
	public CHttpConnectionCallback Do(String QL) {
		return Do(QL, null);
	}

	/**
	 * 提供QL请求
	 * @param QL 查询字符串
	 * @param PostValue Map<String, String> 占位符使用的参数值
	 * @return
	 */
	public CHttpConnectionCallback Do(String QL, Map<String, String> PostValue) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(_Server);
			sb.append("api.ashx?");
			sb.append("ver=" + _Ver + "&");
			sb.append("appkey=" + _AppKey + "&");
			Date date = new Date();
			SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String times = from.format(date);
			sb.append("timestamp=" + times + "&");
			sb.append("sign=" + MD5(_AppKey + _AppSecret + times));
			if(PostValue==null){
				PostValue = new HashMap<String, String>();
			}
			PostValue.put("ql", QL);
			
			//Log.i(ConstantsSetting.EfficiencyTestTag, "begin to call server api.");
			
			String url = sb.toString();
			HttpConnection http = new HttpConnection();
			CHttpConnectionCallback callback = new CHttpConnectionCallback();
			if (PostValue == null) {
				http.syncConnect(url, HttpMethod.GET, callback);
			} else {
				http.syncConnect(url, PostValue, HttpMethod.POST, callback);
			}
			return callback;
		} catch (Exception ex) {
			return null;
		}

	}

	/**
	 * 处理JSON到对象Lists
	 * @author hzumu.com
	 *
	 */
	public class CHttpConnectionCallback implements HttpConnectionCallback {
		private String _JSON;

		public String get_JSON() {
			return _JSON;
		}

		public void set_JSON(String _JSON) {
			this._JSON = _JSON;
		}

		private List<Map<String, String>> Lists;

		public List<Map<String, String>> getLists() {
			return Lists;
		}

		public void setLists(List<Map<String, String>> lists) {
			Lists = lists;
		}

		private String _PageSize,_PageIndex,_RecordCount;
		
		public String get_PageSize() {
			return _PageSize;
		}

		public void set_PageSize(String _PageSize) {
			this._PageSize = _PageSize;
		}

		public String get_PageIndex() {
			return _PageIndex;
		}

		public void set_PageIndex(String _PageIndex) {
			this._PageIndex = _PageIndex;
		}

		public String get_RecordCount() {
			return _RecordCount;
		}

		private boolean _IsSuccess = false;
		
		public boolean is_IsSuccess() {
			return _IsSuccess;
		}

		public void set_IsSuccess(boolean _IsSuccess) {
			this._IsSuccess = _IsSuccess;
		}

		public void set_RecordCount(String _RecordCount) {
			this._RecordCount = _RecordCount;
		}

		@Override
		public void execute(String response) {
			
			//Log.i(ConstantsSetting.EfficiencyTestTag, "api call back.");
			
			this._JSON = response;
			// 将数据加载到Lists中
			try {  
				JSONObject jsonObject = new JSONObject(response);//转换为JSONObject
				String _Command = "";
				Iterator<?> it = jsonObject.keys();
	            while(it.hasNext()){//遍历JSONObject  
	            	_Command = (String) it.next().toString();
	            	break;
	            }  
				
			    if(_Command.equals("error_response"))//
			    {
			    	Lists = null;
			    }
			    else if(_Command.indexOf("select")>=0)
			    {
			    	Lists = new ArrayList<Map<String, String>>();
			    	//TODO 取值并写入 集合
			    	jsonObject = jsonObject.getJSONObject(_Command);
			    	it = jsonObject.keys();
		            while(it.hasNext()){//遍历JSONObject  
		            	String strKey = (String) it.next().toString();
		            	if(strKey.equals("PageSize"))
		            	{
		            		_PageSize = jsonObject.getString("PageSize");
		            	}
		            	else if(strKey.equals("PageIndex"))
		            	{
		            		_PageIndex = jsonObject.getString("PageIndex");
		            	}
		            	else if(strKey.equals("RecordCount"))
		            	{
		            		_RecordCount = jsonObject.getString("RecordCount");
		            	}
		            }
			    	
			    	JSONArray jsonArray = jsonObject.getJSONArray("list");
			    	for(int i=0;i<jsonArray.length();i++)
			    	{
			    		Map<String, String> map = new HashMap<String, String>();
		            	it = ((JSONObject)jsonArray.get(i)).keys();
			            while(it.hasNext()){//遍历JSONObject  
			            	String key = it.next().toString();
			            	map.put(key, ((JSONObject)jsonArray.get(i)).getString(key));
			            }
			            Lists.add(map);
			    	}
			    	_IsSuccess=true;
			    }
			    else if(_Command.indexOf("insert")>=0||_Command.indexOf("delete")>=0||_Command.indexOf("update")>=0||_Command.indexOf("plugin")>=0)
			    {
			    	Lists = null;
			    	//TODO 判断服务器执行状态
			    	_IsSuccess=jsonObject.getJSONObject(_Command).getString("Status").equals("true");
			    }

				Log.i(ConstantsSetting.EfficiencyTestTag, "api response data read completed.");
				
			} catch (JSONException ex) {  
			    // 异常处理代码  
				Lists = null;
			}
		}
		
	}

	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
}
