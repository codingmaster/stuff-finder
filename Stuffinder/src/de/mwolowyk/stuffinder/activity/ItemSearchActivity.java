package de.mwolowyk.stuffinder.activity;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import de.mwolowyk.stuffinder.R;
import de.mwolowyk.stuffinder.database.ItemTable;

public class ItemSearchActivity extends ListActivity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    
	    
        setContentView(R.xml.searchable);

	    // Get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      doSearch(query);
	    }
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.xml.searchable, menu);
	    super.onCreateOptionsMenu(menu);
	    return true;
	}
	
	private void doSearch(String query) {
		Cursor cursor;
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		queryBuilder.setTables(ItemTable.TABLE_NAME);
		queryBuilder.appendWhere(ItemTable.NAME + "=" + query);
	}

	
}
