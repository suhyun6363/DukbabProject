package kr.ac.duksung.dukbab.navigation;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import kr.ac.duksung.dukbab.LoginActivity;
import kr.ac.duksung.dukbab.R;

public class MypageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        // SharedPreferences 초기화
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        // 사용자 메일 정보 가져오기 (예: 이전 화면에서 넘겨받은 정보 또는 SharedPreferences 사용)
        String nickname = sharedPreferences.getString("nickname", ""); // 사용자 이메일

        // TextView 찾기
        TextView welcomeMessageTextView = view.findViewById(R.id.welcomeMessageTextView);
        Button logoutBtn = view.findViewById(R.id.logoutButton);


        // 사용자 메일 정보를 텍스트뷰에 설정
        welcomeMessageTextView.setText(nickname + " 님");
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogoutClick(view); // 다이얼로그 표시
            }
        });

        Button editProfileBtn = view.findViewById(R.id.editProfileButton);
        // MypageFragment.java

    // editProfileBtn 클릭 시 호출되는 리스너 내부
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사용자 이메일 정보 가져오기
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                String userEmail = sharedPreferences.getString("username", ""); // 사용자 이메일

                // ChangePasswordActivity로 이동하면서 사용자 이메일 정보를 전달
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                intent.putExtra("username", userEmail); // 사용자 이메일 정보를 인텐트에 추가
                startActivity(intent);
            }
        });


        return view;
    }

    public void onLogoutClick(View view) {
        // 로그아웃 여부 확인 팝업 띄우기
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 확인을 누르면 로그인 정보 초기화
                        clearLoginInfo();

                        // 로그인 페이지로 이동
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // 기존 스택 비우기
                        startActivity(intent);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 취소를 누르면 아무 동작 없음
                    }
                });

        // 팝업을 표시
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // 로그인 정보 초기화
    private void clearLoginInfo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.remove("password");
        editor.apply();
    }
}

