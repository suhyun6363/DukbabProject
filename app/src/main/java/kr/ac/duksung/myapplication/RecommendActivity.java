package kr.ac.duksung.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.List;

public class RecommendActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        textView = findViewById(R.id.textView);

        // Retrofit 객체 생성
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.219.101:5001/") // API 엔드포인트의 기본 URL을 설정합니다.
                .addConverterFactory(GsonConverterFactory.create()) // JSON 데이터를 자바 객체로 변환하기 위해 Gson 컨버터를 사용합니다.
                .build();

        // Retrofit 인터페이스 구현체 생성
        ApiService2 apiService = retrofit.create(ApiService2.class);

        // API 호출
        Call<RecommendItem> call = apiService.getRecommendations();
        call.enqueue(new Callback<RecommendItem>() {
            @Override
            public void onResponse(Call<RecommendItem> call, Response<RecommendItem> response) {
                if (response.isSuccessful()) {
                    RecommendItem item = response.body();
                    String username = item.getUsername();
                    List<Integer> numbers = item.getNumbers();

                    // 서버에서 받은 데이터를 가공하여 StringBuilder에 추가
                    StringBuilder result = new StringBuilder();
                    result.append("Username: ").append(username).append("\n");
                    result.append("Numbers: ").append(numbers.toString()).append("\n\n");

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
}
