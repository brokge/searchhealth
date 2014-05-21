package coolbuy360.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

public class Util {    
	/**
	 * 百度push相关设置和方法
	 * 
	 */
	public static final String TAG = "BaiduPush";
	public static final String RESPONSE_METHOD = "method";
	public static final String RESPONSE_CONTENT = "content";
	public static final String RESPONSE_ERRCODE = "errcode";
	protected static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
	public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
	public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
	public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
	protected static final String EXTRA_ACCESS_TOKEN = "access_token";
	public static final String EXTRA_MESSAGE = "message";
	
	// 获取AppKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
        	return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
            	apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }
	
	
	
	public static float round(float scale, int times) {// times表示保留小数点times位。scale表示传入的值
		int value = 1;
		for (int i = 0; i <= times; i++) {
			value *= 10;
		}
		int a = ((int) (scale * value)) % 10;
		if (a >= 5) {
			a = (int) (scale * value / 10) + 1;
		} else {
			a = (int) (scale * value / 10);
		}
		float result = ((float) (a * 1)) / (value / 10);
		return result;
	}

	/**
	 * 添加并运行相关权限命令
	 * 
	 * @param command
	 *            命令shell
	 * @return 返回是否运行成功
	 */
	public static boolean runCommand(String command) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);
			Log.i("command", "The Command is : " + command);
			process.waitFor();
		} catch (Exception e) {
			Log.w("Exception ", "Unexpected error - " + e.getMessage());
			return false;
		} finally {
			try {
				process.destroy();
			} catch (Exception e) {
				Log.w("Exception ", "Unexpected error - " + e.getMessage());
			}
		}
		return true;
	}

	/** 执行Linux命令，并返回执行结果。 */
	public static String exec(String[] args) {
		String result = "";
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		Process process = null;
		InputStream errIs = null;
		InputStream inIs = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read = -1;
			process = processBuilder.start();
			errIs = process.getErrorStream();
			while ((read = errIs.read()) != -1) {
				baos.write(read);
			}
			baos.write('\n');
			inIs = process.getInputStream();
			while ((read = inIs.read()) != -1) {
				baos.write(read);
			}
			byte[] data = baos.toByteArray();
			result = new String(data);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (errIs != null) {
					errIs.close();
				}
				if (inIs != null) {
					inIs.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (process != null) {
				process.destroy();
			}
		}
		return result;
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static Date getNowDate() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();

	}

	/**
	 * 获取当前时间
	 * 
	 * @param dateformat
	 *            "yyyy-MM-dd HH:mm:ss aa" 最后的aa表示“上午”或“下午” HH表示24小时制
	 *            如果换成hh表示12小时制
	 * @return 返回当前时间的string串
	 * 
	 */
	public static String getNowDate(String dateformat) {
		String temp_str = "";
		Date dt = new Date();
		// 最后的aa表示“上午”或“下午” HH表示24小时制 如果换成hh表示12小时制
		SimpleDateFormat sdf = new SimpleDateFormat(dateformat);		
		temp_str = sdf.format(dt);
		return temp_str;
	}

	/**
	 * 改变日期格式
	 * 
	 * @param dt
	 *            日期字符串
	 * @param dateformat
	 *            "yyyy-MM-dd HH:mm:ss aa E" 最后的aa表示“上午”或“下午” HH表示24小时制
	 *            如果换成hh表示12小时制，E表示星期几
	 * @return 返回当前时间的string串
	 * 
	 */
	public static String getDateFormat(String dt, String dateformat) {
		String temp_str = "";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		try {
			Date strtodate = formatter.parse(dt);
			SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
			temp_str = sdf.format(strtodate);
			return temp_str;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date strtodate = formatter.parse(dt);
				SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
				temp_str = sdf.format(strtodate);
				return temp_str;
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				return "";
			}
		}
	}		
	
	/**
	 * 改变日期格式
	 * 
	 * @param dt
	 *            日期字符串
	 * @param dateformat
	 *            "yyyy-MM-dd HH:mm:ss aa E" 最后的aa表示“上午”或“下午” HH表示24小时制
	 *            如果换成hh表示12小时制，E表示星期几
	 * @param inputformat
	 * 			传入的文字格式
	 * @return 返回当前时间的string串
	 * 
	 */
	public static String getDateFormat(String dt, String dateformat, String inputformat) {
		String temp_str = "";
		SimpleDateFormat formatter = new SimpleDateFormat(inputformat);
		try {
			Date strtodate = formatter.parse(dt);
			SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
			temp_str = sdf.format(strtodate);
			return temp_str;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return getDateFormat(dt, dateformat);
		}
	}
