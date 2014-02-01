package de.mwolowyk.stuffinder.contentprovider;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import de.mwolowyk.stuffinder.database.CustomTable;
import de.mwolowyk.stuffinder.database.ItemTable;

public class ItemContentProvider extends BasicContentProvider {

	private static final int ITEMS = 10;
	private static final int ITEM_ID = 20;
	private static final int ITEM_SEARCH = 30;

	private static final String ITEMS_PATH = "ITEMs";
	protected static final String AUTHORITY = "de.mwolowyk.stuffinder.contentprovider.items";

	public static final Uri ITEMS_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + ITEMS_PATH);

	public static final String ITEMS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/" + ITEMS_PATH;
	public static final String ITEM_CONTENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/ITEM";

	static {
		sURIMatcher.addURI(AUTHORITY, ITEMS_PATH, ITEMS);
		sURIMatcher.addURI(AUTHORITY, ITEMS_PATH + "/#", ITEM_ID);
		sURIMatcher.addURI(AUTHORITY, ITEMS_PATH + "/#", ITEM_SEARCH);
	}

	public CustomTable getTableClass() {

		return new ItemTable();
	}

	public String getTableName() {
		return ItemTable.TABLE_NAME;
	}

	public boolean isSingleEntity(Uri uri) {
		int uriType = sURIMatcher.match(uri);
		return uriType == ITEM_ID;
	}

	public boolean isSearchEntity(Uri uri) {
		int uriType = sURIMatcher.match(uri);
		return uriType == ITEM_SEARCH;
	}

	@Override
	protected String getBasicPath() {
		return ITEMS_PATH;
	}

	protected SQLiteQueryBuilder getQueryBuilder(Uri uri) {
		// Uisng SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		String tableName = getTableName();
		queryBuilder.setTables(tableName);
		switch (sURIMatcher.match(uri)) {
		case ITEMS:
			break;
		case ITEM_ID:
			queryBuilder.appendWhere(CustomTable.ID + "="
					+ uri.getLastPathSegment());
			break;
		case ITEM_SEARCH:
			String condition = getCondition(uri);
			queryBuilder.appendWhere(condition);
			break;
		default:
			throw new IllegalArgumentException("Unknown Uri: " + uri);
		}
		return queryBuilder;
	}

	private String getCondition(Uri uri) {
		String conditionValue = uri.getLastPathSegment();
		return ItemTable.NAME + "=" + conditionValue;
	}
}
