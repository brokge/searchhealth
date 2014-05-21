package coolbuy360.receiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.frontia.api.FrontiaPushMessageReceiver;

import coolbuy360.logic.NoticeStateConfig;
import coolbuy360.logic.PushConfig;
import coolbuy360.logic.PushUser;
import coolbuy360.logic.User;
import coolbuy360.searchhealth.ConMain;
import coolbuy360.searchhealth.DrugStorePromotionDetail;
import coolbuy360.searchhealth.More;
import coolbuy360.searchhealth.MoreNews;
import coolbuy360.searchhealth.News;
import coolbuy360.searchhealth.PushCustomer;
import coolbuy360.service.CommonMethod;
import coolbuy360.service.JsonUtril;

public class ExPushMessageReceiver extends FrontiaPushMessageReceiver {

	String BaiduUserID = "";

	static Boolean isBindSuccess = false;
    Context _context;
	
	static  Map<String, String> pushuserMap=new  HashMap<String, String>();
	
	//自定义通知页面=4，升级=5
//	private enum IntentFlagValue {
//		PromationIntent, DiseaseIntent, DrugStoreIntent, CustomerIntent, UpdateIntent
//	}
	
	
	

	
	
	/**
	 * 调用 PushManager.startWork 后，sdk 将对 push server 发起绑定请求，这个过程是异步 的。绑定请求的结果通过
	 * onBind 返回。
	 */
	@Override
	public void onBind(Context context, int errorCode, String appid,
			String userId, String channelId, String requestId) {
		// TODO Auto-generated method stub
		//Toast.makeText(context, "" + userId + "," + errorCode,Toast.LENGTH_SHORT).show();
		if (errorCode == 0) {
			// 绑定之后的操作
			BaiduUserID = PushConfig.getValue(context, PushConfig.BaiduUserID);
			// 如果share文件中的值为空，则重新绑定值
			if (BaiduUserID.equals("")) {
				PushConfig.setValue(context, PushConfig.BaiduUserID, userId);
							
			}
			PushConfig.tagHandle(context);	//	如果绑定成功开始设置tag
		}
	}

