/**
 * 
 */
package coolbuy360.searchhealth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import coolbuy360.adapter.MemberHealthFootprintListAdapter;
import coolbuy360.adapter.SmartAdapter.OnDeleteItemListener;
import coolbuy360.control.CusProgressDialog;
import coolbuy360.control.GestureListView;
import coolbuy360.logic.ConstantsSetting;
import coolbuy360.logic.HealthFootprint;
import coolbuy360.logic.User;
import coolbuy360.service.CommandResult;
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
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 会员健康足迹
 * 
 * @author yangxc
 * 
 */

public class MemberHealthFootprint extends Activity implements
		OnItemClickListener {
	
	private static final int Action_Add = 1;
	private static final int Action_Edit = 2;
	private static final int DIALOG_Delete = 1;
	private int deletePosition = -1;
	// List<Map<String, String>> sourceList = new ArrayList<Map<String,
	// String>>();
	GestureListView member_footprint_liv;
	// private Dialog pBarcheck;
	private CusProgressDialog pBarcheck;

	private int pageIndex = 1;// 属于第几页数据
	private int pageSize = ConstantsSetting.QLDefaultPageSize;// 每页显示的条数
	private boolean isloading;
	private Button loadMoreButton;
	private ProgressBar proBar;
	private TextView protxt;
	private View loadMoreView;
	MemberHealthFootprintListAdapter healthFootprintAdapter;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode == RESULT_OK)
		{
			switch (requestCode) {
			case Action_Add:
				isloading=false;
				pageIndex=1;
				new AsyLoadData().execute();
				break;
	        case Action_Edit:
	        	// 修改成功后，更新列表中的修改项的数据，并刷新显示
				Bundle resultBundle = data.getExtras();
			
				int position = resultBundle.getInt("position");
				Log.i("chenlinwei", position+"");
				Map<String, String> item = (Map<String, String>)healthFootprintAdapter.getItem(position);
				item.put("eventtime", resultBundle.getString("eventtime"));
				item.put("summarize", resultBundle.getString("summarize"));			
				item.put("remarks", resultBundle.getString("remarks"));
				healthFootprintAdapter.notifyDataSetChanged();
				break;
	
			default:
				break;
			   }
		}
		super.onActivityResult(requestCode, resultCode, data);		
	
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i("chenlinwei", "健康足迹进来了");
		setContentView(R.layout.member_health_footprint);
		Button savaBtn = (Button) findViewById(R.id.actionbar_add_btn);
		// 设置返回按钮
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		member_footprint_liv = (GestureListView) this
				.findViewById(R.id.member_footprint_liv);

		// ××××××××××××××××××准备初始化foot_loading控件×××××××××××××××××××××
		loadMoreView = getLayoutInflater().inflate(R.layout.p_druglist_foot,
				null);
		loadMoreButton = (Button) loadMoreView
				.findViewById(R.id.p_listview_footbtn);
		proBar = (ProgressBar) loadMoreView.findViewById(R.id.p_probar);
		protxt = (TextView) loadMoreView.findViewById(R.id.p_protxt);
		member_footprint_liv.addFooterView(loadMoreView); // 设置列表底部视图

		// 返回按钮的操作事件
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MemberHealthFootprint.this.finish();
			}
		});
		// 保存按钮的操作事件
		savaBtn.setOnClickListener(new addOnClick());

		new AsyLoadData().execute();
	}

	/**
	 * 初始化适配器
	 */
	private void InitAdapter(List<Map<String, String>> sourceList) {
		healthFootprintAdapter = new MemberHealthFootprintListAdapter(this,
				sourceList);
		member_footprint_liv.setAdapter(healthFootprintAdapter);
		member_footprint_liv.setOnItemClickListener(this);
		healthFootprintAdapter
				.setOnDeleteItemListener(new OnDeleteItemListener() {
					@Override
					public void onDeleteItem(View v, int position) {
						// TODO Auto-generated method stub
						deletePosition = position;
						showDialog(DIALOG_Delete);
					}
				});
		// 设置滚动时的监听事件
		member_footprint_liv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

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
					}
				}

			}
		});

	}

	/**
	 * 翻页加载更多数据
	 */
	private class AsynLoader_more extends AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerdruglist;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			isloading = true;
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				innerdruglist = HealthFootprint.getHealthFootprintInstance()
						.getHFootprintList(User.getMemberID(), pageSize,
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
			if (result == 0) {
				for (Map<String, String> item : innerdruglist) {
					healthFootprintAdapter.addItem(item);
				}
				if (innerdruglist.size() < pageSize) {
					loadMoreView.setVisibility(View.GONE);
				} else {
					loadMoreButton.setText("更多");
					loadMoreButton.setVisibility(View.VISIBLE);
				}
				proBar.setVisibility(View.GONE);
				protxt.setVisibility(View.GONE);// 隐藏progressbar
				isloading = false;
				//healthFootprintAdapter.count += innerdruglist.size();
				healthFootprintAdapter.notifyDataSetChanged();
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

	/**
	 * 添加按钮的操作事件
	 * 
	 * @author Administrator
	 * 
	 */
	private final class addOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent().setClass(MemberHealthFootprint.this,
					MemberHFootprintInfo.class);
			Bundle bundle = new Bundle();
			bundle.putString("action", "add");
			intent.putExtras(bundle);
			// MemberHealthFootprint.this.startActivity(intent);
			MemberHealthFootprint.this.startActivityForResult(intent, Action_Add);
		}
	}

	/**
	 * 异步加载数据并，并初始化listview
	 * 
	 * @author Administrator
	 * 
	 */
	public class AsyLoadData extends AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerdruglist;

		@Override
		protected void onPreExecute() {
			pBarcheck = new CusProgressDialog(MemberHealthFootprint.this,
					R.style.dialog);
			pBarcheck.setPregressBar(true);
			pBarcheck.setMessage("正在加载中");
			pBarcheck.setCanceledOnTouchOutside(false);
			pBarcheck.show();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				innerdruglist = HealthFootprint.getHealthFootprintInstance()
						.getHFootprintList(User.getMemberID(), pageSize,
								pageIndex);
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
			if (result == 0) {
				InitAdapter(innerdruglist);
				pBarcheck.cancel();
			} 
			if(result==1)
			{
				pBarcheck.setProgressBarHide();
				//显示
				pBarcheck.SetRefreshBtnShow();			
				//pBarcheck.setMessage("没有加载到数据，请刷新");
				pBarcheck.SetMessagetxtShow("没有健康数据记录\n点击刷新");
				pBarcheck.setCanceledOnTouchOutside(true);
				pBarcheck.setReFreshListener("", new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// 再次加载
						pBarcheck.cancel();
						new AsyLoadData().execute();
					}
				});
			}
			else {				
				pBarcheck.setProgressBarHide();
				//显示
				pBarcheck.SetRefreshBtnShow();
				pBarcheck.SetMessagetxtShow("加载出错\n点击重试");
				pBarcheck.setCanceledOnTouchOutside(true);
				pBarcheck.setReFreshListener("", new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// 再次加载
						pBarcheck.cancel();
						new AsyLoadData().execute();
					}
				});
			}
			super.onPostExecute(result);
		}
	}

	/**
	 * 异步删除记录
	 */
	private class AsyncDelete extends AsyncTask<Integer, Void, Boolean> {
		int position = -1;
		CommandResult resultmessage;
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			/*
			 * pBarcheck = new Dialog(MemberHealthFootprint.this,
			 * R.style.dialog);
			 * pBarcheck.setContentView(R.layout.custom_progress);
			 * pBarcheck.setCancelable(true); pBarcheck.show();
			 */
			pBarcheck = new CusProgressDialog(MemberHealthFootprint.this,
					R.style.dialog);
			pBarcheck.setPregressBar(true);
			pBarcheck.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Integer... params) {
			// TODO Auto-generated method stub

			try {
				if (params.length > 0) {
					/*
					 * CommandResult resultmessage = Drug.unCollect(
					 * getBaseContext(), params[0]); return
					 * resultmessage.getResult();
					 */
					position = params[0];
					Map<String, String> item = (Map<String, String>)healthFootprintAdapter.getItem(position);
					String id = item.get("id");
				    resultmessage =HealthFootprint.getHealthFootprintInstance().Delete(id);
					return resultmessage.getResult();					
					// 数据库删除的相关操作
					
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
			if (result)// 如果登录成功
			{
				member_footprint_liv.deleteSuccess(position);
				pBarcheck.cancel();
				Toast.makeText(getBaseContext(), "删除成功", Toast.LENGTH_SHORT)
						.show();
			} else {				
				Toast.makeText(getBaseContext(),resultmessage.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 事件处理 其中flingState的值为事件 参数pos为ListView的每一项
	 */
	/*
	 * private class setOnItemClick implements OnItemClickListener {
	 * 
	 * @Override public void onItemClick(AdapterView<?> parent, View view, int
	 * position, long id) { // TODO Auto-generated method stub
	 * 
	 * } }
	 */

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		// TODO Auto-generated method stub
		char flingState = member_footprint_liv.getFlingState();
		Log.v("MY_TAG", "onItemClick: state=" + flingState + ", pos=" + pos);

		switch (flingState) {
		// 处理左滑事件
		case GestureListView.FLING_LEFT: {
			// Toast.makeText( this, "Fling Left:"+pos,
			// Toast.LENGTH_SHORT).show();
			/*Dialog dialog = new Dialog(this);
			dialog.setTitle("左滑" + pos);
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();*/
			member_footprint_liv.setFlingState(GestureListView.FLING_CLICK);
			break;
		}
			// 处理右滑事件
		case GestureListView.FLING_RIGHT: {
			// Toast.makeText( this, "Fling Right:"+pos,
			// Toast.LENGTH_SHORT).show();
			/*Dialog dialog1 = new Dialog(this);
			dialog1.setTitle("右滑" + pos);
			dialog1.setCanceledOnTouchOutside(true);
			dialog1.show();*/
			member_footprint_liv.setFlingState(GestureListView.FLING_CLICK);
			break;
		}
			// 处理点击事件
		case GestureListView.FLING_CLICK: {
			switch (pos) {
			case 0:
				break;
			case 1:
				break;
			}
			
			//FootView Item 点击
			if (arg3 == -1)
				return;
			
			// Toast.makeText( this, "Click Item:"+pos,
			Map<String, String> item = (Map<String, String>)healthFootprintAdapter.getItem(pos);			
    		Intent intent = new Intent().setClass(this,	MemberHFootprintInfo.class);
    		intent.putExtra("action", "edit");
    		intent.putExtra("id", item.get("id"));
    		intent.putExtra("eventtime", item.get("eventtime"));    		
    		intent.putExtra("summarize", item.get("summarize"));
    		intent.putExtra("remarks", item.get("remarks"));
    		intent.putExtra("position", pos);
    		startActivityForResult(intent, Action_Edit);
			break;
		}
	 }
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case DIALOG_Delete:
			return new AlertDialog.Builder(MemberHealthFootprint.this)
					.setTitle("提示")
					.setMessage("您确定要删除记录吗?")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked OK so do some stuff */
									new AsyncDelete().execute(deletePosition);
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							}).create();
		}
		return null;
	}

}
