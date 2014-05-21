/**
 * 
 */
package coolbuy360.searchhealth;

import java.util.List;
import java.util.Map;

import com.baidu.android.pushservice.PushConstants;

import coolbuy360.adapter.PushMessageListViewAdapter;
import coolbuy360.logic.ConstantsSetting;
import coolbuy360.logic.NoticeStateConfig;
import coolbuy360.service.JsonUtril;
import coolbuy360.service.StrictModeWrapper;
import coolbuy360.service.TimestampException;
import coolbuy360.service.searchApp;
import android.app.Activity;
import android.content.Intent;
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

/**
 * @author yangxc
 *
 */
public class NoticeCenter extends Activity {
	private ListView listView;
	private int pageIndex = 1;// ���ڵڼ�ҳ����
	private int pageSize = ConstantsSetting.QLDefaultPageSize;// ÿҳ��ʾ������
	private boolean isloading;
	private Button loadMoreButton;
	private ProgressBar proBar;
	private TextView protxt;
	private View loadMoreView;
	private PushMessageListViewAdapter adapter;

	LinearLayout async_begin;
	LinearLayout async_error;
	searchApp app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// StrictModeWrapper.init(this);
		StrictModeWrapper.init(getBaseContext());
		super.onCreate(savedInstanceState);
		
		// Ϊ�˳���׼��
		app=searchApp.getInstance();
		app.addActivity(this);
		setContentView(R.layout.notice_center_list);
		
		listView = (ListView) this.findViewById(R.id.notice_center_list_liv);
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
				NoticeCenter.this.finish();// 
			}
		});
		
		// ������������������������������������������������������ʼ���б�����Դ����������������������������������������������������������������������
		new AsyncLoader_GuessInfo().execute();

		// �����¹��ܡ�New��ͼ��
		String message_IsVisited = NoticeStateConfig.getValue(this,
				NoticeStateConfig.Message_IsVisited);
		if (message_IsVisited.equals("0")) {
			if (ConMain.mConMain != null) {
				ConMain.mConMain.setNewFunction("��Ա", false);
				Member imember = Member.getInstance();
				if(imember!=null){
					imember.setNewFunction(5, false);
				}
				NoticeStateConfig.setValue(this,
						NoticeStateConfig.Message_IsVisited, "1");
			}
		}
	}

	/**
	 * ��ʼ��������
	 * 
	 * @param name
	 *            ����idֵ
	 */
	private void initAdapter(List<Map<String, String>> sourcelist) {
		// TODO Auto-generated method stub
		adapter = new PushMessageListViewAdapter(this, sourcelist);
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
				Map<String, String> map = (Map<String, String>) adapter.getItem(posion);
				String customContentString = map.get("AndroidParameters");
				if (customContentString == null||customContentString.equals("")) {
					// intent:#Intent;launchFlags=0x10000000;component=coolbuy360.searchhealth.About;end
				} 
				else 
				{
					Map<String, Object> cusMap = JsonUtril.getMap(customContentString);	
					//���ݰٶ�api��Ҫ��open_typeΪ2ʱ�Ż���Ч
					if (cusMap.get("open_type").toString().equals("2")) {
						// ��ȡ�Զ��������е�ֵ
						 String intentFlag= cusMap.get("intent_flag").toString();	 
						 if(intentFlag.equals("1"))//�ҳ��
						 {			 
							    Intent aIntent = new Intent();
								//aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								aIntent.setClass(NoticeCenter.this, DrugStorePromotionDetail.class);
								aIntent.putExtra("promotionid",Long.parseLong(cusMap.get("promotion_id").toString()));	
								NoticeCenter.this.startActivity(aIntent);
							 
						 }else  if (intentFlag.equals("2")) //ҩ��intent
						 {					
						 } else  if (intentFlag.equals("3")) //ҩ��intent
						 {					
						 }else if (intentFlag.equals("4")) {
							 //�����Ĭ��ҳ��	
							    Intent aIntent = new Intent();
								//aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								aIntent.setClass(NoticeCenter.this, PushCustomer.class);
								aIntent.putExtra("messageid",cusMap.get("message_id").toString());
								aIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_TITLE, map.get("Title"));
								aIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT, map.get("Content"));	
								aIntent.putExtra("sendtime", map.get("SendTime"));			
								NoticeCenter.this.startActivity(aIntent);
						 }else 
						 {
						 }
					}
					else
					{
					}
				}
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
				innerlist = coolbuy360.logic.PushMessage.getList(getBaseContext(), pageSize,
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
				loadMoreButton.setText("û�и�����Ϣ��");
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
				innerlist = coolbuy360.logic.PushMessage.getList(getBaseContext(), pageSize, 1);
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
				async_error_txt.setText("��û����Ϣ��");
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
