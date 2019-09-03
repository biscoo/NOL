package com.example.android.nol.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

public class AdminOrders {

    private String address,city,date,state,time,pid;
    @PropertyName ("client name")
    private String clientName;
    @PropertyName("total amount")
    private String totalAmount;
    @PropertyName("zip code")
    private String zipCode;
    @PropertyName("phone number")
    private String phoneNumber;

    public AdminOrders() {
    }

    public AdminOrders(String address, String city, String date, String state, String time, String clientName, String totalAmount, String zipCode, String phoneNumber, String pid) {
        this.address = address;
        this.city = city;
        this.date = date;
        this.state = state;
        this.time = time;
        this.clientName = clientName;
        this.totalAmount = totalAmount;
        this.zipCode = zipCode;
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @PropertyName ("client name")
    public String getClientName() {
        return clientName;
    }
    @PropertyName ("client name")
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @PropertyName("total amount")
    public String getTotalAmount() {
        return totalAmount;
    }

    @PropertyName("total amount")
    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    @PropertyName("zip code")
    public String getZipCode() {
        return zipCode;
    }

    @PropertyName("zip code")
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @PropertyName("phone number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @PropertyName("phone number")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
