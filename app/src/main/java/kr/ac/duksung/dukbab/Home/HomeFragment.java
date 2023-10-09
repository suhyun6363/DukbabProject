package kr.ac.duksung.dukbab.Home;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class HomeFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private RecyclerView cartView;
    private CartAdapter cartAdapter;
    private CartDTO cartItem;
    private TextView totalCountTextView, totalPriceTextView, removeAll;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        cartView = view.findViewById(R.id.cart);
        totalCountTextView = view.findViewById(R.id.totalCount);
        totalPriceTextView = view.findViewById(R.id.totalPrice);
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

            removeAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeAllClicked(view);
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

        else {
            orderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // cartList가 비어있을 때 AlertDialog를 표시
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("장바구니가 비어 있습니다")
                            .setMessage("장바구니에 메뉴를 추가해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });

            cartAdapter = new CartAdapter(cartList);
            cartView.setAdapter(cartAdapter);
            cartView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        return view;
    }

    public void addMenuToCart(CartDTO cartItem) {
        cartList.add(cartItem);
        cartAdapter.notifyDataSetChanged();
    }

    // 전체 삭제 TextView 클릭 시 호출되는 메서드
    public void removeAllClicked(View view) {
        // 카트에 담긴 모든 상품 삭제
        cartList.clear();
        // RecyclerView 업데이트
        cartAdapter.notifyDataSetChanged();
    }
}

