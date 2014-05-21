/**
 * 
 */
package coolbuy360.searchhealth;

import java.util.HashMap;
import java.util.Map;

import coolbuy360.dateview.DateDialog;
import coolbuy360.service.CommandResult;
import coolbuy360.service.CommonMethod;
import coolbuy360.service.Util;
import coolbuy360.service.searchApp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 会员手术记录详情
 * @author yangxc
 *
 */
public class MemberOperationInfo extends Activity {

	EditText member_operation_info_edt_operationname;
	EditText member_operation_info_edt_remarks;

	TextView member_operation_info_txv_implementtime;

	Map<String, String> operationInfoMap = new HashMap<String, String>();
	
	String action = "add";
	int position = -1;
	Bundle rootBundle;
	
	private Dialog pBarcheck;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_operation_info);
		// 为退出做准备
		searchApp.getInstance().addActivity(this);

		// 设置返回按钮
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MemberOperationInfo.this.finish();
			}
		});
		
		// 获取界面控件		
		// 输入类
		member_operation_info_edt_operationname = (EditText) this
				.findViewById(R.id.member_operation_info_edt_operationname);
		member_operation_info_edt_remarks = (EditText) this
				.findViewById(R.id.member_operation_info_edt_remarks);
		// 选择类
		member_operation_info_txv_implementtime = (TextView) this
				.findViewById(R.id.member_operation_info_txv_implementtime);
				
		// 选择类点击区域
		LinearLayout member_operation_info_item_implementtime = (LinearLayout) this
				.findViewById(R.id.member_operation_info_item_implementtime);
		
		Button actionbar_save_btn = (Button) this
				.findViewById(R.id.actionbar_save_btn);
		actionbar_save_btn.setOnClickListener(new onSaveBtnClick());
		
		// 注册点击效果
		member_operation_info_item_implementtime.setOnTouchListener(new CommonMethod.setOnPressed());
		
		// “选择发生时间”点击
		member_operation_info_item_implementtime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				final DateDialog datedialogtest = new DateDialog(
						MemberOperationInfo.this);
				datedialogtest.setCustomerDate(false);
				// datedialogtest.setYear(true, 1911);
				String implementtime = operationInfoMap.get("implementtime");
				if(implementtime!=null && !implementtime.equals("")) {
					datedialogtest.setCustomerDate(implementtime, "yyyy-MM-dd hh:mm:ss");
				}
				datedialogtest.setTitle("请选择手术实施时间");
				datedialogtest.show();
				datedialogtest.setOkListener("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String time = datedialogtest.getDateTime("yyyy-MM-dd");
						CommonMethod.setSelectFieldText(
								member_operation_info_txv_implementtime, time);
						operationInfoMap.put("implementtime",
								Util.getDateFormat(time,
										"yyyy-MM-dd hh:mm:ss",
										"yyyy-MM-dd"));
					}
				});
			}
		});

		TextView actionbar_page_title=(TextView)this.findViewById(R.id.actionbar_page_title);
		rootBundle = getIntent().getExtras();
		action = rootBundle.getString("action");
		if (action.equals("add")) {
			actionbar_page_title.setText("新增手术记录");
		} else {
			actionbar_page_title.setText("修改手术记录");
			String operationid = rootBundle.getString("id");
			position = rootBundle.getInt("position");
			new AsyncLoadInfo().execute(operationid);
		}
	}
	
	/**
	 * 根据上级页面传来的数据获取Info信息
	 * @return
	 */
	private Map<String, String> getOperationInfo() {
		Map<String, String> infoMap = new HashMap<String, String>();
		infoMap.put("id", rootBundle.getString("id"));
		infoMap.put("implementtime", Util
				.getDateFormat(rootBundle.getString("implementtime"),
						"yyyy-MM-dd hh:mm:ss",
						"yyyy/MM/dd hh:mm:ss"));
		infoMap.put("operationname", rootBundle.getString("operationname"));
		infoMap.put("remarks", rootBundle.getString("remarks"));
		return infoMap;
	}
	
	/**
	 * 根据数据初始化界面值
	 * @param infoMap
	 */
	private void InitViewData(Map<String, String> infoMap) {
		String operationname = infoMap.get("operationname");
		String implementtime = infoMap.get("implementtime");
		String remarks = infoMap.get("remarks");
		if(operationname!=null && !operationname.equals("")){
			member_operation_info_edt_operationname.setText(operationname);
		}
		if(implementtime!=null && !implementtime.equals("")){
			CommonMethod.setSelectFieldText(
					member_operation_info_txv_implementtime, Util
							.getDateFormat(implementtime,
									"yyyy-MM-dd",
									"yyyy-MM-dd hh:mm:ss"));
		}
		if(remarks!=null && !remarks.equals("")){
			member_operation_info_edt_remarks.setText(remarks);
		}
	}
	
	/**
	 * 异步加载数据
	 */
	private class AsyncLoadInfo extends AsyncTask<String, Void, Integer> {
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pBarcheck = new Dialog(MemberOperationInfo.this, R.style.dialog);
			pBarcheck.setContentView(R.layout.custom_progress);
			pBarcheck.setCancelable(true);
			pBarcheck.show();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				operationInfoMap = getOperationInfo();
				if (operationInfoMap != null) {
						return 0;
				} else {
					return 2;
				}
			} catch (Exception e) {
				return 2;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if (result == 0) {
				InitViewData(operationInfoMap);
				pBarcheck.cancel();
			} else {
				pBarcheck.cancel();
				Toast.makeText(MemberOperationInfo.this, "数据加载错误", Toast.LENGTH_LONG).show();
			}
			//super.onPostExecute(result);
		}
	}
	
	// 保存按钮点击事件
	private final class onSaveBtnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			String operationname = member_operation_info_edt_operationname.getText().toString().trim();
			String remarks = member_operation_info_edt_remarks.getText().toString().trim();
			operationInfoMap.put("operationname", operationname);
			operationInfoMap.put("remarks", remarks);
			new AsyncSave().execute();
		}
	}
	
	/**
	 * 异步保存记录
	 */
	private class AsyncSave extends AsyncTask<String, Void, CommandResult> {
		
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pBarcheck = new Dialog(MemberOperationInfo.this, R.style.dialog);			
			pBarcheck.setContentView(R.layout.custom_progress);
			pBarcheck.setCancelable(true);
			pBarcheck.show();
			super.onPreExecute();
		}
		
		@Override
		protected CommandResult doInBackground(String... params) {
			// TODO Auto-generated method stub
			CommandResult result = new CommandResult(false, "未知错误，保存失败。");
			try {
				if (action.equals("add")) {
					result = coolbuy360.logic.MemberOperation
							.insert(operationInfoMap);
				} else {
					result = coolbuy360.logic.MemberOperation
							.update(operationInfoMap);
				}
				return result;				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return result;
			}
		}

		@Override
		protected void onPostExecute(CommandResult result) {
			// TODO Auto-generated method stub
			if (result.getResult())// 如果保存成功
			{
				pBarcheck.cancel();								
				Toast.makeText(getBaseContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
				
				Intent resultIntent = new Intent();
				if (action.equals("add")) {
				} else {
					resultIntent.putExtra("position", position);
					resultIntent.putExtra("implementtime", Util
							.getDateFormat(operationInfoMap.get("implementtime"),
									"yyyy/MM/dd hh:mm:ss",
									"yyyy-MM-dd hh:mm:ss"));
					resultIntent.putExtra("operationname", operationInfoMap.get("operationname"));
					resultIntent.putExtra("remarks", operationInfoMap.get("remarks"));
				}
				MemberOperationInfo.this.setResult(RESULT_OK, resultIntent);
				MemberOperationInfo.this.finish();				
			} else {
				pBarcheck.cancel();
				Toast.makeText(getBaseContext(), result.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
	}

}
