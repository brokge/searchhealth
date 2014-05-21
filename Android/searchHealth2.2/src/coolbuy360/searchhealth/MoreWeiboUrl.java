package coolbuy360.searchhealth;

import coolbuy360.service.searchApp;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MoreWeiboUrl  extends Activity {
	WebView weibourl;
	private Dialog pBarcheck;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moreweibo_url);
		// 为退出做准备
		searchApp.getInstance().addActivity(this);
		
		Bundle bundle=getIntent().getExtras();
		String url=bundle.getString("weibo_url");
		weibourl=(WebView)findViewById(R.id.more_weibo_url_web);
		weibourl.loadUrl(url);
		//new  asyLoadUrl().execute(url);
		weibourl.setWebViewClient(new WebViewClient(){			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				pBarcheck = new Dialog(MoreWeiboUrl.this,R.style.dialog);
				pBarcheck.setContentView(R.layout.custom_progress);
				// dialog.setTitle("Indeterminate");
				/*pBarcheck.setMessage("正在获取医生信息");
				pBarcheck.setIndeterminate(true);*/
				pBarcheck.setCancelable(true);
				pBarcheck.show();				
				//super.onPageStarted(view, url, favicon);
			}			
			@Override
	         public boolean shouldOverrideUrlLoading(WebView view, String url) {
	 
	               view.loadUrl(url);   //在当前的webview中跳转到新的url
	 
	          return true;
	         }
			
			
			@Override
			public void onPageFinished(WebView view, String url) {
				pBarcheck.cancel();
				//super.onPageFinished(view, url);
			}

			
	    });	
	}
private final class asyLoadUrl extends AsyncTask<String, Void, Integer>
{
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		pBarcheck = new Dialog(MoreWeiboUrl.this,R.style.dialog);
		// dialog.setTitle("Indeterminate");
		pBarcheck.setContentView(R.layout.custom_progress);
		// dialog.setTitle("Indeterminate");
		/*pBarcheck.setMessage("正在获取医生信息");
		pBarcheck.setIndeterminate(true);*/
		pBarcheck.setCancelable(true);
		pBarcheck.show();	
		super.onPreExecute();
	}
	@Override
	protected Integer doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		weibourl.loadUrl(params[0]);
		weibourl.setWebViewClient(new WebViewClient(){
			
	         /* (non-Javadoc)
			 * @see android.webkit.WebViewClient#onPageStarted(android.webkit.WebView, java.lang.String, android.graphics.Bitmap)
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
			}

			/* (non-Javadoc)
			 * @see android.webkit.WebViewClient#onPageFinished(android.webkit.WebView, java.lang.String)
			 */
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
			}

			/* (non-Javadoc)
			 * @see android.webkit.WebViewClient#onReceivedError(android.webkit.WebView, int, java.lang.String, java.lang.String)
			 */
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			@Override
	         public boolean shouldOverrideUrlLoading(WebView view, String url) {
	 
	          view.loadUrl(url);   //在当前的webview中跳转到新的url
	          
	 
	          return true;
	         }
	       });
		
		return 1;
	}
	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		if(result==1)
		{
			pBarcheck.cancel();
		}
	}
	


}
}
