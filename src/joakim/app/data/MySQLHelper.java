package joakim.app.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;
import android.util.Log;

public class MySQLHelper extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_NAME = "AppointmentDB";
	
	private static final String TABLE_APPOINTMENTS = "appointments";
	private static final String KEY_ID = "id";
	private static final String KEY_PRIORITY = "priority", KEY_DESCRIPTION = "description",KEY_SUMMARY = "summary",KEY_YEAR = "year", KEY_TIME = "time",KEY_PERSISTENT = "persistent"; 
	private static final String[] COLUMNS = {KEY_ID, KEY_PRIORITY, KEY_DESCRIPTION, KEY_SUMMARY, KEY_YEAR, KEY_TIME, KEY_PERSISTENT};
	
	public MySQLHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_APPOINTMENT_TABLE = "CREATE TABLE appointments (" +
	"id INTEGER PRIMARY KEY AUTOINCREMENT," +
	"priority INTEGER," +
	"description TEXT," +
	"summary TEXT," + 
	"year INTEGER," +
	"time TEXT, " +
	"persistent INTEGER )";
		//dateTime formatted as MMDDHHMMSS
		db.execSQL(CREATE_APPOINTMENT_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Drop older appointmentTable if exists
		db.execSQL("DROP TABLE IF EXISTS appointments");
		
		//create fresh table
		this.onCreate(db);
	}
	
	//------------------ Methods for interacting with objects
	
	public void addAppointment(Appointment appointment){
		Log.d("addAppointment", appointment.getTime().toString());
		
		//get reference to writeable database
		SQLiteDatabase db = this.getWritableDatabase();
		
		//create contentvalues to add key column/value
		ContentValues values = new ContentValues();

		String datetime = outputDateTime(appointment.getDateTime());
		
		values.put(KEY_PRIORITY, appointment.getPriority());
		values.put(KEY_DESCRIPTION, appointment.getDescription());
		values.put(KEY_SUMMARY, appointment.getSummary());
		values.put(KEY_YEAR, appointment.getTime().year);
		values.put(KEY_TIME, datetime);
		values.put(KEY_PERSISTENT, appointment.isPersistent() ? 1 : 0);
		
		db.insert(TABLE_APPOINTMENTS,
				null,
				values);
		
		//set the auto-increment ID from database to the current object.
		String query = "Select * FROM " + TABLE_APPOINTMENTS;
		
		//function to assign ID from database to object
		Cursor cursor = db.rawQuery(query, null);
		if(cursor != null){
			cursor.moveToLast();
			int id = cursor.getInt(0);
			Log.d("setId " + id, appointment.toString());
			appointment.setId(cursor.getInt(0));
		}
		
		db.close();
	}
	
	public Appointment getAppointment(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = 
				db.query(
						TABLE_APPOINTMENTS, 
						COLUMNS, 
						" id = ?",
						new String[] {String.valueOf(id)}, 
						 null, //group by
						 null, //having
						 null, //order by
						 null); //limit
		
		if(cursor != null)
			cursor.moveToFirst();
		
		Appointment app = new Appointment();
		app = setAppointmentInfo(app, cursor);
		db.close();
		Log.d("getAppointment("+id+")", app.toString());
						
		return app;
	}
	
	//finds the closest appointment forward in time.
	public Appointment getClosestAppointment(){
		Time time = new Time();
		time.setToNow();
		SQLiteDatabase db = this.getReadableDatabase();
		int[] array = {time.month, time.monthDay, time.hour, time.minute, time.second};
		
		String currentDate = outputDateTime(array);
		Log.d("db.getClosestOfTime", currentDate);
		int year = time.year;
		String sql = "SELECT * FROM "+  TABLE_APPOINTMENTS + 
				" WHERE " + year + " <= " + KEY_YEAR + 
				" AND "+ KEY_TIME + " <= " + currentDate + 
				" ORDER BY "+ KEY_TIME + " ASC LIMIT 1";
		Cursor cursor = db.rawQuery(sql, null);
			try{
			cursor.moveToFirst();
			Appointment app = new Appointment();
			app = setAppointmentInfo(app, cursor);
			Log.d("db.getClosestAppointment", app.getTime().toString());
			db.close();
			return app;
			}catch(CursorIndexOutOfBoundsException e){
				e.printStackTrace();
		}
		db.close();
		
		
		return null;
	}
	public ArrayList<ArrayList<Appointment>> getAllAppointments(){
		ArrayList<Appointment> aMon = new ArrayList<Appointment>();
		ArrayList<Appointment> aTue = new ArrayList<Appointment>();
		ArrayList<Appointment> aWed = new ArrayList<Appointment>();
		ArrayList<Appointment> aThu = new ArrayList<Appointment>();
		ArrayList<Appointment> aFri = new ArrayList<Appointment>();
		ArrayList<Appointment> aSat = new ArrayList<Appointment>();
		ArrayList<Appointment> aSun = new ArrayList<Appointment>();
		
		ArrayList<ArrayList<Appointment>> appointmentDays = new ArrayList<ArrayList<Appointment>>();
		appointmentDays.add(aMon);
		appointmentDays.add(aTue);
		appointmentDays.add(aWed);
		appointmentDays.add(aThu);
		appointmentDays.add(aFri);
		appointmentDays.add(aSat);
		appointmentDays.add(aSun);
		
		String query = "Select * FROM " + TABLE_APPOINTMENTS;
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		Appointment app = null;
		if(cursor.moveToFirst()){
			do{
				app = new Appointment();
				app = setAppointmentInfo(app, cursor);
				
				Log.d("setAppointment.Getappointments", app.getSummary() + " - " + app.getTime().toString());
				addToDayArray(appointmentDays, app);
			}while(cursor.moveToNext());
		}
		
		Log.d("getAllAppointments()", appointmentDays.toString());
		
		
		return appointmentDays;
	}
	//sets all the info from from the database in to a given Appointment-object.
	private Appointment setAppointmentInfo(Appointment a, Cursor c){
		Log.d("cursorInfo", c.getInt(0) + ":" + c.getInt(1) + ":" + c.getString(2) + ":" + c.getString(3) + ":" + c.getString(4) + ":" + c.getString(5));
		a.setId(c.getInt(0));
		a.setPriority(c.getInt(1));
		a.setDescription(c.getString(2));
		a.setSummary(c.getString(3));
		a.setDateTime(c.getInt(4), c.getString(5));
		a.setPersistent(c.getInt(6)>0);
		Log.d("setAppointmentInfo", a.toString() + ":" + a.getTime().toString());
		return a;
	}
	
	public int updateAppointment(Appointment appointment){
		SQLiteDatabase db = this.getWritableDatabase();
		
		String datetime = outputDateTime(appointment.getDateTime());
		Log.d("updateAppointment", datetime + " : " + appointment.getId());
		
		ContentValues values = new ContentValues();
		values.put(KEY_PRIORITY, appointment.getPriority());
		values.put(KEY_DESCRIPTION, appointment.getDescription());
		values.put(KEY_SUMMARY, appointment.getSummary());
		values.put(KEY_YEAR, appointment.getTime().year);
		values.put(KEY_TIME, datetime);
		values.put(KEY_PERSISTENT, appointment.isPersistent() ? 1 : 0);
		
		//find relevant row in database to update
		int i = db.update(TABLE_APPOINTMENTS, 
				values, 
				KEY_ID+" = ?", 
				new String[]{String.valueOf(appointment.getId())});
		
//		String query = "Select * FROM " + TABLE_APPOINTMENTS + " WHERE id=" + appointment.getId();
//		Cursor c = db.rawQuery(query, null);
//		c.moveToFirst();
		
		Log.d("updateAppointment", appointment.getTime().toString() + " : " + appointment.getId());
		
		db.close();
		
		return i;
	}
	public void deleteAppointment(Appointment appointment){
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_APPOINTMENTS, 
				KEY_ID+" = " + appointment.getId(), 
				null);
		Log.d("deleteAppointment()", appointment.toString());
		db.close();
	}
	
	//how many rows are in the table?
	public int getCount(){
		String query = "Select COUNT(*) FROM " + TABLE_APPOINTMENTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.rawQuery(query, null);
		cur.moveToFirst();
		int count = cur.getInt(0);
		
		return count;
	}
	//outputs a string that is always length == 10 with datetime.
	
	private String outputDateTime(int[] array){
		int i = 0;
		StringBuilder sb = new StringBuilder();
			while(i < array.length){
				if(array[i] < 10)
					sb.append('0').append(array[i]);
				else
					sb.append(array[i]);
				i++;
			}
		return sb.toString();
	}
	
	//random methods for help
	private void addToDayArray(ArrayList<ArrayList<Appointment>> appointmentDays, Appointment a) {

		int weekday = a.getTime().weekDay;
		
		switch (weekday) {
		case 0:
			appointmentDays.get(0).add(a);
			break;
		case 1:
			appointmentDays.get(1).add(a);
			break;
		case 2:
			appointmentDays.get(2).add(a);
			break;
		case 3:
			appointmentDays.get(3).add(a);
			break;
		case 4:
			appointmentDays.get(4).add(a);
			break;
		case 5:
			appointmentDays.get(5).add(a);
			break;
		default:
			appointmentDays.get(6).add(a);
			break;
		}
	}

}
