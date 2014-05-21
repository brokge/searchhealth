package coolbuy360.searchhealth;

import android.R.layout;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomerAlertDialog extends Dialog {

	public static final int DISPLAYTXT = 1;
	public static final int DISPLAYEDIT = 2;
	private int DisplayPar = 1;
	Context memberContext;
	Button btnok;
	Button btncancel;
	EditText userEditText;
	EditText confirmEmailEditText;
	TextView messageTextView;
	TextView titleTextView;
	View viewLineView;
	String messageString = "奥奥";

	// CustomerDialogInterface customerDialogInterface;
	protected CustomerAlertDialog(Context context) {
		super(context);
		memberContext = context;
		// TODO Auto-generated constructor stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	protected CustomerAlertDialog(Context context, int theme, int displaypar) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		DisplayPar = displaypar;
		memberContext = context;		
		
		
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);		
	/*	*/
	}

	public void setokOnClick(String text, android.view.View.OnClickListener onClickListener) {	
		btnok.setText(text);
		btnok.setOnClickListener(onClickListener);
	}

	public void setCancelOnClick(String text, 
			android.view.View.OnClickListener onClickListener) {
		btncancel.setText(text);
		btncancel.setOnClickListener(onClickListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// View tempview=View.inflate(memberContext,
		// R.layout.customer_alertdialog, null);
		super.setContentView(R.layout.customer_alertdialog);
		// TODO Auto-generated method stub
		LinearLayout parentLayout=(LinearLayout)this.findViewById(R.id.customer_dialog_parent);
		int width=ConMain.getDisplayWidth()-20;
		ViewGroup.LayoutParams lps = parentLayout.getLayoutParams();
		lps.width = width;
		parentLayout.setLayoutParams(lps);
		/*Window window = this.getWindow();
		LayoutParams lpLayoutParams = new LayoutParams();
	
		lpLayoutParams.width =LayoutParams.MATCH_PARENT;
		lpLayoutParams.height =LayoutParams.WRAP_CONTENT;
		lpLayoutParams.gravity = Gravity.FILL_HORIZONTAL;
		
		//lpLayoutParams.horizontalMargin=10;
		window.setAttributes(lpLayoutParams);*/
		
		btnok = (Button) this.findViewById(R.id.customer_alertdialog_ok);
		btncancel = (Button) this
				.findViewById(R.id.customer_alertdialog_cancel);
		// btncancel.setOnClickListener(new negativeButtonOnClick());
		userEditText = (EditText) this
				.findViewById(R.id.customer_reg_edit_user);
		confirmEmailEditText = (EditText) this
				.findViewById(R.id.customer_reg_edit_confiremail);
		messageTextView = (TextView) this
				.findViewById(R.id.customer_reg_message);
		titleTextView = (TextView) this.findViewById(R.id.customer_reg_title);
		viewLineView = (View) this
				.findViewById(R.id.customer_alertdialog_viewline);
		switch (DisplayPar) {
		case DISPLAYTXT:
			userEditText.setVisibility(View.GONE);
			confirmEmailEditText.setVisibility(View.GONE);
			btnok.setVisibility(View.GONE);
			messageTextView.setVisibility(View.VISIBLE);
			btncancel.setVisibility(View.VISIBLE);
			viewLineView.setVisibility(View.GONE);
			// messageTextView.setText(messageString);
			break;
		case DISPLAYEDIT:
			userEditText.setVisibility(View.VISIBLE);
			confirmEmailEditText.setVisibility(View.VISIBLE);
			btnok.setVisibility(View.VISIBLE);
			btncancel.setVisibility(View.VISIBLE);
			viewLineView.setVisibility(View.VISIBLE);
			messageTextView.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	/*
	 * public void OkClick() {
	 * this.customerDialogInterface.CustomerOkListener(); } public void
	 * setCustomerOkListener(CustomerDialogInterface customerDialogListener) {
	 * this.customerDialogInterface=customerDialogListener; } public void
	 * setCustomerCancelListener(CustomerDialogInterface customerDialogListener)
	 * { this.customerDialogInterface=customerDialogListener; } public void
	 * CancelClick() { this.customerDialogInterface.CustomerCancelListener(); }
	 */
	/**
	 * 设置提醒的文字
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		if (messageTextView.getVisibility() == View.VISIBLE) {
			messageTextView.setText(message);
		}
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		if (titleTextView.getVisibility() == View.VISIBLE) {
			titleTextView.setText(title);
		}
	}

	public String getUserEditText() {
		if (userEditText.getVisibility() == View.VISIBLE) {
			return userEditText.getText().toString();
		} else {
			return "";
		}

	}

	public String getEmailEditText() {
		if (confirmEmailEditText.getVisibility() == View.VISIBLE) {
			return confirmEmailEditText.getText().toString();
		} else {
			return "";
		}
	}

}
