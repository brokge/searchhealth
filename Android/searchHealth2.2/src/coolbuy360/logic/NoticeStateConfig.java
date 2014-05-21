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
 * 通知状态配置管理
 * @author yangxc
 *
 */
public class NoticeStateConfig {
	
	protected static final String PREFS_FILE = "notice_state_config"; 
	//
    public static final String Message_LastVisite = "Message_LastVisite"; 
	//
    public static final String Dissertation_LastVisite = "Dissertation_LastVisite"; 
	//
    public static final String Message_LastReceive = "Message_LastReceive"; 
    public static final String News_LastVisite = "News_LastVisite"; 
    
	//
    public static final String Dissertation_LastReceive = "Dissertation_LastReceive"; 
    public static final String News_LastReceive = "News_LastReceive"; 
	//
    public static final String Message_HasNew = "Message_HasNew"; 
	//
    public static final String Dissertation_HasNew = "Dissertation_HasNew"; 
   // public static final String Function_HasNew = "Dissertation_HasNew"; 
    //
    public static final String Message_IsVisited = "Message_IsVisited";
    //
    public static final String Dissertation_IsVisited = "Dissertation_IsVisited";
    //
    public static final String HealthReport_IsVisited = "HealthReport_IsVisited";
	//
    public static final String Column0_HasNew = "Column0_HasNew"; 
	//
    public static final String Column1_HasNew = "Column1_HasNew"; 
	//
    public static final String Column2_HasNew = "Column2_HasNew"; 
	//
    public static final String Column3_HasNew = "Column3_HasNew";     
    //
    public static final String News_IsVisited = "News_IsVisited";
    
    
    
    
    //文件是否被创建
    public static final String Config_IsCreated = "Config_IsCreated";
    
    protected static String _isCreated = null; 
    private Context _context;
    
    protected static Map<String, String> _configs = new HashMap<String, String>();
    protected static Map<String, String> _defaultValues = new HashMap<String, String>()
    {
    	{
            put(Config_IsCreated, "true");
    		put(Message_LastVisite, "2013-12-09 00:00:00");
    		put(Dissertation_LastVisite, "2013-12-09 00:00:00");
    		put(News_LastVisite, "2013-12-09 00:00:00");
    		put(Message_LastReceive, "2013-12-09 00:00:00");    		
    		put(Dissertation_LastReceive, "2013-12-09 00:00:00");
    		put(News_LastReceive, "2013-12-09 00:00:00");    		
            put(Message_HasNew, "0");
            put(Dissertation_HasNew, "0");
            put(Message_IsVisited, "1");
            put(Dissertation_IsVisited, "1");
            put(News_IsVisited, "0");
            put(HealthReport_IsVisited, "0");
            put(Column0_HasNew, "0");
            put(Column1_HasNew, "0");
            put(Column2_HasNew, "0");
            put(Column3_HasNew, "0");
    	}
    };
    
    /**
     * 实例化AppConfig
     * @param context 传入getbaseContext()
     */
    public NoticeStateConfig(Context context)
    { 
    	_context = context;
        if(_isCreated == null)
        { 
            synchronized (NoticeStateConfig.class) 
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
     * 获取新闻所有栏目状态是否有新的新闻
     * @return
     *       任意一个有新的新闻，则返回为1，否则返回0
     */
    public static String getNewsAllState(Context context)
    {
    	
    	String column0=getValue(context,Column0_HasNew);
    	String column1=getValue(context,Column1_HasNew);
       	String column2=getValue(context,Column2_HasNew);
    	String column3=getValue(context,Column3_HasNew);    	
    	if(column0.equals("1")||column1.equals("1")||column2.equals("1")||column3.equals("1"))
    	{
    		return "1";    		
    	}
    	else
    	{    		
    		return "0";
    	}     	
    }
    /**
     * 获取tab栏中“更多”是否需要显示红点儿：如果里面有任何一个item有新的状态，则显示红点儿
     * @return
     *       如果专题或新闻任意一个有新的状态（即：新消息没有被访问过）
     *       则返回为1，否则返回0
     */
    public static String getMoreAllState(Context context)
    {    	
       String dissertation_HasNew=getValue(context,Dissertation_HasNew);    	
       if(getNewsAllState(context).equals("1")||dissertation_HasNew.equals("1"))    	
    	{
    		return "1";    		
    	}
    	else
    	{    		
    		return "0";
    	}     	
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
