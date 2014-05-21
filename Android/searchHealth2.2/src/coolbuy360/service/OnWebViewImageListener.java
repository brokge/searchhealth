package coolbuy360.service;
/**
 * 监听webview上的图片
 * 
 * @author chenlw
 *
 */

public interface OnWebViewImageListener {
	/**
	 * 点击webview上的图片，传入该缩略图的大图Url
	 * @param bigImageUrl
	 */
	void onImageClick(String bigImageUrl);
}
