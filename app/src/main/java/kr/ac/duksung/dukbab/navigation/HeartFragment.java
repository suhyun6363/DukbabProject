package kr.ac.duksung.dukbab.navigation;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import kr.ac.duksung.dukbab.Home.MenuDTO;
import kr.ac.duksung.dukbab.R;
import kr.ac.duksung.dukbab.api.RecommendRetrofit;

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
        Button recommendButton = view.findViewById(R.id.recommendButton);

        heartAdapter = new HeartAdapter(context, heartMenuList);
        heartView.setAdapter(heartAdapter);
        heartView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        Log.d("HeartFragment", "onCreate 실행");

        // recommendButton 클릭 시 이벤트 처리
        recommendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // HeartFragment에서 선택한 메뉴 목록을 RecommendRetrofit으로 보냅니다.
                RecommendRetrofit.sendMenuIds(heartMenuList);
            }
        });

        // Log the menu IDs in JSON format
        logMenuIdsInJsonFormat();

        return view;
    }

    // HeartFragment에 메뉴를 추가하는 메서드
    public void addMenuToHeart(MenuDTO menu) {
        heartMenuList.add(menu);
        heartAdapter.notifyDataSetChanged();
        // Log the menu IDs in JSON format after adding a menu
        logMenuIdsInJsonFormat();
    }

    // HeartFragment에서 메뉴를 삭제하는 메서드
    public void removeMenuFromHeart(MenuDTO menu) {
        heartMenuList.remove(menu);
        heartAdapter.notifyDataSetChanged();
        // Log the menu IDs in JSON format after removing a menu
        logMenuIdsInJsonFormat();
    }

    private void logMenuIdsInJsonFormat() {
        List<Integer> menuIds = new ArrayList<>();
        for (MenuDTO menu : heartMenuList) {
            menuIds.add(menu.getMenuId());
        }
        Gson gson = new Gson();
        String jsonMenuIds = gson.toJson(menuIds);
        Log.d("HeartFragment", "Menu IDs in JSON format: " + jsonMenuIds);
    }
}