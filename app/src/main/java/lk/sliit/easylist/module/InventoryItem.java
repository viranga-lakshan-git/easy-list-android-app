package lk.sliit.easylist.module;

public class InventoryItem {
    String itemName;
    String category;
    String imgUrl;
    float quantity;
    float unitPrice;

    public InventoryItem() {}

    public InventoryItem(String itemName, String category, String imgUrl, float quantity, float unitPrice) {
        this.itemName = itemName;
        this.category = category;
        this.imgUrl = imgUrl;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getQuantity() {
        return quantity;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }
}
