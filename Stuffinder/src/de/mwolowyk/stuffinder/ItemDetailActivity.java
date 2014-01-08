package de.mwolowyk.stuffinder;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import de.mwolowyk.stuffinder.contentprovider.MyContentProvider;
import de.mwolowyk.stuffinder.database.ItemTable;

public class ItemDetailActivity extends Activity {
	private Spinner mCategory;
	private EditText mTitleText;
	private EditText mBodyText;

	private Uri itemUri;
	
	@Override
	  protected void onCreate(Bundle bundle) {
	    super.onCreate(bundle);
	    setContentView(R.layout.item_edit);

	    mCategory = (Spinner) findViewById(R.id.category);
	    mTitleText = (EditText) findViewById(R.id.item_edit_name);
	    mBodyText = (EditText) findViewById(R.id.item_edit_description);
	    Bundle extras = getIntent().getExtras();
	    
	    
	    // check from the saved Instance
	    itemUri = (bundle == null) ? null : (Uri) bundle
	        .getParcelable(MyContentProvider.CONTENT_ITEM_TYPE);

	    // Or passed from the other activity
	    if (extras != null) {
	      itemUri = extras
	          .getParcelable(MyContentProvider.CONTENT_ITEM_TYPE);

	      fillData(itemUri);
	    }
	   

	  }
	
	// create the menu based on the XML defintion
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.editmenu, menu);
			super.onCreateOptionsMenu(menu);
			return true;
		}
	
		
		// Reaction to the menu selection
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.accept:
			   	  if (TextUtils.isEmpty(mTitleText.getText().toString())) {
		              makeToast();
		            } else {
		              setResult(RESULT_OK);
		              finish();
		            }
				return true;
				
			case R.id.cancel:
				Intent i = new Intent(this, ItemsOverviewActivity.class);
				startActivity(i);
			}
			return super.onOptionsItemSelected(item);
		}

	private void fillData(Uri uri) {
	    String[] projection = { ItemTable.NAME,
	        ItemTable.DESCRIPTION, ItemTable.CATEGORY };
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
	
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    saveState();
	    outState.putParcelable(MyContentProvider.CONTENT_ITEM_TYPE, itemUri);
	  }

	  @Override
	  protected void onPause() {
	    super.onPause();
	    saveState();
	  }

	  private void saveState() {
	    String category = (String) mCategory.getSelectedItem();
	    String summary = mTitleText.getText().toString();
	    String description = mBodyText.getText().toString();

	    // only save if either summary or description
	    // is available

	    if (description.length() == 0 && summary.length() == 0) {
	      return;
	    }

	    ContentValues values = new ContentValues();
	    values.put(ItemTable.CATEGORY, category);
	    values.put(ItemTable.NAME, summary);
	    values.put(ItemTable.DESCRIPTION, description);

	    if (itemUri == null) {
	      // New item
	      itemUri = getContentResolver().insert(MyContentProvider.CONTENT_URI, values);
	    } else {
	      // Update item
	      getContentResolver().update(itemUri, values, null, null);
	    }
	  }

	  private void makeToast() {
		    Toast.makeText(ItemDetailActivity.this, "Please maintain a summary",
		        Toast.LENGTH_LONG).show();
		  }

}
