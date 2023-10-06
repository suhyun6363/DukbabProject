package kr.ac.duksung.dukbab.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.ac.duksung.dukbab.GridSpaceItemDecoration;
import kr.ac.duksung.dukbab.R;

public class MenuViewFragment extends Fragment implements MenuAdapter.MenuAdapterListener {
    public static final String TAG = "MenuViewFragment";
    private static final String ARG_TAB_TEXT = "tab_text";

    private ImageView imageView;
    private TextView tabText;
    private RecyclerView recyclerView, cartView;
    private MenuAdapter menuAdapter;
    public List<CartDTO> cartList = new ArrayList<>();
    private List<String> newSelectedOptionList = new ArrayList<>();
    private CartDTO cartItem;
    //private MenuViewListener menuViewListener;
    public OptionDrawerFragment optionDrawerFragment;
/*
    public interface MenuViewListener {
        void cartItemToHomeFragment(CartDTO cartItem);
    }
  */
    // MenuViewFragment를 생성하고 필요한 데이터를 전달하는 정적 메서드
    public static MenuViewFragment newInstance(String tabText) {
        MenuViewFragment fragment = new MenuViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TAB_TEXT, tabText);

        fragment.setArguments(args);
        return fragment;
    }

    public void openOptionDrawerFragment(MenuDTO menu) {
        optionDrawerFragment = OptionDrawerFragment.newInstance(menu);
        optionDrawerFragment.show(getParentFragmentManager(), OptionDrawerFragment.TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menuview, container, false);

        imageView = view.findViewById(R.id.congestionImg);
        tabText = view.findViewById(R.id.tabText);
        recyclerView = view.findViewById(R.id.menuRecyclerView);


        // 전달된 데이터를 가져와서 화면에 설정
        Bundle args = getArguments();
        if (args != null) {
            String tabTextString = args.getString(ARG_TAB_TEXT);

            imageView.setImageResource(getCongestionImg());
            tabText.setText(tabTextString);
            List<MenuDTO> menuList = getMenuData();

            // RecyclerView에 메뉴 데이터 설정
            menuAdapter = new MenuAdapter(menuList); //
            menuAdapter.setMenuAdapterListener(this);
            recyclerView.setAdapter(menuAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            recyclerView.addItemDecoration(new GridSpaceItemDecoration(3, 28));
        }

        return view;
    }

    private int getCongestionImg() {
        return R.drawable.img_redcircle;
    }


    private List<MenuDTO> getMenuData() {
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
