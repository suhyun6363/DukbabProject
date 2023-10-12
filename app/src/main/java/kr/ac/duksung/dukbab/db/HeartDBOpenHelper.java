package kr.ac.duksung.dukbab.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HeartDBOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "heart_db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "hearted_menus";
    private static final String COLUMN_ID = "_id";
    public static final String COLUMN_MENU_NAME = "menu_name";
    public static final String COLUMN_MENU_PRICE = "menu_price";


    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_MENU_NAME + " TEXT, " +
            COLUMN_MENU_PRICE + " TEXT);";

    public HeartDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade policy when the database schema changes
    }
}
