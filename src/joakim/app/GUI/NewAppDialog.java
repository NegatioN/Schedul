package joakim.app.GUI;

import java.eu.inmite.android.lib.dialogs.*;

import joakim.app.schedul.R;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.support.v4.app.*;




public class NewAppDialog extends SimpleDialogFragment {
	
	private final String TITLE = "Create Appointment";
	private static String TAG = "Create";

	public static void show(FragmentActivity activity) {
		new NewAppDialog().show(activity.getSupportFragmentManager(), TAG);
		FragmentManager fm = activity.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.commit();
	}
	
	public BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {
		
		builder.setTitle(TITLE);
		builder.setView(LayoutInflater.from(getActivity()).inflate(R.layout.dialogfragment_layout, null));
		return builder;
		
	}
}
