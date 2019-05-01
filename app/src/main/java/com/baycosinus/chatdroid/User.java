package com.baycosinus.chatdroid;

public class User
{
    public String username;
    public String password;
    public String IP;
    public boolean status;

    User(String username, String password, String IP,boolean status)
    {
        this.username = username;
        this.password = password;
        this.IP = IP;
        this.status = status;
    }
    }
