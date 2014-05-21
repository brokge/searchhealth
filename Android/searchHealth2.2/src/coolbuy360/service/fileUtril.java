package coolbuy360.service;
import java.io.File;
import java.io.IOException;
import android.R.string;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import android.util.Log;

public class fileUtril {
	private static final String TAG = "FileUtil";
/**
 * 
 * @param imageUri
 *       图片的服务器uri
 * @return
 */
	public static File getCacheFile(String imageUri){
		File cacheFile = null;
		try {
			//判断是否有内存卡
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File sdCardDir = Environment.getExternalStorageDirectory();
				String fileName = getFileName(imageUri);
				File dir = new File(sdCardDir.getCanonicalPath()
						+ AaynImageLoaderUtil.CACHE_DIR);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				cacheFile = new File(dir, fileName);
				Log.i(TAG, "exists:" + cacheFile.exists() + ",dir:" + dir + ",file:" + fileName);
			} 			
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "getCacheFileError:" + e.getMessage());
		}
		
		return cacheFile;
	}
	
	
	/**
	 * 获得图片文件名的方法
	 * @param path
	 *        传入文件url
	 * @return
	 */
	public static String getFileName(String path) {
		if (path.equals(""))
			return "";
		else {
			int index = path.lastIndexOf("/");
			return path.substring(index + 1);	
		}
		
	}
	
	/* 
	 * Java文件操作 获取文件扩展名 
	 *  
	 */   
	    public static String getExtensionName(String filename) {    
	        if ((filename != null) && (filename.length() > 0)) {    
	            int dot = filename.lastIndexOf('.');    
	            if ((dot >-1) && (dot < (filename.length() - 1))) {    
	                return filename.substring(dot + 1);    
	            }    
	        }    
	        return filename;    
	    }  

	    /* 
	     * Java文件操作 获取不带扩展名的文件名 
	     */   
	  public static String getFileNameNoEx(String filename) {    
	            if ((filename != null) && (filename.length() > 0)) {    
	                int dot = filename.lastIndexOf('.');    
	                if ((dot >-1) && (dot < (filename.length()))) {    
	                    return filename.substring(0, dot);    
	                }    
	            }    
	            return filename;    
	    }    

	
	public static String getSDCachePath() {		
		File sdCardDir = Environment.getExternalStorageDirectory();		
		 String contextPath="";
		try {
			contextPath = sdCardDir.getCanonicalPath()+ AaynImageLoaderUtil.CACHE_DIR;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contextPath.toString();		
	}
	
	
	public static File getSDandCacheFile(Context context, String imageUri){
	
		File cacheFile = null;		
		//String fileName=getFileNameNoEx( getFileName(imageUri));
		String fileName=getFileName(imageUri);
		//String extstr=".png";
		try {
			//判断是否有内存卡
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File sdCardDir = Environment.getExternalStorageDirectory();
				
				File dir = new File(sdCardDir.getCanonicalPath()
						+ AaynImageLoaderUtil.CACHE_DIR);
				if(dir.exists())
				{				
				 cacheFile = new File(dir, fileName);
				 Log.i(TAG, "exists SD:" + cacheFile.exists() + ",dir:" + dir + ",file:" + fileName);
				}
				
			} 	
			else {
				String filepath =context.getFilesDir() + File.separator
				      + fileName;				
		        cacheFile = new File(filepath);
		        if(cacheFile.exists())
		        {
		        	 Log.i(TAG, "exists CACHE:" + cacheFile.exists() + ",dir:" + context.getFilesDir() + ",file:" + fileName);
		        	
		        }
		      
			}
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "getCacheFileError:" + e.getMessage());
		}
		
		return cacheFile;	
		
	}
	public static File saveSDandCacheFile(Context context, String imageUri)
	{
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
		
		}
		else {
			
		}
		
		
		return null;
	}
	
	
	
	
}
