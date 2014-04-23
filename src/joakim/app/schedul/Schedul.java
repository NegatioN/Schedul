package joakim.app.schedul;

import java.util.ArrayList;

import joakim.app.data.Appointment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
	private ArrayList<Appointment>	aL�r			= new ArrayList<Appointment>();
	private ArrayList<Appointment>	aS�n			= new ArrayList<Appointment>();
	private TextView displayAppointment;
	private Runnable tvUpdater;
	private Handler tvHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedul);

		testFillArray(aMan);
		testFillArray(aOns);
		testFillArray(aTir);
		testFillArray(aS�n);
		displayAppointment = (TextView)findViewById(R.id.tvDisplayAppointment);
		
		
		
		 tvHandler =new Handler();
		 tvUpdater = new Runnable(){

		        @Override
		        public void run() {
		        	Appointment appointment = findMostRecentAppointment();
		        	if(appointment != null){
		        		removeExpiredAppointment(appointment);
		        		displayAppointment.setBackgroundColor(appointment.getPriority());
		        		displayAppointment.setText(appointment.getSummary());
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
			intent = new Intent(this,UserSettings.class);
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
		result.putParcelableArrayListExtra("l�r", aL�r);
		result.putParcelableArrayListExtra("s�n", aS�n);
		return result;

	}

	private void updateArrayLists(Intent data) {
		aMan = data.getParcelableArrayListExtra("man");
		aTir = data.getParcelableArrayListExtra("tir");
		aOns = data.getParcelableArrayListExtra("ons");
		aTor = data.getParcelableArrayListExtra("tor");
		aFre = data.getParcelableArrayListExtra("fre");
		aL�r = data.getParcelableArrayListExtra("l�r");
		aS�n = data.getParcelableArrayListExtra("s�n");
	}
	

	private Appointment findMostRecentAppointment() {
		Time time = new Time();
		time.setToNow();
		Appointment closestAppointment = null;
		// finner riktig dagsarray
		ArrayList<Appointment> a = findDayArray(time);
		closestAppointment = a.get(0);
		return closestAppointment;
	}
	
	private ArrayList<Appointment> findDayArray(Time t){
		ArrayList<Appointment> array = null;
		
		switch (t.weekDay) {
		case 0:
			if (!aS�n.isEmpty()) array = aS�n;
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
			if (!aL�r.isEmpty()) array = aL�r;
			break;
		}
		return array;
	}
	
	private boolean appointmentExpired(Appointment a){
		Time time = new Time();
		time.setToNow();
		//currently getTime will result in a time object with day0, second0, year0, month0 so it cant be compared well.
		if(a.getTime().toMillis(false) - time.toMillis(false) < 0){
			return true;
		}
		return false;
	}
	
	private void removeExpiredAppointment(Appointment a){
		if(appointmentExpired(a)){
			findDayArray(a.getTime()).remove(a);
		}
	}
	
	//TEST-PROGRAM-METHODS
	private void testFillArray(ArrayList<Appointment> app) {
		Appointment[] appointments = new Appointment[3];
		Time t = new Time();
		t.set(0, 55, 4, 0, 0, 0);
		appointments[0] = new Appointment(Appointment.NIMPORTANT,
				"Yolo forever", t, false);
		appointments[1] = new Appointment(Appointment.URGENT,
				"2 timer programmering", t, false);
		appointments[2] = new Appointment(Appointment.MEDIUM, "Lag middag", t,
				false);
		app.add(appointments[0]);
		app.add(appointments[1]);
		app.add(appointments[2]);
	}
	

}
