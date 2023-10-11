package kr.ac.duksung.dukbab.Home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kr.ac.duksung.dukbab.HomeActivity;
import kr.ac.duksung.dukbab.R;
import kr.ac.duksung.dukbab.db.CartDBOpenHelper;
import kr.ac.duksung.dukbab.db.MenuDBOpenHelper;
import kr.ac.duksung.dukbab.db.OrderDBOpenHelper;

public class OptionDrawerFragment extends BottomSheetDialogFragment {
    public static final String TAG = "OptionDrawerFragment";

    private MenuDTO menu;
    private ImageView menuImg, minusButton, plusButton, heartButton;
    private TextView menuName, menuPrice, quantityTextView, optionTotalPrice;
    private RecyclerView optionView;
    private Button btnCartToOrder;
    private List<String> selectedOptionsList = new ArrayList<>();
    private List<String> newSelectedOptionList = new ArrayList<>();
    private boolean isHeartSelected = false;
    private CartDTO cartItem;
    private int quantityInt = 1;
    private BtnaddToCartListener btnaddToCartListener;

    public interface BtnaddToCartListener {
        void nodifyChange();
    }

    public void setBtnaddToCartListener(BtnaddToCartListener btnaddToCartListener) {
        this.btnaddToCartListener = btnaddToCartListener;
    }

    public static OptionDrawerFragment newInstance(MenuDTO menu) {
        OptionDrawerFragment fragment = new OptionDrawerFragment();
        Bundle args = new Bundle();
        args.putParcelable("menu", menu);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_optiondrawer, container, false);

        /*
        // Fragment의 높이 설정
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int peekHeight = (int) (screenHeight * 0.8);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, peekHeight);
*/
        menuImg = view.findViewById(R.id.imageView);
        menuName = view.findViewById(R.id.menuName);
        menuPrice = view.findViewById(R.id.menuPrice);
        //TextView detailContent = view.findViewById(R.id.detailContent);
        optionView = view.findViewById(R.id.optionView);
        Button btnAddToCart = view.findViewById(R.id.cart_btn);
        //drawerFooter = view.findViewById(R.id.drawerFooter);
        heartButton = view.findViewById(R.id.heart);
        minusButton = view.findViewById(R.id.minus);
        quantityTextView = view.findViewById(R.id.quantity);
        plusButton = view.findViewById(R.id.plus);
        optionTotalPrice = view.findViewById(R.id.optionTotalPrice);
        btnCartToOrder = view.findViewById(R.id.order_btn);

