/**
 * 
 */
package coolbuy360.receiver;

import coolbuy360.service.UpdateApp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author yangxc
 * ������ɺ󣬵��֪ͨ�����ն�Ӧ�Ĳ���������ȡ��װ�ļ�Ȩ�ޣ�������װ����
 */
public class InstallReceiver extends BroadcastReceiver {

	/**
	 * 
	 */
	public InstallReceiver() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
        Bundle rootBundle = intent.getExtras();
		// ����������ɺ�ص�����װ���¡�
		if (rootBundle != null) {
			Boolean isUpdateCallBack = rootBundle.getBoolean("isUpdateCallBack");
			Boolean downloadComplete = rootBundle.getBoolean("downloadComplete");
			if (isUpdateCallBack && downloadComplete) {
				String updateFileSaveURL = rootBundle.getString("updateFileSaveURL");
				UpdateApp.installUpdateApk(context, updateFileSaveURL);
			}
		}
	}

}
