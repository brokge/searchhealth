package coolbuy360.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import coolbuy360.logic.ConstantsSetting;
import coolbuy360.service.CommandResult;
import android.content.Context;

public class UserInfo {
	
	private static UserInfo userInfo;
	
	/**
	 * 私有构造函数，保证单例模式
	 * 
	 * @param context
	 */
	private UserInfo() {

	}

	/**
	 * 获取单例对象
	 * 
	 * @param context
	 * @return
	 */

	public static UserInfo getUserInfoInstance() {
		if (userInfo == null) {
			userInfo = new UserInfo();
		}
		return userInfo;
	}

	/**
	 * 根据memberId查询会员详细信息，返回一条记录
	 * 
	 * @param memberId会员id
	 * @return
	 */
	public List<Map<String, String>> getUserInfo(int memberId) {
		String strQL = ConstantsSetting.QLGetUserInfo;
		strQL = String.format(strQL, memberId);
		return ConstantsSetting.qLGetList(0, 0, strQL, null);
	}
	
//	update Table set a=1,b=2,c=$c$ where id=1 and id=2 or name=$name$ and name^标题 模糊查询 
//	public static String QLSetUserInfo=" UPDATE [DrugCenter].[dbo].[USR_Member] SET "
//				+"USR_Member.MemberName='%1$s' "
//				+",USR_Member.Email='%2$s'"
//				+",USR_Member.Mobile='%3$s'"
//				+",USR_Member.Sex='%4$s'"
//				+",USR_Member.Job='%5$s'"
//				+",USR_Member.HC='%6$s'"
//				+",USR_Member.Income='%7$s'"
//				+",USR_Member.BuyWay='%8$s'"
//				+",USR_Member.FamilyAddress='%9$s'"
//				+",USR_Member.IdCardNum='%10$s'"
//				+",USR_Member.Birthday='%11$s'"
//				+",USR_Member.Telephone='%12$s'"
//				+",USR_Member.EducationLevel='%13$s'"
//				+",USR_Member.CommonHospital='%14$s'" 
//				+"where UserName='%15$s'  " ;
	
	/***
	 * 执行更新用户详细信息(map参数数据)		
	 */
	public  Boolean updateUserInfo(Map<String,String> userInfoMap)
	{
		String strQL=ConstantsSetting.QLSetUserInfo;
		Map<String, String> postValue=new HashMap<String, String>();
		if(!userInfoMap.isEmpty())
		{
//			strQL=String .format(strQL,userInfoMap.get("membername"), userInfoMap.get("email"),userInfoMap.get("mobile"),userInfoMap.get("sex"),
//					userInfoMap.get("job"),userInfoMap.get("hc"),userInfoMap.get("income"),userInfoMap.get("buyway"),userInfoMap.get("familyaddress"),
//					userInfoMap.get("idcardnum"),userInfoMap.get("birthday"),userInfoMap.get("telephone"),userInfoMap.get("educationlevel"),
//					userInfoMap.get("commonhospital"),userInfoMap.get("username")
//			)
			postValue=userInfoMap;			
		}		
		return ConstantsSetting.qlUpdate(strQL, postValue);
	}
	
