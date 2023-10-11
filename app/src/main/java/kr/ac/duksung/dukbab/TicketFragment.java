package kr.ac.duksung.dukbab;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import kr.ac.duksung.dukbab.R;
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

        // 티켓에 표시할 TextView와 Button 찾기
        storeNameTextView = view.findViewById(R.id.storeNameTextView);
        foodNameTextView = view.findViewById(R.id.foodNameTextView);
        orderNumberTextView = view.findViewById(R.id.orderNumberTextView);
        orderTimeTextView = view.findViewById(R.id.orderTimeTextView);
        confirmButton = view.findViewById(R.id.confirmButton);

        // TextView에 데이터 설정
        storeNameTextView.setText(storeName);
        foodNameTextView.setText(foodName);
        orderNumberTextView.setText("no." + orderNumber);
        orderTimeTextView.setText(orderTime);

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