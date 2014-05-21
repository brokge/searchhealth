package coolbuy360.service;

import java.io.File;
import java.text.DecimalFormat;
import java.io.FileInputStream;

public class FileServiceSize {

	private static FileServiceSize instance;

	public FileServiceSize() {
	}
/**
 * 采用单例模式来玩的
 * @return
 */
	public static FileServiceSize getInstance() {
		if (instance == null) {
			instance = new FileServiceSize();
		}
		return instance;
	}

	
	/**获取文件大小
	 * @param f
	 *         文件实例
	 *  @return
	 *        返回文件大小
	 */
	public long getFileSizes(File f) throws Exception {
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s = fis.available();
		} else {
			f.createNewFile();
			System.out.println("文件不存在");
		}
		return s;
	}
	/** 
	    * 获取文件夹大小 
	    * @param file File实例 
	    * @return long 单位为M 
	    * @throws Exception 
	    */  
	   public static long getFolderSize(File file)throws Exception{  
	       long size = 0;  
	       File[] fileList = file.listFiles(); 
	      
	       for (int i = 0; i < fileList.length; i++)  
	       {  
	           if (fileList[i].isDirectory())  
	           {  
	               size = size + getFolderSize(fileList[i]);  
	           } else  
	           {  
	               size = size + fileList[i].length();  
	           }  
	       } 
	       
	       //return size/1048576; 
	       return size;
	   }  
	


	/*** 转换文件大小单位(b/kb/mb/gb) ***/
	/**超过一定数量级转换成最近的单位
	 * @param fileS
	 *            文件的字节数
	 * @return
	 *           返回带单位的字符串
	 */
	public static String FormetFileSize(long fileS) {// 转换文件大小
		if(fileS==0){
			return "0KB";
		}
		
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "GB";
		}
		return fileSizeString;
	}

	/*** 获取文件个数 
	 *@param  
	 *     File文件实例
	 * @return 
	 *     返回文件个数
	 */
	public long getlist(File f) {// 递归求取目录文件个数
		long size = 0;
		File[] flist = f.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getlist(flist[i]);
				size--;
			}
		}
		return size;
	}
}
