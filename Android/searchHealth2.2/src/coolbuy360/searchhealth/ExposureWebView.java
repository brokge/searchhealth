package coolbuy360.searchhealth;

import coolbuy360.service.searchApp;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

public class ExposureWebView extends Activity {
	private Dialog pBarcheck;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//Ϊ�˳���׼��
		searchApp.getInstance().addActivity(this);
		setContentView(R.layout.exposure_webview);
		Bundle bundle=getIntent().getExtras();
		String url=bundle.getString("weburl");
		
		//final String mimeType = "text/html";
		
		WebView more_exposure_webview=(WebView)this.findViewById(R.id.more_exposure_webview);
		more_exposure_webview.getSettings().setDefaultTextEncodingName("UTF-8");
		more_exposure_webview.loadUrl(url);	
		
		more_exposure_webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url); // �ڵ�ǰ��webview����ת���µ�url
				return true;
			}
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				pBarcheck = new Dialog(ExposureWebView.this,R.style.dialog);
				pBarcheck.setContentView(R.layout.custom_progress);
				// dialog.setTitle("Indeterminate");
				/*pBarcheck.setMessage("���ڻ�ȡҽ����Ϣ");
				pBarcheck.setIndeterminate(true);*/
				pBarcheck.setCancelable(true);
				pBarcheck.show();				
				//super.onPageStarted(view, url, favicon);
			}			
			
			
			@Override
			public void onPageFinished(WebView view, String url) {
				pBarcheck.cancel();
				//super.onPageFinished(view, url);
			}

			
			
			
		});
		
		ImageButton actionbar_pre_btn=(ImageButton)this.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ExposureWebView.this.finish();
			}
		});
		
		
	}
	
	

}
