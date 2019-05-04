package com.baycosinus.chatdroid;

import android.widget.ArrayAdapter;

public class User
{
    public int userID;
    public String username;
    public String password;
    public String IP;


    User(int userID, String username, String password, String IP)
    {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.IP = IP;
    }
}
