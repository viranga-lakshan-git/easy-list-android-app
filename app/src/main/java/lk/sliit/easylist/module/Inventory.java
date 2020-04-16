package lk.sliit.easylist.module;

import java.util.ArrayList;

public class Inventory {
    String sellerID;
    ArrayList<InventoryItem> inventoryItems;

    public Inventory() {}

    public Inventory(String sellerID, ArrayList<InventoryItem> inventoryItems) {
        this.sellerID = sellerID;
        this.inventoryItems = inventoryItems;
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public ArrayList<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public void setInventoryItems(ArrayList<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }
}
