package coolbuy360.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.content.Context;

public class Validator {	
	Boolean isValidator=false;
	String message="";
    String check=null; 	
 	Context ctx;
 	private static Validator validator;	
	/**
	 * 私有构造函数，保证单例模式
	 * @param context
	 */
	private Validator(Context context)
	{	
 		this.ctx=context;
	}
	/**
	 * 获取单例对象
	 * @param context
	 * @return
	 */	
	public static Validator getValidatorInstance(Context context)
	{
		if(validator==null)
		{			
			validator=new Validator(context);			
		}		
		return validator;		
	}
	 //内容验证
	/**
	 * 验证是否为相应的字符串（内容合格, 数字，字母，下划线）
	 * @param contentStr
  	 *        需要验证的字符串  
  	 * @param  message
  	 *       验证不成功后的错误信息     
  	 *           
	 * @return
	 *       返回自身对象
	 */
	
	public Validator CheckContent(String contentStr,String message){
		 char tempName[]=contentStr.toCharArray();	
		 Validator vallidator=Validator.getValidatorInstance(ctx);
		//验证内容 
		 for(int i=0;i<tempName.length;i++){
		       if((tempName[i]>47 && tempName[i]<58)  ||  (tempName[i]>64 && tempName[i]<91) || (tempName[i]>96 && tempName[i]<123) || (tempName[i]==95)){		    	  
		    	   vallidator.message="ok";
		    	   vallidator.isValidator=true;    	   
			       //内容合格, 数字，字母，下划线 	    	     
		       }else{
		    	   vallidator.message=message;
		    	   vallidator.isValidator=false;		    	 
		       }
	      }
		   return vallidator;
	  }
	/**
	 * 根据正则表达式来验证是否相应的串
	 * @param contentStr
  	 *        需要验证的字符串
  	 * @param reg 
  	 *        正则表达式
  	 * @param  message
  	 *       验证不成功后的错误信息         
	 * @return
	 */
	public Validator CheckContentByReg(String contentStr, String reg ,String message){
		 //char tempName[]=names.toCharArray();	
		 Validator vallidator=Validator.getValidatorInstance(ctx);
		 if(contentStr.matches(reg))
		 {
			 vallidator.isValidator=true;			 
		 }
		 else
		 {
			 vallidator.message="message";
			 vallidator.isValidator=true;			 
		 }	
		return vallidator;
	 }	   	   
	/**
	 * 验证字符串的长度
	 * @param contentStr
  	 *        需要验证的字符串
  	 * @param min 
  	 *        最小长度
  	 * @param max 
  	 *        最大长度
  	 * @param  message
  	 *       验证不成功后的错误信息         
	 * @return
	 */
    public Boolean contentLengthCheck(String contentStr,int min ,int max){
    	   char tempName[]=contentStr.toCharArray();
    	   boolean islong=false;
    	   Validator vallidator=Validator.getValidatorInstance(ctx);
    	   if(tempName.length>=min && tempName.length<=max){	      
	    		   islong=true;	        
	           }else{	        	  
	        	   islong=false;	    		  
	           }	   
    	  return islong;		
	}	
    
    /**
     *判断字符串是否为允许范围的字符串
     *允许的字符串为：字母，数字，下划线
     */
    public boolean isContent(String content) {
		    Pattern p = Pattern
		    .compile("^[a-zA-Z][a-zA-Z0-9]*$");
		    Matcher m = p.matcher(content);		
		    return m.matches();
    }   
    //判断手机格式是否正确
    public boolean isMobileNO(String mobiles) {
		    Pattern p = Pattern
		    .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		    Matcher m = p.matcher(mobiles);		
		    return m.matches();
    }
    //判断电话号码格式是否正确
    public boolean isTelephone(String mobiles) {
		    Pattern p = Pattern
		    .compile("^(0[0-9]{2,3}\\-)([0-9]{7,8})(\\-[0-9]{1,4})?$");
		    Matcher m = p.matcher(mobiles);		
		    return m.matches();
    }
    //判断email格式是否正确
    public boolean isEmail(String email) {
		    String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		    Pattern p = Pattern.compile(str);
		    Matcher m = p.matcher(email);		
		    return m.matches();
    }
    //判断是否全是数字
    public boolean isNumeric(String str) {
		    Pattern pattern = Pattern.compile("[0-9]*");
		    Matcher isNum = pattern.matcher(str);
		    if (!isNum.matches()) {
		    return false;
		    }
		    return true;
	 }
    /**
	  * 18位或者15位身份证验证 18位的最后一位可以是字母x
	  * 
	  * @param text
	  * @return
	  */
    public boolean isIDCardNum(String str)
    {   
    	  //String regx = "[0-9]{17}x";
    	  //String reg1 = "[0-9]{15}";
    	  //String regex = "[0-9]{18}";
    	 Pattern p = Pattern.compile("[0-9]{17}x|[0-9]{15}|[0-9]{18}");
		 Matcher m = p.matcher(str);   	  
    	 if(!m.matches())
    	 {
    		 return false;    		 
    	 }
    	 else
    	 {
    		 return true;    		 
    	 }   	
    }
    
    
    
    //判断是否是ip
    public boolean isIP(String str) {
		    Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		    Matcher isNum = pattern.matcher(str);
		    if (!isNum.matches()) {
		    return false;
		    }
		    return true;
	 }    
   //验证是否是网址
    public boolean isUrl(String str)
    {
    	String regStr= "^((https|http|ftp|rtsp|mms)?://)"
            + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@  
            + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184  
            + "|" // 允许IP和DOMAIN（域名） 
            + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.  
             + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名  
            + "[a-z]{2,6})" // first level domain- .com or .museum  
            + "(:[0-9]{1,4})?" // 端口- :80  
            + "((/?)|" // a slash isn't required if there is no file name  
            + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
    	  Pattern pattern = Pattern.compile(regStr);
    	  Matcher isurl = pattern.matcher(str);
		    if (!isurl.matches()) {
		    return false;
		    }
		    return true;
    }    

}
