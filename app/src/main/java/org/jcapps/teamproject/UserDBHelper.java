package org.jcapps.teamproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Database helper class to facilitate interactions with an SQLite DB.
 */
public class UserDBHelper extends SQLiteOpenHelper {

    private static UserDBHelper sInstance;
    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME = "userDB.db";
    public static final String TABLE_FILTER = "filter";
    public static final String TABLE_FAVORITE = "favorite";

    // Filter Table
    public static final String FILTER_SORT = "sort";
    public static final String FILTER_RADIUS = "radius_filter";
    public static final String FILTER_CATEGORY = "category_filter";

    // Favorite Table
    public static final String FAVORITE_ID = "id";
    public static final String FAVORITE_USER_RATING = "user_rating";

    private static final String CREATE_FILTER_TABLE =
            "CREATE TABLE " + TABLE_FILTER +
                    " (" + FILTER_SORT + " INTEGER," +
                    FILTER_RADIUS + " INTEGER," +
                    FILTER_CATEGORY + " TEXT" + ");";

    private static final String CREATE_FAVORITE_TABLE =
            "CREATE TABLE " + TABLE_FAVORITE +
                    " (" + FAVORITE_ID + " TEXT," +
                    FAVORITE_USER_RATING + " TEXT" + ");";

    private static final String SET_INITIAL_FILTERS =
            "INSERT INTO " + TABLE_FILTER + " VALUES(1,11,'');";

    private static final String DELETE_FILTERS =
            "DELETE FROM " + TABLE_FILTER + ";";

    private static final String DROP_FILTER_TABLE = "DROP TABLE IF EXISTS " + TABLE_FILTER + ";";
    private static final String DROP_FAVORITE_TABLE = "DROP TABLE IF EXISTS " + TABLE_FAVORITE + ";";

    // Singleton access method
    public static synchronized UserDBHelper getInstance(Context context) {

        // use application context to prevent leaking of activity context: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new UserDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    // constructor is private to prevent direct instantiation
    private UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FILTER_TABLE);
        db.execSQL(SET_INITIAL_FILTERS);
        db.execSQL(CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("UserDBHelper", "Upgrading Application's database from version " + oldVersion + " to " + newVersion +
                " to " + newVersion + ", which will destroy all user tables.");
        db.execSQL(DROP_FILTER_TABLE);
        db.execSQL(DROP_FAVORITE_TABLE);
        onCreate(db);
    }

    public void setFavorite(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FAVORITE_USER_RATING, 1);

        String selection = FAVORITE_ID + " = ?";
        String[] selectionArgs = {id};

        db.update(TABLE_FAVORITE,
                values,
                selection,
                selectionArgs
        );
    }

    public void removeFavorite(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FAVORITE_USER_RATING, 0);

        String selection = FAVORITE_ID + " = ?";
        String[] selectionArgs = {name};

        db.update(TABLE_FAVORITE,
                values,
                selection,
                selectionArgs
        );
    }

    public long setFilter(int radius_filter, int sort, String category_filter) {

        ContentValues filterV = new ContentValues();
        filterV.put(FILTER_RADIUS, radius_filter);
        filterV.put(FILTER_SORT, sort);
        filterV.put(FILTER_CATEGORY, category_filter);

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(DELETE_FILTERS);

        long returnId = db.insert(TABLE_FILTER, null, filterV);
        db.close();
        return returnId;
    }

    public Map<String, String> getFilter() {
        HashMap<String, String> filterList = new HashMap<String, String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from filter", null);

        cursor.moveToFirst();
        // Log.i("CURSOR_ROW", cursor.getString(0) + " : " + cursor.getString(1) + " : " + cursor.getString(2));

        filterList.put("sort", cursor.getString(0));
        filterList.put("radius_filter", cursor.getString(1));
        filterList.put("category_filter", cursor.getString(2));

        return filterList;
    }

}