package coolbuy360.adapter;

import java.util.List;
import java.util.Map;

import coolbuy360.searchhealth.R;
import coolbuy360.service.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MemberHealthFootprintListAdapter extends SmartAdapter implements DeleteAbleAdapter {
	/*private final GestureDetector detector;*/
	private List<Map<String, String>> sourceList;
    private Context ctx = null;
    private LayoutInflater inflater = null;
    public int count;
    /*FlingListeber listener;*/
    
	public MemberHealthFootprintListAdapter(Context context,List<Map<String, String>> sourcelist)
	{
		//super();
		this.sourceList = sourcelist;
		this.ctx = context;
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
		if (sourceList != null)
			count= sourceList.size();
		else
			count= 0;
	}
	@Override
	public int getCount() {
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		final int tposition = position;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.member_footprint_listitem, null);
			viewHolder.member_footprint_listitem = (LinearLayout) convertView
					.findViewById(R.id.member_footprint_listitem);
			viewHolder.member_footprint_listitem_txv_date = (TextView) convertView
					.findViewById(R.id.member_footprint_listitem_txv_date);
			viewHolder.member_footprint_listitem_txv_name = (TextView) convertView
					.findViewById(R.id.member_footprint_listitem_txv_summarize);
			viewHolder.member_footprint_listitem_btn_del = (Button) convertView
					.findViewById(R.id.member_footprint_listitem_btn_del);
			viewHolder.member_footprint_listitem_btn_del.setVisibility(View.GONE);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		Map<String, String> itemmap = sourceList.get(position);
		viewHolder.member_footprint_listitem_txv_date.setText(Util.getDateFormat(
				itemmap.get("eventtime"), "yyyy-MM-dd E"));
		viewHolder.member_footprint_listitem_txv_name.setText(itemmap.get("summarize"));
		final View tview = convertView;
		viewHolder.member_footprint_listitem_btn_del.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(onDeleteItemListener!=null){
					onDeleteItemListener.onDeleteItem(tview, tposition);
				}
			}
		});
		
		/*final Map<String, String> item = sourceList.get(position);
        listener.setItem(item);
        listener.setHolder(viewHolder);*/
        //holder.llItem.setOnTouchListener(new SwipeListener(holder,item));
        /*viewHolder.member_disease_listitem.setOnTouchListener(new View.OnTouchListener() {
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return detector.onTouchEvent(event);
            }
        });	*/	
		
		return convertView;
	}	
	
	public void addItem(Map<String, String> item) {
		sourceList.add(item);
	}
	
	
	private class ViewHolder {
		TextView member_footprint_listitem_txv_date;
		TextView member_footprint_listitem_txv_name;
		Button member_footprint_listitem_btn_del;
		LinearLayout member_footprint_listitem;
	}
	@Override
	public void showDeleteButton(View itemview, int position) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = (ViewHolder) itemview.getTag();
		viewHolder.member_footprint_listitem_btn_del.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideDeleteButton(View itemview, int position) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = (ViewHolder) itemview.getTag();
		viewHolder.member_footprint_listitem_btn_del.setVisibility(View.GONE);		
	}
	@Override
	public void deleteSuccess(View itemview, int position) {
		sourceList.remove(position);
		hideDeleteButton(itemview, position);
		notifyDataSetChanged();
	}

}
