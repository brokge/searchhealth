/**
 * 
 */
package coolbuy360.searchhealth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import coolbuy360.searchhealth.MemberLogin.loginbtnOnClick;
import coolbuy360.service.CommonMethod;
import coolbuy360.service.searchApp;

/**
 * @author yangxc
 * ��������
 */
public class HealthDossier extends Activity {
		
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.health_dossier);
		// Ϊ�˳���׼��
		searchApp.getInstance().addActivity(this);

		// ���÷��ذ�ť
		ImageButton actionbar_pre_btn = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		actionbar_pre_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HealthDossier.this.finish();
			}
		});
		Log.i("chenlinwei", "");	
		
		
		// �������
		LinearLayout health_dossier_item_footprint = (LinearLayout) this
				.findViewById(R.id.health_dossier_item_footprint);
		LinearLayout health_dossier_item_disease = (LinearLayout) this
				.findViewById(R.id.health_dossier_item_disease);
		LinearLayout health_dossier_item_contagious = (LinearLayout) this
				.findViewById(R.id.health_dossier_item_contagious);
		LinearLayout health_dossier_item_herediary = (LinearLayout) this
				.findViewById(R.id.health_dossier_item_herediary);
		LinearLayout health_dossier_item_allergic = (LinearLayout) this
				.findViewById(R.id.health_dossier_item_allergic);
		LinearLayout health_dossier_item_operation = (LinearLayout) this
				.findViewById(R.id.health_dossier_item_operation);
		
		// ע����Ч��
		health_dossier_item_footprint.setOnTouchListener(new CommonMethod.setOnPressed());
		health_dossier_item_disease.setOnTouchListener(new CommonMethod.setOnPressed());
		health_dossier_item_contagious.setOnTouchListener(new CommonMethod.setOnPressed());
		health_dossier_item_herediary.setOnTouchListener(new CommonMethod.setOnPressed());
		health_dossier_item_allergic.setOnTouchListener(new CommonMethod.setOnPressed());
		health_dossier_item_operation.setOnTouchListener(new CommonMethod.setOnPressed());
		
		// �������㼣�����
		health_dossier_item_footprint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				Log.i("chenlinwei", "�����㼣");
				Intent MemberHealthFootprintIntent = new Intent().setClass(HealthDossier.this,
						MemberHealthFootprint.class);				
				HealthDossier.this.startActivity(MemberHealthFootprintIntent);
			}
		});
		
		// ������ʷ�����
		health_dossier_item_disease.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				Intent memberDiseaseIntent = new Intent().setClass(HealthDossier.this,
						MemberDisease.class);
				memberDiseaseIntent.putExtra("diseasetype", "all");
				HealthDossier.this.startActivity(memberDiseaseIntent);				
			}
		});	
		
		// ����Ⱦ��ʷ�����
		health_dossier_item_contagious.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				Intent memberDiseaseIntent = new Intent().setClass(HealthDossier.this,
						MemberDisease.class);
				memberDiseaseIntent.putExtra("diseasetype", "contagious");		
				HealthDossier.this.startActivity(memberDiseaseIntent);			
			}
		});	
		
		// ���Ŵ���ʷ�����
		health_dossier_item_herediary.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				Intent memberDiseaseIntent = new Intent().setClass(HealthDossier.this,
						MemberDisease.class);
				memberDiseaseIntent.putExtra("diseasetype", "hereditary");
				HealthDossier.this.startActivity(memberDiseaseIntent);	
				
			}
		});	
		
		// ������ʷ�����
		health_dossier_item_allergic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				Intent memberAllergicIntent = new Intent().setClass(HealthDossier.this,
						MemberAllergic.class);
				HealthDossier.this.startActivity(memberAllergicIntent);					
			}
		});	
		
		// �����������
		health_dossier_item_operation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setPressed(false);
				Intent memberOperationIntent = new Intent().setClass(HealthDossier.this,
						MemberOperation.class);
				HealthDossier.this.startActivity(memberOperationIntent);					
			}
		});	
	}	
}
