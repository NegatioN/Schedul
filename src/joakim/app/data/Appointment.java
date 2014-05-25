package joakim.app.data;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;
import android.util.Log;

public class Appointment implements Parcelable{

	public static final int URGENT = Color.RED, MEDIUM = Color.YELLOW, NIMPORTANT = Color.GREEN, NOTREAL = Color.TRANSPARENT;
	
	private int priority;
	private String description, summary;
	private Time time;
	private boolean persistent;
	private String regex = "\\s+";
	private int id;
	
	public Appointment(){
		//empty constructor for ease in DB
	}
	
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
		this.id = in.readInt();
		this.priority = in.readInt();
		this.description = in.readString();
		this.summary = in.readString();
		
		//read Time-object
		int year = in.readInt();
		int month = in.readInt();
		int monthDay = in.readInt();
		int hour = in.readInt();
		int minute = in.readInt();
		Log.d("createParcel", "Y:"+year + " M:" + month + " D:" + monthDay + " H:" +hour + "M:" + minute);
		time.set(0, minute, hour, monthDay, month, year);
		
		time.normalize(false);
		this.time = time;

		
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
		return "ID:" + getId() + " Prioritet: " + getPriority() + " Beskrivelse: " + getDescription() + " Tid: " + getTime().hour + ":" + getTime().minute + " isPersistent:" + isPersistent();
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
	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return this.id;
	}

	public boolean isPersistent() {
		return persistent;
	}

	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}
	//methods for interaction with timeObject and database
	public String getDateTime(){
		Time time = getTime();
		Log.d("getDateTime", time.toString() + " Month:" + time.month);
		String dateTime = "" + time.year + "-" +time.month+"-"+time.monthDay+"T"+time.hour+":"+time.minute+":"+time.second;
		return dateTime;
	}
	public void setDateTime(String string){
		String reg = "[-:T]";
		String[] values = string.split(reg);
		
		Time time = new Time();
		time.year = Integer.parseInt(values[0]);
		time.month = Integer.parseInt(values[1]);
		time.monthDay = Integer.parseInt(values[2]);
		time.hour = Integer.parseInt(values[3]);
		time.minute = Integer.parseInt(values[4]);
		time.second = Integer.parseInt(values[5]);
		
		time.normalize(false);
		
		Log.d("setDateTime", values[0]+":"+values[1]+":"+values[2]+":"+values[3]+":"+values[4]+":"+values[5]+ "    "+ time.toString());
		setTime(time);
		
	}

	//start Parcelable-stuff
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
		Time time = getTime();
		dest.writeInt(id);
		dest.writeInt(priority);
		dest.writeString(description);
		dest.writeString(summary);
		dest.writeInt(time.year);
		dest.writeInt(time.month);
		dest.writeInt(time.monthDay);
		dest.writeInt(time.hour);
		dest.writeInt(time.minute);
		dest.writeInt(persistent ? 1 : 0);
	}
	
}
