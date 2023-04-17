package dk.kea.dat22b.goodwishesregistry.model;

import java.util.List;

public class WishList {
    private int wishListId;
    private int userId;
    private String wishListName;
    private String occation;

    private List<WishListItems> wisher;
    public WishList() {
    }

    public WishList(int wishListId, int userId, String wishListName, String occation, List<WishListItems> wisher ) {
        this.wishListId = wishListId;
        this.userId = userId;
        this.wishListName = wishListName;
        this.occation = occation;
        this.wisher=wisher;
    }
    public WishList(int wishListId,String wishListName, String occation, int userId) {
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

    public List<WishListItems> getWisher()
        {
            return wisher;
        }

    public void setWisher(List<WishListItems> wisher)
        {
            this.wisher = wisher;
        }

    @Override
    public String toString() {
        return "WishList{" +
                "wishListId=" + wishListId +
                ", userId=" + userId +
                ", wishListName='" + wishListName + '\'' +
                ", occation='" + occation + '\'' +
                " wishlistitems:{ "+ wisher+"} '}'";
    }
}
