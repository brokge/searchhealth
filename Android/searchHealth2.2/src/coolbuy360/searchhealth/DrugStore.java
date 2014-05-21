package coolbuy360.searchhealth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData.Item;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import coolbuy360.adapter.StoreListViewAdapter;
import coolbuy360.logic.AppConfig;
import coolbuy360.logic.ConstantsSetting;
import coolbuy360.pulltorefresh.PullToRefreshBase;
import coolbuy360.pulltorefresh.PullToRefreshListView;
import coolbuy360.pulltorefresh.PullToRefreshBase.Mode;
import coolbuy360.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import coolbuy360.service.DisplayUtil;
import coolbuy360.service.LocationInfo;
import coolbuy360.service.LocationProvider;
import coolbuy360.service.TimestampException;
import coolbuy360.service.searchApp;

@SuppressLint("NewApi")
public class DrugStore extends Activity {

	private PullToRefreshListView refreshListView;
	
	private LocationProvider innerLocationProvider = null;
	LocationInfo.SItude station = null;
	
	/*protected double latitude = 30.280506;
	protected double longitude = 120.107582;*/
	protected double latitude = 0.0;
	protected double longitude = 0.0;
	
	private StoreListViewAdapter adapter;
	private int pageIndex = 0;
	private int pagesize = ConstantsSetting.QLDefaultPageSize;

	private int searchRang;
	private int ishc;
	LinearLayout async_begin;
	LinearLayout async_error;
	//TextView address;

	Boolean isCompleted = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		innerLocationProvider = searchApp.mLocationProvider;
		// Ϊ�˳���׼��
		searchApp.getInstance().addActivity(this);
		setContentView(R.layout.drugstore);

		// ========================����ing�Ŀؼ�===============================/
		async_begin = (LinearLayout) findViewById(R.id.async_begin);
		async_begin.setVisibility(View.VISIBLE);
		async_error = (LinearLayout) findViewById(R.id.async_error);	

		adapter = new StoreListViewAdapter(this, new ArrayList<Map<String,String>>());
		refreshListView = (PullToRefreshListView) findViewById(R.id.drugstorelist_ptflistview);
		initPullToRefreshListView(refreshListView);
		
		ImageButton imgbtndialog = (ImageButton) this
				.findViewById(R.id.s_dialogTitle);
		imgbtndialog.setOnClickListener(new dialogOnclicck());

