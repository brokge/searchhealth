/**
 * 
 */
package coolbuy360.searchhealth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;



import coolbuy360.dbhelper.DBnewshelper;
import coolbuy360.logic.Article;
import coolbuy360.logic.Article.ArticleType;
import coolbuy360.logic.NoticeStateConfig;
import coolbuy360.service.BuilderGestureExt;
import coolbuy360.service.CommandResult;
import coolbuy360.service.Util;
import coolbuy360.service.searchApp;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import coolbuy360.service.CommonMethod;
/**
 * @author yangxc
 *
 */
public class ArticleDetail extends Activity {

	private String articleid = "";
	private String articletype = "";
	private String title = "";
	private String resume = "";
	private String createtime = "";
	private String updatetime = "";
	CommandResult resultmessage;

	LinearLayout async_begin;
	LinearLayout async_error;
	TextView article_detail_author;
	private WebView webview;
	private GestureDetector gestureDetector;  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article_detail);
		// 为退出做准备
		searchApp.getInstance().addActivity(this);
		Bundle bundle = getIntent().getExtras();
		articleid = bundle.getString("articleid");
		title = bundle.getString("title");
		resume = bundle.getString("resume");
		articletype = bundle.getString("articletype");
		if (articletype != null
				&& articletype.equals(Article.ArticleType.column.toString())) {
			updatetime = bundle.getString("updatetime");
		} else {
			createtime = bundle.getString("createtime");
		}
		
		async_begin = (LinearLayout) findViewById(R.id.async_begin);
		async_error = (LinearLayout) findViewById(R.id.async_error);

		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArticleDetail.this.finish();
				overridePendingTransition(R.anim.push_no,R.anim.push_top_out);
			}
		});

		TextView article_detail_title = (TextView) this
				.findViewById(R.id.article_detail_title);
		article_detail_title.setText(title);
		TextView article_detail_date = (TextView) this
				.findViewById(R.id.article_detail_date);
		if (articletype != null
				&& articletype.equals(Article.ArticleType.column.toString())) {
			article_detail_date.setText(Util
					.getDateFormat(updatetime,
					"yyyy-MM-dd E"));		
		} else {
			article_detail_date.setText(Util
					.getDateFormat(createtime,
					"yyyy-MM-dd E"));			
		}
		article_detail_author = (TextView) this
				.findViewById(R.id.article_detail_author);
		
		webview = (WebView) this.findViewById(R.id.article_detail_webview);
	    WebSettings settings = webview.getSettings(); 
	    settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
	    
	    CommonMethod.addWebImageShow(this, webview);
	    
		new AsyLoadDrugInfo().execute();
		
		GestureEvent();
		webview.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				return gestureDetector.onTouchEvent(event);
			}
		});
		
	}
	private void GestureEvent() {
		gestureDetector = new  BuilderGestureExt(this,new BuilderGestureExt.OnGestureResult() {
            @Override

            public void onGestureResult(int direction) {
               if(direction==2||direction==3)
               {
                //show(Integer.toString(direction));
            	ArticleDetail.this.finish();
   				overridePendingTransition(R.anim.push_no,R.anim.push_right_out);
               }
            	

            }
        }
        ).Buile();	
	}
	 @Override
	 public boolean onTouchEvent(MotionEvent event) {

	        return gestureDetector.onTouchEvent(event);

	    }
	 private void show(String value){

	        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();

	    }
	/**
	 * 异步加载药品信息
	 */
	private class AsyLoadDrugInfo extends AsyncTask<Integer, Void, Integer> {
		List<Map<String, String>> innerlist;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			async_begin.setVisibility(View.VISIBLE);
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub

			try {
				if (articletype != null
						&& articletype.equals(Article.ArticleType.column
								.toString())) {

					Map<String, String> innerMap = new HashMap<String, String>();
					DBnewshelper dBnewshelper = new DBnewshelper(
							ArticleDetail.this);
					innerMap = dBnewshelper.FindByID(Integer
							.parseInt(articleid));
					String webData = "";
					if (innerMap != null) {
						webData = innerMap.get("Detail");
					}
					if (webData != null && !(webData.equals(""))) {
						innerlist = new ArrayList<Map<String, String>>();
						innerlist.add(innerMap);
					} else {
						innerlist = coolbuy360.logic.Article.getInfo(articleid);
						if (innerlist != null && innerlist.size() > 0) {
							dBnewshelper.update(innerlist.get(0), "detail");
						}
					}
				} else {
					innerlist = coolbuy360.logic.Article.getInfo(articleid);
				}
				
				if (innerlist != null) {
					return (innerlist.size() > 0) ? 0 : 1;
				} else {
					return 2;// 网络连接错误
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return 2;
			}
		}
		
		/**
		 * 初始化webview的数据
		 * @param list
		 *        异步获取的list值
		 */
		@SuppressLint("NewApi")
		private void initWebView(Map<String, String> sourceMap) {

			String webData = sourceMap.get("Detail");
			String author = sourceMap.get("Author");
			if (author != null && !(author.equals(""))) {
				article_detail_author.setText(author);
				article_detail_author.setVisibility(View.VISIBLE);
			}
			// webview.loadData(webData, mimeType, encoding)
			// webview.loadData(webData, "text/html", "utf-8");
			String html = "";
			html = "<html>" + "<body>" + Html.fromHtml(webData) + "</body>"
					+ "</html>";			
			
			html=html.replaceAll("(<img[^>]+src=\")(\\S+)\"",
			"$1$2\" onClick=\"javascript:mWebViewImageListener.onImageClick('$2')\"");
			System.out.println(html);
			//Log.i("chenlinwei", html);
			webview.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
			webview.setWebChromeClient(new WebChromeClient());
			// 在同种分辨率的情况下,屏幕密度不一样的情况下,自动适配页面:
			/*DisplayMetrics dm = getResources().getDisplayMetrics();
			int scale = dm.densityDpi;*/
			// 获得不同的密度值
			/*if (scale == 240) { 
				webview.getSettings().setDefaultZoom(ZoomDensity.FAR);
			} else if (scale == 160) {
				webview.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
			} else {
				webview.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
			}*/

			// 设置WebView的一些功能点:
			webview.getSettings().setJavaScriptEnabled(true);
			// 设置scrollbar
			webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
			webview.setHorizontalScrollBarEnabled(false);
			webview.setVerticalScrollbarOverlay(true);
			// 设置支不支持缩放
			webview.getSettings().setSupportZoom(false);
			webview.getSettings().setBuiltInZoomControls(false);
			// 初始缩放值
			//webview.setInitialScale(100);
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if (result == 0) {			
				initWebView(innerlist.get(0));				
				async_begin.setVisibility(View.GONE);
			} else if (result == 1) {
				async_begin.setVisibility(View.GONE);
				LinearLayout async_error = (LinearLayout) findViewById(R.id.async_error);
				TextView async_error_txt = (TextView) async_error
						.findViewById(R.id.async_error_txt);
				async_error_txt.setText("没有找到对应的报道。");
				Button async_error_reflesh = (Button) async_error
						.findViewById(R.id.async_error_reflesh);
				async_error_reflesh.setVisibility(View.GONE);
				async_error.setVisibility(View.VISIBLE);
			} else if (result == 2) {
				async_begin.setVisibility(View.GONE);
				async_error.setVisibility(View.VISIBLE);
				TextView async_error_txt = (TextView) async_error
						.findViewById(R.id.async_error_txt);
				async_error_txt.setText(R.string.error_nonetwork);
				Button async_error_reflesh = (Button) async_error
						.findViewById(R.id.async_error_reflesh);
				async_error_reflesh.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						async_error.setVisibility(View.GONE);
						async_begin.setVisibility(View.VISIBLE);
						new AsyLoadDrugInfo().execute();
					}
				});
				return;
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	

	
	
	
	
	
	
	
}
