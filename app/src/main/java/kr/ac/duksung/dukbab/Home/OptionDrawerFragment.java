package kr.ac.duksung.dukbab.Home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import kr.ac.duksung.dukbab.R;

public class OptionDrawerFragment extends BottomSheetDialogFragment implements OptionContentAdapter.OnOptionSelectedListener{
    public static final String TAG = "OptionDrawerFragment";

    private MenuDTO menu;
    private ImageView menuImg;
    private TextView menuName, menuPrice;
    private RecyclerView optionView;
    //private ConstraintLayout drawerFooter;
    private List<OptionDTO> selectedOptions = new ArrayList<>();
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private BottomSheetDialog dialog;

    @Override
    public void onOptionSelected(List<String> selectedOptions) {
        // 선택한 옵션 리스트를 이곳에서 사용할 수 있습니다.
        // selectedOptions를 통해 원하는 작업을 수행하세요.
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


        Bundle args = getArguments();
        if (args != null) {
            MenuDTO menu = args.getParcelable("menu");
            // menu 객체를 사용하여 필요한 초기화 작업 수행
            menuImg.setImageResource(menu.getImageResourceId());
            menuName.setText(menu.getName());
            menuPrice.setText("￦ " + menu.getPrice());

            List<OptionDTO> optionList = getOptionData();

            OptionAdapter optionAdapter = new OptionAdapter(optionList);
            optionView.setAdapter(optionAdapter);
            optionView.setLayoutManager(new LinearLayoutManager(getContext()));



            // "담기" 버튼 클릭 이벤트 처리
            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // 옵션 선택 정보와 메뉴 정보를 장바구니에 추가
                    addToCart(menu, selectedOptions);

                    // 모달 다이얼로그 표시
                    showCartConfirmationDialog(menu, selectedOptions);

                    // 슬라이딩 드로어 닫기
                    dismiss();

                    //Toast.makeText(optionAdapter.ge)
                }
            });


        }

        return view;
    }

    private void addToCart(MenuDTO menu, List<OptionDTO> selectedOptions) {
        // 선택한 옵션과 메뉴 정보를 장바구니에 추가하는 로직을 구현
    }

    private void showCartConfirmationDialog(MenuDTO menu, List<OptionDTO> selectedOptions) {
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

        optionList.add(option1);
        optionList.add(option2);

        return optionList;
    }
}
