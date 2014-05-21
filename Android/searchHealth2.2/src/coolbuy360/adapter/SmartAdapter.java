/**
 * 
 */
package coolbuy360.adapter;

import android.view.View;
import android.widget.BaseAdapter;

/**
 * 
 * @author yangxc
 *
 */
public abstract class SmartAdapter extends BaseAdapter{

	OnDeleteItemListener onDeleteItemListener;
	
	public void setOnDeleteItemListener(OnDeleteItemListener l)
    {
    	onDeleteItemListener = l;
    }
	
	public interface OnDeleteItemListener {
		
        void onDeleteItem(View v, int position);
    }
}
