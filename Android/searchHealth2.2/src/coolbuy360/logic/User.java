/**
 * 
 */
package coolbuy360.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import coolbuy360.service.CommandResult;
import coolbuy360.service.MD5;
import android.R.bool;
import android.R.integer;
import android.R.string;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.CursorJoiner.Result;

/**
 * �û���Ϣ
 * @author yangxc 
 */
public class User {

	protected static final String PREFS_FILE = "u_0";
	protected static final String Device_AppUserID = "Device_AppUserID";
	protected static final String Device_IsUse = "Device_IsUse";
	protected static final String Member_AppUserID = "Member_AppUserID";
	protected static final String User_MemberID = "User_MemberID";
	protected static final String Member_LoginID = "Member_LoginID";
	protected static final String Member_Password = "Member_Password";
	protected static final String User_AutoLogin = "User_AutoLogin";
	protected static final String User_AutoLoginAble = "User_AutoLoginAble";
	protected static final String Member_Mobile = "Member_Mobile";
	protected static final String Profile_IsCreated = "Profile_IsCreated";

	/**
	 * ���Ϊ�Ƿ��¼
	 */
	public static Boolean IsLogged = false;
	private static String _devAppUserID = null;
	public static String _loginID = null;
	private Context _context;

	protected static String _isCreated = null;

	public static Map<String, String> _profiles = new HashMap<String, String>();
	private final static Map<String, String> _defaultValues = new HashMap<String, String>() {
		{
			put(Profile_IsCreated, "true");
			put(User_AutoLogin, "1");
			put(User_AutoLoginAble, "0");
			put(Device_IsUse, "1");
		}
	};

	public User(Context context) {
		this._context = context;
		if (_isCreated == null) {
			synchronized (User.class) {
				if (_isCreated == null) {
					// Ĭ���û��ļ�
					final SharedPreferences prefs = context
							.getSharedPreferences(PREFS_FILE, 0);
					_isCreated = prefs.getString(Profile_IsCreated, null);
					if (_isCreated == null) {
						// ȥ�����ȡUserID
						// GetUserID
						_isCreated = "true";
					}
				}
			}
		}
	}

	/**
	 * ��ʼ��User��Ϣ����ȡ���������ļ�
	 * 
	 * @param context
	 */
	public static void initialization(Context context) {
		if (_isCreated == null) {
			synchronized (User.class) {
				if (_isCreated == null) {
					// Ĭ���û��ļ�
					final SharedPreferences prefs = context
							.getSharedPreferences(PREFS_FILE, 0);
					_isCreated = prefs.getString(Profile_IsCreated, null);
					if (_isCreated == null) {
						Set keysSet = _defaultValues.keySet();
						Iterator iterator = keysSet.iterator();
						while (iterator.hasNext()) {
							String key = iterator.next().toString();// key
							String value = _defaultValues.get(key);// value
							_profiles.put(key, value);
							// ��ֵд�������ļ�
							prefs.edit().putString(key, value).commit();
						}
						_isCreated = "true";
					} else {
						getAll(context);
						_isCreated = "true";
					}
				}
			}
		}
	}
	
	/**
	 * ��Աע�ᣬ����CommandResult��
	 * @param mobile �ֻ�����
	 * @param email ע������
	 * @param pwd ע��ĵ�¼���루δ���ܵģ�
	 * @return
	 */
	public CommandResult register(String mobile, String email, String pwd)
	{
		CommandResult result = new CommandResult(false, "ע��ʧ�ܣ�δ֪����");
		pwd = MD5.getMD5(pwd);
		//����ע�����
		//��ע��ɹ�������[result],[message],[count],MemberID,UserName,MemberName,Email,Mobile,MemAppUserID;
		//��ע��ʧ�ܣ�����[result],[message]��ֵ��
		String strQL = "plugin DrugCenter.Logic.V1 Member Register($mobile$,$email$,$pwd$)";
		Map<String,String> postValue = new HashMap<String, String>();
		postValue.put("mobile", mobile);
		postValue.put("email", email);
		postValue.put("pwd", pwd);
		List<Map<String,String>> resultList = ConstantsSetting.qLGetListByProcedure(0, 0, strQL, postValue);
		if (resultList != null && resultList.size() > 0) {
			Map<String, String> values = resultList.get(0);
			String qlResult = values.get("result");
			if (qlResult.equals("true")) {
				result.setResult(true);
				result.setMessage(values.get("message"));
				setValues(values);
				//_profiles.putAll(values);
				User.IsLogged = true;
				setValue(Member_LoginID, mobile);
				setValue(User_AutoLoginAble, "1");
				setValue(Member_Password, pwd);
				return result;
			} else {
				result.setMessage(values.get("message"));
				return result;
			}
		} else {
			return result;
		}
	}

