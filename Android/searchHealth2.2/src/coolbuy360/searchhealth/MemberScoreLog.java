/**
 * 
 */
package coolbuy360.searchhealth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import coolbuy360.adapter.MemberScoreLogListAdapter;
import coolbuy360.logic.ConstantsSetting;
import coolbuy360.logic.User;
import coolbuy360.pulltorefresh.PullToRefreshBase;
import coolbuy360.pulltorefresh.PullToRefreshListView;
import coolbuy360.pulltorefresh.PullToRefreshBase.Mode;
import coolbuy360.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import coolbuy360.service.TimestampException;
import coolbuy360.service.searchApp;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ��Ա����ֵ��־
 * @author yangxc
 *
 */
public class MemberScoreLog extends Activity {

	public static final int LOADSUCCESS=0;
	public static final int LOADERROR=1;
	public static final int NETWORKTIMEOUT=-2;
	public static final int NETWORKNONE=2;
	private PullToRefreshListView refreshListView;		
	
	private MemberScoreLogListAdapter adapter;
	private int pagesize = ConstantsSetting.QLDefaultPageSize;

	//LinearLayout async_begin;
	LinearLayout async_error;
	
	View listview_empty_error;
	View LoadingView;

	Boolean isCompleted = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Ϊ�˳���׼��
		searchApp.getInstance().addActivity(this);
		setContentView(R.layout.member_scorelog);
		
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MemberScoreLog.this.finish();
				overridePendingTransition(R.anim.push_no,R.anim.push_top_out);
			}
		});

		// ========================����ing�Ŀؼ�===============================/
		//async_begin = (LinearLayout) findViewById(R.id.async_begin);
		//async_begin.setVisibility(View.VISIBLE);
		async_error = (LinearLayout) findViewById(R.id.async_error);	
		
		LayoutInflater layoutInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
		listview_empty_error= layoutInflater.inflate(R.layout.listview_emptyview_error, null);
				
		adapter = new MemberScoreLogListAdapter(this, new ArrayList<Map<String,String>>());
		refreshListView = (PullToRefreshListView) findViewById(R.id.member_scorelog_ptflistview);
		
		LoadingView = layoutInflater.inflate(R.layout.listview_emptyview_loading, null);
		refreshListView.setEmptyView(LoadingView);
		initPullToRefreshListView(refreshListView);		

		// ===========================�״μ��ص�����===========================/
		new AsyncLoader_Refresh().execute();// ��һ�μ�������	
	}
	
	/**
	 * ��ʼ��PullToRefreshListView
	 * 
	 * @param rtflv
	 */
	public void initPullToRefreshListView(PullToRefreshListView rtflv) {
		rtflv.setMode(Mode.BOTH);
		rtflv.setOnRefreshListener(new MyOnRefreshListener(rtflv));
		
		if (adapter != null) {
			rtflv.setAdapter(adapter);
		}
		
		rtflv.getRefreshableView().setSelector(R.color.transparent);
		/*rtflv.getRefreshableView().setDivider(null);
		rtflv.getRefreshableView().setBackgroundColor(
				getResources().getColor(R.color.drugstore_bg_gray));
		int pixof5dp = DisplayUtil.dip2px(this, 5);
		rtflv.getRefreshableView().setPadding(0, pixof5dp, 0, pixof5dp);*/
	}
	
	class MyOnRefreshListener implements OnRefreshListener2<ListView> {

		public MyOnRefreshListener(PullToRefreshListView ptflv) {
			
		}

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			// ����ˢ��
			refreshView.getLoadingLayoutProxy().setReleaseLabel("�ſ�ˢ������");
			String label = DateUtils.formatDateTime(MemberScoreLog.this,
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
			String createtime = "2099-01-01 11:56:43.157";
			Map<String, String> bottomMap;
			int count = adapter.getCount();
			try {
				if (count > 0) {
					bottomMap = (Map<String, String>) adapter.getItem(count - 1);
					createtime = bottomMap.get("CreateTime");
				}
			} catch (Exception e) {
				// TODO �Զ����ɵ� catch ��
			}
			new AsynLoader_More().execute(createtime);
		}
	}
	
	/**
	 * ˢ������
	 */
	private class AsyncLoader_Refresh extends
			AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerlist = null;
		
		@Override
		protected Integer doInBackground(String... params) {
			try {
				innerlist = coolbuy360.logic.Score.getLogs(User.getMemberID(),
						pagesize, "");
				if (innerlist != null) {
					return (innerlist.size() > 0) ? LOADSUCCESS : LOADERROR;
				} else {
					return NETWORKNONE;// �������Ӵ���
				}
			} catch (TimestampException ex) {
				return NETWORKTIMEOUT;
			} 
			catch (Exception e) {
				return NETWORKNONE;
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		// �������
		protected void onPostExecute(Integer result) {
			TextView async_error_txt = (TextView) async_error
					.findViewById(R.id.async_error_txt);
			Button async_error_refleshbtn = (Button) async_error
					.findViewById(R.id.async_error_reflesh);
			switch (result) {
			case LOADSUCCESS:
				adapter.clear();
				adapter.addItems(innerlist);
				adapter.notifyDataSetChanged();
				refreshListView.getRefreshableView().requestFocusFromTouch();
				refreshListView.getRefreshableView().setSelection(0);			
				//async_begin.setVisibility(View.GONE);
				async_error.setVisibility(View.GONE);
				LoadingView.setVisibility(View.GONE);
				break;
			case LOADERROR:
				//async_begin.setVisibility(View.GONE);				
				async_error_txt.setText("��û�н���ֵ��¼��");				
				async_error_refleshbtn.setVisibility(View.GONE);
				async_error.setVisibility(View.VISIBLE);
				refreshListView.setEmptyView(listview_empty_error);					
				break;				
			case NETWORKNONE:
				async_error.setVisibility(View.VISIBLE);
				async_error_txt.setText(R.string.error_nonetwork);			
				async_error_refleshbtn.setVisibility(View.VISIBLE);
				async_error_refleshbtn.setOnClickListener(new refreshOnclick());
				ListViewEmptyError(R.string.error_nonetwork,new refreshOnclick());
				break;
			case NETWORKTIMEOUT:
				async_error.setVisibility(View.VISIBLE);
				async_error_txt.setText(R.string.error_timestamp);	
				async_error_refleshbtn.setVisibility(View.VISIBLE);
				async_error_refleshbtn.setOnClickListener(new refreshOnclick());
				ListViewEmptyError(R.string.error_timestamp,new refreshOnclick());
				break;
			default:
				break;
			}			
			refreshListView.onRefreshComplete();
		}
	}	
	
	public void ListViewEmptyError(int message,OnClickListener onclick) {
		 LoadingView.setVisibility(View.GONE);
		 refreshListView.setEmptyView(listview_empty_error);
		 TextView txtMessageView=(TextView)listview_empty_error.findViewById(R.id.listview_empty_message);
		 txtMessageView.setText(message);		 
		 Button btnRefreshViewButton=(Button)listview_empty_error.findViewById(R.id.listview_empty_refresh);
		 btnRefreshViewButton.setOnClickListener(onclick);
	}
	
	public class refreshOnclick implements OnClickListener
	{		
		@Override
		public void onClick(View v) {
			// TODO �Զ����ɵķ������
			async_error.setVisibility(View.GONE);
			listview_empty_error.setVisibility(View.GONE);
			LoadingView.setVisibility(View.VISIBLE);			
			//async_begin.setVisibility(View.VISIBLE);
			new AsyncLoader_Refresh().execute();
		}
	}
	
	/**
	 * �첽���ظ���
	 */
	private class AsynLoader_More extends AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerlist = null;
		
		@Override
		protected Integer doInBackground(String... params) {
			try {
				String starttime = "2099-01-01 11:56:43.157";
				if (params.length > 0) {
					starttime = params[0];
				}
				innerlist = coolbuy360.logic.Score.getLogs(User.getMemberID(),
						pagesize, starttime);
				if (innerlist != null) {
					return (innerlist.size() > 0) ? 0 : 1;
				} else {
					return 2;// �������Ӵ���
				}
			} catch (Exception e) {
				return 2;
			}
		}

		@Override                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			switch (result) {
			case 0:
				adapter.addItems(innerlist);
				adapter.notifyDataSetChanged();			
				async_error.setVisibility(View.GONE);
				break;
			case 1:
				Toast.makeText(MemberScoreLog.this, "û�и�������", Toast.LENGTH_SHORT)
						.show();
				break;
			case 2:
				Toast.makeText(MemberScoreLog.this, "�������ݳ���", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
			refreshListView.onRefreshComplete();
		}
	}
	
}
