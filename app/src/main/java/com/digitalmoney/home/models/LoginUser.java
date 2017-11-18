package com.digitalmoney.home.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by shailesh on 14/11/17.
 */


@IgnoreExtraProperties
public class LoginUser {

    public String mobileno;
    public String password;

    public LoginUser() {}

    public LoginUser(String mobileno, String password) {
        this.mobileno =  mobileno;
        this.password = password;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginUser{" +
                "mobileno='" + mobileno + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}