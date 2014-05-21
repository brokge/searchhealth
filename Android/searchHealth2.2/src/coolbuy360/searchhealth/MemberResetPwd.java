/**
 * 
 */
package coolbuy360.searchhealth;

import coolbuy360.logic.User;
import coolbuy360.service.CommandResult;
import coolbuy360.service.CommonMethod;
import coolbuy360.service.PasswordDigitsKey;
import coolbuy360.service.Validator;
import coolbuy360.service.searchApp;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

/**
 * @author yangxc
 * 
 *         会员找回密码
 * 
 */
public class MemberResetPwd extends Activity {

	EditText member_resetpwd_edit_mobile = null;
	EditText member_resetpwd_edit_email = null;
	EditText member_resetpwd_edit_newpwd = null;
	EditText member_resetpwd_edit_renewpwd = null;

	boolean mobileValidate = false;
	boolean emailValidate = false;
	boolean newPwdValidate = false;
	boolean renewPwdValidate = false;

	public ProgressDialog pBarcheck;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_resetpwd);
		// 为退出做准备
		searchApp.getInstance().addActivity(this);

		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MemberResetPwd.this.finish();
			}
		});

		member_resetpwd_edit_mobile = (EditText) this
				.findViewById(R.id.member_resetpwd_edit_mobile);
		member_resetpwd_edit_email = (EditText) this
				.findViewById(R.id.member_resetpwd_edit_email);
		member_resetpwd_edit_newpwd = (EditText) this
				.findViewById(R.id.member_resetpwd_edit_newpwd);
		member_resetpwd_edit_renewpwd = (EditText) this
				.findViewById(R.id.member_resetpwd_edit_renewpwd);

		// 设置密码框的字符限制
		member_resetpwd_edit_newpwd.setKeyListener(new PasswordDigitsKey());
		member_resetpwd_edit_renewpwd.setKeyListener(new PasswordDigitsKey());

		// 验证输入框里的格式的监听事件
		VaridateEdittxt();

		Button member_resetpwd_btn_reset = (Button) this
				.findViewById(R.id.member_resetpwd_btn_reset);
		member_resetpwd_btn_reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CommonMethod.clearFocuse(MemberResetPwd.this);
				if (mobileValidate && emailValidate && newPwdValidate
						&& renewPwdValidate) {
					new AsyncResetPwd().execute();
				} else {
					if (!mobileValidate)
						checkMobile(member_resetpwd_edit_mobile);
					if (!emailValidate)
						checkEmail(member_resetpwd_edit_email);
					if (!newPwdValidate)
						checkNewPwd(member_resetpwd_edit_newpwd);
					Toast.makeText(MemberResetPwd.this, "请检查错误项",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		ImageButton member_resetpwd_btn_emaildes = (ImageButton) this
				.findViewById(R.id.member_resetpwd_btn_emaildes);
		member_resetpwd_btn_emaildes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String msgString = "    请填写您账号注册时填写的邮箱或账号绑定的邮箱地址，用于身份验证。\n    若您未绑定邮箱地址，请电话联系我们取回账号密码。电话号码：0571-87072025";
				Dialog dialog = new AlertDialog.Builder(MemberResetPwd.this)
						.setTitle("邮箱说明")
						.setMessage(msgString)
						.setPositiveButton("拨打电话",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										Intent intent = new Intent(
												Intent.ACTION_DIAL,
												Uri.parse("tel:057187072025"));
										intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										MemberResetPwd.this
												.startActivity(intent);
									}
								}).setCancelable(true)
						.setNegativeButton("确定", null).show();
			}
		});
	}

	// 手机号码验证
	private void checkMobile(EditText et) {
		String input = et.getText().toString();
		if (input.length() != 0 && !input.equals("")) {
			if (!Validator.getValidatorInstance(MemberResetPwd.this)
					.isMobileNO(input)) {
				mobileValidate = false;
				et.setError(changeErrorColor("请输入正确的手机号码"));
			} else
				mobileValidate = true;
		} else {
			mobileValidate = false;
			et.setError(changeErrorColor("请输入您注册的手机号码"));
		}
	}

	// 邮箱码验证
	private void checkEmail(EditText et) {
		String input = et.getText().toString();
		if (input.length() != 0 && !input.equals("")) {
			if (!Validator.getValidatorInstance(MemberResetPwd.this).isEmail(
					input)) {
				emailValidate = false;
				et.setError(changeErrorColor("请输入正确的电子邮箱"));
			} else
				emailValidate = true;
		} else {
			emailValidate = false;
			et.setError(changeErrorColor("请输入您注册的电子邮箱进行验证"));
		}
	}

	// 新密码验证
	private void checkNewPwd(EditText et) {
		String input = et.getText().toString();
		int pwdlen = input.length();
		if (pwdlen < 6 || pwdlen > 12) {
			newPwdValidate = false;
			et.setError(changeErrorColor("密码长度为6-12个字符"));
		} else {
			newPwdValidate = true;
		}

		String renewpwdinput = member_resetpwd_edit_renewpwd.getText()
				.toString();
		if (input.equals(renewpwdinput)) {
			renewPwdValidate = true;
			member_resetpwd_edit_renewpwd.setError(null);
		} else {
			renewPwdValidate = false;
			member_resetpwd_edit_renewpwd.setError(changeErrorColor("密码不一致"));
		}
	}

	/**
	 * 验证重要数据的完整性监听
	 */
	private void VaridateEdittxt() {
		// 手机号码验证
		member_resetpwd_edit_mobile
				.setOnFocusChangeListener(new OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						EditText et = (EditText) v;
						if (!hasFocus) {
							checkMobile(et);
						}
					}
				});

		// 邮箱的验证
		member_resetpwd_edit_email
				.setOnFocusChangeListener(new OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						EditText et = (EditText) v;
						if (!hasFocus) {
							checkEmail(et);
						}
					}
				});

		// 新密码的验证
		member_resetpwd_edit_newpwd
				.setOnFocusChangeListener(new OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						EditText et = (EditText) v;
						if (!hasFocus) {
							checkNewPwd(et);
						}
					}
				});

		// 重复新密码的验证
		member_resetpwd_edit_renewpwd
				.setOnFocusChangeListener(new OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						EditText et = (EditText) v;
						String input = et.getText().toString();
						if (!hasFocus) {
							String newpwdinput = member_resetpwd_edit_newpwd
									.getText().toString();
							if (!(input.equals(newpwdinput))) {
								renewPwdValidate = false;
								et.setError(changeErrorColor("密码不一致"));
							} else {
								renewPwdValidate = true;
							}
						}
					}
				});
	}

	private SpannableStringBuilder changeErrorColor(String errorMsg) {
		// int ecolor =R.color.green ; // whatever color you want
		String estring = errorMsg;
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.RED);
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
		ssbuilder.setSpan(fgcspan, 0, estring.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ssbuilder;
	}

	class AsyncResetPwd extends AsyncTask<String, Void, CommandResult> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pBarcheck = new ProgressDialog(MemberResetPwd.this);
			pBarcheck.setMessage("正在验证……");
			pBarcheck.setIndeterminate(true);
			pBarcheck.setCancelable(true);
			pBarcheck.show();
		}

		@Override
		protected CommandResult doInBackground(String... params) {
			// TODO Auto-generated method stub
			User user = new User(getBaseContext());
			CommandResult result = new CommandResult(false, "未知错误，保存失败。");
			try {
				result = user.resetPwd(member_resetpwd_edit_mobile.getText()
						.toString(), member_resetpwd_edit_email.getText()
						.toString(), member_resetpwd_edit_newpwd.getText()
						.toString());
				return result;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return result;
			}
		}

		@Override
		protected void onPostExecute(CommandResult result) {
			// TODO Auto-generated method stub
			Dialog dialog;
			if (result.getResult())// 如果登录成功
			{
				pBarcheck.cancel();
				member_resetpwd_edit_mobile.setText("");
				member_resetpwd_edit_email.setText("");
				member_resetpwd_edit_newpwd.setText("");
				member_resetpwd_edit_renewpwd.setText("");
				dialog = new AlertDialog.Builder(MemberResetPwd.this)
						.setTitle("重置成功")
						.setMessage("密码重置成功，用新密码重新登录吧。")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										MemberResetPwd.this.finish();
									}
								}).setCancelable(true).show();
			} else {
				pBarcheck.cancel();
				dialog = new AlertDialog.Builder(MemberResetPwd.this)
						.setTitle("重置失败！").setMessage(result.getMessage())
						.setCancelable(true).setNegativeButton("确定", null)
						.show();
			}
		}
	}
}
