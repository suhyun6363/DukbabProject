package kr.ac.duksung.dukbab;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class ReviewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button writeReviewButton = findViewById(R.id.write_review_button);
        writeReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "리뷰 작성" 버튼을 클릭할 때 ReviewWriteActivity를 모달 다이얼로그 형태로 띄웁니다.
                Intent intent = new Intent(ReviewActivity.this, ReviewwriteActivity.class);
                startActivity(intent);
            }
        });
    }


}

