package kr.ac.duksung.dukbab.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.ac.duksung.dukbab.R;

public class OptionContentAdapter extends RecyclerView.Adapter<OptionContentAdapter.ViewHolder> {

    public interface OnOptionSelectedListener {
        void onOptionSelected(List<String> selectedOptions);
    }

    private List<String> optionContentList;
    private List<String> selectedOptionContents = new ArrayList<>();; // 선택한 옵션 내용을 저장할 리스트
    private int selectedPosition = -1; // 초기 선택 위치 (-1은 아무것도 선택되지 않은 상태를 나타냄)
    private OnOptionSelectedListener onOptionSelectedListener;

    public OptionContentAdapter(List<String> optionContentList) {
        this.optionContentList = optionContentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.optioncontent_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String optionContent = optionContentList.get(position);
        holder.optionButton.setText(optionContent);

        // 버튼의 선택 상태를 확인하고 UI를 업데이트
        if (position == selectedPosition) {
            holder.optionButton.setBackgroundResource(R.drawable.btn_selected); // 선택한 버튼의 배경 설정
            holder.optionButton.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black)); // 선택한 버튼의 텍스트 색상 설정
            //holder.heart.setImageResource(R.drawable.ic_heart_fill); // 선택한 상태의 하트 이미지 설정
        } else {
            holder.optionButton.setBackgroundResource(R.drawable.btn_default); // 선택되지 않은 버튼의 배경 설정
            holder.optionButton.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.gray_666)); // 선택되지 않은 버튼의 텍스트 색상 설정
            //holder.heart.setImageResource(R.drawable.ic_heart_notfill); // 선택되지 않은 상태의 하트 이미지 설정
        }

        // 버튼 클릭 이벤트 처리
        holder.optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 선택한 버튼의 위치를 업데이트하고 RecyclerView를 갱신하여 UI를 변경
                selectedPosition = position;
                notifyDataSetChanged();

                // 선택한 옵션을 가져와서 처리
                String selectedOption = optionContentList.get(selectedPosition);
                if (selectedOptionContents.contains(selectedOption)) {
                    // 이미 선택된 옵션인 경우, 제거
                    selectedOptionContents.remove(selectedOption);
                } else {
                    // 선택되지 않은 옵션인 경우, 추가
                    selectedOptionContents.add(selectedOption);
                }

                // 선택한 옵션 리스트를 콜백을 통해 전달
                if (onOptionSelectedListener != null) {
                    onOptionSelectedListener.onOptionSelected(selectedOptionContents);
                }

                // String msg = String.valueOf(selectedOptionContents);
                //Toast.makeText(v.getContext(), msg, Toast.LENGTH_SHORT);
            }
        });
/*
        // 하트 이미지 클릭 이벤트 처리
        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 하트 이미지 상태에 따라 다른 이미지로 변경
                if (position == selectedPosition) {
                    // 이미 선택된 상태인 경우, 선택 해제 (ic_heart_default)
                    holder.heart.setImageResource(R.drawable.ic_heart_default);
                } else {
                    // 선택되지 않은 상태인 경우, 선택 (ic_heart_fill)
                    holder.heart.setImageResource(R.drawable.ic_heart_fill);
                }
            }
        });
 */
    }

    @Override
    public int getItemCount() {
        return optionContentList.size();
    }

    public void setOnOptionSelectedListener(OnOptionSelectedListener listener) {
        this.onOptionSelectedListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button optionButton;
        //ImageView heart; // ImageView 추가

        public ViewHolder(@NonNull View itemView) {
            super(itemView); // ViewHolder 객체에 itemView 저장
            optionButton = itemView.findViewById(R.id.optionButton);
            //heart = itemView.findViewById(R.id.heart);
        }
    }
}
