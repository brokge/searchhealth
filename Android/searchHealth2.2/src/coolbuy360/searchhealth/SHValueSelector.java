/**
 * 
 */
package coolbuy360.searchhealth;

import coolbuy360.service.TempDataPool;
import coolbuy360.service.searchApp;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author yangxc ��ֵѡ����
 */
public class SHValueSelector extends Activity {

	BaseAdapter adapter;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sh_value_selector);
		// Ϊ�˳���׼��
		searchApp.getInstance().addActivity(this);

		// ���÷��ذ�ť
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SHValueSelector.this.finish();
			}
		});

		TextView actionbar_page_title = (TextView) this
				.findViewById(R.id.actionbar_page_title);
		ListView sh_value_selector_liv = (ListView) this
				.findViewById(R.id.sh_value_selector_liv);

		Bundle bundle = getIntent().getExtras();
		// ����ҳ�����
		actionbar_page_title.setText(bundle.getString("title"));

		String adapterkey = bundle.getString("adapterkey");
		// ͨ������ҳ�洫����������������ΨһKey����ʱ���ݳػ�ȡ��Ӧ��������
		adapter = TempDataPool.getAdapter(adapterkey);
		// ���б�����
		sh_value_selector_liv.setAdapter(adapter);
		// ʹ��������ʱ���ݳ����ٶ�Ӧ��������
		TempDataPool.destroyAdapter(adapterkey);

		sh_value_selector_liv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int posion,
					long arg3) {
				// ��ѡ�����йܵ���ʱ���ݳ�
				String resultkey = TempDataPool.putSelectorResult(adapter
						.getItem(posion));
				Intent resultIntent = new Intent();
				// ����ѡ�����ΨһKey���ص�����ҳ��
				resultIntent.putExtra("resultkey", resultkey);
				SHValueSelector.this.setResult(RESULT_OK, resultIntent);
				SHValueSelector.this.finish();
			}
		});
	}
}
