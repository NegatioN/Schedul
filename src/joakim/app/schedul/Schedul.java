package joakim.app.schedul;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import joakim.app.GUI.AlarmCountdown;
import joakim.app.data.Appointment;
import joakim.app.data.AppointmentComparator;
import joakim.app.data.ColorHandler;
import joakim.app.data.MySQLHelper;
import android.app.Activity;
import android.app.AlarmManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Schedul extends Activity {

	private TextView				displayAppointment, countDown;
	private Runnable				tvUpdater;
	private Handler					tvHandler;
	private AlarmCountdown alarmCountDown;
	private MySQLHelper db = new MySQLHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedul);
		
		displayAppointment = (TextView) findViewById(R.id.tvDisplayAppointment);
		countDown = (TextView) findViewById(R.id.tvCountDown);

		tvHandler = new Handler();
		tvUpdater = new Runnable() {

			@Override
			public void run() {
				Log.d("schedul.run", "ping");
				Appointment appointment = findMostRecentAppointment();
				if (appointment != null) {
//					removeExpiredAppointment(appointment);
					int alphaColor = ColorHandler.convertColorAlpha(appointment.getPriority());
					displayAppointment.setBackgroundColor(alphaColor);
					displayAppointment.setText(appointment.getSummary());
//					setAlarmFragment(appointment);
					
				}
			}
		};

	}
	
	protected void onStart(){
		super.onStart();
		//updates our displayed appointment on start-screen.
		tvHandler.post(tvUpdater);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mainmenu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.action_add:
			intent = new Intent(this, AddTodo.class);
			startActivity(intent);
			break;
		case R.id.action_settings:
			intent = new Intent(this, UserSettings.class);
			startActivity(intent);
			break;
		}

		return true;
	}

	public Appointment findMostRecentAppointment(){
		return db.getClosestAppointment();
	}

	// makes an alarm and will remove the previous one if it has not triggered
// (last part not implemented yet)
//	public boolean setAlarmFragment(Appointment app) {
//		if (app != null) {
//			alarm = new AlarmService();
//
//			alarm.setAlarm(this, app);
//
//			return true;
//		}
//
//		return false;
//	}


	//method will not be needed later because alarm will call removeappointment after it's run.
//	private boolean appointmentExpired(Appointment a) {
//		Time time = new Time();
//		time.setToNow();
//		Log.d("removeLog", "First: " + a.getTime().toMillis(false) + "\n Second: " + time.toMillis(false));
//		if (a.getTime().toMillis(false) - time.toMillis(false) < 0) {
//			return true;
//		}
//
//		return false;
//	}
//	
//
//	public void removeExpiredAppointment(Appointment a) {
//		Log.d("removeLog", "vi er i metoden, appointment ikke null");
//		if(appointmentExpired(a)){
//			try {
//				Log.d("removeLog", "appointment == expired");
//				ArrayList<Appointment>al = findDayArray(a.getTime());
//				al.remove(a);
//				//we create a new object 7 days in the future after current appointment and add it to the list.
//				if(a.isPersistent()){
//					
////					al.add(TimeHandler.makeNew(a));
//				}
//				db.deleteAppointment(a);
//					
//					
//			} catch (NullPointerException np) {
//				return;
//			}
//	}
//
//	}
	
	// TEST-PROGRAM-METHODS
	
	
	private void parcelableLog(ArrayList<Appointment> appointments){
		for(Appointment a : appointments)
			Log.d("parcelableLog", a.getTime().toString());
	}

}
