package de.mwolowyk.stuffinder.contentprovider;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import de.mwolowyk.stuffinder.database.CustomTable;
import de.mwolowyk.stuffinder.database.DatabaseHelper;
import de.mwolowyk.stuffinder.database.ItemTable;
import de.mwolowyk.stuffinder.database.PlaceTable;

public class MyContentProvider extends ContentProvider {

	private DatabaseHelper database;

	// used for the UriMacher
	private static final int ITEMS = 10;
	private static final int ITEM_ID = 20;
	private static final int PLACES = 30;
	private static final int PLACE_ID = 40;

	private static final String AUTHORITY = "de.mwolowyk.stuffinder.contentprovider.all";

	private static final String BASE_PATH = "ITEMs";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);

	public static final String CONTENT_ITEMS_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/ITEMs";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/ITEM";

	public static final String CONTENT_PLACES_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/PLACEs";
	public static final String CONTENT_PLACE_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/PLACE";

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, ITEMS);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", ITEM_ID);
	//	sURIMatcher.addURI(AUTHORITY, BASE_PATH, PLACES);
	//	sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", PLACE_ID);
		
	}

	@Override
	public boolean onCreate() {
		database = new DatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		// Uisng SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// check if the caller has requested a column which does not exists
	//	checkColumns(projection);

		// Set the table
		int uriType = sURIMatcher.match(uri);
		
		
		String tableName = getTableName(uriType);
		queryBuilder.setTables(tableName);

		switch (uriType) {
		case ITEMS:
		case PLACES:
			break;
		case ITEM_ID:
		case PLACE_ID:
			// adding the ID to the original query
			queryBuilder.appendWhere(CustomTable.ID + "="
					+ uri.getLastPathSegment());
			break;
		
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		// make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}
	
	private String getTableName(int uriType){
		String tableName = PlaceTable.TABLE_NAME;
		if(uriType == ITEMS || uriType == ITEM_ID){
			tableName = ItemTable.TABLE_NAME;
		}
		else if(uriType == PLACES || uriType == PLACE_ID){
			tableName = PlaceTable.TABLE_NAME;
		}
		else{
			throw new IllegalArgumentException("Unknown URI: " + uriType);
		}
		return tableName;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		String tableName = getTableName(uriType);
		
		long id = 0;
		switch (uriType) {
		case ITEMS:
		case PLACES:
			id = sqlDB.insert(tableName, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		String tableName = getTableName(uriType);
		
		switch (uriType) {
		case ITEMS:
		case PLACES:
			rowsDeleted = sqlDB.delete(tableName, selection,
					selectionArgs);
			break;
			
		case ITEM_ID:
		case PLACE_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(tableName, CustomTable.ID
						+ "=" + id, null);
			} else {
				rowsDeleted = sqlDB.delete(tableName, CustomTable.ID
						+ "=" + id + " and " + selection, selectionArgs);
			}
			break;
	
			
	
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		int uriType = sURIMatcher.match(uri);
		String tableName = getTableName(uriType);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
		case ITEMS:
		case PLACES:
			rowsUpdated = sqlDB.update(tableName, values, selection,
					selectionArgs);
			break;
		case ITEM_ID:
		case PLACE_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(tableName, values,
						CustomTable.ID + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(tableName, values,
						CustomTable.ID + "=" + id + " and " + selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	private void checkColumns(String[] projection) {
		String[] available = { ItemTable.CATEGORY, ItemTable.DESCRIPTION,
				ItemTable.NAME, ItemTable.ID };
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(available));
			// check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException(
						"Unknown columns in projection");
			}
		}
	}

}
