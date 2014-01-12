package de.mwolowyk.stuffinder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "stuffinder.db";
	  private static final int DATABASE_VERSION = 4;

	  public DatabaseHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  // Method is called during creation of the database
	  @Override
	  public void onCreate(SQLiteDatabase database) {
		  PlaceTable.onCreate(database);
		  ItemTable.onCreate(database);
		  ItemPlaceTable.onCreate(database);
		  
	    
	  }

	  // Method is called during an upgrade of the database,
	  // e.g. if you increase the database version
	  @Override
	  public void onUpgrade(SQLiteDatabase database, int oldVersion,
	      int newVersion) {
		PlaceTable.onUpgrade(database, oldVersion, newVersion);
	    ItemTable.onUpgrade(database, oldVersion, newVersion);
	    ItemPlaceTable.onUpgrade(database, oldVersion, newVersion);
	  }

}
