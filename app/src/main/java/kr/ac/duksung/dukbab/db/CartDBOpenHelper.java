package kr.ac.duksung.dukbab.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.ac.duksung.dukbab.Home.CartDTO;

public class CartDBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Cart.db";
    private static final int DB_VERSION = 4;
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
                String menuPrice = cursor.getString(cursor.getColumnIndex(CartDBOpenHelper.COLUMN_MENU_PRICE)).replace("￦", "").replace(",", "").trim();
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

    // 해당 메서드는 주어진 CartDTO 객체를 기반으로 cart.db에서 해당 아이템을 삭제합니다.
    public void deleteCartItem(CartDTO cartItem) {
        // 데이터베이스에 데이터가 1개 이상 있는지 확인
        if (isDatabaseNotEmpty()) {
            SQLiteDatabase db = this.getWritableDatabase();
            String menuName = cartItem.getMenuName(); // 해당 아이템의 메뉴 이름
            String menuOption = TextUtils.join(", ", cartItem.getSelectedOptions()); // 해당 아이템의 메뉴 옵션을 문자열로 변환

            // 삭제할 아이템을 식별하는 조건문을 구성합니다.
            String whereClause = COLUMN_MENU_NAME + " = ? AND " +
                    COLUMN_MENU_OPTION + " = ?";

            // 조건에 맞는 데이터를 삭제합니다.
            db.delete(TABLE_NAME, whereClause, new String[]{menuName, menuOption});

            db.close();
        }
    }

    // totalQuantity 업데이트 메서드
    public void updateTotalQuantity(int totalQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MENU_QUANTITY, totalQuantity);

        // 모든 데이터를 업데이트합니다.
        db.update(TABLE_NAME, values, null, null);
        db.close();
    }

    public void updateCartItemQuantity(CartDTO cartItem, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_MENU_QUANTITY, newQuantity);
        // 여기서 새로운 가격(newPrice)를 계산합니다. 메뉴 가격 * 수량
        int newPrice = Integer.parseInt(cartItem.getMenuPrice().replace("￦", "").replace(",", "").trim()) * newQuantity;
        values.put(COLUMN_MENU_PRICE, "￦ " + newPrice);

        String whereClause = COLUMN_MENU_NAME + " = ? AND " + COLUMN_MENU_OPTION + " = ?";
        String menuName = cartItem.getMenuName();
        String menuOption = TextUtils.join(", ", cartItem.getSelectedOptions());

        String[] whereArgs = {menuName, menuOption};

        db.update(TABLE_NAME, values, whereClause, whereArgs);
        db.close();

        // 증가된 newQuantity 값으로 totalQuantity 계산
        int totalQuantity = 0;
        for (CartDTO item : getCartItems()) {
            totalQuantity += item.getMenuQuantity();
        }
        updateTotalQuantity(totalQuantity);
    }

    public boolean isDatabaseNotEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();

        return count > 0;
    }

    // 데이터베이스 초기화 메소드 추가
    public void clearCartDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }
}