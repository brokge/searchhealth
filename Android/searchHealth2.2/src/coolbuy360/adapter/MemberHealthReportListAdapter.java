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
import android.widget.TextView;
import coolbuy360.searchhealth.R;
import coolbuy360.service.Util;

/**
 * 会员健康报告列表适配器
 * 
 * @author yangxc
 *
 */
public class MemberHealthReportListAdapter extends BaseAdapter {

	private List<Map<String, String>> sourceList;
	private Context ctx = null;
	private LayoutInflater inflater = null;

	public MemberHealthReportListAdapter(Context context,
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
		final int tposition = position;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.member_healthreport_listitem,
					null);
			viewHolder.member_healthreport_listitem_txv_date = (TextView) convertView
					.findViewById(R.id.member_healthreport_listitem_txv_date);
			viewHolder.member_healthreport_listitem_txv_content = (TextView) convertView
					.findViewById(R.id.member_healthreport_listitem_txv_content);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Map<String, String> itemmap = sourceList.get(position);
		viewHolder.member_healthreport_listitem_txv_date.setText(Util
				.getDateFormat(itemmap.get("EventTime"),
						"yyyy-MM-dd E"));
		
		String reporttype = itemmap.get("ReportType");
		String content = itemmap.get("Content");
		if (reporttype != null && !(reporttype.equals(""))) {
			if (reporttype.equals("disease")) {
				viewHolder.member_healthreport_listitem_txv_content
						.setText("确诊患  " + content);
			} else if (reporttype.equals("operation")) {
				viewHolder.member_healthreport_listitem_txv_content
						.setText("实施  " + content + " 手术");
			} else if (reporttype.equals("allergic")) {
				viewHolder.member_healthreport_listitem_txv_content
						.setText("发生  " + content + " 过敏");
			} else {
				viewHolder.member_healthreport_listitem_txv_content
						.setText(content);				
			}
		} else {
			viewHolder.member_healthreport_listitem_txv_content
					.setText(content);
		}
		
		return convertView;
	}

	private class ViewHolder {
		TextView member_healthreport_listitem_txv_date;
		TextView member_healthreport_listitem_txv_content;
	}

	public void addItem(Map<String, String> item) {
		sourceList.add(item);
	}
}
