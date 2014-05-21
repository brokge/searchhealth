/**
 * 
 */
package coolbuy360.searchhealth;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.mapapi.utils.CoordinateConvert;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import coolbuy360.control.MyMapView;
import coolbuy360.control.MyPoiOverlay;
import coolbuy360.service.LocationInfo;
import coolbuy360.service.LocationProvider;
import coolbuy360.service.LocationProvider.OnReceiveLocationListener;
import coolbuy360.service.Util;
import coolbuy360.service.searchApp;

/**
 * @author yangxc
 *
 */
public class BDrugStoreMap extends Activity {

	final static String TAG = "MapActivty";
	MyMapView mMapView = null;

	public MKMapViewListener mMapListener = null;
	FrameLayout mMapViewContainer = null;
	
	/*LoadMode loadMode = LoadMode.ShowAll;
	ReturnMode returnMode = ReturnMode.Normal;*/
	Bundle bundle = null;
	
	private LocationProvider innerLocationProvider = null;
	LocationInfo.SItude station = null;
	private MKSearch mSearch = null;   // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	
	private String searchkey = "ҩ��";
	private int searchrange = 50000;
	private int pagesize = 15;
	
	/*protected double latitude = 30.280506;
	protected double longitude = 120.107582;*/
	/*protected double latitude = 0.0;
	protected double longitude = 0.0;	*/
	GeoPoint localpt;

	MyLocationOverlay myLocationOverlay = null;
	MyPoiOverlay poiOverlay = null;
	LocationData locData = null;
	Boolean isLocationReceive = true;
	Boolean poimode = false;

	LinearLayout async_begin;
	LinearLayout async_error;
	TextView location_address_txt;
	ImageView location_address_ico;
	LinearLayout location_address_btn;
	Animation operatingAnim;

