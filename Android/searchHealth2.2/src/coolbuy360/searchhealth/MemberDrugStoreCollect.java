package coolbuy360.searchhealth;

import java.util.List;
import java.util.Map;

import coolbuy360.adapter.StoreListViewCollegeAdapter;
import coolbuy360.logic.ConstantsSetting;
import coolbuy360.logic.DrugStoreFavorite;
import coolbuy360.service.searchApp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class MemberDrugStoreCollect extends Activity {
	private StoreListViewCollegeAdapter adapter;
	int pageSize = ConstantsSetting.QLDefaultPageSize;
	int pageIndex = 1;
	private boolean isloading;
	ListView drugstorelist_listview;
	
	private Button loadMoreButton;
	private View loadMoreView;
	private ProgressBar proBar;
	private TextView protxt;

	LinearLayout async_begin;
	LinearLayout async_error;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_store_collect);
		// Ϊ�˳���׼��
		searchApp.getInstance().addActivity(this);

		// ���÷��ذ�ť
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MemberDrugStoreCollect.this.finish();
			}
		});

		TextView actionbar_page_title = (TextView) this
				.findViewById(R.id.actionbar_page_title);
		actionbar_page_title.setText(R.string.member_collect_store);
		// =============================�����ظ���ʱ�Ŀؼ�==================/
		loadMoreView = getLayoutInflater().inflate(R.layout.p_druglist_foot,
				null);
		loadMoreButton = (Button) loadMoreView
				.findViewById(R.id.p_listview_footbtn);
		proBar = (ProgressBar) loadMoreView.findViewById(R.id.p_probar);
		protxt = (TextView) loadMoreView.findViewById(R.id.p_protxt);
		drugstorelist_listview = (ListView) this
				.findViewById(R.id.drugstorelist_listview);
		drugstorelist_listview.addFooterView(loadMoreView);//
		// ================================================================
		// ========================����ing�Ŀؼ�===============================/
		async_begin = (LinearLayout) findViewById(R.id.async_begin);
		async_begin.setVisibility(View.VISIBLE);
		async_error = (LinearLayout) findViewById(R.id.async_error);

		new AsyncLoader().execute();
	}

	/**
	 * ���ظ��ఴť����¼� -moreonclick
	 */
	private final class moreOnClick implements OnClickListener {
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
		// ��õ�ǰ����
		adapter = new StoreListViewCollegeAdapter(this, storelist);
		adapter.count = storelist.size();
		if (storelist.size() < pageSize) {
			loadMoreView.setVisibility(View.GONE);
		}
		
		drugstorelist_listview.setVisibility(View.VISIBLE);
		drugstorelist_listview.setAdapter(adapter);

		drugstorelist_listview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (firstVisibleItem + visibleItemCount == totalItemCount) {
					if (!isloading)// ���û�м�����
					{
						loadMoreButton.setVisibility(View.GONE);
						proBar.setVisibility(View.VISIBLE);
						protxt.setVisibility(View.VISIBLE);
						new AsynLoader_more().execute();
					} else {
						// storeListView.removeFooterView(loadMoreView);//
					}
				}
			}
		});
		
		drugstorelist_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> v, View arg1, int posion,
					long arg3) {
				// TODO Auto-generated method stub
				
				//FootView Item ���
				if (arg3 == -1)
					return;
				
				Map<String, String> map = (Map<String, String>) drugstorelist_listview
						.getItemAtPosition(posion);

				String storeid = map.get("drugstoreid");
				// Toast.makeText(DrugStore.this, storeid, 1).show();
				Bundle bundle = new Bundle();
				bundle.putString("drugstoreid", storeid);

				Intent storedetailIntent = new Intent().setClass(
						MemberDrugStoreCollect.this, DrugStoreDetaill.class);
				storedetailIntent.putExtras(bundle);
				startActivity(storedetailIntent);
			}
		});
		
		//loadMoreButton.setOnClickListener(new moreOnClick());
	}

	/**
	 * �첽���ظ��Ķ�ķ���
	 */
	private class AsynLoader_more extends
			AsyncTask<List<Map<String, String>>, Void, Integer> {
		List<Map<String, String>> innerdruglist;

		// 0 �������ݳɹ� ��1 ��������Ϊ�� �� 2�����������Ӵ����Ϊnull
		@Override
		protected Integer doInBackground(List<Map<String, String>>... params) {
			// TODO Auto-generated method stub
			try {
				innerdruglist = DrugStoreFavorite.getListNoLocation(
						getBaseContext(), pageSize, pageIndex + 1);
				if (innerdruglist != null) {
					return (innerdruglist.size() > 0) ? 0 : 1;
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
				if (innerdruglist.size() < pageSize) {
					loadMoreView.setVisibility(View.GONE);
				} else {
					loadMoreButton.setText("����");
					loadMoreButton.setVisibility(View.VISIBLE);
				}
				proBar.setVisibility(View.GONE);// ����progressbar
				protxt.setVisibility(View.GONE);
				isloading = false;
				adapter.count += innerdruglist.size();
				adapter.notifyDataSetChanged();
				pageIndex++;
			} else if (result == 1) { // ������ݼ������
				if (innerdruglist.size() < pageSize) {
					loadMoreView.setVisibility(View.GONE);
				} else {
					loadMoreButton.setVisibility(View.VISIBLE);
				}
				// loadMoreButton.setVisibility(View.VISIBLE);
				loadMoreButton.setBackgroundColor(getResources().getColor(
						R.color.transparent));
				loadMoreButton.setClickable(false);
				loadMoreButton.setText("û�и������ݣ�");
				proBar.setVisibility(View.GONE); // ����progressbar
				protxt.setVisibility(View.GONE);
			} else if (result == 2) {
				loadMoreButton.setVisibility(View.VISIBLE);
				loadMoreButton.setBackgroundColor(getResources().getColor(
						R.color.transparent));
				loadMoreButton.setText("�������Ӵ���");
				proBar.setVisibility(View.GONE);
				protxt.setVisibility(View.GONE);// ����progressbar
				isloading = false;
			}
		}
	}

	/**
	 * ��һ���첽��������
	 */
	private class AsyncLoader extends
			AsyncTask<List<Map<String, String>>, Void, Integer> {
		List<Map<String, String>> innerdruglist;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loadMoreButton.setText("����");
			loadMoreView.setVisibility(View.VISIBLE);
		}

		@Override
		protected Integer doInBackground(List<Map<String, String>>... params) {
			// TODO Auto-generated method stub
			try {
				innerdruglist = DrugStoreFavorite.getListNoLocation(
						getBaseContext(), pageSize, pageIndex);
				if (innerdruglist != null) {
					return (innerdruglist.size() > 0) ? 0 : 1;
				} else {
					return 2;// �������Ӵ���
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
				InitData(innerdruglist);
				async_begin.setVisibility(View.GONE);
			} else if (result == 1) {				
				async_begin.setVisibility(View.GONE);
				TextView async_error_txt = (TextView) async_error
						.findViewById(R.id.async_error_txt);
				Button async_error_reflesh = (Button) async_error
						.findViewById(R.id.async_error_reflesh);
				async_error_reflesh.setVisibility(View.GONE);
				async_error_txt.setText("����û���ղ��κ�ҩ��");
				async_error.setVisibility(View.VISIBLE);
			} else if (result == 2) {								
				async_begin.setVisibility(View.GONE);
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
						async_begin.setVisibility(View.VISIBLE);
						new AsyncLoader().execute();
					}
				});
			}
		}
	}
}
