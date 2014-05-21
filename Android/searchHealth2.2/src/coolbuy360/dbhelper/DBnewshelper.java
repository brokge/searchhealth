package coolbuy360.dbhelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import coolbuy360.logic.Article;

import android.R.integer;
import android.content.Context;
import android.database.Cursor;

public class DBnewshelper extends abstractDatabaseHelper {

	private String TableName = "news";
	private String DatabaseName = "searchhealthdb.db";
	private int DatabaseVersion = 1;
	private Context mcontext;
	
	public DBnewshelper(Context context) {		
		mcontext = context;
	}
	
	@Override
	protected String getTag() {
		return TableName;
	}

	@Override
	protected String getDatabaseName1() {
		// TODO Auto-generated method stub
		return DatabaseName;
	}

	@Override
	protected int getDatabaseVersion() {
		// TODO Auto-generated method stub
		return DatabaseVersion;
	}

	@Override
	protected String[] createDBTables() {
		// TODO Auto-generated method stub
		String[] stringParStrings = { "CREATE TABLE "
				+ TableName
				// +
				// "(NewsID integer primary key autoincrement,ArticleID integer, Title varchar(50), Image TEXT,BigImage TEXT,Author nvarchar(50),Resume TEXT,Detail TEXT,CreateTime DATETIME,UpdateTime DATETIME)"
				// };
				+ "(NewsID integer primary key autoincrement,ArticleID integer, Title varchar(50), Image TEXT,BigImage TEXT,Author nvarchar(50),Resume TEXT,Detail TEXT,OrderNo integer,CreateTime DATETIME,UpdateTime DATETIME,ArticleType NVARCHAR(30),ParentID integer)" };
		return stringParStrings;
	}

	@Override
	protected String[] dropDBTables() {
		// TODO Auto-generated method stub
		String[] dropStrings = { "DROP TABLE IF EXISTS " + TableName };
		return dropStrings;
	}

	// �����������飬����[ArticleID],[Author],[Detail]��ֵ��
	// ���ؽ�����Ѷ��Ŀ�µ������б�����[ArticleID],[Title],[Image],[BigImage],[Resume],[OrderNo],[UpdateTime]��ֵ��

	/**
	 * @param mapValue
	 *            �����ݼ��ϣ��������ͣ�{@link Map}
	 *            <p>
	 *            ArticleID,Title,Image,BigImage,Resume,OrderNo,UpdateTime
	 */
	public void Insert(Map<String, String> mapValue, String articleType,
			String parentID) {
		this.open(mcontext, abstractDatabaseHelper.WriteAble);
		String ArticleID = mapValue.get("ArticleID");
		String Title = mapValue.get("Title");
		// String Author = mapValue.get("Author");
		String Author = "";
		String Image = mapValue.get("Image");
		String BigImage = mapValue.get("BigImage");
		String Resume = mapValue.get("Resume");
		// String Detail = mapValue.get("Detail");
		String Detail = "";
		String OrderNo = mapValue.get("OrderNo");
		// String CreateTime = mapValue.get("CreateTime");
		String CreateTime = "";
		String UpdateTime = mapValue.get("UpdateTime");
		String ArticleType = articleType;
		String ParentID = parentID;
		// database.execSQL("insert into person(name,age) values(?,?)",new
		// Object[]{person.getName(),person.getAge()});
		String insertSqlString = "insert into "
				+ TableName
				+ "(ArticleID,Title,Author,Image,BigImage,Resume,Detail,OrderNo,CreateTime,UpdateTime,ArticleType,ParentID)values('"
				+ ArticleID + "','" + Title + "','" + Author + "','" + Image
				+ "','" + BigImage + "','" + Resume + "','" + Detail + "','"
				+ OrderNo + "',NULL,'" + UpdateTime + "','"
				+ ArticleType + "','" + ParentID + "')";

		mDb.execSQL(insertSqlString);
		this.close();
	}	

