package kr.ac.duksung.dukbab.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class OrderDBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Order.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "Order";

    // Order 테이블의 컬럼 정의
    public static final String COLUMN_ORDER_ID = "orderId";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_ORDER_DATE = "orderDate";
    public static final String COLUMN_STORE_ID = "storeId";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_TOTAL_PRICE = "totalPrice";
    public static final String COLUMN_PAYMENT_METHOD = "paymentMethod";


    public OrderDBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Review.db 데이터베이스의 테이블 생성 쿼리
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_ORDER_DATE + " TEXT, " +
                COLUMN_STORE_ID + " TEXT, " +
                COLUMN_STATUS + " TEXT, " +
                COLUMN_TOTAL_PRICE + " TEXT, " +
                COLUMN_PAYMENT_METHOD + " TEXT)";
        db.execSQL(createTableQuery);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Order.db 데이터베이스의 업그레이드 로직
        Log.w(OrderDBOpenHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}




