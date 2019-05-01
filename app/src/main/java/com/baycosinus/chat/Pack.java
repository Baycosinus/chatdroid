package com.baycosinus.chat;

import android.util.Log;

import org.json.JSONObject;

public class Pack
{
    public String type;
    public User from;
    public User to;
    public String message;

    Pack(String type, User from, User to, String message)
    {
        this.type = type;
        this.from = from;
        this.to = to;
        this.message = message;
    }

    public JSONObject Pack2JSON()
    {
        try
        {

            JSONObject from = new JSONObject();
            from.put("username", this.from.username);
            from.put("password",this.from.password);
            from.put("ip",this.from.IP);

            JSONObject to = new JSONObject();
            to.put("username",this.to.username);
            to.put("ip",this.to.IP);

            JSONObject p = new JSONObject();
            p.put("type", this.type);
            p.put("from",from);
            p.put("to",to);
            p.put("message",this.message);
            return p;
        }
        catch(Exception e)
        {
            Log.e("JSONParse Exception", e.toString());
            return null;
        }
    }
}