	/**
	 * PushManager.stopWork() 的回调函数。
	 */
	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
		// TODO Auto-generated method stub

	}

	/**
	 * listTags() 的回调函数。
	 */
	@Override
	public void onListTags(Context context, int errorCode, List<String> tags,
			String requestId) {
		// TODO Auto-generated method stub

	}

	/**
	 * 接收透传消息的函数。
	 */
	@Override
	public void onMessage(Context context, String message,
			String customContentString) {
		// TODO Auto-generated method stub
		// Toast.makeText(context, customContentString,Toast.LENGTH_LONG).show();
		if (message == null||message.equals("")) {
			// intent:#Intent;launchFlags=0x10000000;component=coolbuy360.searchhealth.About;end
		} 
		else 
		{
			Map<String, Object> cusMap = JsonUtril.getMap(message);
			if (cusMap != null) {
				String messageType = cusMap.get("type").toString();
				String timestamp = cusMap.get("timestamp").toString();
				// 根据消息显示更多及专题的新消息红点图标
				if (messageType != null && messageType.equals("dissertation")) {
					NoticeStateConfig.setValue(context, NoticeStateConfig.Dissertation_LastReceive, timestamp);
					String dissertation_HasNew=NoticeStateConfig.getValue(context, NoticeStateConfig.Dissertation_HasNew);
					if (dissertation_HasNew.equals("0")) {
						NoticeStateConfig.setValue(context, NoticeStateConfig.Dissertation_HasNew, "1");
						if (ConMain.mConMain != null) {
							ConMain.mConMain.setNewNotice("更多", true);
							More imore = More.getInstance();
							if (imore != null) {
								imore.setNewNotice(1, true);
							}
						}
					}

					Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);  
			        long [] pattern = {100,400,100,400};   // 停止 开启 停止 开启   
					vibrator.vibrate(pattern, -1); //重复两次上面的pattern 如果只想震动一次，index设为-1 
				} /*else if (messageType != null && messageType.equals("column")) {
					News inews = News.getInstance();
					if (inews != null) {
						String columnid = cusMap.get("type2").toString();
						String targetid = cusMap.get("targetid").toString();
						inews.setNewNotice(columnid, View.VISIBLE);
					}
				}*/
				//
				else  if (messageType != null && messageType.equals("column"))
				{				
					NoticeStateConfig.setValue(context, NoticeStateConfig.News_LastReceive, timestamp);					
					MoreNews moreNews=MoreNews.getInstance();
					String columnid = cusMap.get("type2").toString();
					String targetid = cusMap.get("targetid").toString();
					String news_HasNew=NoticeStateConfig.getNewsAllState(context);;
					//If  the 'MoreNews ' is operating ,thus notices display cricle dot.
					if(moreNews!=null)
					{						
						moreNews.setNewNotice(columnid, View.VISIBLE);						
					}
					else
					{						
						/*int index=CommonMethod.getColumnIndex(columnid);					
						NoticeStateConfig.setValue(context, "Column" + index	+ "_HasNew", "1");*/
						
						int index=CommonMethod.getColumnIndex(columnid);					
						NoticeStateConfig.setValue(context, "Column" + index	+ "_HasNew", "1");						
					}					
					if (news_HasNew.equals("0")) {						
						if (ConMain.mConMain != null) {
							ConMain.mConMain.setNewNotice("更多", true);
							More imore = More.getInstance();
							if (imore != null) {
								imore.setNewNotice(0, true);
							}
						}
					}					
					Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);  
			        long [] pattern = {100,400,100,400};   // 停止 开启 停止 开启   
					vibrator.vibrate(pattern, -1);
					
				}
				
				
			}  
		}
	}

	/**
	 * 接收通知点击的函数。注：推送通知被用户点击前，应用无法通过接口获取通知的内 容。
	 */
	@Override
	public void onNotificationClicked(Context context, String titleString,
			String descriptionString, String customContentString) {
		// TODO Auto-generated method stub
        Log.i("chenlinwei", customContentString);		
		if (customContentString == null||customContentString.equals("")) {
			// intent:#Intent;launchFlags=0x10000000;component=coolbuy360.searchhealth.About;end
			Intent aIntent = new Intent();
			aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			aIntent.setClass(context, ConMain.class);
			context.startActivity(aIntent);
		} 
		else 
		{
			Map<String, Object> cusMap = JsonUtril.getMap(customContentString);	
			//根据百度api的要求open_type为2时才会生效
			if (cusMap.get("open_type").toString().equals("2")) {
				// 获取自定义内容中的值
				 String intentFlag= cusMap.get("intent_flag").toString();	 
				 if(intentFlag.equals("1"))//活动页面
				 {			 
					    Intent aIntent = new Intent();
						aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						aIntent.setClass(context, DrugStorePromotionDetail.class);								
						Bundle bundle=new Bundle();
						bundle.putLong("promotionid",Long.parseLong(cusMap.get("promotion_id").toString()));
						aIntent.putExtras(bundle);			
						context.startActivity(aIntent);
					 
				 }else  if (intentFlag.equals("2")) //药店intent
				 {					
				 } else  if (intentFlag.equals("3")) //药店intent
				 {					
				 }else if (intentFlag.equals("4")) {
					 //否则打开默认页面	
					    Intent aIntent = new Intent();
						aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						aIntent.setClass(context, PushCustomer.class);
						// String title =
						// intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_TITLE);				
						Bundle bundle=new Bundle();
						bundle.putString("messageid",cusMap.get("message_id").toString());
						bundle.putString(PushConstants.EXTRA_NOTIFICATION_TITLE, titleString);
						bundle.putString(PushConstants.EXTRA_NOTIFICATION_CONTENT, descriptionString);
						aIntent.putExtras(bundle);				
						context.startActivity(aIntent);
				 }else 
				 {
				 }
			}
			else
			{
			}
		}
	}

	/**
	 * setTags() 的回调函数。
	 */
	@Override
	public void onSetTags(Context context, int errorCode,
			List<String> sucessTags, List<String> failTags, String requestId) {
		// TODO Auto-generated method stub
		// 如果绑定tags成功则开始把tags存储在share文件中
		//if (errorCode == 0) {
		    Boolean isUploadBoolean=false;
			String tagStr = PushConfig.getValue(context, PushConfig.Tags);
			if (tagStr==null|| tagStr.equals("")) {
				isUploadBoolean=true;
				PushConfig.setValue(context, PushConfig.Tags,"0-");	
			}
			else {
				List<String> tagedList=new ArrayList<String>();
		        String[] tageds=tagStr.split("-");
		        for(String s : tageds) {    
		        	tagedList.add(s);    
		        }  
	            for(int i=0;i<sucessTags.size();i++)
	            {
	            	if(tagedList.indexOf(sucessTags.get(i))==-1)  // 如果上传至百度 没有本地保存并没有上传到自己的服务器
	            	{	            		
	            		tagStr+=sucessTags.get(i) + "-";
	            		isUploadBoolean=true;
	            	}
	            }
	            //如果存在没有写入sharep的tags，则写入
	            if(isUploadBoolean)
	            {	            
	            PushConfig.setValue(context, PushConfig.Tags, tagStr);	   
	            }
			}
            //如果设置了新tag则开始上传
            if (isUploadBoolean) {
            	upLoadData(context);
			}
		
		/*	if(isBindSuccess)
			{
				//开始上传至我们的服务器
				upLoadData(context);
				//isBindSuccess=false;
			}
			else
			{								
				//如果没有绑定成功则重新启动
		       PushManager.resumeWork(context);		      
		       PushConfig.tagHandle(context);		
			}*/
			
		//}
	}

	/**
	 * delTags() 的回调函数。
	 */
	@Override
	public void onDelTags(Context arg0, int arg1, List<String> arg2,
			List<String> arg3, String arg4) {
		// TODO Auto-generated method stub
	}
	
	public void upLoadData(Context context) {
		_context=context;
		//PushConfig pushConfig=new  PushConfig(context);
		//baiduuserid,memberid,tags,devicenum,devicetype,isuse
		pushuserMap.put("baiduuserid", PushConfig.getValue(context, PushConfig.BaiduUserID));
		pushuserMap.put("tags", PushConfig.getValue(context, PushConfig.Tags));
		if (User.getMemberID()==null||User.getMemberID().equals("")) {
			pushuserMap.put("memberid", "-1");
		}
		else {
			pushuserMap.put("memberid", User.getMemberID());
		}		
		
		pushuserMap.put("devicenum", getDeviceID(context));
		if (Build.MODEL!=null) {
			pushuserMap.put("devicetype", "Android"+Build.MODEL.toString());
		}
		else {
			pushuserMap.put("devicetype", "Android");
		}
		pushuserMap.put("isuse", "1");//默认是使用
		Log.i("chenlinwei", pushuserMap.toString());
		//如果已经上传过则开始更新操作
		/*if (PushConfig.getValue(context, PushConfig.ISUpload).equals("false")) {
			 new asyUploadData().execute("insert");
		}
		else {
			new asyUploadData().execute("update");
		}*/	
		new asyUploadData().execute("");
	}
	
	public class asyUploadData extends AsyncTask<String,Void,Boolean >
	{	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			// String paramsValString=params[0].toString();
			// CommandResult result;
			/* if (paramsValString.equals("insert")) {
				 result = PushUser.insert(pushuserMap);	
			 }
			 else if (paramsValString.equals("update")) {
				result=PushUser.update(pushuserMap);
			 }*/
			 /* else {
				result=null;
			  }	*/	
			if(pushuserMap.size()>0)
			{
				return PushUser.PushUserHandle(pushuserMap);
		    }
			else {
				//PushManager.re
				return false;
			}
		}
		@Override
		// 处理界面
		protected void onPostExecute(Boolean result) {			
			if (result) {			
				PushConfig.setValue(_context, PushConfig.ISUpload, "true");
			}
			else {
				
				PushConfig.setValue(_context, PushConfig.ISUpload, "false");
			}	
		}
	}	
	
	public String getDeviceID(Context context) {

		// 获得是androidid
		// Log.i("chenlinwei", "getAndroidId"+DeviceId.getAndroidId(this));
		// Log.i("chenlinwei", "getDeviceID"+DeviceId.getDeviceID(this));
		// Log.i("chenlinwei", "getIMEI"+DeviceId.getIMEI(this));
		//
		// Log.i("chenlinwei", "getCUID"+CommonParam.getCUID(this));
		// Log.i("chenlinwei", "getMODEL"+Build.MODEL);//mi is
		//
		// Log.i("chenlinwei", "getUSER:"+Build.USER);//builder
		// Log.i("chenlinwei", "getPRODUCT:"+Build.PRODUCT);//mione_plus
		// Log.i("chenlinwei", "getTYPE:"+Build.TYPE);//user
		//
		// Log.i("chenlinwei", "getDEVICE:"+Build.DEVICE);
		// Log.i("chenlinwei", "getDEVICE:"+Build.);
		// Log.i("chenlinwei", "getHARDWARE:"+Build.HARDWARE);
		// Log.i("chenlinwei", "getHARDWARE:"+Build.DISPLAY);
		final String androidId = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
		return androidId;
	}
}
