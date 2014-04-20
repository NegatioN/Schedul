package joakim.app.data;

import java.util.ArrayList;
import java.util.Iterator;

import android.widget.TextView;

//searches through the arraylist of appointments for a string-value
public class ArrayListHandler {

	//METODENE ER USIKRE FOR USYNKRONISERTE THREADS, MÅ FINNE ANNEN LØSNING?
	
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
