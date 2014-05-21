package coolbuy360.searchhealth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baidu.android.pushservice.PushConstants;

import coolbuy360.control.CusProgressDialog;
import coolbuy360.logic.PushUser;
import coolbuy360.service.Util;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PushCustomer extends Activity {
	private CusProgressDialog cusProgressDialog;
	TextView pushContent;
	TextView pushTitle;
	TextView pushTime;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String messageidString = bundle.getString("messageid");
		String title = bundle.getString(PushConstants.EXTRA_NOTIFICATION_TITLE);
		String descriString = bundle
				.getString(PushConstants.EXTRA_NOTIFICATION_CONTENT);
		String sendtime = bundle.getString("sendtime");
		// Bundle bundle= intent.getExtras();
		setContentView(R.layout.push_customer_activity);
		pushContent = (TextView) this.findViewById(R.id.push_Content);
		pushTitle = (TextView) this.findViewById(R.id.push_Title);
		pushTime = (TextView) this.findViewById(R.id.push_time);
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new PreOnClick());

		pushTitle.setText(title);
		pushContent.setText(descriString);
		if (sendtime == null || sendtime.equals("")) {
			pushTime.setText(Util.getNowDate("MM-dd HH:mm"));
		} else {
			pushTime.setText(Util.getDateFormat(sendtime, "MM-dd HH:mm"));
		}

		if (!messageidString.equals("") && messageidString != null
				&& !messageidString.equals("0")) {
			new asyloadData().execute(messageidString);
		}
	}

	public class PreOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			PushCustomer.this.finish();
		}
	}

	/**
	 * 初始化控件数据
	 * 
	 * @param ListMaps
	 */
	private void InitViewData(List<Map<String, String>> ListMaps) {
		Map<String, String> maps = ListMaps.get(0);
		String titleString = maps.get("messagestitle").toString();
		String timeString = maps.get("sendtime").toString();
		String contentString = maps.get("messagescontent").toString();
		pushTitle.setText(titleString);
		pushTime.setText(Util.getDateFormat(timeString, "MM-dd HH:mm"));
		pushContent.setText(contentString);
	}

	/**
	 * 异步加载网页数据
	 * 
	 * @author Administrator
	 * 
	 */
	private class asyloadData extends AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerList = new ArrayList<Map<String, String>>();

		@Override
		protected void onPreExecute() {
			cusProgressDialog = new CusProgressDialog(PushCustomer.this,
					R.style.dialog);
			cusProgressDialog.setPregressBar(true);
			cusProgressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			int returnValue = 2;
			if (params.length > 0) {
				try {
					String id = params[0];
					// Log.i("chenlinwei", "促销id：" + id);
					innerList = PushUser.getMessageInfo(params[0]);
					if (innerList != null && innerList.size() > 0) {
						returnValue = 0;
					} else {
						returnValue = 1;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					returnValue = 2;
				}
			}
			return returnValue;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if (result == 0) {
				// InitViewData(innerList);
				// android.util.Log.i(tag, msg)
				Log.i("chenlinwei", innerList + "");
				InitViewData(innerList);
				cusProgressDialog.cancel();
			} else if (result == 1) {
				cusProgressDialog.cancel();
				Toast.makeText(PushCustomer.this, "暂时无信息的详细内容",
						Toast.LENGTH_LONG).show();
			}
			else {
				cusProgressDialog.cancel();
				Toast.makeText(PushCustomer.this, "数据加载错误", Toast.LENGTH_LONG)
						.show();
			}
			super.onPostExecute(result);
		}
	}
}
