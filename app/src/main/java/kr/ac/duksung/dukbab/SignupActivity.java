package kr.ac.duksung.dukbab;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import kr.ac.duksung.dukbab.db.Database;

public class SignupActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextSignupPassword;
    private EditText editTextConfirmPassword;
    private EditText editTextNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSignupPassword = findViewById(R.id.editTextSignupPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextNickname = findViewById(R.id.editTextNickname);
    }

    public void onSignupClick(View view) {
        String username = editTextEmail.getText().toString();
        String password = editTextSignupPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();
        String nickname = editTextNickname.getText().toString();

        // 입력 필드의 빈칸 여부를 확인
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "빈칸을 모두 채워주세요.", Toast.LENGTH_SHORT).show();
        } else if (!username.endsWith("duksung.ac.kr")) { // "duksung.ac.kr"로 끝나지 않을 때
            Toast.makeText(this, "덕성메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            // 여기에서 실제 회원가입 로직을 구현하세요.
            // 데이터베이스에 사용자 정보를 저장하는 코드를 추가
            Database database = Database.getInstance();
            database.open(this);

            // Check if the username already exists in the database
            boolean userExists = database.checkUserExists(username);
            if (userExists) {
                Toast.makeText(this, "이미 존재하는 이메일입니다.", Toast.LENGTH_SHORT).show();
                database.close();
            } else if (password.equals(confirmPassword)) {
                long result = database.insert(username, password, nickname);
                database.close();

                if (result != -1) {
                    Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                    finish(); // 회원가입 액티비티 종료
                } else {
                    Toast.makeText(this, "회원가입 실패. 다시 시도하세요.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onCancelClick(View view) {
        // 취소 버튼을 클릭했을 때 로그인 화면으로 이동하도록 설정
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // 현재 액티비티 종료
    }



}