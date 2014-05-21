/**
 * 
 */
package coolbuy360.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.widget.BaseAdapter;

/**
 * @author yangxc
 * 临时数据池
 */
public class TempDataPool {
	
	private static Map<String, BaseAdapter> AdapterPool = new HashMap<String, BaseAdapter>();
	private static Map<String, Object> SelectorResultPool = new HashMap<String, Object>();
	
	/**
	 * 托管一个适配器
	 * @param adapter
	 * @return 返回适配器对就的唯一Key
	 */
	public static String putAdapter(BaseAdapter adapter)
	{
		String key = UUID.randomUUID().toString();
		AdapterPool.put(key, adapter);
		return key;
	}
	
	/**
	 * 托管一个选择器结果项
	 * @param value
	 * @return 返回选择器结果项对应的唯一Key
	 */
	public static String putSelectorResult(Object value)
	{
		String key = UUID.randomUUID().toString();
		SelectorResultPool.put(key, value);
		return key;
	}
	
	/**
	 * 从数据池获取一个适配器
	 * @param key
	 * @return
	 */
	public static BaseAdapter getAdapter(String key)
	{
		BaseAdapter result = AdapterPool.get(key);
		return result;
	}
	
	/**
	 * 从数据池获取一个选择器结果项
	 * @param key
	 * @return
	 */
	public static Object getSelectorResult(String key)
	{
		Object result = SelectorResultPool.get(key);
		return result;
	}
	
	/**
	 * 使用完成后销毁相应的适配器
	 * @param key
	 */
	public static void destroyAdapter(String key)
	{
		AdapterPool.remove(key);
	}
	
	/**
	 * 使用完成后销毁相应的选择器结果项
	 * @param key
	 */
	public static void destroySelectorResult(String key)
	{
		SelectorResultPool.remove(key);
	}
}
