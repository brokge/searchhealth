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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import coolbuy360.searchhealth.R;
import coolbuy360.service.Util;

/**
 * 会员手术记录适配器
 * @author yangxc
 *
 */
public class MemberOperationListAdapter extends SmartAdapter implements
		DeleteAbleAdapter {

	private List<Map<String, String>> sourceList;
	private Context ctx = null;
	private LayoutInflater inflater = null;

	public MemberOperationListAdapter(Context context,
			List<Map<String, String>> sourcelist) {
		// TODO Auto-generated constructor stub
		super();
		this.sourceList = sourcelist;
		this.ctx = context;
		inflater = (LayoutInflater) ctx
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
		final int tposition = position;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.member_operation_listitem,
					null);
			viewHolder.member_operation_listitem_txv_date = (TextView) convertView
					.findViewById(R.id.member_operation_listitem_txv_date);
			viewHolder.member_operation_listitem_txv_operationname = (TextView) convertView
					.findViewById(R.id.member_operation_listitem_txv_operationname);
			viewHolder.member_operation_listitem_btn_del = (Button) convertView
					.findViewById(R.id.member_operation_listitem_btn_del);
			viewHolder.member_operation_listitem_btn_del
					.setVisibility(View.GONE);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Map<String, String> itemmap = sourceList.get(position);
		viewHolder.member_operation_listitem_txv_date.setText(Util
				.getDateFormat(itemmap.get("implementtime"),
						"yyyy-MM-dd E"));
		viewHolder.member_operation_listitem_txv_operationname.setText(itemmap.get("operationname"));

		final View tview = convertView;

		viewHolder.member_operation_listitem_btn_del
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (onDeleteItemListener != null) {
							onDeleteItemListener.onDeleteItem(tview, tposition);
						}
					}
				});

		return convertView;
	}

	private class ViewHolder {
		TextView member_operation_listitem_txv_date;
		TextView member_operation_listitem_txv_operationname;
		Button member_operation_listitem_btn_del;
	}

	public void addItem(Map<String, String> item) {
		sourceList.add(item);
	}

	@Override
	public void showDeleteButton(View itemview, int position) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = (ViewHolder) itemview.getTag();
		viewHolder.member_operation_listitem_btn_del.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideDeleteButton(View itemview, int position) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = (ViewHolder) itemview.getTag();
		viewHolder.member_operation_listitem_btn_del.setVisibility(View.GONE);
	}

	@Override
	public void deleteSuccess(View itemview, int position) {
		sourceList.remove(position);
		hideDeleteButton(itemview, position);
		notifyDataSetChanged();
	}

}
