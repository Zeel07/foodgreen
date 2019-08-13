package com.example.foodgreen;

public class model_new_sell_order {
    String id;
    private String data_dish_name;
    private String data_dish_price;
    private String data_dish_quantity;
    private String data_dish_description;
    private String data_cook_time;
    private String data_cook_date;
    private String data_expire_time;
    private String data_expire_date;
    private String image_name;
    private String food_category;
    private String user_data_email;
    private String user_data_phonenum;
    private String user_data_username;
    private String data_seller_email;
    private String data_seller_phonenum;
    private String data_seller_username;

    public String getData_seller_phonenum() {
        return data_seller_phonenum;
    }

    public void setData_seller_phonenum(String data_seller_phonenum) {
        this.data_seller_phonenum = data_seller_phonenum;
    }

    public String getData_seller_username() {
        return data_seller_username;
    }

    public void setData_seller_username(String data_seller_username) {
        this.data_seller_username = data_seller_username;
    }

    public String getUser_data_phonenum() {
        return user_data_phonenum;
    }

    public void setUser_data_phonenum(String user_data_phonenum) {
        this.user_data_phonenum = user_data_phonenum;
    }

    public String getUser_data_username() {
        return user_data_username;
    }

    public void setUser_data_username(String user_data_username) {
        this.user_data_username = user_data_username;
    }

    public String getData_seller_email() {
        return data_seller_email;
    }

    public void setData_seller_email(String data_seller_email) {
        this.data_seller_email = data_seller_email;
    }

    public String getUser_data_email() {
        return user_data_email;
    }

    public void setUser_data_email(String user_data_email) {
        this.user_data_email = user_data_email;
    }

    public String getFood_category() {
        return food_category;
    }

    public void setFood_category(String food_category) {
        this.food_category = food_category;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData_dish_name() {
        return data_dish_name;
    }

    public void setData_dish_name(String data_dish_name) {
        this.data_dish_name = data_dish_name;
    }

    public String getData_dish_price() {
        return data_dish_price;
    }

    public void setData_dish_price(String data_dish_price) {
        this.data_dish_price = data_dish_price;
    }

    public String getData_dish_quantity() {
        return data_dish_quantity;
    }

    public void setData_dish_quantity(String data_dish_quantity) {
        this.data_dish_quantity = data_dish_quantity;
    }

    public String getData_dish_description() {
        return data_dish_description;
    }

    public void setData_dish_description(String data_dish_description) {
        this.data_dish_description = data_dish_description;
    }

    public String getData_cook_time() {
        return data_cook_time;
    }

    public void setData_cook_time(String data_cook_time) {
        this.data_cook_time = data_cook_time;
    }

    public String getData_cook_date() {
        return data_cook_date;
    }

    public void setData_cook_date(String data_cook_date) {
        this.data_cook_date = data_cook_date;
    }

    public String getData_expire_time() {
        return data_expire_time;
    }

    public void setData_expire_time(String data_expire_time) {
        this.data_expire_time = data_expire_time;
    }

    public String getData_expire_date() {
        return data_expire_date;
    }

    public void setData_expire_date(String data_expire_date) {
        this.data_expire_date = data_expire_date;
    }

    public model_new_sell_order(String data_dish_name, String data_dish_price, String data_dish_quantity, String data_dish_description, String data_cook_time, String data_cook_date, String data_expire_time, String data_expire_date, String image_name, String food_category, String user_data_email, String user_data_phonenum, String user_data_username){
        this.data_dish_name = data_dish_name;
        this.data_dish_price = data_dish_price;
        this.data_dish_quantity = data_dish_quantity;
        this.data_dish_description = data_dish_description;
        this.data_cook_time = data_cook_time;
        this.data_cook_date = data_cook_date;
        this.data_expire_time = data_expire_time;
        this.data_expire_date = data_expire_date;
        this.image_name = image_name;
        this.food_category = food_category;
        this.user_data_email = user_data_email;
        this.user_data_phonenum = user_data_phonenum;
        this.user_data_username = user_data_username;
    }

    public model_new_sell_order(String data_dish_name, String data_dish_price, String data_dish_quantity, String data_dish_description, String data_cook_time, String data_cook_date, String data_expire_time, String data_expire_date, String image_name, String food_category, String user_data_email, String user_data_phonenum, String user_data_username, String data_seller_email, String data_seller_phonenum, String data_seller_username){
        this.data_dish_name = data_dish_name;
        this.data_dish_price = data_dish_price;
        this.data_dish_quantity = data_dish_quantity;
        this.data_dish_description = data_dish_description;
        this.data_cook_time = data_cook_time;
        this.data_cook_date = data_cook_date;
        this.data_expire_time = data_expire_time;
        this.data_expire_date = data_expire_date;
        this.image_name = image_name;
        this.food_category = food_category;
        this.user_data_email = user_data_email;
        this.user_data_phonenum = user_data_phonenum;
        this.user_data_username = user_data_username;
        this.data_seller_email = data_seller_email;
        this.data_seller_phonenum = data_seller_phonenum;
        this.data_seller_username = data_seller_username;
    }
}