	/**
	 * ��Ա��¼
	 * 
	 * @param loginID
	 *            ��¼ID
	 * @param pwd
	 *            ����
	 * @param originalPwd
	 *            ����Ƿ�ԭʼ���룬1��ʾδ���ܵ�ԭʼ���룬0��ʾ���ܹ������루�������ļ���ȡ�ģ�
	 * @param autoLogin
	 *            ����Ƿ��Զ���¼
	 * @return ���ص�¼�����trueΪ��¼�ɹ�
	 */
	public Boolean login(String loginID, String pwd, Boolean originalPwd,
			Boolean autoLogin) {
		setValue(User_AutoLogin, autoLogin ? "1" : "0");
		String strQL = ConstantsSetting.QLUserLogin;
		Map<String, String> postValue = new HashMap<String, String>();
		postValue.put("id", loginID);
		postValue.put("pwd", originalPwd ? MD5.getMD5(pwd) : pwd);
		postValue.put("deviceAppUID", User.getDevAppUserID(_context));
		List<Map<String, String>> result = ConstantsSetting.qLGetList(0, 0,
				strQL, postValue);
		if (result != null && result.size() > 0) {
			Map<String, String> values = result.get(0);
			int count = Integer.parseInt(values.get("count").toString());
			if (count > 0) {
				setValues(values);
				//_profiles.putAll(values);
				User.IsLogged = true;
				setValue(Member_LoginID, loginID);
				setValue(User_AutoLoginAble, "1");
				if (autoLogin)
					setValue(Member_Password, originalPwd ? MD5.getMD5(pwd)
							: pwd);
				return User.IsLogged;
			}
		}
		setValue(User_AutoLoginAble, "0");
		User.IsLogged = false;
		return User.IsLogged;
	}

	/**
	 * �Զ���¼
	 * 
	 * @return
	 */
	public Boolean login() {
		String loginid = getLoginID(_context);
		String pwd = getLoginPassword(_context);
		if (loginid == null || pwd == null || loginid.equals("")
				|| pwd.equals("")) {
			return false;
		} else {
			return login(loginid, pwd, false, true);
		}
	}

	/**
	 * ��Աע����¼
	 * 
	 * @return
	 */
	public Boolean logout() {
		User.IsLogged = false;
		setValue(User_AutoLoginAble, "0");
		setValue("Sex", "9");
		return User.IsLogged;
	}

	/**
	 * �޸�����
	 * 
	 * @param oldPwd
	 *            ����ǰ��������
	 * @param newPwd
	 *            ����ǰ��������
	 * @return CommandResult���ز��������getResult()�õ�trueΪ�ɹ���getMessage()�õ�˵����Ϣ��
	 */
	public CommandResult changePassword(String oldPwd, String newPwd) {
		String strQL = ConstantsSetting.QLChangePassword;
		Map<String, String> postValue = new HashMap<String, String>();
		postValue.put("id", User.getMemberID());
		postValue.put("oldPwd", MD5.getMD5(oldPwd));
		postValue.put("newPwd", MD5.getMD5(newPwd));
		List<Map<String, String>> result = ConstantsSetting.qLGetList(0, 0,
				strQL, postValue);
		if (result != null && result.size() > 0) {
			Map<String, String> values = result.get(0);
			CommandResult commandResult = new CommandResult(
					values.get("result"), values.get("message"));
			if (commandResult.getResult()) {
				setValue(User_AutoLoginAble, "0");
			}
			return commandResult;
		}
		CommandResult commandResult = new CommandResult("false", "δ֪����");
		return commandResult;
	}
	
