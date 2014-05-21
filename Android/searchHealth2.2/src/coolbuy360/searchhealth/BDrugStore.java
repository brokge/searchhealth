/**
 * 
 */
package coolbuy360.searchhealth;

import java.util.ArrayList;

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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import coolbuy360.adapter.BStoreListViewAdapter;
import coolbuy360.logic.ConstantsSetting;
import coolbuy360.pulltorefresh.PullToRefreshBase;
import coolbuy360.pulltorefresh.PullToRefreshListView;
import coolbuy360.pulltorefresh.PullToRefreshBase.Mode;
import coolbuy360.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import coolbuy360.service.DisplayUtil;
import coolbuy360.service.LocationInfo;
import coolbuy360.service.LocationProvider;
import coolbuy360.service.Util;
import coolbuy360.service.searchApp;

/**
 * @author yangxc
 *
 */
public class BDrugStore extends Activity {

	private PullToRefreshListView refreshListView;
	
	private LocationProvider innerLocationProvider = null;
	LocationInfo.SItude station = null;
	private MKSearch mSearch = null;   // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	
	private String searchkey = "ҩ��";
	private int searchrange = 50000;
	
	/*protected double latitude = 30.280506;
	protected double longitude = 120.107582;*/
	protected double latitude = 0.0;
	protected double longitude = 0.0;
	GeoPoint localpt;
	
	private BStoreListViewAdapter adapter;
	private int pageindex = 0;
	private int pagesize = ConstantsSetting.QLDefaultPageSize;	
	Boolean isRefresh = true;

	/*private int searchRang;
	private int ishc;*/
	LinearLayout async_error;
	
	View listview_empty_error;
	View LoadingView;

	Boolean isCompleted = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		innerLocationProvider = searchApp.mLocationProvider;
		// Ϊ�˳���׼��
		searchApp.getInstance().addActivity(this);
		setContentView(R.layout.drugstore);

		async_error = (LinearLayout) findViewById(R.id.async_error);
		
		LayoutInflater layoutInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
		listview_empty_error= layoutInflater.inflate(R.layout.listview_emptyview_error, null);

		adapter = new BStoreListViewAdapter(this, new ArrayList<MKPoiInfo>());
		refreshListView = (PullToRefreshListView) findViewById(R.id.drugstorelist_ptflistview);
		
		LoadingView = layoutInflater.inflate(R.layout.listview_emptyview_loading, null);
		refreshListView.setEmptyView(LoadingView);
		initPullToRefreshListView(refreshListView);
		
		ImageButton imgbtndialog = (ImageButton) this
				.findViewById(R.id.s_dialogTitle);
		imgbtndialog.setVisibility(View.GONE);
		/*imgbtndialog.setOnClickListener(new dialogOnclicck());*/

		ImageButton actionbar_tomap_btn = (ImageButton) this
				.findViewById(R.id.actionbar_tomap_btn);
		actionbar_tomap_btn.setOnClickListener(new changOnclick());
		// ==========================��ȡ�����ļ��е���Ϣ=====================/
		/*AppConfig config = new AppConfig(getBaseContext());
		searchRang = config.getStore_SearchRange(getBaseContext());
		ishc = config.getStore_IsHC(getBaseContext());*/
		// ��ʼ������ģ�飬ע�������¼�����
        mSearch = new MKSearch();    
        mSearch.init(searchApp.getInstance().mBMapManager, new DrugStoreSearchListener());
        mSearch.setPoiPageCapacity(pagesize);        

