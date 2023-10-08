package kr.ac.duksung.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.List;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;





public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DietScheduleActivity로 이동하면서 결과 데이터를 함께 전달하는 코드
                Intent intent = new Intent(MainActivity.this, DietScheduleActivity.class);
                intent.putExtra("result", "여기에 결과 데이터를 넣으세요.");
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // RecommendActivity로 이동하는 코드
                Intent intent = new Intent(MainActivity.this, RecommendActivity.class);
                startActivity(intent);
            }
        });
    }
}
