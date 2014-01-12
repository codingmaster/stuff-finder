package de.mwolowyk.stuffinder;

import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public abstract class BasicDetailActivity extends Activity {

	// create the menu based on the XML defintion
	public boolean onCreateOptionsMenu(Menu menu, int menuId) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(menuId, menu);
		super.onCreateOptionsMenu(menu);
		return true;
	}

	protected abstract String getElementContentType();

	protected abstract String getSetContentType();

	protected abstract String getTitleString();

	protected abstract Uri getUri();

	protected abstract void setUri(Uri uri);

	protected abstract void fillData(Uri uri);

	protected abstract void saveState();

	@SuppressLint("NewApi")
	protected void saveState(ContentValues values, Uri contentUri) {
		Uri uri = getUri();
		if (!getTitleString().isEmpty()) {
			if (uri == null) {
				// New place
				uri = getContentResolver().insert(contentUri, values);
				setUri(uri);
			} else {
				// Update place
				getContentResolver().update(uri, values, null, null);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		String title = getTitleString();
		switch (item.getItemId()) {

		case R.id.accept:
			if (TextUtils.isEmpty(title)) {
				makeToast();
			} else {

				setResult(RESULT_OK);
				finish();
			}
			return true;

		case R.id.cancel:
			Intent i = new Intent(this, ItemsOverviewActivity.class);
			startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}

	private void makeToast() {
		Toast.makeText(this, "@string/no_name", Toast.LENGTH_LONG).show();
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putParcelable(getElementContentType(), getUri());
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

}
