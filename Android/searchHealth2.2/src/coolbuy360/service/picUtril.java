package coolbuy360.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;

public class picUtril {

	private static final String TAG = "PicUtil";

	/**
	 * ����һ����������(URL)��ȡbitmapDrawableͼ��
	 * 
	 * @param imageUri
	 * @return
	 */
	public static BitmapDrawable getfriendicon(URL imageUri) {

		BitmapDrawable icon = null;
		try {
			HttpURLConnection hp = (HttpURLConnection) imageUri
					.openConnection();
			icon = new BitmapDrawable(hp.getInputStream());// ��������ת����bitmap
			hp.disconnect();// �ر�����
		} catch (Exception e) {
		}
		return icon;
	}

	/**
	 * ����һ����������(String)��ȡbitmapDrawableͼ��
	 * 
	 * @param imageUri
	 * @return
	 */
	public static BitmapDrawable getcontentPic(String imageUri) {
		URL imgUrl = null;
		try {
			imgUrl = new URL(imageUri);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		BitmapDrawable icon = null;
		try {
			HttpURLConnection hp = (HttpURLConnection) imgUrl.openConnection();
			icon = new BitmapDrawable(hp.getInputStream());// ��������ת����bitmap
			hp.disconnect();// �ر�����
		} catch (Exception e) {
		}
		return icon;
	}

	/**
	 * ����һ����������(URL)��ȡbitmapͼ��
	 * 
	 * @param imageUri
	 * @return
	 */
	public static Bitmap getusericon(URL imageUri) {
		// ��ʾ�����ϵ�ͼƬ
		URL myFileUrl = imageUri;
		Bitmap bitmap = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * ����һ����������(String)��ȡbitmapͼ��
	 * 
	 * @param imageUri
	 * @return
	 * @throws MalformedURLException
	 */
	public static Bitmap getbitmap(String imageUri) {
		// ��ʾ�����ϵ�ͼƬ
		Bitmap bitmap = null;
		try {
			URL myFileUrl = new URL(imageUri);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();		
			Log.i(TAG, "image downloading." + imageUri);
			/*BitmapFactory.Options options=new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inSampleSize = 2; //width��hight��Ϊԭ����2��һ
*/			
			
			//������ʱ�ڴ�
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inTempStorage = new byte[4 * 1024];
			
			InputStream is = conn.getInputStream();	
			
			if(!is.equals(null))
			{
			bitmap = BitmapFactory.decodeStream(is);
			}
			else
			{
				bitmap=null;
				
			}
			is.close();

			Log.i(TAG, "image download finished." + imageUri);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		//zoomBitmap
		return bitmap;
	}
	//����һ������ͼƬurl��ȡInputStream�ķ���
  public static byte[] getBytes(InputStream is) throws IOException {
       ByteArrayOutputStream outstream = new ByteArrayOutputStream();
       byte[] buffer = new byte[1024]; // ������װ
	   int len = -1;
       while ((len = is.read(buffer)) != -1) {
	      outstream.write(buffer, 0, len);
	   }
	   outstream.close();
	    // �ر���һ��Ҫ�ǵá�
	  return outstream.toByteArray();
   }

	//Ȼ��ʹ�÷���decodeByteArray���������������룬����Bitmap����
	  //  byte[] data = getBytesFromInputStream(new URL(imgUrl).openStream());
	   // Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);


	/**
	 * ����ͼƬ ͬʱд������sd�����ļ���
	 * 
	 * @param context
	 * @param imageUri
	 * @return
	 * @throws MalformedURLException
	 */
	public static Bitmap getbitmapAndwrite( Context context, String imageUri) {
		Bitmap bitmap = null;
	
		try {
			// ��ʾ�����ϵ�ͼƬ
			URL myFileUrl = new URL(imageUri);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();	
			//BufferedInputStream	inputStream = new BufferedInputStream(is);
			bitmap= BitmapFactory.decodeStream(is);
			//bitmap= BitmapFactory.decodeStream(inputStream);
			// byte[] data =picUtril.getBytes(inputStream);//ת�������飬��ֹ�Ͱ汾����������
			// bitmap =  BitmapFactory.decodeByteArray(data, 0, data.length);	
			// 
			//bitmap
			 if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// picUtril.saveFileToSD(imageUri,is);	
				 picUtril.saveFileToSD1(imageUri,bitmap);
			 }
			else {						
			
				picUtril.saveImage(context,fileUtril.getFileName(imageUri),bitmap,8,CompressFormat.PNG);
			}		
			
			
			/*//����sd���б������·���ﴴ��һ�����ļ�
			File cacheFile = fileUtril.getCacheFile(imageUri);
			BufferedOutputStream bos = null;
			//���Ѷ�ȡ���ļ���д�뵽���ļ���
			bos = new BufferedOutputStream(new FileOutputStream(cacheFile));
			Log.i(TAG, "write file to " + cacheFile.getCanonicalPath());//֤���Ѿ�������һ���ļ��������ļ���

			byte[] buf = new byte[1024];
			int len = 0;
			// �������ϵ�ͼƬ�洢������
			while ((len = is.read(buf)) > 0) {
				bos.write(buf, 0, len);
			}			
			is.close();
			bos.close();
*/
			// �ӱ��ؼ���ͼƬ
			//bitmap = BitmapFactory.decodeFile(cacheFile.getCanonicalPath());
			//String name = MD5.getMD5(imageUri);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	/**
	 * ��ȡbitmap
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static Bitmap getBitmap(Context context, String fileName) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			fis = context.openFileInput(fileName);
			bitmap = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return bitmap;
	}
	public static Bitmap getBitmapFromFile(Context context, File file) {
		Bitmap bitmap = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(fis);
			
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
		}	   
		return bitmap;
	}
	
	/**
	 * saveFileToSD
	 * @param imageUri
	 * @param is 
	 *         InputStream
	 */
	public static void saveFileToSD( String imageUri,InputStream is ) {	
		File cacheFile = fileUtril.getCacheFile(imageUri);
		BufferedOutputStream bos = null;
		
		BufferedInputStream inputStream = null;
	
		//���Ѷ�ȡ���ļ���д�뵽���ļ���
		try {
			bos = new BufferedOutputStream(new FileOutputStream(cacheFile));
			inputStream = new BufferedInputStream(is);
			try {
				Log.i(TAG, "write file to " + cacheFile.getCanonicalPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//֤���Ѿ�������һ���ļ��������ļ���
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		}	

		byte[] buf = new byte[1024];
		int len = 0;
		// �������ϵ�ͼƬ�洢������
		try {
			while ((len = inputStream.read(buf)) > 0) {
				try {
					bos.write(buf, 0, len);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			inputStream.close();
			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
       
		
	}
	
	
	/**
	 * �����ļ����ڴ濨��
	 * @param picName
	 *         ��Ҫ������ļ���
	 * @param bitmap
	 *      �����bitmap����
	 * @return
	 */
	public static boolean saveFileToSD1(String imageUri, Bitmap bitmap) {
		boolean nowbol = false;
		try {
			/*File saveFile = new File("/mnt/sdcard/download/searchHealth/" + picName
					+ ".png");*/
			//File saveFile = new File("/mnt/sdcard/download/searchHealth/" + picName					);
			File cacheFile = fileUtril.getCacheFile(imageUri);		
			FileOutputStream saveFileOutputStream;
			saveFileOutputStream = new FileOutputStream(cacheFile);
			nowbol = bitmap.compress(Bitmap.CompressFormat.PNG, 100,
					saveFileOutputStream);
			saveFileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nowbol;
	}
	/**
	 * ����ͼƬ��CACHE
	 * @param context
	 * @param fileName
	 * @param bitmap
	 * @param quality
	 * @throws IOException
	 */
	public  static void saveImage(Context context, String fileName,
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
	
	public static void writeTofiles(Context context, Bitmap bitmap,
			String filename) {
		BufferedOutputStream outputStream = null;
		try {
			outputStream = new BufferedOutputStream(context.openFileOutput(
					filename, Context.MODE_PRIVATE));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���ļ�д�뻺��ϵͳ��
	 * 
	 * @param filename
	 * @param is
	 * @return
	 */
	public static String writefile(Context context, String filename,
			InputStream is) {
		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		try {
			inputStream = new BufferedInputStream(is);
			outputStream = new BufferedOutputStream(context.openFileOutput(
					filename, Context.MODE_PRIVATE));
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, length);
			}
		} catch (Exception e) {
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return context.getFilesDir() + "/" + filename + ".jpg";
	}

	// �Ŵ���СͼƬ
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidht, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return newbmp;
	}

	// ��Drawableת��ΪBitmap
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;

	}
	/**
	 * (����)�ػ�ͼƬ
	 * 
	 * @param context
	 *            Activity
	 * @param bitmap
	 * @return
	 */
	public static Bitmap reDrawBitMap(Activity context, Bitmap bitmap) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int rHeight = dm.heightPixels;
		int rWidth = dm.widthPixels;
		// float rHeight=dm.heightPixels/dm.density+0.5f;
		// float rWidth=dm.widthPixels/dm.density+0.5f;
		// int height=bitmap.getScaledHeight(dm);
		// int width = bitmap.getScaledWidth(dm);
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();
		float zoomScale;
		/** ��ʽ1 **/
		// if(rWidth/rHeight>width/height){//�Ը�Ϊ׼
		// zoomScale=((float) rHeight) / height;
		// }else{
		// //if(rWidth/rHeight<width/height)//�Կ�Ϊ׼
		// zoomScale=((float) rWidth) / width;
		// }
		/** ��ʽ2 **/
		// if(width*1.5 >= height) {//�Կ�Ϊ׼
		// if(width >= rWidth)
		// zoomScale = ((float) rWidth) / width;
		// else
		// zoomScale = 1.0f;
		// }else {//�Ը�Ϊ׼
		// if(height >= rHeight)
		// zoomScale = ((float) rHeight) / height;
		// else
		// zoomScale = 1.0f;
		// }
		/** ��ʽ3 **/
		if (width >= rWidth)
			zoomScale = ((float) rWidth) / width;
		else
			zoomScale = 1.0f;
		// ��������ͼƬ�õ�matrix����
		Matrix matrix = new Matrix();
		// ����ͼƬ����
		matrix.postScale(zoomScale, zoomScale);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}
/*	private void saveBmpToSd(Bitmap bm,String url)
	{
		if(bm==null)
		{
			Log.w(TAG, "Trying to save null bitmap");
			return;
		}
		
		ir()
		
	}
	
	*//**
	* ����sdcard�ϵ�ʣ��ռ�
	* @return
	*/
/*	private int freeSpaceOnSd() {
	StatFs stat = new StatFs(Environment.getExternalStorageDirectory() .getPath());
	double sdFreeMB = ((double)stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
	return (int) sdFreeMB;
	} */
	

/*	// ���Բ��ͼƬ�ķ���
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		if(bitmap == null){
			return null;
		}
		
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	// ��ô���Ӱ��ͼƬ����
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
				width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}
*/
}