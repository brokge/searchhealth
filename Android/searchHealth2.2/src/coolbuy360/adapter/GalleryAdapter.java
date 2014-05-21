package coolbuy360.adapter;

import java.util.List;

import coolbuy360.control.MyImageView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
/**
 * Gallery的适配器类，主要用于加载图片
 * @author lyc
 *
 */
public class GalleryAdapter extends BaseAdapter {

	private Context context;
	private List<Bitmap> bitmaps;
	

	public GalleryAdapter(Context context,List<Bitmap> bmps) {
		this.context = context;
		this.bitmaps=bmps;
	}

	@Override
	public int getCount() {
		return bitmaps.size();
	}

	@Override
	public Object getItem(int position) {
		return bitmaps.get(position) ;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//每次移动获取图片并重新加载，当图片很多时可以构造函数就把bitmap引入并放入list当中，
		//然后在getview方法当中取来直接用
		Bitmap bmp = bitmaps.get(position);
		MyImageView view = new MyImageView(context,bmp.getWidth(),bmp.getHeight());
		view.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		view.setImageBitmap(bmp);
		return view;
	}

}
