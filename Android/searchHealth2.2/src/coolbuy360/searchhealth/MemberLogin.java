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
	//Ĭ���Զ���¼����Զ�Զ���¼
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
		// Ϊ�˳���׼��
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
		
		initEdit();// ��ʼ����¼����״̬

		//regbtn.setOnClickListener(new regbtnOnClick());
		txtMemberReCallPwd.setOnClickListener(new resetPwdOnClick());
		txtMemberRegister.setOnClickListener(new regbtnOnClick());
	}

	/**
	 * ��ʼ����¼����״̬
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
		// Ĭ���Զ���¼����Զ�Զ���¼
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
				//Ĭ���Զ���¼����Զ�Զ���¼
				ischecked = true;
			}
			new errorAsyncLogin().execute(isorienPwd);

		}

	}

	/**
	 * ע�ᰴť�ĵ���¼�
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
	 * �һ��������¼�
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
	 * ��¼��ť�ĵ���¼�
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
				Toast.makeText(MemberLogin.this, "�������¼�˺�",
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (userpwd.equals("")) {
				Toast.makeText(MemberLogin.this, "����������",
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (member_login_chk_autochk.isChecked()) {
				ischecked = true;
			} else {
				//Ĭ���Զ���¼����Զ�Զ���¼
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
			pBarcheck.setMessage("��¼�С���");
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
				//Ĭ���Զ���¼����Զ�Զ���¼
				islogin = user.login(userid, userpwd, true, ischecked);
			}

			return islogin;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if (result)// �����¼�ɹ�
			{
				// Toast.makeText(MemberLogin.this, "chen", 1).show();
				// Toast.makeText(MemberLogin.this, "chen", 1).show();
				//��¼�ɹ�push֪ͨ����ز���
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
				MemberLogin.this.finish();// �˳���¼����
			} else {
				pBarcheck.cancel();
				Dialog dialog = new AlertDialog.Builder(MemberLogin.this)
						.setTitle("��¼ʧ��").setMessage("�ʺŻ������������������")
						.setCancelable(true).setNegativeButton("ȡ��", null)
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
			pBarcheck.setMessage("�ɾ�����¼�У��¼�����");
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
				//Ĭ���Զ���¼����Զ�Զ���¼
				islogin = user.login(userid, userpwd, true, ischecked);
			}

			return islogin;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if (result)// �����¼�ɹ�
			{
				// Toast.makeText(MemberLogin.this, "chen", 1).show();
				//��¼�ɹ�push֪ͨ����ز���
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
				MemberLogin.this.finish();// �˳���¼����
			} else {
				pBarcheck.cancel();
				Dialog dialog = new AlertDialog.Builder(MemberLogin.this)
						.setTitle("��¼���ɹ�").setMessage("�����˺Ż������Ƿ����")
						.setCancelable(true).setNegativeButton("ȡ��", null)
						.show();
				/* dialog.setCancelMessage()); */
			}
		}
	}
}
