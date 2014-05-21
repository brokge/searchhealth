/**
 * 
 */
package coolbuy360.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


//import coolbuy360.service.OnWebViewImageListener;


import coolbuy360.searchhealth.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * @author yangxc
 *
 */
public class CommonMethod {
	
	/**
	 * 设置布局区域的OnPress事件捕获
	 */
	public final static class setOnPressed implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setPressed(true);
				break;
			/*
			 * case MotionEvent.ACTION_UP: v.setPressed(false); break;
			 */
			}
			return false;
		}
	}
	
	/**
	 * 选择器选择值后，编辑界面获取到选择的值，并将值显示在界面上，同时处理对应控件的外观
	 * @param textView
	 * @param text
	 */
	public static void setSelectFieldText(TextView textView, String text)
	{
		textView.setText(text);
		float size = searchApp.getInstance().getResources()
				.getDimensionPixelSize(R.dimen.setting_item_selected_textsize);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		textView.setTextColor(searchApp.getInstance().getResources()
				.getColor(R.color.setting_item_selected_textcolor));
	}
	
	/**
	 * 移除界面上的焦点
	 * @param activity
	 */
	public static void clearFocuse(Activity activity) {
		try {
			activity.getCurrentFocus().clearFocus();
		} catch (Exception e) {
		}
	}
	
	public static List<Map<String, String>> getDiseaseNamesSource()
	{
		List<Map<String, String>> sourceList = new ArrayList<Map<String, String>>();
		sourceList.add(new HashMap<String, String>(){{ put("value", "1");put("text", "高血压");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "2");put("text", "糖尿病");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "3");put("text", "冠心病");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "4");put("text", "恶性肿瘤");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "5");put("text", "脑中风");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "6");put("text", "肺炎");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "7");put("text", "结核病");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "8");put("text", "精神分裂症");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "9");put("text", "肝炎");}});
		return sourceList;
	}
	
	public static List<Map<String, String>> getAllergenSource()
	{
		List<Map<String, String>> sourceList = new ArrayList<Map<String, String>>();
		sourceList.add(new HashMap<String, String>(){{ put("value", "1");put("text", "青霉素");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "2");put("text", "磺胺");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "3");put("text", "链霉素");}});
		return sourceList;
	}
	
	public static List<Map<String, String>> getYesNoSource()
	{
		List<Map<String, String>> sourceList = new ArrayList<Map<String, String>>();
		sourceList.add(new HashMap<String, String>(){{ put("value", "1");put("text", "是");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "0");put("text", "否");}});		
		return sourceList;
	}
	
	public static List<Map<String, String>> getSexNamesSource()
	{
		List<Map<String, String>> sourceList = new ArrayList<Map<String, String>>();
		sourceList.add(new HashMap<String, String>(){{ put("value", "1");put("text", "男");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "0");put("text", "女");}});		
		return sourceList;
	}
	
	public static List<Map<String, String>> getJobNamesSource()
	{
		List<Map<String, String>> sourceList = new ArrayList<Map<String, String>>();
		sourceList.add(new HashMap<String, String>(){{ put("value", "1");put("text", "工人");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "2");put("text", "离退休者");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "3");put("text", "专业技术人员");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "4");put("text", "行政管理者");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "5");put("text", "办事人员");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "6");put("text", "军人");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "7");put("text", "企业家");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "8");put("text", "商业服务业员工");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "9");put("text", "学生");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "10");put("text", "农村居民");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "11");put("text", "其它");}});
		return sourceList;
	}
	
	public static List<Map<String, String>> getEducationLevelSource()
	{
		List<Map<String, String>> sourceList = new ArrayList<Map<String, String>>();
		sourceList.add(new HashMap<String, String>(){{ put("value", "1");put("text", "文盲及半文盲");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "2");put("text", "小学");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "3");put("text", "初中");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "4");put("text", "高中");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "5");put("text", "中专");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "6");put("text", "大专");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "7");put("text", "本科及以上");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "8");put("text", "不详");}});		
		return sourceList;
	}
	
	public static List<Map<String, String>> getHcSource()
	{
		List<Map<String, String>> sourceList = new ArrayList<Map<String, String>>();
		sourceList.add(new HashMap<String, String>(){{ put("value", "1");put("text", "正常");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "2");put("text", "冻结");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "3");put("text", "无");}});			
		return sourceList;
	}
	
	public static List<Map<String, String>> getBuyWaySource()
	{
		List<Map<String, String>> sourceList = new ArrayList<Map<String, String>>();
		sourceList.add(new HashMap<String, String>(){{ put("value", "1");put("text", "药店");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "2");put("text", "医院");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "3");put("text", "网上");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "3");put("text", "其它");}});	
		return sourceList;
	}
	
	/**
	 * 添加网页的点击图片展示支持
	 */
	//@SuppressLint("SetJavaScriptEnabled")
	@SuppressLint("JavascriptInterface")
	public static void addWebImageShow(final Context cxt, WebView wv) {
		wv.getSettings().setJavaScriptEnabled(true);
		wv.addJavascriptInterface(new OnWebViewImageListener() {

			
			@Override	
			public void onImageClick(String bigImageUrl) {
				if (bigImageUrl != null)
					CommonMethod.showImageZoomDialog(cxt, bigImageUrl);
			}
		}, "mWebViewImageListener");
	}
		
	public static void showImageZoomDialog(Context context, String imgUrl) {
		Intent intent = new Intent(context, ImageZoomDialog.class);
		intent.putExtra("img_url", imgUrl);
		context.startActivity(intent);
	}
	/**
	 * 获取默认的栏目列表
	 */
	public static List<Map<String, String>> getDefaultChanel() {
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
	public  static int getColumnIndex(String columnid) {
		int index = 0;		
		 List<Map<String, String>> Chanel=getDefaultChanel() ;
		for (Map<String, String> columnitem : Chanel) {
			if (columnid.equals(columnitem.get("ColumnID"))) {
				return index;
			} else {
				index++;
			}
		}
		return 100;
	}
	
}
