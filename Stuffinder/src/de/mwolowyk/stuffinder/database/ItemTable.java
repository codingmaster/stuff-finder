package de.mwolowyk.stuffinder.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ItemTable {
	
	public static final String TABLE_ITEMS = "item";
	public static final String ITEM_ID = "_id";
	public static final String ITEM_CATEGORY = "category";
	public static final String ITEM_NAME = "name";
	public static final String ITEM_DESCRIPTION = "description";

	private static final String DATABASE_CREATE = "create table "
			+ TABLE_ITEMS
			 + "(" 
		      + ITEM_ID + " integer primary key autoincrement, " 
		      + ITEM_CATEGORY + " text not null, " 
		      + ITEM_NAME + " text not null," 
		      + ITEM_DESCRIPTION + " text not null" 
		      + ");";

	
	public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
	      int newVersion) {
	    Log.w(ItemTable.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
	    onCreate(database);
	  }

}
