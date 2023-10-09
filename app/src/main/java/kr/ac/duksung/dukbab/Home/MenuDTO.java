package kr.ac.duksung.dukbab.Home;

import android.os.Parcel;
import android.os.Parcelable;

public class MenuDTO implements Parcelable{
    //private int menuID;
    private int storeId;
    private String name;
    private String price;
    private int imageResourceId;

    public MenuDTO(int storeId, String name, String price, int imageResourceId) {
        this.storeId = storeId;
        this.name = name;
        this.price = price;
        this.imageResourceId = imageResourceId;
    }

    public int getStoreId() { return storeId; }

    public void setStoreId(int storeId) { this.storeId = storeId; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    // Parcelable 구현 메서드
    protected MenuDTO(Parcel in) {
        storeId = in.readInt();
        name = in.readString();
        price = in.readString();
        imageResourceId = in.readInt();
    }

    public static final Parcelable.Creator<MenuDTO> CREATOR = new Parcelable.Creator<MenuDTO>() {
        @Override
        public MenuDTO createFromParcel(Parcel in) {
            return new MenuDTO(in);
        }

        @Override
        public MenuDTO[] newArray(int size) {
            return new MenuDTO[size];
        }
    };


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(storeId);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeInt(imageResourceId);
    }
}
