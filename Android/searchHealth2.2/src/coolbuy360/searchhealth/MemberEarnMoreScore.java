package coolbuy360.searchhealth;

import coolbuy360.service.searchApp;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 赚取更多健康值
 * 
 * @author yangxc
 * 
 */
public class MemberEarnMoreScore extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memberhealthscore_earnmore);

		// 为退出做准备
		searchApp.getInstance().addActivity(this);

		TextView titleView = (TextView) this
				.findViewById(R.id.actionbar_page_title);
		titleView.setText(R.string.program_healthscore_earnmore);
		WebView legal_webview = (WebView) this
				.findViewById(R.id.earn_more_score_webview);

		String urlString = "file:///android_asset/earn_more_score.html";
		legal_webview.getSettings().setDefaultTextEncodingName("UTF-8");
		legal_webview.loadUrl(urlString);

		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MemberEarnMoreScore.this.finish();
				overridePendingTransition(R.anim.push_no,R.anim.push_top_out);
			}
		});

	}

}
