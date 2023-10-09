package kr.ac.duksung.dukbab;


import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import kr.ac.duksung.dukbab.db.DBOpenHelper;
import kr.ac.duksung.dukbab.db.Database;
import kr.ac.duksung.dukbab.navigation.ReviewwriteActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private CheckBox checkBoxAuto;
    private Database database;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        checkBoxAuto = findViewById(R.id.checkBoxAuto);

        // Database 인스턴스 생성
        database = Database.getInstance();
        database.open(this);

        // SharedPreferences 초기화
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // 저장된 로그인 정보가 있는 경우 자동 로그인 시도
        if (isLoggedIn()) {
            autoLogin();
        }
    }

    public void onLoginButtonClick(View view) {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        // 입력값이 비어있는지 검사
        if (username.isEmpty() || password.isEmpty()) {
            // 정보를 입력해주세요 메시지 표시
            Toast.makeText(this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            // 데이터베이스에서 입력된 사용자명(username)에 해당하는 비밀번호 조회
            Cursor cursor = database.searchPassword(username, password);

            if (cursor != null && cursor.moveToFirst()) {
                // 비밀번호가 일치하면 HomeActivity로 이동
                String nickname = getNicknameByUsername(username);
                saveNicknameToSharedPreferences(nickname);

                if (checkBoxAuto.isChecked()) {
                    // 체크박스가 선택된 경우, 자동 로그인을 위해 사용자명과 비밀번호를 저장합니다.
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username);
                    editor.putString("password", password); // 실제로는 안전한 방식으로 저장해야 합니다.
                    editor.apply();
                }

                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
            } else {
                // 이메일 또는 비밀번호가 다르면 메시지 표시
                Toast.makeText(this, "이메일 또는 비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // username을 기반으로 nickname을 가져오는 메서드
    private String getNicknameByUsername(String username) {
        DBOpenHelper dbHelper = new DBOpenHelper(this);
        // 읽기 모드로 데이터베이스 연결
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {DBOpenHelper.COLUMN_NICKNAME};
        String selection = DBOpenHelper.COLUMN_EMAIL + "=?";
        String[] selectionArgs = {username};

        try {
            Cursor cursor = db.query(DBOpenHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                // nickname 값을 가져옵니다.
                String nickname = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_NICKNAME));
                cursor.close();
                db.close(); // 데이터베이스 연결 닫기
                return nickname;
            }
        } catch (SQLException e) {
            Log.e("LoginActivity", "Error fetching nickname: " + e.getMessage());
        }

        db.close(); // 데이터베이스 연결 닫기
        return ""; // 데이터를 찾지 못한 경우 빈 문자열 반환
    }

    private void saveNicknameToSharedPreferences(String nickname) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nickname", nickname);
        editor.apply();
    }

    // 로그인 상태 확인
    private boolean isLoggedIn() {
        // 저장된 로그인 정보(username, password)가 있는지 확인
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");

        return !savedUsername.isEmpty() && !savedPassword.isEmpty();
    }

    // 자동 로그인을 시도하는 메서드
    private void autoLogin() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish(); // 로그인 화면 종료
    }

    public void onSignupButtonClick(View view) {
        // 회원가입 화면으로 이동
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
