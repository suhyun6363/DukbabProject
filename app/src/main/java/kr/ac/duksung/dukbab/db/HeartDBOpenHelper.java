package kr.ac.duksung.dukbab.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HeartDBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Heart.db";
    private static final int DB_VERSION = 4;
    public static final String HEART_TABLE_NAME = "Heart";
    public static final String COLUMN_HEART_MENU_ID = "heartMenuId";
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_MENU_ID = "menuId";

    public HeartDBOpenHelper (Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + HEART_TABLE_NAME + " (" +
                COLUMN_HEART_MENU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_MENU_ID + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES User(user_id), " +
                "FOREIGN KEY (" + COLUMN_MENU_ID + ") REFERENCES Menu(menu_id)" +
                ")";
        db.execSQL(createTableQuery);
    }

    // 하트 정보를 추가
    public long addHeartInfo(int userId, int menuId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_MENU_ID, menuId);
        long newRowId = db.insert(HEART_TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }

    // 하트 정보를 제거
    public int removeHeartInfo(int userId, int menuId) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = COLUMN_USER_ID + " = ? AND " + COLUMN_MENU_ID + " = ?";
        String[] whereArgs = {String.valueOf(userId), String.valueOf(menuId)};
        int deletedRows = db.delete(HEART_TABLE_NAME, whereClause, whereArgs);
        db.close();
        return deletedRows;
    }

    // 사용자가 메뉴를 즐겨찾기하였는지 확인
    public boolean isMenuHearted(int userId, int menuId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_HEART_MENU_ID};
        String selection = COLUMN_USER_ID + " = ? AND " + COLUMN_MENU_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId), String.valueOf(menuId)};
        Cursor cursor = db.query(HEART_TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        boolean isHearted = cursor.getCount() > 0;

        cursor.close();
        db.close();
        return isHearted;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //  데이터베이스의 버전이 변경될 때 호출되며, 기존 테이블을 삭제하고 새로운 테이블 생성
        Log.w(DBOpenHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + HEART_TABLE_NAME);
        onCreate(db);
    }

}
