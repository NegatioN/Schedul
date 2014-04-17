package joakim.app.schedul;

import joakim.app.GUI.DragZoneListener;
import joakim.app.GUI.TouchListener;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class AddTodo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_todo);
		// Show the Up button in the action bar.
		setupActionBar();
		
	    findViewById(R.id.myimage1).setOnTouchListener(new TouchListener());
	    findViewById(R.id.myimage2).setOnTouchListener(new TouchListener());
	    findViewById(R.id.myimage3).setOnTouchListener(new TouchListener());
	    findViewById(R.id.myimage4).setOnTouchListener(new TouchListener());
	    findViewById(R.id.myimage5).setOnTouchListener(new TouchListener());
	    findViewById(R.id.myimage6).setOnTouchListener(new TouchListener());
	    findViewById(R.id.myimage7).setOnTouchListener(new TouchListener());
	    findViewById(R.id.topLeft).setOnDragListener(new DragZoneListener(this));
	    findViewById(R.id.topMid).setOnDragListener(new DragZoneListener(this));
	    findViewById(R.id.topRight).setOnDragListener(new DragZoneListener(this));
	    findViewById(R.id.midLeft).setOnDragListener(new DragZoneListener(this));
	    findViewById(R.id.midCenter).setOnDragListener(new DragZoneListener(this));
	    findViewById(R.id.midRight).setOnDragListener(new DragZoneListener(this));
	    findViewById(R.id.botLeft).setOnDragListener(new DragZoneListener(this));
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_todo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
