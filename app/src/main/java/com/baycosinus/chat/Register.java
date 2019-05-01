package com.baycosinus.chat;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class Register extends AppCompatActivity {
    EditText usernameTB, passwordTB, password2TB, hostTB, portTB;
    Button cancelButton, registerButton;

    public String username, password, HOST;
    public int PORT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameTB = findViewById(R.id.usernameTB);
        passwordTB = findViewById(R.id.passwordTB);
        password2TB = findViewById(R.id.password2TB);
        hostTB = findViewById(R.id.hostTB);
        portTB = findViewById(R.id.portTB);
        cancelButton = findViewById(R.id.cancelButton);
        registerButton = findViewById(R.id.registerButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameTB.getText().toString();
                password = passwordTB.getText().toString();
                String password2 = password2TB.getText().toString();
                HOST = hostTB.getText().toString();
                PORT = Integer.valueOf(portTB.getText().toString());

                if(password.equals(password2))
                {
                    Pack p = new Pack("check_available", new User(username,null,null), new User(null,null,null), null);
                    JSONObject pack = p.Pack2JSON();
                    new Send(HOST,PORT).execute(pack);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Passwords does not match.", Toast.LENGTH_LONG).show();
                }
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
                new Register.Listen(PORT).execute();
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
                ServerSocket serverSocket = new ServerSocket(Integer.valueOf(PORT));
                Socket socket = serverSocket.accept();
                InputStream input = socket.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                response = reader.readLine();
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
        protected void onPostExecute(String s)
        {
            boolean result = Boolean.valueOf(s);
            if(result)
            {
                Pack p = new Pack("register", new User(username, password, null), new User(null,null,null),null);
                JSONObject pack = p.Pack2JSON();
                new Send(HOST,PORT).execute(pack);

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Username is not availableç",Toast.LENGTH_LONG).show();
            }
        }
    }
}
