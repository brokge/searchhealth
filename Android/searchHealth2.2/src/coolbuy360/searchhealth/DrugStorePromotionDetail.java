/**
 * 
 */
package coolbuy360.searchhealth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import coolbuy360.control.CusProgressDialog;
import coolbuy360.logic.Promotion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;
import coolbuy360.service.CommonMethod;
/**
 * @author Administrator
 * 
 */
public class DrugStorePromotionDetail extends Activity {

	// String promotionId = null;
	private CusProgressDialog cusProgressDialog;
	private WebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drugstore_promotion_detail);
		Bundle bundle = getIntent().getExtras();
		String promotionId = bundle.getLong("promotionid") + "";
		//Toast.makeText(DrugStorePromotionDetail.this, promotionId+"", 1).show();
		webview = (WebView) this.findViewById(R.id.promotion_webview);
	    WebSettings settings = webview.getSettings(); 
	    settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
	    CommonMethod.addWebImageShow(this, webview);
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);

		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DrugStorePromotionDetail.this.finish();
			}
		});
		new asyloadData().execute(promotionId);
	}
	/**
	 * ��ʼ��webview������
	 * @param list
	 *        �첽��ȡ��listֵ
	 */
	@SuppressLint("NewApi")
	private void initWebView(List<Map<String, String>> list) {

		String webData = list.get(0).get("detail");
		// webview.loadData(webData, mimeType, encoding)
		Log.i("chenlinwei", webData);
		// webview.loadData(webData, "text/html", "utf-8");
		String html = "";
		html = "<html>" + "<body>" + Html.fromHtml(webData) + "</body>"
				+ "</html>";
		
		html=html.replaceAll("(<img[^>]+src=\")(\\S+)\"",
		"$1$2\" onClick=\"javascript:mWebViewImageListener.onImageClick('$2')\"");
		
		webview.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
		webview.setWebChromeClient(new WebChromeClient());
		// ��ͬ�ֱַ��ʵ������,��Ļ�ܶȲ�һ���������,�Զ�����ҳ��:
		/*DisplayMetrics dm = getResources().getDisplayMetrics();
		int scale = dm.densityDpi;*/
		// ��ò�ͬ���ܶ�ֵ
		/*if (scale == 240) { //
			webview.getSettings().setDefaultZoom(ZoomDensity.FAR);
		} else if (scale == 160) {
			webview.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
		} else {
			webview.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
		}*/

		// ����WebView��һЩ���ܵ�:
		webview.getSettings().setJavaScriptEnabled(true);
		// ����scrollbar
		webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webview.setHorizontalScrollBarEnabled(false);
		webview.setVerticalScrollbarOverlay(true);
		// ����֧��֧������
		webview.getSettings().setSupportZoom(false);
		webview.getSettings().setBuiltInZoomControls(false);
		// ��ʼ����ֵ
		//webview.setInitialScale(100);
	}
/**
 * �첽������ҳ����
 * @author Administrator
 *
 */
	private class asyloadData extends AsyncTask<String, Void, Integer> {
		List<Map<String, String>> innerList = new ArrayList<Map<String, String>>();

		@Override
		protected void onPreExecute() {
			cusProgressDialog = new CusProgressDialog(
					DrugStorePromotionDetail.this, R.style.dialog);
			cusProgressDialog.setPregressBar(true);
			cusProgressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			int returnValue = 2;
			if (params.length > 0) {
				try {
					String id = params[0];
					Log.i("chenlinwei", "����id��" + id);
					innerList = Promotion.getInfo(params[0]);
					if (innerList != null && innerList.size() > 0) {
						returnValue = 0;
					} else {
						returnValue = 1;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					returnValue = 2;
				}
			}
			return returnValue;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if (result == 0) {
				// InitViewData(innerList);
				// android.util.Log.i(tag, msg)
				Log.i("chenlinwei", innerList + "");
				cusProgressDialog.cancel();
				initWebView(innerList);

			} else if (result == 1) {
				cusProgressDialog.cancel();
				Toast.makeText(DrugStorePromotionDetail.this, "��ʱû�л��ϸ��Ϣ",
						Toast.LENGTH_LONG).show();
			}

			else {
				cusProgressDialog.cancel();
				Toast.makeText(DrugStorePromotionDetail.this, "���ݼ��ش���",
						Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}

	}

}
