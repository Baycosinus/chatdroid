package com.baycosinus.chatdroid;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Command
{
    public int userID;
    private String HOST;
    private int PORT;
    private static Context context;
    public String response;
    private Exception exception;
    List<User> userlist = new ArrayList<>();
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
    }

    public void Login(String username, String password)
    {
        String command = "Login:" + username + ":" + password;
        class task extends AsyncTask<String,Void,Integer>
        {
            @Override
            protected Integer doInBackground(String... strings) {
                NFunc func = new NFunc();
                func.Send(HOST,PORT,strings[0]);
                response = func.Receive(PORT);
                Log.e("Log",String.valueOf(response));
                return  Integer.parseInt(response);
            }

            @Override
            protected void onPostExecute(Integer result) {
                //super.onPostExecute(response);
                Log.e("Log","On Post Execute");
                Log.e("Log",String.valueOf(result));
                userID = result;
                Log.e("USERID", String.valueOf(userID) );
                if(result == 0)
                {
                    Toast.makeText(context,"Login failed. Please check your credentials.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent intent = new Intent(context, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("userID",String.valueOf(userID));
                    intent.putExtra("HOST", HOST);
                    intent.putExtra("PORT", PORT);
                    context.startActivity(intent);
                }
            }
        }
        new task().execute(command);

    }
    public void Logout(String uid)
    {
        String command = "Logout:" + uid;
        class task extends AsyncTask<String,Void,Integer>
        {
            @Override
            protected Integer doInBackground(String... strings) {
                NFunc func = new NFunc();
                func.Send(HOST,PORT,strings[0]);
                response = func.Receive(PORT);
                Log.e("Log",String.valueOf(response));
                return  Integer.parseInt(response);
            }

            @Override
            protected void onPostExecute(Integer result) {
                //super.onPostExecute(response);
                Log.e("Log","On Post Execute");
                Log.e("Log",String.valueOf(result));
                userID = result;
                if(result == 0)
                {
                    Toast.makeText(context,"Login failed. Please check your credentials.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent intent = new Intent(context, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("userID",userID);
                    context.startActivity(intent);
                }
            }

        }
        new task().execute(command);
    }

    public void Register(final String username, String password)
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
    public void GetOnlineList()
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
                Log.e("Log","On Post Execute");
                Log.e("Log",String.valueOf(result));
                userlist.clear();
                String[] users = response.split(":");
                HashMap<String, String> hashMap = new HashMap<>();
                for (int i = 0; i < users.length; i++)
                {
                    String user[] = users[i].split(",");
                    hashMap.put(user[1], user[2]);
                }
                List<HashMap<String,String>> listItems = new ArrayList<>();
                SimpleAdapter adapter = new SimpleAdapter(context, listItems, R.layout.online_list_item,
                        new String[]{"First Line", "Second Line"},
                        new int[]{R.id.UsernameText, R.id.IPText});

                Iterator it = hashMap.entrySet().iterator();
                while(it.hasNext())
                {
                    HashMap<String,String> resultMap = new HashMap<>();
                    Map.Entry pair = (Map.Entry)it.next();
                    resultMap.put("First Line", pair.getKey().toString());
                    resultMap.put("Second Line", pair.getValue().toString());
                    listItems.add(resultMap);
                }
                DashboardActivity d = new DashboardActivity();
                d.onlineList.setAdapter(adapter);
            }
        }
        String command = "GetOnlineList:";
        new task().execute(command);
    }
}
