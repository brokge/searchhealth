/**
 * 
 */
package coolbuy360.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.text.TextUtils;

/**
 * @author yangxc 本地文件的相关操作
 */
public class FileService {
	private Context context;

	public FileService(Context context) {
		this.context = context;
	}

	/**
	 * 写数据 把数据流写入data/data下的程序文件中
	 * 
	 * @param fileName
	 *            文件名包括后缀
	 * @param writestr
	 *            需要写入的字符串
	 */
	public void writeFile(String fileName, String writestr) throws IOException {
		try {

			FileOutputStream fout = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			byte[] bytes = writestr.getBytes();
			fout.write(bytes);
			fout.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 保存图片到
	 * @param context
	 * @param fileName
	 * @param bitmap
	 * @param quality
	 * @throws IOException
	 */
	public  void saveImage( String fileName,
			Bitmap bitmap, int quality,CompressFormat format) throws IOException {
		if (bitmap == null || fileName == null || context == null)
			return;

		FileOutputStream fos = context.openFileOutput(fileName,
				Context.MODE_PRIVATE);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(format, quality, stream);
		byte[] bytes = stream.toByteArray();
		fos.write(bytes);
		fos.close();
	}
	/**
	 * 读数据(DATA)
	 * 
	 * @param fileName
	 *            文件名 读取文本文件返回utf-8格式的string
	 * 
	 * 
	 */
	public String readFile(String fileName) throws IOException {
		String res = "";
		try {
			FileInputStream fin = context.openFileInput(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;

	}

	/**
	 * 写数据（DATA） 把数据流写入data/data下的程序文件中
	 * 
	 * @param fileName
	 *            文件名包括后缀
	 * @param writestr
	 *            需要写入的数据流
	 * @exception 抛出异常
	 */
	public void writeFile(String fileName, InputStream writestr)
			throws IOException {
		try {

			FileOutputStream fout = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			byte buf[] = new byte[128];
			do {
				int numread = writestr.read(buf);
				if (numread <= 0) {
					break;
				}
				fout.write(buf, 0, numread);
			} while (true);

			fout.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 写数据到SD中的文件
	 * 
	 * @param fileName
	 *            文件名（默认为主目录 ）
	 * @param write_str
	 *            需要操作的字符串
	 */
	public void writeFileSdcardFile(String fileName, String write_str)
			throws IOException {
		try {

			FileOutputStream fout = new FileOutputStream(fileName);
			byte[] bytes = write_str.getBytes();

			fout.write(bytes);
			fout.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读SD中的文件
	 * 
	 * @param fileName
	 *            文件名--默认路径为sdcard的主目录
	 * @exception 抛出异常
	 */
	public String readFileSdcardFile(String fileName) throws IOException {
		String res = "";
		try {
			FileInputStream fin = new FileInputStream(fileName);

			int length = fin.available();

			byte[] buffer = new byte[length];
			fin.read(buffer);

			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/***
	 * 
	 * *res/raw和assets的相同点： 1.两者目录下的文件在打包后会原封不动的保存在apk包中，不会被编译成二进制。
	 * res/raw和assets的不同点：
	 * 1.res/raw中的文件会被映射到R.java文件中，访问的时候直接使用资源ID即R.id.filename
	 * ；assets文件夹下的文件不会被映射到R.java中，访问的时候需要AssetManager类。
	 * 2.res/raw不可以有目录结构，而assets则可以有目录结构，也就是assets目录下可以再建立文件夹
	 * 
	 */
	
	
	/**
	 * 从resource的raw中读取文件数据(文本文件)：
	 * 
	 * @param context
	 *            上下文对象
	 * @param rawId
	 *            raw中在R文件中生成的ID
	 * @return 返回string
	 * 
	 */
	public String readFileRaw(Context context, int rawId) {
		String res = "";
		try {
			// 得到资源中的Raw数据流
			InputStream in = context.getResources().openRawResource(rawId);

			// 得到数据的大小
			int length = in.available();

			byte[] buffer = new byte[length];

			// 读取数据
			in.read(buffer);

			// 依test.txt的编码类型选择合适的编码，如果不调整会乱码
			res = EncodingUtils.getString(buffer, "BIG5");

			// 关闭
			in.close();
			return res;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 从asset中读取文件
	 * 
	 * @param context
	 *            上下文对象
	 * @param fileName
	 *            文件名
	 * @return 返回文件的string
	 */
	public String redFileAsset(Context context, String fileName) {
		String res = "";
		try {

			// 得到资源中的asset数据流
			InputStream in = context.getResources().getAssets().open(fileName);

			int length = in.available();
			byte[] buffer = new byte[length];

			in.read(buffer);
			in.close();
			res = EncodingUtils.getString(buffer, "UTF-8");
			return res;

		} catch (Exception e) {

			e.printStackTrace();
			return null;

		}
	}

	/**
	 * 把assets中的文件移动到data中（assets该目录下的文件大小不能超过1M）
	 * 
	 * @param context
	 *            上下文对象
	 * @param strAssetsFilePath
	 *            asset文件名
	 * @param strDesFilePath
	 *            data文件路径下新建和接收copy的文件
	 * @return
	 */
	public boolean assetsCopyData(Context context, String strAssetsFilePath,
			String strDesFilePath) {
		boolean bIsSuc = true;
		InputStream inputStream = null;
		OutputStream outputStream = null;

		File file = new File(strDesFilePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
				Runtime.getRuntime().exec("chmod 766 " + file);
			} catch (IOException e) {
				bIsSuc = false;
			}

		} else {// 存在
			return true;
		}

		try {
			inputStream = context.getAssets().open(strAssetsFilePath);
			outputStream = new FileOutputStream(file);

			int nLen = 0;

			byte[] buff = new byte[1024 * 1];
			while ((nLen = inputStream.read(buff)) > 0) {
				outputStream.write(buff, 0, nLen);
			}

			// 完成
		} catch (IOException e) {
			bIsSuc = false;
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}

				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				bIsSuc = false;
			}

		}

		return bIsSuc;
	}

	/*
	 * String Name = File.getName(); //获得文件或文件夹的名称： String parentPath =
	 * File.getParent(); //获得文件或文件夹的父目录 String path =
	 * File.getAbsoultePath();//绝对路经 String path = File.getPath();//相对路经
	 * File.createNewFile();//建立文件 File.mkDir(); //建立文件夹 File.isDirectory();
	 * //判断是文件或文件夹 File[] files = File.listFiles(); //列出文件夹下的所有文件和文件夹名
	 * File.renameTo(dest); //修改文件夹和文件名 File.delete(); //删除文件夹或文件
	 */
	/**
	 * 获取文件名
	 * 
	 * @param f
	 *            File文件的实例
	 */
	public String getname(File f) {
		String Name = f.getName();
		return Name;
	}

	/**
	 * 获得文件或文件夹的父目录
	 * 
	 * @param f
	 *            File文件的实例对象
	 * @return
	 */
	public String getparentPath(File f) {
		String parentPath = f.getParent(); // 获得文件或文件夹的父目录
		return parentPath;
	}

	/**
	 * 获得文件绝对路经
	 * 
	 * @param f
	 *            File文件的实例对象
	 * @return
	 */
	public String getabsoultePath(File f) {
		String path = f.getPath();// 相对路经
		return path;
	}

	/**
	 * 创建文件
	 * 
	 * @param path
	 *            文件路径包括文件名后后缀名
	 * @return 返回是否成功 true 表示成功 false 表示失败
	 * @throws IOException
	 */
	public Boolean createFile(String path) throws IOException {
		File file = new File(path);
		// File myTempFile = File.createTempFile(fileNa, "." + fileEx);
		return file.createNewFile();// 建立文件
	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 *            创建文件夹的路径
	 * @return 是否成功
	 */
	public Boolean createMkdir(String path) {
		File file = new File(path);
		return file.mkdir();
	}

	/**
	 * 判断是不是文件夹
	 * 
	 * @param path
	 *            需要检查的文件路径
	 * @return 是否是文件夹
	 */
	public Boolean isDirectory(String path) {
		File file = new File(path);
		return file.isDirectory();
	}

	/**
	 * 列出文件夹下的所有文件和文件夹名
	 * 
	 * @param path
	 *            需要列出的父目录
	 * @return
	 * 
	 *         File[]集合
	 */
	public File[] fileList(String path) {
		File file = new File(path);
		File[] files = file.listFiles(); // 列出文件夹下的所有文件和文件夹名

		return files;
	}

	/**
	 * 更改文件或文件夹名
	 * 
	 * @param oldpath
	 *            旧文件名的路径
	 * @param newpath
	 * @return
	 */
	public Boolean reName(String oldpath, File newfile) {
		File file = new File(oldpath);
		return file.renameTo(newfile);
	}

	/**
	 * 删除指定文件或文件夹
	 * 
	 * @param path
	 *            指定文件或文件夹的路径
	 *@param  deleteThisPath
	 *        	      是否删除这个路径
	 * @return 返回是否操作成功
	 */

	public void deleteFolderFile(String filePath, boolean deleteThisPath)
			throws IOException {
		if (!TextUtils.isEmpty(filePath)) {
			File file = new File(filePath);

			if (file.isDirectory()) {// 处理目录
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFolderFile(files[i].getAbsolutePath(), true);
				}
			}
			if (deleteThisPath) {
				if (!file.isDirectory()) {// 如果是文件，删除
					file.delete();
				} else {// 目录
					if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
						file.delete();
					}
				}
			}
		}
	}

}
