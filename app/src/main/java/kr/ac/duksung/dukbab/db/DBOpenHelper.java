package kr.ac.duksung.dukbab.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "User.db";
    private static final int DB_VERSION = 4;
    public static final String TABLE_NAME = "Users";

    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_NICKNAME = "nickname";
    //    public static final String COLUMN_HEART = "heart";
    public static final String COLUMN_HEART_MENU_ID = "hearMenuId";

    public DBOpenHelper (Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 데이터베이스 생성될 때 호출되며, 테이블을 생성하는 SQL쿼리를 실행
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_EMAIL + " TEXT PRIMARY KEY, " +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PASSWORD + " TEXT NOT NULL, " +
                COLUMN_NICKNAME + " TEXT NOT NULL, " +
                COLUMN_HEART_MENU_ID + " INTEGER);";
        db.execSQL(createTableQuery);
    }

    public void updateFavoriteMenuId(String email, int menuId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_HEART_MENU_ID, menuId);

        String selection = COLUMN_EMAIL + "=?";
        String[] selectionArgs = {email};

        db.update(TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    public int getFavoriteMenuId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_HEART_MENU_ID};
        String selection = COLUMN_EMAIL + "=?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        int favoriteMenuId = -1; // 디폴트 값 설정

        if (cursor != null && cursor.moveToFirst()) {
            favoriteMenuId = cursor.getInt(cursor.getColumnIndex(COLUMN_HEART_MENU_ID));
            cursor.close();
        }

        db.close();
        return favoriteMenuId;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //  데이터베이스의 버전이 변경될 때 호출되며, 기존 테이블을 삭제하고 새로운 테이블 생성
        Log.w(DBOpenHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}