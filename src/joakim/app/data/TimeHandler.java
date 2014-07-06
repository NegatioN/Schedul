package joakim.app.data;

import joakim.app.schedul.R;
import android.app.AlarmManager;
import android.text.format.Time;
import android.util.Log;
import android.view.View;

// edits day/week of appointment based on where we start and end the dragging
// motion in addTodo-screen.
public class TimeHandler {

	//displaces the date of an appointment based on where user moves it in the week-calendar.
	public static void changeTimeOfAppointment(Appointment a, View slutt){
		Time time = new Time();
		time.setToNow();
		time.normalize(false);

		int endDay = findWeekDay(slutt);
		int nowDay = time.weekDay;
		int startDay = a.getTime().weekDay;
		// if we have a sunday on our hands we treat it as a 7
		if (startDay == 0) startDay = 7;
		if (nowDay == 0) nowDay = 7;
		
		Log.d("Timehandler Days", "now:" + nowDay + " start:" + startDay + " end:" + endDay);
		
		int displaceDays = endDay - startDay;
		
		Log.d("Timehandler.YearDay", "Appointment: " +a.getTime().yearDay + " now: " + time.yearDay);
		if(displaceDays == 0)
			return;
		
		
		//if this appointment has been made new, and gets moved back to the previous week again.
		else if(a.getTime().yearDay - time.yearDay == 7){
			//we always move backwards one week comparatively.
//			displaceDays -= 7;
			Log.d("Timehandler.madenew", "Appointment: " +a.getTime().yearDay + " now: " + time.yearDay);
		}
		else if(displaceDays < 0 ){
			//our appointment has been moved "backwards" since END - START < 0
			//if we traverse back, but both start and end are HIGHER than NOW, it should be unchanged! we have moved back in current week
			//if we traverse back, but both start and end are LOWER than NOW it should be unchanged! we have moved back within the next week.
			
			//if traverse back, start is higher than or equal to NOW, but end is lower, we have gone into the next week
			if(nowDay <= startDay && nowDay > endDay){
			displaceDays += 7;
				if(nowDay == endDay){
					//we need to check the time-object as to wether we are in current week, or next
				}
			}
		}
		else{
			//our appointment has been moved "forward"
			
			if(nowDay <= endDay && nowDay > startDay){
				displaceDays -= 7;
				if(nowDay == endDay){
					//we need to check the time-object as to wether we are in current week, or previous
				}
			}
			
		}
		a.getTime().monthDay += displaceDays;
		Log.d("Timehandler displacedays", displaceDays + "");
		a.getTime().normalize(false);
		
		Log.d("Timehandler changeApp", a.getTime().toString());
		
	}

	//metoden funker ikke som den skal. Vi vil incremente med 7 dager på appointmenten.
	public static Appointment makeNew(Appointment a) {
		Log.d("TimeHandler makenew old", a.getTime().toString());
		Time appTime = a.getTime();
		appTime.monthDay+=7;
		appTime.normalize(false);
		a.setTime(appTime);
		
		Log.d("TimeHandler makenew new", a.getTime().toString());
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
