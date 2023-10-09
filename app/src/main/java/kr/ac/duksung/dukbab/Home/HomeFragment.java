package kr.ac.duksung.dukbab.Home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import kr.ac.duksung.dukbab.R;
import kr.ac.duksung.dukbab.db.CartDBOpenHelper;

public class HomeFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private RecyclerView cartView;
    private CartAdapter cartAdapter;
    private CartDTO cartItem;
    private TextView totalCount, totalPrice, removeAll;
    private Button orderBtn;
    private List<CartDTO> cartList = new ArrayList<>();

    public static HomeFragment newInstance(CartDTO cartItem) {
        Bundle args = new Bundle();
        args.putParcelable("cartItem", cartItem); // 데이터를 추가
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize your database helper
        CartDBOpenHelper dbHelper = new CartDBOpenHelper(requireContext());

        // Retrieve cart items from the database
        cartList = dbHelper.getCartItems();

        // Initialize your RecyclerView adapter
        cartAdapter = new CartAdapter(cartList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        // RecyclerView 초기화
        RecyclerView cartRecyclerView = view.findViewById(R.id.cart);

        // RecyclerView에 어댑터 설정
        cartAdapter = new CartAdapter(cartList);
        cartRecyclerView.setAdapter(cartAdapter);

        // RecyclerView에 레이아웃 매니저 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        cartRecyclerView.setLayoutManager(layoutManager);

        totalCount = view.findViewById(R.id.totalCount);
        totalPrice = view.findViewById(R.id.totalPrice);
        removeAll = view.findViewById(R.id.removeAll);
        orderBtn = view.findViewById(R.id.order_btn);

        // 탭 추가
        tabLayout.addTab(tabLayout.newTab().setText("오늘의 메뉴"));
        tabLayout.addTab(tabLayout.newTab().setText("마라탕"));
        tabLayout.addTab(tabLayout.newTab().setText("분식"));
        tabLayout.addTab(tabLayout.newTab().setText("수제돈까스"));
        tabLayout.addTab(tabLayout.newTab().setText("해장국"));
        tabLayout.addTab(tabLayout.newTab().setText("일식"));

        // ViewPager에 어댑터 연결
        MenuPageAdapter pageAdapter = new MenuPageAdapter(requireActivity(), tabLayout);
        viewPager.setAdapter(pageAdapter);

        // ViewPager2 페이지 변경 이벤트 감지
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                // ViewPager2 페이지가 변경될 때 탭을 선택한 것처럼 업데이트
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        // TabLayout의 탭 선택 리스너 설정
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        Bundle args = getArguments();
        if(args != null) {
            cartItem = args.getParcelable("cartItem");
            cartList.add(cartItem);
    cartAdapter = new CartAdapter(cartList);
            cartView.setAdapter(cartAdapter);
            cartView.setLayoutManager(new LinearLayoutManager(getContext()));

            cartAdapter.notifyDataSetChanged();

            totalCount.setText("총 " + cartList.size() + "개");

            //totalPrice.

            removeAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartList.clear();
                    cartAdapter.notifyDataSetChanged();
                }
            });

            // 주문 버튼 클릭 리스너 설정
            orderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 주문 정보를 다음 화면(주문 창 액티비티)으로 전달
                    Intent intent = new Intent(getContext(), OrderActivity.class);
                    intent.putParcelableArrayListExtra("cartList", new ArrayList<>(cartList));
                    startActivity(intent);
                }
            });
        }
        return view;
    }
}

