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
 * �Զ���ĵ������غ�ˢ�µ�dialog
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
		// ���ø����Ƿ���ʾ
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
	 * ˢ�°�ť����ز���
	 * 
	 * @param str
	 *            ���ˢ�µ�ʱ����ʾ������
	 * @param listener
	 *            ��ť�ļ����¼�
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
	 * �����¶���ʱ�����Ƿ���ʾ
	 */
	public void setPregressBar(Boolean isDisplay) {

		this.isDisplayPressBar = isDisplay;
	}
	/**
	 * �����¶���ʱ�����Ƿ���ʾ
	 */
	public void setRefreshBtn(Boolean isDisplay) {
		this.isDisplayRefreshBtn = isDisplay;
	}
	/**
	 * �����¶���ʱ��������ʾ������
	 */
	public void setMessage(String messageStr) {
		this.message = messageStr;
	}

	/**
	 * ��������progressbar��Ĭ����ʾ
	 */
	public void setProgressBarHide() {
		this.pregressBar.setVisibility(View.GONE);
	}

	/**
	 * ������ʾˢ�°�ť�ؼ���Ĭ�ϲ���ʾ
	 */
	public void SetRefreshBtnShow() {
		this.refreshBtn.setVisibility(View.VISIBLE);
	}

	/**
	 * ������ʾmessage�ؼ���Ĭ�ϲ���ʾ��
	 * @param message
	 *        ��ʾ��Ϣ��7��������ã�
	 */
	public void SetMessagetxtShow(String message) {
		this.messageTxt.setVisibility(View.VISIBLE);
		this.messageTxt.setText(message);
	}

}
