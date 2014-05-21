package coolbuy360.adapter;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import coolbuy360.control.MyShowImgDialog;
import coolbuy360.logic.ConstantsSetting;
import coolbuy360.searchhealth.R;
import coolbuy360.service.ImageManager;
import coolbuy360.service.SetImgResoruce;
import coolbuy360.service.Util;
import coolbuy360.service.searchApp;

public class DrugSearchListViewAdapter extends BaseAdapter {
	private List<Map<String, String>> drugsearchlist;
	private LayoutInflater inflater;
	public int count = ConstantsSetting.QLDefaultPageSize;
	private Context _context;
	private String url_path_s;
	private String url_path_b;

	public DrugSearchListViewAdapter(Context context,
			List<Map<String, String>> drugsearchlist) {
		this._context=context;
		this.drugsearchlist = drugsearchlist;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.url_path_s = Util.getDrugSmallImgPath();
		this.url_path_b = Util.getDrugBigImgPath();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return drugsearchlist.get(position);
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
			convertView = inflater.inflate(R.layout.p_druglist_item, null);
			viewHolder.p_listview_title = (TextView) convertView
					.findViewById(R.id.p_listview_title);
			viewHolder.p_listview_store = (TextView) convertView
					.findViewById(R.id.p_listview_store);
			viewHolder.p_listview_img = (ImageView) convertView
					.findViewById(R.id.p_listview_img);
			viewHolder.p_listview_bao = (ImageView) convertView
					.findViewById(R.id.p_listview_bao);
			viewHolder.p_listview_h = (ImageView) convertView
					.findViewById(R.id.p_listview_h);
			viewHolder.p_listview_otc = (ImageView) convertView
					.findViewById(R.id.p_listview_otc);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		/**
		 * ",dbo.drgApprovalTypeConverter(DRG_Info.ApprovalNum) as approvaltype"
		 * + //��׼���ͣ�h-��ҩ��z-��ҩ��b-����Ʒ��s-������ҩ��j-����ҩƷ
		 * ",DRG_Info.IsHCDrug as ishcdrug" + //�Ƿ�ҽ��ҩ���0����ʾ���񡱣���1����ʾ���ǡ�
		 * ",DRG_Info.PrescriptionType as prescriptiontype" +
		 * //�������ͣ�1-����ҩ��2-����Ǵ���ҩ��3-����Ǵ���ҩ
		 * 
		 * 
		 * * ���ݹؼ��ַ���ҩƷ���ϣ�����drugid��drugname��approvalnum��approvaltype,
		 * ishcdrug��prescriptiontype�� drugtypeid��drugimg��enterprisename��ֵ��
		 */
		Map<String, String> itemmap = drugsearchlist.get(position);
		String imgurl = drugsearchlist.get(position).get("drugimg");
		String title = drugsearchlist.get(position).get("drugname");
		String companyname = drugsearchlist.get(position).get("enterprisename");
		String approvaltype = drugsearchlist.get(position).get("approvaltype");// h
		String ishcdrug = drugsearchlist.get(position).get("ishcdrug");// bao
		String prescriptiontype = drugsearchlist.get(position).get(
				"prescriptiontype");// otc

		viewHolder.p_listview_store.setText(companyname);
		viewHolder.p_listview_title.setText(title);
		// asyncImageLoader.loadImage(position,imgurl,imageLoadListener);
		if (approvaltype != null && !approvaltype.equals("")) {
			int h = SetImgResoruce.imgResource(approvaltype.trim());
			if (h != 0) {
				viewHolder.p_listview_h.setImageResource(h);
				viewHolder.p_listview_h.setVisibility(View.VISIBLE);
			} else {
				viewHolder.p_listview_h.setVisibility(View.GONE);
			}
		}
		if (ishcdrug != null && !ishcdrug.equals("")) {
			int bao = SetImgResoruce.imgResurce_bao(ishcdrug.trim());
			if (bao != 0) {
				viewHolder.p_listview_bao.setImageResource(bao);
				viewHolder.p_listview_bao.setVisibility(View.VISIBLE);
			} else {
				viewHolder.p_listview_bao.setVisibility(View.GONE);
			}
		}

		if (prescriptiontype != null && !prescriptiontype.equals("")) {
			int otc = SetImgResoruce.imageResurce_otc(Integer
					.parseInt(prescriptiontype));
			if (otc != 0) {
				viewHolder.p_listview_otc.setImageResource(otc);
				viewHolder.p_listview_otc.setVisibility(View.VISIBLE);
			} else {
				viewHolder.p_listview_otc.setVisibility(View.GONE);
			}
		}
		
		if (imgurl != null && !imgurl.equals("")) {
			Log.i("chenlinwei", url_path_s + "" + imgurl);
			// ����ͼƬ��ͼ��·��
			ImageManager.from(_context).displayImage(viewHolder.p_listview_img,
					url_path_s + imgurl, R.drawable.drug_photo_def_pic, 150, 150);
			
			// ��ʾ��ͼ
			viewHolder.p_listview_img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String url= url_path_b + drugsearchlist.get(tposition).get("drugimg");;
					Bundle bundle = new Bundle();
					bundle.putString("imgpath", url);
					Log.i("chenlinwei", url + "::clss");

					Intent intent = new Intent().setClass(_context,
							MyShowImgDialog.class);
					intent.putExtras(bundle);
					((Activity) _context).startActivity(intent);
				}
			});
			Log.i("chenlinwei", "��ͼΪ" + url_path_b + imgurl);

		} else {
			/*
			 * AaynImageLoaderUtil asynImageLoader = new AaynImageLoaderUtil();
			 * asynImageLoader.showImageAsyn(viewHolder.p_listview_img, imgurl,
			 * R.drawable.loading);
			 */
			viewHolder.p_listview_img
					.setImageResource(R.drawable.drug_photo_def_pic);
		}

		/*if (imgurl == null || imgurl.equals("")) {
			viewHolder.p_listview_img.setImageResource(R.drawable.tab_drug_img);
		} else {
			AaynImageLoaderUtil asynImageLoader = new AaynImageLoaderUtil();
			asynImageLoader.showImageAsyn(viewHolder.p_listview_img, imgurl,
					R.drawable.loading);
		}*/
		
		return convertView;
	}

	public class ViewHolder {
		TextView p_listview_title;
		TextView p_listview_store;
		ImageView p_listview_img;
		ImageView p_listview_bao;
		ImageView p_listview_otc;
		ImageView p_listview_h;
	}

	/**
	 * ����б���
	 * 
	 * @param item
	 */
	public void addItem(Map<String, String> item) {
		drugsearchlist.add(item);
	}

}
