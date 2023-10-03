package kr.ac.duksung.dukbab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class OptionDrawerFragment extends BottomSheetDialogFragment {

    private MenuDTO menu;

    public static OptionDrawerFragment newInstance(MenuDTO menu) {
        OptionDrawerFragment fragment = new OptionDrawerFragment();
        fragment.menu = menu;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_optiondrawer, container, false);

        TextView menuName = view.findViewById(R.id.menuName);
        TextView menuPrice = view.findViewById(R.id.menuPrice);
        //TextView detailContent = view.findViewById(R.id.detailContent);

        menuName.setText(menu.getName());
        menuPrice.setText(menu.getPrice());

        ArrayList<>

        // "담기" 버튼 클릭 이벤트 처리
        Button btnAddToCart = view.findViewById(R.id.cart_btn);
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 옵션 선택 정보와 메뉴 정보를 장바구니에 추가
                addToCart(menu, selectedOptions);

                // 모달 다이얼로그 표시
                showCartConfirmationDialog(menu, selectedOptions, totalPrice);

                // 슬라이딩 드로어 닫기
                dismiss();
            }
        });

        return view;
    }

    private void addToCart(MenuDTO menu, List<OptionDTO> selectedOptions) {
        // 선택한 옵션과 메뉴 정보를 장바구니에 추가하는 로직을 구현
    }

    private void showCartConfirmationDialog(MenuDTO menu, List<OptionDTO> selectedOptions, int totalPrice) {
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
}
