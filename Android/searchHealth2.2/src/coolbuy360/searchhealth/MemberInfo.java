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
 * @author yangxc 个人资料编辑页面
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
		// 为退出做准备
		searchApp.getInstance().addActivity(this);

		// 设置返回按钮
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MemberInfo.this.finish();
			}
		});
		// 获取上面传过来的值
		// 获取界面控件
		// 输入类
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
		// 选择类
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

		// 选择类点击区域
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
		
		// 注册点击效果
		member_info_item_sex
				.setOnTouchListener(new CommonMethod.setOnPressed());
		member_info_item_birthday
				.setOnTouchListener(new CommonMethod.setOnPressed());
		member_info_item_educationlevel.setOnTouchListener(new CommonMethod.setOnPressed());
		member_info_item_hc.setOnTouchListener(new CommonMethod.setOnPressed());
		member_info_item_job.setOnTouchListener(new CommonMethod.setOnPressed());
		member_info_item_buyway.setOnTouchListener(new CommonMethod.setOnPressed());
		//验证输入框里的格式的监听事件
		VaridateEdittxt();
		
		// “选择性别”点击
		member_info_item_sex.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				CommonMethod.clearFocuse(MemberInfo.this);
				Intent intent = new Intent().setClass(MemberInfo.this,
						SHValueSelector.class);
				// 实例化列表数据源的适配器
				NomalSelectorAdapter adapter = new NomalSelectorAdapter(
						MemberInfo.this, CommonMethod.getSexNamesSource());
				// 将适配器托管到临时数据池
				String adapterkey = TempDataPool.putAdapter(adapter);
				// 传递适配器的唯一Key到选择器页面
				intent.putExtra("adapterkey", adapterkey);
				intent.putExtra("title", "您的性别");
				// 设置requestCode为对应的字段
				startActivityForResult(intent, Fields.Sex);

			}
		});
		
		// “选择出生日期”点击
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
				if(!dateStr.equals("请选择您的出生日期"))
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
				datedialogtest.setTitle("请选择您的出生日期");
				datedialogtest.show();
				datedialogtest.setOkListener("确定", new OnClickListener() {
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
		
		//选择“教育水平”点击
		member_info_item_educationlevel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				CommonMethod.clearFocuse(MemberInfo.this);
				Intent intent = new Intent().setClass(MemberInfo.this,
						SHValueSelector.class);
				// 实例化列表数据源的适配器
				NomalSelectorAdapter adapter = new NomalSelectorAdapter(
						MemberInfo.this, CommonMethod.getEducationLevelSource());
				// 将适配器托管到临时数据池
				String adapterkey = TempDataPool.putAdapter(adapter);
				// 传递适配器的唯一Key到选择器页面
				intent.putExtra("adapterkey", adapterkey);
				intent.putExtra("title", "选择文化程度");
				// 设置requestCode为对应的字段
				startActivityForResult(intent, Fields.EducationLevel);
			}
		});
		
		//选择“医保情况”点击
		member_info_item_hc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				CommonMethod.clearFocuse(MemberInfo.this);
				Intent intent = new Intent().setClass(MemberInfo.this,
						SHValueSelector.class);
				// 实例化列表数据源的适配器
				NomalSelectorAdapter adapter = new NomalSelectorAdapter(
						MemberInfo.this, CommonMethod.getHcSource());
				// 将适配器托管到临时数据池
				String adapterkey = TempDataPool.putAdapter(adapter);
				// 传递适配器的唯一Key到选择器页面
				intent.putExtra("adapterkey", adapterkey);
				intent.putExtra("title", "请选择医保情况");
				// 设置requestCode为对应的字段
				startActivityForResult(intent, Fields.Hc);
			}
		});
		
		//选择“工作情况”点击
		member_info_item_job.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				CommonMethod.clearFocuse(MemberInfo.this);
				Intent intent = new Intent().setClass(MemberInfo.this,
						SHValueSelector.class);
				// 实例化列表数据源的适配器
				NomalSelectorAdapter adapter = new NomalSelectorAdapter(
						MemberInfo.this, CommonMethod.getJobNamesSource());
				// 将适配器托管到临时数据池
				String adapterkey = TempDataPool.putAdapter(adapter);
				// 传递适配器的唯一Key到选择器页面
				intent.putExtra("adapterkey", adapterkey);
				intent.putExtra("title", "选择您的职业");
				// 设置requestCode为对应的字段
				startActivityForResult(intent, Fields.Job);
			}
		});
    	//选择“购买途径”点击
		member_info_item_buyway.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				CommonMethod.clearFocuse(MemberInfo.this);
				Intent intent = new Intent().setClass(MemberInfo.this,
						SHValueSelector.class);
				// 实例化列表数据源的适配器
				NomalSelectorAdapter adapter = new NomalSelectorAdapter(
						MemberInfo.this, CommonMethod.getBuyWaySource());
				// 将适配器托管到临时数据池
				String adapterkey = TempDataPool.putAdapter(adapter);
				// 传递适配器的唯一Key到选择器页面
				intent.putExtra("adapterkey", adapterkey);
				intent.putExtra("title", "选择购药途径");
				// 设置requestCode为对应的字段
				startActivityForResult(intent, Fields.BuyWay);
			}
		});	
		
				
		Bundle bundle = getIntent().getExtras();
		memberID = bundle.getString("memberid");
		Log.i("chenlinwei", "加载:" + memberID);
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
	 * 验证重要数据的完整性
	 */
	private void VaridateEdittxt()
	{
		//身份证号码验证
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
						Log.i("chenlinwei", input.length()+"身份证号输入框的长度");
						if(!Validator.getValidatorInstance(MemberInfo.this).isIDCardNum(input))
						{
							idcardNumValidate=false;							
							
							dt.setError(changeErrorColor("请输入正确的身份证号"));						
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
		
		//手机号码验证
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
							et.setError(changeErrorColor("请输入正确的手机号码"));	
							//<font color='blue'>出错</font>
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
		
		//电话号码的验证
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
							et.setError(changeErrorColor("电话号码格式：0571-86199111"));							
						}
						else
							telephoneValidate=true;						
					}
					else
						telephoneValidate=true;
				}
			}
		});
		
		//邮箱的验证
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
							et.setError(changeErrorColor("请输入正确的电子邮箱"));							
						}
						else
							emailValidate=true;							
					}
					else
						emailValidate=true;
				}
			}
		});
		
		//家庭住址的验证
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
							et.setError(changeErrorColor("家庭住址不能超过100个字"));							
						}
						else
							addressValidate=true;						
					}
					else
						addressValidate=true;
				}
			}
		});
		
		//姓名的验证
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
							et.setError(changeErrorColor("姓名长度不能超过20个字"));	
						}						
						else
							membernameValidate=true;
					}
					else
						membernameValidate=true;
				}
			}
		});
		
		//常去医院名的验证
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
							et.setError(changeErrorColor("医院名称不能超过20个字"));
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
	 * 异步加载用户数据
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
						Log.i("chenlinwei", "加载list数据集返回值:0");
						return 0;
					} else {
						Log.i("chenlinwei", "加载list数据集返回值:1");
						return 1;
					}
				} else {
					return 2;
				}

			} catch (Exception e) {
				Log.i("chenlinwei", "加载list数据集返回值:2");
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
				Toast.makeText(MemberInfo.this, "加载数据出错", Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}
	}

	// 初始化数据显示
	private void InitViewData(Map<String, String> memberInfoMap) {
		if (memberInfoMap.size() > 0) {
			CommonMethod.setSelectFieldText(member_info_txv_username,
					memberInfoMap.get("username"));
			memberName = memberInfoMap.get("username");
			//Log.i("chenlinwei", "用户名:" + memberInfoMap.get("username"));
			//Log.i("chenlinwei", "性别:" + memberInfoMap.get("sex"));
			String sex = memberInfoMap.get("sex").equals("1") ? "男"
					: (memberInfoMap.get("sex").equals("0") ? "女" : "请选择性别");
			CommonMethod.setSelectFieldText(member_info_txv_sex, sex);

			// Log.i("chenlinwei", "会员姓名:" + memberInfoMap.get("membername"));
			if (memberInfoMap.get("membername").equals("")
					|| memberInfoMap.get("membername") == null) {
			} else {
				member_info_edt_membername.setText(memberInfoMap
						.get("membername"));
			}
			if (memberInfoMap.get("idcardnum").equals("")
					|| memberInfoMap.get("idcardnum") == null) {
				Log.i("chenlinwei", "身份证号:" + memberInfoMap.get("idcardnum"));
			} else {
				idCardNum = memberInfoMap.get("idcardnum");
				// 身份证号掩码显示
				String xidCardNum = idCardNum.substring(0, idCardNum.length()-4) + "**" + idCardNum.substring(idCardNum.length()-2);
				member_info_edt_idcardnum.setText(xidCardNum);
				member_info_edt_idcardnum.setEnabled(false);
			}
			
			// Log.i("chenlinwei", "生日:" + memberInfoMap.get("birthday"));
			String birthday = memberInfoMap.get("birthday");
			if (birthday == null || birthday.equals("")
					|| birthday.contains("1753")) {
			} else {
				CommonMethod.setSelectFieldText(member_info_txv_birthday,
						Util.getDateFormat(birthday, "yyyy-MM-dd"));
			}

			// Log.i("chenlinwei", "手机号:" + memberInfoMap.get("mobile"));
			if (memberInfoMap.get("mobile").equals("")) {
			} else {
				member_info_edt_mobile.setText(memberInfoMap.get("mobile"));
				member_info_edt_mobile.setEnabled(false);
			}

			// Log.i("chenlinwei", "电话:" + memberInfoMap.get("telephone"));
			if (memberInfoMap.get("telephone").equals("")) {
			} else {
				member_info_edt_telephone.setText(memberInfoMap
						.get("telephone"));
			}

			// Log.i("chenlinwei", "邮箱:" + memberInfoMap.get("email"));
			if (memberInfoMap.get("email").equals("")) {
			} else {
				member_info_edt_email.setText(memberInfoMap.get("email"));
			}
			// Log.i("chenlinwei", "家庭住址:" +
			// memberInfoMap.get("familyaddress"));
			if (memberInfoMap.get("familyaddress").equals("")) {
			} else {
				member_info_edt_familyaddress.setText(memberInfoMap
						.get("familyaddress"));
			}

			// Log.i("chenlinwei", "文化程度:" +
			// memberInfoMap.get("educationlevel"));
			if (memberInfoMap.get("educationlevel").equals("")) {

			} else {
				CommonMethod.setSelectFieldText(member_info_txv_educationlevel,
						memberInfoMap.get("educationlevel"));
			}
			// member_info_txv_educationlevel.setText(memberInfoMap.get("educationlevel").equals("")
			// ? "请选择文化程度" : memberInfoMap.get("educationlevel"));
			// Log.i("chenlinwei", "文化程度:" + memberInfoMap.get("job"));
			// 职业
			if (memberInfoMap.get("job").equals("")) {
			} else {
				CommonMethod.setSelectFieldText(member_info_txv_job,
						memberInfoMap.get("job"));
			}
			// Log.i("chenlinwei", "医保:" + memberInfoMap.get("hc"));
			if (memberInfoMap.get("hc").equals("")) {
			} else {
				CommonMethod.setSelectFieldText(member_info_txv_hc,
						memberInfoMap.get("hc"));
			}
			// Log.i("chenlinwei", "支出:" + memberInfoMap.get("income"));
			// member_info_edt_income.setText(memberInfoMap.get("income"));
			if (memberInfoMap.get("income").equals("")) {
			} else {
				member_info_edt_income.setText(memberInfoMap.get("income"));
			}
			// Log.i("chenlinwei", "购药途径:" + memberInfoMap.get("buyway"));
			// /member_info_txv_buyway.setText(memberInfoMap.get("buyway").equals("")
			// ? "请选择购药途径"
			// : memberInfoMap.get("buyway"));
			if (memberInfoMap.get("buyway").equals("")) {
			} else {
				CommonMethod.setSelectFieldText(member_info_txv_buyway,
						memberInfoMap.get("buyway"));
			}

			// Log.i("chenlinwei", "常去医院:" +
			// memberInfoMap.get("commonhospital"));
			if (memberInfoMap.get("commonhospital").equals("")) {
			} else {
				member_info_edt_commonhospital.setText(memberInfoMap
						.get("commonhospital"));
			}
		}
	}

	// 保存按钮点击事件
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
				Toast.makeText(MemberInfo.this, "请检查错误项", Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * 更新信息
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
								member_info_txv_sex.getText().toString() == "男" ? "1"
										: (member_info_txv_sex.getText()
												.toString() == "女" ? "0" : "9"),
								member_info_edt_idcardnum.isEnabled() ? member_info_edt_idcardnum
										.getText().toString().trim() : idCardNum,
								// "请选择您的出生日期"
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
								// 请选择文化程度
								member_info_txv_educationlevel
										.getText()
										.toString()
										.equals(MemberInfo.this
												.getResources()
												.getString(
														R.string.txt_info_txv_educationlevel)) ? ""
										: member_info_txv_educationlevel
												.getText().toString(),
								// "请选择职业"
								member_info_txv_job
										.getText()
										.toString()
										.equals(MemberInfo.this
												.getResources()
												.getString(
														R.string.txt_info_txv_job)) ? ""
										: member_info_txv_job.getText()
												.toString(),
								// 请选择医保情况
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
								// 请选择最常用的购买途径
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
				// 如果成功
				if (updateResult.getResult()) {
					return 1;
				} else {
					return 0;
				}
			} catch (Exception e) {//如果出错
				return 2;
			}
		}
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if (result == 1) {
				memberName = member_info_edt_membername.getText().toString();
				pBarcheck.cancel();
				Toast.makeText(MemberInfo.this, "保存会员信息成功", Toast.LENGTH_LONG).show();
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
				Toast.makeText(MemberInfo.this, "保存时出现错误", Toast.LENGTH_LONG).show();
			} else if (result == 3) {
				pBarcheck.cancel();
				Toast.makeText(MemberInfo.this, checkResult.getMessage(), Toast.LENGTH_LONG).show();
			} else {
				pBarcheck.cancel();
				Toast.makeText(MemberInfo.this, "系统或字段错误", Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) { 
			// 根据resultCode处理不同情况下返回的值
			String resultKey = data.getExtras().getString("resultkey");
			Object resultObject = TempDataPool.getSelectorResult(resultKey);
			switch (requestCode) {
			case Fields.Sex:				
				// 通过选择器返回的唯一Key从临时数据池获取对应的数据项				
				if (resultObject != null) {
					Map<String, String> resultMap = (Map<String, String>) resultObject;
					String resultString = resultMap.get("text");
					CommonMethod.setSelectFieldText(member_info_txv_sex, resultString);
					member_info_txv_sex.setText(resultString);
					//diseaseInfoMap.put("diseasename", resultString);
				}
				// 返回数据项使用完后从临时数据池销毁
				TempDataPool.destroySelectorResult(resultKey);
				break;
			case Fields.EducationLevel:
				// 通过选择器返回的唯一Key从临时数据池获取对应的数据项				
				if (resultObject != null) {
					Map<String, String> resultMap = (Map<String, String>) resultObject;
					String resultString = resultMap.get("text");
					CommonMethod.setSelectFieldText(member_info_txv_educationlevel, resultString);
					member_info_txv_educationlevel.setText(resultString);
					//diseaseInfoMap.put("diseasename", resultString);
				}
				// 返回数据项使用完后从临时数据池销毁
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
				// 返回数据项使用完后从临时数据池销毁
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
				// 返回数据项使用完后从临时数据池销毁
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
				// 返回数据项使用完后从临时数据池销毁
				TempDataPool.destroySelectorResult(resultKey);
				break;	
			default:
				break;
			}
		} else if (resultCode == RESULT_CANCELED) { // 物理“返回”

		}
	}

	/**
	 * 定义当前页面中标识
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
	 * @Description: TODO 弹出日期时间选择器
	 */
	/*
	 * private void showDateTimePicker() { Calendar calendar =
	 * Calendar.getInstance(); int year = calendar.get(Calendar.YEAR); int month
	 * = calendar.get(Calendar.MONTH); int day = calendar.get(Calendar.DATE);
	 * int hour = calendar.get(Calendar.HOUR_OF_DAY); int minute =
	 * calendar.get(Calendar.MINUTE);
	 * 
	 * // 添加大小月月份并将其转换为list,方便之后的判断 String[] months_big = { "1", "3", "5", "7",
	 * "8", "10", "12" }; String[] months_little = { "4", "6", "9", "11" };
	 * 
	 * final List<String> list_big = Arrays.asList(months_big); final
	 * List<String> list_little = Arrays.asList(months_little);
	 * 
	 * datedialog = new Dialog(this); datedialog.setTitle("请选择日期"); //
	 * 找到dialog的布局文件 LayoutInflater inflater = (LayoutInflater)
	 * getSystemService(LAYOUT_INFLATER_SERVICE); View dateview =
	 * inflater.inflate(R.layout.datepicker, null);
	 * 
	 * // 年 final WheelView wv_year = (WheelView)
	 * dateview.findViewById(R.id.year); wv_year.setAdapter(new
	 * NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
	 * wv_year.setCyclic(true);// 可循环滚动 wv_year.setLabel("年");// 添加文字
	 * wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
	 * 
	 * // 月 final WheelView wv_month = (WheelView)
	 * dateview.findViewById(R.id.month); wv_month.setAdapter(new
	 * NumericWheelAdapter(1, 12)); wv_month.setCyclic(true);
	 * wv_month.setLabel("月"); wv_month.setCurrentItem(month);
	 * 
	 * // 日 final WheelView wv_day = (WheelView)
	 * dateview.findViewById(R.id.day); wv_day.setCyclic(true); //
	 * 判断大小月及是否闰年,用来确定"日"的数据 if (list_big.contains(String.valueOf(month + 1))) {
	 * wv_day.setAdapter(new NumericWheelAdapter(1, 31)); } else if
	 * (list_little.contains(String.valueOf(month + 1))) { wv_day.setAdapter(new
	 * NumericWheelAdapter(1, 30)); } else { // 闰年 if ((year % 4 == 0 && year %
	 * 100 != 0) || year % 400 == 0) wv_day.setAdapter(new
	 * NumericWheelAdapter(1, 29)); else wv_day.setAdapter(new
	 * NumericWheelAdapter(1, 28)); } wv_day.setLabel("日");
	 * wv_day.setCurrentItem(day - 1);
	 * 
	 * // 时 final WheelView wv_hours = (WheelView) view.findViewById(R.id.hour);
	 * wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
	 * wv_hours.setCyclic(true); wv_hours.setCurrentItem(hour);
	 * 
	 * // 分 final WheelView wv_mins = (WheelView) view.findViewById(R.id.mins);
	 * wv_mins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
	 * wv_mins.setCyclic(true); wv_mins.setCurrentItem(minute);
	 * 
	 * // 添加"年"监听 OnWheelChangedListener wheelListener_year = new
	 * OnWheelChangedListener() { public void onChanged(WheelView wheel, int
	 * oldValue, int newValue) { int year_num = newValue + START_YEAR; //
	 * 判断大小月及是否闰年,用来确定"日"的数据 if (list_big.contains(String
	 * .valueOf(wv_month.getCurrentItem() + 1))) { wv_day.setAdapter(new
	 * NumericWheelAdapter(1, 31)); } else if
	 * (list_little.contains(String.valueOf(wv_month .getCurrentItem() + 1))) {
	 * wv_day.setAdapter(new NumericWheelAdapter(1, 30)); } else { if ((year_num
	 * % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0)
	 * wv_day.setAdapter(new NumericWheelAdapter(1, 29)); else
	 * wv_day.setAdapter(new NumericWheelAdapter(1, 28)); } } }; // 添加"月"监听
	 * OnWheelChangedListener wheelListener_month = new OnWheelChangedListener()
	 * { public void onChanged(WheelView wheel, int oldValue, int newValue) {
	 * int month_num = newValue + 1; // 判断大小月及是否闰年,用来确定"日"的数据 if
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
	 * // 根据屏幕密度来指定选择器字体的大小 int textSize = 0;
	 * 
	 * textSize = 12;
	 * 
	 * wv_day.TEXT_SIZE = textSize; // wv_hours.TEXT_SIZE = textSize; //
	 * wv_mins.TEXT_SIZE = textSize; wv_month.TEXT_SIZE = textSize;
	 * wv_year.TEXT_SIZE = textSize;
	 * 
	 * Button btn_sure = (Button) dateview.findViewById(R.id.btn_datetime_sure);
	 * Button btn_cancel = (Button) dateview
	 * .findViewById(R.id.btn_datetime_cancel); // 确定
	 * btn_sure.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View arg0) { // TODO Auto-generated method
	 * stub // 如果是个数,则显示为"02"的样式 String parten = "00"; DecimalFormat decimal =
	 * new DecimalFormat(parten); // 设置日期的显示 //
	 * tv_text.setText((wv_year.getCurrentItem() + START_YEAR) + "-" String
	 * time= wv_year.getCurrentItem() + START_YEAR + "-" +
	 * decimal.format((wv_month.getCurrentItem() + 1)) + "-" +
	 * decimal.format((wv_day.getCurrentItem() + 1)) + " " // +
	 * decimal.format(wv_hours.getCurrentItem()) + ":" // +
	 * decimal.format(wv_mins.getCurrentItem()) ;
	 * Toast.makeText(MemberInfo.this, time+"", 1).show(); //给界面赋值
	 * member_info_txv_birthday.setText(time); datedialog.dismiss(); } }); // 取消
	 * btn_cancel.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View arg0) { // TODO Auto-generated method
	 * stub datedialog.dismiss(); } }); // 设置dialog的布局,并显示
	 * datedialog.setContentView(dateview); datedialog.show(); }
	 */
}
