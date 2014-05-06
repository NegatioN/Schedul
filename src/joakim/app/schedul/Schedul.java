package joakim.app.schedul;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import joakim.app.GUI.AlarmCountdown;
import joakim.app.data.Appointment;
import joakim.app.data.AppointmentComparator;
import joakim.app.data.TimeHandler;
import android.app.Activity;
import android.app.AlarmManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Schedul extends Activity {

	private static final int		REQUEST_CODE	= 10;
	public static final String		RESULT_REQUEST	= "startForResult";

	// ArrayLists av lagret data, sendes til sub-context.
	private ArrayList<Appointment>	aMan			= new ArrayList<Appointment>();
	private ArrayList<Appointment>	aTir			= new ArrayList<Appointment>();
	private ArrayList<Appointment>	aOns			= new ArrayList<Appointment>();
	private ArrayList<Appointment>	aTor			= new ArrayList<Appointment>();
	private ArrayList<Appointment>	aFre			= new ArrayList<Appointment>();
	private ArrayList<Appointment>	aLør			= new ArrayList<Appointment>();
	private ArrayList<Appointment>	aSøn			= new ArrayList<Appointment>();
	private TextView				displayAppointment, countDown;
	private Runnable				tvUpdater;
	private Handler					tvHandler;
	private Alarm					alarm;
	private AlarmCountdown alarmCountDown;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedul);

// testFillArray(aMan);
// testFillArray(aOns);
// testFillArray(aTir);
//		testFillArray(aSøn);
// testFillArray(aLør);
// testFillArray(aTor);
// testFillArray(aFre);
		displayAppointment = (TextView) findViewById(R.id.tvDisplayAppointment);
		countDown = (TextView) findViewById(R.id.tvCountDown);

		tvHandler = new Handler();
		tvUpdater = new Runnable() {

			@Override
			public void run() {
				Time t = new Time();
				t.setToNow();

				Appointment appointment = findMostRecentAppointment(t);
				if (appointment != null) {
					removeExpiredAppointment(appointment);
					displayAppointment.setBackgroundColor(appointment
							.getPriority());
					displayAppointment.setText(appointment.getSummary());
					setAlarmFragment(appointment);
					
				}

			}

		};
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
			intent = sendArrayLists();
			startActivityForResult(intent, REQUEST_CODE);
			break;
		case R.id.action_settings:
			intent = new Intent(this, UserSettings.class);
			startActivity(intent);
			break;
		}

		return true;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			if (data.hasExtra("man")) {
				updateArrayLists(data);
				tvHandler.post(tvUpdater);
			}
		}
	}

	// sends arrays and starts AddTodo activity.
	private Intent sendArrayLists() {
		Intent result = new Intent(this, AddTodo.class);
		result.putExtra(RESULT_REQUEST, true);
		result.putParcelableArrayListExtra("man", aMan);
		result.putParcelableArrayListExtra("tir", aTir);
		result.putParcelableArrayListExtra("ons", aOns);
		result.putParcelableArrayListExtra("tor", aTor);
		result.putParcelableArrayListExtra("fre", aFre);
		result.putParcelableArrayListExtra("lør", aLør);
		result.putParcelableArrayListExtra("søn", aSøn);
		return result;

	}

	private void updateArrayLists(Intent data) {
		aMan = data.getParcelableArrayListExtra("man");
		aTir = data.getParcelableArrayListExtra("tir");
		aOns = data.getParcelableArrayListExtra("ons");
		aTor = data.getParcelableArrayListExtra("tor");
		aFre = data.getParcelableArrayListExtra("fre");
		aLør = data.getParcelableArrayListExtra("lør");
		aSøn = data.getParcelableArrayListExtra("søn");
	}

	private Appointment findMostRecentAppointment(Time t) {

		return findMostRecentAppointment(t, 1);
	}

	// recursive method for finding closest day with appointment. Gives up after
