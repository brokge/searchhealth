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
 * 应用设置
 * @author yangxc
 */
public class AppConfig 
{
	protected static final String PREFS_FILE = "app_config"; 
	//2G/3G环境下是否自动加载网络图片
    public static final String Is_2G3G_AutoLoadImage = "Is_2G3G_AutoLoadImage"; 
	//图片品质，“hight”表示高品质、“low”表示低品质
    public static final String Img_Quality = "Img_Quality"; 
    //药店查询范围，默认为2公里，-1表示全城
    public static final String Store_SearchRange = "Store_SearchRange";
    //药店查询条件，是否医保指定药店，-1表示不限
    public static final String Store_IsHC = "Store_IsHC";
    //文件是否被创建
    public static final String Config_IsCreated = "Config_IsCreated";
    //是否已经上传所在地
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
     * 实例化AppConfig
     * @param context 传入getbaseContext()
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
                    		//将值写入配置文件
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
     * 保存App设置的值
     * @param context 传getbaseContext()
     * @param values 设置的键值集合
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
    			//将值写入配置文件
    			prefs.edit().putString(key, value).commit();
    		}
        }
    }
    
    /**
     * 保存App设置的值
     * @param values 设置的键值集合
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
    			//将值写入配置文件
    			prefs.edit().putString(key, value).commit();
    		}
        }
    }
    
    /**
     * 获取App设置的值
     * @param context 传getbaseContext()
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
	    		//从配置文件获取配置的值
	    		value = prefs.getString(key, _defaultValues.get(key));
	    		_configs.put(key, value);
	        }
    	}
    	return value;
    }
    
    /**
     * 获取App设置的值
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
	    		//从配置文件获取配置的值
	    		value = prefs.getString(key, _defaultValues.get(key));
	    		_configs.put(key, value);
	        }
    	}
    	return value;
    }

    /**
     * 获取所有设置
     * @return
     */
    public Map<String, String> getAll() 
    { 
    	synchronized(AppConfig.class)
    	{ 
    		final SharedPreferences prefs = _context.getSharedPreferences(PREFS_FILE, 0); 
    		//从配置文件获取配置的值
    		Map<String, String> allconfigs = (Map<String, String>) prefs.getAll();
    		_configs.putAll(allconfigs);
    	}
    	return _configs;
    }
}
