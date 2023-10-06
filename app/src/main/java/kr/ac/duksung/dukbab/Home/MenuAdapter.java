package kr.ac.duksung.dukbab.Home;

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

import kr.ac.duksung.dukbab.R;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private List<MenuDTO> menuList;
    private MenuAdapterListener menuAdapterListener;

    public interface MenuAdapterListener{
        void openOptionDrawerFragment(MenuDTO menu);
    }

    public void setMenuAdapterListener(MenuAdapterListener menuAdapterListener) {
        this.menuAdapterListener = menuAdapterListener;
    }

    public MenuAdapter(List<MenuDTO> menuList) {
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuDTO menu = menuList.get(position);

        holder.menuName.setText(menu.getName());
        holder.menuPrice.setText("￦ " + menu.getPrice());
        holder.menuImage.setImageResource(menu.getImageResourceId());
        holder.menuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 옵션 선택 슬라이딩 드로어 열기
            if(menuAdapterListener != null)
                menuAdapterListener.openOptionDrawerFragment(menu);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView menuName;
        public TextView menuPrice;
        public ImageView menuImage;
        public RelativeLayout menuItem;

        public ViewHolder(View itemView) {
            super(itemView);
            menuName = itemView.findViewById(R.id.menuName);
            menuPrice = itemView.findViewById(R.id.menuPrice);
            menuImage = itemView.findViewById(R.id.menuImage);
            menuItem = itemView.findViewById(R.id.menuItem);
        }
    }
}

