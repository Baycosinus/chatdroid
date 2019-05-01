package com.baycosinus.chatdroid;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Receive extends AsyncTask<Void, Void, String>
{
    private String HOST;
    private String PORT;

    Receive(String HOST, String PORT)
    {
        this.HOST = HOST;
        this.PORT = PORT;
    }
    public interface AsyncResponse {
        void processFinish(String output);
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
            Log.e("Server response", response);
            reader.close();
            socket.close();
            serverSocket.close();
        }
        catch (Exception e)
        {
            Log.e("Exception",e.toString());
        }

        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
    }
}