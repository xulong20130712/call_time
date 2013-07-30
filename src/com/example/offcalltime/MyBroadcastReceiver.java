package com.example.offcalltime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.CallLog.Calls;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {
	private static final String TAG = "ffffffffffffffff";
	private Context mContext;
	private long newCall_time = 0L;
	private long ringing_time = 0L;
	private long offhook_time = 0L;
	private long idle_time = 0L;

	/**
	 * 电话状态（phone_state)分为： TelephonyManager.CALL_STATE_RINGING ， 响铃
	 * TelephonyManager.CALL_STATE_OFFHOOK ，摘机占线
	 * TelephonyManager.CALL_STATE_IDLE ，挂机待机
	 * 
	 * 通话过程包含的广播Action "android.intent.action.NEW_OUTGOING_CALL" 去电广播
	 * "android.intent.action.PHONE_STATE" 可以理解为电话状态发生改变，从intent中可以获取到当前的电话状态。
	 */

	@Override
	public void onReceive(Context context, Intent intent) {

		mContext = context;
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
		if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {

			newCall_time = System.currentTimeMillis();
			Log.e(TAG, "begin :" + System.currentTimeMillis());
		} else if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {

			Long begin = System.currentTimeMillis();
			int state = tm.getCallState();
			switch (state) {

			case TelephonyManager.CALL_STATE_RINGING:
				ringing_time = begin;
				Log.e(TAG, "ringing begin :" + begin);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				Log.e(TAG, "OFFHOOK begin :" + begin);
				offhook_time = begin;
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				idle_time = begin;
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
				Cursor c = DataUtil.getMobileInfo(mContext);

				String num = "";
				String name = "";//从通讯录中获取对应的联系人
				String date = "";
				String wait_time = "";
				String real_time = "";

				if (c.moveToFirst()) {
					num = c.getString(c.getColumnIndex(Calls.NUMBER));
					name= MySQLiteDatabase.getContactNameFromPhoneBook(mContext, num);
					date = c.getLong(c.getColumnIndex(Calls.DATE)) + "";

					String content = DataUtil.getCallTime(newCall_time,
							ringing_time, offhook_time, idle_time);
					Log.e("09090909090909", ""+ content);
					if (content.contains("*")) {

						wait_time = content.substring(0, content.indexOf("*"));
						real_time = content.substring(content.indexOf("*")+ 1);
					}

				}
				SimpleDateFormat simple= new SimpleDateFormat("yyyy-MM-dd-hh-mm");
				date= simple.format(new Date());
				Log.e("date:", date);
				int type = DataUtil.getCallType(newCall_time, ringing_time, offhook_time, idle_time);
				UIUtil util= new UIUtil(mContext);
				util.Notification(type, wait_time, real_time, num, name, date);
			}
		}
	}
}
