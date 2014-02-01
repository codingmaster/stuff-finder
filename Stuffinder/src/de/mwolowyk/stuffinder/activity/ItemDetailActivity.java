package de.mwolowyk.stuffinder.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Spinner;
import de.mwolowyk.stuffinder.R;
import de.mwolowyk.stuffinder.contentprovider.ItemContentProvider;
import de.mwolowyk.stuffinder.database.ItemTable;

public class ItemDetailActivity extends BasicDetailActivity {

	protected Spinner mCategory;
	protected EditText mTitleText;
	protected EditText mBodyText;
	protected Uri uri;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		int layoutId = R.layout.item_edit;
		setContentView(layoutId);

		mTitleText = (EditText) findViewById(R.id.item_edit_name);
		mCategory = (Spinner) findViewById(R.id.item_edit_category);
		mBodyText = (EditText) findViewById(R.id.item_edit_description);

		Bundle extras = getIntent().getExtras();

		uri = (bundle == null) ? null : (Uri) bundle
				.getParcelable(getElementContentType());
		if (extras != null) {
			uri = extras.getParcelable(getElementContentType());
			fillData(uri);
		}
	}

	// create the menu based on the XML defintion
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu, R.menu.editmenu);

	}

	// Reaction to the menu selection

	protected void fillData(Uri uri) {
		String[] projection = { ItemTable.NAME, ItemTable.DESCRIPTION,
				ItemTable.CATEGORY };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		if (cursor != null) {
			cursor.moveToFirst();
			String category = cursor.getString(cursor
					.getColumnIndexOrThrow(ItemTable.CATEGORY));

			for (int i = 0; i < mCategory.getCount(); i++) {

				String s = (String) mCategory.getItemAtPosition(i);
				if (s.equalsIgnoreCase(category)) {
					mCategory.setSelection(i);
				}
			}

			mTitleText.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(ItemTable.NAME)));
			mBodyText.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(ItemTable.DESCRIPTION)));

			// always close the cursor
			cursor.close();
		}
	}

	public void saveState() {

		ContentValues values = new ContentValues();
		values.put(ItemTable.CATEGORY, (String) mCategory.getSelectedItem());
		values.put(ItemTable.NAME, mTitleText.getText().toString());
		values.put(ItemTable.DESCRIPTION, mBodyText.getText().toString());

		super.saveState(values, ItemContentProvider.ITEMS_CONTENT_URI);
	}

	@Override
	protected String getElementContentType() {
		return ItemContentProvider.ITEM_CONTENT_TYPE;
	}

	@Override
	protected String getSetContentType() {
		return ItemContentProvider.ITEMS_CONTENT_TYPE;
	}

	protected String getTitleString() {
		String result = "";
		if (mTitleText != null) {
			result = mTitleText.getText().toString();
		}
		return result;
	}
	
	@Override
	protected Uri getUri() {
		return uri;
	}
	
	@Override
	protected void setUri(Uri uri){
		this.uri = uri;
	}

}
