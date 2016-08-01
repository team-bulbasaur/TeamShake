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
    public static final String TABLE_USERS = "favorites";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_RESTAURANT_ID = "restaurant_id";
    public static final String COLUMN_RATINGIMG = "ratingIMG";
    public static final String COLUMN_USERRATING = "userrating";
    public static final String COLUMN_USERCOMMENT = "usercomment";

    public UserDBHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " +
                TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_RESTAURANT_ID + " TEXT,"
                + COLUMN_RATINGIMG + " TEXT,"
                + COLUMN_USERRATING + " TEXT,"
                + COLUMN_USERCOMMENT + " TEXT"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public long insert(Favorites fav) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, fav.getId());
        cv.put(COLUMN_NAME, fav.getName());
        cv.put(COLUMN_RESTAURANT_ID, fav.getRestaurant_id());
        cv.put(COLUMN_RATINGIMG, fav.getRatingImg());
        cv.put(COLUMN_USERRATING, fav.getUserrating());
        cv.put(COLUMN_USERCOMMENT, fav.getUsercomment());

        long ret = database.insert(TABLE_USERS, null, cv);
        database.close();

        return ret;
    }

    public ArrayList<Favorites> getAll() {
        ArrayList<Favorites> favorites = new ArrayList<>();

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(String.format("SELECT * FROM %s", TABLE_USERS), null);

            if (cursor.moveToFirst()) {

                while (!cursor.isAfterLast()) {

                    Favorites favorite = new Favorites(
                            cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_RESTAURANT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_RATINGIMG)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_USERRATING)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_USERCOMMENT))
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
        long ret = database.delete(TABLE_USERS, COLUMN_NAME + "=?", new String[] { fav.getName() });
        database.close();
        return ret;
    }

    public void deleteAll() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("delete from " + TABLE_USERS);
        database.close();
    }
}