	/**
	 * 更新用户的详细信息（单个参数）
	 * @param memberName
	 *        姓名
	 * @param Sex
	 *         性别
	 * @param IdCardNum
	 *        身份证号
	 * @param Birthday
	 *        出生日期  
	 * @param Mobile
	 *        手机
	 * @param TelePhone
	 *        电话号码
	 * @param Email 
	 *        邮箱
	 * @param FamilyAddress
	 *        家庭住址
	 * @param EducationLevel
	 *         教育层次
	 * @param Job
	 *        工作
	 * @param HC
	 *        是否医保：取值为 ：1正常 2冻结 3无
	 * @param Income
	 *        医疗支出
	 * @param BuyWay
	 *        购买途径
	 * @param CommonHospital
	 *        首选医院
	 *@param userName
	 *       用户名（唯一标志符）
	 * @return
	 */
	public Boolean updateUserInfo2(Context context, String memberName,String Sex,String IdCardNum,String Birthday,String Mobile,String TelePhone, String Email, String FamilyAddress,
			String EducationLevel, String Job,String HC,String Income,String BuyWay,String CommonHospital,String memberid)
	{	
		//String strQL=ConstantsSetting.QLSetUserInfo;
		Map<String, String> postValue=new HashMap<String, String>();	
		String QLSetUserInfo=" UPDATE USR_Member SET "
			+"MemberName=$membername$ "
			+",Email=$email$"
			+",Mobile=$mobile$"
			+",Sex=$sex$"
			+",Job=$job$"
			+",HC=$hc$"
			+",Income=$income$"
			+",BuyWay=$buyway$"
			+",FamilyAddress=$familyaddress$"
			+",IdCardNum=$idcardnum$";	
		if(!Birthday.equals(""))
		{
			QLSetUserInfo=QLSetUserInfo+",Birthday=$birthday$";
			postValue.put("birthday", Birthday);
		}
		QLSetUserInfo=QLSetUserInfo	+",Telephone=$telephone$"
			+",EducationLevel=$educationlevel$"
			+",CommonHospital=$commonhospital$" 
			+"  where MemberID=$memberid$  " ;			
		postValue.put("membername", memberName);
		postValue.put("sex", Sex);
		postValue.put("idcardnum", IdCardNum);	
		postValue.put("mobile", Mobile);
		postValue.put("telephone", TelePhone);
		postValue.put("email", Email);
		postValue.put("familyaddress", FamilyAddress);
		postValue.put("educationlevel", EducationLevel);
		postValue.put("job", Job);
		postValue.put("hc", HC);
		postValue.put("income", Income);
		postValue.put("buyway", BuyWay);
		postValue.put("commonhospital", CommonHospital);
		postValue.put("memberid", memberid);
		Boolean resultBoolean = ConstantsSetting.qlUpdate(QLSetUserInfo, postValue);
		// 若更新成功，则更新本地缓存数据。
		if (resultBoolean) {
			try {
				User iuser = new User(context);
				iuser.setValue("MemberName", memberName);
				iuser.setValue("Email", Email);
				iuser.setValue("Mobile", Mobile);
				iuser.setValue("Sex", Sex);
				iuser.setValue("Job", Job);
				iuser.setValue("HC", HC);
				iuser.setValue("Income", Income);
				iuser.setValue("BuyWay", BuyWay);
				iuser.setValue("FamilyAddress", FamilyAddress);
				iuser.setValue("IdCardNum", IdCardNum);
				iuser.setValue("Birthday", Birthday);
				iuser.setValue("Telephone", TelePhone);
				iuser.setValue("EducationLevel", EducationLevel);
				iuser.setValue("CommonHospital", CommonHospital);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return resultBoolean;
			}
		}
		return resultBoolean;
	}
	
	/**
	 * 更新用户的详细信息（单个参数）
	 * @param memberName
	 *        姓名
	 * @param Sex
	 *         性别
	 * @param IdCardNum
	 *        身份证号
	 * @param Birthday
	 *        出生日期  
	 * @param Mobile
	 *        手机
	 * @param TelePhone
	 *        电话号码
	 * @param Email 
	 *        邮箱
	 * @param FamilyAddress
	 *        家庭住址
	 * @param EducationLevel
	 *         教育层次
	 * @param Job
	 *        工作
	 * @param HC
	 *        是否医保：取值为 ：1正常 2冻结 3无
	 * @param Income
	 *        医疗支出
	 * @param BuyWay
	 *        购买途径
	 * @param CommonHospital
	 *        首选医院
	 *@param userName
	 *       用户名（唯一标志符）
	 * @return
	 */
	public CommandResult updateUserInfo(Context context, String memberName,String Sex,String IdCardNum,String Birthday,String Mobile,String TelePhone, String Email, String FamilyAddress,
			String EducationLevel, String Job,String HC,String Income,String BuyWay,String CommonHospital,String memberid) {
		String strQL="plugin DrugCenter.Logic.V1 Member Update("
			+"$membername$"
			+",$email$"
			+",$mobile$"
			+",$sex$"
			+",$job$"
			+",$hc$"
			+",$income$"
			+",$buyway$"
			+",$familyaddress$"
			+",$idcardnum$"	
			+",$birthday$"
			+",$telephone$"
			+",$educationlevel$"
			+",$commonhospital$" 
			+",$memberid$)";		

		Map<String, String> postValue = new HashMap<String, String>();
		postValue.put("birthday", Birthday);
		postValue.put("membername", memberName);
		postValue.put("sex", Sex);
		postValue.put("idcardnum", IdCardNum);	
		postValue.put("mobile", Mobile);
		postValue.put("telephone", TelePhone);
		postValue.put("email", Email);
		postValue.put("familyaddress", FamilyAddress);
		postValue.put("educationlevel", EducationLevel);
		postValue.put("job", Job);
		postValue.put("hc", HC);
		postValue.put("income", Income);
		postValue.put("buyway", BuyWay);
		postValue.put("commonhospital", CommonHospital);
		postValue.put("memberid", memberid);
		
		List<Map<String, String>> result = ConstantsSetting.qLGetList(0, 0,
				strQL, postValue);
		if (result != null && result.size() > 0) {
			Map<String, String> values = result.get(0);
			CommandResult commandResult = new CommandResult(
					values.get("result"), values.get("message"));
			commandResult.setOriginalResult(values);
			return commandResult;
		}
		CommandResult commandResult = new CommandResult("false", "未知错误");
		return commandResult;
	}
}
