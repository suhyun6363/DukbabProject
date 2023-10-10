package kr.ac.duksung.dukbab.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;


public class MenuDBOpenHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Menu.db";
    public static final String DB_TABLE_MENU = "Menu";
    public static final String COLUMN_MENU_ID = "menuId";
    public static final String COLUMN_MENU_NAME = "menuName";
    public static final String COLUMN_MENU_PRICE = "price";
    public static final String COLUMN_MENU_IMG = "img";
    public static final String COLUMN_MENU_HEART = "heart";
    public static final String COLUMN_STORE_ID = "storeId";

    public MenuDBOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + DB_TABLE_MENU + " (" +
                COLUMN_MENU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MENU_NAME + " TEXT, " +
                COLUMN_MENU_PRICE + " TEXT, " +
                COLUMN_MENU_IMG + " TEXT, " +
                COLUMN_MENU_HEART + " TEXT, " +
                COLUMN_STORE_ID + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_STORE_ID + ") REFERENCES " + StoreDBOpenHelper.DB_TABLE_STORE + "(" + StoreDBOpenHelper.COLUMN_STORE_ID + "))";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MenuDBOpenHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_MENU);
        onCreate(db);
    }

    public String getStoreIdByMenuName(String menuName) {
        String storeId = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_STORE_ID};
        String selection = COLUMN_MENU_NAME + "=?";
        String[] selectionArgs = {menuName};

        Cursor cursor = db.query(DB_TABLE_MENU, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            storeId = cursor.getString(cursor.getColumnIndex(COLUMN_STORE_ID));
            cursor.close();
        }

        db.close();
        return storeId;
    }
}