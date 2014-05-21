package coolbuy360.searchhealth;

import java.util.List;
import java.util.Map;

import coolbuy360.logic.Drug;
import coolbuy360.service.AaynImageLoaderUtil;
import coolbuy360.service.ImageManager;
import coolbuy360.service.Util;
import coolbuy360.service.searchApp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

@SuppressLint("NewApi")
public class ExposureDetail extends Activity {	
	AaynImageLoaderUtil asynImageLoader;	
	
	LinearLayout async_begin;
	
	ImageView imgview;
	TextView txtname;
	TextView txtdescri;
	RatingBar ratdanger;
	TextView txtcom;	
	TextView txt_drugpubtime;
	TextView txt_drugresult;
	TextView txt_drugdanger;
	TextView txt_drugdescri;
	TextView txt_drugproducttime;
	TextView txt_drugproductnum;
	TextView txt_drugresource;
	TextView txt_drugdangerhead;
	LinearLayout linelayout_danger;
	LinearLayout linelayout_drug_desc;
	ImageView img_h;
	LinearLayout exposure_druglist_item;
	LinearLayout exposure_drug_resource_layout;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//为退出做准备
		searchApp.getInstance().addActivity(this);
		setContentView(R.layout.exposure_detail);
		Bundle bundle=new Bundle();
		bundle=getIntent().getExtras();
		String blacklistID=bundle.getString("drugnameid");
		int  drugtype=Integer.parseInt(bundle.getString("drugtype"));
	
		
		ImageButton actionbar_pre_btn=(ImageButton)this.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ExposureDetail.this.finish();
			}
		});
		async_begin = (LinearLayout) this.findViewById(R.id.async_begin);
		async_begin.setVisibility(View.VISIBLE);
		loadData(blacklistID,drugtype);		
	}
	/**
	 * 初始加载数据
	 * @param druglistid
	 *        药品列表id
	 * @param type
	 *        类型id
	 */
	private void loadData(String druglistid,int type)
	{	
		 imgview=(ImageView)this.findViewById(R.id.exposure_listview_img);
		 txtname=(TextView)this.findViewById(R.id.exposure_listview_name);
		 txtdescri=(TextView)this.findViewById(R.id.exposure_listview_descri);
		 ratdanger=(RatingBar)this.findViewById(R.id.exposure_listview_ratingbar);
		 txtcom=(TextView)this.findViewById(R.id.exposure_listview_com);
		
		 txt_drugpubtime=(TextView)this.findViewById(R.id.exposure_drug_pubtime);
		 txt_drugresult=(TextView)this.findViewById(R.id.exposure_drug_result);
		 txt_drugdanger=(TextView)this.findViewById(R.id.exposure_drug_danger);
		 txt_drugdescri=(TextView)this.findViewById(R.id.exposure_drug_decri);
		 txt_drugproducttime=(TextView)this.findViewById(R.id.exposure_drug_producttime);
		 txt_drugproductnum=(TextView)this.findViewById(R.id.exposure_drug_productnum);
		 txt_drugresource=(TextView)this.findViewById(R.id.exposure_drug_resource);
		 txt_drugdangerhead=(TextView)this.findViewById(R.id.exposure_drug_danger_head);
		 linelayout_danger=(LinearLayout)this.findViewById(R.id.exposure_drug_danger_layout);
		 linelayout_drug_desc=(LinearLayout)this.findViewById(R.id.exposure_drug_decri_layout);
		 img_h=(ImageView)this.findViewById(R.id.exposure_listview_h);
		 exposure_druglist_item=(LinearLayout)this.findViewById(R.id.exposure_druglist_item_id);
		 exposure_drug_resource_layout=(LinearLayout)this.findViewById(R.id.exposure_drug_resource_layout);
		//1  black  2  red
		switch (type) {
		case 1:	
			//List<Map<String,String>> druginfolist=coolbuy360.logic.BlackDrug.getInfo(druglistid);
			//druginfo=druginfolist.get(0);
			//bindBlack();
			 new  AsyLoad().execute("black",druglistid);
			break;
		case 2:
			
			//List<Map<String,String>> redruginfolist=coolbuy360.logic.RedDrug.getInfo(druglistid);		
			//druginfo=redruginfolist.get(0);
			//bindRed();
			new  AsyLoad().execute("red",druglistid);
			break;	

		default:
			break;
		}
	}

	private void bindRed(Map<String,String> druginfo) {
		final String DrugID=druginfo.get("drugid");
		String reddrugimg=druginfo.get("drugimg");
		String reddrugname=druginfo.get("drugname");
		String redsamplingresults=druginfo.get("samplingresults");
		String redexpdescr=druginfo.get(" updescr");
		String redexptime=druginfo.get("uptime");//抽检日期
		String redproductiondate=druginfo.get("productiondate");
		String redbatchnum=druginfo.get("batchnum");//
		String redsrctitle=druginfo.get("srctitle");
		final String redsrcurl=druginfo.get("srcurl");//源链接
		
		txtname.setText(reddrugname);
		txtdescri.setText("合格药品");
		txt_drugpubtime.setText(Util.getDateFormat(redexptime, "yyyy-MM-dd"));
		txt_drugresult.setText(redsamplingresults);
		if (redexpdescr != null && !(redexpdescr.equals(""))) {
			txt_drugdescri.setText(redexpdescr);
		} else {
			linelayout_drug_desc.setVisibility(View.GONE);
		}
		txt_drugproducttime.setText(Util.getDateFormat(redproductiondate, "yyyy-MM-dd") );
		txt_drugproductnum.setText(redbatchnum);
		txt_drugresource.setText(redsrctitle);
		
		if (reddrugimg != null && !reddrugimg.equals("")) {
			ImageManager.from(this).displayImage(imgview, reddrugimg,
					R.drawable.drug_photo_def_pic, 150, 150);
		} else {
			imgview.setImageResource(R.drawable.drug_photo_def_pic);
		}	
		
		/*asynImageLoader = new AaynImageLoaderUtil();
		asynImageLoader.showImageAsyn(imgview, reddrugimg,
				R.drawable.loading);*/
	
		exposure_drug_resource_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent webIntent=new Intent().setClass(ExposureDetail.this, ExposureWebView.class);
				Bundle bundle=new Bundle();
				bundle.putString("weburl", redsrcurl);
				webIntent.putExtras(bundle);
				startActivity(webIntent);
				
			}
		});
		
		linelayout_danger.setVisibility(View.GONE);

		ratdanger.setVisibility(View.GONE);
		txtcom.setVisibility(View.GONE);
		
		exposure_druglist_item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				try {
					List<Map<String, String>> druglist=Drug.getInfo(DrugID);
					Map<String, String> map = (Map<String, String>)druglist.get(0);
					String drug_name = map.get("drugname");
					String drug_id = map.get("drugid");
					String drug_imgurl = map.get("drugimg");
					String drug_store = map.get("enterprisename");
					String drug_otc=map.get("prescriptiontype");
					String drug_h=map.get("approvaltype");
					String drug_bao=map.get("ishcdrug");

					Bundle bundle = new Bundle();
					bundle.putString("drugname", drug_name);
					bundle.putString("drugid", drug_id);
					bundle.putString("drugimg", drug_imgurl);
					bundle.putString("drugstore", drug_store);
					bundle.putString("h", drug_h.trim());
					bundle.putString("otc", drug_otc.trim());
					bundle.putString("bao", drug_bao.trim());
					
					Intent drugintent = new Intent().setClass(ExposureDetail.this,
								DrugProductDetail.class);// 跳入下一个activity
					drugintent.putExtras(bundle);
					startActivity(drugintent);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void bindBlack(Map<String,String> druginfo) {
		String drugimg=druginfo.get("drugimg");
		String drugname=druginfo.get("drugname");
		int dangerlever= Integer.parseInt(druginfo.get("dangerlever")) ;
		String exptime=druginfo.get("exptime");
		String samplingresults=druginfo.get("samplingresults");
		String danger=druginfo.get("danger");
		String expdescr=druginfo.get(" expdescr");
		String productiondate=druginfo.get("productiondate");
		String batchnum=druginfo.get("batchnum");//
		String srctitle=druginfo.get("srctitle");
		final String srcurl=druginfo.get("srcurl");
		
		txtname.setText(drugname);
		ratdanger.setRating(dangerlever);
		
		txt_drugpubtime.setText(Util.getDateFormat(exptime, "yyyy-MM-dd ") );
		txt_drugresult.setText(samplingresults);
		txt_drugdanger.setText(danger);
		if (expdescr != null && !(expdescr.equals(""))) {
			txt_drugdescri.setText(expdescr);
		} else {
			linelayout_drug_desc.setVisibility(View.GONE);
		}
		txt_drugproducttime.setText(Util.getDateFormat(productiondate, "yyyy-MM-dd "));
		txt_drugproductnum.setText(batchnum);
		txt_drugresource.setText(srctitle);
		
		if (drugimg != null && !drugimg.equals("")) {
			ImageManager.from(this).displayImage(imgview, drugimg,
					R.drawable.drug_photo_def_pic, 150, 150);
		} else {
			imgview.setImageResource(R.drawable.drug_photo_def_pic);
		}
		
		/*asynImageLoader = new AaynImageLoaderUtil();
		asynImageLoader.showImageAsyn(imgview, drugimg,
				R.drawable.loading);*/
		
		exposure_drug_resource_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent webIntent=new Intent().setClass(ExposureDetail.this, ExposureWebView.class);
				Bundle bundle=new Bundle();
				bundle.putString("weburl", srcurl);
				webIntent.putExtras(bundle);
				startActivity(webIntent);
				
			}
		});
		
		img_h.setVisibility(View.GONE);
		txtdescri.setVisibility(View.GONE);			
		txtcom.setVisibility(View.VISIBLE);
		ratdanger.setVisibility(View.VISIBLE);
	}
