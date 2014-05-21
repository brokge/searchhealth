/**
 * 
 */
package coolbuy360.control;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKSearch;

import coolbuy360.searchhealth.BDrugStoreRoute;
import coolbuy360.searchhealth.R;

/**
 * 药店标记层
 * @author yangxc
 */
public class MyPoiOverlay extends PoiOverlay {
    
	MKSearch mSearch;
    MapView mMapView;
    Activity mActivity;
    PopupOverlay pop;

    public MyPoiOverlay(Activity activity, MapView mapView, MKSearch search) {
        super(activity, mapView);
        mSearch = search;
        mMapView = mapView;
        mActivity = activity;
    }

	@Override
	protected boolean onTap(int i) {
		//super.onTap(i);
		MKPoiInfo info = getPoi(i);
		showPopup(info, (MyMapView) mMapView, mActivity);
		
		/*if (info.hasCaterDetails) {
			mSearch.poiDetailSearch(info.uid);
		}*/
		return true;
	}

	/**
	 * @param info
	 */
	public static void showPopup(final MKPoiInfo info, MyMapView mMapView, final Activity mActivity) {
		if (mMapView.pop == null) {
			mMapView.pop = new PopupOverlay(mMapView, new PopupClickListener() {

				@Override
				public void onClickedPopup(int arg0) {
					// TODO 自动生成的方法存根

				}
			});
			//((MyMapView)mMapView).pop = pop;
		}

		// 弹窗弹出位置
		View viewCache = mActivity.getLayoutInflater().inflate(
				R.layout.map_poi_popview, null);
		TextView nameTextView = (TextView) viewCache.findViewById(R.id.map_poi_popview_name);
		nameTextView.setText(info.name);
		TextView addressTextView = (TextView) viewCache.findViewById(R.id.map_poi_popview_address);
		addressTextView.setText(info.address);
		
		LinearLayout map_poi_popview_btn_route = (LinearLayout) viewCache.findViewById(R.id.map_poi_popview_btn_route);
		map_poi_popview_btn_route.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent toIntent = new Intent().setClass(
						mActivity, BDrugStoreRoute.class);
				toIntent.putExtra("endlatitude", info.pt.getLatitudeE6() / 1E6);
				toIntent.putExtra("endlongitude", info.pt.getLongitudeE6() / 1E6);
				toIntent.putExtra("storename", info.name);
				mActivity.startActivity(toIntent);
			}
		});
		
		LinearLayout map_poi_popview_btn_call = (LinearLayout) viewCache.findViewById(R.id.map_poi_popview_btn_call);
		if (info.phoneNum == null || info.phoneNum.equals("")) {
			map_poi_popview_btn_call.setVisibility(View.GONE);
			ImageView map_poi_popview_btn_call_spliter = (ImageView) viewCache.findViewById(R.id.map_poi_popview_btn_call_spliter);
			map_poi_popview_btn_call_spliter.setVisibility(View.GONE);
		} else {
			map_poi_popview_btn_call.setTag(info.phoneNum);
			map_poi_popview_btn_call.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					String callNum = v.getTag().toString();
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri
							.parse("tel:" + callNum));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mActivity.startActivity(intent);
				}
			});
		}
		
		// 弹出pop,隐藏pop
		mMapView.pop.showPopup(viewCache, info.pt, 30);
	}
}