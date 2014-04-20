package joakim.app.GUI;

import java.util.ArrayList;

import joakim.app.data.Appointment;
import joakim.app.schedul.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class ArrayListAdapter extends ArrayAdapter<Appointment>{

	
	private Context context;
	private ArrayList<Appointment> objects;
	public ArrayListAdapter(Context context, ArrayList<Appointment> objects) {
		super(context, R.layout.row_layout, objects);
		this.context = context;
		this.objects = objects;
	}
	
	  public View getView(int position, View convertView, ViewGroup parent) {
		    LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = inflater.inflate(R.layout.row_layout, parent, false);
		    TextView textView = (TextView) rowView.findViewById(R.id.summary);
		    textView.setBackgroundColor(objects.get(position).getPriority());
		    textView.setText(objects.get(position).getSummary());
		    textView.setOnTouchListener(new LvOnItemTouchListener());

		    return rowView;
		  }

	  public ArrayList<Appointment> getObjects(){
		  return objects;
	  }
}
