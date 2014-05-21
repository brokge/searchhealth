
package coolbuy360.adapter;

import java.util.List;
import java.util.Map;

import coolbuy360.searchhealth.R;
import coolbuy360.service.AaynImageLoaderUtil;
import coolbuy360.service.ImageManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class MoreWeiboDoctorAdater extends BaseAdapter {
	private List<Map<String, Object>> weiboList;
	private Context mcontext;
	private LayoutInflater inflater;// 创建引用的item需要用到填从器

	// public View temView;
	public MoreWeiboDoctorAdater(Context context,
			List<Map<String, Object>> weibolist) {
		// TODO Auto-generated constructor stub
		this.mcontext = context;
		this.weiboList = weibolist;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return weiboList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return weiboList.get(position);
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
			convertView = inflater.inflate(R.layout.moreweibolist_item,
					null);
			viewHolder.weibo_listview_img = (ImageView) convertView
					.findViewById(R.id.more_weibo_listview_img);
			viewHolder.weibo_listview_name = (TextView) convertView
					.findViewById(R.id.more_weibo_listview_name);
			viewHolder.weibo_listview_descri = (TextView) convertView
					.findViewById(R.id.more_weibo_listview_descri);
			viewHolder.weibo_listview_fun = (TextView) convertView
					.findViewById(R.id.more_weibo_listview_fun);			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 包含键值：BlackListID、DrugID、PubTime、DangerLever、Danger、drugname。所有键值小写。
		Map<String, Object> itemmap = weiboList.get(position);
		String imgurl = itemmap.get("profile_image_url").toString();
		String screen_name = itemmap.get("screen_name").toString();
		String followers_count = itemmap.get("followers_count").toString();
		String description = itemmap.get("description").toString();
		 

		viewHolder.weibo_listview_name.setText(screen_name);
		viewHolder.weibo_listview_descri.setText("擅长领域："+description);	
		viewHolder.weibo_listview_fun.setText("粉丝数："+followers_count);
		if (imgurl != null && !imgurl.equals("")) {
		  /*  Class sMode;
			try {
				sMode = Class.forName("android.media.ThumbnailUtils");				
				ImageManager.from(mcontext).displayImage(
						viewHolder.weibo_listview_img, imgurl,
						R.drawable.drug_photo_def_pic, 150, 150);				
			} catch (ClassNotFoundException e) {
				AaynImageLoaderUtil asynImageLoader = new AaynImageLoaderUtil();
				asynImageLoader.showImageAsyn(viewHolder.weibo_listview_img, imgurl,
						R.drawable.loading);
			}*/ 
			ImageManager.from(mcontext).displayImage(
					viewHolder.weibo_listview_img, imgurl,
					R.drawable.drug_photo_def_pic, 150, 150);		
		
		} else {
			viewHolder.weibo_listview_img
					.setImageResource(R.drawable.drug_photo_def_pic);
		}

		return convertView;
	}

	private class ViewHolder {
		TextView weibo_listview_name;
		TextView weibo_listview_descri;
		ImageView weibo_listview_img;
		TextView weibo_listview_fun;
	}
}
