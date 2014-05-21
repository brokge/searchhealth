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
		//Ϊ�˳���׼��
		searchApp.getInstance().addActivity(this);
		    tabHost = (TabHost)this.findViewById(android.R.id.tabhost);
		   	tabHost.setup();// ����tabhost		   

			Intent blackintent = new Intent(Exposure.this, BlackDrug.class);
			setUpTab(new TextView(this), "�ڰ�", blackintent);	      
			        
	        Intent redintent = new Intent(Exposure.this, RedDrug.class);
			setUpTab(new TextView(this), "���",  redintent);
			
			
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
	 *            ��Ҫ���õ�view
	 * @param tag
	 *            ��ʾ���ı�ֵ
	 * @param drawable
	 *            ʹ�õ�ͼƬ
	 * @param intent
	 *            intent
	 */
	private void setUpTab(final View view, final String tag, 
			Intent intent) {
		View tabview = createTabView(tabHost.getContext(), tag);
		// tabspec�ǰ�tab��������ͨ��һ������ʽ
		TabSpec setContent = tabHost.newTabSpec(tag).setIndicator(tabview)
				.setContent(intent);
		tabHost.addTab(setContent);
	}

	/**
	 * ����tab��view
	 * 
	 * @param context
	 *            ����������Ķ���
	 * @param text
	 *            �ı���ʾ������
	 * @param drawable
	 *            ico��ʾ��ͼƬ
	 * @return
	 */
	private View createTabView(Context context, String text) {
		// TODO Auto-generated method stub
		// �൱�ڰ�xml��ʽ��װ��ֵ
		View view = LayoutInflater.from(context).inflate(R.layout.exposure_tab_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.exposure_txt);
		tv.setText(text);	
		
		return view;
	}

}
