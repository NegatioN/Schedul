package joakim.app.data;

import net.simonvt.numberpicker.NumberPicker;
import android.text.format.Time;
import android.util.Log;
import android.widget.EditText;
import android.widget.TimePicker;

public class FragmentDataHandler {


	//create an appointment for display on screen so user can drag into desired week-day
	public static Appointment createAppointmentObject(int priorityColor, EditText et, NumberPicker hours, NumberPicker minutes){
		Time time = new Time();
		
		time.setToNow();
		//set all Time-variables except seconds.
		time.set(0, minutes.getValue(), hours.getValue(), time.monthDay, time.month, time.year);
		
		time.normalize(false);
		
		Log.d("createAppointmentDialog", time.toString());
		String desc = et.getText().toString();
		
		//endre til å lage en summary-text
		Appointment app = new Appointment(priorityColor, desc, time,true);
		return app;
	}
}
