package coolbuy360.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import coolbuy360.searchhealth.R;

public class DiseaseDescriptionAdapter extends BaseExpandableListAdapter {
	public List<Map<String, String>> groups;
	public  List<List<Map<String, String>>> childs;
	private Context context;
	public DiseaseDescriptionAdapter( Context context, List<Map<String, String>> groups,  List<List<Map<String, String>>> childs) {
		this.context = context;
		this.groups = groups;
		this.childs = childs;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childs.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(convertView==null)
		{
			LayoutInflater layoutInflater = (LayoutInflater) context
			 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView=(LinearLayout) layoutInflater.inflate(R.layout.d_disease_child,
					null);			
		}
		String txtString=((Map<String, String>) getChild(groupPosition,
				childPosition)).get("child");
		
		TextView txtcontent=(TextView)convertView.findViewById(R.id.d_disease_child_content);
		txtcontent.setText(txtString);
		
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		 if (childs != null) {
			if (childs.size() > groupPosition
					&& childs.get(groupPosition) != null) {
				childs.get(groupPosition).size();
			} else {
				return 0;
			}
		} else
			return 0;

		return (childs != null) ? childs.get(groupPosition).size() : 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return (groups != null) ? groups.get(groupPosition) : null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return (groups != null) ? groups.size() : 0;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		String textString=groups.get(groupPosition).get("group");
		if(convertView==null)
		{
			LayoutInflater layoutInflater = (LayoutInflater) context
			 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView=(LinearLayout) layoutInflater.inflate(R.layout.d_disease_group,
					null);			
		}
		
		TextView txtcontent=(TextView)convertView.findViewById(R.id.d_disease_group_content);
		txtcontent.setText(textString);		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
