/**
 * 
 */
package coolbuy360.searchhealth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.MapView.LayoutParams;
import com.baidu.mapapi.utils.CoordinateConvert;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import coolbuy360.logic.AppConfig;
import coolbuy360.logic.Drug;
import coolbuy360.service.LocationInfo;
import coolbuy360.service.LocationProvider;
import coolbuy360.service.TimestampException;
import coolbuy360.service.searchApp;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author yangxc
 * 
 */
public class DrugStoreMap extends Activity {

	final static String TAG = "MainActivty";
	MapView mMapView = null;

	public MKMapViewListener mMapListener = null;
	FrameLayout mMapViewContainer = null;
	
	LoadMode loadMode = LoadMode.ShowAll;
	ReturnMode returnMode = ReturnMode.Normal;
	Bundle bundle = null;

	/*Button testItemButton = null;
	Button removeItemButton = null;
	Button removeAllItemButton = null;
	EditText indexText = null;*/
	//int index = 0;
	
	private LocationProvider innerLocationProvider = null;
	LocationInfo.SItude station = null;
	
	/*protected double latitude = 30.280506;
	protected double longitude = 120.107582;*/
	protected double latitude = 0.0;
	protected double longitude = 0.0;	

	MyLocationOverlay myLocationOverlay = null;
	LocationData locData = null;

	LinearLayout async_begin;
	LinearLayout async_error;
	TextView location_address_txt;
	ImageView location_address_ico;

