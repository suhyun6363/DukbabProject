package kr.ac.duksung.dukbab.api;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

import kr.ac.duksung.dukbab.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.util.Log;

// 집 192.168.219.101
// 학교 172.20.8.37
public class TodayMenuFragment extends Fragment {

    private TableLayout tableLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_menu, container, false);

        tableLayout = view.findViewById(R.id.tableLayout);

        // 집 192.168.219.101
        // 학교 172.20.8.37

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.20.8.37:5000/") // Flask API의 기본 URL을 입력하세요
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

                        TableRow row = new TableRow(requireContext());
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
                Log.e("API Call", "Error: " + t.getMessage());
            }

        });

        return view;
    }

    private TextView createTextView(DietItem dietItem) {
        TextView textView = new TextView(requireContext());
        textView.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        String text =  dietItem.get날짜() +
                " " + dietItem.get요일() +
                "\n식단: " + dietItem.get식단() + "\n";
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        return textView;
    }
}
