package com.baycosinus.chatdroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

public class ChatActivity extends AppCompatActivity {

    String ip = "";
    int me = 0;
    int other = 0;
    int PORT = 8888;

    ListView chat;
    List<String[]> msgList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ip = getIntent().getStringExtra("ip");
        me = Integer.valueOf(getIntent().getStringExtra("me"));
        final EditText msgTB = findViewById(R.id.msgTB);
        Button sendButton = findViewById(R.id.sendButton);
        chat = findViewById(R.id.chatList);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = msgTB.getText().toString();
                Pack p = new Pack("message", new User(me,null,null, null), new User(other,null,null,null), message );
                JSONObject pack = p.Pack2JSON();
                msgTB.setText("");
                String[] msg = new String[]{String.valueOf(me), message};
                msgList.add(msg);
                new Send(ip, PORT).execute(pack);
                RefreshChat();
                Log.e("Size", String.valueOf(msgList.size()));
            }
        });

        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Listen(PORT).execute();
            }
        });

    }
    void RefreshChat()
    {
        try
        {
            chat.setAdapter(null);
            HashMap<String,String> msgs = new HashMap<>();
            for (int k = 0; k < msgList.size(); k++)
            {
                msgs.put(msgList.get(k)[0], msgList.get(k)[1]);
            }
            List<HashMap<String,String>> listItems = new ArrayList<>();

            SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),listItems, R.layout.message_bubble, new String[]{"MessageText"}, new int[]{R.id.msgText});

            Iterator it = msgs.entrySet().iterator();
            while(it.hasNext())
            {
                HashMap<String,String> resultsMap = new HashMap<>();
                Map.Entry pair = (Map.Entry)it.next();
                resultsMap.put("MessageText", pair.getValue().toString());
                listItems.add(resultsMap);
            }

            chat.setAdapter(adapter);
        }
        catch (Exception e)
        {
            Log.e("Exception", e.getLocalizedMessage());
        }
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
                new Listen(PORT).execute();
            }
        }
    }

    class Listen extends AsyncTask<Void, Void, String>
    {
        private int PORT;
        Listen(int PORT)
        {
            this.PORT = PORT;
        }
        @Override
        protected String doInBackground(Void... voids) {
            String response = "";

            try
            {
                Log.e("test", "test");
                ServerSocket serverSocket = new ServerSocket(Integer.valueOf(PORT));
                serverSocket.setReuseAddress(true);
                Socket socket = serverSocket.accept();
                InputStream input = socket.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                response = reader.readLine();
                String[] value = new String[]{String.valueOf(other), response};
                msgList.add(value);
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
        protected void onPostExecute(String s) {
           RefreshChat();
    }
}
}

