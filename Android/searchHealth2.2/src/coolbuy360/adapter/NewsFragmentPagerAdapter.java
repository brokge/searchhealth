package coolbuy360.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import coolbuy360.dbhelper.DBnewshelper;
import coolbuy360.logic.Article;
import coolbuy360.pulltorefresh.PullToRefreshListView;
import coolbuy360.searchhealth.News;
import coolbuy360.searchhealth.NewsSub0;
import coolbuy360.searchhealth.NewsSub1;
import coolbuy360.searchhealth.NewsSub2;
import coolbuy360.searchhealth.NewsSub3;
import coolbuy360.searchhealth.NewsSubTest;
import coolbuy360.searchhealth.R;
import coolbuy360.searchhealth.TestFragment;
import coolbuy360.service.CommandResult;
import coolbuy360.service.DisplayParams;
import coolbuy360.service.DisplayUtil;
import coolbuy360.service.ImageManager;
import coolbuy360.service.Util;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NewsFragmentPagerAdapter extends FragmentPagerAdapter {

	NewListAdapter newListAdapter;

	public Context mcontext;
	public static final int HTTP_REQUEST_SUCCESS = -1;
	public static final int HTTP_REQUEST_ERROR = 0;	

	public List<Map<String, String>> ChanelList;

	public NewsFragmentPagerAdapter(FragmentManager fm,
			List<Map<String, String>> chanelList, Context context) {
		super(fm);
		ChanelList = chanelList;
		mcontext = context;
		
	}

	@Override
	public Fragment getItem(int position) {	
			String parentid = ChanelList.get(position).get("ColumnID")
					.toString();
			// NewsSubTest.getInstance(mcontext, News.articleType.toString(), parentid);
			 return new NewsSubTest(mcontext, News.articleType.toString(), parentid,position);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return ChanelList.get(position).get("ColumnName").toUpperCase();
	}

	@Override
	public int getCount() {
		return ChanelList.size();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		return super.instantiateItem(container, position);
	}


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return super.getItemId(position);
	}
}
