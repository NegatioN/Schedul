package joakim.app.data;

import java.util.Comparator;

import android.text.format.Time;


public class AppointmentComparator implements Comparator<Appointment>{

	@Override
	public int compare(Appointment lhs, Appointment rhs) {
		Time left = lhs.getTime();
		Time right = rhs.getTime();
		
		if(left.hour < right.hour)
			return -1;
		else if(left.hour > right.hour)
			return 1;
		if(left.minute < right.minute)
			return -1;
		else if(left.minute > right.minute)
			return 1;
		return 0;
	}

}
