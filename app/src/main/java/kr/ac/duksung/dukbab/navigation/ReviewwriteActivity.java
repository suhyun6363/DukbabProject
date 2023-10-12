package kr.ac.duksung.dukbab.navigation;

import static java.lang.String.valueOf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.content.Intent;

import kr.ac.duksung.dukbab.R;
import kr.ac.duksung.dukbab.db.Database;
import kr.ac.duksung.dukbab.db.ReviewDBOpenHelper;

import android.view.Window;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReviewwriteActivity extends AppCompatActivity {

    private Spinner restaurantSpinner;
    private EditText menuNameEditText;
    private RatingBar ratingBar;
    private EditText reviewContentEditText;
    private ReviewDBOpenHelper mReviewDBOpenHelper;
    private SQLiteDatabase mReviewDB;
    private String username;
    private String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("리뷰 작성하기");
        setContentView(R.layout.activity_reviewwrite);

        Button saveButton = findViewById(R.id.save_button);
        Button cancelButton = findViewById(R.id.cancel_button);

        // Spinner 초기화
        restaurantSpinner = findViewById(R.id.restaurantSpinner);

        // Spinner에 음식점명 목록을 설정

        String[] restaurantNames = {"오늘의 메뉴", "마라탕", "분식", "수제돈까스", "파스타"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, restaurantNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        restaurantSpinner.setAdapter(adapter);

        menuNameEditText = findViewById(R.id.menuNameEditText);
        ratingBar = findViewById(R.id.ratingBar);
        reviewContentEditText = findViewById(R.id.reviewContentEditText);

        // SharedPreferences 초기화
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // 사용자 메일 정보 가져오기 (예: 이전 화면에서 넘겨받은 정보 또는 SharedPreferences 사용)
        username = sharedPreferences.getString("username", ""); // 사용자 이메일
        nickname = sharedPreferences.getString("nickname", ""); // 사용자 이메일

        // ReviewDBOpenHelper 초기화
        mReviewDBOpenHelper = new ReviewDBOpenHelper(this);

        // 데이터베이스 열기
        mReviewDB = mReviewDBOpenHelper.getWritableDatabase();

        // "취소" 버튼 클릭 이벤트 핸들러
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 Activity를 종료하여 ReviewFragment로 돌아감
                finish();
            }
        });

        // "저장" 버튼 클릭 이벤트 핸들러
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReviewToDatabase(username);
            }
        });

    }

    private void saveReviewToDatabase(String email) {
        // Spinner에서 선택된 음식점명을 가져오기
        String storeName = restaurantSpinner.getSelectedItem().toString();
        // EditText에서 메뉴명, 리뷰 내용 가져오기
        String menuName = menuNameEditText.getText().toString();
        String reviewContent = reviewContentEditText.getText().toString();
        // RatingBar에서 평점 가져오기
        float rating = ratingBar.getRating();

        // 현재 날짜와 시간을 가져오는 부분
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String reviewCreatedDate = dateFormat.format(currentDate);

        ContentValues values = new ContentValues();
        values.put(ReviewDBOpenHelper.COLUMN_STORE_NAME, storeName);
        values.put(ReviewDBOpenHelper.COLUMN_EMAIL, username);
        values.put(ReviewDBOpenHelper.COLUMN_NICKNAME, nickname);
        values.put(ReviewDBOpenHelper.COLUMN_MENU_NAME, menuName);
        values.put(ReviewDBOpenHelper.COLUMN_RATING, rating);
        values.put(ReviewDBOpenHelper.COLUMN_REVIEW_CONTENT, reviewContent);
        values.put(ReviewDBOpenHelper.COLUMN_REVIEW_CREATED_DATE, reviewCreatedDate);

        // 데이터베이스에 리뷰 저장
        long result = mReviewDB.insert(ReviewDBOpenHelper.TABLE_NAME, null, values);

        // 저장 결과에 따른 처리
        if (result != -1) {
            // 저장이 성공적으로 완료되었다는 결과 코드를 설정합니다.
            setResult(RESULT_OK);  // 여기서 Activity 클래스의 RESULT_OK 상수를 직접 사용합니다.

            // 현재 Activity를 종료하여 ReviewFragment로 돌아갑니다.
            finish();
        } else {
            // 저장에 실패한 경우 처리
            Toast.makeText(this, "저장에 실패했습니다", Toast.LENGTH_SHORT).show();
        }
    }
    public interface OnReviewSavedListener {
        void onReviewSaved();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 데이터베이스 연결 종료
        if (mReviewDB != null && mReviewDB.isOpen()) {
            mReviewDB.close();
        }
    }
}
