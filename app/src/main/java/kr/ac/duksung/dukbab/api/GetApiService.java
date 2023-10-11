package kr.ac.duksung.dukbab.api;


import retrofit2.Call;
import retrofit2.http.GET;

// 추천용
public interface GetApiService {
    @GET("filtering")
    Call<RecommendItem> getRecommendations();

}

