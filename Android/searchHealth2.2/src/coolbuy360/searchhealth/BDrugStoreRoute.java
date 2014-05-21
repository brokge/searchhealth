/**
 * 
 */
package coolbuy360.searchhealth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.mapapi.utils.CoordinateConvert;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import coolbuy360.control.MyMapView;
import coolbuy360.control.MyPoiOverlay;
import coolbuy360.service.LocationInfo;
import coolbuy360.service.LocationProvider;
import coolbuy360.service.Util;
import coolbuy360.service.searchApp;
import coolbuy360.service.LocationProvider.OnReceiveLocationListener;

/**
 * ǰ��ҩ��·��
 * @author yangxc
 *
 */
public class BDrugStoreRoute extends Activity {

	final static String TAG = "MapActivty";
	MyMapView mMapView = null;

	//public MKMapViewListener mMapListener = null;
	FrameLayout mMapViewContainer = null;
	
	private LocationProvider innerLocationProvider = null;
	LocationInfo.SItude station = null;		
	GeoPoint localpt;
	GeoPoint endpt;
	double endlatitude;
	double endlongitude;
	private MKSearch mSearch = null;   // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��

	MyLocationOverlay myLocationOverlay = null;
	LocationData locData = null;

	TextView actionbar_page_title;
	LinearLayout async_begin;
	LinearLayout async_error;
	TextView location_address_txt;
	ImageView location_address_ico;
	LinearLayout location_address_btn;
	LinearLayout bdrugstoremap_route_nav;
	String storename = "����ҩ��";
	Animation operatingAnim;
	
	//���·�߽ڵ����
	Button mBtnPre = null;// ��һ���ڵ�
	Button mBtnNext = null;// ��һ���ڵ�
	int nodeIndex = -2;// �ڵ�����,������ڵ�ʱʹ��
	MKRoute route = null;// ����ݳ�/����·�����ݵı�����������ڵ�ʱʹ��
	RouteOverlay routeOverlay = null;
	boolean useDefaultIcon = false;
	int searchType = -1;// ��¼���������ͣ����ּݳ�/���к͹���
	private PopupOverlay pop = null;// ��������ͼ�㣬����ڵ�ʱʹ��
	private TextView popupText = null;// ����view
	private View viewCache = null;
	private Boolean isRouted = false;

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
		mMapView.setBuiltInZoomControls(false);
		
		//��ʼ������
        mBtnPre = (Button)findViewById(R.id.pre);
        mBtnNext = (Button)findViewById(R.id.next);
        mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		
		OnClickListener nodeClickListener = new OnClickListener(){
			public void onClick(View v) {
				//���·�߽ڵ�
				nodeClick(v);
			}
        };

        mBtnPre.setOnClickListener(nodeClickListener);
        mBtnNext.setOnClickListener(nodeClickListener);

        //���� ��������ͼ��
        createPaopao();
        
		/*mMapListener = new MKMapViewListener() {

			@Override
			public void onMapMoveFinish() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				// TODO Auto-generated method stub
				String title = "";
				if (mapPoiInfo != null) {
					title = mapPoiInfo.strText;
					Toast.makeText(BDrugStoreMap.this, title,
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
				// TODO �Զ����ɵķ������
				
			}
		};
		
		mMapView.regMapViewListener(searchApp.getInstance().mBMapManager,
				mMapListener);*/

