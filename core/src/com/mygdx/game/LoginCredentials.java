package com.mygdx.game;

public class LoginCredentials {
    private int userID;
    private String username;
    private String password;
    private Boolean isAuth;

    public LoginCredentials() {
        isAuth = true;
    }

    public LoginCredentials(String username, String password) {
        this.username = username;
        this.password = password;
        isAuth = false;
        userID = -1;
    }

    public Boolean getAuth() {
        return isAuth;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
