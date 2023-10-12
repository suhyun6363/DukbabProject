package kr.ac.duksung.dukbab.Home;

import android.util.Log;
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

    private MenuAdapterListener menuAdapterListener;
    private List<MenuDTO> menuList = null;
    private FragmentManager fragmentManager; // FragmentManager를 멤버 변수로 추가

    public interface MenuAdapterListener{
        void openOptionDrawerFragment(MenuDTO menu);
    }

    public void setMenuAdapterListener(MenuAdapterListener menuAdapterListener) {
        this.menuAdapterListener = menuAdapterListener;
    }

    public MenuAdapter(List<MenuDTO> menuList, FragmentManager fragmentManager) {
        this.menuList = menuList;
        this.fragmentManager = fragmentManager; // FragmentManager를 초기화
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
        Log.d("MenuAdapter", "menuName 호출"+menu.getName());
        holder.menuPrice.setText("￦ " + menu.getPrice());
        Log.d("MenuAdapter", "menuName 호출"+menu.getPrice());
        Log.d("MenuAdapter", "menuName 호출"+menu.getImageResourceId());

        holder.menuImage.setImageResource(menu.getImageResourceId());
        Log.d("이미지 호출", "후");
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

