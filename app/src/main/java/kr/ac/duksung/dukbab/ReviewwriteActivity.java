package kr.ac.duksung.dukbab;

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

public class ReviewwriteActivity extends AppCompatActivity {

    private Spinner restaurantSpinner;
    private EditText menuNameEditText;
    private RatingBar ratingBar;
    private EditText reviewContentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                // ReviewActivity로 돌아가는 Intent를 생성
                Intent intent = new Intent(ReviewwriteActivity.this, ReviewActivity.class);
                // ReviewActivity를 새로 시작하고 현재 Activity를 종료
                startActivity(intent);
                finish();
            }
        });
    }

    // 저장 버튼 클릭 이벤트 핸들러
    public void onSaveClick(View view) {
        String restaurantName = restaurantSpinner.getSelectedItem().toString();
        String menuName = menuNameEditText.getText().toString();
        float rating = ratingBar.getRating();
        String reviewContent = reviewContentEditText.getText().toString();

        // 이후 리뷰를 저장하거나 다른 작업을 수행하는 코드를 추가할 수 있습니다.
        // 예를 들어, 데이터베이스에 리뷰를 저장하거나 서버에 업로드할 수 있습니다.

        // 예시: 간단한 토스트 메시지 표시
        Toast.makeText(this, "리뷰가 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }
}
