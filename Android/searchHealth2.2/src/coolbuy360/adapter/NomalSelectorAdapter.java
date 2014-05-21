/**
 * 
 */
package coolbuy360.adapter;

import java.util.List;
import java.util.Map;

import coolbuy360.searchhealth.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author yangxc
 * 标准选择器列表适配器
 */
public class NomalSelectorAdapter extends BaseAdapter {

	private List<Map<String, String>> sourceList;
	private LayoutInflater inflater;
	
	public NomalSelectorAdapter(Context context, List<Map<String, String>> sourcelist) {
		// TODO Auto-generated constructor stub
		this.sourceList = sourcelist;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			convertView = inflater.inflate(R.layout.nomal_selector_item, null);
			viewHolder.nomal_selector_item_txt = (TextView) convertView
					.findViewById(R.id.nomal_selector_item_txt);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.nomal_selector_item_txt.setText(sourceList.get(position).get("text"));
		return convertView;
	}

	private class ViewHolder {
		TextView nomal_selector_item_txt;
	}
	
	public void addItem(Map<String, String> item) {
		sourceList.add(item);
	}

}
