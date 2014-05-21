/**
 * 
 */
package coolbuy360.searchhealth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import coolbuy360.logic.AppConfig;

import coolbuy360.service.CommonMethod;
import coolbuy360.service.FileService;
import coolbuy360.service.FileServiceSize;
import coolbuy360.service.UpdateApp;
import coolbuy360.service.Util;
import coolbuy360.service.fileUtril;
import coolbuy360.service.searchApp;

/**
 * ����
 * 
 * @author yangxc
 * 
 */
public class Settings extends Activity {

	Long cacheSize;
	Long fileSize;
	Long totalSize;
	TextView txt_size;
	RelativeLayout setting_item_imgQuality;
	AppConfig appconfig;
	CheckBox setting_check_autoOptimize;
	String imgQualityValue;
	String autoischeck;
	searchApp app;
	Dialog imgQualityDialog;
	TextView setting_desc_imgQuality;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		// Ϊ�˳���׼��
		app = searchApp.getInstance();
		app.addActivity(this);

		// ���÷��ذ�ť
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Settings.this.finish();
			}
		});

		setting_desc_imgQuality = (TextView) this
				.findViewById(R.id.setting_desc_imgQuality);
		RelativeLayout setting_item_autoOptimize = (RelativeLayout) this
				.findViewById(R.id.setting_item_autoOptimize);
		setting_item_imgQuality = (RelativeLayout) this
				.findViewById(R.id.setting_item_imgQuality);
		RelativeLayout setting_item_clearCache = (RelativeLayout) this
				.findViewById(R.id.setting_item_clearCache);
		RelativeLayout setting_item_checkUpdate = (RelativeLayout) this
				.findViewById(R.id.setting_item_checkUpdate);
		RelativeLayout setting_item_legalNotice = (RelativeLayout) this
				.findViewById(R.id.setting_item_legalNotice);
		RelativeLayout setting_item_aboutUs = (RelativeLayout) this
				.findViewById(R.id.setting_item_aboutUs);

		setting_check_autoOptimize = (CheckBox) this
				.findViewById(R.id.setting_check_autoOptimize);
		txt_size = (TextView) this.findViewById(R.id.setting_size_cache);
		setting_item_autoOptimize.setOnTouchListener(new CommonMethod.setOnPressed());
		/* setting_item_imgQuality.setOnTouchListener(); */
		setting_item_clearCache.setOnTouchListener(new CommonMethod.setOnPressed());
		setting_item_checkUpdate.setOnTouchListener(new CommonMethod.setOnPressed());
		setting_item_legalNotice.setOnTouchListener(new CommonMethod.setOnPressed());
		setting_item_aboutUs.setOnTouchListener(new CommonMethod.setOnPressed());

		appconfig = new AppConfig(Settings.this);
		new AsyloadState().execute();// ��ʼ��״̬

		// ���Զ��Ż������
		setting_item_autoOptimize.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				// ����CheckBox�Ĺ�ѡ״̬
				setting_check_autoOptimize.setChecked(setting_check_autoOptimize
						.isChecked() ? false : true);
			}
		});
		// �Զ��Ż�checkѡ��״̬�ı��ʱ��ִ�е�״̬
		setting_check_autoOptimize
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						// ִ������Ż�����
						if (isChecked) {
							setting_item_imgQuality.setClickable(false);
							setting_item_imgQuality
									.setBackgroundDrawable(getResources()
											.getDrawable(
													R.drawable.settingitem_tail_disable));
							autoSetting(isChecked);

						} else {
							setting_item_imgQuality.setClickable(true);
							setting_item_imgQuality
									.setBackgroundDrawable(getResources()
											.getDrawable(
													R.drawable.settingitem_tail));
							autoSetting(isChecked);
						}

					}
				});

		// ��ͼƬ���������
		setting_item_imgQuality.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				LayoutInflater factory = LayoutInflater.from(Settings.this);
				final View EntryView = factory.inflate(
						R.layout.setting_imgquality_dialog, null);

				final RelativeLayout setting_imgquality_item_smallimg = (RelativeLayout) EntryView
						.findViewById(R.id.setting_imgquality_item_smallimg);
				final RelativeLayout setting_imgquality_item_bigimg = (RelativeLayout) EntryView
						.findViewById(R.id.setting_imgquality_item_bigimg);
				final RadioButton radiobtn_big = (RadioButton) EntryView
						.findViewById(R.id.setting_autoOptimize_radio_bigimg);
				final RadioButton radiobtn_small = (RadioButton) EntryView
						.findViewById(R.id.setting_autoOptimize_radio_smallimg);
				radiocheck(imgQualityValue, radiobtn_big, radiobtn_small);

				setting_imgquality_item_smallimg
						.setOnTouchListener(new CommonMethod.setOnPressed());
				setting_imgquality_item_bigimg
						.setOnTouchListener(new CommonMethod.setOnPressed());

				setting_imgquality_item_smallimg
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								updateImgQualtyDialog("low", radiobtn_big,
										radiobtn_small);
							}
						});

				setting_imgquality_item_bigimg
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								updateImgQualtyDialog("high", radiobtn_big,
										radiobtn_small);
							}
						});

				radiobtn_big.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						updateImgQualtyDialog("high", radiobtn_big,
								radiobtn_small);
					}
				});

				radiobtn_small.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						updateImgQualtyDialog("low", radiobtn_big,
								radiobtn_small);
					}
				});

				imgQualityDialog = new AlertDialog.Builder(Settings.this)
						.setView(EntryView).setCancelable(true).show();
				imgQualityDialog.setCanceledOnTouchOutside(true);
			}
		});

		// ��������桱���
		setting_item_clearCache.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				Log.i("chenlinwei", Util.getNowDate("yyyy-MM-dd HH:mm:ss") + "");
				Boolean isRun = Util.runCommand("chmod 777 " + getCacheDir());
				// int delCount= Utril.clearCacheFolder(getCacheDir(),
				// Long.parseLong(Utril.getNowDate("yyyy-MM-dd HH:mm:ss")));
				if (isRun) {

					new asyDelateCache().execute();
				} else {
					Toast.makeText(Settings.this, "��Ȩ�޲���", 1).show();
				}

				/*
				 * if(delCount>0) { Toast.makeText(Settings.this,
				 * "ɾ�����ݸ�����"+delCount, 1).show(); }
				 */
			}
		});

		// �������¡����
		setting_item_checkUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				UpdateApp update = new UpdateApp(Settings.this);
				update.startupdate();
			}
		});

		// ���������������
		setting_item_legalNotice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				Intent pageIntent = new Intent().setClass(Settings.this,
						LegalNotice.class);
				startActivity(pageIntent);
			}
		});

		// �����ڡ����
		setting_item_aboutUs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				Intent pageIntent = new Intent().setClass(Settings.this,
						About.class);
				startActivity(pageIntent);
			}
		});
	}

	/**
	 * ���������첽������
	 * 
	 * @author chenlw
	 * 
	 */
	private final class asyDelateCache extends AsyncTask<String, Void, Integer> {
		private ProgressDialog pBarcheck;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pBarcheck = new ProgressDialog(Settings.this);
			// dialog.setTitle("Indeterminate");
			pBarcheck.setMessage("����ɾ�����棡");
			pBarcheck.setIndeterminate(true);
			pBarcheck.setCancelable(true);
			pBarcheck.show();
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				FileService filservice = new FileService(Settings.this);
				filservice.deleteFolderFile(getCacheDir() + "", true);
				filservice.deleteFolderFile(getFilesDir() + "", true);
                filservice.deleteFolderFile( fileUtril.getSDCachePath(), false);
				if (totalSize > 0) {
					return 0;
				} else {

					return 1;
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				// Toast.makeText(Settings.this, e.getMessage(), 1).show();
				return 1;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			pBarcheck.cancel();
			if (result == 0) {
				Toast.makeText(Settings.this,
						"������棺" + FileServiceSize.FormetFileSize(totalSize), 1)
						.show();
				totalSize = Long.parseLong("0");
				txt_size.setText("0B");
			} else {
				Toast.makeText(Settings.this, "û�л�����Ҫ���", 1).show();
			}
		}

	}

	/**
	 * ��ʼ����ȡ����
	 */
	private Integer InitData() {
		try {
			cacheSize = FileServiceSize.getFolderSize(getCacheDir());
			fileSize = FileServiceSize.getFolderSize(getFilesDir());
			totalSize = cacheSize + fileSize;

			autoischeck = appconfig.getValue(AppConfig.Is_2G3G_AutoLoadImage);
			imgQualityValue = appconfig.getValue(AppConfig.Img_Quality);// ͼƬ��������

			return 0;
		} catch (Exception e) {
			return 1;
		}
	}

	/**
	 * ��ʼ��չʾ������̬
	 */
	private void showData() {
		txt_size.setText(FileServiceSize.FormetFileSize(totalSize) + "");
		setting_check_autoOptimize.setChecked(autoischeck.equals("true") ? true
				: false);
		setting_desc_imgQuality
				.setText(imgQualityValue.equals("low") ? "�����������Сͼ"
						: "����������ش�ͼ");
	}

	/**
	 * radioButtonʵ�ֵ�ѡ��ʽ
	 * 
	 * @param quarity
	 * @param radiobtn_big
	 * @param radionbtn_small
	 */
	private void radiocheck(String quarity, RadioButton radiobtn_big,
			RadioButton radiobtn_small) {
		if (quarity.equals("low")) {
			radiobtn_small.setChecked(true);
			radiobtn_big.setChecked(false);
		} else {
			radiobtn_big.setChecked(true);
			radiobtn_small.setChecked(false);
		}
	}

	/**
	 * �Զ��Ż���ش���
	 * 
	 * @param ischeck
	 */
	private void autoSetting(Boolean ischeck) {
		if (ischeck) {
			/*
			 * int connecttype = Util.getConnectedType(Settings.this); if
			 * (connecttype == 1)// ����wifi�� {
			 * appconfig.setValue("Is_2G3G_AutoLoadImage", "true"); } else {
			 * appconfig.setValue("Is_2G3G_AutoLoadImage", "false"); }
			 */
			appconfig.setValue("Is_2G3G_AutoLoadImage", "true");
		} else {
			appconfig.setValue("Is_2G3G_AutoLoadImage", "false");
		}
		app.initAppConfig();// �ٴγ�ʼAppConfig�е�ֵ
	}

	/**
	 * ����ͼƬ��������ֵ
	 * 
	 * @param value
	 */
	private void saveImgQualitySettings(String value) {
		appconfig.setValue("Img_Quality", value);
		app.initAppConfig();// �ٴγ�ʼAppConfig�е�ֵ
		setting_desc_imgQuality.setText(value.equals("low") ? "�����������Сͼ"
				: "����������ش�ͼ");
		if (imgQualityDialog != null) {
			imgQualityDialog.cancel();
		}
	}

	/**
	 * ���µ�����״̬
	 * 
	 * @param whoClick
	 * @param radiobtn_big
	 * @param radionbtn_small
	 */
	private void updateImgQualtyDialog(String whoClick,
			RadioButton radiobtn_big, RadioButton radiobtn_small) {
		if (imgQualityValue == null || !(imgQualityValue.equals(whoClick))) {
			imgQualityValue = whoClick;
			if (imgQualityValue.equals("low")) {
				radiobtn_small.setChecked(true);
				radiobtn_big.setChecked(false);
			} else {
				radiobtn_big.setChecked(true);
				radiobtn_small.setChecked(false);
			}
			saveImgQualitySettings(imgQualityValue);
		} else {
			if (imgQualityDialog != null) {
				imgQualityDialog.cancel();
			}
		}
	}

	// private void setting()

	private final class AsyloadState extends AsyncTask<String, Void, Integer> {
		@Override
		protected Integer doInBackground(String... params) {
			return InitData();
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if (result == 0) {
				showData();
			} else {
				Toast.makeText(Settings.this, "��ȡ�����ļ���Ϣ����", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
}
