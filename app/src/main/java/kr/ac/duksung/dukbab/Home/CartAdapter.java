package kr.ac.duksung.dukbab.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.ac.duksung.dukbab.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartDTO> cartList;
    private int quantityInt;

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
        holder.menuPrice.setText("￦ " + cartItem.getMenuPrice());
        holder.optionList.setText(cartItem.getSelectedOptions().toString());
        quantityInt = cartItem.getMenuQuantity();
        holder.quantityTextView.setText(Integer.toString(cartItem.getMenuQuantity()));

        int menuPriceInt = Integer.parseInt(cartItem.getMenuPrice().replace(",", "")); // 가격에서 특수 문자 제거
        int optionTotalPriceInt = menuPriceInt * quantityInt; // 수량과 가격을 곱하여 총합 가격 계산
        // 총합 가격을 숫자 포맷팅을 사용하여 표시
        String formattedOptionTotalPrice = String.format("￦ %,d", optionTotalPriceInt);
        holder.totalPrice.setText(formattedOptionTotalPrice);

        if(quantityInt > 1)
            holder.minusButton.setImageResource(R.drawable.ic_minus);
        else
            holder.minusButton.setImageResource(R.drawable.ic_minus_default);

        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityInt--;
                if(quantityInt == 1)
                    holder.minusButton.setImageResource(R.drawable.ic_minus_default);
                holder.quantityTextView.setText(String.valueOf(quantityInt));

                int menuPriceInt = Integer.parseInt(cartItem.getMenuPrice().replace(",", "")); // 가격에서 특수 문자 제거
                int optionTotalPriceInt = menuPriceInt * quantityInt; // 수량과 가격을 곱하여 총합 가격 계산
                // 총합 가격을 숫자 포맷팅을 사용하여 표시
                String formattedOptionTotalPrice = String.format("￦ %,d", optionTotalPriceInt);
                holder.totalPrice.setText(formattedOptionTotalPrice);
            }
        });

        holder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantityInt++;
                if(quantityInt > 1) {
                    holder.quantityTextView.setText(String.valueOf(quantityInt));
                    int menuPriceInt = Integer.parseInt(cartItem.getMenuPrice().replace(",", "")); // 가격에서 특수 문자 제거
                    int optionTotalPriceInt = menuPriceInt * quantityInt; // 수량과 가격을 곱하여 총합 가격 계산
                    // 총합 가격을 숫자 포맷팅을 사용하여 표시
                    String formattedOptionTotalPrice = String.format("￦ %,d", optionTotalPriceInt);
                    holder.totalPrice.setText(formattedOptionTotalPrice);
                }
            }
        });


        // 필요에 따라 아이템 삭제 기능을 추가하세요.
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 해당 아이템을 삭제하는 로직을 구현하세요.
                cartList.remove(position);
                notifyDataSetChanged();
            }
        });
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
        public TextView menuName, menuPrice, optionList, quantityTextView, totalPrice;
        public ImageView minusButton, plusButton, removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            menuName = itemView.findViewById(R.id.menuName);
            menuPrice = itemView.findViewById(R.id.menuPrice);
            optionList = itemView.findViewById(R.id.optionList);
            minusButton = itemView.findViewById(R.id.minus);
            quantityTextView = itemView.findViewById(R.id.quantity);
            plusButton = itemView.findViewById(R.id.plus);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            removeButton = itemView.findViewById(R.id.ic_remove);
        }
    }
}

