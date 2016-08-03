package org.jcapps.teamproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Database helper class to facilitate interactions with an SQLite DB.
 */
public class FilterDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userDB.db";
    public static final String TABLE_FILTER = "filter";

    public static final String COLUMN_FILTER_SORT = "sort";
    public static final String COLUMN_FILTER_RADIUS = "radius_filter";
    public static final String COLUMN_FILTER_CATEGORY = "category_filter";

    public FilterDBHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FILTER_TABLE = "CREATE TABLE " +
                TABLE_FILTER + "("
                + COLUMN_FILTER_SORT + " INTEGER,"
                + COLUMN_FILTER_RADIUS + " INTEGER,"
                + COLUMN_FILTER_CATEGORY + " TEXT"
                + ")";
        db.execSQL(CREATE_FILTER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILTER);
        onCreate(db);
    }

    public long insert(Filter filter) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FILTER_SORT, filter.getSort());
        cv.put(COLUMN_FILTER_RADIUS, filter.getRadius_filter());
        cv.put(COLUMN_FILTER_CATEGORY, filter.getCategory_filter());

        long ret = database.insert(TABLE_FILTER, null, cv);
        database.close();

        return ret;
    }

    public ArrayList<Filter> getAll() {
        ArrayList<Filter> filters = new ArrayList<>();

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(String.format("SELECT * FROM %s", TABLE_FILTER), null);

            if (cursor.moveToFirst()) {

                while (!cursor.isAfterLast()) {

                    Filter filter = new Filter(
                            cursor.getInt(cursor.getColumnIndex(COLUMN_FILTER_SORT)),
                            cursor.getInt(cursor.getColumnIndex(COLUMN_FILTER_RADIUS)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_FILTER_CATEGORY))
                    );

                    filters.add(filter);
                    cursor.moveToNext();
                }
            }
        }
        finally {
            if (cursor != null)
                cursor.close();
            else
                Log.d("RRG", "Cursor is null");
        }

        return filters;
    }

    public void deleteFilter() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("delete from " + TABLE_FILTER);
        database.close();
    }
}