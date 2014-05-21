package coolbuy360.service;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * ��Ļ��������
 * 
 * @author carrey
 * 
 */
public class DisplayParams {
	/** ��Ļ��ȡ���px */
	public int screenWidth;
	/** ��Ļ�߶ȡ���px */
	public int screenHeight;
	/** ��Ļ�ܶȡ���dpi */
	public int densityDpi;
	/** ����ϵ������densityDpi/160 */
	public float scale;
	/** ��������ϵ�� */
	public float fontScale;
	/** ��Ļ���� */
	public int screenOrientation;
	/** ��ʾ��Ļ����ֱ */
	public final static int SCREEN_ORIENTATION_VERTICAL = 1;
	/** ��ʾ��Ļ����ˮƽ */
	public final static int SCREEN_ORIENTATION_HORIZONTAL = 2;

	private static DisplayParams singleInstance;

	/**
	 * ˽�й��췽��
	 * 
	 * @param context
	 */
	private DisplayParams(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		densityDpi = dm.densityDpi;
		scale = dm.density;
		fontScale = dm.scaledDensity;
		screenOrientation = screenHeight > screenWidth ? SCREEN_ORIENTATION_VERTICAL
				: SCREEN_ORIENTATION_HORIZONTAL;
	}

	/**
	 * ��ȡʵ��
	 * 
	 * @param context
	 * @return
	 */
	public static DisplayParams getInstance(Context context) {
		if (singleInstance == null) {
			singleInstance = new DisplayParams(context);
		}
		return singleInstance;
	}

	/**
	 * ��ȡ�µ�ʵ��
	 * 
	 * @param context
	 * @return
	 */
	public static DisplayParams getNewInstance(Context context) {
		if (singleInstance != null) {
			singleInstance = null;
		}
		return getInstance(context);
	}
}
