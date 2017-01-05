package com.tiy.datatbase;

/**
 * Created by crci1 on 1/4/2017.
 */
public class Users {
    private int userId;
    private String username;
    private String fullname;

    public Users(int userId, String username, String fullname) {
        this.userId = userId;
        this.username = username;
        this.fullname = fullname;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
