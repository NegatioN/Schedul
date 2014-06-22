package joakim.app.schedul;

import joakim.app.data.Appointment;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

public class AlarmService extends IntentService{

	private int counter = 0;
	private int interval = -1;
	private final int HOUR = 60;
	
	public AlarmService() {
		super("AlarmService");
	}

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("AlarmService", "Received start id " + startId + ": " + intent);
        onHandleIntent(intent);
            return START_STICKY;
    }

    protected void onHandleIntent(Intent intent){
    	
    	Appointment a;
    	String appTitle = "";
    	String time = "";
    	if(intent.hasExtra("appointment")){
    		a = intent.getParcelableExtra("appointment");
    		appTitle = a.getDescription();
    		time = a.getTime().hour + ":" + a.getTime().minute;
    	}
    	
    	Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    	
    	NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    	Intent i = new Intent(this.getApplicationContext(), Schedul.class);
    	PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
    	
    	Notification.Builder mNotify = new Notification.Builder(this)
    	.setContentTitle("ALARM")
    	.setContentText(appTitle + " - " + time)
    	.setSmallIcon(R.drawable.ic_launcher)
    	.setContentIntent(pi)
    	.setSound(sound)
    	.setVibrate(new long[] { 1000, 1000 });
//    	.addAction(0, "Exit?", pi);
    	
    	
    	final Notification notify = mNotify.getNotification();
    	
    	mNM.notify(1, notify);
    }
    
	public void setAlarm(Context context, Appointment app) {
				
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, AlarmService.class);
		
		//add the appointment so we can reference the information in our notification.
		i.putExtra("appointment", app);

//		//find user setting for intervals
		setInterval(context);
//		
		PendingIntent pi = PendingIntent.getService(context, 0, i, 0);

		
		//set repeating or not based on user settings
		//if -1, we should only have one alarm for the appointment at the designated time.
			am.set(AlarmManager.RTC_WAKEUP, app.getTime().toMillis(false), pi);

//		am.setRepeating(AlarmManager.RTC_WAKEUP, time.toMillis(false),
//				1000 * 60 * interval, pi); // Millisec * Second * Minute
		
		counter++;
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
    
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
