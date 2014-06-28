package joakim.app.GUI;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;

import joakim.app.data.Appointment;
import joakim.app.data.AppointmentComparator;
import joakim.app.data.ArrayListHandler;
import joakim.app.data.MySQLHelper;
import joakim.app.data.TimeHandler;
import joakim.app.schedul.AddTodo;
import joakim.app.schedul.AlarmService;
import joakim.app.schedul.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

// settes på en listview
public class DragZoneListener implements OnDragListener {

	private Context	context;
	private MySQLHelper db;
	private AlarmService as;
	
	public DragZoneListener(Context c, MySQLHelper db, AlarmService as) {
		context = c;
		this.db = db;
		this.as = as;
	}
	
	

	public boolean onDrag(View v, DragEvent event) {

		Drawable enterShape = context.getResources().getDrawable(
				R.drawable.shape_droptarget);
		Drawable normalShape = context.getResources().getDrawable(
				R.drawable.shape);
		// finn parent av view som er dropzone, altså linearlayouten under.
		LinearLayout parent = (LinearLayout) v.getParent();
		// finn textview som har blitt flyttet
		TextView view = (TextView) event.getLocalState();

		int action = event.getAction();
		switch (action) {
		case DragEvent.ACTION_DRAG_STARTED:
			// do nothing
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			parent.setBackgroundDrawable(enterShape);
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			parent.setBackgroundDrawable(normalShape);
			break;
		case DragEvent.ACTION_DROP:

			View parentView = (View) view.getParent();
			Appointment appointment;
			
			
			// hvis dette er ett nytt objekt laget i fragmentet
			if (parentView instanceof LinearLayout) {
				AddTodo getter = (AddTodo) context;
				appointment = getter.getRecentAppointment();
				//edits day/week of appointment based on where we start and end.
				TimeHandler.changeTimeOfAppointment(appointment, v);
				//add new appointment to database
				db.addAppointment(appointment);
				
				
			} 
			//hvis dette er ett objekt som flyttes fra en list til en annen
			else {

				// finn parent av view som er dropzone, altså linearlayouten under.
				// må ha second getparent pga relativelayout i TV-definisjonen
				ListView startLv = (ListView) parentView.getParent();
				ArrayListAdapter startAdapter = (ArrayListAdapter) startLv
						.getAdapter();
				ArrayList<Appointment> startList = startAdapter.getObjects();
				// finner appointment i LV vi starta i, slik at vi kan fjerne og adde.
				appointment = ArrayListHandler.findElementByText(
						startList, view);
				
				//edits day/week of appointment based on where we start and end.
				TimeHandler.changeTimeOfAppointment(appointment, v);

				//update old appointment in DB
				db.updateAppointment(appointment);
				// use all variables and update listviews
				removeItem(appointment, startList, startAdapter);
			}
			
			// get listview, adapter and arraylist of shadow-end
			ListView dropLv = (ListView) v;
			ArrayListAdapter dropAdapter = (ArrayListAdapter) dropLv
					.getAdapter();
			
			

			
			ArrayList<Appointment> dropList = dropAdapter.getObjects();
			addItem(appointment, dropList, dropAdapter);

			view.setVisibility(View.VISIBLE);
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			parent.setBackgroundDrawable(normalShape);
			view.setVisibility(View.VISIBLE);
		default:
			break;
		}
		return true;
	}

	private void addItem(Appointment a, ArrayList<Appointment> al,
			ArrayListAdapter taa) {
		// add en sortering av objektet on-add basert på Time-objektet.
// Comparator osv
		al.add(a);
		Collections.sort(al, new AppointmentComparator());
		taa.notifyDataSetChanged();
		as.setAlarm(context, db.getClosestAppointment());
	}

	private void removeItem(Appointment a, ArrayList<Appointment> al,
			ArrayListAdapter taa) {
		al.remove(a);
		taa.notifyDataSetChanged();

	}

	

}