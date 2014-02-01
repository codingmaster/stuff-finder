package de.mwolowyk.stuffinder.contentprovider;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import de.mwolowyk.stuffinder.database.CustomTable;
import de.mwolowyk.stuffinder.database.PlaceTable;

public class PlaceContentProvider extends BasicContentProvider {
	private static final int PLACES = 60;
	private static final int PLACE_ID = 70;
	private static final int PLACE_SEARCH = 80;
	


	private static final String PLACES_PATH = "PLACEs";
	protected static final String AUTHORITY = "de.mwolowyk.stuffinder.contentprovider.places";
	
	public static final Uri PLACES_CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + PLACES_PATH);


	public static final String PLACES_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/" + PLACES_PATH;
	public static final String PLACE_CONTENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/PLACE";
	
	static{
		sURIMatcher.addURI(AUTHORITY, PLACES_PATH, PLACES);
		sURIMatcher.addURI(AUTHORITY, PLACES_PATH + "/#", PLACE_ID);
		sURIMatcher.addURI(AUTHORITY, PLACES_PATH + "/#", PLACE_SEARCH);
	}
	
	public String getTableName() {
		return PlaceTable.TABLE_NAME;
	}
	
	public boolean isSingleEntity(Uri uri){
		int uriType = sURIMatcher.match(uri);
		return uriType == PLACE_ID;
	}
	
	@Override
	protected boolean isSearchEntity(Uri uri) {
		int uriType = sURIMatcher.match(uri);
		return uriType == PLACE_SEARCH;
	}
	
	@Override
	protected String getBasicPath() {
		return PLACES_PATH;
	}
	
	protected SQLiteQueryBuilder getQueryBuilder(Uri uri) {
		// Uisng SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		String tableName = getTableName();
		queryBuilder.setTables(tableName);
		switch (sURIMatcher.match(uri)) {
		case PLACES:
			break;
		case PLACE_ID:
			queryBuilder.appendWhere(CustomTable.ID + "="
					+ uri.getLastPathSegment());
			break;
		case PLACE_SEARCH:
			break;
		default:
			throw new IllegalArgumentException("Unknown Uri: " + uri);
		}
		return queryBuilder;
	}


}