        actionbar_page_title = (TextView) findViewById(R.id.actionbar_page_title);
		async_begin = (LinearLayout) findViewById(R.id.async_begin);
		async_begin.setVisibility(View.VISIBLE);
		async_error = (LinearLayout) findViewById(R.id.async_error);
		bdrugstoremap_route_nav = (LinearLayout) findViewById(R.id.bdrugstoremap_route_nav);
		bdrugstoremap_route_nav.setVisibility(View.VISIBLE);
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
				location_address_ico.setImageResource(R.drawable.icon_loc);
				if (operatingAnim != null) {
					location_address_ico.startAnimation(operatingAnim);
				}
				innerLocationProvider.updateListener();
			}
		});
		
		innerLocationProvider.setOnReceiveLocationListener(new MyOnReceiveLocationListener());
		
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BDrugStoreRoute.this.finish();
			}
		});
				
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			endlatitude = bundle.getDouble("endlatitude", 0);
			endlongitude = bundle.getDouble("endlongitude", 0);
			endpt = new GeoPoint((int) (endlatitude * 1E6),
					(int) (endlongitude * 1E6));
			storename = bundle.getString("storename");
			if (storename == null || storename.equals("")) {
				storename = "����ҩ��";
			}
		}
		actionbar_page_title.setText("ǰ��" + storename);

		// ��ʼ������ģ�飬ע�������¼�����
        mSearch = new MKSearch();    
        mSearch.init(searchApp.getInstance().mBMapManager, new DrugStoreRouteListener());
        
		refreshData();
	}
	
	/**
	 * ������������ͼ��
	 */
	public void createPaopao(){
		
        //���ݵ����Ӧ�ص�
        PopupClickListener popListener = new PopupClickListener(){
			@Override
			public void onClickedPopup(int index) {
				
			}
        };
        pop = new PopupOverlay(mMapView,popListener);
        mMapView.pop = pop;
	}
	
	/**
	 * �ڵ����ʾ��
	 * @param v
	 */
	public void nodeClick(View v){
		viewCache = getLayoutInflater().inflate(R.layout.s_popview, null);
        popupText = (TextView) viewCache.findViewById(R.id.s_map_pop_txt);
		if (searchType == 0 || searchType == 2){
			//�ݳ�������ʹ�õ����ݽṹ��ͬ���������Ϊ�ݳ����У��ڵ����������ͬ
			if (nodeIndex < -1 || route == null || nodeIndex >= route.getNumSteps())
				return;
			
			//��һ���ڵ�
			if (mBtnPre.equals(v) && nodeIndex > 0){
				//������
				nodeIndex--;
				GeoPoint steppt = route.getStep(nodeIndex).getPoint();
				String messageString = route.getStep(nodeIndex).getContent();
				showPop(steppt, messageString);
			}
			//��һ���ڵ�
			if (mBtnNext.equals(v) && nodeIndex < (route.getNumSteps()-1)){
				//������
				nodeIndex++;
				GeoPoint steppt = route.getStep(nodeIndex).getPoint();
				String messageString = route.getStep(nodeIndex).getContent();
				if (nodeIndex == route.getNumSteps() - 1) {
					messageString = "�����յ�" + storename;
				}
				showPop(steppt, messageString);
			}
		}		
	}

	/**
	 * @param steppt
	 * @param message
	 */
	private void showPop(GeoPoint steppt, String message) {
		//�ƶ���ָ������������
		mMapView.getController().animateTo(steppt);
		//��������
		popupText.setText(message);
		pop.showPopup(viewCache,
				route.getStep(nodeIndex).getPoint(),
				0);
	}
	
	private class MyOnReceiveLocationListener implements OnReceiveLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				setLocaltionError();
			} else {
				//async_error.setVisibility(View.GONE);
				station = innerLocationProvider.getLocation();
				localpt = new GeoPoint((int) (station.latitude * 1E6),
						(int) (station.longitude * 1E6));
				localpt = CoordinateConvert.fromGcjToBaidu(localpt);
				showMyLocation();
				//mSearch.poiSearchNearBy(searchkey, localpt, searchrange);
				if (!isRouted) {
					SearchRouteProcess(localpt, endpt);					
				}
			}
		}		
	}
	
	private class DrugStoreRouteListener implements MKSearchListener {

		public void onGetDrivingRouteResult(MKDrivingRouteResult res,
				int error) {
			async_begin.setVisibility(View.GONE);
			// �����յ������壬��Ҫѡ�����ĳ����б���ַ�б�
			if (error == MKEvent.ERROR_ROUTE_ADDR) {
				// �������е�ַ
				// ArrayList<MKPoiInfo> stPois =
				// res.getAddrResult().mStartPoiList;
				// ArrayList<MKPoiInfo> enPois =
				// res.getAddrResult().mEndPoiList;
				// ArrayList<MKCityListInfo> stCities =
				// res.getAddrResult().mStartCityList;
				// ArrayList<MKCityListInfo> enCities =
				// res.getAddrResult().mEndCityList;
				return;
			}
			// ����ſɲο�MKEvent�еĶ���
			if (error != 0 || res == null) {
				Toast.makeText(BDrugStoreRoute.this, "��Ǹ��δ�ҵ����",
						Toast.LENGTH_SHORT).show();
				return;
			}

			searchType = 0;
			if (routeOverlay == null) {
				routeOverlay = new RouteOverlay(BDrugStoreRoute.this, mMapView);
				// ���·��ͼ��
				mMapView.getOverlays().add(routeOverlay);
			}
			// �˴���չʾһ��������Ϊʾ��
			routeOverlay.setData(res.getPlan(0).getRoute(0));
			// ִ��ˢ��ʹ��Ч
			mMapView.refresh();
			/*// ʹ��zoomToSpan()���ŵ�ͼ��ʹ·������ȫ��ʾ�ڵ�ͼ��
			mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(),
					routeOverlay.getLonSpanE6());*/
			// �ƶ���ͼ�����
			mMapView.getController().animateTo(res.getStart().pt);
			// ��·�����ݱ����ȫ�ֱ���
			route = res.getPlan(0).getRoute(0);
			// ����·�߽ڵ��������ڵ����ʱʹ��
			nodeIndex = -1;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);
			isRouted = true;
		}

		public void onGetTransitRouteResult(MKTransitRouteResult res,
				int error) {
		}

		public void onGetWalkingRouteResult(MKWalkingRouteResult res,
				int error) {
			async_begin.setVisibility(View.GONE);
			// �����յ������壬��Ҫѡ�����ĳ����б���ַ�б�
			if (error == MKEvent.ERROR_ROUTE_ADDR) {
				// �������е�ַ
				// ArrayList<MKPoiInfo> stPois =
				// res.getAddrResult().mStartPoiList;
				// ArrayList<MKPoiInfo> enPois =
				// res.getAddrResult().mEndPoiList;
				// ArrayList<MKCityListInfo> stCities =
				// res.getAddrResult().mStartCityList;
				// ArrayList<MKCityListInfo> enCities =
				// res.getAddrResult().mEndCityList;
				return;
			}
			if (error != 0 || res == null) {
				Toast.makeText(BDrugStoreRoute.this, "��Ǹ��δ�ҵ����",
						Toast.LENGTH_SHORT).show();
				return;
			}

			searchType = 2;
			if (routeOverlay == null) {
				routeOverlay = new RouteOverlay(BDrugStoreRoute.this, mMapView);
				// ���·��ͼ��
				mMapView.getOverlays().add(routeOverlay);
			}
			// �˴���չʾһ��������Ϊʾ��
			routeOverlay.setData(res.getPlan(0).getRoute(0));
			// ִ��ˢ��ʹ��Ч
			mMapView.refresh();
			/*// ʹ��zoomToSpan()���ŵ�ͼ��ʹ·������ȫ��ʾ�ڵ�ͼ��
			mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(),
					routeOverlay.getLonSpanE6());*/
			// �ƶ���ͼ�����
			mMapView.getController().animateTo(res.getStart().pt);
			// ��·�����ݱ����ȫ�ֱ���
			route = res.getPlan(0).getRoute(0);
			// ����·�߽ڵ��������ڵ����ʱʹ��
			nodeIndex = -1;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);
			isRouted = true;		    
		}
		public void onGetAddrResult(MKAddrInfo res, int error) {
		}
		public void onGetPoiResult(MKPoiResult res, int arg1, int arg2) {
		}
		public void onGetBusDetailResult(MKBusLineResult result, int iError) {
		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
		}

		@Override
		public void onGetPoiDetailSearchResult(int type, int iError) {
		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult result, int type,
				int error) {
		}
	}

	/**
	 * ����·�߹滮����ʾ��
	 * @param v
	 */
	void SearchRouteProcess(GeoPoint frompt, GeoPoint topt) {
		//��������ڵ��·������
		route = null;
		routeOverlay = null;
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		
		// ������յ��name���и�ֵ��Ҳ����ֱ�Ӷ����긳ֵ����ֵ�����򽫸��������������
		MKPlanNode stNode = new MKPlanNode();
		stNode.pt = frompt;
		MKPlanNode enNode = new MKPlanNode();
		enNode.pt = topt;
		
		double distance = DistanceUtil.getDistance(frompt, topt);
		if (distance > 1000) {
			actionbar_page_title.setText("�ݳ�ǰ��" + storename);
			mSearch.drivingSearch(null, stNode, null, enNode);
		} else {
			actionbar_page_title.setText("����ǰ��" + storename);
			mSearch.walkingSearch(null, stNode, null, enNode);			
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
	private void refreshData() {
		try {
			isRouted = false;
			async_begin.setVisibility(View.VISIBLE);
			if (Util.isNetworkConnected(this)) {
				if (innerLocationProvider == null) {
					innerLocationProvider = searchApp.mLocationProvider;
					innerLocationProvider
							.setOnReceiveLocationListener(new MyOnReceiveLocationListener());
				}
				station = innerLocationProvider.getLocation();
				if (station == null
						|| (station.latitude == 0.0 && station.longitude == 0.0)) {
					innerLocationProvider.updateListener();
				} else {
					GeoPoint googlept = new GeoPoint(
							(int) (station.latitude * 1E6),
							(int) (station.longitude * 1E6));
					localpt = CoordinateConvert.fromGcjToBaidu(googlept);
					showMyLocation();
					SearchRouteProcess(localpt, endpt);
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
			refreshData();
		}
	}
	
	/**
	 * ��ʾ�ֻ�λ��
	 */
	private void showMyLocation() {
		try {
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
			mMapView.getController().animateTo(localpt);
			mMapView.refresh();
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			
		}
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
}
