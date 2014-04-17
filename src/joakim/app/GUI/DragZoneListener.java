package joakim.app.GUI;

import joakim.app.schedul.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class DragZoneListener implements OnDragListener{

	private Context context;
	
	public DragZoneListener(Context c){
		context = c;
	}
	
    public boolean onDrag(View v, DragEvent event) {
    	
        Drawable enterShape = context.getResources().getDrawable(R.drawable.shape_droptarget);
        Drawable normalShape = context.getResources().getDrawable(R.drawable.shape);
        int action = event.getAction();
        switch (event.getAction()) {
        case DragEvent.ACTION_DRAG_STARTED:
          // do nothing
          break;
        case DragEvent.ACTION_DRAG_ENTERED:
          v.setBackgroundDrawable(enterShape);
          break;
        case DragEvent.ACTION_DRAG_EXITED:
          v.setBackgroundDrawable(normalShape);
          break;
        case DragEvent.ACTION_DROP:
          // Dropped, reassign View to ViewGroup
          View view = (View) event.getLocalState();
          ViewGroup owner = (ViewGroup) view.getParent();
          owner.removeView(view);
          LinearLayout container = (LinearLayout) v;
          container.addView(view);
          view.setVisibility(View.VISIBLE);
          break;
        case DragEvent.ACTION_DRAG_ENDED:
          v.setBackgroundDrawable(normalShape);
        default:
          break;
        }
        return true;
      }

}
