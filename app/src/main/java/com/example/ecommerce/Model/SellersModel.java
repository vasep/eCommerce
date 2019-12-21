package com.example.ecommerce.Model;

public class SellersModel {
    private String password;
    private String phone;
    private String address;
    private String email;
    private String sid;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return sid;
    }

    public void setId(String id) {
        this.sid = id;
    }

    public SellersModel(String name, String password, String phone, String address, String email, String sid) {
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.sid = sid;
    }

    public SellersModel() {

    }
}
