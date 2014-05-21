package coolbuy360.searchhealth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.mapapi.utils.e;

import coolbuy360.adapter.ProgramAdapter;
import coolbuy360.adapter.ProgramAdapter.ProgramFiles;
import coolbuy360.control.ScoreChangePopup;
import coolbuy360.logic.NoticeStateConfig;
import coolbuy360.logic.Score;
import coolbuy360.logic.User;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class Member extends Activity {

	public static Member iMember = null;
	
	TextView txtname = null;
	TextView member_txt_score = null;
	Button btn_login = null;
	TextView txtpermission = null;
	GridView gridview;
	
	private static int loginReqCode = 1;
	private static int changPwdCode = 2;
	private static int memberinfoCode = 3;
	private static int checkPwdCode = 4;
	
	/*private final Integer[] programImages = {
			R.drawable.program_ico_drugfav_img,
			R.drawable.program_ico_drugstorefav_img,
			R.drawable.program_ico_purchasehistory_img,
			R.drawable.program_ico_historyofmedicine_img,
			R.drawable.program_ico_purchasehistory_img,
			R.drawable.program_ico_historyofmedicine_img,
			R.drawable.program_ico_changepwd_img,
			R.drawable.program_ico_vipcards_img };
	private final Integer[] programNames = { R.string.program_drugfav,
			R.string.program_drugstorefav, R.string.program_memberinfo,
			R.string.program_healthdossier, R.string.program_purchasehistory,
			R.string.program_historyofmedicine, R.string.program_changepwd,
			R.string.program_vipcards };
	private final String[] programTags = { "drugfav", "drugstorefav", "memberinfo", 
			"healthdossier", "purchasehistory", "historyofmedicine", "changepwd", "vipcards" };
	private final Boolean[] programIsUseAble = { true, true, true, true, false, false,
			true, false };*/
	
	private final Integer[] programImages = {
			R.drawable.program_ico_purchasehistory_img,
			R.drawable.program_ico_historyofmedicine_img,
			R.drawable.program_ico_drugfav_img,
			R.drawable.program_ico_drugstorefav_img,
			R.drawable.program_ico_changepwd_img,
			R.drawable.program_ico_noticecenter_img,
			R.drawable.program_ico_healthtrack_img,
			R.drawable.program_ico_score_img };
	private final Integer[] programNames = { R.string.program_memberinfo,
			R.string.program_healthdossier, R.string.program_drugfav,
			R.string.program_drugstorefav, R.string.program_changepwd,
			R.string.program_noticecenter, R.string.program_healthtrack, 
			R.string.program_healthscore};
	private final String[] programTags = { "memberinfo", 
			"healthreport", "drugfav", "drugstorefav", "changepwd", 
			"noticecenter", "healthtrack","healthscore" };
	private final Boolean[] programIsUseAble = { true, true, true, true, true, true, true ,true};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			//����ӵ�¼��ת����
			if (requestCode == loginReqCode) {
				checkIsAutoLogin(User.IsLogged);
				if (User.IsLogged) {
					Bundle backBundle = data.getExtras();
					if (backBundle != null) {
						runProgram(backBundle.getString("backprogram"), true);
					}
				}
			} 
			// ��������֤��ת����
			else if (requestCode == checkPwdCode) {
				if (User.IsLogged) {
					Bundle backBundle = data.getExtras();
					if (backBundle != null) {
						runProgram(backBundle.getString("backprogram"), true);
					}
				}
			}
			// �޸Ļ�Ա��Ϣ���ص�ֵ
			else if (requestCode == memberinfoCode) {
				Bundle bundle = data.getExtras();
				String memberName = bundle.getString("membername");
				txtname.setText(memberName);
				int addscore = bundle.getInt("addscore");
				if (addscore != 0) {
					User.updateScore(this, addscore + "");
					showScoreChange(R.id.member_name, addscore);
				}
			}
			// ����Ǵ��޸����봰����ת������
			else if (requestCode == changPwdCode) {
				logout();
			}		
		}		
	}

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member);
		
		iMember = this;

		txtpermission = (TextView) this.findViewById(R.id.member_permission);
		txtname = (TextView) this.findViewById(R.id.member_name);
		member_txt_score = (TextView) this.findViewById(R.id.member_txt_score);
		member_txt_score.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				memberHealthOnClick();
			}
		});
		btn_login = (Button) this.findViewById(R.id.btn_login);
		checkIsAutoLogin(User.IsLogged);
		
		// ��ʼ����Ա�����б�
		List<Map<String, Object>> programList = new ArrayList<Map<String, Object>>();
		int i = 0;
		for (Integer programimage : programImages) {
			Map<String, Object> programitem = new HashMap<String, Object>();
			programitem.put(ProgramFiles.Image.toString(), programimage);
			programitem.put(ProgramFiles.Name.toString(), programNames[i]);
			programitem.put(ProgramFiles.Tag.toString(), programTags[i]);
			programitem.put(ProgramFiles.IsUseAble.toString(), programIsUseAble[i]);
			programList.add(programitem);
			i++;
		}

		/*// ���ý��������¹���ͼ��
		String healthReport_IsVisited = NoticeStateConfig.getValue(this,
				NoticeStateConfig.HealthReport_IsVisited);
		if (healthReport_IsVisited.equals("0")) {
			programList.get(1).put(ProgramFiles.NewFunction.toString(), true);
		}*/

		// ʵ����һ��������
		ProgramAdapter adapter = new ProgramAdapter(this, programList);
		// ���GridViewʵ��
		gridview = (GridView) findViewById(R.id.program_gridview);
		// ��GridView����������������
		// gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int posion,
					long arg3) {
				// TODO Auto-generated method stub
				runProgram(programTags[posion], false);
			}
		});
	}
	
	/**
	 * ������Ӧ����
	 * @param programname
	 */
	private void runProgram(String programname, Boolean ischecked) {
		if (programname != null) {
			if (programname.equals("drugfav")) {
				drugFavOnClick();
			} else if (programname.equals("drugstorefav")) {
				storeFavOnClick();
			} else if (programname.equals("changepwd")) {
				changePwdOnClick();
			} else if (programname.equals("memberinfo")) {
				memberInfoOnClick();
			} else if (programname.equals("noticecenter")) {
				noticeCenterOnClick();
			} else if (programname.equals("healthtrack")) {
				healthTrackOnClick(ischecked);
			} else if (programname.equals("healthreport")) {
				healthReportOnClick(ischecked);
			} else if (programname.equals("healthscore")) {
				memberHealthOnClick();
			} else if (programname.equals("")) {

			} else {
				tempOnClick();
			}
		}
	}

	/**
	 * ҩ���ղص���¼�����
	 */
	public void storeFavOnClick() {
		Intent drugIntent = new Intent().setClass(Member.this,
				MemberDrugStoreCollect.class);
		startActivity(drugIntent);
	}

	/**
	 * ҩƷ�ղص���¼�����
	 */
	private void drugFavOnClick() {
		Intent drugIntent = new Intent().setClass(Member.this,
				MemberDrugCollect.class);
		startActivity(drugIntent);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		Toast.makeText(Member.this, "�¼���,����¼�¼�", 1).show();
		checkIsAutoLogin(User.IsLogged);
		//Log.i("chenlinwei", "-----onNewIntent");		
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		checkIsAutoLogin(User.IsLogged);
		//Log.i("chenlinwei", "-----onrestart");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (User.IsLogged) {
			refreshScore();
		}
	}

	/**
	 * �������ϵ���¼�����
	 */
	public void memberInfoOnClick() {
		if (User.IsLogged) {
			Intent memberInfoIntent = new Intent().setClass(Member.this,
					MemberInfo.class);
			Bundle bundle = new Bundle();
			bundle.putString("memberid", User.getMemberID());
			memberInfoIntent.putExtras(bundle);
			startActivityForResult(memberInfoIntent,memberinfoCode);
		} else {
			Intent loginIntent = new Intent().setClass(Member.this,
					MemberLogin.class);
			loginIntent.putExtra("backprogram", "memberinfo");
			startActivityForResult(loginIntent, loginReqCode);
		}
	}

	/**
	 * ������������������¼�����
	 */
	public void healthReportOnClick(Boolean ischecked) {
		if (User.IsLogged) {
			if (ischecked) {
				Intent memberHealthReportIntent = new Intent().setClass(Member.this,
						MemberHealthReport.class);
				this.startActivity(memberHealthReportIntent);				
			} else {
				Intent checkIntent = new Intent().setClass(Member.this,
						MemberCheckPwd.class);
				checkIntent.putExtra("backprogram", "healthreport");
				startActivityForResult(checkIntent, checkPwdCode);				
			}
		} else {
			Intent loginIntent = new Intent().setClass(Member.this,
					MemberLogin.class);
			loginIntent.putExtra("backprogram", "healthreport");
			startActivityForResult(loginIntent, loginReqCode);
		}
	}

	/**
	 * �����켣����¼�����
	 */
	public void healthTrackOnClick(Boolean ischecked) {
		if (User.IsLogged) {
			if (ischecked) {
				Intent healthDossierIntent = new Intent().setClass(Member.this,
						HealthDossier.class);
				this.startActivity(healthDossierIntent);
			} else {
				Intent checkIntent = new Intent().setClass(Member.this,
						MemberCheckPwd.class);
				checkIntent.putExtra("backprogram", "healthtrack");
				startActivityForResult(checkIntent, checkPwdCode);				
			} 
		} else {
			Intent loginIntent = new Intent().setClass(Member.this,
					MemberLogin.class);
			loginIntent.putExtra("backprogram", "healthtrack");
			startActivityForResult(loginIntent, loginReqCode);
		}
	}

	/**
	 * �޸�����
	 */
	public void changePwdOnClick() {
		if (User.IsLogged) {
			Intent changPwdIntent = new Intent().setClass(Member.this,
					MemberPwdChange.class);
			startActivityForResult(changPwdIntent,changPwdCode);
		} else {
			Intent loginIntent = new Intent().setClass(Member.this,
					MemberLogin.class);
			loginIntent.putExtra("backprogram", "changepwd");
			startActivityForResult(loginIntent, loginReqCode);
		}
	}

	/**
	 * ��Ϣ����
	 */
	public void noticeCenterOnClick() {
		if (User.IsLogged) {
			Intent noticeCenterIntent = new Intent().setClass(Member.this,
					NoticeCenter.class);
			this.startActivity(noticeCenterIntent);
		} else {
			Intent loginIntent = new Intent().setClass(Member.this,
					MemberLogin.class);
			loginIntent.putExtra("backprogram", "noticecenter");
			startActivityForResult(loginIntent, loginReqCode);
		}
	}

	/**
	 * ��ʱ�¼�
	 */
	private void tempOnClick() {
		Toast.makeText(Member.this, "�������ڿ�����", Toast.LENGTH_SHORT).show();
	}

	/**
	 * ��¼�¼�
	 * 
	 * @author chenlw
	 * 
	 */
	public class loginOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			Intent loginIntent = new Intent().setClass(Member.this,
					MemberLogin.class);
			startActivityForResult(loginIntent,loginReqCode);
		}
	}
	/**
	 * ����ֵ����¼�����
	 */
	public void memberHealthOnClick() {
		if (User.IsLogged) {
			Intent memberInfoIntent = new Intent().setClass(Member.this,
					MemberHealthScore.class);
			Bundle bundle = new Bundle();
			bundle.putString("memberid", User.getMemberID());
			memberInfoIntent.putExtras(bundle);
			startActivityForResult(memberInfoIntent,memberinfoCode);
		} else {
			Intent loginIntent = new Intent().setClass(Member.this,
					MemberLogin.class);
			loginIntent.putExtra("backprogram", "healthscore");
			startActivityForResult(loginIntent, loginReqCode);
		}
	}
	/**
	 * ע���¼�
	 * 
	 * @author chenlw
	 * 
	 */
	public class logoutOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			/*
			 * Intent loginIntent=new Intent().setClass(Member.this,
			 * MemberLogin.class); startActivityForResult(loginIntent,0);
			 */
			logout();
		}
	}

	/**
	 * ע������ز���
	 * 
	 */
	private void logout() {
		User user = new User(getBaseContext());
		Boolean logoutStat = user.logout();
		if (!logoutStat) {
			checkIsAutoLogin(User.IsLogged);
		}
	}

	/**
	 * ����Ƿ��Ѿ����ڵ�¼״̬
	 * 
	 * @param islogin
	 *            �����ļ��е�״ֵ̬ --true�������Ѿ���¼����֮û�е�¼
	 */
	public void checkIsAutoLogin(Boolean islogin) {
		if (islogin)// ����Ѿ��Զ���¼
		{
			btn_login.setText("ע��");
			txtname.setText(User.getMemberName(getBaseContext()));
			refreshScore();
			btn_login.setOnClickListener(new logoutOnClick());
			String drugStoreName = User.getValue(getBaseContext(),
					"DrugStoreName");
			String drugStoreID = User.getValue(getBaseContext(), "DrugStoreID");
			String specialIDs = "6204,6205,6206,6207,";
			if (drugStoreName != null && !drugStoreName.equals("")) {
				if (drugStoreID != null && !drugStoreID.equals("")
						&& specialIDs.contains(drugStoreID)) {
					txtpermission.setText("����" + drugStoreName + "�Ļ�Ա��");
				} else {
					txtpermission.setText("����" + drugStoreName
							+ "�Ļ�Ա���������ܸ�ҩ�����ػ�Ա�Ż�");
				}
			} else {
				txtpermission.setText("��ӭʹ�ý���100��Ա����");
			}
		} else {
			txtname.setText("δ��¼");
			member_txt_score.setText("0");
			btn_login.setText("��¼");
			btn_login.setOnClickListener(new loginOnClick());
			txtpermission.setText(R.string.member_txt_nopermission);
		}
	}

	/**
	 * ˢ�½���ֵ
	 */
	private void refreshScore() {
		String score = User.getScore(getBaseContext());
		if (score == null || score.equals("")) {
			member_txt_score.setText("0");
		} else {
			member_txt_score.setText(score);
		}
	}
	
	public void setNewNotice(int position, Boolean value) {
		ProgramAdapter pad = (ProgramAdapter)(gridview.getAdapter());
		Map<String, Object> mprogram = (Map<String, Object>)(pad.getItem(position));		
		if(value){
			mprogram.put(ProgramFiles.NewNotice.toString(), true);
		} else {
			mprogram.put(ProgramFiles.NewNotice.toString(), null);
		}
		pad.notifyDataSetChanged();
	}
	
	public void setNewFunction(int position, Boolean value) {
		ProgramAdapter pad = (ProgramAdapter)(gridview.getAdapter());
		Map<String, Object> mprogram = (Map<String, Object>)(pad.getItem(position));
		if(value){
			mprogram.put(ProgramFiles.NewFunction.toString(), true);
		} else {
			mprogram.put(ProgramFiles.NewFunction.toString(), null);
		}
		pad.notifyDataSetChanged();
	}
	
	/**
	 * ��ʾ���ֱ仯����
	 * @param parentViewID
	 * @param value
	 */
	private void showScoreChange(int parentViewID, int value) {
		ScoreChangePopup popupWindow = new ScoreChangePopup(this, value);
		popupWindow.showAtLocation(findViewById(parentViewID),
				Gravity.CENTER, 0, 0);
		popupWindow.delayedDismiss();		
	}
	
	// ����ģʽ�л�ȡΨһ��MyApplicationʵ��
	public static Member getInstance() {
		if (iMember == null) {
			return null;
		}
		return iMember;
	}
	
	/**
	 * ģ��Home������ϵͳ��������Ӧ�ý���
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			Intent intent = new Intent(Intent.ACTION_MAIN);  
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// ע��  
            intent.addCategory(Intent.CATEGORY_HOME);  
            this.startActivity(intent);  
            return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/*private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				// Log.i(TAG, "�˳�");
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}*/
}
