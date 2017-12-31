package com.gadgetscure.gadgetscure.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.gadgetscure.gadgetscure.R;
import com.gadgetscure.gadgetscure.data.DbContract;

/**
 * Created by awasthi's on 12/16/2017.
 */

public class ReceiptsCursorAdapter extends CursorAdapter {

    public ReceiptsCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView refTextView = (TextView) view.findViewById(R.id.refno);
        TextView deviceTextView = (TextView) view.findViewById(R.id.device);
        TextView issueTextView = (TextView) view.findViewById(R.id.issue);
        TextView dateTextView = (TextView) view.findViewById(R.id.date);

        // Find the columns of pet attributes that we're interested in
        int refColumnIndex = cursor.getColumnIndex(DbContract.DbEntry.COLUMN_REF);
        int deviceColumnIndex = cursor.getColumnIndex(DbContract.DbEntry.COLUMN_DEVICE);
        int issueColumnIndex=cursor.getColumnIndex(DbContract.DbEntry.COLUMN_ISSUE);
        int dateColumnIndex=cursor.getColumnIndex(DbContract.DbEntry.COLUMN_DATE);

        // Read the pet attributes from the Cursor for the current pet
        String refNo = cursor.getString(refColumnIndex);
        String deviceString = cursor.getString(deviceColumnIndex);
        String issue = cursor.getString(issueColumnIndex);
        String dateString = cursor.getString(dateColumnIndex);

        // If the pet breed is empty string or null, then use some default text

        // Update the TextViews with the attributes for the current pet
        refTextView.setText(refNo);
        deviceTextView.setText(deviceString);
        issueTextView.setText(issue);
        dateTextView.setText(dateString);
    }
}
