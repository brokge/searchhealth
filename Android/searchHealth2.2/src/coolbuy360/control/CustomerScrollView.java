package coolbuy360.control;



import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ScrollView;

public class CustomerScrollView extends ScrollView {

/*
	Context mContext;
	private View mView;
	private float touchY;
	private int scrollY = 0;
	private boolean handleStop = false;
	private int eachStep = 0;

	private static final int MAX_SCROLL_HEIGHT = 200;// ��󻬶�����
	private static final float SCROLL_RATIO = 0.4f;// ����ϵ��,ԽС������Խ��
	private ImageView headerBackgroundView;
    public void setBackImageView(ImageView imageView ) {
	   headerBackgroundView=imageView;
    }
	public CustomerScrollView(Context context) {
		super(context);
		this.mContext = context;
	}

	public CustomerScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public CustomerScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	@Override
	protected void onFinishInflate() {
		if (getChildCount() > 0) {
			this.mView = getChildAt(0);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
			touchY = arg0.getY();
		}
		return super.onInterceptTouchEvent(arg0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mView == null) {
			return super.onTouchEvent(ev);
		} else {
			commonOnTouchEvent(ev);
		}
		return super.onTouchEvent(ev);
	}

	private void commonOnTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_UP:
			if (mView.getScrollY() != 0) {
				handleStop = true;
				animation();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			float nowY = ev.getY();
			int deltaY = (int) (touchY - nowY);
			touchY = nowY;
			if (isNeedMove()) {
				int offset = mView.getScrollY();
				if (offset < MAX_SCROLL_HEIGHT && offset > -MAX_SCROLL_HEIGHT) {
					mView.scrollBy(0, (int) (deltaY * SCROLL_RATIO));
					handleStop = false;
				}

				
				 * current_Top = (int) (initTop + image_move_H); current_Bottom
				 * = (int) (initBottom + image_move_H);
				 * headerBackgroundView.layout(headerBackgroundView.getLeft(),
				 * current_Top, headerBackgroundView.getRight(),
				 * current_Bottom);
				 

			}

			break;
		default:
			break;
		}
	}

	public void headerBackgroundViewAnimation () {
		//headerBackgroundView
	}
	
	private boolean isNeedMove() {
		int viewHight = mView.getMeasuredHeight();
		int srollHight = getHeight();
		int offset = viewHight - srollHight;
		int scrollY = getScrollY();
		if (scrollY == 0 || scrollY == offset) {
			return true;
		}
		return false;
	}

	private void animation() {
		scrollY = mView.getScrollY();
		eachStep = scrollY / 10;
		resetPositionHandler.sendEmptyMessage(0);
	}

	Handler resetPositionHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (scrollY != 0 && handleStop) {
				scrollY -= eachStep;
				if ((eachStep < 0 && scrollY > 0)
						|| (eachStep > 0 && scrollY < 0)) {
					scrollY = 0;
				}
				mView.scrollTo(0, scrollY);
				this.sendEmptyMessageDelayed(0, 5);
			}
		};
	};*/

	private final String TAG = CustomerScrollView.class.getSimpleName();

	private View inner;// ����View

	private float touchY;// ���ʱY����

	private float deltaY;// Y�Ử���ľ���

	private float initTouchY;// �״ε����Y����

	private boolean shutTouch = false;// �Ƿ�ر�ScrollView�Ļ���.

	private Rect normal = new Rect();// ����(����ֻ�Ǹ���ʽ��ֻ�������ж��Ƿ���Ҫ����.)

	private boolean isMoveing = false;// �Ƿ�ʼ�ƶ�.

	private ImageView imageView;// ����ͼ�ؼ�.
	private View line_up;// ����
	private int line_up_top;// ���ߵ�top
	private int line_up_bottom;// ���ߵ�bottom

	private int initTop, initBottom;// ��ʼ�߶�

