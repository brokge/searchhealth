/**
 * 
 */
package coolbuy360.control;

import coolbuy360.searchhealth.R;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 健康值变化的弹窗
 * @author yangxc
 *
 */
public class ScoreChangePopup extends PopupWindow {
	private int mTextColor;
	private TextView msgTextView;
	
	/**
	 * 
	 */
	public ScoreChangePopup() {
		// TODO 自动生成的构造函数存根
	}
	
	/**
	 * 
	 */
	public ScoreChangePopup(Context context, int value) {
		// TODO 自动生成的构造函数存根
		View contentView = LayoutInflater.from(context).inflate(
				R.layout.pop_score, null);
		msgTextView = (TextView) contentView
				.findViewById(R.id.pop_score_txt);
		mTextColor = context.getResources().getColor(R.color.deepgreen);
		
		String text = "";
		if (value > 0) {
			text = "+" + value; 
		} else if (value < 0) {
			text = "-" + value;
			mTextColor = context.getResources().getColor(R.color.deepred);
		}
		
		msgTextView.setTextColor(mTextColor);
		msgTextView.setText(text);

		ViewTreeObserver vto2 = msgTextView.getViewTreeObserver();
		vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				msgTextView.getViewTreeObserver().removeGlobalOnLayoutListener(
						this);
				if (msgTextView.getWidth() < msgTextView.getHeight()) {
					msgTextView.setWidth(msgTextView.getHeight());
				} else {
					msgTextView.setHeight(msgTextView.getWidth());
				}
			}
		});
		
        setContentView(contentView);
        setWidth(LayoutParams.WRAP_CONTENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        
        setTouchable(true);
        setAnimationStyle(R.style.popwin_anim_style);
	}

	/**
	 * @param context
	 */
	public ScoreChangePopup(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
	}

	/**
	 * @param contentView
	 */
	public ScoreChangePopup(View contentView) {
		super(contentView);
		// TODO 自动生成的构造函数存根
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ScoreChangePopup(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
	}

	/**
	 * @param width
	 * @param height
	 */
	public ScoreChangePopup(int width, int height) {
		super(width, height);
		// TODO 自动生成的构造函数存根
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ScoreChangePopup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO 自动生成的构造函数存根
	}

	/**
	 * @param contentView
	 * @param width
	 * @param height
	 */
	public ScoreChangePopup(View contentView, int width, int height) {
		super(contentView, width, height);
		// TODO 自动生成的构造函数存根
	}

	/**
	 * @param contentView
	 * @param width
	 * @param height
	 * @param focusable
	 */
	public ScoreChangePopup(View contentView, int width, int height,
			boolean focusable) {
		super(contentView, width, height, focusable);
		// TODO 自动生成的构造函数存根
	}

	final Handler pophandler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// handler.postDelayed(this, 1000);
			dismiss();
		}
	};
	
	/**
	 * 延迟关闭弹窗，默认2秒
	 */
	public void delayedDismiss() {
		delayedDismiss(2000);
	}
	
	/**
	 * 延迟关闭弹窗，默认2秒
	 * @param delayMillis
	 */
	public void delayedDismiss(long delayMillis) {
		try {
			pophandler.postDelayed(runnable, delayMillis);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
		}
	}		
}
