package coolbuy360.searchhealth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import coolbuy360.PageIndicator.TabPageIndicator;
import coolbuy360.adapter.NewsPageAdapter;
import coolbuy360.logic.NewsColumn;
import coolbuy360.logic.NoticeStateConfig;
import coolbuy360.logic.Article.ArticleType;
import coolbuy360.service.CommandResult;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

public class MoreNews extends FragmentActivity {

	public static MoreNews iNews = null;
	public static ArticleType articleType = ArticleType.column;
	public static int PageSize = 10;
	public Dialog pBarcheck;
	TabPageIndicator indicator;
	NewsPageAdapter adapter;
	//NewsPageAdapter adapter;
	List<Map<String, String>> Chanel;
	boolean ISLoadIndicator = false;
	ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/*
		 * try { StrictModeWrapper.init(this); } catch(Throwable throwable) {
		 * Log.v("StrictMode", "... is not available. Punting..."); }
		 */
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.more_news);		
	
		TextView titleView = (TextView) this
				.findViewById(R.id.actionbar_page_title);
		titleView.setText(R.string.news);		
		ImageButton prebtnButton = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		prebtnButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				MoreNews.this.finish();
			}
		});		
		iNews = this;
		Chanel = getDefaultChanel();
		InitData(Chanel);
		if (ISLoadIndicator) {
			try {
				new asyLoadInit().execute();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 获取默认的栏目列表
	 */
	private List<Map<String, String>> getDefaultChanel() {
		List<Map<String, String>> defaultChanel = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("ColumnName", "热点");
		map.put("ColumnID", "1");
		defaultChanel.add(map);
		map = new HashMap<String, String>();
		map.put("ColumnName", "育儿");
		map.put("ColumnID", "3");
		defaultChanel.add(map);
		map = new HashMap<String, String>();
		map.put("ColumnName", "养生");
		map.put("ColumnID", "2");
		defaultChanel.add(map);
		map = new HashMap<String, String>();
		map.put("ColumnName", "两性");
		map.put("ColumnID", "4");
		defaultChanel.add(map);
		return defaultChanel;
	}	
	/**
	 * 根据栏目ID获取栏目索引
	 * @param columnid
	 * @return
	 */
	private int getColumnIndex(String columnid) {
		int index = 0;
		for (Map<String, String> columnitem : Chanel) {
			if (columnid.equals(columnitem.get("ColumnID"))) {
				return index;
			} else {
				index++;
			}
		}
		return 100;
	}
	
	/**
	 * 设置新资讯通知圆点状态
	 * @param columnid
	 * @param visibility View.GONE或View.VISIBLE
	 */
	public void setNewNotice(String columnid, int visibility) {
		try {
			int index = getColumnIndex(columnid);
			if (index != 100) {
				int noticeState = indicator.getNewNoticeState(index);
				if (noticeState != visibility) {			
					
					indicator.setNewNotice(index, visibility);
					if (visibility == View.GONE) {
						NoticeStateConfig.setValue(this, "Column" + index
								+ "_HasNew", "0");
					} else {
						NoticeStateConfig.setValue(this, "Column" + index
								+ "_HasNew", "1");
					}
				}
			}
		} catch (Exception e) {
			
		}
		
/*	String ishasNew=NoticeStateConfig.getNewsAllState(this);	
	if(ishasNew.equals("0"))
	{		
			More imore = More.getInstance();
			if(imore!=null){
				imore.setNewNotice(0, false);
			}			
		
	}*/
	
		
		
		
	}

	private void InitData(List<Map<String, String>> itemChanel) {
	
		adapter = new NewsPageAdapter(getSupportFragmentManager(),itemChanel, MoreNews.this);	
		
		pager = (ViewPager) findViewById(R.id.more_news_pager);
	
		pager.setAdapter(adapter);
		indicator = (TabPageIndicator) findViewById(R.id.more_news_indicator);
		indicator.setViewPager(pager);
		
		
		
		// 隐藏新功能“New”图标
		String news_IsVisited = NoticeStateConfig.getValue(this,
				NoticeStateConfig.News_IsVisited);
		if (news_IsVisited.equals("0")) {
			if (ConMain.mConMain != null) {
				ConMain.mConMain.setNewFunction("更多", false);
				More imore = More.getInstance();
				if(imore!=null){
					imore.setNewFunction(0, false);
				}
				NoticeStateConfig.setValue(this,
						NoticeStateConfig.News_IsVisited, "1");
			}
		}		
		// 启动时加载上次关闭的新消息状态
		try {
			for(int i=0; i<4; i++) {
				String value = NoticeStateConfig.getValue(this, "Column" + i
						+ "_HasNew");
				if (value.equals("1")) {
					indicator.setNewNotice(i, View.VISIBLE);
				} else {
					indicator.setNewNotice(i, View.GONE);
				}
			}
		} catch (Exception e) {

		}

		indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {			
			}
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {				
			}
			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
		
		
		
		
		
	}

	public class asyLoadInit extends AsyncTask<String, Void, Integer> {
		CommandResult checkResult = new CommandResult(false, "未知错误");
		List<Map<String, String>> innerList = new ArrayList<Map<String, String>>();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoading();
		}

		@Override
		protected Integer doInBackground(String... params) {
			innerList = NewsColumn.getList(0);
			if (innerList != null && !innerList.isEmpty()) {
				checkResult = new CommandResult(false, "");
				return 1;
			} else {
				checkResult = new CommandResult(false, "频道数据为空");
				return 0;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (result.equals(1)) {
				/*
				 * Chanel=new HashMap<String, String>(){{
				 * put("1","新闻1");put("2","健康");put("3","养生");put("4","美容"); }};
				 */
				// InitData(Chanel);
				Log.i("chenlinwei", innerList.get(0).toString());
				Chanel = getDefaultChanel();
				// InitData(Chanel);
				adapter.ChanelList = innerList;// innerList.get(0);
				adapter.notifyDataSetChanged();
				indicator.notifyDataSetChanged();
			} else {
				Chanel = getDefaultChanel();
				// InitData(Chanel);
				adapter.ChanelList = Chanel;
				adapter.notifyDataSetChanged();
				indicator.notifyDataSetChanged();
			}
			pBarcheck.cancel();
		}
	}

	public void showLoading() {
		if(pBarcheck==null)
		{
		pBarcheck = new Dialog(MoreNews.this, R.style.dialog);
		pBarcheck.setContentView(R.layout.custom_progress);
		pBarcheck.setCancelable(true);	
		pBarcheck.show();
		}
		// dialog.setTitle("Indeterminate");		
		
		
	}

	public void cancelLoading() {
		
		pBarcheck.cancel();
	}

/*	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
			intent.addCategory(Intent.CATEGORY_HOME);
			this.startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}*/
	
	// 单例模式中获取唯一的MyApplication实例
	public static MoreNews getInstance() {
		if (iNews == null) {
			return null;
		}
		return iNews;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO 自动生成的方法存根
		//super.onSaveInstanceState(outState);
	}

}
