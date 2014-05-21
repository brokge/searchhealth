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
 * 下载完成后，点击通知栏接收对应的参数，并获取安装文件权限，启动安装程序
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
		// 更新下载完成后回调，安装更新。
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
