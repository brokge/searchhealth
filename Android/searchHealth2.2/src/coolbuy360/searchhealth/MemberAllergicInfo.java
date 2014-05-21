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
 * ��Ա������¼����
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
		// Ϊ�˳���׼��
		searchApp.getInstance().addActivity(this);

		// ���÷��ذ�ť
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MemberAllergicInfo.this.finish();
			}
		});
		
		// ��ȡ����ؼ�		
		// ������
		member_allergic_info_edt_symptom = (EditText) this
				.findViewById(R.id.member_allergic_info_edt_symptom);
		member_allergic_info_edt_remarks = (EditText) this
				.findViewById(R.id.member_allergic_info_edt_remarks);
		// ѡ����
		member_allergic_info_txv_occurrencetime = (TextView) this
				.findViewById(R.id.member_allergic_info_txv_occurrencetime);
		member_allergic_info_txv_allergen = (TextView) this
				.findViewById(R.id.member_allergic_info_txv_allergen);
				
		// ѡ����������
		LinearLayout member_allergic_info_item_occurrencetime = (LinearLayout) this
				.findViewById(R.id.member_allergic_info_item_occurrencetime);
		LinearLayout member_allergic_info_item_allergen = (LinearLayout) this
				.findViewById(R.id.member_allergic_info_item_allergen);
		
		Button actionbar_save_btn = (Button) this
				.findViewById(R.id.actionbar_save_btn);
		actionbar_save_btn.setOnClickListener(new onSaveBtnClick());
		
		// ע����Ч��
		member_allergic_info_item_occurrencetime.setOnTouchListener(new CommonMethod.setOnPressed());
		member_allergic_info_item_allergen.setOnTouchListener(new CommonMethod.setOnPressed());
		
		// ��ѡ����ʱ�䡱���
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
				datedialogtest.setTitle("��ѡ����ʱ��");
				datedialogtest.show();
				datedialogtest.setOkListener("ȷ��", new OnClickListener() {
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
		
		// ��ѡ�����Դ�����
		member_allergic_info_item_allergen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				CommonMethod.clearFocuse(MemberAllergicInfo.this);
				Intent intent = new Intent().setClass(MemberAllergicInfo.this,
						SHInputSelector.class);
				// ʵ�����б�����Դ��������
				NomalSelectorAdapter adapter = 
					new NomalSelectorAdapter(MemberAllergicInfo.this, 
							CommonMethod.getAllergenSource());
				// ���������йܵ���ʱ���ݳ�
				String adapterkey = TempDataPool.putAdapter(adapter);
				// ������������ΨһKey��ѡ����ҳ��
				intent.putExtra("adapterkey", adapterkey);
				intent.putExtra("title", "ѡ�����Դ");
				intent.putExtra("fieldname", "����Դ����");
				// �����ֶ��������󳤶�
				intent.putExtra("maxlen", 50);
				// ���뵱ǰֵ
				String nowValue = allergicInfoMap.get("allergen");
				if (nowValue != null && !(nowValue.equals(""))) {
					intent.putExtra("nowvalue", nowValue);
				}
				
				// ����requestCodeΪ��Ӧ���ֶ�
				startActivityForResult(intent, Fields.Allergen);
			}
		});		

		TextView actionbar_page_title=(TextView)this.findViewById(R.id.actionbar_page_title);
		rootBundle = getIntent().getExtras();
		action = rootBundle.getString("action");
		if (action.equals("add")) {
			actionbar_page_title.setText("����������¼");
		} else {
			actionbar_page_title.setText("�޸Ĺ�����¼");
			String allergicid = rootBundle.getString("id");
			position = rootBundle.getInt("position");
			new AsyncLoadInfo().execute(allergicid);
		}
	}
	
	/**
	 * �����ϼ�ҳ�洫�������ݻ�ȡInfo��Ϣ
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
	 * �������ݳ�ʼ������ֵ
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
	 * �첽��������
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
				Toast.makeText(MemberAllergicInfo.this, "���ݼ��ش���", Toast.LENGTH_LONG).show();
			}
			//super.onPostExecute(result);
		}
	}
	
	// ���水ť����¼�
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
	 * �첽�����¼
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
			CommandResult result = new CommandResult(false, "δ֪���󣬱���ʧ�ܡ�");
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
			if (result.getResult())// �������ɹ�
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
			// ����resultCode����ͬ����·��ص�ֵ
			switch (requestCode) {
			case Fields.Allergen: {
				String resultKey = data.getExtras().getString("resultkey");
				// ͨ��ѡ�������ص�ΨһKey����ʱ���ݳػ�ȡ��Ӧ��������
				Object resultObject = TempDataPool.getSelectorResult(resultKey);
				if (resultObject != null) {
					Map<String, String> resultMap = (Map<String, String>) resultObject;
					String resultString = resultMap.get("text");
					CommonMethod.setSelectFieldText(member_allergic_info_txv_allergen, resultString);
					allergicInfoMap.put("allergen", resultString);
				}
				// ����������ʹ��������ʱ���ݳ�����
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
		} else if (resultCode == RESULT_CANCELED) { // �������ء�

		}
	}
	
	/**
	 * ���嵱ǰҳ����ѡ�����͵��ֶα�ʶ
	 * @author yangxc
	 *
	 */
	private class Fields {
		public static final int OccurrenceTime = 1;
		public static final int Allergen = 2;
	}
}