	/**
	 * 圆心经纬度坐标
	 */
	/*int cLat = 39909230;
	int cLon = 116397428;*/
	// 存放overlayitem
	public List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
	// 存放药店ID
	public List<String> idlist = new ArrayList<String>();
	// 存放overlay图片
	//public List<Drawable> res = new ArrayList<Drawable>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		innerLocationProvider = searchApp.mLocationProvider;
		searchApp app = (searchApp) this.getApplication();
		if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(getApplicationContext());
            /**
             * 如果BMapManager没有初始化则初始化BMapManager
             */
            app.mBMapManager.init(new searchApp.MyGeneralListener());
        }
		setContentView(R.layout.drugstoremap);
		mMapView = (MapView) findViewById(R.id.bmapView);
		initMapView();
		mMapView.getController().setZoom(13);
		mMapView.getController().enableClick(true);
		mMapView.setBuiltInZoomControls(true);
		/*testItemButton = (Button) findViewById(R.id.button1);
		removeItemButton = (Button) findViewById(R.id.button2);
		removeAllItemButton = (Button) findViewById(R.id.button3);*/

		/*OnClickListener clickListener = new OnClickListener() {
			public void onClick(View v) {
				testItemClick();
			}
		};
		OnClickListener removeListener = new OnClickListener() {
			public void onClick(View v) {
				testRemoveItemClick();
			}
		};
		OnClickListener removeAllListener = new OnClickListener() {
			public void onClick(View v) {
				testRemoveAllItemClick();
			}
		};*/

		/*testItemButton.setOnClickListener(clickListener);
		removeItemButton.setOnClickListener(removeListener);
		removeAllItemButton.setOnClickListener(removeAllListener);*/
		
		mMapListener = new MKMapViewListener() {

			@Override
			public void onMapMoveFinish() {
				// TODO Auto-generated method stub
				Log.d("hjtest", "hjtest" + "onMapMoveFinish");
			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				// TODO Auto-generated method stub
				String title = "";
				if (mapPoiInfo != null) {
					title = mapPoiInfo.strText;
					Toast.makeText(DrugStoreMap.this, title,
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				// TODO Auto-generated method stub
				//Log.d("hjtest", "hjtest" + "getmap OK");
			}

			@Override
			public void onMapAnimationFinish() {
				// TODO Auto-generated method stub
				//Log.d("hjtest", "hjtest" + "onMapAnimationFinish");
			}

			@Override
			public void onMapLoadFinish() {
				// TODO 自动生成的方法存根
				
			}
		};
		mMapView.regMapViewListener(searchApp.getInstance().mBMapManager,
				mMapListener);

		async_begin = (LinearLayout) findViewById(R.id.async_begin);
		async_begin.setVisibility(View.VISIBLE);
		async_error = (LinearLayout) findViewById(R.id.async_error);
		location_address_txt = (TextView) findViewById(R.id.location_address_txt);
		location_address_ico = (ImageView) findViewById(R.id.location_address_ico);
		
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DrugStoreMap.this.finish();
			}
		});
		
		/*// overlay 数量
		int iSize = 9;
		double pi = 3.1415926;
		// overlay半径
		int r = 50000;
		// 准备overlay 数据
		for (int i = 0; i < iSize; i++) {
			int lat = (int) (cLat + r * Math.cos(2 * i * pi / iSize));
			int lon = (int) (cLon + r * Math.sin(2 * i * pi / iSize));
			OverlayItem item = new OverlayItem(new GeoPoint(lat, lon), "item"
					+ i, "item" + i);
			item.setMarker(getResources().getDrawable(R.drawable.position_center));
			mGeoList.add(item);
		}*/
		
		//testItemClick();
		
		try {
			bundle = getIntent().getExtras();
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (bundle != null) {
			loadMode = DrugStoreMap.loadModeTransform(bundle.getString("loadmode"), LoadMode.ShowAll);
			returnMode = DrugStoreMap.returnModeTransform(bundle.getString("returnmode"), ReturnMode.Normal);
		}
		
		new AsyncLoader_GuessInfo().execute();
	}

	@Override
    protected void onPause() {
		mMapView.setVisibility(View.INVISIBLE);
        mMapView.onPause();
        super.onPause();
    }
    
    @Override
    protected void onResume() {
		mMapView.setVisibility(View.VISIBLE);
        mMapView.onResume();
        super.onResume();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	mMapView.onSaveInstanceState(outState);
    	
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	mMapView.onRestoreInstanceState(savedInstanceState);
    }

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		/*searchApp app = (searchApp) this.getApplication();
		if (app.mBMapManager != null) {
			app.mBMapManager.destroy();
			app.mBMapManager = null;
		}*/
		super.onDestroy();
	}

	private void initMapView() {
        mMapView.setLongClickable(true);
        //mMapController.setMapClickEnable(true);
        //mMapView.setSatellite(false);
    }

	/*public void testRemoveAllItemClick() {
		mMapView.getOverlays().clear();
		mMapView.refresh();

	}*/

	/*public void testRemoveItemClick() {
		int n = (int) (Math.random() * (mGeoList.size() - 1));
		Drawable marker = DrugStoreMap.this.getResources().getDrawable(
				R.drawable.position_center);
		mMapView.getOverlays().clear();

		OverlayTest ov = new OverlayTest(marker, this);
		for (int i = 0; i < mGeoList.size(); i++) {
			if (i != n)
				ov.addItem(mGeoList.get(i));
		}
		mMapView.getOverlays().add(ov);

		mMapView.refresh();
		mMapView.getController().setCenter(new GeoPoint(cLat, cLon));
	}*/	
		
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.activity_main, menu);
		return false;
	}*/

	/**
	 * 药店信息覆盖层
	 * @author yangxc
	 *
	 */
	static class OverlayTest extends ItemizedOverlay<OverlayItem> {		
		public List<OverlayItem> innermGeoList = null;
		private Context mContext = null;
	    static PopupOverlay pop = null;
		private Button mBtn = null;
		private View mPopView = null;
		Toast mToast = null;

		public OverlayTest(Drawable marker, Context context, MapView mapView) {
			super(marker, mapView);
			this.mContext = context;
			pop = new PopupOverlay(((DrugStoreMap)mContext).mMapView,
					new PopupClickListener() {

						@Override
						public void onClickedPopup(int index) {
							/*if (null == mToast)
								mToast = Toast
										.makeText(mContext, "popup item :"
												+ index + " is clicked.",
												Toast.LENGTH_SHORT);
							else
								mToast.setText("popup item :" + index
										+ " is clicked.");
							mToast.show();*/
							
							/*Bundle innerbundle = new Bundle();
							innerbundle.putString("drugstoreid", getItems().get(index).getSnippet());
							
							if (((DrugStoreMap)mContext).returnMode.equals(ReturnMode.NeedResult)) {
								Intent resultIntent = new Intent();
								resultIntent.putExtras(innerbundle);
								((Activity) mContext).setResult(RESULT_OK, resultIntent);
								((Activity) mContext).finish();
							} else {
								innerbundle.putString("returnmode", ReturnMode.NeedResult.toString());
								Intent storedetailIntent = new Intent().setClass(mContext,
										DrugStoreDetaill.class);
								storedetailIntent.putExtras(innerbundle);
								((Activity) mContext).startActivityForResult(storedetailIntent, 1);
							}*/
						}
					});
		}

		/**
		 * 药店标记点击事件
		 */
		protected boolean onTap(int index) {

			GeoPoint pt = getItems().get(index).getPoint();
			//((DrugStoreMap)mContext).mMapView.getController().setCenter(pt);//移动到中心点儿
			((DrugStoreMap)mContext).mMapView.getController().animateTo(pt);
			//mMapView.getController().setZoom(15);//3--18  缩放等级逐渐扩大
			
			/*Bitmap[] bmps = new Bitmap[3];
			if (index % 3 == 0) {
				try {
					bmps[0] = BitmapFactory.decodeStream(mContext.getAssets()
							.open("marker1.png"));
					bmps[1] = BitmapFactory.decodeStream(mContext.getAssets()
							.open("marker2.png"));
					bmps[2] = BitmapFactory.decodeStream(mContext.getAssets()
							.open("marker3.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				pop.showPopup(bmps, mGeoList.get(index).getPoint(), 32);
			} else if (index % 3 == 1) {
				try {
					bmps[2] = BitmapFactory.decodeStream(mContext.getAssets()
							.open("marker1.png"));
					bmps[1] = BitmapFactory.decodeStream(mContext.getAssets()
							.open("marker2.png"));
					bmps[0] = BitmapFactory.decodeStream(mContext.getAssets()
							.open("marker3.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				pop.showPopup(bmps, mGeoList.get(index).getPoint(), 32);
			} else {
				mPopView = super.getLayoutInflater().inflate(R.layout.s_popview, null);
				if (mBtn == null)
					mBtn = new Button(mContext);
				mBtn.setText("TestTest");
				mMapView.addView(mBtn, new MapView.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
						mGeoList.get(index).getPoint(),
						MapView.LayoutParams.BOTTOM_CENTER));
			}*/	
					
			if (mPopView == null) {
				LayoutInflater inflater = LayoutInflater.from(mContext);
				mPopView = inflater.inflate(R.layout.s_popview, null);
			}
			
			TextView txtpop = (TextView) mPopView
					.findViewById(R.id.s_map_pop_txt);
			txtpop.setText(getItems().get(index).getTitle());
			LinearLayout poplayout = (LinearLayout) mPopView
					.findViewById(R.id.s_map_pop_layout);
			final Integer mindex = index;
			mPopView.setTag(index);
			
			/* Bitmap[] bmps = new Bitmap[1];
			bmps[0] = getViewBitmap(mPopView);
			pop.showPopup(bmps, getItem(index).getPoint(), 32);*/
			
			// poplayout.setTag(idlist.get(i));
			txtpop.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Bundle innerbundle = new Bundle();
					innerbundle.putString("drugstoreid", innermGeoList.get(mindex).getSnippet());
					
					if (((DrugStoreMap)mContext).returnMode.equals(ReturnMode.NeedResult)) {
						Intent resultIntent = new Intent();
						resultIntent.putExtras(innerbundle);
						((Activity) mContext).setResult(RESULT_OK, resultIntent);
						((Activity) mContext).finish();
					} else {
						innerbundle.putString("returnmode", ReturnMode.NeedResult.toString());
						Intent storedetailIntent = new Intent().setClass(mContext,
								DrugStoreDetaill.class);
						storedetailIntent.putExtras(innerbundle);
						((Activity) mContext).startActivityForResult(storedetailIntent, 1);
					}
				}
			});
						
			((DrugStoreMap)mContext).mMapView.addView(mPopView, new MapView.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
					getItems().get(index).getPoint(),
					MapView.LayoutParams.BOTTOM_CENTER));
			
			return true;
		}

		/**
		 * 药店数据覆盖层点击事件
		 */
		public boolean onTap(GeoPoint pt, MapView mapView) {
			if (pop != null) {
				pop.hidePop();
				if (mBtn != null) {
					((DrugStoreMap)mContext).mMapView.removeView(mBtn);
					mBtn = null;
				}
				if (mPopView != null) {
					((DrugStoreMap)mContext).mMapView.removeView(mPopView);
					mPopView = null;
				}
			}
			
			super.onTap(pt, mapView);
			return false;
		}

		// 自2.1.1 开始，使用 add/remove 管理overlay , 无需重写以下接口
		/*
		@Override
		protected OverlayItem createItem(int i) {
			return mGeoList.get(i);
		}
		
		@Override
		public int size() {
			return mGeoList.size();
		}
		*/

		/*public void addItem(OverlayItem item) {
			innermGeoList.add(item);
			populate();
		}

		public void removeItem(int index) {
			innermGeoList.remove(index);
			populate();
		}*/
		
		private List<OverlayItem> getItems(){
			if(innermGeoList == null)
				innermGeoList = getAllItem();
			return innermGeoList;
		}
	}
	
	/**
	 * View转Bitmap
	 * @param view
	 * @return
	 */
	private static Bitmap getViewBitmap(View view) {
		view.clearFocus();
		view.setPressed(false);

		boolean willNotCache = view.willNotCacheDrawing();
		view.setWillNotCacheDrawing(false);

		int color = view.getDrawingCacheBackgroundColor();
		view.setDrawingCacheBackgroundColor(0);

		if (color != 0) {
			view.destroyDrawingCache();
		}
		view.buildDrawingCache();
		Bitmap cacheBitmap = view.getDrawingCache();
		if (cacheBitmap == null) {
			return null;
		}

		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

		view.destroyDrawingCache();
		view.setWillNotCacheDrawing(willNotCache);
		view.setDrawingCacheBackgroundColor(color);

		return bitmap;
	}
	
	/**
	 * 异步获取药店数据
	 */
	private class AsyncLoader_GuessInfo extends
			AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerlist = null;

		@Override
		protected Integer doInBackground(String... params) {
			try {
				if(innerLocationProvider != null){
					station = innerLocationProvider.getLocation();
					if (station == null || (station.latitude == 0.0 && station.longitude == 0.0)) {
						innerLocationProvider.updateListener();
					    station = innerLocationProvider.getLocation();
					}
					if (station == null || (station.latitude == 0.0 && station.longitude == 0.0)) {
					       return 3;
					}
				} else {
					return 3;
				}
				innerlist = getData();
				if (innerlist != null) {
					return (innerlist.size() > 0) ? 0 : 1;
				} else {
					return 2;
				}
			} catch (TimestampException ex) {
				return -2;
			}catch (Exception ex) {
				return 2;
			}
		}

		@Override
		// 处理界面
		protected void onPostExecute(Integer result) {
			try {
				if (result == 0) {
					initStoreData(innerlist);
					location_address_txt.setText(station.address);
					location_address_ico.setImageResource(R.drawable.icon_loc_suc);
					async_begin.setVisibility(View.GONE);
				} else if (result == 1) {
					location_address_txt.setText(station.address);
					location_address_ico.setImageResource(R.drawable.icon_loc_suc);
					
					mMapView.getOverlays().clear();
					showMyLocation();
					
					GeoPoint point = new GeoPoint((int) (latitude * 1e6),
							(int) (longitude * 1e6));
					mMapView.getController().setCenter(point);
					
					async_begin.setVisibility(View.GONE);
				} else {
					async_begin.setVisibility(View.GONE);
					async_error.setVisibility(View.VISIBLE);
					TextView async_error_txt = (TextView) async_error
							.findViewById(R.id.async_error_txt);
					if (result == -2) {
						async_error_txt.setText(R.string.error_timestamp);
					} else if (result == 3){
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
							new AsyncLoader_GuessInfo().execute();
						}
					});
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
			} 
		}
	}
	
	/**
	 * 根据不同的加载模式、筛选条件获取相应的药店数据
	 * @return
	 */
	private List<Map<String, String>> getData() {
		List<Map<String, String>> innerlist = null;
		AppConfig config = new AppConfig(getBaseContext());
		int ishc = config.getStore_IsHC(getBaseContext());
		int searchRang = config.getStore_SearchRange(getBaseContext());
		latitude = station.latitude;
		longitude = station.longitude;
		String cityName = station.city;

		if (loadMode.equals(LoadMode.ShowByDrug)
				|| loadMode.equals(LoadMode.PositionByDrug)) {
			String drugid = bundle.getString("drugid");
			if (searchRang == -1) {
				// Toast.makeText(getApplicationContext(),
				// Location.getlati().toString(), 1).show();
				// Log.i("chenlinwei", cityName + "2");
				innerlist = Drug.whereToBuyMapMode(drugid, cityName, latitude,
						longitude, ishc, 0, 0);
			} else {
				innerlist = Drug.whereToBuyMapMode(drugid, searchRang, latitude,
						longitude, ishc, 0, 0);
			}
		} else {
			if (searchRang == -1) {
				// Toast.makeText(getApplicationContext(),
				// Location.getlati().toString(), 1).show();
				// Log.i("chenlinwei", cityName + "2");
				innerlist = coolbuy360.logic.DrugStore.getListMapMode(cityName,
						latitude, longitude, ishc, 0, 0);
			} else {
				// Toast.makeText(getApplicationContext(),
				// Location.getlati().toString(), 1).show();
				innerlist = coolbuy360.logic.DrugStore.getListMapMode(
						(double) searchRang, latitude, longitude, ishc, 0, 0);
			}
		}
		return innerlist;
	}	
	
	/**
	 * 实例化药店层
	 * @param innerlist
	 */
	public void initStoreData(List<Map<String, String>> innerlist) {
		Drawable marker = DrugStoreMap.this.getResources().getDrawable(
				R.drawable.position_red_ico);
		mMapView.getOverlays().clear();
		OverlayTest ov = new OverlayTest(marker, this, mMapView);
		Integer positionIndex = -1;
		
		for (Map<String, String> map : innerlist) {
			GeoPoint googlePoint = new GeoPoint((int) (Double.parseDouble(map
					.get("latvalue")) * 1E6), (int) (Double.parseDouble(map
					.get("longvalue")) * 1E6));
			GeoPoint bdPoint = CoordinateConvert.fromGcjToBaidu(googlePoint);
			OverlayItem storeItem = new OverlayItem(bdPoint,
					map.get("drugstorename"), map.get("drugstoreid"));
			mGeoList.add(storeItem);
			idlist.add(map.get("drugstoreid"));

			if (loadMode.equals(LoadMode.Position)
					|| loadMode.equals(LoadMode.PositionByDrug)) {
				String positionDrugStoreID = bundle.getString("drugstoreid");
				if (map.get("drugstoreid").equals(positionDrugStoreID)) {
					positionIndex = mGeoList.indexOf(storeItem);
				}
			}
		}
		
		/*for (OverlayItem item : mGeoList) {
			ov.addItem(item);
		}*/
		ov.addItem(mGeoList);
		mMapView.getOverlays().add(ov);
		mMapView.refresh();
		showMyLocation();
		//mMapView.getController().setCenter(new GeoPoint(cLat, cLon));
		
		if (positionIndex != -1) {
			ov.onTap(positionIndex);
		} else {
			GeoPoint googlePoint = new GeoPoint((int) (latitude * 1e6),
					(int) (longitude * 1e6));
			GeoPoint bdPoint = CoordinateConvert.fromGcjToBaidu(googlePoint); 
			mMapView.getController().setCenter(bdPoint);
		}
	}
	
	/**
	 * 显示手机位置
	 */
	private void showMyLocation() {
		myLocationOverlay = new MyLocationOverlay(mMapView);
		locData = new LocationData();
		GeoPoint googlePoint = new GeoPoint((int) (latitude * 1e6),
				(int) (longitude * 1e6));
		GeoPoint bdPoint = CoordinateConvert.fromGcjToBaidu(googlePoint); 
		locData.latitude = bdPoint.getLatitudeE6() / 1e6;
		locData.longitude = bdPoint.getLongitudeE6() / 1e6;
		locData.accuracy = station.accuracy;
		locData.direction = station.direction;
		myLocationOverlay.setData(locData);
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		mMapView.refresh();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) { 			
			/*String drugstoreid = data.getExtras().getString("drugstoreid");
			Integer positionIndex = idlist.indexOf(drugstoreid);
			if (positionIndex != -1) {
				((OverlayTest)(mMapView.getOverlays().get(0))).onTap(positionIndex);
			}*/
		}
	}
	
	/**
	 * 将字符串转换为LoadMode
	 * 
	 * @param value
	 * @return
	 */
	public static LoadMode loadModeTransform(String value, LoadMode defaultvalue) {
		if (value != null) {
			String innervalue = value.trim().toLowerCase();
			if (innervalue.equals("showall")) {
				return LoadMode.ShowAll;
			} else if (innervalue.equals("position")) {
				return LoadMode.Position;
			} else if (innervalue.equals("showbydrug")) {
				return LoadMode.ShowByDrug;
			} else if (innervalue.equals("positionbydrug")) {
				return LoadMode.PositionByDrug;
			} else {
				return defaultvalue;
			}
		} else {
			return defaultvalue;
		}
	}

	/**
	 * 药店加载模式
	 * 
	 * @author yangxc
	 * 
	 */
	public enum LoadMode {
		/**
		 * 加载附近所有药店
		 */
		ShowAll,
		/**
		 * 加载并定位药店
		 */
		Position,
		/**
		 * 根据药品加载药店
		 */
		ShowByDrug,
		/**
		 * 根据药品加载并定位药店
		 */
		PositionByDrug
	}

	/**
	 * 将字符串转换为ReturnMode
	 * 
	 * @param value
	 * @return
	 */
	public static ReturnMode returnModeTransform(String value, ReturnMode defaultvalue) {
		if (value != null) {
			String innervalue = value.trim().toLowerCase();		
			if (innervalue.equals("normal")) {
				return ReturnMode.Normal;
			} else if (innervalue.equals("needresult")) {
				return ReturnMode.NeedResult;
			} else {
				return defaultvalue;
			}
		} else {
			return defaultvalue;
		}
	}

	/**
	 * 返回模式
	 * 
	 * @author yangxc
	 * 
	 */
	public enum ReturnMode {
		/**
		 * 默认返回模式，无其它操作
		 */
		Normal,
		/**
		 * 需要返回值的返回模式
		 */
		NeedResult
	}
}