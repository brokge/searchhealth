/**
 * 
 */
package coolbuy360.adapter;

import java.util.List;

import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import coolbuy360.searchhealth.BDrugStoreMap;
import coolbuy360.searchhealth.R;
import coolbuy360.service.CommonMethod;
import coolbuy360.service.Util;

/**
 * @author yangxc
 *
 */
public class BStoreListViewAdapter extends BaseAdapter {

	private List<MKPoiInfo> sourcelist;
	private LayoutInflater inflater;
	//private OnClickListener onClickListener;
	private Context _context;
	GeoPoint _localpt;
	
	public BStoreListViewAdapter(Context context,
			List<MKPoiInfo> storelist) {
		_context = context;
		this.sourcelist = storelist;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setLocalGeoPoint(GeoPoint localpt) {
		_localpt = localpt;
	}

	@Override
	public int getCount() {
		return sourcelist.size();
	}

	@Override
	public Object getItem(int position) {
		return sourcelist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.s_store_item, null);
			viewHolder.s_listview_position = (TextView) convertView
					.findViewById(R.id.s_list_position_txt);
			viewHolder.s_listview_name = (TextView) convertView
					.findViewById(R.id.s_list_name);
			viewHolder.s_listview_address = (TextView) convertView
					.findViewById(R.id.s_list_address);
			viewHolder.s_listview_bao = (ImageView) convertView
					.findViewById(R.id.s_list_bao);
			viewHolder.s_listview_ding = (ImageView) convertView
					.findViewById(R.id.s_list_ding);
			viewHolder.s_listview_song = (ImageView) convertView
					.findViewById(R.id.s_list_song);
			viewHolder.s_listview_dao = (ImageView) convertView
					.findViewById(R.id.s_list_dao);
			viewHolder.s_listview_v = (ImageView) convertView
					.findViewById(R.id.s_list_v);
			viewHolder.s_listview_hours = (ImageView) convertView
					.findViewById(R.id.s_list_hours);
			viewHolder.s_store_item_img_promotion = (ImageView) convertView
					.findViewById(R.id.s_store_item_img_promotion);
			// viewHolder.s_listview_callButton=(ImageButton)convertView.findViewById(R.id.s_list_call);
			// viewHolder.s_listview_calltext = (TextView) convertView.findViewById(R.id.s_list_call_text);
			viewHolder.drugstore_location_btn = (LinearLayout) convertView
					.findViewById(R.id.drugstore_location_btn);
			viewHolder.drugstore_phonecall_btn = (LinearLayout) convertView
					.findViewById(R.id.drugstore_phonecall_btn);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		/*
		 * 查找附近的药店，分页查找，包含键值：DrugStoreID，DrugStoreName，Tel，Mobile，IsTel，IsDoor，IsCOD
		 * ，
		 * IsHC，Is24Hour，IsMember，LongValue，LatValue，Address，distance，OldPrice，
		 * NowPrice。所有键值小写。
		 */
		viewHolder.s_listview_bao.setVisibility(View.GONE);
		viewHolder.s_listview_song.setVisibility(View.GONE);
		viewHolder.s_listview_dao.setVisibility(View.GONE);
		viewHolder.s_listview_v.setVisibility(View.GONE);
		viewHolder.s_listview_hours.setVisibility(View.GONE);
		viewHolder.s_listview_ding.setVisibility(View.GONE);
		viewHolder.s_store_item_img_promotion.setVisibility(View.GONE);
		
		final MKPoiInfo itemmap = sourcelist.get(position);

		String drugstorename = itemmap.name;
		String address = itemmap.address;
		String tel = itemmap.phoneNum;
		
		if (!drugstorename.equals("")) {
			viewHolder.s_listview_name.setText(drugstorename);
		}
		if (!address.equals("")) {
			viewHolder.s_listview_address.setText(address);
		}

		if (tel!=null && !tel.equals("")) {
			viewHolder.drugstore_phonecall_btn.setVisibility(View.VISIBLE);
			viewHolder.drugstore_phonecall_btn.setTag(tel);
		} else {
			viewHolder.drugstore_phonecall_btn.setVisibility(View.GONE);			
		}
		
		GeoPoint storept = itemmap.pt;		
		double distance = DistanceUtil.getDistance(_localpt, storept);
		String distanceString = "0米";		
		
		if (distance >= 1000) {
			float fdistance = Util.round(Float.parseFloat(distance/1000.00 + ""), 2);
			distanceString = fdistance + "公里";
		} else {
			distanceString = (int) distance + "米";
		}
		
		viewHolder.s_listview_position.setText(distanceString);

		viewHolder.drugstore_phonecall_btn
				.setOnTouchListener(new CommonMethod.setOnPressed());
		viewHolder.drugstore_phonecall_btn
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO 致电按钮，调用拨号界面 
						String callNum = v.getTag().toString();
						Intent intent = new Intent(Intent.ACTION_DIAL, Uri
								.parse("tel:" + callNum));
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						((Activity) _context).startActivity(intent);
					}
				});

		viewHolder.drugstore_location_btn
				.setOnTouchListener(new CommonMethod.setOnPressed());
		viewHolder.drugstore_location_btn
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO 定位药店到地图
						Intent mapintent = new Intent().setClass(_context,
								BDrugStoreMap.class);
						mapintent.putExtra("storename", itemmap.name);
						mapintent.putExtra("address", itemmap.address);
						mapintent.putExtra("phone", itemmap.phoneNum);
						mapintent.putExtra("poilatitudee6", itemmap.pt.getLatitudeE6());
						mapintent.putExtra("poilongitudee6", itemmap.pt.getLongitudeE6());
						((Activity) _context).startActivity(mapintent);
					}
				});

		return convertView;
	}

	private class ViewHolder {
		TextView s_listview_position;
		TextView s_listview_name;
		TextView s_listview_address;
		ImageView s_listview_bao;
		ImageView s_listview_ding;
		ImageView s_listview_song;
		ImageView s_listview_dao;
		ImageView s_listview_v;
		ImageView s_listview_hours;
		ImageView s_store_item_img_promotion;
		// ImageButton s_listview_callButton;
		//TextView s_listview_calltext;
		LinearLayout drugstore_location_btn;
		LinearLayout drugstore_phonecall_btn;
	}

	public void addItem(MKPoiInfo item) {
		sourcelist.add(item);
	}
	
	public void addItems(List<MKPoiInfo> list) {
		for (MKPoiInfo item : list) {
			sourcelist.add(item);
		}
	}

	public void clear() {
		int size = sourcelist.size();
		if (size > 0) {
			sourcelist.removeAll(sourcelist);
		}
	}
}
