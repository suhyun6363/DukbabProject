package kr.ac.duksung.dukbab.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.List;

import kr.ac.duksung.dukbab.GridSpaceItemDecoration;
import kr.ac.duksung.dukbab.R;

public class OptionAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<OptionDTO> optionList;
    private OptionContentAdapter optionContentAdapter;

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

