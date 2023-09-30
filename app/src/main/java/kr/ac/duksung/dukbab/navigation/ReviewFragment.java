package kr.ac.duksung.dukbab.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kr.ac.duksung.dukbab.R;
import kr.ac.duksung.dukbab.navigation.ReviewwriteActivity;

public class ReviewFragment extends Fragment {

    public ReviewFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_review, container, false);

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
