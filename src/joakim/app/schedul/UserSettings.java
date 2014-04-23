package joakim.app.schedul;

import joakim.app.GUI.SettingsFragment;
import android.app.Activity;
import android.os.Bundle;

public class UserSettings extends Activity{
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
	  // TODO Auto-generated method stub
	  super.onCreate(savedInstanceState);
	  
	  getFragmentManager().beginTransaction().replace(android.R.id.content,
	                new SettingsFragment()).commit();
	 }
}
