package kr.ac.duksung.dukbab.Home;

import android.util.Log;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.ac.duksung.dukbab.db.Database;
import kr.ac.duksung.dukbab.GridSpaceItemDecoration;
import kr.ac.duksung.dukbab.R;
import kr.ac.duksung.dukbab.db.MenuDBOpenHelper;

public class MenuViewFragment extends Fragment implements MenuAdapter.MenuAdapterListener {

    //    private Map<String, HomeFragment.StoreCongestion> storeCongestionMap = new HashMap<>();
    public static final String TAG = "MenuViewFragment";

    private ImageView imageView;
    private TextView tabText;
    private RecyclerView recyclerView, cartView;
    private MenuAdapter menuAdapter;
    public List<CartDTO> cartList = new ArrayList<>();
    private List<String> newSelectedOptionList = new ArrayList<>();
    private CartDTO cartItem;
    public OptionDrawerFragment optionDrawerFragment;
    private static final String ARG_STORE_ID = "storeId";
    private int heartFlag = 0;

    // MenuViewFragment를 생성하고 필요한 데이터를 전달하는 정적 메서드
    public static MenuViewFragment newInstance(int storeId, String jsonRecommendations) {
        MenuViewFragment fragment = new MenuViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STORE_ID, storeId); // storeId를 Bundle에 추가
        args.putString("jsonRecommendations", jsonRecommendations);
        fragment.setArguments(args);
        return fragment;
    }

    public void openOptionDrawerFragment(MenuDTO menu) {
        optionDrawerFragment = OptionDrawerFragment.newInstance(menu, heartFlag);
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
            int storeId = args.getInt(ARG_STORE_ID);
            String jsonRecommendations = args.getString("jsonRecommendations");

            imageView.setImageResource(getCongestionImg(storeId));

            switch (storeId) {
                case 1:
                    tabText.setText("추천");
                    break;
                case 2:
                    tabText.setText("오늘의 메뉴");
                    break;
                case 3:
                    tabText.setText("마라탕");
                    break;
                case 4:
                    tabText.setText("분식");
                    break;
                case 5:
                    tabText.setText("수제돈까스");
                    break;
                case 6:
                    tabText.setText("파스타");
                    break;
                default:
                    tabText.setText("기타 가게");
                    break;
            }

            if(storeId != 1) {
                List<MenuDTO> menuList = getMenuData(storeId);
                menuAdapter = new MenuAdapter(menuList, getParentFragmentManager()); //
            }
            else {
                if(jsonRecommendations != null) {
                    List<MenuDTO> recommendMenuList = getRecommendData(jsonRecommendations);
                    menuAdapter = new MenuAdapter(recommendMenuList, getParentFragmentManager()); //
                }
                else {
                    List<MenuDTO> menuList = new ArrayList<>();
                    menuAdapter = new MenuAdapter(menuList, getParentFragmentManager()); //
                }

            }

            // RecyclerView에 메뉴 데이터 설정
            menuAdapter.setMenuAdapterListener(this);
            recyclerView.setAdapter(menuAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            recyclerView.addItemDecoration(new GridSpaceItemDecoration(3, 28));

            // menu.db에 데이터 추가
            Database database = Database.getInstance();
            database.openMenuDB(requireContext()); // Menu 데이터베이스 열기
            database.deleteDuplicateMenus();

            //오늘의 메뉴에 메뉴 추가
            database.addMenu("오늘의 메뉴A", "6,500", String.valueOf(R.drawable.placeholder_image), 0, 2);
            database.addMenu("오늘의 메뉴B", "6,500", String.valueOf(R.drawable.placeholder_image), 0, 2);

            // 마라탕 가게에 메뉴 추가
            database.addMenu("마라탕", "6,900", String.valueOf(R.drawable.placeholder_image), 0, 3);
            database.addMenu("마라샹궈", "8,700", String.valueOf(R.drawable.placeholder_image), 0, 3);
            database.addMenu("파인애플볶음밥", "7,000", String.valueOf(R.drawable.placeholder_image), 0, 3);
            database.addMenu("빙홍차", "2,500", String.valueOf(R.drawable.placeholder_image), 0, 3);
            database.addMenu("꿔바로우(소)", "5,000", String.valueOf(R.drawable.placeholder_image), 0, 3);
            database.addMenu("꿔바로우(대)", "7,000", String.valueOf(R.drawable.placeholder_image), 0, 3);

            // 분식 가게에 메뉴 추가
            database.addMenu("마성떡볶이", "3,500", String.valueOf(R.drawable.placeholder_image), 0, 4);
            database.addMenu("치킨꼬치떡볶이", "5,500", String.valueOf(R.drawable.placeholder_image), 0, 4);
            database.addMenu("마성라면", "3,500", String.valueOf(R.drawable.placeholder_image), 0, 4);
            database.addMenu("만두라면", "4,000", String.valueOf(R.drawable.placeholder_image), 0, 4);
            database.addMenu("치즈라면", "4,000", String.valueOf(R.drawable.placeholder_image), 0, 4);
            database.addMenu("부산어묵", "2,000", String.valueOf(R.drawable.placeholder_image), 0, 4);
            database.addMenu("빨간어묵", "2,500", String.valueOf(R.drawable.placeholder_image), 0, 4);
            database.addMenu("찰순대", "3,500", String.valueOf(R.drawable.placeholder_image), 0, 4);
            database.addMenu("버터장조림덮밥", "5,000", String.valueOf(R.drawable.placeholder_image), 0, 4);
            database.addMenu("치킨마요덮밥", "5,000", String.valueOf(R.drawable.placeholder_image), 0, 4);
            database.addMenu("마성김밥", "3,000", String.valueOf(R.drawable.placeholder_image), 0, 4);
            database.addMenu("참치김밥", "3,500", String.valueOf(R.drawable.placeholder_image), 0, 4);
            database.addMenu("모듬튀김", "5,000", String.valueOf(R.drawable.placeholder_image), 0, 4);
            database.addMenu("오징어튀김", "5,000", String.valueOf(R.drawable.placeholder_image), 0, 4);
            database.addMenu("야채튀김", "3,000", String.valueOf(R.drawable.placeholder_image), 0, 4);
            database.addMenu("삼각잡채말이만두", "5,000", String.valueOf(R.drawable.placeholder_image), 0, 4);
            database.addMenu("고추튀김", "3,000", String.valueOf(R.drawable.placeholder_image), 0, 4);

            // 수제돈까스 가게에 메뉴 추가
            database.addMenu("카레덮밥", "5,000", String.valueOf(R.drawable.placeholder_image), 0, 5);
            database.addMenu("돈카츠", "6,900", String.valueOf(R.drawable.placeholder_image), 0, 5);
            database.addMenu("돈카츠카레덮밥", "7,500", String.valueOf(R.drawable.placeholder_image), 0, 5);
            database.addMenu("새우카레덮밥", "7,500", String.valueOf(R.drawable.placeholder_image), 0, 5);
            database.addMenu("더블돈카츠", "9,500", String.valueOf(R.drawable.placeholder_image), 0, 5);

            // 파스타 가게에 메뉴 추가
            database.addMenu("고기리들기름파스타", "5,000", String.valueOf(R.drawable.placeholder_image), 0, 6);
            database.addMenu("우삼겹알리올리오", "6,900", String.valueOf(R.drawable.placeholder_image), 0, 6);
            database.addMenu("클래식토마토파스타", "7,500", String.valueOf(R.drawable.placeholder_image), 0, 6);
            database.addMenu("트러플버섯크림파스타", "7,500", String.valueOf(R.drawable.placeholder_image), 0, 6);
            database.addMenu("4분돼지김치파스타", "9,500", String.valueOf(R.drawable.placeholder_image), 0, 6);
            database.addMenu("대패삼겹크림파스타", "5,000", String.valueOf(R.drawable.placeholder_image), 0, 6);
            database.addMenu("매콤로제파스타", "6,900", String.valueOf(R.drawable.placeholder_image), 0, 6);
            database.addMenu("김치삼겹필라프", "7,500", String.valueOf(R.drawable.placeholder_image), 0, 6);
            database.addMenu("콜라(500ml)", "7,500", String.valueOf(R.drawable.placeholder_image), 0, 6);
            database.addMenu("사이다(500ml)", "9,500", String.valueOf(R.drawable.placeholder_image), 0, 6);

            database.closeMenuDB(); // Menu 데이터베이스 닫기
        }
        return view;
    }

    private int getCongestionImg(int storeId) {
        if (storeId == 1) {
            return R.drawable.ic_heart_fill;
        } else {
            // 추천 제외한 가게 탭들은 혼잡도 뜸
            return R.drawable.img_greencircle;
        }
    }

