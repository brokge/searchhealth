package coolbuy360.searchhealth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import coolbuy360.adapter.NewListAdapter;
import coolbuy360.control.AdvViewPager;
import coolbuy360.dbhelper.DBnewshelper;
import coolbuy360.logic.Article;
import coolbuy360.pulltorefresh.PullToRefreshBase;
import coolbuy360.pulltorefresh.PullToRefreshListView;
import coolbuy360.pulltorefresh.PullToRefreshBase.Mode;
import coolbuy360.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import coolbuy360.searchhealth.NewsSub0.GetNewsTask;
import coolbuy360.searchhealth.NewsSub0.GetNewsTask2;
import coolbuy360.searchhealth.NewsSub0.MyOnRefreshListener2;
import coolbuy360.service.CommandResult;
import coolbuy360.service.DisplayParams;
import coolbuy360.service.Util;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NewsSub3 extends BaseFragment {
	private static final String KEY_CONTENT = "NewsSub0:Content";
	public static final int HTTP_REQUEST_SUCCESS = -1;
	public static final int HTTP_REQUEST_ERROR = 0;
	private List<View> advs = null;
	private AdvViewPager vpAdv = null;
	public View view;
	public View headerView;
	ViewGroup vg;
	private ImageView[] imageViews = null;
	private NewListAdapter sub3NewAdapter = null;
	private int currentPage = 0;
	private PullToRefreshListView refreshListView;
	public Context mContext;
	String articleType;
	String parentID;
	Boolean isCompleted = false;

	public NewsSub3(Context context, NewListAdapter adapter) {
		mContext = context;
		sub3NewAdapter = adapter;
	}

	public NewsSub3(Context context, String articltype, String parentid) {
		mContext = context;
		articleType = articltype;
		parentID = parentid;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if ((savedInstanceState != null)
				&& savedInstanceState.containsKey(KEY_CONTENT)) {
		}
		view = inflater.inflate(R.layout.basefragment_listview, null);
		refreshListView = (PullToRefreshListView) view
				.findViewById(R.id.ptrlvSubNews);
		initPullToRefreshListView(refreshListView, sub3NewAdapter);
		
		return view;
	}

	/**
	 * 初始化PullToRefreshListView<br>
	 * 初始化在PullToRefreshListView中的ViewPager广告栏
	 * 
	 * @param rtflv
	 * @param adapter
	 */
	public void initPullToRefreshListView(PullToRefreshListView rtflv,
			NewListAdapter adapter) {
		rtflv.setMode(Mode.BOTH);
		rtflv.setOnRefreshListener(new MyOnRefreshListener2(rtflv));
		rtflv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				// sub3NewAdapter
				Map<String, String> maplist = (Map<String, String>) parent
						.getItemAtPosition(position);
				//Log.i("newsub0",maplist.get("ArticleID") + maplist.get("Title"));
				// TODO Auto-generated method stub
				startDetailActivity(getActivity(), maplist.get("ArticleID"),
						maplist.get("Title"), articleType,
						maplist.get("UpdateTime"));
			}
		});

		/*
		 * RelativeLayout rlAdv = (RelativeLayout) LayoutInflater.from(mContext)
		 * .inflate(R.layout.sliding_advertisement, null); vpAdv =
		 * (AdvViewPager) rlAdv.findViewById(R.id.vpAdv); vg = (ViewGroup)
		 * rlAdv.findViewById(R.id.viewGroup);
		 * 
		 * advs = new ArrayList<View>(); ImageView iv; iv = new
		 * ImageView(mContext); iv.setBackgroundResource(R.drawable.new_img1);
		 * advs.add(iv);
		 * 
		 * iv = new ImageView(mContext);
		 * iv.setBackgroundResource(R.drawable.new_img2); advs.add(iv);
		 * 
		 * iv = new ImageView(mContext);
		 * iv.setBackgroundResource(R.drawable.new_img3); advs.add(iv);
		 * 
		 * iv = new ImageView(mContext);
		 * iv.setBackgroundResource(R.drawable.new_img4); advs.add(iv);
		 * 
		 * vpAdv.setAdapter(new AdvAdapter());
		 * 
		 * vpAdv.setOnPageChangeListener(new OnPageChangeListener() {
		 * 
		 * @Override public void onPageSelected(int arg0) { currentPage = arg0;
		 * for (int i = 0; i < advs.size(); i++) { if (i == arg0) {
		 * imageViews[i] .setBackgroundResource(R.drawable.banner_dian_focus); }
		 * else { imageViews[i]
		 * .setBackgroundResource(R.drawable.banner_dian_blur); } } }
		 * 
		 * @Override public void onPageScrolled(int arg0, float arg1, int arg2)
		 * {
		 * 
		 * }
		 * 
		 * @Override public void onPageScrollStateChanged(int arg0) {
		 * 
		 * } });
		 * 
		 * imageViews = new ImageView[advs.size()]; ImageView imageView; for
		 * (int i = 0; i < advs.size(); i++) { imageView = new
		 * ImageView(mContext); imageView.setLayoutParams(new LayoutParams(20,
		 * 20)); imageViews[i] = imageView; if (i == 0) { imageViews[i]
		 * .setBackgroundResource(R.drawable.banner_dian_focus); } else {
		 * imageViews[i] .setBackgroundResource(R.drawable.banner_dian_blur); }
		 * vg.addView(imageViews[i]); }
		 * 
		 * rtflv.getRefreshableView().addHeaderView(rlAdv, null, false);
		 * 
		 * 
		 * final Handler handler = new Handler() {
		 * 
		 * @Override public void handleMessage(Message msg) {
		 * vpAdv.setCurrentItem(msg.what); super.handleMessage(msg); }
		 * 
		 * 
		 * 
		 * 
		 * }; new Thread(new Runnable() {
		 * 
		 * @Override public void run() { while (true) { try {
		 * Thread.sleep(5000); currentPage++; if(currentPage > advs.size()-1) {
		 * currentPage = 0; } handler.sendEmptyMessage(currentPage); } catch
		 * (InterruptedException e) { e.printStackTrace(); } } } }).start();
		 */

		/*
		 * AbsoluteLayout abslayout=new AbsoluteLayout (this); 05
		 * setContentView(abslayout); 06 //创建一个button按钮 07 Button btn1 = new
		 * Button(this); 08 btn1.setText(”this is a button”); 09 btn1.setId(1);
		 * 10 //确定这个控件的大小和位置 11 AbsoluteLayout.LayoutParams lp1 = 12 new
		 * AbsoluteLayout.LayoutParams( 13 ViewGroup.LayoutParams.WRAP_CONTENT,
		 * 14 ViewGroup.LayoutParams.WRAP_CONTENT, 15 0,100); 16
		 * abslayout.addView(btn1, lp1 );
		 */

		// LinearLayout linearLayout=new LinearLayout(mContext);
		//Log.i("newsub0", "oncreateview");
		if (sub3NewAdapter != null) {
			//Log.i("newsub0", "sub3NewAdapter is not null oncreateview");
			if (headerView != null) {
				rtflv.getRefreshableView().addHeaderView(headerView, null,
						false);
			}
			rtflv.setAdapter(sub3NewAdapter);
		} else {
			//Log.i("newsub0", " sub3NewAdapter is null oncreateview");
		}
		// rtflv.setAdapter(adapter);
	}

	/**
	 * 获取N条模拟的新闻数据<br>
	 * 打包成ArrayList返回
	 * 
	 * @return
	 */
	public List<Map<String, String>> getSimulationNews(int n) {
		List<Map<String, String>> ret = new ArrayList<Map<String, String>>();
		HashMap<String, String> hm;
		for (int i = 0; i < n; i++) {
			hm = new HashMap<String, String>();
			if (i % 2 == 0) {
				hm.put("uri",
						"http://images.china.cn/attachement/jpg/site1000/20131029/001fd04cfc4813d9af0118.jpg");
			} else {
				hm.put("uri",
						"http://photocdn.sohu.com/20131101/Img389373139.jpg");
			}
			hm.put("title", "国内成品油价两连跌几成定局");
			hm.put("content", "国内成品油今日迎调价窗口，机构预计每升降价0.1元。");
			hm.put("review", i + "跟帖");
			hm.put("id", i + "新闻");
			ret.add(hm);
		}
		return ret;
	}

	/**
	 * 上拉请求网络获得新闻信息（添加）
	 * 
	 * @author brokge
	 * 
	 */
	class GetNewsTask extends AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerList = new ArrayList<Map<String, String>>();
		CommandResult commandResult = new CommandResult(false, "请检查网络");
		int PageSize;
		String Parentid;
		String Articletype;
		private PullToRefreshListView mPtrlv;
		String Updatetime;

		public GetNewsTask(PullToRefreshListView ptrlv, int pagesize,
				String parentid, String articletype, String updatetime) {
			this.mPtrlv = ptrlv;
			PageSize = pagesize;
			Parentid = parentid;
			Articletype = articletype;
			Updatetime = updatetime;
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				if (Util.isNetworkConnected(mContext)) {
					if (!isCompleted) {
						// 先加载本地数据，如果本地数据为空则加载远程数据，加载完成后写入本地库
						DBnewshelper dBnewshelper = new DBnewshelper(mContext);
						innerList = dBnewshelper.GetList(Articletype, Parentid,
								PageSize, Updatetime);
						if (innerList==null||innerList.size() <= 0) {
							innerList = Article.getListOfColumn(Parentid, PageSize,
									Updatetime);
							if (innerList.size() > 0) {
								// 如果从服务器加载成功，则开始写入数据到本地库
								dBnewshelper.Insert(innerList, Articletype,
										Parentid);
								commandResult.setResult(true);
								commandResult.setMessage("远程数据加载成功");
								return HTTP_REQUEST_SUCCESS;
							} else {
								commandResult.setResult(false);
								commandResult.setMessage("没有更多数据");
								isCompleted = true;
								return HTTP_REQUEST_ERROR;
							}
						} else {
							commandResult.setResult(true);
							commandResult.setMessage("本地缓存数据加载成功");
							return HTTP_REQUEST_SUCCESS;
						}
					} else {
						commandResult.setResult(false);
						commandResult.setMessage("没有更多数据");
						return HTTP_REQUEST_ERROR;
					}
				}
				return HTTP_REQUEST_ERROR;
			} catch (Exception e) {
				commandResult.setResult(false);
				commandResult.setMessage("异常错误");
				return HTTP_REQUEST_ERROR;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			switch (result) {
			case HTTP_REQUEST_SUCCESS:
				sub3NewAdapter.addNews(innerList);
				sub3NewAdapter.notifyDataSetChanged();
				break;
			case HTTP_REQUEST_ERROR:
				if (Util.isNetworkConnected(mContext)&&isCompleted) {
					mPtrlv.getLoadingLayoutProxy().setPullLabel(
							commandResult.getMessage());
					//mPtrlv.getLoadingLayoutProxy().setReleaseLabel("尝试加载最新数据");	
					
				} else {
					Toast.makeText(getActivity(), commandResult.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
				break;
			}

			mPtrlv.onRefreshComplete();
		}

	}

	class MyOnRefreshListener2 implements OnRefreshListener2<ListView> {

		private PullToRefreshListView mPtflv;

		public MyOnRefreshListener2(PullToRefreshListView ptflv) {
			this.mPtflv = ptflv;
		}

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			// 下拉刷新
			refreshView.getLoadingLayoutProxy().setReleaseLabel("放开以刷新数据");			
			String label = DateUtils.formatDateTime(getActivity(),
					System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
							| DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_ABBREV_ALL);
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
			new GetNewsTask2(mPtflv, News.PageSize, parentID, articleType)
					.execute();
			//刷新数据后状态归原
			isCompleted=false;			
			refreshView.getLoadingLayoutProxy().setPullLabel("下拉以刷新");
		
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			
			// 上拉加载		
			// 如果还有更多数据
			// 上拉加载		
			// 如果还有更多数据
			String updatetime = "2099-01-01 11:56:43.157";
			Map<String, String> bottomMap;
			int count = sub3NewAdapter.getCount();
			if(count>0)
			{
			   bottomMap = (Map<String, String>) sub3NewAdapter
					.getItem(count - 1);
		       updatetime = bottomMap.get("UpdateTime");
			}			
			//Log.i("newsub0", count + "：数据集合 最后的updatetime=" + updatetime);
			new GetNewsTask(mPtflv, News.PageSize, parentID, articleType,
					updatetime).execute();

		}
	}

	/**
	 * 下拉请求网络获得新闻信息（刷新）
	 * 
	 * @author brokge
	 */
	class GetNewsTask2 extends AsyncTask<String, Void, Integer> {

		List<Map<String, String>> innerList = new ArrayList<Map<String, String>>();
		CommandResult commandResult = new CommandResult(false, "请检查网络");
		int PageSize;
		String Parentid;
		String Articletype;
		private PullToRefreshListView mPtrlv;

		public GetNewsTask2(PullToRefreshListView ptrlv, int pagesize,
				String parentid, String articletype) {
			this.mPtrlv = ptrlv;
			PageSize = pagesize;
			Parentid = parentid;
			Articletype = articletype;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				if (Util.isNetworkConnected(mContext)) {
					// 加载网络数据，并添加到本地库中,事先删除本地库的相关数据
					innerList = Article.getListOfColumn(Parentid, PageSize, "");
					if (innerList!=null&&innerList.size() > 0) {
						DBnewshelper dBnewshelper = new DBnewshelper(mContext);
						dBnewshelper.Delete("ArticleType='" + Articletype
								+ "' and ParentID=" + Parentid);
						dBnewshelper.Insert(innerList, Articletype, Parentid);
						commandResult.setResult(true);
						commandResult.setMessage("远程数据加载成功");
						return HTTP_REQUEST_SUCCESS;
					} else {
						commandResult.setResult(false);
						commandResult.setMessage("远程数据加载为空");
						return HTTP_REQUEST_ERROR;
					}
				} 
				else {
					commandResult.setResult(false);
					commandResult.setMessage("请检查网络连接");
					return HTTP_REQUEST_ERROR;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				commandResult.setResult(false);
				commandResult.setMessage("异常错误");
				return HTTP_REQUEST_ERROR;
			}
		}
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			switch (result) {
			case HTTP_REQUEST_SUCCESS:
				Map<String, String> headerMap = innerList.get(0);
				News inews = News.getInstance();
				if (inews!=null) {
					inews.setNewNotice(parentID, View.GONE);
				}
				if (headerMap.get("BigImage") != null
						&& !headerMap.get("BigImage").equals("")) {
					innerList.remove(0);
					/*
					 * FrameLayout frameLayout = (FrameLayout) LayoutInflater
					 * .from(mContext).inflate( R.layout.news_frame_headertitle,
					 * null); ImageView imageView = (ImageView) frameLayout
					 * .findViewById(R.id.news_headerImage); int width =
					 * DisplayParams.getInstance(mContext).screenWidth;
					 * ViewGroup.LayoutParams lps = imageView.getLayoutParams();
					 * lps.height = width / 2; lps.width = width;
					 * imageView.setLayoutParams(lps); String imgurl =
					 * headerMap.get("BigImage"); String imagePath =
					 * Util.getDissertationImgPath(); if
					 * (imgurl.toLowerCase().startsWith("http://")) {
					 * ImageManager.from(mContext).displayImage(imageView,
					 * imgurl, R.drawable.promotion_def_pic, width, width / 2);
					 * } else { // 设置图片的路径
					 * ImageManager.from(mContext).displayImage(imageView,
					 * imagePath + imgurl, R.drawable.promotion_def_pic, width,
					 * width / 2); } TextView titleView = (TextView) frameLayout
					 * .findViewById(R.id.news_headerTitle);
					 * titleView.setText(headerMap.get("Title")); TextView
					 * typeView = (TextView) frameLayout
					 * .findViewById(R.id.news_headerType);
					 * typeView.setText("置顶资讯"); final String ArticleID =
					 * headerMap.get("ArticleID") .toString(); final String
					 * updatetimeString = headerMap.get("UpdateTime")
					 * .toString(); imageView.setOnClickListener(new
					 * OnClickListener() {
					 * 
					 * @Override public void onClick(View v) { Log.i("newsub0",
					 * v.getTag().toString());
					 * startDetailActivity(getActivity(), ArticleID,
					 * "国外新闻头条马航失踪", articleType, updatetimeString); } });
					 */
					UPrefreshLoadData2(headerMap, innerList);
				} else {
					UPrefreshLoadData2(null, innerList);
				}
				break;
			case HTTP_REQUEST_ERROR:
				Toast.makeText(mContext, commandResult.getMessage(),
						Toast.LENGTH_SHORT).show();
				break;
			}
			mPtrlv.onRefreshComplete();
		}
	}

	class AdvAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return advs.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(advs.get(position));
		}

		@Override
		public Object instantiateItem(View container, final int position) {
			((ViewPager) container).addView(advs.get(position));
			/*
			 * advs.get(position).setOnClickListener(new OnClickListener() {
			 * 
			 * @Override public void onClick(View v) { // TODO Auto-generated
			 * method stub switch (position) { case 0:
			 * Toast.makeText(getBaseContext(), "您点击的是图片"+position+1, 1).show();
			 * break; case 1: Toast.makeText(getBaseContext(),
			 * "您点击的是图片"+position+1, 1).show(); break; default:
			 * Toast.makeText(getBaseContext(), "您点击的是图片"+position+1, 1).show();
			 * break; } } });
			 */
			return advs.get(position);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.i("newsub0", " onSaveInstanceState");

	}

	public void reLoadDataNoticeChange(List<Map<String, String>> ListData) {
		sub3NewAdapter.addNews(ListData);
		sub3NewAdapter.notifyDataSetChanged();
	}
	public void  OnlySetAdapter(NewListAdapter adapter) {	
		sub3NewAdapter = adapter;
		refreshListView.setAdapter(sub3NewAdapter);
	}

	public void addHeaderView(Map<String, String> headerMap,
			NewListAdapter adapter) {
		headerView = CreateHeaderView(headerMap);
		refreshListView.getRefreshableView().addHeaderView(headerView, null,
				false);
		sub3NewAdapter = adapter;
		refreshListView.setAdapter(sub3NewAdapter);
		//sub3NewAdapter.notifyDataSetChanged();
	}

	public void UPrefreshLoadData(View view, List<Map<String, String>> list) {
		refreshListView.getRefreshableView().removeHeaderView(headerView);
		if (view != null) {
			headerView = view;
			refreshListView.getRefreshableView().addHeaderView(view, null,
					false);
		}
		sub3NewAdapter.clearList();
		sub3NewAdapter.addNews(list);		
		sub3NewAdapter.notifyDataSetChanged();
	}

	public void UPrefreshLoadData2(Map<String, String> headerMap,
			List<Map<String, String>> list) {
		if(headerMap!=null)
		{		
			/*if(headerView!=null)
			{
			headerView.setVisibility(View.VISIBLE);
			headerView.findViewById(R.id.news_headerImage).setVisibility(View.VISIBLE);
			headerView.findViewById(R.id.news_header_line_title).setVisibility(View.VISIBLE);
			}*/
		    UpdateHeaderView(headerView,headerMap,list);
		}
		else {
			if(headerView!=null)
			{
				headerView.setVisibility(View.GONE);
				headerView.findViewById(R.id.news_headerImage).setVisibility(View.GONE);
				headerView.findViewById(R.id.news_header_line_title).setVisibility(View.GONE);
			}
		}
		sub3NewAdapter.clearList();
		sub3NewAdapter.addNews(list);
		sub3NewAdapter.notifyDataSetChanged();
	}

	private void UpdateHeaderView(View headView, Map<String, String> headerMap,List<Map<String, String>> innerList) {
		if(headView!=null)
		{
			ImageView imageView = (ImageView) headView
					.findViewById(R.id.news_headerImage);
			int width = DisplayParams.getInstance(mContext).screenWidth;
			ViewGroup.LayoutParams lps = imageView.getLayoutParams();
			lps.height = width / 2;
			lps.width = width;
			imageView.setLayoutParams(lps);
			String imgurl = headerMap.get("BigImage");
			String imagePath = Util.getNewsImgPath();
		/*	if (imgurl.toLowerCase().startsWith("http://")) {
				ImageManager.from(mContext).displayImage(imageView, imgurl,
						R.drawable.promotion_def_pic, width, width / 2);
			} else {			
				ImageManager.from(mContext).displayImage(imageView,
						imagePath + imgurl, R.drawable.promotion_def_pic, width,
						width / 2);
			}*/
			if (imgurl.toLowerCase().startsWith("http://")||imgurl.equals("")) {				
			} else {			
				imgurl=imagePath + imgurl;
			}
			BaseLoadImage(mContext,imgurl,imageView);
			
			TextView titleView = (TextView) headView
					.findViewById(R.id.news_headerTitle);
			titleView.setText(headerMap.get("Title"));
			TextView typeView = (TextView) headView
					.findViewById(R.id.news_headerType);
			typeView.setText(R.string.news_header_title_top);
			final String ArticleID = headerMap.get("ArticleID").toString();
			final String updatetimeString = headerMap.get("UpdateTime").toString();
			final String Title=headerMap.get("Title").toString();
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {				
					startDetailActivity(getActivity(), ArticleID, Title,
							News.articleType.toString(), updatetimeString);
				}
			});
		 }
		else {
			NewListAdapter subNewAdapter=new NewListAdapter(mContext, innerList);
			addHeaderView(headerMap, subNewAdapter);
		}
	}

	public View CreateHeaderView(Map<String, String> headerMap) {
		FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.news_frame_headertitle, null);
		ImageView imageView = (ImageView) frameLayout
				.findViewById(R.id.news_headerImage);
		int width = DisplayParams.getInstance(mContext).screenWidth;
		ViewGroup.LayoutParams lps = imageView.getLayoutParams();
		lps.height = width / 2;
		lps.width = width;
		imageView.setLayoutParams(lps);
		String imgurl = headerMap.get("BigImage");
		String imagePath = Util.getNewsImgPath();
		/*if (imgurl.toLowerCase().startsWith("http://")) {
			ImageManager.from(mContext).displayImage(imageView, imgurl,
					R.drawable.promotion_def_pic, width, width / 2);
		} else {
			// 设置图片的路径
			ImageManager.from(mContext).displayImage(imageView,
					imagePath + imgurl, R.drawable.promotion_def_pic, width,
					width / 2);
		}*/
		if (imgurl.toLowerCase().startsWith("http://")) {				
		} else {			
			imgurl=imagePath + imgurl;
		}
		BaseLoadImage(mContext,imgurl,imageView);
		TextView titleView = (TextView) frameLayout
				.findViewById(R.id.news_headerTitle);
		titleView.setText(headerMap.get("Title"));
		TextView typeView = (TextView) frameLayout
				.findViewById(R.id.news_headerType);
		typeView.setText(R.string.news_header_title_top);
		final String ArticleID = headerMap.get("ArticleID").toString();
		final String updatetimeString = headerMap.get("UpdateTime").toString();
		final String Title = headerMap.get("Title").toString();
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				startDetailActivity(getActivity(), ArticleID, Title,
						News.articleType.toString(), updatetimeString);
			}
		});

		return frameLayout;
	}

}
