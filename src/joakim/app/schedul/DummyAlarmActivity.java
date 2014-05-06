package joakim.app.schedul;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

//dummy-class works, dialog pops up and buttons function. 
public class DummyAlarmActivity extends Activity{

	
		@Override
		public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);

		    AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setTitle("Alarm")
		       .setMessage("message")
		       .setIcon(R.drawable.ic_launcher);
		       builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
	               
		      
		       builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});

		    final AlertDialog dialog = builder.create();
		    dialog.show();
		    
		}
	
}
