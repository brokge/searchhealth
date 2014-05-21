package coolbuy360.searchhealth;

import coolbuy360.logic.User;
import coolbuy360.service.CommandResult;
import coolbuy360.service.PasswordDigitsKey;
import coolbuy360.service.searchApp;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MemberPwdChange extends Activity {
	String oldpwd = "";
	String newpwd = "";
	String renewpwd = "";
	EditText member_pwd_edit_change_old;
	EditText member_pwd_change_edit_new;
	EditText member_pwd_change_edit_renew;
	public ProgressDialog pBarcheck;
	CommandResult cmdresult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_pwd_change);
		// 为退出做准备
		searchApp.getInstance().addActivity(this);

		member_pwd_edit_change_old = (EditText) this
				.findViewById(R.id.member_pwd_edit_change_old);
		member_pwd_change_edit_new = (EditText) this
				.findViewById(R.id.member_pwd_change_edit_new);
		member_pwd_change_edit_renew = (EditText) this
				.findViewById(R.id.member_pwd_change_edit_renew);

		//设置密码框的字符限制
		member_pwd_edit_change_old.setKeyListener(new PasswordDigitsKey());
		member_pwd_change_edit_new.setKeyListener(new PasswordDigitsKey());
		member_pwd_change_edit_renew.setKeyListener(new PasswordDigitsKey());

		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);// 返回按钮
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MemberPwdChange.this.finish();
			}
		});

		Button btnPwdConfim = (Button) this
				.findViewById(R.id.member_pwd_change_btn_confim);
		btnPwdConfim.setOnClickListener(new changeOnClick());
	}

	public class changeOnClick implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			oldpwd = member_pwd_edit_change_old.getText().toString().trim();
			newpwd = member_pwd_change_edit_new.getText().toString().trim();
			renewpwd = member_pwd_change_edit_renew.getText().toString().trim();
			
			if(oldpwd.equals("")){
				Toast.makeText(MemberPwdChange.this, "请输入当前密码", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(newpwd.equals("")){
				Toast.makeText(MemberPwdChange.this, "请输入新密码", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(renewpwd.equals("")){
				Toast.makeText(MemberPwdChange.this, "请再次输入确认新密码", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (newpwd.equals(renewpwd)) {
				new AsyncChangPwd().execute();
			} else {
				Toast.makeText(MemberPwdChange.this, "新密码两次输入不一样", Toast.LENGTH_SHORT).show();
				return;
			}
		}

	}

	class AsyncChangPwd extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pBarcheck = new ProgressDialog(MemberPwdChange.this);
			// dialog.setTitle("Indeterminate");
			pBarcheck.setMessage("正在提交……");
			pBarcheck.setIndeterminate(true);
			pBarcheck.setCancelable(true);
			pBarcheck.show();

		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			User user = new User(getBaseContext());//
			cmdresult = user.changePassword(oldpwd, newpwd);
			if (cmdresult.getResult()) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			Dialog dialog;
			if (result)// 如果登录成功
			{

				// Toast.makeText(MemberLogin.this, "chen", 1).show();

				pBarcheck.cancel();
				dialog = new AlertDialog.Builder(MemberPwdChange.this)
						.setTitle("修改成功")
						.setMessage("密码修改成功！")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

										/*
										 * Intent intent=new
										 * Intent().setClass(MemberPwdChange
										 * .this, Member.class);
										 * 
										 * startActivity(intent);
										 */
										Intent intent = new Intent().setClass(
												MemberPwdChange.this,
												Member.class);
										MemberPwdChange.this.setResult(RESULT_OK,intent);
										MemberPwdChange.this.finish();

									}
								}).setCancelable(true)
						.setNegativeButton("取消", null).show();

			} else {
				pBarcheck.cancel();
				dialog = new AlertDialog.Builder(MemberPwdChange.this)
						.setTitle("修改失败！")
						.setMessage(cmdresult.getMessage() + "，请重新输入")
						.setCancelable(true).setNegativeButton("取消", null)
						.show();
				/* dialog.setCancelMessage()); */
			}
		}

	}

}
