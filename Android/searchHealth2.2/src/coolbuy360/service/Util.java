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
	 * �ٶ�push������úͷ���
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
	
	// ��ȡAppKey
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
	
	
	
	public static float round(float scale, int times) {// times��ʾ����С����timesλ��scale��ʾ�����ֵ
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
	 * ��Ӳ��������Ȩ������
	 * 
	 * @param command
	 *            ����shell
	 * @return �����Ƿ����гɹ�
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

	/** ִ��Linux���������ִ�н���� */
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
	 * ��ȡ��ǰʱ��
	 * 
	 * @return
	 */
	public static Date getNowDate() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();

	}

	/**
	 * ��ȡ��ǰʱ��
	 * 
	 * @param dateformat
	 *            "yyyy-MM-dd HH:mm:ss aa" ����aa��ʾ�����硱�����硱 HH��ʾ24Сʱ��
	 *            �������hh��ʾ12Сʱ��
	 * @return ���ص�ǰʱ���string��
	 * 
	 */
	public static String getNowDate(String dateformat) {
		String temp_str = "";
		Date dt = new Date();
		// ����aa��ʾ�����硱�����硱 HH��ʾ24Сʱ�� �������hh��ʾ12Сʱ��
		SimpleDateFormat sdf = new SimpleDateFormat(dateformat);		
		temp_str = sdf.format(dt);
		return temp_str;
	}

	/**
	 * �ı����ڸ�ʽ
	 * 
	 * @param dt
	 *            �����ַ���
	 * @param dateformat
	 *            "yyyy-MM-dd HH:mm:ss aa E" ����aa��ʾ�����硱�����硱 HH��ʾ24Сʱ��
	 *            �������hh��ʾ12Сʱ�ƣ�E��ʾ���ڼ�
	 * @return ���ص�ǰʱ���string��
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
	 * �ı����ڸ�ʽ
	 * 
	 * @param dt
	 *            �����ַ���
	 * @param dateformat
	 *            "yyyy-MM-dd HH:mm:ss aa E" ����aa��ʾ�����硱�����硱 HH��ʾ24Сʱ��
	 *            �������hh��ʾ12Сʱ�ƣ�E��ʾ���ڼ�
	 * @param inputformat
	 * 			��������ָ�ʽ
	 * @return ���ص�ǰʱ���string��
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
 * ���ַ���ת����Date����
 * @param dt
 *        �����ַ���
 * @param dateformat
 *        ת���ĸ�ʽ
 * @return
 *       ����Date����
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
	 *            �ļ�·��
	 * @param numDays
	 *            ��������
	 * @return ���ص���ɾ�����ļ���
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
	 * //����ʹ�û��棺
	 * WebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
	 * 
	 * //��ʹ�û��棺 WebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
	 * 
	 * ���˳�ʱɾ������ File file = CacheManager.getCacheFileBaseDir(); if (file != null
	 * && file.exists() && file.isDirectory()) { for (File item :
	 * file.listFiles()) { item.delete(); } file.delete(); }
	 * 
	 * context.deleteDatabase("webview.db");
	 * context.deleteDatabase("webviewCache.db");
	 */

	/**
	 * �ж��������� TYPE_DUMMY 8 (0x00000008) TYPE_ETHERNET 9 (0x00000009) TYPE_MOBILE
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
	 * �жϷ�����������
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
	 * �ж�wifi�Ƿ�����
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		WifiManager manager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		return manager.isWifiEnabled();
	}
	
	/**
	 * �������û�ȡҩƷͼƬ·��
	 * @param AutoIsCheck
	 *        �Ƿ��Զ��Ż�
	 * @param Img_Quality
	 *        �ֶ��Ż�ʱ��ͼƬ����  high  |  low
	 * @param Net_Type
	 *         ��ǰ��������   1��ʾwifi  0��ʾmobile
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
	 * ��ȡ����ͼƬ·��
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
	 * ��ȡ��ѶͼƬ·��
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
