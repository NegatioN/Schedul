package joakim.app.schedul;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class AlarmService extends Service{

	public AlarmService(){

	}
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
            return START_NOT_STICKY;
    }

    public void onCreate(){
    	Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    	
    	NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    	Intent intent = new Intent(this.getApplicationContext(), Schedul.class);
    	PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
    	
    	Notification.Builder mNotify = new Notification.Builder(this)
    	.setContentTitle("ALARM")
    	.setContentText("THIS IS AN ALARM")
    	.setSmallIcon(R.drawable.ic_launcher)
    	.setContentIntent(pi)
    	.setSound(sound);
//    	.addAction(0, "Exit?", pi);
    	
    	final Notification notify = mNotify.getNotification();
    	
    	mNM.notify(1, notify);
    	
    	
    }
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
