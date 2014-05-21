package coolbuy360.control;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpParams;

import coolbuy360.adapter.GalleryAdapter;
import coolbuy360.searchhealth.R;
import coolbuy360.service.searchApp;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Gallery;
public class MyShowImgDialog extends Activity {

	private String imgpath;
	//MyImageView1 image;
	public static int screenW;
	public static int screenH;
	
	private boolean isScale = false; // 是否缩放
	private float beforeLenght = 0.0f; // 两触点距离
	private float afterLenght = 0.0f;
	private float currentScale = 1.0f;
	private ProgressDialog progressDialog;
	private PorterDuffView myProgressView;
	List<Bitmap> bmps;
	private MyGallery mygallery;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.myshow_img_dialog);
		Bundle bundle = getIntent().getExtras();
		imgpath = bundle.getString("imgpath");
		//image = new MyImageView1(this);
		Rect frame = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		screenW = this.getWindowManager().getDefaultDisplay().getWidth();
		screenH = this.getWindowManager().getDefaultDisplay().getHeight();
		/*Bitmap bmp = picUtril.getbitmap(imgpath);
		Log.i("chenlinwei", bmp + "");*/
		myProgressView=(PorterDuffView)findViewById(R.id.myprogress);
		mygallery=(MyGallery)findViewById(R.id.mygallery);
		myProgressView.setOnClickListener(new onclick());
		//myProgressView.invalidate();
		//new asyLoadImg().execute(imgpath);
		new asyLoadImg(myProgressView).execute(imgpath);
		
	}
	
	private final class onclick implements View.OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v instanceof PorterDuffView) {
				PorterDuffView pdView = (PorterDuffView) v;
                if (pdView.isLoading() == false) {
                      //  DownloadImgTask task = new DownloadImgTask(pdView);
                       // task.execute(STRING_ARR[pdView.getId() % STRING_ARR.length]);
                		new asyLoadImg(pdView).execute(imgpath);
                       /* pdView.setPorterDuffMode(true);
                        pdView.setLoading(true);
                        pdView.setProgress(0);
                        pdView.invalidate();*/
                }
			}

		}
	}
private void InitGallery(List<Bitmap> bmps)
{
	//gallery = (MyGallery) findViewById(R.id.mygallery);
	mygallery.setVerticalFadingEdgeEnabled(false);
	mygallery.setHorizontalFadingEdgeEnabled(false);// );//
													// 设置view在水平滚动时，水平边不淡出。
	GalleryAdapter adapter = new GalleryAdapter(this, bmps);
	mygallery.setAdapter(adapter);
}
	/**
	 * 计算两点间的距离
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Log.i("lyc", "touched---------------");
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_POINTER_DOWN:
			beforeLenght = spacing(event);
			if (beforeLenght > 5f) {
				isScale = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			/* 处理拖动 */
			if (isScale) {
				afterLenght = spacing(event);
				if (afterLenght < 5f)
					break;
				float gapLenght = afterLenght - beforeLenght;
				if (gapLenght == 0) {
					break;
				} else if (Math.abs(gapLenght) > 5f) {
					float scaleRate = gapLenght / 854;
					Animation myAnimation_Scale = new ScaleAnimation(
							currentScale, currentScale + scaleRate,
							currentScale, currentScale + scaleRate,
							Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					myAnimation_Scale.setDuration(100);
					myAnimation_Scale.setFillAfter(true);
					myAnimation_Scale.setFillEnabled(true);
					currentScale = currentScale + scaleRate;
					mygallery.getSelectedView().setLayoutParams(
							new Gallery.LayoutParams(
									(int) (480 * (currentScale)),
									(int) (854 * (currentScale))));
					beforeLenght = afterLenght;
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_POINTER_UP:
			isScale = false;
			break;
		}

		//return super.onTouchEvent(event);
		return false;
	}
	private final class asyLoadImg extends AsyncTask<String , Float, Bitmap>
	{
		searchApp app=searchApp.getInstance();
		HttpClient httpClient=app.getHttpClient();
		Bitmap bit=null;
		List<Bitmap> bmps = new ArrayList<Bitmap>();
		private PorterDuffView  myProgressView;
		  public asyLoadImg(PorterDuffView pdView) {
              this.myProgressView = pdView;
		  }

		
		
		private void printHttpResponse(HttpResponse httpResponse) {
               Header[] headerArr = httpResponse.getAllHeaders();
               for (int i = 0; i < headerArr.length; i++) {
                       Header header = headerArr[i];                    
               }
               HttpParams params = httpResponse.getParams();
              
       }
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//super.onPreExecute();
			/*progressDialog=new ProgressDialog(MyShowImgDialog.this);
			//progressDialog.set
			progressDialog.setIndeterminate(false);// false代表根据程序进度确切的显示进度  
		    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置进度条的形  
			progressDialog.setProgress(0);
			progressDialog.setMax(100);
			progressDialog.show();*/
			// PorterDuffView pdView = (PorterDuffView) 
		/*	mygallery.setVisibility(View.GONE);
			myProgressView.setPorterDuffMode(true);
			myProgressView.setLoading(true);
			myProgressView.setProgress(0);*/
			
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			//Log.i(TAG, "doInBackground:" + params[0]);            
			
			try {
				HttpGet httpGet = new HttpGet(params[0]);
				InputStream is = null;
				ByteArrayOutputStream baos = null;
				try {
					HttpResponse httpResponse = httpClient.execute(httpGet);
					printHttpResponse(httpResponse);
					HttpEntity httpEntity = httpResponse.getEntity();
					long length = httpEntity.getContentLength();
					// Log.i(TAG, "content length=" + length);
					is = httpEntity.getContent();
					if (is != null) {
						baos = new ByteArrayOutputStream();
						byte[] buf = new byte[128];
						int read = -1;
						int count = 0;
						while ((read = is.read(buf)) != -1) {
							baos.write(buf, 0, read);
							count += read;
							publishProgress(count * 1.0f / length);
						}
						// Log.i(TAG, "count=" + count + " length=" + length);
						byte[] data = baos.toByteArray();
						Bitmap bit = BitmapFactory.decodeByteArray(data, 0,
								data.length);
						return bit;
					}
				} catch (ClientProtocolException e) {
					// e.printStackTrace();
				} catch (IOException e) {
					// e.printStackTrace();
				} catch (Exception e) {
					// e.printStackTrace();
				} finally {
					try {
						if (baos != null) {
							baos.close();
						}
						if (is != null) {
							is.close();
						}
					} catch (IOException e) {
						// e.printStackTrace();
					} catch (Exception e) {
						// e.printStackTrace();
					}
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}
            return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			if(result!=null)
			{					
				Log.i("chenlinwei", result+"");
			
				mygallery.setVisibility(View.VISIBLE);
				bmps.add(result);				
				InitGallery(bmps);
				myProgressView.setVisibility(View.GONE);
				//progressDialog.cancel();
			}
			else
			{				
				Log.i("chenlinwei", "下载图片出现错误");
				MyShowImgDialog.this.finish();
			}
		}

		@Override
		protected void onProgressUpdate(Float... values) {
			// TODO Auto-generated method stub
			Float value=values[0];
		/*	int count=(int)(value*100) ;
			progressDialog.setProgress(count);*/
			myProgressView.setProgress(value);
			
		}
		
		
	}
	

}