		ImageButton actionbar_tomap_btn = (ImageButton) this
				.findViewById(R.id.actionbar_tomap_btn);
		actionbar_tomap_btn.setOnClickListener(new changOnclick());
		// ==========================��ȡ�����ļ��е���Ϣ=====================/
		AppConfig config = new AppConfig(getBaseContext());
		searchRang = config.getStore_SearchRange(getBaseContext());
		ishc = config.getStore_IsHC(getBaseContext());
		// ===========================�״μ��ص�����===========================/
		new AsyncLoader_Refresh().execute();// ��һ�μ�������
		//address = (TextView) this.findViewById(R.id.s_map_addre_txt);		
	}
	
	/**
	 * ��ʼ��PullToRefreshListView
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
				
				Map<String, String> map = (Map<String, String>) parent.getAdapter()
						.getItem(position);

				String storeid = map.get("drugstoreid");
				// Toast.makeText(DrugStore.this, storeid, 1).show();
				Bundle innerbundle = new Bundle();
				innerbundle.putString("drugstoreid", storeid);
				Intent storedetailIntent = new Intent().setClass(
						DrugStore.this, DrugStoreDetaill.class);
				storedetailIntent.putExtras(innerbundle);
				startActivity(storedetailIntent);
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
			String label = DateUtils.formatDateTime(DrugStore.this,
					System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
							| DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_ABBREV_ALL);
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
			new AsyncLoader_Refresh().execute();
			// ˢ�����ݺ�״̬��ԭ
			isCompleted = false;
			refreshView.getLoadingLayoutProxy().setPullLabel("����ˢ��");
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			// ��������
			// ������и�������
			new AsynLoader_More().execute();
		}
	}

	/**
	 * �ı������ļ� -dialogOnclick
	 * 
	 * @author Administrator
	 * 
	 */
	private final class dialogOnclicck implements OnClickListener {
		@Override
		public void onClick(View v) {

			CustomeDialog customeDialog = new CustomeDialog(DrugStore.this,
					R.style.dialog);
			Window window = customeDialog.getWindow();
			LayoutParams params = new LayoutParams();
			params.y = getResources().getDimensionPixelOffset(
					R.dimen.actionbar_height);
			params.height = LayoutParams.WRAP_CONTENT;
			params.gravity = Gravity.TOP;
			window.setAttributes(params);
			
			// customeDialog.setButton("ȷ��", null);
			customeDialog.setCanceledOnTouchOutside(true);
			// customeDialog.setButton(R.id.dialog_ok, "ȷ��",new okonclick());

			// customeDialog.setc

			/*
			 * customeDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
			 * new DialogInterface.OnClickListener() { public void
			 * onClick(DialogInterface dialog, int whichButton) {
			 * 
			 * User clicked Yes so do some stuff
			 * Toast.makeText(getApplicationContext(), "11",1).show();
			 * //initdate(); } });
			 */

			customeDialog.show();
			customeDialog.setonOklistrner("ȷ��", new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// Toast.makeText(DrugStore.this , "����¼������ˣ�", 1).show();

					Map<String, String> configmap = (Map<String, String>) v
							.getTag();
					/*
					 * aa.put(AppConfig.Store_SearchRange, searchRang+"");
					 * aa.put(AppConfig.Store_IsHC, ishc+"");
					 */
					searchRang = Integer.parseInt(configmap
							.get(AppConfig.Store_SearchRange));
					ishc = Integer.parseInt(configmap.get(AppConfig.Store_IsHC));

					async_error.setVisibility(View.GONE);
					async_begin.setVisibility(View.VISIBLE);
					new AsyncLoader_Refresh().execute();// �����첽��������
				}
			});
		}
	}

	/**
	 * ����ɸѡ������ȡ��Ӧ��ҩ������
	 * @return
	 */
	private List<Map<String, String>> getData(int pageindex){		
		List<Map<String, String>> innerlist = null;
		latitude = station.latitude;
		longitude = station.longitude;
		String cityName = station.city;
		
		if (searchRang == -1) {
			innerlist = coolbuy360.logic.DrugStore.getList(cityName, latitude,
					longitude, ishc, pagesize, pageindex + 1);
		} else {
			innerlist = coolbuy360.logic.DrugStore.getList((double) searchRang,
					latitude, longitude, ishc, pagesize, pageindex + 1);
		}
		return innerlist;
	}

	/**
	 * ��ת��ͼģʽ -changOnclick()
	 * 
	 * @author Administrator
	 * 
	 */
	private final class changOnclick implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// Toast.makeText(DrugStore.this, "ҩ���ͼģʽ������", 0).show();
			Intent mapIntent = new Intent(DrugStore.this, DrugStoreMap.class);
			startActivity(mapIntent);
		}
	}

	/**
	 * �첽���ظ���ķ���
	 * 
	 * @author Administrator
	 * 
	 */
	private class AsynLoader_More extends AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerlist = null;
		
		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				if(innerLocationProvider != null){
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
				innerlist = getData(pageIndex);// ��ȡ����
				if (innerlist != null) {
					return (innerlist.size() > 0) ? 0 : 1;
				} else {
					return 2;// �������Ӵ���
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
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			switch (result) {
			case 0:
				adapter.addItems(innerlist);
				adapter.notifyDataSetChanged();
				pageIndex++;				
				async_error.setVisibility(View.GONE);
				break;
			case 1:
				Toast.makeText(DrugStore.this, "û�и�������", Toast.LENGTH_SHORT)
						.show();
				break;
			case 2:
				Toast.makeText(DrugStore.this, "�������Ӵ���", Toast.LENGTH_SHORT)
						.show();
				break;
			case 3:
				Toast.makeText(DrugStore.this, "�ֻ���λʧ��", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
			refreshListView.onRefreshComplete();
		}
	}

	/**
	 * ��һ�μ��ظ���ҩ������
	 */
	private class AsyncLoader_Refresh extends
			AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerlist = null;
		
		@Override
		protected Integer doInBackground(String... params) {
			try {
				
				//Log.i(ConstantsSetting.EfficiencyTestTag, "begin to call DrugStore list Logic.");
				
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
				innerlist = getData(0);
				if (innerlist != null) {
					return (innerlist.size() > 0) ? 0 : 1;
				} else {
					return 2;// �������Ӵ���
				}
			} catch (TimestampException ex) {
				return -2;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return 2;
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		// �������
		protected void onPostExecute(Integer result) {
			if (result == 0) {								
				adapter.clear();
				adapter.addItems(innerlist);
				adapter.notifyDataSetChanged();
				//refreshListView.getRefreshableView().scrollTo(0, 0);
				refreshListView.getRefreshableView().requestFocusFromTouch();
				refreshListView.getRefreshableView().setSelection(0);
				async_begin.setVisibility(View.GONE);
				async_error.setVisibility(View.GONE);
				pageIndex=0;
				pageIndex++;
				
				//Log.i(ConstantsSetting.EfficiencyTestTag, "DrugStore list data UI show completed.");
				
			} else if (result == 1) {
				async_begin.setVisibility(View.GONE);
				TextView async_error_txt = (TextView) async_error
						.findViewById(R.id.async_error_txt);
				if (searchRang == -1) {
					async_error_txt.setText("û���ҵ������ڳ��е����ҩ����Ϣ��");
				} else {
					async_error_txt
							.setText("û���ҵ�����" + searchRang + "�����ڵ����ҩ����Ϣ��");
				}
				Button async_error_reflesh = (Button) async_error
						.findViewById(R.id.async_error_reflesh);
				async_error_reflesh.setVisibility(View.GONE);
				async_error.setVisibility(View.VISIBLE);
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
						new AsyncLoader_Refresh().execute();
					}
				});
			} 
			refreshListView.onRefreshComplete();
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

	/*private long exitTime = 0;

	// ��д�˳���ť���¼�
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				// Log.i(TAG, "�˳�");
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}*/

}
