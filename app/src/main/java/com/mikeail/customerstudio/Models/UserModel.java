package com.mikeail.customerstudio.Models;

public class UserModel {
    private String userKey;
    private String userName;
    private String userEmail;
    private String userPass;

    public UserModel(String userKey, String userName, String userEmail, String userPass) {
        this.userKey = userKey;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPass = userPass;
    }

    public UserModel() {
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }
}
