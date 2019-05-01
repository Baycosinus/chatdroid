package com.baycosinus.chatdroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Ref;
import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity {

    public String HOST;
    public String PORT;
    public int  uid;
    public ListView onlineList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        uid  = getIntent().getIntExtra("userID", uid);
        HOST = getIntent().getStringExtra("HOST");
        PORT = getIntent().getStringExtra("PORT");

        Button refreshButton = findViewById(R.id.refreshButton);
        Button logoutButton = findViewById(R.id.logoutButton);
        onlineList = findViewById(R.id.onlineList);
        final Command c = new Command(HOST, PORT);
        RefreshList();
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.Logout(uid);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshList();
            }
        });
    }

    public void RefreshList()
    {

    }
}
