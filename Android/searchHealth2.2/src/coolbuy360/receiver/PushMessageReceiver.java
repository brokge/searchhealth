package coolbuy360.receiver;

import com.baidu.android.pushservice.PushConstants;

import coolbuy360.searchhealth.About;
import coolbuy360.searchhealth.ConMain;
import coolbuy360.searchhealth.DrugStorePromotionDetail;
import coolbuy360.searchhealth.TestPage;
import coolbuy360.service.Util;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
/**
 * �ٶ�pushReceiver
 * @author habei 
 *
 */
public class PushMessageReceiver extends BroadcastReceiver {
	/** TAG to Log */
	public static final String TAG = PushMessageReceiver.class.getSimpleName();
	AlertDialog.Builder builder;
	/**
	 * @param context
	 *            Context
	 * @param intent
	 *            ���յ�intent
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub		
		Log.d(TAG, ">>> Receive intent: \r\n" + intent);

		
		
		//͸����Ϣ��ʹ��
		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
			//��ȡ��Ϣ����
			String message = intent.getExtras().getString(
					PushConstants.EXTRA_PUSH_MESSAGE_STRING);

			//��Ϣ���û��Զ������ݶ�ȡ��ʽ
			Log.i(TAG, "onMessage: " + message);
			
			//�Զ������ݵ�json��
        	Log.d(TAG, "EXTRA_EXTRA = " + intent.getStringExtra(PushConstants.EXTRA_EXTRA));		

        	
        	Toast.makeText(context,	 "EXTRA_EXTRA = " + intent.getStringExtra(PushConstants.EXTRA_EXTRA), Toast.LENGTH_SHORT).show();
        	
        	
			//�û��ڴ��Զ��崦����Ϣ,���´���Ϊdemo����չʾ��
//			Intent responseIntent = null;
//			responseIntent = new Intent(Util.ACTION_MESSAGE);
//			responseIntent.putExtra(Util.EXTRA_MESSAGE, message);
//			responseIntent.setClass(context, ConMain.class);
//			responseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			context.startActivity(responseIntent);

		} else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {
			//����󶨵ȷ����ķ�������
			//PushManager.startWork()�ķ���ֵͨ��PushConstants.METHOD_BIND�õ�
			
			//��ȡ����
			final String method = intent
					.getStringExtra(PushConstants.EXTRA_METHOD);
			//�������ش����롣���󶨷��ش��󣨷�0������Ӧ�ý���������������Ϣ��
			//��ʧ�ܵ�ԭ���ж��֣�������ԭ�򣬻�access token���ڡ�
			//�벻Ҫ�ڳ���ʱ���м򵥵�startWork���ã����п��ܵ�����ѭ����
			//����ͨ���������Դ���������������ʱ�����µ����������
			int errorCode = intent
					.getIntExtra(PushConstants.EXTRA_ERROR_CODE,
							PushConstants.ERROR_SUCCESS);
			String content = "";
			if (intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT) != null) {
				//��������
				content = new String(
					intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT));
			}
			
			//�û��ڴ��Զ��崦����Ϣ,���´���Ϊdemo����չʾ��	
			Log.d(TAG, "onMessage: method : " + method);
			Log.d(TAG, "onMessage: result : " + errorCode);
			Log.d(TAG, "onMessage: content : " + content);
			
			Toast.makeText(
					context,
					"method hahahahahhahahhahhahhahahahahahhaahahah: " + method + "\n result: " + errorCode
							+ "\n content = " + content, Toast.LENGTH_SHORT)
					.show();

//			Intent responseIntent = null;
//			responseIntent = new Intent(Util.ACTION_RESPONSE);
//			responseIntent.putExtra(Util.RESPONSE_METHOD, method);
//			responseIntent.putExtra(Util.RESPONSE_ERRCODE,
//					errorCode);
//			responseIntent.putExtra(Util.RESPONSE_CONTENT, content);
//			responseIntent.setClass(context, ConMain.class);
//			responseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			context.startActivity(responseIntent);
			
		//��ѡ��֪ͨ�û�����¼�����
		} else if (intent.getAction().equals(PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)) {
			Log.d(TAG, "intent=" + intent.toUri(0));			
			//�Զ������ݵ�json��
        	Log.d(TAG, "EXTRA_EXTRA = " + intent.getStringExtra(PushConstants.EXTRA_EXTRA));			
//			Intent cusIntent= new Intent();
//			//setClass��ת��ָ����activity��ȥ
//			cusIntent.setClass(context, DrugStorePromotionDetail.class);
//			//��������ģʽ
//			cusIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);		
//			String title = intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_TITLE);
//			cusIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_TITLE, title);
//			String content = intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT);
//			cusIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT, content);
//			
//			context.startActivity(cusIntent);		
        	if (intent.getStringExtra(PushConstants.EXTRA_EXTRA)==null) {
//              	Intent cusIntent=new Intent().setClass(context, About.class);
//            	cusIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);		
//            	context.startActivity(cusIntent);
			
        	    Intent callback = new Intent();
                callback.setClass(context, About.class);
                callback.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                try {
    				PendingIntent.getActivity(context, 0, callback, PendingIntent.FLAG_CANCEL_CURRENT).send();
    			} catch (CanceledException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	
        	}
        	
   
		}
		
		
		
	}

}
