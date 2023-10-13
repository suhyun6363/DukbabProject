package kr.ac.duksung.dukbab.Home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import kr.ac.duksung.dukbab.HomeActivity;
import kr.ac.duksung.dukbab.R;
import kr.co.bootpay.android.BootpayAnalytics;

public class OrderConfirmActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderconfirm);

        Button goHomeBtn = findViewById(R.id.btn);

        goHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToHome();
            }
        });
    }

    // "돌아가기" 버튼을 클릭했을 때 호출될 메서드
    private void returnToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish(); // 현재 액티비티를 종료

    }
}
