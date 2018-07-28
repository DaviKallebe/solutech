package com.example.bruno.myapplication.commons;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class CheckConnectivity extends AsyncTask<Void, Void, Boolean> {

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            int timeOut = 1500;
            SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);

            Socket socket = new Socket();
            socket.connect(socketAddress, timeOut);
            socket.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
