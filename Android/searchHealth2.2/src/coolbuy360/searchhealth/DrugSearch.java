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
import coolbuy360.adapter.DrugSearchListViewAdapter;
import coolbuy360.logic.ConstantsSetting;
import coolbuy360.logic.Drug;
import coolbuy360.service.TimestampException;
import coolbuy360.service.searchApp;
import coolbuy360.zxing.CaptureActivity;

public class DrugSearch extends Activity {
	private String keywordString;
	private ListView druglist_listview;
	private Button loadMoreButton;
	private View loadMoreView;
	private ProgressBar proBar;
	private TextView protxt;
	EditText searchbar_inputtext;
	private DrugSearchListViewAdapter adapter;

	private int pageIndex = 1;
	private int pageSize = ConstantsSetting.QLDefaultPageSize;
	private boolean isloading;

	LinearLayout async_begin;
	LinearLayout async_error;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.p_drug_search_result);
		// Ϊ�˳���׼��
		searchApp.getInstance().addActivity(this);
		Bundle bundle = getIntent().getExtras();
		keywordString = bundle.get("keyword").toString();

		// *******************************����ļ����¼���������������������������������������������������������������
		async_begin = (LinearLayout) findViewById(R.id.async_begin);
		async_begin.setVisibility(View.VISIBLE);
		async_error = (LinearLayout) findViewById(R.id.async_error);
		druglist_listview = (ListView) this.findViewById(R.id.druglist_listview);
		druglist_listview.setVisibility(View.GONE);

		loadMoreView = getLayoutInflater().inflate(R.layout.p_druglist_foot,
				null);
		loadMoreButton = (Button) loadMoreView
				.findViewById(R.id.p_listview_footbtn);
		proBar = (ProgressBar) loadMoreView.findViewById(R.id.p_probar);
		protxt = (TextView) loadMoreView.findViewById(R.id.p_protxt);
		druglist_listview.addFooterView(loadMoreView);//
		searchbar_inputtext = (EditText) this.findViewById(R.id.searchbar_inputtext);
		searchbar_inputtext.setText(keywordString);

		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DrugSearch.this.finish();
			}
		});

		//������ť�¼�
		ImageButton searchbar_searchbtn = (ImageButton) this
				.findViewById(R.id.searchbar_searchbtn);
		searchbar_searchbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				keywordString = searchbar_inputtext.getText().toString().trim();
				if (!keywordString.equals("")) {
					async_begin.setVisibility(View.VISIBLE);
					druglist_listview.setVisibility(View.GONE);
					new AsyncLoader().execute();
				} else {
					Toast.makeText(DrugSearch.this, "�������ѯ�ؼ��֣�", Toast.LENGTH_SHORT).show();
				}

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				// ���������
				imm.hideSoftInputFromWindow(searchbar_inputtext.getWindowToken(), 0);
			}
		});		

		//ɨ�谴ť�¼�
		ImageButton searchbar_scanbtn = (ImageButton) this
				.findViewById(R.id.searchbar_scanbtn);
		searchbar_scanbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent openCameraIntent = new Intent(DrugSearch.this,
						CaptureActivity.class);
				startActivityForResult(openCameraIntent, 0);
			}
		});

		new AsyncLoader().execute();
	}

	/**
	 * ���ݽ���������󶨼�����
	 * 
	 * @param druglist
	 */
	private void initAdapter(List<Map<String, String>> druglist) {
		// TODO Auto-generated method stub

		adapter = new DrugSearchListViewAdapter(this, druglist);
		adapter.count = druglist.size();// �����һҳû��4���Ļ���ô�죿
		if (druglist.size() < pageSize) {
			loadMoreView.setVisibility(View.GONE);
		}
		druglist_listview.setVisibility(View.VISIBLE);
		druglist_listview.setAdapter(adapter);// �Զ�ΪidΪ��list��listview����������

		druglist_listview.setOnScrollListener(new OnScrollListener() {

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

		druglist_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int posion,
					long arg3) {
				
				//FootView Item ���
				if (arg3 == -1)
					return;
				
				// ExpandableListView ev = (ExpandableListView) parent;
				Map<String, String> map = (Map<String, String>) druglist_listview
						.getItemAtPosition(posion);
				String drug_name = map.get("drugname");
				String drug_id = map.get("drugid");
				String drug_imgurl = map.get("drugimg");
				String drug_store = map.get("enterprisename");
				String drug_otc = map.get("prescriptiontype");
				String drug_h = map.get("approvaltype");
				String drug_bao = map.get("ishcdrug");

				Bundle bundle = new Bundle();
				bundle.putString("drugname", drug_name);
				bundle.putString("drugid", drug_id);
				bundle.putString("drugimg", drug_imgurl);
				bundle.putString("drugstore", drug_store);
				bundle.putString("h", drug_h.trim());
				bundle.putString("otc", drug_otc.trim());
				bundle.putString("bao", drug_bao.trim());
				/*
				 * new Intent().setClass(DrugProduct.this, DrugList.class);
				 */
				Intent drugintent = new Intent().setClass(DrugSearch.this,
						DrugProductDetail.class);// ������һ��activity
				drugintent.putExtras(bundle);
				startActivity(drugintent);
			}
		});
		//loadMoreButton.setOnClickListener(new moreOnClick());
	}

	/**
	 * ��һ�μ���ҳ���첽��ȡ����ҩƷ�б�
	 */
	private class AsyncLoader extends AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerdruglist;

		@Override
		protected Integer doInBackground(String... params) {
			try {
				// ��������
				innerdruglist = Drug.getListByKeyWord(keywordString, pageSize,
						1);
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
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loadMoreButton.setText("����");
			loadMoreView.setVisibility(View.VISIBLE);
		}

		@Override
		// �������
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
				async_error_txt.setText("û���ҵ���ص�ҩƷ");
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
						// ���������
						imm.hideSoftInputFromWindow(
								searchbar_inputtext.getWindowToken(), 0);
						
						new AsyncLoader().execute();
					}
				});
				return;
			}
		}
	}

	private final class moreOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			loadMoreButton.setVisibility(View.GONE);
			proBar.setVisibility(View.VISIBLE);
			protxt.setVisibility(View.VISIBLE);
			new AsynLoader_more().execute();
		}
	}

	/**
	 * �첽���ظ�������
	 */
	private class AsynLoader_more extends AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerdruglist;

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				innerdruglist = Drug.getListByKeyWord(keywordString, pageSize,
						pageIndex + 1);
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
				proBar.setVisibility(View.GONE);
				protxt.setVisibility(View.GONE);// ����progressbar
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
				// isloading = false;
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// ����ɨ�������ڽ�������ʾ��
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");

			Intent drugdetailIntent = new Intent().setClass(DrugSearch.this,
					DrugProductDetail.class);
			Bundle bundle1 = new Bundle();
			bundle1.putString("serisecode", scanResult);
			drugdetailIntent.putExtras(bundle1);
			startActivity(drugdetailIntent);
		}
	}
}
