package kr.ac.duksung.myapplication;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DietScheduleActivity extends AppCompatActivity {

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_schedule);

        tableLayout = findViewById(R.id.tableLayout);

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

                    int halfSize = dietItems.size() / 2;
                    for (int i = 0; i < halfSize; i++) {
                        DietItem dietItemA = dietItems.get(i);
                        DietItem dietItemB = dietItems.get(i + halfSize);

                        TableRow row = new TableRow(DietScheduleActivity.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(
                                TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));

                        // 오늘의 메뉴A
                        TextView textViewA = createTextView(dietItemA);
                        row.addView(textViewA);

                        // 오늘의 메뉴B
                        TextView textViewB = createTextView(dietItemB);
                        row.addView(textViewB);

                        tableLayout.addView(row);
                    }
                } else {
                    // 에러 처리
                }
            }

            @Override
            public void onFailure(Call<List<DietItem>> call, Throwable t) {
                // 오류 처리
            }
        });
    }
    private TextView createTextView(DietItem dietItem) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        String text = "날짜: " + dietItem.get날짜() +
                "\n요일: " + dietItem.get요일() +
                "\n식단: " + dietItem.get식단() + "\n";
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        return textView;
    }
}
