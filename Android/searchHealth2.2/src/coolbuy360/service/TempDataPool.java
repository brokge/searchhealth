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
 * ��ʱ���ݳ�
 */
public class TempDataPool {
	
	private static Map<String, BaseAdapter> AdapterPool = new HashMap<String, BaseAdapter>();
	private static Map<String, Object> SelectorResultPool = new HashMap<String, Object>();
	
	/**
	 * �й�һ��������
	 * @param adapter
	 * @return �����������Ծ͵�ΨһKey
	 */
	public static String putAdapter(BaseAdapter adapter)
	{
		String key = UUID.randomUUID().toString();
		AdapterPool.put(key, adapter);
		return key;
	}
	
	/**
	 * �й�һ��ѡ���������
	 * @param value
	 * @return ����ѡ����������Ӧ��ΨһKey
	 */
	public static String putSelectorResult(Object value)
	{
		String key = UUID.randomUUID().toString();
		SelectorResultPool.put(key, value);
		return key;
	}
	
	/**
	 * �����ݳػ�ȡһ��������
	 * @param key
	 * @return
	 */
	public static BaseAdapter getAdapter(String key)
	{
		BaseAdapter result = AdapterPool.get(key);
		return result;
	}
	
	/**
	 * �����ݳػ�ȡһ��ѡ���������
	 * @param key
	 * @return
	 */
	public static Object getSelectorResult(String key)
	{
		Object result = SelectorResultPool.get(key);
		return result;
	}
	
	/**
	 * ʹ����ɺ�������Ӧ��������
	 * @param key
	 */
	public static void destroyAdapter(String key)
	{
		AdapterPool.remove(key);
	}
	
	/**
	 * ʹ����ɺ�������Ӧ��ѡ���������
	 * @param key
	 */
	public static void destroySelectorResult(String key)
	{
		SelectorResultPool.remove(key);
	}
}
