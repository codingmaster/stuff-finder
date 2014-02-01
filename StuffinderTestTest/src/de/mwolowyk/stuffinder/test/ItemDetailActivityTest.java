package de.mwolowyk.stuffinder.test;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import de.mwolowyk.stuffinder.activity.ItemDetailActivity;

public class ItemDetailActivityTest extends
		ActivityInstrumentationTestCase2<ItemDetailActivity> {

	private static final int ADAPTER_COUNT = 4;
	public static final int INITIAL_POSITION = 0;
	public static final int TEST_POSITION = 1;

	private ItemDetailActivity mActivity;
	private Spinner category;
	private EditText name;
	private EditText description;
	private SpinnerAdapter mPlanetData;
	private String mSelection;
	private int mPos;
	private Instrumentation mInstrumentation;

	public ItemDetailActivityTest() {
		super(ItemDetailActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mInstrumentation = getInstrumentation();

		setActivityInitialTouchMode(false);

		mActivity = getActivity();

		category = (Spinner) mActivity
				.findViewById(de.mwolowyk.stuffinder.R.id.item_edit_category);
		name = (EditText)mActivity.findViewById(de.mwolowyk.stuffinder.R.id.item_edit_name);
		description = (EditText)mActivity.findViewById(de.mwolowyk.stuffinder.R.id.item_edit_description);


	} // end of setUp() method definition

//	public void testPreConditions() {
//		assertTrue(mSpinner.getOnItemSelectedListener() != null);
//		assertTrue(mPlanetData != null);
//		assertEquals(mPlanetData.getCount(), ADAPTER_COUNT);
//	}

	public void testNewItem() {

		mActivity.runOnUiThread(new Runnable() {
			public void run() {
				name.requestFocus();
				name.setText("test name");
				
				category.requestFocus();
				category.setSelection(TEST_POSITION);
				
				description.requestFocus();
				description.setText("test description");
				mActivity.saveState();

			} // end of run() method definition
		} // end of anonymous Runnable object instantiation
				); // end of invocation of runOnUiThread

		mInstrumentation.waitForIdleSync();
//		this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
//		for (int i = 1; i <= TEST_POSITION; i++) {
//			this.sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
//		} // end of for loop
//
//		this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
//
//		mPos = category.getSelectedItemPosition();
//		mSelection = (String) category.getItemAtPosition(mPos);
//		Log.println(Log.INFO, "tag", "SpinnerResult: " + mSelection);
//		assertNotNull(mSelection);
	} // end of testSpinnerUI() method definition

}
