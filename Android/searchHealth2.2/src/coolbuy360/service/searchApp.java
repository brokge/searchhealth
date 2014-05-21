package coolbuy360.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;

import coolbuy360.log.CrashHandler;
import coolbuy360.logic.AppConfig;
import coolbuy360.logic.NoticeStateConfig;
import coolbuy360.logic.User;

import com.baidu.frontia.FrontiaApplication;


public class searchApp extends FrontiaApplication {

	/* public class searchApp extends Application { */
	
	private static searchApp mInstance = null;
	
	//新浪微博授权key
	public static final String CONSUMER_KEY = "4240941240";// 替换为开发者的appkey，例如"1646212860";
	public static final String REDIRECT_URL = "http://www.sina.com.cn";
	public static final String ACCESS_TOKEN = "2.00opZdgDEYYAdE012f1f81fdxenZqB";
	//图片的路径app.wcjk100.com/app/drugimg/real/hight/200_200|1280_800
    //											/low/200_200|1280_800		
	public static final String Drug_Img_Path_H_S ="http://app.wcjk100.com/app/drugimg/high/200_200/";
	public static final String Drug_Img_Path_H_B ="http://app.wcjk100.com/app/drugimg/high/1280_800/";
	public static final String Drug_Img_Path_L_S ="http://app.wcjk100.com/app/drugimg/low/200_200/";
	public static final String Drug_Img_Path_L_B ="http://app.wcjk100.com/app/drugimg/low/1280_800/";
	public static final String Promotion_Img_Path_H ="http://app.wcjk100.com/app/promotionimg/high/960_360/";
	public static final String Promotion_Img_Path_L ="http://app.wcjk100.com/app/promotionimg/low/960_360/";
	public static final String News_Img_Path_H ="http://app.wcjk100.com/app/subjectimg/high/960_360/";
	public static final String News_Img_Path_L ="http://app.wcjk100.com/app/subjectimg/low/960_360/";
	public static final String News_Img_Path_L_S ="http://app.wcjk100.com/app/subjectimg/low/300_300/";
	/*测试地址*/
	/*public static final String Promotion_Img_Path_H ="http://192.168.1.150/promotionimg/high/960_360/";
	public static final String Promotion_Img_Path_L ="http://192.168.1.150/promotionimg/low/960_360/";*/
	public static String autoischeck="false";//判断是否是自动优化
	public static String Img_Quality="low";//判读啊加载图片的质量
	public static int Net_Type=0;

	// 授权Key
	// 申请地址：http://developer.baidu.com/map/android-mobile-apply-key.htm
	// 百度MapAPI的管理类
    public BMapManager mBMapManager = null;
    // 授权Key正确，验证通过
	public boolean m_bKeyRight = true; 
	private HttpClient httpClient;
	
	public static LocationProvider mLocationProvider = null;
	
	@Override
	public void onCreate() {
		//super.onCreate();
		mInstance = this;
		mLocationProvider = new LocationProvider(this);
		mLocationProvider.startLocation();
		initEngineManager(this);
		
		// 启动日志生成服务
		/*Intent logServiceIntent = new Intent(getApplicationContext(), LogService.class); 
		startService(logServiceIntent);*/
		
		Log.v("searchHealthAPP", "onCreate");
				
		httpClient = this.createHttpClient();
		
		// 初始化用户配置文件
		initUserConfig();
		//初始化AppConfig
		initAppConfig();
		//初始化通知状态
		initNoticeStateConfig();
		
		// 初始化httpClient	
		//Log.i("chenlinwei", this.getFilesDir() + "");
		//请将工程的 Application 类继承 FrontiaApplication 类，在 onCreate 函数中加上:	super.onCreate()，否则会崩溃
		super.onCreate();//
		
		// 启动异常崩溃监听
		CrashHandler crashHandler = CrashHandler.getInstance();  
        crashHandler.init(getApplicationContext());
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	public static class MyGeneralListener implements MKGeneralListener {
		
		 @Override
	        public void onGetNetworkState(int iError) {
	            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
	                Toast.makeText(searchApp.getInstance().getApplicationContext(), "您的网络出错啦！",
	                    Toast.LENGTH_LONG).show();
	            }
	            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
	                Toast.makeText(searchApp.getInstance().getApplicationContext(), "输入正确的检索条件！",
	                        Toast.LENGTH_LONG).show();
	            }
	            // ...
	        }

