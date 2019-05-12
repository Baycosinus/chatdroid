package com.baycosinus.chatdroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    public ListView onlineList;
    public List<User> userList = new ArrayList<>();
    public int userID;
    public String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        userID = Integer.valueOf(getIntent().getStringExtra("uid"));
        username = getIntent().getStringExtra("username");
        final String HOST = getIntent().getStringExtra("HOST");
        final int PORT = Integer.valueOf(getIntent().getStringExtra("PORT"));

        Toast.makeText(getApplicationContext(),"UID:" + String.valueOf(userID), Toast.LENGTH_LONG).show();

        Button refreshButton = findViewById(R.id.refreshButton);
        Button logoutButton = findViewById(R.id.logoutButton);
        onlineList = findViewById(R.id.onlineList);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pack p = new Pack("logout", new User(userID, null,null,null), new User(0,null,null,null), null);
                JSONObject pack = p.Pack2JSON();
                new Send(HOST,PORT).execute(pack);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlineList.setAdapter(null);
                Pack p = new Pack("get_online", new User(userID,null,null,null), new User(0,null,null,null),null);
                JSONObject pack = p.Pack2JSON();
                new Send(HOST,PORT).execute(pack);
            }
        });

        refreshButton.performClick();

    }

    class Send extends AsyncTask<JSONObject, Void, Boolean>
    {
        private String HOST;
        private int PORT;
        Send(String HOST, int PORT)
        {
            this.HOST = HOST;
            this.PORT = PORT;
        }
        @Override
        protected Boolean doInBackground(JSONObject... packs) {
            try {
                Socket writeSocket = new Socket(HOST, PORT);
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(writeSocket.getOutputStream()));
                writer.print(packs[0]);
                writer.flush();
                writeSocket.close();
                return true;
            }
            catch (Exception e)
            {
                Log.e("AsyncSend Exception",e.toString());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean)
        {
            if(aBoolean)
            {
                new DashboardActivity.Listen(HOST,PORT).execute();
            }
        }
    }

    class Listen extends AsyncTask<Void, Void, JSONObject>
    {
        private String HOST;
        private int PORT;
        Listen(String HOST, int PORT)
        {
            this.HOST = HOST;
            this.PORT = PORT;
        }
        @Override
        protected JSONObject doInBackground(Void... voids) {
            String responseString = "";
            JSONObject response = new JSONObject();
            try
            {
                ServerSocket serverSocket = new ServerSocket(Integer.valueOf(PORT));
                serverSocket.setReuseAddress(true);
                Socket socket = serverSocket.accept();
                InputStream input = socket.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                responseString = reader.readLine();
                responseString = responseString.replace("\\","");
                Log.e("Response", responseString);
                response = new JSONObject(responseString);
                reader.close();
                socket.close();
                serverSocket.close();
            }
            catch (Exception e)
            {
                Log.e("Async Listen Exception",e.toString());
            }

            return response;
        }

        @Override
        protected void onPostExecute(JSONObject list)
        {
            try
            {
                JSONArray array = list.getJSONArray("online_users");
                userList.clear();
                for(int i = 0; i < array.length(); i++)
                {
                    JSONObject j = (JSONObject)array.get(i);
                    int uid = Integer.valueOf((Integer) j.get("userID"));
                    String username = (String)j.get("username");
                    String ip = (String)j.get("ip");
                    User u = new User(uid,username,null,ip);
                    userList.add(u);
                }
                HashMap<String,String> users = new HashMap<>();
                for (int k = 0; k < userList.size(); k++)
                {
                    users.put(userList.get(k).username, userList.get(k).IP);
                }
                List<HashMap<String,String>> listItems = new ArrayList<>();

                SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),listItems, R.layout.online_list_item, new String[]{"FirstLine", "SecondLine"}, new int[]{R.id.FirstLine, R.id.SecondLine});

                Iterator it = users.entrySet().iterator();
                while(it.hasNext())
                {
                    HashMap<String,String> resultsMap = new HashMap<>();
                    Map.Entry pair = (Map.Entry)it.next();
                    resultsMap.put("FirstLine", pair.getKey().toString());
                    resultsMap.put("SecondLine",pair.getValue().toString());
                    listItems.add(resultsMap);
                }

                onlineList.setAdapter(adapter);
                onlineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView otherTV = (TextView) view.findViewById(R.id.FirstLine);
                        String other = otherTV.getText().toString();
                        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                        intent.putExtra("ip", HOST);
                        intent.putExtra("other", other);
                        intent.putExtra("username",username);
                        startActivity(intent);
                    }
                });
            }
            catch (Exception e)
            {
                Log.e("JSON Array Exception", e.toString());
            }

        }
    }
}
