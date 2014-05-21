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
 * @author yangxc �����ļ�����ز���
 */
public class FileService {
	private Context context;

	public FileService(Context context) {
		this.context = context;
	}

	/**
	 * д���� ��������д��data/data�µĳ����ļ���
	 * 
	 * @param fileName
	 *            �ļ���������׺
	 * @param writestr
	 *            ��Ҫд����ַ���
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
	 * ����ͼƬ��
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
	 * ������(DATA)
	 * 
	 * @param fileName
	 *            �ļ��� ��ȡ�ı��ļ�����utf-8��ʽ��string
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
	 * д���ݣ�DATA�� ��������д��data/data�µĳ����ļ���
	 * 
	 * @param fileName
	 *            �ļ���������׺
	 * @param writestr
	 *            ��Ҫд���������
	 * @exception �׳��쳣
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
	 * д���ݵ�SD�е��ļ�
	 * 
	 * @param fileName
	 *            �ļ�����Ĭ��Ϊ��Ŀ¼ ��
	 * @param write_str
	 *            ��Ҫ�������ַ���
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
	 * ��SD�е��ļ�
	 * 
	 * @param fileName
	 *            �ļ���--Ĭ��·��Ϊsdcard����Ŀ¼
	 * @exception �׳��쳣
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
	 * *res/raw��assets����ͬ�㣺 1.����Ŀ¼�µ��ļ��ڴ�����ԭ�ⲻ���ı�����apk���У����ᱻ����ɶ����ơ�
	 * res/raw��assets�Ĳ�ͬ�㣺
	 * 1.res/raw�е��ļ��ᱻӳ�䵽R.java�ļ��У����ʵ�ʱ��ֱ��ʹ����ԴID��R.id.filename
	 * ��assets�ļ����µ��ļ����ᱻӳ�䵽R.java�У����ʵ�ʱ����ҪAssetManager�ࡣ
	 * 2.res/raw��������Ŀ¼�ṹ����assets�������Ŀ¼�ṹ��Ҳ����assetsĿ¼�¿����ٽ����ļ���
	 * 
	 */
	
	
	/**
	 * ��resource��raw�ж�ȡ�ļ�����(�ı��ļ�)��
	 * 
	 * @param context
	 *            �����Ķ���
	 * @param rawId
	 *            raw����R�ļ������ɵ�ID
	 * @return ����string
	 * 
	 */
	public String readFileRaw(Context context, int rawId) {
		String res = "";
		try {
			// �õ���Դ�е�Raw������
			InputStream in = context.getResources().openRawResource(rawId);

			// �õ����ݵĴ�С
			int length = in.available();

			byte[] buffer = new byte[length];

			// ��ȡ����
			in.read(buffer);

			// ��test.txt�ı�������ѡ����ʵı��룬���������������
			res = EncodingUtils.getString(buffer, "BIG5");

			// �ر�
			in.close();
			return res;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * ��asset�ж�ȡ�ļ�
	 * 
	 * @param context
	 *            �����Ķ���
	 * @param fileName
	 *            �ļ���
	 * @return �����ļ���string
	 */
	public String redFileAsset(Context context, String fileName) {
		String res = "";
		try {

			// �õ���Դ�е�asset������
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
	 * ��assets�е��ļ��ƶ���data�У�assets��Ŀ¼�µ��ļ���С���ܳ���1M��
	 * 
	 * @param context
	 *            �����Ķ���
	 * @param strAssetsFilePath
	 *            asset�ļ���
	 * @param strDesFilePath
	 *            data�ļ�·�����½��ͽ���copy���ļ�
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

		} else {// ����
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

			// ���
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
	 * String Name = File.getName(); //����ļ����ļ��е����ƣ� String parentPath =
	 * File.getParent(); //����ļ����ļ��еĸ�Ŀ¼ String path =
	 * File.getAbsoultePath();//����·�� String path = File.getPath();//���·��
	 * File.createNewFile();//�����ļ� File.mkDir(); //�����ļ��� File.isDirectory();
	 * //�ж����ļ����ļ��� File[] files = File.listFiles(); //�г��ļ����µ������ļ����ļ�����
	 * File.renameTo(dest); //�޸��ļ��к��ļ��� File.delete(); //ɾ���ļ��л��ļ�
	 */
	/**
	 * ��ȡ�ļ���
	 * 
	 * @param f
	 *            File�ļ���ʵ��
	 */
	public String getname(File f) {
		String Name = f.getName();
		return Name;
	}

	/**
	 * ����ļ����ļ��еĸ�Ŀ¼
	 * 
	 * @param f
	 *            File�ļ���ʵ������
	 * @return
	 */
	public String getparentPath(File f) {
		String parentPath = f.getParent(); // ����ļ����ļ��еĸ�Ŀ¼
		return parentPath;
	}

	/**
	 * ����ļ�����·��
	 * 
	 * @param f
	 *            File�ļ���ʵ������
	 * @return
	 */
	public String getabsoultePath(File f) {
		String path = f.getPath();// ���·��
		return path;
	}

	/**
	 * �����ļ�
	 * 
	 * @param path
	 *            �ļ�·�������ļ������׺��
	 * @return �����Ƿ�ɹ� true ��ʾ�ɹ� false ��ʾʧ��
	 * @throws IOException
	 */
	public Boolean createFile(String path) throws IOException {
		File file = new File(path);
		// File myTempFile = File.createTempFile(fileNa, "." + fileEx);
		return file.createNewFile();// �����ļ�
	}

	/**
	 * �����ļ���
	 * 
	 * @param path
	 *            �����ļ��е�·��
	 * @return �Ƿ�ɹ�
	 */
	public Boolean createMkdir(String path) {
		File file = new File(path);
		return file.mkdir();
	}

	/**
	 * �ж��ǲ����ļ���
	 * 
	 * @param path
	 *            ��Ҫ�����ļ�·��
	 * @return �Ƿ����ļ���
	 */
	public Boolean isDirectory(String path) {
		File file = new File(path);
		return file.isDirectory();
	}

	/**
	 * �г��ļ����µ������ļ����ļ�����
	 * 
	 * @param path
	 *            ��Ҫ�г��ĸ�Ŀ¼
	 * @return
	 * 
	 *         File[]����
	 */
	public File[] fileList(String path) {
		File file = new File(path);
		File[] files = file.listFiles(); // �г��ļ����µ������ļ����ļ�����

		return files;
	}

	/**
	 * �����ļ����ļ�����
	 * 
	 * @param oldpath
	 *            ���ļ�����·��
	 * @param newpath
	 * @return
	 */
	public Boolean reName(String oldpath, File newfile) {
		File file = new File(oldpath);
		return file.renameTo(newfile);
	}

	/**
	 * ɾ��ָ���ļ����ļ���
	 * 
	 * @param path
	 *            ָ���ļ����ļ��е�·��
	 *@param  deleteThisPath
	 *        	      �Ƿ�ɾ�����·��
	 * @return �����Ƿ�����ɹ�
	 */

	public void deleteFolderFile(String filePath, boolean deleteThisPath)
			throws IOException {
		if (!TextUtils.isEmpty(filePath)) {
			File file = new File(filePath);

			if (file.isDirectory()) {// ����Ŀ¼
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFolderFile(files[i].getAbsolutePath(), true);
				}
			}
			if (deleteThisPath) {
				if (!file.isDirectory()) {// ������ļ���ɾ��
					file.delete();
				} else {// Ŀ¼
					if (file.listFiles().length == 0) {// Ŀ¼��û���ļ�����Ŀ¼��ɾ��
						file.delete();
					}
				}
			}
		}
	}

}
