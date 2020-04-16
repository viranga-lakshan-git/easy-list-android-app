package lk.sliit.easylist.module;

import java.util.ArrayList;

public class OrderDetails {
    String name;
    String address;
    String mobile;
    String email;
    String price;
    ArrayList<ListItem> foundItems;
    String orderStatus;
    String buyerKey, sellerKey;

    public String getBuyerKey() {
        return buyerKey;
    }

    public void setBuyerKey(String buyerKey) {
        this.buyerKey = buyerKey;
    }

    public String getSellerKey() {
        return sellerKey;
    }

    public void setSellerKey(String sellerKey) {
        this.sellerKey = sellerKey;
    }

    public OrderDetails(String name, String address, String mobile, String email, String price, ArrayList<ListItem> foundItems) {
        this.name = name;
        this.address = address;
        this.mobile = mobile;
        this.email = email;
        this.price = price;
        this.foundItems = foundItems;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ArrayList<ListItem> getFoundItems() {
        return foundItems;
    }

    public void setFoundItems(ArrayList<ListItem> foundItems) {
        this.foundItems = foundItems;
    }
}
