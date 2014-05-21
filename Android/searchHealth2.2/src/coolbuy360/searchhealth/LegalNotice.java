/**
 * 
 */
package coolbuy360.searchhealth;

import coolbuy360.logic.Drug;
import coolbuy360.logic.DrugFavorite;
import coolbuy360.logic.DrugStoreFavorite;
import coolbuy360.logic.User;
import coolbuy360.service.searchApp;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * 法律声明
 * @author yangxc
 *
 */
public class LegalNotice extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.legal_notice);
		// 为退出做准备
		searchApp.getInstance().addActivity(this);		
		WebView legal_webview = (WebView) this.findViewById(R.id.legal_webview);		
		
		String urlString = "file:///android_asset/legal_notice.html";
		legal_webview.getSettings().setDefaultTextEncodingName("UTF-8");
		legal_webview.loadUrl(urlString);
				
		ImageButton actionbar_pre_btn=(ImageButton)this.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LegalNotice.this.finish();
			}
		});
	}
}
