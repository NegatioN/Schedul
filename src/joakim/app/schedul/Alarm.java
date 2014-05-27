package joakim.app.schedul;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

public class Alarm extends BroadcastReceiver {

	private int counter = 0;
	private int interval = -1;
	private final int HOUR = 60;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, "");
		wl.acquire();

		//TODO IN BROADCAST
		if(counter > interval/HOUR){
			cancelAlarm(context);
			wl.release();
			return;
		}
		
		Log.d("AlarmFragment", "Runde:" + counter);
//		showFragment(this.context);
//		Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); 
		counter++;
		wl.release();
	}

	public void setAlarm(Context context, Time time) {
		
		
		
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, AlarmService.class);
//		
//		//find user setting for intervals
		setInterval(context);
//		
		PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
		
		//set repeating or not based on user settings
		//if -1, we should only have one alarm for the appointment at the designated time.
		if(interval == -1){
			am.set(AlarmManager.RTC_WAKEUP, time.toMillis(false) + AlarmManager.INTERVAL_HOUR, pi);
		}
		else
		am.setRepeating(AlarmManager.RTC_WAKEUP, time.toMillis(false),
				1000 * 60 * interval, pi); // Millisec * Second * Minute
		Toast.makeText(context, "Alarm set " + time.hour + ":" + time.minute, Toast.LENGTH_SHORT).show();
	}

	public void cancelAlarm(Context context) {
		Intent intent = new Intent(context, Alarm.class);
		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}
	
	private void setInterval(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		int intervalMinutes = Integer.parseInt(preferences.getString("list_preference", "-1"));
		interval = intervalMinutes;
	}
	private void showFragment(Context c) {
//		DummyAlarmActivity daa = new DummyAlarmActivity();
//		FragmentManager fm =  daa.getFragmentManager();
//		AlarmDialog alarmDialog = AlarmDialog.newInstance(daa, "ALARM");
//		alarmDialog.show(fm, "fragment_alarm");
//		Intent i = new Intent(c, DummyAlarmActivity.class);
//		((Activity)c).startActivity(i);
	}

}
