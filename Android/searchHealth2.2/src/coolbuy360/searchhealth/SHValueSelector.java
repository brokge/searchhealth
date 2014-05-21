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
 * @author yangxc 表值选择器
 */
public class SHValueSelector extends Activity {

	BaseAdapter adapter;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sh_value_selector);
		// 为退出做准备
		searchApp.getInstance().addActivity(this);

		// 设置返回按钮
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
		// 设置页面标题
		actionbar_page_title.setText(bundle.getString("title"));

		String adapterkey = bundle.getString("adapterkey");
		// 通过父级页面传过来的数据适配器唯一Key从临时数据池获取对应的适配器
		adapter = TempDataPool.getAdapter(adapterkey);
		// 绑定列表数据
		sh_value_selector_liv.setAdapter(adapter);
		// 使用完后从临时数据池销毁对应的适配器
		TempDataPool.destroyAdapter(adapterkey);

		sh_value_selector_liv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int posion,
					long arg3) {
				// 将选择项托管到临时数据池
				String resultkey = TempDataPool.putSelectorResult(adapter
						.getItem(posion));
				Intent resultIntent = new Intent();
				// 传递选择项的唯一Key返回到父级页面
				resultIntent.putExtra("resultkey", resultkey);
				SHValueSelector.this.setResult(RESULT_OK, resultIntent);
				SHValueSelector.this.finish();
			}
		});
	}
}
