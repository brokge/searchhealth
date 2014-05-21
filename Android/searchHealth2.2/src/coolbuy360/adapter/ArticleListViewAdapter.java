/**
 * 
 */
package coolbuy360.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import coolbuy360.searchhealth.R;
import coolbuy360.service.ImageManager;
import coolbuy360.service.Util;

/**
 * @author yangxc
 *
 */
public class ArticleListViewAdapter extends BaseAdapter {

	private List<Map<String, String>> sourceList;
	private Context ctx = null;
	private LayoutInflater inflater = null;
	//private String imagePath;

	public ArticleListViewAdapter(Context context,
			List<Map<String, String>> sourcelist) {
		// TODO Auto-generated constructor stub
		super();
		this.sourceList = sourcelist;
		this.ctx = context;
		inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//this.imagePath = Util.getDissertationImgPath();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (sourceList != null)
			return sourceList.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return sourceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.article_listitem,
					null);
			viewHolder.article_listitem_title = (TextView) convertView
					.findViewById(R.id.article_listitem_title);
			viewHolder.article_listitem_resume = (TextView) convertView
					.findViewById(R.id.article_listitem_resume);
			/*viewHolder.article_listitem_image = (ImageView) convertView
					.findViewById(R.id.article_listitem_image);*/
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Map<String, String> itemmap = sourceList.get(position);
		viewHolder.article_listitem_title.setText(itemmap.get("Title"));
		viewHolder.article_listitem_resume.setText(itemmap.get("Resume"));

		/*String imgurl = sourceList.get(position % sourceList.size()).get("Image");
		
		if (imgurl != null && !imgurl.equals("")) {
			if(imgurl.toLowerCase().startsWith("http://")) {
				ImageManager.from(ctx).displayImage(viewHolder.dissertation_listitem_image,
						imgurl, R.drawable.promotion_def_pic, 150, 150);
			} else {
			// …Ë÷√Õº∆¨µƒ¬∑æ∂
				ImageManager.from(ctx).displayImage(viewHolder.dissertation_listitem_image,
						imagePath + imgurl, R.drawable.promotion_def_pic, 150, 150);
			}

		} else {
			viewHolder.dissertation_listitem_image
					.setImageResource(R.drawable.promotion_def_pic);
		}*/

		return convertView;
	}

	private class ViewHolder {
		TextView article_listitem_title;
		TextView article_listitem_resume;
		/*ImageView article_listitem_image;*/
	}

	public void addItem(Map<String, String> item) {
		sourceList.add(item);
	}
}
