package kr.ac.duksung.dukbab.Home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;

public class MenuPageAdapter extends FragmentStateAdapter {
    private final int[] storeIds;
    private final TabLayout tabLayout;

    public MenuPageAdapter(FragmentActivity activity, final TabLayout tabLayout) {
        super(activity);
        this.tabLayout = tabLayout;
        this.storeIds = new int[]{
                1, // 추천
                2, // 오늘의 메뉴
                3, // 마라탕
                4, // 분식
                5, // 수제돈까스
                6 // 파스타
        };
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int storeId = getStoreIdByPosition(position);
        return MenuViewFragment.newInstance(storeId);
    }

    private int getStoreIdByPosition(int position) {
        if (position >= 0 && position < storeIds.length) {
            return storeIds[position];
        }
        // 기본값으로 1
        return 1;
    }

    @Override
    public int getItemCount() {
        return storeIds.length;
    }
}
