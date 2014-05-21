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
 * @author yangxc 可输入的表值选择器
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
		// 为退出做准备
		searchApp.getInstance().addActivity(this);

		// 设置返回按钮
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SHInputSelector.this.finish();
			}
		});

		// 确定按钮
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
		// 设置页面标题
		actionbar_page_title.setText(bundle.getString("title"));
		maxLen = bundle.getInt("maxlen");
		fieldName = bundle.getString("fieldname");
		sh_input_selector_input.setHint("请输入" + fieldName);
		String nowValue = bundle.getString("nowvalue");
		if (nowValue != null && !(nowValue.equals(""))) {
			sh_input_selector_input.setText(nowValue);
		}

		String adapterkey = bundle.getString("adapterkey");
		// 通过父级页面传过来的数据适配器唯一Key从临时数据池获取对应的适配器
		adapter = TempDataPool.getAdapter(adapterkey);
		// 绑定列表数据
		sh_input_selector_liv.setAdapter(adapter);
		// 使用完后从临时数据池销毁对应的适配器
		TempDataPool.destroyAdapter(adapterkey);

		sh_input_selector_liv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int posion,
					long arg3) {
				// 将选择项托管到临时数据池
				String resultkey = TempDataPool.putSelectorResult(adapter
						.getItem(posion));
				Intent resultIntent = new Intent();
				// 传递选择项的唯一Key返回到父级页面
				resultIntent.putExtra("resultkey", resultkey);
				SHInputSelector.this.setResult(RESULT_OK, resultIntent);
				SHInputSelector.this.finish();
			}
		});

	}

	// 保存按钮点击事件
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

				// 将选择项托管到临时数据池
				String resultkey = TempDataPool.putSelectorResult(resultItem);
				Intent resultIntent = new Intent();
				// 传递选择项的唯一Key返回到父级页面
				resultIntent.putExtra("resultkey", resultkey);
				SHInputSelector.this.setResult(RESULT_OK, resultIntent);
				SHInputSelector.this.finish();
			} else {
				Toast.makeText(SHInputSelector.this, "请输入" + fieldName,
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
			.setTitle("提示")
			.setMessage(fieldName + "不能超过" + maxLen + "个字。")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {

					/* User clicked OK so do some stuff */
				}
			})
			.create();
		}
		return null;
	}
}
