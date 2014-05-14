package joakim.app.data;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;


//class should save any object we need in sharedPreferences. We won't be making a database since we assume we'll only have to save around 70 objects max a week.
//Change to SQLite database at a later date?
public class ObjectSaver extends Service{

	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	
	
	
	
	public static void saveAppointment(Appointment a){
		
	}

	public void onCreate(){
		sp = getApplicationContext().getSharedPreferences("appointments", 0);
		editor = sp.edit();
		
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
