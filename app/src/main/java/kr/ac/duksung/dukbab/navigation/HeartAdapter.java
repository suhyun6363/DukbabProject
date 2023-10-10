package kr.ac.duksung.dukbab.navigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import kr.ac.duksung.dukbab.Home.MenuDTO;
import kr.ac.duksung.dukbab.Home.OptionDrawerFragment;
import kr.ac.duksung.dukbab.R;

public class HeartAdapter extends RecyclerView.Adapter<HeartAdapter.HeartViewHolder> {

    private List<MenuDTO> heartMenuList;
    private FragmentManager fragmentManager;

    public HeartAdapter(FragmentManager fragmentManager, List<MenuDTO> heartMenuList) {
        this.fragmentManager = fragmentManager;
        this.heartMenuList = heartMenuList;
    }

    @NonNull
    @Override
    public HeartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.heart_item, parent, false);
        return new HeartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeartViewHolder holder, int position) {
        MenuDTO menu = heartMenuList.get(position);

        // 아이템 뷰에 데이터 설정
        holder.menuImage.setImageResource(menu.getImageResourceId());
        holder.menuName.setText(menu.getName());
        holder.menuPrice.setText("￦ " + menu.getPrice());

        holder.menuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuDTO menu = heartMenuList.get(holder.getAdapterPosition());
                OptionDrawerFragment optionDrawerFragment = OptionDrawerFragment.newInstance(menu);
                optionDrawerFragment.show(fragmentManager, OptionDrawerFragment.TAG);
            }
        });

        // 하트 아이콘 클릭 이벤트 처리
        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 하트 아이콘 클릭 시 동작 정의
                // 예: 해당 메뉴를 찜 목록에서 삭제하는 기능 추가
                removeItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return heartMenuList.size();
    }

    public class HeartViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout menuItem;
        public ImageView menuImage;
        public TextView menuName, menuPrice;
        public ImageView heart;

        public HeartViewHolder(@NonNull View itemView) {
            super(itemView);
            menuImage = itemView.findViewById(R.id.menuImage);
            menuName = itemView.findViewById(R.id.menuName);
            menuPrice = itemView.findViewById(R.id.menuPrice);
            menuItem = itemView.findViewById(R.id.menuItem);
            heart = itemView.findViewById(R.id.heart);
        }
    }

    // 아이템 삭제 메서드
    public void removeItem(int position) {
        heartMenuList.remove(position);
        notifyItemRemoved(position);
    }
}