	/**
	 * ��Ա�������룬����CommandResult��
	 * @param mobile �ֻ�����
	 * @param email ע������
	 * @param newpwd �����루δ���ܵģ�
	 * @return
	 */
	public CommandResult resetPwd(String mobile, String email, String newpwd)
	{
		CommandResult result = new CommandResult(false, "��������ʧ�ܣ�δ֪����");
		newpwd = MD5.getMD5(newpwd);
		String strQL = "plugin DrugCenter.Logic.V1 Member ResetPwd($mobile$,$email$,$newpwd$)";
		Map<String,String> postValue = new HashMap<String, String>();
		postValue.put("mobile", mobile);
		postValue.put("email", email);
		postValue.put("newpwd", newpwd);
		List<Map<String,String>> resetResultList = ConstantsSetting.qLGetListByProcedure(0, 0, strQL, postValue);
		if (resetResultList != null && resetResultList.size() > 0) {
			Map<String, String> values = resetResultList.get(0);
			String qlResult = values.get("result");
			if (qlResult.equals("true")) {
				result.setResult(true);
				result.setMessage(values.get("message"));
				return result;
			} else {
				result.setMessage(values.get("message"));
				return result;
			}
		} else {
			return result;
		}
	}
	
	/**
	 * ��Աǩ��������CommandResult��
	 * @return
	 */
	public CommandResult checkIn()
	{
		CommandResult result = new CommandResult(false, "ǩ��ʧ�ܣ�δ֪����");
		//����ǩ������
		//��ǩ���ɹ�������[result],[message],[addscore],[score],[checkintimes],[nextscore],[today];today��ʾ�����Ƿ�ǩ����״̬��1Ϊ��ǩ����0Ϊδǩ��
		//��ǩ��ʧ�ܣ�����[result],[message]��ֵ��
		String strQL = "plugin DrugCenter.Logic.V1 Member CheckIn($memberid$)";
		Map<String,String> postValue = new HashMap<String, String>();
		postValue.put("memberid", getMemberID());
		List<Map<String,String>> resultList = ConstantsSetting.qLGetListByProcedure(0, 0, strQL, postValue);
		if (resultList != null && resultList.size() > 0) {
			Map<String, String> values = resultList.get(0);
			String qlResult = values.get("result");
			if (qlResult.equals("true")) {
				result.setResult(true);
				result.setMessage(values.get("message"));
				result.setOriginalResult(values);
				setValue("Score", values.get("score"));
				//setValues(values);
				return result;
			} else {
				result.setMessage(values.get("message"));
				return result;
			}
		} else {
			return result;
		}
	}
	
	/**
	 * ��ȡǩ��״̬������CommandResult��
	 * @return
	 */
	public CommandResult getCheckInState()
	{
		CommandResult result = new CommandResult(false, "����ǩ��״̬ʧ�ܣ�δ֪����");
		//����ǩ������
		//����ȡ�ɹ�������[score],[checkintimes],[nextscore],[today];
		//����ȡʧ�ܣ�����[message]��ֵ��
		String strQL = "plugin DrugCenter.Logic.V1 Member GetCheckInState($memberid$)";
		Map<String,String> postValue = new HashMap<String, String>();
		postValue.put("memberid", getMemberID());
		List<Map<String,String>> resultList = ConstantsSetting.qLGetListByProcedure(0, 0, strQL, postValue);
		if (resultList != null && resultList.size() > 0) {
			Map<String, String> values = resultList.get(0);
			String qlscore = values.get("score");
			if (qlscore != null) {
				result.setResult(true);
				result.setMessage("����ǩ��״̬�ɹ���");
				result.setOriginalResult(values);
				setValue("Score", values.get("score"));
				//setValues(values);
				return result;
			} else {
				result.setMessage(values.get("message"));
				return result;
			}
		} else {
			return result;
		}
	}

	/**
	 * ��ȡ��ԱID
	 * 
	 * @return
	 */
	public static String getMemberID() {
		if (User.IsLogged) {
			return _profiles.get("MemberID");
		} else {
			return null;
		}
	}

	/**
	 * ��ȡ��Ա��Ӧ��ID
	 * 
	 * @return
	 */
	public static String getMemberAppUserID() {
		if (User.IsLogged) {
			return _profiles.get("MemAppUserID");
		} else {
			return null;
		}
	}

