package coolbuy360.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import coolbuy360.logic.ConstantsSetting;
import coolbuy360.searchhealth.R;

public class DiseaseSearchListViewAdater extends BaseAdapter {
	private List<Map<String, String>> diseaseList;
	private LayoutInflater inflater;
	public int count = ConstantsSetting.QLDefaultPageSize;

	public DiseaseSearchListViewAdater(Context context,
			List<Map<String, String>> diseaseList) {
		this.diseaseList = diseaseList;
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
		return diseaseList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.d_disease_search_result_item, null);
			viewHolder.d_listview_name = (TextView) convertView
					.findViewById(R.id.d_disease_search_name);
			viewHolder.d_listview_symptoms = (TextView) convertView
					.findViewById(R.id.d_disease_search_symptoms);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Map<String, String> itemmap = diseaseList.get(position);
		String diseasename = itemmap.get("diseasename");// 名称
		String diseasesysmptoms = itemmap.get("diseasealias");// 症状
		viewHolder.d_listview_name.setText(diseasename);

		if (diseasesysmptoms != null && !(diseasesysmptoms.equals(""))) {
			viewHolder.d_listview_symptoms.setVisibility(View.VISIBLE);
			viewHolder.d_listview_symptoms.setText(diseasesysmptoms);
		} else {

			viewHolder.d_listview_symptoms.setVisibility(View.GONE);
		}

		return convertView;
	}

	public class ViewHolder {
		TextView d_listview_name;
		TextView d_listview_symptoms;
	}

	/**
	 * 添加列表项
	 * 
	 * @param item
	 */
	public void addItem(Map<String, String> item) {
		diseaseList.add(item);
	}

}
