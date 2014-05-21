/**
 * 
 */
package coolbuy360.searchhealth;

import java.util.List;
import java.util.Map;

import coolbuy360.adapter.ArticleListViewAdapter;
import coolbuy360.adapter.DissertationListViewAdapter;
import coolbuy360.logic.ConstantsSetting;
import coolbuy360.service.ImageManager;
import coolbuy360.service.StrictModeWrapper;
import coolbuy360.service.TimestampException;
import coolbuy360.service.Util;
import coolbuy360.service.searchApp;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author yangxc
 *
 */
public class DissertationDetail extends Activity {
	private ListView listView;
	private int pageIndex = 1;// ���ڵڼ�ҳ����
	private int pageSize = ConstantsSetting.QLDefaultPageSize;// ÿҳ��ʾ������
	private boolean isloading;
	private Button loadMoreButton;
	private ProgressBar proBar;
	private TextView protxt;
	private View loadMoreView;
	private ArticleListViewAdapter adapter;

	LinearLayout async_begin;
	LinearLayout async_error;
	searchApp app;
	
	String dissertationid;
	String dtitle;
	String dresume;
	String imgurl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// StrictModeWrapper.init(this);
		StrictModeWrapper.init(getBaseContext());
		super.onCreate(savedInstanceState);
		
		// Ϊ�˳���׼��
		app=searchApp.getInstance();
		app.addActivity(this);
		setContentView(R.layout.dissertation_detail);

		Bundle bundle = getIntent().getExtras();
		dissertationid = bundle.getString("dissertationid");
		dtitle = bundle.getString("title");
		dresume = bundle.getString("resume");
		imgurl = bundle.getString("image");
		
		listView = (ListView) this.findViewById(R.id.dissertation_detail_liv);
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
				DissertationDetail.this.finish();// 
			}
		});
		
		View dissertation_detail_head = getLayoutInflater().inflate(
				R.layout.dissertation_detail_head, null);
		TextView dissertation_detail_head_title = (TextView) dissertation_detail_head
				.findViewById(R.id.dissertation_detail_head_title);
		dissertation_detail_head_title.setText(dtitle);
		TextView dissertation_detail_head_resume = (TextView) dissertation_detail_head
				.findViewById(R.id.dissertation_detail_head_resume);
		dissertation_detail_head_resume.setText(dresume);

		String imagePath = Util.getNewsImgPath();
		ImageView dissertation_detail_head_image = (ImageView) dissertation_detail_head
				.findViewById(R.id.dissertation_detail_head_image);
		if (imgurl != null && !imgurl.equals("")) {
			if(imgurl.toLowerCase().startsWith("http://")) {
				ImageManager.from(this).displayImage(dissertation_detail_head_image,
						imgurl, R.drawable.promotion_def_pic, 150, 150);
			} else {
			// ����ͼƬ��·��
				ImageManager.from(this).displayImage(dissertation_detail_head_image,
						imagePath + imgurl, R.drawable.promotion_def_pic, 150, 150);
			}

		} else {
			dissertation_detail_head_image.setVisibility(View.GONE);
		}
		int imgwidth = ConMain.getDisplayWidth();
		int imgheight = (240*imgwidth)/640;
		ViewGroup.LayoutParams lps = dissertation_detail_head_image.getLayoutParams();
		lps.height = imgheight;
		dissertation_detail_head_image.setLayoutParams(lps);
		
		listView.addHeaderView(dissertation_detail_head, null, false);
		
		// ������������������������������������������������������ʼ���б�����Դ����������������������������������������������������������������������
		new AsyncLoader_GuessInfo().execute();
	}

	/**
	 * ��ʼ��������
	 * 
	 * @param name
	 *            ����idֵ
	 */
	private void initAdapter(List<Map<String, String>> sourcelist) {
		// TODO Auto-generated method stub
		adapter = new ArticleListViewAdapter(this, sourcelist);
		if (sourcelist.size() < pageSize) {
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
				Map<String, String> map = (Map<String, String>) parent.getAdapter().getItem(posion);
				Intent tintent = new Intent().setClass(DissertationDetail.this,
						ArticleDetail.class);// ������һ��activity
				tintent.putExtra("articleid", map.get("ArticleID"));
				tintent.putExtra("title", map.get("Title"));
				tintent.putExtra("resume", map.get("Resume"));
				tintent.putExtra("createtime", map.get("CreateTime"));
				startActivity(tintent);
			}
		});
	}

	/**
	 * ��ҳ���ظ�������
	 */
	private class AsynLoader_more extends AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerlist;

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub

			try {
				innerlist = coolbuy360.logic.Article.getListOfDissertation(dissertationid, pageSize,
						pageIndex + 1);
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
			isloading = true;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			// StrictModeWrapper.init(getApplicationContext());
			if (result == 0) {
				for (Map<String, String> item : innerlist) {
					adapter.addItem(item);
				}
				if (innerlist.size() < pageSize) {
					loadMoreView.setVisibility(View.GONE);
				} else {
					loadMoreButton.setText("����");
					loadMoreButton.setVisibility(View.VISIBLE);
				}
				proBar.setVisibility(View.GONE);
				protxt.setVisibility(View.GONE);// ����progressbar
				isloading = false;
				adapter.notifyDataSetChanged();
				pageIndex++;
			} else if (result == 1) { // ������ݼ������
				if (innerlist.size() < pageSize) {
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
		List<Map<String, String>> innerlist;

		@Override
		protected Integer doInBackground(String... params) {
			try {
				innerlist = coolbuy360.logic.Article.getListOfDissertation(dissertationid, pageSize, 1);
				if (innerlist != null) {
					return (innerlist.size() > 0 ) ? 0 : 1;
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
				initAdapter(innerlist);
				async_begin.setVisibility(View.GONE);
			} else if (result == 1) {
				async_begin.setVisibility(View.GONE);
				TextView async_error_txt = (TextView) async_error
						.findViewById(R.id.async_error_txt);
				Button async_error_reflesh = (Button) async_error
						.findViewById(R.id.async_error_reflesh);
				async_error_reflesh.setVisibility(View.GONE);
				async_error_txt.setText("��û����ر�����");
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