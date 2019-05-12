package com.baycosinus.chatdroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    EditText usernameTB, passwordTB, hostTB, portTB;
    Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        String username = getIntent().getStringExtra("username");

        usernameTB = findViewById(R.id.usernameTB);
        passwordTB = findViewById(R.id.passwordTB);
        hostTB = findViewById(R.id.ipTB);
        portTB = findViewById(R.id.portTB);

        if (username != null && !username.isEmpty())
        {
            usernameTB.setText(username);
        }

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String username = usernameTB.getText().toString();
                String password = passwordTB.getText().toString();
                String HOST = hostTB.getText().toString();
                int PORT = Integer.valueOf(portTB.getText().toString());

                Pack p = new Pack("login",new User(0,username, password, null), new User(0,null,null,null), null);
                JSONObject pack = p.Pack2JSON();
                new Send(HOST,PORT).execute(pack);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
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
                Log.e("Result", response);
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
            int result = Integer.valueOf(s);
            if(result != 0)
            {
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                intent.putExtra("uid", s);
                intent.putExtra("username",usernameTB.getText().toString());
                intent.putExtra("HOST",hostTB.getText().toString());
                intent.putExtra("PORT", portTB.getText().toString());
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Login failed.", Toast.LENGTH_LONG).show();
            }

        }
    }
}
