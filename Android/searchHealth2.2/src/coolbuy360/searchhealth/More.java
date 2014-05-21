package coolbuy360.searchhealth;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import coolbuy360.adapter.ProgramAdapter;
import coolbuy360.adapter.ProgramAdapter.ProgramFiles;
import coolbuy360.logic.NoticeStateConfig;
import coolbuy360.service.searchApp;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class More extends Activity {
	
	public static More iMore = null;
	GridView gridview = null;
	
	// ΢��ҽ�� 
	// R.drawable.program_ico_weibodoc_img,
	// R.string.program_weibodoc, 
	// "weibodoc", 

	private final Integer[] programImages = {
			R.drawable.program_ico_news_img,
			R.drawable.program_ico_dissertation_img,
			R.drawable.program_ico_exposure_img,
			R.drawable.program_ico_feedback_img,
			R.drawable.program_ico_setting_img,
			R.drawable.program_ico_about_img };
	private final Integer[] programNames = {R.string.program_news, R.string.program_dissertation, R.string.program_exposure,
			R.string.program_feedback,
			R.string.program_setting, R.string.program_about };
	private final String[] programTags = { "news","dissertation", "exposure", "feedback",
			"setting", "about" };
	private final Boolean[] programIsUseAble = { true,true, true, true, true, true };

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Ϊ�˳���׼��
		searchApp.getInstance().addActivity(this);
		setContentView(R.layout.more);
		iMore = this;
		
		/*// �ж�8��������Ƿ��Ѿ�������
		Date nowDate = new Date();
		Date meetendDate = coolbuy360.service.Util.getDateFromStr(
				"2013-12-12 23:59:59", "yyyy-MM-dd HH:mm:ss");
		if (nowDate.compareTo(meetendDate) < 0) {
			programImages[0] = R.drawable.program_ico_dissertation_new_img;
		}*/

		// ��ʼ�����๦���б�
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

		setDisIco(programList);


		//���ר��no������
		/*if (dissertation_IsVisited.equals("0")) {
			programList.get(1).put(ProgramFiles.NewFunction.toString(), true);
		} else {
			programList.get(1).put(ProgramFiles.NewFunction.toString(), null);
			String dissertation_HasNew = NoticeStateConfig.getValue(this,
					NoticeStateConfig.Dissertation_HasNew);
			// ���has a news message
			if (dissertation_HasNew.equals("1")) {
				programList.get(1).put(ProgramFiles.NewNotice.toString(), true);
			} else {
				programList.get(1).put(ProgramFiles.NewNotice.toString(), null);
			}
		}*/
		// ʵ����һ��������
		ProgramAdapter adapter = new ProgramAdapter(this, programList);
		// ���GridViewʵ��
		gridview = (GridView) findViewById(R.id.program_gridview);
		// ��GridView����������������
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int posion,
					long arg3) {				
				// TODO Auto-generated method stub
				if (programTags[posion].equals("news")) {
					// �ع���
					Intent baoguangIntent = new Intent().setClass(More.this,
							MoreNews.class);
					startActivity(baoguangIntent);
				}			
				if (programTags[posion].equals("exposure")) {
					// �ع���
					Intent baoguangIntent = new Intent().setClass(More.this,
							Exposure.class);
					startActivity(baoguangIntent);
				} else if (programTags[posion].equals("weibodoc")) {
					Intent intent = new Intent().setClass(More.this,
							MoreWeiboDoctor.class);
					startActivity(intent);
				} else if (programTags[posion].equals("feedback")) {
					Intent pageIntent = new Intent().setClass(More.this,
							Feedback.class);
					startActivity(pageIntent);
				} else if (programTags[posion].equals("setting")) {
					Intent pageIntent = new Intent().setClass(More.this,
							Settings.class);
					startActivity(pageIntent);
				} else if (programTags[posion].equals("about")) {
					startActivity(new Intent(More.this, About.class));
				} else if (programTags[posion].equals("dissertation")) {
					startActivity(new Intent(More.this, Dissertation.class));
				}
			}
		});	
		
		/*Button testBtn=(Button) findViewById(R.id.testbtn);
		testBtn.setLongClickable(true);
		testBtn.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				
				Intent testIntent=new Intent ().setClass(More.this,TestPage.class );
				startActivity(testIntent);
				return false;
			}
		});*/
	}
	
	private void setDisIco(List<Map<String, Object>> programList)
	{		
		//����new�Ϻ�����newico
		String news_IsVisited=NoticeStateConfig.getValue(this, NoticeStateConfig.News_IsVisited);			
		setDisNewIco(news_IsVisited,programList,0);
		
		String dissertation_IsVisited = NoticeStateConfig.getValue(this,
				NoticeStateConfig.Dissertation_IsVisited);
		setDisNewIco(dissertation_IsVisited,programList,1);
	}
	
	/**
	 * ������ʾnew��icon
	 * @param isvisited
	 *        
	 * @param programList
	 * @param postion
	 *        index
	 */
	private void setDisNewIco(String isvisited,List<Map<String, Object>> programList,int postion)
	{		
				//���ר��no������
				if (isvisited.equals("0")) {
					programList.get(postion).put(ProgramFiles.NewFunction.toString(), true);
				} else {
					programList.get(postion).put(ProgramFiles.NewFunction.toString(), null);
					String HasNew="0";					
					switch (postion) {
					case 0:
						HasNew=NoticeStateConfig.getNewsAllState(this);
						break;
					case 1:
						HasNew = NoticeStateConfig.getValue(this,
								NoticeStateConfig.Dissertation_HasNew);				
						break;				
					default:
						break;
					}					
					setDisCricleIco(HasNew, programList, postion);
				}		
	}	
	/**
	 * �����Ƿ���ʾԲ���
	 * @param isHasNew
	 * @param programList
	 * @param postion
	 */
	private void setDisCricleIco(String isHasNew,List<Map<String, Object>> programList,int postion)
	{		
		// ���has a news message
		if (isHasNew.equals("1")) {
			programList.get(postion).put(ProgramFiles.NewNotice.toString(), true);
		} else {
			programList.get(postion).put(ProgramFiles.NewNotice.toString(), null);
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
	
	// ����ģʽ�л�ȡΨһ��MyApplicationʵ��
	public static More getInstance() {
		if (iMore == null) {
			return null;
		}
		return iMore;
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

	@Override
	protected void onResume() {
		// TODO �Զ����ɵķ������
		//Toast.makeText(this, "Resume", 0).show();
		String isNewsHasNew=NoticeStateConfig.getNewsAllState(this);
		if(isNewsHasNew.equals("0"))
		{
			setNewNotice(0, false);			
		}
		String isMoreHasNew=NoticeStateConfig.getMoreAllState(this);
		if(isMoreHasNew.equals("0"))
		{
			ConMain.mConMain.setNewNotice("����", false);			
		}
		
		super.onResume() ;
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
