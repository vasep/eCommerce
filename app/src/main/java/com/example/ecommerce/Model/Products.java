package com.example.ecommerce.Model;

public class Products {
    private String productName;
    private String category;
    private String description;
    private String price;
    private String image;
    private String date;
    private String pid;
    private String time;

    private String productState;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProductState() {
        return productState;
    }

    public void setProductState(String productState) {
        this.productState = productState;
    }

    public Products(String productName, String category, String description, String price, String image, String date, String pid, String time) {
        this.productName = productName;
        this.category = category;
        this.description = description;
        this.price = price;
        this.image = image;
        this.date = date;
        this.pid = pid;
        this.time = time;
    }

    public Products() {

    }
}
