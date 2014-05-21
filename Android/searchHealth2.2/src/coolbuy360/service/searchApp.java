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
	
	//����΢����Ȩkey
	public static final String CONSUMER_KEY = "4240941240";// �滻Ϊ�����ߵ�appkey������"1646212860";
	public static final String REDIRECT_URL = "http://www.sina.com.cn";
	public static final String ACCESS_TOKEN = "2.00opZdgDEYYAdE012f1f81fdxenZqB";
	//ͼƬ��·��app.wcjk100.com/app/drugimg/real/hight/200_200|1280_800
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
	/*���Ե�ַ*/
	/*public static final String Promotion_Img_Path_H ="http://192.168.1.150/promotionimg/high/960_360/";
	public static final String Promotion_Img_Path_L ="http://192.168.1.150/promotionimg/low/960_360/";*/
	public static String autoischeck="false";//�ж��Ƿ����Զ��Ż�
	public static String Img_Quality="low";//�ж�������ͼƬ������
	public static int Net_Type=0;

	// ��ȨKey
	// �����ַ��http://developer.baidu.com/map/android-mobile-apply-key.htm
	// �ٶ�MapAPI�Ĺ�����
    public BMapManager mBMapManager = null;
    // ��ȨKey��ȷ����֤ͨ��
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
		
		// ������־���ɷ���
		/*Intent logServiceIntent = new Intent(getApplicationContext(), LogService.class); 
		startService(logServiceIntent);*/
		
		Log.v("searchHealthAPP", "onCreate");
				
		httpClient = this.createHttpClient();
		
		// ��ʼ���û������ļ�
		initUserConfig();
		//��ʼ��AppConfig
		initAppConfig();
		//��ʼ��֪ͨ״̬
		initNoticeStateConfig();
		
		// ��ʼ��httpClient	
		//Log.i("chenlinwei", this.getFilesDir() + "");
		//�뽫���̵� Application ��̳� FrontiaApplication �࣬�� onCreate �����м���:	super.onCreate()����������
		super.onCreate();//
		
		// �����쳣��������
		CrashHandler crashHandler = CrashHandler.getInstance();  
        crashHandler.init(getApplicationContext());
	}

	// �����¼���������������ͨ�������������Ȩ��֤�����
	public static class MyGeneralListener implements MKGeneralListener {
		
		 @Override
	        public void onGetNetworkState(int iError) {
	            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
	                Toast.makeText(searchApp.getInstance().getApplicationContext(), "���������������",
	                    Toast.LENGTH_LONG).show();
	            }
	            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
	                Toast.makeText(searchApp.getInstance().getApplicationContext(), "������ȷ�ļ���������",
	                        Toast.LENGTH_LONG).show();
	            }
	            // ...
	        }

	        @Override
	        public void onGetPermissionState(int iError) {
	        	//����ֵ��ʾkey��֤δͨ��
	            if (iError != 0) {
	                //��ȨKey����
	                /*Toast.makeText(searchApp.getInstance().getApplicationContext(), 
	                        "����������ȷ����ȨKey,������������������Ƿ�������error: "+iError, Toast.LENGTH_LONG).show();*/
	                searchApp.getInstance().m_bKeyRight = false;
	            }
	            else{
	            	searchApp.getInstance().m_bKeyRight = true;
	            	/*Toast.makeText(searchApp.getInstance().getApplicationContext(), 
	                        "key��֤�ɹ�", Toast.LENGTH_LONG).show();*/
	            }
	        }		
	}

	@Override
	// ��������app���˳�֮ǰ����mapadpi��destroy()�����������ظ���ʼ��������ʱ������
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
	 * ��ʼ���û������ļ�
	 */
	public void initUserConfig() {
		// Toast.makeText(getBaseContext(), "�Զ���¼", 1).show();
		User.initialization(getBaseContext());
	}
	
	public void initAppConfig()
	{
		AppConfig appconfig=new AppConfig(getBaseContext());
		autoischeck=appconfig.getValue("Is_2G3G_AutoLoadImage");						
		Img_Quality=appconfig.getValue("Img_Quality");//ͼƬ��������	
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

	// �ر����ӹ��������ͷ���Դ
	private void shutdownHttpClient() {
		if (httpClient != null && httpClient.getConnectionManager() != null) {
			httpClient.getConnectionManager().shutdown();
		}
	}

	// ����HttpClientʵ��
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

	// �����ṩHttpClientʵ��
	public HttpClient getHttpClient() {
		return httpClient;
	}

	// �����˳��ķ���

	private List<Activity> activityList = new LinkedList<Activity>();

	// ����ģʽ�л�ȡΨһ��MyApplicationʵ��
	public static searchApp getInstance() {
		if (mInstance == null) {
			mInstance = new searchApp();
		}
		return mInstance;
	}

	// ���Activity��������
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
		
		// �˳�ʱ���֪ͨ��
		NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.cancelAll();

    	// ��������Activity��finish
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
                    "BMapManager  ��ʼ������!", Toast.LENGTH_LONG).show();
        }
	}
}
