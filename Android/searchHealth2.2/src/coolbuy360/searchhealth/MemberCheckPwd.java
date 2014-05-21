/**
 * 
 */
package coolbuy360.searchhealth;

import coolbuy360.logic.User;
import coolbuy360.service.CommandResult;
import coolbuy360.service.CommonMethod;
import coolbuy360.service.MD5;
import coolbuy360.service.PasswordDigitsKey;
import coolbuy360.service.searchApp;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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

/**
 * ��֤����
 * @author yangxc
 *
 */
public class MemberCheckPwd extends Activity {

	EditText member_checkpwd_edit_pwd = null;

	boolean pwdValidate = false;
	String backprogram = "";

	public ProgressDialog pBarcheck;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_checkpwd);
		// Ϊ�˳���׼��
		searchApp.getInstance().addActivity(this);

		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MemberCheckPwd.this.finish();
			}
		});
		
		member_checkpwd_edit_pwd = (EditText) this
				.findViewById(R.id.member_checkpwd_edit_pwd);

		// �����������ַ�����
		member_checkpwd_edit_pwd.setKeyListener(new PasswordDigitsKey());

		// ��֤�������ĸ�ʽ�ļ����¼�
		VaridateEdittxt();
		
		Bundle rootBundle = getIntent().getExtras();
		if (rootBundle != null) {
			backprogram = rootBundle.getString("backprogram");
		}

		Button member_checkpwd_btn_check = (Button) this
				.findViewById(R.id.member_checkpwd_btn_check);
		member_checkpwd_btn_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CommonMethod.clearFocuse(MemberCheckPwd.this);
				if (pwdValidate) {
					new AsyncResetPwd().execute();
				} else {
					if (!pwdValidate)
						checkPwd(member_checkpwd_edit_pwd);
					Toast.makeText(MemberCheckPwd.this, "���������",
							Toast.LENGTH_LONG).show();
				}
			}
		});		
	}

	// ������֤
	private void checkPwd(EditText et) {
		String input = et.getText().toString();
		int pwdlen = input.length();
		if (pwdlen < 6 || pwdlen > 12) {
			pwdValidate = false;
			et.setError(changeErrorColor("���볤��Ϊ6-12���ַ�"));
		} else {
			pwdValidate = true;
		}
	}

	/**
	 * ��֤��Ҫ���ݵ������Լ���
	 */
	private void VaridateEdittxt() {
		// ���������֤
		member_checkpwd_edit_pwd
				.setOnFocusChangeListener(new OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						EditText et = (EditText) v;
						if (!hasFocus) {
							checkPwd(et);
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
			pBarcheck = new ProgressDialog(MemberCheckPwd.this);
			pBarcheck.setMessage("������֤����");
			pBarcheck.setIndeterminate(true);
			pBarcheck.setCancelable(true);
			pBarcheck.show();
		}

		@Override
		protected CommandResult doInBackground(String... params) {
			// TODO Auto-generated method stub
			User user = new User(getBaseContext());
			CommandResult result = new CommandResult(false, "δ֪������֤ʧ�ܡ�");
			try {
				String opwd = User.getValue(MemberCheckPwd.this, "Member_Password");
				String pwd = member_checkpwd_edit_pwd.getText().toString();
				if (MD5.getMD5(pwd).toLowerCase().equals(opwd.toLowerCase())) {
					result.setResult(true);
					result.setMessage("��֤�ɹ���");
				} else {
					result.setMessage("������������ԡ�");
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
			Dialog dialog;
			if (result.getResult())// �����¼�ɹ�
			{
				pBarcheck.cancel();
				member_checkpwd_edit_pwd.setText("");
				Intent memberIntent = new Intent().setClass(MemberCheckPwd.this,
						Member.class);
				memberIntent.putExtra("backprogram", backprogram);
				MemberCheckPwd.this.setResult(RESULT_OK,memberIntent);
				MemberCheckPwd.this.finish();
			} else {
				pBarcheck.cancel();
				dialog = new AlertDialog.Builder(MemberCheckPwd.this)
						.setTitle("����").setMessage(result.getMessage())
						.setCancelable(true).setNegativeButton("ȷ��", null)
						.show();
			}
		}
	}
}
