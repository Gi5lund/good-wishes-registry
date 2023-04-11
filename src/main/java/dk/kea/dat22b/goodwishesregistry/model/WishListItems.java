package dk.kea.dat22b.goodwishesregistry.model;

public class WishListItems {

    private int wishListId;
    private int itemLineId;
    private String itemName;
    private int itemQTY;
    private String itemDescription;
    private String itemURL;
    private double itemPrice;
    private boolean itemReserved;
    private String itemReservedBy;

    public WishListItems() {
    }

    public WishListItems(int wishListId, int itemLineId, String itemName, int itemQTY, String itemDescription, String itemURL, double itemPrice, boolean itemReserved, String itemReservedBy) {
        this.wishListId = wishListId;
        this.itemLineId = itemLineId;
        this.itemName = itemName;
        this.itemQTY = itemQTY;
        this.itemDescription = itemDescription;
        this.itemURL = itemURL;
        this.itemPrice = itemPrice;
        this.itemReserved = itemReserved;
        this.itemReservedBy = itemReservedBy;
    }

    public int getWishListId() {
        return wishListId;
    }

    public void setWishListId(int wishListId) {
        this.wishListId = wishListId;
    }

    public int getItemLineId() {
        return itemLineId;
    }

    public void setItemLineId(int itemLineId) {
        this.itemLineId = itemLineId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemQTY() {
        return itemQTY;
    }

    public void setItemQTY(int itemQTY) {
        this.itemQTY = itemQTY;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemURL() {
        return itemURL;
    }

    public void setItemURL(String itemURL) {
        this.itemURL = itemURL;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public boolean isItemReserved() {
        return itemReserved;
    }

    public void setItemReserved(boolean itemReserved) {
        this.itemReserved = itemReserved;
    }

    public String getItemReservedBy() {
        return itemReservedBy;
    }

    public void setItemReservedBy(String itemReservedBy) {
        this.itemReservedBy = itemReservedBy;
    }

    @Override
    public String toString() {
        return "WishListItems{" +
                "wishListId=" + wishListId +
                ", itemLineId=" + itemLineId +
                ", itemName='" + itemName + '\'' +
                ", itemQTY=" + itemQTY +
                ", itemDescription='" + itemDescription + '\'' +
                ", itemURL='" + itemURL + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemReserved=" + itemReserved +
                ", itemReservedBy='" + itemReservedBy + '\'' +
                '}';
    }
}
