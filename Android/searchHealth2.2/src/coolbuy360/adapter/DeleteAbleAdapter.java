/**
 * 
 */
package coolbuy360.adapter;

import android.view.View;

/**
 * 允许删除项的Adapter接口
 * @author yangxc
 *
 */
public interface DeleteAbleAdapter {
	
	public void showDeleteButton(View itemview, int position);
	
	public void hideDeleteButton(View itemview, int position);
	
	public void deleteSuccess(View itemview, int position);

}
