package coolbuy360.searchhealth;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import coolbuy360.logic.User;
import coolbuy360.service.CommandResult;
import coolbuy360.service.PasswordDigitsKey;
import coolbuy360.service.Validator;
import coolbuy360.service.searchApp;

public class MemberReg extends Activity {
	
	EditText member_reg_edit_user;
	EditText member_reg_edit_email;
	EditText member_reg_edit_pwd;
	EditText member_reg_edit_confirmpwd;
	Button member_reg_btn_ok;
	Button member_reg_btn_reset;
	Dialog pBarcheck;

	boolean userMobileValidate = true;
	boolean emailValidate = true;
	boolean pwdValidate = true;
	boolean isEqualsPwd = false;

	String memberUserString = "";
	String memberEmailString = "";
	String memberPwdString = "";
	String memberPwdConfirmString = "";

	
//  InputMethodManager  manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_reg);
		// 为退出做准备
		searchApp.getInstance().addActivity(this);

		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);// 返回按钮
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MemberReg.this.finish();
			}
		});
		member_reg_edit_user=(EditText)this.findViewById(R.id.member_reg_edit_user);
		member_reg_edit_email=(EditText)this.findViewById(R.id.member_reg_edit_confiremail);
		member_reg_edit_pwd =(EditText)this.findViewById(R.id.member_reg_edit_pwd);
		member_reg_edit_confirmpwd=(EditText)this.findViewById(R.id.member_reg_edit_confirmpwd);
		member_reg_btn_ok=(Button)this.findViewById(R.id.member_reg_btn_ok);
		member_reg_btn_reset=(Button)this.findViewById(R.id.member_reg_btn_reset);
		member_reg_btn_ok.setOnClickListener(new regBtnOnClick());
		member_reg_btn_reset.setOnClickListener(new resetBtnOnClick());

		// 设置密码框的字符限制
		member_reg_edit_pwd.setKeyListener(new PasswordDigitsKey());
		member_reg_edit_confirmpwd.setKeyListener(new PasswordDigitsKey());
		
		member_reg_edit_user.setOnFocusChangeListener(new OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				EditText et=(EditText)v;
				String input=et.getText().toString();
				if(!hasFocus)
				{
					if(input.length()!=0&&!input.equals(""))
					{
						if(Validator.getValidatorInstance(MemberReg.this).isMobileNO(input))
						{
								
							   userMobileValidate=true;
							   memberUserString=input;
						}
						else
						{
							userMobileValidate=false;
							et.setError(changeErrorColor("请输入正确的手机号"));	
						}
					}
					else
					{
						userMobileValidate=false;
						et.setError(changeErrorColor("请输入您的登录手机号"));	
					}
				}
			}
		});
		//邮箱的验证
		member_reg_edit_email.setOnFocusChangeListener(new OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				EditText et=(EditText)v;
				String input=et.getText().toString();
				if(!hasFocus)
				{
					if(input.length()!=0&&!input.equals(""))
					{
						if(Validator.getValidatorInstance(MemberReg.this).isEmail(input))
						{
							emailValidate=true;		
						    memberEmailString=input;
						    
						   if( !memberPwdConfirmString.equals("")&&input.equals(memberPwdConfirmString))
						   {
							  isEqualsPwd=true;
						   }
												
						}
						else
						{							
							emailValidate=false;
							et.setError(changeErrorColor("请输入正确的邮箱地址"));	  
						    
						}
					}
					else
					{
						emailValidate=false;
						et.setError(changeErrorColor("请输入您的常用邮箱，以便于找回密码"));	
					}
				}
			}
		});
		//原密码验证
		member_reg_edit_pwd.setOnFocusChangeListener(new OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub				
				EditText et=(EditText)v;
				String input=et.getText().toString();
				if(!hasFocus)
				{
					if(input.length()!=0&&!input.equals(""))
					{//&&Validator.getValidatorInstance(MemberReg.this).isContent(input)
						if(Validator.getValidatorInstance(MemberReg.this).contentLengthCheck(input,6,12))
						{
							pwdValidate=true;	
							memberPwdString=input;
														
						}
						else
						{
							pwdValidate=false;
							et.setError(changeErrorColor("密码长度为6~12位的字母、数字或下划线"));
						}
					}
					else
					{
						pwdValidate=false;
						et.setError(changeErrorColor("请输入登录密码,长度为6~12位的字母、数字或下划线"));	
					}
				}
			}
		});
		member_reg_edit_confirmpwd.setOnFocusChangeListener(new OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				EditText et=(EditText)v;
				String input=et.getText().toString();
				
				 String pwdString= member_reg_edit_pwd.getText().toString();
				 if(!hasFocus)
				 {
						if(input.equals(pwdString))
						{	
							isEqualsPwd=true;
							memberPwdConfirmString=input;
							
						}
						else {
							isEqualsPwd=false;
							et.setError(changeErrorColor("两次密码不同"));
						}
				 }				
			}
		});		
	}
	
	public  class regBtnOnClick implements OnClickListener  {
		@Override
		public void onClick(View v) {
			member_reg_edit_user.clearFocus();
			member_reg_edit_email.clearFocus();
			member_reg_edit_pwd.clearFocus();
			member_reg_edit_confirmpwd.clearFocus();
			// TODO Auto-generated method stub
			//Toast.makeText(getApplicationContext(), "确认注册", 1).show();
			if(memberUserString.equals(""))
			{
				member_reg_edit_user.setError(changeErrorColor("手机号不能为空"));
				userMobileValidate=false;
			}	
			if(memberEmailString.equals(""))
			{
				member_reg_edit_email.setError(changeErrorColor("邮箱不能为空"));
				emailValidate=false;
			}
			if(memberPwdString.equals(""))
			{
				member_reg_edit_pwd.setError(changeErrorColor("密码不能为空"));	
				pwdValidate=false;
				
			}
			if(memberPwdConfirmString.equals(""))
			{
				member_reg_edit_confirmpwd.setError(changeErrorColor("确认密码不能为空"));				
			}
			
			if(userMobileValidate&&emailValidate&&pwdValidate)
			{
				if(isEqualsPwd)
				{
					new asyRegMethod().execute();					
				}
				else {
					Toast.makeText(MemberReg.this, "两次密码不同", 1).show();
				}
			}
			else
				Toast.makeText(MemberReg.this, "请检查错误项", 1).show();	
		}		
	}	
	
	private class  asyRegMethod extends AsyncTask<String, Void, Integer>
	{			
		CommandResult checkResult = new CommandResult(false, "未知错误，注册失败。");	
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showLoading();
		}
		@Override
		protected Integer doInBackground(String... params) {				
			User user=new User(MemberReg.this);
			try {
				checkResult = user.register(memberUserString,
						memberEmailString, memberPwdString);
				if (checkResult.getResult()) {
					return 1;
				} else {
					return 0;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return 0;
			}
		}	
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pBarcheck.cancel();
			final CustomerAlertDialog customerAlertDialog=new CustomerAlertDialog(MemberReg.this, R.style.customerdialog,CustomerAlertDialog.DISPLAYTXT);
			//customerAlertDialog.setCanceledOnTouchOutside(true);	
			customerAlertDialog.show();
			if(result==1)
			{		
				customerAlertDialog.setMessage(checkResult.getMessage());	
				customerAlertDialog.setTitle("信息提示");
				customerAlertDialog.setCancelOnClick("确定", new OnClickListener() {					
					@Override
					public void onClick(View v) {
						searchApp.getInstance().exitObject("coolbuy360.searchhealth.MemberLogin");
						MemberReg.this.finish();
						customerAlertDialog.cancel();
						
					}
				});				
		   }
			else
			{			
				customerAlertDialog.show();
				customerAlertDialog.setMessage(checkResult.getMessage());
				customerAlertDialog.setTitle("信息提示");
				customerAlertDialog.setCancelOnClick("确定", new OnClickListener() {					
					@Override
					public void onClick(View v) {						
						customerAlertDialog.cancel();						
					}
				});	
			}	
		}
	}
	public  class resetBtnOnClick implements OnClickListener  {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub			
			member_reg_edit_user.setText("");
			member_reg_edit_pwd.setText("");
			member_reg_edit_confirmpwd.setText("");
			member_reg_edit_email.setText("");
			/*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemberReg.this);
			alertDialogBuilder.setMessage("错误信息");			
			alertDialogBuilder.setNegativeButton("取消",new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}				
			});
			alertDialogBuilder.show();*/			
			/*CustomerAlertDialog customerAlertDialog=new CustomerAlertDialog(MemberReg.this, R.style.dialog,CustomerAlertDialog.DISPLAYEDIT);
			customerAlertDialog.setCanceledOnTouchOutside(true);			
			customerAlertDialog.show();*/
			/*final CustomerAlertDialog customerAlertDialog=new CustomerAlertDialog(MemberReg.this, R.style.customerdialog,CustomerAlertDialog.DISPLAYEDIT);				
			customerAlertDialog.setCanceledOnTouchOutside(true);	
			customerAlertDialog.show();
			customerAlertDialog.setTitle("提示信息");
			customerAlertDialog.setMessage("地村枯井中是");
			customerAlertDialog.setCancelOnClick("取消",new OnClickListener() {				
				@Override
				public void onClick(View v) {					 				 
					customerAlertDialog.cancel();
				}
			});
			customerAlertDialog.setokOnClick("ok",new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(MemberReg.this, "这是ok", 1).show();					
				}
			});	*/
		}
		
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

	public void showLoading()
	{
		pBarcheck = new Dialog(MemberReg.this, R.style.dialog);
		// dialog.setTitle("Indeterminate");
		pBarcheck.setContentView(R.layout.custom_progress);
		pBarcheck.setCancelable(true);
		pBarcheck.show();
		
	}
	

}
