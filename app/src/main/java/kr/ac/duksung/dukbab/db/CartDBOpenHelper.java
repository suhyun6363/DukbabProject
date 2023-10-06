package kr.ac.duksung.dukbab.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CartDBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Cart.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "Cart";

    // Cart 테이블의 컬럼 정의
    public static final String COLUMN_CART_ID = "cartId";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_NICKNAME = "nickname";
    public static final String COLUMN_SELECTED_RESTAURANT = "selectedRestaurant";
    public static final String COLUMN_MENU_NAME = "menuName";
    public static final String COLUMN_ORDER_ID = "orderId";
    public static final String COLUMN_MENU_OPTION_ID = "menuOptionId";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_CART_CREATED_DATE = "cartCreatedDate";

    public CartDBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Cart.db 데이터베이스의 테이블 생성 쿼리
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_NICKNAME + " TEXT, " +
                    COLUMN_SELECTED_RESTAURANT + " TEXT, " +
                    COLUMN_MENU_NAME + " TEXT, " +
                    COLUMN_ORDER_ID + " INTEGER, " +
                    COLUMN_MENU_OPTION_ID + " INTEGER, " +
                    COLUMN_QUANTITY + " INTEGER, " +
                    COLUMN_CART_CREATED_DATE + " TEXT);";
        db.execSQL(createTableQuery);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Order.db 데이터베이스의 업그레이드 로직
        Log.w(CartDBOpenHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}