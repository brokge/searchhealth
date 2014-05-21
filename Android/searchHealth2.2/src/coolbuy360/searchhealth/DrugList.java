package coolbuy360.searchhealth;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import coolbuy360.adapter.ListViewAdapter;
import coolbuy360.control.MyShowImgDialog;
import coolbuy360.logic.ConstantsSetting;
import coolbuy360.logic.Drug;
import coolbuy360.service.StrictModeWrapper;
import coolbuy360.service.TimestampException;
import coolbuy360.service.searchApp;

public class DrugList extends Activity {
	private ListView listView;
	private int pageIndex = 1;// ���ڵڼ�ҳ����
	private int pageSize = ConstantsSetting.QLDefaultPageSize;// ÿҳ��ʾ������
	private boolean isloading;
	private Button loadMoreButton;
	private ProgressBar proBar;
	private TextView protxt;
	private View loadMoreView;
	private ListViewAdapter adapter;
	private String drugTypeID;
	private String diseaseID;
	private String prevPage;

	LinearLayout async_begin;
	LinearLayout async_error;
	searchApp app;
	//OnClickListener showBigOnClick;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// StrictModeWrapper.init(this);
		StrictModeWrapper.init(getBaseContext());
		super.onCreate(savedInstanceState);
		
		// Ϊ�˳���׼��
		app=searchApp.getInstance();
		app.addActivity(this);
		setContentView(R.layout.p_druglist);

		Bundle bundle = getIntent().getExtras();
		prevPage = bundle.getString("from");

		// �����������������������������������������ñ����������������������������������������������������������������������
		TextView actionbar_page_title = (TextView) this.findViewById(R.id.actionbar_page_title);
		if (prevPage.equals("drugtype")) {
			drugTypeID = bundle.getString("type_id"); // ��ȡ�ϸ����洫������ֵ
			actionbar_page_title.setText(bundle.getString("drug_title")); // ȡ���ϸ����洫������typetitle
		} else if (prevPage.equals("disease")) {
			diseaseID = bundle.getString("disease_id"); // ��ȡ�ϸ����洫������ֵ
			actionbar_page_title.setText(bundle.getString("disease_name")); // ȡ���ϸ����洫������disease_name
		}

		listView = (ListView) this.findViewById(R.id.druglist_listview);
		// ������������������������������������׼����ʼ��foot_loading�ؼ�������������������������������������������
		loadMoreView = getLayoutInflater().inflate(R.layout.p_druglist_foot,
				null);
		loadMoreButton = (Button) loadMoreView
				.findViewById(R.id.p_listview_footbtn);
		proBar = (ProgressBar) loadMoreView.findViewById(R.id.p_probar);
		protxt = (TextView) loadMoreView.findViewById(R.id.p_protxt);
		listView.addFooterView(loadMoreView); // �����б�ײ���ͼ
		// *******************************����ļ����¼���������������������������������������������������������������
		async_begin = (LinearLayout) findViewById(R.id.async_begin);
		async_begin.setVisibility(View.VISIBLE);
		async_error = (LinearLayout) findViewById(R.id.async_error);
		listView.setVisibility(View.GONE);
		// ������һ��
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// onKeyDown(KeyEvent.KEYCODE_BACK,null);
				DrugList.this.finish();// ������ǰactivity������һactivity�������Ҫ�������Ļ������51cto�е�����
			}
		});
		// ������������������������������������������������������ʼ���б�����Դ����������������������������������������������������������������������

		new AsyncLoader_GuessInfo().execute();
	}

	/**
	 * ��ʼ��������
	 * 
	 * @param name
	 *            ����idֵ
	 */
	private void initAdapter(List<Map<String, String>> druglist) {
		// TODO Auto-generated method stub
		/*OnClickListener showBigOnClick=new OnClickListener() {				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub				
					//String url=(String)v.getTag();
					String url="http://app.wcjk100.com/app/drugimg/high/1280_800/testimg.jpg";
					//ShowImgDialog show=new ShowImgDialog(url, DrugList.this);
					Bundle bundle=new Bundle();
					bundle.putString("imgpath", url);
					Log.i("chenlinwei", url+"::clss");
					
					Intent intent=new Intent().setClass(DrugList.this, MyShowImgDialog.class);
					intent.putExtras(bundle);
					startActivity(intent);				
				}
		};*/
		adapter = new ListViewAdapter(this, druglist);
		adapter.count = druglist.size();// �����һҳû��4���Ļ���ô�죿
		if (druglist.size() < pageSize) {
			loadMoreView.setVisibility(View.GONE);
		}
		listView.setVisibility(View.VISIBLE);
		listView.setAdapter(adapter);// �Զ�ΪidΪ��list��listview����������

		listView.setOnScrollListener(new OnScrollListener() {
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
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int posion,
					long arg3) {
				
				//FootView Item ���
				if (arg3 == -1)
					return;
				
				// ExpandableListView ev = (ExpandableListView) parent;
				Map<String, String> map = (Map<String, String>) listView
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
				Intent drugintent = new Intent().setClass(DrugList.this,
						DrugProductDetail.class);// ������һ��activity
				drugintent.putExtras(bundle);
				startActivity(drugintent);
			}
		});
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
	 * ��ҳ���ظ�������
	 */
	private class AsynLoader_more extends AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerdruglist;

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub

			try {
				if (prevPage.equals("drugtype")) {
					innerdruglist = Drug.getListByDrugType(drugTypeID,
							pageSize, pageIndex + 1); // ȡ�õ�ǰҳ���ݺ���Ӵ�druglist��
				} else if (prevPage.equals("disease")) {
					innerdruglist = Drug.getListByDisease(diseaseID, pageSize,
							pageIndex + 1); // ȡ�õ�ǰҳ���ݺ���Ӵ�druglist��
				}
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

	/**
	 * ��һ���첽��������
	 */
	private class AsyncLoader_GuessInfo extends
			AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerdruglist;

		@Override
		protected Integer doInBackground(String... params) {
			try {
				// ��������
				/* if(params[0].length()>0) */
				// model=
				// IntegralDataServiceHelper.GetRank(params[0],ProjectConstant.AppID);
				// list= IntegralDataServiceHelper.GetTopList(0,
				// 10,ProjectConstant.AppID);
				if (prevPage.equals("drugtype")) {
					innerdruglist = Drug.getListByDrugType(drugTypeID,
							pageSize, 1);
				} else if (prevPage.equals("disease")) {
					innerdruglist = Drug.getListByDisease(diseaseID, pageSize,
							1);
				}
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
			loadMoreView.setVisibility(View.VISIBLE);
		}

		@Override
		// �������
		protected void onPostExecute(Integer result) {
			if (result == 0) {
				initAdapter(innerdruglist);
				async_begin.setVisibility(View.GONE);
			} else if (result == 1) {
				async_begin.setVisibility(View.GONE);
				TextView async_error_txt = (TextView) async_error
						.findViewById(R.id.async_error_txt);
				Button async_error_reflesh = (Button) async_error
						.findViewById(R.id.async_error_reflesh);
				async_error_reflesh.setVisibility(View.GONE);
				async_error_txt.setText("û���ҵ���ص�ҩƷ");
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