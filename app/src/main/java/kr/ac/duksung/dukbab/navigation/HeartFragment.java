package kr.ac.duksung.dukbab.navigation;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.ac.duksung.dukbab.GridSpaceItemDecoration;
import kr.ac.duksung.dukbab.Home.MenuDTO;
import kr.ac.duksung.dukbab.R;

public class HeartFragment extends Fragment {

    private List<MenuDTO> heartMenuList = new ArrayList<>();
    private HeartAdapter heartAdapter;
    private RecyclerView heartView;
    private FragmentManager context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getParentFragmentManager();
        View view = inflater.inflate(R.layout.fragment_heart, container, false);

        heartView = view.findViewById(R.id.heartView);

        heartAdapter = new HeartAdapter(context, heartMenuList);
        heartView.setAdapter(heartAdapter);
        heartView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        Log.d("HeartFragment", "onCreate 실행");

        return view;
    }

    // HeartFragment에 메뉴를 추가하는 메서드
    public void addMenuToHeart(MenuDTO menu) {
        heartMenuList.add(menu);
        heartAdapter.notifyDataSetChanged();
    }

    // ...

    // HeartFragment에서 메뉴를 삭제하는 메서드
    public void removeMenuFromHeart(MenuDTO menu) {
        heartMenuList.remove(menu);
        heartAdapter.notifyDataSetChanged();
    }
}
