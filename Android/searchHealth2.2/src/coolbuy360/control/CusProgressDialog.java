package coolbuy360.control;

import coolbuy360.searchhealth.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 自定义的弹出加载和刷新的dialog
 * 
 * @author Administrator
 * 
 */
public class CusProgressDialog extends Dialog {

	public CusProgressDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CusProgressDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	Button refreshBtn;
	ProgressBar pregressBar;
	TextView messageTxt;
	Boolean isDisplayPressBar = false;
	Boolean isDisplayRefreshBtn = false;
	String message = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_progress);
		refreshBtn = (Button) this.findViewById(R.id.btn_refresh);
		pregressBar = (ProgressBar) this.findViewById(R.id.progressbar);
		messageTxt = (TextView) this.findViewById(R.id.txt_message);
		// 设置各自是否显示
		if (isDisplayPressBar) {
			pregressBar.setVisibility(View.VISIBLE);
		} else {
			pregressBar.setVisibility(View.GONE);
		}
		if (isDisplayRefreshBtn) {
			refreshBtn.setVisibility(View.VISIBLE);
		} else {
			refreshBtn.setVisibility(View.GONE);
		}
		if (!message.equals("")) {
			messageTxt.setVisibility(View.VISIBLE);
			messageTxt.setText(message);

		} else {
			messageTxt.setVisibility(View.GONE);
		}

	}

	/**
	 * 刷新按钮的相关操作
	 * 
	 * @param str
	 *            点击刷新的时候显示的文字
	 * @param listener
	 *            按钮的监听事件
	 */
	public void setReFreshListener(final String str,
			final View.OnClickListener listener) {
		refreshBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.onClick(v);
				v.setVisibility(View.GONE);
				pregressBar.setVisibility(View.VISIBLE);
				if (!message.equals("")) {
					messageTxt.setVisibility(View.VISIBLE);
					messageTxt.setText(message);
				} else {
					messageTxt.setVisibility(View.GONE);
				}
			}
		});
	}

	// onRefreshListener ReshListener;
	/**
	 * 创建新对象时设置是否显示
	 */
	public void setPregressBar(Boolean isDisplay) {

		this.isDisplayPressBar = isDisplay;
	}
	/**
	 * 创建新对象时设置是否显示
	 */
	public void setRefreshBtn(Boolean isDisplay) {
		this.isDisplayRefreshBtn = isDisplay;
	}
	/**
	 * 创建新对象时设置是显示的内容
	 */
	public void setMessage(String messageStr) {
		this.message = messageStr;
	}

	/**
	 * 设置隐藏progressbar，默认显示
	 */
	public void setProgressBarHide() {
		this.pregressBar.setVisibility(View.GONE);
	}

	/**
	 * 设置显示刷新按钮控件，默认不显示
	 */
	public void SetRefreshBtnShow() {
		this.refreshBtn.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置显示message控件，默认不显示，
	 * @param message
	 *        显示信息（7字以内最好）
	 */
	public void SetMessagetxtShow(String message) {
		this.messageTxt.setVisibility(View.VISIBLE);
		this.messageTxt.setText(message);
	}

}
