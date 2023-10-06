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
    private SQLiteDatabase mDB = null;          // User.db 데이터베이스
    private SQLiteDatabase mReviewDB = null;    // Review.db 데이터베이스
    private SQLiteDatabase mCartDB = null;      // Cart.db 데이터베이스
    private SQLiteDatabase mOrderDB = null;     // Order.db 데이터베이스
    private DBOpenHelper mDBopenHelper = null;
    private ReviewDBOpenHelper mReviewDBOpenHelper = null;
    private CartDBOpenHelper mCartDBOpenHelper = null;
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

    // Review.db 데이터베이스 열기
    public void openReviewDB(Context context) throws SQLException {
        if (mReviewDBOpenHelper == null) {
            mReviewDBOpenHelper = new ReviewDBOpenHelper(context); // ReviewDBOpenHelper를 초기화합니다.
            this.context = context;
        }
        mReviewDB = mReviewDBOpenHelper.getWritableDatabase();
    }

    // Cart.db 데이터베이스 열기
    public void openCartDB(Context context) throws SQLException {
        if (mCartDBOpenHelper == null) {
            mCartDBOpenHelper = new CartDBOpenHelper(context);
            mCartDB = mCartDBOpenHelper.getWritableDatabase();
        }
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

    // Review.db 데이터베이스 닫기
    public void closeReviewDB() {
        if (mReviewDB != null && mReviewDB.isOpen()) {
            mReviewDB.close();
        }
    }

    // Cart.db 데이터베이스 닫기
    public void closeCartDB() {
        if (mCartDB != null && mCartDB.isOpen()) {
            mCartDB.close();
        }
    }

    // Order.db 데이터베이스 닫기
    public void closeOrderDB() {
        if (mOrderDB != null && mOrderDB.isOpen()) {
            mOrderDB.close();
        }
    }

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
                OrderDBOpenHelper.COLUMN_NICKNAME,
                OrderDBOpenHelper.COLUMN_ORDER_DATE,
                OrderDBOpenHelper.COLUMN_SELECTED_RESTAURANT,
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
                String[] selectionArgs = {email};
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

    // 장바구니 데이터 저장하는 메서드
    // 후에 cartActivity 생기면 옮길 예정
    public long insertCart(String email, String nickname, String selectedRestaurant, String menuName, String menuOptionId, int quantity, String cartCreatedDate) {
        mCartDB.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(CartDBOpenHelper.COLUMN_EMAIL, email);
            values.put(CartDBOpenHelper.COLUMN_NICKNAME, nickname);
            values.put(CartDBOpenHelper.COLUMN_SELECTED_RESTAURANT, selectedRestaurant);
            values.put(CartDBOpenHelper.COLUMN_MENU_NAME, menuName);
            values.put(CartDBOpenHelper.COLUMN_MENU_OPTION_ID, menuOptionId);
            values.put(CartDBOpenHelper.COLUMN_QUANTITY, quantity);
            values.put(CartDBOpenHelper.COLUMN_CART_CREATED_DATE, cartCreatedDate);

            long result = mCartDB.insert(CartDBOpenHelper.TABLE_NAME, null, values);

            if (result != -1) {
                mCartDB.setTransactionSuccessful();
            }
            return result;
        } catch (SQLException e) {
            Log.e("Database", "Error inserting cart data: " + e.getMessage());
            return -1;
        } finally {
            mCartDB.endTransaction();
        }
    }


    // 주문 데이터를 저장하는 메서드
    // 후에 orderActivity 생기면 옮길 예정
    public long insertOrder(String email, String nickname, String orderDate, String selectedRestaurant, String status, String totalPrice, String paymentMethod) {
        mOrderDB.beginTransaction(); // 트랜잭션 시작
        try {
            ContentValues values = new ContentValues();
            values.put(OrderDBOpenHelper.COLUMN_EMAIL, email);
            values.put(OrderDBOpenHelper.COLUMN_NICKNAME, nickname);
            values.put(OrderDBOpenHelper.COLUMN_ORDER_DATE, orderDate);
            values.put(CartDBOpenHelper.COLUMN_SELECTED_RESTAURANT, selectedRestaurant);
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
