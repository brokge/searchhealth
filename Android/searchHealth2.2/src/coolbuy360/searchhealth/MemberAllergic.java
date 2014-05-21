/**
 * 
 */
package coolbuy360.searchhealth;

import java.util.List;
import java.util.Map;

import coolbuy360.adapter.MemberAllergicListAdapter;
import coolbuy360.adapter.SmartAdapter.OnDeleteItemListener;
import coolbuy360.control.GestureListView;
import coolbuy360.logic.ConstantsSetting;
import coolbuy360.logic.User;
import coolbuy360.service.CommandResult;
import coolbuy360.service.TimestampException;
import coolbuy360.service.searchApp;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

/**
 * ��Ա����ʷ
 * @author yangxc
 *
 */
public class MemberAllergic extends Activity implements OnItemClickListener {

	private static final int DIALOG_Delete = 1;
	private static final int Action_Add = 1;
	private static final int Action_Edit = 2;
	
	private int deletePosition = -1;	
	private GestureListView member_allergic_liv;	
	private Dialog pBarcheck;	

	private int pageIndex = 1;// ���ڵڼ�ҳ����
	private int pageSize = ConstantsSetting.QLDefaultPageSize;// ÿҳ��ʾ������
	private boolean isloading;
	private Button loadMoreButton;
	private ProgressBar proBar;
	private TextView protxt;
	private View loadMoreView;
	private MemberAllergicListAdapter adapter;

