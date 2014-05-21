package coolbuy360.searchhealth;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import coolbuy360.adapter.PromotionGalleryAdapter;
import coolbuy360.control.ADGallery;
import coolbuy360.control.ScoreChangePopup;
import coolbuy360.logic.ConstantsSetting;
import coolbuy360.logic.DrugStore;
import coolbuy360.searchhealth.DrugStoreMap.LoadMode;
import coolbuy360.searchhealth.DrugStoreMap.ReturnMode;
import coolbuy360.service.CommandResult;
import coolbuy360.service.LocationInfo;
import coolbuy360.service.LocationProvider;
import coolbuy360.service.TimestampException;
import coolbuy360.service.Util;
import coolbuy360.service.searchApp;

public class DrugStoreDetaill extends Activity {

	private Bundle bundle;
	String drugstoreid;
	
	private Boolean ischecked = true;
	private String call = null;
	private String phone = null;
	CheckBox collect_checkbox;
	LinearLayout async_begin;
	LinearLayout async_error;
	LinearLayout drugstore_Linear_info;
	LinearLayout drugstore_detail_loader_promotion;
	LinearLayout drugstore_detail_llt_promotion;
	ImageButton actionbar_tomap_btn;

	LoadMode loadMode = LoadMode.Position;
	ReturnMode returnMode = ReturnMode.Normal;
	
	private LocationProvider innerLocationProvider = null;
	LocationInfo.SItude station = null;
	
	/*protected double latitude = 30.280506;
	protected double longitude = 120.107582;*/
	protected double latitude = 0.0;
	protected double longitude = 0.0;

	
	public ImageTimerTask timeTaks = null;
	int gallerypisition = 0;
	public ADGallery myImagesGallary;
	private Thread timeThread = null;
	public boolean timeFlag = true;
	private boolean isExit = false;
	private int positon = 0;
	Timer autoGallery = new Timer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		innerLocationProvider = searchApp.mLocationProvider;
		// 为退出做准备
		searchApp.getInstance().addActivity(this);
		setContentView(R.layout.drugstore_detail);
		
