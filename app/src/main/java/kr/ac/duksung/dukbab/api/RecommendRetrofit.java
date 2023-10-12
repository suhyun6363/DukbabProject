package kr.ac.duksung.dukbab.api;

import android.util.Log;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import kr.ac.duksung.dukbab.Home.MenuDTO;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// 집 192.168.219.101
// 학교 172.20.8.37

public class RecommendRetrofit {

    // 서버로 메뉴 아이디 목록을 전송하고 추천 숫자를 받아옴
    public static void sendMenuIds(List<MenuDTO> menuList) {

        // HeartFragment에서 받아온 메뉴 ID 배열을 JSON 형식의 문자열로 변환
        Gson gson = new Gson();
        List<Integer> menuIds = new ArrayList<>();
        for (MenuDTO menu : menuList) {
            menuIds.add(menu.getMenuId());
        }
        String jsonMenuIds = gson.toJson(menuIds);

        // JSON 형식의 데이터를 RequestBody로 변환
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonMenuIds);


        // 집 192.168.219.101
        // 학교 172.20.8.37
        // Retrofit 객체 생성
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.20.26.157:5001/") // 서버의 기본 URL을 설정
                .addConverterFactory(GsonConverterFactory.create()) // Gson 컨버터 사용
                .build();

        // Retrofit 인터페이스 구현체 생성
        PostApiService postApiService = retrofit.create(PostApiService.class);

        // POST 요청 보내기
        Call<RecommendItem> call = postApiService.sendMenuIds(requestBody);
        call.enqueue(new Callback<RecommendItem>() {
            @Override
            public void onResponse(Call<RecommendItem> call, Response<RecommendItem> response) {
                if (response.isSuccessful()) {
                    RecommendItem item = response.body();
                    List<Integer> recommendations = item.getNumbers();

                    // 서버에서 받은 추천 숫자를 가공하여 StringBuilder에 추가
                    StringBuilder result = new StringBuilder("추천 받은 숫자: ");
                    for (int number : recommendations) {
                        result.append(number).append(" ");
                    }

                    // 추천 받은 숫자를 로그에 출력
                    Log.d("Recommendation", result.toString());
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
        });
    }

}
