package coolbuy360.dbhelper;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * TODO 数据库帮手基类，一个数据库对应一个具体实现。 可以在内部定义表名，字段名和各种业务逻辑操作等等。 对数据库的增删改操作，请使用事务管理。
 * 对数据的查询操作，请在使用Cursor前，调用startManagingCursor()方法管理Cursor。
 * 
 */
@SuppressLint("NewApi")
public abstract class abstractDatabaseHelper {
	
	public static int ReadAble = 0;
	public static int WriteAble = 1;
	
	/**
	 * SQLite数据库实例
	 */
	protected SQLiteDatabase mDb = null;

	/**
	 * 数据库创建帮手
	 */
	protected CreateDBHelper mDbHelper = null;

	/**
	 * 获得当前数据库帮手类标识(一般是该类名称)，用于日志等的记录
	 * 
	 * @return
	 */
	protected abstract String getTag();

	/**
	 * 获得数据库名称
	 * 
	 * @return
	 */
	protected abstract String getDatabaseName1();

	/**
	 * 获得数据库版本，值至少为1。 当数据库结构发生改变的时候，请将此值加1，系统会在初始化时自动调用
	 * createDBTables和dropDBTables方法更新数据库结构。
	 * 
	 * @return
	 */
	protected abstract int getDatabaseVersion();

	/**
	 * 创建数据库表的SQL语句，一个元素一条语句
	 * 
	 * @return
	 */
	protected abstract String[] createDBTables();

	/**
	 * 删除数据库表的SQL语句，一个元素一条语句
	 * 
	 * @return
	 */
	protected abstract String[] dropDBTables();

	/**
	 * 
	 * TODO 内部数据库创建帮手类
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
		 * 批量执行Sql语句
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
	 * 打开或者创建一个指定名称的数据库
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
	 * 关闭数据库
	 */
	public void close() {
		if (mDbHelper != null) {
			Log.i(getTag(), "Close database '" + getDatabaseName1() + "'");
			mDbHelper.close();
		}
	}

}
