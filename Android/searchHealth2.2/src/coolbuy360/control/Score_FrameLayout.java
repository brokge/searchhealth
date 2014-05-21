package coolbuy360.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class Score_FrameLayout extends FrameLayout {

	public Score_FrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
	}

	/* （非 Javadoc）
	 * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
	 */
	@Override
public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO 自动生成的方法存根
		
	/*	if(ev.getAction()==MotionEvent.ACTION_MOVE)
		{
			
			
		}*/
		
		return super.dispatchTouchEvent(ev);
}

	/* （非 Javadoc）
	 * @see android.view.ViewGroup#onInterceptTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO 自动生成的方法存根
		return super.onInterceptTouchEvent(ev);
		//return false;
	}
	
	
	

}
