package dk.kea.dat22b.goodwishesregistry.model;

public class WishList {
    private int wishListId;
    private int wishItemCount;
    private int userId;
    private String wishListName;
    private String occation;

    public WishList() {
    }

    public WishList(int wishListId, int wishItemCount, int userId, String wishListName, String occation) {
        this.wishListId = wishListId;
        this.wishItemCount = wishItemCount;
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

    public int getWishItemCount() {
        return wishItemCount;
    }

    public void setWishItemCount(int wishItemCount) {
        this.wishItemCount = wishItemCount;
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
                ", wishItemCount=" + wishItemCount +
                ", userId=" + userId +
                ", wishListName='" + wishListName + '\'' +
                ", occation='" + occation + '\'' +
                '}';
    }
}
