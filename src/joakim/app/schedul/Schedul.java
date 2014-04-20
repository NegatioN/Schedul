package joakim.app.schedul;

import joakim.app.data.Appointment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Schedul extends Activity {

	//determines which sub-acitivity gets called.
	private static final int REQUEST_CODE = 10;
	public static final String RESULT_REQUEST = "startForResult";
	Appointment closestAppointment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedul);
		Intent i = new Intent(this, AddTodo.class);
		i.putExtra(RESULT_REQUEST, true);
		startActivityForResult(i ,REQUEST_CODE);

	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mainmenu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_add:
			Intent intent = new Intent(this, AddTodo.class);
			startActivity(intent);
			break;
		}

		return true;
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
			if(data.hasExtra("result")){
				closestAppointment = (Appointment) data.getParcelableExtra("result");
				System.out.println(closestAppointment.toString());
			}
		}
	}

}
