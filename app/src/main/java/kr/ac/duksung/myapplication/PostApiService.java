package kr.ac.duksung.myapplication;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PostApiService {
    @POST("filtering")
    Call<RecommendItem> sendNumbers(@Body RequestBody requestBody);
}
