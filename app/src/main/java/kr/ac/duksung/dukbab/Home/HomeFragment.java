package kr.ac.duksung.dukbab.Home;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kr.ac.duksung.dukbab.R;
import kr.ac.duksung.dukbab.db.CartDBOpenHelper;
import kr.ac.duksung.dukbab.db.Database;
import kr.ac.duksung.dukbab.db.OrderDBOpenHelper;
import kr.ac.duksung.dukbab.db.ReviewDBOpenHelper;

public class HomeFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private RecyclerView cartRecyclerView;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 데이터베이스 헬퍼 초기화
        CartDBOpenHelper dbHelper = new CartDBOpenHelper(requireContext());

        // 데이터베이스에서 cartItems 가져오기
        cartList = dbHelper.getCartItems();
    }

    public void processOrder() {
        // Cart 데이터를 가져올 DBHelper를 생성
        CartDBOpenHelper cartDBOpenHelper = new CartDBOpenHelper(requireContext());

        // Order 데이터를 저장할 DBHelper를 생성
        OrderDBOpenHelper orderDBOpenHelper = new OrderDBOpenHelper(requireContext());

        // 데이터베이스 연결을 가져옴
        SQLiteDatabase cartDB = cartDBOpenHelper.getReadableDatabase();
        SQLiteDatabase orderDB = orderDBOpenHelper.getWritableDatabase();

        // cart.db에서 orders.db에 복사할 데이터를 선택
        String[] columns = {
                CartDBOpenHelper.COLUMN_EMAIL,
                CartDBOpenHelper.COLUMN_NICKNAME,
                CartDBOpenHelper.COLUMN_STORE_ID,
                CartDBOpenHelper.COLUMN_MENU_NAME,
                CartDBOpenHelper.COLUMN_MENU_OPTION,
                CartDBOpenHelper.COLUMN_MENU_PRICE,
                CartDBOpenHelper.COLUMN_MENU_QUANTITY,
        };

        int totalOrderPrice = 0;

        // cart.db에서 데이터를 읽어와서 orders.db로 복사
        Cursor cursor = cartDB.query(CartDBOpenHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ContentValues values = new ContentValues();
                values.put(OrderDBOpenHelper.COLUMN_EMAIL, cursor.getString(cursor.getColumnIndex(CartDBOpenHelper.COLUMN_EMAIL)));
                values.put(OrderDBOpenHelper.COLUMN_NICKNAME, cursor.getString(cursor.getColumnIndex(CartDBOpenHelper.COLUMN_NICKNAME)));
                values.put(OrderDBOpenHelper.COLUMN_STORE_ID, cursor.getString(cursor.getColumnIndex(CartDBOpenHelper.COLUMN_STORE_ID)));
                values.put(OrderDBOpenHelper.COLUMN_MENU_NAME, cursor.getString(cursor.getColumnIndex(CartDBOpenHelper.COLUMN_MENU_NAME)));
                values.put(OrderDBOpenHelper.COLUMN_MENU_OPTION, cursor.getString(cursor.getColumnIndex(CartDBOpenHelper.COLUMN_MENU_OPTION)));
                values.put(OrderDBOpenHelper.COLUMN_MENU_PRICE, cursor.getString(cursor.getColumnIndex(CartDBOpenHelper.COLUMN_MENU_PRICE)));
                values.put(OrderDBOpenHelper.COLUMN_MENU_QUANTITY, cursor.getString(cursor.getColumnIndex(CartDBOpenHelper.COLUMN_MENU_QUANTITY)));

                // 메뉴 가격과 수량을 가져옴
                String menuPriceString = cursor.getString(cursor.getColumnIndex(CartDBOpenHelper.COLUMN_MENU_PRICE));
                int menuPrice = Integer.parseInt(menuPriceString.replaceAll("[^0-9]", "")); // "￦ 11000"에서 숫자만 추출

                int menuQuantity = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CartDBOpenHelper.COLUMN_MENU_QUANTITY)));

                // 해당 메뉴의 총 가격을 계산하고 누적
                int totalPrice = menuPrice * menuQuantity;
                totalOrderPrice += totalPrice; // 총 주문 가격에 누적

                // orders.db에 데이터 추가
                values.put(OrderDBOpenHelper.COLUMN_TOTAL_PRICE, String.valueOf(totalOrderPrice)); // 메뉴의 총 가격 저장

                // 현재 날짜와 시간을 가져오는 부분
                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String orderDate = dateFormat.format(currentDate);
                values.put(OrderDBOpenHelper.COLUMN_ORDER_DATE, orderDate);


                long result = orderDB.insert(OrderDBOpenHelper.TABLE_NAME, null, values);
            }
            cursor.close(); // 커서를 닫음
        }

        // cart.db 초기화
        cartDBOpenHelper.clearCartDatabase();

        // 데이터베이스 연결 종료
        cartDB.close();
        orderDB.close();

        // 사용자에게 메시지 표시 또는 다음 화면으로 이동 등의 작업 수행
        Toast.makeText(getActivity(), "주문 화면으로 넘어갑니다", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        // RecyclerView 초기화
        cartRecyclerView = view.findViewById(R.id.cart);

        // Adapter 초기화 및 RecyclerView에 연결
        cartAdapter = new CartAdapter(cartList, this);
        cartRecyclerView.setAdapter(cartAdapter);

        // RecyclerView에 레이아웃 매니저 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        cartRecyclerView.setLayoutManager(layoutManager);

        totalCountTextView = view.findViewById(R.id.totalCount);
        totalPriceTextView = view.findViewById(R.id.totalPrice);
        removeAll = view.findViewById(R.id.removeAll);
        orderBtn = view.findViewById(R.id.order_btn);

        // 추천 탭 추가
        tabLayout.addTab(tabLayout.newTab().setText("추천"));
        tabLayout.addTab(tabLayout.newTab().setText("오늘의 메뉴"));
        tabLayout.addTab(tabLayout.newTab().setText("마라탕"));
        tabLayout.addTab(tabLayout.newTab().setText("분식"));
        tabLayout.addTab(tabLayout.newTab().setText("수제돈까스"));
        tabLayout.addTab(tabLayout.newTab().setText("파스타"));

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
            cartAdapter = new CartAdapter(cartList, this);
            cartRecyclerView.setAdapter(cartAdapter);
            cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            cartAdapter.notifyDataSetChanged();

            removeAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeAllClicked(view);
                    updateTotalPriceAndCount();

                }
            });
            updateTotalPriceAndCount();
        }

        else {
            orderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cartList.isEmpty()) {
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
                    else {
                        Log.d(TAG, "btnCartToOrder 클릭됨"); // 로그 메시지 추가

                        processOrder();

                        // 장바구니 RecyclerView 갱신
                        cartList.clear();
                        cartAdapter.notifyDataSetChanged();
                        // 주문이 완료되면 다음 Activity로 이동
                    }
                }

            });
        }

        // Store 데이터 삽입
        Database database = Database.getInstance();
        database.openStoreDB(requireContext()); // Store 데이터베이스 열기
        database.getStoreData(); // Store 데이터 조회

        //중복 데이터 삭제
        database.deleteDuplicateStores();

        // 가게 정보 추가
        // 혼잡도 추가할 예정
        database.addStore(1,"오늘의 메뉴", "Actual congestion info for 오늘의 메뉴");
        database.addStore(2,"마라탕", "Actual congestion info for 마라탕");
        database.addStore(3,"분식", "Actual congestion info for 분식");
        database.addStore(4,"수제돈까스", "Actual congestion info for 수제돈까스");
        database.addStore(5,"파스타", "Actual congestion info for 파스타");

        // Store 데이터베이스 닫기
        database.closeStoreDB();

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

        // CartDBOpenHelper 인스턴스를 생성
        CartDBOpenHelper dbOpenHelper = new CartDBOpenHelper(requireContext()); // 여기서 'v.getContext()'를 사용하여 Context를 가져옵니다.
        dbOpenHelper.clearCartDatabase();

        // 총 가격 및 수량 업데이트
        updateTotalPriceAndCount();
    }

    // 메서드를 추가하여 totalPriceTextView와 totalCountTextView 업데이트
    public void updateTotalPriceAndCount() {
        int totalMenuPrice = 0;
        int totalQuantity = 0;

        // cartList를 반복하면서 합을 계산
        for (CartDTO cartItem : cartList) {
            int listMenuPrice = Integer.parseInt(cartItem.getMenuPrice().replace(",", ""));
            int listMenuQuantity = cartItem.getMenuQuantity();

            // 각 항목의 가격과 수량을 곱하여 합계에 더함
            totalMenuPrice += (listMenuPrice * listMenuQuantity);
            totalQuantity += listMenuQuantity;
        }

        // 합계를 포맷팅하여 TextView에 표시
        String formattedTotalPrice = String.format("%,d", totalMenuPrice); // 가격을 포맷팅
        totalPriceTextView.setText(formattedTotalPrice);

        totalCountTextView.setText(String.valueOf(totalQuantity)); // 수량은 문자열로 설정
    }
}