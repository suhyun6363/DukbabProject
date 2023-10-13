package kr.ac.duksung.dukbab.navigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import kr.ac.duksung.dukbab.Home.MenuDTO;
import kr.ac.duksung.dukbab.R;
import kr.ac.duksung.dukbab.db.Database;
import kr.ac.duksung.dukbab.db.MenuDBOpenHelper;

public class RecommendActivity extends AppCompatActivity {

    private RecyclerView recommendRecyclerView;
    private RecommendAdapter recommendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        recommendRecyclerView = findViewById(R.id.recommendRecyclerView);
        recommendRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            Intent intent = getIntent();
            if (intent != null) {
                List<Integer> recommendationIdList = intent.getIntegerArrayListExtra("recomMenuIdList");
                if (recommendationIdList != null) {
                    Log.d("RecommendActivity12132", recommendationIdList.toString());
                    Toast.makeText(this, recommendationIdList.toString(), Toast.LENGTH_SHORT).show();
                    List<MenuDTO> recommendMenuList = getRecommendData(recommendationIdList);
                    Log.d("RecommendActivity", recommendMenuList.toString());
                    recommendAdapter = new RecommendAdapter(recommendMenuList);
                    recommendRecyclerView.setAdapter(recommendAdapter);
                    //recommendRecyclerView.notify();
                }
            }
    }

    private List<MenuDTO> getRecommendData(List<Integer> recommendationIdList) {
        List<MenuDTO> recommendMenuList = new ArrayList<>();

        // 데이터베이스에서 추천된 메뉴 ID에 해당하는 메뉴를 검색하고 recommendMenuList에 추가함
        Database database = Database.getInstance();
        database.openMenuDB(getApplicationContext());

        for (int recommendMenuId : recommendationIdList) {
            Cursor cursor = database.searchMenuByMenuId(recommendMenuId);
            if (cursor != null) {
                try {
                    while (cursor.moveToNext()) {
                        int storeIdIndex = cursor.getColumnIndex(MenuDBOpenHelper.COLUMN_STORE_ID);
                        int menuNameIndex = cursor.getColumnIndex(MenuDBOpenHelper.COLUMN_MENU_NAME);
                        int priceIndex = cursor.getColumnIndex(MenuDBOpenHelper.COLUMN_MENU_PRICE);
                        int imgIndex = cursor.getColumnIndex(MenuDBOpenHelper.COLUMN_MENU_IMG);

                        int storeId = cursor.getInt(storeIdIndex);
                        String menuName = cursor.getString(menuNameIndex);
                        String price = cursor.getString(priceIndex);
                        String img = cursor.getString(imgIndex);

                        // 검색된 메뉴를 MenuDTO 객체로 생성하여 메뉴 목록에 추가
                        recommendMenuList.add(new MenuDTO(storeId, menuName, price, Integer.parseInt(img)));
                    }
                } finally {
                    cursor.close();
                }
            }
        }
        database.closeMenuDB();
        return recommendMenuList;
    }
}
