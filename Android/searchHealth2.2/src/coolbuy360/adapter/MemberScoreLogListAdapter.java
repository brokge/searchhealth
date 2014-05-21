/**
 * 
 */
package coolbuy360.adapter;

import java.util.List;
import java.util.Map;

import coolbuy360.searchhealth.R;
import coolbuy360.service.Util;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * ª·‘±Ω°øµ÷µ»’÷æ¡–±ÌœÓ  ≈‰∆˜
 * @author yangxc
 *
 */
public class MemberScoreLogListAdapter extends BaseAdapter {

	private List<Map<String, String>> sourcelist;
	private LayoutInflater inflater;
	private Context _context;

	public MemberScoreLogListAdapter(Context context,
			List<Map<String, String>> list) {
		// TODO Auto-generated constructor stub
		_context = context;
		this.sourcelist = list;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return sourcelist.size();
	}

	@Override
	public Object getItem(int position) {
		return sourcelist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.member_scorelog_listitem, null);
			viewHolder.member_scorelog_listitem_event = (TextView) convertView
					.findViewById(R.id.member_scorelog_listitem_event);
			viewHolder.member_scorelog_listitem_time = (TextView) convertView
					.findViewById(R.id.member_scorelog_listitem_time);
			viewHolder.member_scorelog_listitem_score = (TextView) convertView
					.findViewById(R.id.member_scorelog_listitem_score);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		Map<String, String> itemmap = sourcelist.get(position);
		
		String eventtxt = "ªÒ‘˘Ω°øµ÷µ";		
		String event = itemmap.get("EventType");
		if (event != null) {
			if (event.equals("checkin")) {
				eventtxt = "«©µΩÀÕΩ°øµ";
			} else if (event.equals("register")) {
				eventtxt = "◊¢≤·ÀÕΩ°øµ";
			} else if (event.equals("invitation")) {
				eventtxt = "—˚«ÎÀÕΩ°øµ";
			} else if (event.equals("drugfav")) {
				eventtxt = " ’≤ÿ“©∆∑ÀÕΩ°øµ";
			} else if (event.equals("drugstorefav")) {
				eventtxt = " ’≤ÿ“©µÍÀÕΩ°øµ";
			} else if (event.equals("completeminfo")) {
				eventtxt = "ÕÍ…∆∏ˆ»À–≈œ¢ÀÕΩ°øµ";
			}
		}
		viewHolder.member_scorelog_listitem_event.setText(eventtxt);
		
		try {
			viewHolder.member_scorelog_listitem_time.setText(Util
					.getDateFormat(itemmap.get("CreateTime"),
							"yyyy-MM-dd HH:mm"));
			String scoreString = itemmap.get("Score");
			int score = Integer.parseInt(scoreString);
			if (score > 0) {
				viewHolder.member_scorelog_listitem_score.setText("+" + score);
				viewHolder.member_scorelog_listitem_score
						.setTextColor(_context.getResources().getColor(R.color.deepgreen));
			} else {
				viewHolder.member_scorelog_listitem_score.setText(scoreString);
				viewHolder.member_scorelog_listitem_score
						.setTextColor(_context.getResources().getColor(R.color.deepred));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return convertView;
	}

	private class ViewHolder {
		TextView member_scorelog_listitem_event;
		TextView member_scorelog_listitem_time;
		TextView member_scorelog_listitem_score;
	}

	public void addItem(Map<String, String> item) {
		sourcelist.add(item);
	}
	
	public void addItems(List<Map<String, String>> list) {
		for (Map<String, String> item : list) {
			sourcelist.add(item);
		}
	}

	public void clear() {
		int size = sourcelist.size();
		if (size > 0) {
			sourcelist.removeAll(sourcelist);
		}
	}
	
}
