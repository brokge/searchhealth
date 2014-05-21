package coolbuy360.searchhealth;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class NewsDetail extends Activity {
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);

		 String articleID =getIntent().getStringExtra("ArticleID");
		 String  title=getIntent().getStringExtra("Title");
		 String  chanel=getIntent().getStringExtra("Chanel");
		 
		 TextView txtView=(TextView)this.findViewById(R.id.actionbar_page_title);
		 txtView.setText(chanel);
		 ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		 actionbar_pre_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NewsDetail.this.finish();
				overridePendingTransition(R.anim.push_no,R.anim.push_top_out);
			}
		});
	}
	
	

}
