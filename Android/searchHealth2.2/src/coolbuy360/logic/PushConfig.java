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
	//1���û�tags���û�BaiduUserID���Ƿ����push��Ϣ
	protected static final String PREFS_FILE = "push_config"; 
	//tagsֵ
    public static final String Tags = "Tags"; 
	//�ٶ�userid
    public static final String BaiduUserID = "BaiduUserID"; 
    //�Ƿ����������Ϣ
    public static final String ISReceive = "ISReceive";
    
    public static final String ISUpload="ISUpload";
    
    //�ļ��Ƿ񱻴���
    public static final String Config_IsCreated = "Config_IsCreated";

    
    protected static String _isCreated = null; 
    private Context _context;    
    protected static Map<String, String> _configs = new HashMap<String, String>();
    protected static Map<String, String> _defaultValues = new HashMap<String, String>()
    {
    	{
            put(Config_IsCreated, "true");
    		put(Tags, "");//Ĭ�ϱ�ǩΪ��
    		put(BaiduUserID, "");//Ĭ��useidΪ��
            put(ISReceive, "1");  //Ĭ�Ͻ���push֪ͨ      
            put(ISUpload, "false");
    	}
    };
    
    /**
     * ʵ����PushConfig
     * @param context ����getbaseContext()
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
                    		//��ֵд�������ļ�
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
     * ����App���õ�ֵ
     * @param context ��getbaseContext()
     * @param values ���õļ�ֵ����
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
	    	synchronized(PushConfig.class)
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
	    	synchronized(PushConfig.class)
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
    	synchronized(PushConfig.class)
    	{ 
    		final SharedPreferences prefs = _context.getSharedPreferences(PREFS_FILE, 0); 
    		//�������ļ���ȡ���õ�ֵ
    		Map<String, String> allconfigs = (Map<String, String>) prefs.getAll();
    		_configs.putAll(allconfigs);
    	}
    	return _configs;
    }	
	
/**
 * tag����ز�����һ����ݵ�¼״̬�����в�ͬ�Ĳ�����
 * @param context
 */
    public static void tagHandle(Context context ) {  	
    	
       List<String> tags=new ArrayList<String>();	
		//tagֵʹ��drugStoreName
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
        	   //���share�ļ�tags��Ϊ�գ����治�������ֵ�����û���¼�����б�Ҫ������ɾ����ǰ��tag���������tag
        	  int a= tagedList.indexOf(tagString);
        	   
        	   
	           	if(tagedList.indexOf(tagString)==-1)  // ����Ƿ������ǰ��¼tag/���������
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
		   //���share�ļ�tags��Ϊ�գ������ִ������ֵ
		   //���share�ļ�tags��Ϊ�գ����治�������ֵ�����û���¼�����б�Ҫ������ɾ����ǰ��tag���������tag
			if(tagedString.indexOf(tagString)==-1)//����Ƿ������ǰ��¼tag/���������
			{	
				android.util.Log.i("chenlinwei","tagString"+tagString);
				tags.add(tagString);
				PushManager.setTags(context,tags);
				
			}	
			else
			{
				//��ִ����ز���	
				  android.util.Log.i("chenlinwei", "tagString  û��ִ����ز���"+tagString);
				
			}*/
		   
		
		}
		//���û�е�¼�����״ε�½��ʱ�����ó�ʼtag
		else {
			tags.add("0");
			if(tagedString==null||tagedString.equals(""))//��ʼtag
			{			
				PushManager.setTags(context,tags);				
			}		
		}		
	}
    
    
    
    
    
}
