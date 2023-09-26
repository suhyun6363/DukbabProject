package kr.ac.duksung.dukbab_yeeun;


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

    // 다른 버튼 클릭 이벤트 처리 메서드를 추가할 수 있습니다.
}
