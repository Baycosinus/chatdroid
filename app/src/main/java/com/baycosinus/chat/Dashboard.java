package com.baycosinus.chat;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        int id = Integer.valueOf(getIntent().getStringExtra("uid"));
        Toast.makeText(getApplicationContext(),"UID:" + String.valueOf(id), Toast.LENGTH_LONG).show();


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
                new Dashboard.Listen(PORT).execute();
            }
        }
    }

    class Listen extends AsyncTask<Void, Void, JSONObject>
    {
        private int PORT;
        Listen(int PORT)
        {
            this.PORT = PORT;
        }
        @Override
        protected JSONObject doInBackground(Void... voids) {
            String responseString = "";
            JSONObject response = new JSONObject();
            try
            {
                ServerSocket serverSocket = new ServerSocket(Integer.valueOf(PORT));
                Socket socket = serverSocket.accept();
                InputStream input = socket.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                responseString = reader.readLine();
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

        }
    }
}
