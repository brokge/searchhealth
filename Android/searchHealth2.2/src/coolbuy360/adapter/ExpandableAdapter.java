package coolbuy360.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import coolbuy360.searchhealth.R;



public class ExpandableAdapter extends BaseExpandableListAdapter{
	public class GroupViewHolder {
		private TextView nameTextView;
		// private Button rightBtn;
		}

	public class ChildViewHolder {
		private TextView nameTextView;
	}
	//
	private View temview;
	private GroupViewHolder groupHolder;
	private ChildViewHolder childHolder;
	private Context context;
	public List<Map<String, String>> groups;
	public List<List<Map<String, String>>> childs;
    //private OnClickListener onClickListener;
    
   
    public int cid;
	/*
	 * �ӿؼ���Click
	 * ���캯��:(���������ֶε�ʱ��Ż�����) ����1:context���� ����2:һ���б�����Դ ����3:�����б�����Դ
	 */
	/*public ExpandableAdapter(Context context, List<Map<String, String>> groups,
			List<List<Map<String, String>>> childs,OnClickListener onclick) {
		this.groups = groups;
		this.childs = childs;
		this.context = context;
		this.onClickListener = onclick;
	}	*/
	
	public ExpandableAdapter(Context context, List<Map<String, String>> groups,
			List<List<Map<String, String>>> childs) {
		this.groups = groups;
		this.childs = childs;
		this.context = context;
	}	
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childs.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	// ��ȡ�����б��View����
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		@SuppressWarnings("unchecked")
		
		Map<String, String> itemmap = (Map<String, String>) getChild(
				groupPosition, childPosition);
		String text = itemmap.get("drugtypename");
		String cid = itemmap.get("drugtypeid");
		if (convertView != null)// �ж���ת�����view�Ƿ�Ϊ��
		{
			temview = convertView;
		} else {
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			temview = (LinearLayout) layoutInflater.inflate(R.layout.exp_child,
					null);
		}
		childHolder = (ChildViewHolder) temview.getTag();
		if (childHolder == null) {
			childHolder = new ChildViewHolder();
			childHolder.nameTextView = (TextView) temview
					.findViewById(R.id.p_expchild_txt);
			//childHolder.openbtn=(ImageButton) temview.findViewById(R.id.child_rightbtn)
			//�ӿؼ���Click
			//childHolder.openbtn.setOnClickListener(onClickListener);
			
			temview.setTag(childHolder);//������childHolder��view������
		}
		//Toast.makeText(context, cid+"" , 1).show();
		childHolder.nameTextView.setText(text);
		//
		/*drugBudde drugbuddle=new drugBudde();
		drugbuddle.drugtypename=text;
		drugbuddle.drugid=cid;*/

		temview.setTag(R.id.drugtype_id,cid);
		temview.setTag(R.id.drugtype_name,text);

		return temview;
	}



	@Override
	public int getChildrenCount(int groupPosition) {
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
		return (groups != null) ? groups.get(groupPosition) : null;
	}

	@Override
	public int getGroupCount() {
		return (groups != null) ? groups.size() : 0;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// ��ȡһ���б�View����
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {	
         
		String text = groups.get(groupPosition).get("drugtypename");// ���group����Ҫ��ʾ������
		// GroupViewHolder groupHolder=()
		if (convertView != null) {
			temview = convertView;
		}
		else {
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// ��ȡһ���б����ļ�,������ӦԪ������
			temview = (LinearLayout) layoutInflater.inflate(R.layout.exp_group,
					null);
			// TextView textView = (TextView) linearLayout
			// .findViewById(R.id.p_expgroup_txt);
			// textView.setText(text);
			// �����ڴ��������¼�

		}
		groupHolder = (GroupViewHolder) temview.getTag();// ȡ��������ı�ǩ����
		if (groupHolder == null) {
			groupHolder = new GroupViewHolder();
		
			groupHolder.nameTextView = (TextView) temview
					.findViewById(R.id.p_expgroup_txt);			
			temview.setTag(groupHolder);
		}
		groupHolder.nameTextView.setText(text);
		//����ֵ
		groupHolder.nameTextView.setTag(R.id.drugtype_id,cid);		
		groupHolder.nameTextView.setTag(R.id.drugtype_name,text);
		
		ImageView group_indicator_ico = (ImageView) temview
				.findViewById(R.id.group_indicator_ico);
		if (isExpanded) {
			group_indicator_ico
					.setImageResource(R.drawable.ico_drugtype_expanded);
			temview.setBackgroundResource(R.color.drugtype_group_bgcolor_expended);
		} else {
			group_indicator_ico
					.setImageResource(R.drawable.ico_drugtype_unexpanded);
			temview.setBackgroundResource(R.drawable.drugtype_group_bg);
		}
		
		return temview;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// String	
		// updateText(str);
		
		return true;
	}


	

}
























