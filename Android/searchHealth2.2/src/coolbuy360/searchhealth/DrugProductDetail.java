package coolbuy360.searchhealth;

import java.util.List;
import java.util.Map;

import com.baidu.location.BDLocationListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import coolbuy360.adapter.detailListViewAdapter;
import coolbuy360.control.MyShowImgDialog;
import coolbuy360.logic.AppConfig;
import coolbuy360.logic.ConstantsSetting;
import coolbuy360.logic.Drug;
import coolbuy360.service.AaynImageLoaderUtil;
import coolbuy360.service.ImageManager;
import coolbuy360.service.LocationInfo;
import coolbuy360.service.LocationProvider;
import coolbuy360.service.SetImgResoruce;
import coolbuy360.service.Util;
import coolbuy360.service.searchApp;

@SuppressLint("NewApi")
public class DrugProductDetail extends Activity {
	private ListView drugstorelist_listview;
	private Button loadMoreButton;
	private View loadMoreView;
	private ProgressBar proBar;
	private TextView protxt;
	private LinearLayout p_detail_relativelayouy;
	private ImageButton p_detaillocal_btn_P;
	private ImageButton p_detailinstruction_btn_P;

	String drug_name = null;
	String drug_id = null;
	String drug_store = null;
	String drug_imgurl = null;
	String drug_otc = null;
	String drug_h = null;
	String drug_bao = null;
	String serisecode = null;
	Bundle bundle = null;
	private int pageIndex = 0;
	private int pagesize = ConstantsSetting.QLDefaultPageSize;
	private boolean isloading;
	private String url_path_s;
	private String url_path_b;

	int searchRang;
	int ishc;

	LinearLayout async_begin;
	LinearLayout async_error;

	private LocationProvider innerLocationProvider = null;
	LocationInfo.SItude station = null;
	
	/*protected double latitude = 30.280506;
	protected double longitude = 120.107582;*/
	protected double latitude = 0.0;
	protected double longitude = 0.0;

	private detailListViewAdapter adapter;
	BDLocationListener mLocationListener = null;// create时注册此listener，Destroy时需要Remove

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		innerLocationProvider = searchApp.mLocationProvider;
		// 为退出做准备
		searchApp.getInstance().addActivity(this);
		setContentView(R.layout.drugproduct_detail);
		/******************* 获取上级传过来的值 *********************/
		bundle = getIntent().getExtras();
		drug_name = bundle.getString("drugname");
		drug_id = bundle.getString("drugid");
		drug_store = bundle.getString("drugstore");
		drug_imgurl = bundle.getString("drugimg");
		drug_h = bundle.getString("h");
		drug_otc = bundle.getString("otc");
		drug_bao = bundle.getString("bao");
		serisecode = bundle.getString("serisecode");
		
		this.url_path_s = Util.getDrugSmallImgPath();
		this.url_path_b = Util.getDrugBigImgPath();

		p_detaillocal_btn_P = (ImageButton) this
				.findViewById(R.id.p_detaillocal_btn_P);
		p_detailinstruction_btn_P = (ImageButton) this
				.findViewById(R.id.p_detailinstruction_btn_P);
		p_detaillocal_btn_P.setVisibility(View.GONE); // 隐藏地图模式按钮
		p_detailinstruction_btn_P.setVisibility(View.GONE); // 隐藏说明书按钮

