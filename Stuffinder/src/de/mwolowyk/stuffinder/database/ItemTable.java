package de.mwolowyk.stuffinder.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ItemTable extends CustomTable{
	
	public static final String TABLE_NAME = "item";
	public static final String CATEGORY = "category";
	public static final String DESCRIPTION = "description";
	public static final String PLACE = "place";
	public static final String ITEMS_WITH_PLACES_VIEW = "ItemsWithPlacesView"; 
	private static final String TRIGGER_NAME = "ItemsPlacesTrigger";
	

	private static final String DATABASE_CREATE = "create table "
			+ TABLE_NAME
			 + "(" 
		      + ID + " integer primary key autoincrement, " 
		      + CATEGORY + " text not null, " 
		      + NAME + " text not null, " 
		      + DESCRIPTION + " text not null, "
		      + PLACE + " integer, "
		      + "FOREIGN KEY (" + PLACE + ") REFERENCES " + PlaceTable.TABLE_NAME + " (" + PlaceTable.ID + ") "
		      + ");";

	
	private static final String CREATE_TRIGGER = "create trigger " + TRIGGER_NAME
			+ " BEFORE INSERT" 
			+ " ON " + TABLE_NAME 
			+ " FOR EACH ROW BEGIN"
			+ " SELECT CASE WHEN ((SELECT " + PlaceTable.ID + " FROM " + PlaceTable.TABLE_NAME 
			+ " WHERE " + PlaceTable.ID + " NOT NULL AND "+ PlaceTable.ID + "=new." + PLACE + " ) IS NULL)" 
			+ " THEN RAISE (ABORT, 'Item/Place Foreign key violation') END;"
			+ " END;"
			;
	
	private static final String CREATE_VIEW = "create view " + ITEMS_WITH_PLACES_VIEW
			+ " AS SELECT " + TABLE_NAME + "." + ID + " AS _id, " 
			+ TABLE_NAME + "." + NAME + ", " 
			+ TABLE_NAME + "." + CATEGORY + ", "
			+ TABLE_NAME + "." + DESCRIPTION + ", "
			+ PlaceTable.TABLE_NAME + "." + PlaceTable.NAME
			+ " FROM " + TABLE_NAME + " JOIN " + PlaceTable.TABLE_NAME
			+ " ON " + TABLE_NAME + "." + PLACE + " = " + PlaceTable.TABLE_NAME + "." + PlaceTable.ID;			
			; 

	
	public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	   // database.execSQL(CREATE_TRIGGER);
	    database.execSQL(CREATE_VIEW);
	    
	  }

	  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
	      int newVersion) {
		  
	    Log.w(ItemTable.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	    database.execSQL("DROP TRIGGER IF EXISTS " + TRIGGER_NAME);
	    database.execSQL("DROP VIEW IF EXISTS " + ITEMS_WITH_PLACES_VIEW);
	    
	    onCreate(database);
	  }

}
