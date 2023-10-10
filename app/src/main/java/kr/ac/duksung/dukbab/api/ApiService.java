package kr.ac.duksung.dukbab.api;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

//오메용
public interface ApiService {
    @GET("get_diet_schedule")
    Call<List<DietItem>> getDietSchedule();
}
