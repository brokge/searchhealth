package coolbuy360.searchhealth;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.WeiboParameters;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.net.RequestListener;
import com.weibo.sdk.android.sso.SsoHandler;
import com.weibo.sdk.android.util.Utility;

import coolbuy360.adapter.MoreWeiboDoctorAdater;
import coolbuy360.logic.WeiboDoctor;
import coolbuy360.searchhealth.MemberLogin.loginbtnOnClick;
import coolbuy360.service.JsonUtril;
import coolbuy360.service.searchApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.AsyncTask;

import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MoreWeiboDoctor extends Activity {
	private UsersAPI mUserapi;
	private Weibo mWeibo;
	public static Oauth2AccessToken accessToken;
	public static final String TAG = "sinasdk";
	SsoHandler mSsoHandler;
	private ListView doctorListView;
	private Handler handler;
	List<Map<String, Object>> DoctorInfoList;
	private Dialog pBarcheck;
	Button btnlogout;
	searchApp app = searchApp.getInstance();
	LinearLayout async_error;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moreweibodoctor);
		// 为退出做准备
		searchApp.getInstance().addActivity(this);
		// mWeibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);
		handler = new Handler();
		
		async_error = (LinearLayout) findViewById(R.id.async_error);
		// initAuth();
		initToken();
	}

	/**
	 * 获取固定的AccessToken来获取相关的数据（需要app.CONSUMER_KEY, app.REDIRECT_URL
	 * ,ACCESS_TOKEN三个相关参数） mWeibo = Weibo.getInstance(app.CONSUMER_KEY,
	 * app.REDIRECT_URL)//提供app.CONSUMER_KEY, app.REDIRECT_URL Oauth2AccessToken
	 * accesstoken= new
	 * Oauth2AccessToken(app.ACCESS_TOKEN,"10000000");//提供accesstoken
	 */
	private void initToken() {
		ImageButton actionbar_pre_btn = (ImageButton) findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MoreWeiboDoctor.this.finish();
			}
		});
		/*
		 * Oauth2AccessToken aa= new Oauth2AccessToken();
		 * aa.setToken(app.ACCESS_TOKEN);
		 */
		mWeibo = Weibo.getInstance(app.CONSUMER_KEY, app.REDIRECT_URL);
		// MoreWeiboDoctor.accessToken =
		// AccessTokenKeeper.readAccessToken(this);
		// mWeibo .setupConsumerConfig(app.CONSUMER_KEY, app.REDIRECT_URL);
		Oauth2AccessToken accesstoken = new Oauth2AccessToken(app.ACCESS_TOKEN,
				"1");
		// MoreWeiboDoctor.accessToken = mWeibo.accessToken;
		Log.i("chenlinwei", accesstoken + ":accessToken" + "app.CONSUMER_KEY:"
				+ app.CONSUMER_KEY);
		mUserapi = new UsersAPI(accesstoken);
		new asynLoadDoctor().execute();
	}

	/**
	 * 初始验证以及数据加载（暂时用不到）
	 */
	private void initAuth() {
		mWeibo = Weibo.getInstance(app.CONSUMER_KEY, app.REDIRECT_URL);
		MoreWeiboDoctor.accessToken = AccessTokenKeeper.readAccessToken(this);
		ImageButton actionbar_pre_btn = (ImageButton) findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MoreWeiboDoctor.this.finish();
			}
		});
		btnlogout = (Button) findViewById(R.id.more_weibo_btnTitle_change);
		// 取消验证信息
		btnlogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Dialog dialog = new AlertDialog.Builder(MoreWeiboDoctor.this)
						.setIcon(android.R.drawable.alert_dark_frame)
						.setTitle("确定切换帐号？")
						.setMessage("切换帐号的需要重新验证账户信息")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										MoreWeiboDoctor.accessToken = null;
										AccessTokenKeeper
												.clear(MoreWeiboDoctor.this);
										MoreWeiboDoctor.this.finish();

									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}

								}).create();
				dialog.show();
			}
		});
		btnlogout.setVisibility(View.GONE);
		if (MoreWeiboDoctor.accessToken.isSessionValid()) {
			btnlogout.setVisibility(View.VISIBLE);
			Weibo.isWifi = Utility.isWifi(this);
			String date = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
					.format(new java.util.Date(MoreWeiboDoctor.accessToken
							.getExpiresTime()));
			mUserapi = new UsersAPI(accessToken);
			// 异步加载微博医生列表
			new asynLoadDoctor().execute();
		} else {
			// mText.setText("使用SSO登录前，请检查手机上是否已经安装新浪微博客户端，目前仅3.0.0及以上微博客户端版本支持SSO；如果未安装，将自动转为Oauth2.0进行认证");
			mSsoHandler = new SsoHandler(MoreWeiboDoctor.this, mWeibo);
			mSsoHandler.authorize(new AuthDialogListener());
		}
	}

	/**
	 * 异步加载微博医生列表
	 * 
	 * @author chenlw
	 * 
	 */
	private class asynLoadDoctor extends AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerDoctorList ;
		List<Map<String, Object>> innerDoctorInfoList = new ArrayList<Map<String, Object>>();
		long[] uids;
		String[] unames;
		Map<String, String> map;
		int i = 0;
		// int j=0;
		StringBuilder userStr = new StringBuilder();

		// 0代表成功 1代表数据为空 2代表网络连接有误
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pBarcheck = new Dialog(MoreWeiboDoctor.this,R.style.dialog);
			pBarcheck.setContentView(R.layout.custom_progress);
			// dialog.setTitle("Indeterminate");
			/*pBarcheck.setMessage("正在获取医生信息");
			pBarcheck.setIndeterminate(true);*/
			pBarcheck.setCancelable(true);
			pBarcheck.show();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				innerDoctorList = WeiboDoctor.getList(0, 0);
				if (innerDoctorList != null) {
					if (innerDoctorList.size() > 0) {
						Log.i("chenlinwei", "加载list数据集返回值:0");
						return 0;
					} else {
						Log.i("chenlinwei", "加载list数据集返回值:1");
						return 1;
					}
				} else {
					return 2;
				}
			} catch (Exception e) {
				Log.i("chenlinwei", "加载list数据集返回值:2");
				return 2;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if (result == 0) {
				// uids = new long[innerDoctorList.size()];//给long[]初始化大小
				unames = new String[innerDoctorList.size()];// 给string[]初始化大小和long[]的初始化形式一样
				Log.i("chenlinwei", "医生个数：" + innerDoctorList.size());
				while (i < innerDoctorList.size()) {
					map = innerDoctorList.get(i);
					// uids[i]= Long.parseLong(map.get("doctorname"));
					unames[i] = map.get("wbusername");
					mUserapi.show(Long.parseLong(unames[i]),
							new RequestListener() {
								@Override
								public void onIOException(IOException arg0) {
									// Log.i("chenlinwei", "IOException错误信息：" +
									// arg0.getMessage());
									handler.post(mErrorResults);// 跨线程更新页面
								}

								@Override
								public void onError(WeiboException arg0) {
									handler.post(mErrorResults);// 跨线程更新页面
									// Log.i("chenlinwei", "WeiboException错误信息："
									// + arg0.getMessage());
								}

								@Override
								public void onComplete(String content) {
									// j++;
									Log.i("chenlinwei",
											content	+ "121212");
									if(!content.equals(""))
									{										
										//userStr.append(content);										
										innerDoctorInfoList.add(JsonUtril
												.getMap(content));
										if (innerDoctorInfoList.size() == innerDoctorList
												.size()) {
											Log.i("chenlinwei",
													innerDoctorInfoList.size()
															+ "：新组成的数据集合个数");
											DoctorInfoList = innerDoctorInfoList;
											// initListData(innerDoctorInfoList);
											handler.post(mUpdateResults);// 跨线程更新页面
											/*
											 * 多线程的相关操作 Message msg=new Message();
											 * msg.what=1; handler.sendMessage(msg);
											 */
										}
									}
									else
									{
										Toast.makeText(MoreWeiboDoctor.this, "读取没有相关医生信息", Toast.LENGTH_SHORT).show();
									}
								}
							});

					i++;
				}

			} else if (result == 1) {
				pBarcheck.cancel();
				TextView async_error_txt = (TextView) async_error
						.findViewById(R.id.async_error_txt);
				Button async_error_reflesh = (Button) async_error
						.findViewById(R.id.async_error_reflesh);
				async_error_reflesh.setVisibility(View.GONE);
				async_error_txt.setText("没有找到相关的医生");
				async_error.setVisibility(View.VISIBLE);
			} else if (result == 2) {
				pBarcheck.cancel();
				async_error.setVisibility(View.VISIBLE);
				TextView async_error_txt = (TextView) async_error
						.findViewById(R.id.async_error_txt);
				async_error_txt.setText(R.string.error_nonetwork);
				Button async_error_reflesh = (Button) async_error
						.findViewById(R.id.async_error_reflesh);
				async_error_reflesh.setVisibility(View.VISIBLE);
				async_error_reflesh.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						async_error.setVisibility(View.GONE);
						new asynLoadDoctor().execute();
					}
				});
			}

		}

	}

	/**
	 * 跨线程更新UI相关操作
	 */
	Runnable mUpdateResults = new Runnable() {
		public void run() {
			initListData(DoctorInfoList);
		}
	};
	/**
	 * 
	 */
	Runnable mErrorResults = new Runnable() {
		public void run() {
			// initListData(DoctorInfoList);
			pBarcheck.cancel();
			Toast.makeText(MoreWeiboDoctor.this, "出现不明原因错误", 1).show();
		}
	};

	/**
	 * 更新UI的方法
	 * 
	 * @param weibolist
	 */
	private void initListData(final List<Map<String, Object>> weibolist) {
		doctorListView = (ListView) MoreWeiboDoctor.this
				.findViewById(R.id.more_weibo_listview);
		doctorListView.setAdapter(new MoreWeiboDoctorAdater(
				MoreWeiboDoctor.this, weibolist));
		doctorListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int posion,
					long arg3) {
				// TODO Auto-generated method stub
				String url = "http://www.weibo.com/"
						+ weibolist.get(posion).get("profile_url");
				//Toast.makeText(MoreWeiboDoctor.this, url + "", 1).show();

				Intent intent = new Intent().setClass(MoreWeiboDoctor.this,
						MoreWeiboUrl.class);
				Bundle bundle = new Bundle();
				bundle.putString("weibo_url", url);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		pBarcheck.cancel();
	}

	/**
	 * 异步加载微博账户数量信息：粉丝数 、关注数、微博数。（暂时用不到）
	 * 
	 * @author chenlw
	 * 
	 */
	private class asynLoadDoctorCount extends AsyncTask<long[], Void, Integer> {
		@Override
		protected Integer doInBackground(long[]... params) {
			// TODO Auto-generated method stub
			if (params.length > 0) {
				Log.i("chenlinwei", "params.length：" + params.length);
				mUserapi.counts(params[0], new RequestListener() {
					@Override
					public void onIOException(IOException arg0) {

					}

					@Override
					public void onError(WeiboException arg0) {
					}

					@Override
					public void onComplete(String content) {
						Log.i("chenlinwei", "sina返回的：" + content);
						// Log.i("chenlinwei", content);
					}
				});
			}
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub

			super.onPostExecute(result);
		}

	}

	/**
	 * 两种验证方法的监听事件(这个暂时用不到，在sso验证和auth验证获取AccessToken的时候才会用到，本程序是固定AccessToken)
	 * 
	 * @author chenlw
	 * 
	 */
	class AuthDialogListener implements WeiboAuthListener {
		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			MoreWeiboDoctor.accessToken = new Oauth2AccessToken(token,
					expires_in);
			if (MoreWeiboDoctor.accessToken.isSessionValid()) {
				btnlogout.setVisibility(View.VISIBLE);
				String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
						.format(new java.util.Date(MoreWeiboDoctor.accessToken
								.getExpiresTime()));
				/*
				 * mText.setText("认证成功: \r\n access_token: " + token + "\r\n" +
				 * "expires_in: " + expires_in + "\r\n有效期：" + date);
				 */
				try {
					Class sso = Class
							.forName("com.weibo.sdk.android.api.WeiboAPI");// 如果支持weiboapi的话，显示api功能演示入口按钮
					// apiBtn.setVisibility(View.VISIBLE);
				} catch (ClassNotFoundException e) {
					// e.printStackTrace();
					Log.i(TAG, "com.weibo.sdk.android.api.WeiboAPI not found");
				}
				// cancelBtn.setVisibility(View.VISIBLE);
				AccessTokenKeeper.keepAccessToken(MoreWeiboDoctor.this,
						accessToken);
				Toast.makeText(MoreWeiboDoctor.this, "认证成功", Toast.LENGTH_SHORT)
						.show();
				// 首次验证成功
				mUserapi = new UsersAPI(accessToken);
				new asynLoadDoctor().execute();
			}
		}

		@Override
		public void onError(WeiboDialogError e) {
			Toast.makeText(getApplicationContext(),
					"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getApplicationContext(),
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

	}

	/**
	 * 这个暂时用不到（在sso验证和auth验证获取AccessToken的时候才会用到，本程序是固定AccessToken）
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i(TAG, " " + resultCode);
		/**
		 * 下面两代码，仅当sdk支持sso时有效，
		 */
		if (mSsoHandler != null) {
			Log.i(TAG, " " + resultCode);
			try {
				mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(MoreWeiboDoctor.this, "返回值有错误",
						Toast.LENGTH_SHORT);
			}
		}
	}

}
