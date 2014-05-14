package joakim.app.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLHelper extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_NAME = "AppointmentDB";
	
	private static final String TABLE_APPOINTMENTS = "appointments";
	private static final String KEY_ID = "id";
	private static final String KEY_PRIORITY = "priority", KEY_DESCRIPTION = "description",KEY_SUMMARY = "summary",KEY_TIME = "time",KEY_PERSISTENT = "persistent"; 
	private static final String[] COLUMNS = {KEY_ID, KEY_PRIORITY, KEY_DESCRIPTION, KEY_SUMMARY, KEY_TIME, KEY_PERSISTENT};
	
	public MySQLHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_APPOINTMENT_TABLE = "CREATE TABLE Appointments (" +
	"id INTEGER PRIMARY KEY AUTOINCREMENT," +
	"priority INTEGER," +
	"description TEXT," +
	"summary TEXT" + 
	"time DATETIME, " +
	"persistent BOOLEAN )";
		//dateTime formatted as YYYY-MM-DDTHH:MM:SS
		db.execSQL(CREATE_APPOINTMENT_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Drop older appointmentTable if exists
		db.execSQL("DROP TABLE IF EXISTS appointments");
		
		//create fresh table
		this.onCreate(db);
	}
	
	public void addAppointment(Appointment appointment){
		Log.d("addAppointment", appointment.toString());
		
		//get reference to writeable database
		SQLiteDatabase db = this.getWritableDatabase();
		
		//create contentvalues to add key column/value
		ContentValues values = new ContentValues();
		values.put(KEY_PRIORITY, appointment.getPriority());
		values.put(KEY_DESCRIPTION, appointment.getDescription());
		values.put(KEY_SUMMARY, appointment.getSummary());
		values.put(KEY_TIME, appointment.getDateTime());
		values.put(KEY_PERSISTENT, appointment.isPersistent());
		
		db.insert(TABLE_APPOINTMENTS,
				null,
				values);
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
		app.setPriority(cursor.getInt(0));
		app.setDescription(cursor.getString(1));
		app.setSummary(cursor.getString(2));
		app.setDateTime(cursor.getString(3));
		app.setPersistent(cursor.getInt(4)>0);
		
		Log.d("getAppointment("+id+")", app.toString());
						
		return app;
	}
	public ArrayList<Appointment> getAllAppointments(){
		return null;
	}
	public int updateAppointment(Appointment appointment){
		return -1;
	}
	public void deleteAppointment(Appointment appointment){
		
	}

}
