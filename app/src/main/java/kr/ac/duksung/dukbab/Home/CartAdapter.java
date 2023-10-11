package kr.ac.duksung.dukbab.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import kr.ac.duksung.dukbab.R;
import kr.ac.duksung.dukbab.db.CartDBOpenHelper;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> implements OptionDrawerFragment.BtnaddToCartListener {

    private List<CartDTO> cartList;
    private HomeFragment homeFragment; // HomeFragment 참조

    public CartAdapter(List<CartDTO> cartList, HomeFragment homeFragment) {
        this.cartList = cartList;
        this.homeFragment = homeFragment; // HomeFragment 참조를 설정
    }

    public void nodifyChange() {
        notifyDataSetChanged();
    }

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
        int quantityInt = cartItem.getMenuQuantity();
        holder.quantityTextView.setText(Integer.toString(quantityInt));

        int menuPriceInt = Integer.parseInt(cartItem.getMenuPrice().replace(",", "")); // 가격에서 특수 문자 제거
        int optionTotalPriceInt = menuPriceInt * quantityInt; // 수량과 가격을 곱하여 총합 가격 계산
        // 총합 가격을 숫자 포맷팅을 사용하여 표시
        String formattedOptionTotalPrice = String.format("￦ %,d", optionTotalPriceInt);
        holder.totalPriceTextView.setText(formattedOptionTotalPrice);

        if(quantityInt > 1) {
            holder.minusButton.setImageResource(R.drawable.ic_minus);
            holder.minusButton.setEnabled(true);
        }
        else {
            holder.minusButton.setImageResource(R.drawable.ic_minus_default);
            holder.minusButton.setEnabled(false);
        }


        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = cartItem.getMenuQuantity();
                if (currentQuantity > 1) {
                    // 1. 데이터베이스의 menuquantity를 감소시킴
                    int newQuantity = currentQuantity - 1;

                    // CartDBOpenHelper 인스턴스를 생성
                    CartDBOpenHelper dbOpenHelper = new CartDBOpenHelper(v.getContext()); // 여기서 'v.getContext()'를 사용하여 Context를 가져옵니다.

                    // CartDBOpenHelper 인스턴스를 사용하여 업데이트
                    dbOpenHelper.updateCartItemQuantity(cartItem, newQuantity);

                    // 2. 변경된 menuquantity를 가져와서 화면에 업데이트
                    cartItem.setMenuQuantity(newQuantity);
                    notifyDataSetChanged();
                    if (homeFragment != null) {
                        homeFragment.updateTotalPriceAndCount();
                    }
                }
            }
        });

        holder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = cartItem.getMenuQuantity();
                // 1. 데이터베이스의 menuquantity를 감소시킴
                int newQuantity = currentQuantity + 1;

                // CartDBOpenHelper 인스턴스를 생성
                CartDBOpenHelper dbOpenHelper = new CartDBOpenHelper(view.getContext()); // 여기서 'v.getContext()'를 사용하여 Context를 가져옵니다.

                // CartDBOpenHelper 인스턴스를 사용하여 업데이트
                dbOpenHelper.updateCartItemQuantity(cartItem, newQuantity);

                // 2. 변경된 menuquantity를 가져와서 화면에 업데이트
                cartItem.setMenuQuantity(newQuantity);
                notifyDataSetChanged();
                if (homeFragment != null) {
                    homeFragment.updateTotalPriceAndCount();
                };
            }
        });


        // 필요에 따라 아이템 삭제 기능을 추가하세요.
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 해당 아이템을 삭제하는 로직을 구현하세요.
                CartDTO cartItemToRemove = cartList.get(position);

                // CartDBOpenHelper 인스턴스를 생성
                CartDBOpenHelper dbOpenHelper = new CartDBOpenHelper(v.getContext()); // 여기서 'v.getContext()'를 사용하여 Context를 가져옵니다.

                // deleteCartItem 메서드를 호출하여 데이터베이스에서 아이템 삭제
                dbOpenHelper.deleteCartItem(cartItemToRemove);

                cartList.remove(position);
                notifyDataSetChanged();

                homeFragment.updateTotalPriceAndCount();
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
        public TextView menuName, menuPrice, optionList, quantityTextView, totalPriceTextView;
        public ImageView minusButton, plusButton, removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            menuName = itemView.findViewById(R.id.menuName);
            menuPrice = itemView.findViewById(R.id.menuPrice);
            optionList = itemView.findViewById(R.id.optionList);
            minusButton = itemView.findViewById(R.id.minus);
            quantityTextView = itemView.findViewById(R.id.quantity);
            plusButton = itemView.findViewById(R.id.plus);
            totalPriceTextView = itemView.findViewById(R.id.totalPrice);
            removeButton = itemView.findViewById(R.id.ic_remove);
        }
    }

}