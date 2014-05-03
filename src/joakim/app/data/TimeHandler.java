package joakim.app.data;

import joakim.app.schedul.R;
import android.app.AlarmManager;
import android.content.Context;
import android.text.format.Time;
import android.view.View;



//edits day/week of appointment based on where we start and end the dragging motion in addTodo-screen.
public class TimeHandler {

	// remove milliseconds from Time-object? that way we only need to find the
// difference in days.
	public static void changeTimeOfAppointment(Appointment app,
			View slutt) {
		Time time = new Time();
		time.setToNow();
		
		int endDay = findWeekDay(slutt);
		int nowDay = time.weekDay;
		int startDay = app.getTime().weekDay;

		// flip the denominator so it makes sense.
		long displaceTime = (startDay - endDay)
				* AlarmManager.INTERVAL_DAY; // days times milliseconds in a day

		//special conditions 
		if (endDay < startDay && endDay < nowDay && nowDay < startDay) {
				// if this weeks appointment gets moved to next week, add 7
				displaceTime += 7 * AlarmManager.INTERVAL_DAY;
			
		} 
		else if(nowDay < endDay && startDay < endDay && startDay < nowDay){
			// if next weeks appointment gets moved to current week, subtract 7
			displaceTime -= 7 * AlarmManager.INTERVAL_DAY;
		}
		Time appTime = app.getTime();
		appTime.set(appTime.toMillis(true) - displaceTime);
		app.setTime(appTime);
		System.out.println(appTime.toString() + " GAYDAR");
		return;
	}


	// finds the weekday of a Listview passed in by the draglistener
	private static int findWeekDay(View view) {
	// treat sunday as 7 for practical purposes.
		switch (view.getId()) {
		case R.id.listView1:
			return Time.MONDAY;
		case R.id.listView2:
			return Time.TUESDAY;
		case R.id.listView3:
			return Time.WEDNESDAY;
		case R.id.listView4:
			return Time.THURSDAY;
		case R.id.listView5:
			return Time.FRIDAY;
		case R.id.listView6:
			return Time.SATURDAY;
		default:
			return Time.SUNDAY + 7;
		}

	}

}
