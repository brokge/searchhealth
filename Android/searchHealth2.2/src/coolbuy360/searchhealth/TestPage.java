/**
 * 
 */
package coolbuy360.searchhealth;

import java.util.List;
import java.util.Map;

import com.weibo.sdk.android.api.WeiboAPI.USER_CATEGORY;

import coolbuy360.logic.Drug;
import coolbuy360.logic.DrugFavorite;
import coolbuy360.logic.DrugStoreFavorite;
import coolbuy360.logic.User;
import coolbuy360.logic.UserInfo;
import coolbuy360.service.searchApp;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author yangxc
 * 
 */
public class TestPage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testpage);
		// 为退出做准备
		searchApp.getInstance().addActivity(this);

		User.initialization(getBaseContext());

		final EditText test_txt_1 = (EditText) this
				.findViewById(R.id.test_txt_1);
		final EditText test_etxt_id = (EditText) this
				.findViewById(R.id.test_etxt_id);
		final EditText test_etxt_pwd = (EditText) this
				.findViewById(R.id.test_etxt_pwd);
		Button test_btn_login = (Button) this.findViewById(R.id.test_btn_login);
		Button test_btn_logout = (Button) this.findViewById(R.id.test_btn_logout);
		Button test_btn_devuid = (Button) this.findViewById(R.id.test_btn_devuid);
		Button test_btn_memuid = (Button) this.findViewById(R.id.test_btn_memuid);
		Button test_btn_isdrugfav = (Button) this.findViewById(R.id.test_btn_isdrugfav);
		Button test_btn_drugfav = (Button) this.findViewById(R.id.test_btn_drugfav);
		Button test_btn_drugunfav = (Button) this.findViewById(R.id.test_btn_drugunfav);
		Button test_btn_getdrugfavs = (Button) this.findViewById(R.id.test_btn_getdrugfavs);
		Button test_btn_getdrugstorefav = (Button) this.findViewById(R.id.test_btn_getdrugstorefav);
		Button test_btn_getdrugstorefavsnoloc = (Button) this.findViewById(R.id.test_btn_getdrugstorefavsnoloc);
		Button test_btn_legalnotice = (Button) this.findViewById(R.id.test_btn_legalnotice);
		Button test_btn_listmap = (Button) this.findViewById(R.id.test_btn_listmap);
		Button test_btn_pointmap = (Button) this.findViewById(R.id.test_btn_pointmap);
		Button test_btn_userinfo=(Button) this.findViewById(R.id.test_btn_userinfo);
		test_btn_userinfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			List<Map<String, String>> userinfoList=	UserInfo.getUserInfoInstance().getUserInfo(4);
			test_txt_1.setText(userinfoList.get(0).toString());
			
			//test_txt_1.setText(userinfoList.size()+"");	
				
				
			}
		});
		test_btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				User testUser = new User(getBaseContext());
				Boolean result = testUser.login(test_etxt_id
						.getText().toString(), test_etxt_pwd.getText()
						.toString(), true, true);
				String textshow = test_txt_1.getText() + "\n" + "LoginTest:" + result.toString();
				textshow += "\n" + User._profiles.toString();
				textshow += "\n" + User.getMemberName(getBaseContext());
				test_txt_1.setText(textshow);		
				test_txt_1.setSelection(textshow.length());	
			}
		});
		test_btn_logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				User testUser = new User(getBaseContext());
				Boolean result = testUser.logout();
				String textshow = test_txt_1.getText() + "\n" + "LogoutTest:" + result.toString();
				test_txt_1.setText(textshow);
				test_txt_1.setSelection(textshow.length());
			}
		});
		test_btn_devuid.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				String textshow = test_txt_1.getText() + "\n" + User.getDevAppUserID(getBaseContext());
				test_txt_1.setText(textshow);
				test_txt_1.setSelection(textshow.length());
			}
		});
		test_btn_memuid.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub		
				String textshow = test_txt_1.getText() + "\n" + User.getMemberAppUserID();
				test_txt_1.setText(textshow);			
				test_txt_1.setSelection(textshow.length());	
			}
		});
		test_btn_isdrugfav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub		
				String textshow = test_txt_1.getText() + "\n" + Drug.isCollected(getBaseContext(), test_etxt_id
						.getText().toString());
				test_txt_1.setText(textshow);		
				test_txt_1.setSelection(textshow.length());		
			}
		});
		test_btn_drugfav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub		
				String textshow = test_txt_1.getText() + "\n" + Drug.doCollect(getBaseContext(), test_etxt_id
						.getText().toString().toString());
				test_txt_1.setText(textshow);		
				test_txt_1.setSelection(textshow.length());		
			}
		});
		test_btn_drugunfav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub		
				String textshow = test_txt_1.getText() + "\n" + Drug.unCollect(getBaseContext(), test_etxt_id
						.getText().toString().toString());
				test_txt_1.setText(textshow);		
				test_txt_1.setSelection(textshow.length());
			}
		});
		test_btn_getdrugfavs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub		
				String textshow = test_txt_1.getText() + "\n" + DrugFavorite.getList(getBaseContext(), 5, 1);
				test_txt_1.setText(textshow);		
				test_txt_1.setSelection(textshow.length());
			}
		});
		test_btn_getdrugstorefav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub		
				String textshow = test_txt_1.getText() + "\n" + DrugStoreFavorite.getList(getBaseContext(), 30.280506,120.107582, 5, 1);
				test_txt_1.setText(textshow);		
				test_txt_1.setSelection(textshow.length());
			}
		});
		test_btn_getdrugstorefavsnoloc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub		
				String textshow = test_txt_1.getText() + "\n" + DrugStoreFavorite.getListNoLocation(getBaseContext(), 5, 1);
				test_txt_1.setText(textshow);		
				test_txt_1.setSelection(textshow.length());
			}
		});
		test_btn_legalnotice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub		
				Intent testpageIntent = new Intent().setClass(
						TestPage.this, LegalNotice.class);
				startActivity(testpageIntent);
			}
		});
		test_btn_listmap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub		
				Intent testpageIntent = new Intent().setClass(
						TestPage.this, DrugStoreMap.class);
				startActivity(testpageIntent);
			}
		});
		test_btn_pointmap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putString("drugstoreid", "14");
				bundle.putString("loadmode", "position");
				Intent storedetailIntent = new Intent().setClass(TestPage.this,
						DrugStoreMap.class);
				storedetailIntent.putExtras(bundle);
				TestPage.this.startActivity(storedetailIntent);
			}
		});

	}
	
	
	
	
	

	class AsyncLoader_GuessInfo extends AsyncTask<String, Void, Integer> {
		@Override
		protected Integer doInBackground(String... params) {
			int result = 0;
			try {

				/*
				 * druglist = Drug.getListByDrugType(cid, 8, 1);
				 * if(druglist!=null)
				 */
				result = 2;
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
				/*
				 * listView.setVisibility(View.VISIBLE);
				 * async_begin.setVisibility(View.GONE); initAdapter(cid);
				 */
			} else {
				/*
				 * async_begin.setVisibility(View.GONE); LinearLayout
				 * async_error=(LinearLayout)findViewById(R.id.async_error);
				 * async_error.setVisibility(View.VISIBLE);
				 */

			}
		}
	}
}
