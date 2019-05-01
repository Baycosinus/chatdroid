package com.baycosinus.chatdroid;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
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
        Bundle b = getIntent().getExtras();
        if (b != null)
        {
            usernameTB.setText(b.getString("username"));
        }
        final TextView passwordTB = findViewById(R.id.pwordTB);
        final TextView hostTB = findViewById(R.id.ipTB);
        final TextView portTB = findViewById(R.id.portTB);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String username = usernameTB.getText().toString();
                String password = passwordTB.getText().toString();
                String HOST = hostTB.getText().toString();
                String PORT = portTB.getText().toString();

                if(username != "" || password != "")
                {
                    new Task(getApplicationContext()).execute(username, password, HOST, PORT);
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
class Task extends AsyncTask<String,Void, Integer>
{
    private Context context;

    public Task(Context context)
    {
        this.context = context;
    }
    @Override
    protected Integer doInBackground(String... strings) {
        Command c = new Command(strings[2], strings[3]);
        int uid = c.Login(strings[0], strings[1]);
        return uid;
    }

    @Override
    protected void onPostExecute(Integer uid)
    {
        if(uid != 0)
        {
            Intent intent = new Intent(context,DashboardActivity.class);
        }
    }
}
