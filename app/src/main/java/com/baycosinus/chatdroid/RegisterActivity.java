package com.baycosinus.chatdroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registerButton = findViewById(R.id.registerButton);
        Button cancelButton = findViewById(R.id.registerButton);
        final EditText usernameTB = findViewById(R.id.usernameTB);
        final EditText passwordTB = findViewById(R.id.pwordTB);
        final EditText passwordTB2 = findViewById(R.id.pwordTB2);
        final EditText ipTB = findViewById(R.id.serverTB);
        final EditText portTB = findViewById(R.id.portTB);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameTB.getText().toString();

                String password = passwordTB.getText().toString();
                String password2 = passwordTB2.getText().toString();
                String ip = ipTB.getText().toString();
                int port = Integer.parseInt(portTB.getText().toString());
                Command c = new Command(getApplicationContext(), ip, port);
                if(!password.equals(password2))
                {
                    Toast.makeText(getApplicationContext(),"Passwords does not match.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    c.UsernameAvailable(username, password);
                }
                //READ DATA
            }
        });

    }
}
