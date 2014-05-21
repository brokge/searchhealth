/**
 * 
 */
package coolbuy360.adapter;

import java.util.List;
import java.util.Map;

import coolbuy360.searchhealth.R;
import coolbuy360.service.Util;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author yangxc
 *
 */
public class PushMessageListViewAdapter extends BaseAdapter {

	private List<Map<String, String>> sourceList;
	private Context ctx = null;
	private LayoutInflater inflater = null;

	public PushMessageListViewAdapter(Context context,
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
			convertView = inflater.inflate(R.layout.notice_center_listitem,
					null);
			viewHolder.notice_center_listitem_title = (TextView) convertView
					.findViewById(R.id.notice_center_listitem_title);
			viewHolder.notice_center_listitem_content = (TextView) convertView
					.findViewById(R.id.notice_center_listitem_content);
			viewHolder.notice_center_listitem_sendtime = (TextView) convertView
					.findViewById(R.id.notice_center_listitem_sendtime);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Map<String, String> itemmap = sourceList.get(position);
		viewHolder.notice_center_listitem_title.setText(itemmap.get("Title"));
		viewHolder.notice_center_listitem_content.setText(itemmap.get("Content"));
		viewHolder.notice_center_listitem_sendtime.setText(Util
				.getDateFormat(itemmap.get("SendTime"),
						"yyyy-MM-dd HH:mm"));

		return convertView;
	}

	private class ViewHolder {
		TextView notice_center_listitem_title;
		TextView notice_center_listitem_content;
		TextView notice_center_listitem_sendtime;
	}

	public void addItem(Map<String, String> item) {
		sourceList.add(item);
	}
}
