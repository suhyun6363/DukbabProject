package kr.ac.duksung.dukbab.Home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;
import kr.ac.duksung.dukbab.api.MyCallback;

public class MenuPageAdapter extends FragmentStateAdapter{
    private final int[] storeIds;
    private final TabLayout tabLayout;
    private String jsonRecommendations;

    public MenuPageAdapter(FragmentActivity activity, final TabLayout tabLayout, String jsonRecommendations) {
        super(activity);
        this.tabLayout = tabLayout;
        this.storeIds = new int[]{
                1, // 추천
                2, // 오늘의 메뉴
                3, // 마라탕
                4, // 분식
                5, // 수제돈까스
                6 // 파스타'
        };
        this.jsonRecommendations = jsonRecommendations;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int storeId = getStoreIdByPosition(position);
        Log.d("MenuPageAdapter", "storeId: "+storeId);
        if(jsonRecommendations != null)
            Log.d("MenuPageAdapter", "jsonRecommendations: "+jsonRecommendations);
        else
            Log.d("MenuPageAdapter", "null입");

        return MenuViewFragment.newInstance(storeId, jsonRecommendations);
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
