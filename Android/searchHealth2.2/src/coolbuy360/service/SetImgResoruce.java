package coolbuy360.service;

import android.util.Log;
import coolbuy360.searchhealth.R;

public   class SetImgResoruce {	
	public  static int imageResurce_otc(int C )
	{
		int id=0;
		switch (C) {
		case 1:
			id=R.drawable.ico_drug_rx;
			break;
		case 2:
			id=R.drawable.ico_drug_rotc;	
				break;
		case 3:
			id=R.drawable.ico_drug_gotc;	
			break;
		case 5:
			id=R.drawable.ico_drug_rx;
			break;
		default:
			return id;
		}
		return id;
	}
	
	public static String imageResource_otc_txt(int C ) {
		
		String id="";
		switch (C) {
		case 1:
			id="����ҩ";
			break;
		case 2:
			id="����Ǵ���ҩ";	
			break;
		case 3:
			id="����Ǵ���ҩ";	
			break;	
		case 5:
			id="�����ƴ���ҩ";	
			break;		
		}
		return id;
		
	}
	
	
	public static int imgResurce_bao (String C)
	{	
		int id=0;
		Log.i("TAG", "BAO:"+C);
		if(C.equals(1+""))
		{
			id =R.drawable.ico_drug_yibao;
			return id;
		}
		
		else {
		return id;	
		}
	
	}
	public static String imgResurce_bao_txt (String C)
	{	
		String id="";
		Log.i("TAG", "BAO:"+C);
		if(C.equals(1+""))
		{
			id="ҽ��ҩƷ";
			return id;
		}
		
		else {
		return id;	
		}
	
	}
	
	/**
	 * ��ʾͼƬ�ж�
	 * @param C 
	 *         ��Ҫ�жϵ�ֵ
	 * @return
	 */
	public static int imgResource(String C )
	{
		int id=0;
		Log.i("TAG", "H:"+C);
		if(C.equals("h"))
		{
		id=R.drawable.ico_drug_h;
		 return id;
		}
		if(C.equals("z"))
		{
			 return R.drawable.ico_drug_z;
		}
		 if(C.equals("b"))
		{
			return R.drawable.ico_drug_b;
		}
		 if(C.equals("s"))
		{
			return R.drawable.ico_drug_s;
		}
		if(C.equals("j"))
		{
			return R.drawable.ico_drug_j;
		}	
		else {
			return id;
		}
	
		
	}
	
	
	public static String imgResource_txt(String C )
	{
	
		Log.i("TAG", "H:"+C);
		if(C.equals("h"))
		{
		
		 return "��ѧҩƷ(��ҩ)";
		}
		if(C.equals("z"))
		{
			 return "�г�ҩ";
		}
		 if(C.equals("b"))
		{
			return "����Ʒ";
		}
		 if(C.equals("s"))
		{
			return "������ҩ";
		}
		if(C.equals("j"))
		{
			return "����ҩƷ";
		}	
		else {
			return "";
		}
	
		
	}
	
	
	
}
