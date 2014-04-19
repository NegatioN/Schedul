package joakim.app.GUI;

import joakim.app.schedul.Appointment;
import joakim.app.schedul.R;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

public class CreateAppointmentDialog extends DialogFragment implements OnItemSelectedListener{

	private EditText mEditText;
	private Spinner sPriorities;
	private Button bAdd;
	private TimePicker tp;
	private int[] priorityColors = new int[3];
	private static Context c;
	private int priorityColor = 5;
	
	//interface som importeres til main for sending av appointment-objekt.
    public interface CreateAppointmentDialogListener {
        void onFinishCreateAppointmentDialog(Appointment inputText);
    }
    
	
	public CreateAppointmentDialog(){
		//required empty constructor
	}
	public static CreateAppointmentDialog newInstance(Context context, String title){
		c = context;
		CreateAppointmentDialog frag = new CreateAppointmentDialog();
		Bundle args = new Bundle();
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_appointment, container);
        //find all views
        mEditText = (EditText) view.findViewById(R.id.descriptionAppointment);
        tp = (TimePicker) view.findViewById(R.id.tpAppointment);
        tp.setIs24HourView(true);
        sPriorities = (Spinner) view.findViewById(R.id.priority_spinnerAppointment);
        makeSpinners(sPriorities);
        //finner knapp, setter listener
        bAdd = (Button) view.findViewById(R.id.bCreate);
        fillPriorityArray();
        bAdd.setOnClickListener(new View.OnClickListener() {
			
			//caller interface-metoden fra main og lukker fragmentet.
			public void onClick(View v) {
	            CreateAppointmentDialogListener listener = (CreateAppointmentDialogListener) getActivity();
	            listener.onFinishCreateAppointmentDialog(createAppointmentObject());
	            dismiss();
		    	
			}
		});
        
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }
    
    
	//fyller bare prioritetsarrays med strings og color-values for bakgrunnen appointment-object skal gi textview i listviews.
	private void fillPriorityArray(){
		priorityColors[0] = Appointment.NIMPORTANT;
		priorityColors[1] = Appointment.MEDIUM;
		priorityColors[2] = Appointment.URGENT;
				
	}
	
	private void makeSpinners(Spinner s){
		ArrayAdapter<CharSequence> aa = ArrayAdapter.createFromResource(c, R.array.priorities, android.R.layout.simple_spinner_item);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sPriorities.setOnItemSelectedListener(this);
		s.setAdapter(aa);
	}
	//create an appointment for display on screen so user can drag into desired week-day
	private Appointment createAppointmentObject(){
		int hour = tp.getCurrentHour();
		int min = tp.getCurrentMinute();
		Time time = new Time();
		//sorterer bare relativt innenfor lista, så trenger ikke d/m/y
		time.set(0, min, hour, 0, 0, 0);
		
		String desc = mEditText.getText().toString();
		
		//endre til å lage en summary-text
		Appointment app = new Appointment(priorityColor, desc, desc, time);
		return app;
	}
	//lager summary-text basert på input description. "første ord, eller kode av setning"
	private String summary(String string){
		String summary = null;
		
		return summary;
	}
	
	//onlistitemselectedListener
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		priorityColor = priorityColors[pos];
		//get priorityColor[pos] og send til objektet som lages.
	}
	public void onNothingSelected(AdapterView<?> parent) {
//sett default priority Not important		
	}
}
