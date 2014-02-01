package de.mwolowyk.stuffinder.test;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import de.mwolowyk.stuffinder.activity.PlaceDetailActivity;

public class PlaceDetailActivityTest extends
ActivityInstrumentationTestCase2<PlaceDetailActivity>{
	
	private PlaceDetailActivity mActivity;
	private EditText name;
	private EditText description;
	private Instrumentation mInstrumentation;
	
	public PlaceDetailActivityTest(){
		super(PlaceDetailActivity.class);
	}
	
	protected void setUp() throws Exception{
		super.setUp();
		mInstrumentation = getInstrumentation();
		
		setActivityInitialTouchMode(false);
		
		mActivity = getActivity();
		name = (EditText)mActivity.findViewById(de.mwolowyk.stuffinder.R.id.place_edit_name);
		description = (EditText)mActivity.findViewById(de.mwolowyk.stuffinder.R.id.place_edit_description);

	}
	
	public void testNewPlace(){
		mActivity.runOnUiThread(new Runnable() {
			public void run() {
				name.requestFocus();
				name.setText("test place");
				
				description.requestFocus();
				description.setText("test place description");
				mActivity.saveState();

			} // end of run() method definition
		} // end of anonymous Runnable object instantiation
				); // end of invocation of runOnUiThread

		mInstrumentation.waitForIdleSync();
	}

}
