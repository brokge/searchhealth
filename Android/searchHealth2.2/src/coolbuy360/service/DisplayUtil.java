package coolbuy360.service;

import android.content.Context;


	/**
	 * 单位转换工具
	 * 
	 * @author carrey
	 * 
	 */
	public class DisplayUtil {
		/**
		 * 将px值转换为dip或dp值，保证尺寸大小不变
		 * 
		 * @param pxValue
		 * @param scale
		 *            （DisplayMetrics类中属性density）
		 * @return
		 */
		public static int px2dip(float pxValue, float scale) {
			return (int) (pxValue / scale + 0.5f);
		}
		
		/**
		 * 将px值转换为dip或dp值，保证尺寸大小不变
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
		 * 将dip或dp值转换为px值，保证尺寸大小不变
		 * 
		 * @param dipValue
		 * @param scale
		 *            （DisplayMetrics类中属性density）
		 * @return
		 */
		public static int dip2px(float dipValue, float scale) {
			return (int) (dipValue * scale + 0.5f);
		}

		/**
		 * 将dip或dp值转换为px值，保证尺寸大小不变
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
		 * 将px值转换为sp值，保证文字大小不变
		 * 
		 * @param pxValue
		 * @param fontScale
		 *            （DisplayMetrics类中属性scaledDensity）
		 * @return
		 */
		public static int px2sp(float pxValue, float fontScale) {
			return (int) (pxValue / fontScale + 0.5f);
		}

		/**
		 * 将px值转换为sp值，保证文字大小不变
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
		 * 将sp值转换为px值，保证文字大小不变
		 * 
		 * @param spValue
		 * @param fontScale
		 *            （DisplayMetrics类中属性scaledDensity）
		 * @return
		 */
		public static int sp2px(float spValue, float fontScale) {
			return (int) (spValue * fontScale + 0.5f);
		}

		/**
		 * 将sp值转换为px值，保证文字大小不变
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

