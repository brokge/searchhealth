/**
 * 
 */
package coolbuy360.logic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Ӧ������
 * @author yangxc
 */
public class AppConfig 
{
	protected static final String PREFS_FILE = "app_config"; 
	//2G/3G�������Ƿ��Զ���������ͼƬ
    public static final String Is_2G3G_AutoLoadImage = "Is_2G3G_AutoLoadImage"; 
	//ͼƬƷ�ʣ���hight����ʾ��Ʒ�ʡ���low����ʾ��Ʒ��
    public static final String Img_Quality = "Img_Quality"; 
    //ҩ���ѯ��Χ��Ĭ��Ϊ2���-1��ʾȫ��
    public static final String Store_SearchRange = "Store_SearchRange";
    //ҩ���ѯ�������Ƿ�ҽ��ָ��ҩ�꣬-1��ʾ����
    public static final String Store_IsHC = "Store_IsHC";
    //�ļ��Ƿ񱻴���
    public static final String Config_IsCreated = "Config_IsCreated";
    //�Ƿ��Ѿ��ϴ����ڵ�
    public static final String IsUploaded_LocationAddress = "IsUploaded_LocationAddress";
    
    protected static String _isCreated = null; 
    private Context _context;
    
    protected static Map<String, String> _configs = new HashMap<String, String>();
    protected static Map<String, String> _defaultValues = new HashMap<String, String>()
    {
    	{
            put(Config_IsCreated, "true");
    		put(Is_2G3G_AutoLoadImage, "true");
    		put(Img_Quality, "low");
            put(Store_SearchRange, "20");
            put(Store_IsHC, "-1");
            put(IsUploaded_LocationAddress, "false");
    	}
    };
    
    /**
     * ʵ����AppConfig
     * @param context ����getbaseContext()
     */
    public AppConfig(Context context)
    { 
    	_context = context;
        if(_isCreated == null)
        { 
            synchronized (AppConfig.class) 
            { 
                if(_isCreated == null) 
                { 
                    final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0); 
                    _isCreated = prefs.getString(Config_IsCreated, null);                     
                    if(_isCreated == null) 
                    {
                    	Set keysSet = _defaultValues.keySet();
                    	Iterator iterator = keysSet.iterator();
                    	while(iterator.hasNext()) 
                    	{
                    		String key = iterator.next().toString();//key
                    		String value = _defaultValues.get(key);//value
                    		_configs.put(key, value);
                    		//��ֵд�������ļ�
                    		prefs.edit().putString(key, value).commit();
                    	}
                    	_isCreated = "true";
                    }
                } 
            } 
        }
    }
    
    public boolean getIs2G3GAutoLoadImage(Context context) 
    { 
    	String value = getValue(context, Is_2G3G_AutoLoadImage);
    	return Boolean.getBoolean(value);
    }
    
    public boolean getIs2G3GAutoLoadImage() 
    { 
    	String value = getValue(Is_2G3G_AutoLoadImage);
    	return Boolean.getBoolean(value);
    }
    
    public int getStore_SearchRange(Context context) 
    { 
    	String value = getValue(context, Store_SearchRange);
    	return Integer.parseInt(value);
    }
    
    public int getStore_SearchRange() 
    { 
    	String value = getValue(Store_SearchRange);
    	return Integer.parseInt(value);
    }
    
    public int getStore_IsHC(Context context) 
    { 
    	String value = getValue(context, Store_IsHC);
    	return Integer.parseInt(value);
    }
    
    public int getStore_IsHC() 
    { 
    	String value = getValue(Store_IsHC);
    	return Integer.parseInt(value);
    }
    
    public static void setValue(Context context, String key, String value) 
    {
    	synchronized (AppConfig.class) 
        {
    		final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0); 
    		_configs.put(key, value);
    		prefs.edit().putString(key, value).commit();
        }
    }
        
    public void setValue(String key, String value) 
    {
    	synchronized (AppConfig.class) 
        {
    		final SharedPreferences prefs = _context.getSharedPreferences(PREFS_FILE, 0); 
    		_configs.put(key, value);
    		prefs.edit().putString(key, value).commit();
        }
    }    
    
    /**
     * ����App���õ�ֵ
     * @param context ��getbaseContext()
     * @param values ���õļ�ֵ����
     */
    public static void setValues(Context context, Map<String, String> values)
    {
    	synchronized (AppConfig.class) 
        {
    		final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0); 
    		Set keysSet = values.keySet();
    		Iterator iterator = keysSet.iterator();
    		while(iterator.hasNext()) 
    		{
    			String key = iterator.next().toString();//key
    			String value = values.get(key);//value
    			_configs.put(key, value);
    			//��ֵд�������ļ�
    			prefs.edit().putString(key, value).commit();
    		}
        }
    }
    
    /**
     * ����App���õ�ֵ
     * @param values ���õļ�ֵ����
     */
    public void setValues(Map<String, String> values)
    {
    	synchronized (AppConfig.class) 
        {
    		final SharedPreferences prefs = _context.getSharedPreferences(PREFS_FILE, 0); 
    		Set keysSet = values.keySet();
    		Iterator iterator = keysSet.iterator();
    		while(iterator.hasNext()) 
    		{
    			String key = iterator.next().toString();//key
    			String value = values.get(key);//value
    			_configs.put(key, value);
    			//��ֵд�������ļ�
    			prefs.edit().putString(key, value).commit();
    		}
        }
    }
    
    /**
     * ��ȡApp���õ�ֵ
     * @param context ��getbaseContext()
     * @param key
     * @return
     */
    public static String getValue(Context context, String key) 
    { 
    	String value = _configs.get(key);
    	if(value==null)
    	{
	    	synchronized(AppConfig.class)
	        { 
	    		final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0); 
	    		//�������ļ���ȡ���õ�ֵ
	    		value = prefs.getString(key, _defaultValues.get(key));
	    		_configs.put(key, value);
	        }
    	}
    	return value;
    }
    
    /**
     * ��ȡApp���õ�ֵ
     * @param key
     * @return
     */
    public String getValue(String key) 
    { 
    	String value = _configs.get(key);
    	if(value==null)
    	{
	    	synchronized(AppConfig.class)
	        { 
	    		final SharedPreferences prefs = _context.getSharedPreferences(PREFS_FILE, 0); 
	    		//�������ļ���ȡ���õ�ֵ
	    		value = prefs.getString(key, _defaultValues.get(key));
	    		_configs.put(key, value);
	        }
    	}
    	return value;
    }

    /**
     * ��ȡ��������
     * @return
     */
    public Map<String, String> getAll() 
    { 
    	synchronized(AppConfig.class)
    	{ 
    		final SharedPreferences prefs = _context.getSharedPreferences(PREFS_FILE, 0); 
    		//�������ļ���ȡ���õ�ֵ
    		Map<String, String> allconfigs = (Map<String, String>) prefs.getAll();
    		_configs.putAll(allconfigs);
    	}
    	return _configs;
    }
}