	/*// ���overlayitem
	public List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
	// ���ҩ��ID
	public List<String> idlist = new ArrayList<String>();
	// ���overlayͼƬ
	//public List<Drawable> res = new ArrayList<Drawable>();
*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		innerLocationProvider = searchApp.mLocationProvider;
		searchApp app = (searchApp) this.getApplication();
		if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(getApplicationContext());
            /**
             * ���BMapManagerû�г�ʼ�����ʼ��BMapManager
             */
            app.mBMapManager.init(new searchApp.MyGeneralListener());
        }
		setContentView(R.layout.bdrugstoremap);
		mMapView = (MyMapView) findViewById(R.id.bmapView);
		initMapView();
		mMapView.getController().setZoom(16);
		mMapView.getController().enableClick(true);
		mMapView.setBuiltInZoomControls(true);
		
		mMapListener = new MKMapViewListener() {

			@Override
			public void onMapMoveFinish() {
				Log.d("hjtest", "hjtest" + "onMapMoveFinish");
				refreshData(mMapView.getMapCenter());
			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				/*String title = "";
				if (mapPoiInfo != null) {
					title = mapPoiInfo.strText;
					Toast.makeText(BDrugStoreMap.this, title,
							Toast.LENGTH_SHORT).show();
				}*/
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				//Log.d("hjtest", "hjtest" + "getmap OK");
			}

			@Override
			public void onMapAnimationFinish() {
				//Log.d("hjtest", "hjtest" + "onMapAnimationFinish");
			}

			@Override
			public void onMapLoadFinish() {
				
			}
		};
		
		mMapView.regMapViewListener(searchApp.getInstance().mBMapManager,
				mMapListener);

		async_begin = (LinearLayout) findViewById(R.id.async_begin);
		async_begin.setVisibility(View.VISIBLE);
		async_error = (LinearLayout) findViewById(R.id.async_error);
		location_address_txt = (TextView) findViewById(R.id.location_address_txt);
		location_address_ico = (ImageView) findViewById(R.id.location_address_ico);
		location_address_btn = (LinearLayout) findViewById(R.id.location_address_btn);
		
		operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);  
		// ����������ת
		LinearInterpolator lin = new LinearInterpolator();  
		operatingAnim.setInterpolator(lin);
		
		location_address_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isLocationReceive) {
					isLocationReceive = false;
					location_address_ico.setImageResource(R.drawable.icon_loc);
					if (operatingAnim != null) {
						location_address_ico.startAnimation(operatingAnim);
					}
					innerLocationProvider.updateListener();
				}
			}
		});
		
		innerLocationProvider.setOnReceiveLocationListener(new MyOnReceiveLocationListener());
		
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BDrugStoreMap.this.finish();
			}
		});

		// ��ʼ������ģ�飬ע�������¼�����
        mSearch = new MKSearch();    
        mSearch.init(searchApp.getInstance().mBMapManager, new DrugStoreSearchListener());
        mSearch.setPoiPageCapacity(pagesize); 
        		
		try {
			bundle = getIntent().getExtras();
			if (bundle != null) {
				String storename = bundle.getString("storename");
				String saddress = bundle.getString("address");
				String sphone = bundle.getString("phone");
				int poilatitudee6 = bundle.getInt("poilatitudee6", 0);
				int poilongitudee6 = bundle.getInt("poilongitudee6", 0);
				if (storename != null || !(storename.equals(""))) {
					MKPoiInfo mkPoiInfo = new MKPoiInfo();
					mkPoiInfo.name = storename;
					mkPoiInfo.address = saddress;
					mkPoiInfo.phoneNum = sphone;
					mkPoiInfo.pt = new GeoPoint(poilatitudee6, poilongitudee6);
					mMapView.getController().animateTo(mkPoiInfo.pt);
					MyPoiOverlay.showPopup(mkPoiInfo, mMapView, this);
					refreshData(mkPoiInfo.pt);
					poimode = true;
					innerLocationProvider.updateListener();
					return;
				}
			}			
		} catch (Exception e) {

		}
		
		refreshData(null);	
	}
	
	private class MyOnReceiveLocationListener implements OnReceiveLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			isLocationReceive = true;
			if (location == null) {
				setLocaltionError();
			} else {
				//async_error.setVisibility(View.GONE);
				station = innerLocationProvider.getLocation();
				localpt = new GeoPoint((int) (station.latitude * 1E6),
						(int) (station.longitude * 1E6));
				localpt = CoordinateConvert.fromGcjToBaidu(localpt);
				showMyLocation();
				if (!poimode) {
					mSearch.poiSearchNearBy(searchkey, localpt, searchrange);
				}
				poimode = false;
			}
		}		
	}
	
	private class DrugStoreSearchListener implements MKSearchListener {

		//�ڴ˴�������ҳ���
        @Override
        public void onGetPoiDetailSearchResult(int type, int error) {
            
        }
        
        /**
         * �ڴ˴���poi�������
         */
        public void onGetPoiResult(MKPoiResult res, int type, int error) {
        	
			// ����ſɲο�MKEvent�еĶ���
			if (error != 0 || res == null || res.getCurrentNumPois() == 0) {
				setNoResultError();;
			} else if (res.getCurrentNumPois() > 0) {
				async_error.setVisibility(View.GONE);
				if (poiOverlay == null) {
					poiOverlay = new MyPoiOverlay(BDrugStoreMap.this, mMapView, mSearch);
	                mMapView.getOverlays().add(poiOverlay);
				} 
				poiOverlay.setData(res.getAllPoi());
                mMapView.refresh();
                //��ePoiTypeΪ2��������·����4��������·��ʱ�� poi����Ϊ��
				/*for (MKPoiInfo info : res.getAllPoi()) {
					if (info.pt != null) {
						mMapView.getController().animateTo(info.pt);
						break;
					}
				}	*/		
			} else if (res.getCityListNum() > 0) {
				// ������ؼ����ڱ���û���ҵ����������������ҵ�ʱ�����ذ����ùؼ�����Ϣ�ĳ����б�
				/*String strInfo = "��";
				for (int i = 0; i < res.getCityListNum(); i++) {
					strInfo += res.getCityListInfo(i).city;
					strInfo += ",";
				}
				strInfo += "�ҵ����";
				Toast.makeText(PoiSearchDemo.this, strInfo, Toast.LENGTH_LONG)
						.show();*/
			}
			async_begin.setVisibility(View.GONE);
		}

		public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error) {
		}

		public void onGetTransitRouteResult(MKTransitRouteResult res, int error) {
		}

		public void onGetWalkingRouteResult(MKWalkingRouteResult res, int error) {
		}

		public void onGetAddrResult(MKAddrInfo res, int error) {
		}

		public void onGetBusDetailResult(MKBusLineResult result, int iError) {
		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult result, int type,
				int error) {

		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {

		}	
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
	
	/**
	 * ˢ������
	 */
	private void refreshData(GeoPoint pt) {
		try {
			async_begin.setVisibility(View.VISIBLE);
			if (Util.isNetworkConnected(this)) {
				if (pt == null) {
					if (innerLocationProvider == null) {
						innerLocationProvider = searchApp.mLocationProvider;
						innerLocationProvider.setOnReceiveLocationListener(new MyOnReceiveLocationListener());						
					} 
					station = innerLocationProvider.getLocation();
					if (station == null || (station.latitude == 0.0 && station.longitude == 0.0)) {
						innerLocationProvider.updateListener();
					} else {
						GeoPoint googlept = new GeoPoint((int) (station.latitude * 1E6),
								(int) (station.longitude * 1E6));
						localpt = CoordinateConvert.fromGcjToBaidu(googlept);
						isLocationReceive = true;
						showMyLocation();
						mSearch.poiSearchNearBy(searchkey, localpt, searchrange);
					}
				} else {					
					mSearch.poiSearchNearBy(searchkey, pt, searchrange);					
				}
			} else {
				setNetError();
			}
		} catch (Exception e) {
			setNetError();
		}
	}
	
	/**
	 * ���ö�λ������ʾ
	 */
	private void setLocaltionError() {
		async_begin.setVisibility(View.GONE);
		TextView async_error_txt = (TextView) async_error
				.findViewById(R.id.async_error_txt);
		Button async_error_refleshbtn = (Button) async_error
				.findViewById(R.id.async_error_reflesh);
		async_error.setVisibility(View.VISIBLE);
		async_error_txt.setText(R.string.error_location);	
		async_error_refleshbtn.setVisibility(View.VISIBLE);
		async_error_refleshbtn.setOnClickListener(new refreshOnclick());
	}
	
	/**
	 * �������������������ʾ
	 */
	private void setNoResultError() {
		async_begin.setVisibility(View.GONE);
		TextView async_error_txt = (TextView) async_error
				.findViewById(R.id.async_error_txt);
		Button async_error_refleshbtn = (Button) async_error
				.findViewById(R.id.async_error_reflesh);
		async_error.setVisibility(View.VISIBLE);
		async_error_txt.setText("û���ҵ�������ҩ����Ϣ");	
		async_error_refleshbtn.setVisibility(View.VISIBLE);
		async_error_refleshbtn.setOnClickListener(new refreshOnclick());
	}

	/**
	 * �������������ʾ
	 */
	private void setNetError() {
		async_begin.setVisibility(View.GONE);
		TextView async_error_txt = (TextView) async_error
				.findViewById(R.id.async_error_txt);
		Button async_error_refleshbtn = (Button) async_error
				.findViewById(R.id.async_error_reflesh);
		async_error.setVisibility(View.VISIBLE);
		async_error_txt.setText(R.string.error_nonetwork);	
		async_error_refleshbtn.setVisibility(View.VISIBLE);
		async_error_refleshbtn.setOnClickListener(new refreshOnclick());
	}
	
	/**
	 * ˢ�µ��
	 */
	public class refreshOnclick implements OnClickListener
	{		
		@Override
		public void onClick(View v) {
			async_error.setVisibility(View.GONE);		
			//async_begin.setVisibility(View.VISIBLE);
			refreshData(null);
		}
	}
	
	/**
	 * ��ʾ�ֻ�λ��
	 */
	private void showMyLocation() {
		if (myLocationOverlay == null) {
			myLocationOverlay = new MyLocationOverlay(mMapView);
			mMapView.getOverlays().add(myLocationOverlay);
		}
		locData = new LocationData();
		locData.latitude = localpt.getLatitudeE6() / 1e6;
		locData.longitude = localpt.getLongitudeE6() / 1e6;
		try {
			locData.accuracy = station.accuracy;
			locData.direction = station.direction;
			location_address_txt.setText(station.address);
			location_address_ico.setImageResource(R.drawable.icon_loc_suc);
			location_address_ico.clearAnimation();
		} catch (Exception e) {

		}
		myLocationOverlay.setData(locData);
		myLocationOverlay.enableCompass();
		if (!poimode) {
			mMapView.getController().animateTo(localpt);
		}
		mMapView.refresh();
	}
	
	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) { 			
			String drugstoreid = data.getExtras().getString("drugstoreid");
			Integer positionIndex = idlist.indexOf(drugstoreid);
			if (positionIndex != -1) {
				((OverlayTest)(mMapView.getOverlays().get(0))).onTap(positionIndex);
			}
		}
	}*/
	
	/**
	 * ���ַ���ת��ΪLoadMode
	 * 
	 * @param value
	 * @return
	 *//*
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

	*//**
	 * ҩ�����ģʽ
	 * 
	 * @author yangxc
	 * 
	 *//*
	public enum LoadMode {
		*//**
		 * ���ظ�������ҩ��
		 *//*
		ShowAll,
		*//**
		 * ���ز���λҩ��
		 *//*
		Position,
		*//**
		 * ����ҩƷ����ҩ��
		 *//*
		ShowByDrug,
		*//**
		 * ����ҩƷ���ز���λҩ��
		 *//*
		PositionByDrug
	}

	*//**
	 * ���ַ���ת��ΪReturnMode
	 * 
	 * @param value
	 * @return
	 *//*
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

	*//**
	 * ����ģʽ
	 * 
	 * @author yangxc
	 * 
	 *//*
	public enum ReturnMode {
		*//**
		 * Ĭ�Ϸ���ģʽ������������
		 *//*
		Normal,
		*//**
		 * ��Ҫ����ֵ�ķ���ģʽ
		 *//*
		NeedResult
	}*/
}
