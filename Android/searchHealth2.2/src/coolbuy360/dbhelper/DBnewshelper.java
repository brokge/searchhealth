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

	// 返回文章详情，包含[ArticleID],[Author],[Detail]键值。
	// 返回健康资讯栏目下的文章列表，包含[ArticleID],[Title],[Image],[BigImage],[Resume],[OrderNo],[UpdateTime]键值。

	/**
	 * @param mapValue
	 *            ：数据集合，参数类型：{@link Map}
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
	 *            ：数据集合，参数类型：{@link List}
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
	 * 更新数据库表的方法
	 * 
	 * @param mapValue
	 *            ：数据集合，参数类型：{@link Map}
	 * @param objhandle
	 *            :操作类型：1.list;2.detail
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
	 * 通过id条件获取值
	 * 
	 * @param id
	 *            ：参数类型为int ,所对应的字段是ArticleID
	 * @return 返回的是一个Map<String,String>
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
	 * 获取list集合通过条件
	 * @param articleType 资讯类型，包含column，dissertation
	 * @param parentID 父栏目或专题ID
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param starttime	翻页起始时间戳，若为空字符，则取第一页数据
	 * @return
	 */
	public List<Map<String, String>> GetList(String articleType, String parentID, int pageSize, String starttime) {
		// //Cursor是游标类 游标在数据库中其实就是一个数据集
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
	 * 获取list集合通过条件
	 * @param articleType 资讯类型，包含column，dissertation
	 * @param parentID 父栏目或专题ID
	 * @param pageSize 每页条数，0表示按系统默认值
	 * @param starttime	翻页起始时间戳，若为空字符，则取第一页数据
	 * @return
	 */
	public String GetLastNewsTimestamp(String articleType, String parentID) {
		// //Cursor是游标类 游标在数据库中其实就是一个数据集
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
	 * Delete方法
	 * 
	 * @param ids
	 *            :Integer[]数组，
	 */
	public void Delete(Integer[] ids) {
		this.open(mcontext, abstractDatabaseHelper.WriteAble);
		if (ids.length > 0) {
			StringBuilder sb = new StringBuilder();
			for (Integer id : ids) {
				sb.append('?').append(',');
			}
			// 删除最后一个字符
			sb.deleteCharAt(sb.length() - 1);

			// execSQL是执行sql语句
			mDb.execSQL("delete from " + TableName + " where ArticleID in("
					+ sb + ")", (Object[]) ids);
		}
		this.close();
	}

	/***
	 * Delete方法
	 * 
	 * @param strWhere
	 *            :删除的条件语句 可以为这个样的格式id=1 and name='hangsan'
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
	 *            开始的索引
	 * @param maxResult
	 *            每页数量
	 * @param orderColumn
	 *            排序依据
	 * @return 返回类型为：{@link List} 集合，里面的对象为： {@link Map}
	 */
	public List<Map<String, String>> GetListByRange(int startResult,
			int maxResult, String orderColumn) {
		this.open(mcontext, abstractDatabaseHelper.ReadAble);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		// Cursor是游标类 游标在数据库中其实就是一个数据集
		// rawQuery(String sql,String[]s) 参数一 是一个sql语句
		// 参数二是参数一sql语句中条件的占位符所存的具体值，这些值是一个字符string数组
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
	 *            开始的索引
	 * @param maxResult
	 *            每页数量
	 * @param orderColumn
	 *            排序依据
	 * @return 返回类型为：{@link List} 集合，里面的对象为： {@link Map}
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
	 * 获取总数据数
	 * 
	 * @return 返回long类型
	 */
	public long getcount() {
		this.open(mcontext, abstractDatabaseHelper.ReadAble);
		// Cursor是游标类 游标在数据库中其实就是一个数据集
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
