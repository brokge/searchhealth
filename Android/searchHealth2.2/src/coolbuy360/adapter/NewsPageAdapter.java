package coolbuy360.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;
import coolbuy360.dbhelper.DBnewshelper;
import coolbuy360.logic.Article;
import coolbuy360.searchhealth.MoreNews;
import coolbuy360.searchhealth.News;
import coolbuy360.searchhealth.NewsSub;
import coolbuy360.searchhealth.NewsSubTest;
import coolbuy360.searchhealth.R;
import coolbuy360.service.CommandResult;
import coolbuy360.service.Util;

public class NewsPageAdapter extends FragmentPagerAdapter {

	NewListAdapter newListAdapter;

	public Context mcontext;
	public static final int HTTP_REQUEST_SUCCESS = -1;
	public static final int HTTP_REQUEST_ERROR = 0;	

	public List<Map<String, String>> ChanelList;

	public NewsPageAdapter(FragmentManager fm,
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
			 return new NewsSub(mcontext, MoreNews.articleType.toString(), parentid,position);
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
