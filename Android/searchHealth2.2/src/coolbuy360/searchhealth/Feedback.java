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
		// 为退出做准备
		searchApp.getInstance().addActivity(this);
		setContentView(R.layout.feedback);
		mEditText = (EditText) this.findViewById(R.id.feedback_txtcontent_edit);
		mTextView = (TextView) this.findViewById(R.id.feedback_txtcount_text);
		actionbar_pre_btn=(ImageButton)this.findViewById(R.id.actionbar_pre_btn);
		
		
		mEditText.addTextChangedListener(mTextWatcher);
		mEditText.setSelection(mEditText.length());
		Button btnup = (Button) this.findViewById(R.id.feedback_btntitle_up);
		setLeftCount();//统计字数
		/*mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				EditText edit = (EditText) v;
				if (hasFocus == false)// 如果失去焦点
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
				// 如果获得焦点
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
					pBarupcontent.setMessage("正在提交……");
					pBarupcontent.setIndeterminate(true);
					pBarupcontent.setCancelable(true);
					pBarupcontent.show();

					new AsyncLoader_upcontent().execute();
				} else {
					Toast.makeText(Feedback.this, "您还没填写建议哦",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	/**
	 * 監文本框狀態
	 */
	private TextWatcher mTextWatcher = new TextWatcher() {

		private int editStart;

		private int editEnd;

		public void afterTextChanged(Editable s) {
			editStart = mEditText.getSelectionStart();
			editEnd = mEditText.getSelectionEnd();

			// 先去掉监听器，否则会出现栈溢出
			mEditText.removeTextChangedListener(mTextWatcher);

			// 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
			// 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
			while (calculateLength(s.toString()) > MAX_COUNT) { // 当输入字符个数超过限制的大小时，进行截断操作
				s.delete(editStart - 1, editEnd);
				editStart--;
				editEnd--;
			}
			mEditText.setText(s);
			mEditText.setSelection(editStart);

			// 恢复监听器
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
	 * 计算分享内容的字数，一个汉字=两个英文字母，一个中文标点=两个英文标点 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
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
	 * 刷新剩余输入字数,最大值新浪微博是140个字，人人网是200个字
	 */
	private void setLeftCount() {
		mTextView.setText(String.valueOf((MAX_COUNT - getInputCount())));
	}

	/**
	 * 获取用户输入的分享内容字数
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
		// 处理界面
		protected void onPostExecute(Integer result) {

			if (result == 2) {
				pBarupcontent.cancel();
				Toast.makeText(Feedback.this, "提交成功,感谢您提的建议……", Toast.LENGTH_LONG).show();
				mEditText.setText("");

			} else {
				pBarupcontent.cancel();
				Toast.makeText(Feedback.this, "提交失败", Toast.LENGTH_LONG).show();

			}
		}

	}

}
