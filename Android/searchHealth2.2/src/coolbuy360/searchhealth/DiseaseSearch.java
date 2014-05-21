package coolbuy360.searchhealth;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import coolbuy360.adapter.DiseaseSearchListViewAdater;
import coolbuy360.logic.ConstantsSetting;
import coolbuy360.logic.Disease;

import coolbuy360.service.TimestampException;
import coolbuy360.service.searchApp;

public class DiseaseSearch extends Activity {

	private String keywordString;
	private ListView searchListView;
	private Button loadMoreButton;
	private View loadMoreView;
	private ProgressBar proBar;
	private TextView protxt;
	EditText searchbar_inputtext;
	private DiseaseSearchListViewAdater adapter;

	private int pageIndex = 1;
	private int pagesize = ConstantsSetting.QLDefaultPageSize;
	private boolean isloading;

	LinearLayout async_begin;
	LinearLayout async_error;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.d_disease_search_result);
		// 为退出做准备
		searchApp.getInstance().addActivity(this);
		Bundle bundle = getIntent().getExtras();
		keywordString = bundle.get("disekeyword").toString();

		loadMoreView = getLayoutInflater().inflate(R.layout.p_druglist_foot,
				null);
		loadMoreButton = (Button) loadMoreView
				.findViewById(R.id.p_listview_footbtn);
		proBar = (ProgressBar) loadMoreView.findViewById(R.id.p_probar);
		protxt = (TextView) loadMoreView.findViewById(R.id.p_protxt);
		searchListView = (ListView) this
				.findViewById(R.id.dis_search_result_list);
		searchListView.addFooterView(loadMoreView);//
		searchbar_inputtext = (EditText) this.findViewById(R.id.searchbar_inputtext);
		ImageButton searchbar_searchbtn = (ImageButton) this
				.findViewById(R.id.searchbar_searchbtn);
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);

		searchbar_searchbtn.setOnClickListener(new searchOnclick());
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DiseaseSearch.this.finish();
			}
		});

		searchbar_inputtext.setText(keywordString);
		searchbar_inputtext.setSelection(keywordString.length());

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// 隐藏软键盘
		imm.hideSoftInputFromWindow(searchbar_inputtext.getWindowToken(), 0);

		async_begin = (LinearLayout) findViewById(R.id.async_begin);
		async_begin.setVisibility(View.VISIBLE);
		async_error = (LinearLayout) findViewById(R.id.async_error);
		searchListView.setVisibility(View.GONE);
		new AsyncLoader().execute();
	}

	/**
	 * 数据解析，界面绑定及处理
	 * 
	 * @param diseaselist
	 */
	private void initAdapter(List<Map<String, String>> diseaselist) {
		// TODO Auto-generated method stub

		adapter = new DiseaseSearchListViewAdater(this, diseaselist);
		adapter.count = diseaselist.size();// 如果第一页没有4条的话怎么办？
		if (diseaselist.size() < pagesize) {
			loadMoreView.setVisibility(View.GONE);
		}
		searchListView.setVisibility(View.VISIBLE);
		searchListView.setAdapter(adapter);// 自动为id为了list的listview设置适配器

		searchListView.setOnScrollListener(new OnScrollListener() {

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
						// Toast.makeText(DrugStore.this, pageIndex, 1)
						// .show();
						// Log.i("pageindex", pageIndex + "," + pageSize);
						new AsynLoader_more().execute();
					} else {
						// storeListView.removeFooterView(loadMoreView);//
					}
				}
			}
		});

		searchListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long arg3) {
				
				//FootView Item 点击
				if (arg3 == -1)
					return;
				
				// ExpandableListView ev = (ExpandableListView) parent;
				Map<String, String> maplist = (Map<String, String>) parent
						.getItemAtPosition(position);
				String diseaseid = maplist.get("diseaseid");
				String diseasename = maplist.get("diseasename");
				Intent decriptionIntent = new Intent().setClass(
						DiseaseSearch.this, DiseaseDecription.class);
				Bundle bundle = new Bundle();
				bundle.putString("diseaseid", diseaseid);
				bundle.putString("diseasesname", diseasename);
				decriptionIntent.putExtras(bundle);
				startActivity(decriptionIntent);
			}
		});
		//loadMoreButton.setOnClickListener(new moreOnClick());
	}

	/**
	 * 第一次加载页面异步读取搜索药品列表
	 */
	private class AsyncLoader extends AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerdruglist;

		@Override
		protected Integer doInBackground(String... params) {
			try {
				// 加载数据
				if (keywordString.length() > 0) {
					innerdruglist = Disease.getListByKeyWord(keywordString,
							pagesize, 1);
				}
				if (innerdruglist != null) {
					return (innerdruglist.size() > 0) ? 0 : 1;
				} else {
					return 2;// 网络连接错误
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
			loadMoreButton.setText("更多");
			loadMoreView.setVisibility(View.VISIBLE);
		}

		@Override
		// 处理界面
		protected void onPostExecute(Integer result) {
			if (result == 0) {
				initAdapter(innerdruglist);
				async_begin.setVisibility(View.GONE);
				async_error.setVisibility(View.GONE);
				isloading = false;
				pageIndex = 1;
			} else if (result == 1) {
				async_begin.setVisibility(View.GONE);
				TextView async_error_txt = (TextView) async_error
						.findViewById(R.id.async_error_txt);
				async_error_txt.setText("没有找到您查询的疾病信息。");
				Button async_error_reflesh = (Button) async_error
						.findViewById(R.id.async_error_reflesh);
				async_error_reflesh.setVisibility(View.GONE);
				async_error.setVisibility(View.VISIBLE);
			} else {
				// Toast.makeText(DrugStore.this, "-1", 1).show();
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

						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						// 隐藏软键盘
						imm.hideSoftInputFromWindow(
								searchbar_inputtext.getWindowToken(), 0);

						new AsyncLoader().execute();
					}
				});
			}
		}
	}

	/**
	 * 搜索按钮点击
	 */
	private final class searchOnclick implements
			android.view.View.OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			keywordString = searchbar_inputtext.getText().toString().trim();
			if (!keywordString.equals("")) {
				async_begin.setVisibility(View.VISIBLE);
				searchListView.setVisibility(View.GONE);
				new AsyncLoader().execute();
			} else {
				Toast.makeText(DiseaseSearch.this, "请输入查询关键字！", Toast.LENGTH_SHORT).show();
			}

			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			// 隐藏软键盘
			imm.hideSoftInputFromWindow(searchbar_inputtext.getWindowToken(), 0);
		}
	}

	private final class moreOnClick implements
			android.view.View.OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			loadMoreButton.setVisibility(View.GONE);
			proBar.setVisibility(View.VISIBLE);
			protxt.setVisibility(View.VISIBLE);
			new AsynLoader_more().execute();
		}
	}

	/**
	 * 异步加载更多数据
	 */
	private class AsynLoader_more extends AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerdruglist;

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				innerdruglist = Disease.getListByKeyWord(keywordString,
						pagesize, pageIndex + 1); // 取得当前页数据后添加大druglist中

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
				;
				adapter.notifyDataSetChanged();
				pageIndex++;
			} else if (result == 1) {
				if (innerdruglist.size() < pagesize) {
					loadMoreView.setVisibility(View.GONE);
				} else {
					loadMoreButton.setVisibility(View.VISIBLE);
				}

				loadMoreButton.setBackgroundColor(getResources().getColor(
						R.color.transparent));
				loadMoreButton.setClickable(false);
				loadMoreButton.setText("没有更多数据！");
				proBar.setVisibility(View.GONE); // 隐藏progressbar
				protxt.setVisibility(View.GONE);
			} else if (result == 2)// 如果数据加载完后
			{
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
}
