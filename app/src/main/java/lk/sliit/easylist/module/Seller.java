package lk.sliit.easylist.module;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class Seller implements Parcelable {
    String name;
    String email;
    int mobileNumber;
    String shopName;
    String shopAddress;
    double lat;
    double lng;

    DataSnapshot ds;



    ArrayList<InventoryItem> inventoryItems=new ArrayList<>();

    public Seller(){  }

    public Seller(String name, String email, int mobileNumber, String shopName, String shopAddress, double lat, double lng) {
        this.email = email;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.lat = lat;
        this.lng = lng;
    }

    protected Seller(Parcel in) {
        name = in.readString();
        email = in.readString();
        mobileNumber = in.readInt();
        shopName = in.readString();
        shopAddress = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
    }

    public static final Creator<Seller> CREATOR = new Creator<Seller>() {
        @Override
        public Seller createFromParcel(Parcel in) {
            return new Seller(in);
        }

        @Override
        public Seller[] newArray(int size) {
            return new Seller[size];
        }
    };

    public DataSnapshot getDs() {
        return ds;
    }

    public void setDs(DataSnapshot ds) {
        this.ds = ds;
    }

    public void setInventoryItems() {
        for (DataSnapshot d : ds.getChildren()) {
            for(DataSnapshot d2 : d.getChildren()) {
                InventoryItem inventoryItem = d2.getValue(InventoryItem.class);
                inventoryItems.add(inventoryItem);
            }
        }
    }

    public ArrayList<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(int mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeInt(mobileNumber);
        dest.writeString(shopName);
        dest.writeString(shopAddress);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }
}
