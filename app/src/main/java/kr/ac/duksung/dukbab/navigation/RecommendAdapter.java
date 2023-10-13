package kr.ac.duksung.dukbab.navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kr.ac.duksung.dukbab.Home.MenuDTO;
import kr.ac.duksung.dukbab.R;

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {

    private List<MenuDTO> recommendList;

    public RecommendAdapter(List<MenuDTO> recommendList) {
        this.recommendList = recommendList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MenuDTO recommendMenuItem = recommendList.get(position);

        holder.menuName.setText(recommendMenuItem.getName());
        holder.menuPrice.setText("￦ " + recommendMenuItem.getPrice());
        holder.menuImage.setImageResource(recommendMenuItem.getImageResourceId());

    }

    @Override
    public int getItemCount() {
        return recommendList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView menuName;
        public TextView menuPrice;
        public ImageView menuImage;
        public RelativeLayout menuItem;

        // ViewHolder 내의 View 요소를 선언
        public ViewHolder(View itemView) {
            super(itemView);
            menuName = itemView.findViewById(R.id.menuName);
            menuPrice = itemView.findViewById(R.id.menuPrice);
            menuImage = itemView.findViewById(R.id.menuImage);
            menuItem = itemView.findViewById(R.id.menuItem);
            // View 요소를 초기화
        }
    }
}

