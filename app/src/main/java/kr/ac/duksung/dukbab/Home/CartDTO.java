package kr.ac.duksung.dukbab.Home;

import java.util.ArrayList;
import java.util.List;

public class CartDTO {
    //private int menuId;
    private String menuName;
    private String menuPrice;
    //private int menuQuantity;
    private List<String> selectedOptions;

    public CartDTO(String menuName, String menuPrice, List<String> selectedOptions) {
        //this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        //this.menuQuantity = menuQuantity;
        this.selectedOptions = selectedOptions;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getMenuPrice() {
        return menuPrice;
    }

    public List<String> getSelectedOptions() {
        return selectedOptions;
    }
}
