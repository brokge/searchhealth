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
 * 用户信息
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
	 * 标记为是否登录
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
					// 默认用户文件
					final SharedPreferences prefs = context
							.getSharedPreferences(PREFS_FILE, 0);
					_isCreated = prefs.getString(Profile_IsCreated, null);
					if (_isCreated == null) {
						// 去服务端取UserID
						// GetUserID
						_isCreated = "true";
					}
				}
			}
		}
	}

	/**
	 * 初始化User信息，读取本地配置文件
	 * 
	 * @param context
	 */
	public static void initialization(Context context) {
		if (_isCreated == null) {
			synchronized (User.class) {
				if (_isCreated == null) {
					// 默认用户文件
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
							// 将值写入配置文件
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
	 * 会员注册，返回CommandResult。
	 * @param mobile 手机号码
	 * @param email 注册邮箱
	 * @param pwd 注册的登录密码（未加密的）
	 * @return
	 */
	public CommandResult register(String mobile, String email, String pwd)
	{
		CommandResult result = new CommandResult(false, "注册失败，未知错误");
		pwd = MD5.getMD5(pwd);
		//请求注册过程
		//若注册成功，返回[result],[message],[count],MemberID,UserName,MemberName,Email,Mobile,MemAppUserID;
		//若注册失败，返回[result],[message]键值。
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
	 * 会员登录
	 * 
	 * @param loginID
	 *            登录ID
	 * @param pwd
	 *            密码
	 * @param originalPwd
	 *            标记是否原始密码，1表示未加密的原始密码，0表示加密过的密码（从配置文件读取的）
	 * @param autoLogin
	 *            标记是否自动登录
	 * @return 返回登录结果，true为登录成功
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
	 * 自动登录
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
	 * 会员注销登录
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
	 * 修改密码
	 * 
	 * @param oldPwd
	 *            加密前的老密码
	 * @param newPwd
	 *            加密前的新密码
	 * @return CommandResult返回操作结果，getResult()得到true为成功，getMessage()得到说明信息。
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
		CommandResult commandResult = new CommandResult("false", "未知错误");
		return commandResult;
	}
	
	/**
	 * 会员重置密码，返回CommandResult。
	 * @param mobile 手机号码
	 * @param email 注册邮箱
	 * @param newpwd 新密码（未加密的）
	 * @return
	 */
	public CommandResult resetPwd(String mobile, String email, String newpwd)
	{
		CommandResult result = new CommandResult(false, "密码重置失败，未知错误");
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
	 * 会员签到，返回CommandResult。
	 * @return
	 */
	public CommandResult checkIn()
	{
		CommandResult result = new CommandResult(false, "签到失败，未知错误");
		//请求签到过程
		//若签到成功，返回[result],[message],[addscore],[score],[checkintimes],[nextscore],[today];today表示今天是否签到的状态，1为已签到，0为未签到
		//若签到失败，返回[result],[message]键值。
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
	 * 获取签到状态，返回CommandResult。
	 * @return
	 */
	public CommandResult getCheckInState()
	{
		CommandResult result = new CommandResult(false, "更新签到状态失败，未知错误");
		//请求签到过程
		//若获取成功，返回[score],[checkintimes],[nextscore],[today];
		//若获取失败，返回[message]键值。
		String strQL = "plugin DrugCenter.Logic.V1 Member GetCheckInState($memberid$)";
		Map<String,String> postValue = new HashMap<String, String>();
		postValue.put("memberid", getMemberID());
		List<Map<String,String>> resultList = ConstantsSetting.qLGetListByProcedure(0, 0, strQL, postValue);
		if (resultList != null && resultList.size() > 0) {
			Map<String, String> values = resultList.get(0);
			String qlscore = values.get("score");
			if (qlscore != null) {
				result.setResult(true);
				result.setMessage("更新签到状态成功。");
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
	 * 获取会员ID
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
	 * 获取会员的应用ID
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
	 * 获取设备的应用ID
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
	 * 获取登录ID
	 * 
	 * @param context
	 * @return
	 */
	public static String getLoginID(Context context) {
		return getValue(context, Member_LoginID);
	}

	/**
	 * 获取登录密码
	 * 
	 * @param context
	 * @return
	 */
	public static String getLoginPassword(Context context) {
		return getValue(context, Member_Password);
	}

	/**
	 * 获取会员姓名
	 * 
	 * @param context
	 * @return
	 */
	public static String getMemberName(Context context) {
		return getValue(context, "MemberName");
	}

	/**
	 * 获取会员健康值
	 * 
	 * @param context
	 * @return
	 */
	public static String getScore(Context context) {
		return getValue(context, "Score");
	}

	/**
	 * 更新本地会员健康值
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
			// TODO 自动生成的 catch 块
		}
	}

	/**
	 * 获取username
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserName(Context context) {
		return getValue(context, "UserName");
	}

	/**
	 * 设置自动登录值
	 * 
	 * @param context
	 * @param autoLogin
	 *            是否自动登录的值
	 */
	private static void setAutoLogin(Context context, Boolean autoLogin) {
		setValue(context, User.User_AutoLogin, autoLogin ? "1" : "0");
	}

	/**
	 * 获取自动登录设置值，true表示自动登录选项为选中
	 * 
	 * @param context
	 *            传getbaseContext()
	 * @return
	 */
	public static Boolean getAutoLogin(Context context) {
		String autoLogin = getValue(context, User_AutoLogin);
		return (autoLogin.equals("1")) ? true : false;
	}

	/**
	 * 获取自动登录有效状态，true表示可以自动登录
	 * 
	 * @param context
	 *            传getbaseContext()
	 * @return
	 */
	public static Boolean getAutoLoginAble(Context context) {
		String autoLoginAble = getValue(context, User_AutoLoginAble);
		return (autoLoginAble.equals("1")) ? true : false;
	}

	/**
	 * 保存User设置的值
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
	 * 保存User设置的值
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
	 * 保存User设置的值
	 * 
	 * @param context
	 * @param values
	 *            设置的键值集合
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
				// 将值写入配置文件
				prefs.edit().putString(key, value).commit();
			}
		}
	}

	/**
	 * 保存User设置的值
	 * 
	 * @param values
	 *            设置的键值集合
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
				// 将值写入配置文件
				prefs.edit().putString(key, value).commit();
			}
		}
	}

	/**
	 * 获取User设置的值
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
				// 从配置文件获取配置的值
				value = prefs.getString(key, _defaultValues.get(key));
				_profiles.put(key, value);
			}
		}
		return value;
	}

	/**
	 * 获取User设置的值
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
				// 从配置文件获取配置的值
				value = prefs.getString(key, _defaultValues.get(key));
				_profiles.put(key, value);
			}
		}
		return value;
	}

	/**
	 * 获取所有设置
	 * 
	 * @param context
	 * @return
	 */
	public static Map<String, String> getAll(Context context) {
		synchronized (User.class) {
			final SharedPreferences prefs = context.getSharedPreferences(
					PREFS_FILE, 0);
			// 从配置文件获取配置的值
			Map<String, String> allconfigs = (Map<String, String>) prefs
					.getAll();
			_profiles.putAll(allconfigs);
		}
		return _profiles;
	}

	/**
	 * 获取所有设置
	 * 
	 * @return
	 */
	public Map<String, String> getAll() {
		synchronized (User.class) {
			final SharedPreferences prefs = _context.getSharedPreferences(
					PREFS_FILE, 0);
			// 从配置文件获取配置的值
			Map<String, String> allconfigs = (Map<String, String>) prefs
					.getAll();
			_profiles.putAll(allconfigs);
		}
		return _profiles;
	}
	
	/**
	 * 上传用户的首个位置
	 * @param context
	 * @param address
	 * @return
	 */
	public static CommandResult updateFirstLocation(Context context, String address) {
		CommandResult result = new CommandResult(false, "保存失败。");
	    
	    String devAppUID = User.getDevAppUserID(context);
	    if(devAppUID==null){
	    	result.setMessage("无效的手机用户。");
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
	    	result.setMessage("未知错误，保存失败。");
			return result;
		}
		
		return result;
	}	

	/**
	 * 验证身份证号是否被注册
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
				return new CommandResult(false, "身份证号已被注册。");
			} else {
				return new CommandResult(true, "");
			}			
		} else {
			return new CommandResult(true, "");			
		}
	}

	/**
	 * 验证手机号是否被注册
	 * @param mobile
	 * @return
	 */
	public static CommandResult checkMobile(String mobile)
	{
		String strQL = ConstantsSetting.QLCheckMemberMobile;
		strQL = String.format(strQL, mobile);			
		List<Map<String,String>> list = ConstantsSetting.qLGetList(1, 1,strQL, null);
		if(list==null || list.size()>0) {
			return new CommandResult(false, "手机号已被注册。");
		} else {
			return new CommandResult(true, "");
		}	
	}

}
