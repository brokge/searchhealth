/**
 * 
 */
package coolbuy360.control;

import java.util.Map;

import coolbuy360.adapter.DeleteAbleAdapter;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * ֧�����һ����¼���ListView
 * @author yangxc
 * 
 */
public class GestureListView extends ListView {

    //private Context ctx = null;
	// �¼�״̬
	public final static char FLING_CLICK = 0;
	public final static char FLING_LEFT = 1;
	public final static char FLING_RIGHT = 2;
	private char _flingState = FLING_CLICK;
	private int _delPosition = -1;
	private View _delView;
	private Boolean _isDelMode = false;

	private GestureDetector mLvDetector = new GestureDetector(new FlingListeber());	

	public GestureListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public GestureListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public GestureListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	/*public void setContext(Context context) {
		ctx = context;
	}*/
	
	public void resetMode(){
		this._delPosition = -1;
		this._isDelMode = false;
		this._flingState = FLING_CLICK;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLvDetector.onTouchEvent(ev))
			return true;
		return super.onTouchEvent(ev);
	}
	
	class FlingListeber implements GestureDetector.OnGestureListener{

		// ��ָ����Ļ���ƶ�����С�ڴ�ֵ���ᱻ��Ϊ������
		private static final int SWIPE_MIN_DISTANCE = 120;
		// ��ָ����Ļ���ƶ��ٶ�С�ڴ�ֵ���ᱻ��Ϊ����
		private static final int SWIPE_THRESHOLD_VELOCITY = 50;

		@Override
		public boolean onDown(MotionEvent e) {
			/*int position = pointToPosition((int) e.getX(),
					(int) e.getY());
			if (position != ListView.INVALID_POSITION) {
				View child = getChildAt(position
						- getFirstVisiblePosition());
				if (child != null)
					child.setPressed(true);
			}*/
			if (_isDelMode) {
				try {
					Object realObject = getAdapter();
					if(realObject.getClass().equals(HeaderViewListAdapter.class)) {
						HeaderViewListAdapter hvAdapter = (HeaderViewListAdapter)realObject;
						realObject = hvAdapter.getWrappedAdapter();
					}
					DeleteAbleAdapter adapter = (DeleteAbleAdapter)realObject;
					adapter.hideDeleteButton(_delView, _delPosition);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
				}
				resetMode();
			}
			return false;
		}

		/*@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2,
				float velocityX, float velocityY) {
			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				// left
				int position = pointToPosition((int) e1.getX(),
						(int) e1.getY());
				if (position != ListView.INVALID_POSITION) {
	            	Dialog dialog = new Dialog(ctx);
	            	dialog.setTitle("��"+position);
	            	dialog.setCanceledOnTouchOutside(true);
	            	dialog.show();
					Log.i("GestureDemo", "ListView left");
				}
				//mVf.showNext();
				return true;
			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				// right
				int position = pointToPosition((int) e1.getX(),
						(int) e1.getY());
				if (position != ListView.INVALID_POSITION) {
	            	Dialog dialog = new Dialog(ctx);
	            	dialog.setTitle("�һ�"+position);
	            	dialog.setCanceledOnTouchOutside(true);
	            	dialog.show();
					Log.i("GestureDemo", "ListView right");
				}
				//mVf.showPrevious();
				return true;
			}
			return false;
		}*/
		
		/**
	     * �������ͣ�
		 * 		e1����1��ACTION_DOWN MotionEvent
		 * 		e2�����һ��ACTION_MOVE MotionEvent
		 * 		vX��X���ϵ��ƶ��ٶȣ�����/��
		 * 		vY��Y���ϵ��ƶ��ٶȣ�����/��
		 * �������� ��
		 * 		X����λ�ƴ���minX��Y����λ��С��maxY���ƶ��ٶȴ���minV����/��
		 * 
	     */
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float vX, float vY)
		{
			final int minX = 120 ,maxY = 50, minV = 50;
			int x1 = (int) e1.getX(), x2 = (int) e2.getX();
			int y1 = (int) e1.getY(), y2 = (int) e2.getY();
			
			if( Math.abs(x1-x2)>minX && Math.abs(y1-y2)<maxY && Math.abs(vX)>minV) {
				if(x1>x2) {
					Log.v("MY_TAG", "Fling Left");
					// left
					_flingState = FLING_LEFT;
				}
				else {
					Log.v("MY_TAG", "Fling Right");
					// right
					_flingState = FLING_RIGHT;
				}
				
				int position = pointToPosition((int) e1.getX(),
						(int) e1.getY());
				if (position != ListView.INVALID_POSITION) {
					View child = getChildAt(position
							- getFirstVisiblePosition());
					if (child != null)
			    		showDeleteButton(child, position);
				}
			}
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			System.out.println("Listview long press");
			int position = pointToPosition((int) e.getX(),
					(int) e.getY());
			if (position != ListView.INVALID_POSITION) {
				View child = getChildAt(position
						- getFirstVisiblePosition());
				if (child != null)
					GestureListView.this.showContextMenuForChild(child);
			}
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			/*int position = pointToPosition((int) e.getX(),
					(int) e.getY());
			if (position != ListView.INVALID_POSITION) {
	        	Dialog dialog = new Dialog(ctx);
	        	dialog.setTitle("���"+position);
	        	dialog.setCanceledOnTouchOutside(true);
	        	dialog.show();
				return true;			
			}*/
			return false;
		}
    }

	/**
	 * ��ȡ��������״̬
	 * @return the flingState
	 */
	public char getFlingState() {
		return _flingState;
	}

	/**
	 * @param flingState the flingState to set
	 */
	public void setFlingState(char flingState) {
		this._flingState = flingState;
	}

	/**
	 * ��ȡɾ���к�
	 * @return the _delPosition
	 */
	public int getdelPosition() {
		return _delPosition;
	}

	/**
	 * @param _delPosition the _delPosition to set
	 */
	public void setdelPosition(int delPosition) {
		this._delPosition = delPosition;
	}

	/**
	 * ��ȡ�Ƿ���ɾ��ģʽ
	 * @return the _isDelMode
	 */
	public Boolean getisDelMode() {
		return _isDelMode;
	}

	/**
	 * @param _isDelMode the _isDelMode to set
	 */
	public void setisDelMode(Boolean isDelMode) {
		this._isDelMode = isDelMode;
	}
	
	public void showDeleteButton(View itemview, int position) {
		// TODO Auto-generated method stub
		try {
			_delPosition = position;
			_delView = itemview;
			_isDelMode = true;
			Object realObject = getAdapter();
			if(realObject.getClass().equals(HeaderViewListAdapter.class)) {
				HeaderViewListAdapter hvAdapter = (HeaderViewListAdapter)realObject;
				realObject = hvAdapter.getWrappedAdapter();
			}
			DeleteAbleAdapter adapter = (DeleteAbleAdapter)realObject;
			adapter.showDeleteButton(itemview, position);
			//_flingState = GestureListView.FLING_CLICK;
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}

	public void hideDeleteButton(View itemview, int position) {
		// TODO Auto-generated method stub
		
	}
	
	public void deleteSuccess(int position) {
		if (position != ListView.INVALID_POSITION) {
			View child = getChildAt(position
					- getFirstVisiblePosition());
			if (child != null) {
				try {
					Object realObject = getAdapter();
					if(realObject.getClass().equals(HeaderViewListAdapter.class)) {
						HeaderViewListAdapter hvAdapter = (HeaderViewListAdapter)realObject;
						realObject = hvAdapter.getWrappedAdapter();
					}
					DeleteAbleAdapter adapter = (DeleteAbleAdapter)realObject;
					adapter.deleteSuccess(child, position);
					resetMode();
				} catch (Exception e) {
					// TODO Auto-generated catch block
				}
			}
		}
	}
}
