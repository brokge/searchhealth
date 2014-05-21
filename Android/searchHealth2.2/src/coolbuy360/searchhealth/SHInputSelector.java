/**
 * 
 */
package coolbuy360.searchhealth;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import coolbuy360.service.TempDataPool;
import coolbuy360.service.searchApp;

/**
 * @author yangxc ������ı�ֵѡ����
 */
public class SHInputSelector extends Activity {
	private static final int DIALOG_MESSAGE = 1;

	EditText sh_input_selector_input;
	BaseAdapter adapter;
	int maxLen = 0;
	String fieldName = "";

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sh_input_selector);
		// Ϊ�˳���׼��
		searchApp.getInstance().addActivity(this);

		// ���÷��ذ�ť
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SHInputSelector.this.finish();
			}
		});

		// ȷ����ť
		Button actionbar_ok_btn = (Button) this
				.findViewById(R.id.actionbar_ok_btn);
		actionbar_ok_btn.setOnClickListener(new onOKBtnClick());

		TextView actionbar_page_title = (TextView) this
				.findViewById(R.id.actionbar_page_title);
		sh_input_selector_input = (EditText) this
				.findViewById(R.id.sh_input_selector_input);
		ListView sh_input_selector_liv = (ListView) this
				.findViewById(R.id.sh_input_selector_liv);

		Bundle bundle = getIntent().getExtras();
		// ����ҳ�����
		actionbar_page_title.setText(bundle.getString("title"));
		maxLen = bundle.getInt("maxlen");
		fieldName = bundle.getString("fieldname");
		sh_input_selector_input.setHint("������" + fieldName);
		String nowValue = bundle.getString("nowvalue");
		if (nowValue != null && !(nowValue.equals(""))) {
			sh_input_selector_input.setText(nowValue);
		}

		String adapterkey = bundle.getString("adapterkey");
		// ͨ������ҳ�洫����������������ΨһKey����ʱ���ݳػ�ȡ��Ӧ��������
		adapter = TempDataPool.getAdapter(adapterkey);
		// ���б�����
		sh_input_selector_liv.setAdapter(adapter);
		// ʹ��������ʱ���ݳ����ٶ�Ӧ��������
		TempDataPool.destroyAdapter(adapterkey);

		sh_input_selector_liv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int posion,
					long arg3) {
				// ��ѡ�����йܵ���ʱ���ݳ�
				String resultkey = TempDataPool.putSelectorResult(adapter
						.getItem(posion));
				Intent resultIntent = new Intent();
				// ����ѡ�����ΨһKey���ص�����ҳ��
				resultIntent.putExtra("resultkey", resultkey);
				SHInputSelector.this.setResult(RESULT_OK, resultIntent);
				SHInputSelector.this.finish();
			}
		});

	}

	// ���水ť����¼�
	private final class onOKBtnClick implements OnClickListener {
		@Override
		public void onClick(View v) {

			String inputValue = sh_input_selector_input.getText().toString()
					.trim();
			if (inputValue != null && !(inputValue.equals(""))) {

				if (maxLen != 0 && inputValue.length() > maxLen) {
					showDialog(DIALOG_MESSAGE);
					return;
				}

				Map<String, String> resultItem = new HashMap<String, String>();
				resultItem.put("value", "");
				resultItem.put("text", inputValue);

				// ��ѡ�����йܵ���ʱ���ݳ�
				String resultkey = TempDataPool.putSelectorResult(resultItem);
				Intent resultIntent = new Intent();
				// ����ѡ�����ΨһKey���ص�����ҳ��
				resultIntent.putExtra("resultkey", resultkey);
				SHInputSelector.this.setResult(RESULT_OK, resultIntent);
				SHInputSelector.this.finish();
			} else {
				Toast.makeText(SHInputSelector.this, "������" + fieldName,
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case DIALOG_MESSAGE:
			return new AlertDialog.Builder(SHInputSelector.this)
			.setTitle("��ʾ")
			.setMessage(fieldName + "���ܳ���" + maxLen + "���֡�")
			.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {

					/* User clicked OK so do some stuff */
				}
			})
			.create();
		}
		return null;
	}
}
