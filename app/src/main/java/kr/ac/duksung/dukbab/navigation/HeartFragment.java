package kr.ac.duksung.dukbab.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import kr.ac.duksung.dukbab.Home.MenuDTO;
import kr.ac.duksung.dukbab.R;
import kr.ac.duksung.dukbab.api.MyCallback;
import kr.ac.duksung.dukbab.api.PostApiService;
import kr.ac.duksung.dukbab.api.RecommendItem;
import kr.ac.duksung.dukbab.api.RecommendRetrofit;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HeartFragment extends Fragment {

    private List<MenuDTO> heartMenuList = new ArrayList<>();
    private HeartAdapter heartAdapter;
    private RecyclerView heartView;
    private FragmentManager context;
    private int heartFlag = 1;
    private List<Integer> recomMenuList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getParentFragmentManager();
        View view = inflater.inflate(R.layout.fragment_heart, container, false);

        heartView = view.findViewById(R.id.heartView);
        Button recommendButton = view.findViewById(R.id.recommendButton);

        heartAdapter = new HeartAdapter(context, heartMenuList, heartFlag);
        heartView.setAdapter(heartAdapter);
        heartView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // recommendButton 클릭 시 이벤트 처리
        recommendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRecommendations();
            }
        });

        // Log the menu IDs in JSON format
        logMenuIdsInJsonFormat();

        return view;
    }

    // HeartFragment에 메뉴를 추가하는 메서드
    public void addMenuToHeart(MenuDTO menu) {
        heartMenuList.add(menu);
        heartAdapter.notifyDataSetChanged();
        // Log the menu IDs in JSON format after adding a menu
        logMenuIdsInJsonFormat();
    }

    // HeartFragment에서 메뉴를 삭제하는 메서드
    public void removeMenuFromHeart(MenuDTO menu) {
        heartMenuList.remove(menu);
        heartAdapter.notifyDataSetChanged();
        // Log the menu IDs in JSON format after removing a menu
        logMenuIdsInJsonFormat();
    }

    private void logMenuIdsInJsonFormat() {
        List<Integer> menuIds = new ArrayList<>();
        for (MenuDTO menu : heartMenuList) {
            menuIds.add(menu.getMenuId());
        }
        Gson gson = new Gson();
        String jsonMenuIds = gson.toJson(menuIds);
        Log.d("HeartFragment", "Menu IDs in JSON format: " + jsonMenuIds);
    }

    private void requestRecommendations() {
        Gson gson = new Gson();
        List<Integer> menuIds = new ArrayList<>();
        for (MenuDTO menu : heartMenuList) {
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

        Call<RecommendItem> call = postApiService.sendMenuIds(requestBody);
        call.enqueue(new Callback<RecommendItem>() {
            @Override
            public void onResponse(Call<RecommendItem> call, Response<RecommendItem> response) {
                if (response.isSuccessful()) {
                    RecommendItem item = response.body();
                    List<Integer> recommendations = item.getNumbers();

                    // 추천 데이터를 처리
                    handleRecommendations(recommendations);

                    // 모달창 띄우기
                    showRecommendModal(recommendations);
                } else {
                    // 서버 응답이 실패한 경우
                    Log.d("Recommendation", "데이터 가져오기에 실패했습니다");
                }
            }

            @Override
            public void onFailure(Call<RecommendItem> call, Throwable t) {
                // 네트워크 요청 자체가 실패한 경우
                t.printStackTrace();
                Log.d("Recommendation", "서버 연결에 실패했습니다: " + t.getMessage());
            }
        });
    }

    // 모달창을 띄우는 메서드
    private void showRecommendModal(List<Integer> recommendations) {
        // 추천 데이터를 가지고 RecommendActivity를 모달창으로 띄웁니다.
        Intent intent = new Intent(getContext(), RecommendActivity.class);
        ArrayList<Integer> recomMenuIdList = new ArrayList<>(recommendations);
        intent.putIntegerArrayListExtra("recomMenuIdList", recomMenuIdList); // 추천 데이터를 전달
        startActivity(intent);
        Log.d("HeartFragment", recomMenuIdList.toString());
    }

    private void handleRecommendations(List<Integer> recommendations) {
        // List<Integer>를 JSON 문자열로 변환
        Gson gson = new Gson();
        String jsonRecommendations = gson.toJson(recommendations);
    }

}