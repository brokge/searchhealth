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
	// Ԥ��װ
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
	 * ��ʾ������
	 */
	public void startupdate() {
		new AsyncLoader_version().execute();
	}

	/**
	 * ���ؼ����µĹ���
	 */
	public void checkupdate() {
		new Asynccheck_version().execute();
	}

	/**
	 * ��̨������
	 */
	public class Asynccheck_version extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			int result = 0;
			try {
				/**
				 * ��ѯ���°汾��Ϣ������verno,vername,updatedescr,updateurl��ֵ��
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
		// �������
		protected void onPostExecute(Integer result) {

			if (result == 2) {

				String lastVersionNo = lastVersion.get("verno");// ��ȡ�汾��
				if (lastVersionNo == null || lastVersionNo.equals("")) {
					lastVersionCode = 1;
				} else {
					lastVersionCode = Integer.parseInt(lastVersionNo);
				}
				String lastVersionname = lastVersion.get("vername");// ��ȡ�汾ȫ����
				strURL = lastVersion.get("updateurl");
				/*
				 * Toast.makeText(ConMain.this, "�������˰汾"+NowVersion,
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
							.setTitle("��⵽�°汾")
							.setView(EntryView)
							// .setMessage("�����°汾������£�")
							// ��������
							.setPositiveButton("���ϸ���",// ����ȷ����ť
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// ��ʾ���޽�������
											/*pBar = new ProgressDialog(context);
											pBar.setTitle("��������");
											pBar.setMessage("���Ժ�...");
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

											// 2013-10-29 ֪ͨ���н������ط���	
											downloadStart();
										}
									})
							.setNegativeButton("�Ժ����",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											// ���"ȡ��"��ť֮���˳�����
										}
									}).create();// ����
					// dialog.
					// ��ʾ�Ի���
					dialog.show();
				}

				else {
					// save();
					/*
					 * Toast.makeText(context, "��ǰΪ���°汾,���ø���",
					 * Toast.LENGTH_LONG) .show();
					 */
				}

			} else {
				/* Toast.makeText(context, "���ӷ��������ɹ�", 1).show(); */
			}
		}

	}

	/**
	 * ǰ̨������
	 */
	public class AsyncLoader_version extends AsyncTask<String, Void, Integer> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pBarcheck = new ProgressDialog(context,ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
			// dialog.setTitle("Indeterminate");
			pBarcheck.setMessage("���ڼ��汾��Ϣ�����Ե�");
			pBarcheck.setIndeterminate(true);
			pBarcheck.setCancelable(true);
			pBarcheck.show();

		}

		@Override
		protected Integer doInBackground(String... params) {
			int result = 0;
			try {
				/**
				 * ��ѯ���°汾��Ϣ������verno,vername,updatedescr,updateurl��ֵ��
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
		// �������
		protected void onPostExecute(Integer result) {
			Log.i("ExerciseGuess", "onPostExecute(Result result) called");

			if (result == 2) {
				pBarcheck.cancel();
				String lastVersionNo = lastVersion.get("verno");// ��ȡ�汾��
				if (lastVersionNo == null || lastVersionNo.equals("")) {
					lastVersionCode = 1;
				} else {
					lastVersionCode = Integer.parseInt(lastVersionNo);
				}
				String lastVersionname = lastVersion.get("vername");// ��ȡ�汾ȫ����
				strURL = lastVersion.get("updateurl");
				/*
				 * Toast.makeText(ConMain.this, "�������˰汾"+NowVersion,
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
							.setTitle("��⵽�°汾")
							.setView(EntryView)
							// .setMessage("�����°汾������£�")
							// ��������
							.setPositiveButton("���ϸ���",// ����ȷ����ť
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// ��ʾ���޽�������
											/*pBar = new ProgressDialog(context);
											pBar.setTitle("��������");
											pBar.setMessage("���Ժ�...");
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

											// 2013-10-29 ֪ͨ���н������ط���	
											downloadStart();
										}
									})
							.setNegativeButton("�Ժ����",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											// ���"ȡ��"��ť֮���˳�����
										}
									}).create();// ����
					// dialog.
					// ��ʾ�Ի���
					dialog.show();
				} else {
					// save();
					Toast.makeText(context, "��ǰΪ���°汾�����ø���", Toast.LENGTH_LONG)
							.show();
				}
			} else {
				pBarcheck.cancel();
				Toast.makeText(context, "���ӷ��������ɹ�", 1).show();
			}
		}
	}
	
	/**
	 * �������أ���ʾ��֪ͨ��
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

	/* ȡ��Զ���ļ� */
	private void getDataSource(String strPath) throws Exception {
		if (!URLUtil.isNetworkUrl(strPath)) {
			// Log.d("Tag", "error");
		} else {
			/*
			 * ȡ��URL URL myURL = new URL(strPath); �������� URLConnection conn =
			 * myURL.openConnection(); conn.connect();
			 */
			/* InputStream �����ļ� */
			InputStream is = httpURI.getStreamFromURL(strPath);
			if (is == null) {
				// Log.d("tag", "error");
				throw new RuntimeException("stream is null");
			}
			/*
			 * try { ������ʱ�ļ� File myTempFile = File.createTempFile(fileNa, "." +
			 * fileEx); myTempFile.getAbsolutePath();
			 * myTempFile.deleteOnExit();//�˳�ʱɾ���ļ� ���ļ�д����ʱ�� FileOutputStream fos
			 * = new FileOutputStream(myTempFile); byte buf[] = new byte[128];
			 * do { int numread = is.read(buf); if (numread <= 0) { break; }
			 * fos.write(buf, 0, numread); } while (true); ���ļ����а�װ
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
	 * ���ֻ��ϴ��ļ���
	 * 
	 * @param F
	 *            File��ʵ��
	 */
	private void openFile(File f) {
		pBar.cancel();
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);

		/* ����getMIMEType()��ȡ��MimeType */
		String type = getMIMEType(f);
		/* �趨intent��file��MimeType */
		intent.setDataAndType(Uri.fromFile(f), type);
		context.startActivity(intent);
	}
	
	/**
	 * �򿪰�װ�ļ�
	 * @param context
	 * @param fileURL
	 */
	public static void installUpdateApk(Context context, String fileURL) {
		
		// ��ȡ�ļ���Ȩ��
    	// [�ļ���701:drwx-----x]
        /*String[] args1 = { "chmod", "701", this.getFilesDir().toString() };
        exec(args1);*/
        // [�ļ�604:-rw----r--]
        /*String[] args2 = { "chmod", "604", getFilesDir()+ "/" + updateFileName };
        exec(args2);*/		
        Util.runCommand("chmod 604 " + fileURL);
		
		File f = new File(fileURL);
		
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);

		// ����getMIMEType()��ȡ��MimeType 
		String type = "application/vnd.android.package-archive";
		// �趨intent��file��MimeType 
		intent.setDataAndType(Uri.fromFile(f), type);
		context.startActivity(intent);
	}

	/**
	 * �ж��ļ�MimeType
	 * 
	 * @param F
	 *            File��ʵ��
	 * @return ����MinmeType
	 */
	private String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		/* ȡ����չ�� */
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		/* ����չ�������;���MimeType */
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
		/* ����޷�ֱ�Ӵ򿪣�����������嵥��ʹ����ѡ�� */
		if (end.equals("apk")) {
		} else {
			type += "/*";
		}
		return type;
	}

	/**
	 * ���ص�ǰ����汾��
	 * 
	 * @param context
	 *            �����Ķ���
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
	 * ���ص�ǰ����汾��
	 * 
	 * @param context
	 *            �����Ķ���
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
