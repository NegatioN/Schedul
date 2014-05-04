package joakim.app.data;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

public class Appointment implements Parcelable{

	public static final int URGENT = Color.RED, MEDIUM = Color.YELLOW, NIMPORTANT = Color.GREEN, NOTREAL = Color.TRANSPARENT;
	
	private int priority;
	private String description, summary;
	private Time time;
	private boolean persistent;
	private String regex = "\\s+";
	
	public Appointment(int priority, String description, Time time, boolean persistent){
		//rekkefølgen er viktig pga summary caller time
		this.priority = priority;
		this.description = description;
		this.time = time;
		this.persistent = persistent;
		this.summary = summary(description);	
	}
	//constructor for create parcelable i main.
	public Appointment(Parcel in){
		Time time = new Time();
		this.priority = in.readInt();
		this.description = in.readString();
		this.summary = in.readString();
		int hour = in.readInt();
		int minute = in.readInt();
		time.set(0, minute, hour, 0, 0, 0);
		this.time = time;
		//FIKS PÅ TIME FRA PARCELABLE
		if(in.readInt() == 1) this.persistent = true;
		else
			this.persistent = false;
	}
	
	//lager summary-text basert på input description. "første ord, eller kode av setning"
	private String summary(String string){
		String summary = "";
		String timeText = getTime().hour + ":" + getTime().minute;
		String[] allWords = string.split(regex);
		if(allWords.length == 1)
			return allWords[0] + " - " + timeText;
		for(String s : allWords){
			summary += s.charAt(0);
		}
		
		
		return summary + " - " + timeText;
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

	public boolean isPersistent() {
		return persistent;
	}

	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	//makes parcel-created appointment objects using parcel-constructor.
	public static final Parcelable.Creator<Appointment> CREATOR = new Parcelable.Creator<Appointment>() {

		@Override
		public Appointment createFromParcel(Parcel parcel) {
			return new Appointment(parcel);
		}

		@Override
		public Appointment[] newArray(int size) {
			return new Appointment[size];
		}
		
		
	};
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(priority);
		dest.writeString(description);
		dest.writeString(summary);
		dest.writeInt(getTime().hour);
		dest.writeInt(getTime().minute);
		dest.writeInt(persistent ? 1 : 0);
	}
	
}
