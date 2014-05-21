/**
 * 
 */
package coolbuy360.adapter;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import coolbuy360.searchhealth.R;
import coolbuy360.service.Util;

import android.app.Dialog;
import android.content.Context;
import android.opengl.Visibility;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * »áÔ±¼²²¡Ê·ÊÊÅäÆ÷
 * @author yangxc
 *
 */
public class MemberDiseaseListAdapter extends SmartAdapter implements DeleteAbleAdapter {
	
	/*private final GestureDetector detector;*/
	private List<Map<String, String>> sourceList;
    private Context ctx = null;
    private LayoutInflater inflater = null;
    /*FlingListeber listener;*/
    
    public MemberDiseaseListAdapter(Context context, List<Map<String, String>> sourcelist) {
		// TODO Auto-generated constructor stub
    	super();
		this.sourceList = sourcelist;
		this.ctx = context;
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		/*listener = new FlingListeber();
		detector = new GestureDetector(listener);*/
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
			convertView = inflater.inflate(R.layout.member_disease_listitem, null);
			viewHolder.member_disease_listitem = (LinearLayout) convertView
					.findViewById(R.id.member_disease_listitem);
			viewHolder.member_disease_listitem_txv_date = (TextView) convertView
					.findViewById(R.id.member_disease_listitem_txv_date);
			viewHolder.member_disease_listitem_txv_name = (TextView) convertView
					.findViewById(R.id.member_disease_listitem_txv_name);
			viewHolder.member_disease_listitem_btn_del = (Button) convertView
					.findViewById(R.id.member_disease_listitem_btn_del);
			viewHolder.member_disease_listitem_btn_del.setVisibility(View.GONE);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Map<String, String> itemmap = sourceList.get(position);
		viewHolder.member_disease_listitem_txv_date.setText(Util.getDateFormat(
				itemmap.get("diagnosetime"), "yyyy-MM-dd E"));
		viewHolder.member_disease_listitem_txv_name.setText(itemmap.get("diseasename"));
		
		final View tview = convertView;
		
		viewHolder.member_disease_listitem_btn_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

            	/*Dialog dialog = new Dialog(ctx);
            	dialog.setTitle("btnµã»÷");
            	dialog.setCanceledOnTouchOutside(true);
            	dialog.show();*/
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

	private class ViewHolder {
		TextView member_disease_listitem_txv_date;
		TextView member_disease_listitem_txv_name;
		Button member_disease_listitem_btn_del;
		LinearLayout member_disease_listitem;
	}
	
	public void addItem(Map<String, String> item) {
		sourceList.add(item);
	}

	@Override
	public void showDeleteButton(View itemview, int position) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = (ViewHolder) itemview.getTag();
		viewHolder.member_disease_listitem_btn_del.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideDeleteButton(View itemview, int position) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = (ViewHolder) itemview.getTag();
		viewHolder.member_disease_listitem_btn_del.setVisibility(View.GONE);		
	}

	@Override
	public void deleteSuccess(View itemview, int position) {
		sourceList.remove(position);
		hideDeleteButton(itemview, position);
		notifyDataSetChanged();
	}
	
	/*class FlingListeber implements GestureDetector.OnGestureListener{

		Map<String, String> item;
        ViewHolder holder;
        
        public Map<String, String> getItem() {
            return item;            
        }

        public void setItem(Map<String, String> item) {
            this.item = item;
        }        
        
        public ViewHolder getHolder() {
            return holder;
        }

        public void setHolder(ViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY) {
            // TODO Auto-generated method stub
            if(e2.getX()-e1.getX()>20){
            	Dialog dialog = new Dialog(ctx);
            	dialog.setTitle("×ó»¬"+item.get("diseasename"));
            	dialog.setCanceledOnTouchOutside(true);
            	dialog.show();
                Toast.makeText(ctx, "×ó»¬"+item.get("diseasename"), 3000).show();                
            }else if(e1.getX()-e2.getX()>20){
            	Dialog dialog = new Dialog(ctx);
            	dialog.setTitle("ÓÒ»¬"+item.get("diseasename"));
            	dialog.setCanceledOnTouchOutside(true);
            	dialog.show();
                Toast.makeText(ctx, "ÓÒ»¬"+item.get("diseasename"), 3000).show();
            }
            
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                float distanceX, float distanceY) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
        	Dialog dialog = new Dialog(ctx);
        	dialog.setTitle("µã»÷"+item.get("diseasename"));
        	dialog.setCanceledOnTouchOutside(true);
        	dialog.show();
            Toast.makeText(ctx, "µã»÷item", 3000).show();
            return false;
        }        
    }*/
}
