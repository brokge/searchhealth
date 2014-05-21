package coolbuy360.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import coolbuy360.logic.ConstantsSetting;
import coolbuy360.searchhealth.R;
import coolbuy360.service.AaynImageLoaderUtil;
import coolbuy360.service.ImageManager;
import coolbuy360.service.Util;

public class RedListViewAdapter extends BaseAdapter {
	private List<Map<String, String>> ExposureList;
	private Context mcontext;
	private LayoutInflater inflater;// 创建引用的item需要用到填从器
	public int count = ConstantsSetting.QLDefaultPageSize;

	// public View temView;
	public RedListViewAdapter(Context context,
			List<Map<String, String>> druglist) {
		// TODO Auto-generated constructor stub
		this.mcontext = context;
		this.ExposureList = druglist;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return ExposureList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.exposure_druglist_item,
					null);
			viewHolder.exposure_listview_img = (ImageView) convertView
					.findViewById(R.id.exposure_listview_img);
			viewHolder.exposure_listview_name = (TextView) convertView
					.findViewById(R.id.exposure_listview_name);
			viewHolder.exposure_listview_descri = (TextView) convertView
					.findViewById(R.id.exposure_listview_descri);
			viewHolder.exposure_listview_com = (TextView) convertView
					.findViewById(R.id.exposure_listview_com);
			viewHolder.exposure_listview_rating = (RatingBar) convertView
					.findViewById(R.id.exposure_listview_ratingbar);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 查询红榜集合，包含键值：RedListID、DrugID、PubTime、UpTime、SamplingResults、drugname。所有键值小写。
		Map<String, String> itemmap = ExposureList.get(position);
		String imgurl = itemmap.get("drugimg");
		String drugname = itemmap.get("drugname");
		String SamplingResults = itemmap.get(
				"samplingResults");
		String uptime = itemmap.get("uptime");
		viewHolder.exposure_listview_name.setText(drugname);
		viewHolder.exposure_listview_descri.setText("合格药品");

		viewHolder.exposure_listview_com.setText(Util.getDateFormat(uptime,
				"yyyy-MM-dd"));
		// viewHolder.exposure_listview_rating.setIsIndicator(true);//不允许更改
		viewHolder.exposure_listview_rating.setVisibility(View.GONE);

		/*
		 * AaynImageLoaderUtil asynImageLoader = new AaynImageLoaderUtil();
		 * asynImageLoader.showImageAsyn(viewHolder.exposure_listview_img,
		 * imgurl, R.drawable.loading);
		 */

		if (imgurl != null && !imgurl.equals("")) {
			ImageManager.from(mcontext).displayImage(
					viewHolder.exposure_listview_img, imgurl,
					R.drawable.drug_photo_def_pic, 150, 150);

		} else {
			viewHolder.exposure_listview_img
					.setImageResource(R.drawable.drug_photo_def_pic);
		}

		return convertView;
	}

	private class ViewHolder {
		TextView exposure_listview_name;
		TextView exposure_listview_descri;
		ImageView exposure_listview_img;
		TextView exposure_listview_com;
		RatingBar exposure_listview_rating;
	}
	
	/**
	 * 添加列表项
	 * 
	 * @param item
	 */
	public void addItem(Map<String, String> item) {
		ExposureList.add(item);
	}
}
