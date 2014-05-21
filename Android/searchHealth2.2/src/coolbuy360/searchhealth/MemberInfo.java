/**
 * 
 */
package coolbuy360.searchhealth;

import java.lang.reflect.Member;
import java.util.List;
import java.util.Map;

import coolbuy360.adapter.NomalSelectorAdapter;
import coolbuy360.dateview.DateDialog;
import coolbuy360.logic.User;
import coolbuy360.logic.UserInfo;
import coolbuy360.service.CommandResult;
import coolbuy360.service.CommonMethod;
import coolbuy360.service.TempDataPool;
import coolbuy360.service.Util;
import coolbuy360.service.Validator;
import coolbuy360.service.searchApp;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Editable.Factory;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author yangxc �������ϱ༭ҳ��
 */
public class MemberInfo extends Activity {

	EditText member_info_edt_membername;
	EditText member_info_edt_idcardnum;
	EditText member_info_edt_mobile;
	EditText member_info_edt_telephone;
	EditText member_info_edt_email;
	EditText member_info_edt_familyaddress;
	EditText member_info_edt_income;
	EditText member_info_edt_commonhospital;

	TextView member_info_txv_username;
	TextView member_info_txv_sex;
	TextView member_info_txv_birthday;
	TextView member_info_txv_educationlevel;
	TextView member_info_txv_job;
	TextView member_info_txv_hc;
	TextView member_info_txv_buyway;
	
	Button actionbar_save_btn;

