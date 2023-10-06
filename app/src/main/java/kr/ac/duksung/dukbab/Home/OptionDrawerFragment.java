package kr.ac.duksung.dukbab.Home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
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

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import kr.ac.duksung.dukbab.R;

public class OptionDrawerFragment extends BottomSheetDialogFragment {
    public static final String TAG = "OptionDrawerFragment";

    private MenuDTO menu;
    private ImageView menuImg;
    private TextView menuName, menuPrice;
    private RecyclerView optionView;
    private List<String> selectedOptionsList = new ArrayList<>();
    private List<String> newSelectedOptionList = new ArrayList<>();
    private boolean isHeartSelected = false;
    private CartDTO cartItem;



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
        ImageView heart = view.findViewById(R.id.heart);


        Bundle args = getArguments();
        if (args != null) {
            menu = args.getParcelable("menu");
            // menu 객체를 사용하여 필요한 초기화 작업 수행
            menuImg.setImageResource(menu.getImageResourceId());
            menuName.setText(menu.getName());
            menuPrice.setText("￦ " + menu.getPrice());

            List<OptionDTO> optionList = getOptionData();

            OptionAdapter optionAdapter = new OptionAdapter(optionList);
            //OptionContentAdapter optionContentAdapter = new OptionContentAdapter(optionList.)
            optionView.setAdapter(optionAdapter);
            optionView.setLayoutManager(new LinearLayoutManager(getContext()));


            // "담기" 버튼 클릭 이벤트 처리
            // CartDTO 이용
            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedOptionsList = optionAdapter.getSelectedOptionList();
                    // 옵션 선택 정보와 메뉴 정보를 장바구니에 추가
                    cartItem = createCartItem(menu, optionList, selectedOptionsList);
                    Log.d(TAG, cartItem.getMenuName() + cartItem.getMenuPrice() + cartItem.getSelectedOptions().toString());

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
                }
            });

            // 하트 이미지 클릭 이벤트 처리
            heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 현재 하트 이미지 상태에 따라 다른 이미지로 변경
                    if (isHeartSelected) {
                        // 이미 선택된 상태인 경우, 선택 해제 (ic_heart_default)
                        heart.setImageResource(R.drawable.ic_heart_default);
                        isHeartSelected = false;
                    } else {
                        // 선택되지 않은 상태인 경우, 선택 (ic_heart_fill)
                        heart.setImageResource(R.drawable.ic_heart_fill);
                        isHeartSelected = true;
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
        cartItem = new CartDTO(menuName, menuPrice, newSelectedOptionList);
        Log.d(TAG, cartItem.getSelectedOptions().toString());
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

    private List<OptionDTO> getOptionData() {
        List<OptionDTO> optionList = new ArrayList<>();

        // Option1 객체 생성 (예: 맵기 옵션)
        List<String> mapLevelOptions = new ArrayList<>();
        mapLevelOptions.add("1단계");
        mapLevelOptions.add("2단계");
        mapLevelOptions.add("3단계");
        mapLevelOptions.add("4단계");
        mapLevelOptions.add("5단계");
        OptionDTO option1 = new OptionDTO("맵기", mapLevelOptions);

        // Option2 객체 생성 (예: 밥 양 옵션)
        List<String> riceAmountOptions = new ArrayList<>();
        riceAmountOptions.add("적게");
        riceAmountOptions.add("보통");
        riceAmountOptions.add("많이");
        OptionDTO option2 = new OptionDTO("밥 양", riceAmountOptions);
/*
        List<String> meatOptions = new ArrayList<>();
        meatOptions.add("추가");
        meatOptions.add("없음");
        OptionDTO option3 = new OptionDTO("고기 추가(+500원)", meatOptions);
*/
        optionList.add(option1);
        optionList.add(option2);
        //optionList.add(option3);

        return optionList;
    }
}
