package com.example.abhinav.studentfirebase;

/**
 * Created by ABHINAV on 23-08-2016.
 */
public class Users {
    String name;
    String phone;
    String imageurl;
    String email;
String id;
    public Users() {
    }



    public Users(String name, String phone, String email,String id) {
        this.name = name;
        this.phone = phone;

this.id=id;
        this.email=email;
    }





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
