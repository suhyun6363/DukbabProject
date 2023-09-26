package kr.ac.duksung.dukbab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class TodayMenuFragment extends Fragment {

    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;

    public TodayMenuFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todaymenu, container, false);

        recyclerView = view.findViewById(R.id.menuRecyclerView);

        // 가상의 메뉴 데이터를 생성합니다. 실제 데이터로 대체하세요.
        List<MenuDTO> menuList = generateMenuData();

        // RecyclerView에 어댑터를 설정합니다.
        menuAdapter = new MenuAdapter(menuList);
        recyclerView.setAdapter(menuAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.addItemDecoration(new GridSpaceItemDecoration(3, 28));

        return view;
    }

    // 가상의 메뉴 데이터를 생성하는 메서드 (실제 데이터로 대체 필요)
    private List<MenuDTO> generateMenuData() {
        List<MenuDTO> menuList = new ArrayList<>();

        // 메뉴 항목을 생성하고 리스트에 추가합니다.
        menuList.add(new MenuDTO("마라탕", "10,000", R.drawable.img_maratang));
        menuList.add(new MenuDTO("마라샹궈", "12,000", R.drawable.img_maratang));
        menuList.add(new MenuDTO("빙홍차", "8,000", R.drawable.img_maratang));
        menuList.add(new MenuDTO("꿔바로우", "10,000", R.drawable.img_maratang));

        menuList.add(new MenuDTO("마라탕", "10,000", R.drawable.img_maratang));
        menuList.add(new MenuDTO("마라샹궈", "12,000", R.drawable.img_maratang));
        menuList.add(new MenuDTO("빙홍차", "8,000", R.drawable.img_maratang));
        menuList.add(new MenuDTO("꿔바로우", "10,000", R.drawable.img_maratang));


        // 실제 데이터로 대체하세요.

        return menuList;
    }
}
