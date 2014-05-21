/**
 * 
 */
package coolbuy360.searchhealth;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import coolbuy360.adapter.DeleteAbleAdapter;
import coolbuy360.control.CusProgressDialog;
import coolbuy360.dateview.DateDialog;
import coolbuy360.logic.HealthFootprint;
import coolbuy360.logic.User;
import coolbuy360.service.CommandResult;
import coolbuy360.service.CommonMethod;
import coolbuy360.service.Util;

/**
 * ��Ա�����¼���¼����
 * 
 * @author yangxc
 * 
 */
public class MemberHFootprintInfo extends Activity {
	TextView member_footprint_info_txv_eventtime;
	LinearLayout member_footprint_info_item_eventtime;
	EditText member_footprint_info_edt_summarize;
	EditText member_footprint_info_edt_remarks;
	TextView actionbar_page_title;
	String Action;
	String InfoID;
	ACTION action;

	private Dialog pBarcheck;
	private String memberID;
	Bundle rootbundle;
	int position = -1;
	
    private CusProgressDialog cusProgressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_footprint_info);
		rootbundle = getIntent().getExtras();
		// ����ϼ��������Ĳ�����ʽ	
		member_footprint_info_item_eventtime = (LinearLayout) this
				.findViewById(R.id.member_footprint_info_item_eventtime);
		member_footprint_info_txv_eventtime = (TextView) this
				.findViewById(R.id.member_footprint_info_txv_eventtime);
		member_footprint_info_edt_summarize = (EditText) this
				.findViewById(R.id.member_footprint_info_edt_summarize);
		member_footprint_info_edt_remarks = (EditText) this
				.findViewById(R.id.member_footprint_info_edt_remarks);
		actionbar_page_title=(TextView)this.findViewById(R.id.actionbar_page_title);
		Action = rootbundle.getString("action");	
		
		// ���÷��ذ�ť
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MemberHFootprintInfo.this.finish();
			}
		});
		Button SaveBtn = (Button) this.findViewById(R.id.actionbar_save_btn);
		// ���水ť����¼�
		SaveBtn.setOnClickListener(new saveOnClick());
		// ע����Ч��
		member_footprint_info_item_eventtime
				.setOnTouchListener(new CommonMethod.setOnPressed());
		member_footprint_info_item_eventtime
				.setOnClickListener(new onDateItemClick());
		
		if (Action.equals("add")) {
			actionbar_page_title.setText("��ӽ�����־");
			action = ACTION.ADD;
		} else {
			actionbar_page_title.setText("�޸Ľ�����־");
			action = ACTION.EDIT;
			InfoID = rootbundle.getString("id");
			position=rootbundle.getInt("position");
			//�޸�ҳ��
			 new AsyLoadData().execute();
		}
	}

	/**
	 * �����ϼ�ҳ�洫�������ݻ�ȡInfo��Ϣ
	 * @return
	 */
	private Map<String, String> getHFootprintInfo() {
		Map<String, String> infoMap = new HashMap<String, String>();
		infoMap.put("id", rootbundle.getString("id"));
		infoMap.put("eventtime", rootbundle.getString("eventtime"));
		infoMap.put("summarize", rootbundle.getString("summarize"));		
		infoMap.put("remarks", rootbundle.getString("remarks"));
		return infoMap;
	}
	
	
	/**
	 * ����ѡ��item�ĵ���¼�
	 * 
	 * @author Administrator
	 * 
	 */
	private class onDateItemClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			v.setPressed(false);
			// showDateTimePicker();
		   String eventtime=member_footprint_info_txv_eventtime.getText().toString();
			final DateDialog datedialogtest = new DateDialog(
					MemberHFootprintInfo.this);
			if(!eventtime.equals("��ѡ���¼�ʱ��"))
			{
				//Date dt= Util.getDateFromStr(dateStr, "yyyy-MM-dd");
				datedialogtest.setCustomerDate(eventtime, "yyyy-MM-dd");
			}
			else
			{				   
				datedialogtest.setYear(true);
				datedialogtest.setMonth(true);
				datedialogtest.setDay(true);
			  // datedialogtest.setHour(true);				
			}		
		
			// datedialogtest.setYear(true, 1911);
			datedialogtest.setYear(true);
			datedialogtest.setMonth(true);
			datedialogtest.setDay(true);
			datedialogtest.setTitle("��ѡ���¼�ʱ��");
			datedialogtest.show();
			datedialogtest.setOkListener("ȷ��", new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// String time = v.getTag().toString();
					// Toast.makeText(MemberInfo.this,
					// datedialogtest.getMonth(), 1).show();
					CommonMethod.setSelectFieldText(
							member_footprint_info_txv_eventtime,
							datedialogtest.getDateTime("yyyy-MM-dd E"));
					// member_info_txv_birthday.setText(time);
				}
			});
		}
	}

	/**
	 * ���水ť����¼�
	 * 
	 * @author Administrator
	 * 
	 */
	private class saveOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String footprintEventtime=member_footprint_info_txv_eventtime.getText().toString();
			if(footprintEventtime.equals("��ѡ���¼�ʱ��"))
			{			
				Toast.makeText(MemberHFootprintInfo.this, "��ѡ���¼�ʱ��", 1).show();
			}
			else
			{			
			 new AsyHandleData().execute();
			}
		}
	}

	/**
	 * �첽�������ݣ��������º���ӣ�
	 * 
	 * @author Administrator
	 * 
	 */
	private final class AsyHandleData extends AsyncTask<String, Void, Integer> {	
		HealthFootprint healthFootprint = HealthFootprint
				.getHealthFootprintInstance();
		Map<String, String> innerMap;
		CommandResult commonResult;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
		/*	pBarcheck = new Dialog(MemberHFootprintInfo.this, R.style.dialog);
			// dialog.setTitle("Indeterminate");
			pBarcheck.setContentView(R.layout.custom_progress);
			pBarcheck.setCancelable(true);
			pBarcheck.show();*/
			cusProgressDialog =new CusProgressDialog(MemberHFootprintInfo.this,R.style.dialog);
			cusProgressDialog.setPregressBar(true);		
			cusProgressDialog.setCanceledOnTouchOutside(true);
			cusProgressDialog.show();
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(String... params) {
			String eventTime = member_footprint_info_txv_eventtime.getText()
					.toString();
			String summarize = member_footprint_info_edt_summarize.getText()
					.toString();
			String remarks = member_footprint_info_edt_remarks.getText()
					.toString();
			Boolean isSuccess = false;
			int returnValue = 1;
			switch (action) {
			case ADD:
				try {
					isSuccess = healthFootprint.insert(User.getMemberID(),
							eventTime.equals("��ѡ���¼�ʱ��") ? "" : eventTime,
							summarize, remarks);
					Log.i("chenlinwei", isSuccess+"");
				} catch (Exception e) {
					returnValue = 2;
				}
				break;
			case EDIT:
				try {
					innerMap=new HashMap<String, String>();
					innerMap.put("eventtime", eventTime);
					innerMap.put("summarize", summarize);
					innerMap.put("remarks", remarks);
					innerMap.put("id",InfoID);
					try {
						commonResult=HealthFootprint.getHealthFootprintInstance().update(innerMap);
						isSuccess=commonResult.getResult();
					} catch (Exception e) {
						// TODO Auto-generated catch block
					returnValue=2;
					}					
					Log.i("chenlinwei", isSuccess+"");
				} catch (Exception e) {
					returnValue = 2;
				}		
				
				if(innerMap.size()>0)
				{
				  returnValue=0;
				}
				else
				{
					returnValue=2;
					
				}
				break;

			default:
				break;
			}
			if (isSuccess) {
				returnValue = 0;
			} else {
				returnValue = 1;
			}
			return returnValue;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Integer result) {
			//pBarcheck.cancel();
			if(result==0)
			{		
				cusProgressDialog.cancel();
				Intent resultIntent =new Intent().setClass(MemberHFootprintInfo.this, MemberHealthFootprint.class);
				if(action==ACTION.ADD)
				{					
				}
				else
				{					
					resultIntent.putExtra("position", position);
					resultIntent.putExtra("eventtime", Util
							.getDateFormat(innerMap.get("eventtime"),
									"yyyy/MM/dd hh:mm:ss",
									"yyyy-MM-dd E"));
					Log.i("chenlinwei","shijiange:"+ Util
							.getDateFormat(innerMap.get("eventtime"),
									"yyyy/MM/dd hh:mm:ss",
									"yyyy-MM-dd E"));
					resultIntent.putExtra("summarize",innerMap.get("summarize"));				
					resultIntent.putExtra("remarks",innerMap.get("remarks"));
				}
				
				MemberHFootprintInfo.this.setResult(RESULT_OK, resultIntent);
				MemberHFootprintInfo.this.finish();
				
				//Toast.makeText(MemberHFootprintInfo.this, "��ӳɹ�", 1).show();
				
			}else if(result==1)
			{		
				/*cusProgressDialog.setPregressBar(false);
				cusProgressDialog.setRefreshBtn(true);
				cusProgressDialog.setMessage("������¼���");
				cusProgressDialog.setReFreshListener("", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						new AsyHandleData().execute();
					}
				});	*/	
				cusProgressDialog.cancel();
				Intent intent =new Intent().setClass(MemberHFootprintInfo.this, MemberHealthFootprint.class);
				MemberHFootprintInfo.this.setResult(RESULT_OK, intent);
				MemberHFootprintInfo.this.finish();				
			}
			else
			{			
				Toast.makeText(MemberHFootprintInfo.this, commonResult.getMessage(), 1).show();				
			}		
		}

	}

	/**
	 * �첽��ʼ�����ݣ����μ��أ�
	 * 
	 * @author Administrator
	 * 
	 */
	private final class AsyLoadData extends AsyncTask<String, Void, Integer> {
		Map<String, String> innerMap=new HashMap<String, String>();
		int returnValue=1;
		@Override
		protected void onPreExecute() {
			cusProgressDialog =new CusProgressDialog(MemberHFootprintInfo.this,R.style.dialog);
			cusProgressDialog.setPregressBar(true);		
			cusProgressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			innerMap=getHFootprintInfo();
			if(innerMap.size()>0)
			{
			  returnValue=0;
			}
			else
			{
				returnValue=2;
				
			}
			return returnValue;
		}		
		@Override
		protected void onPostExecute(Integer result) {
			if(returnValue==0)
			{
				InitViewData(innerMap);
				cusProgressDialog.cancel();
			}
			else {
				cusProgressDialog.cancel();
				Toast.makeText(MemberHFootprintInfo.this, "���ݼ��ش���", Toast.LENGTH_LONG).show();
			}			
			super.onPostExecute(result);
		}

	}
	
	/**
	 * �������ݳ�ʼ������ֵ
	 * @param infoMap
	 */
	private void InitViewData(Map<String, String> infoMap) {		
		String eventtime = infoMap.get("eventtime");
		String summarize = infoMap.get("summarize");
		String remarks = infoMap.get("remarks");
		if(eventtime!=null && !eventtime.equals("")){
			CommonMethod.setSelectFieldText(member_footprint_info_txv_eventtime,Util.getDateFormat(eventtime, "yyyy-MM-dd E"));
		}
		if(summarize!=null && !summarize.equals("")){
			CommonMethod.setSelectFieldText(member_footprint_info_edt_summarize, summarize);
		}
		
		if(remarks!=null && !remarks.equals("")){
			CommonMethod.setSelectFieldText(member_footprint_info_edt_remarks,remarks);
			//member_footprint_info_edt_remarks.setText(remarks);
		}
	}
	
	enum ACTION {
		EDIT, ADD
	}

}
