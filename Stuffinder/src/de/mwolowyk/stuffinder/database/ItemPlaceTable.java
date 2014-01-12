package de.mwolowyk.stuffinder.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ItemPlaceTable {

	private static final String TABLE_NAME = "item_place";
	private static final String KEY_ID = "_id";
	private static final String ITEM_ID = "item_id";
	private static final String PLACE_ID = "place_id";

	private static final String CREATE_TABLE_ITEM_PLACE = "CREATE TABLE "
			+ TABLE_NAME + "(" + KEY_ID + " integer primary key, " + ITEM_ID
			+ " integer, " + PLACE_ID + " integer) ";
	
	public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(CREATE_TABLE_ITEM_PLACE);

	    
	  }

	  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
	      int newVersion) {
		  
	    Log.w(ItemTable.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	   
	    
	    onCreate(database);
	  }

}
