package coolbuy360.searchhealth;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

import coolbuy360.service.Util;
import coolbuy360.service.searchApp;
import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
//import android.widget.ImageView;

public class SplashMain extends Activity {
	private final int splash_display_length=3000;//�ӳ�����
	//private ImageView mImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Ϊ�˳���׼��
		searchApp.getInstance().addActivity(this);
		//����ȫ��Ļ��ʾ  �������setContentView��
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_splash_main);
		//����imageview����
//		mImageView=(ImageView)findViewById(R.id.SplashImageView);
//		mImageView.setImageResource(R.drawable.start_img_9);
		//���ӳ�ָ����ʱ����ִ��
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent(SplashMain.this, ConMain.class);
				SplashMain.this.startActivity(mainIntent);
				SplashMain.this.finish();
			}
		},splash_display_length);
		
		//Log.i("chenlinwei", "SplashMain Create");
		
	
		
		
	
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
     setIntent(intent);
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_splash_main, menu);
		return true;
	}*/
	
	

}