// 7 days with no appointments
	private Appointment findMostRecentAppointment(Time t, int counter) {
		if (counter > 7) return null;

		// finds current array
		ArrayList<Appointment> a = findDayArray(t);

		Appointment closestAppointment = null;

		// tries getting next appointment from a day's arraylist, else go to next day.
try{
	//if we're in current day, find closest appointment by hours+minutes and upwards
	if(counter == 1)
		closestAppointment = findClosestAppointmentByTime(t, a);
	//else get first object in closest array with object in it.
	else
       closestAppointment = a.get(0);
}catch(NullPointerException e){
		// we did not find a suitable appointment in searched array.
			Time time = t;
			// checks weekday and does ++
			if (time.weekDay != 6)
				time.weekDay++;
			else
				time.weekDay = 0;
			return findMostRecentAppointment(time, ++counter);
		}
		return closestAppointment;
	}

	// find the closest appointment through an iterator of the arraylist. used for the current day of the week.
	private Appointment findClosestAppointmentByTime(Time t, ArrayList<Appointment> al) throws NullPointerException{

		Appointment currentTimeAppointment = new Appointment(Appointment.NOTREAL,
				"text", t, false);
		AppointmentComparator ac = new AppointmentComparator();

			for(Appointment a : al){
				//is current time less than the appointment we're checking?
				if (ac.compare(currentTimeAppointment, a) <= 0){
					return a;
				}

			}
			//if we have not returned an object, it means we can't find one in the array greater than our current time.
				throw new NullPointerException("Appointment is null in the first day searched");
	}

	// makes an alarm and will remove the previous one if it has not triggered
// (last part not implemented yet)
	private boolean setAlarmFragment(Appointment app) {
		if (app != null) {
			alarm = new Alarm();
			Time t = app.getTime();
			t.set(t.toMillis(false) - AlarmManager.INTERVAL_HOUR);
			alarm.setAlarm(this, t);

			return true;
		}

		return false;
	}

	private ArrayList<Appointment> findDayArray(Time t) {
		ArrayList<Appointment> array = null;

		switch (t.weekDay) {
		case 0:
			if (!aSøn.isEmpty()) array = aSøn;
			break;
		case 1:
			if (!aMan.isEmpty()) array = aMan;
			break;
		case 2:
			if (!aTir.isEmpty()) array = aTir;
			break;
		case 3:
			if (!aOns.isEmpty()) array = aOns;
			break;
		case 4:
			if (!aTor.isEmpty()) array = aTor;
			break;
		case 5:
			if (!aFre.isEmpty()) array = aFre;
			break;
		default:
			if (!aLør.isEmpty()) array = aLør;
			break;
		}
		return array;
	}

	//method will not be needed later because alarm will call removeappointment after it's run.
	private boolean appointmentExpired(Appointment a) {
		Time time = new Time();
		time.setToNow();
		if (a.getTime().toMillis(false) - time.toMillis(false) < 0) {
			return true;
		}

		return false;
	}

	private void removeExpiredAppointment(Appointment a) {
		if (appointmentExpired(a)) {
			try {
				ArrayList<Appointment>al = findDayArray(a.getTime());
				al.remove(a);
				
				//we create a new object 7 days in the future after current appointment and add it to the list.
				if(a.isPersistent()){
//					al.add(TimeHandler.makeNew(a));
				}
					
			} catch (NullPointerException np) {
				return;
			}
		}
	}

	// TEST-PROGRAM-METHODS
	private void findPreferences() {
		// sånn her henter vi sharedpreferences om vi skal gjøre noe med det en
// plass.
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean checkbox = pref.getBoolean("checkbox_preference", false);
		System.out.println(checkbox);
		System.out.println(pref.getAll().toString());
	}

	private void testFillArray(ArrayList<Appointment> app) {
		Appointment[] appointments = new Appointment[3];
		Time time = new Time();
		Calendar cal = new GregorianCalendar();

		// set all Time-variables except seconds.
		time.set(0, 24, 5, cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
		appointments[0] = new Appointment(Appointment.NIMPORTANT,
				"Yolo forever", time, true);
		appointments[1] = new Appointment(Appointment.URGENT,
				"2 timer programmering", time, true);
		appointments[2] = new Appointment(Appointment.MEDIUM, "Lag middag",
				time, true);
		app.add(appointments[0]);
		app.add(appointments[1]);
		app.add(appointments[2]);
	}

}
