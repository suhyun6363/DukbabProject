package kr.ac.duksung.dukbab;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void onMyPageClick(View view) {
        // 마이페이지로 이동하는 코드
        Intent intent = new Intent(this, MypageActivity.class);
        startActivity(intent);
    }

    public void onReviewClick(View view) {
        // 리뷰페이지로 이동하는 코드
        Intent intent = new Intent(this, ReviewActivity.class);
        startActivity(intent);
    }


}
