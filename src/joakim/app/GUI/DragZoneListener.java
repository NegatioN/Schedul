package joakim.app.GUI;

import java.util.ArrayList;

import joakim.app.schedul.Appointment;
import joakim.app.schedul.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


//settes på en listview
public class DragZoneListener implements OnDragListener{

	private Context context;
	
	public DragZoneListener(Context c, ArrayList<Appointment> objects){
		context = c;
	}
	
    public boolean onDrag(View v, DragEvent event) {
    	
        Drawable enterShape = context.getResources().getDrawable(R.drawable.shape_droptarget);
        Drawable normalShape = context.getResources().getDrawable(R.drawable.shape);
        //finn parent av view som er dropzone, altså linearlayouten under.
        LinearLayout parent = (LinearLayout) v.getParent();
      //finn textview som har blitt flyttet
        TextView view = (TextView) event.getLocalState();
        
        int action = event.getAction();
        switch (action) {
        case DragEvent.ACTION_DRAG_STARTED:
          // do nothing
          break;
        case DragEvent.ACTION_DRAG_ENTERED:
          parent.setBackgroundDrawable(enterShape);
          break;
        case DragEvent.ACTION_DRAG_EXITED:
          parent.setBackgroundDrawable(normalShape);
          break;
        case DragEvent.ACTION_DROP:
        	

        //finn parent av view som er dropzone, altså linearlayouten under.
          //må dobbel getparent pga relativelayout i TV-definisjonen
          ListView startLv =  (ListView) (view.getParent().getParent());
//          startList.removeView(view);
          ArrayListAdapter startAdapter = (ArrayListAdapter)startLv.getAdapter();
          ArrayList<Appointment> startList = startAdapter.getObjects();
          //finner appointment i LV vi starta i, slik at vi kan fjerne og adde.
          Appointment appointment = ArrayListSearch.findElementByText(startList, view);
          
          //get listview, adapter and arraylist of shadow-start
          ListView dropLv = (ListView) v;
          ArrayListAdapter dropAdapter = (ArrayListAdapter)dropLv.getAdapter();
          ArrayList<Appointment> dropList = dropAdapter.getObjects();
          
          //use all variables and update listviews
          addItem(appointment, dropList, dropAdapter);
          removeItem(appointment, startList ,startAdapter);
          
          view.setVisibility(View.VISIBLE);
          break;
        case DragEvent.ACTION_DRAG_ENDED:
          parent.setBackgroundDrawable(normalShape);
          view.setVisibility(View.VISIBLE);
        default:
          break;
        }
        return true;
      }
    
	
	private void addItem(Appointment a, ArrayList<Appointment> al, ArrayListAdapter taa){
		al.add(a);
		taa.notifyDataSetChanged();
	}
	private void removeItem(Appointment a, ArrayList<Appointment> al, ArrayListAdapter taa){
		al.remove(a);
		taa.notifyDataSetChanged();
		
	}

}