/*	
//		",DRG_RedList.DrugID as DrugID" +   //药品ID
	",DRG_RedList.PubTime as PubTime" +  //发布时间
	",DRG_RedList.UpTime as UpTime" +  //上榜时间
	",DRG_RedList.ProductionDate as ProductionDate " +  //生产日期
	",DRG_RedList.BatchNum as BatchNum " +  //批号
	",DRG_RedList.SamplingResults as SamplingResults " +  //抽检结果
	",DRG_RedList.SrcURL as SrcURL " +  //来源网页网址
	",DRG_RedList.SrcTitle as SrcTitle " +  //来源网页标题
	",DRG_RedList.UpDescr as UpDescr " +  //上榜概述
	",DRG_Info.DrugName as drugname " +  //药品名称
	",DRG_Info.DrugImg as DrugImg "; //药品图片
*/	
	private  final class  AsyLoad extends AsyncTask<String, Void, Integer>
	{
		List<Map<String,String>> innerDrugInfolist;
		Map<String,String> innerDrugInfo;
		Type type=null; 
		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			if(params.length>0)
			{			
				if(params[0].equals("black"))
				{
					type=Type.BLACK;
					try {
						innerDrugInfolist=coolbuy360.logic.BlackDrug.getInfo(params[1]);
						innerDrugInfo=innerDrugInfolist.get(0);
						if(innerDrugInfo!=null)
						{
							
							return (innerDrugInfo.size()>0)?0:1;					
						}
						else
						{
							return 2;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						return 2;
					}
				}
				else
				{					
					type=Type.RED;
					try {
						innerDrugInfolist=coolbuy360.logic.RedDrug.getInfo(params[1]);
						innerDrugInfo=innerDrugInfolist.get(0);
						if(innerDrugInfo!=null)
						{
							return (innerDrugInfo.size()>0)?0:1;					
						}
						else
						{
							return 2;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						return 2;
					}
				}
			}
				
			return null;
		}
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			
			switch (type) 
			{
			 case BLACK:
				
				if(result==0)
				{
					bindBlack(innerDrugInfo);	
					async_begin.setVisibility(View.GONE);
				}				
				else if(result==1) {
					async_begin.setVisibility(View.GONE);
					LinearLayout async_error = (LinearLayout) findViewById(R.id.async_error);
					TextView async_error_txt=(TextView)async_error.findViewById(R.id.async_error_txt);
					async_error_txt.setText("没有相关数据");
					async_error.setVisibility(View.VISIBLE);
				}
				else if(result==2)
				{
					async_begin.setVisibility(View.GONE);
					LinearLayout async_error = (LinearLayout) findViewById(R.id.async_error);
					async_error.setVisibility(View.VISIBLE);
					return;
				}
				
				break;

			 case RED:				
				 if(result==0)
					{
						bindRed(innerDrugInfo);
						async_begin.setVisibility(View.GONE);
					}				
					else if(result==1) {
						async_begin.setVisibility(View.GONE);
						LinearLayout async_error = (LinearLayout) findViewById(R.id.async_error);
						TextView async_error_txt=(TextView)async_error.findViewById(R.id.async_error_txt);
						async_error_txt.setText("没有相关数据");
						async_error.setVisibility(View.VISIBLE);
					}
					else if(result==2)
					{			
						async_begin.setVisibility(View.GONE);
						LinearLayout async_error = (LinearLayout) findViewById(R.id.async_error);
						async_error.setVisibility(View.VISIBLE);
						return;
					}
					
				 
				break;			
			
			}
		}		
	}
	
	enum Type
	{
		BLACK,RED
	}

}