	/**
	 * ��ȡ�豸��Ӧ��ID
	 * 
	 * @param context
	 * @return
	 */
	public static String getDevAppUserID(Context context) {
		String devappuid = _profiles.get(Device_AppUserID);
		if (devappuid == null) {
			String strQL = ConstantsSetting.QLGetDevAppUserID;
			Map<String, String> postValue = new HashMap<String, String>();
			postValue.put("deviceID", new DeviceUuidFactory(context)
					.getDeviceUuid().toString());
			List<Map<String, String>> result = ConstantsSetting.qLGetList(0, 0,
					strQL, postValue);
			if (result != null && result.size() > 0) {
				Map<String, String> values = result.get(0);
				int count = Integer
						.parseInt(values.get("DevUCount").toString());
				if (count > 0) {
					_profiles.putAll(values);
					String devAppUID = values.get("DevAppUserID").toString();
					setValue(context, Device_AppUserID, devAppUID);
					setValue(context, Device_IsUse, values.get("DevIsUse")
							.toString());
					return devAppUID;
				}
			}
		}
		return devappuid;
	}

	/**
	 * ��ȡ��¼ID
	 * 
	 * @param context
	 * @return
	 */
	public static String getLoginID(Context context) {
		return getValue(context, Member_LoginID);
	}

	/**
	 * ��ȡ��¼����
	 * 
	 * @param context
	 * @return
	 */
	public static String getLoginPassword(Context context) {
		return getValue(context, Member_Password);
	}

	/**
	 * ��ȡ��Ա����
	 * 
	 * @param context
	 * @return
	 */
	public static String getMemberName(Context context) {
		return getValue(context, "MemberName");
	}

	/**
	 * ��ȡ��Ա����ֵ
	 * 
	 * @param context
	 * @return
	 */
	public static String getScore(Context context) {
		return getValue(context, "Score");
	}

	/**
	 * ���±��ػ�Ա����ֵ
	 * 
	 * @param context
	 * @return
	 */
	public static void updateScore(Context context, String addscore) {
		try {
			int score = Integer.parseInt(getValue(context, "Score"));
			int addscorenum = Integer.parseInt(addscore);
			setValue(context, "Score", (score + addscorenum) + "");
		} catch (NumberFormatException e) {
			// TODO �Զ����ɵ� catch ��
		}
	}

	/**
	 * ��ȡusername
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserName(Context context) {
		return getValue(context, "UserName");
	}

	/**
	 * �����Զ���¼ֵ
	 * 
	 * @param context
	 * @param autoLogin
	 *            �Ƿ��Զ���¼��ֵ
	 */
	private static void setAutoLogin(Context context, Boolean autoLogin) {
		setValue(context, User.User_AutoLogin, autoLogin ? "1" : "0");
	}

	/**
	 * ��ȡ�Զ���¼����ֵ��true��ʾ�Զ���¼ѡ��Ϊѡ��
	 * 
	 * @param context
	 *            ��getbaseContext()
	 * @return
	 */
	public static Boolean getAutoLogin(Context context) {
		String autoLogin = getValue(context, User_AutoLogin);
		return (autoLogin.equals("1")) ? true : false;
	}

	/**
	 * ��ȡ�Զ���¼��Ч״̬��true��ʾ�����Զ���¼
	 * 
	 * @param context
	 *            ��getbaseContext()
	 * @return
	 */
	public static Boolean getAutoLoginAble(Context context) {
		String autoLoginAble = getValue(context, User_AutoLoginAble);
		return (autoLoginAble.equals("1")) ? true : false;
	}

	/**
	 * ����User���õ�ֵ
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setValue(Context context, String key, String value) {
		synchronized (User.class) {
			final SharedPreferences prefs = context.getSharedPreferences(
					PREFS_FILE, 0);
			_profiles.put(key, value);
			prefs.edit().putString(key, value).commit();
		}
	}

	/**
	 * ����User���õ�ֵ
	 * 
	 * @param key
	 * @param value
	 */
	public void setValue(String key, String value) {
		synchronized (User.class) {
			final SharedPreferences prefs = _context.getSharedPreferences(
					PREFS_FILE, 0);
			_profiles.put(key, value);
			prefs.edit().putString(key, value).commit();
		}
	}