/**
 * 从字符串转化成Date类型
 * @param dt
 *        日期字符串
 * @param dateformat
 *        转化的格式
 * @return
 *       返回Date类型
 */
	public static Date getDateFromStr(String dt, String dateformat)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(dateformat);
		Date strtodate;
		try {
			strtodate = formatter.parse(dt);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		 return null;
		}
		return strtodate;
		
	}
	/*
	 * formatter = new SimpleDateFormat("yyyy-MM-dd"); updatetime =
	 * formatter.format(strtodate);
	 */

	// clear the cache before time numDays
	/**
	 * @param dir
	 *            文件路径
	 * @param numDays
	 *            截至日期
	 * @return 返回的是删除的文件数
	 */
	/*
	 * public static int clearCacheFolder(File dir, long numDays) {
	 * 
	 * int deletedFiles = 0;
	 * 
	 * if (dir!= null && dir.isDirectory()) {
	 * 
	 * try {
	 * 
	 * for (File child:dir.listFiles()) {
	 * 
	 * if (child.isDirectory()) {
	 * 
	 * deletedFiles += clearCacheFolder(child, numDays);
	 * 
	 * }
	 * 
	 * if (child.lastModified() < numDays) {
	 * 
	 * if (child.delete()) {
	 * 
	 * deletedFiles++;
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * } catch(Exception e) {
	 * 
	 * e.printStackTrace();
	 * 
	 * }
	 * 
	 * }
	 * 
	 * return deletedFiles;
	 * 
	 * }
	 */

	/*
	 * //优先使用缓存：
	 * WebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
	 * 
	 * //不使用缓存： WebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
	 * 
	 * 在退出时删除缓存 File file = CacheManager.getCacheFileBaseDir(); if (file != null
	 * && file.exists() && file.isDirectory()) { for (File item :
	 * file.listFiles()) { item.delete(); } file.delete(); }
	 * 
	 * context.deleteDatabase("webview.db");
	 * context.deleteDatabase("webviewCache.db");
	 */

	/**
	 * 判断网络类型 TYPE_DUMMY 8 (0x00000008) TYPE_ETHERNET 9 (0x00000009) TYPE_MOBILE
	 * 0 (0x00000000) TYPE_MOBILE_DUN 4 (0x00000004) TYPE_MOBILE_HIPRI 5
	 * (0x00000005) TYPE_MOBILE_MMS 2 (0x00000002) TYPE_MOBILE_SUPL 3
	 * (0x00000003) TYPE_WIFI 1 (0x00000001) TYPE_WIMAX 6 (0x00000006)
	 * DEFAULT_NETWORK_PREFERENCE 1 (0x00000001) TYPE_BLUETOOTH 7 (0x00000007)
	 * 
	 * @param context
	 * @return
	 */
	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}
	
	/**
	 * 判断否有网络连接
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		int netState = getConnectedType(context);
		if (netState == ConnectivityManager.TYPE_WIFI
				|| netState == ConnectivityManager.TYPE_MOBILE) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 判断wifi是否连接
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		WifiManager manager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		return manager.isWifiEnabled();
	}
	
	/**
	 * 根据配置获取药品图片路径
	 * @param AutoIsCheck
	 *        是否自动优化
	 * @param Img_Quality
	 *        手动优化时的图片质量  high  |  low
	 * @param Net_Type
	 *         当前网络类型   1表示wifi  0表示mobile
	 * @return
	 */
	public static String getDrugSmallImgPath() {
		if (searchApp.autoischeck.equals("true")) {
			if (searchApp.Net_Type == 1) {
				return searchApp.Drug_Img_Path_H_S;
			} else {
				return searchApp.Drug_Img_Path_L_S;
			}
		} else if (searchApp.Img_Quality.equals("high")) {
			return searchApp.Drug_Img_Path_H_S;
		} else {
			return searchApp.Drug_Img_Path_L_S;
		}
	}

	public static String getDrugBigImgPath() {
		if (searchApp.autoischeck.equals("true")) {
			if (searchApp.Net_Type == 1) {
				return searchApp.Drug_Img_Path_H_B;
			} else {
				return searchApp.Drug_Img_Path_L_B;
			}
		} else if (searchApp.Img_Quality.equals("high")) {
			return searchApp.Drug_Img_Path_H_B;
		} else {
			return searchApp.Drug_Img_Path_L_B;
		}
	}

	/**
	 * 获取促销图片路径
	 * @return
	 */
	public static String getPromotionImgPath() {
		if (searchApp.autoischeck.equals("true")) {
			if (searchApp.Net_Type == 1) {
				return searchApp.Promotion_Img_Path_H;
			} else {
				return searchApp.Promotion_Img_Path_L;
			}
		} else if (searchApp.Img_Quality.equals("high")) {
			return searchApp.Promotion_Img_Path_H;
		} else {
			return searchApp.Promotion_Img_Path_L;
		}
	}

	/**
	 * 获取资讯图片路径
	 * @return
	 */
	public static String getNewsImgPath() {
		if (searchApp.autoischeck.equals("true")) {
			if (searchApp.Net_Type == 1) {
				return searchApp.News_Img_Path_H;
			} else {
				return searchApp.News_Img_Path_L;
			}
		} else if (searchApp.Img_Quality.equals("high")) {
			return searchApp.News_Img_Path_H;
		} else {
			return searchApp.News_Img_Path_L;
		}
	}
}
