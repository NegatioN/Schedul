package joakim.app.data;

import joakim.app.schedul.R;
import android.app.AlarmManager;
import android.text.format.Time;
import android.util.Log;
import android.view.View;

// edits day/week of appointment based on where we start and end the dragging
// motion in addTodo-screen.
public class TimeHandler {

	// remove milliseconds from Time-object? that way we only need to find the
// difference in days.
	public static void changeTimeOfAppointment(Appointment app, View slutt) {
		Time time = new Time();
		time.setToNow();

		int endDay = findWeekDay(slutt);
		int nowDay = time.weekDay;
		int startDay = app.getTime().weekDay;
		// if we have a sunday on our hands we treat it as a 7
		if (startDay == 0) startDay = 7;
		if (nowDay == 0) nowDay = 7;

		// difference between days times millis in a day
		long displaceTime = -(startDay - endDay) * AlarmManager.INTERVAL_DAY;

		// special conditions
		if (startDay >= nowDay && endDay < nowDay) {
			// if this weeks appointment gets moved to next week, add 7
			displaceTime += (7 * AlarmManager.INTERVAL_DAY);

		} else if (startDay < nowDay && endDay >= nowDay) {
			// if next weeks appointment gets moved to current week, subtract 7
			displaceTime -= (7 * AlarmManager.INTERVAL_DAY);
		}
		// if we're dropping it in the same day, we don't need to displace
// anything.
		else if (startDay == endDay) return;

		Time appTime = app.getTime();
		appTime.set(appTime.toMillis(false) + displaceTime);
		app.setTime(appTime);
		Log.d("Timehandler changeApp", appTime.toString());
		return;
	}

	//metoden funker ikke som den skal. Vi vil incremente med 7 dager p� appointmenten.
	public static Appointment makeNew(Appointment a) {
		Log.d("TimeHandler makenew", a.getTime().toString());
		Time appTime = a.getTime();
		appTime.yearDay+=7;
		appTime.normalize(false);
		a.setTime(appTime);
		
		Log.d("TimeHandler makenew", a.getTime().toString());
		return a;
	}

	// finds the weekday of a Listview passed in by the draglistener
	private static int findWeekDay(View view) {
		// treat sunday as 7 for practical purposes.
		switch (view.getId()) {
		case R.id.mondayLv:
			return Time.MONDAY;
		case R.id.tuesdayLv:
			return Time.TUESDAY;
		case R.id.wednesdayLv:
			return Time.WEDNESDAY;
		case R.id.thursdayLv:
			return Time.THURSDAY;
		case R.id.fridayLv:
			return Time.FRIDAY;
		case R.id.saturdayLv:
			return Time.SATURDAY;
		default:
			return (Time.SUNDAY + 7);
		}

	}

}
