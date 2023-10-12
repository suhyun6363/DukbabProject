package kr.ac.duksung.dukbab.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;

public class StoreDBOpenHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Store.db";
    public static final String DB_TABLE_STORE = "Store";
    public static final String COLUMN_STORE_ID = "storeId";
    public static final String COLUMN_STORE_NAME = "storeName";
    public static final String COLUMN_CONGESTION_INFO = "congestionInfo";

    public StoreDBOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + DB_TABLE_STORE + "(" +
                COLUMN_STORE_ID + " INTEGER PRIMARY KEY," +
                COLUMN_STORE_NAME + " TEXT," +
                COLUMN_CONGESTION_INFO + " INTEGER)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(StoreDBOpenHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_STORE);
        onCreate(db);
    }
}

