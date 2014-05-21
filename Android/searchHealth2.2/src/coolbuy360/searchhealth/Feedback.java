package coolbuy360.searchhealth;

import coolbuy360.logic.AppVersion;
import coolbuy360.searchhealth.R.color;
import coolbuy360.service.searchApp;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Feedback extends Activity {
	private static final long MAX_COUNT = 500;
	private Boolean isEmpty = true;
	private EditText mEditText = null;
	private ImageButton actionbar_pre_btn=null;
	private TextView mTextView = null;
	String curtxtstring = "";
	public ProgressDialog pBarupcontent = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Ϊ�˳���׼��
		searchApp.getInstance().addActivity(this);
		setContentView(R.layout.feedback);
		mEditText = (EditText) this.findViewById(R.id.feedback_txtcontent_edit);
		mTextView = (TextView) this.findViewById(R.id.feedback_txtcount_text);
		actionbar_pre_btn=(ImageButton)this.findViewById(R.id.actionbar_pre_btn);
		
		
		mEditText.addTextChangedListener(mTextWatcher);
		mEditText.setSelection(mEditText.length());
		Button btnup = (Button) this.findViewById(R.id.feedback_btntitle_up);
		setLeftCount();//ͳ������
		/*mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				EditText edit = (EditText) v;
				if (hasFocus == false)// ���ʧȥ����
				{
					curtxtstring = edit.getText().toString().trim();
					if (curtxtstring.length() > 0
							&& !curtxtstring
									.equals(R.string.feedback_txtcontent)) {
						isEmpty = false;
						edit.setText(curtxtstring);
					} else {
						edit.setText(R.string.feedback_txtcontent);
					}

				}
				// �����ý���
				else {
					if (isEmpty == true) {
						edit.setTextColor(color.blue);
						edit.setText("");
					}

				}

			}
		});*/

		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Feedback.this.finish();
			}
		});
		
		
		btnup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				curtxtstring = mEditText.getText().toString().trim();
				if (!curtxtstring.equals("")) {
					pBarupcontent = new ProgressDialog(Feedback.this);
					pBarupcontent.setMessage("�����ύ����");
					pBarupcontent.setIndeterminate(true);
					pBarupcontent.setCancelable(true);
					pBarupcontent.show();

					new AsyncLoader_upcontent().execute();
				} else {
					Toast.makeText(Feedback.this, "����û��д����Ŷ",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	/**
	 * �O�ı����B
	 */
	private TextWatcher mTextWatcher = new TextWatcher() {

		private int editStart;

		private int editEnd;

		public void afterTextChanged(Editable s) {
			editStart = mEditText.getSelectionStart();
			editEnd = mEditText.getSelectionEnd();

			// ��ȥ������������������ջ���
			mEditText.removeTextChangedListener(mTextWatcher);

			// ע������ֻ��ÿ�ζ�������EditText�������󳤶ȣ����ܶ�ɾ���ĵ����ַ��󳤶�
			// ��Ϊ����Ӣ�Ļ�ϣ������ַ����ԣ�calculateLength�������᷵��1
			while (calculateLength(s.toString()) > MAX_COUNT) { // �������ַ������������ƵĴ�Сʱ�����нضϲ���
				s.delete(editStart - 1, editEnd);
				editStart--;
				editEnd--;
			}
			mEditText.setText(s);
			mEditText.setSelection(editStart);

			// �ָ�������
			mEditText.addTextChangedListener(mTextWatcher);

			setLeftCount();
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

	};

	/**
	 * ����������ݵ�������һ������=����Ӣ����ĸ��һ�����ı��=����Ӣ�ı�� ע�⣺�ú����Ĳ������ڶԵ����ַ����м��㣬��Ϊ�����ַ������������1
	 * 
	 * @param c
	 * @return
	 */
	private long calculateLength(CharSequence c) {
		double len = 0;
		for (int i = 0; i < c.length(); i++) {
			int tmp = (int) c.charAt(i);
			if (tmp > 0 && tmp < 127) {
				len += 0.5;
			} else {
				len++;
			}
		}
		return Math.round(len);
	}

	/**
	 * ˢ��ʣ����������,���ֵ����΢����140���֣���������200����
	 */
	private void setLeftCount() {
		mTextView.setText(String.valueOf((MAX_COUNT - getInputCount())));
	}

	/**
	 * ��ȡ�û�����ķ�����������
	 * 
	 * @return
	 */
	private long getInputCount() {
		return calculateLength(mEditText.getText().toString());
	}

	class AsyncLoader_upcontent extends AsyncTask<String, Void, Integer> {
		@Override
		protected Integer doInBackground(String... params) {
			int result = 0;
			try {
				Boolean upstate = coolbuy360.logic.Feedback
						.insert(getBaseContext(), curtxtstring);
				if (upstate == true) {
					result = 2;
				}
			} catch (Exception ex) {
				result = -1;
			}
			return result;
		}

		@Override
		// �������
		protected void onPostExecute(Integer result) {

			if (result == 2) {
				pBarupcontent.cancel();
				Toast.makeText(Feedback.this, "�ύ�ɹ�,��л����Ľ��顭��", Toast.LENGTH_LONG).show();
				mEditText.setText("");

			} else {
				pBarupcontent.cancel();
				Toast.makeText(Feedback.this, "�ύʧ��", Toast.LENGTH_LONG).show();

			}
		}

	}

}
