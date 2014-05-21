package coolbuy360.service;

import java.io.File;

import android.os.Environment;

public class SdcardService {

	/**
	 * 判断SD卡是否存在
	 */
	public static boolean hasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断sdcard总大小
	 * 
	 * @param sdcard的路径
	 * @return 返回总大小 private String sdcard_path = "/sdcard" ; Long sdcardAllSize
	 *         = sdcardAllSize(sdcard_path);
	 */
	public long sdcardAllSize(String path) {
		File pathFile = new File(path); // 取得sdcard文件路径
		android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());
		long nTotalBlocks = statfs.getBlockCount(); // 获取SDCard上BLOCK总数
		long nBlocSize = statfs.getBlockSize(); // 获取SDCard上每个block的SIZE
		long nAvailaBlock = statfs.getAvailableBlocks(); // 获取可供程序使用的Block的数量
		long nFreeBlock = statfs.getFreeBlocks(); // 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)
		long nSDTotalSize = nTotalBlocks * nBlocSize / 1024 / 1024; // 计算SDCard
																	// 总容量大小MB
		return nSDTotalSize;
	}

	/**
	 * 判断sdcard剩余大小
	 * 
	 * @param sdcard的路径
	 * @return 返回总大小 private String sdcard_path = "/sdcard" ; Long
	 *         sdcardFreeSize = sdcardFreeSize(sdcard_path);
	 */
	public long sdcardFreeSize(String path) {
		File pathFile = new File(path); // 取得sdcard文件路径
		android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());
		long nTotalBlocks = statfs.getBlockCount(); // 获取SDCard上BLOCK总数
		long nBlocSize = statfs.getBlockSize(); // 获取SDCard上每个block的SIZE
		long nAvailaBlock = statfs.getAvailableBlocks(); // 获取可供程序使用的Block的数量
		long nFreeBlock = statfs.getFreeBlocks(); // 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)
		long nSDFreeSize = nAvailaBlock * nBlocSize / 1024 / 1024; // 计算 SDCard
																	// 剩余大小MB
		return nSDFreeSize;

	}

	/**
	 * 判断sdcard已经使用大小
	 * 
	 * @param sdcard的路径
	 * @return 返回总大小 private String sdcard_path = "/sdcard" ; Long
	 *         sdcardUsedSize = sdcardUsedSize(sdcard_path);
	 */
	public long sdcardUsedSize(String path) {
		File pathFile = new File(path); // 取得sdcard文件路径
		android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());
		long nTotalBlocks = statfs.getBlockCount(); // 获取SDCard上BLOCK总数
		long nBlocSize = statfs.getBlockSize(); // 获取SDCard上每个block的SIZE
		long nAvailaBlock = statfs.getAvailableBlocks(); // 获取可供程序使用的Block的数量
		long nFreeBlock = statfs.getFreeBlocks(); // 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)
		long nSDFreeSize = nAvailaBlock * nBlocSize / 1024 / 1024; // 计算 SDCard
																	// 剩余大小MB
		long nSDTotalSize = nTotalBlocks * nBlocSize / 1024 / 1024; // 计算SDCard
																	// 总容量大小MB
		return nSDTotalSize - nSDFreeSize;
		// return FileFolder_All_Size(path) - FileFolder_Free_Size(path) ;
		// //用这个方法不太好，最好用没有注掉的code，且也可以弄两个变量一减不用函数

	}

}
