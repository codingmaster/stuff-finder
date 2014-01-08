package de.mwolowyk.stufffinder;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import de.mwolowyk.stuffinder.contentprovider.MyItemContentProvider;
import de.mwolowyk.stuffinder.database.ItemTable;

@SuppressLint("NewApi")
public class ItemsOverviewActivity extends ListActivity implements
		LoaderCallbacks<Cursor> {
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int DELETE_ID = Menu.FIRST + 1;
	// private Cursor cursor;
	private SimpleCursorAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_list);
		this.getListView().setDividerHeight(2);
		fillData();
		registerForContextMenu(getListView());
	}

	// create the menu based on the XML defintion
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.listmenu, menu);
		super.onCreateOptionsMenu(menu);
		return true;
	}

	// Reaction to the menu selection
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.insert:
			createItem();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			Uri uri = Uri.parse(MyItemContentProvider.CONTENT_URI + "/"
					+ info.id);
			getContentResolver().delete(uri, null, null);
			fillData();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	private void createItem() {
		Intent i = new Intent(this, ItemDetailActivity.class);
		startActivity(i);
	}
	
	 // Opens the second activity if an entry is clicked
	  @Override
	  protected void onListItemClick(ListView l, View v, int position, long id) {
	    super.onListItemClick(l, v, position, id);
	    Intent i = new Intent(this, ItemDetailActivity.class);
	    Uri todoUri = Uri.parse(MyItemContentProvider.CONTENT_URI + "/" + id);
	    i.putExtra(MyItemContentProvider.CONTENT_ITEM_TYPE, todoUri);

	    startActivity(i);
	  }
	  
	  private void fillData() {

		    // Fields from the database (projection)
		    // Must include the _id column for the adapter to work
		    String[] from = new String[] { ItemTable.ITEM_NAME, ItemTable.ITEM_DESCRIPTION };
		    // Fields on the UI to which we map
		    int[] to = new int[] { R.id.name };

		    getLoaderManager().initLoader(0, null, this);
		    adapter = new SimpleCursorAdapter(this, R.layout.item_row, null, from,
		        to, 0);

		    setListAdapter(adapter);
		  }

	  @Override
	  public void onCreateContextMenu(ContextMenu menu, View v,
	      ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    menu.add(0, DELETE_ID, 0, R.string.delete);
	  }
	  
	// creates a new loader after the initLoader () call
	  @Override
	  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
	    String[] projection = { ItemTable.ITEM_ID, ItemTable.ITEM_NAME, ItemTable.ITEM_DESCRIPTION};
	    CursorLoader cursorLoader = new CursorLoader(this,
	        MyItemContentProvider.CONTENT_URI, projection, null, null, null);
	    return cursorLoader;
	  }
	  
	  
	  @Override
	  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
	    adapter.swapCursor(data);
	  }

	  @Override
	  public void onLoaderReset(Loader<Cursor> loader) {
	    // data is not available anymore, delete reference
	    adapter.swapCursor(null);
	  }
}
