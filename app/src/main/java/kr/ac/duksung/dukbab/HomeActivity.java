package kr.ac.duksung.dukbab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.content.Intent;


import kr.ac.duksung.dukbab.navigation.HeartFragment;
import kr.ac.duksung.dukbab.Home.HomeFragment;
import kr.ac.duksung.dukbab.navigation.MypageFragment;
import kr.ac.duksung.dukbab.navigation.ReviewActivity;


public class HomeActivity extends AppCompatActivity {
    private Toolbar toolBar;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout mainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar); // 툴바를 액션바로 설정

        mainContent = findViewById(R.id.main_content);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new HomeFragment()).commit();

        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(new HomeFragment());
                    return true;
                case R.id.navigation_review:
                    // 리뷰 아이콘 클릭 시 ReviewActivity로 이동
                    startActivity(new Intent(HomeActivity.this, ReviewActivity.class));
                    return true;

                case R.id.navigation_heart:
                    replaceFragment(new HeartFragment());
                    return true;
                case R.id.navigation_mypage:
                    replaceFragment(new MypageFragment());
                    return true;
            }
            return false;
        });
    }

    // 툴바에 메뉴 아이콘 추가
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_actions, menu);
        return true;
    }

    // 툴바 메뉴 아이콘 클릭 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bell:
                // 벨 아이콘 클릭 시 동작 정의
                // 예: Toast 메시지 표시
                Toast.makeText(this, "벨 아이콘 클릭", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_ticket:
                // 티켓 아이콘 클릭 시 동작 정의
                // 예: 다른 액티비티로 이동
                Toast.makeText(this, "티켓 아이콘 클릭", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Fragment를 교체하는 메서드
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();
    }
}