//    private int getCongestionImg(int storeId) {
//        if (storeId == 1) {
//            // For storeId 1, set a specific image resource
//            return R.drawable.ic_heart_fill; // Use the appropriate image resource
//        }
//
//        if (storeCongestionMap != null) {
//            HomeFragment.StoreCongestion congestion = storeCongestionMap.get(String.valueOf(storeId));
//            if (congestion != null) {
//                int imageResourceId = congestion.getCongestionImageResourceId();
//                return imageResourceId;
//            }
//        }
//
//        // 혼잡도 이미지를 기본 이미지로 설정하거나, 다른 로직에 따라 적절한 이미지 리소스 ID 반환
//        return R.drawable.img_greencircle;
//    }



    private List<MenuDTO> getMenuData(int storeId) {
        List<MenuDTO> menuList = new ArrayList<>();

        // storeId에 해당하는 가게의 메뉴만 데이터베이스에서 검색
        Database database = Database.getInstance();
        database.openMenuDB(requireContext());
        Cursor cursor = database.searchMenuByStoreId(storeId);

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    int menuNameIndex = cursor.getColumnIndex(MenuDBOpenHelper.COLUMN_MENU_NAME);
                    int priceIndex = cursor.getColumnIndex(MenuDBOpenHelper.COLUMN_MENU_PRICE);
                    int imgIndex = cursor.getColumnIndex(MenuDBOpenHelper.COLUMN_MENU_IMG);
                    String menuName = cursor.getString(menuNameIndex);
                    String price = cursor.getString(priceIndex);
                    String img = cursor.getString(imgIndex);

                    // 검색된 메뉴를 MenuDTO 객체로 생성하여 메뉴 목록에 추가
                    menuList.add(new MenuDTO(storeId, menuName, price, Integer.parseInt(img)));
                }
            } finally {
                cursor.close();
                database.closeMenuDB();
            }
        }
        return menuList;
    }

    private List<MenuDTO> getRecommendData(String jsonRecommendations) {
        List<MenuDTO> recommendMenuList = new ArrayList<>();
        // jsonRecommendations를 파싱하여 추천된 메뉴 ID 목록을 얻어옴
        List<Integer> recommendedMenuIds = new Gson().fromJson(jsonRecommendations, new TypeToken<List<Integer>>() {
        }.getType());

        // 데이터베이스에서 추천된 메뉴 ID에 해당하는 메뉴를 검색하고 recommendMenuList에 추가함
        Database database = Database.getInstance();
        //database.openMenuDB(requireContext());

        MenuDBOpenHelper dbHepler = new MenuDBOpenHelper(requireContext());


        for (int recommendMenuId : recommendedMenuIds) {
            Cursor cursor = database.searchMenuByMenuId(recommendMenuId);
            if (cursor != null && cursor.moveToFirst()) {
                int storeIdIndex = cursor.getColumnIndex(MenuDBOpenHelper.COLUMN_STORE_ID);
                int menuNameIndex = cursor.getColumnIndex(MenuDBOpenHelper.COLUMN_MENU_NAME);
                int priceIndex = cursor.getColumnIndex(MenuDBOpenHelper.COLUMN_MENU_PRICE);
                int imgIndex = cursor.getColumnIndex(MenuDBOpenHelper.COLUMN_MENU_IMG);

                int storeId = cursor.getInt(storeIdIndex);
                String menuName = cursor.getString(menuNameIndex); // 메뉴 이름을 가져옵니다.
                String price = cursor.getString(priceIndex);
                String img = cursor.getString(imgIndex);

                // 검색된 메뉴를 MenuDTO 객체로 생성하여 recommendMenuList에 추가
                recommendMenuList.add(new MenuDTO(storeId, menuName, price, Integer.parseInt(img)));

                // 메뉴 이름을 출력합니다.
                Log.d("MenuName", "MenuName for menuId " + recommendMenuId + ": " + menuName);

                cursor.close();
            }
        }



        database.closeMenuDB();
        return recommendMenuList;
    }
}