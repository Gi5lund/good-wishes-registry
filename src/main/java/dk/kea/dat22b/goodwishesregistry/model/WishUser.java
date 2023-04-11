package dk.kea.dat22b.goodwishesregistry.model;

public class WishUser {

    private int userId;
    private String userName;
    private String UserPassword;

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

    @Override
    public String toString() {
        return "WishUser{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", UserPassword='" + UserPassword + '\'' +
                '}';
    }
}
