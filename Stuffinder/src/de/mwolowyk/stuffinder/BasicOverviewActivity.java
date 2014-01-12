package de.mwolowyk.stuffinder;

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

@SuppressLint("NewApi")
public abstract class BasicOverviewActivity extends ListActivity implements
LoaderCallbacks<Cursor>{
	private static final int DELETE_ID = Menu.FIRST + 1;
	// private Cursor cursor;
	protected SimpleCursorAdapter adapter;
	
	
	public abstract Uri getContentUri();
	public abstract String getContentType();
	
	public void onCreate(Bundle savedInstanceState, int layout){
		super.onCreate(savedInstanceState);
		setContentView(layout);
		this.getListView().setDividerHeight(2);
		fillData();
		registerForContextMenu(getListView());
	}
	
	protected abstract void fillData();
	
	
	public boolean onCreateOptionsMenu(Menu menu, int menuId){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(menuId, menu);
		super.onCreateOptionsMenu(menu);
		return true;
	}
	
	
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			Uri uri = Uri.parse(getContentUri() + "/"
					+ info.id);
			getContentResolver().delete(uri, null, null);
			fillData();
			return true;
		}
		return super.onContextItemSelected(item);
	}	
	
	 // Opens the second activity if an entry is clicked
	  @Override
	  protected void onListItemClick(ListView l, View v, int position, long id) {
	    super.onListItemClick(l, v, position, id);
	    Intent i = new Intent(this, ItemDetailActivity.class);
	    Uri contentUri = Uri.parse(getContentUri() + "/" + id);
	    i.putExtra(getContentType(), contentUri);

	    startActivity(i);
	  }
	  
	  @Override
	  public void onCreateContextMenu(ContextMenu menu, View v,
	      ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    menu.add(0, DELETE_ID, 0, R.string.delete);
	  }
	  
	  public Loader<Cursor> onCreateLoader(int id, Bundle args, String[] projection) {
		  CursorLoader cursorLoader = new CursorLoader(this,
				  getContentUri(), projection, null, null, null);
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
