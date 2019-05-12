package com.baycosinus.chatdroid;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class ChatActivity extends AppCompatActivity {

    String ip = "";
    String me = "";
    String other = "";
    int PORT = 8888;

    LinearLayout chatContainer;
    List<String[]> msgList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ip = getIntent().getStringExtra("ip");
        me = getIntent().getStringExtra("username");
        other = getIntent().getStringExtra("other");

        Toast.makeText(getApplicationContext(), ip + "," + me + "," + other, Toast.LENGTH_LONG).show();
        final EditText msgTB = findViewById(R.id.msgTB);
        Button sendButton = findViewById(R.id.sendButton);
        chatContainer = findViewById(R.id.messageContainer);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = msgTB.getText().toString();
                if(!msgTB.getText().equals(null) || !msgTB.getText().equals(""))
                {
                    Pack p = new Pack("send_message", new User(0,me,null, null), new User(0,other,null,null), message );
                    JSONObject pack = p.Pack2JSON();
                    msgTB.setText("");
                    String[] msg = new String[]{me, message};
                    msgList.add(msg);
                    new Send(ip, PORT).execute(pack);
                    RefreshChat();
                    Log.e("Size", String.valueOf(msgList.size()));
                }
            }
        });

        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pack p = new Pack("receive_message", new User(0,me,null, null), new User(0,other,null,null), null );
                JSONObject pack = p.Pack2JSON();
                new Send(ip,PORT).execute(pack);
            }
        });
        refreshButton.performClick();
        RefreshChat();
    }
    void RefreshChat()
    {
        chatContainer.removeAllViews();
        for(String[] s: msgList)
        {
            LayoutInflater li = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View message = li.inflate(R.layout.message_bubble, null);
            TextView messageText = message.getRootView().findViewById(R.id.msgText);
            messageText.setText(s[1]);
            LinearLayout msgBubble = message.findViewById(R.id.bubbleLayout);
            if(s[0].equals(String.valueOf(me)))
            {
                msgBubble.setGravity(Gravity.RIGHT);
                messageText.setBackgroundColor(getResources().getColor(R.color.outgoingMessage));
            }
            if(s[0].equals(String.valueOf(other)))
            {
                msgBubble.setGravity(Gravity.LEFT);
                messageText.setBackgroundColor(getResources().getColor(R.color.incomingMessage));
            }
            if(chatContainer.getChildCount() != msgList.size())
            {
                chatContainer.addView(message);
            }

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
                new Listen(HOST,PORT).execute();
            }
        }
    }

    class Listen extends AsyncTask<Void, Void, String>
    {
        private String HOST;
        private int PORT;
        Listen(String HOST,int PORT)
        {
            this.HOST = HOST;
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
                Log.e("MSG",response);
                if(!response.equals("[]")) {
                    msgList.clear();
                    response = response.replace("[","");
                    response = response.replace("]","");
                    response = response.replace(")","");
                    response = response.replace("(","");
                    Log.e("response",response);
                    String[] values = response.split(",");
                    for(int i = 0; i < values.length; i+=4)
                    {
                        String user = values[i+1].replace("'","");
                        user = user.replace(" ","");
                        String msg = values[i+3].replace("'","");
                        Log.e(">", "From:" + user + ", " + "Message:" + msg);
                        String[] message = new String[]{user,msg};
                        msgList.add(message);

                    }
                }
                else{}
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

