/**
 * 
 */
package coolbuy360.service;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;

import coolbuy360.adapter.SmartAdapter.OnDeleteItemListener;
import coolbuy360.service.LocationInfo.SItude;

/**
 * 
 */
public class LocationProvider {
    private LocationClient mLocationClient = null;

    private static SItude station = null;
    private MyBDListener listener = new MyBDListener();
	public static String TAG = "searchHealth_Location";

    Context context;

    public LocationProvider(Context context) {
        super();
        this.context = context;
    }

    public void startLocation() {
    	if (mLocationClient == null) {
    		mLocationClient = new LocationClient(context);
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true); // 打开gps
            //option.setCoorType("bd09ll"); // 设置坐标类型为bd09ll
            option.setCoorType("gcj02"); // 设置坐标类型为gcj02
            //option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
            option.setProdName("searchHealth"); // 设置产品线名称
            
    		//option.setServiceName("com.baidu.location.service_v2.9");
    		option.setPoiExtraInfo(true);	
    		option.setAddrType("all");	//设置返回完整地址信息
    		//option.setScanSpan(3000);	//设置自动定位时间间隔

    		option.setPoiNumber(10); //设置Poi返回数量
    		option.disableCache(false); //设置不支持缓存定位	
            
            mLocationClient.setLocOption(option);
            mLocationClient.registerLocationListener(listener);
		}
        
        mLocationClient.start();//将开启与获取位置分开，就可以尽量的在后面的使用中获取到位置        
    }

    /**
     * 停止，减少资源消耗
     */
    public void stopListener() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
            mLocationClient = null;
        }
    }

    /**
     * 更新位置并保存到SItude中
     */
    public void updateListener() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.requestLocation();
            //Logger.i("update the location");
        } else {
			startLocation();
		}
    }

    /**
     * 获取经纬度信息
     * 
     * @return
     */
    public SItude getLocation() {
        return station;
    }

    private class MyBDListener implements BDLocationListener {
    	
    	@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null){
				mLocationClient.requestLocation();
				//mLocationClient.requestOfflineLocation();
				return;
			}
			/*if (location.getCity() == null) {
                int type = mLocationClient.requestLocation();
                Log.i(TAG, "first request false" + type);
                return;
            }*/
			station = new SItude();
			station.latitude = location.getLatitude();
			station.longitude = location.getLongitude();
			station.city = location.getCity();
			station.address = location.getAddrStr();
			station.accuracy = location.getRadius();
			station.direction = location.getDerect();
            
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
//				sb.append("\n省：");
//				sb.append(location.getProvince());
//				sb.append("\n市：");
//				sb.append(location.getCity());
//				sb.append("\n区/县：");
//				sb.append(location.getDistrict());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}
			sb.append("\nsdk version : ");
			sb.append(mLocationClient.getVersion());
			sb.append("\nisCellChangeFlag : ");
			sb.append(location.isCellChangeFlag());
			Log.i(TAG, sb.toString());
			
			if (onReceiveLocationListener != null) {
				onReceiveLocationListener.onReceiveLocation(location);
			}
		}
		
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null){
				return ; 
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append("Poi time : ");
			sb.append(poiLocation.getTime());
			sb.append("\nerror code : "); 
			sb.append(poiLocation.getLocType());
			sb.append("\nlatitude : ");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(poiLocation.getLongitude());
			sb.append("\nradius : ");
			sb.append(poiLocation.getRadius());
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			} 
			if(poiLocation.hasPoi()){
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
			}else{				
				sb.append("noPoi information");
			}
			Log.i(TAG, sb.toString());
		}
    }
    
    OnReceiveLocationListener onReceiveLocationListener;
	
	public void setOnReceiveLocationListener(OnReceiveLocationListener l)
    {
		onReceiveLocationListener = l;
    }
	
	public interface OnReceiveLocationListener {
		
        void onReceiveLocation(BDLocation location);
    }
}
