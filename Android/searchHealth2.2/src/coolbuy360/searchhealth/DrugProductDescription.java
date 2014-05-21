package coolbuy360.searchhealth;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import coolbuy360.control.ScoreChangePopup;
import coolbuy360.logic.Drug;
import coolbuy360.logic.DrugStore;
import coolbuy360.service.AaynImageLoaderUtil;
import coolbuy360.service.CommandResult;
import coolbuy360.service.SetImgResoruce;
import coolbuy360.service.searchApp;

public class DrugProductDescription extends Activity {

	private String drugid = "";
	private String drugname = "";
	private String drugstore = "";
	private String drugimg = "";
	private String h = "";
	private String otc = "";
	private String bao = "";
	CheckBox collect_checkbox;
	private Boolean ischecked;

	LinearLayout async_begin;
	LinearLayout async_error;
	TextView p_decri_txt_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drugproduct_description);
		// 为退出做准备
		searchApp.getInstance().addActivity(this);
		Bundle bundle = getIntent().getExtras();
		drugid = bundle.getString("drugid");
		drugname = bundle.getString("drugname");
		drugstore = bundle.getString("drugstore");
		drugimg = bundle.getString("drugimg");

		bao = bundle.getString("bao");
		otc = bundle.getString("otc");
		h = bundle.getString("h");

		async_begin = (LinearLayout) findViewById(R.id.async_begin);
		async_error = (LinearLayout) findViewById(R.id.async_error);
		p_decri_txt_content = (TextView) this
				.findViewById(R.id.p_decri_txt_content);
		collect_checkbox = (CheckBox) this.findViewById(R.id.collect_checkbox);
		LinearLayout collect_btn = (LinearLayout) this
				.findViewById(R.id.collect_btn);
		collect_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (collect_checkbox.isChecked()) {
					collect_checkbox.setChecked(false);
				} else {
					collect_checkbox.setChecked(true);
				}
			}
		});

		ImageView imgviewdruimg = (ImageView) this
				.findViewById(R.id.p_descri_img);
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DrugProductDescription.this.finish();
			}
		});

		// imgviewdruimg.seti
		AaynImageLoaderUtil asynImageLoader = new AaynImageLoaderUtil();
		asynImageLoader.showImageAsyn(imgviewdruimg, drugimg,
				R.drawable.loading);

		TextView txtdrugnameTextView = (TextView) this
				.findViewById(R.id.p_descri_title);
		txtdrugnameTextView.setText(drugname);
		TextView txtdrugstore = (TextView) this
				.findViewById(R.id.p_descri_store);
		txtdrugstore.setText(drugstore);

		ImageView imgviewotc = (ImageView) this.findViewById(R.id.p_descri_otc);

		TextView txtviewotc = (TextView) this
				.findViewById(R.id.p_descri_txt_otc);
		if (otc != null & !otc.equals("")) {
			int otcimg = SetImgResoruce.imageResurce_otc(Integer.parseInt(otc
					.trim()));
			if (otcimg != 0) {
				imgviewotc.setImageResource(otcimg);
				txtviewotc.setText(SetImgResoruce.imageResource_otc_txt(Integer
						.parseInt(otc.trim())));
				imgviewotc.setVisibility(View.VISIBLE);
				txtviewotc.setVisibility(View.VISIBLE);
			}
		}
		ImageView imgviewbao = (ImageView) this.findViewById(R.id.p_descri_bao);
		TextView txtviewbao = (TextView) this
				.findViewById(R.id.p_descri_txt_bao);
		if (bao != null & !bao.equals("")) {
			int baoimg = SetImgResoruce.imgResurce_bao(bao.trim());
			if (baoimg != 0) {
				imgviewbao.setImageResource(baoimg);
				txtviewbao
						.setText(SetImgResoruce.imgResurce_bao_txt(bao.trim()));
				imgviewbao.setVisibility(View.VISIBLE);
				txtviewbao.setVisibility(View.VISIBLE);
			}
		}
		ImageView imgviewh = (ImageView) this.findViewById(R.id.p_descri_h);
		TextView txtviewh = (TextView) this.findViewById(R.id.p_descri_txt_h);
		if (h != null & !h.equals("")) {
			int himg = SetImgResoruce.imgResource(h.trim());
			if (himg != 0) {
				imgviewh.setImageResource(himg);
				txtviewh.setText(SetImgResoruce.imgResource_txt(h.trim()));
				txtviewh.setVisibility(View.VISIBLE);
				imgviewh.setVisibility(View.VISIBLE);
			}
		}
		
		new AsyLoadDrugInfo().execute();
	}

	/**
	 * 异步加载药品信息
	 */
	private class AsyLoadDrugInfo extends AsyncTask<Integer, Void, Integer> {
		List<Map<String, String>> innerdruglist;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			async_begin.setVisibility(View.VISIBLE);
			p_decri_txt_content.setVisibility(View.GONE);
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub

			try {
				innerdruglist = Drug.getInfo(drugid);
				if (innerdruglist != null) {
					return (innerdruglist.size() > 0) ? 0 : 1;
				} else {
					return 2;// 网络连接错误
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return 2;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if (result == 0) {
				StringBuilder txtstring = new StringBuilder();
				Map<String, String> druginfo = innerdruglist.get(0);
				Map<String, String> drugproperties = Drug.DrugProperties;
				Set keyset = drugproperties.keySet();
				Iterator iterator = keyset.iterator();
				while (iterator.hasNext()) {
					String key = iterator.next().toString();// key
					String value = druginfo.get(key);
					if (value != null && !(value.equals(""))) {
						txtstring
								.append(" <span style=\"font-size: 18px;font-weight:bold; \">【"
										+ drugproperties.get(key)
										+ "】</span><br/>");
						txtstring.append(druginfo.get(key) + "<br/>");
					}
				}
				p_decri_txt_content
						.setText(Html.fromHtml(txtstring.toString()));
				p_decri_txt_content.setVisibility(View.VISIBLE);
				
				async_begin.setVisibility(View.GONE);

				// 异步初始化药品收藏状态
				new AsyLoadDrugIsCollected().execute();
			} else if (result == 1) {
				async_begin.setVisibility(View.GONE);
				LinearLayout async_error = (LinearLayout) findViewById(R.id.async_error);
				TextView async_error_txt = (TextView) async_error
						.findViewById(R.id.async_error_txt);
				async_error_txt.setText("没有找到当前药品的说明书。");
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
						new AsyLoadDrugInfo().execute();
					}
				});
				return;
			}
		}
	}

	/**
	 * 异步判断药品的收藏状态
	 */
	private class AsyLoadDrugIsCollected extends
			AsyncTask<Integer, Void, Integer> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub

			try {
				ischecked = Drug.isCollected(getBaseContext(), drugid);
				if (ischecked != null) {
					return 0;
				} else {
					return 2;// 网络连接错误
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return 2;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if (result == 0) {
				if (ischecked) {
					collect_checkbox.setChecked(true);
				} else {
					collect_checkbox.setChecked(false);
				}
				collect_checkbox.setOnCheckedChangeListener(new chkOnCheckedChang());
			} else if (result == 2) {
				collect_checkbox.setChecked(false);
				collect_checkbox.setOnCheckedChangeListener(new chkOnCheckedChang());
				Toast.makeText(DrugProductDescription.this, "获取收藏状态失败",
						Toast.LENGTH_SHORT).show();
				return;
			}
		}
	}

	/**
	 * 设置chk状态改变的监听事件
	 * 
	 * @author chenlw
	 */
	public class chkOnCheckedChang implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			ischecked = collect_checkbox.isChecked();
			if (ischecked) {
				new AsyncCollect().execute(drugid, "do");
			} else {
				new AsyncCollect().execute(drugid, "undo");
			}
		}
	}

	/**
	 * 药品收藏
	 */
	private class AsyncCollect extends AsyncTask<String, Void, Boolean> {
		CommandResult resultmessage;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub

			try {
				if (params.length > 0) {
					if (params[1].toString().equals("do")) {
						resultmessage = Drug.doCollect(getBaseContext(),
								params[0]);
						return resultmessage.getResult();
					} else {
						resultmessage = Drug.unCollect(getBaseContext(),
								params[0]);
						return resultmessage.getResult();
					}
				} else {
					return false;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if (result)// 如果登录成功
			{
				Toast.makeText(getBaseContext(), "操作成功", Toast.LENGTH_SHORT).show();
				try {
					int addscore = Integer.parseInt(resultmessage
							.getValue("addscore"));
					if (addscore != 0) {
						showScoreChange(R.id.collect_btn, addscore);
					}
				} catch (Exception e) {
				}
			} else {
				Toast.makeText(getBaseContext(), resultmessage.getMessage(), Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
	
	/**
	 * 显示积分变化弹窗
	 * @param parentViewID
	 * @param value
	 */
	private void showScoreChange(int parentViewID, int value) {
		ScoreChangePopup popupWindow = new ScoreChangePopup(this, value);
		popupWindow.showAtLocation(findViewById(parentViewID),
				Gravity.CENTER, 0, 0);
		popupWindow.delayedDismiss();
	}
}
