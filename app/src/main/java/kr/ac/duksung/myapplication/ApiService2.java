package kr.ac.duksung.myapplication;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiService2 {
    @GET("filtering")
    Call<RecommendItem> getRecommendations();
}

