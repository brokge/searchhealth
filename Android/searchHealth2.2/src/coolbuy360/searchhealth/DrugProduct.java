package coolbuy360.searchhealth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import coolbuy360.adapter.ExpandableAdapter;
import coolbuy360.logic.DrugType;
import coolbuy360.logic.User;

import coolbuy360.service.StrictModeWrapper;
import coolbuy360.service.TimestampException;
import coolbuy360.service.searchApp;
import coolbuy360.zxing.CaptureActivity;

public class DrugProduct extends Activity {

	private ExpandableListView elv;

	private List<Map<String, String>> child;
	int expandFlag = -1;
	ExpandableAdapter viewAdapter;
	List<Map<String, String>> groups = null;
	List<List<Map<String, String>>> childs = null;
	DrugType.DrugTypes dt = null;
	private int posion;
	LinearLayout async_begin;
	LinearLayout async_error;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		StrictModeWrapper.init(this); // 调用严苛模式

		super.onCreate(savedInstanceState);
		// 为退出做准备
		searchApp.getInstance().addActivity(this);
		setContentView(R.layout.drugproduct);
		// 通过ID查找到main.xml中的ExpandableListView控件
		elv = (ExpandableListView) findViewById(R.id.p_expandablleListView);
		// 设置箭头左右
		int width = ConMain.getDisplayWidth();
		elv.setIndicatorBounds(width - 50, width - 20);

		/*ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DrugProduct.this.finish();
				overridePendingTransition(R.anim.push_no,R.anim.push_top_out);
			}
		});*/
		
