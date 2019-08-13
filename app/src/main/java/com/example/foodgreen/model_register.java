package com.example.foodgreen;


public class model_register {
    String id;
    private String username;
    private String email;
    private String password;
    private String phonenum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }


    public model_register(String email, String  password, String username, String phonenum) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.phonenum = phonenum;
    }

}