		/*** 加载更多的按钮 ***/
		loadMoreView = getLayoutInflater().inflate(R.layout.p_druglist_foot,
				null);
		loadMoreButton = (Button) loadMoreView
				.findViewById(R.id.p_listview_footbtn);
		/***** 显示loading过程 *****/
		proBar = (ProgressBar) loadMoreView.findViewById(R.id.p_probar);
		protxt = (TextView) loadMoreView.findViewById(R.id.p_protxt);
		drugstorelist_listview = (ListView) this.findViewById(R.id.drugstorelist_listview);
		drugstorelist_listview.addFooterView(loadMoreView);//

		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DrugProductDetail.this.finish();
			}
		});

		p_detail_relativelayouy = (LinearLayout) this
				.findViewById(R.id.p_detail_relativelayouy);
		async_begin = (LinearLayout) findViewById(R.id.async_begin);
		async_begin.setVisibility(View.VISIBLE);
		async_error = (LinearLayout) findViewById(R.id.async_error);
		// 获得当前
		AppConfig config = new AppConfig(getBaseContext());
		searchRang = config.getStore_SearchRange(getBaseContext());
		ishc = config.getStore_IsHC(getBaseContext());
		
		/********* 判断是否从条码扫描进入 *********/
		if (serisecode == null) {
			new AsyLoadStore().execute(1);
			initdata(drug_name, drug_store, drug_imgurl);
			p_detailinstruction_btn_P.setVisibility(View.VISIBLE); // 显示说明书按钮
		} else {
			new AsyLoad().execute();
		}
		
		// RelativeLayout点击事件
		p_detail_relativelayouy.setOnClickListener(new descriptionOnclick());
		ImageButton btnDescription = (ImageButton) this
				.findViewById(R.id.p_detailinstruction_btn_P);
		btnDescription.setOnClickListener(new descriptionOnclick());
		ImageButton btnLocal = (ImageButton) this
				.findViewById(R.id.p_detaillocal_btn_P);
		// 定位按钮事件
		btnLocal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mapintent = new Intent().setClass(
						DrugProductDetail.this, DrugStoreMap.class);
				Bundle innerbundle = new Bundle();
				innerbundle.putString("loadmode", DrugStoreMap.LoadMode.ShowByDrug.toString());
				innerbundle.putString("drugid", drug_id);
				mapintent.putExtras(innerbundle);
				startActivity(mapintent);
			}
		});
	}
	
	/**
	 * RelativeLayout点击事件
	 * 
	 * @author chenlw
	 * 
	 */
	private final class descriptionOnclick implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			bundle = new Bundle();
			bundle.putString("drugid", drug_id);
			bundle.putString("drugname", drug_name);
			bundle.putString("drugstore", drug_store);
			bundle.putString("drugimg", drug_imgurl);

			bundle.putString("h", drug_h);
			bundle.putString("otc", drug_otc);
			bundle.putString("bao", drug_bao);
			Intent DescriptionIntent = new Intent().setClass(
					DrugProductDetail.this, DrugProductDescription.class);
			DescriptionIntent.putExtras(bundle);
			startActivity(DescriptionIntent);
		}
	}

	/**
	 *初始化数据
	 */
	private void initdata(String name, String store, String imgurl) {
		final String timgurl = imgurl;
		
		// TODO Auto-generated method stub
		TextView drug_title = (TextView) this.findViewById(R.id.p_detail_title);
		TextView drug_store = (TextView) this.findViewById(R.id.p_detail_store);
		ImageView drug_img = (ImageView) this.findViewById(R.id.p_detail_img);

		ImageView p_detail_bao = (ImageView) this
				.findViewById(R.id.p_detail_bao);
		ImageView p_detail_h = (ImageView) this.findViewById(R.id.p_detail_h);
		ImageView p_detail_otc = (ImageView) this
				.findViewById(R.id.p_detail_otc);

		drug_title.setText(name);// 显示名称
		drug_store.setText(store);// 显示店名
		// 下面的显示药品属性相关信息的图标

		if (drug_h != null & !drug_h.equals("")) {
			int h = SetImgResoruce.imgResource(drug_h.trim());
			if (h != 0) {
				p_detail_h.setVisibility(View.VISIBLE);
				p_detail_h.setImageResource(h);
			}
		}

		if (drug_bao != null & !drug_bao.equals("")) {
			int bao = SetImgResoruce.imgResurce_bao(drug_bao.trim());
			if (bao != 0) {
				p_detail_bao.setVisibility(View.VISIBLE);
				p_detail_bao.setImageResource(bao);
			}
		}

		if (drug_otc != null & !drug_otc.equals("")) {
			int otc = SetImgResoruce.imageResurce_otc(Integer.parseInt(drug_otc
					.trim()));
			if (otc != 0) {
				p_detail_otc.setVisibility(View.VISIBLE);
				p_detail_otc.setImageResource(otc);
			}
		}
		
		if (imgurl != null && !imgurl.equals("")) {
			// 加载图片的方法
			/*AaynImageLoaderUtil asynImageLoader = new AaynImageLoaderUtil();
			asynImageLoader.showImageAsyn(drug_img, imgurl, R.drawable.loading);*/
			Log.i("chenlinwei", url_path_s + "" + imgurl);
			// 设置图片大图的路径
			ImageManager.from(this).displayImage(drug_img, url_path_s + imgurl,R.drawable.drug_photo_def_pic,150,150);
			
			// 显示大图
			drug_img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String url= url_path_b + timgurl;
					Bundle bundle = new Bundle();
					bundle.putString("imgpath", url);
					Log.i("chenlinwei", url + "::clss");

					Intent intent = new Intent().setClass(DrugProductDetail.this,
							MyShowImgDialog.class);
					intent.putExtras(bundle);
					DrugProductDetail.this.startActivity(intent);
				}
			});
		} else {
			drug_img.setImageResource(R.drawable.drug_photo_def_pic);
		}
	}

	/**
	 * 加载更多按钮点击事件 -moreonclick
	 */
	private final class moreonclick implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			loadMoreButton.setVisibility(View.GONE);
			proBar.setVisibility(View.VISIBLE);
			protxt.setVisibility(View.VISIBLE);
			new AsynLoader_more().execute();
		}
	}

	private void InitData(List<Map<String, String>> storelist) {
		// 获得当前数据
		adapter = new detailListViewAdapter(this, storelist, drug_id);
		adapter.count = storelist.size();
		if (storelist.size() < pagesize) {
			drugstorelist_listview.removeFooterView(loadMoreView);
		}
		drugstorelist_listview.setVisibility(View.VISIBLE);
		drugstorelist_listview.setAdapter(adapter);
		//loadMoreButton.setOnClickListener(new moreonclick());
		drugstorelist_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int posion,
					long arg3) {
				// TODO Auto-generated method stub
				
				//FootView Item 点击
				if (arg3 == -1)
					return;
				
				Map<String, String> map = (Map<String, String>) drugstorelist_listview
						.getItemAtPosition(posion);
				String storeid = map.get("drugstoreid");

				Bundle innerbundle = new Bundle();
				innerbundle.putString("loadmode", DrugStoreMap.LoadMode.PositionByDrug.toString());
				innerbundle.putString("drugstoreid", storeid);
				innerbundle.putString("drugid", drug_id);
				Intent storeIntent = new Intent(DrugProductDetail.this,
						DrugStoreDetaill.class);
				storeIntent.putExtras(innerbundle);
				//startActivityForResult(storeIntent, requestCode)
				startActivity(storeIntent);
			}
		});

		drugstorelist_listview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (firstVisibleItem + visibleItemCount == totalItemCount) {
					if (!isloading)// 如果没有加载中
					{
						loadMoreButton.setVisibility(View.GONE);
						proBar.setVisibility(View.VISIBLE);
						protxt.setVisibility(View.VISIBLE);
						
						new AsynLoader_more().execute();
					} else {
						// drugstorelist_listview.removeFooterView(loadMoreView);//
					}
				}
			}
		});
	}

	/**
	 * 异步加载更改多的方法
	 */
	private class AsynLoader_more extends
			AsyncTask<List<Map<String, String>>, Void, Integer> {
		List<Map<String, String>> innerdruglist;

		// 0 代表数据成功 ；1 代表数据为空或为null ； 2代表网络连接错误
		@Override
		protected Integer doInBackground(List<Map<String, String>>... params) {
			// TODO Auto-generated method stub
			try {
				if(innerLocationProvider != null){
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
				innerdruglist = getData();
				if (innerdruglist != null) {
					return (innerdruglist.size() > 0) ? 0 : 1;
				} else {
					return 2;// 网络连接错误
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return 2;
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isloading = true;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			// StrictModeWrapper.init(getApplicationContext());
			if (result == 0) {
				for (Map<String, String> item : innerdruglist) {
					adapter.addItem(item);
				}
				if (innerdruglist.size() < pagesize) {
					loadMoreView.setVisibility(View.GONE);
				} else {
					loadMoreButton.setText("更多");
					loadMoreButton.setVisibility(View.VISIBLE);
				}
				proBar.setVisibility(View.GONE);
				protxt.setVisibility(View.GONE);// 隐藏progressbar
				isloading = false;
				adapter.count += innerdruglist.size();
				adapter.notifyDataSetChanged();
				pageIndex++;
				return;
			} else if (result == 1) // 如果数据加载完后
			{
				if (innerdruglist.size() < pagesize) {
					loadMoreView.setVisibility(View.GONE);
				} else {
					loadMoreButton.setVisibility(View.VISIBLE);
				}
				// loadMoreButton.setVisibility(View.VISIBLE);
				loadMoreButton.setBackgroundColor(getResources().getColor(
						R.color.transparent));
				loadMoreButton.setClickable(false);
				loadMoreButton.setText("没有更多数据！");
				proBar.setVisibility(View.GONE); // 隐藏progressbar
				protxt.setVisibility(View.GONE);
				// isloading = false;
				return;
			} else if (result == 2) {
				loadMoreButton.setVisibility(View.VISIBLE);
				loadMoreButton.setBackgroundColor(getResources().getColor(
						R.color.transparent));
				loadMoreButton.setText("网络连接错误");
				proBar.setVisibility(View.GONE);// 隐藏progressbar
				protxt.setVisibility(View.GONE);
				isloading = false;
				return;
			} else if (result == 3) {
				loadMoreButton.setVisibility(View.VISIBLE);
				loadMoreButton.setBackgroundColor(getResources().getColor(
						R.color.transparent));
				loadMoreButton.setText("手机定位失败");
				proBar.setVisibility(View.GONE);// 隐藏progressbar
				protxt.setVisibility(View.GONE);
				isloading = false;
				return;
			}
		}

	}

	/**
	 * 异步加载药品信息数据
	 */
	private class AsyLoad extends
			AsyncTask<List<Map<String, String>>, Void, Integer> {
		List<Map<String, String>> innerdruglist;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			p_detail_relativelayouy.setVisibility(View.GONE);
		}

		@Override
		protected Integer doInBackground(List<Map<String, String>>... params) {
			// TODO Auto-generated method stub

			try {

				if (serisecode != null) {
					innerdruglist = Drug.getListByBarCode(serisecode, 0, 0);

					if (innerdruglist != null) {
						return (innerdruglist.size() > 0) ? 0 : 1;
					} else {
						return 2;// 网络连接错误
					}
				} else {
					return 2;
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
				p_detail_relativelayouy.setVisibility(View.VISIBLE);

				drug_name = innerdruglist.get(0).get("drugname");
				drug_store = innerdruglist.get(0).get("enterprisename");
				drug_imgurl = innerdruglist.get(0).get("drugimg");
				drug_otc = innerdruglist.get(0).get("prescriptiontype");
				drug_h = innerdruglist.get(0).get("approvaltype");
				drug_bao = innerdruglist.get(0).get("ishcdrug");
				drug_id = innerdruglist.get(0).get("drugid");

				initdata(drug_name, drug_store, drug_imgurl);
				async_begin.setVisibility(View.GONE);
				async_error = (LinearLayout) findViewById(R.id.async_error);
				async_error.setVisibility(View.GONE);
				p_detaillocal_btn_P.setVisibility(View.GONE); // 隐藏地图模式按钮
				p_detailinstruction_btn_P.setVisibility(View.VISIBLE); // 显示说明书按钮
				new AsyLoadStore().execute();
			} else if (result == 1) {
				p_detail_relativelayouy.setVisibility(View.GONE);
				async_begin.setVisibility(View.GONE);
				TextView async_error_txt = (TextView) async_error
						.findViewById(R.id.async_error_txt);
				async_error_txt.setText("没有找到您查询的药品信息。");
				Button async_error_reflesh = (Button) async_error
						.findViewById(R.id.async_error_reflesh);
				async_error_reflesh.setVisibility(View.GONE);
				async_error.setVisibility(View.VISIBLE);
				p_detaillocal_btn_P.setVisibility(View.GONE); // 隐藏地图模式按钮
				p_detailinstruction_btn_P.setVisibility(View.GONE); // 隐藏说明书按钮
			} else if (result == 2) {
				p_detail_relativelayouy.setVisibility(View.GONE);
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
						new AsyLoad().execute();
					}
				});
				p_detaillocal_btn_P.setVisibility(View.GONE); // 隐藏地图模式按钮
				p_detailinstruction_btn_P.setVisibility(View.GONE); // 隐藏说明书按钮
				return;
			}

		}

	}

	/**
	 * 异步加载附近药店数据
	 */
	private class AsyLoadStore extends AsyncTask<Integer, Void, Integer> {
		List<Map<String, String>> innerdruglist;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pageIndex = 0;// 重置页面索引，防止点击更多时误导此数据
			loadMoreButton.setText("更多");
			loadMoreView.setVisibility(View.VISIBLE);
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			try {
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
				innerdruglist = getData();
				if (innerdruglist != null) {
					return (innerdruglist.size() > 0) ? 0 : 1;
				} else {
					return 2;// 网络连接错误
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.i("chenlinwei", e.getMessage() + "");
				return 2;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if (result == 0) {
				InitData(innerdruglist);
				async_begin.setVisibility(View.GONE);
				pageIndex++;
				isloading = false;
				p_detaillocal_btn_P.setVisibility(View.VISIBLE); // 显示地图模式按钮
				p_detailinstruction_btn_P.setVisibility(View.VISIBLE); // 显示说明书按钮
			} else if (result == 1) {
				async_begin.setVisibility(View.GONE);
				TextView async_error_txt = (TextView) async_error
						.findViewById(R.id.async_error_txt);
				async_error_txt.setText("附近没有找到出售此药品的药店。");
				Button async_error_reflesh = (Button) async_error
						.findViewById(R.id.async_error_reflesh);
				async_error_reflesh.setVisibility(View.GONE);
				async_error.setVisibility(View.VISIBLE);
				p_detaillocal_btn_P.setVisibility(View.GONE); // 隐藏地图模式按钮
				p_detailinstruction_btn_P.setVisibility(View.VISIBLE); // 显示说明书按钮
			} else {
				async_begin.setVisibility(View.GONE);
				async_error.setVisibility(View.VISIBLE);
				TextView async_error_txt = (TextView) async_error
						.findViewById(R.id.async_error_txt);
				if (result == 3){
					async_error_txt.setText(R.string.error_location);
				} else {
					async_error_txt.setText(R.string.error_nonetwork);
				}
				Button async_error_reflesh = (Button) async_error
						.findViewById(R.id.async_error_reflesh);
				async_error_reflesh.setVisibility(View.VISIBLE);
				async_error_reflesh.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						async_error.setVisibility(View.GONE);
						async_begin.setVisibility(View.VISIBLE);
						new AsyLoadStore().execute();
					}
				});
				isloading = false;
				p_detaillocal_btn_P.setVisibility(View.GONE); // 隐藏地图模式按钮
				p_detailinstruction_btn_P.setVisibility(View.VISIBLE); // 显示说明书按钮				
			} 
		}
	}
	
	/**
	 * 根据筛选条件获取相应的药店数据
	 * @return
	 */
	private List<Map<String, String>> getData(){		
		List<Map<String, String>> innerlist = null;
		latitude = station.latitude;
		longitude = station.longitude;
		String cityName = station.city;
		
		if (searchRang == -1) {
			innerlist = Drug.whereToBuy(drug_id, cityName, latitude,
					longitude, ishc, pagesize, pageIndex + 1);
		} else {
			innerlist = Drug.whereToBuy(drug_id, searchRang,
					latitude, longitude, ishc, pagesize, pageIndex + 1);
		}
		return innerlist;
	}
}
