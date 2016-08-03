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
public class UserDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userDB.db";
    public static final String TABLE_FAVORITES = "favorites";

    public static final String COLUMN_FAV_ID = "_id";
    public static final String COLUMN_FAV_NAME = "name";
    public static final String COLUMN_FAV_USERRATING = "userrating";

    public UserDBHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FAVORITES_TABLE = "CREATE TABLE " +
                TABLE_FAVORITES + "("
                + COLUMN_FAV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FAV_NAME + " TEXT,"
                + COLUMN_FAV_USERRATING + " TEXT"
                + ")";
        db.execSQL(CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    public long insert(Favorites fav) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FAV_ID, fav.getId());
        cv.put(COLUMN_FAV_NAME, fav.getName());
        cv.put(COLUMN_FAV_USERRATING, fav.getUserrating());

        long ret = database.insert(TABLE_FAVORITES, null, cv);
        database.close();

        return ret;
    }

    public ArrayList<Favorites> getAll() {
        ArrayList<Favorites> favorites = new ArrayList<>();

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(String.format("SELECT * FROM %s", TABLE_FAVORITES), null);

            if (cursor.moveToFirst()) {

                while (!cursor.isAfterLast()) {

                    Favorites favorite = new Favorites(
                            cursor.getString(cursor.getColumnIndex(COLUMN_FAV_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_FAV_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_FAV_USERRATING))
                    );

                    favorites.add(favorite);
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

        return favorites;
    }

    public long delete(Favorites fav) {
        SQLiteDatabase database = this.getWritableDatabase();
        long ret = database.delete(TABLE_FAVORITES, COLUMN_FAV_ID + "=?", new String[] { fav.getId() });
        database.close();
        return ret;
    }

    public void deleteAll() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("delete from " + TABLE_FAVORITES);
        database.close();
    }
}