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
	 * private String currentFilePath = ""; // 预安zhua private String fileEx =
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
		mTabHost.setup();// 创建tabhost
	}

	/**
	 * 初始化pushconfig
	 */
	private void InitPushConfig() {		
		/**
		 * baidupush相关的操作
		 */
		Resources resource = this.getResources();
		String pkgName = this.getPackageName();		
		// 以apikey的方式登录，一般放在主Activity的onCreate中
		PushManager.startWork(getApplicationContext(),PushConstants.LOGIN_TYPE_API_KEY,Util.getMetaValue(ConMain.this, "api_key"));
		//设置自定义的通知样式，如果想使用系统默认的可以不加这段代码
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
		 * 准备tags的过程分为以下步骤
		 * 1，是否登录，如果登录：获取药店store标签；没有登录：设置默认标签
		 * 2,检查是否自动登录:自动登录则登录成功之后再设置tag,不自动登录则设置默认tag
		 */	
		Boolean isAutoLoginAble = User.getAutoLoginAble(getBaseContext());
		Toast.makeText(getBaseContext(), "是否自动登录？"+isAutoLoginAble+"",1 );
		Log.i("chenlinwei","是否自动登录？"+isAutoLoginAble+"" );
//		if (!isAutoLoginAble) {//如果不自动登录
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
		 * 探测SQLite数据库操作 .penaltyLog() // 打印logcat .penaltyDeath().build());
		 */
		StrictModeWrapper.init(getBaseContext());
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为退出做准备
		searchApp.getInstance().addActivity(this);
		/*
		 * requestWindowFeature(Window.FEATURE_LEFT_ICON);
		 * setContentView(R.layout.conmain);
		 * getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
		 * R.drawable.ic_launcher);
		 */
		// 这中方法只是针对所有的tab中title设置
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); // 注意顺序
		setContentView(R.layout.conmain); // 注意顺序
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.title);
	
		///////////////////////////////////////////////////////////////////////////////////////////////////////	
		mConMain = this;//
		// save();//保存版本信息	
		setupTabHost();
		
		tabs = new HashMap<String, View>();

		Intent intent = new Intent(ConMain.this, DrugProduct.class);
		setUpTab(new TextView(this), "药品", R.drawable.ic_tab_drug, intent);

		/*Intent intent = new Intent(ConMain.this, News.class);
		setUpTab(new TextView(this), "资讯", R.drawable.ic_tab_news, intent);*/

		/*intent = new Intent(ConMain.this, DrugStore.class);
		setUpTab(new TextView(this), "药店", R.drawable.ic_tab_drugstore, intent);*/

		intent = new Intent(ConMain.this, BDrugStore.class);
		setUpTab(new TextView(this), "药店", R.drawable.ic_tab_drugstore, intent);

		intent = new Intent(ConMain.this, DrugDisease.class);
		setUpTab(new TextView(this), "疾病", R.drawable.ic_tab_disease, intent);

		intent = new Intent(ConMain.this, Member.class);
		setUpTab(new TextView(this), "会员", R.drawable.ic_tab_member, intent);

		intent = new Intent(ConMain.this, More.class);
		setUpTab(new TextView(this), "更多", R.drawable.ic_tab_more, intent);		
		// 检查更新
		UpdateApp update = new UpdateApp(this);
		update.checkupdate();
		
		InitPushConfig();
		// 启动自动监听，上传位置及自动登录
		autoRunSpanTimer.schedule(autoRunTask, 1000);

		// 显示新功能New图标
		/*String healthReport_IsVisited = NoticeStateConfig.getValue(this,
				NoticeStateConfig.HealthReport_IsVisited);
		if (healthReport_IsVisited.equals("0")) {
			setNewFunction("会员", true);
		}*/
		
		String dissertation_IsVisited = NoticeStateConfig.getValue(this,
				NoticeStateConfig.Dissertation_IsVisited);
		String news_IsVisited = NoticeStateConfig.getValue(this,
				NoticeStateConfig.News_IsVisited);
		if (dissertation_IsVisited.equals("0")||news_IsVisited.equals("0")) {
			//setNewFunction("更多", true);
		} else {
			String dissertation_HasNew = NoticeStateConfig.getValue(this,
					NoticeStateConfig.Dissertation_HasNew);			
			String news_hasNew=NoticeStateConfig.getNewsAllState(this);			
			if (dissertation_HasNew.equals("1")||news_hasNew.equals("1")) {
				setNewNotice("更多", true);
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
	 *            需要设置的view
	 * @param tag
	 *            显示的文本值
	 * @param drawable
	 *            使用的图片
	 * @param intent
	 *            intent
	 */
	private void setUpTab(final View view, final String tag, int drawable,
			Intent intent) {
		View tabview = createTabView(mTabHost.getContext(), tag, drawable);
		// tabspec是把tab区隔开，通过一定的样式
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
	 * 创建tab的view
	 * 
	 * @param context
	 *            传入的上下文对像
	 * @param text
	 *            文本显示的内容
	 * @param drawable
	 *            ico显示的图片
	 * @return
	 */
	private View createTabView(Context context, String text, int drawable) {
		// TODO Auto-generated method stub
		// 相当于把xml样式中装入值
		View view = LayoutInflater.from(context).inflate(R.layout.tab_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		ImageView iv = (ImageView) view.findViewById(R.id.icon);
		iv.setImageResource(drawable);
		return view;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "意见反馈").setIcon(
				R.drawable.menu_feedback_ico);
		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "检测更新").setIcon(
				R.drawable.menu_update_ico);
		menu.add(Menu.NONE, Menu.FIRST + 3, 3, "设置").setIcon(
				R.drawable.menu_setting_ico);
		menu.add(Menu.NONE, Menu.FIRST + 4, 4, "退出").setIcon(
				R.drawable.menu_logout_ico);		
		setMenuBackground();		
		// setIcon()方法为菜单设置图标，这里使用的是系统自带的图标，同学们留意一下,以
		// android.R开头的资源是系统提供的，我们自己提供的资源是以R开头的
		// downLoadFile("http://gdown.baidu.com/data/wisegame/82272d43548dbe25/shuiguorenzhezhongwen.apk");
		return true;
	}
	
	//设置menu菜单的背景
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
									// R.drawable.menu_backg);//设置背景图片
									view.setBackgroundColor(Color.BLACK);// 设置背景色
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
		// 解析Version网页，获取版本号
		// NowVersion = getVersionxml(VersionUri);// 获得的是版本号的xml文档

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
			 * pBarcheck.setMessage("正在检查版本信息！请稍等");
			 * pBarcheck.setIndeterminate(true); pBarcheck.setCancelable(true);
			 * pBarcheck.show();
			 */
			// 装载获取当前的版本号
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
					.setTitle("确定退出")
					.setMessage("确定退出程序啊？")
					.setIcon(R.drawable.ic_launcher)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									searchApp.getInstance().exit();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							}).
					/*
					 * setNeutralButton("查看详情", new
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
    
    // 异步自动运行的任务
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

				// 自动登录
				if (isAutoLoginAble && !(User.IsLogged)) {
					// Toast.makeText(getBaseContext(), "开始自动登录", 1).show();
					new AsyncAutoLogin().execute();
				}
                 
				// 上传定位位置
				if (isUploadedLocation.equals("false")) {
					UpdateLocation uLocation = new UpdateLocation(
							getBaseContext());
					uLocation.SubmitFirstLocation();
				}

				autoRunTask.cancel();
				autoRunTask = new AutoRunTimerTask();
				//根据监听次数延长监听周期
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
	 * 异步自动登录
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
		// 处理界面
		protected void onPostExecute(Integer result) {

			if (result == 1) {
				// 更新会员中心界面
				Member iMember = Member.getInstance();
				if (iMember != null) {
					iMember.checkIsAutoLogin(User.IsLogged);
					
				}
				//如果登录成功则进行相关操作,则进行再一次的检查tag设置
			/*	*//**
				 * 准备tags的过程分为以下步骤
				 * 1，是否登录，如果登录：获取药店store标签；没有登录：不设置标签
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
	 * 取得远程文件 private void getDataSource(String strPath) throws Exception { if
	 * (!URLUtil.isNetworkUrl(strPath)) { Log.d("Tag", "error"); } else {
	 * 
	 * 取得URL URL myURL = new URL(strPath); 建立联机 URLConnection conn =
	 * myURL.openConnection(); conn.connect();
	 * 
	 * InputStream 下载文件 InputStream is = httpURI.getStreamFromURL(strPath); if
	 * (is == null) { Log.d("tag", "error"); throw new
	 * RuntimeException("stream is null"); } 建立临时文件 File myTempFile =
	 * File.createTempFile(fileNa, "." + fileEx); myTempFile.getAbsolutePath();
	 * 将文件写入临时盘 FileOutputStream fos = new FileOutputStream(myTempFile); byte
	 * buf[] = new byte[128]; do { int numread = is.read(buf); if (numread <= 0)
	 * { break; } fos.write(buf, 0, numread); } while (true);
	 * 
	 * 打开文件进行安装 openFile(myTempFile); // openFile(c); try { is.close(); } catch
	 * (Exception ex) { Log.d("Tag", "error"); Log.e(TAG, "error: " +
	 * ex.getMessage(), ex); } } }
	 * 
	 * 在手机上打开文件的method private void openFile(File f) { pBar.cancel(); Intent
	 * intent = new Intent(); intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	 * intent.setAction(android.content.Intent.ACTION_VIEW);
	 * 
	 * 调用getMIMEType()来取得MimeType String type = getMIMEType(f);
	 * 设定intent的file与MimeType intent.setDataAndType(Uri.fromFile(f), type);
	 * startActivity(intent); }
	 * 
	 * 判断文件MimeType的method private String getMIMEType(File f) { String type =
	 * ""; String fName = f.getName(); 取得扩展名 String end = fName
	 * .substring(fName.lastIndexOf(".") + 1, fName.length()) .toLowerCase();
	 * 
	 * 按扩展名的类型决定MimeType if (end.equals("m4a") || end.equals("mp3") ||
	 * end.equals("mid") || end.equals("xmf") || end.equals("ogg") ||
	 * end.equals("wav")) { type = "audio"; } else if (end.equals("3gp") ||
	 * end.equals("mp4")) { type = "video"; } else if (end.equals("jpg") ||
	 * end.equals("gif") || end.equals("png") || end.equals("jpeg") ||
	 * end.equals("bmp")) { type = "image"; } else if (end.equals("apk")) {
	 * android.permission.INSTALL_PACKAGES type =
	 * "application/vnd.android.package-archive"; } else { type = "*"; }
	 * 如果无法直接打开，就跳出软件清单给使用者选择 if (end.equals("apk")) { } else { type += "/*"; }
	 * return type; }
	 *//**
	 * 删除前文件
	 * 
	 * @param strFileName
	 */
	/*
	 * private void delFile(String strFileName) { File myFile = new
	 * File(strFileName); if (myFile.exists()) { myFile.delete(); } }
	 *//**
	 * 返回当前程序版本名
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
	 * 获取现在使用的版本号
	 * 
	 * @return
	 */
	/*
	 * boolean load() { Properties properties = new Properties(); try {
	 * FileInputStream stream = this.openFileInput("Versionfile.cfg"); // 读取文件内容
	 * properties.load(stream); } catch (FileNotFoundException e) { return
	 * false; } catch (IOException e) { return false; } PastVersion =
	 * String.valueOf(properties.get("Version").toString()); // 得到版本号
	 * Toast.makeText(this, PastVersion, 1).toString(); return true; }
	 */

	/**
	 * 保存现在的版本号
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
	 * = 0; try { // 加载数据 if(params[0].length()>0) // model= //
	 * IntegralDataServiceHelper.GetRank(params[0],ProjectConstant.AppID); //
	 * list= IntegralDataServiceHelper.GetTopList(0, //
	 * 10,ProjectConstant.AppID);
	 *//**
	 * 查询最新版本信息，包含verno,vername,updatedescr,updateurl键值。
	 */
	/*
	 * lastVersion = AppVersion.getLastVersion(); if(lastVersion==null) { result
	 * = -1; } else { result = 2; } } catch (Exception ex) { result = -1; }
	 * return result; }
	 * 
	 * @Override // 处理界面 protected void onPostExecute(Integer result) {
	 * Log.i("ExerciseGuess", "onPostExecute(Result result) called");
	 * 
	 * if (result == 2) { pBarcheck.cancel(); LastVersion =
	 * lastVersion.get("verno");// 获取版本号 String
	 * lastVersionname=lastVersion.get("vername");//获取版本全名称 strURL
	 * =lastVersion.get("updateurl"); Toast.makeText(ConMain.this,
	 * "服务器端版本"+NowVersion, Toast.LENGTH_LONG) .show(); String nowVersionName =
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
	 * ) .setTitle("检测到新版本") .setView(EntryView) //.setMessage("发现新版本，请更新！") //
	 * 设置内容 .setPositiveButton("马上更新",// 设置确定按钮 new
	 * DialogInterface.OnClickListener() { public void onClick( DialogInterface
	 * dialog, int which) { pBar = new ProgressDialog( ConMain.this);
	 * pBar.setTitle("正在下载"); pBar.setMessage("请稍候...");
	 * pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER); fileEx = strURL
	 * .substring( strURL.lastIndexOf(".") + 1, strURL.length()) .toLowerCase();
	 * fileNa = strURL.substring( strURL.lastIndexOf("/") + 1,
	 * strURL.lastIndexOf(".")); getFile(strURL); } })
	 * .setNegativeButton("稍后更新", new DialogInterface.OnClickListener() { public
	 * void onClick( DialogInterface dialog, int whichButton) { //
	 * 点击"取消"按钮之后退出程序 } }).create();// 创建 //dialog. // 显示对话框 dialog.show(); }
	 * 
	 * else { //save(); Toast.makeText(ConMain.this, "当前为最新版本,不用更新",
	 * Toast.LENGTH_LONG) .show(); }
	 * 
	 * } else { Toast.makeText(ConMain.this, "链接服务器不成功", 1).show(); } } }
	 */

	/*
	 * private long exitTime = 0;
	 * 
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
	 * if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() ==
	 * KeyEvent.ACTION_DOWN){ if((System.currentTimeMillis()-exitTime) > 2000){
	 * Toast.makeText(getApplicationContext(), "再按一次退出程序",
	 * Toast.LENGTH_SHORT).show(); exitTime = System.currentTimeMillis(); } else
	 * { //Log.i(TAG, "退出"); finish(); System.exit(0); } return true; } return
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
	 * Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
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
