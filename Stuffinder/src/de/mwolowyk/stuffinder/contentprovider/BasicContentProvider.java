package de.mwolowyk.stuffinder.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import de.mwolowyk.stuffinder.database.CustomTable;
import de.mwolowyk.stuffinder.database.DatabaseHelper;

public abstract class BasicContentProvider extends ContentProvider {

	protected DatabaseHelper database;

	// used for the UriMacher


	protected static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	protected abstract String getTableName();
	protected abstract boolean isSingleEntity(Uri uri);
	protected abstract String getBasicPath();
	
	@Override
	public boolean onCreate() {
		database = new DatabaseHelper(getContext());
		return false;
	}


	@Override
	public String getType(Uri uri) {
		return null;
	}


	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
				// Uisng SQLiteQueryBuilder instead of query() method
				SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
			
				String tableName = getTableName();
				queryBuilder.setTables(tableName);
			
				if(isSingleEntity(uri)){
					queryBuilder.appendWhere(CustomTable.ID + "="
							+ uri.getLastPathSegment());
				}
				
				SQLiteDatabase db = database.getWritableDatabase();
				Cursor cursor = queryBuilder.query(db, projection, selection,
						selectionArgs, null, null, sortOrder);
				// make sure that potential listeners are getting notified
				cursor.setNotificationUri(getContext().getContentResolver(), uri);
			
				return cursor;
			}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		String tableName = getTableName();
		long id = 0;
		if(!isSingleEntity(uri)){
			id = sqlDB.insert(tableName, null, values);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(getBasicPath() + "/" + id);
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		String tableName = getTableName();
		
		if(!isSingleEntity(uri)){
			rowsDeleted = sqlDB.delete(tableName, selection,
					selectionArgs);
		}
		else{
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(tableName, CustomTable.ID
						+ "=" + id, null);
			} else {
				rowsDeleted = sqlDB.delete(tableName, CustomTable.ID
						+ "=" + id + " and " + selection, selectionArgs);
			}
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		String tableName = getTableName();
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsUpdated = 0;
		if(!isSingleEntity(uri)){
			rowsUpdated = sqlDB.update(tableName, values, selection,
					selectionArgs);
		}
		else{
			String id = uri.getLastPathSegment();
			
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(tableName, values,
						CustomTable.ID + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(tableName, values,
						CustomTable.ID + "=" + id + " and " + selection,
						selectionArgs);
			}
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

}
