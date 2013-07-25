package com.example.offcalltime;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.CallLog.Calls;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
	private static final String TAG = "ffffffffffffffff";
	private Context mContext;
	private static long _begin= 0L;
	private long _end= 0L;
	static final String[] CALL_LOG_PROJECTION = new String[] { Calls._ID, // 0
			Calls.NUMBER, // 1
			Calls.DATE, // 2
			Calls.DURATION, // 3
			Calls.TYPE, // 4
			Calls.CACHED_NAME };

	
	/**
	 * 电话状态（phone_state)分为： 
          TelephonyManager.CALL_STATE_RINGING ， 响铃
          TelephonyManager.CALL_STATE_OFFHOOK ，摘机占线
          TelephonyManager.CALL_STATE_IDLE ，挂机待机

	 * 通话过程包含的广播Action
         "android.intent.action.NEW_OUTGOING_CALL" 去电广播
         "android.intent.action.PHONE_STATE"  可以理解为电话状态发生改变，从intent中可以获取到当前的电话状态。
	 */
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		mContext = context;
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
		if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
			
			_begin= System.currentTimeMillis();
			Log.e(TAG, "begin :" + System.currentTimeMillis());
		} else 
			if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
				
			Long begin = System.currentTimeMillis();
			int state = tm.getCallState();
			switch (state) {
			
			case TelephonyManager.CALL_STATE_RINGING:
				_begin= begin;
				Log.e(TAG, "ringing begin :" + begin);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				Log.e(TAG, "OFFHOOK begin :" + begin);
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				_end= begin;
				Log.e(TAG, "IDLE end :" + begin);
				break;
			default:
				break;
			}
			if (state == TelephonyManager.CALL_STATE_IDLE) {
				
				try {
					
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				// 取最后一条通话纪录
				Cursor c = mContext.getContentResolver().query(Calls.CONTENT_URI, CALL_LOG_PROJECTION, null, null, "_id asc LIMIT -1 OFFSET (select count(_id) from calls )-1");
				
				String num= "";
				if (c.moveToFirst()) {
					num= c.getString(c.getColumnIndex(Calls.NUMBER));
					Log.e(TAG,
							" _ID "+ c.getLong(c.getColumnIndex(Calls._ID))
							+ " NUMBER "+ num
							+ " DATE "+ c.getLong(c.getColumnIndex(Calls.DATE))
							+ " DURATION "+ c.getInt(c.getColumnIndex(Calls.DURATION))
							+ " TYPE "+ c.getInt(c.getColumnIndex(Calls.TYPE))
							+ " CACHED_NAME "+ c.getString(c.getColumnIndex(Calls.CACHED_NAME))
							+"\n"
							+_end+ "\n"+ _begin+"\n"
							+"响铃时间为"+ (float)((_end-_begin)/1000)+ "s"
							);
				}
				Toast.makeText(context, "====000", Toast.LENGTH_SHORT).show();
				
				NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
				Notification n = new Notification(R.drawable.ic_launcher, "Hello,there!", System.currentTimeMillis());
				n.flags = Notification.FLAG_AUTO_CANCEL;
				Intent i = new Intent();
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK); 
				//PendingIntent
				PendingIntent contentIntent = PendingIntent.getActivity(context, R.string.app_name, i, PendingIntent.FLAG_UPDATE_CURRENT);
				n.setLatestEventInfo(context, "未接来电", "号码："+num+ "\n时间是:"+ (float)((_end-_begin)/1000)+ "s", contentIntent);
				nm.notify(R.string.app_name, n);
			}
		}
	}
}
