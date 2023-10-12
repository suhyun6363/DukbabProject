package kr.ac.duksung.dukbab.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.bootpay.android.Bootpay;
import kr.co.bootpay.android.BootpayAnalytics;
import kr.co.bootpay.android.events.BootpayEventListener;
import kr.co.bootpay.android.models.BootExtra;
import kr.co.bootpay.android.models.BootItem;
import kr.co.bootpay.android.models.BootUser;
import kr.co.bootpay.android.models.Payload;

import kr.ac.duksung.dukbab.R;

public class OrderActivity extends AppCompatActivity {

    private Intent intent;
    private List<CartDTO> cartList;
    private int totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        totalPrice = intent.getIntExtra("totalPrice", 0);
        // totalPrice를 사용하여 원하는 작업을 수행

        Log.d("OrderActivity", "totalPrice" + totalPrice);
        onClick_request();

        // 초기설정 - 해당 프로젝트(안드로이드)의 application id 값을 설정합니다. 결제와 통계를 위해 꼭 필요합니다.
        BootpayAnalytics.init(this, "65258d8fd25985001fbb8438");
    }

    public void onClick_request() {
        // 결제호출
        BootUser user = new BootUser().setPhone("010-1234-5678");
        BootExtra extra = new BootExtra().setCardQuota("0,2,3");

        List items = new ArrayList<>();
        BootItem item1 = new BootItem().setName("마우스").setId("ITEM_CODE_MOUSE").setQty(1).setPrice(5000d);
        BootItem item2 = new BootItem().setName("키보드").setId("ITEM_KEYBOARD_MOUSE").setQty(1).setPrice(5000d);

        items.add(item1);
        items.add(item2);

        Payload payload = new Payload();
        payload.setApplicationId("65258d8fd25985001fbb8438")
                .setOrderName("덕밥 학식 주문")
                .setPg("이니시스")
                .setMethod("카드")
                .setOrderId("1234")
                .setPrice(Double.valueOf(totalPrice))
                .setUser(user)
                .setExtra(extra);
                //.setItems(items);

        Map map = new HashMap<>();
        map.put("1", "abcdef");
        map.put("2", "abcdef55");
        map.put("3", 1234);
        payload.setMetadata(map);
//        payload.setMetadata(new Gson().toJson(map));

        Bootpay.init(getSupportFragmentManager(), getApplicationContext())
                .setPayload(payload)
                .setEventListener(new BootpayEventListener() {
                    @Override
                    public void onCancel(String data) {
                        Log.d("bootpay", "cancel: " + data);
                    }

                    @Override
                    public void onError(String data) {
                        Log.d("bootpay", "error: " + data);
                    }

                    @Override
                    public void onClose() {
                        Bootpay.removePaymentWindow();
                    }

                    @Override
                    public void onIssued(String data) {
                        Log.d("bootpay", "issued: " +data);
                    }

                    @Override
                    public boolean onConfirm(String data) {
                        Log.d("bootpay", "confirm: " + data);
//                        Bootpay.transactionConfirm(data); //재고가 있어서 결제를 진행하려 할때 true (방법 1)
                        return true; //재고가 있어서 결제를 진행하려 할때 true (방법 2)
//                        return false; //결제를 진행하지 않을때 false
                    }

                    @Override
                    public void onDone(String data) {
                        Log.d("done", data);
                        Intent intent = new Intent(getApplicationContext(), OrderConfirmActivity.class);
                        startActivity(intent);
                    }
                }).requestPayment();

    }
}
