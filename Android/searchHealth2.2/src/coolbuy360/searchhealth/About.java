/**
 * 
 */
package coolbuy360.searchhealth;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import coolbuy360.service.UpdateApp;
import coolbuy360.service.searchApp;

/**
 * 关于App
 * @author yangxc
 *
 */
public class About extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		// 为退出做准备
		searchApp.getInstance().addActivity(this);
		
		ImageButton actionbar_pre_btn=(ImageButton)this.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				About.this.finish();
			} 
		});		

		TextView about_version_txt=(TextView)this.findViewById(R.id.about_version_txt);
		String versionName = UpdateApp.getAppVersionName(getBaseContext());
		about_version_txt.setText("Android 版本  v" + versionName);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		  float x = event.getX();
          float y = event.getY();

          switch (event.getAction()) {
              case MotionEvent.ACTION_DOWN:
                  mTouchStartX = mTouchCurrX = x;
                  mTouchStartY = mTouchCurrY = y;                 
                 
                  break;
              case MotionEvent.ACTION_MOVE:
                  mTouchCurrX = x;
                  mTouchCurrY = y;
                 
                  break;
              case MotionEvent.ACTION_UP:
                
                  
                  break;
          }
          return true;
	}
	
    private float mTouchStartX;
    private float mTouchStartY;
    private float mTouchCurrX;
    private float mTouchCurrY;
	public void MoveActivity() {
		
	}
	
	
	
}