		async_begin = (LinearLayout) findViewById(R.id.async_begin);
		async_error = (LinearLayout) findViewById(R.id.async_error);
		drugstore_Linear_info = (LinearLayout) findViewById(R.id.drugstore_Linear_info);
		drugstore_detail_loader_promotion = (LinearLayout) findViewById(R.id.drugstore_detail_loader_promotion);
		drugstore_detail_llt_promotion = (LinearLayout) findViewById(R.id.drugstore_detail_llt_promotion);
		collect_checkbox = (CheckBox) this.findViewById(R.id.collect_checkbox);
		LinearLayout collect_btn = (LinearLayout) this
				.findViewById(R.id.collect_btn);
		collect_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (collect_checkbox.isChecked()) {
					collect_checkbox.setChecked(false);
				} else {
					collect_checkbox.setChecked(true);
				}
			}
		});

		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DrugStoreDetaill.this.finish();
			}
		});

		actionbar_tomap_btn = (ImageButton) this
				.findViewById(R.id.actionbar_tomap_btn);
		actionbar_tomap_btn.setOnClickListener(new changeOnClickListener());
		
		//初始加载数据
		loadData();		
	}
	
	/**
	 * 加载数据
	 */
	private  void loadData()
	{
		bundle = getIntent().getExtras();
		drugstoreid = bundle.getString("drugstoreid");// 获取上级传过来的值
		/*latitude = bundle.getDouble("latitude");
		longitude = bundle.getDouble("longitude");*/
		loadMode = DrugStoreMap.loadModeTransform(bundle.getString("loadmode"), LoadMode.Position);
		returnMode = DrugStoreMap.returnModeTransform(bundle.getString("returnmode"), ReturnMode.Normal);

		new AsyncLoad().execute();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) { 			
			drugstoreid = data.getExtras().getString("drugstoreid");
			new AsyncLoad().execute();
		}
	}

	/**
	 * 初始化数据
	 */
	private void initData(Map<String, String> storeinfo) {
		TextView txtname = (TextView) this.findViewById(R.id.s_detail_txtname);
		TextView txtaddress = (TextView) this
				.findViewById(R.id.s_detail_txtaddress);
		TextView txtdistance = (TextView) this
				.findViewById(R.id.s_detail_txtdistance);
		TextView txttime = (TextView) this.findViewById(R.id.s_detail_txttime);
		TextView txtcall = (TextView) this.findViewById(R.id.s_detail_txtcall);
		TextView txtphone = (TextView) this
				.findViewById(R.id.s_detail_txtphone);
		/*
		 * ImageView imgbao=(ImageView)this.findViewById(R.id.s_detail_imgbao);
		 * TextView txtbao=(TextView)this.findViewById(R.id.s_detail_txtbao);
		 * ImageView
		 * imgding=(ImageView)this.findViewById(R.id.s_detail_imgding); TextView
		 * txtding=(TextView)this.findViewById(R.id.s_detail_txtding); ImageView
		 * imgsong=(ImageView)this.findViewById(R.id.s_detail_imgsong); TextView
		 * txtsong=(TextView)this.findViewById(R.id.s_detail_txtsong); ImageView
		 * imgdao=(ImageView)this.findViewById(R.id.s_detail_imgdao); TextView
		 * txtdao=(TextView)this.findViewById(R.id.s_detail_txtdao); ImageView
		 * imgv=(ImageView)this.findViewById(R.id.s_detail_imgv); TextView
		 * txtv=(TextView)this.findViewById(R.id.s_detail_txtv); ImageView
		 * img24=(ImageView)this.findViewById(R.id.s_detail_img24); TextView
		 * txt24=(TextView)this.findViewById(R.id.s_detail_txt24);
		 */

		TextView txtother = (TextView) this
				.findViewById(R.id.s_detail_txtotherservice);
		TextView txtintroduct = (TextView) this
				.findViewById(R.id.s_detail_txtintroduct);
		/*
		 * 根据药店ID查询药店详细信息，返回一条记录，包含键值：DrugStoreID，DrugStoreName，Tel，Mobile，IsTel，
		 * IsDoor，IsCOD，
		 * IsHC，Is24Hour，IsMember，LongValue，LatValue，Address，distance
		 * ，OldPrice，NowPrice，Intro，Logo，BusinessTime，
		 * DoorContent，OtherService，FullAddress。所有键值小写。
		 */

		LinearLayout tabrowcall = (LinearLayout) this
				.findViewById(R.id.s_detail_tabrowcall);
		LinearLayout tabrowcallphone = (LinearLayout) this
				.findViewById(R.id.s_detail_tabrowphone);

		ImageButton callbtn = (ImageButton) this
				.findViewById(R.id.s_detail_btncall);
		ImageButton phonebtn = (ImageButton) this
				.findViewById(R.id.s_detail_btnphone);

		txtname.setText(storeinfo.get("drugstorename"));
		txtaddress.setText(storeinfo.get("address"));
		String distance = storeinfo.get("distance");
		if (!distance.equals("")) {
			float distan = Util.round(Float.parseFloat(distance), 2);
			if (distan > 1) {
				distance = distan + "公里";
			} else {
				distance = distan * 1000 + "米";
			}
			// distance=Utril.round(Float.parseFloat(distance), 2)+"";
			txtdistance.setText(distance);
		}

		String is24 = storeinfo.get("is24hour");// 24小时
		String isv = storeinfo.get("ismember");// 会员
		String isding = storeinfo.get("istel");// 电话订购
		String issong = storeinfo.get("isdoor");// 送货上门
		String isdao = storeinfo.get("iscod");// 货到付款
		String isbao = storeinfo.get("ishc");// 医保
		call = storeinfo.get("tel");
		phone = storeinfo.get("mobile");
		String otherservice = storeinfo.get("otherservice");
		txtother.setText(otherservice);
		String intro = storeinfo.get("intro");
		txtintroduct.setText(intro);

		if (!call.equals("")) {
			txtcall.setText(call);
			callbtn.setOnClickListener(new callOnclick());
		} else {
			tabrowcall.setVisibility(View.GONE);
		}

		if (!phone.equals("")) {
			txtphone.setText(phone);
			phonebtn.setOnClickListener(new phoneOnclick());

		} else {
			tabrowcallphone.setVisibility(View.GONE);
		}

		if (!is24.equals("1")) {
			LinearLayout tabrowcall24 = (LinearLayout) this
					.findViewById(R.id.s_detail_tabrow24);
			tabrowcall24.setVisibility(View.GONE);
			txttime.setText(storeinfo.get("businesstime"));
		}
		else{
			txttime.setText("0:00~24:00");
		}

		if (!isv.equals("1")) {

			LinearLayout tabrowcallv = (LinearLayout) this
					.findViewById(R.id.s_detail_tabrowv);
			tabrowcallv.setVisibility(View.GONE);

		}
		if (!isbao.equals("1")) {
			LinearLayout tabrowcallbao = (LinearLayout) this
					.findViewById(R.id.s_detail_tabrowbao);
			tabrowcallbao.setVisibility(View.GONE);
		}
		if (!issong.equals("1")) {
			LinearLayout tabrowcallsong = (LinearLayout) this
					.findViewById(R.id.s_detail_tabrowsong);
			tabrowcallsong.setVisibility(View.GONE);
		}
		if (!isdao.equals("1")) {
			LinearLayout tabrowcalldao = (LinearLayout) this
					.findViewById(R.id.s_detail_tabrowdao);
			tabrowcalldao.setVisibility(View.GONE);
		}
		if (!isding.equals("1")) {
			LinearLayout tabrowcallding = (LinearLayout) this
					.findViewById(R.id.s_detail_tabrowding);
			tabrowcallding.setVisibility(View.GONE);
		}

		/*if (!otherservice.equals("")) {
			txtother.setText(otherservice);
		}
		if (!intro.equals("")) {
			txtintroduct.setText(intro);
		}*/

	}

	/**
	 * 异步加载药店信息
	 */
	private class AsyncLoad extends AsyncTask<String, Void, Integer> {
		private List<Map<String, String>> storeinfolist = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			async_begin.setVisibility(View.VISIBLE);
			drugstore_Linear_info.setVisibility(View.GONE);
			actionbar_tomap_btn.setVisibility(View.GONE);			
		}

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub

			try {

				//Log.i(ConstantsSetting.EfficiencyTestTag, "begin to call DrugStore detail Logic.");
				
				if(innerLocationProvider != null){
					station = innerLocationProvider.getLocation();
					if (station.latitude == 0.0 && station.longitude == 0.0) {
						innerLocationProvider.updateListener();
					    station = innerLocationProvider.getLocation();
					}
					if (station.latitude == 0.0 && station.longitude == 0.0) {
					       return 3;
					}
				} else {
					return 3;
				}
				latitude = station.latitude;
				longitude = station.longitude;
				
				storeinfolist = DrugStore.getInfo(latitude, longitude,
						drugstoreid);
				if (storeinfolist != null) {
					return (storeinfolist.size() > 0) ? 0 : 1;
				} else {
					return 2;// 网络连接错误
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return 2;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if (result == 0)//
			{
				initData(storeinfolist.get(0));
				drugstore_Linear_info.setVisibility(View.VISIBLE);
				actionbar_tomap_btn.setVisibility(View.VISIBLE);

				async_begin.setVisibility(View.GONE);

				// 异步初始化药店收藏状态
				new AsyLoadDrugStoreIsCollected().execute();
				//加载活动状态的
				new AsyncLoadPromotion().execute();
				
				//Log.i(ConstantsSetting.EfficiencyTestTag, "DrugStore detail UI show completed.");
				
			} else if (result == 1) {
				async_begin.setVisibility(View.GONE);
				LinearLayout async_error = (LinearLayout) findViewById(R.id.async_error);
				TextView async_error_txt = (TextView) async_error
						.findViewById(R.id.async_error_txt);
				async_error_txt.setText("没有找到当前药店的信息。");
				Button async_error_reflesh = (Button) async_error
						.findViewById(R.id.async_error_reflesh);
				async_error_reflesh.setVisibility(View.GONE);
				async_error.setVisibility(View.VISIBLE);
			} else if (result == 2) {
				async_begin.setVisibility(View.GONE);
				async_error.setVisibility(View.VISIBLE);
				TextView async_error_txt = (TextView) async_error
						.findViewById(R.id.async_error_txt);
				async_error_txt.setText(R.string.error_nonetwork);
				Button async_error_reflesh = (Button) async_error
						.findViewById(R.id.async_error_reflesh);
				async_error_reflesh.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						async_error.setVisibility(View.GONE);
						async_begin.setVisibility(View.VISIBLE);
						new AsyncLoad().execute(drugstoreid);
					}
				});
			} else if (result == 3) {
				async_begin.setVisibility(View.GONE);
				async_error.setVisibility(View.VISIBLE);
				TextView async_error_txt = (TextView) async_error
						.findViewById(R.id.async_error_txt);
				async_error_txt.setText(R.string.error_location);
				Button async_error_reflesh = (Button) async_error
						.findViewById(R.id.async_error_reflesh);
				async_error_reflesh.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						async_error.setVisibility(View.GONE);
						async_begin.setVisibility(View.VISIBLE);
						new AsyncLoad().execute(drugstoreid);
					}
				});
			}
		}
	}
	
	/**
	 * 异步加载促销活动信息
	 */
	private class AsyncLoadPromotion extends
			AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerlist;

		@Override
		protected Integer doInBackground(String... params) {
			try {
				
				//Log.i(ConstantsSetting.EfficiencyTestTag, "begin to call DrugStore promotion Logic.");
				
				innerlist = coolbuy360.logic.Promotion.getListByDrugStore(
						drugstoreid, 5, 1);
				if (innerlist != null) {
					return (innerlist.size() > 0) ? 0 : 1;
				} else {
					return 2;
				}
			} catch (TimestampException ex) {
				return -2;
			} catch (Exception ex) {
				return 2;
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			drugstore_detail_llt_promotion.setVisibility(View.GONE);
			drugstore_detail_loader_promotion.setVisibility(View.VISIBLE);
		}

		@Override
		// 处理界面
		protected void onPostExecute(Integer result) {
			if (result == 0) {
				initPromotion(innerlist);
				drugstore_detail_loader_promotion.setVisibility(View.GONE);
				drugstore_detail_llt_promotion.setVisibility(View.VISIBLE);
				startPromotion();				
			} else if (result == 1) {
				drugstore_detail_loader_promotion.setVisibility(View.GONE);
			} else {
				drugstore_detail_loader_promotion.setVisibility(View.GONE);
			}

			//Log.i(ConstantsSetting.EfficiencyTestTag, "DrugStore promotion UI show completed.");
		}
	}

	private void initPromotion(List<Map<String, String>> sourceList) {
		// Bitmap image=
		// BitmapFactory.decodeResource(getResources(),R.drawable.icon);
		// imagesCache.put("background_non_load",image); //设置缓存中默认的图片

		myImagesGallary = (ADGallery) findViewById(R.id.dst_detail_gallery_promotion);
		myImagesGallary.setImageActivity(this);

		PromotionGalleryAdapter galleryAdapter = new PromotionGalleryAdapter(this, sourceList);
		myImagesGallary.setAdapter(galleryAdapter);
		
		//生成幻灯片索引小圆点
		LinearLayout pointLinear = (LinearLayout) findViewById(R.id.dst_detail_promotion_point);
		for (int i = 0; i < sourceList.size(); i++) {
			ImageView pointView = new ImageView(this);
			if (i == 0) {
				pointView
						.setBackgroundResource(R.drawable.activity_feature_point_cur);
			} else
				pointView
						.setBackgroundResource(R.drawable.activity_feature_point);
			pointLinear.addView(pointView);
		}
		
		myImagesGallary.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// System.out.println(arg2+"arg2");
				
				//Log.i("promotion", "itemclick"+arg3);
				
				/*Dialog dialog = new Dialog(DrugStoreDetaill.this);
            	dialog.setTitle("点击"+arg3);
            	dialog.setCanceledOnTouchOutside(true);
            	dialog.show();*/
				
				Intent intent = new Intent().setClass(DrugStoreDetaill.this,
						DrugStorePromotionDetail.class);
				intent.putExtra("promotionid", arg3);
				DrugStoreDetaill.this.startActivity(intent);
			}
		});	
	}
	
	private void startPromotion() {
		Message msg = new Message();
		Bundle date = new Bundle();// 存放数据
		date.putInt("pos", 0);
		msg.setData(date);
		msg.what = 1;// 消息标识
		autoGalleryHandler.sendMessage(msg);
		
		timeTaks = new ImageTimerTask();
		autoGallery.scheduleAtFixedRate(timeTaks, 5000, 5000);
		timeThread = new Thread() {
			public void run() {
				while (!isExit) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					synchronized (timeTaks) {
						if (!timeFlag) {
							timeTaks.timeCondition = true;
							timeTaks.notifyAll();
						}
					}
					timeFlag = true;
				}
			};
		};
		timeThread.start();
	}
	
	/**
	 * 异步判断药店的收藏状态
	 */
	private class AsyLoadDrugStoreIsCollected extends
			AsyncTask<Integer, Void, Integer> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub

			try {

				//Log.i(ConstantsSetting.EfficiencyTestTag, "begin to call DrugStore favorite state Logic.");
				
				ischecked = DrugStore
						.isCollected(getBaseContext(), drugstoreid);
				if (ischecked != null) {
					return 0;
				} else {
					return 2;// 网络连接错误
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return 2;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if (result == 0) {
				if (ischecked) {
					collect_checkbox.setChecked(true);
				} else {
					collect_checkbox.setChecked(false);
				}
				collect_checkbox
						.setOnCheckedChangeListener(new chkOnCheckedChangeListener());
			} else if (result == 2) {
				collect_checkbox.setChecked(false);
				collect_checkbox
						.setOnCheckedChangeListener(new chkOnCheckedChangeListener());
				Toast.makeText(DrugStoreDetaill.this, "获取收藏状态失败",
						Toast.LENGTH_SHORT).show();
			}
			
			//Log.i(ConstantsSetting.EfficiencyTestTag, "DrugStore favorite state UI show completed.");
		}
	}

	/**
	 * 收藏状态改变的时候操作
	 * 
	 * @author Administrator
	 * 
	 */
	public class chkOnCheckedChangeListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			ischecked = collect_checkbox.isChecked();
			if (ischecked) {
				new AsyncCollect().execute(drugstoreid, "do");
			} else {
				new AsyncCollect().execute(drugstoreid, "undo");
			}
		}

	}

	private final class callOnclick implements
			android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ call));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}

	}

	private final class phoneOnclick implements
			android.view.View.OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ phone));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
	}

	/**
	 * 点击进入地图
	 */
	private final class changeOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Bundle innerbundle = new Bundle();
			innerbundle.putString("drugstoreid", drugstoreid);
						
			if (returnMode.equals(ReturnMode.NeedResult)) {				
				Intent resultIntent = new Intent();
				resultIntent.putExtras(innerbundle);
				DrugStoreDetaill.this.setResult(RESULT_OK, resultIntent);
				DrugStoreDetaill.this.finish();
			} else {
				if(loadMode.equals(LoadMode.PositionByDrug)){
					innerbundle.putString("drugid", bundle.getString("drugid"));
				}
				innerbundle.putString("loadmode", loadMode.toString());
				innerbundle.putString("returnmode", ReturnMode.NeedResult.toString());
				Intent mapintent = new Intent().setClass(DrugStoreDetaill.this,
						DrugStoreMap.class);
				mapintent.putExtras(innerbundle);			
				startActivityForResult(mapintent, 1);
			}
		}
	}

	/**
	 * 异步收藏的操作
	 * 
	 * @author Administrator
	 * 
	 */
	private class AsyncCollect extends AsyncTask<String, Void, Boolean> {
		CommandResult resultmessage;

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub

			try {

				//Log.i(ConstantsSetting.EfficiencyTestTag, "begin to call DrugStore favorite action Logic.");
				
				if (params.length > 0) {
					if (params[1].toString().equals("do")) {
						resultmessage = DrugStore.doCollect(getBaseContext(),
								params[0]);
						return resultmessage.getResult();
					} else {
						resultmessage = DrugStore.unCollect(getBaseContext(),
								params[0]);
						return resultmessage.getResult();
					}

				} else {
					return false;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if (result)//
			{
				Toast.makeText(getBaseContext(), "操作成功", Toast.LENGTH_SHORT).show();
				try {
					int addscore = Integer.parseInt(resultmessage
							.getValue("addscore"));
					if (addscore != 0) {
						showScoreChange(R.id.collect_btn, addscore);
					}
				} catch (Exception e) {
				}
				
				//Log.i(ConstantsSetting.EfficiencyTestTag, "DrugStore favorite action do complete.");
				
			} else {
				Toast.makeText(getBaseContext(), resultmessage.getMessage(), Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
   
	public void changePointView(int cur) {
		LinearLayout pointLinear = (LinearLayout) findViewById(R.id.dst_detail_promotion_point);
		View view = pointLinear.getChildAt(positon);
		View curView = pointLinear.getChildAt(cur);
		if (view != null && curView != null) {
			ImageView pointView = (ImageView) view;
			ImageView curPointView = (ImageView) curView;
			pointView.setBackgroundResource(R.drawable.activity_feature_point);
			curPointView
					.setBackgroundResource(R.drawable.activity_feature_point_cur);
			positon = cur;
		}
	}

	final Handler autoGalleryHandler = new Handler() {
		public void handleMessage(Message message) {
			super.handleMessage(message);
			switch (message.what) {
			case 1:
				myImagesGallary.setSelection(message.getData().getInt("pos"));
				break;
			}
		}
	};

	public class ImageTimerTask extends TimerTask {
		public volatile boolean timeCondition = true;

		// int gallerypisition = 0;
		public void run() {
			synchronized (this) {
				while (!timeCondition) {
					try {
						Thread.sleep(5000);
						wait();
					} catch (InterruptedException e) {
						Thread.interrupted();
					}
				}
			}
			try {
				gallerypisition = myImagesGallary.getSelectedItemPosition() + 1;
				System.out.println(gallerypisition + "");
				Message msg = new Message();
				Bundle date = new Bundle();// 存放数据
				date.putInt("pos", gallerypisition);
				msg.setData(date);
				msg.what = 1;// 消息标识
				autoGalleryHandler.sendMessage(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 显示积分变化弹窗
	 * @param parentViewID
	 * @param value
	 */
	private void showScoreChange(int parentViewID, int value) {
		ScoreChangePopup popupWindow = new ScoreChangePopup(this, value);
		popupWindow.showAtLocation(findViewById(parentViewID),
				Gravity.CENTER, 0, 0);
		popupWindow.delayedDismiss();
	}
}
