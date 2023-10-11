package kr.ac.duksung.dukbab.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ReviewDBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Review.db";
    private static final int DB_VERSION = 5;
    public static final String TABLE_NAME = "Review";

    // Review 테이블의 컬럼 정의
    public static final String COLUMN_REVIEW_ID = "reviewId";
    public static final String COLUMN_STORE_NAME = "storeName"; //// storeId -> storeName으로 수정
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_NICKNAME = "nickname";
    public static final String COLUMN_MENU_NAME = "menuName"; ////
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_REVIEW_CONTENT = "content";
    public static final String COLUMN_REVIEW_CREATED_DATE = "reviewCreatedDate";

    public ReviewDBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Review.db 데이터베이스의 테이블 생성 쿼리
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STORE_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_NICKNAME + " TEXT, " +
                COLUMN_MENU_NAME + " TEXT, " +
                COLUMN_RATING + " REAL, " +
                COLUMN_REVIEW_CONTENT + " TEXT, " +
                COLUMN_REVIEW_CREATED_DATE + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Review.db 데이터베이스의 업그레이드 로직
        Log.w(ReviewDBOpenHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 사용자 이메일로 리뷰 검색
    public Cursor searchReviewByEmail(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ?";
        return db.rawQuery(query, new String[]{userEmail});
    }

    public Cursor getAllReviews() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(query, null);
    }


    // 리뷰 저장
    public long saveReview(String storeName, String userEmail, String nickname, String menuName, float rating, String reviewContent, String reviewCreatedDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STORE_NAME, storeName);
        values.put(COLUMN_EMAIL, userEmail);
        values.put(COLUMN_NICKNAME, nickname);
        values.put(COLUMN_MENU_NAME, menuName);
        values.put(COLUMN_RATING, rating);
        values.put(COLUMN_REVIEW_CONTENT, reviewContent);
        values.put(COLUMN_REVIEW_CREATED_DATE, reviewCreatedDate);

        // 데이터베이스에 리뷰 저장
        return db.insert(TABLE_NAME, null, values);
    }
}
