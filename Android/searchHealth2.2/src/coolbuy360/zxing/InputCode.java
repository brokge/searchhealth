package coolbuy360.zxing;

import coolbuy360.searchhealth.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputCode extends Activity {
	EditText edit_input;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.input_code);

		Button btn_cancel = (Button) this.findViewById(R.id.input_btn_cancel);
		Button btn_recamera = (Button) this
				.findViewById(R.id.input_btn_recamera);
		Button btn_ok = (Button) this.findViewById(R.id.input_btn_ok);
		edit_input = (EditText) this.findViewById(R.id.input_edit_code);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String code = edit_input.getText().toString().trim();
				if (!code.equals("")) {
					Intent resultIntent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("serisecode", code);
					resultIntent.putExtras(bundle);
					InputCode.this.setResult(RESULT_OK, resultIntent);
					InputCode.this.finish();
				} else {
					Toast.makeText(InputCode.this, "请输入要查询的条形码！",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		btn_recamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent resultIntent = new Intent();
				InputCode.this.setResult(RESULT_CANCELED, null);
				InputCode.this.finish();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputCode.this.setResult(2, null);
				InputCode.this.finish();
			}
		});
	}

}
