package kr.ac.duksung.dukbab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.List;

import kr.ac.duksung.dukbab.Home.OptionDTO;

public class OptionAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<OptionDTO> optionList;

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

        // 서브 RecyclerView 설정
        SubOptionAdapter subOptionAdapter = new SubOptionAdapter(option.getSubOptions());
        optionViewHolder.subOptionRecyclerView.setAdapter(subOptionAdapter);
        optionViewHolder.subOptionRecyclerView.setLayoutManager(new LinearLayoutManager(optionViewHolder.itemView.getContext()));
    }

    @Override
    public int getItemCount() {
        return optionList.size();
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

