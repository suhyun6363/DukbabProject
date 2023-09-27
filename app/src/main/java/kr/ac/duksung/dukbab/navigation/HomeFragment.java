package kr.ac.duksung.dukbab.navigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import kr.ac.duksung.dukbab.BunsikFragment;
import kr.ac.duksung.dukbab.HaejangSoupFragment;
import kr.ac.duksung.dukbab.HomemadeCutletFragment;
import kr.ac.duksung.dukbab.JpFoodFragment;
import kr.ac.duksung.dukbab.MaraTangFragment;
import kr.ac.duksung.dukbab.R;
import kr.ac.duksung.dukbab.TodayMenuFragment;

public class HomeFragment extends Fragment {
    private TabLayout tablayout;
    private ViewPager2 viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tablayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewPager);

        // 탭 추가
        tablayout.addTab(tablayout.newTab().setText("오늘의 메뉴"));
        tablayout.addTab(tablayout.newTab().setText("마라탕"));
        tablayout.addTab(tablayout.newTab().setText("분식"));
        tablayout.addTab(tablayout.newTab().setText("수제돈까스"));
        tablayout.addTab(tablayout.newTab().setText("해장국"));
        tablayout.addTab(tablayout.newTab().setText("일식"));

        // TabLayout의 탭 선택 리스너 설정
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        // ViewPager에 어댑터 연결
        MenuPagerAdapter pagerAdapter = new MenuPagerAdapter(requireActivity()); // FragmentActivity를 사용
        viewPager.setAdapter(pagerAdapter);

        return view;
    }

    public static class MenuPagerAdapter extends FragmentStateAdapter {

        public MenuPagerAdapter(FragmentActivity activity) {
            super(activity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new TodayMenuFragment();
                case 1:
                    return new MaraTangFragment();
                case 2:
                    return new BunsikFragment();
                case 3:
                    return new HomemadeCutletFragment();
                case 4:
                    return new HaejangSoupFragment();
                case 5:
                    return new JpFoodFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 6;
        }
    }
}
