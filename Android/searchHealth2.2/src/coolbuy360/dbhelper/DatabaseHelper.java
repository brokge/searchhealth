package coolbuy360.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME="";
	private static final int DATABASE_VERSION=1;
	private static final String NOTES_TABLE_NAME="";
	private static DatabaseHelper databaseHelper;
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);		
		// TODO Auto-generated constructor stub
		
	  
		
	}

	public static SQLiteDatabase getInstance(Context context,String dbname)
	{
		if(databaseHelper==null)
		{
			
			databaseHelper=new DatabaseHelper(context, dbname, null, 1);
		}		
		return databaseHelper.getReadableDatabase();		
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		//db.execSQL("CREATE TABLE"+NOTES_TABLE_NAME+)
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	
	
	
}