		// ===========================�״μ��ص�����===========================/
		refreshData();// ��һ�μ�������
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
				if (isRefresh) {
					ListViewEmptyError("û���ҵ�������ҩ����Ϣ", new refreshOnclick());
				} else {
					Toast.makeText(BDrugStore.this, "û�и�������", Toast.LENGTH_SHORT).show();
				}
			} else if (res.getCurrentNumPois() > 0) {
				
				if (isRefresh) {
					adapter.clear();
					adapter.addItems(res.getAllPoi());
					adapter.notifyDataSetChanged();
					refreshListView.getRefreshableView().requestFocusFromTouch();
					refreshListView.getRefreshableView().setSelection(0);
					async_error.setVisibility(View.GONE);
					LoadingView.setVisibility(View.GONE);
					pageindex = 0;
					pageindex++;
				} else {
					adapter.addItems(res.getAllPoi());
					adapter.notifyDataSetChanged();
					pageindex++;				
					async_error.setVisibility(View.GONE);
				}				
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
			refreshListView.onRefreshComplete();
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
	
	/**
	 * ��ʼ��PullToRefreshListView<br>
	 * 
	 * @param rtflv
	 * @param adapter
	 */
	public void initPullToRefreshListView(PullToRefreshListView rtflv) {
		rtflv.setMode(Mode.BOTH);
		rtflv.setOnRefreshListener(new MyOnRefreshListener(rtflv));
		rtflv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View View,
					int position, long arg3) {

				//FootView Item ���
				if (arg3 == -1)
					return;
				
				MKPoiInfo itemmap = (MKPoiInfo) parent.getAdapter()
						.getItem(position);
				
				Intent mapintent = new Intent().setClass(BDrugStore.this,
						BDrugStoreMap.class);
				mapintent.putExtra("storename", itemmap.name);
				mapintent.putExtra("address", itemmap.address);
				mapintent.putExtra("phone", itemmap.phoneNum);
				mapintent.putExtra("poilatitudee6", itemmap.pt.getLatitudeE6());
				mapintent.putExtra("poilongitudee6", itemmap.pt.getLongitudeE6());
				startActivity(mapintent);
			}
		});
		if (adapter != null) {
			rtflv.setAdapter(adapter);
		}

		rtflv.getRefreshableView().setSelector(R.color.transparent);
		rtflv.getRefreshableView().setDivider(null);
		rtflv.getRefreshableView().setBackgroundColor(
				getResources().getColor(R.color.drugstore_bg_gray));
		int pixof5dp = DisplayUtil.dip2px(this, 5);
		rtflv.getRefreshableView().setPadding(0, pixof5dp, 0, pixof5dp);
	}
	
	class MyOnRefreshListener implements OnRefreshListener2<ListView> {

		public MyOnRefreshListener(PullToRefreshListView ptflv) {
			
		}

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			// ����ˢ��
			refreshView.getLoadingLayoutProxy().setReleaseLabel("�ſ�ˢ������");
			String label = DateUtils.formatDateTime(BDrugStore.this,
					System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
							| DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_ABBREV_ALL);
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
			refreshData();
			// ˢ�����ݺ�״̬��ԭ
			isCompleted = false;
			refreshView.getLoadingLayoutProxy().setPullLabel("����ˢ��");
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			// ��������
			// ������и�������
			loadMoreData();
		}
	}

	/**
	 * �ı������ļ� -dialogOnclick
	 */
	/*private final class dialogOnclicck implements OnClickListener {
		@Override
		public void onClick(View v) {

			CustomeDialog customeDialog = new CustomeDialog(BDrugStore.this,
					R.style.dialog);
			Window window = customeDialog.getWindow();
			LayoutParams params = new LayoutParams();
			params.y = getResources().getDimensionPixelOffset(
					R.dimen.actionbar_height);
			params.height = LayoutParams.WRAP_CONTENT;
			params.gravity = Gravity.TOP;
			window.setAttributes(params);
			
			customeDialog.setCanceledOnTouchOutside(true);

			customeDialog.show();
			customeDialog.setonOklistrner("ȷ��", new OnClickListener() {

				@Override
				public void onClick(View v) {
					Map<String, String> configmap = (Map<String, String>) v
							.getTag();
					searchRang = Integer.parseInt(configmap
							.get(AppConfig.Store_SearchRange));
					ishc = Integer.parseInt(configmap.get(AppConfig.Store_IsHC));

					async_error.setVisibility(View.GONE);
					async_begin.setVisibility(View.VISIBLE);
					new AsyncLoader_Refresh().execute();// �����첽��������
				}
			});
		}
	}*/

	/**
	 * ����ɸѡ������ȡ��Ӧ��ҩ������
	 * @return
	 */
	/*private List<MKPoiInfo> getData(int pageindex){		
		List<MKPoiInfo> innerlist = null;
		latitude = station.latitude;
		longitude = station.longitude;
		String cityName = station.city;
		
		if (searchRang == -1) {
			innerlist = coolbuy360.logic.BDrugStore.getList(cityName, latitude,
					longitude, ishc, pagesize, pageindex + 1);
		} else {
			innerlist = coolbuy360.logic.BDrugStore.getList((double) searchRang,
					latitude, longitude, ishc, pagesize, pageindex + 1);
		}
		return innerlist;
	}*/

	/**
	 * ��ת��ͼģʽ -changOnclick()
	 */
	private final class changOnclick implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent mapIntent = new Intent(BDrugStore.this, BDrugStoreMap.class);
			startActivity(mapIntent);
		}
	}

	/**
	 * ˢ������
	 */
	private void refreshData() {
		try {
			if (Util.isNetworkConnected(this)) {
				if (innerLocationProvider != null) {
					station = innerLocationProvider.getLocation();
					if (station == null || (station.latitude == 0.0 && station.longitude == 0.0)) {
						innerLocationProvider.updateListener();
						station = innerLocationProvider.getLocation();
					}
					if (station == null || (station.latitude == 0.0 && station.longitude == 0.0)) {
						setLocaltionError();
						refreshListView.onRefreshComplete();
					}
				} else {
					setLocaltionError();
					refreshListView.onRefreshComplete();
				}
				isRefresh = true;
				localpt = new GeoPoint((int) (station.latitude * 1E6),
						(int) (station.longitude * 1E6));
				localpt = CoordinateConvert.fromGcjToBaidu(localpt);
				adapter.setLocalGeoPoint(localpt);
				mSearch.poiSearchNearBy(searchkey, localpt, searchrange);
			} else {
				setNetError();
				refreshListView.onRefreshComplete();
			}
		} catch (Exception e) {
			setNetError();
			refreshListView.onRefreshComplete();
		}
	}
	
	/**
	 * ���ظ�������
	 */
	private void loadMoreData() {
		try {
			if (Util.isNetworkConnected(this)) {
				if (pageindex > 0) {
					isRefresh = false;
					mSearch.goToPoiPage(pageindex);
				} else {
					refreshListView.onRefreshComplete();
				}
			} else {
				Toast.makeText(BDrugStore.this, "�������Ӵ���", Toast.LENGTH_SHORT)
						.show();
				refreshListView.onRefreshComplete();
			}
		} catch (Exception e) {
			Toast.makeText(BDrugStore.this, "�������Ӵ���", Toast.LENGTH_SHORT)
					.show();
			refreshListView.onRefreshComplete();			
		}
	}
	
	/**
	 * ���ö�λ������ʾ
	 */
	private void setLocaltionError() {
		TextView async_error_txt = (TextView) async_error
				.findViewById(R.id.async_error_txt);
		Button async_error_refleshbtn = (Button) async_error
				.findViewById(R.id.async_error_reflesh);
		async_error.setVisibility(View.VISIBLE);
		async_error_txt.setText(R.string.error_location);	
		async_error_refleshbtn.setVisibility(View.VISIBLE);
		async_error_refleshbtn.setOnClickListener(new refreshOnclick());
		ListViewEmptyError(R.string.error_location,new refreshOnclick());
	}
	
	/**
	 * �������������ʾ
	 */
	private void setNetError() {
		TextView async_error_txt = (TextView) async_error
				.findViewById(R.id.async_error_txt);
		Button async_error_refleshbtn = (Button) async_error
				.findViewById(R.id.async_error_reflesh);
		async_error.setVisibility(View.VISIBLE);
		async_error_txt.setText(R.string.error_nonetwork);	
		async_error_refleshbtn.setVisibility(View.VISIBLE);
		async_error_refleshbtn.setOnClickListener(new refreshOnclick());
		ListViewEmptyError(R.string.error_nonetwork,new refreshOnclick());
	}
	
	/**
	 * �����б�Ϊ�մ�����ʾ
	 * @param message
	 * @param onclick
	 */
	public void ListViewEmptyError(int message, OnClickListener onclick) {
		LoadingView.setVisibility(View.GONE);
		refreshListView.setEmptyView(listview_empty_error);
		TextView txtMessageView = (TextView) listview_empty_error
				.findViewById(R.id.listview_empty_message);
		txtMessageView.setText(message);
		Button btnRefreshViewButton = (Button) listview_empty_error
				.findViewById(R.id.listview_empty_refresh);
		btnRefreshViewButton.setOnClickListener(onclick);
	}
	
	/**
	 * �����б�Ϊ�մ�����ʾ
	 * @param message
	 * @param onclick
	 */
	public void ListViewEmptyError(String message, OnClickListener onclick) {
		LoadingView.setVisibility(View.GONE);
		refreshListView.setEmptyView(listview_empty_error);
		TextView txtMessageView = (TextView) listview_empty_error
				.findViewById(R.id.listview_empty_message);
		txtMessageView.setText(message);
		Button btnRefreshViewButton = (Button) listview_empty_error
				.findViewById(R.id.listview_empty_refresh);
		btnRefreshViewButton.setOnClickListener(onclick);
	}
	
	/**
	 * ˢ�µ��
	 */
	public class refreshOnclick implements OnClickListener
	{		
		@Override
		public void onClick(View v) {
			async_error.setVisibility(View.GONE);
			listview_empty_error.setVisibility(View.GONE);
			LoadingView.setVisibility(View.VISIBLE);			
			//async_begin.setVisibility(View.VISIBLE);
			refreshData();
		}
	}
		
	/**
	 * ģ��Home������ϵͳ��������Ӧ�ý���
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			Intent intent = new Intent(Intent.ACTION_MAIN);  
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// ע��  
            intent.addCategory(Intent.CATEGORY_HOME);  
            this.startActivity(intent);  
            return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
