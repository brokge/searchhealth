package coolbuy360.searchhealth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import coolbuy360.adapter.NewListAdapter;
import coolbuy360.dbhelper.DBnewshelper;
import coolbuy360.logic.Article;
import coolbuy360.pulltorefresh.PullToRefreshBase;
import coolbuy360.pulltorefresh.PullToRefreshListView;
import coolbuy360.pulltorefresh.PullToRefreshBase.Mode;
import coolbuy360.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import coolbuy360.service.CommandResult;
import coolbuy360.service.DisplayParams;
import coolbuy360.service.Util;
import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class NewsSubTest extends BaseFragment {
	public static final int HTTP_REQUEST_SUCCESS = -1;
	public static final int HTTP_REQUEST_ERROR = 0;
	public static final int VIEWVISIBLE = 0;
	public static final int VIEWGONE = 1;
	public View view;
	public View headerView;
	private NewListAdapter subNewAdapter = null;
	private Map<Integer, Integer> postionMap = new HashMap<Integer, Integer>();
	private PullToRefreshListView refreshListView;
	public Context mContext;
	String articleType;
	String parentID;
	Boolean isCompleted = false;
	public static final int NO_LOADED = 0;
	public static final int LOADING = 1;
	public static final int LOADED = 2;
	public static final int LOADERROR = 3;
	public static NewsSubTest newsSubTest;
	public int Postion;
    public String errorMessageString;
    public boolean isError;
	// LinearLayout async_begin;
	LinearLayout async_error;
	View listview_empty_error;
	View LoadingView;

	public NewsSubTest(Context context, String articltype, String parentid,
			int postion) {
		mContext = context;
		articleType = articltype;
		parentID = parentid;
		Postion = postion;
		postionMap.put(postion, NO_LOADED);
	}

	public NewsSubTest(Context context, String articltype, String parentid) {
		mContext = context;
		articleType = articltype;
		parentID = parentid;
		postionMap.put(Integer.parseInt(parentID) - 1, NO_LOADED);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.basefragment_listview, null);
		refreshListView = (PullToRefreshListView) view
				.findViewById(R.id.ptrlvSubNews);

		/*
		 * ImageView imgview=new ImageView(mContext); Bitmap bitmap =
		 * BitmapFactory.decodeResource( mContext.getResources(),
		 * R.drawable.about_logo_img); Bitmap resizedBitmap =
		 * Bitmap.createScaledBitmap(bitmap , 160, 65, true);
		 * imgview.setImageDrawable(new BitmapDrawable(resizedBitmap));
		 */
		async_error = (LinearLayout) view.findViewById(R.id.async_error);
		LayoutInflater layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listview_empty_error = layoutInflater.inflate(
				R.layout.listview_emptyview_error, null);
		LayoutInflater minflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LoadingView = minflater.inflate(
				R.layout.listview_emptyview_loading, null);
		refreshListView.setEmptyView(LoadingView);
		initPullToRefreshListView(refreshListView, subNewAdapter);
		return view;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		if (postionMap.get(Postion) == NO_LOADED) {				
			postionMap.put(Postion, LOADING);
			
			new GetNewsTaskFrist(articleType, parentID, News.PageSize)
					.execute();
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
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
		rtflv.setOnRefreshListener(new MyOnRefreshListener(rtflv));
		rtflv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View View,
					int position, long arg3) {

				Map<String, String> maplist = (Map<String, String>) parent
						.getItemAtPosition(position);

				TextView titleTextView = (TextView) View
						.findViewById(R.id.tvTitle);
				titleTextView.setTextColor(getResources().getColor(
						R.color.news_item_content));
				startDetailActivity(getActivity(), maplist.get("ArticleID"),
						maplist.get("Title"), articleType,
						maplist.get("UpdateTime"));
			}
		});
		if (subNewAdapter != null) {
			if (headerView != null) {
				rtflv.getRefreshableView().addHeaderView(headerView, null,
						false);
			}
			rtflv.setMode(Mode.BOTH);
			rtflv.setAdapter(subNewAdapter);
		}
		if(postionMap.get(Postion) ==LOADERROR)
		{
			ListViewEmptyError(errorMessageString,
					new refreshOnclick());			
		}
		if(isError)
		{
			setAsync_error(VIEWVISIBLE, errorMessageString);			
		}
		
	}

	class MyOnRefreshListener implements OnRefreshListener2<ListView> {
		private PullToRefreshListView mPtflv;

		public MyOnRefreshListener(PullToRefreshListView ptflv) {
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
			new GetDownNewsTask(mPtflv, News.PageSize, parentID, articleType)
					.execute();
			// 刷新数据后状态归原
			isCompleted = false;
			refreshView.getLoadingLayoutProxy().setPullLabel("下拉以刷新");
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			// 上拉加载
			// 如果还有更多数据
			String updatetime = "2099-01-01 11:56:43.157";
			Map<String, String> bottomMap;
			int count = subNewAdapter.getCount();
			if (count > 0) {
				bottomMap = (Map<String, String>) subNewAdapter
						.getItem(count - 1);
				updatetime = bottomMap.get("UpdateTime");
			}
			new GetUpNewsTask(mPtflv, News.PageSize, parentID, articleType,
					updatetime).execute();

		}
	}

	/**
	 * 外部调用刷新页面数据
	 */
	public void RefreshData() {
		isCompleted = false;
		refreshListView.autoRefresh();
	}

	/**
	 * 下拉请求网络获得新闻信息（刷新）
	 * 
	 * @author brokge
	 */
	class GetDownNewsTask extends AsyncTask<String, Void, Integer> {

		List<Map<String, String>> innerList = new ArrayList<Map<String, String>>();
		CommandResult commandResult = new CommandResult(false, "请检查网络");
		int PageSize;
		String Parentid;
		String Articletype;
		private PullToRefreshListView mPtrlv;

		public GetDownNewsTask(PullToRefreshListView ptrlv, int pagesize,
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
					DBnewshelper dBnewshelper = new DBnewshelper(mContext);
					String lastTimestamp = dBnewshelper.GetLastNewsTimestamp(
							Articletype, Parentid);
					if (lastTimestamp != null && !(lastTimestamp.equals(""))) {
						lastTimestamp = lastTimestamp + "@0";
					} else {
						lastTimestamp = "";
					}
					// 加载网络数据，并添加到本地库中,事先删除本地库的相关数据
					innerList = Article.getListOfColumn(Parentid, PageSize,
							lastTimestamp);
					if (innerList == null) {
						commandResult.setResult(false);
						commandResult.setMessage("远程数据加载为空");
						return HTTP_REQUEST_ERROR;
					} else if (innerList != null && innerList.size() > 0) {
						dBnewshelper.Delete("ArticleType='" + Articletype
								+ "' and ParentID=" + Parentid);
						dBnewshelper.Insert(innerList, Articletype, Parentid);
						commandResult.setResult(true);
						commandResult.setMessage("远程数据加载成功");
						return HTTP_REQUEST_SUCCESS;
					} else {
						commandResult.setResult(false);
						commandResult.setMessage("没有新资讯");
						return HTTP_REQUEST_ERROR;
					}
				} else {
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
				News inews = News.getInstance();
				if (inews != null) {
					inews.setNewNotice(parentID, View.GONE);
				}
				RefreshLoadData(innerList);
				// async_error.getVisibility()
				setAsync_error(VIEWGONE, commandResult.getMessage());
				break;
			case HTTP_REQUEST_ERROR:
				if (!commandResult.getMessage().equals("没有新资讯")) {
					Toast.makeText(mContext, commandResult.getMessage(),
							Toast.LENGTH_SHORT).show();					
					setAsync_error(VIEWVISIBLE, commandResult.getMessage());
				} else {
					setAsync_error(VIEWGONE, commandResult.getMessage());
				}
				break;
			}
			mPtrlv.onRefreshComplete();
			mPtrlv.refreshComplate();
		}
	}

	/**
	 * 上拉请求网络获得新闻信息（添加）
	 * 
	 * @author brokge
	 * 
	 */
	class GetUpNewsTask extends AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerList = new ArrayList<Map<String, String>>();
		CommandResult commandResult = new CommandResult(false, "请检查网络");
		int PageSize;
		String Parentid;
		String Articletype;
		private PullToRefreshListView mPtrlv;
		String Updatetime;

		public GetUpNewsTask(PullToRefreshListView ptrlv, int pagesize,
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
						if (innerList == null || innerList.size() <= 0) {
							innerList = Article.getListOfColumn(Parentid,
									PageSize, Updatetime);
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
				subNewAdapter.addNews(innerList);
				subNewAdapter.notifyDataSetChanged();
				break;
			case HTTP_REQUEST_ERROR:
				if (Util.isNetworkConnected(mContext) && isCompleted) {
					mPtrlv.getLoadingLayoutProxy().setPullLabel(
							commandResult.getMessage());
				} else {
					Toast.makeText(getActivity(), commandResult.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
				break;
			}

			mPtrlv.onRefreshComplete();
		}

	}

	private void RefreshLoadData(List<Map<String, String>> list) {
		Map<String, String> headerMap = list.get(0);
		// 如果存在大图
		if (headerMap.get("BigImage") != null
				&& !headerMap.get("BigImage").equals("")) {
			list.remove(0);
			UpdateHeadViewVisible(VIEWVISIBLE);
			UpdateHeadViewData(headerMap);
		} else {
			UpdateHeadViewVisible(VIEWGONE);
		}
		subNewAdapter.clearList();
		subNewAdapter.addNews(list);
		subNewAdapter.notifyDataSetChanged();
	}

	private void UpdateHeadViewVisible(int visible) {
		if (headerView == null) {
			CreateHeaderView();
		}
		switch (visible) {
		case VIEWVISIBLE:
			headerView.findViewById(R.id.news_headerImage).setVisibility(
					View.VISIBLE);
			headerView.findViewById(R.id.news_header_line_title).setVisibility(
					View.VISIBLE);
			break;
		case VIEWGONE:
			headerView.findViewById(R.id.news_headerImage).setVisibility(
					View.GONE);
			headerView.findViewById(R.id.news_header_line_title).setVisibility(
					View.GONE);
			break;
		default:
			break;
		}
	}

	/**
	 * 先创建headerview
	 * 
	 * @param headerMap
	 */
	public void CreateHeaderView() {
		FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.news_frame_headertitle, null);
		headerView = frameLayout;
	}

	/**
	 * 刷新完成如果有大图数据 更新headview
	 * 
	 * @param headerMap
	 */
	private void UpdateHeadViewData(Map<String, String> headerMap) {
		ImageView imageView = (ImageView) headerView
				.findViewById(R.id.news_headerImage);
		int width = DisplayParams.getInstance(mContext).screenWidth;
		ViewGroup.LayoutParams lps = imageView.getLayoutParams();
		lps.height = width / 2;
		lps.width = width;
		imageView.setLayoutParams(lps);
		String imgurl = headerMap.get("BigImage");
		String imagePath = Util.getNewsImgPath();
		if (imgurl.toLowerCase().startsWith("http://") || imgurl.equals("")) {
		} else {
			imgurl = imagePath + imgurl;
		}
		BaseLoadImage(mContext, imgurl, imageView);

		TextView titleView = (TextView) headerView
				.findViewById(R.id.news_headerTitle);
		titleView.setText(headerMap.get("Title"));
		TextView typeView = (TextView) headerView
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
	}

	/**
	 * 第一次加载时的相关操作
	 * 
	 * @param list
	 */
	public void FristLoadData(List<Map<String, String>> list) {		
		CreateHeaderView();
		refreshListView = (PullToRefreshListView) view
				.findViewById(R.id.ptrlvSubNews);
		refreshListView.getRefreshableView().addHeaderView(headerView);
		Map<String, String> headerMap = list.get(0);
		// 如果存在大图
		if (headerMap.get("BigImage") != null
				&& !headerMap.get("BigImage").equals("")) {
			list.remove(0);
			UpdateHeadViewVisible(VIEWVISIBLE);
			UpdateHeadViewData(headerMap);
			subNewAdapter = new NewListAdapter(mContext, list);
		} else {
			subNewAdapter = new NewListAdapter(mContext, list);
			UpdateHeadViewVisible(VIEWGONE);
		}
		refreshListView.setMode(Mode.BOTH);
		refreshListView.setAdapter(subNewAdapter);
	
		// initPullToRefreshListView(refreshListView, subNewAdapter);
	}

	/**
	 * 从本地抓取或从网络获得新闻信息
	 * 
	 * @author brokge
	 * 
	 */
	public class GetNewsTaskFrist extends AsyncTask<String, Void, Integer> {
		CommandResult commandResult = new CommandResult(false, "请检查网络");
		int PageSize;
		String Parentid;
		String Articletype;
		List<Map<String, String>> innerList = new ArrayList<Map<String, String>>();
		Boolean isFromServer = true;

		/**
		 * 从本地抓取或从网络获得新闻信息
		 * 
		 * @param articletype
		 *            文章类型：资讯频道或者还是专题
		 * @param parentid
		 *            频道id
		 * @param pagesize
		 *            每页的条数
		 */
		public GetNewsTaskFrist(String articletype, String parentid,
				int pagesize) {
			PageSize = pagesize;
			Parentid = parentid;
			Articletype = articletype;

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			listview_empty_error.setVisibility(View.GONE);
			LoadingView.setVisibility(View.VISIBLE);
			refreshListView.setMode(Mode.DISABLED);
		}

		@Override
		protected Integer doInBackground(String... params) {
			DBnewshelper dBnewshelper = new DBnewshelper(mContext);
			try {
				if (Util.isNetworkConnected(mContext)) {
					// try {
					// Thread.sleep(5000);
					// 第一次进入，先加载本地数据，如果本地数据为空则加载远程数据，加载完成后写入本地库

					innerList = dBnewshelper.GetList(Articletype, Parentid,
							PageSize, "");
					if (innerList == null || innerList.size() <= 0) {
						innerList = Article.getListOfColumn(Parentid, PageSize,
								"");
						if (innerList != null && innerList.size() > 0) {
							// 如果从服务器加载成功，则开始写入数据到本地库
							dBnewshelper.Insert(innerList, Articletype,
									Parentid);
							commandResult.setResult(true);
							commandResult.setMessage("远程数据加载成功");
							return HTTP_REQUEST_SUCCESS;
						} else {
							commandResult.setResult(false);
							commandResult.setMessage("远程数据加载失败");
							return HTTP_REQUEST_ERROR;
						}
					} else {
						commandResult.setResult(true);
						commandResult.setMessage("本地缓存数据加载成功1");
						isFromServer = false;
						return HTTP_REQUEST_SUCCESS;
					}
				} else {
					innerList = dBnewshelper.GetList(Articletype, Parentid,
							PageSize, "");
					if (innerList != null && innerList.size() > 0) {
						commandResult.setResult(true);
						commandResult.setMessage("本地缓存数据加载成功2");
						return HTTP_REQUEST_SUCCESS;
					}
					return HTTP_REQUEST_ERROR;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				commandResult.setResult(false);
				commandResult.setMessage("远程数据加载失败");
				return HTTP_REQUEST_ERROR;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			switch (result) {
			case HTTP_REQUEST_SUCCESS:
				postionMap.put(Postion, LOADED);
				FristLoadData(innerList);
				if (!isFromServer) {
					RefreshData();
				}
				LoadingView.setVisibility(View.GONE);
				break;

			case HTTP_REQUEST_ERROR:
				postionMap.put(Postion, LOADERROR);	
				Toast.makeText(mContext, commandResult.getMessage(),
						Toast.LENGTH_SHORT).show();
				ListViewEmptyError(commandResult.getMessage(),
						new refreshOnclick());
				// todo: 此处添加网络请求错误相关代码
				break;
			}
		}

	}

	/**
	 * listview为空时的的相关更改
	 * 
	 * @param message
	 * @param onclick
	 */
	public void ListViewEmptyError(int message, OnClickListener onclick) {
		
		LoadingView.setVisibility(View.GONE);
		refreshListView.setEmptyView(listview_empty_error);
		TextView txtMessageView = (TextView) listview_empty_error
				.findViewById(R.id.listview_empty_message);
		txtMessageView.setText(message);
		Button btnRefreshViewButton = (Button) listview_empty_error
				.findViewById(R.id.listview_empty_refresh);
		btnRefreshViewButton.setOnClickListener(onclick);
	}

	/**
	 * listview为空时的的相关更改
	 * 
	 * @param message
	 * @param onclick
	 */
	public void ListViewEmptyError(String message, OnClickListener onclick) {
		errorMessageString=message;
		LoadingView.setVisibility(View.GONE);
		refreshListView.setEmptyView(listview_empty_error);
		TextView txtMessageView = (TextView) listview_empty_error
				.findViewById(R.id.listview_empty_message);
		txtMessageView.setText(message);
		Button btnRefreshViewButton = (Button) listview_empty_error
				.findViewById(R.id.listview_empty_refresh);
		btnRefreshViewButton.setOnClickListener(onclick);
	}

	public class refreshOnclick implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			// async_error.setVisibility(View.GONE);
		
			// async_begin.setVisibility(View.VISIBLE);
			// new AsyncLoader_Refresh().execute();
			new GetNewsTaskFrist(articleType, parentID, News.PageSize)
					.execute();
		}
	}

	public void setAsync_error(int diplay, String message) {
		errorMessageString=message;
		switch (diplay) {
		case VIEWGONE:
			isError=false;
			async_error.setVisibility(View.GONE);
			break;
		case VIEWVISIBLE:
			isError=true;
			async_error.setVisibility(View.VISIBLE);
			Button refreshButton = (Button) async_error
					.findViewById(R.id.async_error_reflesh);
			TextView messageTextView = (TextView) async_error
					.findViewById(R.id.async_error_txt);
			messageTextView.setText(message);
			refreshButton.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					RefreshData();
				}
			});
			break;
		default:
			break;
		}

	}

}
