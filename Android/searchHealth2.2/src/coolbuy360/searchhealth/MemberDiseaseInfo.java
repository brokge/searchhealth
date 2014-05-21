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
 * ��Ա����ʷ����
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
		// Ϊ�˳���׼��
		searchApp.getInstance().addActivity(this);

		// ���÷��ذ�ť
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MemberDiseaseInfo.this.finish();
			}
		});
		
		// ��ȡ����ؼ�		
		// ������
		member_disease_info_edt_remarks = (EditText) this
				.findViewById(R.id.member_disease_info_edt_remarks); 
		// ѡ����
		member_disease_info_txv_diagnosetime = (TextView) this
				.findViewById(R.id.member_disease_info_txv_diagnosetime);
		member_disease_info_txv_diseasename = (TextView) this
				.findViewById(R.id.member_disease_info_txv_diseasename);
		member_disease_info_txv_iscontagious = (TextView) this
				.findViewById(R.id.member_disease_info_txv_iscontagious);
		member_disease_info_txv_ishereditary = (TextView) this
				.findViewById(R.id.member_disease_info_txv_ishereditary);
				
		// ѡ����������
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
		
		// ע����Ч��
		member_disease_info_item_diagnosetime.setOnTouchListener(new CommonMethod.setOnPressed());
		member_disease_info_item_diseasename.setOnTouchListener(new CommonMethod.setOnPressed());
		member_disease_info_item_iscontagious.setOnTouchListener(new CommonMethod.setOnPressed());
		member_disease_info_item_ishereditary.setOnTouchListener(new CommonMethod.setOnPressed());
		
		// ��ѡ��ȷ��ʱ�䡱���
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
				datedialogtest.setTitle("��ѡ��ȷ��ʱ��");
				datedialogtest.show();
				datedialogtest.setOkListener("ȷ��", new OnClickListener() {
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
		
		// ��ѡ�񼲲����ơ����
		member_disease_info_item_diseasename.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				CommonMethod.clearFocuse(MemberDiseaseInfo.this);
				Intent intent = new Intent().setClass(MemberDiseaseInfo.this,
						SHInputSelector.class);
				// ʵ�����б�����Դ��������
				NomalSelectorAdapter adapter = 
					new NomalSelectorAdapter(MemberDiseaseInfo.this, 
							CommonMethod.getDiseaseNamesSource());
				// ���������йܵ���ʱ���ݳ�
				String adapterkey = TempDataPool.putAdapter(adapter);
				// ������������ΨһKey��ѡ����ҳ��
				intent.putExtra("adapterkey", adapterkey);
				intent.putExtra("title", "ѡ�񼲲�����");
				intent.putExtra("fieldname", "��������");
				// �����ֶ��������󳤶�
				intent.putExtra("maxlen", 50);
				// ���뵱ǰֵ
				String nowValue = diseaseInfoMap.get("diseasename");
				if (nowValue != null && !(nowValue.equals(""))) {
					intent.putExtra("nowvalue", nowValue);
				}
				
				// ����requestCodeΪ��Ӧ���ֶ�
				startActivityForResult(intent, Fields.DiseaseName);
			}
		});
		
		// ��ѡ���Ƿ�Ⱦ�������
		member_disease_info_item_iscontagious.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				CommonMethod.clearFocuse(MemberDiseaseInfo.this);
				Intent intent = new Intent().setClass(MemberDiseaseInfo.this,
						SHValueSelector.class);
				// ʵ�����б�����Դ��������
				NomalSelectorAdapter adapter = 
					new NomalSelectorAdapter(MemberDiseaseInfo.this, 
							CommonMethod.getYesNoSource());
				// ���������йܵ���ʱ���ݳ�
				String adapterkey = TempDataPool.putAdapter(adapter);
				// ������������ΨһKey��ѡ����ҳ��
				intent.putExtra("adapterkey", adapterkey);
				intent.putExtra("title", "ѡ���Ƿ�Ⱦ��");
				
				// ����requestCodeΪ��Ӧ���ֶ�
				startActivityForResult(intent, Fields.IsContagious);
			}
		});
		
		// ��ѡ���Ƿ��Ŵ��������
		member_disease_info_item_ishereditary.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				CommonMethod.clearFocuse(MemberDiseaseInfo.this);
				Intent intent = new Intent().setClass(MemberDiseaseInfo.this,
						SHValueSelector.class);
				// ʵ�����б�����Դ��������
				NomalSelectorAdapter adapter = 
					new NomalSelectorAdapter(MemberDiseaseInfo.this, 
							CommonMethod.getYesNoSource());
				// ���������йܵ���ʱ���ݳ�
				String adapterkey = TempDataPool.putAdapter(adapter);
				// ������������ΨһKey��ѡ����ҳ��
				intent.putExtra("adapterkey", adapterkey);
				intent.putExtra("title", "ѡ���Ƿ��Ŵ���");
				
				// ����requestCodeΪ��Ӧ���ֶ�
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
		
		String titleString = "����";
		if(diseaseType.equals("contagious")) {
			titleString = "��Ⱦ��";
		} else if (diseaseType.equals("hereditary")) {
			titleString = "�Ŵ���";				
		} 
		
		action = rootBundle.getString("action");
		if (action.equals("add")) {
			actionbar_page_title.setText("����" + titleString + "��¼");
			if(diseaseType.equals("contagious")) {
				diseaseInfoMap.put("iscontagious", "1");
				diseaseInfoMap.put("ishereditary", "0");
			} else if (diseaseType.equals("hereditary")) {
				diseaseInfoMap.put("iscontagious", "0");
				diseaseInfoMap.put("ishereditary", "1");
			} else {
				diseaseInfoMap.put("iscontagious", "0");
				diseaseInfoMap.put("ishereditary", "0");
				CommonMethod.setSelectFieldText(member_disease_info_txv_iscontagious, "��");
				CommonMethod.setSelectFieldText(member_disease_info_txv_ishereditary, "��");
			}
		} else {
			actionbar_page_title.setText("�޸�" + titleString + "��¼");
			String diseaseid = rootBundle.getString("id");
			position = rootBundle.getInt("position");
			new AsyncLoadInfo().execute(diseaseid);
		}
	}
	
	/**
	 * �����ϼ�ҳ�洫�������ݻ�ȡInfo��Ϣ
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
	 * �������ݳ�ʼ������ֵ
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
					iscontagious.equals("1") ? "��" : "��");
		}
		if(ishereditary!=null && !ishereditary.equals("")){			
			CommonMethod.setSelectFieldText(
					member_disease_info_txv_ishereditary,
					ishereditary.equals("1") ? "��" : "��");
		}
		if(remarks!=null && !remarks.equals("")){
			member_disease_info_edt_remarks.setText(remarks);
		}
	}
	
	/**
	 * �첽��������
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
				Toast.makeText(MemberDiseaseInfo.this, "���ݼ��ش���", Toast.LENGTH_LONG).show();
			}
			//super.onPostExecute(result);
		}
	}
	
	// ���水ť����¼�
	private final class onSaveBtnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			String remarks = member_disease_info_edt_remarks.getText().toString().trim();
			diseaseInfoMap.put("remarks", remarks);			
			new AsyncSave().execute();			
		}
	}
	
	/**
	 * �첽�����¼
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
			CommandResult result = new CommandResult(false, "δ֪���󣬱���ʧ�ܡ�");
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
			if (result.getResult())// �������ɹ�
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
			// ����resultCode����ͬ����·��ص�ֵ
			switch (requestCode) {
			case Fields.DiseaseName: {
				String resultKey = data.getExtras().getString("resultkey");
				// ͨ��ѡ�������ص�ΨһKey����ʱ���ݳػ�ȡ��Ӧ��������
				Object resultObject = TempDataPool.getSelectorResult(resultKey);
				if (resultObject != null) {
					Map<String, String> resultMap = (Map<String, String>) resultObject;
					String resultString = resultMap.get("text");
					CommonMethod.setSelectFieldText(member_disease_info_txv_diseasename, resultString);
					diseaseInfoMap.put("diseasename", resultString);
				}
				// ����������ʹ��������ʱ���ݳ�����
				TempDataPool.destroySelectorResult(resultKey);
				break;
			}
			case Fields.DiagnoseTime: {
				break;
			}
			case Fields.IsContagious: {
				String resultKey = data.getExtras().getString("resultkey");
				// ͨ��ѡ�������ص�ΨһKey����ʱ���ݳػ�ȡ��Ӧ��������
				Object resultObject = TempDataPool.getSelectorResult(resultKey);
				if (resultObject != null) {
					Map<String, String> resultMap = (Map<String, String>) resultObject;
					String resultString = resultMap.get("text");
					CommonMethod.setSelectFieldText(member_disease_info_txv_iscontagious, resultString);
					diseaseInfoMap.put("iscontagious", resultMap.get("value"));
				}
				// ����������ʹ��������ʱ���ݳ�����
				TempDataPool.destroySelectorResult(resultKey);
				break;
			}
			case Fields.IsHereditary: {
				String resultKey = data.getExtras().getString("resultkey");
				// ͨ��ѡ�������ص�ΨһKey����ʱ���ݳػ�ȡ��Ӧ��������
				Object resultObject = TempDataPool.getSelectorResult(resultKey);
				if (resultObject != null) {
					Map<String, String> resultMap = (Map<String, String>) resultObject;
					String resultString = resultMap.get("text");
					CommonMethod.setSelectFieldText(member_disease_info_txv_ishereditary, resultString);
					diseaseInfoMap.put("ishereditary", resultMap.get("value"));
				}
				// ����������ʹ��������ʱ���ݳ�����
				TempDataPool.destroySelectorResult(resultKey);
				break;
			}
			default:
				break;
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
		public static final int DiagnoseTime = 1;
		public static final int DiseaseName = 2;
		public static final int IsContagious = 3;
		public static final int IsHereditary = 4;
	}
}
