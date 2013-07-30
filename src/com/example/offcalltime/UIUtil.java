package com.example.offcalltime;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;

public class UIUtil {

	
	private Context context;
	
	public UIUtil(Context context) {
		
		this.context= context;
	}
	
	public void updateNotifacation(final Looper looper) {
		
		new Thread() {
			
			public void run() {
				
//				Handler handler= new Handler(looper);
			};
		}.start();
	}
	
	public void Notification(final int callType, final String wait_time, final String real_time, final String num, final String name, final String date) {
		
		int type= getCallType(callType);
		NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification n = new Notification(R.drawable.ic_launcher, context.getText(R.string.new_callMSG), System.currentTimeMillis());
		n.flags = Notification.FLAG_AUTO_CANCEL;
		Intent i = new Intent();
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK); 
		//PendingIntent
		PendingIntent contentIntent = PendingIntent.getActivity(context, R.string.app_name, i, PendingIntent.FLAG_UPDATE_CURRENT);
		
		String notification_msg= ""+ getString(R.string.title);
		switch(callType) {
		
		case CallType.MISS_CALL:
			
			if(name== null|| "".equals(name)) {
				
				notification_msg+= getString(R.string.MISS_CALL)+ getString(R.string.name)+ num+ getString(R.string.waitTime)+ wait_time+ getString(R.string.date)+ date;
			} else {
				
				notification_msg+= getString(R.string.MISS_CALL)+ getString(R.string.name)+ name+ getString(R.string.waitTime)+ wait_time+ getString(R.string.date)+ date;
			}
			
			break;
		case CallType.OUT_CALL:
			
			notification_msg+= getString(R.string.OUT_CALL);
			break;
		case CallType.USEFULL_CALL:
	
			notification_msg+= getString(R.string.USEFULL_CALL);
			break;
		}
		
		n.setLatestEventInfo(context, context.getText(type), notification_msg, contentIntent);
		nm.notify(R.string.app_name, n);
		MySQLiteDatabase db= new MySQLiteDatabase(context, "xulong_db", null, 1);
		Log.e("09090（）9090", notification_msg);
	}
	
	public String getString(final int num) {
		
		return context.getText(num)+ "";
	}
	
	private int getCallType(final int type) {
		
		int call_type= 0;
		switch (type) {

		case CallType.USEFULL_CALL:

			call_type= CallType.USEFULL_CALL_str;
			break;
		case CallType.MISS_CALL:

			call_type= CallType.MISS_CALL_str;
			break;
		case CallType.OUT_CALL:

			call_type= CallType.OUT_CALL_str;
			break;
		}
		return call_type;
	}
}
