package coolbuy360.searchhealth;

import coolbuy360.service.BuilderGestureExt;
import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MemberScoreExchange extends Activity {

	private GestureDetector gestureDetector;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_scoreexchange);
		TextView titleView = (TextView) this
				.findViewById(R.id.actionbar_page_title);
		titleView.setText(R.string.member_scoreexchange);
		ImageButton prebtnButton = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		prebtnButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				MemberScoreExchange.this.finish();
			}
		});
		GestureEvent();
	}
	private void GestureEvent() {
		gestureDetector = new BuilderGestureExt(this,
				new BuilderGestureExt.OnGestureResult() {
					@Override
					public void onGestureResult(int direction) {
						if (direction == 2 || direction == 3) {
							// show(Integer.toString(direction));
							MemberScoreExchange.this.finish();
							overridePendingTransition(R.anim.push_no,
									R.anim.push_right_out);
						}
					}
				}).Buile();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);

	}

}
