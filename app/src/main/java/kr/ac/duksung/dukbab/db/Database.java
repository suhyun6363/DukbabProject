package kr.ac.duksung.dukbab.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Database{
    private static Database instance;
    private SQLiteDatabase mDB = null; // User.db 데이터베이스
    private SQLiteDatabase mStoreDB = null; //store.db
    private SQLiteDatabase mMenuDB = null; // Menu.db
    private SQLiteDatabase mReviewDB = null; // Review.db 데이터베이스
    private SQLiteDatabase mOrderDB = null; // Order.db 데이터베이스
    private DBOpenHelper mDBopenHelper = null;
    private StoreDBOpenHelper mStoreDBOpenHelper = null;
    private MenuDBOpenHelper mMenuDBOpenHelper = null;
    private ReviewDBOpenHelper mReviewDBOpenHelper = null;
    private OrderDBOpenHelper mOrderDBOpenHelper = null;
    private Context context;
    private SharedPreferences sharedPreferences;


    private Database(){
    }

    public static Database getInstance(){
        if(instance == null) {
            instance = new Database();
        }
        return instance;
    }

    // 데이터베이스 열기 위해 호출. context매개변수를 받아서 데이터베이스 초기화
    // User.db 데이터베이스 열기
    public void open(Context context) throws SQLException {
        if(mDBopenHelper == null) {
            mDBopenHelper = new DBOpenHelper(context);
            mDB = mDBopenHelper.getWritableDatabase();
        }
    }

    // Store.db 데이터베이스 열기
    public void openStoreDB(Context context) throws SQLException {
        if (mStoreDBOpenHelper == null) {
            mStoreDBOpenHelper = new StoreDBOpenHelper(context);
            this.context = context;
        }
        mStoreDB = mStoreDBOpenHelper.getWritableDatabase();
    }

    //Menu.db
    public void openMenuDB(Context context) throws SQLException {
        if (mMenuDBOpenHelper == null) {
            mMenuDBOpenHelper = new MenuDBOpenHelper(context);
            this.context = context;
        }
        mMenuDB = mMenuDBOpenHelper.getWritableDatabase();
    }

    // Review.db 데이터베이스 열기
    public void openReviewDB(Context context) throws SQLException {
        if (mReviewDBOpenHelper == null) {
            mReviewDBOpenHelper = new ReviewDBOpenHelper(context); // ReviewDBOpenHelper를 초기화합니다.
            this.context = context;
        }
        mReviewDB = mReviewDBOpenHelper.getWritableDatabase();
    }

    // Order.db 데이터베이스 열기
    public void openOrderDB(Context context) throws SQLException {
        if (mOrderDBOpenHelper == null) {
            mOrderDBOpenHelper = new OrderDBOpenHelper(context); // OrderDBOpenHelper를 초기화합니다.
            this.context = context;
        }
        mOrderDB = mOrderDBOpenHelper.getWritableDatabase();
    }


    //  User.db 데이터베이스 닫기
    public void close() {
        if (mDB != null && mDB.isOpen()) {
            mDB.close();
        }
    }

    // Store.db 데이터베이스 닫기
    public void closeStoreDB() {
        if (mStoreDB != null && mStoreDB.isOpen()) {
            mStoreDB.close();
        }
    }

    // Menu.db 데이터베이스 닫기
    public void closeMenuDB() {
        if (mMenuDB != null && mMenuDB.isOpen()) {
            mMenuDB.close();
        }
    }

    // Review.db 데이터베이스 닫기
    public void closeReviewDB() {
        if (mReviewDB != null && mReviewDB.isOpen()) {
            mReviewDB.close();
        }
    }

    // Order.db 데이터베이스 닫기
    public void closeOrderDB() {
        if (mOrderDB != null && mOrderDB.isOpen()) {
            mOrderDB.close();
        }
    }
//---------------------------------------------------------------------
    // 가게 데이터베이스
    public void addStore(int storeId, String storeName, String congestionInfo) {
        if (!isStoreExists(storeName)) {   //중복 방지
            ContentValues values = new ContentValues();
            values.put(StoreDBOpenHelper.COLUMN_STORE_ID, storeId);
            values.put(StoreDBOpenHelper.COLUMN_STORE_NAME, storeName);
            values.put(StoreDBOpenHelper.COLUMN_CONGESTION_INFO, congestionInfo);
            try {
                openStoreDB(context); // 데이터베이스 열기
                mStoreDB.insert(StoreDBOpenHelper.DB_TABLE_STORE, null, values);
            } catch (SQLException e) {
                // 예외 처리: 데이터베이스 열기 실패 또는 삽입 실패
                e.printStackTrace();
            } finally {
                closeStoreDB(); // 데이터베이스 닫기 (항상 닫아야 함)
            }
        }
    }
    // 가게 데이터 중복 방지
    private boolean isStoreExists(String storeName) {
        SQLiteDatabase db = mStoreDBOpenHelper.getReadableDatabase();
        String[] columns = {StoreDBOpenHelper.COLUMN_STORE_NAME};
        String selection = StoreDBOpenHelper.COLUMN_STORE_NAME + "=?";
        String[] selectionArgs = {storeName};
        Cursor cursor = db.query(StoreDBOpenHelper.DB_TABLE_STORE, columns, selection, selectionArgs, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // 메뉴 데이터베이스
    public void addMenu(String menuName, String price, String img, int heart, int storeId) {
        if (!isMenuExists(menuName, storeId)) { //중복 방지
            ContentValues values = new ContentValues();
            values.put(MenuDBOpenHelper.COLUMN_MENU_NAME, menuName);
            values.put(MenuDBOpenHelper.COLUMN_MENU_PRICE, price);
            values.put(MenuDBOpenHelper.COLUMN_MENU_IMG, img);
            values.put(MenuDBOpenHelper.COLUMN_MENU_HEART, heart);
            values.put(MenuDBOpenHelper.COLUMN_STORE_ID, storeId);
            try {
                openMenuDB(context);
                mMenuDB.insert(MenuDBOpenHelper.DB_TABLE_MENU, null, values);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeMenuDB();
            }
        }
    }
    // 메뉴 데이터 중복 방지
    private boolean isMenuExists(String menuName, int storeId) {
        SQLiteDatabase db = mMenuDBOpenHelper.getReadableDatabase();
        String[] columns = {MenuDBOpenHelper.COLUMN_MENU_NAME};
        String selection = MenuDBOpenHelper.COLUMN_MENU_NAME + "=? AND " + MenuDBOpenHelper.COLUMN_STORE_ID + "=?";
        String[] selectionArgs = {menuName, String.valueOf(storeId)};
        Cursor cursor = db.query(MenuDBOpenHelper.DB_TABLE_MENU, columns, selection, selectionArgs, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    //중복 데이터 삭제
    public void deleteDuplicateStores() {
        SQLiteDatabase db = mStoreDBOpenHelper.getWritableDatabase();

        // 중복된 가게 이름을 식별하기 위한 서브쿼리를 사용하여 중복된 가게 이름을 찾기
        String subquery = "SELECT " + StoreDBOpenHelper.COLUMN_STORE_NAME +
                " FROM " + StoreDBOpenHelper.DB_TABLE_STORE +
                " GROUP BY " + StoreDBOpenHelper.COLUMN_STORE_NAME +
                " HAVING COUNT(*) > 1";

        // 중복된 가게 이름을 가진 레코드를 삭제
        String whereClause = StoreDBOpenHelper.COLUMN_STORE_NAME + " IN (" + subquery + ")";
        int deletedRows = db.delete(StoreDBOpenHelper.DB_TABLE_STORE, whereClause, null);

        Log.d("Database", "Deleted " + deletedRows + " duplicate stores.");
    }

    public void deleteDuplicateMenus() {
        SQLiteDatabase db = mMenuDBOpenHelper.getWritableDatabase();

        // 중복된 메뉴 이름을 식별하기 위한 서브쿼리를 사용하여 중복된 메뉴 이름을 찾기
        String subquery = "SELECT " + MenuDBOpenHelper.COLUMN_MENU_NAME +
                " FROM " + MenuDBOpenHelper.DB_TABLE_MENU +
                " GROUP BY " + MenuDBOpenHelper.COLUMN_MENU_NAME +
                " HAVING COUNT(*) > 1";

        // 중복된 메뉴 이름을 가진 레코드를 삭제
        String whereClause = MenuDBOpenHelper.COLUMN_MENU_NAME + " IN (" + subquery + ")";
        int deletedRows = db.delete(MenuDBOpenHelper.DB_TABLE_MENU, whereClause, null);

        Log.d("Database", "Deleted " + deletedRows + " duplicate menus.");
    }

    public void getStoreData() {
        SQLiteDatabase db = mStoreDBOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(StoreDBOpenHelper.DB_TABLE_STORE, null, null, null, null, null, null);

        if (cursor != null) {
            try {
                int storeNameIndex = cursor.getColumnIndex("storeName");
                int congestionInfoIndex = cursor.getColumnIndex("congestionInfo");

                while (cursor.moveToNext()) {
                    String storeName = cursor.getString(storeNameIndex);
                    String congestionInfo = cursor.getString(congestionInfoIndex);

                    Log.d("StoreData", "Store Name: " + storeName + ", Congestion Info: " + congestionInfo);
                }
            } finally {
                cursor.close();
            }
        }
    }

    // Database 클래스에 searchMenuByStoreId 메서드 추가
    public Cursor searchMenuByStoreId(int storeId) {
        SQLiteDatabase db = mMenuDBOpenHelper.getReadableDatabase();
        String[] columns = {
                MenuDBOpenHelper.COLUMN_MENU_ID,
                MenuDBOpenHelper.COLUMN_MENU_NAME,
                MenuDBOpenHelper.COLUMN_MENU_PRICE,
                MenuDBOpenHelper.COLUMN_MENU_IMG
        };
        String selection = MenuDBOpenHelper.COLUMN_STORE_ID + "=?";
        String[] selectionArgs = {String.valueOf(storeId)};
        return db.query(MenuDBOpenHelper.DB_TABLE_MENU, columns, selection, selectionArgs, null, null, null);
    }/*

    // 메뉴 ID 가져오기 (메뉴 이름으로)
    public int getMenuIdFromDatabase(String menuName) {
        int menuId = -1;
        SQLiteDatabase db = mMenuDBOpenHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            String[] columns = {MenuDBOpenHelper.COLUMN_MENU_ID};
            String selection = MenuDBOpenHelper.COLUMN_MENU_NAME + "=?";
            String[] selectionArgs = {menuName};

            cursor = db.query(MenuDBOpenHelper.DB_TABLE_MENU, columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(MenuDBOpenHelper.COLUMN_MENU_ID);
                if (columnIndex >= 0) {
                    menuId = cursor.getInt(columnIndex);
                } else {
                    // 컬럼 이름을 찾지 못한 경우에 대한 처리
                    Log.e("getMenuId", "Column not found: " + MenuDBOpenHelper.COLUMN_MENU_ID);
                }
            } else {
                // 커서가 비어있거나 원하는 위치에 없을 경우에 대한 처리
                Log.e("getMenuId", "Cursor is empty or not positioned correctly.");
            }
        } catch (SQLException e) {
            Log.e("getMenuId", "Error getting menu ID: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return menuId;
    }

    // 메뉴 하트 업데이트
    public void updateMenuHeart(int menuId, int heartValue) {
        SQLiteDatabase db = mMenuDBOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MenuDBOpenHelper.COLUMN_MENU_HEART, heartValue);

        String whereClause = MenuDBOpenHelper.COLUMN_MENU_ID + "=?";
        String[] whereArgs = {String.valueOf(menuId)};

        db.update(MenuDBOpenHelper.DB_TABLE_MENU, values, whereClause, whereArgs);
        db.close();
    } */
// ----------------------------------------------------------------

    // 이메일 검색
    public Cursor searchEmail(String email){
        String[] columns = {DBOpenHelper.COLUMN_EMAIL};
        String selection = DBOpenHelper.COLUMN_EMAIL + "=?";
        String[] selectionArgs = {email};

        try {
            return mDB.query(DBOpenHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        } catch (SQLException e) {
            Log.e("Database", "Error searching for email: " + e.getMessage());
            return null;
        }
    }

    // 리뷰 검색 (Review.db)
    public Cursor searchReview(String email) {  // 닉네임은 중복가능해서 email로 검색함
        String[] columns = {
                ReviewDBOpenHelper.COLUMN_REVIEW_ID,
                ReviewDBOpenHelper.COLUMN_SELECTED_RESTAURANT,
                ReviewDBOpenHelper.COLUMN_MENU_NAME,
                ReviewDBOpenHelper.COLUMN_RATING,
                ReviewDBOpenHelper.COLUMN_REVIEW_CONTENT,
                ReviewDBOpenHelper.COLUMN_REVIEW_CREATED_DATE,
                ReviewDBOpenHelper.COLUMN_EMAIL,
                ReviewDBOpenHelper.COLUMN_NICKNAME
        };
        try {
            // 이메일 필터가 null이면 모든 리뷰를 반환하도록 처리
            String orderBy = ReviewDBOpenHelper.COLUMN_REVIEW_CREATED_DATE + " DESC"; // 내림차순 정렬
            if (email == null) {
                return mReviewDB.query(ReviewDBOpenHelper.TABLE_NAME, columns, null, null, null, null, orderBy);
            } else {
                // 특정 이메일에 해당하는 리뷰를 반환
                String selection = ReviewDBOpenHelper.COLUMN_EMAIL + "=?";
                String[] selectionArgs = {email};
                return mReviewDB.query(ReviewDBOpenHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, orderBy);
            }
        } catch (SQLException e) {
            Log.e("Database", "Error searching for reviews: " + e.getMessage());
            return null;
        }
    }

    // 주문 검색 (Order.db)
    public Cursor searchOrder(String email) {
        String[] columns = {
                OrderDBOpenHelper.COLUMN_ORDER_ID,
                OrderDBOpenHelper.COLUMN_EMAIL,
                OrderDBOpenHelper.COLUMN_ORDER_DATE,
                OrderDBOpenHelper.COLUMN_STORE_ID,
                OrderDBOpenHelper.COLUMN_STATUS,
                OrderDBOpenHelper.COLUMN_TOTAL_PRICE,
                OrderDBOpenHelper.COLUMN_PAYMENT_METHOD
        };
        try {
            // 이메일 필터가 null이면 모든 리뷰를 반환하도록 처리
            if (email == null) {
                return mOrderDB.query(OrderDBOpenHelper.TABLE_NAME, columns, null, null, null, null, null);
            } else {
                // 특정 이메일에 해당하는 리뷰를 반환
                String selection = OrderDBOpenHelper.COLUMN_EMAIL + "=?";
                String[] selectionArgs = {String.valueOf(email)};
                return mOrderDB.query(OrderDBOpenHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            }
        } catch (SQLException e) {
            Log.e("Database", "Error searching for reviews: " + e.getMessage());
            return null;
        }
    }

    public Cursor searchPassword(String email, String password) {
        String[] columns = {DBOpenHelper.COLUMN_PASSWORD};
        String selection = DBOpenHelper.COLUMN_EMAIL + "=? AND " + DBOpenHelper.COLUMN_PASSWORD + "=?";
        String[] selectionArgs = {email, password};
        Cursor cursor = null;

        try {
            // 데이터베이스 연결 열기
            open(context);

            cursor = mDB.query(DBOpenHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        } catch (SQLException e) {
            Log.e("Database", "Error searching for password: " + e.getMessage());
        }

        return cursor;
    }

    //  user 데이터 저장하는 메서드
    public long insert(String email, String pw, String nickname) {
        mDB.beginTransaction(); // 트랜잭션 시작
        try {
            // 이메일을 먼저 검색하여 이미 존재하는지 확인
            Cursor cursor = searchEmail(email);
            if (cursor != null && cursor.getCount() > 0) {
                // 이미 이메일이 존재하면 업데이트하지 말고 롤백
                cursor.close();
                return -1;
            }
            ContentValues values = new ContentValues();
            values.put(DBOpenHelper.COLUMN_EMAIL, email);
            values.put(DBOpenHelper.COLUMN_PASSWORD, pw);
            values.put(DBOpenHelper.COLUMN_NICKNAME, nickname);

            long result = mDB.insert(DBOpenHelper.TABLE_NAME, null, values);

            if (result != -1) {
                mDB.setTransactionSuccessful(); // 트랜잭션 성공 표시
            }

            return result;
        } catch (SQLException e) {
            Log.e("Database", "Error inserting data: " + e.getMessage());
            return -1;
        } finally {
            mDB.endTransaction(); // 트랜잭션 종료
        }
    }


    // 주문 데이터를 저장하는 메서드
    public long insertOrder(String orderId, String email, String orderDate, String storeId, String status, String totalPrice, String paymentMethod) {
        mOrderDB.beginTransaction(); // 트랜잭션 시작
        try {
            ContentValues values = new ContentValues();
            values.put(OrderDBOpenHelper.COLUMN_ORDER_ID, orderId);
            values.put(OrderDBOpenHelper.COLUMN_EMAIL, email);
            values.put(OrderDBOpenHelper.COLUMN_ORDER_DATE, orderDate);
            values.put(OrderDBOpenHelper.COLUMN_STORE_ID, storeId);
            values.put(OrderDBOpenHelper.COLUMN_STATUS, status);
            values.put(OrderDBOpenHelper.COLUMN_TOTAL_PRICE, totalPrice);
            values.put(OrderDBOpenHelper.COLUMN_PAYMENT_METHOD, paymentMethod);

            long result = mOrderDB.insert(OrderDBOpenHelper.TABLE_NAME, null, values);

            if (result != -1) {
                mOrderDB.setTransactionSuccessful(); // 트랜잭션 성공 표시
            }

            return result;
        } catch (SQLException e) {
            Log.e("Database", "Error inserting order data: " + e.getMessage());
            return -1;
        } finally {
            mOrderDB.endTransaction(); // 트랜잭션 종료
        }
    }
}
