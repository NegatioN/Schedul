package joakim.app.GUI;

import joakim.app.data.Appointment;
import joakim.app.data.FragmentDataHandler;
import joakim.app.schedul.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

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
	private TimePicker tp;
	private EditText mEditText;
	private Button bAdd;
	private Spinner sPriorities;
	private int priorityColor = 5;
	private int[] priorityColors = new int[3];
	
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
    
    
    
	public Builder setPositiveButton(int textId) {
		// TODO Auto-generated method stub
    	OnClickListener listener = new OnClickListener(
    			new OnClickListener());
		return super.setPositiveButton(textId, listener);
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

        //find all views
        mEditText = (EditText) customView.findViewById(R.id.descriptionAppointment);
        tp = (TimePicker) customView.findViewById(R.id.tpAppointment);
        tp.setIs24HourView(true);
        sPriorities = (Spinner) customView.findViewById(R.id.priority_spinnerAppointment);
        makeSpinner(sPriorities,customView.getContext());
        //finner knapp, setter listener
        bAdd = (Button) customView.findViewById(R.id.bCreate);
        fillPriorityArray();
        bAdd.setOnClickListener(new View.OnClickListener() {
			
			//caller interface-metoden fra main og lukker fragmentet.
			public void onClick(View v) {
	            CreateAppointmentDialogListener listener = (CreateAppointmentDialogListener)this;
	            listener.onFinishCreateAppointmentDialog(FragmentDataHandler.createAppointmentObject(priorityColor, mEditText, tp));
//	            this.dismiss();
		    	
			}
		});
    	
    	
    	return this;
    }
    
    @Override
    public AlertDialog show() {
    	if (mTitle.getText().equals("")) mDialogView.findViewById(R.id.topPanel).setVisibility(View.GONE);
    	return super.show();
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
	
	//interface som importeres til main for sending av appointment-objekt.
    public interface CreateAppointmentDialogListener {
        void onFinishCreateAppointmentDialog(Appointment inputText);
    }
    

}
