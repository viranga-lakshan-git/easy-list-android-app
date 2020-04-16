package lk.sliit.easylist.module;

public class Seller {
    String name;
    String email;
    int mobileNumber;
    String shopName;
    String shopAddress;

    public Seller(String name, String email, int mobileNumber, String shopName, String shopAddress) {
        this.email = email;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
