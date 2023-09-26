package kr.ac.duksung.dukbab_yeeun;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class SignupActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextSignupPassword;
    private EditText editTextConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSignupPassword = findViewById(R.id.editTextSignupPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
    }

    public void onSignupClick(View view) {
        String email = editTextEmail.getText().toString();
        String password = editTextSignupPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        // 입력 필드의 빈칸 여부를 확인
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "빈칸을 모두 채워주세요.", Toast.LENGTH_SHORT).show();
        } else if (!email.endsWith("duksung.ac.kr")) { // "duksung.ac.kr"로 끝나지 않을 때
            Toast.makeText(this, "덕성메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else if (password.equals(confirmPassword)) {
            // 비밀번호 확인이 일치할 때 회원가입 처리
            // 여기에서 실제 회원가입 로직을 구현하세요.
            Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show();
            // 필요한 경우 회원 정보를 저장하고 로그인 화면으로 이동할 수 있습니다.
            finish(); // 회원가입 액티비티 종료
        } else {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    //자동로그인 기능을 추가하고싶어용

    public void onCancelClick(View view) {
        // 취소 버튼을 클릭했을 때 로그인 화면으로 이동하도록 설정
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // 현재 액티비티 종료
    }



}