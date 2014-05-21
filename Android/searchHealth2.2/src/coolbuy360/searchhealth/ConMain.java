package coolbuy360.searchhealth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

import coolbuy360.logic.AppConfig;
import coolbuy360.logic.NoticeStateConfig;
import coolbuy360.logic.PushConfig;
import coolbuy360.logic.User;
import coolbuy360.searchhealth.MemberLogin.loginbtnOnClick;
import coolbuy360.searchhealth.R.string;
import coolbuy360.service.StrictModeWrapper;
import coolbuy360.service.UpdateApp;
import coolbuy360.service.UpdateLocation;
import coolbuy360.service.Util;
import coolbuy360.service.searchApp;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.os.StrictMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.ViewGroup.LayoutParams;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class ConMain extends TabActivity {

	private TabHost mTabHost;
	public static ConMain mConMain;//

	private Map<String, View> tabs;
	private static final String TAG = "DOWNLOADAPK";
	/** Called when the activity is first created. */
	// private String PastVersion;
	/* private String LastVersion = "1"; */
	public ProgressDialog pBar;
	/*
	 * private String currentFilePath = ""; // Ԥ��zhua private String fileEx =
	 * ""; private String fileNa = ""; private String strURL =
	 * "http://api.wcyy.cn:8080/apk/searchhealth.apk";
	 */
	/*
	 * private String VersionUri =
	 * "http://192.168.68.120/VersionService.asmx/GetVersion";
	 */

	public ProgressDialog pBarcheck;

	private void setupTabHost() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();// ����tabhost
	}

	/**
	 * ��ʼ��pushconfig
	 */
	private void InitPushConfig() {		
		/**
		 * baidupush��صĲ���
		 */
		Resources resource = this.getResources();
		String pkgName = this.getPackageName();		
		// ��apikey�ķ�ʽ��¼��һ�������Activity��onCreate��
		PushManager.startWork(getApplicationContext(),PushConstants.LOGIN_TYPE_API_KEY,Util.getMetaValue(ConMain.this, "api_key"));
		//�����Զ����֪ͨ��ʽ�������ʹ��ϵͳĬ�ϵĿ��Բ�����δ���
		CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
				getApplicationContext(),
				resource.getIdentifier("notification_custom_builder", "layout", pkgName), 
				resource.getIdentifier("notification_icon", "id", pkgName), 
				resource.getIdentifier("notification_titl	e", "id", pkgName), 
				resource.getIdentifier("notification_text", "id", pkgName));
        cBuilder.setNotificationFlags(Notification.FLAG_NO_CLEAR);
        cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
        cBuilder.setLayoutDrawable(resource.getIdentifier("simple_notification_icon", "drawable", pkgName));
		PushManager.setNotificationBuilder(this, 1, cBuilder);	
		
		/**
		 * ׼��tags�Ĺ��̷�Ϊ���²���
		 * 1���Ƿ��¼�������¼����ȡҩ��store��ǩ��û�е�¼������Ĭ�ϱ�ǩ
		 * 2,����Ƿ��Զ���¼:�Զ���¼���¼�ɹ�֮��������tag,���Զ���¼������Ĭ��tag
		 */	
		Boolean isAutoLoginAble = User.getAutoLoginAble(getBaseContext());
		Toast.makeText(getBaseContext(), "�Ƿ��Զ���¼��"+isAutoLoginAble+"",1 );
		Log.i("chenlinwei","�Ƿ��Զ���¼��"+isAutoLoginAble+"" );
