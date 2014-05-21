package coolbuy360.adapter;

import java.util.List;
import java.util.Map;

import android.R.string;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import coolbuy360.logic.ConstantsSetting;
import coolbuy360.searchhealth.DrugStoreMap;
import coolbuy360.searchhealth.R;
import coolbuy360.searchhealth.DrugStoreMap.LoadMode;
import coolbuy360.searchhealth.DrugStoreMap.ReturnMode;
import coolbuy360.service.Util;

public class detailListViewAdapter extends BaseAdapter {
	private List<Map<String, String>> drugstore_list;
	private LayoutInflater inflater;
	public int count = ConstantsSetting.QLDefaultPageSize;
	// private OnClickListener onClickListener;
	private Context _context;
	private String _drugid;

	public detailListViewAdapter(Context context,
			List<Map<String, String>> drugstorelist, OnClickListener onclick) {
		// TODO Auto-generated constructor stub
		_context = context;
		this.drugstore_list = drugstorelist;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// this.onClickListener=onclick;
	}

	public detailListViewAdapter(Context context,
			List<Map<String, String>> drugstorelist, String drugID) {
		// TODO Auto-generated constructor stub
		_context = context;
		_drugid = drugID;
		this.drugstore_list = drugstorelist;
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
		return drugstore_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		final int tposition = position;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.p_store_item, null);
		}
		TextView storetitle = (TextView) convertView
				.findViewById(R.id.p_store_item_name);
		TextView drugprice = (TextView) convertView
				.findViewById(R.id.p_store_item_nowprice);
		
		  /*Button
		  callphone=(Button)convertView.findViewById(R.id.p_store_item_call);*/
		
		TextView distancetxt = (TextView) convertView
				.findViewById(R.id.p_store_item_distance);	 

		ImageView baoImg = (ImageView) convertView
				.findViewById(R.id.p_store_item_bao);
		ImageView dingImg = (ImageView) convertView
				.findViewById(R.id.p_store_item_ding);
		ImageView songImg = (ImageView) convertView
				.findViewById(R.id.p_store_item_song);
		ImageView daoImg = (ImageView) convertView
				.findViewById(R.id.p_store_item_dao);
		ImageView vImg = (ImageView) convertView
				.findViewById(R.id.p_store_item_v);
		ImageView hoursImg = (ImageView) convertView
				.findViewById(R.id.p_store_item_24);
		LinearLayout drugstore_location_btn = (LinearLayout) convertView
				.findViewById(R.id.drugstore_location_btn);
		LinearLayout drugstore_phonecall_btn = (LinearLayout) convertView
				.findViewById(R.id.drugstore_phonecall_btn);
		
		Map<String, String> itemmap = drugstore_list.get(position);
		storetitle.setText(itemmap.get("drugstorename"));
		String nowprice = itemmap.get("nowprice");
		if (nowprice == null || nowprice.equals("") || nowprice.equals("0.00")
				|| nowprice.equals("0")) {
			drugprice.setText("未知");
		} else {
			drugprice.setText("￥" + nowprice);
		}
		String tel = itemmap.get("tel");
		String mobile = itemmap.get("mobile");
		String distance = itemmap.get("distance");

		String ishc = itemmap.get("ishc");// 保
		String istel = itemmap.get("istel");// 电话订购
		String isdoor = itemmap.get("isdoor");// 送货上门
		String iscod = itemmap.get("iscod");// 货到付款
		String isv = itemmap.get("ismember");// V
		String ishours = itemmap.get("is24hour");// 24

		if (Integer.parseInt(ishc) == 1) {
			baoImg.setVisibility(View.VISIBLE);
		} else {
			baoImg.setVisibility(View.GONE);
		}

		if (Integer.parseInt(isdoor) == 1) {
			songImg.setVisibility(View.VISIBLE);
		} else {
			songImg.setVisibility(View.GONE);
		}
		if (Integer.parseInt(iscod) == 1) {
			daoImg.setVisibility(View.VISIBLE);
		} else {
			daoImg.setVisibility(View.GONE);
		}
		if (Integer.parseInt(isv) == 1) {
			vImg.setVisibility(View.VISIBLE);
		} else {
			vImg.setVisibility(View.GONE);
		}
		if (Integer.parseInt(ishours) == 1) {
			hoursImg.setVisibility(View.VISIBLE);
		} else {
			hoursImg.setVisibility(View.GONE);
		}

		// p_store_item_distance
		if (!distance.equals("")) {
			float distan = Util.round(Float.parseFloat(distance), 2);
			if (distan > 1) {
				distance = distan + "公里";
			} else {
				distance = distan * 1000 + "米";
			}
		}
		distancetxt.setText(distance);

		if (!tel.equals("") || !mobile.equals("")) {
			drugstore_phonecall_btn.setVisibility(View.VISIBLE);

			if (!tel.equals("")) {
				drugstore_phonecall_btn.setTag(tel);

			} else {
				drugstore_phonecall_btn.setTag(mobile);

			}
		} else {
			drugstore_phonecall_btn.setVisibility(View.GONE);
		}

		/*
		 * if(!tel.equals("")||!mobile.equals("")) { if(!tel.equals("")) {
		 * callphone.setTag(tel); } else { callphone.setTag(mobile); }
		 * 
		 * } else if(tel.equals("")&mobile.equals("")) {
		 * //callphone.setTag(tel); callphone.setVisibility(View.GONE);
		 * 
		 * }
		 */

		//callphone.setOnClickListener(onClickListener);
		drugstore_phonecall_btn.setOnTouchListener(new setOnPressed());
		drugstore_phonecall_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 致电按钮，调用拨号界面
				String callNum = v.getTag().toString();
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ callNum));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				((Activity) _context).startActivity(intent);
			}
		});

		drugstore_location_btn.setOnTouchListener(new setOnPressed());
		drugstore_location_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 定位药店到地图
				Bundle innerbundle = new Bundle();
				innerbundle.putString("drugstoreid", drugstore_list.get(tposition)
						.get("drugstoreid"));
				innerbundle.putString("drugid", _drugid);
				innerbundle.putString("loadmode", LoadMode.PositionByDrug.toString());
				innerbundle.putString("returnmode",
						ReturnMode.Normal.toString());
				Intent mapintent = new Intent().setClass(_context,
						DrugStoreMap.class);
				mapintent.putExtras(innerbundle);
				((Activity) _context).startActivity(mapintent);
			}
		});

		return convertView;
	}

	public void addItem(Map<String, String> item) {
		drugstore_list.add(item);
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
