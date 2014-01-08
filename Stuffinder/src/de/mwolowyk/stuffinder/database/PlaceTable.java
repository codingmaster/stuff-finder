package de.mwolowyk.stuffinder.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PlaceTable extends CustomTable{
	public static final String TABLE_NAME = "place";
	public static final String DESCRIPTION = "description";

	private static final String DATABASE_CREATE = "create table "
			+ TABLE_NAME
			 + "(" 
		      + ID + " integer primary key autoincrement, " 
		      + NAME + " text not null," 
		      + DESCRIPTION + " text not null" 
		      + ");";
	
	public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
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
