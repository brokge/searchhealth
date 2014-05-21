package coolbuy360.service;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import android.R.anim;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;
import coolbuy360.logic.AppVersion;
import coolbuy360.searchhealth.R;

public class UpdateApp {
	private Context context;

	public ProgressDialog pBar;
	private String currentFilePath = "";
	// 预安装
	private String fileEx = "";
	private String fileNa = "";
	private String strURL = "http://app.wcjk100.com/apk/searchhealth.apk";
	/*
	 * private String VersionUri =
	 * "http://192.168.68.120/VersionService.asmx/GetVersion";
	 */
	// private String LastVersion = "";
	private Integer lastVersionCode;
	public ProgressDialog pBarcheck;
	private Map<String, String> lastVersion = null;

	public UpdateApp(Context context) {
		this.context = context;
	}

	/**
	 * 显示检查更新
	 */
	public void startupdate() {
		new AsyncLoader_version().execute();
	}

	/**
	 * 隐藏检查更新的过程
	 */
	public void checkupdate() {
		new Asynccheck_version().execute();
	}

	/**
	 * 后台检测更新
	 */
	public class Asynccheck_version extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			int result = 0;
			try {
				/**
				 * 查询最新版本信息，包含verno,vername,updatedescr,updateurl键值。
				 */
				lastVersion = AppVersion.getLastVersion();
				if (lastVersion == null) {
					result = -1;
				} else {
					result = 2;
				}
			} catch (Exception ex) {
				result = -1;
			}
			return result;
		}

		@Override
		// 处理界面
		protected void onPostExecute(Integer result) {

			if (result == 2) {

				String lastVersionNo = lastVersion.get("verno");// 获取版本号
				if (lastVersionNo == null || lastVersionNo.equals("")) {
					lastVersionCode = 1;
				} else {
					lastVersionCode = Integer.parseInt(lastVersionNo);
				}
				String lastVersionname = lastVersion.get("vername");// 获取版本全名称
				strURL = lastVersion.get("updateurl");
				/*
				 * Toast.makeText(ConMain.this, "服务器端版本"+NowVersion,
				 * Toast.LENGTH_LONG) .show();
				 */
				// String nowVersionName = getAppVersionName(context);
				Integer nowVersionCode = getAppVersionCode(context);
				if (nowVersionCode != null && nowVersionCode < lastVersionCode) {

					LayoutInflater factory = LayoutInflater.from(context);
					final View EntryView = factory.inflate(R.layout.moreupdate,
							null);
					TextView versionName = (TextView) EntryView
							.findViewById(R.id.version_name);
					TextView version_size = (TextView) EntryView
							.findViewById(R.id.version_size);
					TextView version_date = (TextView) EntryView
							.findViewById(R.id.version_date);
					TextView version_decri = (TextView) EntryView
							.findViewById(R.id.version_decri);
					versionName.setText(lastVersionname);
					version_size.setText(lastVersion.get("filesize"));

					String updatetime = "";
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss");
					Date strtodate;
					try {
						strtodate = formatter.parse(lastVersion
								.get("updatetime"));
						formatter = new SimpleDateFormat("yyyy-MM-dd");
						updatetime = formatter.format(strtodate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
					}
					version_date.setText(updatetime);

					version_decri.setText(lastVersion.get("updatedescr"));
					Dialog dialog = new AlertDialog.Builder(context)
							.setIcon(android.R.drawable.ic_dialog_info)
							.setTitle("检测到新版本")
							.setView(EntryView)
							// .setMessage("发现新版本，请更新！")
							// 设置内容
							.setPositiveButton("马上更新",// 设置确定按钮
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// 提示框无进度下载
											/*pBar = new ProgressDialog(context);
											pBar.setTitle("正在下载");
											pBar.setMessage("请稍候...");
											pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
											fileEx = strURL
													.substring(
															strURL.lastIndexOf(".") + 1,
															strURL.length())
													.toLowerCase();
											fileNa = strURL.substring(
													strURL.lastIndexOf("/") + 1,
													strURL.lastIndexOf("."));
											getFile(strURL);*/

											// 2013-10-29 通知栏有进度下载服务	
											downloadStart();
										}
									})
							.setNegativeButton("稍后更新",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											// 点击"取消"按钮之后退出程序
										}
									}).create();// 创建
					// dialog.
					// 显示对话框
					dialog.show();
				}

				else {
					// save();
					/*
					 * Toast.makeText(context, "当前为最新版本,不用更新",
					 * Toast.LENGTH_LONG) .show();
					 */
				}

			} else {
				/* Toast.makeText(context, "链接服务器不成功", 1).show(); */
			}
		}

	}

	/**
	 * 前台检测更新
	 */
	public class AsyncLoader_version extends AsyncTask<String, Void, Integer> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pBarcheck = new ProgressDialog(context,ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
			// dialog.setTitle("Indeterminate");
			pBarcheck.setMessage("正在检查版本信息，请稍等");
			pBarcheck.setIndeterminate(true);
			pBarcheck.setCancelable(true);
			pBarcheck.show();

		}

		@Override
		protected Integer doInBackground(String... params) {
			int result = 0;
			try {
				/**
				 * 查询最新版本信息，包含verno,vername,updatedescr,updateurl键值。
				 */
				lastVersion = AppVersion.getLastVersion();
				if (lastVersion == null) {
					result = -1;
				} else {
					result = 2;
				}
			} catch (Exception ex) {
				result = -1;
			}
			return result;
		}

		@Override
		// 处理界面
		protected void onPostExecute(Integer result) {
			Log.i("ExerciseGuess", "onPostExecute(Result result) called");

			if (result == 2) {
				pBarcheck.cancel();
				String lastVersionNo = lastVersion.get("verno");// 获取版本号
				if (lastVersionNo == null || lastVersionNo.equals("")) {
					lastVersionCode = 1;
				} else {
					lastVersionCode = Integer.parseInt(lastVersionNo);
				}
				String lastVersionname = lastVersion.get("vername");// 获取版本全名称
				strURL = lastVersion.get("updateurl");
				/*
				 * Toast.makeText(ConMain.this, "服务器端版本"+NowVersion,
				 * Toast.LENGTH_LONG) .show();
				 */
				// String nowVersionName = getAppVersionName(context);
				Integer nowVersionCode = getAppVersionCode(context);
				if (nowVersionCode != null && nowVersionCode < lastVersionCode) {

					LayoutInflater factory = LayoutInflater.from(context);
					final View EntryView = factory.inflate(R.layout.moreupdate,
							null);
					TextView versionName = (TextView) EntryView
							.findViewById(R.id.version_name);
					TextView version_size = (TextView) EntryView
							.findViewById(R.id.version_size);
					TextView version_date = (TextView) EntryView
							.findViewById(R.id.version_date);
					TextView version_decri = (TextView) EntryView
							.findViewById(R.id.version_decri);
					versionName.setText(lastVersionname);
					version_size.setText(lastVersion.get("filesize"));

					String updatetime = "";
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss");
					Date strtodate;
					try {
						strtodate = formatter.parse(lastVersion
								.get("updatetime"));
						formatter = new SimpleDateFormat("yyyy-MM-dd");
						updatetime = formatter.format(strtodate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
					}
					version_date.setText(updatetime);

					version_decri.setText(lastVersion.get("updatedescr"));
					Dialog dialog = new AlertDialog.Builder(context)
							.setIcon(android.R.drawable.ic_dialog_info)
							.setTitle("检测到新版本")
							.setView(EntryView)
							// .setMessage("发现新版本，请更新！")
							// 设置内容
							.setPositiveButton("马上更新",// 设置确定按钮
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// 提示框无进度下载
											/*pBar = new ProgressDialog(context);
											pBar.setTitle("正在下载");
											pBar.setMessage("请稍候...");
											pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
											fileEx = strURL
													.substring(
															strURL.lastIndexOf(".") + 1,
															strURL.length())
													.toLowerCase();
											fileNa = strURL.substring(
													strURL.lastIndexOf("/") + 1,
													strURL.lastIndexOf("."));
											getFile(strURL);*/

											// 2013-10-29 通知栏有进度下载服务	
											downloadStart();
										}
									})
							.setNegativeButton("稍后更新",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											// 点击"取消"按钮之后退出程序
										}
									}).create();// 创建
					// dialog.
					// 显示对话框
					dialog.show();
				} else {
					// save();
					Toast.makeText(context, "当前为最新版本，不用更新", Toast.LENGTH_LONG)
							.show();
				}
			} else {
				pBarcheck.cancel();
				Toast.makeText(context, "连接服务器不成功", 1).show();
			}
		}
	}
	
	/**
	 * 启动下载，显示到通知栏
	 */
	private void downloadStart()
	{
        Intent updateIntent =new Intent(context, UpdateService.class);  
        updateIntent.putExtra("updateFileDownLoadURL", strURL);
        String updateFileName = strURL.substring(strURL.lastIndexOf("/") + 1);
        updateIntent.putExtra("updateFileSaveURL", context.getFilesDir()+ "/" + updateFileName);
        context.startService(updateIntent);	
	}

	private void getFile(final String strPath) {
		pBar.show();
		try {
			if (strPath.equals(currentFilePath)) {
				getDataSource(strPath);
			}
			currentFilePath = strPath;
			Runnable r = new Runnable() {
				public void run() {
					try {
						getDataSource(strPath);
					} catch (Exception e) {
						// Log.e(TAG, e.getMessage(), e);
					}
				}
			};
			new Thread(r).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* 取得远程文件 */
	private void getDataSource(String strPath) throws Exception {
		if (!URLUtil.isNetworkUrl(strPath)) {
			// Log.d("Tag", "error");
		} else {
			/*
			 * 取得URL URL myURL = new URL(strPath); 建立联机 URLConnection conn =
			 * myURL.openConnection(); conn.connect();
			 */
			/* InputStream 下载文件 */
			InputStream is = httpURI.getStreamFromURL(strPath);
			if (is == null) {
				// Log.d("tag", "error");
				throw new RuntimeException("stream is null");
			}
			/*
			 * try { 建立临时文件 File myTempFile = File.createTempFile(fileNa, "." +
			 * fileEx); myTempFile.getAbsolutePath();
			 * myTempFile.deleteOnExit();//退出时删除文件 将文件写入临时盘 FileOutputStream fos
			 * = new FileOutputStream(myTempFile); byte buf[] = new byte[128];
			 * do { int numread = is.read(buf); if (numread <= 0) { break; }
			 * fos.write(buf, 0, numread); } while (true); 打开文件进行安装
			 * openFile(myTempFile); // openFile(c);
			 * 
			 * is.close(); } catch (Exception ex) {
			 */
			try {
				// File myfile=
				FileService fsv = new FileService(context);

				fsv.writeFile(fileNa + "." + fileEx, is);
				Boolean aa = Util.runCommand("chmod 777 "
						+ context.getFilesDir() + "/" + fileNa + "." + fileEx);
				// Log.i("chenlinwei", aa + "");
				openFile(new File(context.getFilesDir() + "/" + fileNa + "."
						+ fileEx));

				// context installPackage

			} catch (Exception ex1) {
				Log.e("chenlinwei", "error: " + ex1.getMessage(), ex1);
			}
			// }
		}
	}

	/**
	 * 在手机上打开文件的
	 * 
	 * @param F
	 *            File的实例
	 */
	private void openFile(File f) {
		pBar.cancel();
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);

		/* 调用getMIMEType()来取得MimeType */
		String type = getMIMEType(f);
		/* 设定intent的file与MimeType */
		intent.setDataAndType(Uri.fromFile(f), type);
		context.startActivity(intent);
	}
	
	/**
	 * 打开安装文件
	 * @param context
	 * @param fileURL
	 */
	public static void installUpdateApk(Context context, String fileURL) {
		
		// 获取文件打开权限
    	// [文件夹701:drwx-----x]
        /*String[] args1 = { "chmod", "701", this.getFilesDir().toString() };
        exec(args1);*/
        // [文件604:-rw----r--]
        /*String[] args2 = { "chmod", "604", getFilesDir()+ "/" + updateFileName };
        exec(args2);*/		
        Util.runCommand("chmod 604 " + fileURL);
		
		File f = new File(fileURL);
		
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);

		// 调用getMIMEType()来取得MimeType 
		String type = "application/vnd.android.package-archive";
		// 设定intent的file与MimeType 
		intent.setDataAndType(Uri.fromFile(f), type);
		context.startActivity(intent);
	}

	/**
	 * 判断文件MimeType
	 * 
	 * @param F
	 *            File的实例
	 * @return 返回MinmeType
	 */
	private String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		/* 取得扩展名 */
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		/* 按扩展名的类型决定MimeType */
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("apk")) {
			/* android.permission.INSTALL_PACKAGES */
			type = "application/vnd.android.package-archive";
		} else {
			type = "*";
		}
		/* 如果无法直接打开，就跳出软件清单给使用者选择 */
		if (end.equals("apk")) {
		} else {
			type += "/*";
		}
		return type;
	}

	/**
	 * 返回当前程序版本名
	 * 
	 * @param context
	 *            上下文对象
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			// versioncode = pi.versionCode;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}

	/**
	 * 返回当前程序版本号
	 * 
	 * @param context
	 *            上下文对象
	 */
	public static int getAppVersionCode(Context context) {
		int versionCode = 1;
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionCode = pi.versionCode;
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionCode;
	}

}
