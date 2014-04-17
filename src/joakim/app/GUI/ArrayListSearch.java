package joakim.app.GUI;

import java.util.ArrayList;
import java.util.Iterator;

import joakim.app.schedul.Appointment;
import android.widget.TextView;

//searches through the arraylist of appointments for a string-value
public class ArrayListSearch {

	public static Appointment findElementByText(ArrayList<Appointment> objects, TextView view){
		String searchText = view.getText().toString();
		
		Iterator<Appointment> i = objects.iterator();
		Appointment holder = null;
		while(i.hasNext()){
			holder = i.next();
			if(searchText.equals(holder.getSummary())){
				return holder;
			}
		}
		
		return null;
	}
	
}
