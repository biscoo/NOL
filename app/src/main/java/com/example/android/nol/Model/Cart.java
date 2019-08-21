package com.example.android.nol.Model;

public class Cart {

    private String name, pid, price,quantity, discount,image ;

    public Cart(){

    }

    public Cart(String name, String pid, String price, String quantity, String discount, String image) {
        this.name = name;
        this.pid = pid;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
