package coolbuy360.service;
import org.apache.http.message.BasicNameValuePair;

import coolbuy360.searchhealth.R;

import android.app.Activity;
import android.content.Intent;


public class IntentUtil {
	public static void start_activity(Activity activity,Class<?> cls,BasicNameValuePair...name)
	{
		Intent intent=new Intent();
		intent.setClass(activity,cls);
		for(int i=0;i<name.length;i++)
		{
			intent.putExtra(name[i].getName(), name[i].getValue());
		}
		activity.startActivity(intent);
		activity.overridePendingTransition(R.anim.push_top_in,R.anim.push_no);
		//activity.getParent().overridePendingTransition(R.anim.push_top_in,R.anim.push_no);
	}
}
