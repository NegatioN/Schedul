package joakim.app.schedul;

import joakim.app.data.Appointment;
import joakim.app.data.MySQLHelper;
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
	//current context and appointment
	private static Context context;
	private MySQLHelper db;
	
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
    		a = intent.getParcelableExtra("appointment");
    		appTitle = a.getDescription();
    		time = a.getTime().hour + ":" + a.getTime().minute;
    	
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
    	
    	
    	db = new MySQLHelper(context);
    	db.deleteAppointment(a);
//    	callNextAlarm(a);
    }
    
	public void setAlarm(Context context, Appointment app) {
		this.context = context;
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, AlarmService.class);
		
		//add the appointment so we can reference the information in our notification.
		i.putExtra("appointment", app);
		
		PendingIntent pi = PendingIntent.getService(context, 0, i, 0);

		am.set(AlarmManager.RTC_WAKEUP, app.getTime().toMillis(false), pi);
		
		Toast.makeText(context, "ALARM SET to: " + app.getTime().hour, Toast.LENGTH_SHORT).show();
		
	}
    
	public void cancelAlarm(Context context) {
		Intent intent = new Intent(context, Alarm.class);
		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}
	
	private void callNextAlarm(Appointment a){
    	Schedul master = (Schedul) this.context;
    	//we delete the appointment from the database
//    	master.removeExpiredAppointment(a);
    	
    	//we set the next alarm when the notification is sent.
    	Time t = new Time();
    	t.setToNow();
    	Appointment nextAppointment = master.findMostRecentAppointment();
    	setAlarm(this.context, nextAppointment);
	}

	private void setInterval(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		int intervalMinutes = Integer.parseInt(preferences.getString("list_preference", "-1"));
//		interval = intervalMinutes;
	}
    
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
