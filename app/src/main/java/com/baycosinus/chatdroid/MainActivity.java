package com.baycosinus.chatdroid;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);

        final TextView usernameTB = findViewById(R.id.usernameTB);
        final TextView passwordTB = findViewById(R.id.pwordTB);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String username = usernameTB.getText().toString();
                String password = passwordTB.getText().toString();

                if(username != "" || password != "")
                {
                    //Command c = new Command(getApplicationContext(), );
                    //c.Login(username,password);
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(intent);

            }
        });
    }
}