		ImageButton searchar_searchbtn = (ImageButton) this
				.findViewById(R.id.searchbar_searchbtn);
		final EditText searchbar_inputtext = (EditText) this
				.findViewById(R.id.searchbar_inputtext);
		ImageButton searchbar_scanbtn = (ImageButton) this
				.findViewById(R.id.searchbar_scanbtn);
		searchbar_scanbtn.setOnClickListener(new scanOnclick());
		// final
		searchar_searchbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String keyword = searchbar_inputtext.getText().toString().trim();
				if (!keyword.equals("")) {
					Bundle bundle = new Bundle();
					Intent drugsearchIntent = new Intent().setClass(
							DrugProduct.this, DrugSearch.class);
					bundle.putString("keyword", keyword);

					drugsearchIntent.putExtras(bundle);
					startActivity(drugsearchIntent);
				} else {
					Toast.makeText(DrugProduct.this, "请输入查询关键字", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		// elv.setGroupIndicator(this.getResources().getDrawable(R.layout.expandablelistviewselector));//设置替换图标
		// elv.seti
		// elv.setOnGroupClickListener(onGroupClickListener);//设置Group点击时加载子类
		// ×××××××××××××××××××××××××××初始化数据×××××××××××××××××××××××××××××××

		// ***************************载入的加载事件×××××××××××××××××××××××××××××××
		async_begin = (LinearLayout) findViewById(R.id.async_begin);
		async_begin.setVisibility(View.VISIBLE);
		elv.setVisibility(View.GONE);
		new AsyncLoader_GuessInfo().execute();

		// 进入测试页
		/*ImageView gotestimg = (ImageView) this.findViewById(R.id.p_imgTitle);
		gotestimg.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Intent testpageIntent = new Intent().setClass(DrugProduct.this,
						TestPage.class);
				startActivity(testpageIntent);
				return false;
			}
		});*/		
	}

	/**
	 * 扫描点击事件
	 * 
	 * @author Administrator
	 * 
	 */
	private final class scanOnclick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent openCameraIntent = new Intent(DrugProduct.this,
					CaptureActivity.class);
			startActivityForResult(openCameraIntent, 0);
		}

	}

	/*@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// String tags=v.getTag().toString();
		// Map<String, String> cdata
		// =viewAdapter.groups.get(Integer.parseInt(tags));

		// Object aa=v.getTag(1);
		// drugBudde drug=(drugBudde)v.getTag(1);
		// String tags=v.getTag(); //获得button上面的值
		Intent childIntent = new Intent().setClass(DrugProduct.this,
				DrugList.class);
		String drugtype_name = v.getTag(R.id.drugtype_name).toString();
		String drugtype_id = v.getTag(R.id.drugtype_id).toString();

		Bundle bundle = new Bundle();
		bundle.putString("type_id", drugtype_id);// 设置需要传递的值
		bundle.putString("drug_title", drugtype_name);
		bundle.putString("from", "drugtype");
		// bundle.putString("type_name", )
		childIntent.putExtras(bundle);
		startActivityForResult(childIntent, 0);
	}*/

	/**
	 * 初始化适配器
	 * 
	 */
	private void initadapter() {
		/*
		 * if (groups == null | groups.size() <= 0) { Toast.makeText(this,
		 * "请确认网络是否连接", 1).show(); }
		 */// else {
		viewAdapter = new ExpandableAdapter(this, groups, childs);
		elv.setAdapter(viewAdapter);

		/**
		 * 父节点点击时触发的事件
		 */
		elv.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					final int groupPosition, long id) {
				// ExpandableListView ev = (ExpandableListView) parent;
				// Map<String, String> glist = (Map<String, String>)
				// ev.getItemAtPosition(groupPosition);
				// View childview =ev.getChildAt(groupPosition);
				Map<String, String> glist = groups.get(groupPosition);
				List<Map<String, String>> clist = childs.get(groupPosition);
				// childview.
				int childcount = clist.size();
				if (childcount > 0) {
					return false;
				} else {
					String gname = glist.get("drugtypename");
					String gid = glist.get("drugtypeid");
					posion = groupPosition;
					// Toast.makeText(DrugProduct.this, childcount+"",
					// 1).show();

					Intent listIntent = new Intent().setClass(DrugProduct.this,
							DrugList.class);
					Bundle bundle = new Bundle();
					bundle.putString("type_id", gid);// 设置需要传递的值
					bundle.putString("drug_title", gname);
					bundle.putString("from", "drugtype");
					// bundle.putString("type_name", )
					listIntent.putExtras(bundle);
					// startActivityForResult(listIntent, 0);
					startActivity(listIntent);
					return true;
				}

			}
		});

		/**
		 * 子节点点击的时候触发的事件
		 */
		elv.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// ExpandableListView ev = (ExpandableListView) parent;
				// ev.getChildAt(groupPosition;

				// Toast.makeText(getBaseContext(),
				// String.valueOf(childPosition)+"" , 1).show();

				Intent childIntent = new Intent().setClass(DrugProduct.this,
						DrugList.class);
				/*
				 * String drugtype_name =
				 * v.getTag(R.id.drugtype_name).toString(); String drugtype_id =
				 * v.getTag(R.id.drugtype_id).toString();
				 */
				// ListView listView=(ListView)v;
				// Toast.makeText(DrugProduct.this, childPosition+"", 1).show();

				// ev.getChildAt(groupPosition)
				// Map<String, String> childMap=childMaplist.get(0);
				/*
				 * String drugtype_name =
				 * v.getTag(R.id.drugtype_name).toString(); String drugtype_id =
				 * v.getTag(R.id.drugtype_id).toString();
				 */

				String drugtype_name = v.getTag(R.id.drugtype_name).toString();
				String drugtype_id = v.getTag(R.id.drugtype_id).toString();

				// Toast.makeText(DrugProduct.this, drugtype_id, 1).show();
				Bundle bundle = new Bundle();
				bundle.putString("type_id", drugtype_id);// 设置需要传递的值
				bundle.putString("drug_title", drugtype_name);
				bundle.putString("from", "drugtype");
				// bundle.putString("type_name", )
				childIntent.putExtras(bundle);
				// startActivityForResult(childIntent, 0);
				startActivity(childIntent);
				return true;
			}
		});
	}

	// }

	private void loaddata() {
		DrugType.DrugTypes dt = DrugType.getAllDrugTypes();
		groups = new ArrayList<Map<String, String>>();
		childs = new ArrayList<List<Map<String, String>>>();
		childs = dt.ChildTypes;
		groups = dt.RootTypes;

		// TODO Auto-generated catch block
		// e.printStackTrace();
		// Toast.makeText(this, "请确认网络是否连接", 1).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");

			Intent drugdetailIntent = new Intent().setClass(DrugProduct.this,
					DrugProductDetail.class);
			Bundle bundle1 = new Bundle();
			bundle1.putString("serisecode", scanResult);
			drugdetailIntent.putExtras(bundle1);
			startActivity(drugdetailIntent);

			// Toast.makeText(this, scanResult, 1).show();
			// resultTextView.setText(scanResult);

			/*
			 * Intent intent = new
			 * Intent(BarCodeActivity.this,WebViewActivity.class); Intent intent
			 * = new Intent(BarCodeActivity.this,MainActivity.class);
			 * intent.putExtra("url", scanResult); startActivity(intent);
			 */
		}
	}

	/**
	 * 异步获取药品分类数据
	 */
	private class AsyncLoader_GuessInfo extends
			AsyncTask<String, Void, Integer> {
		@Override
		protected Integer doInBackground(String... params) {
			int result = 0;
			try {
				// 加载数据
				loaddata();
				
				//应用启动获取设备用户ID，新设备从服务端注册一个新的ID
				try {
					User.getDevAppUserID(DrugProduct.this.getBaseContext());
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				if (groups != null)
					result = 2;
				// Toast.makeText(DrugStore.this, "2", 1).show();
			} catch (TimestampException ex) {
				result = -2;
			} catch (Exception ex) {
				result = -1;
			}
			return result;
		}

		@Override
		// 处理界面
		protected void onPostExecute(Integer result) {
			Log.i("ExerciseGuess", "onPostExecute(Result result) called");

			if (result == 2) {
				initadapter();
				elv.setVisibility(View.VISIBLE);
				async_begin.setVisibility(View.GONE);
			} else {
				// Toast.makeText(DrugStore.this, "-1", 1).show();
				async_begin.setVisibility(View.GONE);
				async_error = (LinearLayout) findViewById(R.id.async_error);
				async_error.setVisibility(View.VISIBLE);
				TextView async_error_txt = (TextView) async_error
						.findViewById(R.id.async_error_txt);
				if (result == -2) {
					async_error_txt.setText(R.string.error_timestamp);
				} else {
					async_error_txt.setText(R.string.error_nonetwork);
				}				
				Button reload_btn = (Button) async_error
						.findViewById(R.id.async_error_reflesh);
				reload_btn.setOnClickListener(new OnClickListener() {

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
	/**
	 * 模拟Home键返回系统，不结束应用进程
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			Intent intent = new Intent(Intent.ACTION_MAIN);  
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意  
            intent.addCategory(Intent.CATEGORY_HOME);  
            this.startActivity(intent);  
            return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/*private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				// Log.i(TAG, "退出");
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}*/

}