package joakim.app.schedul;

import java.util.ArrayList;

import joakim.app.data.Appointment;
import android.app.Activity;
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
	private TextView displayAppointment;
	private Runnable tvUpdater;
	private Handler tvHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedul);

//		testFillArray(aMan);
//		testFillArray(aOns);
//		testFillArray(aTir);
//		testFillArray(aSøn);
		testFillArray(aLør);
//		testFillArray(aTor);
//		testFillArray(aFre);
		displayAppointment = (TextView)findViewById(R.id.tvDisplayAppointment);
		
		
		
		 tvHandler =new Handler();
		 tvUpdater = new Runnable(){

		        @Override
		        public void run() {
		        	Time t = new Time();
		        	t.setToNow();
		        	
		        	Appointment appointment = findMostRecentAppointment(t);
		        	if(appointment != null){
		        		removeExpiredAppointment(appointment);
		        		displayAppointment.setBackgroundColor(appointment.getPriority());
		        		displayAppointment.setText(appointment.getSummary());
		        	}

		        }

		    };
		    tvHandler.post(tvUpdater);
		    
		    //test how to get sharedpreferences from settings-page.
		    findPreferences();
		    
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
	
	//recursive method for finding closest day with appointment. Gives up after 7 days with no appointments
	private Appointment findMostRecentAppointment(Time t, int counter){
		if(counter > 7)
			return null;
		
		//finds current array
		ArrayList<Appointment> a = findDayArray(t);
		
		Appointment closestAppointment = null;
		
		//tries getting earliest appointment from listview, else go to next day.
		try{
		closestAppointment = a.get(0);
		}catch(NullPointerException e){
			Time time = t;
			//checks weekday and does ++ 
			if(time.weekDay != 6)
			time.weekDay++;
			else
				time.weekDay = 0;
			return findMostRecentAppointment(time, ++counter);
		}
		return closestAppointment;
	}
	
	private ArrayList<Appointment> findDayArray(Time t){
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
			try{
			findDayArray(a.getTime()).remove(a);
			}catch(NullPointerException np){
				return;
			}
		}
	}
	
	//TEST-PROGRAM-METHODS
	private void findPreferences(){
		//sånn her henter vi sharedpreferences om vi skal gjøre noe med det en plass.
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		boolean checkbox = pref.getBoolean("checkbox_preference", false);
		System.out.println(checkbox);
		System.out.println(pref.getAll().toString());
	}
	
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
