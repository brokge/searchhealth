package coolbuy360.searchhealth;

import coolbuy360.logic.PushConfig;
import coolbuy360.logic.User;
import coolbuy360.service.CommonMethod;
import coolbuy360.service.searchApp;
import coolbuy360.zxing.CaptureActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MemberLogin extends Activity {

	String userid = "";
	String userpwd = "";
	EditText member_login_edit_user = null;
	EditText member_login_edit_pwd = null;
	Boolean islogin = false;
	//默认自动登录，永远自动登录
	Boolean ischecked = true;
	CheckBox member_login_chk_autochk = null;
	Boolean isorienPwd = true;
	String backprogram = "";
	public ProgressDialog pBarcheck;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_login);
		// 为退出做准备
		searchApp.getInstance().addActivity(this);

		// Button
		// regbtn=(Button)this.findViewById(R.id.member_login_btntitle_up);

		member_login_edit_user = (EditText) this
				.findViewById(R.id.member_login_edit_user);
		member_login_edit_pwd = (EditText) this
				.findViewById(R.id.member_login_edit_pwd);
		member_login_chk_autochk = (CheckBox) this
				.findViewById(R.id.member_login_chk_autochk);
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
	    /*Button regbtn = (Button) this
				.findViewById(R.id.member_login_btntitle_reg);*/
		TextView txtMemberReCallPwd = (TextView) this
				.findViewById(R.id.member_reCallPwd);
		TextView txtMemberRegister = (TextView) this
				.findViewById(R.id.member_register);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MemberLogin.this.finish();
			}
		});
		
		initEdit();// 初始化登录界面状态

		//regbtn.setOnClickListener(new regbtnOnClick());
		txtMemberReCallPwd.setOnClickListener(new resetPwdOnClick());
		txtMemberRegister.setOnClickListener(new regbtnOnClick());
	}

	/**
	 * 初始化登录界面状态
	 */
	private void initEdit() {
		Button loginbtn = (Button) this.findViewById(R.id.member_login_bt_nok);
		Bundle rootBundle = getIntent().getExtras();

		if (rootBundle != null) {
			backprogram = rootBundle.getString("backprogram");
			
			String autoLoginatat = rootBundle.getString("autoLoginatat");
			if (autoLoginatat != null && autoLoginatat.equals("ERROR")) {
				member_login_edit_user.setText(User
						.getLoginID(getBaseContext()));
				member_login_edit_pwd.setText(User
						.getLoginPassword(getBaseContext()));
				member_login_chk_autochk.setChecked(true);
				isorienPwd = false;
				loginbtn.setOnClickListener(new errorLoginbtnOnClick());
				return;
			}
		}

		member_login_edit_user.setText(User.getLoginID(getBaseContext()));
		// 默认自动登录，永远自动登录
		member_login_chk_autochk.setChecked(true);
		isorienPwd = true;
		loginbtn.setOnClickListener(new loginbtnOnClick());
	}

	public class errorLoginbtnOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			/*
			 * userid=member_login_edit_user.getText().toString().trim();
			 * userpwd=member_login_edit_pwd.getText().toString().trim(); User
			 * user=new User(MemberLogin.this); islogin=user.login(userid,
			 * userpwd, true);
			 */
			userid = member_login_edit_user.getText().toString().trim();
			userpwd = member_login_edit_pwd.getText().toString().trim();
			if (member_login_chk_autochk.isChecked()) {
				ischecked = true;
			} else {
				//默认自动登录，永远自动登录
				ischecked = true;
			}
			new errorAsyncLogin().execute(isorienPwd);

		}

	}

	/**
	 * 注册按钮的点击事件
	 * 
	 * @author Administrator
	 * 
	 */
	public class regbtnOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent regintent = new Intent().setClass(MemberLogin.this,
					MemberReg.class);
			startActivity(regintent);
		}
	}

	/**
	 * 找回密码点击事件
	 * 
	 * @author yangxc
	 * 
	 */
	public class resetPwdOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent resetpwdintent = new Intent().setClass(MemberLogin.this,
					MemberResetPwd.class);
			startActivity(resetpwdintent);
		}
	}

	/**
	 * 登录按钮的点击事件
	 * 
	 * @author Administrator
	 * 
	 */
	public class loginbtnOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			/*
			 * userid=member_login_edit_user.getText().toString().trim();
			 * userpwd=member_login_edit_pwd.getText().toString().trim(); User
			 * user=new User(MemberLogin.this); islogin=user.login(userid,
			 * userpwd, true);
			 */
			userid = member_login_edit_user.getText().toString().trim();
			userpwd = member_login_edit_pwd.getText().toString().trim();

			if (userid.equals("")) {
				Toast.makeText(MemberLogin.this, "请输入登录账号",
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (userpwd.equals("")) {
				Toast.makeText(MemberLogin.this, "请输入密码",
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (member_login_chk_autochk.isChecked()) {
				ischecked = true;
			} else {
				//默认自动登录，永远自动登录
				ischecked = true;
			}
			new AsyncLogin().execute(isorienPwd);

		}
	}

	class AsyncLogin extends AsyncTask<Boolean, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pBarcheck = new ProgressDialog(MemberLogin.this);
			// dialog.setTitle("Indeterminate");
			pBarcheck.setMessage("登录中……");
			pBarcheck.setIndeterminate(true);
			pBarcheck.setCancelable(true);
			pBarcheck.show();

		}

		@Override
		protected Boolean doInBackground(Boolean... params) {
			// TODO Auto-generated method stub
			User user = new User(getBaseContext());//
			if (params[0].booleanValue()) {
				islogin = user.login(userid, userpwd, true, ischecked);
			} else {
				//默认自动登录，永远自动登录
				islogin = user.login(userid, userpwd, true, ischecked);
			}

			return islogin;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if (result)// 如果登录成功
			{
				// Toast.makeText(MemberLogin.this, "chen", 1).show();
				// Toast.makeText(MemberLogin.this, "chen", 1).show();
				//登录成功push通知的相关操作
				PushConfig.tagHandle(getBaseContext());		
				pBarcheck.cancel();
				Intent memberIntent = new Intent().setClass(MemberLogin.this,
						Member.class);
				/*
				 * Bundle bundle=new Bundle(); bundle.putString("loginid",
				 * userid); memberIntent.putExtras(bundle);
				 */
				// startActivity(memberIntent);
				memberIntent.putExtra("backprogram", backprogram);
				MemberLogin.this.setResult(RESULT_OK, memberIntent);
				MemberLogin.this.finish();// 退出登录界面
			} else {
				pBarcheck.cancel();
				Dialog dialog = new AlertDialog.Builder(MemberLogin.this)
						.setTitle("登录失败").setMessage("帐号或密码错误，请重新输入")
						.setCancelable(true).setNegativeButton("取消", null)
						.show();
				/* dialog.setCancelMessage()); */
			}
		}
	}

	class errorAsyncLogin extends AsyncTask<Boolean, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pBarcheck = new ProgressDialog(MemberLogin.this);
			// dialog.setTitle("Indeterminate");
			pBarcheck.setMessage("可劲儿登录中，甭急……");
			pBarcheck.setIndeterminate(true);
			pBarcheck.setCancelable(true);
			pBarcheck.show();
		}

		@Override
		protected Boolean doInBackground(Boolean... params) {
			// TODO Auto-generated method stub
			User user = new User(getBaseContext());//
			if (params[0].booleanValue()) {
				islogin = user.login(userid, userpwd, true, ischecked);
			} else {
				//默认自动登录，永远自动登录
				islogin = user.login(userid, userpwd, true, ischecked);
			}

			return islogin;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if (result)// 如果登录成功
			{
				// Toast.makeText(MemberLogin.this, "chen", 1).show();
				//登录成功push通知的相关操作
				PushConfig.tagHandle(getApplicationContext());				
				//
				pBarcheck.cancel();
				Intent memberIntent = new Intent().setClass(MemberLogin.this,
						Member.class);
				/*
				 * Bundle bundle=new Bundle(); bundle.putString("loginid",
				 * userid); memberIntent.putExtras(bundle);
				 */
				// startActivity(memberIntent);
				memberIntent.putExtra("backprogram", backprogram);
				MemberLogin.this.setResult(RESULT_OK,memberIntent);
				//startActivity(memberIntent);
				MemberLogin.this.finish();// 退出登录界面
			} else {
				pBarcheck.cancel();
				Dialog dialog = new AlertDialog.Builder(MemberLogin.this)
						.setTitle("登录不成功").setMessage("请检查账号或密码是否错误。")
						.setCancelable(true).setNegativeButton("取消", null)
						.show();
				/* dialog.setCancelMessage()); */
			}
		}
	}
}
