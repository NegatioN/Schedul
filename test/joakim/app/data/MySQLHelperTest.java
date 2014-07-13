package joakim.app.data;

import java.util.ArrayList;

import joakim.app.tests.AppointmentRandomizer;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class MySQLHelperTest extends AndroidTestCase {

	private MySQLHelper				db;
	private ArrayList<Appointment>	appointments	= AppointmentRandomizer
															.createAppointments();

	public MySQLHelperTest() {
	}

	public static void setUpBeforeClass() throws Exception {
	}

	public static void tearDownAfterClass() throws Exception {
	}

	public void setUp() {
		RenamingDelegatingContext context = new RenamingDelegatingContext(
				getContext(), "test_");
		db = new MySQLHelper(context);

		for (Appointment a : appointments)
			db.addAppointment(a);

	}

	public void tearDown() throws Exception {
		db.close();
		super.tearDown();
	}

	public void testAddAppointment() {
		fail("Not yet implemented");
	}

	public void testGetAppointment() {
		fail("Not yet implemented");
	}

	public void testGetClosestAppointment() {
		fail("Not yet implemented");
	}

}
