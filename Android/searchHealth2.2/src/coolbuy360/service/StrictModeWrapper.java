package coolbuy360.service;

import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
//import android.os.StrictMode;
import android.util.Log;

public class StrictModeWrapper {

	  @SuppressLint("NewApi")
	public static void init(Context context) {
		// check if android:debuggable is set to true
		int appFlags = context.getApplicationInfo().flags;
		if ((appFlags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {

			try {
				//Android 2.3及以上调用严苛模式
				Class sMode = Class.forName("android.os.StrictMode");
				Method enableDefaults = sMode.getMethod("enableDefaults");
				enableDefaults.invoke(null);
			} catch (Exception e) {
				// StrictMode not supported on this device, punt
				Log.v("StrictMode", "... not supported. Skipping...");
			}

			/*
			 * StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
			 * .detectDiskReads() .detectDiskWrites() .detectNetwork()
			 * .penaltyLog() .build()); StrictMode.setVmPolicy(new
			 * StrictMode.VmPolicy.Builder() .detectLeakedSqlLiteObjects()
			 * .penaltyLog() .penaltyDeath() .build());
			 */
		}
	} 
}
