/**
 * 
 */
package coolbuy360.service;

import coolbuy360.logic.AppConfig;
import coolbuy360.logic.User;
import android.content.Context;
import android.os.AsyncTask;

/**
 * 提交位置
 * @author yangxc
 *
 */
public class UpdateLocation {
	
	private Context context;
	LocationProvider innerLocationProvider = null;

	public UpdateLocation(Context context) {
		this.context = context;
		innerLocationProvider = searchApp.mLocationProvider;
	}
	
	public void SubmitFirstLocation() {
		new AsyncSubmitFirstLocation().execute();
	}
	
	/**
	 * 后台提交位置
	 */
	public class AsyncSubmitFirstLocation extends AsyncTask<String, Void, CommandResult.Result> {

		CommandResult subResult;
		
		@Override
		protected CommandResult.Result doInBackground(String... params) {
			try {
				LocationInfo.SItude station = null;
				if (innerLocationProvider != null) {
					station = innerLocationProvider.getLocation();
					if (station.latitude == 0.0 && station.longitude == 0.0) {
						innerLocationProvider.updateListener();
						station = innerLocationProvider.getLocation();
					}
					if (station.latitude == 0.0 && station.longitude == 0.0) {
						return CommandResult.Result.Location_Error;
					}
				} else {
					return CommandResult.Result.Location_Error;
				}
				if (station.address == null || station.address.equals("")) {
					return CommandResult.Result.Location_Error;
				}
				subResult = User.updateFirstLocation(context, station.address);
				if (subResult != null) {
					if(subResult.getResult()) {
						return CommandResult.Result.True;						
					} else {
						return CommandResult.Result.False;
					}
				} else {
					return CommandResult.Result.UnKnow_Error;
				}
			} catch (Exception ex) {
				return CommandResult.Result.UnKnow_Error;
			}
		}

		@Override
		protected void onPostExecute(CommandResult.Result result) {

			if (result == CommandResult.Result.True) {
				// 更改本地标记
				AppConfig.setValue(context, AppConfig.IsUploaded_LocationAddress, "true");

			} else {
				 //Toast.makeText(context, "链接服务器不成功", 1).show(); 
			}
		}
	}
}
