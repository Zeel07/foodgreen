package com.example.foodgreen;

public class model_bid {
    public String getData_seller_email() {
        return data_seller_email;
    }

    public void setData_seller_email(String data_seller_email) {
        this.data_seller_email = data_seller_email;
    }

    private String data_seller_email;

    public String getData_seller_phonenum() {
        return data_seller_phonenum;
    }

    public void setData_seller_phonenum(String data_seller_phonenum) {
        this.data_seller_phonenum = data_seller_phonenum;
    }

    private String data_seller_phonenum;

    public String getData_seller_username() {
        return data_seller_username;
    }

    public void setData_seller_username(String data_seller_username) {
        this.data_seller_username = data_seller_username;
    }

    private String data_seller_username;

    public String getBid_value() {
        return bid_value;
    }

    public void setBid_value(String bid_value) {
        this.bid_value = bid_value;
    }

    private String bid_value;

    public String getData_seller_location() {
        return data_seller_location;
    }

    public void setData_seller_location(String data_seller_location) {
        this.data_seller_location = data_seller_location;
    }

    private String data_seller_location;

    public model_bid(String data_seller_email, String data_seller_phonenum, String data_seller_username, String data_seller_location, String bid_value){
        this.data_seller_email = data_seller_email;
        this.data_seller_phonenum = data_seller_phonenum;
        this.data_seller_username = data_seller_username;
        this.data_seller_location = data_seller_location;
        this.bid_value = bid_value;
    }
}