//		if (!isAutoLoginAble) {//������Զ���¼
//			PushConfig.tagHandle(getApplicationContext());			
//		}		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		/*
		 * StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		 * .detectDiskReads().detectDiskWrites().detectNetwork() //
		 * .penaltyLog().build()); StrictMode.setVmPolicy(new
		 * StrictMode.VmPolicy.Builder() .detectLeakedSqlLiteObjects() //
		 * ̽��SQLite���ݿ���� .penaltyLog() // ��ӡlogcat .penaltyDeath().build());
		 */
		StrictModeWrapper.init(getBaseContext());
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Ϊ�˳���׼��
		searchApp.getInstance().addActivity(this);
		/*
		 * requestWindowFeature(Window.FEATURE_LEFT_ICON);
		 * setContentView(R.layout.conmain);
		 * getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
		 * R.drawable.ic_launcher);
		 */
		// ���з���ֻ��������е�tab��title����
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); // ע��˳��
		setContentView(R.layout.conmain); // ע��˳��
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.title);
	
		///////////////////////////////////////////////////////////////////////////////////////////////////////	
		mConMain = this;//
		// save();//����汾��Ϣ	
		setupTabHost();
		
		tabs = new HashMap<String, View>();

		Intent intent = new Intent(ConMain.this, DrugProduct.class);
		setUpTab(new TextView(this), "ҩƷ", R.drawable.ic_tab_drug, intent);

		/*Intent intent = new Intent(ConMain.this, News.class);
		setUpTab(new TextView(this), "��Ѷ", R.drawable.ic_tab_news, intent);*/

		/*intent = new Intent(ConMain.this, DrugStore.class);
		setUpTab(new TextView(this), "ҩ��", R.drawable.ic_tab_drugstore, intent);*/

		intent = new Intent(ConMain.this, BDrugStore.class);
		setUpTab(new TextView(this), "ҩ��", R.drawable.ic_tab_drugstore, intent);

		intent = new Intent(ConMain.this, DrugDisease.class);
		setUpTab(new TextView(this), "����", R.drawable.ic_tab_disease, intent);

		intent = new Intent(ConMain.this, Member.class);
		setUpTab(new TextView(this), "��Ա", R.drawable.ic_tab_member, intent);

		intent = new Intent(ConMain.this, More.class);
		setUpTab(new TextView(this), "����", R.drawable.ic_tab_more, intent);		
		// ������
		UpdateApp update = new UpdateApp(this);
		update.checkupdate();
		
		InitPushConfig();
		// �����Զ��������ϴ�λ�ü��Զ���¼
		autoRunSpanTimer.schedule(autoRunTask, 1000);

		// ��ʾ�¹���Newͼ��
		/*String healthReport_IsVisited = NoticeStateConfig.getValue(this,
				NoticeStateConfig.HealthReport_IsVisited);
		if (healthReport_IsVisited.equals("0")) {
			setNewFunction("��Ա", true);
		}*/
		
		String dissertation_IsVisited = NoticeStateConfig.getValue(this,
				NoticeStateConfig.Dissertation_IsVisited);
		String news_IsVisited = NoticeStateConfig.getValue(this,
				NoticeStateConfig.News_IsVisited);
		if (dissertation_IsVisited.equals("0")||news_IsVisited.equals("0")) {
			//setNewFunction("����", true);
		} else {
			String dissertation_HasNew = NoticeStateConfig.getValue(this,
					NoticeStateConfig.Dissertation_HasNew);			
			String news_hasNew=NoticeStateConfig.getNewsAllState(this);			
			if (dissertation_HasNew.equals("1")||news_hasNew.equals("1")) {
				setNewNotice("����", true);
			}
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		PushManager.activityStoped(this);
	}	
	@Override
	public void onStart() {
		super.onStart();
		PushManager.activityStarted(this);
	}	
	
	/**
	 * @param view
	 *            ��Ҫ���õ�view
	 * @param tag
	 *            ��ʾ���ı�ֵ
	 * @param drawable
	 *            ʹ�õ�ͼƬ
	 * @param intent
	 *            intent
	 */
	private void setUpTab(final View view, final String tag, int drawable,
			Intent intent) {
		View tabview = createTabView(mTabHost.getContext(), tag, drawable);
		// tabspec�ǰ�tab��������ͨ��һ������ʽ
		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview)
				.setContent(intent);
		mTabHost.addTab(setContent);
		tabs.put(tag, tabview);
	}
	
	public void setNewNotice(String key, Boolean value) {
		View tabview = tabs.get(key);
		if (tabview != null) {
			ImageView ico_newfunction = (ImageView) tabview
					.findViewById(R.id.tab_main_ico_newfunction);
			if (ico_newfunction != null) {
				ImageView ico_newnotice = (ImageView) tabview
						.findViewById(R.id.tab_main_ico_newnotice);
				if (ico_newfunction.getVisibility() == View.GONE) {
					if (value) {
						ico_newnotice.setVisibility(View.VISIBLE);
					} else {
						ico_newnotice.setVisibility(View.GONE);
					}
				} else {
					ico_newnotice.setVisibility(View.GONE);
				}
			}
		}
	}
	
	public void setNewFunction(String key, Boolean value) {
		View tabview = tabs.get(key);
		if (tabview != null) {
			ImageView ico_newfunction = (ImageView) tabview
					.findViewById(R.id.tab_main_ico_newfunction);
			if (ico_newfunction != null) {
				if (value) {
					ico_newfunction.setVisibility(View.VISIBLE);
				} else {
					ico_newfunction.setVisibility(View.GONE);
				}
			}
		}
	}

	/**
	 * ����tab��view
	 * 
	 * @param context
	 *            ����������Ķ���
	 * @param text
	 *            �ı���ʾ������
	 * @param drawable
	 *            ico��ʾ��ͼƬ
	 * @return
	 */
	private View createTabView(Context context, String text, int drawable) {
		// TODO Auto-generated method stub
		// �൱�ڰ�xml��ʽ��װ��ֵ
		View view = LayoutInflater.from(context).inflate(R.layout.tab_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		ImageView iv = (ImageView) view.findViewById(R.id.icon);
		iv.setImageResource(drawable);
		return view;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "�������").setIcon(
				R.drawable.menu_feedback_ico);
		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "������").setIcon(
				R.drawable.menu_update_ico);
		menu.add(Menu.NONE, Menu.FIRST + 3, 3, "����").setIcon(
				R.drawable.menu_setting_ico);
		menu.add(Menu.NONE, Menu.FIRST + 4, 4, "�˳�").setIcon(
				R.drawable.menu_logout_ico);		
		setMenuBackground();		
		// setIcon()����Ϊ�˵�����ͼ�꣬����ʹ�õ���ϵͳ�Դ���ͼ�꣬ͬѧ������һ��,��
		// android.R��ͷ����Դ��ϵͳ�ṩ�ģ������Լ��ṩ����Դ����R��ͷ��
		// downLoadFile("http://gdown.baidu.com/data/wisegame/82272d43548dbe25/shuiguorenzhezhongwen.apk");
		return true;
	}
	
	//����menu�˵��ı���
	protected void setMenuBackground() {

		Log.d(TAG, "Enterting setMenuBackGround");
		try {
			getLayoutInflater().setFactory(new Factory() {

				@Override
				public View onCreateView(String name, Context context,
						AttributeSet attrs) {
					// TODO Auto-generated method stub
					if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {

						try { // Ask our inflater to create the view
							LayoutInflater f = getLayoutInflater();
							final View view = f.createView(name, null, attrs);
							new Handler().post(new Runnable() {
								public void run() {
									// view.setBackgroundResource(
									// R.drawable.menu_backg);//���ñ���ͼƬ
									view.setBackgroundColor(Color.BLACK);// ���ñ���ɫ
								}
							});
							return view;
						} catch (InflateException e) {
						} catch (ClassNotFoundException e) {
						}
					}
					return null;
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// ����Version��ҳ����ȡ�汾��
		// NowVersion = getVersionxml(VersionUri);// ��õ��ǰ汾�ŵ�xml�ĵ�

		int itemId = item.getItemId();

		switch (itemId) {
		case 2:
			Intent feedbackIntent = new Intent().setClass(this, Feedback.class);
			startActivity(feedbackIntent);
			break;
		case 3:

			/*
			 * pBarcheck= new ProgressDialog(this);
			 * //dialog.setTitle("Indeterminate");
			 * pBarcheck.setMessage("���ڼ��汾��Ϣ�����Ե�");
			 * pBarcheck.setIndeterminate(true); pBarcheck.setCancelable(true);
			 * pBarcheck.show();
			 */
			// װ�ػ�ȡ��ǰ�İ汾��
			// load();
			// showDialog(id)
			UpdateApp update = new UpdateApp(this);
			update.startupdate();

			break;
		case 4:
			Intent pageIntent = new Intent().setClass(this, Settings.class);
			startActivity(pageIntent);
			break;
		case 5:
			Dialog alertDialog = new AlertDialog.Builder(this)
					.setTitle("ȷ���˳�")
					.setMessage("ȷ���˳����򰡣�")
					.setIcon(R.drawable.ic_launcher)
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									searchApp.getInstance().exit();
								}
							})
					.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							}).
					/*
					 * setNeutralButton("�鿴����", new
					 * DialogInterface.OnClickListener() {
					 * 
					 * @Override public void onClick(DialogInterface dialog, int
					 * which) { // TODO Auto-generated method stub } }).
					 */
					create();
			alertDialog.show();

			break;
		default:
			break;
		}

		return false;
	}
	
    Handler handler = new Handler(){   
        public void handleMessage(Message msg) {  
            switch (msg.what) {      
            case 1:      
        		//Log.i("handler", aaaaaa+"");
                break;      
            }      
            super.handleMessage(msg);  
        } 
    };

    int looptimes = 1;
	Timer autoRunSpanTimer = new Timer();  
    TimerTask autoRunTask = new AutoRunTimerTask();
    
    // �첽�Զ����е�����
    private class AutoRunTimerTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub    		
			String isUploadedLocation = AppConfig.getValue(
					getBaseContext(), AppConfig.IsUploaded_LocationAddress);
    		Boolean isAutoLoginAble = User.getAutoLoginAble(getBaseContext());
			if ((isAutoLoginAble && !(User.IsLogged))
					|| isUploadedLocation.equals("false")) {
				Log.i("timer", looptimes + "");
				looptimes ++;
				/*Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);*/

				// �Զ���¼
				if (isAutoLoginAble && !(User.IsLogged)) {
					// Toast.makeText(getBaseContext(), "��ʼ�Զ���¼", 1).show();
					new AsyncAutoLogin().execute();
				}
                 
				// �ϴ���λλ��
				if (isUploadedLocation.equals("false")) {
					UpdateLocation uLocation = new UpdateLocation(
							getBaseContext());
					uLocation.SubmitFirstLocation();
				}

				autoRunTask.cancel();
				autoRunTask = new AutoRunTimerTask();
				//���ݼ��������ӳ���������
				if(looptimes > 130) {
					autoRunSpanTimer.schedule(autoRunTask, 300000);
				} else if(looptimes > 120) {
					autoRunSpanTimer.schedule(autoRunTask, 60000);					
				} else if(looptimes > 100) {
					autoRunSpanTimer.schedule(autoRunTask, 30000);					
				} else {
					autoRunSpanTimer.schedule(autoRunTask, 5000);						
				}
			} else {
				autoRunTask.cancel();
			}
		} 	
    }
	
	/**
	 * �첽�Զ���¼
	 */
	public class AsyncAutoLogin extends AsyncTask<String, Void, Integer> {
		Boolean loginstat = false;

		@Override
		protected Integer doInBackground(String... params) {
			try {
				User user = new User(getBaseContext());
				Boolean loginstat = user.login();
				if(loginstat) {
					return 1;
				} else {
					return 0;
				}
			} catch (Exception ex) {
				return 0;
			}
		}

		@Override
		// �������
		protected void onPostExecute(Integer result) {

			if (result == 1) {
				// ���»�Ա���Ľ���
				Member iMember = Member.getInstance();
				if (iMember != null) {
					iMember.checkIsAutoLogin(User.IsLogged);
					
				}
				//�����¼�ɹ��������ز���,�������һ�εļ��tag����
			/*	*//**
				 * ׼��tags�Ĺ��̷�Ϊ���²���
				 * 1���Ƿ��¼�������¼����ȡҩ��store��ǩ��û�е�¼�������ñ�ǩ
				 *//*		
				 PushConfig.tagHandle(getApplicationContext());*/
				PushConfig.tagHandle(getApplicationContext());	
			}
			else {
			}
		}
	}

	public static int getDisplayWidth()
	{
		return mConMain.getWindowManager().getDefaultDisplay().getWidth();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	/*
	 * private void getFile(final String strPath) { pBar.show(); try { if
	 * (strPath.equals(currentFilePath)) { getDataSource(strPath); }
	 * currentFilePath = strPath; Runnable r = new Runnable() { public void
	 * run() { try { getDataSource(strPath); } catch (Exception e) { Log.e(TAG,
	 * e.getMessage(), e); } } }; new Thread(r).start(); } catch (Exception e) {
	 * e.printStackTrace(); } }
	 * 
	 * ȡ��Զ���ļ� private void getDataSource(String strPath) throws Exception { if
	 * (!URLUtil.isNetworkUrl(strPath)) { Log.d("Tag", "error"); } else {
	 * 
	 * ȡ��URL URL myURL = new URL(strPath); �������� URLConnection conn =
	 * myURL.openConnection(); conn.connect();
	 * 
	 * InputStream �����ļ� InputStream is = httpURI.getStreamFromURL(strPath); if
	 * (is == null) { Log.d("tag", "error"); throw new
	 * RuntimeException("stream is null"); } ������ʱ�ļ� File myTempFile =
	 * File.createTempFile(fileNa, "." + fileEx); myTempFile.getAbsolutePath();
	 * ���ļ�д����ʱ�� FileOutputStream fos = new FileOutputStream(myTempFile); byte
	 * buf[] = new byte[128]; do { int numread = is.read(buf); if (numread <= 0)
	 * { break; } fos.write(buf, 0, numread); } while (true);
	 * 
	 * ���ļ����а�װ openFile(myTempFile); // openFile(c); try { is.close(); } catch
	 * (Exception ex) { Log.d("Tag", "error"); Log.e(TAG, "error: " +
	 * ex.getMessage(), ex); } } }
	 * 
	 * ���ֻ��ϴ��ļ���method private void openFile(File f) { pBar.cancel(); Intent
	 * intent = new Intent(); intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	 * intent.setAction(android.content.Intent.ACTION_VIEW);
	 * 
	 * ����getMIMEType()��ȡ��MimeType String type = getMIMEType(f);
	 * �趨intent��file��MimeType intent.setDataAndType(Uri.fromFile(f), type);
	 * startActivity(intent); }
	 * 
	 * �ж��ļ�MimeType��method private String getMIMEType(File f) { String type =
	 * ""; String fName = f.getName(); ȡ����չ�� String end = fName
	 * .substring(fName.lastIndexOf(".") + 1, fName.length()) .toLowerCase();
	 * 
	 * ����չ�������;���MimeType if (end.equals("m4a") || end.equals("mp3") ||
	 * end.equals("mid") || end.equals("xmf") || end.equals("ogg") ||
	 * end.equals("wav")) { type = "audio"; } else if (end.equals("3gp") ||
	 * end.equals("mp4")) { type = "video"; } else if (end.equals("jpg") ||
	 * end.equals("gif") || end.equals("png") || end.equals("jpeg") ||
	 * end.equals("bmp")) { type = "image"; } else if (end.equals("apk")) {
	 * android.permission.INSTALL_PACKAGES type =
	 * "application/vnd.android.package-archive"; } else { type = "*"; }
	 * ����޷�ֱ�Ӵ򿪣�����������嵥��ʹ����ѡ�� if (end.equals("apk")) { } else { type += "/*"; }
	 * return type; }
	 *//**
	 * ɾ��ǰ�ļ�
	 * 
	 * @param strFileName
	 */
	/*
	 * private void delFile(String strFileName) { File myFile = new
	 * File(strFileName); if (myFile.exists()) { myFile.delete(); } }
	 *//**
	 * ���ص�ǰ����汾��
	 */
	/*
	 * public static String getAppVersionName(Context context) { String
	 * versionName = ""; try { // ---get the package info--- PackageManager pm =
	 * context.getPackageManager(); PackageInfo pi =
	 * pm.getPackageInfo(context.getPackageName(), 0); versionName =
	 * pi.versionName; //versioncode = pi.versionCode; if (versionName == null
	 * || versionName.length() <= 0) { return ""; } } catch (Exception e) {
	 * Log.e("VersionInfo", "Exception", e); } return versionName; }
	 */

	/**
	 * ��ȡ����ʹ�õİ汾��
	 * 
	 * @return
	 */
	/*
	 * boolean load() { Properties properties = new Properties(); try {
	 * FileInputStream stream = this.openFileInput("Versionfile.cfg"); // ��ȡ�ļ�����
	 * properties.load(stream); } catch (FileNotFoundException e) { return
	 * false; } catch (IOException e) { return false; } PastVersion =
	 * String.valueOf(properties.get("Version").toString()); // �õ��汾��
	 * Toast.makeText(this, PastVersion, 1).toString(); return true; }
	 */

	/**
	 * �������ڵİ汾��
	 * 
	 * @return
	 */
	/*
	 * boolean save() { Properties properties = new Properties();
	 * properties.put("Version", NowVersion); try { FileOutputStream stream =
	 * this.openFileOutput("Versionfile.cfg", Context.MODE_WORLD_WRITEABLE);
	 * properties.store(stream, ""); } catch (FileNotFoundException e) { return
	 * false; } catch (IOException e) { return false; } return true; }
	 */
	/*
	 * class AsyncLoader_version extends AsyncTask<String, Void, Integer> {
	 * private Map<String, String> lastVersion = null;
	 * 
	 * @Override protected Integer doInBackground(String... params) { int result
	 * = 0; try { // �������� if(params[0].length()>0) // model= //
	 * IntegralDataServiceHelper.GetRank(params[0],ProjectConstant.AppID); //
	 * list= IntegralDataServiceHelper.GetTopList(0, //
	 * 10,ProjectConstant.AppID);
	 *//**
	 * ��ѯ���°汾��Ϣ������verno,vername,updatedescr,updateurl��ֵ��
	 */
	/*
	 * lastVersion = AppVersion.getLastVersion(); if(lastVersion==null) { result
	 * = -1; } else { result = 2; } } catch (Exception ex) { result = -1; }
	 * return result; }
	 * 
	 * @Override // ������� protected void onPostExecute(Integer result) {
	 * Log.i("ExerciseGuess", "onPostExecute(Result result) called");
	 * 
	 * if (result == 2) { pBarcheck.cancel(); LastVersion =
	 * lastVersion.get("verno");// ��ȡ�汾�� String
	 * lastVersionname=lastVersion.get("vername");//��ȡ�汾ȫ���� strURL
	 * =lastVersion.get("updateurl"); Toast.makeText(ConMain.this,
	 * "�������˰汾"+NowVersion, Toast.LENGTH_LONG) .show(); String nowVersionName =
	 * getAppVersionName(getApplicationContext()); if (nowVersionName != null &&
	 * !nowVersionName.equals(LastVersion)) {
	 * 
	 * LayoutInflater factory = LayoutInflater.from(mConMain); final View
	 * EntryView = factory.inflate(R.layout.moreupdate, null); TextView
	 * versionName=(TextView)EntryView.findViewById(R.id.version_name); TextView
	 * version_size=(TextView)EntryView.findViewById(R.id.version_size);
	 * TextView
	 * version_date=(TextView)EntryView.findViewById(R.id.version_date);
	 * TextView
	 * version_decri=(TextView)EntryView.findViewById(R.id.version_decri);
	 * versionName.setText(lastVersionname);
	 * version_size.setText(lastVersion.get("filesize"));
	 * 
	 * String updatetime = ""; SimpleDateFormat formatter = new
	 * SimpleDateFormat("yyyy/MM/dd hh:mm:ss"); Date strtodate; try { strtodate
	 * = formatter.parse(lastVersion.get("updatetime")); formatter = new
	 * SimpleDateFormat("yyyy-MM-dd"); updatetime = formatter.format(strtodate);
	 * } catch (ParseException e) { // TODO Auto-generated catch block }
	 * version_date.setText(updatetime);
	 * 
	 * version_decri.setText(lastVersion.get("updatedescr")); Dialog dialog =
	 * new
	 * AlertDialog.Builder(ConMain.this).setIcon(android.R.drawable.ic_dialog_info
	 * ) .setTitle("��⵽�°汾") .setView(EntryView) //.setMessage("�����°汾������£�") //
	 * �������� .setPositiveButton("���ϸ���",// ����ȷ����ť new
	 * DialogInterface.OnClickListener() { public void onClick( DialogInterface
	 * dialog, int which) { pBar = new ProgressDialog( ConMain.this);
	 * pBar.setTitle("��������"); pBar.setMessage("���Ժ�...");
	 * pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER); fileEx = strURL
	 * .substring( strURL.lastIndexOf(".") + 1, strURL.length()) .toLowerCase();
	 * fileNa = strURL.substring( strURL.lastIndexOf("/") + 1,
	 * strURL.lastIndexOf(".")); getFile(strURL); } })
	 * .setNegativeButton("�Ժ����", new DialogInterface.OnClickListener() { public
	 * void onClick( DialogInterface dialog, int whichButton) { //
	 * ���"ȡ��"��ť֮���˳����� } }).create();// ���� //dialog. // ��ʾ�Ի��� dialog.show(); }
	 * 
	 * else { //save(); Toast.makeText(ConMain.this, "��ǰΪ���°汾,���ø���",
	 * Toast.LENGTH_LONG) .show(); }
	 * 
	 * } else { Toast.makeText(ConMain.this, "���ӷ��������ɹ�", 1).show(); } } }
	 */

	/*
	 * private long exitTime = 0;
	 * 
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
	 * if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() ==
	 * KeyEvent.ACTION_DOWN){ if((System.currentTimeMillis()-exitTime) > 2000){
	 * Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
	 * Toast.LENGTH_SHORT).show(); exitTime = System.currentTimeMillis(); } else
	 * { //Log.i(TAG, "�˳�"); finish(); System.exit(0); } return true; } return
	 * super.onKeyDown(keyCode, event); } public boolean
	 * dispatchKeyEvent(KeyEvent event) { int keyCode=event.getKeyCode();
	 * switch(keyCode) { case KeyEvent.KEYCODE_BACK: { if(event.isLongPress()) {
	 * this.stopService(intent); System.exit(0); return true; }else { boolean
	 * flag=false; return flag; } } } return super.dispatchKeyEvent(event);
	 * 
	 * }
	 */

	/*
	 * private static Boolean isExit = false;
	 * 
	 * private static Boolean hasTask = false;
	 * 
	 * Timer tExit = new Timer();
	 * 
	 * TimerTask task = new TimerTask() {
	 * 
	 * 
	 * 
	 * @Override
	 * 
	 * public void run() {
	 * 
	 * isExit = false;
	 * 
	 * hasTask = true;
	 * 
	 * }
	 * 
	 * };
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public boolean onKeyDown(int keyCode, KeyEvent event) {
	 * 
	 * // TODO Auto-generated method stub
	 * 
	 * if(keyCode == KeyEvent.KEYCODE_BACK){
	 * 
	 * // System.out.println("user back down");
	 * 
	 * if(isExit == false ) {
	 * 
	 * isExit = true;
	 * 
	 * Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
	 * 
	 * if(!hasTask) {
	 * 
	 * tExit.schedule(task, 2000);
	 * 
	 * }} else {
	 * 
	 * }
	 * 
	 * finish();
	 * 
	 * System.exit(0);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * return false;
	 * 
	 * }
	 */
}
