package kr.ac.duksung.dukbab.navigation;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.content.Intent;

import kr.ac.duksung.dukbab.R;
import android.view.Window;

public class ReviewwriteActivity extends AppCompatActivity {

    private Spinner restaurantSpinner;
    private EditText menuNameEditText;
    private RatingBar ratingBar;
    private EditText reviewContentEditText;

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
        // DB 완성되면 정보 가져오게 바꿀 예정
        String[] restaurantNames = {"마라탕", "분식", "수제 돈까스", "오늘의 메뉴", "해장국", "일식"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, restaurantNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        restaurantSpinner.setAdapter(adapter);

        menuNameEditText = findViewById(R.id.menuNameEditText);
        ratingBar = findViewById(R.id.ratingBar);
        reviewContentEditText = findViewById(R.id.reviewContentEditText);

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
                // Spinner에서 선택된 음식점명을 가져오기
                String selectedRestaurant = restaurantSpinner.getSelectedItem().toString();
                // EditText에서 메뉴명, 리뷰 내용 가져오기
                String menuName = menuNameEditText.getText().toString();
                String reviewContent = reviewContentEditText.getText().toString();
                // RatingBar에서 평점 가져오기
                float rating = ratingBar.getRating();

                // 여기에 저장 로직을 추가하면 됩니다.
                // 저장 로직이 완료되면 Toast 메시지를 띄워 사용자에게 알립니다.
                // 저장이 성공했을 때의 처리를 여기에 추가하세요.

                // 저장이 완료되었음을 사용자에게 알리는 Toast 메시지 띄우기
                Toast.makeText(ReviewwriteActivity.this, "작성이 완료되었습니다", Toast.LENGTH_SHORT).show();

                // 현재 Activity를 종료하여 ReviewFragment로 돌아감
                finish();
            }
        });



    }






}
