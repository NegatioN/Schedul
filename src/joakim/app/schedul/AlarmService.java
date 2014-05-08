package joakim.app.schedul;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

public class AlarmService extends IntentService{

	public AlarmService(){
		super("AlarmService");
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		sendBroadcast(intent);
	}

}
