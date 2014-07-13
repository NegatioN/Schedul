package joakim.app.tests;

import java.util.ArrayList;

import joakim.app.data.Appointment;
import android.text.format.Time;

public class AppointmentRandomizer {

	private static String[] words = {"Helt", "Konge", "Dagmamma", "Apekatt", "Gorilla", "Passe", "Spille"};
	
	public static ArrayList<Appointment> createAppointments(){
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		for(int i = 0; i < 25; i++){
			appointments.add(createAppointment());
		}
		
		return appointments;
	}
	
	private static Appointment createAppointment(){
		int randomInt = (int) (Math.random()*10);
		String description = "";
		for(int i = 0; i < 4; i++)
			description += (wordRandomizer() + " ");
		Time time = new Time();
		time.year = (int) (Math.random()*2050);
		time.yearDay = (int) (Math.random()*364);
		time.normalize(false);
		boolean persistent = (randomInt>4);
		Appointment a = new Appointment(1, description, time, persistent);
		return a;
	}
	
	private static String wordRandomizer(){
		int randomInt = (int)(Math.random()*7);
		return words[randomInt];
	}
	
	
}
