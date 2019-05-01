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
        Button cancelButton = findViewById(R.id.cancelButton);
        final EditText usernameTB = findViewById(R.id.usernameTB);
        final EditText passwordTB = findViewById(R.id.pwordTB);
        final EditText passwordTB2 = findViewById(R.id.pwordTB2);
        final EditText ipTB = findViewById(R.id.serverTB);
        final EditText portTB = findViewById(R.id.portTB);
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
                String username = usernameTB.getText().toString();
                String password = passwordTB.getText().toString();
                String password2 = passwordTB2.getText().toString();
                String ip = ipTB.getText().toString();
                String port = portTB.getText().toString();
                Command c = new Command(ip, port);
                if(!password.equals(password2))
                {
                    Toast.makeText(getApplicationContext(),"Passwords does not match.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(c.UsernameAvailable(username))
                    {
                        c.Register(username,password);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Username is not available.", Toast.LENGTH_LONG).show();
                    }
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
                //READ DATA
            }
        });

    }
}
