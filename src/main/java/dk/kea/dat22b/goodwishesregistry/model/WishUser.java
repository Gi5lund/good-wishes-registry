package dk.kea.dat22b.goodwishesregistry.model;

import java.util.List;

public class WishUser {

    private int userId;
    private String userName;
    private String UserPassword;
    private List<WishList> wishLists;

    public WishUser() {
    }

    public WishUser(int userId, String userName, String userPassword) {
        this.userId = userId;
        this.userName = userName;
        UserPassword = userPassword;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

    public List<WishList> getWishLists()
        {
            return wishLists;
        }

    public void setWishLists(List<WishList> wishLists)
        {
            this.wishLists = wishLists;
        }

    @Override
    public String toString() {
        return "WishUser{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", UserPassword='" + UserPassword + '\'' +
                ", Wishlists: ["+wishLists+"] }";
    }
}
