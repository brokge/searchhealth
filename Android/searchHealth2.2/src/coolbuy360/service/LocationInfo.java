/**
 * 
 */
package coolbuy360.service;

/**
 * 地理坐标信息
 * 
 * @author yangxc
 * 
 */
public class LocationInfo {
	/** 经纬度信息结构体 */
	public static class SItude {
		/** 纬度 */
		public double latitude;
		/** 经度 */
		public double longitude;
		/** 地址 */
		public String address;
		/** 城市 */
		public String city;
		/** 定位精度 */
		public float accuracy;
		/** GPS定位时方向角度 */
		public float direction;
	}
}
