package kr.ac.duksung.dukbab.Home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.ac.duksung.dukbab.GridSpaceItemDecoration;
import kr.ac.duksung.dukbab.Home.OptionDTO;
import kr.ac.duksung.dukbab.R;

public class OptionAdapter extends RecyclerView.Adapter<ViewHolder> implements OptionContentAdapter.OnOptionSelectedListener {

    private static final String TAG = "OptionAdapter";
    private List<OptionDTO> optionList;
    private List<String> selectedOptionList = new ArrayList<>();
    private List<String> optionContents;
    private boolean optionFound = false;
    private OptionContentAdapter optionContentAdapter;


    public void onOptionSelected(String selectedOption, int position) {
        //Log.d(TAG, "optioncontentposition" + position);
        for(OptionDTO option : optionList) {
            if(option.getOptionContents().contains(selectedOption)) {
                optionContents = option.getOptionContents();
                break;
            }
        }
        for (String sO : selectedOptionList) {
            if (optionContents.contains(sO)) {
                selectedOptionList.remove(sO);
                selectedOptionList.add(selectedOption);
                optionFound = true;
                break;
            }
        }

        if (!optionFound) {
            selectedOptionList.add(selectedOption);
        }
        optionFound = false;
    }

    public OptionAdapter(List<OptionDTO> optionList) {
        this.optionList = optionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.option_item, parent, false);
        return new OptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OptionDTO option = optionList.get(position);
        OptionViewHolder optionViewHolder = (OptionViewHolder) holder; //optionViewHolder를 통해 옵션1, 2.. 항목과

        optionViewHolder.optionName.setText(option.getName());

        // 세부 옵션 목록을 RecyclerView에 설정
        optionContentAdapter = new OptionContentAdapter(option.getOptionContents());
        optionContentAdapter.setOnOptionSelectedListener(this);
        GridLayoutManager layoutManager = new GridLayoutManager(optionViewHolder.optionContentView.getContext(), 3); // 열 수
        optionViewHolder.optionContentView.setLayoutManager(layoutManager);
        optionViewHolder.optionContentView.addItemDecoration(new GridSpaceItemDecoration(3, 28));
        optionViewHolder.optionContentView.setAdapter(optionContentAdapter);
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

/*
    public List<String> getSelectedOptionList() {
        List<String> selectedOptionList = new ArrayList<>();
        for (OptionDTO option : getSelectedOptionContents) {
            selectedOptionStrings.add(option.getName() + ": " + option.getSelectedOption());
        }
    }
*/
    public List<String> getSelectedOptionList() {
        return selectedOptionList;
    }

    // ViewHolder 클래스
    public static class OptionViewHolder extends ViewHolder {
        public TextView optionName;
        public RecyclerView optionContentView;

        public OptionViewHolder(@NonNull View itemView) {
            super(itemView);
            optionName = itemView.findViewById(R.id.optionName);
            optionContentView = itemView.findViewById(R.id.optionContentView);
        }
    }
}

