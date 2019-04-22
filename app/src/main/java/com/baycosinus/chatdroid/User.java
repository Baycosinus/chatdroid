package com.baycosinus.chatdroid;

public class User
{
    private final String username;
    private final String IP;
    private final boolean status;

    User(String username, String IP,boolean status)
    {
        this.username = username;
        this.IP = IP;
        this.status = status;
    }
}
