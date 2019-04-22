package com.baycosinus.chatdroid;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;



public class NFunc
{
    public boolean Send(String TARGET, int PORT, String message)
    {
        try {
            Socket writeSocket = new Socket(TARGET, PORT);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(writeSocket.getOutputStream()));
            writer.print(message);
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
    public String Receive(int PORT)
    {
        String response = "";

        try
        {
            ServerSocket serverSocket = new ServerSocket(PORT);
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
}