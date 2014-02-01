package de.mwolowyk.stuffinder.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;
import de.mwolowyk.stuffinder.R;
import de.mwolowyk.stuffinder.contentprovider.ItemContentProvider;
import de.mwolowyk.stuffinder.database.ItemTable;


@SuppressLint("NewApi")
public class ItemsOverviewActivity extends BasicOverviewActivity  {

	
	@Override
	public Uri getContentUri() {
		return ItemContentProvider.ITEMS_CONTENT_URI;
	}

	@Override
	public String getContentType() {
		return ItemContentProvider.ITEM_CONTENT_TYPE;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.item_list);
	}
	
	  protected void fillData() {

		    // Fields from the database (projection)
		    // Must include the _id column for the adapter to work
		    String[] from = new String[] { ItemTable.NAME, ItemTable.DESCRIPTION };
		    // Fields on the UI to which we map
		    int[] to = new int[] { R.id.name };

		    getLoaderManager().initLoader(0, null, this);
		    adapter = new SimpleCursorAdapter(this, R.layout.item_row, null, from,
		        to, 0);

		    setListAdapter(adapter);
		  }

	// create the menu based on the XML defintion
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return onCreateOptionsMenu(menu, R.menu.item_listmenu);
	}

	// Reaction to the menu selection
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.insert:
			createItem();
			return true;
		case R.id.places:
			listPlaces();
			return true;
			
		}
		return super.onOptionsItemSelected(item);
	}

	private void createItem() {
		Intent i = new Intent(this, ItemDetailActivity.class);
		startActivity(i);
	}
	
	private void listPlaces(){
		Intent i = new Intent(this, PlacesOverviewActivity.class);
		startActivity(i);
	}
	
	  
	// creates a new loader after the initLoader () call
	  @Override
	  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
	    String[] projection = { ItemTable.ID, ItemTable.NAME, ItemTable.DESCRIPTION};
	    return onCreateLoader(id, args, projection);
	  }
	  
	  


	

	
}
