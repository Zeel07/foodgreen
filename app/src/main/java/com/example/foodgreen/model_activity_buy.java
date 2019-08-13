package com.example.foodgreen;

public class model_activity_buy {
    String id;
    private String data_dish_name;
    private String data_quantity;
    private String data_description;
    private String data_expected_time;
    private String data_expected_date;
    private String food_category;
    private String user_data_email;
    private String user_data_phonenum;
    private String user_data_username;

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

    public String getData_quantity() {
        return data_quantity;
    }

    public void setData_quantity(String data_quantity) {
        this.data_quantity = data_quantity;
    }

    public String getData_description() {
        return data_description;
    }

    public void setData_description(String data_description) {
        this.data_description = data_description;
    }

    public String getData_expected_time() {
        return data_expected_time;
    }

    public void setData_expected_time(String data_expected_time) {
        this.data_expected_time = data_expected_time;
    }

    public String getData_expected_date() {
        return data_expected_date;
    }

    public void setData_expected_date(String data_expected_date) {
        this.data_expected_date = data_expected_date;
    }

    public model_activity_buy(String data_dish_name, String data_quantity, String data_description, String data_expected_time, String data_expected_date, String food_category, String user_data_email, String user_data_phonenum, String user_data_username){
        this.data_dish_name = data_dish_name;
        this.data_quantity = data_quantity;
        this.data_description = data_description;
        this.data_expected_time = data_expected_time;
        this.data_expected_date = data_expected_date;
        this.food_category = food_category;
        this.user_data_email = user_data_email;
        this.user_data_phonenum = user_data_phonenum;
        this.user_data_username = user_data_username;
    }
}
