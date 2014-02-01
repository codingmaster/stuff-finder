package de.mwolowyk.stuffinder.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import de.mwolowyk.stuffinder.R;
import de.mwolowyk.stuffinder.contentprovider.PlaceContentProvider;
import de.mwolowyk.stuffinder.database.PlaceTable;

public class PlaceDetailActivity extends BasicDetailActivity {

	protected EditText mTitleText;
	protected EditText mBodyText;
	protected Uri uri;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		int layoutId = R.layout.place_edit;
		setContentView(layoutId);
		
		mTitleText = (EditText) findViewById(R.id.place_edit_name);
		mBodyText = (EditText) findViewById(R.id.place_edit_description);

		Bundle extras = getIntent().getExtras();

		uri = (bundle == null) ? null : (Uri) bundle
				.getParcelable(getElementContentType());
		if (extras != null) {
			uri = extras.getParcelable(getElementContentType());
			fillData(uri);
		}
	}

	@Override
	protected void fillData(Uri uri) {
		String[] projection = { PlaceTable.NAME, PlaceTable.DESCRIPTION };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		if (cursor != null) {
			cursor.moveToFirst();

			mTitleText.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(PlaceTable.NAME)));
			mBodyText.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(PlaceTable.DESCRIPTION)));

			// always close the cursor
			cursor.close();
		}
	}

	@Override
	public void saveState() {

		ContentValues values = new ContentValues();
		values.put(PlaceTable.NAME, getTitleString());
		values.put(PlaceTable.DESCRIPTION, mBodyText.getText().toString());

		super.saveState(values, PlaceContentProvider.PLACES_CONTENT_URI);
			
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu, R.menu.editmenu);
	}

	@Override
	protected String getElementContentType() {
		return PlaceContentProvider.PLACE_CONTENT_TYPE;
	}

	@Override
	protected String getSetContentType() {
		return PlaceContentProvider.PLACES_CONTENT_TYPE;
	}

	@Override
	protected String getTitleString(){
		String result = "";
		if(mTitleText != null){
			result = mTitleText.getText().toString();
		}
		return result;
	}

	@Override
	protected Uri getUri() {
		return uri;
	}

	@Override
	protected void setUri(Uri uri) {
		this.uri = uri;
	}
	

}
