package coolbuy360.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import coolbuy360.receiver.InstallReceiver;
import coolbuy360.searchhealth.ConMain;
import coolbuy360.searchhealth.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class UpdateService extends Service {
	
	private final static int DOWNLOAD_COMPLETE = 0;
	private final static int DOWNLOAD_FAIL = 1;
	// 文件下载状态，1表示正在下载。
	private static int DOWNLOAD_STATE = 0;
	// 文件存储
	private File updateDir = null;
	private File updateFile = null;

	// 通知栏
	private NotificationManager updateNotificationManager = null;
	private Notification updateNotification = null;
	// 通知栏跳转Intent
	private Intent updateIntent = null;
	private PendingIntent updatePendingIntent = null;
	
	private String app_name = "健康100";
	private String updateFileSaveURL = "";
	private String updateFileDownLoadURL = "";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// 若有正在下载更新的任务，则不再新增下载任务。
		if (DOWNLOAD_STATE == 1)
			return 0;
		
		if(intent == null)
			return 0;
		
		//app_name = getResources().getString(R.string.);
		
		// 获取传值
		updateFileSaveURL = intent.getStringExtra("updateFileSaveURL");
		updateFileDownLoadURL = intent.getStringExtra("updateFileDownLoadURL");
		// 创建文件
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
				.getExternalStorageState())) {
			// updateDir = new File(Environment.getExternalStorageDirectory(),"app/download/");
			String updateDirURL = updateFileSaveURL.substring(0,
					updateFileSaveURL.lastIndexOf("/"));
			updateDir = new File(updateDirURL);
			updateFile = new File(updateFileSaveURL);
		}

		this.updateNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		this.updateNotification = new Notification();
		// this.updateNotification.flags = Notification.FLAG_ONGOING_EVENT ;

		// 设置下载过程中，点击通知栏，回到主界面
		// updateIntent = new Intent(this, ConMain.class);
		updateIntent = new Intent(Intent.ACTION_MAIN);
		updateIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		updateIntent.setComponent(new ComponentName(this.getPackageName(), this
				.getPackageName() + "." + "ConMain"));
		// 关键的一步，设置启动模式
		updateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		updatePendingIntent = PendingIntent.getActivity(this, 0, updateIntent,
				0);
		// 设置通知栏显示内容
		updateNotification.icon = R.drawable.app_ico;
		updateNotification.tickerText = app_name + "开始下载";
		updateNotification.setLatestEventInfo(this, app_name + "正在下载更新", "0%",
				updatePendingIntent);
		// 发出通知
		updateNotificationManager.notify(R.id.install_notice, updateNotification);

		// 开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
		new Thread(new updateRunnable()).start();// 这个是下载的重点，是下载的过程

		return super.onStartCommand(intent, flags, startId);
	}

	private Handler updateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) { 
			case DOWNLOAD_COMPLETE:
				updateNotification.flags |= updateNotification.FLAG_AUTO_CANCEL;

				// 点击安装PendingIntent
				/*Uri uri = Uri.fromFile(updateFile);
				Intent installIntent = new Intent(Intent.ACTION_VIEW);
				installIntent.setDataAndType(uri,
						"application/vnd.android.package-archive");
				updatePendingIntent = PendingIntent.getActivity(
						UpdateService.this, 0, installIntent, 0);*/
				 
				// 回调ConMain主界面启动安装
				/*Intent installIntent = new Intent(UpdateService.this,
						ConMain.class);
				installIntent.putExtra("isUpdateCallBack", true);
				installIntent.putExtra("downloadComplete", true);
				installIntent.putExtra("updateFileSaveURL", updateFileSaveURL);
				updatePendingIntent = PendingIntent.getActivity(
						UpdateService.this, 0, installIntent,
						PendingIntent.FLAG_UPDATE_CURRENT);*/
				
				// 回调InstallReceiver启动安装
				Intent installIntent = new Intent(UpdateService.this,
						InstallReceiver.class);
				installIntent.putExtra("isUpdateCallBack", true);
				installIntent.putExtra("downloadComplete", true);
				installIntent.putExtra("updateFileSaveURL", updateFileSaveURL);
				updatePendingIntent = PendingIntent.getBroadcast(
						UpdateService.this, 0, installIntent,
						PendingIntent.FLAG_UPDATE_CURRENT);

				// updateNotification.defaults = Notification.DEFAULT_SOUND;  //铃声提醒
				updateNotification.setLatestEventInfo(UpdateService.this,
						app_name, "下载完成,点击安装。", updatePendingIntent);
				updateNotificationManager.notify(R.id.install_notice, updateNotification);

				// 停止服务
				stopService(updateIntent);
				break;
			case DOWNLOAD_FAIL:
				// 下载失败
				updateNotification.setLatestEventInfo(UpdateService.this,
						app_name, "下载失败。", updatePendingIntent);
				updateNotificationManager.notify(0, updateNotification);
				break;
			default:
				stopService(updateIntent);
				break;
			}
		}
	};

	class updateRunnable implements Runnable {
		Message message = updateHandler.obtainMessage();

		public void run() {
			message.what = DOWNLOAD_COMPLETE;
			try {
				// 增加权限<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE">;
				if (!updateDir.exists()) {
					updateDir.mkdirs();
				}
				if (!updateFile.exists()) {
					updateFile.createNewFile();
				}

				// 下载函数
				// 增加权限<uses-permission android:name="android.permission.INTERNET">;
				long downloadSize = downloadUpdateFile(updateFileDownLoadURL,
						updateFile);
				if (downloadSize > 0) {
					// 下载成功
					DOWNLOAD_STATE = 0;
					/*
					 * runCommand("chmod 701 " + Uri.fromFile(updateDir));
					 * runCommand("chmod 604 " + Uri.fromFile(updateFile));
					 */
					updateHandler.sendMessage(message);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				DOWNLOAD_STATE = 0;
				message.what = DOWNLOAD_FAIL;
				// 下载失败
				updateHandler.sendMessage(message);
			}
		}
	}

	public long downloadUpdateFile(String downloadUrl, File saveFile)
			throws Exception {
		int downloadCount = 0;
		int currentSize = 0;
		long totalSize = 0;
		int updateTotalSize = 0;

		HttpURLConnection httpConnection = null;
		InputStream is = null;
		FileOutputStream fos = null;

		try {
			DOWNLOAD_STATE = 1;
			URL url = new URL(downloadUrl);
			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection
					.setRequestProperty("User-Agent", "PacificHttpClient");
			if (currentSize > 0) {
				httpConnection.setRequestProperty("RANGE", "bytes="
						+ currentSize + "-");
			}
			httpConnection.setConnectTimeout(10000);
			httpConnection.setReadTimeout(20000);
			updateTotalSize = httpConnection.getContentLength();
			if (httpConnection.getResponseCode() == 404) {
				throw new Exception("fail!");
			}
			is = httpConnection.getInputStream();
			fos = new FileOutputStream(saveFile, false);
			byte buffer[] = new byte[4096];
			int readsize = 0;
			while ((readsize = is.read(buffer)) > 0) {
				fos.write(buffer, 0, readsize);
				totalSize += readsize;
				// 为了防止频繁的通知导致应用吃紧，百分比增加10才通知一次
				if ((downloadCount == 0)
						|| (int) (totalSize * 100 / updateTotalSize) - 10 > downloadCount) {
					downloadCount += 10;
					updateNotification.setLatestEventInfo(UpdateService.this,
							app_name + "正在下载更新", (int) totalSize * 100 / updateTotalSize
									+ "%", updatePendingIntent);
					updateNotificationManager.notify(R.id.install_notice, updateNotification);
				}
			}
		} finally {
			if (httpConnection != null) {
				httpConnection.disconnect();
			}
			if (is != null) {
				is.close();
			}
			if (fos != null) {
				fos.close();
			}
		}

		/*// [文件夹701:drwx-----x]
		String[] args1 = { "chmod", "701", Uri.fromFile(updateDir).toString() };
		exec(args1);
		// [文件604:-rw----r--]
		String[] args2 = { "chmod", "604", Uri.fromFile(updateFile).toString() };
		exec(args2);*/

		return totalSize;
	}
	
	// 没用的===================
	public void shownotification(String msg) {
		NotificationManager barmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notice = new Notification(
				android.R.drawable.stat_notify_chat, "服务器发来信息了",
				System.currentTimeMillis());
		notice.flags = Notification.FLAG_AUTO_CANCEL;
		Intent appIntent = new Intent(Intent.ACTION_MAIN);
		// appIntent.setAction(Intent.ACTION_MAIN);
		appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		appIntent.setComponent(new ComponentName(this.getPackageName(), this
				.getPackageName() + "." + "UpdateService"));
		appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 关键的一步，设置启动模式
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				appIntent, 0);
		notice.setLatestEventInfo(this, "通知", "信息:" + msg, contentIntent);
		barmanager.notify(0, notice);
	}
}
