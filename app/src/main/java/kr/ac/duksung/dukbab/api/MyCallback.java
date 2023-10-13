package kr.ac.duksung.dukbab.api;

import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import kr.ac.duksung.dukbab.Home.HomeFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCallback implements Callback<RecommendItem> {
    public interface RecommendationListener {
        void onRecommendationsReceived(String jsonRecommendations);
    }

    public String jsonRecommendations;
    private RecommendationListener recommendationListener;

    public MyCallback(RecommendationListener listener) {
        this.recommendationListener = listener;
    }

    @Override
    public void onResponse(Call<RecommendItem> call, Response<RecommendItem> response) {
        if (response.isSuccessful()) {
            RecommendItem item = response.body();
            List<Integer> recommendations = item.getNumbers();

            // List<Integer>를 JSON 문자열로 변환
            Gson gson = new Gson();
            jsonRecommendations = gson.toJson(recommendations);

            // 추천 받은 숫자를 로그에 출력
            Log.d("Recommendation", "추천 받은 숫자(JSON 형식): " + jsonRecommendations);

            // 추천 받은 숫자를 인터페이스를 통해 전달
            recommendationListener.onRecommendationsReceived(jsonRecommendations);

        } else {
            // 서버 응답이 실패한 경우
            Log.d("Recommendation", "Failed to get data");
        }
    }

    @Override
    public void onFailure(Call<RecommendItem> call, Throwable t) {
        // 네트워크 요청 자체가 실패한 경우
        t.printStackTrace();
        Log.d("Recommendation", "Failed to connect to the server: " + t.getMessage());
    }
}


