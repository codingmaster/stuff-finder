package de.mwolowyk.stufffinder;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import de.mwolowyk.stuffinder.contentprovider.MyItemContentProvider;
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
	    Button confirmButton = (Button) findViewById(R.id.accept);
	    Bundle extras = getIntent().getExtras();
	    
	    
	    // check from the saved Instance
	    itemUri = (bundle == null) ? null : (Uri) bundle
	        .getParcelable(MyItemContentProvider.CONTENT_ITEM_TYPE);

	    // Or passed from the other activity
	    if (extras != null) {
	      itemUri = extras
	          .getParcelable(MyItemContentProvider.CONTENT_ITEM_TYPE);

	      fillData(itemUri);
	    }
	    
	    confirmButton.setOnClickListener(new View.OnClickListener() {
	      public void onClick(View view) {

	    	  if (TextUtils.isEmpty(mTitleText.getText().toString())) {
	              makeToast();
	            } else {
	              setResult(RESULT_OK);
	              finish();
	            }
	      }

	    });

	  }
	
	private void fillData(Uri uri) {
	    String[] projection = { ItemTable.ITEM_NAME,
	        ItemTable.ITEM_DESCRIPTION, ItemTable.ITEM_CATEGORY };
	    Cursor cursor = getContentResolver().query(uri, projection, null, null,
	        null);
	    if (cursor != null) {
	      cursor.moveToFirst();
	      String category = cursor.getString(cursor
	          .getColumnIndexOrThrow(ItemTable.ITEM_CATEGORY));

	      for (int i = 0; i < mCategory.getCount(); i++) {

	        String s = (String) mCategory.getItemAtPosition(i);
	        if (s.equalsIgnoreCase(category)) {
	          mCategory.setSelection(i);
	        }
	      }

	      mTitleText.setText(cursor.getString(cursor
	          .getColumnIndexOrThrow(ItemTable.ITEM_NAME)));
	      mBodyText.setText(cursor.getString(cursor
	          .getColumnIndexOrThrow(ItemTable.ITEM_DESCRIPTION)));

	      // always close the cursor
	      cursor.close();
	    }
	  }
	
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    saveState();
	    outState.putParcelable(MyItemContentProvider.CONTENT_ITEM_TYPE, itemUri);
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
	    values.put(ItemTable.ITEM_CATEGORY, category);
	    values.put(ItemTable.ITEM_NAME, summary);
	    values.put(ItemTable.ITEM_DESCRIPTION, description);

	    if (itemUri == null) {
	      // New item
	      itemUri = getContentResolver().insert(MyItemContentProvider.CONTENT_URI, values);
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
