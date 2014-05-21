package coolbuy360.searchhealth;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import coolbuy360.adapter.BlackListViewAdapter;

import coolbuy360.logic.ConstantsSetting;
import coolbuy360.service.TimestampException;
import coolbuy360.service.searchApp;

public class BlackDrug extends Activity {
	BlackListViewAdapter listAdapter;
	private ListView black_drug_listview;
	private int pageSize = ConstantsSetting.QLDefaultPageSize;
	private int pageIndex = 1;
	private boolean isloading;
	LinearLayout async_begin;
	LinearLayout async_error;
	private Button loadMoreButton;
	private ProgressBar proBar;
	private TextView protxt;
	private View loadMoreView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exposure_drug);
		//为退出做准备
		searchApp.getInstance().addActivity(this);
		black_drug_listview = (ListView) this.findViewById(R.id.Exposure_list);
		
		async_begin = (LinearLayout) findViewById(R.id.async_begin);
		async_begin.setVisibility(View.GONE);
		async_error = (LinearLayout) findViewById(R.id.async_error);
		
		black_drug_listview.setVisibility(View.GONE);
		// ××××××××××××××××××准备初始化foot_loading控件×××××××××××××××××××××
		loadMoreView = getLayoutInflater().inflate(R.layout.p_druglist_foot,null);
		loadMoreButton = (Button) loadMoreView.findViewById(R.id.p_listview_footbtn);
		proBar = (ProgressBar) loadMoreView.findViewById(R.id.p_probar);
		protxt = (TextView) loadMoreView.findViewById(R.id.p_protxt);
		black_drug_listview.addFooterView(loadMoreView); // 设置列表底部视图
		
		new AsyncLoader_GuessInfo().execute();
	}

	private final class onScrollListener implements OnScrollListener
	{
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (firstVisibleItem + visibleItemCount == totalItemCount) {
				if (!isloading)// 如果没有加载中
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

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub			
		}		
	}

	private final class itemOnClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int posion,
				long arg3) {
			// TODO Auto-generated method stub
			
			//FootView Item 点击
			if (arg3 == -1)
				return;
			
			// Toast.makeText(BlackDrug.this, "haha", 1).show();
			Map<String, String> map = (Map<String, String>) black_drug_listview
					.getItemAtPosition(posion);
			// 包含键值：BlackListID、DrugID、PubTime、DangerLever、Danger、drugname。所有键值小写。
			String drugId = map.get("blacklistid");
			Bundle bundle = new Bundle();
			bundle.putString("drugnameid", drugId);
			bundle.putString("drugtype", "1");

			Intent drugintent = new Intent().setClass(BlackDrug.this,
					ExposureDetail.class);
			drugintent.putExtras(bundle);
			startActivity(drugintent);
		}
	}	
	
	/**
	 * 翻页加载更多数据
	 */
	private class AsynLoader_more extends AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerdruglist;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isloading = true;
		}

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub

			try {
				innerdruglist = coolbuy360.logic.BlackDrug.getList(pageSize,
						pageIndex + 1);
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
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			// StrictModeWrapper.init(getApplicationContext());
			if (result == 0) {
				for (Map<String, String> item : innerdruglist) {
					listAdapter.addItem(item);
				}
				if (innerdruglist.size() < pageSize) {
					// loadMoreView.setVisibility(View.GONE);
					loadMoreButton.setText("没有更多数据");
				} else {
					loadMoreButton.setText("更多");
					loadMoreButton.setVisibility(View.VISIBLE);
				}
				proBar.setVisibility(View.GONE);
				protxt.setVisibility(View.GONE);// 隐藏progressbar
				isloading = false;
				listAdapter.count += innerdruglist.size();
				listAdapter.notifyDataSetChanged();
				pageIndex++;
			} else if (result == 1) { // 如果数据加载完后
				if (innerdruglist.size() < pageSize) {
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
			} else if (result == 2) {
				loadMoreButton.setVisibility(View.VISIBLE);
				loadMoreButton.setBackgroundColor(getResources().getColor(
						R.color.transparent));
				loadMoreButton.setText("网络连接错误");
				proBar.setVisibility(View.GONE);
				protxt.setVisibility(View.GONE);// 隐藏progressbar
				isloading = false;
			}
		}
	}

	private void initData(List<Map<String, String>> druglist) {

		listAdapter = new BlackListViewAdapter(BlackDrug.this, druglist);
		listAdapter.count = druglist.size();
		if (druglist.size() < pageSize) {
			loadMoreView.setVisibility(View.GONE);
		}
		black_drug_listview.setVisibility(View.VISIBLE);
		black_drug_listview.setAdapter(listAdapter);
		
		black_drug_listview.setOnItemClickListener(new itemOnClick());
		black_drug_listview.setOnScrollListener(new onScrollListener());

		//loadMoreButton.setOnClickListener(new moreOnClick());
	}
	
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

	/**
	 * 第一次异步加载数据
	 */
	class AsyncLoader_GuessInfo extends AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerdruglist;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loadMoreView.setVisibility(View.VISIBLE);
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				// 加载数据
				innerdruglist = coolbuy360.logic.BlackDrug.getList(pageSize, 1);
				if (innerdruglist != null) {
					return (innerdruglist.size() > 0) ? 0 : 1;
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
		// 处理界面
		protected void onPostExecute(Integer result) {
			if (result == 0) {
				// initData();
				initData(innerdruglist);
				async_begin.setVisibility(View.GONE);
			} else if (result == 1) {
				async_begin.setVisibility(View.GONE);
				TextView async_error_txt = (TextView) async_error
						.findViewById(R.id.async_error_txt);
				Button async_error_reflesh = (Button) async_error
						.findViewById(R.id.async_error_reflesh);
				async_error_reflesh.setVisibility(View.GONE);
				async_error_txt.setText("没有找到相关的黑榜信息");
				async_error.setVisibility(View.VISIBLE);
			} else {
				async_begin.setVisibility(View.GONE);
				async_error.setVisibility(View.VISIBLE);
				TextView async_error_txt = (TextView) async_error
						.findViewById(R.id.async_error_txt);
				if (result == -2) {
					async_error_txt.setText(R.string.error_timestamp);
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
		}
	}

}
