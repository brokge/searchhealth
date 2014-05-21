package coolbuy360.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;

import com.baidu.android.pushservice.PushManager;

import android.R.string;
import android.content.Context;
import android.content.SharedPreferences;

public class PushConfig {
	//1，用户tags，用户BaiduUserID，是否接收push信息
	protected static final String PREFS_FILE = "push_config"; 
	//tags值
    public static final String Tags = "Tags"; 
	//百度userid
    public static final String BaiduUserID = "BaiduUserID"; 
    //是否接受推送信息
    public static final String ISReceive = "ISReceive";
    
    public static final String ISUpload="ISUpload";
    
    //文件是否被创建
    public static final String Config_IsCreated = "Config_IsCreated";

    
    protected static String _isCreated = null; 
    private Context _context;    
    protected static Map<String, String> _configs = new HashMap<String, String>();
    protected static Map<String, String> _defaultValues = new HashMap<String, String>()
    {
    	{
            put(Config_IsCreated, "true");
    		put(Tags, "");//默认标签为空
    		put(BaiduUserID, "");//默认useid为空
            put(ISReceive, "1");  //默认接收push通知      
            put(ISUpload, "false");
    	}
    };
    
    /**
     * 实例化PushConfig
     * @param context 传入getbaseContext()
     */
    public PushConfig(Context context)
    { 
    	_context = context;
        if(_isCreated == null)
        { 
            synchronized (PushConfig.class) 
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
    
    public String getTags(Context context) 
    { 
    	String value = getValue(context, Tags);
    	return value;
    }    
   
    public String getBaiduUserID(Context context) 
    { 
    	String value = getValue(context, BaiduUserID);
    	return value;
    }
    
    public int getISReceive(Context context) 
    { 
    	String value = getValue(context, ISReceive);
    	return Integer.parseInt(value);
    }
    
    public int getISReceive() 
    { 
    	String value = getValue(ISReceive);
    	return Integer.parseInt(value);
    }
    
    public static void setValue(Context context, String key, String value) 
    {
    	synchronized (PushConfig.class) 
        {
    		final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0); 
    		_configs.put(key, value);
    		prefs.edit().putString(key, value).commit();
        }
    }
        
    public void setValue(String key, String value) 
    {
    	synchronized (PushConfig.class) 
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
    	synchronized (PushConfig.class) 
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
    	synchronized (PushConfig.class) 
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
	    	synchronized(PushConfig.class)
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
	    	synchronized(PushConfig.class)
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
    	synchronized(PushConfig.class)
    	{ 
    		final SharedPreferences prefs = _context.getSharedPreferences(PREFS_FILE, 0); 
    		//从配置文件获取配置的值
    		Map<String, String> allconfigs = (Map<String, String>) prefs.getAll();
    		_configs.putAll(allconfigs);
    	}
    	return _configs;
    }	
	
/**
 * tag的相关操作，一般根据登录状态来进行不同的操作；
 * @param context
 */
    public static void tagHandle(Context context ) {  	
    	
       List<String> tags=new ArrayList<String>();	
		//tag值使用drugStoreName
	   String tagString=User.getValue(context, "DrugStoreID");
	   String tagedString=PushConfig.getValue(context, "Tags");
	
	   android.util.Log.i("chenlinwei", User.IsLogged+"--"+tagString+"--"+tagedString);
	   if(User.IsLogged)
		{  
		 if (tagString!=null&&!tagString.equals("")) {
		    List<String> tagedList=new ArrayList<String>();
	        String[] tageds=tagedString.split("-");
	        for(String s : tageds) {    
	        	tagedList.add(s);    
	        }  
           for(int i=0;i<tagedList.size();i++)
           {
        	   //如果share文件tags不为空，里面不存在这个值：新用户登录，如有必要可以先删除以前的tag，再添加新tag
        	  int a= tagedList.indexOf(tagString);
        	   
        	   
	           	if(tagedList.indexOf(tagString)==-1)  // 检查是否包含当前登录tag/如果不包含
	           	{
	           		android.util.Log.i("chenlinwei","tagString"+tagString);
					tags.add(tagString);					
	           	}           	
            }	
           if(tags.size()>0)
           {
            PushManager.setTags(context,tags);
           }
		  }
		   
		  /* if (tagString!=null) {	   
		   android.util.Log.i("chenlinwei", User.IsLogged+"--"+tagString+"--"+tagedString);
		   //如果share文件tags不为空，里面又存在这个值
		   //如果share文件tags不为空，里面不存在这个值：新用户登录，如有必要可以先删除以前的tag，再添加新tag
			if(tagedString.indexOf(tagString)==-1)//检查是否包含当前登录tag/如果不包含
			{	
				android.util.Log.i("chenlinwei","tagString"+tagString);
				tags.add(tagString);
				PushManager.setTags(context,tags);
				
			}	
			else
			{
				//不执行相关操作	
				  android.util.Log.i("chenlinwei", "tagString  没有执行相关操作"+tagString);
				
			}*/
		   
		
		}
		//如果没有登录，在首次登陆的时候，设置初始tag
		else {
			tags.add("0");
			if(tagedString==null||tagedString.equals(""))//初始tag
			{			
				PushManager.setTags(context,tags);				
			}		
		}		
	}
    
    
    
    
    
}
