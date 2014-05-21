package coolbuy360.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import coolbuy360.searchhealth.R;
import coolbuy360.service.Util;
import coolbuy360.service.searchApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewListAdapter extends BaseAdapter {
	
	private ImageLoader imageLoader = null;
	private	DisplayImageOptions options = null;
	
	static class ViewHolder {
		ImageView ivPreview;
		TextView tvTitle;
		TextView tvContent;
		//TextView tvReview;
	}
	
	private Context context;
	private List<Map<String, String>> news;

	public NewListAdapter(Context context,List<Map<String, String>> innerList) {
		this.context = context;
		this.news = innerList;
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.drug_photo_def_pic)
				.displayer(new RoundedBitmapDisplayer(0xff000000, 15))
				.cacheInMemory().cacheOnDisc().build();
	}
	
	@Override
	public int getCount() {
		return news.size();
	}

	@Override
	public Map<String,String> getItem(int position) {
	
		return news.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_news, null);
			holder = new ViewHolder();
			holder.ivPreview = (ImageView) convertView.findViewById(R.id.ivPreview);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
			/*
			 * holder.tvReview = (TextView)
			 * convertView.findViewById(R.id.tvReview);
			 */
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String imgurl = getItem(position).get("Image");
		
		if(imgurl!=null&&!imgurl.equals(""))
		{
			String imagePath = searchApp.News_Img_Path_L_S;
			if (!(imgurl.toLowerCase().startsWith("http://"))) {
				imgurl = imagePath + imgurl;
				}
			imageLoader.displayImage(imgurl, holder.ivPreview, options);
		}
		else {
			holder.ivPreview.setImageResource(R.drawable.drug_photo_def_pic);
		}		
		holder.tvTitle.setText( getItem(position).get("Title"));
		holder.tvContent.setText(getItem(position).get("Resume"));
		
		//holder.tvReview.setText(getItem(position).get("review"));
		
		return convertView;
	}
	
	public void addNews(List<Map<String, String>> addNews) {
		for (Map<String, String> hm : addNews) {
			news.add(hm);
		}
	}

	public void clearList() {
		int size = news.size();
		if (size > 0) {
			news.removeAll(news);
		}
	}
}
