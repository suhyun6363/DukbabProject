package kr.ac.duksung.myapplication;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

//오메용
public interface ApiService {

    //http://192.168.219.101:5000/get_diet_schedule
    @GET("get_diet_schedule")
    Call<List<DietItem>> getDietSchedule();
}
