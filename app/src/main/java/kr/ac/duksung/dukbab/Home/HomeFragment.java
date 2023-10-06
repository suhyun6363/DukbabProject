package kr.ac.duksung.dukbab.Home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import kr.ac.duksung.dukbab.R;

public class HomeFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private RecyclerView cartView;
    private List<CartDTO> cartItemList = new ArrayList<CartDTO>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        // 탭 추가
        tabLayout.addTab(tabLayout.newTab().setText("오늘의 메뉴"));
        tabLayout.addTab(tabLayout.newTab().setText("마라탕"));
        tabLayout.addTab(tabLayout.newTab().setText("분식"));
        tabLayout.addTab(tabLayout.newTab().setText("수제돈까스"));
        tabLayout.addTab(tabLayout.newTab().setText("해장국"));
        tabLayout.addTab(tabLayout.newTab().setText("일식"));

        // ViewPager에 어댑터 연결
        MenuPageAdapter pageAdapter = new MenuPageAdapter(requireActivity(), tabLayout);
        viewPager.setAdapter(pageAdapter);

        // ViewPager2 페이지 변경 이벤트 감지
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                // ViewPager2 페이지가 변경될 때 탭을 선택한 것처럼 업데이트
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        // TabLayout의 탭 선택 리스너 설정
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return view;
    }
}

