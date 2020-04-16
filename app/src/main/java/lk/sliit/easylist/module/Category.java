package lk.sliit.easylist.module;

public class Category {
    private String categoryName;
    private String imgURL;

    public Category() {
    }

    public Category(String categoryName, String imgURL) {
        this.categoryName = categoryName;
        this.imgURL = imgURL;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }


}