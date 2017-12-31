package com.gadgetscure.gadgetscure.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.gadgetscure.gadgetscure.R;
import com.gadgetscure.gadgetscure.adapters.ReceiptsCursorAdapter;
import com.gadgetscure.gadgetscure.data.DbContract;

public class OrderActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    ReceiptsCursorAdapter mCursorAdapter;
    private static final int RECEIPT_LOADER = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.historytoolbar);
        toolbar.setTitle("Booking History");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        ListView receiptsListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        receiptsListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        // There is no pet data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new ReceiptsCursorAdapter(this, null);
        receiptsListView.setAdapter(mCursorAdapter);
        getLoaderManager().initLoader(RECEIPT_LOADER, null, this);








    }
    private void deleteAllReceipts() {
        int rowsDeleted = getContentResolver().delete(DbContract.DbEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.receipts_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option// Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllReceipts();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                DbContract.DbEntry._ID,
                DbContract.DbEntry.COLUMN_REF,
                DbContract.DbEntry.COLUMN_DEVICE,
                DbContract.DbEntry.COLUMN_ISSUE,
                DbContract.DbEntry.COLUMN_DATE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                DbContract.DbEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);


    }
}