	LinearLayout async_begin;
	LinearLayout async_error;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_allergic);
		// Ϊ�˳���׼��
		searchApp.getInstance().addActivity(this);

		// ���÷��ذ�ť
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MemberAllergic.this.finish();
			}
		});	
		
		Button actionbar_add_btn = (Button) this
				.findViewById(R.id.actionbar_add_btn);
		actionbar_add_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent().setClass(MemberAllergic.this, MemberAllergicInfo.class);
	    		intent.putExtra("action", "add");
	    		startActivityForResult(intent, Action_Add);
			}
		});
		
		member_allergic_liv = (GestureListView) this
				.findViewById(R.id.member_allergic_liv);
		
		loadMoreView = getLayoutInflater().inflate(R.layout.p_druglist_foot,
				null);
		loadMoreButton = (Button) loadMoreView
				.findViewById(R.id.p_listview_footbtn);
		proBar = (ProgressBar) loadMoreView.findViewById(R.id.p_probar);
		protxt = (TextView) loadMoreView.findViewById(R.id.p_protxt);
		member_allergic_liv.addFooterView(loadMoreView);
		// *******************************����ļ����¼���������������������������������������������������������������
		async_begin = (LinearLayout) findViewById(R.id.async_begin);
		async_begin.setVisibility(View.VISIBLE);
		async_error = (LinearLayout) findViewById(R.id.async_error);
		member_allergic_liv.setVisibility(View.GONE);
		
		new AsyncLoadList().execute();
	}
	
    /**
     * �¼�����
     * ����flingState��ֵΪ�¼�
     * ����posΪListView��ÿһ��
     */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		// TODO Auto-generated method stub
		char flingState = member_allergic_liv.getFlingState();
        Log.v("MY_TAG", "onItemClick: state="+flingState+", pos="+pos);

    	switch(flingState) {
    	// �������¼�
    	case GestureListView.FLING_LEFT: {
    		member_allergic_liv.setFlingState(GestureListView.FLING_CLICK);
        	break;
    	}
        // �����һ��¼�
    	case GestureListView.FLING_RIGHT: {
    		member_allergic_liv.setFlingState(GestureListView.FLING_CLICK);
        	break;
    	}
        // �������¼�
    	case GestureListView.FLING_CLICK: {
    		switch(pos) {
    		case 0:break;
    		case 1:break;
    		}
			
			//FootView Item ���
			if (arg3 == -1)
				return;
			
			Map<String, String> item = (Map<String, String>)adapter.getItem(pos);			
    		Intent intent = new Intent().setClass(this,	MemberAllergicInfo.class);
    		intent.putExtra("action", "edit");
    		intent.putExtra("position", pos);
    		intent.putExtra("id", item.get("id"));
    		intent.putExtra("occurrencetime", item.get("occurrencetime"));
    		intent.putExtra("allergen", item.get("allergen"));
    		intent.putExtra("symptom", item.get("symptom"));
    		intent.putExtra("remarks", item.get("remarks"));
    		startActivityForResult(intent, Action_Edit);
    		break;
    	}
    	}
	}
	
	/**
	 * ��ʼ��������
	 * 
	 * @param name
	 *            ����idֵ
	 */
	private void initAdapter(List<Map<String, String>> sourceList) {
		// TODO Auto-generated method stub
		adapter = new MemberAllergicListAdapter(MemberAllergic.this, sourceList);
		if (sourceList.size() < pageSize) {
			loadMoreView.setVisibility(View.GONE);
		} else {
		}
		member_allergic_liv.setVisibility(View.VISIBLE);
		member_allergic_liv.setAdapter(adapter);
		
		// ����ɾ���¼�
		adapter.setOnDeleteItemListener(new OnDeleteItemListener() {
			
			@Override
			public void onDeleteItem(View v, int position) {
				// TODO Auto-generated method stub
				deletePosition = position;
				showDialog(DIALOG_Delete);
			}
		});
		
		// ���õ�����¼�
		member_allergic_liv.setOnItemClickListener(this);

		member_allergic_liv.setOnScrollListener(new OnScrollListener() {
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

					}
				}
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
	 * ��һ���첽��������
	 */
	private class AsyncLoadList extends
			AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerlist;

		@Override
		protected Integer doInBackground(String... params) {
			try {
				String memberid = User.getMemberID();
				if (memberid != null) {
					innerlist = coolbuy360.logic.MemberAllergic
							.getListByMemberID(memberid, pageSize, 1);
				} else {
					return -3;
				}
				if (innerlist != null) {
					return (innerlist.size() > 0) ? 0 : 1;
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
			pageIndex = 1;
			isloading = false;
			async_error.setVisibility(View.GONE);
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
				async_error_txt.setText("û�й�����¼");
				async_error.setVisibility(View.VISIBLE);
			} else {
				async_begin.setVisibility(View.GONE);
				async_error.setVisibility(View.VISIBLE);
				TextView async_error_txt = (TextView) async_error
						.findViewById(R.id.async_error_txt);
				if (result == -2) {
					async_error_txt.setText(R.string.error_timestamp);
				} else if (result == -3) {
					async_error_txt.setText(R.string.error_unlogin);
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
						new AsyncLoadList().execute();
					}
				});
			}
		}
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
				String memberid = User.getMemberID();
				if (memberid != null) {
					innerlist = coolbuy360.logic.MemberAllergic
							.getListByMemberID(memberid, pageSize,
									pageIndex + 1); 
				} else {
					return -3;
				}
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
				loadMoreView.setVisibility(View.VISIBLE);
				loadMoreButton.setVisibility(View.VISIBLE);
				loadMoreButton.setBackgroundColor(getResources().getColor(
						R.color.transparent));
				loadMoreButton.setClickable(false);
				loadMoreButton.setText("û�и�������");
				proBar.setVisibility(View.GONE); // ����progressbar
				protxt.setVisibility(View.GONE);
				// isloading = false;
			} else {
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
	 * �첽ɾ����¼
	 */
	private class AsyncDelete extends AsyncTask<Integer, Void, Boolean> {
		int position = -1;
		
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pBarcheck = new Dialog(MemberAllergic.this, R.style.dialog);			
			pBarcheck.setContentView(R.layout.custom_progress);
			pBarcheck.setCancelable(true);
			pBarcheck.show();
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(Integer... params) {
			// TODO Auto-generated method stub

			try {
				if (params.length > 0) {
					position = params[0];
					Map<String, String> item = (Map<String, String>)adapter.getItem(position);
					String id = item.get("id");
					CommandResult resultmessage = coolbuy360.logic.MemberAllergic.Delete(id);
					return resultmessage.getResult();
				} else {
					return false;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if (result)// ���ɾ���ɹ�
			{
				member_allergic_liv.deleteSuccess(position);
				pBarcheck.cancel();
				Toast.makeText(getBaseContext(), "ɾ���ɹ�", Toast.LENGTH_SHORT).show();
			} else {
				pBarcheck.cancel();
				Toast.makeText(getBaseContext(), "ɾ��ʧ��", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case DIALOG_Delete:
			return new AlertDialog.Builder(this)
			.setTitle("��ʾ")
			.setMessage("��ȷ��Ҫɾ����¼��?")
			.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {

					/* User clicked OK so do some stuff */
					new AsyncDelete().execute(deletePosition);
				}
			})
			.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			})
			.create();
		}
		return null;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) { 
			// ����resultCode����ͬ����·��ص�ֵ
			switch (requestCode) {
			case Action_Add:
				// �����ɹ������¼�������ˢ�µ�ǰ�б�ҳ��
				new AsyncLoadList().execute();
				break;

			case Action_Edit:
				// �޸ĳɹ��󣬸����б��е��޸�������ݣ���ˢ����ʾ
				Bundle resultBundle = data.getExtras();
				int position = resultBundle.getInt("position");
				Map<String, String> item = (Map<String, String>)adapter.getItem(position);
				item.put("occurrencetime", resultBundle.getString("occurrencetime"));
				item.put("allergen", resultBundle.getString("allergen"));
				item.put("symptom", resultBundle.getString("symptom"));
				item.put("remarks", resultBundle.getString("remarks"));
				adapter.notifyDataSetChanged();
				break;
				
			default:
				break;
			}
		} else if (resultCode == RESULT_CANCELED) { // �������ء�

		}
	}
}