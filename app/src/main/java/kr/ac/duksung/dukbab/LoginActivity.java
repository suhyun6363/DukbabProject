package kr.ac.duksung.dukbab;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
    }

    public void onLoginButtonClick(View view) {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        // 여기에서 실제 로그인 로직을 구현하세요.


        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        /*


        if (username.equals("사용자명") && password.equals("비밀번호")) {
            // 로그인 성공 시 다음 화면으로 이동
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        } else {
            // 로그인 실패 시 메시지 표시
            Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show();
        }

         */
    }

    public void onSignupButtonClick(View view) {
        // 회원가입 화면으로 이동
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
