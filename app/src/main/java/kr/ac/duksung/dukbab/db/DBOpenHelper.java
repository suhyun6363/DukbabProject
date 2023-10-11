package kr.ac.duksung.dukbab.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.content.ContentValues;
import android.database.Cursor;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "User.db";
    private static final int DB_VERSION = 4;
    public static final String TABLE_NAME = "Users";

    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_NICKNAME = "nickname";

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 데이터베이스 생성될 때 호출되며, 테이블을 생성하는 SQL 쿼리를 실행
        String createTableQuery = " CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_EMAIL + " TEXT PRIMARY KEY, " +
                COLUMN_PASSWORD + " TEXT NOT NULL, " +
                COLUMN_NICKNAME + " TEXT NOT NULL);";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 데이터베이스의 버전이 변경될 때 호출되며, 기존 테이블을 삭제하고 새로운 테이블을 생성
        Log.w(DBOpenHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public String getPasswordByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String password = null;

        String[] projection = {COLUMN_PASSWORD};
        String selection = COLUMN_EMAIL + "=?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
            cursor.close();
        }

        db.close();
        return password;
    }

    // 사용자의 비밀번호 업데이트 메서드
    public void updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);

        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { email };

        int count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs);

        db.close();
    }
}