	        @Override
	        public void onGetPermissionState(int iError) {
	        	//非零值表示key验证未通过
	            if (iError != 0) {
	                //授权Key错误：
	                /*Toast.makeText(searchApp.getInstance().getApplicationContext(), 
	                        "请在输入正确的授权Key,并检查您的网络连接是否正常！error: "+iError, Toast.LENGTH_LONG).show();*/
	                searchApp.getInstance().m_bKeyRight = false;
	            }
	            else{
	            	searchApp.getInstance().m_bKeyRight = true;
	            	/*Toast.makeText(searchApp.getInstance().getApplicationContext(), 
	                        "key认证成功", Toast.LENGTH_LONG).show();*/
	            }
	        }		
	}

	@Override
	// 建议在您app的退出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消耗
	public void onTerminate() {
		// TODO Auto-generated method stub
		if (mBMapManager != null) {
			mBMapManager.destroy();
			mBMapManager = null;
		}
		if(mLocationProvider !=null){
			mLocationProvider.stopListener();
		}
		super.onTerminate();
	}

	/**
	 * 初始化用户配置文件
	 */
	public void initUserConfig() {
		// Toast.makeText(getBaseContext(), "自动登录", 1).show();
		User.initialization(getBaseContext());
	}
	
	public void initAppConfig()
	{
		AppConfig appconfig=new AppConfig(getBaseContext());
		autoischeck=appconfig.getValue("Is_2G3G_AutoLoadImage");						
		Img_Quality=appconfig.getValue("Img_Quality");//图片质量问题	
		Net_Type=Util.getConnectedType(getBaseContext());
	}
	
	public void initNoticeStateConfig() {
		NoticeStateConfig noticestateconfig = new NoticeStateConfig(
				getBaseContext());
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		this.shutdownHttpClient();
	}

	// 关闭连接管理器并释放资源
	private void shutdownHttpClient() {
		if (httpClient != null && httpClient.getConnectionManager() != null) {
			httpClient.getConnectionManager().shutdown();
		}
	}

	// 创建HttpClient实例
	private HttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params,
				HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);
		HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
		HttpConnectionParams.setSoTimeout(params, 20 * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schReg.register(new Scheme("https",
				SSLSocketFactory.getSocketFactory(), 443));

		ClientConnectionManager connMgr = new ThreadSafeClientConnManager(
				params, schReg);

		return new DefaultHttpClient(connMgr, params);
	}

	// 对外提供HttpClient实例
	public HttpClient getHttpClient() {
		return httpClient;
	}

	// 完美退出的方法

	private List<Activity> activityList = new LinkedList<Activity>();

	// 单例模式中获取唯一的MyApplication实例
	public static searchApp getInstance() {
		if (mInstance == null) {
			mInstance = new searchApp();
		}
		return mInstance;
	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void exitObject(String className) {
		for (Activity activity : activityList) {
			// if(activity.getClass().equals())
			if (activity.getClass().getName().equals(className)) {
				activity.finish();
			}
		}
	}
	
	public void exit() {
		
		// 退出时清除通知栏
		NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.cancelAll();

    	// 遍历所有Activity并finish
		for (Activity activity : activityList) {
			activity.finish();
		}
		if (mBMapManager != null) {
			mBMapManager.destroy();
			mBMapManager = null;
		}
		if(mLocationProvider !=null){
			mLocationProvider.stopListener();
		}
		System.exit(0);
	}
		
	public void initEngineManager(Context context) {
		if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(new MyGeneralListener())) {
            Toast.makeText(searchApp.getInstance().getApplicationContext(), 
                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
	}
}
