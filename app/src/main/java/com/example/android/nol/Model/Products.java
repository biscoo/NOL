package com.example.android.nol.Model;

public class Products {

    private String productName, description, price, image, category, pId,date,time;

    public Products(){

    }

    public Products(String productName, String description, String price, String image, String category, String pId, String date, String time) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.image = image;
        this.category = category;
        this.pId = pId;
        this.date = date;
        this.time = time;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String pname) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
