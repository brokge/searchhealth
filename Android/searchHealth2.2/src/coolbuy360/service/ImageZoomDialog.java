package coolbuy360.service;

import java.io.File;
import java.io.IOException;

import coolbuy360.searchhealth.R;

/*import net.oschina.app.AppContext;
import net.oschina.app.AppException;
import net.oschina.app.R;
import net.oschina.app.api.ApiClient;
import net.oschina.app.common.FileUtils;
import net.oschina.app.common.ImageUtils;
import net.oschina.app.common.StringUtils;
import net.oschina.app.common.UIHelper;
import net.oschina.app.ui.ImageZoomDialog;*/
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class ImageZoomDialog extends Activity implements OnTouchListener,
		OnClickListener {

	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();
	PointF start = new PointF();
	PointF mid = new PointF();
	DisplayMetrics dm;
	float oldDist = 1f;
	private ImageView imgView;
	private Button zoomIn, zoomOut;
	private ViewSwitcher mViewSwitcher;
	private Button btnSave;

	// button zoom
	private float scaleWidth = 1;
	private float scaleHeight = 1;
	private Bitmap bitmap, zoomedBMP;
	private int zoom_level = 0;
	private static final double ZOOM_IN_SCALE = 1.25;// �Ŵ�ϵ��
	private static final double ZOOM_OUT_SCALE = 0.8;// ��Сϵ��

	float minScaleR;// ��С���ű���
	static final float MAX_SCALE = 4f;// ������ű���

	// We can be in one of these 3 states
	static final int NONE = 0;// ��ʼ״̬
	static final int DRAG = 1;// �϶�
	static final int ZOOM = 2;// ����
	int mode = NONE;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_zoom_dialog);
		this.initView();
		this.initData();
	}

	private void initView() {
	
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);// ��ȡ�ֱ���

		// zoomIn = (Button) findViewById(R.id.zoom_in);// �Ŵ�ť
		// zoomOut = (Button) findViewById(R.id.zoom_out);// ��С��ť
		// zoomIn.setOnClickListener(this);
		// zoomOut.setOnClickListener(this);

		imgView = (ImageView) findViewById(R.id.imagezoomdialog_image);
		imgView.setOnTouchListener(this);// ���ô�������

		mViewSwitcher = (ViewSwitcher) findViewById(R.id.imagezoomdialog_view_switcher);

		// ���浽ͼ��
		btnSave = (Button) findViewById(R.id.btn_save);
		/*btnSave.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				try {
					String saveImagePath = ((AppContext) getApplication())
							.getSaveImagePath();
					ImageUtils.saveImageToSD(ImageZoomDialog.this,
							saveImagePath + ImageUtils.getTempFileName()
									+ ".jpg", bitmap, 100);
					UIHelper.ToastMessage(ImageZoomDialog.this, "����ɹ�");
				} catch (IOException e) {
					e.printStackTrace();
					UIHelper.ToastMessage(ImageZoomDialog.this, "����ʧ��");
				}
			}
		});
	*/
	}

	private void initData() {
		final String imgURL = getIntent().getStringExtra("img_url");
		final String ErrMsg = getString(R.string.msg_load_image_fail);
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1 && msg.obj != null) {
					bitmap = (Bitmap) msg.obj;
					imgView.setImageBitmap(bitmap);
					minZoom();// ������С���ű�
					CheckView();// ����ͼ�����
					imgView.setImageMatrix(matrix);
					mViewSwitcher.showNext();
					btnSave.setVisibility(View.VISIBLE);
				} else {
				//	UIHelper.ToastMessage(ImageZoomDialog.this, ErrMsg);
					Toast.makeText(ImageZoomDialog.this, ErrMsg, Toast.LENGTH_LONG).show();
					finish();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				Bitmap bmp = null;
				String filename = fileUtril.getFileName(imgURL);
				try {
					// ��ȡ����ͼƬ
					if (imgURL.endsWith("portrait.gif")
							|| imgURL.equals("")) {
						bmp = BitmapFactory
								.decodeResource(imgView.getResources(),
										R.drawable.down_img);
					}
					if (bmp == null) {
						// �Ƿ��л���ͼƬ
						// Environment.getExternalStorageDirectory();����/sdcard
						/*String filepath = getFilesDir() + File.separator
								+ filename;
						File file = new File(filepath);
						if (file.exists()) {
							bmp = picUtril.getBitmap(imgView.getContext(),
									filename);
						}*/
						 File file= fileUtril.getSDandCacheFile(ImageZoomDialog.this,imgURL);
						 //
						 if(file!=null)
						 {
							 bmp= picUtril.getBitmapFromFile(ImageZoomDialog.this,file) ;							 
						 }
						 else {
							bmp=null;
						}
						
						//bmp= picUtril.getBitmapFromFile(ImageZoomDialog.this, fileUtril.getCacheFile(imgURL));
					}
					if (bmp == null) {					
						bmp = picUtril.getbitmapAndwrite(ImageZoomDialog.this,imgURL); //ApiClient.getNetBitmap(imgURL);
						if (bmp != null) {
							/*try {
								// дͼƬ����
								ImageUtils.saveImage(imgView.getContext(),
										filename, bmp);
							} catch (IOException e) {
								e.printStackTrace();
							}*/
							// ����ͼƬ
							bmp = picUtril.reDrawBitMap(ImageZoomDialog.this,
									bmp);
						}
					}
					msg.what = 1;
					msg.obj = bmp;
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}

	public boolean onTouch(View v, MotionEvent event) {
		// Handle touch events here...
		ImageView imgView = (ImageView) v;

		// Handle touch events here...
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// ��������ģʽ(����)
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			// Log.d(TAG, "mode=DRAG");
			mode = DRAG;
			break;
		// ���ö�㴥��ģʽ(����)
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			// Log.d(TAG, "oldDist=" + oldDist);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
				// Log.d(TAG, "mode=ZOOM");
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			// Log.d(TAG, "mode=NONE");
			break;
		// ��ΪDRAGģʽ�������ƶ�ͼƬ
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				matrix.set(savedMatrix);
				// ����λ��
				matrix.postTranslate(event.getX() - start.x, event.getY()
						- start.y);
			}
			// ��ΪZOOMģʽ�����㴥������
			else if (mode == ZOOM) {
				float newDist = spacing(event);
				// Log.d(TAG, "newDist=" + newDist);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = newDist / oldDist;
					// �������ű�����ͼƬ�е�λ��
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
			}
			break;
		}

		// Perform the transformation
		imgView.setImageMatrix(matrix);
		CheckView();
		return true; // indicate event was handled
	}

	/**
	 * ���������С���ű������Զ�����
	 */
	private void CheckView() {
		float p[] = new float[9];
		matrix.getValues(p);
		if (mode == ZOOM) {
			if (p[0] < minScaleR) {
				matrix.setScale(minScaleR, minScaleR);
			}
			if (p[0] > MAX_SCALE) {
				matrix.set(savedMatrix);
			}
		}
		center();
	}

	/**
	 * ��С���ű��������Ϊ100%
	 */
	private void minZoom() {
		// minScaleR = Math.min(
		// (float) dm.widthPixels / (float) bitmap.getWidth(),
		// (float) dm.heightPixels / (float) bitmap.getHeight());
		if (bitmap.getWidth() >= dm.widthPixels)
			minScaleR = ((float) dm.widthPixels) / bitmap.getWidth();
		else
			minScaleR = 1.0f;

		if (minScaleR < 1.0) {
			matrix.postScale(minScaleR, minScaleR);
		}
	}

	private void center() {
		center(true, true);
	}

	/**
	 * �����������
	 */
	protected void center(boolean horizontal, boolean vertical) {
		Matrix m = new Matrix();
		m.set(matrix);
		RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
		m.mapRect(rect);

		float height = rect.height();
		float width = rect.width();

		float deltaX = 0, deltaY = 0;

		if (vertical) {
			// ͼƬС����Ļ��С���������ʾ��������Ļ���Ϸ������������ƣ��·�������������
			int screenHeight = dm.heightPixels;
			if (height < screenHeight) {
				deltaY = (screenHeight - height) / 2 - rect.top;
			} else if (rect.top > 0) {
				deltaY = -rect.top;
			} else if (rect.bottom < screenHeight) {
				deltaY = imgView.getHeight() - rect.bottom;
			}
		}

		if (horizontal) {
			int screenWidth = dm.widthPixels;
			if (width < screenWidth) {
				deltaX = (screenWidth - width) / 2 - rect.left;
			} else if (rect.left > 0) {
				deltaX = -rect.left;
			} else if (rect.right < screenWidth) {
				deltaX = screenWidth - rect.right;
			}
		}
		matrix.postTranslate(deltaX, deltaY);
	}

	// �����ƶ�����
	@SuppressLint("NewApi")
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	// �����е�λ��
	@SuppressLint("NewApi")
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	// �Ŵ���С��ť����¼�
	// @Override
	public void onClick(View v) {
		if (v == zoomIn) {
			enlarge();
		} else if (v == zoomOut) {
			small();
		}
	}

	// ��ť�����С����
	private void small() {
		int bmpWidth = bitmap.getWidth();
		int bmpHeight = bitmap.getHeight();

		scaleWidth = (float) (scaleWidth * ZOOM_OUT_SCALE);
		scaleHeight = (float) (scaleHeight * ZOOM_OUT_SCALE);

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		zoomedBMP = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight,
				matrix, true);
		imgView.setImageBitmap(zoomedBMP);
	}

	// ��ť����Ŵ���
	private void enlarge() {
		try {
			int bmpWidth = bitmap.getWidth();
			int bmpHeight = bitmap.getHeight();

			scaleWidth = (float) (scaleWidth * ZOOM_IN_SCALE);
			scaleHeight = (float) (scaleHeight * ZOOM_IN_SCALE);

			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			zoomedBMP = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight,
					matrix, true);
			imgView.setImageBitmap(zoomedBMP);
		} catch (Exception e) {
			// can't zoom because of memory issue, just ignore, no big deal
		}
	}
}