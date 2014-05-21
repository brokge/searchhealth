package coolbuy360.service;

import java.io.File;
import java.text.DecimalFormat;
import java.io.FileInputStream;

public class FileServiceSize {

	private static FileServiceSize instance;

	public FileServiceSize() {
	}
/**
 * ���õ���ģʽ�����
 * @return
 */
	public static FileServiceSize getInstance() {
		if (instance == null) {
			instance = new FileServiceSize();
		}
		return instance;
	}

	
	/**��ȡ�ļ���С
	 * @param f
	 *         �ļ�ʵ��
	 *  @return
	 *        �����ļ���С
	 */
	public long getFileSizes(File f) throws Exception {
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s = fis.available();
		} else {
			f.createNewFile();
			System.out.println("�ļ�������");
		}
		return s;
	}
	/** 
	    * ��ȡ�ļ��д�С 
	    * @param file Fileʵ�� 
	    * @return long ��λΪM 
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
	


	/*** ת���ļ���С��λ(b/kb/mb/gb) ***/
	/**����һ��������ת��������ĵ�λ
	 * @param fileS
	 *            �ļ����ֽ���
	 * @return
	 *           ���ش���λ���ַ���
	 */
	public static String FormetFileSize(long fileS) {// ת���ļ���С
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

	/*** ��ȡ�ļ����� 
	 *@param  
	 *     File�ļ�ʵ��
	 * @return 
	 *     �����ļ�����
	 */
	public long getlist(File f) {// �ݹ���ȡĿ¼�ļ�����
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
