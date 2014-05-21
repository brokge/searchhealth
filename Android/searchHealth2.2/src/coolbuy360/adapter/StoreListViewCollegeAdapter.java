package coolbuy360.adapter;

import java.util.List;
import java.util.Map;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import coolbuy360.logic.ConstantsSetting;
import coolbuy360.searchhealth.DrugStoreMap;
import coolbuy360.searchhealth.R;
import coolbuy360.searchhealth.DrugStoreMap.LoadMode;
import coolbuy360.searchhealth.DrugStoreMap.ReturnMode;
import coolbuy360.service.Util;

public class StoreListViewCollegeAdapter extends BaseAdapter {
	private List<Map<String, String>> storelist;
	private LayoutInflater inflater;
	public int count = ConstantsSetting.QLDefaultPageSize;
	// private OnClickListener onClickListener;
	private Context _context;

	public StoreListViewCollegeAdapter(Context context,
			List<Map<String, String>> storelist, OnClickListener onclick) {
		// TODO Auto-generated constructor stub
		_context = context;
		this.storelist = storelist;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// this.onClickListener=onclick;
	}

	public StoreListViewCollegeAdapter(Context context,
			List<Map<String, String>> storelist) {
		// TODO Auto-generated constructor stub
		_context = context;
		this.storelist = storelist;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// this.onClickListener=onclick;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return storelist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final int tposition = position;

		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.s_store_item, null);
			viewHolder.s_listview_position = (TextView) convertView
					.findViewById(R.id.s_list_position_txt);
			// viewHolder.s_listview_positionimg=(ImageView)convertView.findViewById(R.id.s_list_position);
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
		Map<String, String> itemmap = storelist.get(position);
		String ishc = itemmap.get("ishc");// 保
		viewHolder.s_listview_bao.setTag((Object) ishc);

		// String distance = storelist.get(position).get("distance");
		String drugstorename = itemmap.get("drugstorename");
		String address = itemmap.get("address");
		String istel = itemmap.get("istel");// 电话订购
		String tel = itemmap.get("tel");
		String mobile = itemmap.get("mobile");

		String isdoor = itemmap.get("isdoor");// 送货上门
		String iscod = itemmap.get("iscod");// 货到付款
		String isv = itemmap.get("ismember");// V
		String ishours = itemmap.get("is24hour");// 24
		String ispromotionon = itemmap.get("ispromotionon");
		viewHolder.drugstore_location_btn.setVisibility(View.GONE);
		viewHolder.s_listview_position.setVisibility(View.GONE);
		/*
		 * if(!distance.equals("")) { float
		 * distan=Utril.round(Float.parseFloat(distance), 2); if (distan>1) {
		 * distance=distan+"公里"; } else { distance=distan*1000+"米"; }
		 */
		// distance=Utril.round(Float.parseFloat(distance), 2)+"";

		/*
		 * viewHolder.s_listview_position.setText(distance); }
		 */
		if (!drugstorename.equals("")) {
			viewHolder.s_listview_name.setText(drugstorename);
		}
		if (!address.equals("")) {
			viewHolder.s_listview_address.setText(address);
		}

		if (Integer.parseInt(ishc) == 1) {
			viewHolder.s_listview_bao.setVisibility(View.VISIBLE);
		} else {
			viewHolder.s_listview_bao.setVisibility(View.GONE);
		}

		if (Integer.parseInt(isdoor) == 1) {
			viewHolder.s_listview_song.setVisibility(View.VISIBLE);
		} else {
			viewHolder.s_listview_song.setVisibility(View.GONE);
		}
		if (Integer.parseInt(iscod) == 1) {
			viewHolder.s_listview_dao.setVisibility(View.VISIBLE);
		} else {
			viewHolder.s_listview_dao.setVisibility(View.GONE);
		}
		if (Integer.parseInt(isv) == 1) {
			viewHolder.s_listview_v.setVisibility(View.VISIBLE);
		} else {
			viewHolder.s_listview_v.setVisibility(View.GONE);
		}
		if (Integer.parseInt(ishours) == 1) {
			viewHolder.s_listview_hours.setVisibility(View.VISIBLE);
		} else {
			viewHolder.s_listview_hours.setVisibility(View.GONE);
		}

		if (Integer.parseInt(istel) == 1) {
			viewHolder.s_listview_ding.setVisibility(View.VISIBLE);
		} else {
			viewHolder.s_listview_ding.setVisibility(View.GONE);
		}

		if (Integer.parseInt(ispromotionon) == 1) {
			viewHolder.s_store_item_img_promotion.setVisibility(View.VISIBLE);
		} else {
			viewHolder.s_store_item_img_promotion.setVisibility(View.GONE);
		}

		if (!tel.equals("") | !mobile.equals("")) {
			viewHolder.drugstore_phonecall_btn.setVisibility(View.VISIBLE);

			if (!tel.equals("")) {
				viewHolder.drugstore_phonecall_btn.setTag(tel);

			} else {
				viewHolder.drugstore_phonecall_btn.setTag(mobile);

			}
		} else {

			// callphone.setTag(tel);
			viewHolder.drugstore_phonecall_btn.setVisibility(View.GONE);
		}
		/*
		 * else { viewHolder.s_listview_callButton.setVisibility(View.VISIBLE);
		 * viewHolder.s_listview_calltext.setVisibility(View.VISIBLE); }
		 */
		// viewHolder.s_listview_callButton.setOnClickListener(onClickListener);
		viewHolder.drugstore_phonecall_btn
				.setOnTouchListener(new setOnPressed());
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

		/*viewHolder.drugstore_location_btn
				.setOnTouchListener(new setOnPressed());
		viewHolder.drugstore_location_btn
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO 定位药店到地图
						Bundle innerbundle = new Bundle();
						innerbundle.putString("drugstoreid",
								storelist.get(tposition).get("drugstoreid"));

						innerbundle.putString("loadmode",
								LoadMode.Position.toString());
						innerbundle.putString("returnmode",
								ReturnMode.Normal.toString());
						Intent mapintent = new Intent().setClass(_context,
								DrugStoreMap.class);
						mapintent.putExtras(innerbundle);
						((Activity) _context).startActivity(mapintent);
					}
				});*/

		return convertView;
	}

	private class ViewHolder {
		ImageView s_listview_positionimg;
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
		// TextView s_listview_calltext;
		LinearLayout drugstore_location_btn;
		LinearLayout drugstore_phonecall_btn;
	}

	public void addItem(Map<String, String> item) {
		storelist.add(item);
	}

	/**
	 * 设置布局区域的OnPress事件捕获
	 */
	private final class setOnPressed implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setPressed(true);
				break;
			/*
			 * case MotionEvent.ACTION_UP: v.setPressed(false); break;
			 */
			}
			return false;
		}
	}

}
