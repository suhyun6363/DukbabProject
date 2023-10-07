package kr.ac.duksung.dukbab.Home;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CartDTO implements Parcelable{
    //private int menuId;
    private String menuName;
    private String menuPrice;
    private int menuQuantity;
    private List<String> selectedOptions;

    public CartDTO(String menuName, String menuPrice, int menuQuantity, List<String> selectedOptions) {
        //this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuQuantity = menuQuantity;
        this.selectedOptions = selectedOptions;
    }

    public String getMenuName() {
        return menuName;
    }

    public int getMenuQuantity() {
        return menuQuantity;
    }

    public String getMenuPrice() {
        return menuPrice;
    }

    public List<String> getSelectedOptions() {
        return selectedOptions;
    }

    protected CartDTO(Parcel in) {
        menuName = in.readString();
        menuPrice = in.readString();
        selectedOptions = in.createStringArrayList();
    }

    public static final Parcelable.Creator<CartDTO> CREATOR = new Parcelable.Creator<CartDTO>() {
        @Override
        public CartDTO createFromParcel(Parcel in) {
            return new CartDTO(in);
        }

        @Override
        public CartDTO[] newArray(int size) {
            return new CartDTO[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(menuName);
        dest.writeString(menuPrice);
        dest.writeStringList(selectedOptions);
    }
}
