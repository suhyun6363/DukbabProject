package kr.ac.duksung.dukbab.navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kr.ac.duksung.dukbab.R;
import kr.ac.duksung.dukbab.db.Database;
import kr.ac.duksung.dukbab.navigation.ReviewwriteActivity;

public class ReviewFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReviewAdapter adapter;
    private SharedPreferences sharedPreferences;

    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Review.db 데이터베이스 열기
        Database database = Database.getInstance();
        database.openReviewDB(getContext());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_review, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        // RecyclerView 레이아웃 매니저 설정 (LinearLayoutManager 사용)
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 데이터베이스에서 리뷰 데이터를 쿼리하고 커서를 얻습니다.
        Database database = Database.getInstance();
        database.openReviewDB(requireContext());
        Cursor cursor = database.searchReview(null); // 'email'을 사용자의 이메일로 대체합니다.

        // RecyclerView 어댑터 생성 및 설정
        adapter = new ReviewAdapter(cursor);
        recyclerView.setAdapter(adapter);

        Button writeReviewButton = view.findViewById(R.id.write_review_button);
        writeReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "리뷰 작성" 버튼을 클릭할 때 ReviewWriteActivity를 모달 다이얼로그 형태로 띄웁니다.
                Intent intent = new Intent(getActivity(), ReviewwriteActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
