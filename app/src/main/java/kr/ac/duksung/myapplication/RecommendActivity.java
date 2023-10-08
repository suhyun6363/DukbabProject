package kr.ac.duksung.myapplication;
// RecommendActivity.java
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        textView = findViewById(R.id.textView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.219.101:5001/") // API 엔드포인트의 기본 URL을 설정합니다.
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService2 apiService = retrofit.create(ApiService2.class);

        Call<RecommendItem> call = apiService.getRecommendations();
        call.enqueue(new Callback<RecommendItem>() {
            @Override
            public void onResponse(Call<RecommendItem> call, Response<RecommendItem> response) {
                if (response.isSuccessful()) {
                    RecommendItem recommendItem = response.body();
                    if (recommendItem != null) {
                        StringBuilder stringBuilder = new StringBuilder();

                        RecommendItem.User user1 = recommendItem.getUser1();
                        RecommendItem.User user2 = recommendItem.getUser2();
                        RecommendItem.User user3 = recommendItem.getUser3();

                        // User 1
                        stringBuilder.append("User 1: ").append(user1.getUsername()).append("\n");
                        stringBuilder.append("Numbers: ").append(user1.getNumbers()).append("\n\n");

                        // User 2
                        stringBuilder.append("User 2: ").append(user2.getUsername()).append("\n");
                        stringBuilder.append("Numbers: ").append(user2.getNumbers()).append("\n\n");

                        // User 3
                        stringBuilder.append("User 3: ").append(user3.getUsername()).append("\n");
                        stringBuilder.append("Numbers: ").append(user3.getNumbers()).append("\n\n");

                        textView.setText(stringBuilder.toString());
                    }
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<RecommendItem> call, Throwable t) {
                // Handle failure
            }
        });
    }
}
