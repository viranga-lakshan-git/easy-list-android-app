package lk.sliit.easylist.module;

public class Item {
    private  String category;
    private String itemName;
    private String imgURL;

    public Item() {
    }

    public Item(String category, String itemName, String imgURL) {
        this.itemName = itemName;
        this.imgURL = imgURL;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }


}