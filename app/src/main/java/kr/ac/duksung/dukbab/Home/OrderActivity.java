package kr.ac.duksung.dukbab.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import kr.ac.duksung.dukbab.R;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // 주문 정보를 받아옴
        ArrayList<CartDTO> cartList = getIntent().getParcelableArrayListExtra("cartList");

        if(cartList != null)
            Log.d("OrderActivity", "Send!!");
/*
        // cartList를 화면에 표시하거나 처리함
        // 예를 들어, 주문 목록을 RecyclerView로 표시하려면 Adapter를 사용하여 화면에 표시하세요.
        RecyclerView orderRecyclerView = findViewById(R.id.orderRecyclerView);
        OrderAdapter orderAdapter = new OrderAdapter(cartList);
        orderRecyclerView.setAdapter(orderAdapter);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
*/

    }
}
