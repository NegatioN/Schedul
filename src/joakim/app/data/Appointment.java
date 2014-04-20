package joakim.app.data;

import android.graphics.Color;
import android.text.format.Time;

public class Appointment {

	
	//ADD EN REPEATABLE-BOOLEAN. For når user vil at appointmenten skal gjentas neste uke også.
	
	public static final int URGENT = Color.RED, MEDIUM = Color.YELLOW, NIMPORTANT = Color.GREEN;
	
	private int priority;
	private String description, summary;
	private Time time;
	
	public Appointment(int priority, String description, String summary, Time time){
		
		this.priority = priority;
		this.description = description;
		this.summary = summary;
		this.time = time;
		
	}

	public String toString(){
		//testmetode som ikke gir noe atm.
//		return "Prioritet: " + getPriority() + " Beskrivelse: " + getDescription() + " Tid: " + getTime().hour + ":" + getTime().minute;
		return getSummary();
	}
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}
	
}
