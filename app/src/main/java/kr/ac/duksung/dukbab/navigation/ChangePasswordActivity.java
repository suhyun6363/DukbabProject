package kr.ac.duksung.dukbab.navigation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import kr.ac.duksung.dukbab.R;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText currentPasswordEditText;
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private Button changePasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        currentPasswordEditText = findViewById(R.id.currentPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        changePasswordButton = findViewById(R.id.changePasswordButton);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String currentPassword = currentPasswordEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();


        String savedPassword = "1234"; // 저장된 비밀번호 > db에서 불러와야할듯

        if (currentPassword.equals(savedPassword)) {
            if (newPassword.equals(confirmPassword)) {
                // 비밀번호 변경 로직을 여기에 추가
                // newPassword 변수에 변경된 비밀번호가 저장되어 있음
                // 비밀번호 변경 성공 메시지를 토스트로 표시
                Toast.makeText(this, "비밀번호가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                finish(); // 화면 닫기
            } else {
                // 새 비밀번호와 비밀번호 확인이 일치하지 않을 때 에러 메시지 표시
                Toast.makeText(this, "새 비밀번호와 비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // 현재 비밀번호가 일치하지 않을 때 에러 메시지 표시
            Toast.makeText(this, "현재 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
