package kr.ac.duksung.dukbab;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kr.ac.duksung.dukbab.Home.CartDTO;
import kr.ac.duksung.dukbab.R;
import kr.ac.duksung.dukbab.db.CartDBOpenHelper;
import kr.ac.duksung.dukbab.db.OrderDBOpenHelper;

import android.widget.Toast;
import android.content.Intent;


public class TicketFragment extends Fragment {
    private TextView storeNameTextView, foodNameTextView, orderNumberTextView, orderTimeTextView;
    private Button confirmButton;

    // 티켓에 표시할 데이터를 저장할 변수들
    private String storeName, foodName, orderNumber, orderTime;

    // TicketFragment의 생성자
    public TicketFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 전달된 데이터 받아오기
        Bundle bundle = getArguments();
        if (bundle != null) {
            storeName = bundle.getString("storeName");
            foodName = bundle.getString("foodName");
            orderNumber = bundle.getString("orderNumber");
            orderTime = bundle.getString("orderTime");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ticket, container, false);

        // 데이터베이스 헬퍼 클래스 생성
        OrderDBOpenHelper orderDBOpenHelper = new OrderDBOpenHelper(getContext());

        // 데이터를 쿼리하기 위한 쿼리 문자열 (여기서는 모든 레코드를 가져오는 예제입니다)
        String query = "SELECT * FROM " + OrderDBOpenHelper.TABLE_NAME;

        // 최근 주문 내역을 가져옴
        Cursor cursor = orderDBOpenHelper.getLatestOrder();


        // 티켓에 표시할 TextView와 Button 찾기
        storeNameTextView = view.findViewById(R.id.storeNameTextView);
        foodNameTextView = view.findViewById(R.id.foodNameTextView);
        orderNumberTextView = view.findViewById(R.id.orderNumberTextView);
        orderTimeTextView = view.findViewById(R.id.orderTimeTextView);
        confirmButton = view.findViewById(R.id.confirmButton);

        // 커서의 moveToFirst() 메소드를 호출하여 데이터가 있는지 확인
        if (cursor.moveToFirst()) {
            // 데이터베이스에서 데이터를 읽고 TextView에 설정
            storeNameTextView.setText(cursor.getString(cursor.getColumnIndex("storeId")));
            foodNameTextView.setText(cursor.getString(cursor.getColumnIndex("menuName")));
            orderNumberTextView.setText("no." + cursor.getString(cursor.getColumnIndex("orderId")));
            orderTimeTextView.setText(cursor.getString(cursor.getColumnIndex("orderDate")));
        } else {
            // 데이터베이스에 주문 내역이 없을 경우 메시지를 설정
            storeNameTextView.setText("주문내역이 없습니다.");
            foodNameTextView.setText("");
            orderNumberTextView.setText("");
            orderTimeTextView.setText("");
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("음식을 수령하셨습니다");

                // Home 엑티비티로 이동
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);

                // 현재 프래그먼트를 백 스택에서 제거
                if(getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        return view;
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}