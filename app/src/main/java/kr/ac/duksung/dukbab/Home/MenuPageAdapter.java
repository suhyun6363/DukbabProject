package kr.ac.duksung.dukbab.Home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;

public class MenuPageAdapter extends FragmentStateAdapter {
    private final int[] storeIds;
//    private final int[] congestionInfos;
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
        int congestionInfo = getCongestionInfoByPosition(position);
        return MenuViewFragment.newInstance(storeId, congestionInfo);
    }

    private int getCongestionInfoByPosition(int position) {
        switch (position) {
            case 1:
                return 0; // 첫 번째 탭에 대한 혼잡 정보
            case 2:
            case 3:
            case 4:
                return 2;
            case 5:
            case 6:
                return 1; // 두 번째부터 여섯 번째 탭까지는 혼잡 정보 2로 설정
            default:
                return 1; // 나머지 탭은 혼잡 정보 1로 설정
        }
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
