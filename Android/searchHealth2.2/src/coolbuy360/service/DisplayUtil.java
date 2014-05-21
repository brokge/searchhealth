package coolbuy360.service;

import android.content.Context;


	/**
	 * ��λת������
	 * 
	 * @author carrey
	 * 
	 */
	public class DisplayUtil {
		/**
		 * ��pxֵת��Ϊdip��dpֵ����֤�ߴ��С����
		 * 
		 * @param pxValue
		 * @param scale
		 *            ��DisplayMetrics��������density��
		 * @return
		 */
		public static int px2dip(float pxValue, float scale) {
			return (int) (pxValue / scale + 0.5f);
		}
		
		/**
		 * ��pxֵת��Ϊdip��dpֵ����֤�ߴ��С����
		 * 
		 * @param context
		 * @param pxValue
		 * @return
		 */
		public static int px2dip(Context context, float pxValue) {
			float scale = context.getResources().getDisplayMetrics().density;
			return (int) (pxValue / scale + 0.5f);
		}

		/**
		 * ��dip��dpֵת��Ϊpxֵ����֤�ߴ��С����
		 * 
		 * @param dipValue
		 * @param scale
		 *            ��DisplayMetrics��������density��
		 * @return
		 */
		public static int dip2px(float dipValue, float scale) {
			return (int) (dipValue * scale + 0.5f);
		}

		/**
		 * ��dip��dpֵת��Ϊpxֵ����֤�ߴ��С����
		 * 
		 * @param context
		 * @param dipValue
		 * @return
		 */
		public static int dip2px(Context context, float dipValue) {
			float scale = context.getResources().getDisplayMetrics().density;
			return (int) (dipValue * scale + 0.5f);
		}

		/**
		 * ��pxֵת��Ϊspֵ����֤���ִ�С����
		 * 
		 * @param pxValue
		 * @param fontScale
		 *            ��DisplayMetrics��������scaledDensity��
		 * @return
		 */
		public static int px2sp(float pxValue, float fontScale) {
			return (int) (pxValue / fontScale + 0.5f);
		}

		/**
		 * ��pxֵת��Ϊspֵ����֤���ִ�С����
		 * 
		 * @param context
		 * @param pxValue
		 * @return
		 */
		public static int px2sp(Context context, float pxValue) {
			float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
			return (int) (pxValue / fontScale + 0.5f);
		}

		/**
		 * ��spֵת��Ϊpxֵ����֤���ִ�С����
		 * 
		 * @param spValue
		 * @param fontScale
		 *            ��DisplayMetrics��������scaledDensity��
		 * @return
		 */
		public static int sp2px(float spValue, float fontScale) {
			return (int) (spValue * fontScale + 0.5f);
		}

		/**
		 * ��spֵת��Ϊpxֵ����֤���ִ�С����
		 * 
		 * @param context
		 * @param spValue
		 * @return
		 */
		public static int sp2px(Context context, float spValue) {
			float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
			return (int) (spValue * fontScale + 0.5f);
		}
	}

