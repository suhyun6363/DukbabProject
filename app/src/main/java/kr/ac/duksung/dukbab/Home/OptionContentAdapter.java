package kr.ac.duksung.dukbab.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kr.ac.duksung.dukbab.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.ac.duksung.dukbab.R;
import kr.ac.duksung.dukbab.db.Database;

public class OptionContentAdapter extends RecyclerView.Adapter<OptionContentAdapter.ViewHolder> {

    public interface OnOptionSelectedListener {
        void onOptionSelected(String selectedOption, int position);
    }

    private List<String> optionContentList;
    private int selectedPosition = -1; // 초기 선택 위치 (-1은 아무것도 선택되지 않은 상태를 나타냄)
    private String selectedOption = null;

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
            //holder.heart.setImageResource(R.drawable.ic_heart_default); // 선택되지 않은 상태의 하트 이미지 설정
        }

        // 버튼 클릭 이벤트 처리
        if (onOptionSelectedListener != null) {
            final int pos = position;
            holder.optionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // 이전에 선택한 아이템의 UI를 초기화
                    int previousSelectedPosition = selectedPosition;
                    selectedPosition = pos;

                    // 변경된 아이템과 이전 아이템의 UI 업데이트
                    notifyItemChanged(previousSelectedPosition);
                    notifyItemChanged(selectedPosition);

                    selectedOption = optionContentList.get(pos);
                    onOptionSelectedListener.onOptionSelected(selectedOption, pos);
                }
            });
        }

//        holder.heart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 현재 하트 이미지 상태에 따라 다른 이미지로 변경
//                if (position == selectedPosition) {
//                    // 이미 선택된 상태인 경우, 선택 해제 (ic_heart_default)
//                    holder.heart.setImageResource(R.drawable.ic_heart_default);
//                    // 메뉴 찜 상태 업데이트: 찜 취소 (0)
//                    String selectedOption = optionContentList.get(position);
//                    int menuId = Database.getInstance().getMenuIdFromDatabase(selectedOption); // getMenuIdFromDatabase 메서드 호출
//                    if (menuId != -1) {
//                        Database.getInstance().updateMenuHeart(menuId, 0); // 메뉴 찜 취소 (0)
//                    }
//                } else {
//                    // 선택되지 않은 상태인 경우, 선택 (ic_heart_fill)
//                    holder.heart.setImageResource(R.drawable.ic_heart_fill);
//
//                    // 메뉴 찜 상태 업데이트: 찜 추가 (1)
//                    String selectedOption = optionContentList.get(position);
//                    int menuId = Database.getInstance().getMenuIdFromDatabase(selectedOption); // getMenuIdFromDatabase 메서드 호출
//                    if (menuId != -1) {
//                        Database.getInstance().updateMenuHeart(menuId, 1); // 메뉴 찜 추가 (1)
//                    }
//                }
//            }
//        });
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView); // ViewHolder 객체에 itemView 저장
            optionButton = itemView.findViewById(R.id.optionButton);
        }
    }
}
