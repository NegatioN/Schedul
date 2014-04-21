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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedul);

		testFillArray(aMan);
		testFillArray(aOns);
		testFillArray(aS�n);
		displayAppointment = (TextView)findViewById(R.id.tvDisplayAppointment);
		
		
		
		 final Handler handler=new Handler();
		 handler.post(new Runnable(){

		        @Override
		        public void run() {
		        	Appointment appointment = findMostRecentAppointment();
		        	if(appointment != null){
		        		displayAppointment.setBackgroundColor(appointment.getPriority());
		        		displayAppointment.setText(appointment.getSummary());
		        	}
		             // upadte textView here

		            handler.postDelayed(this,500); // set time here to refresh textView

		        }

		    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mainmenu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_add:
			Intent intent = sendArrayLists();
			startActivityForResult(intent, REQUEST_CODE);
			break;
		}

		return true;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			if (data.hasExtra("man")) {
				updateArrayLists(data);
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
		switch (time.weekDay) {
		case 0:
			if (!aS�n.isEmpty()) closestAppointment = aS�n.get(0);
			break;
		case 1:
			if (!aMan.isEmpty()) closestAppointment = aMan.get(0);
			break;
		case 2:
			if (!aTir.isEmpty()) closestAppointment = aTir.get(0);
			break;
		case 3:
			if (!aOns.isEmpty()) closestAppointment = aOns.get(0);
			break;
		case 4:
			if (!aTor.isEmpty()) closestAppointment = aTor.get(0);
			break;
		case 5:
			if (!aFre.isEmpty()) closestAppointment = aFre.get(0);
			break;
		default:
			if (!aL�r.isEmpty()) closestAppointment = aL�r.get(0);
			break;
		}
		return closestAppointment;
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
