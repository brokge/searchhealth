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
	 * ���ò��������OnPress�¼�����
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
	 * ѡ����ѡ��ֵ�󣬱༭�����ȡ��ѡ���ֵ������ֵ��ʾ�ڽ����ϣ�ͬʱ�����Ӧ�ؼ������
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
	 * �Ƴ������ϵĽ���
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
		sourceList.add(new HashMap<String, String>(){{ put("value", "1");put("text", "��Ѫѹ");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "2");put("text", "����");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "3");put("text", "���Ĳ�");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "4");put("text", "��������");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "5");put("text", "���з�");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "6");put("text", "����");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "7");put("text", "��˲�");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "8");put("text", "�������֢");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "9");put("text", "����");}});
		return sourceList;
	}
	
	public static List<Map<String, String>> getAllergenSource()
	{
		List<Map<String, String>> sourceList = new ArrayList<Map<String, String>>();
		sourceList.add(new HashMap<String, String>(){{ put("value", "1");put("text", "��ù��");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "2");put("text", "�ǰ�");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "3");put("text", "��ù��");}});
		return sourceList;
	}
	
	public static List<Map<String, String>> getYesNoSource()
	{
		List<Map<String, String>> sourceList = new ArrayList<Map<String, String>>();
		sourceList.add(new HashMap<String, String>(){{ put("value", "1");put("text", "��");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "0");put("text", "��");}});		
		return sourceList;
	}
	
	public static List<Map<String, String>> getSexNamesSource()
	{
		List<Map<String, String>> sourceList = new ArrayList<Map<String, String>>();
		sourceList.add(new HashMap<String, String>(){{ put("value", "1");put("text", "��");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "0");put("text", "Ů");}});		
		return sourceList;
	}
	
	public static List<Map<String, String>> getJobNamesSource()
	{
		List<Map<String, String>> sourceList = new ArrayList<Map<String, String>>();
		sourceList.add(new HashMap<String, String>(){{ put("value", "1");put("text", "����");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "2");put("text", "��������");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "3");put("text", "רҵ������Ա");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "4");put("text", "����������");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "5");put("text", "������Ա");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "6");put("text", "����");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "7");put("text", "��ҵ��");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "8");put("text", "��ҵ����ҵԱ��");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "9");put("text", "ѧ��");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "10");put("text", "ũ�����");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "11");put("text", "����");}});
		return sourceList;
	}
	
	public static List<Map<String, String>> getEducationLevelSource()
	{
		List<Map<String, String>> sourceList = new ArrayList<Map<String, String>>();
		sourceList.add(new HashMap<String, String>(){{ put("value", "1");put("text", "��ä������ä");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "2");put("text", "Сѧ");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "3");put("text", "����");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "4");put("text", "����");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "5");put("text", "��ר");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "6");put("text", "��ר");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "7");put("text", "���Ƽ�����");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "8");put("text", "����");}});		
		return sourceList;
	}
	
	public static List<Map<String, String>> getHcSource()
	{
		List<Map<String, String>> sourceList = new ArrayList<Map<String, String>>();
		sourceList.add(new HashMap<String, String>(){{ put("value", "1");put("text", "����");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "2");put("text", "����");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "3");put("text", "��");}});			
		return sourceList;
	}
	
	public static List<Map<String, String>> getBuyWaySource()
	{
		List<Map<String, String>> sourceList = new ArrayList<Map<String, String>>();
		sourceList.add(new HashMap<String, String>(){{ put("value", "1");put("text", "ҩ��");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "2");put("text", "ҽԺ");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "3");put("text", "����");}});
		sourceList.add(new HashMap<String, String>(){{ put("value", "3");put("text", "����");}});	
		return sourceList;
	}
	
	/**
	 * �����ҳ�ĵ��ͼƬչʾ֧��
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
	 * ��ȡĬ�ϵ���Ŀ�б�
	 */
	public static List<Map<String, String>> getDefaultChanel() {
		List<Map<String, String>> defaultChanel = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("ColumnName", "�ȵ�");
		map.put("ColumnID", "1");
		defaultChanel.add(map);
		map = new HashMap<String, String>();
		map.put("ColumnName", "����");
		map.put("ColumnID", "3");
		defaultChanel.add(map);
		map = new HashMap<String, String>();
		map.put("ColumnName", "����");
		map.put("ColumnID", "2");
		defaultChanel.add(map);
		map = new HashMap<String, String>();
		map.put("ColumnName", "����");
		map.put("ColumnID", "4");
		defaultChanel.add(map);
		return defaultChanel;
	}	
	/**
	 * ������ĿID��ȡ��Ŀ����
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
