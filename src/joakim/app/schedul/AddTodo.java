package joakim.app.schedul;

import java.util.ArrayList;

import joakim.app.GUI.ArrayListAdapter;
import joakim.app.GUI.CreateAppointmentDialog;
import joakim.app.GUI.CreateAppointmentDialog.CreateAppointmentDialogListener;
import joakim.app.GUI.DragZoneListener;
import joakim.app.GUI.LvOnItemTouchListener;
import joakim.app.data.Appointment;
import joakim.app.data.MySQLHelper;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class AddTodo extends Activity implements CreateAppointmentDialogListener{

	private ArrayList<Appointment> aMan;
	private ArrayList<Appointment> aTir;
	private ArrayList<Appointment> aOns;
	private ArrayList<Appointment> aTor;
	private ArrayList<Appointment> aFre;
	private ArrayList<Appointment> aLør;
	private ArrayList<Appointment> aSøn;
	private TextView draggableAppointment;
	private Button bFragmentStart;
	private Appointment recentAppointment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(getIntent().hasExtra(Schedul.RESULT_REQUEST))
			loadArrayLists(getIntent());
		
		setContentView(R.layout.activity_add_todo);
		
		//define the area where we make a new appointment.
		draggableAppointment = (TextView) findViewById(R.id.tvDraggableAppointment);
		draggableAppointment.setOnTouchListener(new LvOnItemTouchListener());
		draggableAppointment.setVisibility(View.GONE);
		bFragmentStart = (Button) findViewById(R.id.bFragmentStart);
		bFragmentStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showFragment();
			}
		});
		
		
		
		// Show the Up button in the action bar.
		setupActionBar();
		initializeViews();
		
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_todo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
//			NavUtils.navigateUpFromSameTask(this);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private void showFragment() {
		FragmentManager fm = getFragmentManager();
		CreateAppointmentDialog appDialog = CreateAppointmentDialog
				.newInstance(this, "Create Appointment");
		appDialog.show(fm, "fragment_create_appointment");
	}

	public void onFinishCreateAppointmentDialog(Appointment app) {
		//use appointment for create textview/button?
		draggableAppointment.setBackgroundColor(app.getPriority());
		draggableAppointment.setText(app.getSummary());
		draggableAppointment.setVisibility(View.VISIBLE);
		
		//send appointment to dragzonelistener
		recentAppointment = app;
	}
	
	//used in the dragListener when a new appointment is found
	public Appointment getRecentAppointment(){
		return recentAppointment;
	}
	
	//avslutter activity og sender alle arrays til main.
	public void finish(){
		setResult(RESULT_OK, sendArrayLists());
		super.finish();
	}
	
	
	
	
	//initialize views, dropzoneadapters, ontouchadapters
	private void initializeViews(){
		ListView lw = (ListView)findViewById(R.id.mondayLv);
		ListView lw2 = (ListView)findViewById(R.id.tuesdayLv);
		ListView lw3 = (ListView)findViewById(R.id.wednesdayLv);
		ListView lw4 = (ListView)findViewById(R.id.thursdayLv);
		ListView lw5 = (ListView)findViewById(R.id.fridayLv);
		ListView lw6 = (ListView)findViewById(R.id.saturdayLv);
		ListView lw7 = (ListView)findViewById(R.id.sundayLv);
		
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean appointmentLocked = preferences.getBoolean("checkbox_preference", false);
		
		//we make the db here, so all dragzones can share the same one for writing objects.
		MySQLHelper db = new MySQLHelper(getApplicationContext());
			initializeListeners(lw, aMan,appointmentLocked,db);
			initializeListeners(lw2, aTir,appointmentLocked,db);
			initializeListeners(lw3, aOns,appointmentLocked,db);
			initializeListeners(lw4, aTor,appointmentLocked,db);
			initializeListeners(lw5, aFre,appointmentLocked,db);
			initializeListeners(lw6, aLør,appointmentLocked,db);
			initializeListeners(lw7, aSøn,appointmentLocked,db);
			
		
	}
	
	//generates new arraylists if the database is empty.
	private void newLists(){
		aMan = new ArrayList<Appointment>();
		aTir = new ArrayList<Appointment>();
		aOns = new ArrayList<Appointment>();
		aTor = new ArrayList<Appointment>();
		aFre = new ArrayList<Appointment>();
		aLør = new ArrayList<Appointment>();
		aSøn = new ArrayList<Appointment>();
	}
	
	//initializes listeners based on user-settings
	private void initializeListeners(ListView lw, ArrayList<Appointment> al, boolean appLocked, MySQLHelper db){
		lw.setAdapter(new ArrayListAdapter(this,al));
		if(!appLocked)
		lw.setOnDragListener(new DragZoneListener(this, db));
	}
	
	private void loadArrayLists(Intent data){
		aMan = data.getParcelableArrayListExtra("man");
		aTir = data.getParcelableArrayListExtra("tir");
		aOns = data.getParcelableArrayListExtra("ons");
		aTor = data.getParcelableArrayListExtra("tor");
		aFre = data.getParcelableArrayListExtra("fre");
		aLør = data.getParcelableArrayListExtra("lør");
		aSøn = data.getParcelableArrayListExtra("søn");
	}
	private Intent sendArrayLists(){
		Intent result = new Intent();
		result.putParcelableArrayListExtra("man", aMan);
		result.putParcelableArrayListExtra("tir", aTir);
		result.putParcelableArrayListExtra("ons", aOns);
		result.putParcelableArrayListExtra("tor", aTor);
		result.putParcelableArrayListExtra("fre", aFre);
		result.putParcelableArrayListExtra("lør", aLør);
		result.putParcelableArrayListExtra("søn", aSøn);
		return result;
	}
	

}
