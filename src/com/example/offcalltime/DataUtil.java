package com.example.offcalltime;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog.Calls;

public class DataUtil {
	
	
	/**
	 * 获取该条电话类型(未接电话,已接电话,拨出电话)
	 * @param newCall_time
	 * @param ringing_time
	 * @param offhook_time
	 * @param idle_time
	 * @return
	 */
	public static int getCallType(final long newCall_time, final long ringing_time, final long offhook_time, final long idle_time) {
		
		if(offhook_time== 0&& newCall_time== 0) {
			
			return CallType.MISS_CALL;
		}else{
			
			if(newCall_time!= 0) {
				
				return CallType.OUT_CALL;
			}
			return CallType.USEFULL_CALL;
		}
	}
	
	/**
	 * 提供该条电话记录的时间
	 * return "占机时间"+ "*"+ "有效时间"
	 */
	public static String getCallTime(final long newCall_time, final long ringing_time, final long offhook_time, final long idle_time) {
		
		//拨出电话并且已经有实际接听时间
		if(newCall_time!= 0&& offhook_time!= 0) {
			
			String content= (offhook_time- ringing_time)+ "*"+ (idle_time- offhook_time);
			return content;
		} 
		//拨出电话但是没有接通
		if(newCall_time!= 0&& offhook_time==0) {
			
			String content= (offhook_time- ringing_time)+ "*"+ " ";
			return content;
		}
		//未接电话
		if(newCall_time== 0&& offhook_time==0) {
			
			String content= (idle_time- ringing_time)+ "*"+ " ";
			return content;
		}
		//已接电话
		if(newCall_time== 0&& offhook_time==0) {
			
			String content= (offhook_time- ringing_time)+ "*"+ (idle_time- offhook_time);
			return content;
		}
			
		return null;
	}
	
	public static Cursor getMobileInfo(Context mContext) {
		
		final String[] CALL_LOG_PROJECTION = new String[] { Calls._ID, // 0
			Calls.NUMBER, // 1
			Calls.DATE, // 2
			Calls.DURATION, // 3
			Calls.TYPE, // 4
			Calls.CACHED_NAME };
		Cursor cursor= null;
		
		// 取最后一条通话纪录
		cursor = mContext.getContentResolver().query(Calls.CONTENT_URI, CALL_LOG_PROJECTION, null, null, "_id asc LIMIT -1 OFFSET (select count(_id) from calls )-1");
		return cursor;
	}

}
