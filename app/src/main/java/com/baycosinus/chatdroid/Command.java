package com.baycosinus.chatdroid;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.NetPermission;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Command
{
    private String HOST;
    private int PORT;
    private static Context context;
    public String response;
    private Exception exception;
    Command(Context context, String HOST, int PORT)
    {
        this.context = context;
        this.HOST = HOST;
        this.PORT = PORT;
    }
    public void UsernameAvailable(final String username, final String password)
    {

        class task extends AsyncTask<String,Void,Boolean>
        {
            @Override
            protected Boolean doInBackground(String... strings) {
                NFunc func = new NFunc();
                func.Send(HOST,PORT,strings[0]);
                response = func.Receive(PORT);
               return Boolean.parseBoolean(response);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                //super.onPostExecute(response);
                Log.e("Log","On Post Execute");
                Log.e("Log",String.valueOf(result));
                if (!result)
                {
                    Toast.makeText(context, "Username already in use.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Log.e("Test", "Registering...");
                    Register(username, password);
                }
            }
        }
        String command = "CheckAvailable:" + username;
        new task().execute(command);

        //new NFunc(context, HOST,PORT).execute(command);
        Log.e("Log:","Async sonrası çalışıyor.");
    }

    public void Login(String username, String password)
    {
        String command = "Login:" + username + ":" + password;
        class task extends AsyncTask<String,Void,Void>
        {
            @Override
            protected Void doInBackground(String... strings) {
                NFunc func = new NFunc();
                func.Send(HOST,PORT,strings[0]);
                response = func.Receive(PORT);
                return  null;
            }

            @Override
            protected void onPostExecute(Void result) {
                //super.onPostExecute(response);
                Log.e("Log","On Post Execute");
                Log.e("Log",String.valueOf(result));
            }
        }
        new task().execute(command);

    }
    public static boolean Logout(int uid){return false;}

    public void Register(String username, String password)
    {
        String command = "Register:" + username + ":" + password;

        class task extends AsyncTask<String,Void,Void>
        {
            @Override
            protected Void doInBackground(String... strings) {
                NFunc func = new NFunc();
                func.Send(HOST,PORT,strings[0]);
                response = func.Receive(PORT);
                return  null;
            }

            @Override
            protected void onPostExecute(Void result) {
                //super.onPostExecute(response);
                Log.e("Log","On Post Execute");
                Log.e("Log",String.valueOf(result));
            }
        }
        new task().execute(command);

    }
    //public static List<User> GetOnlineList(){ List<User> userlist = new List<User>();
    //return userlist;}
}
