package kr.ac.duksung.dukbab.Home;

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
                1, // 오늘의 메뉴의 경우, 원하는 가게의 storeId로 변경
                2, // 마라탕의 경우, 원하는 가게의 storeId로 변경
                3, // 분식의 경우, 원하는 가게의 storeId로 변경
                4, // 수제돈까스의 경우, 원하는 가게의 storeId로 변경
                5  // 파스타의 경우, 원하는 가게의 storeId로 변경
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
        // 기본값으로 설정
        return 1;
    }

    @Override
    public int getItemCount() {
        return storeIds.length;
    }
}
