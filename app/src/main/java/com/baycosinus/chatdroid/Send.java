package com.baycosinus.chatdroid;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;



public class Send extends AsyncTask<JSONObject, Void, Boolean>
{
    private String HOST;
    private String PORT;

    public interface AsyncResponse {
        void processFinish(String output);
    }
    Send(String HOST, String PORT)
    {
        this.HOST = HOST;
        this.PORT = PORT;
    }

    @Override
    protected Boolean doInBackground(JSONObject... jsonObjects) {
        try {
            Socket writeSocket = new Socket(HOST, Integer.valueOf(PORT));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(writeSocket.getOutputStream()));
            writer.print(jsonObjects[0]);
            writer.flush();
            writeSocket.close();
            return true;
        }
        catch (Exception e)
        {
            Log.e("Exception",e.toString());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}