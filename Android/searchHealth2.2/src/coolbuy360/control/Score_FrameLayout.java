package coolbuy360.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class Score_FrameLayout extends FrameLayout {

	public Score_FrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO �Զ����ɵĹ��캯�����
	}

	/* ���� Javadoc��
	 * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
	 */
	@Override
public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO �Զ����ɵķ������
		
	/*	if(ev.getAction()==MotionEvent.ACTION_MOVE)
		{
			
			
		}*/
		
		return super.dispatchTouchEvent(ev);
}

	/* ���� Javadoc��
	 * @see android.view.ViewGroup#onInterceptTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO �Զ����ɵķ������
		return super.onInterceptTouchEvent(ev);
		//return false;
	}
	
	
	

}
