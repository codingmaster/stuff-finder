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
import de.mwolowyk.stuffinder.contentprovider.PlaceContentProvider;
import de.mwolowyk.stuffinder.database.PlaceTable;

@SuppressLint("NewApi")
public class PlacesOverviewActivity extends BasicOverviewActivity {

	@Override
	public Uri getContentUri() {
		return PlaceContentProvider.PLACES_CONTENT_URI;
	}

	@Override
	public String getContentType() {
		return PlaceContentProvider.PLACE_CONTENT_TYPE;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.place_list);
	}

	@Override
	protected void fillData() {
		String[] from = new String[] { PlaceTable.NAME, PlaceTable.DESCRIPTION };
		int[] to = new int[] { R.id.name };
		getLoaderManager().initLoader(0, null, this);
		adapter = new SimpleCursorAdapter(this, R.layout.place_row, null, from,
				to, 0);
		setListAdapter(adapter);
	}

	// create the menu based on the XML defintion
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return onCreateOptionsMenu(menu, R.menu.place_listmenu);
	}
	
	// Reaction to the menu selection
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.insert:
				createPlace();
				return true;
			case R.id.items:
				listItems();
				return true;
				
			}
			return super.onOptionsItemSelected(item);
		}
		
	private void createPlace(){
		Intent i = new Intent(this, PlaceDetailActivity.class);
		startActivity(i);
	}
	
	private void listItems(){
		Intent i = new Intent(this, ItemsOverviewActivity.class);
		startActivity(i);
	}

	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = {PlaceTable.ID, PlaceTable.NAME, PlaceTable.DESCRIPTION};
		return onCreateLoader(id, args, projection);
	}

	
}