	private Dialog pBarcheck;
	String memberID;
	boolean idcardNumValidate=true;	
	boolean mobileValidate=true;
	boolean telephoneValidate=true;	
	boolean emailValidate=true;
	boolean addressValidate=true;
	boolean membernameValidate=true;
	boolean commonHospitalValidate=true;
	//private static int START_YEAR = 1900, END_YEAR = 2100;
	private String memberName="";
	String idCardNum;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_info);
		// Ϊ�˳���׼��
		searchApp.getInstance().addActivity(this);

		// ���÷��ذ�ť
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MemberInfo.this.finish();
			}
		});
		// ��ȡ���洫������ֵ
		// ��ȡ����ؼ�
		// ������
		member_info_edt_membername = (EditText) this
				.findViewById(R.id.member_info_edt_membername);
		member_info_edt_idcardnum = (EditText) this
				.findViewById(R.id.member_info_edt_idcardnum);
		member_info_edt_mobile = (EditText) this
				.findViewById(R.id.member_info_edt_mobile);
		member_info_edt_telephone = (EditText) this
				.findViewById(R.id.member_info_edt_telephone);
		member_info_edt_email = (EditText) this
				.findViewById(R.id.member_info_edt_email);
		member_info_edt_familyaddress = (EditText) this
				.findViewById(R.id.member_info_edt_familyaddress);
		member_info_edt_income = (EditText) this
				.findViewById(R.id.member_info_edt_income);
		member_info_edt_commonhospital = (EditText) this
				.findViewById(R.id.member_info_edt_commonhospital);
		// ѡ����
		member_info_txv_username = (TextView) this
				.findViewById(R.id.member_info_txv_username);
		member_info_txv_sex = (TextView) this
				.findViewById(R.id.member_info_txv_sex);
		member_info_txv_birthday = (TextView) this
				.findViewById(R.id.member_info_txv_birthday);

		member_info_txv_educationlevel = (TextView) this
				.findViewById(R.id.member_info_txv_educationlevel);
		member_info_txv_job = (TextView) this
				.findViewById(R.id.member_info_txv_job);
		member_info_txv_hc = (TextView) this
				.findViewById(R.id.member_info_txv_hc);
		member_info_txv_buyway = (TextView) this
				.findViewById(R.id.member_info_txv_buyway);

		// ѡ����������
		LinearLayout member_info_item_sex = (LinearLayout) this
				.findViewById(R.id.member_info_item_sex);
		LinearLayout member_info_item_birthday = (LinearLayout) this
				.findViewById(R.id.member_info_item_birthday);
		
		LinearLayout member_info_item_educationlevel=(LinearLayout)this.findViewById(R.id.member_info_item_educationlevel);
		
		LinearLayout member_info_item_job=(LinearLayout)this.findViewById(R.id.member_info_item_job);
		
		LinearLayout member_info_item_hc=(LinearLayout)this.findViewById(R.id.member_info_item_hc);
		
		LinearLayout member_info_item_buyway=(LinearLayout)this.findViewById(R.id.member_info_item_buyway);
		
		
		actionbar_save_btn = (Button) this
				.findViewById(R.id.actionbar_save_btn);
		actionbar_save_btn.setOnClickListener(new onSaveBtnClick());		
		
		// ע����Ч��
		member_info_item_sex
				.setOnTouchListener(new CommonMethod.setOnPressed());
		member_info_item_birthday
				.setOnTouchListener(new CommonMethod.setOnPressed());
		member_info_item_educationlevel.setOnTouchListener(new CommonMethod.setOnPressed());
		member_info_item_hc.setOnTouchListener(new CommonMethod.setOnPressed());
		member_info_item_job.setOnTouchListener(new CommonMethod.setOnPressed());
		member_info_item_buyway.setOnTouchListener(new CommonMethod.setOnPressed());
		//��֤�������ĸ�ʽ�ļ����¼�
		VaridateEdittxt();
		
		// ��ѡ���Ա𡱵��
		member_info_item_sex.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				CommonMethod.clearFocuse(MemberInfo.this);
				Intent intent = new Intent().setClass(MemberInfo.this,
						SHValueSelector.class);
				// ʵ�����б�����Դ��������
				NomalSelectorAdapter adapter = new NomalSelectorAdapter(
						MemberInfo.this, CommonMethod.getSexNamesSource());
				// ���������йܵ���ʱ���ݳ�
				String adapterkey = TempDataPool.putAdapter(adapter);
				// ������������ΨһKey��ѡ����ҳ��
				intent.putExtra("adapterkey", adapterkey);
				intent.putExtra("title", "�����Ա�");
				// ����requestCodeΪ��Ӧ���ֶ�
				startActivityForResult(intent, Fields.Sex);

			}
		});
		
		// ��ѡ��������ڡ����
		member_info_item_birthday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				// showDateTimePicker();
				String dateStr=member_info_txv_birthday.getText().toString();
				final DateDialog datedialogtest = new DateDialog(
						MemberInfo.this);
				datedialogtest.setCustomerDate(true);
				if(!dateStr.equals("��ѡ�����ĳ�������"))
				{
					//Date dt= Util.getDateFromStr(dateStr, "yyyy-MM-dd");
					datedialogtest.setCustomerDate(dateStr, "yyyy-MM-dd");
				}
				else
				{				   
					datedialogtest.setYear(true);
					datedialogtest.setMonth(true);
					datedialogtest.setDay(true);
				  // datedialogtest.setHour(true);				
				}
				datedialogtest.setTitle("��ѡ�����ĳ�������");
				datedialogtest.show();
				datedialogtest.setOkListener("ȷ��", new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String time = v.getTag().toString();
						// Toast.makeText(MemberInfo.this,
						// datedialogtest.getMonth(), 1).show();
						CommonMethod.setSelectFieldText(member_info_txv_birthday,time);
						//member_info_txv_birthday.setText(time);
					}
				});
			}
		});
		
		//ѡ�񡰽���ˮƽ�����
		member_info_item_educationlevel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				CommonMethod.clearFocuse(MemberInfo.this);
				Intent intent = new Intent().setClass(MemberInfo.this,
						SHValueSelector.class);
				// ʵ�����б�����Դ��������
				NomalSelectorAdapter adapter = new NomalSelectorAdapter(
						MemberInfo.this, CommonMethod.getEducationLevelSource());
				// ���������йܵ���ʱ���ݳ�
				String adapterkey = TempDataPool.putAdapter(adapter);
				// ������������ΨһKey��ѡ����ҳ��
				intent.putExtra("adapterkey", adapterkey);
				intent.putExtra("title", "ѡ���Ļ��̶�");
				// ����requestCodeΪ��Ӧ���ֶ�
				startActivityForResult(intent, Fields.EducationLevel);
			}
		});
		
		//ѡ��ҽ����������
		member_info_item_hc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				CommonMethod.clearFocuse(MemberInfo.this);
				Intent intent = new Intent().setClass(MemberInfo.this,
						SHValueSelector.class);
				// ʵ�����б�����Դ��������
				NomalSelectorAdapter adapter = new NomalSelectorAdapter(
						MemberInfo.this, CommonMethod.getHcSource());
				// ���������йܵ���ʱ���ݳ�
				String adapterkey = TempDataPool.putAdapter(adapter);
				// ������������ΨһKey��ѡ����ҳ��
				intent.putExtra("adapterkey", adapterkey);
				intent.putExtra("title", "��ѡ��ҽ�����");
				// ����requestCodeΪ��Ӧ���ֶ�
				startActivityForResult(intent, Fields.Hc);
			}
		});
		
		//ѡ�񡰹�����������
		member_info_item_job.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				CommonMethod.clearFocuse(MemberInfo.this);
				Intent intent = new Intent().setClass(MemberInfo.this,
						SHValueSelector.class);
				// ʵ�����б�����Դ��������
				NomalSelectorAdapter adapter = new NomalSelectorAdapter(
						MemberInfo.this, CommonMethod.getJobNamesSource());
				// ���������йܵ���ʱ���ݳ�
				String adapterkey = TempDataPool.putAdapter(adapter);
				// ������������ΨһKey��ѡ����ҳ��
				intent.putExtra("adapterkey", adapterkey);
				intent.putExtra("title", "ѡ������ְҵ");
				// ����requestCodeΪ��Ӧ���ֶ�
				startActivityForResult(intent, Fields.Job);
			}
		});
    	//ѡ�񡰹���;�������
		member_info_item_buyway.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				CommonMethod.clearFocuse(MemberInfo.this);
				Intent intent = new Intent().setClass(MemberInfo.this,
						SHValueSelector.class);
				// ʵ�����б�����Դ��������
				NomalSelectorAdapter adapter = new NomalSelectorAdapter(
						MemberInfo.this, CommonMethod.getBuyWaySource());
				// ���������йܵ���ʱ���ݳ�
				String adapterkey = TempDataPool.putAdapter(adapter);
				// ������������ΨһKey��ѡ����ҳ��
				intent.putExtra("adapterkey", adapterkey);
				intent.putExtra("title", "ѡ��ҩ;��");
				// ����requestCodeΪ��Ӧ���ֶ�
				startActivityForResult(intent, Fields.BuyWay);
			}
		});	
		
				
		Bundle bundle = getIntent().getExtras();
		memberID = bundle.getString("memberid");
		Log.i("chenlinwei", "����:" + memberID);
		new asyLoadUserInfo().execute(memberID);
	}
	
	private SpannableStringBuilder changeErrorColor(String errorMsg)
	{
		//int ecolor =R.color.green ; // whatever color you want
		String estring = errorMsg;
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.RED);
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
		ssbuilder.setSpan(fgcspan, 0, estring.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ssbuilder;
	}
	
	/**
	 * ��֤��Ҫ���ݵ�������
	 */
	private void VaridateEdittxt()
	{
		//���֤������֤
		member_info_edt_idcardnum.setOnFocusChangeListener(new OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				EditText dt=(EditText)v;
				String input=dt.getText().toString();
				if(!hasFocus)
				{				
					if(input.length()!=0&&!input.equals(""))
					{
						Log.i("chenlinwei", input.length()+"���֤�������ĳ���");
						if(!Validator.getValidatorInstance(MemberInfo.this).isIDCardNum(input))
						{
							idcardNumValidate=false;							
							
							dt.setError(changeErrorColor("��������ȷ�����֤��"));						
						}
						else
						{
							idcardNumValidate=true;
						}
					}
					else
						idcardNumValidate=true;
				}				
			}
		});
		//member_info_edt_idcardnum.getError();
		
		//�ֻ�������֤
		member_info_edt_mobile.setOnFocusChangeListener(new OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				EditText et=(EditText)v;
				String input=et.getText().toString();
				if(!hasFocus)
				{
					if(input.length()!=0&&!input.equals(""))
					{
						if(!Validator.getValidatorInstance(MemberInfo.this).isMobileNO(input))
						{
							mobileValidate=false;
							et.setError(changeErrorColor("��������ȷ���ֻ�����"));	
							//<font color='blue'>����</font>
						}	
						else
							mobileValidate=true;
					}
					else
					{
						mobileValidate=true;
					}
				}			
			}
		});
		
		//�绰�������֤
		member_info_edt_telephone.setOnFocusChangeListener(new OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				EditText et=(EditText)v;
				String input=et.getText().toString();
				if(!hasFocus)
				{
					if(input.length()!=0&&!input.equals(""))
					{
						if(!Validator.getValidatorInstance(MemberInfo.this).isTelephone(input))
						{
							telephoneValidate=false;
							et.setError(changeErrorColor("�绰�����ʽ��0571-86199111"));							
						}
						else
							telephoneValidate=true;						
					}
					else
						telephoneValidate=true;
				}
			}
		});
		
		//�������֤
		member_info_edt_email.setOnFocusChangeListener(new OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				EditText et=(EditText)v;
				String input=et.getText().toString();
				if(!hasFocus)
				{
					if(input.length()!=0&&!input.equals(""))
					{
						if(!Validator.getValidatorInstance(MemberInfo.this).isEmail(input))
						{
							emailValidate=false;
							et.setError(changeErrorColor("��������ȷ�ĵ�������"));							
						}
						else
							emailValidate=true;							
					}
					else
						emailValidate=true;
				}
			}
		});
		
		//��ͥסַ����֤
		member_info_edt_familyaddress.setOnFocusChangeListener(new OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				EditText et=(EditText)v;
				String input=et.getText().toString();
				if(!hasFocus)
				{
					if(input.length()!=0&&!input.equals(""))
					{
						if(!Validator.getValidatorInstance(MemberInfo.this).contentLengthCheck(input,0,100))
						{
							addressValidate=false;
							et.setError(changeErrorColor("��ͥסַ���ܳ���100����"));							
						}
						else
							addressValidate=true;						
					}
					else
						addressValidate=true;
				}
			}
		});
		
		//��������֤
		member_info_edt_membername.setOnFocusChangeListener(new OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				EditText et=(EditText)v;
				String input=et.getText().toString();
				if(!hasFocus)
				{
					if(input.length()!=0&&!input.equals(""))
					{
						if(!Validator.getValidatorInstance(MemberInfo.this).contentLengthCheck(input,0,20))
						{
							membernameValidate=false;
							et.setError(changeErrorColor("�������Ȳ��ܳ���20����"));	
						}						
						else
							membernameValidate=true;
					}
					else
						membernameValidate=true;
				}
			}
		});
		
		//��ȥҽԺ������֤
		member_info_edt_commonhospital.setOnFocusChangeListener(new OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				EditText et=(EditText)v;
				String input=et.getText().toString();
				if(!hasFocus)
				{
					if(input.length()!=0&&!input.equals(""))
					{
						if(!Validator.getValidatorInstance(MemberInfo.this).contentLengthCheck(input,0,20))
						{
							commonHospitalValidate=false;
							et.setError(changeErrorColor("ҽԺ���Ʋ��ܳ���20����"));
						}
						else
							commonHospitalValidate=true;
					}
					else
						commonHospitalValidate=true;
				}
			}
		});
	}	

	/**
	 * �첽�����û�����
	 * @author Administrator
	 */
	private class asyLoadUserInfo extends AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerDoctorList;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//pBarcheck = new ProgressDialog(MemberInfo.this);
			pBarcheck = new Dialog(MemberInfo.this, R.style.dialog);
			// dialog.setTitle("Indeterminate");
			pBarcheck.setContentView(R.layout.custom_progress);
			pBarcheck.setCancelable(true);
			pBarcheck.show();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				if (params.length > 0) {
					// innerDoctorList=UserInfo.getUserInfoInstance().getUserInfo("31000101w00001");
					innerDoctorList = UserInfo.getUserInfoInstance()
							.getUserInfo(Integer.valueOf(params[0]));
				} else {
					innerDoctorList = UserInfo.getUserInfoInstance()
							.getUserInfo(4);
				}
				if (innerDoctorList != null) {
					if (innerDoctorList.size() > 0) {
						Log.i("chenlinwei", "����list���ݼ�����ֵ:0");
						return 0;
					} else {
						Log.i("chenlinwei", "����list���ݼ�����ֵ:1");
						return 1;
					}
				} else {
					return 2;
				}

			} catch (Exception e) {
				Log.i("chenlinwei", "����list���ݼ�����ֵ:2");
				return 2;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if (result == 0) {
				InitViewData(innerDoctorList.get(0));
				pBarcheck.cancel();
			} else if (result == 1) {
				pBarcheck.cancel();
				actionbar_save_btn.setVisibility(View.GONE);
			} else {
				pBarcheck.cancel();
				actionbar_save_btn.setVisibility(View.GONE);
				Toast.makeText(MemberInfo.this, "�������ݳ���", Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}
	}

	// ��ʼ��������ʾ
	private void InitViewData(Map<String, String> memberInfoMap) {
		if (memberInfoMap.size() > 0) {
			CommonMethod.setSelectFieldText(member_info_txv_username,
					memberInfoMap.get("username"));
			memberName = memberInfoMap.get("username");
			//Log.i("chenlinwei", "�û���:" + memberInfoMap.get("username"));
			//Log.i("chenlinwei", "�Ա�:" + memberInfoMap.get("sex"));
			String sex = memberInfoMap.get("sex").equals("1") ? "��"
					: (memberInfoMap.get("sex").equals("0") ? "Ů" : "��ѡ���Ա�");
			CommonMethod.setSelectFieldText(member_info_txv_sex, sex);

			// Log.i("chenlinwei", "��Ա����:" + memberInfoMap.get("membername"));
			if (memberInfoMap.get("membername").equals("")
					|| memberInfoMap.get("membername") == null) {
			} else {
				member_info_edt_membername.setText(memberInfoMap
						.get("membername"));
			}
			if (memberInfoMap.get("idcardnum").equals("")
					|| memberInfoMap.get("idcardnum") == null) {
				Log.i("chenlinwei", "���֤��:" + memberInfoMap.get("idcardnum"));
			} else {
				idCardNum = memberInfoMap.get("idcardnum");
				// ���֤��������ʾ
				String xidCardNum = idCardNum.substring(0, idCardNum.length()-4) + "**" + idCardNum.substring(idCardNum.length()-2);
				member_info_edt_idcardnum.setText(xidCardNum);
				member_info_edt_idcardnum.setEnabled(false);
			}
			
			// Log.i("chenlinwei", "����:" + memberInfoMap.get("birthday"));
			String birthday = memberInfoMap.get("birthday");
			if (birthday == null || birthday.equals("")
					|| birthday.contains("1753")) {
			} else {
				CommonMethod.setSelectFieldText(member_info_txv_birthday,
						Util.getDateFormat(birthday, "yyyy-MM-dd"));
			}

			// Log.i("chenlinwei", "�ֻ���:" + memberInfoMap.get("mobile"));
			if (memberInfoMap.get("mobile").equals("")) {
			} else {
				member_info_edt_mobile.setText(memberInfoMap.get("mobile"));
				member_info_edt_mobile.setEnabled(false);
			}

			// Log.i("chenlinwei", "�绰:" + memberInfoMap.get("telephone"));
			if (memberInfoMap.get("telephone").equals("")) {
			} else {
				member_info_edt_telephone.setText(memberInfoMap
						.get("telephone"));
			}

			// Log.i("chenlinwei", "����:" + memberInfoMap.get("email"));
			if (memberInfoMap.get("email").equals("")) {
			} else {
				member_info_edt_email.setText(memberInfoMap.get("email"));
			}
			// Log.i("chenlinwei", "��ͥסַ:" +
			// memberInfoMap.get("familyaddress"));
			if (memberInfoMap.get("familyaddress").equals("")) {
			} else {
				member_info_edt_familyaddress.setText(memberInfoMap
						.get("familyaddress"));
			}

			// Log.i("chenlinwei", "�Ļ��̶�:" +
			// memberInfoMap.get("educationlevel"));
			if (memberInfoMap.get("educationlevel").equals("")) {

			} else {
				CommonMethod.setSelectFieldText(member_info_txv_educationlevel,
						memberInfoMap.get("educationlevel"));
			}
			// member_info_txv_educationlevel.setText(memberInfoMap.get("educationlevel").equals("")
			// ? "��ѡ���Ļ��̶�" : memberInfoMap.get("educationlevel"));
			// Log.i("chenlinwei", "�Ļ��̶�:" + memberInfoMap.get("job"));
			// ְҵ
			if (memberInfoMap.get("job").equals("")) {
			} else {
				CommonMethod.setSelectFieldText(member_info_txv_job,
						memberInfoMap.get("job"));
			}
			// Log.i("chenlinwei", "ҽ��:" + memberInfoMap.get("hc"));
			if (memberInfoMap.get("hc").equals("")) {
			} else {
				CommonMethod.setSelectFieldText(member_info_txv_hc,
						memberInfoMap.get("hc"));
			}
			// Log.i("chenlinwei", "֧��:" + memberInfoMap.get("income"));
			// member_info_edt_income.setText(memberInfoMap.get("income"));
			if (memberInfoMap.get("income").equals("")) {
			} else {
				member_info_edt_income.setText(memberInfoMap.get("income"));
			}
			// Log.i("chenlinwei", "��ҩ;��:" + memberInfoMap.get("buyway"));
			// /member_info_txv_buyway.setText(memberInfoMap.get("buyway").equals("")
			// ? "��ѡ��ҩ;��"
			// : memberInfoMap.get("buyway"));
			if (memberInfoMap.get("buyway").equals("")) {
			} else {
				CommonMethod.setSelectFieldText(member_info_txv_buyway,
						memberInfoMap.get("buyway"));
			}

			// Log.i("chenlinwei", "��ȥҽԺ:" +
			// memberInfoMap.get("commonhospital"));
			if (memberInfoMap.get("commonhospital").equals("")) {
			} else {
				member_info_edt_commonhospital.setText(memberInfoMap
						.get("commonhospital"));
			}
		}
	}

	// ���水ť����¼�
	private final class onSaveBtnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			CommonMethod.clearFocuse(MemberInfo.this);
			if (idcardNumValidate && mobileValidate && telephoneValidate
					&& emailValidate && addressValidate
					&& commonHospitalValidate && membernameValidate) {
				new asyUpdateUserInfo().execute();
			}
			else
			{
				Toast.makeText(MemberInfo.this, "���������", Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * ������Ϣ
	 * @author habei
	 */
	private class asyUpdateUserInfo extends AsyncTask<String, Void, Integer> {
		CommandResult checkResult;
		CommandResult updateResult;
		
		@Override
		protected void onPreExecute() {
			pBarcheck = new Dialog(MemberInfo.this, R.style.dialog);
			// dialog.setTitle("Indeterminate");
			pBarcheck.setContentView(R.layout.custom_progress);
			pBarcheck.setCancelable(true);
			pBarcheck.show();
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				if (member_info_edt_idcardnum.isEnabled()) {
					checkResult = User.checkIdCardNum(member_info_edt_idcardnum
							.getText().toString().trim());
					if(!checkResult.getResult())
						return 3;
				}
				if(member_info_edt_mobile.isEnabled()) {
					checkResult = User.checkMobile(member_info_edt_mobile
							.getText().toString().trim());
					if(!checkResult.getResult())
						return 3;
				}
				updateResult = UserInfo
						.getUserInfoInstance()
						.updateUserInfo( MemberInfo.this,
								member_info_edt_membername.getText().toString().trim(),
								member_info_txv_sex.getText().toString() == "��" ? "1"
										: (member_info_txv_sex.getText()
												.toString() == "Ů" ? "0" : "9"),
								member_info_edt_idcardnum.isEnabled() ? member_info_edt_idcardnum
										.getText().toString().trim() : idCardNum,
								// "��ѡ�����ĳ�������"
								member_info_txv_birthday
										.getText()
										.toString()
										.equals(MemberInfo.this
												.getResources()
												.getString(
														R.string.txt_info_txv_birthday)) ? ""
										: member_info_txv_birthday.getText()
												.toString(),
								member_info_edt_mobile.getText().toString().trim(),
								member_info_edt_telephone.getText().toString().trim(),
								member_info_edt_email.getText().toString().trim(),
								member_info_edt_familyaddress.getText()
										.toString(),
								// ��ѡ���Ļ��̶�
								member_info_txv_educationlevel
										.getText()
										.toString()
										.equals(MemberInfo.this
												.getResources()
												.getString(
														R.string.txt_info_txv_educationlevel)) ? ""
										: member_info_txv_educationlevel
												.getText().toString(),
								// "��ѡ��ְҵ"
								member_info_txv_job
										.getText()
										.toString()
										.equals(MemberInfo.this
												.getResources()
												.getString(
														R.string.txt_info_txv_job)) ? ""
										: member_info_txv_job.getText()
												.toString(),
								// ��ѡ��ҽ�����
								member_info_txv_hc
										.getText()
										.toString()
										.equals(MemberInfo.this
												.getResources()
												.getString(
														R.string.txt_info_txv_hc)) ? ""
										: member_info_txv_hc.getText()
												.toString(),
								member_info_edt_income.getText().toString(),
								// ��ѡ����õĹ���;��
								member_info_txv_buyway
										.getText()
										.toString()
										.equals(MemberInfo.this
												.getResources()
												.getString(
														R.string.txt_info_txv_buyway)) ? ""
										: member_info_txv_buyway.getText()
												.toString(),
								member_info_edt_commonhospital.getText()
										.toString(), memberID + "");
				// ����ɹ�
				if (updateResult.getResult()) {
					return 1;
				} else {
					return 0;
				}
			} catch (Exception e) {//�������
				return 2;
			}
		}
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if (result == 1) {
				memberName = member_info_edt_membername.getText().toString();
				pBarcheck.cancel();
				Toast.makeText(MemberInfo.this, "�����Ա��Ϣ�ɹ�", Toast.LENGTH_LONG).show();
				Intent dataIntent = new Intent().setClass(MemberInfo.this,
						Member.class);
				dataIntent.putExtra("membername", memberName);
				MemberInfo.this.setResult(RESULT_OK, dataIntent);
				try {
					int addscore = Integer.parseInt(updateResult
							.getValue("addscore"));
					dataIntent.putExtra("addscore", addscore);
				} catch (Exception e) {
					MemberInfo.this.setResult(RESULT_OK, dataIntent);
				}
				MemberInfo.this.finish();
			} else if (result == 0) {
				pBarcheck.cancel();
				Toast.makeText(MemberInfo.this, "����ʱ���ִ���", Toast.LENGTH_LONG).show();
			} else if (result == 3) {
				pBarcheck.cancel();
				Toast.makeText(MemberInfo.this, checkResult.getMessage(), Toast.LENGTH_LONG).show();
			} else {
				pBarcheck.cancel();
				Toast.makeText(MemberInfo.this, "ϵͳ���ֶδ���", Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) { 
			// ����resultCode����ͬ����·��ص�ֵ
			String resultKey = data.getExtras().getString("resultkey");
			Object resultObject = TempDataPool.getSelectorResult(resultKey);
			switch (requestCode) {
			case Fields.Sex:				
				// ͨ��ѡ�������ص�ΨһKey����ʱ���ݳػ�ȡ��Ӧ��������				
				if (resultObject != null) {
					Map<String, String> resultMap = (Map<String, String>) resultObject;
					String resultString = resultMap.get("text");
					CommonMethod.setSelectFieldText(member_info_txv_sex, resultString);
					member_info_txv_sex.setText(resultString);
					//diseaseInfoMap.put("diseasename", resultString);
				}
				// ����������ʹ��������ʱ���ݳ�����
				TempDataPool.destroySelectorResult(resultKey);
				break;
			case Fields.EducationLevel:
				// ͨ��ѡ�������ص�ΨһKey����ʱ���ݳػ�ȡ��Ӧ��������				
				if (resultObject != null) {
					Map<String, String> resultMap = (Map<String, String>) resultObject;
					String resultString = resultMap.get("text");
					CommonMethod.setSelectFieldText(member_info_txv_educationlevel, resultString);
					member_info_txv_educationlevel.setText(resultString);
					//diseaseInfoMap.put("diseasename", resultString);
				}
				// ����������ʹ��������ʱ���ݳ�����
				TempDataPool.destroySelectorResult(resultKey);
				break;
			case Fields.Job:
				if (resultObject != null) {
					Map<String, String> resultMap = (Map<String, String>) resultObject;
					String resultString = resultMap.get("text");
					CommonMethod.setSelectFieldText(member_info_txv_job, resultString);
					member_info_txv_job.setText(resultString);
					//diseaseInfoMap.put("diseasename", resultString);
				}
				// ����������ʹ��������ʱ���ݳ�����
				TempDataPool.destroySelectorResult(resultKey);
				break;
			case Fields.Hc:
				if (resultObject != null) {
					Map<String, String> resultMap = (Map<String, String>) resultObject;
					String resultString = resultMap.get("text");
					CommonMethod.setSelectFieldText(member_info_txv_hc, resultString);
					member_info_txv_hc.setText(resultString);
					//diseaseInfoMap.put("diseasename", resultString);
				}
				// ����������ʹ��������ʱ���ݳ�����
				TempDataPool.destroySelectorResult(resultKey);
				break;
			case Fields.BuyWay:
				if (resultObject != null) {
					Map<String, String> resultMap = (Map<String, String>) resultObject;
					String resultString = resultMap.get("text");
					CommonMethod.setSelectFieldText(member_info_txv_buyway, resultString);
					member_info_txv_buyway.setText(resultString);
					//diseaseInfoMap.put("diseasename", resultString);
				}
				// ����������ʹ��������ʱ���ݳ�����
				TempDataPool.destroySelectorResult(resultKey);
				break;	
			default:
				break;
			}
		} else if (resultCode == RESULT_CANCELED) { // �������ء�

		}
	}

	/**
	 * ���嵱ǰҳ���б�ʶ
	 * 
	 * @author habei
	 * 
	 */
	private class Fields {
		public static final int Sex = 1;
		public static final int EducationLevel = 2;
		public static final int Job = 3;
		public static final int Hc = 4;
		public static final int BuyWay = 5;
	}

	/**
	 * @Description: TODO ��������ʱ��ѡ����
	 */
	/*
	 * private void showDateTimePicker() { Calendar calendar =
	 * Calendar.getInstance(); int year = calendar.get(Calendar.YEAR); int month
	 * = calendar.get(Calendar.MONTH); int day = calendar.get(Calendar.DATE);
	 * int hour = calendar.get(Calendar.HOUR_OF_DAY); int minute =
	 * calendar.get(Calendar.MINUTE);
	 * 
	 * // ��Ӵ�С���·ݲ�����ת��Ϊlist,����֮����ж� String[] months_big = { "1", "3", "5", "7",
	 * "8", "10", "12" }; String[] months_little = { "4", "6", "9", "11" };
	 * 
	 * final List<String> list_big = Arrays.asList(months_big); final
	 * List<String> list_little = Arrays.asList(months_little);
	 * 
	 * datedialog = new Dialog(this); datedialog.setTitle("��ѡ������"); //
	 * �ҵ�dialog�Ĳ����ļ� LayoutInflater inflater = (LayoutInflater)
	 * getSystemService(LAYOUT_INFLATER_SERVICE); View dateview =
	 * inflater.inflate(R.layout.datepicker, null);
	 * 
	 * // �� final WheelView wv_year = (WheelView)
	 * dateview.findViewById(R.id.year); wv_year.setAdapter(new
	 * NumericWheelAdapter(START_YEAR, END_YEAR));// ����"��"����ʾ����
	 * wv_year.setCyclic(true);// ��ѭ������ wv_year.setLabel("��");// �������
	 * wv_year.setCurrentItem(year - START_YEAR);// ��ʼ��ʱ��ʾ������
	 * 
	 * // �� final WheelView wv_month = (WheelView)
	 * dateview.findViewById(R.id.month); wv_month.setAdapter(new
	 * NumericWheelAdapter(1, 12)); wv_month.setCyclic(true);
	 * wv_month.setLabel("��"); wv_month.setCurrentItem(month);
	 * 
	 * // �� final WheelView wv_day = (WheelView)
	 * dateview.findViewById(R.id.day); wv_day.setCyclic(true); //
	 * �жϴ�С�¼��Ƿ�����,����ȷ��"��"������ if (list_big.contains(String.valueOf(month + 1))) {
	 * wv_day.setAdapter(new NumericWheelAdapter(1, 31)); } else if
	 * (list_little.contains(String.valueOf(month + 1))) { wv_day.setAdapter(new
	 * NumericWheelAdapter(1, 30)); } else { // ���� if ((year % 4 == 0 && year %
	 * 100 != 0) || year % 400 == 0) wv_day.setAdapter(new
	 * NumericWheelAdapter(1, 29)); else wv_day.setAdapter(new
	 * NumericWheelAdapter(1, 28)); } wv_day.setLabel("��");
	 * wv_day.setCurrentItem(day - 1);
	 * 
	 * // ʱ final WheelView wv_hours = (WheelView) view.findViewById(R.id.hour);
	 * wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
	 * wv_hours.setCyclic(true); wv_hours.setCurrentItem(hour);
	 * 
	 * // �� final WheelView wv_mins = (WheelView) view.findViewById(R.id.mins);
	 * wv_mins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
	 * wv_mins.setCyclic(true); wv_mins.setCurrentItem(minute);
	 * 
	 * // ���"��"���� OnWheelChangedListener wheelListener_year = new
	 * OnWheelChangedListener() { public void onChanged(WheelView wheel, int
	 * oldValue, int newValue) { int year_num = newValue + START_YEAR; //
	 * �жϴ�С�¼��Ƿ�����,����ȷ��"��"������ if (list_big.contains(String
	 * .valueOf(wv_month.getCurrentItem() + 1))) { wv_day.setAdapter(new
	 * NumericWheelAdapter(1, 31)); } else if
	 * (list_little.contains(String.valueOf(wv_month .getCurrentItem() + 1))) {
	 * wv_day.setAdapter(new NumericWheelAdapter(1, 30)); } else { if ((year_num
	 * % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0)
	 * wv_day.setAdapter(new NumericWheelAdapter(1, 29)); else
	 * wv_day.setAdapter(new NumericWheelAdapter(1, 28)); } } }; // ���"��"����
	 * OnWheelChangedListener wheelListener_month = new OnWheelChangedListener()
	 * { public void onChanged(WheelView wheel, int oldValue, int newValue) {
	 * int month_num = newValue + 1; // �жϴ�С�¼��Ƿ�����,����ȷ��"��"������ if
	 * (list_big.contains(String.valueOf(month_num))) { wv_day.setAdapter(new
	 * NumericWheelAdapter(1, 31)); } else if
	 * (list_little.contains(String.valueOf(month_num))) { wv_day.setAdapter(new
	 * NumericWheelAdapter(1, 30)); } else { if (((wv_year.getCurrentItem() +
	 * START_YEAR) % 4 == 0 && (wv_year .getCurrentItem() + START_YEAR) % 100 !=
	 * 0) || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
	 * wv_day.setAdapter(new NumericWheelAdapter(1, 29)); else
	 * wv_day.setAdapter(new NumericWheelAdapter(1, 28)); } } };
	 * wv_year.addChangingListener(wheelListener_year);
	 * wv_month.addChangingListener(wheelListener_month);
	 * 
	 * // ������Ļ�ܶ���ָ��ѡ��������Ĵ�С int textSize = 0;
	 * 
	 * textSize = 12;
	 * 
	 * wv_day.TEXT_SIZE = textSize; // wv_hours.TEXT_SIZE = textSize; //
	 * wv_mins.TEXT_SIZE = textSize; wv_month.TEXT_SIZE = textSize;
	 * wv_year.TEXT_SIZE = textSize;
	 * 
	 * Button btn_sure = (Button) dateview.findViewById(R.id.btn_datetime_sure);
	 * Button btn_cancel = (Button) dateview
	 * .findViewById(R.id.btn_datetime_cancel); // ȷ��
	 * btn_sure.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View arg0) { // TODO Auto-generated method
	 * stub // ����Ǹ���,����ʾΪ"02"����ʽ String parten = "00"; DecimalFormat decimal =
	 * new DecimalFormat(parten); // �������ڵ���ʾ //
	 * tv_text.setText((wv_year.getCurrentItem() + START_YEAR) + "-" String
	 * time= wv_year.getCurrentItem() + START_YEAR + "-" +
	 * decimal.format((wv_month.getCurrentItem() + 1)) + "-" +
	 * decimal.format((wv_day.getCurrentItem() + 1)) + " " // +
	 * decimal.format(wv_hours.getCurrentItem()) + ":" // +
	 * decimal.format(wv_mins.getCurrentItem()) ;
	 * Toast.makeText(MemberInfo.this, time+"", 1).show(); //�����渳ֵ
	 * member_info_txv_birthday.setText(time); datedialog.dismiss(); } }); // ȡ��
	 * btn_cancel.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View arg0) { // TODO Auto-generated method
	 * stub datedialog.dismiss(); } }); // ����dialog�Ĳ���,����ʾ
	 * datedialog.setContentView(dateview); datedialog.show(); }
	 */
}
