package kr.ac.duksung.dukbab_yeeun;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MypageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        // 사용자 메일 정보 가져오기 (예: 이전 화면에서 넘겨받은 정보 또는 SharedPreferences 사용)
        String userEmail = "user@example.com"; // 예시로 사용자 메일을 설정

        // TextView 찾기
        TextView welcomeMessageTextView = findViewById(R.id.welcomeMessageTextView);

        // 사용자 메일 정보를 텍스트뷰에 설정
        welcomeMessageTextView.setText(userEmail + " 님");
    }

    public void onLogoutClick(View view) {
        // 로그아웃 여부 확인 팝업 띄우기
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 확인을 누르면 로그인 페이지로 이동
                        Intent intent = new Intent(MypageActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // 기존 스택 비우기
                        startActivity(intent);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 취소를 누르면 아무 동작 없음
                    }
                });

        // 팝업을 표시
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
