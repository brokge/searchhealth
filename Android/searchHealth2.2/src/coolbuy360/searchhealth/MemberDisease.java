/**
 * 
 */
package coolbuy360.searchhealth;

import java.util.List;
import java.util.Map;

import coolbuy360.adapter.MemberDiseaseListAdapter;
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
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author yangxc
 * ��Ա����ʷ
 */
public class MemberDisease extends Activity implements OnItemClickListener {
	
	private static final int DIALOG_Delete = 1;
	private static final int Action_Add = 1;
	private static final int Action_Edit = 2;
	
	private int deletePosition = -1;	
	private GestureListView member_disease_liv;	
	private Dialog pBarcheck;

	private int pageIndex = 1;// ���ڵڼ�ҳ����
	private int pageSize = ConstantsSetting.QLDefaultPageSize;// ÿҳ��ʾ������
	private boolean isloading;
	private Button loadMoreButton;
	private ProgressBar proBar;
	private TextView protxt;
	private View loadMoreView;
	private MemberDiseaseListAdapter adapter;

	LinearLayout async_begin;
	LinearLayout async_error;
	
	String diseaseType = "all";
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_disease);
		// Ϊ�˳���׼��
		searchApp.getInstance().addActivity(this);

		// ���÷��ذ�ť
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MemberDisease.this.finish();
			}
		});	
		
		Button actionbar_add_btn = (Button) this
				.findViewById(R.id.actionbar_add_btn);
		actionbar_add_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent().setClass(MemberDisease.this, MemberDiseaseInfo.class);
	    		intent.putExtra("action", "add");
	    		intent.putExtra("diseasetype", diseaseType);
	    		startActivityForResult(intent, Action_Add);
			}
		});
		
		member_disease_liv = (GestureListView) this
				.findViewById(R.id.member_disease_liv);
		
		/*List<Map<String, String>> sourceList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < 10; i++) {
			Map<String, String> item = new HashMap<String, String>();
			item.put("diseasename", "����Ѫ�ܼ���"+i);
			item.put("diagnosetime", "2013/07/20 20:00:00");
			sourceList.add(item);
		}
		
		MemberDiseaseListAdapter adapter = new MemberDiseaseListAdapter(
				MemberDisease.this, sourceList);		
		member_disease_liv.setAdapter(adapter);	
		
		// ���õ�����¼�
		member_disease_liv.setOnItemClickListener(this);
		
		// ����ɾ���¼�
		adapter.setOnDeleteItemListener(new OnDeleteItemListener() {
			
			@Override
			public void onDeleteItem(View v, int position) {
				// TODO Auto-generated method stub
				deletePosition = position;
				showDialog(DIALOG_Delete);
			}
		});*/
		
		loadMoreView = getLayoutInflater().inflate(R.layout.p_druglist_foot,
				null);
		loadMoreButton = (Button) loadMoreView
				.findViewById(R.id.p_listview_footbtn);
		proBar = (ProgressBar) loadMoreView.findViewById(R.id.p_probar);
		protxt = (TextView) loadMoreView.findViewById(R.id.p_protxt);
		member_disease_liv.addFooterView(loadMoreView);
		// *******************************����ļ����¼���������������������������������������������������������������
		async_begin = (LinearLayout) findViewById(R.id.async_begin);
		async_begin.setVisibility(View.VISIBLE);
		async_error = (LinearLayout) findViewById(R.id.async_error);
		member_disease_liv.setVisibility(View.GONE);
		
		Bundle rootBundle = getIntent().getExtras();
		diseaseType = rootBundle.getString("diseasetype");
		
		TextView actionbar_page_title=(TextView)this.findViewById(R.id.actionbar_page_title);
		if(diseaseType.equals("contagious")) {
			actionbar_page_title.setText("��Ⱦ��ʷ");
		} else if (diseaseType.equals("hereditary")) {
			actionbar_page_title.setText("�Ŵ���ʷ");		
		} 
		
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
		char flingState = member_disease_liv.getFlingState();
        Log.v("MY_TAG", "onItemClick: state="+flingState+", pos="+pos);

    	switch(flingState) {
    	// �������¼�
    	case GestureListView.FLING_LEFT: {
    		member_disease_liv.setFlingState(GestureListView.FLING_CLICK);
        	break;
    	}
        // �����һ��¼�
    	case GestureListView.FLING_RIGHT: {
    		member_disease_liv.setFlingState(GestureListView.FLING_CLICK);
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
    		Intent intent = new Intent().setClass(this,	MemberDiseaseInfo.class);
    		intent.putExtra("action", "edit");
    		intent.putExtra("position", pos);
    		intent.putExtra("diseasetype", diseaseType);
    		intent.putExtra("id", item.get("id"));
    		intent.putExtra("diseasename", item.get("diseasename"));
    		intent.putExtra("diagnosetime", item.get("diagnosetime"));
    		intent.putExtra("iscontagious", item.get("iscontagious"));
    		intent.putExtra("ishereditary", item.get("ishereditary"));
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
		adapter = new MemberDiseaseListAdapter(MemberDisease.this, sourceList);
		if (sourceList.size() < pageSize) {
			loadMoreView.setVisibility(View.GONE);
		} else {
		}
		member_disease_liv.setVisibility(View.VISIBLE);
		member_disease_liv.setAdapter(adapter);
		
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
		member_disease_liv.setOnItemClickListener(this);

		member_disease_liv.setOnScrollListener(new OnScrollListener() {
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
					if(diseaseType.equals("contagious")) {
						innerlist = coolbuy360.logic.MemberDisease
								.getContagiousListByMemberID(memberid, pageSize, 1);					
					} else if (diseaseType.equals("hereditary")) {
						innerlist = coolbuy360.logic.MemberDisease
								.getHereditaryListByMemberID(memberid, pageSize, 1);					
					} else {
						innerlist = coolbuy360.logic.MemberDisease
								.getListByMemberID(memberid, pageSize, 1);
					}
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
				async_error_txt.setText("û�м�����¼");
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
					if(diseaseType.equals("contagious")) {
						innerlist = coolbuy360.logic.MemberDisease
								.getContagiousListByMemberID(memberid, pageSize, pageIndex + 1);					
					} else if (diseaseType.equals("hereditary")) {
						innerlist = coolbuy360.logic.MemberDisease
								.getHereditaryListByMemberID(memberid, pageSize, pageIndex + 1);					
					} else {
						innerlist = coolbuy360.logic.MemberDisease
								.getListByMemberID(memberid, pageSize, pageIndex + 1);
					}
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
			pBarcheck = new Dialog(MemberDisease.this, R.style.dialog);			
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
					CommandResult resultmessage = coolbuy360.logic.MemberDisease.Delete(id);
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
				member_disease_liv.deleteSuccess(position);
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
			return new AlertDialog.Builder(MemberDisease.this)
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
				item.put("diseasename", resultBundle.getString("diseasename"));
				item.put("diagnosetime", resultBundle.getString("diagnosetime"));
				item.put("iscontagious", resultBundle.getString("iscontagious"));
				item.put("ishereditary", resultBundle.getString("ishereditary"));
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
