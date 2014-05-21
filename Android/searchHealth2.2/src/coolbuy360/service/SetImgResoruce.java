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
			id="处方药";
			break;
		case 2:
			id="甲类非处方药";	
			break;
		case 3:
			id="乙类非处方药";	
			break;	
		case 5:
			id="单轨制处方药";	
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
			id="医保药品";
			return id;
		}
		
		else {
		return id;	
		}
	
	}
	
	/**
	 * 显示图片判断
	 * @param C 
	 *         需要判断的值
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
		
		 return "化学药品(西药)";
		}
		if(C.equals("z"))
		{
			 return "中成药";
		}
		 if(C.equals("b"))
		{
			return "保健品";
		}
		 if(C.equals("s"))
		{
			return "生物制药";
		}
		if(C.equals("j"))
		{
			return "进口药品";
		}	
		else {
			return "";
		}
	
		
	}
	
	
	
}
