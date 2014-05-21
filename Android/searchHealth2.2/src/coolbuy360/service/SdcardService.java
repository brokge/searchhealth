package coolbuy360.service;

import java.io.File;

import android.os.Environment;

public class SdcardService {

	/**
	 * �ж�SD���Ƿ����
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
	 * �ж�sdcard�ܴ�С
	 * 
	 * @param sdcard��·��
	 * @return �����ܴ�С private String sdcard_path = "/sdcard" ; Long sdcardAllSize
	 *         = sdcardAllSize(sdcard_path);
	 */
	public long sdcardAllSize(String path) {
		File pathFile = new File(path); // ȡ��sdcard�ļ�·��
		android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());
		long nTotalBlocks = statfs.getBlockCount(); // ��ȡSDCard��BLOCK����
		long nBlocSize = statfs.getBlockSize(); // ��ȡSDCard��ÿ��block��SIZE
		long nAvailaBlock = statfs.getAvailableBlocks(); // ��ȡ�ɹ�����ʹ�õ�Block������
		long nFreeBlock = statfs.getFreeBlocks(); // ��ȡʣ�µ�����Block������(����Ԥ����һ������޷�ʹ�õĿ�)
		long nSDTotalSize = nTotalBlocks * nBlocSize / 1024 / 1024; // ����SDCard
																	// ��������СMB
		return nSDTotalSize;
	}

	/**
	 * �ж�sdcardʣ���С
	 * 
	 * @param sdcard��·��
	 * @return �����ܴ�С private String sdcard_path = "/sdcard" ; Long
	 *         sdcardFreeSize = sdcardFreeSize(sdcard_path);
	 */
	public long sdcardFreeSize(String path) {
		File pathFile = new File(path); // ȡ��sdcard�ļ�·��
		android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());
		long nTotalBlocks = statfs.getBlockCount(); // ��ȡSDCard��BLOCK����
		long nBlocSize = statfs.getBlockSize(); // ��ȡSDCard��ÿ��block��SIZE
		long nAvailaBlock = statfs.getAvailableBlocks(); // ��ȡ�ɹ�����ʹ�õ�Block������
		long nFreeBlock = statfs.getFreeBlocks(); // ��ȡʣ�µ�����Block������(����Ԥ����һ������޷�ʹ�õĿ�)
		long nSDFreeSize = nAvailaBlock * nBlocSize / 1024 / 1024; // ���� SDCard
																	// ʣ���СMB
		return nSDFreeSize;

	}

	/**
	 * �ж�sdcard�Ѿ�ʹ�ô�С
	 * 
	 * @param sdcard��·��
	 * @return �����ܴ�С private String sdcard_path = "/sdcard" ; Long
	 *         sdcardUsedSize = sdcardUsedSize(sdcard_path);
	 */
	public long sdcardUsedSize(String path) {
		File pathFile = new File(path); // ȡ��sdcard�ļ�·��
		android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());
		long nTotalBlocks = statfs.getBlockCount(); // ��ȡSDCard��BLOCK����
		long nBlocSize = statfs.getBlockSize(); // ��ȡSDCard��ÿ��block��SIZE
		long nAvailaBlock = statfs.getAvailableBlocks(); // ��ȡ�ɹ�����ʹ�õ�Block������
		long nFreeBlock = statfs.getFreeBlocks(); // ��ȡʣ�µ�����Block������(����Ԥ����һ������޷�ʹ�õĿ�)
		long nSDFreeSize = nAvailaBlock * nBlocSize / 1024 / 1024; // ���� SDCard
																	// ʣ���СMB
		long nSDTotalSize = nTotalBlocks * nBlocSize / 1024 / 1024; // ����SDCard
																	// ��������СMB
		return nSDTotalSize - nSDFreeSize;
		// return FileFolder_All_Size(path) - FileFolder_Free_Size(path) ;
		// //�����������̫�ã������û��ע����code����Ҳ����Ū��������һ�����ú���

	}

}
