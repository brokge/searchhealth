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
 * Gallery���������࣬��Ҫ���ڼ���ͼƬ
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
		//ÿ���ƶ���ȡͼƬ�����¼��أ���ͼƬ�ܶ�ʱ���Թ��캯���Ͱ�bitmap���벢����list���У�
		//Ȼ����getview��������ȡ��ֱ����
		Bitmap bmp = bitmaps.get(position);
		MyImageView view = new MyImageView(context,bmp.getWidth(),bmp.getHeight());
		view.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		view.setImageBitmap(bmp);
		return view;
	}

}
