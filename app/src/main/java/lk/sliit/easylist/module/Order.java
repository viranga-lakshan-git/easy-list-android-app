package lk.sliit.easylist.module;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Order implements Parcelable,Comparable<Order> {
    String buyerID;
    Seller seller;
    ArrayList<ListItem> foundListItems, notfoundListItems;
    float percentage;
    float totalPrice;

    public Order(){}

    public Order(String buyerID, Seller seller, ArrayList<ListItem> foundListItems, ArrayList<ListItem> notfoundListItems, float percentage, float totalPrice) {
        this.buyerID = buyerID;
        this.seller = seller;
        this.foundListItems = foundListItems;
        this.notfoundListItems = notfoundListItems;
        this.percentage = percentage;
        this.totalPrice = totalPrice;
    }

    protected Order(Parcel in) {
        buyerID = in.readString();
        seller = in.readParcelable(Seller.class.getClassLoader());
        foundListItems = in.createTypedArrayList(ListItem.CREATOR);
        notfoundListItems = in.createTypedArrayList(ListItem.CREATOR);
        percentage = in.readFloat();
        totalPrice = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(buyerID);
        dest.writeParcelable(seller, flags);
        dest.writeTypedList(foundListItems);
        dest.writeTypedList(notfoundListItems);
        dest.writeFloat(percentage);
        dest.writeFloat(totalPrice);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public String getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(String buyerID) {
        this.buyerID = buyerID;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public ArrayList<ListItem> getFoundListItems() {
        return this.foundListItems;
    }

    public void setFoundListItems(ArrayList<ListItem> foundListItems) {
        this.foundListItems = foundListItems;
    }

    public ArrayList<ListItem> getNotfoundListItems() {
        return notfoundListItems;
    }

    public void setNotfoundListItems(ArrayList<ListItem> notfoundListItems) {
        this.notfoundListItems = notfoundListItems;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public int compareTo(Order o) {
        if (percentage<o.getPercentage())
            return 1;
        else if (percentage>o.getPercentage())
            return -1;
        else
            return 0;
    }
}