	/**
	 * @param list
	 *            �����ݼ��ϣ��������ͣ�{@link List}
	 *            <p>
	 *            ArticleID,Title,Image,BigImage,Resume,OrderNo,UpdateTime
	 */
	public void Insert(List<Map<String, String>> list, String articleType,
			String parentID) {
		this.open(mcontext, abstractDatabaseHelper.WriteAble);
		for (Map<String, String> mapValue : list) {
			String ArticleID = mapValue.get("ArticleID");
			String Title = mapValue.get("Title");
			// String Author = mapValue.get("Author");
			String Author = "";
			String Image = mapValue.get("Image");
			String BigImage = mapValue.get("BigImage");
			String Resume = mapValue.get("Resume");
			// String Detail = mapValue.get("Detail");
			String Detail = "";
			String OrderNo = mapValue.get("OrderNo");
			// String CreateTime = mapValue.get("CreateTime");
			String CreateTime = "";
			String UpdateTime = mapValue.get("UpdateTime");
			String ArticleType = articleType;
			String ParentID = parentID;
			// database.execSQL("insert into person(name,age) values(?,?)",new
			// Object[]{person.getName(),person.getAge()});
			String insertSqlString = "insert into "
					+ TableName
					+ "(ArticleID,Title,Author,Image,BigImage,Resume,Detail,OrderNo,CreateTime,UpdateTime,ArticleType,ParentID)values('"
					+ ArticleID + "','" + Title + "','" + Author + "','" + Image
					+ "','" + BigImage + "','" + Resume + "','" + Detail + "','"
					+ OrderNo + "',NULL,'" + UpdateTime + "','"
					+ ArticleType + "','" + ParentID + "')";

			mDb.execSQL(insertSqlString);
		}
		this.close();
	}

	/**
	 * �������ݿ��ķ���
	 * 
	 * @param mapValue
	 *            �����ݼ��ϣ��������ͣ�{@link Map}
	 * @param objhandle
	 *            :�������ͣ�1.list;2.detail
	 */
	public void update(Map<String, String> mapValue, String objhandle) {
		this.open(mcontext, abstractDatabaseHelper.WriteAble);
		String ArticleID = mapValue.get("ArticleID");
		if (objhandle.equals("list")) {
			String BigImage = mapValue.get("BigImage");
			String UpdateTime = mapValue.get("UpdateTime");
			String Title = mapValue.get("Title");
			String Image = mapValue.get("Image");
			String Resume = mapValue.get("Resume");
			mDb.execSQL(
					"update "
							+ TableName
							+ " set Title=?,Image=?,Resume=?,BigImage=?,UpdateTime=? where ArticleID=?",
					new String[] { String.valueOf(Title),
							String.valueOf(Image), String.valueOf(Resume),
							String.valueOf(BigImage),
							String.valueOf(UpdateTime),
							String.valueOf(ArticleID) });

		} else if (objhandle.equals("detail")) {
			String Author = mapValue.get("Author");
			String Detail = mapValue.get("Detail");
			mDb.execSQL(
					"update " + TableName
							+ " set Author=?,Detail=? where ArticleID=?",
					new String[] { String.valueOf(Author),
							String.valueOf(Detail), String.valueOf(ArticleID) });
		}
		this.close();
	}

