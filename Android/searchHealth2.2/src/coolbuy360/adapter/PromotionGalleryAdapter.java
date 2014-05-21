package coolbuy360.adapter;

import java.util.List;
import java.util.Map;

import coolbuy360.control.ADGallery;
import coolbuy360.control.MyShowImgDialog;
import coolbuy360.searchhealth.DrugStoreDetaill;
import coolbuy360.searchhealth.R;
import coolbuy360.service.ImageManager;
import coolbuy360.service.Util;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PromotionGalleryAdapter extends BaseAdapter {

	private List<Map<String, String>> sourceList;
	private Context ctx = null;
	private LayoutInflater inflater = null;
	private String imagePath;
	
	/*private List<String> imageUrls; // 图片地址list
	private Context context;
	private PromotionGalleryAdapter self;
	Uri uri;
	Intent intent;
	ImageView imageView;
	
	public static Integer[] imgs = { R.drawable.one, R.drawable.two,
			R.drawable.three, R.drawable.four };

	private String[] myuri = { "http://www.36939.net/",
			"http://www.36939.net/", "http://www.36939.net/",
			"http://www.36939.net/" };*/

	public PromotionGalleryAdapter(Context context,
			List<Map<String, String>> sourcelist) {
		super();
		this.sourceList = sourcelist;
		this.ctx = context;
		this.inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imagePath = Util.getPromotionImgPath();
	}
	
	public int getRealCount() {
		return sourceList.size();
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		return sourceList.get(position % sourceList.size());
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(sourceList.get(position % sourceList.size()).get("promotionid"));
	}

	@SuppressWarnings("unused")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
				case 0: {
					PromotionGalleryAdapter.this.notifyDataSetChanged();
				}
					break;
				}
				super.handleMessage(msg);
			} catch (Exception e) {
			}
		}
	};

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		final int tposition = position;
		
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.promotion_gallery_item,
					null);
			viewHolder.promotion_gallery_item_image = (ImageView) convertView
					.findViewById(R.id.promotion_gallery_item_image);
			viewHolder.promotion_gallery_item_title = (TextView) convertView
					.findViewById(R.id.promotion_gallery_item_title);
			/*Gallery.LayoutParams params = new Gallery.LayoutParams(
					Gallery.LayoutParams.WRAP_CONTENT,
					Gallery.LayoutParams.WRAP_CONTENT);
			convertView.setLayoutParams(params);*/
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Map<String, String> itemmap = sourceList.get(position % sourceList.size());
		Log.i("promotion", "getview" + position);
		String imgurl = itemmap.get("image");
		String title = itemmap.get("title");
		
		viewHolder.promotion_gallery_item_title.setText(title);
		
		if (imgurl != null && !imgurl.equals("")) {
			if(imgurl.toLowerCase().startsWith("http://")) {
				ImageManager.from(ctx).displayImage(viewHolder.promotion_gallery_item_image,
						imgurl, R.drawable.promotion_def_pic, 150, 150);
			} else {
			// 设置图片的路径
				ImageManager.from(ctx).displayImage(viewHolder.promotion_gallery_item_image,
						imagePath + imgurl, R.drawable.promotion_def_pic, 150, 150);
			}
			
			// 点击图片
			/*viewHolder.promotion_gallery_item_image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String url= url_path_b + drugsearchlist.get(tposition).get("drugimg");;
					Bundle bundle = new Bundle();
					bundle.putString("imgpath", url);
					Log.i("chenlinwei", url + "::clss");

					Intent intent = new Intent().setClass(_context,
							MyShowImgDialog.class);
					intent.putExtras(bundle);
					((Activity) _context).startActivity(intent);
					
					Dialog dialog = new Dialog(ctx);
	            	dialog.setTitle("点击"+tposition);
	            	dialog.setCanceledOnTouchOutside(true);
	            	dialog.show();
				}
			});*/

		} else {
			viewHolder.promotion_gallery_item_image
					.setImageResource(R.drawable.promotion_def_pic);
		}
		
		//imageView.setImageResource(imgs[position % imgs.length]);
		
		// 设置缩放比例：保持原样
		((DrugStoreDetaill)ctx).changePointView(position % sourceList.size());
		/*
		 * imageView.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * //((String) imageView).setSpan(new URLSpan("http://www.36939.net/"),
		 * 13, 15,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		 * 
		 * 
		 * } });
		 */		

		if(parent.getClass().equals(ADGallery.class))
		{
			int width = parent.getWidth();
			Gallery.LayoutParams params = new Gallery.LayoutParams(
					width, (int)(width/2.6));
			convertView.setLayoutParams(params);
			/*viewHolder.promotion_gallery_item_title.setLayoutParams(new LinearLayout.la))*/
			parent.setLayoutParams(new LinearLayout.LayoutParams(width, (int)(width/2.6), 1));
		}		
		
		return convertView;
	}
	
	private class ViewHolder {
		ImageView promotion_gallery_item_image;
		TextView promotion_gallery_item_title;
	}

}
