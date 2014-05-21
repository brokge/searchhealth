package coolbuy360.adapter;

import java.util.List;
import java.util.Map;

import coolbuy360.logic.ConstantsSetting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DiseaseListViewAdapter extends BaseAdapter {
	private List<Map<String, String>> diseaseList;
	private LayoutInflater inflater;
	public int count = ConstantsSetting.QLDefaultPageSize;
	
	public DiseaseListViewAdapter(Context context, List<Map<String, String>> diseaseList) {
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
		// TODO Auto-generated method stub
		return convertView;
	}
	
	
	public class ViewHolder {
		TextView d_listview_name;
		
	}
	
	

}
