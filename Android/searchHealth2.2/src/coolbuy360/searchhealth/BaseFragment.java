package coolbuy360.searchhealth;


import org.apache.http.message.BasicNameValuePair;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


import coolbuy360.control.PullToRefreshListView;
import coolbuy360.logic.Article.ArticleType;
import coolbuy360.service.IntentUtil;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseFragment extends Fragment { 
	private ImageLoader imageLoader = null;
	private	DisplayImageOptions options = null;

	private void InitImageLoader(Context _context)
	{
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(_context));		
		options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.promotion_def_pic)
		.displayer(new RoundedBitmapDisplayer(0xff000000, 10))		
		.cacheInMemory()
		.cacheOnDisc()		
		.build();		
	}
	public void BaseLoadImage(Context context,String url,ImageView imageView) {
		InitImageLoader(context);
		imageLoader.displayImage(url, imageView, options);
		
	}
	
//	/* （非 Javadoc）
//	 * @see android.support.v4.app.Fragment#onViewStateRestored(android.os.Bundle)
//	 */
//	@Override
//	public void onViewStateRestored(Bundle savedInstanceState) {
//		// TODO 自动生成的方法存根
//		super.onViewStateRestored(savedInstanceState);
//	}
	/* （非 Javadoc）
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	
	/**
	 * 列表点击进入detail
	 * @param mContext
	 *        上下文对象 ({@link Context})
	 * @param id
	 *        文章id 
	 * @param title
	 *        文章标题
	 * @param type
	 *        文章所属类型  目前定义的为({@link ArticleType}})
	 * @param updatetime
	 *        最后更新的时间，为list集合的UpdateTime
	 */
	public void startDetailActivity(Activity mContext, String id,
			String title, String type,String updatetime) {
		IntentUtil.start_activity(mContext, ArticleDetail.class,new BasicNameValuePair("articleid",id), new BasicNameValuePair("title", title), new BasicNameValuePair("articletype",type),new BasicNameValuePair("updatetime",updatetime));
	}	
	
	
	
	
	
}
