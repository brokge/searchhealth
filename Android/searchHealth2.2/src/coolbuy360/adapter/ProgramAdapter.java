/**
 * 
 */
package coolbuy360.adapter;

import java.util.List;
import java.util.Map;

import coolbuy360.searchhealth.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Administrator
 * 
 */
public class ProgramAdapter extends BaseAdapter {
	private Context context;
	List<Map<String, Object>> _programList = null;

	public ProgramAdapter(Context context) {
		this.context = context;
	}

	public ProgramAdapter(Context context, List<Map<String, Object>> programList) {
		this.context = context;
		this._programList = programList;
	}

/*	private Integer[] images = { // 九宫格图片的设置
	R.drawable.program_ico_about_img, R.drawable.program_ico_changepwd_img,
			R.drawable.program_ico_drugfav_img,
			R.drawable.program_ico_drugstorefav_img,
			R.drawable.program_ico_exposure_img,
			R.drawable.program_ico_feedback_img,
			R.drawable.program_ico_historyofmedicine_img,
			R.drawable.program_ico_purchasehistory_img,
			R.drawable.program_ico_remind_img,
			R.drawable.program_ico_setting_img,
			R.drawable.program_ico_vipcards_img,
			R.drawable.program_ico_weibodoc_img };
	private String[] texts = {
			// 九宫格图片下方文字的设置
			"关于", "修改密码", "药品收藏", "药店收藏", "曝光栏", "意见反馈", "用药史", "购药记录", "用药提醒",
			"设置", "我的会员卡", "微博医生", };*/

	// get the number
	@Override
	public int getCount() {
		return _programList.size();
	}

	@Override
	public Object getItem(int position) {
		return _programList.get(position);
	}

	// get the current selector's id number
	@Override
	public long getItemId(int position) {
		return position;
	}

	// create view method
	@Override
	public View getView(int position, View view, ViewGroup viewgroup) {
		ImgTextWrapper wrapper;
		if (view == null) {
			wrapper = new ImgTextWrapper();
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.program_gridview_item, null);
			view.setTag(wrapper);
			view.setPadding(5, 10, 5, 10); // 每格的间距
		} else {
			wrapper = (ImgTextWrapper) view.getTag();
		}
		
		Map<String, Object> mprogram = _programList.get(position);
		wrapper.imageView = (ImageView) view.findViewById(R.id.program_ico_img);
		// wrapper.imageView.setBackgroundResource(images[position]);
		wrapper.imageView.setImageDrawable(context.getResources().getDrawable(
				(Integer) (mprogram.get(ProgramFiles.Image.toString()))));
		wrapper.textView = (TextView) view.findViewById(R.id.program_name_text);
		wrapper.textView.setText(context.getString((Integer) (mprogram
				.get(ProgramFiles.Name.toString()))));
		wrapper.stateView = (TextView) view
				.findViewById(R.id.program_state_text);
		Boolean isuseableBoolean = (Boolean) (mprogram
				.get(ProgramFiles.IsUseAble.toString()));
		if (isuseableBoolean) {
			wrapper.stateView.setVisibility(View.GONE);
		} else {
			wrapper.stateView.setVisibility(View.VISIBLE);
		}
		
		// 显示“New”或新消息的“红点”
		wrapper.ico_newfunction = (ImageView) view
				.findViewById(R.id.program_ico_newfunction);
		if (mprogram.get(ProgramFiles.NewFunction.toString()) == null) {
			wrapper.ico_newfunction.setVisibility(View.GONE);
			wrapper.ico_newnotice = (ImageView) view
					.findViewById(R.id.program_ico_newnotice);
			if (mprogram.get(ProgramFiles.NewNotice.toString()) == null) {
				wrapper.ico_newnotice.setVisibility(View.GONE);
			} else {
				wrapper.ico_newnotice.setVisibility(View.VISIBLE);
			}
		} else {
			wrapper.ico_newfunction.setVisibility(View.VISIBLE);
		}
			
		return view;
	}

	class ImgTextWrapper {
		ImageView imageView;
		TextView textView;
		TextView stateView;
		ImageView ico_newnotice;
		ImageView ico_newfunction;
	}

	public enum ProgramFiles {
		/** 功能图标 */
		Image,
		/** 功能名称 */
		Name,
		/** 标识 */
		Tag,
		/** 是否可用 */
		IsUseAble,
		/** 是否有新消息 */
		NewNotice,
		/** 是否新功能 */
		NewFunction
	}
}