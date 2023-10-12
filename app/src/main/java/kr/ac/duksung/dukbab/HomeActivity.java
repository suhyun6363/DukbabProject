package kr.ac.duksung.dukbab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import kr.ac.duksung.dukbab.Home.CartDTO;
import kr.ac.duksung.dukbab.Home.MenuDTO;
import kr.ac.duksung.dukbab.navigation.HeartFragment;
import kr.ac.duksung.dukbab.Home.HomeFragment;
import kr.ac.duksung.dukbab.navigation.MypageFragment;
import kr.ac.duksung.dukbab.navigation.ReviewFragment;
import kr.ac.duksung.dukbab.api.TodayMenuFragment;


public class HomeActivity extends AppCompatActivity {
    private Toolbar toolBar;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout mainContent;
    private ReviewFragment reviewFragment;
    private HomeFragment homeFragment;
    private HeartFragment heartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar); // 툴바를 액션바로 설정

        mainContent = findViewById(R.id.main_content);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Fragment 초기화
        homeFragment = new HomeFragment();
        reviewFragment = new ReviewFragment();
        heartFragment = new HeartFragment();
        MypageFragment mypageFragment = new MypageFragment();

        // FragmentManager를 생성합니다.
        FragmentManager fragmentManager = getSupportFragmentManager();
        // heartFragment를 추가합니다.
        fragmentManager.beginTransaction().add(R.id.main_content, reviewFragment, "ReviewFragment").commit();
        fragmentManager.beginTransaction().add(R.id.main_content, heartFragment, "HeartFragment").commit();
        fragmentManager.beginTransaction().replace(R.id.main_content, homeFragment).commit();

        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(homeFragment, "HomeFragment");
                    return true;
                case R.id.navigation_review:
                    // 리뷰 아이콘 클릭 시 ReviewActivity로 이동
                    replaceFragment(reviewFragment, "ReviewFragment");
                    return true;

                case R.id.navigation_heart:
                    replaceFragment(heartFragment, "HeartFragment");
                    return true;
                case R.id.navigation_mypage:
                    replaceFragment(mypageFragment, "MypageFragment");

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
                // action_bell 메뉴 아이템이 클릭되었을 때 TodayMenuFragment로 이동하는 코드
                TodayMenuFragment todayMenuFragment = new TodayMenuFragment();
                replaceFragment(todayMenuFragment, "TodayMenuFragment");
                return true;
            case R.id.action_ticket:
                TicketFragment ticketFragment = new TicketFragment();
                replaceFragment(ticketFragment, "TicketFragment");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // HeartFragment에서 메뉴를 제거
    public void removeHeartMenuItem(MenuDTO menu) {
        // HeartFragment로 데이터 전달
        if (heartFragment != null) {
            heartFragment.removeMenuFromHeart(menu);
        }
    }

    // HeartFragment에 메뉴 추가
    public void addToHeart(MenuDTO menu) {
        // HeartFragment로 데이터 전달
        if (heartFragment != null) {
            heartFragment.addMenuToHeart(menu);
        }
    }

    // Fragment를 교체하는 메서드
    private void replaceFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment, tag).commit();
    }
}