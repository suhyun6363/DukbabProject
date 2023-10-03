package kr.ac.duksung.dukbab.Home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;

public class MenuPageAdapter extends FragmentStateAdapter {
    private TabLayout tabLayout;

    public MenuPageAdapter(FragmentActivity activity, TabLayout tabLayout) {
        super(activity);
        this.tabLayout = tabLayout;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String selectedTabText = tabLayout.getTabAt(position).getText().toString();

        return MenuViewFragment.newInstance(selectedTabText);
    }

    @Override
    public int getItemCount() {
        return tabLayout.getTabCount();
    }
}
