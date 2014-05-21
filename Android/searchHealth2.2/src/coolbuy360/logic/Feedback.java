/**
 * 
 */
package coolbuy360.logic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

/**
 * 意见反馈
 * @author yangxc
 */
public class Feedback {

	/**
	 * 匿名提交一条反馈意见，并得到操作结果
	 * @param content 反馈内容
	 * @return
	 */
	/*public static Boolean insert(String content)
	{
		String strQL = ConstantsSetting.QLInsertOneFeedback;
		Map<String, String> postValue = new HashMap<String,String>();
		postValue.put("content", content);
		Date date = new Date();
		SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		String times = from.format(date);
		postValue.put("time", times);
		return ConstantsSetting.qLInsert(strQL, postValue);
	}*/

	/**
	 * 提交一条反馈意见，并得到操作结果
	 * @param context
	 * @param content 反馈内容
	 * @return
	 */
	public static Boolean insert(Context context, String content)
	{
		String strQL = "";
		String appUserID = User.IsLogged ? User.getMemberAppUserID() : User
				.getDevAppUserID(context);
		Map<String, String> postValue = new HashMap<String,String>();
		postValue.put("appuserid", appUserID);
		if(User.IsLogged){
			strQL = ConstantsSetting.QLInsertOneFeedbackByMember;
			postValue.put("memberid", User.getMemberID());
		}
		else {
			strQL = ConstantsSetting.QLInsertOneFeedback;
		}
		postValue.put("content", content);
		Date date = new Date();
		SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		String times = from.format(date);
		postValue.put("time", times);
		return ConstantsSetting.qLInsert(strQL, postValue);
	}
	
}