/*
package coolbuy360.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import coolbuy360.searchhealth.R;



public class ExpandableAdapter extends BaseExpandableListAdapter{
	public class GroupViewHolder {
		private TextView nameTextView;
		// private Button rightBtn;
	}

	public class ChildViewHolder {
		private TextView nameTextView;
		private ImageView imageView;
		private ImageButton openbtn;
	}

	//
	private View temview;
	private GroupViewHolder groupHolder;
	private ChildViewHolder childHolder;
	private Context context;
	List<Map<String, String>> groups;
	List<List<Map<String, String>>> childs;
    private OnClickListener onClickListener;
	
	 * ���캯��:(���������ֶε�ʱ��Ż�����) ����1:context���� ����2:һ���б�����Դ ����3:�����б�����Դ
	 
	public ExpandableAdapter(Context context, List<Map<String, String>> groups,
			List<List<Map<String, String>>> childs,OnClickListener onclick) {
		this.groups = groups;
		this.childs = childs;
		this.context = context;
		this.onClickListener = onclick;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childs.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	// ��ȡ�����б��View����
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		@SuppressWarnings("unchecked")
		String text = ((Map<String, String>) getChild(groupPosition,
				childPosition)).get("child");
		if (convertView != null)// �ж���ת�����view�Ƿ�Ϊ��
		{
			temview = convertView;
		} else {
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			temview = (LinearLayout) layoutInflater.inflate(R.layout.exp_child,
					null);

		}
		childHolder = (ChildViewHolder) temview.getTag();
		if (childHolder == null) {
			childHolder = new ChildViewHolder();
			childHolder.nameTextView = (TextView) temview
					.findViewById(R.id.p_expchild_txt);
			childHolder.imageView = (ImageView) temview
					.findViewById(R.id.p_imageView01);
			//childHolder.openbtn=(ImageButton) temview.findViewById(R.id.child_rightbtn)
			childHolder.openbtn=(ImageButton)temview.findViewById(R.id.child_rightbtn);
			childHolder.openbtn.setOnClickListener(onClickListener);
			temview.setTag(childHolder);
		}
		childHolder.nameTextView.setText(text);
		childHolder.imageView.setImageResource(R.drawable.ic_launcher);
		
		

		// ��ȡ�����б��Ӧ�Ĳ����ļ�, �������Ԫ��������Ӧ������

		// tv.setText(text);

		// imageView.setImageResource(R.drawable.ic_launcher);

		return temview;

	}

	protected void startActivityForResult(Intent childIntent, int i) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public int getChildrenCount(int groupPosition) {
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
		return (groups != null) ? groups.get(groupPosition) : null;
	}

	@Override
	public int getGroupCount() {
		return (groups != null) ? groups.size() : 0;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// ��ȡһ���б�View����
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		String text = groups.get(groupPosition).get("drugtypename");// ���group����Ҫ��ʾ������
		// GroupViewHolder groupHolder=()
		if (convertView != null) {
			temview = convertView;
		}
		else {
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// ��ȡһ���б����ļ�,������ӦԪ������
			temview = (LinearLayout) layoutInflater.inflate(R.layout.exp_group,
					null);
			// TextView textView = (TextView) linearLayout
			// .findViewById(R.id.p_expgroup_txt);
			// textView.setText(text);
			// �����ڴ��������¼�

		}
		groupHolder = (GroupViewHolder) temview.getTag();// ȡ��������ı�ǩ����
		if (groupHolder == null) {
			groupHolder = new GroupViewHolder();
			// ������������ݽ�����Ӽ����¼�
			groupHolder.nameTextView = (TextView) temview
					.findViewById(R.id.p_expgroup_txt);
			temview.setTag(groupHolder);
		}
		groupHolder.nameTextView.setText(text);
		return temview;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// String
		// str=item[groupPosition]+ability[groupPosition][childPosition];
		// updateText(str);
		
		return true;
	}

	private void updateset() {
		// TODO Auto-generated method stub
		// Toast.makeText(this, "hello", Toast.LENGTH_LONG).show();
		Toast.makeText(context, "hello", Toast.LENGTH_LONG).show();
	}

}*/