package coolbuy360.searchhealth;

import java.util.HashMap;
import java.util.Map;

import coolbuy360.logic.AppConfig;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class CustomeDialog extends AlertDialog {
	 Map<String, String> configmap;
	public CustomeDialog(Context context, int theme) {
		super(context, theme);
	}

	public CustomeDialog(Context context) {
		super(context);
	}
	
/*	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Toast.makeText(getContext(), "AAAAAAAAAAAAAA", 1).show();
		super.onStart();
	}*/

	@Override
	public void setOnDismissListener(OnDismissListener listener) {
		// TODO Auto-generated method stub
		super.setOnDismissListener(listener);
		Toast.makeText(getContext(), "11", 1).show();
	}

	private int searchRang = -1;
	private int ishc = -1;
	RadioGroup rd_distance = null;
	RadioGroup rd_bao = null;
	AppConfig config=null;

	// ���¶����¼�
	public void setonOklistrner(final String str,
			final View.OnClickListener listener) {
		Button btnok = (Button) this.findViewById(R.id.dialog_ok);
		btnok.setText(str);
		btnok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				configmap=new HashMap<String, String>();
				configmap.put(AppConfig.Store_SearchRange, searchRang+"");
				configmap.put(AppConfig.Store_IsHC, ishc+"");
				
				 //btnok.setTag("distance", distance);
				// btnok.sett
				
				 v.setTag(configmap);
				 config = new AppConfig(getContext()); 
				 config.setValues(configmap);
				 /*config.setValue(AppConfig.Store_SearchRange, searchRang+"");
				 config.setValue(AppConfig.Store_IsHC, ishc+"");*/
				listener.onClick(v);
				dismiss();

			
			}
		});
		// btnok.setOnClickListener(listener);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.storedialog);
		config = new AppConfig(getContext());//��ȡ�����ļ�shareprefence
		searchRang = config.getStore_SearchRange(getContext());
		ishc = config.getStore_IsHC(getContext());

		/* Button btnok = (Button) this.findViewById(R.id.dialog_ok); */
		Button btncancel = (Button) this.findViewById(R.id.dialog_cancel);
		rd_distance = (RadioGroup) this.findViewById(R.id.radiogroup_distance);
		rd_bao = (RadioGroup) this.findViewById(R.id.radiogroup_bao);
		
		checkRang();// ��ȡĬ��ѡ�����
		ishcCheck();

		// btnok.setOnClickListener(new okonclick());

		btncancel.setOnClickListener(new cancelonclick());
		// btncancel
		rd_distance.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int radioButtonId = group.getCheckedRadioButtonId();
				RadioButton rb = (RadioButton) CustomeDialog.this
						.findViewById(radioButtonId);
				String text = rb.getText().toString();
				if (text.equals("5����")) {
					searchRang = 5;
				}
				else if(text.equals("10����")) {
					searchRang = 10;
				}
				else if(text.equals("20����")) {
					searchRang = 20;
				} else {
					searchRang = -1;
				}
			}
		});

		rd_bao.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub

				int radioButtonId = group.getCheckedRadioButtonId();
				RadioButton rb = (RadioButton) CustomeDialog.this
						.findViewById(radioButtonId);
				String text = rb.getText().toString();
				if (text.equals("����")) {
					ishc = 1;
				}
				else if (text.equals("������")) {
					ishc = 0;
				} else {
					ishc = -1;
				}
			}
		});

	}

	/**
	 * ҽ����ѡ�����ж�
	 */
	private void ishcCheck() {
		if (ishc == 0) {
			rd_bao.check(R.id.storedialog_rdioNo);
		}
		else if (ishc == 1) {
			rd_bao.check(R.id.storedialog_rdioYes);

		} else{
			rd_bao.check(R.id.storedialog_rdioYesOrNo);
		}
	}

	/**
	 * �����ѡ�����ж�
	 */
	private void checkRang() {
		if (searchRang == 5) {
			rd_distance.check(R.id.storedialog_rdio5);
		}
		else if (searchRang == 10) {
			rd_distance.check(R.id.storedialog_rdio10);
		}
		else if (searchRang == 20) {
			rd_distance.check(R.id.storedialog_rdio20);
		} else {
			rd_distance.check(R.id.storedialog_rdioMore);
		}
	}

	public final class okonclick implements android.view.View.OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// rd_distance.
			// Toast.makeText(getContext(), distance+"-"+bao+"", 1).show();
			/*
			 * AppConfig config = new AppConfig(getContext()); config.setValue(
			 * getContext(),AppConfig.Store_SearchRange, distance+"");
			 * config.setValue(getContext(),AppConfig.Store_IsHC, bao+"");
			 */

			new okonclick1();
			CustomeDialog.this.dismiss();
			// Map<K, V>
		}

	}

	private final class okonclick1 implements OnDismissListener {
		@Override
		public void onDismiss(DialogInterface dialog) {
			// TODO Auto-generated method stub
			/*
			 * Toast.makeText(getContext(), distance+"-"+bao+"", 1).show();
			 * AppConfig config = new AppConfig(getContext()); config.setValue(
			 * getContext(),AppConfig.Store_SearchRange, distance+"");
			 * config.setValue(getContext(),AppConfig.Store_IsHC, bao+"");
			 */
			Toast.makeText(getContext(), "11", 1).show();
		}

	}

	private final class cancelonclick implements
			android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			CustomeDialog.this.dismiss();
		}
	}

}