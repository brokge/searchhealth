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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

public class ListViewAdapter extends BaseAdapter {
	private List<Map<String, String>> druglist;
	private LayoutInflater inflater;
	public int count = ConstantsSetting.QLDefaultPageSize;
	private Context _context;
	private String url_path_s;
	private String url_path_b;

	public ListViewAdapter(Context context, List<Map<String, String>> druglist) {
		// TODO Auto-generated constructor stub
		this._context = context;
		this.druglist = druglist;
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
		return druglist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
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

		/*
		 * ",dbo.drgApprovalTypeConverter(DRG_Info.ApprovalNum) as approvaltype"
		 * + //批准类型，h-西药，z-中药，b-保健品，s-生物制药，j-进口药品
		 * ",DRG_Info.IsHCDrug as ishcdrug" + //是否医保药物，“0”表示“否”，“1”表示“是”
		 * ",DRG_Info.PrescriptionType as prescriptiontype" +
		 * //处方类型，1-处方药，2-甲类非处方药，3-乙类非处方药
		 */
		Map<String, String> itemmap = druglist.get(position);
		String imgurl = itemmap.get("drugimg");
		String title = itemmap.get("drugname");
		String companyname = itemmap.get("enterprisename");
		String approvaltype = itemmap.get("approvaltype");// h
		String ishcdrug = itemmap.get("ishcdrug");// bao
		String prescriptiontype = itemmap.get("prescriptiontype");// otc

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
			// 设置图片大图的路径
			ImageManager.from(_context).displayImage(viewHolder.p_listview_img,
					url_path_s + imgurl, R.drawable.drug_photo_def_pic, 150, 150);
			// 显示大图
			viewHolder.p_listview_img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String url= url_path_b + druglist.get(tposition).get("drugimg");
					// String url = "http://app.wcjk100.com/app/drugimg/high/1280_800/testimg.jpg";
					// String url ="http://e.hiphotos.baidu.com/album/w%3D2048/sign=76681538eac4b7453494b016fbc41f17/1c950a7b02087bf476ecc4f8f3d3572c11dfcf9b.jpg";//s
					// String url ="http://e.hiphotos.baidu.com/album/w%3D2048/sign=65fc10dc5366d0167e199928a313d407/cefc1e178a82b90116b75022728da9773912ef8d.jpg";//b
					// String url = "http://bizhi.zhuoku.com/bizhi/200711/20071128/Lee_hom_Wong/Lee_hom_Wong005.jpg";
					Bundle bundle = new Bundle();
					bundle.putString("imgpath", url);
					Log.i("chenlinwei", url + "::clss");

					Intent intent = new Intent().setClass(_context,
							MyShowImgDialog.class);
					intent.putExtras(bundle);
					((Activity) _context).startActivity(intent);
				}
			});
			Log.i("chenlinwei", "大图为" + url_path_b + imgurl);
		} else {
			/*
			 * AaynImageLoaderUtil asynImageLoader = new AaynImageLoaderUtil();
			 * asynImageLoader.showImageAsyn(viewHolder.p_listview_img, imgurl,
			 * R.drawable.loading);
			 */
			// viewHolder.p_listview_img.setBackgroundResource(R.drawable.tab_drug_img);
			viewHolder.p_listview_img
					.setImageResource(R.drawable.drug_photo_def_pic);
		}

		return convertView;
	}

	private class ViewHolder {
		TextView p_listview_title;
		TextView p_listview_store;
		ImageView p_listview_img;
		ImageView p_listview_bao;
		ImageView p_listview_otc;
		ImageView p_listview_h;
	}

	/**
	 * 添加列表项
	 * 
	 * @param item
	 */
	public void addItem(Map<String, String> item) {
		druglist.add(item);
	}

}
