package com.baycosinus.chatdroid;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.*;

public class Command
{
    public int userID;
    private String HOST;
    private String PORT;
    public String response;
    private Exception exception;

    Command(String HOST, String PORT)
    {
        this.HOST = HOST;
        this.PORT = PORT;
    }

    public JSONObject String2JSON(Pack pack)
    {
        try
        {

            JSONObject from = new JSONObject();
            from.put("username", pack.from.username);
            from.put("ip",pack.from.IP);

            JSONObject to = new JSONObject();
            to.put("username",pack.to.username);
            to.put("ip",pack.to.IP);

            JSONObject p = new JSONObject();
            p.put("type", pack.type);
            p.put("from",from);
            p.put("to",to);
            p.put("message",pack.msg);
            return p;
        }
        catch(Exception e)
        {
            Log.e("JSONParse Exception", e.toString());
            return null;
        }
    }
    public boolean UsernameAvailable(String username)
    {
        Pack p = new Pack("CheckAvailable",new User(username,"","",false),new User("","","",false),null);
        JSONObject jsonpack = String2JSON(p);
        new Send(HOST,PORT).execute(jsonpack);
        return true;
    }
    public boolean Register(String username, String password){return true;}
    public int Login(String username, String password){ return 0;}
    public void Logout(int uid){}
    public boolean SendMsg(int from, int to, String msg){return true;}

}
