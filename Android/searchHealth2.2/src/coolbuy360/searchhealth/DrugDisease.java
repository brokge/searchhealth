package coolbuy360.searchhealth;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import coolbuy360.adapter.DiseaseListViewAdapter;
import coolbuy360.control.AutoFitTextView;
import coolbuy360.logic.Disease;
import coolbuy360.logic.DrugStore;
import coolbuy360.service.searchApp;

public class DrugDisease extends Activity {

	private List<Map<String, String>> diseaselist = null;
	LinearLayout async_begin;
	Button drugdisease_btntitle_refresh;
	//Button drugdisease_btn_drug;
	AutoFitTextView key1;
	AutoFitTextView key2;
	AutoFitTextView key3;
	AutoFitTextView key4;
	AutoFitTextView key5;
	AutoFitTextView key6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drugdisease);
		// 为退出做准备
		searchApp.getInstance().addActivity(this);

		ImageButton searchbar_searchbtn = (ImageButton) this
				.findViewById(R.id.searchbar_searchbtn);
		final EditText searchbar_inputtext = (EditText) this
				.findViewById(R.id.searchbar_inputtext);

		// 搜索按钮的点击事件
		searchbar_searchbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String keywordString = searchbar_inputtext.getText().toString()
						.trim();
				if (!keywordString.equals("")) {
					Intent searchIntent = new Intent().setClass(
							DrugDisease.this, DiseaseSearch.class);
					Bundle bundle = new Bundle();
					bundle.putString("disekeyword", keywordString);
					searchIntent.putExtras(bundle);
					startActivity(searchIntent);
				} else {
					Toast.makeText(DrugDisease.this, "请输入查询关键字", Toast.LENGTH_SHORT).show();
				}

			}
		});

		async_begin = (LinearLayout) findViewById(R.id.async_begin);
		key1 = (AutoFitTextView) findViewById(R.id.disease_common_key1);
		key2 = (AutoFitTextView) findViewById(R.id.disease_common_key2);
		key3 = (AutoFitTextView) findViewById(R.id.disease_common_key3);
		key4 = (AutoFitTextView) findViewById(R.id.disease_common_key4);
		key5 = (AutoFitTextView) findViewById(R.id.disease_common_key5);
		key6 = (AutoFitTextView) findViewById(R.id.disease_common_key6);

		key1.getPaint().setFakeBoldText(true);
		key2.getPaint().setFakeBoldText(true);
		key3.getPaint().setFakeBoldText(true);
		key4.getPaint().setFakeBoldText(true);
		
		key1.setText("");
		key2.setText("");
		key3.setText("");
		key4.setText("");
		key5.setText("");
		key6.setText("");
		
		drugdisease_btntitle_refresh = (Button) this
				.findViewById(R.id.drugdisease_btntitle_refresh);
		drugdisease_btntitle_refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AsyncLoad().execute();
			}
		});

	/*	drugdisease_btn_drug = (Button) this
				.findViewById(R.id.drugdisease_btn_drug);
		drugdisease_btn_drug.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent searchIntent = new Intent().setClass(DrugDisease.this,
						DrugProduct.class);
				startActivity(searchIntent);
				getParent().overridePendingTransition(R.anim.push_top_in,R.anim.push_no);
			}
		});
*/
		new AsyncLoad().execute();
	}

	// 覆盖
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// ConMain.mConMain
		// .getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.title);

	}

	/**
	 * 异步加载常见疾病信息
	 */
	private class AsyncLoad extends AsyncTask<String, Void, Integer> {
		private List<Map<String, String>> innerlist = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			async_begin.setVisibility(View.VISIBLE);
			drugdisease_btntitle_refresh.setVisibility(View.GONE);
		}

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub

			try {
				innerlist = Disease.getCommonDiseases();
				if (innerlist != null) {
					return (innerlist.size() > 0) ? 0 : 1;
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
			if (result == 0)//
			{
				key1.setText(innerlist.get(0).get("commondiseasename"));
				key2.setText(innerlist.get(1).get("commondiseasename"));
				key3.setText(innerlist.get(2).get("commondiseasename"));
				key4.setText(innerlist.get(3).get("commondiseasename"));
				key5.setText(innerlist.get(4).get("commondiseasename"));
				key6.setText(innerlist.get(5).get("commondiseasename"));
				
				key1.setOnClickListener(new OnCommanKeyClick());
				key2.setOnClickListener(new OnCommanKeyClick());
				key3.setOnClickListener(new OnCommanKeyClick());
				key4.setOnClickListener(new OnCommanKeyClick());
				key5.setOnClickListener(new OnCommanKeyClick());
				key6.setOnClickListener(new OnCommanKeyClick());

				async_begin.setVisibility(View.GONE);
			} else if (result == 1) {
				async_begin.setVisibility(View.GONE);
				drugdisease_btntitle_refresh.setVisibility(View.VISIBLE);
			} else if (result == 2) {
				async_begin.setVisibility(View.GONE);
				drugdisease_btntitle_refresh.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 点击常见疾病关键词查询对应的疾病
	 */
	public final class OnCommanKeyClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			String keywordString = ((AutoFitTextView)v).getText().toString().trim();
			if (keywordString!=null && !(keywordString.equals(""))) {
				Intent searchIntent = new Intent().setClass(DrugDisease.this,
						DiseaseSearch.class);
				Bundle bundle = new Bundle();
				bundle.putString("disekeyword", keywordString);
				searchIntent.putExtras(bundle);
				startActivity(searchIntent);
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
