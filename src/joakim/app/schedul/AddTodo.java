package joakim.app.schedul;

import java.util.ArrayList;

import joakim.app.GUI.ArrayListAdapter;
import joakim.app.GUI.CreateAppointmentDialog;
import joakim.app.GUI.CreateAppointmentDialog.CreateAppointmentDialogListener;
import joakim.app.GUI.DragZoneListener;
import joakim.app.GUI.LvOnItemTouchListener;
import joakim.app.data.Appointment;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class AddTodo extends Activity implements CreateAppointmentDialogListener{

	private ArrayList<Appointment> aMan = new ArrayList<Appointment>();
	private ArrayList<Appointment> aTir = new ArrayList<Appointment>();
	private ArrayList<Appointment> aOns = new ArrayList<Appointment>();
	private ArrayList<Appointment> aTor = new ArrayList<Appointment>();
	private ArrayList<Appointment> aFre = new ArrayList<Appointment>();
	private ArrayList<Appointment> aL�r = new ArrayList<Appointment>();
	private ArrayList<Appointment> aS�n = new ArrayList<Appointment>();
	private TextView draggableAppointment;
	private Button bFragmentStart;
	private Appointment recentAppointment;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_todo);
		
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
		testFillArray(aMan);
		testFillArray(aS�n);
		testFillArray(aTor);
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
			NavUtils.navigateUpFromSameTask(this);
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
	public Appointment getRecentAppointment(){
		return recentAppointment;
	}
	
	
	
	
	//initialize views, dropzoneadapters, ontouchadapters
	private void initializeViews(){
		ListView lw = (ListView)findViewById(R.id.listView1);
		ListView lw2 = (ListView)findViewById(R.id.listView2);
		ListView lw3 = (ListView)findViewById(R.id.listView3);
		ListView lw4 = (ListView)findViewById(R.id.listView4);
		ListView lw5 = (ListView)findViewById(R.id.listView5);
		ListView lw6 = (ListView)findViewById(R.id.listView6);
		ListView lw7 = (ListView)findViewById(R.id.listView7);
		
		lw.setAdapter(new ArrayListAdapter(this,aMan));
		lw2.setAdapter(new ArrayListAdapter(this,aTir));
		lw3.setAdapter(new ArrayListAdapter(this,aOns));
		lw4.setAdapter(new ArrayListAdapter(this,aTor));
		lw5.setAdapter(new ArrayListAdapter(this,aFre));
		lw6.setAdapter(new ArrayListAdapter(this,aL�r));
		lw7.setAdapter(new ArrayListAdapter(this,aS�n));
		
		lw.setOnDragListener(new DragZoneListener(this));
		lw2.setOnDragListener(new DragZoneListener(this));
		lw3.setOnDragListener(new DragZoneListener(this));
		lw4.setOnDragListener(new DragZoneListener(this));
		lw5.setOnDragListener(new DragZoneListener(this));
		lw6.setOnDragListener(new DragZoneListener(this));
		lw7.setOnDragListener(new DragZoneListener(this));
	}
	
	private void testFillArray(ArrayList<Appointment> app){
		Appointment[] appointments = new Appointment[3];
		Time t = new Time();
		t.set(0, 55, 4, 0, 0, 0);
		appointments[0] = new Appointment(Appointment.NIMPORTANT, "Yolo", "Yolo forever", t);
		appointments[1] = new Appointment(Appointment.URGENT, "Programmer", "2 timer programmering", t);
		appointments[2] = new Appointment(Appointment.MEDIUM, "Lag middag", "Lag middag", t);
		app.add(appointments[0]);
		app.add(appointments[1]);
		app.add(appointments[2]);
	}

}