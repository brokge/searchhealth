/**
 * 
 */
package coolbuy360.searchhealth;

import java.util.HashMap;
import java.util.Map;

import coolbuy360.adapter.NomalSelectorAdapter;
import coolbuy360.dateview.DateDialog;
import coolbuy360.service.CommandResult;
import coolbuy360.service.CommonMethod;
import coolbuy360.service.TempDataPool;
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
 * 会员过敏记录详情
 * 
 * @author yangxc
 * 
 */
public class MemberAllergicInfo extends Activity {

	EditText member_allergic_info_edt_symptom;
	EditText member_allergic_info_edt_remarks;

	TextView member_allergic_info_txv_occurrencetime;
	TextView member_allergic_info_txv_allergen;

	Map<String, String> allergicInfoMap = new HashMap<String, String>();
	
	String action = "add";
	int position = -1;
	Bundle rootBundle;
	
	private Dialog pBarcheck;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_allergic_info);
		// 为退出做准备
		searchApp.getInstance().addActivity(this);

		// 设置返回按钮
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MemberAllergicInfo.this.finish();
			}
		});
		
		// 获取界面控件		
		// 输入类
		member_allergic_info_edt_symptom = (EditText) this
				.findViewById(R.id.member_allergic_info_edt_symptom);
		member_allergic_info_edt_remarks = (EditText) this
				.findViewById(R.id.member_allergic_info_edt_remarks);
		// 选择类
		member_allergic_info_txv_occurrencetime = (TextView) this
				.findViewById(R.id.member_allergic_info_txv_occurrencetime);
		member_allergic_info_txv_allergen = (TextView) this
				.findViewById(R.id.member_allergic_info_txv_allergen);
				
		// 选择类点击区域
		LinearLayout member_allergic_info_item_occurrencetime = (LinearLayout) this
				.findViewById(R.id.member_allergic_info_item_occurrencetime);
		LinearLayout member_allergic_info_item_allergen = (LinearLayout) this
				.findViewById(R.id.member_allergic_info_item_allergen);
		
		Button actionbar_save_btn = (Button) this
				.findViewById(R.id.actionbar_save_btn);
		actionbar_save_btn.setOnClickListener(new onSaveBtnClick());
		
		// 注册点击效果
		member_allergic_info_item_occurrencetime.setOnTouchListener(new CommonMethod.setOnPressed());
		member_allergic_info_item_allergen.setOnTouchListener(new CommonMethod.setOnPressed());
		
		// “选择发生时间”点击
		member_allergic_info_item_occurrencetime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				final DateDialog datedialogtest = new DateDialog(
						MemberAllergicInfo.this);
				datedialogtest.setCustomerDate(false);
				// datedialogtest.setYear(true, 1911);
				String occurrencetime = allergicInfoMap.get("occurrencetime");
				if(occurrencetime!=null && !occurrencetime.equals("")) {
					datedialogtest.setCustomerDate(occurrencetime, "yyyy-MM-dd hh:mm:ss");
				}
				datedialogtest.setTitle("请选择发生时间");
				datedialogtest.show();
				datedialogtest.setOkListener("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String time = datedialogtest.getDateTime("yyyy-MM-dd");
						CommonMethod.setSelectFieldText(
								member_allergic_info_txv_occurrencetime, time);
						allergicInfoMap.put("occurrencetime",
								Util.getDateFormat(time,
										"yyyy-MM-dd hh:mm:ss",
										"yyyy-MM-dd"));
					}
				});
			}
		});
		
		// “选择过敏源”点击
		member_allergic_info_item_allergen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				CommonMethod.clearFocuse(MemberAllergicInfo.this);
				Intent intent = new Intent().setClass(MemberAllergicInfo.this,
						SHInputSelector.class);
				// 实例化列表数据源的适配器
				NomalSelectorAdapter adapter = 
					new NomalSelectorAdapter(MemberAllergicInfo.this, 
							CommonMethod.getAllergenSource());
				// 将适配器托管到临时数据池
				String adapterkey = TempDataPool.putAdapter(adapter);
				// 传递适配器的唯一Key到选择器页面
				intent.putExtra("adapterkey", adapterkey);
				intent.putExtra("title", "选择过敏源");
				intent.putExtra("fieldname", "过敏源名称");
				// 传入字段允许的最大长度
				intent.putExtra("maxlen", 50);
				// 传入当前值
				String nowValue = allergicInfoMap.get("allergen");
				if (nowValue != null && !(nowValue.equals(""))) {
					intent.putExtra("nowvalue", nowValue);
				}
				
				// 设置requestCode为对应的字段
				startActivityForResult(intent, Fields.Allergen);
			}
		});		

		TextView actionbar_page_title=(TextView)this.findViewById(R.id.actionbar_page_title);
		rootBundle = getIntent().getExtras();
		action = rootBundle.getString("action");
		if (action.equals("add")) {
			actionbar_page_title.setText("新增过敏记录");
		} else {
			actionbar_page_title.setText("修改过敏记录");
			String allergicid = rootBundle.getString("id");
			position = rootBundle.getInt("position");
			new AsyncLoadInfo().execute(allergicid);
		}
	}
	
	/**
	 * 根据上级页面传来的数据获取Info信息
	 * @return
	 */
	private Map<String, String> getAllergicInfo() {
		Map<String, String> infoMap = new HashMap<String, String>();
		infoMap.put("id", rootBundle.getString("id"));
		infoMap.put("occurrencetime", Util
				.getDateFormat(rootBundle.getString("occurrencetime"),
						"yyyy-MM-dd hh:mm:ss",
						"yyyy/MM/dd hh:mm:ss"));
		infoMap.put("allergen", rootBundle.getString("allergen"));
		infoMap.put("symptom", rootBundle.getString("symptom"));
		infoMap.put("remarks", rootBundle.getString("remarks"));
		return infoMap;
	}
	
	/**
	 * 根据数据初始化界面值
	 * @param infoMap
	 */
	private void InitViewData(Map<String, String> infoMap) {
		String allergen = infoMap.get("allergen");
		String occurrencetime = infoMap.get("occurrencetime");
		String symptom = infoMap.get("symptom");
		String remarks = infoMap.get("remarks");
		if(allergen!=null && !allergen.equals("")){
			CommonMethod.setSelectFieldText(member_allergic_info_txv_allergen, allergen);
		}
		if(occurrencetime!=null && !occurrencetime.equals("")){
			CommonMethod.setSelectFieldText(
					member_allergic_info_txv_occurrencetime, Util
							.getDateFormat(occurrencetime,
									"yyyy-MM-dd",
									"yyyy-MM-dd hh:mm:ss"));
		}
		if(symptom!=null && !symptom.equals("")){
			member_allergic_info_edt_symptom.setText(symptom);
		}
		if(remarks!=null && !remarks.equals("")){
			member_allergic_info_edt_remarks.setText(remarks);
		}
	}
	
	/**
	 * 异步加载数据
	 */
	private class AsyncLoadInfo extends AsyncTask<String, Void, Integer> {
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pBarcheck = new Dialog(MemberAllergicInfo.this, R.style.dialog);
			pBarcheck.setContentView(R.layout.custom_progress);
			pBarcheck.setCancelable(true);
			pBarcheck.show();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				allergicInfoMap = getAllergicInfo();
				if (allergicInfoMap != null) {
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
				InitViewData(allergicInfoMap);
				pBarcheck.cancel();
			} else {
				pBarcheck.cancel();
				Toast.makeText(MemberAllergicInfo.this, "数据加载错误", Toast.LENGTH_LONG).show();
			}
			//super.onPostExecute(result);
		}
	}
	
	// 保存按钮点击事件
	private final class onSaveBtnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			String symptom = member_allergic_info_edt_symptom.getText().toString().trim();
			String remarks = member_allergic_info_edt_remarks.getText().toString().trim();
			allergicInfoMap.put("symptom", symptom);
			allergicInfoMap.put("remarks", remarks);
			new AsyncSave().execute();
		}
	}
	
	/**
	 * 异步保存记录
	 */
	private class AsyncSave extends AsyncTask<String, Void, CommandResult> {
		
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pBarcheck = new Dialog(MemberAllergicInfo.this, R.style.dialog);			
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
					result = coolbuy360.logic.MemberAllergic
							.insert(allergicInfoMap);
				} else {
					result = coolbuy360.logic.MemberAllergic
							.update(allergicInfoMap);
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
					resultIntent.putExtra("occurrencetime", Util
							.getDateFormat(allergicInfoMap.get("occurrencetime"),
									"yyyy/MM/dd hh:mm:ss",
									"yyyy-MM-dd hh:mm:ss"));
					resultIntent.putExtra("allergen", allergicInfoMap.get("allergen"));
					resultIntent.putExtra("symptom", allergicInfoMap.get("symptom"));
					resultIntent.putExtra("remarks", allergicInfoMap.get("remarks"));
				}
				MemberAllergicInfo.this.setResult(RESULT_OK, resultIntent);
				MemberAllergicInfo.this.finish();				
			} else {
				pBarcheck.cancel();
				Toast.makeText(getBaseContext(), result.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) { 
			// 根据resultCode处理不同情况下返回的值
			switch (requestCode) {
			case Fields.Allergen: {
				String resultKey = data.getExtras().getString("resultkey");
				// 通过选择器返回的唯一Key从临时数据池获取对应的数据项
				Object resultObject = TempDataPool.getSelectorResult(resultKey);
				if (resultObject != null) {
					Map<String, String> resultMap = (Map<String, String>) resultObject;
					String resultString = resultMap.get("text");
					CommonMethod.setSelectFieldText(member_allergic_info_txv_allergen, resultString);
					allergicInfoMap.put("allergen", resultString);
				}
				// 返回数据项使用完后从临时数据池销毁
				TempDataPool.destroySelectorResult(resultKey);
				break;
			}
			case Fields.OccurrenceTime: {
				break;
			}
			default: {
				break;
			}
			}
		} else if (resultCode == RESULT_CANCELED) { // 物理“返回”

		}
	}
	
	/**
	 * 定义当前页面中选择类型的字段标识
	 * @author yangxc
	 *
	 */
	private class Fields {
		public static final int OccurrenceTime = 1;
		public static final int Allergen = 2;
	}
}
