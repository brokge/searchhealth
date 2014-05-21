package coolbuy360.searchhealth;

import coolbuy360.service.searchApp;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class Exposure extends TabActivity {
  	TabHost tabHost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exposure);
		//为退出做准备
		searchApp.getInstance().addActivity(this);
		    tabHost = (TabHost)this.findViewById(android.R.id.tabhost);
		   	tabHost.setup();// 创建tabhost		   

			Intent blackintent = new Intent(Exposure.this, BlackDrug.class);
			setUpTab(new TextView(this), "黑榜", blackintent);	      
			        
	        Intent redintent = new Intent(Exposure.this, RedDrug.class);
			setUpTab(new TextView(this), "红榜",  redintent);
			
			
			ImageButton actionbar_pre_btn=(ImageButton)this.findViewById(R.id.actionbar_pre_btn);
			actionbar_pre_btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Exposure.this.finish();
				}
			});
	}
	
	
	/**
	 * @param view
	 *            需要设置的view
	 * @param tag
	 *            显示的文本值
	 * @param drawable
	 *            使用的图片
	 * @param intent
	 *            intent
	 */
	private void setUpTab(final View view, final String tag, 
			Intent intent) {
		View tabview = createTabView(tabHost.getContext(), tag);
		// tabspec是把tab区隔开，通过一定的样式
		TabSpec setContent = tabHost.newTabSpec(tag).setIndicator(tabview)
				.setContent(intent);
		tabHost.addTab(setContent);
	}

	/**
	 * 创建tab的view
	 * 
	 * @param context
	 *            传入的上下文对像
	 * @param text
	 *            文本显示的内容
	 * @param drawable
	 *            ico显示的图片
	 * @return
	 */
	private View createTabView(Context context, String text) {
		// TODO Auto-generated method stub
		// 相当于把xml样式中装入值
		View view = LayoutInflater.from(context).inflate(R.layout.exposure_tab_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.exposure_txt);
		tv.setText(text);	
		
		return view;
	}

}
