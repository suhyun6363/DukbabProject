package kr.ac.duksung.dukbab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

public class HaejangSoupFragment extends Fragment {

    public HaejangSoupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_haejagsoup, container, false);

        // 이 부분에 필요한 코드를 추가할 수 있습니다.

        return view;
    }
}