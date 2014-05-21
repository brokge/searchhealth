/**
 * 
 */
package coolbuy360.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PopupOverlay;

/**
 * �̳�MapView��дonTouchEventʵ�����ݴ������
 * @author yangxc
 */
public class MyMapView extends MapView {
	public PopupOverlay pop = null;// ��������ͼ�㣬���ͼ��ʹ��

	public MyMapView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!super.onTouchEvent(event)) {
			// ��������
			if (pop != null && event.getAction() == MotionEvent.ACTION_UP)
				pop.hidePop();
		}
		return true;
	}
}
