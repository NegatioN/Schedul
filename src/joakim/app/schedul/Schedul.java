package joakim.app.schedul;

import java.util.ArrayList;

import joakim.app.data.Appointment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class Schedul extends Activity {

	
	private static final int REQUEST_CODE = 10;
	public static final String RESULT_REQUEST = "startForResult";
	
	//ArrayLists av lagret data, sendes til sub-context.
	private ArrayList<Appointment> aMan = new ArrayList<Appointment>();
	private ArrayList<Appointment> aTir = new ArrayList<Appointment>();
	private ArrayList<Appointment> aOns = new ArrayList<Appointment>();
	private ArrayList<Appointment> aTor = new ArrayList<Appointment>();
	private ArrayList<Appointment> aFre = new ArrayList<Appointment>();
	private ArrayList<Appointment> aLør = new ArrayList<Appointment>();
	private ArrayList<Appointment> aSøn = new ArrayList<Appointment>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedul);
		
		
		testFillArray(aMan);
		testFillArray(aOns);
		testFillArray(aSøn);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
			if(data.hasExtra("man")){
				aMan = data.getParcelableArrayListExtra("man");
				aTir = data.getParcelableArrayListExtra("tir");
				aOns = data.getParcelableArrayListExtra("ons");
				aTor = data.getParcelableArrayListExtra("tor");
				aFre = data.getParcelableArrayListExtra("fre");
				aLør = data.getParcelableArrayListExtra("lør");
				aSøn = data.getParcelableArrayListExtra("søn");
				System.out.println(aMan.get(0).toString());
			}
		}
	}
	
	//sends arrays and starts AddTodo activity.
	private Intent sendArrayLists(){
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
	
	private void testFillArray(ArrayList<Appointment> app){
		Appointment[] appointments = new Appointment[3];
		Time t = new Time();
		t.set(0, 55, 4, 0, 0, 0);
		appointments[0] = new Appointment(Appointment.NIMPORTANT, "Yolo forever", t,false);
		appointments[1] = new Appointment(Appointment.URGENT, "2 timer programmering", t,false);
		appointments[2] = new Appointment(Appointment.MEDIUM,  "Lag middag", t,false);
		app.add(appointments[0]);
		app.add(appointments[1]);
		app.add(appointments[2]);
	}
	
	

}