	/**
	 * ͨ��id������ȡֵ
	 * 
	 * @param id
	 *            ����������Ϊint ,����Ӧ���ֶ���ArticleID
	 * @return ���ص���һ��Map<String,String>
	 */
	public Map<String, String> FindByID(int id) {
		this.open(mcontext, abstractDatabaseHelper.ReadAble);
		Map<String, String> map = new HashMap<String, String>();
		Cursor cursor = mDb.rawQuery("select * from " + TableName
				+ " where ArticleID=?", new String[] { String.valueOf(id) });
		if (cursor != null && cursor.getCount() > 0) {
			int j = 0;
			while (cursor.moveToPosition(j) && j < 1) {
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					map.put(cursor.getColumnName(i), cursor.getString(i));
				}
				j++;
			}
			cursor.close();
			this.close();
			return map;
		} else {
			cursor.close();
			this.close();
			return null;
		}
	}

	/**
	 * ��ȡlist����ͨ������
	 * @param articleType ��Ѷ���ͣ�����column��dissertation
	 * @param parentID ����Ŀ��ר��ID
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param starttime	��ҳ��ʼʱ�������Ϊ���ַ�����ȡ��һҳ����
	 * @return
	 */
	public List<Map<String, String>> GetList(String articleType, String parentID, int pageSize, String starttime) {
		// //Cursor���α��� �α������ݿ�����ʵ����һ�����ݼ�
		this.open(mcontext, abstractDatabaseHelper.ReadAble);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sqlString = "";
		if (starttime == null || starttime.equals("")) {
			sqlString = "select * from " + TableName + " where ArticleType='"
					+ articleType + "' and ParentID=" + parentID
					+ " order by OrderNo Desc,UpdateTime Desc Limit "
					+ pageSize;
		} else {
			sqlString = "select * from " + TableName + " where UpdateTime<'"
					+ starttime + "' and OrderNo=0 and ArticleType='"
					+ articleType + "' and ParentID=" + parentID
					+ " order by UpdateTime Desc Limit " + pageSize;
		}
		Cursor cursor = mDb.rawQuery(sqlString, null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					map.put(cursor.getColumnName(i), cursor.getString(i));
				}
				list.add(map);
			}
		}
		cursor.close();
		this.close();
		return list;
	}

	/**
	 * ��ȡlist����ͨ������
	 * @param articleType ��Ѷ���ͣ�����column��dissertation
	 * @param parentID ����Ŀ��ר��ID
	 * @param pageSize ÿҳ������0��ʾ��ϵͳĬ��ֵ
	 * @param starttime	��ҳ��ʼʱ�������Ϊ���ַ�����ȡ��һҳ����
	 * @return
	 */
	public String GetLastNewsTimestamp(String articleType, String parentID) {
		// //Cursor���α��� �α������ݿ�����ʵ����һ�����ݼ�
		this.open(mcontext, abstractDatabaseHelper.ReadAble);
		String value = null;
		String sqlString = "";
		sqlString = "select UpdateTime from " + TableName
				+ " where ArticleType='" + articleType + "' and ParentID="
				+ parentID + " order by UpdateTime Desc Limit 1";
		Cursor cursor = mDb.rawQuery(sqlString, null);		
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					value = cursor.getString(0);
				}
			}			
		}
		cursor.close();
		this.close();
		return value;
	}

	/**
	 * Delete����
	 * 
	 * @param ids
	 *            :Integer[]���飬
	 */
	public void Delete(Integer[] ids) {
		this.open(mcontext, abstractDatabaseHelper.WriteAble);
		if (ids.length > 0) {
			StringBuilder sb = new StringBuilder();
			for (Integer id : ids) {
				sb.append('?').append(',');
			}
			// ɾ�����һ���ַ�
			sb.deleteCharAt(sb.length() - 1);

			// execSQL��ִ��sql���
			mDb.execSQL("delete from " + TableName + " where ArticleID in("
					+ sb + ")", (Object[]) ids);
		}
		this.close();
	}

	/***
	 * Delete����
	 * 
	 * @param strWhere
	 *            :ɾ����������� ����Ϊ������ĸ�ʽid=1 and name='hangsan'
	 */
	public void Delete(String strWhere) {
		this.open(mcontext, abstractDatabaseHelper.WriteAble);
		
		
		if (!strWhere.equals(""))
		{
			String sqlString="delete from " + TableName + " where "+strWhere;			
			mDb.execSQL(sqlString);
		}
		this.close();
	}

	/**
	 * 
	 * @param startResult
	 *            ��ʼ������
	 * @param maxResult
	 *            ÿҳ����
	 * @param orderColumn
	 *            ��������
	 * @return ��������Ϊ��{@link List} ���ϣ�����Ķ���Ϊ�� {@link Map}
	 */
	public List<Map<String, String>> GetListByRange(int startResult,
			int maxResult, String orderColumn) {
		this.open(mcontext, abstractDatabaseHelper.ReadAble);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		// Cursor���α��� �α������ݿ�����ʵ����һ�����ݼ�
		// rawQuery(String sql,String[]s) ����һ ��һ��sql���
		// �������ǲ���һsql�����������ռλ������ľ���ֵ����Щֵ��һ���ַ�string����
		Cursor cursor = mDb
				.rawQuery(
						"select * from " + TableName + " " + orderColumn
								+ " limit ?,?",
						new String[] { String.valueOf(startResult),
								String.valueOf(maxResult) });
		while (cursor.moveToNext()) {
			map = new HashMap<String, String>();
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				map.put(cursor.getColumnName(i), cursor.getString(i));
			}
			list.add(map);
		}
		cursor.close();
		this.close();
		return list;

	}

	/**
	 * 
	 * @param startResult
	 *            ��ʼ������
	 * @param maxResult
	 *            ÿҳ����
	 * @param orderColumn
	 *            ��������
	 * @return ��������Ϊ��{@link List} ���ϣ�����Ķ���Ϊ�� {@link Map}
	 */
	public Cursor getdateRawRange(int startResult, int maxResult,
			String orderColumn) {
		this.open(mcontext, abstractDatabaseHelper.ReadAble);
		Cursor cursor = mDb.rawQuery("select * from " + TableName + " " + orderColumn
				+ " limit ?,?", new String[] { String.valueOf(startResult),
				String.valueOf(maxResult) });		
		this.close();
		return cursor;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ����long����
	 */
	public long getcount() {
		this.open(mcontext, abstractDatabaseHelper.ReadAble);
		// Cursor���α��� �α������ݿ�����ʵ����һ�����ݼ�
		Cursor cursor = mDb.rawQuery("select count(*) from " + TableName, null);
		if (cursor.moveToLast()) {
			long count = cursor.getLong(0);
			this.close();
			return count;
		}
		cursor.close();
		this.close();
		return 0;
	}	
}
