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
 * @author yangxc
 * 会员疾病史详情
 */
public class MemberDiseaseInfo extends Activity {	
	
	EditText member_disease_info_edt_remarks;
	
	TextView member_disease_info_txv_diagnosetime;
	TextView member_disease_info_txv_diseasename;
	TextView member_disease_info_txv_iscontagious;	
	TextView member_disease_info_txv_ishereditary;
	
	Map<String, String> diseaseInfoMap = new HashMap<String, String>();
	
	String action = "add";
	String diseaseType = "all";
	int position = -1;
	Bundle rootBundle;
	
	private Dialog pBarcheck;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_disease_info);
		// 为退出做准备
		searchApp.getInstance().addActivity(this);

		// 设置返回按钮
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MemberDiseaseInfo.this.finish();
			}
		});
		
		// 获取界面控件		
		// 输入类
		member_disease_info_edt_remarks = (EditText) this
				.findViewById(R.id.member_disease_info_edt_remarks); 
		// 选择类
		member_disease_info_txv_diagnosetime = (TextView) this
				.findViewById(R.id.member_disease_info_txv_diagnosetime);
		member_disease_info_txv_diseasename = (TextView) this
				.findViewById(R.id.member_disease_info_txv_diseasename);
		member_disease_info_txv_iscontagious = (TextView) this
				.findViewById(R.id.member_disease_info_txv_iscontagious);
		member_disease_info_txv_ishereditary = (TextView) this
				.findViewById(R.id.member_disease_info_txv_ishereditary);
				
		// 选择类点击区域
		LinearLayout member_disease_info_item_diagnosetime = (LinearLayout) this
				.findViewById(R.id.member_disease_info_item_diagnosetime);
		LinearLayout member_disease_info_item_diseasename = (LinearLayout) this
				.findViewById(R.id.member_disease_info_item_diseasename);
		LinearLayout member_disease_info_item_iscontagious = (LinearLayout) this
				.findViewById(R.id.member_disease_info_item_iscontagious);
		LinearLayout member_disease_info_item_ishereditary = (LinearLayout) this
				.findViewById(R.id.member_disease_info_item_ishereditary);
		
		Button actionbar_save_btn = (Button) this
				.findViewById(R.id.actionbar_save_btn);
		actionbar_save_btn.setOnClickListener(new onSaveBtnClick());
		
		// 注册点击效果
		member_disease_info_item_diagnosetime.setOnTouchListener(new CommonMethod.setOnPressed());
		member_disease_info_item_diseasename.setOnTouchListener(new CommonMethod.setOnPressed());
		member_disease_info_item_iscontagious.setOnTouchListener(new CommonMethod.setOnPressed());
		member_disease_info_item_ishereditary.setOnTouchListener(new CommonMethod.setOnPressed());
		
		// “选择确诊时间”点击
		member_disease_info_item_diagnosetime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				final DateDialog datedialogtest = new DateDialog(
						MemberDiseaseInfo.this);
				datedialogtest.setCustomerDate(false);
				// datedialogtest.setYear(true, 1911);
				String diagnosetime = diseaseInfoMap.get("diagnosetime");
				if(diagnosetime!=null && !diagnosetime.equals("")) {
					datedialogtest.setCustomerDate(diagnosetime, "yyyy-MM-dd hh:mm:ss");
				}
				datedialogtest.setTitle("请选择确诊时间");
				datedialogtest.show();
				datedialogtest.setOkListener("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String time = datedialogtest.getDateTime("yyyy-MM-dd");
						CommonMethod.setSelectFieldText(
								member_disease_info_txv_diagnosetime, time);
						diseaseInfoMap.put("diagnosetime",
								Util.getDateFormat(time,
										"yyyy-MM-dd hh:mm:ss",
										"yyyy-MM-dd"));
					}
				});
			}
		});
		
		// “选择疾病名称”点击
		member_disease_info_item_diseasename.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				CommonMethod.clearFocuse(MemberDiseaseInfo.this);
				Intent intent = new Intent().setClass(MemberDiseaseInfo.this,
						SHInputSelector.class);
				// 实例化列表数据源的适配器
				NomalSelectorAdapter adapter = 
					new NomalSelectorAdapter(MemberDiseaseInfo.this, 
							CommonMethod.getDiseaseNamesSource());
				// 将适配器托管到临时数据池
				String adapterkey = TempDataPool.putAdapter(adapter);
				// 传递适配器的唯一Key到选择器页面
				intent.putExtra("adapterkey", adapterkey);
				intent.putExtra("title", "选择疾病名称");
				intent.putExtra("fieldname", "疾病名称");
				// 传入字段允许的最大长度
				intent.putExtra("maxlen", 50);
				// 传入当前值
				String nowValue = diseaseInfoMap.get("diseasename");
				if (nowValue != null && !(nowValue.equals(""))) {
					intent.putExtra("nowvalue", nowValue);
				}
				
				// 设置requestCode为对应的字段
				startActivityForResult(intent, Fields.DiseaseName);
			}
		});
		
		// “选择是否传染病”点击
		member_disease_info_item_iscontagious.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				CommonMethod.clearFocuse(MemberDiseaseInfo.this);
				Intent intent = new Intent().setClass(MemberDiseaseInfo.this,
						SHValueSelector.class);
				// 实例化列表数据源的适配器
				NomalSelectorAdapter adapter = 
					new NomalSelectorAdapter(MemberDiseaseInfo.this, 
							CommonMethod.getYesNoSource());
				// 将适配器托管到临时数据池
				String adapterkey = TempDataPool.putAdapter(adapter);
				// 传递适配器的唯一Key到选择器页面
				intent.putExtra("adapterkey", adapterkey);
				intent.putExtra("title", "选择是否传染病");
				
				// 设置requestCode为对应的字段
				startActivityForResult(intent, Fields.IsContagious);
			}
		});
		
		// “选择是否遗传病”点击
		member_disease_info_item_ishereditary.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				CommonMethod.clearFocuse(MemberDiseaseInfo.this);
				Intent intent = new Intent().setClass(MemberDiseaseInfo.this,
						SHValueSelector.class);
				// 实例化列表数据源的适配器
				NomalSelectorAdapter adapter = 
					new NomalSelectorAdapter(MemberDiseaseInfo.this, 
							CommonMethod.getYesNoSource());
				// 将适配器托管到临时数据池
				String adapterkey = TempDataPool.putAdapter(adapter);
				// 传递适配器的唯一Key到选择器页面
				intent.putExtra("adapterkey", adapterkey);
				intent.putExtra("title", "选择是否遗传病");
				
				// 设置requestCode为对应的字段
				startActivityForResult(intent, Fields.IsHereditary);				
			}
		});

		TextView actionbar_page_title=(TextView)this.findViewById(R.id.actionbar_page_title);
		rootBundle = getIntent().getExtras();
		
		diseaseType = rootBundle.getString("diseasetype");
		if(diseaseType.equals("contagious") || diseaseType.equals("hereditary")) {
			member_disease_info_item_iscontagious.setVisibility(View.GONE);
			member_disease_info_item_ishereditary.setVisibility(View.GONE);			
		} 
		
		String titleString = "疾病";
		if(diseaseType.equals("contagious")) {
			titleString = "传染病";
		} else if (diseaseType.equals("hereditary")) {
			titleString = "遗传病";				
		} 
		
		action = rootBundle.getString("action");
		if (action.equals("add")) {
			actionbar_page_title.setText("新增" + titleString + "记录");
			if(diseaseType.equals("contagious")) {
				diseaseInfoMap.put("iscontagious", "1");
				diseaseInfoMap.put("ishereditary", "0");
			} else if (diseaseType.equals("hereditary")) {
				diseaseInfoMap.put("iscontagious", "0");
				diseaseInfoMap.put("ishereditary", "1");
			} else {
				diseaseInfoMap.put("iscontagious", "0");
				diseaseInfoMap.put("ishereditary", "0");
				CommonMethod.setSelectFieldText(member_disease_info_txv_iscontagious, "否");
				CommonMethod.setSelectFieldText(member_disease_info_txv_ishereditary, "否");
			}
		} else {
			actionbar_page_title.setText("修改" + titleString + "记录");
			String diseaseid = rootBundle.getString("id");
			position = rootBundle.getInt("position");
			new AsyncLoadInfo().execute(diseaseid);
		}
	}
	
	/**
	 * 根据上级页面传来的数据获取Info信息
	 * @return
	 */
	private Map<String, String> getDiseaseInfo() {
		Map<String, String> infoMap = new HashMap<String, String>();
		infoMap.put("id", rootBundle.getString("id"));
		infoMap.put("diagnosetime", Util
				.getDateFormat(rootBundle.getString("diagnosetime"),
						"yyyy-MM-dd hh:mm:ss",
						"yyyy/MM/dd hh:mm:ss"));
		infoMap.put("diseasename", rootBundle.getString("diseasename"));
		infoMap.put("iscontagious", rootBundle.getString("iscontagious"));
		infoMap.put("ishereditary", rootBundle.getString("ishereditary"));
		infoMap.put("remarks", rootBundle.getString("remarks"));
		return infoMap;
	}
	
	/**
	 * 根据数据初始化界面值
	 * @param infoMap
	 */
	private void InitViewData(Map<String, String> infoMap) {
		String diseasename = infoMap.get("diseasename");
		String diagnosetime = infoMap.get("diagnosetime");
		String iscontagious = infoMap.get("iscontagious");
		String ishereditary = infoMap.get("ishereditary");
		String remarks = infoMap.get("remarks");
		if(diseasename!=null && !diseasename.equals("")){
			CommonMethod.setSelectFieldText(member_disease_info_txv_diseasename, diseasename);
		}
		if(diagnosetime!=null && !diagnosetime.equals("")){
			CommonMethod.setSelectFieldText(
					member_disease_info_txv_diagnosetime, Util
							.getDateFormat(diagnosetime,
									"yyyy-MM-dd",
									"yyyy-MM-dd hh:mm:ss"));
		}
		if(iscontagious!=null && !iscontagious.equals("")){			
			CommonMethod.setSelectFieldText(
					member_disease_info_txv_iscontagious,
					iscontagious.equals("1") ? "是" : "否");
		}
		if(ishereditary!=null && !ishereditary.equals("")){			
			CommonMethod.setSelectFieldText(
					member_disease_info_txv_ishereditary,
					ishereditary.equals("1") ? "是" : "否");
		}
		if(remarks!=null && !remarks.equals("")){
			member_disease_info_edt_remarks.setText(remarks);
		}
	}
	
	/**
	 * 异步加载数据
	 */
	private class AsyncLoadInfo extends AsyncTask<String, Void, Integer> {
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pBarcheck = new Dialog(MemberDiseaseInfo.this, R.style.dialog);
			pBarcheck.setContentView(R.layout.custom_progress);
			pBarcheck.setCancelable(true);
			pBarcheck.show();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				diseaseInfoMap = getDiseaseInfo();
				if (diseaseInfoMap != null) {
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
				InitViewData(diseaseInfoMap);
				pBarcheck.cancel();
			} else {
				pBarcheck.cancel();
				Toast.makeText(MemberDiseaseInfo.this, "数据加载错误", Toast.LENGTH_LONG).show();
			}
			//super.onPostExecute(result);
		}
	}
	
	// 保存按钮点击事件
	private final class onSaveBtnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			String remarks = member_disease_info_edt_remarks.getText().toString().trim();
			diseaseInfoMap.put("remarks", remarks);			
			new AsyncSave().execute();			
		}
	}
	
	/**
	 * 异步保存记录
	 */
	private class AsyncSave extends AsyncTask<String, Void, CommandResult> {
		
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pBarcheck = new Dialog(MemberDiseaseInfo.this, R.style.dialog);			
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
					result = coolbuy360.logic.MemberDisease
							.insert(diseaseInfoMap);
				} else {
					result = coolbuy360.logic.MemberDisease
							.update(diseaseInfoMap);
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
					resultIntent.putExtra("diagnosetime", Util
							.getDateFormat(diseaseInfoMap.get("diagnosetime"),
									"yyyy/MM/dd hh:mm:ss",
									"yyyy-MM-dd hh:mm:ss"));
					resultIntent.putExtra("diseasename", diseaseInfoMap.get("diseasename"));
					resultIntent.putExtra("iscontagious", diseaseInfoMap.get("iscontagious"));
					resultIntent.putExtra("ishereditary", diseaseInfoMap.get("ishereditary"));
					resultIntent.putExtra("remarks", diseaseInfoMap.get("remarks"));
				}
				MemberDiseaseInfo.this.setResult(RESULT_OK, resultIntent);
				MemberDiseaseInfo.this.finish();				
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
			case Fields.DiseaseName: {
				String resultKey = data.getExtras().getString("resultkey");
				// 通过选择器返回的唯一Key从临时数据池获取对应的数据项
				Object resultObject = TempDataPool.getSelectorResult(resultKey);
				if (resultObject != null) {
					Map<String, String> resultMap = (Map<String, String>) resultObject;
					String resultString = resultMap.get("text");
					CommonMethod.setSelectFieldText(member_disease_info_txv_diseasename, resultString);
					diseaseInfoMap.put("diseasename", resultString);
				}
				// 返回数据项使用完后从临时数据池销毁
				TempDataPool.destroySelectorResult(resultKey);
				break;
			}
			case Fields.DiagnoseTime: {
				break;
			}
			case Fields.IsContagious: {
				String resultKey = data.getExtras().getString("resultkey");
				// 通过选择器返回的唯一Key从临时数据池获取对应的数据项
				Object resultObject = TempDataPool.getSelectorResult(resultKey);
				if (resultObject != null) {
					Map<String, String> resultMap = (Map<String, String>) resultObject;
					String resultString = resultMap.get("text");
					CommonMethod.setSelectFieldText(member_disease_info_txv_iscontagious, resultString);
					diseaseInfoMap.put("iscontagious", resultMap.get("value"));
				}
				// 返回数据项使用完后从临时数据池销毁
				TempDataPool.destroySelectorResult(resultKey);
				break;
			}
			case Fields.IsHereditary: {
				String resultKey = data.getExtras().getString("resultkey");
				// 通过选择器返回的唯一Key从临时数据池获取对应的数据项
				Object resultObject = TempDataPool.getSelectorResult(resultKey);
				if (resultObject != null) {
					Map<String, String> resultMap = (Map<String, String>) resultObject;
					String resultString = resultMap.get("text");
					CommonMethod.setSelectFieldText(member_disease_info_txv_ishereditary, resultString);
					diseaseInfoMap.put("ishereditary", resultMap.get("value"));
				}
				// 返回数据项使用完后从临时数据池销毁
				TempDataPool.destroySelectorResult(resultKey);
				break;
			}
			default:
				break;
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
		public static final int DiagnoseTime = 1;
		public static final int DiseaseName = 2;
		public static final int IsContagious = 3;
		public static final int IsHereditary = 4;
	}
}