	private int current_Top, current_Bottom;// �϶�ʱʱ�߶ȡ�

	private int lineUp_current_Top, lineUp_current_Bottom;// ����

	private onTurnListener turnListener;

	// ״̬���ϲ����²���Ĭ��
	private enum State {
		UP, DOWN, NOMAL
	};

	// Ĭ��״̬
	private State state = State.NOMAL;

	public void setTurnListener(onTurnListener turnListener) {
		this.turnListener = turnListener;
	}

	public void setLine_up(View line_up) {
		this.line_up = line_up;
	}

	// ע�뱳��ͼ
	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	/***
	 * ���췽��
	 * 
	 * @param context
	 * @param attrs
	 */
	public CustomerScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/***
	 * ���� XML ������ͼ�������.�ú�����������ͼ�������ã�����������ͼ�����֮��. ��ʹ���า���� onFinishInflate
	 * ������ҲӦ�õ��ø���ķ�����ʹ�÷�������ִ��.
	 */
	@Override
	protected void onFinishInflate() {
		if (getChildCount() > 0) {
			inner = getChildAt(0);
		}
	}

	/** touch �¼����� **/
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (inner != null) {
			commOnTouchEvent(ev);
		}
		// ture����ֹ�ؼ�����Ļ���.
		if (shutTouch)
			return true;
		else
			return super.onTouchEvent(ev);

	}

	/* ���� Javadoc��
	 * @see android.widget.ScrollView#onInterceptTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO �Զ����ɵķ������
		/*if (ev.getAction()==MotionEvent.ACTION_MOVE) {
			return true;
		}*/
	/*	if (ev.getAction()==MotionEvent.ACTION_HOVER_MOVE) {
			return true;
		}
		if (ev.getAction()==MotionEvent.ACTION_HOVER_ENTER) {
			return true;
		}*/
		/*if (ev.getAction()==MotionEvent.ACTION_UP) {
			return false;
		}*/
		if(ev.getAction()==MotionEvent.ACTION_DOWN)
		{
			initTouchY = ev.getY();
			current_Top = initTop = imageView.getTop();
			current_Bottom = initBottom = imageView.getBottom();
			if (line_up_top == 0) {
				lineUp_current_Top = line_up_top = line_up.getTop();
				lineUp_current_Bottom = line_up_bottom = line_up.getBottom();
			}
			Log.i(TAG, "CustomerScrollView  onInterceptTouchEvent down��initTouchY��"+initTouchY);
		}		
		/*else {
			
			Log.i(TAG, "CustomerScrollView Ĭ�Ϸ��ص�onInterceptTouchEvent��"+super.onInterceptTouchEvent(ev));
			
		}*/
		return super.onInterceptTouchEvent(ev);
	}
	
	/***
	 * �����¼�
	 * 
	 * @param ev
	 */
	public void commOnTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			initTouchY = ev.getY();
			current_Top = initTop = imageView.getTop();
			current_Bottom = initBottom = imageView.getBottom();
			if (line_up_top == 0) {
				lineUp_current_Top = line_up_top = line_up.getTop();
				lineUp_current_Bottom = line_up_bottom = line_up.getBottom();
			}
			Log.i(TAG, "CustomerScrollView down��initTouchY��"+initTouchY);
			break;
		case MotionEvent.ACTION_UP:
			/** �������� **/
			if (isNeedAnimation()) {
				animation();
			}

			if (getScrollY() == 0) {
				state = State.NOMAL;
			}

			isMoveing = false;
			touchY = 0;
			shutTouch = false;
			
			break;

		/***
		 * �ų�����һ���ƶ����㣬��Ϊ��һ���޷���֪deltaY�ĸ߶ȣ� Ȼ������ҲҪ���г�ʼ�������ǵ�һ���ƶ���ʱ���û��������0.
		 * ֮���¼׼ȷ�˾�����ִ��.
		 */
		case MotionEvent.ACTION_MOVE:

			touchY = ev.getY();
			Log.i(TAG, "CustomerScrollView move��touchY��"+touchY);
			deltaY = touchY - initTouchY;// ��������

			/** �����״�Touch����Ҫ�жϷ�λ��UP OR DOWN **/
			if (deltaY < 0 && state == state.NOMAL) {
				state = State.UP;
			} else if (deltaY > 0 && state == state.NOMAL) {
				state = State.DOWN;
			}

			if (state == State.UP) {
				deltaY = deltaY < 0 ? deltaY : 0;
				isMoveing = false;
				shutTouch = false;

				/** line_up **/
				lineUp_current_Top = (int) (line_up_top - getScrollY());
				lineUp_current_Bottom = (int) (line_up_bottom - getScrollY());

				Log.e(TAG, "top=" + getScrollY());

				line_up.layout(line_up.getLeft(), lineUp_current_Top,
						line_up.getRight(), lineUp_current_Bottom);

			} else if (state == state.DOWN) {
				if (getScrollY() <= deltaY) {
					shutTouch = true;
					isMoveing = true;
				}
				deltaY = deltaY < 0 ? 0 : deltaY;
			}

			if (isMoveing) {
				// ��ʼ��ͷ������
				if (normal.isEmpty()) {
					// ���������Ĳ���λ��
					normal.set(inner.getLeft(), inner.getTop(),
							inner.getRight(), inner.getBottom());
				}
				// �ƶ�����(�����ƶ���1/3)
				float inner_move_H = deltaY / 5;

				inner.layout(normal.left, (int) (normal.top + inner_move_H),
						normal.right, (int) (normal.bottom + inner_move_H));

				/** image_bg **/
				float image_move_H = deltaY / 10;
				current_Top = (int) (initTop + image_move_H);
				current_Bottom = (int) (initBottom + image_move_H);
				imageView.layout(imageView.getLeft(), current_Top,
						imageView.getRight(), current_Bottom);

				/** line_up **/
				lineUp_current_Top = (int) (line_up_top + inner_move_H);
				lineUp_current_Bottom = (int) (line_up_bottom + inner_move_H);
				line_up.layout(line_up.getLeft(), lineUp_current_Top,
						line_up.getRight(), lineUp_current_Bottom);
			}
			break;

		default:
			break;

		}
	}

	/***
	 * ��������
	 */
	public void animation() {

		TranslateAnimation image_Anim = new TranslateAnimation(0, 0,
				Math.abs(initTop - current_Top), 0);
		image_Anim.setDuration(200);
		imageView.startAnimation(image_Anim);

		imageView.layout(imageView.getLeft(), (int) initTop,
				imageView.getRight(), (int) initBottom);

		// �����ƶ�����
		TranslateAnimation inner_Anim = new TranslateAnimation(0, 0,
				inner.getTop(), normal.top);
		inner_Anim.setDuration(200);
		inner.startAnimation(inner_Anim);
		inner.layout(normal.left, normal.top, normal.right, normal.bottom);

		/** line_up **/
		TranslateAnimation line_up_Anim = new TranslateAnimation(0, 0,
				Math.abs(line_up_top - lineUp_current_Top), 0);
		line_up_Anim.setDuration(200);
		line_up.startAnimation(line_up_Anim);
		line_up.layout(line_up.getLeft(), line_up_top, line_up.getRight(),
				line_up_bottom);

		normal.setEmpty();

		/** ����ִ�� **/
		if (current_Top > initTop + 50 && turnListener != null)
			turnListener.onTurn();

	}

	/** �Ƿ���Ҫ�������� **/
	public boolean isNeedAnimation() {
		return !normal.isEmpty();
	}

	/***
	 * ִ�з�ת
	 * 
	 * @author jia
	 * 
	 */
	public interface onTurnListener {

		/** ����ﵽһ���̶Ȳ�ִ�� **/
		void onTurn();
	}

}
