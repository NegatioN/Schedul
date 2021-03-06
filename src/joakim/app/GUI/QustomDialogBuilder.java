package joakim.app.GUI;

import joakim.app.data.Appointment;
import joakim.app.data.FragmentDataHandler;
import joakim.app.schedul.R;
import net.simonvt.numberpicker.NumberPicker;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.format.Time;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class QustomDialogBuilder extends AlertDialog.Builder implements OnItemSelectedListener{

	/** The custom_body layout */
	private View mDialogView;
	
	/** optional dialog title layout */
	private TextView mTitle;
	/** optional alert dialog image */
	private ImageView mIcon;
	/** optional message displayed below title if title exists*/
	private TextView mMessage;
	/** The colored holo divider. You can set its color with the setDividerColor method */
	private View mDivider;
	
	//self-added custom-views
	private EditText mEditText;
	private Button bAdd;
	private Spinner sPriorities;
	private int priorityColor = 5;
	private int[] priorityColors = new int[3];
	private View inflatedView;
	private NumberPicker hp, mp;
	private CheckBox cb;
	
    @Override
	public AlertDialog create() {
    	if (mTitle.getText().equals("")) mDialogView.findViewById(R.id.topPanel).setVisibility(View.GONE);
		return super.create();
	}

	public QustomDialogBuilder(Context context) {
        super(context);

        mDialogView = View.inflate(context, R.layout.qustom_dialog_layout, null);
        setView(mDialogView);

        mTitle = (TextView) mDialogView.findViewById(R.id.alertTitle);
        mMessage = (TextView) mDialogView.findViewById(R.id.message);
        mIcon = (ImageView) mDialogView.findViewById(R.id.icon);
        mDivider = mDialogView.findViewById(R.id.titleDivider);
        
        fillPriorityArray();
        
        
	}

    /** 
     * Use this method to color the divider between the title and content.
     * Will not display if no title is set.
     * 
     * @param colorString for passing "#ffffff"
     */
    public QustomDialogBuilder setDividerColor(String colorString) {
    	mDivider.setBackgroundColor(Color.parseColor(colorString));
    	return this;
    }
 
    @Override
    public QustomDialogBuilder setTitle(CharSequence text) {
        mTitle.setText(text);
        return this;
    }

    public QustomDialogBuilder setTitleColor(String colorString) {
    	mTitle.setTextColor(Color.parseColor(colorString));
    	return this;
    }

    @Override
    public QustomDialogBuilder setMessage(int textResId) {
        mMessage.setText(textResId);
        return this;
    }

    @Override
    public QustomDialogBuilder setMessage(CharSequence text) {
        mMessage.setText(text);
        return this;
    }

    @Override
    public QustomDialogBuilder setIcon(int drawableResId) {
        mIcon.setImageResource(drawableResId);
        return this;
    }

    @Override
    public QustomDialogBuilder setIcon(Drawable icon) {
        mIcon.setImageDrawable(icon);
        return this;
    }
    private QustomDialogBuilder setCloseButton(final AlertDialog ad) {
        bAdd = (Button)inflatedView.findViewById(R.id.bCreate);
		//creates the onclicklistener for the button in our fragment.
		//hack-ish way of solving it.
        bAdd.setOnClickListener(new View.OnClickListener() {
			
			//caller interface-metoden fra main og lukker fragmentet.
			public void onClick(View v) {
				CustomAppointmentDialogListener listener = (CustomAppointmentDialogListener) mDialogView.getContext();
				listener.onFinishCreateAppointmentDialog(FragmentDataHandler.createAppointmentObject(priorityColor, mEditText, hp, mp, cb));
				
	            ad.dismiss();
		    	
			}
		});
        //find checkbox
        cb = (CheckBox) inflatedView.findViewById(R.id.cbPersistent);
        
        return this;
    }
    
    

	/**
     * This allows you to specify a custom layout for the area below the title divider bar
     * in the dialog. As an example you can look at example_ip_address_layout.xml and how
     * I added it in TestDialogActivity.java
     * 
     * @param resId  of the layout you would like to add
     * @param context
     */
    public QustomDialogBuilder setCustomView(int resId, Context context) {
    	final View customView = View.inflate(context, resId, null);
    	((FrameLayout)mDialogView.findViewById(R.id.customPanel)).addView(customView);
    	
    	inflatedView = customView;
        //find all views
        mEditText = (EditText) customView.findViewById(R.id.descriptionAppointment);
        setupTimePicker();
        sPriorities = (Spinner) customView.findViewById(R.id.priority_spinnerAppointment);
        makeSpinner(sPriorities,customView.getContext());
        
        
        fillPriorityArray();
    	
    	
    	return this;
    }
    
    @Override
    public AlertDialog show() {
    	if (mTitle.getText().equals("")) mDialogView.findViewById(R.id.topPanel).setVisibility(View.GONE);
    	AlertDialog ad = super.show();
    	setCloseButton(ad);
    	
    	return ad;
    }
    
    
    /**
     * 
	SECTION FOR SELF-MODIFICATIONS!
     */
    
    
	private void makeSpinner(Spinner s, Context c) {
		ArrayAdapter<CharSequence> aa = ArrayAdapter.createFromResource(c,
				R.array.priorities, android.R.layout.simple_spinner_item);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sPriorities.setOnItemSelectedListener(this);
		s.setAdapter(aa);
	}
    
	// onlistitemselectedListener for spinner-adapter
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		priorityColor = priorityColors[pos];
		// get priorityColor[pos] og send til objektet som lages.
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// sett default priority Not important
	}
	
	// fyller bare prioritetsarrays med strings og color-values for bakgrunnen
	// appointment-object skal gi textview i listviews.
	private void fillPriorityArray() {
		priorityColors[0] = Appointment.NIMPORTANT;
		priorityColors[1] = Appointment.MEDIUM;
		priorityColors[2] = Appointment.URGENT;

	}
	
	//defines our custom time-picker from simonvt's library
	private void setupTimePicker(){
		hp = (NumberPicker) inflatedView.findViewById(R.id.hourPicker);
		mp = (NumberPicker) inflatedView.findViewById(R.id.minutePicker);
		
		hp.setMinValue(0);
		hp.setMaxValue(23);
		mp.setMinValue(0);
		mp.setMaxValue(59);
		
		//sets the numberpickers to now-time
		Time t = new Time();
		t.setToNow();
		hp.setValue(t.hour);
		mp.setValue(t.minute);
		
        hp.setFocusable(true);
        hp.setFocusableInTouchMode(true);
        mp.setFocusable(true);
        mp.setFocusableInTouchMode(true);
	}
	
	//interface som importeres til main for sending av appointment-objekt.
    public interface CustomAppointmentDialogListener {
        void onFinishCreateAppointmentDialog(Appointment inputText);
    }
   

}
