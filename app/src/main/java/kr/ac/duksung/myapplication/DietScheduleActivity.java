package kr.ac.duksung.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.List;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DietScheduleActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_schedule);

        textView = findViewById(R.id.textView2);

        //http://192.168.219.101:5000/get_diet_schedule

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.219.101:5000/") // Flask API의 기본 URL을 입력하세요
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<DietItem>> call = apiService.getDietSchedule();
        call.enqueue(new Callback<List<DietItem>>() {
            @Override
            public void onResponse(Call<List<DietItem>> call, Response<List<DietItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DietItem> dietItems = response.body();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (DietItem dietItem : dietItems) {
                        stringBuilder.append("날짜: ").append(dietItem.get날짜()).append("\n");
                        stringBuilder.append("요일: ").append(dietItem.get요일()).append("\n");
                        stringBuilder.append("식단: ").append(dietItem.get식단()).append("\n\n");
                    }
                    textView.setText(stringBuilder.toString());
                } else {
                    textView.setText("데이터를 가져오지 못했습니다.");
                }
            }

            @Override
            public void onFailure(Call<List<DietItem>> call, Throwable t) {
                textView.setText("API 호출 중 오류 발생: " + t.getMessage());
            }
        });
    }
}