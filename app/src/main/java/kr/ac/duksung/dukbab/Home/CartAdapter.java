package kr.ac.duksung.dukbab.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.ac.duksung.dukbab.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartDTO> cartList;

    public CartAdapter(List<CartDTO> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        // 해당 위치(position)의 cartItem을 가져옵니다.
        CartDTO cartItem = cartList.get(position);

        // cartItem의 정보를 ViewHolder에 설정합니다.
        holder.menuName.setText(cartItem.getMenuName());
        holder.menuPrice.setText(cartItem.getMenuPrice());
        holder.optionList.setText(cartItem.getSelectedOptions().toString());

/*
        // 필요에 따라 아이템 삭제 기능을 추가하세요.
        holder.cartItemRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 해당 아이템을 삭제하는 로직을 구현하세요.
                // cartItems.remove(position);
                // notifyDataSetChanged();
            }
        });

 */
    }

    @Override
    public int getItemCount() {
        if(cartList == null)
            return 0;
        else
            return cartList.size();
    }

    // ViewHolder 클래스
    public static class CartViewHolder extends RecyclerView.ViewHolder {
        public TextView menuName, menuPrice, optionList;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            menuName = itemView.findViewById(R.id.menuName);
            menuPrice = itemView.findViewById(R.id.menuPrice);
            optionList = itemView.findViewById(R.id.optionList);
        }
    }
}

