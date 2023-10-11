package kr.ac.duksung.dukbab.navigation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import kr.ac.duksung.dukbab.R;
import kr.ac.duksung.dukbab.db.DBOpenHelper;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText currentPasswordEditText;
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private Button changePasswordButton;
    private String userEmail;
    private String currentPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        currentPasswordEditText = findViewById(R.id.currentPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        changePasswordButton = findViewById(R.id.changePasswordButton);

        // currentPasswordEditText의 입력 형식을 텍스트로 설정하여 가려지지 않게 함
        currentPasswordEditText.setInputType(android.text.InputType.TYPE_NULL);
        currentPasswordEditText.setKeyListener(null);


        // editProfileBtn 클릭 시 전달된 사용자 이메일을 가져와서 현재 비밀번호를 화면에 표시
        userEmail = getIntent().getStringExtra("username");
        DBOpenHelper dbHelper = new DBOpenHelper(this);
        currentPassword = dbHelper.getPasswordByEmail(userEmail);

        if (currentPassword != null) {
            currentPasswordEditText.setText(currentPassword);
        }

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });

        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // "취소하기" 버튼을 클릭할 때 현재 엑티비티를 종료
                finish();
            }
        });

    }



    private void changePassword() {
        // 비밀번호 변경 로직을 여기에 추가

        // 예제에서는 현재 비밀번호가 데이터베이스에서 가져온 값으로 표시되고,
        // 새 비밀번호와 비밀번호 확인이 일치하면 비밀번호 변경 성공 메시지를 토스트로 표시
        // 실제로는 데이터베이스 업데이트 로직을 여기에 추가해야 합니다.

        String newPassword = newPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (newPassword.equals(confirmPassword)) {
            // 비밀번호 변경 성공 메시지를 토스트로 표시
            Toast.makeText(this, "비밀번호가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
            finish(); // 화면 닫기
        } else {
            // 새 비밀번호와 비밀번호 확인이 일치하지 않을 때 에러 메시지 표시
            Toast.makeText(this, "새 비밀번호와 비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
