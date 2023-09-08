package com.amazing.android.autopompomme.linking;

import android.bluetooth.BluetoothSocket;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedThread extends Thread{

    private final BluetoothSocket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public ConnectedThread(BluetoothSocket socket) {
        this.socket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
        }

        inputStream = tmpIn;
        outputStream = tmpOut;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()
        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                bytes = inputStream.available();
                if (bytes != 0) {
                    buffer = new byte[1024];
                    SystemClock.sleep(100); //pause and wait for rest of data. Adjust this depending on your sending speed.
                    bytes = inputStream.available(); // how many bytes are ready to be read?
                    bytes = inputStream.read(buffer, 0, bytes); // record how many bytes we actually read
                }
            } catch (IOException e) {
                e.printStackTrace();

                break;
            }
        }
    }

    public void write(String name,String pw) {
        Log.d("TEST","n"+name);
        byte[] nameBytes = name.getBytes();
        byte[] pwBytes = pw.getBytes();
        Log.d("TEST","b"+nameBytes);
        try {
            outputStream.write(nameBytes);
        } catch (IOException e) {
        }
    }
}
