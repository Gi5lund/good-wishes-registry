package dk.kea.dat22b.goodwishesregistry.model;

import java.util.ArrayList;
import java.util.List;

public class WishList {
    private int wishListId;
    private int userId;
    private String wishListName;
    private String occation;

    private List<WishListItems> Wisher = new ArrayList<>();
    public WishList() {
    }

    public WishList(int wishListId, int wishItemCount, int userId, String wishListName, String occation) {
        this.wishListId = wishListId;
        this.userId = userId;
        this.wishListName = wishListName;
        this.occation = occation;
    }
    public WishList(int wishListId, int userId, String wishListName, String occation) {
        this.wishListId = wishListId;
        this.userId = userId;
        this.wishListName = wishListName;
        this.occation = occation;
    }

    public int getWishListId() {
        return wishListId;
    }

    public void setWishListId(int wishListId) {
        this.wishListId = wishListId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getWishListName() {
        return wishListName;
    }

    public void setWishListName(String wishListName) {
        this.wishListName = wishListName;
    }

    public String getOccation() {
        return occation;
    }

    public void setOccation(String occation) {
        this.occation = occation;
    }

    @Override
    public String toString() {
        return "WishList{" +
                "wishListId=" + wishListId +
                ", userId=" + userId +
                ", wishListName='" + wishListName + '\'' +
                ", occation='" + occation + '\'' +
                '}';
    }
}
