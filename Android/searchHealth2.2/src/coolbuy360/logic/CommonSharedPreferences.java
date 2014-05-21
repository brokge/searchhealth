package coolbuy360.logic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

public class CommonSharedPreferences {
	private Context _context;
	protected  String PREFS_FILE = "";	
	
	protected static  String Profile_IsCreated = "Profile_IsCreated";
	protected static String _isCreated = null;
	private Map<String, String> _defaultValues=new HashMap<String, String>();	
	public  static Map<String, String> _profiles = new HashMap<String, String>();	
    public Object valueObject=null;	
	public  CommonSharedPreferences(Context context,String fileString,Map<String, String> valueMap,Object valueObject) {		
		this._context = context;
	    this.PREFS_FILE=fileString;
		this._defaultValues=valueMap;	
		this.valueObject=valueObject;
	}
	/**
	 * 	
	 * @param values
	 *            ���õļ�ֵ����
	 */
	private void setValues(Map<String, String> values) {		
		synchronized (valueObject.getClass()) {
			final SharedPreferences prefs = _context.getSharedPreferences(
					PREFS_FILE, 0);
			Set keysSet = values.keySet();
			Iterator iterator = keysSet.iterator();
			while (iterator.hasNext()) {
				String key = iterator.next().toString();// key
				String value = values.get(key);// value
				_profiles.put(key, value);
				// ��ֵд�������ļ�
				prefs.edit().putString(key, value).commit();
			}
		}
	}
	
	
	/**
	 * ��ȡUser���õ�ֵ
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public String getValue( String key) {
		String value = _profiles.get(key);
		if (value == null) {
			synchronized (valueObject.getClass()) {
				final SharedPreferences prefs = _context.getSharedPreferences(
						PREFS_FILE, 0);
				// �������ļ���ȡ���õ�ֵ
				value = prefs.getString(key, _defaultValues.get(key));
				_profiles.put(key, value);
			}
		}
		return value;
	}
	
	

}
