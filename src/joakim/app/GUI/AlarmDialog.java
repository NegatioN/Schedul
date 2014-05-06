package joakim.app.GUI;

import joakim.app.schedul.DummyAlarmActivity;
import joakim.app.schedul.R;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class AlarmDialog extends DialogFragment {

	private TextView appointmentTxt, appointmentTime;
	private Button closeButton;
	
	
	
	   
		public AlarmDialog(){
			//required empty constructor
		}
		
		
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.fragment_alarm, container);
	        //find all views
	        //finner knapp, setter listener
	        
	        initializeViews(view);
	        
	        //
			FragmentManager fm =  getFragmentManager();
	        
	        
	        String title = getArguments().getString("title", "Enter Name");
	        getDialog().setTitle(title);

	        return view;
	    }
	    
	    private void initializeViews(View v){
	    	appointmentTxt = (TextView) v.findViewById(R.id.tvAlarmAppointment);
	    	appointmentTime = (TextView) v.findViewById(R.id.tvAppointmentTime);
	    	closeButton = (Button) v.findViewById(R.id.bCloseFragment);
	    }
	   
	
}
