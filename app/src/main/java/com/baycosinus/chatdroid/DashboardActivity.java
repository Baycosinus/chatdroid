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
    public int PORT;
    public String  uid;
    public ListView onlineList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        uid  = getIntent().getStringExtra("userID");
        HOST = getIntent().getStringExtra("HOST");
        PORT = getIntent().getIntExtra("PORT", PORT);

        Log.e("Log", uid);
        Button refreshButton = findViewById(R.id.refreshButton);
        Button logoutButton = findViewById(R.id.logoutButton);
        onlineList = findViewById(R.id.onlineList);
        final Command c = new Command(getApplicationContext(),HOST, PORT);
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
