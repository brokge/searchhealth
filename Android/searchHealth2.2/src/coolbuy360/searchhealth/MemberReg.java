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
		// Ϊ�˳���׼��
		searchApp.getInstance().addActivity(this);

		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);// ���ذ�ť
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

		// �����������ַ�����
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
							et.setError(changeErrorColor("��������ȷ���ֻ���"));	
						}
					}
					else
					{
						userMobileValidate=false;
						et.setError(changeErrorColor("���������ĵ�¼�ֻ���"));	
					}
				}
			}
		});
		//�������֤
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
							et.setError(changeErrorColor("��������ȷ�������ַ"));	  
						    
						}
					}
					else
					{
						emailValidate=false;
						et.setError(changeErrorColor("���������ĳ������䣬�Ա����һ�����"));	
					}
				}
			}
		});
		//ԭ������֤
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
							et.setError(changeErrorColor("���볤��Ϊ6~12λ����ĸ�����ֻ��»���"));
						}
					}
					else
					{
						pwdValidate=false;
						et.setError(changeErrorColor("�������¼����,����Ϊ6~12λ����ĸ�����ֻ��»���"));	
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
							et.setError(changeErrorColor("�������벻ͬ"));
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
			//Toast.makeText(getApplicationContext(), "ȷ��ע��", 1).show();
			if(memberUserString.equals(""))
			{
				member_reg_edit_user.setError(changeErrorColor("�ֻ��Ų���Ϊ��"));
				userMobileValidate=false;
			}	
			if(memberEmailString.equals(""))
			{
				member_reg_edit_email.setError(changeErrorColor("���䲻��Ϊ��"));
				emailValidate=false;
			}
			if(memberPwdString.equals(""))
			{
				member_reg_edit_pwd.setError(changeErrorColor("���벻��Ϊ��"));	
				pwdValidate=false;
				
			}
			if(memberPwdConfirmString.equals(""))
			{
				member_reg_edit_confirmpwd.setError(changeErrorColor("ȷ�����벻��Ϊ��"));				
			}
			
			if(userMobileValidate&&emailValidate&&pwdValidate)
			{
				if(isEqualsPwd)
				{
					new asyRegMethod().execute();					
				}
				else {
					Toast.makeText(MemberReg.this, "�������벻ͬ", 1).show();
				}
			}
			else
				Toast.makeText(MemberReg.this, "���������", 1).show();	
		}		
	}	
	
	private class  asyRegMethod extends AsyncTask<String, Void, Integer>
	{			
		CommandResult checkResult = new CommandResult(false, "δ֪����ע��ʧ�ܡ�");	
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
				customerAlertDialog.setTitle("��Ϣ��ʾ");
				customerAlertDialog.setCancelOnClick("ȷ��", new OnClickListener() {					
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
				customerAlertDialog.setTitle("��Ϣ��ʾ");
				customerAlertDialog.setCancelOnClick("ȷ��", new OnClickListener() {					
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
			alertDialogBuilder.setMessage("������Ϣ");			
			alertDialogBuilder.setNegativeButton("ȡ��",new DialogInterface.OnClickListener() {				
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
			customerAlertDialog.setTitle("��ʾ��Ϣ");
			customerAlertDialog.setMessage("�ش�ݾ�����");
			customerAlertDialog.setCancelOnClick("ȡ��",new OnClickListener() {				
				@Override
				public void onClick(View v) {					 				 
					customerAlertDialog.cancel();
				}
			});
			customerAlertDialog.setokOnClick("ok",new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(MemberReg.this, "����ok", 1).show();					
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
