package com.baycosinus.chatdroid;

import java.util.Date;

public class Pack
{
    public String type;
    public User from = null;
    public User to = null;
    public String msg = null;

    Pack(String type, User from, User to, String msg)
    {
        this.type = type;
        this.from = from;
        this.to = to;
        this.msg = msg;
    }
}