	/**
	 * ����User���õ�ֵ
	 * 
	 * @param context
	 * @param values
	 *            ���õļ�ֵ����
	 */
	private static void setValues(Context context, Map<String, String> values) {
		synchronized (User.class) {
			final SharedPreferences prefs = context.getSharedPreferences(
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
	 * ����User���õ�ֵ
	 * 
	 * @param values
	 *            ���õļ�ֵ����
	 */
	private void setValues(Map<String, String> values) {
		synchronized (User.class) {
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
	public static String getValue(Context context, String key) {
		String value = _profiles.get(key);
		if (value == null) {
			synchronized (User.class) {
				final SharedPreferences prefs = context.getSharedPreferences(
						PREFS_FILE, 0);
				// �������ļ���ȡ���õ�ֵ
				value = prefs.getString(key, _defaultValues.get(key));
				_profiles.put(key, value);
			}
		}
		return value;
	}

	/**
	 * ��ȡUser���õ�ֵ
	 * 
	 * @param key
	 * @return
	 */
	public String getValue(String key) {
		String value = _profiles.get(key);
		if (value == null) {
			synchronized (User.class) {
				final SharedPreferences prefs = _context.getSharedPreferences(
						PREFS_FILE, 0);
				// �������ļ���ȡ���õ�ֵ
				value = prefs.getString(key, _defaultValues.get(key));
				_profiles.put(key, value);
			}
		}
		return value;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param context
	 * @return
	 */
	public static Map<String, String> getAll(Context context) {
		synchronized (User.class) {
			final SharedPreferences prefs = context.getSharedPreferences(
					PREFS_FILE, 0);
			// �������ļ���ȡ���õ�ֵ
			Map<String, String> allconfigs = (Map<String, String>) prefs
					.getAll();
			_profiles.putAll(allconfigs);
		}
		return _profiles;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return
	 */
	public Map<String, String> getAll() {
		synchronized (User.class) {
			final SharedPreferences prefs = _context.getSharedPreferences(
					PREFS_FILE, 0);
			// �������ļ���ȡ���õ�ֵ
			Map<String, String> allconfigs = (Map<String, String>) prefs
					.getAll();
			_profiles.putAll(allconfigs);
		}
		return _profiles;
	}
	
	/**
	 * �ϴ��û����׸�λ��
	 * @param context
	 * @param address
	 * @return
	 */
	public static CommandResult updateFirstLocation(Context context, String address) {
		CommandResult result = new CommandResult(false, "����ʧ�ܡ�");
	    
	    String devAppUID = User.getDevAppUserID(context);
	    if(devAppUID==null){
	    	result.setMessage("��Ч���ֻ��û���");
	    	return result;
	    }
	    
	    List<QLUpdateField> updateFields = new ArrayList<QLUpdateField>();
	    updateFields.add(new QLUpdateField("Address", address));
	    
	    String whereString = "where AppUserID=" + devAppUID;
		
		try {
			CommandResult updateResult = ConstantsSetting.QLUpdate("USR_APPUser", updateFields, whereString, null);
			if(updateResult!=null) {
				return updateResult;
			} 
		} catch (Exception e) {
			// TODO: handle exception
	    	result.setMessage("δ֪���󣬱���ʧ�ܡ�");
			return result;
		}
		
		return result;
	}	

	/**
	 * ��֤���֤���Ƿ�ע��
	 * @param idcardnum
	 * @return
	 */
	public static CommandResult checkIdCardNum(String idcardnum)
	{
		if(idcardnum!=null && !(idcardnum.equals(""))) {
			String strQL = ConstantsSetting.QLCheckMemberIdCardNum;
			strQL = String.format(strQL, idcardnum);		
			List<Map<String,String>> list = ConstantsSetting.qLGetList(1, 1,strQL, null);
			if(list==null || list.size()>0) {
				return new CommandResult(false, "���֤���ѱ�ע�ᡣ");
			} else {
				return new CommandResult(true, "");
			}			
		} else {
			return new CommandResult(true, "");			
		}
	}

	/**
	 * ��֤�ֻ����Ƿ�ע��
	 * @param mobile
	 * @return
	 */
	public static CommandResult checkMobile(String mobile)
	{
		String strQL = ConstantsSetting.QLCheckMemberMobile;
		strQL = String.format(strQL, mobile);			
		List<Map<String,String>> list = ConstantsSetting.qLGetList(1, 1,strQL, null);
		if(list==null || list.size()>0) {
			return new CommandResult(false, "�ֻ����ѱ�ע�ᡣ");
		} else {
			return new CommandResult(true, "");
		}	
	}

}
