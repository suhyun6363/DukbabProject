package kr.ac.duksung.dukbab.Home;

import android.os.Parcel;
import android.os.Parcelable;

public class MenuDTO implements Parcelable{
    private String name;
    private String price;
    private int imageResourceId;

    public MenuDTO(String name, String price, int imageResourceId) {
        this.name = name;
        this.price = price;
        this.imageResourceId = imageResourceId;
    }

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
        dest.writeString(name);
        dest.writeString(price);
        dest.writeInt(imageResourceId);
    }
}
