package com.example.shoppingapp;

import java.io.Serializable;

//class for set/get for Item details
public class Upload implements Serializable {
    private String mName;
    private String mImageUrl;
    private String price;
    private String description;
    private String User;
    private String city;
    private String phone;

    public Upload(){

    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Upload(String name, String price, String description, String imageUrl, String user, String city,String phone){
        if(name.trim().equals("")){
            name="No Name";
        }
        mName = name;
        mImageUrl = imageUrl;
        this.description = description;
        this.price =  price;
        this.User = user;
        this.city = city;
        this.phone = phone;
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(){
        this.description = description;
    }

    public String getPrice(){
        return price;
    }
    public void setPrice(){
        this.price = price;
    }




    public String getName(){
        return mName;
    }
    public void setName(String name){
        mName = name;
    }
    public String getmImageUrl(){
        return mImageUrl;
    }
    public void setmImageUrl(String imageUrl){
        mImageUrl = imageUrl;
    }

    public void setUser(String user){
        this.User = user;
    }
    public String getUser(){
        return User;
    }

}
