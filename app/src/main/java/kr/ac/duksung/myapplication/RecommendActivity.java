package kr.ac.duksung.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecommendActivity extends AppCompatActivity {

    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;
    private Button recommendButton;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);
        recommendButton = findViewById(R.id.recommendButton);
        textView = findViewById(R.id.textView);

        recommendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자가 입력한 다섯 개의 숫자를 가져옵니다.
                int number1 = Integer.parseInt(editText1.getText().toString());
                int number2 = Integer.parseInt(editText2.getText().toString());
                int number3 = Integer.parseInt(editText3.getText().toString());
                int number4 = Integer.parseInt(editText4.getText().toString());
                int number5 = Integer.parseInt(editText5.getText().toString());


                // 숫자 다섯 개를 JSON 형식으로 변환합니다.
                String jsonNumbers = "[" + number1 + ", " + number2 + ", " + number3 + ", " + number4 + ", " + number5 + "]";

                // JSON 형식의 데이터를 RequestBody로 변환합니다.
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonNumbers);

                // Retrofit 객체 생성
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.219.101:5001/") // API 엔드포인트의 기본 URL을 설정합니다.
                        .addConverterFactory(GsonConverterFactory.create()) // JSON 데이터를 자바 객체로 변환하기 위해 Gson 컨버터를 사용합니다.
                        .build();

                // Retrofit 인터페이스 구현체 생성
                PostApiService postApiService = retrofit.create(PostApiService.class);

                // POST 요청 보내기
                Call<RecommendItem> call = postApiService.sendNumbers(requestBody);
                call.enqueue(new Callback<RecommendItem>() {
                    @Override
                    public void onResponse(Call<RecommendItem> call, Response<RecommendItem> response) {
                        if (response.isSuccessful()) {
                            RecommendItem item = response.body();
                            List<Integer> recommendations = item.getNumbers();

                            // 서버에서 받은 추천 숫자를 가공하여 StringBuilder에 추가
                            StringBuilder result = new StringBuilder("추천 받은 숫자: ");
                            for (int number : recommendations) {
                                result.append(number).append(" ");
                            }

                            // TextView에 결과 표시
                            textView.setText(result.toString());
                        } else {
                            // 서버 응답이 실패한 경우
                            textView.setText("Failed to get data");
                        }
                    }

                    @Override
                    public void onFailure(Call<RecommendItem> call, Throwable t) {
                        // 네트워크 요청 자체가 실패한 경우
                        t.printStackTrace();
                        textView.setText("Failed to connect to the server: " + t.getMessage());
                    }
                });
            }
        });
    }
}
