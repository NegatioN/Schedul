package joakim.app.schedul;

import java.util.ArrayList;

import joakim.app.GUI.ArrayListAdapter;
import joakim.app.GUI.DragZoneListener;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class AddTodo extends Activity {

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
		setContentView(R.layout.activity_add_todo);
		// Show the Up button in the action bar.
		setupActionBar();
		testFillArray(aMan);
		testFillArray(aSøn);
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
		lw6.setAdapter(new ArrayListAdapter(this,aLør));
		lw7.setAdapter(new ArrayListAdapter(this,aSøn));
		
		lw.setOnDragListener(new DragZoneListener(this,aMan));
		lw2.setOnDragListener(new DragZoneListener(this,aTir));
		lw3.setOnDragListener(new DragZoneListener(this,aOns));
		lw4.setOnDragListener(new DragZoneListener(this,aTor));
		lw5.setOnDragListener(new DragZoneListener(this,aFre));
		lw6.setOnDragListener(new DragZoneListener(this,aLør));
		lw7.setOnDragListener(new DragZoneListener(this,aSøn));
	}
	
	private void testFillArray(ArrayList<Appointment> app){
		Appointment[] appointments = new Appointment[3];
		appointments[0] = new Appointment(Appointment.NIMPORTANT, "Yolo", "Yolo forever", null);
		appointments[1] = new Appointment(Appointment.URGENT, "Programmer", "2 timer programmering", null);
		appointments[2] = new Appointment(Appointment.MEDIUM, "Lag middag", "Lag middag", null);
		app.add(appointments[0]);
		app.add(appointments[1]);
		app.add(appointments[2]);
	}

}
