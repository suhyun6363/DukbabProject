package kr.ac.duksung.dukbab.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.ac.duksung.dukbab.Home.CartDTO;

public class CartDBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Cart.db";
    private static final int DB_VERSION = 3;
    public static final String TABLE_NAME = "Cart";

    // Cart 테이블의 컬럼 정의
    public static final String COLUMN_CART_ID = "cartId";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_NICKNAME = "nickname";
    public static final String COLUMN_STORE_ID = "storeId";   //// storeId로 변경
    public static final String COLUMN_MENU_NAME = "menuName";
    public static final String COLUMN_MENU_OPTION = "menuOption"; // 메뉴 옵션
    public static final String COLUMN_MENU_PRICE = "menuPrice"; // 메뉴 가격
    public static final String COLUMN_MENU_QUANTITY = "menuQuantity"; ////
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
                COLUMN_STORE_ID + " INTEGER, " + //// storeId integer로 수정
                COLUMN_MENU_NAME + " TEXT, " +
                COLUMN_MENU_OPTION + " TEXT, " +
                COLUMN_MENU_PRICE + " TEXT, " +
                COLUMN_MENU_QUANTITY + " INTEGER, " + ////
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

    // loadCartData() 메서드 추가
    public List<CartDTO> getCartItems() {
        List<CartDTO> cartList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                CartDBOpenHelper.COLUMN_MENU_NAME,
                CartDBOpenHelper.COLUMN_MENU_PRICE,
                CartDBOpenHelper.COLUMN_MENU_QUANTITY, ////
                CartDBOpenHelper.COLUMN_MENU_OPTION
        };

        Cursor cursor = db.query(CartDBOpenHelper.TABLE_NAME, columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String menuName = cursor.getString(cursor.getColumnIndex(CartDBOpenHelper.COLUMN_MENU_NAME));
                String menuPrice = cursor.getString(cursor.getColumnIndex(CartDBOpenHelper.COLUMN_MENU_PRICE));
                int menuQuantity = cursor.getInt(cursor.getColumnIndex(CartDBOpenHelper.COLUMN_MENU_QUANTITY)); ////
                String menuOption = cursor.getString(cursor.getColumnIndex(CartDBOpenHelper.COLUMN_MENU_OPTION));

                List<String> selectedOptionsList = new ArrayList<>(Arrays.asList(menuOption.split(", ")));

                CartDTO cartItem = new CartDTO(menuName, menuPrice, menuQuantity, selectedOptionsList);
                cartList.add(cartItem);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();

        return cartList;
    }

}