        Bundle args = getArguments();
        if (args != null) {
            menu = args.getParcelable("menu");
            // menu 객체를 사용하여 필요한 초기화 작업 수행
            menuImg.setImageResource(menu.getImageResourceId());
            menuName.setText(menu.getName());
            menuPrice.setText("￦ " + menu.getPrice());
            optionTotalPrice.setText("￦ " + menu.getPrice());

            List<OptionDTO> optionList = getOptionData();

            OptionAdapter optionAdapter = new OptionAdapter(optionList);
            optionView.setAdapter(optionAdapter);
            optionView.setLayoutManager(new LinearLayoutManager(getContext()));

            // Retrieve username and nickname from SharedPreferences
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
            String username = sharedPreferences.getString("username", ""); // 사용자 이메일
            String nickname = sharedPreferences.getString("nickname", ""); // 사용자 닉네임

            // 현재 날짜와 시간을 가져오는 부분
            Calendar calendar = Calendar.getInstance();
            Date currentDate = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String cartCreatedDate = dateFormat.format(currentDate);

            // "담기" 버튼 클릭 이벤트 처리
            // CartDTO 이용
            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedOptionsList = optionAdapter.getSelectedOptionList();
                    // 옵션 선택 정보와 메뉴 정보를 장바구니에 추가
                    if (selectedOptionsList.size() == optionList.size()) {
                        cartItem = createCartItem(menu, optionList, selectedOptionsList);
                        Log.d(TAG, cartItem.getMenuName() + cartItem.getMenuPrice() + cartItem.getSelectedOptions().toString());

/*                        // HomeFragment에 수신
                        if (getActivity() instanceof HomeActivity) {
                            HomeActivity activity = (HomeActivity) getActivity();
                            activity.addToCart(cartItem); // HeartFragment에 데이터 추가 메서드 호출
                        }
*/                        // 데이터베이스에 데이터 추가
                        CartDBOpenHelper dbHelper = new CartDBOpenHelper(requireContext());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        // OptionDrawerFragment 내에서 storeId 가져오기
                        MenuDBOpenHelper dbHelper2 = new MenuDBOpenHelper(requireContext());
                        String storeId = dbHelper2.getStoreIdByMenuName(menu.getName());

                        ContentValues values = new ContentValues();
                        values.put(CartDBOpenHelper.COLUMN_EMAIL, username);
                        values.put(CartDBOpenHelper.COLUMN_NICKNAME, nickname);
                        values.put(CartDBOpenHelper.COLUMN_STORE_ID, storeId); /////
                        values.put(CartDBOpenHelper.COLUMN_MENU_NAME, cartItem.getMenuName());
                        values.put(CartDBOpenHelper.COLUMN_MENU_OPTION, TextUtils.join(", ", cartItem.getSelectedOptions())); // 옵션을 쉼표로 구분하여 저장
                        values.put(CartDBOpenHelper.COLUMN_MENU_PRICE, cartItem.getMenuPrice());
                        values.put(CartDBOpenHelper.COLUMN_MENU_QUANTITY, cartItem.getMenuQuantity()); // 수량 기본값 1로 설정
                        values.put(CartDBOpenHelper.COLUMN_CART_CREATED_DATE, cartCreatedDate); // 현재 날짜 및 시간

                        long result = db.insert(CartDBOpenHelper.TABLE_NAME, null, values);
                        db.close();

                        // HomeFragment에 수신
                        Bundle args = new Bundle();
                        args.putParcelable("cartItem", cartItem);
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        HomeFragment homeFragment = new HomeFragment();
                        homeFragment.setArguments(args);
                        transaction.replace(R.id.main_content, homeFragment);
                        transaction.commit();

                        // 모달 다이얼로그 표시
                        showCartConfirmationDialog();

                        dismiss(); // 옵션창 닫기

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setMessage("옵션을 선택해주세요.");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 확인 버튼 클릭 시, 원하는 동작 수행
                            }
                        });
                        builder.show();
                    }

                }
            });

            /*
            // 다시 수정해야함!(CartDBOpenHelper column으로 복붙한 상태임)
            // "주문하기" 버튼 클릭 이벤트 처리
            btnCartToOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 주문하기 동작을 수행
                    selectedOptionsList = optionAdapter.getSelectedOptionList();
                    // 옵션 선택 정보와 메뉴 정보를 장바구니에 추가
                    if (selectedOptionsList.size() == optionList.size()) {
                        cartItem = createCartItem(menu, optionList, selectedOptionsList);
                        Log.d(TAG, cartItem.getMenuName() + cartItem.getMenuPrice() + cartItem.getSelectedOptions().toString());

                        // 데이터베이스에 데이터 추가
                        OrderDBOpenHelper dbHelper = new OrderDBOpenHelper(requireContext());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        ContentValues values = new ContentValues();
                        values.put(OrderDBOpenHelper.COLUMN_EMAIL, username);
                        values.put(OrderDBOpenHelper.COLUMN_NICKNAME, nickname);
                        //values.put(OrderDBOpenHelper.COLUMN_STORE_IT, storeId);
                        values.put(OrderDBOpenHelper.COLUMN_MENU_NAME, cartItem.getMenuName());
                        values.put(OrderDBOpenHelper.COLUMN_MENU_OPTION, TextUtils.join(", ", cartItem.getSelectedOptions())); // 옵션을 쉼표로 구분하여 저장
                        values.put(OrderDBOpenHelper.COLUMN_MENU_PRICE, cartItem.getMenuPrice());
                        values.put(OrderDBOpenHelper.COLUMN_MENU_QUANTITY, cartItem.getMenuQuantity()); // 수량 기본값 1로 설정
                        values.put(OrderDBOpenHelper.COLUMN_CART_CREATED_DATE, cartCreatedDate); // 현재 날짜 및 시간

                        long result = db.insert(OrderDBOpenHelper.TABLE_NAME, null, values);
                        db.close();
*/


            // 하트 이미지 클릭 이벤트 처리
            heartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 현재 하트 이미지 상태에 따라 다른 이미지로 변경
                    if (isHeartSelected) {
                        // 이미 선택된 상태인 경우, 선택 해제 (ic_heart_default)
                        heartButton.setImageResource(R.drawable.ic_heart_default);
                        isHeartSelected = false;

                        if (getActivity() instanceof HomeActivity) {
                            HomeActivity activity = (HomeActivity) getActivity();
                            activity.removeHeartMenuItem(menu); // 찜 해제 메서드 호출
                        }
                    } else {
                        // 선택되지 않은 상태인 경우, 선택 (ic_heart_fill)
                        heartButton.setImageResource(R.drawable.ic_heart_fill);
                        isHeartSelected = true;

                        // HeartFragment로 데이터 전달
                        if (getActivity() instanceof HomeActivity) {
                            HomeActivity activity = (HomeActivity) getActivity();
                            activity.addToHeart(menu); // HeartFragment에 데이터 추가 메서드 호출
                        }
                    }
                }
            });

            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quantityInt > 1) {
                        quantityInt--;
                        quantityTextView.setText(String.valueOf(quantityInt));
                        updateOptionTotalPrice();
                    }
                    updateButtonsVisibility();
                }
            });

            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    quantityInt++;
                    if(quantityInt > 1) {
                        updateButtonsVisibility();
                        quantityTextView.setText(String.valueOf(quantityInt));
                        updateOptionTotalPrice();
                    }
                }
            });
        }

        return view;
    }

    private CartDTO createCartItem(MenuDTO menuItem, List<OptionDTO> optionList, List<String> selectedOptionsList) {
        String menuName = menuItem.getName();
        String menuPrice = menuItem.getPrice();

    List<String> optionNameList = new ArrayList<>();
        List<List<String>> optionContentsList = new ArrayList<>();
        int idx = 0;

        for(OptionDTO option : optionList) {
            optionNameList.add(option.getName());
            optionContentsList.add(option.getOptionContents());
        }
        for(String optionContent : selectedOptionsList) {
            for(List<String> optionContents : optionContentsList) { //고정
                if(optionContents.contains(optionContent)) {
                    newSelectedOptionList.add(optionNameList.get(idx) + ": " + optionContent);
                    break;
                }
                idx++;
            }
            idx = 0;
        }

        cartItem = new CartDTO(menuName, menuPrice, quantityInt, newSelectedOptionList);
        //Log.d(TAG, cartItem.getSelectedOptions().toString());
        return cartItem;
    }

    private void showCartConfirmationDialog() {
        // 장바구니에 추가되었다는 모달 다이얼로그 표시
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("장바구니에 추가되었습니다.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 확인 버튼 클릭 시, 원하는 동작 수행
                dismiss();
            }
        });
        builder.show();
    }

    // 총합 가격을 업데이트하는 메서드 추가
    private void updateOptionTotalPrice() {
        int menuPriceInt = Integer.parseInt(menu.getPrice().replace(",", "")); // 가격에서 특수 문자 제거
        int optionTotalPriceInt = menuPriceInt * quantityInt; // 수량과 가격을 곱하여 총합 가격 계산
        // 총합 가격을 숫자 포맷팅을 사용하여 표시
        String formattedOptionTotalPrice = String.format("￦ %,d", optionTotalPriceInt);
        optionTotalPrice.setText(formattedOptionTotalPrice);
    }

    private void updateButtonsVisibility() {
        if (quantityInt > 1) {
            minusButton.setImageResource(R.drawable.ic_minus); // quantity가 2 이상이면 minus 아이콘 변경
            minusButton.setEnabled(true);
        } else {
            minusButton.setImageResource(R.drawable.ic_minus_default); // quantity가 1일 때는 기본 아이콘으로 변경
            minusButton.setEnabled(false);
        }
    }

    private List<OptionDTO> getOptionData() {
        List<OptionDTO> optionList = new ArrayList<>();

        // Option1 객체 생성 (예: 맵기 옵션)
        if (menu.getName().equals("마라탕") || menu.getName().equals("마라샹궈")) {
            List<String> mapLevelOptions = new ArrayList<>();
            mapLevelOptions.add("1단계");
            mapLevelOptions.add("2단계");
            mapLevelOptions.add("3단계");
            mapLevelOptions.add("4단계");
            mapLevelOptions.add("5단계");
            OptionDTO option1 = new OptionDTO("맵기", mapLevelOptions);
            optionList.add(option1);
        }

        // Option2 객체 생성 (예: 밥 양 옵션)
        if (!menu.getName().equals("사이다(500ml)") && !menu.getName().equals("콜라(500ml)")) { //사이다 콜라 제외
            List<String> riceAmountOptions = new ArrayList<>();
            riceAmountOptions.add("적게");
            riceAmountOptions.add("보통");
            riceAmountOptions.add("많이");
            OptionDTO option2 = new OptionDTO("밥 양", riceAmountOptions);
            optionList.add(option2);
        }
/*
        List<String> meatOptions = new ArrayList<>();
        meatOptions.add("추가");
        meatOptions.add("없음");
        OptionDTO option3 = new OptionDTO("고기 추가(+500원)", meatOptions);
*/
        //optionList.add(option3);
        return optionList;
    }
}

