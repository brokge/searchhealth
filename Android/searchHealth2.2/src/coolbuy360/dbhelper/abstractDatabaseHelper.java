package coolbuy360.dbhelper;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * TODO ���ݿ���ֻ��࣬һ�����ݿ��Ӧһ������ʵ�֡� �������ڲ�����������ֶ����͸���ҵ���߼������ȵȡ� �����ݿ����ɾ�Ĳ�������ʹ���������
 * �����ݵĲ�ѯ����������ʹ��Cursorǰ������startManagingCursor()��������Cursor��
 * 
 */
@SuppressLint("NewApi")
public abstract class abstractDatabaseHelper {
	
	public static int ReadAble = 0;
	public static int WriteAble = 1;
	
	/**
	 * SQLite���ݿ�ʵ��
	 */
	protected SQLiteDatabase mDb = null;

	/**
	 * ���ݿⴴ������
	 */
	protected CreateDBHelper mDbHelper = null;

	/**
	 * ��õ�ǰ���ݿ�������ʶ(һ���Ǹ�������)��������־�ȵļ�¼
	 * 
	 * @return
	 */
	protected abstract String getTag();

	/**
	 * ������ݿ�����
	 * 
	 * @return
	 */
	protected abstract String getDatabaseName1();

	/**
	 * ������ݿ�汾��ֵ����Ϊ1�� �����ݿ�ṹ�����ı��ʱ���뽫��ֵ��1��ϵͳ���ڳ�ʼ��ʱ�Զ�����
	 * createDBTables��dropDBTables�����������ݿ�ṹ��
	 * 
	 * @return
	 */
	protected abstract int getDatabaseVersion();

	/**
	 * �������ݿ���SQL��䣬һ��Ԫ��һ�����
	 * 
	 * @return
	 */
	protected abstract String[] createDBTables();

	/**
	 * ɾ�����ݿ���SQL��䣬һ��Ԫ��һ�����
	 * 
	 * @return
	 */
	protected abstract String[] dropDBTables();

	/**
	 * 
	 * TODO �ڲ����ݿⴴ��������
	 * 
	 */
	private class CreateDBHelper extends SQLiteOpenHelper {
		public CreateDBHelper(Context ctx) {
			super(ctx, getDatabaseName1(), null, getDatabaseVersion());
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			executeBatch(createDBTables(), db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			if (oldVersion == 1 && newVersion == 2) {

			} else if (newVersion > oldVersion) {
				Log.w(getTag(), "Upgrading database '" + getDatabaseName()
						+ "' from version " + oldVersion + " to " + newVersion);
				executeBatch(dropDBTables(), db);
				onCreate(db);
			}
		}

		/**
		 * ����ִ��Sql���
		 * 
		 * @param sqls
		 * @param db
		 */
		private void executeBatch(String[] sqls, SQLiteDatabase db) {
			if (sqls == null)
				return;

			db.beginTransaction();
			try {
				int len = sqls.length;
				for (int i = 0; i < len; i++) {
					db.execSQL(sqls[i]);
				}
				db.setTransactionSuccessful();
			} catch (Exception e) {
				Log.e(getTag(), e.getMessage(), e);
			} finally {
				db.endTransaction();
			}
		}
	}

	/**
	 * �򿪻��ߴ���һ��ָ�����Ƶ����ݿ�
	 * 
	 * @param dbName
	 * @param ctx
	 */
	public void open(Context ctx, int openflag) {
		Log.i(getTag(), "Open database '" + getDatabaseName1() + "'");
		mDbHelper = new CreateDBHelper(ctx);
		if (openflag == 0) {
			mDb = mDbHelper.getReadableDatabase();
		} else {
			mDb = mDbHelper.getWritableDatabase();
		}
	}

	/**
	 * �ر����ݿ�
	 */
	public void close() {
		if (mDbHelper != null) {
			Log.i(getTag(), "Close database '" + getDatabaseName1() + "'");
			mDbHelper.close();
		}
	}

}
