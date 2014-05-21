package coolbuy360.searchhealth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import coolbuy360.adapter.DiseaseDescriptionAdapter;
import coolbuy360.logic.Disease;
import coolbuy360.logic.DrugStore;
import coolbuy360.service.searchApp;

/**
 * @author Administrator
 *
 */
public class DiseaseDecription extends Activity {
	
	LinearLayout async_begin;
	LinearLayout async_error;
	ExpandableListView dis_decription_explistview;
	
	String diseaseid;
	String diseasename;
	
	//List<Map<String, String>> grouplist;
	
	//List<List<Map<String, String>>> childs;
	//Map<String, String> child;
	//List<Map<String, String>> 
	
	//List<Map<String,String>> getInfo(String diseaseID)
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//为退出做准备
		searchApp.getInstance().addActivity(this);
		setContentView(R.layout.drugdisease_description);
		Bundle bundle=getIntent().getExtras();		
		diseaseid=bundle.getString("diseaseid");
		diseasename=bundle.getString("diseasesname");
		
		async_begin = (LinearLayout) findViewById(R.id.async_begin);
		async_error = (LinearLayout) findViewById(R.id.async_error);
		dis_decription_explistview = (ExpandableListView) findViewById(R.id.dis_decription_explistview);
		
		ImageButton actionbar_pre_btn=(ImageButton)this.findViewById(R.id.actionbar_pre_btn);		
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DiseaseDecription.this.finish();
			}
		});
			
		new AsyncLoad().execute();
	}	
	
	/**初始化数据
	 * @param id
	 *        传入的疾病id
	 */
	private void initdata(Map<String, String> diseaseInfo) {
		// TODO Auto-generated method stub
		
		View disInfoHead=getLayoutInflater().inflate(R.layout.drugdisease_description_head, null);	
		View disInfofoot=getLayoutInflater().inflate(R.layout.drugdisease_description_foot, null);
		
		TextView txtname=(TextView)disInfoHead.findViewById(R.id.dis_decription_name);
		txtname.setText(diseaseInfo.get("diseasename"));	
		TextView txtothername=(TextView)disInfoHead.findViewById(R.id.dis_decription_othername);
		txtothername.setText(diseaseInfo.get("diseasealias"));
		TextView txtpart=(TextView)disInfoHead.findViewById(R.id.dis_decription_part);
		txtpart.setText(diseaseInfo.get("part"));
		TextView txtintroduct=(TextView)disInfoHead.findViewById(R.id.dis_decription_introduct);
		txtintroduct.setText(diseaseInfo.get("descr"));	
		
		List<Map<String, String>> grouplist=new ArrayList<Map<String,String>>();
		List<List<Map<String, String>>> childs=new ArrayList<List<Map<String,String>>>();		
		
		Map<String, String> groupdataMap1=new HashMap<String, String>();
		groupdataMap1.put("group", "症状表现");
		grouplist.add(groupdataMap1);
		Map<String, String> child1_1=new HashMap<String, String>();
		child1_1.put("child", diseaseInfo.get("symptoms"));		
		List<Map<String, String>> child1=new ArrayList<Map<String,String>>();
		child1.add(child1_1);
		childs.add(child1);		
		
		Map<String, String> groupdataMap2=new HashMap<String, String>();
		groupdataMap2.put("group", "诊断鉴别");
		grouplist.add(groupdataMap2);	
		Map<String, String> child2_1=new HashMap<String, String>();
		child2_1.put("child", diseaseInfo.get("diffdiag"));		
		List<Map<String, String>> child2=new ArrayList<Map<String,String>>();
		child2.add(child2_1);
		childs.add(child2);
		
		Map<String, String> groupdataMap3=new HashMap<String, String>();
		groupdataMap3.put("group", "病因");
		grouplist.add(groupdataMap3);
		//childlist.add(diseaseinfolist.get(0).get("pathogeny"));
		Map<String, String> child3_1=new HashMap<String, String>();
		child3_1.put("child", diseaseInfo.get("pathogeny"));		
		List<Map<String, String>> child3=new ArrayList<Map<String,String>>();
		child3.add(child3_1);
		childs.add(child3);
				
		Map<String, String> groupdataMap4=new HashMap<String, String>();
		groupdataMap4.put("group", "治疗");
		grouplist.add(groupdataMap4);
		//childlist.add(diseaseinfolist.get(0).get("treat"));
		
		Map<String, String> child4_1=new HashMap<String, String>();
		child4_1.put("child", diseaseInfo.get("treat"));		
		List<Map<String, String>> child4=new ArrayList<Map<String,String>>();
		child4.add(child4_1);
		childs.add(child4);	
		
		Map<String, String> groupdataMap5=new HashMap<String, String>();
		groupdataMap5.put("group", "预防保健");
		grouplist.add(groupdataMap5);
		//childlist.add(diseaseinfolist.get(0).get("preventivecare"));	
		
		Map<String, String> child5_1=new HashMap<String, String>();
		child5_1.put("child", diseaseInfo.get("preventivecare"));		
		List<Map<String, String>> child5=new ArrayList<Map<String,String>>();
		child5.add(child5_1);
		childs.add(child5);		
		ExpandableListView exp=(ExpandableListView)this.findViewById(R.id.dis_decription_explistview);
		 /*SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(  
                 this, groups, R.layout.groups, new String[] { "group" },  
                 new int[] { R.id.group }, childs, R.layout.childs,  
                 new String[] { "child" }, new int[] { R.id.child });  
         setListAdapter(adapter); */ 
		DiseaseDescriptionAdapter adapter=new DiseaseDescriptionAdapter(this, grouplist, childs);
		
		exp.addHeaderView(disInfoHead);
		
		Button yytj=(Button)disInfofoot.findViewById(R.id.dis_description_yytj); 
		yytj.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(DiseaseDecription.this, "暂无用药推荐数据", 1).show();
				Intent listIntent = new Intent().setClass(DiseaseDecription.this,
						DrugList.class);

				Bundle bundle = new Bundle();
				bundle.putString("disease_id", diseaseid);// 设置需要传递的值
				bundle.putString("disease_name", diseasename);
				bundle.putString("from", "disease");
				// bundle.putString("type_name", )
				listIntent.putExtras(bundle);
				startActivityForResult(listIntent, 0);
			}
		});	

		exp.addFooterView(disInfofoot);		
		exp.setAdapter(adapter);
		
		diseaseInfo=null;
		grouplist=null;
		childs=null;		
	}

	/**
	 * 异步加载药店信息
	 */
	private class AsyncLoad extends AsyncTask<String, Void, Integer> {
		private List<Map<String, String>> dislist = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			async_begin.setVisibility(View.VISIBLE);
			dis_decription_explistview.setVisibility(View.GONE);
		}

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub

			try {
				if (diseaseid !=null && !(diseaseid.equals(""))) {
					dislist = Disease.getInfo(diseaseid);
					if (dislist != null) {
						return (dislist.size() > 0) ? 0 : 1;
					} else {
						return 2;// 网络连接错误
					}
				} else {
					return 1;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return 2;
			}

		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if (result == 0)//
			{
				initdata(dislist.get(0));
				dis_decription_explistview.setVisibility(View.VISIBLE);

				async_begin.setVisibility(View.GONE);
			} else if (result == 1) {
				async_begin.setVisibility(View.GONE);
				TextView async_error_txt = (TextView) async_error
						.findViewById(R.id.async_error_txt);
				async_error_txt.setText("没有找到当前疾病的信息。");
				Button async_error_reflesh = (Button) async_error
						.findViewById(R.id.async_error_reflesh);
				async_error_reflesh.setVisibility(View.GONE);
				async_error.setVisibility(View.VISIBLE);
			} else if (result == 2) {
				async_begin.setVisibility(View.GONE);
				async_error.setVisibility(View.VISIBLE);
				TextView async_error_txt = (TextView) async_error
						.findViewById(R.id.async_error_txt);
				async_error_txt.setText(R.string.error_nonetwork);
				Button async_error_reflesh = (Button) async_error
						.findViewById(R.id.async_error_reflesh);
				async_error_reflesh.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						async_error.setVisibility(View.GONE);
						async_begin.setVisibility(View.VISIBLE);
						new AsyncLoad().execute();
					}
				});
			}
		}
	}
	
	
	/*",DIS_Info.DiseaseName as DiseaseName" +   //疾病名称
	",DIS_Info.Part as Part" +  //部位
	",DIS_Info.Pathogeny as Pathogeny" +  //病因
	",DIS_Info.Symptoms as Symptoms" +  //症状表现
	",DIS_Info.Descr as Descr" +  //概述
	",DIS_Info.PreventiveCare as PreventiveCare" +  //预防保健
	",DIS_Info.DiseaseAlias as DiseaseAlias" +  //疾病别名
	",DIS_Info.DiffDiag as DiffDiag" +  //诊断鉴别
	",DIS_Info.Treat as Treat ";  //治疗
*/	/**
	 * 药品属性Key及属性名称
	 */
/*	public static Map<String,String> diseaseinfo = new HashMap<String, String>()
	{
		{	
			put("symptoms","症状表现");
			put("diffdiag","诊断鉴别");
			put("pathogeny","病因");		
			put("treat","治疗");
			put("preventivecare","预防保健");
		
		}			
	};	*/
	
	
}
