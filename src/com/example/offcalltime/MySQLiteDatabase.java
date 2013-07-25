package com.example.offcalltime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class MySQLiteDatabase extends SQLiteOpenHelper {

	
	private SQLiteDatabase db= null;
	private static final String DB_NAME= "xulong_MobileCallTable";
	
	public MySQLiteDatabase(Context context, String name,
			CursorFactory factory, int version) {

		super(context, name, factory, version);
		db= this.getWritableDatabase();
	}

	/**
	 * 
	 * 创建记录所有电话记录的表
	 * id:用于记录电话记录条数
	 * num:对方电话号码
	 * name:对方姓名(从联系人中获取)
	 * date:电话记录的日期(yyyy.MM.dd.HH.mm)
	 * wait_time:ringing---->offHook时间间隔等待时间，呼叫时间
	 * usefull_time:offHook---->IDLE时间间隔实际通话时间
	 * miss_call_time:ringing---->IDLE时间间隔，未接来电呼叫时间
	 * flag:电话消息类型(1:外拨电话,并且接通电话,2:外拨电话，没接通电话,3:来电，接通电话,4:未接来电)
	 * 
	 */

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("create table if not exists "+ DB_NAME+ " (id integer primary key autoincrement, num varchar(11), name varchar(20), date varchar(16), wait_time integer, usefull_time integer, miss_call_time integer, flag integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		if(oldVersion!= newVersion) {
			
			db.execSQL("DROP TABLE IF EXISTS "+ DB_NAME);  
	        onCreate(db); 
		}
	}
	
	/**
	 * 添加数据库记录
	 * @param num
	 * @param name
	 * @param date
	 * @param wait_time
	 * @param usefull_time
	 * @param miss_call_time
	 * @param flag
	 * @return
	 */
	public boolean insertData(final String num, final String name, final  String date, final int wait_time, final int usefull_time, final int miss_call_time, final int flag) {
		
		boolean insert_flag= false;
		ContentValues values= new ContentValues();
		values.put("num", num);
		values.put("name", name);
		values.put("date", date);
		values.put("wait_time", wait_time);
		values.put("usefull_time", usefull_time);
		values.put("miss_call_time", miss_call_time);
		values.put("flag", flag);
		long insert_flag_result= db.insert(DB_NAME, null, values);
		if(insert_flag_result!= 0) {
			
			insert_flag= false;
		}
		return insert_flag;
	}
	
	/**
	 * 查询通话记录
	 * @param num
	 * @param name
	 * @param date
	 * @return
	 */
	public Cursor queryData(final String num, final String name, final String date)  {
		
		Cursor cursor= null;
		
		db.query(DB_NAME, new String[]{"num", "name", "date", "wait_time", "usefull_time", "miss_call_time", "flag"}, "", "", "", "", "");
		return cursor;
	}
	
	
}